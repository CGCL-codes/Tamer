package org.o14x.migale.renderer;

import java.util.ArrayList;
import org.o14x.migale.Factory;
import org.o14x.migale.RenderContext;
import org.o14x.migale.parser.MigaleComponent;
import org.o14x.migale.script.ScriptEngine;
import org.o14x.utilz.exception.TechException;

/**
 * Renderer for <b>&lt;script/&gt;</b> tag.
 * <br/><br/>
 * Renders : <b>null</b>. The script contained within this tag is executed by this renderer.
 * <br/><br/>
 * Specific attributes : <b>none</b>
 * <br/><br/>
 * This tag encloses a CDATA section corresponding to the source code of the script.
 * 
 * @author Olivier Dangr�aux
 */
public class Script implements Renderer {

    /**
	 * @see Renderer#render(MigaleComponent, Object, RenderContext)
	 */
    public Object render(MigaleComponent migaleComponent, Object renderedObject, RenderContext renderContext) throws TechException {
        if (!migaleComponent.getChildren().isEmpty()) {
            ArrayList children = migaleComponent.getChildren();
            for (int i = 0; i < children.size(); i++) {
                MigaleComponent child = (MigaleComponent) children.get(i);
                if ("script".equals(child.getName())) {
                    renderContext.getRenderKit().render(child, renderContext);
                }
            }
        } else {
            String sourceCode = migaleComponent.getAttribute("sourcecode");
            if (sourceCode != null) {
                ScriptEngine scriptEngine = Factory.getNewScriptEngine(renderContext.getRenderKit());
                scriptEngine.setBinding(renderContext.getScriptBinding());
                scriptEngine.execute(sourceCode);
            }
        }
        return null;
    }
}
