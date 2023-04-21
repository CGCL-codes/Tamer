package siouxsie.mvc.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import siouxsie.mvc.IScreen;
import siouxsie.mvc.IValueStackAware;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.config.Configuration;
import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.providers.XWorkConfigurationProvider;
import com.opensymphony.xwork2.util.ValueStack;
import com.opensymphony.xwork2.util.ValueStackFactory;

/**
 * @author Arnaud Cogoluegnes
 * @version $Id: ScreenCreator.java 143 2008-06-03 20:37:37Z acogo $
 */
public class ScreenCreator {

    private static final Log LOG = LogFactory.getLog(ScreenCreator.class);

    public IScreen create(LaunchConfiguration config) {
        ConfigurationManager confManager = new ConfigurationManager();
        confManager.addContainerProvider(new XWorkConfigurationProvider());
        confManager.addContainerProvider(new TestContainerProvider());
        Configuration conf = confManager.getConfiguration();
        ValueStack vs = conf.getContainer().getInstance(ValueStackFactory.class).createValueStack();
        ActionContext context = new ActionContext(vs.getContext());
        ActionContext.setContext(context);
        vs.push(config.getContext());
        IScreen screen = null;
        try {
            screen = config.getScreenClass().newInstance();
        } catch (InstantiationException e) {
            LOG.error("could not create screen", e);
        } catch (IllegalAccessException e) {
            LOG.error("could not create screen", e);
        }
        if (screen instanceof IValueStackAware) {
            ((IValueStackAware) screen).setValueStack(vs);
        }
        conf.getContainer().inject(screen);
        screen.buildGUI();
        screen.initGUI();
        return screen;
    }
}
