package net.sf.daro.core;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.Resource;
import javax.swing.Action;
import net.sf.daro.core.concurrent.ExecutorService;
import net.sf.daro.core.page.PageContext;
import net.sf.daro.core.page.PageManager;
import net.sf.daro.core.page.PageRegistry;
import net.sf.daro.swing.ActionMethod;
import net.sf.daro.swing.ActionMethodAction;
import net.sf.daro.swing.SwingUtilitiesExt;
import org.apache.commons.lang.StringUtils;

/**
 * @author schoemer
 * 
 */
public class AbstractApplication implements Application {

    @Resource
    private PageRegistry pageRegistry;

    @Resource
    private PageManager pageManager;

    @Resource
    private PageContext pageContext;

    @Resource
    private ExecutorService executorService;

    private ResourceBundle bundle;

    private final Map<String, Action> actions = new HashMap<String, Action>();

    public AbstractApplication() {
        initActions();
    }

    private void initActions() {
        for (final Method method : getClass().getDeclaredMethods()) {
            final ActionMethod annotation = method.getAnnotation(ActionMethod.class);
            if (annotation != null) {
                String actionCommand = StringUtils.defaultIfEmpty(annotation.command(), method.getName());
                boolean actionEnabled = annotation.enabled();
                Action action = new ActionMethodAction(this, method, getExecutorService());
                action.putValue(Action.ACTION_COMMAND_KEY, actionCommand);
                action.setEnabled(actionEnabled);
                SwingUtilitiesExt.configure(action, getResourceBundle(), actionCommand);
                setAction(actionCommand, action);
            }
        }
    }

    protected void setAction(final String actionCommand, final Action action) {
        this.actions.put(actionCommand, action);
    }

    protected Action getAction(final String actionCommand) {
        return this.actions.get(actionCommand);
    }

    protected ResourceBundle getResourceBundle() {
        if (this.bundle == null) {
            this.bundle = createResourceBundle();
        }
        return this.bundle;
    }

    private ResourceBundle createResourceBundle() {
        return ResourceBundle.getBundle(getClass().getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ExecutorService getExecutorService() {
        return this.executorService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageRegistry getPageRegistry() {
        return this.pageRegistry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PageContext getPageContext() {
        return this.pageContext;
    }

    /**
     * Returns the pageManager of the application.
     * 
     * @return the pageManager
     */
    protected PageManager getPageManager() {
        return this.pageManager;
    }
}
