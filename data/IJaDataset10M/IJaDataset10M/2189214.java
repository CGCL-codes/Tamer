package com.tirsen.angkor.jsp;

import com.tirsen.angkor.Application;
import com.tirsen.angkor.Debug;
import com.tirsen.angkor.RenderContext;
import com.tirsen.angkor.View;
import com.tirsen.angkor.ViewFactory;
import com.tirsen.angkor.process.ExecuteContext;
import com.tirsen.angkor.process.Pipeline;
import com.tirsen.angkor.process.RenderValve;
import com.tirsen.angkor.process.Valve;
import org.apache.log4j.Category;
import java.util.Iterator;
import java.util.Map;

/**
 * <!-- $Id: PrerenderValve.java,v 1.3 2002/10/09 21:37:37 tirsen Exp $ -->
 * <!-- $Author: tirsen $ -->
 *
 * @author Jon Tirs&acute;n (tirsen@users.sourceforge.net)
 * @version $Revision: 1.3 $
 */
public class PrerenderValve extends RenderValve {

    private static final Category logger = Debug.getCategory();

    /**
     * The resulting {@link PrerenderedComponentsRegistry} resulting from this valve.
     */
    public static final ExecuteContext.Attribute PrerenderedComponentsRegistryAttribute = new ExecuteContext.Attribute("PrerenderedComponentsRegistry");

    /**
     * Name of the component to be prerendered in the registry.
     */
    private static final ExecuteContext.Attribute RegistryNameAttribute = new ExecuteContext.Attribute("RegistryName");

    public static class DoPrerenderValve implements Valve {

        public void execute(ExecuteContext exec) throws Exception {
            PrerenderedComponentsRegistry registry = (PrerenderedComponentsRegistry) exec.getAttribute(PrerenderedComponentsRegistryAttribute);
            String name = (String) exec.getAttribute(RegistryNameAttribute);
            View view = (View) exec.getAttribute(ExecuteContext.CurrentViewAttribute);
            logger.debug("prerendering view " + name + " = " + view);
            registry.prerender(name, view);
            exec.executeNext();
        }
    }

    protected Pipeline createRenderPipeline() {
        Pipeline pipeline = super.createRenderPipeline();
        pipeline.addValve(new DoPrerenderValve());
        return pipeline;
    }

    public void execute(ExecuteContext exec) throws Exception {
        RenderContext context = (RenderContext) exec.getAttribute(ExecuteContext.RenderContextAttribute);
        Application application = (Application) exec.getAttribute(ExecuteContext.ApplicationAttribute);
        PrerenderedComponentsRegistry registry = new PrerenderedComponentsRegistry(context);
        exec.setAttribute(PrerenderedComponentsRegistryAttribute, registry);
        Map components = application.getComponentsToPrerender();
        Pipeline subPipeline = createRenderPipeline();
        for (Iterator iterator = components.entrySet().iterator(); iterator.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iterator.next();
            ExecuteContext subExec = subPipeline.createSubContext(exec);
            String name = (String) entry.getKey();
            subExec.setAttribute(RegistryNameAttribute, name);
            if (entry.getValue() instanceof View) {
                subExec.setAttribute(ExecuteContext.CurrentViewAttribute, entry.getValue());
            } else if (entry.getValue() instanceof ViewFactory) {
                subExec.setAttribute(ViewFactoryAttribute, entry.getValue());
            }
            subPipeline.execute(subExec);
        }
        exec.executeNext();
    }
}
