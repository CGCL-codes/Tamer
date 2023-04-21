package net.sf.daro.core.page;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import net.sf.daro.internal.core.CorePlugin;
import net.sf.daro.swing.ActionMethod;
import net.sf.daro.swing.ActionMethodAction;
import net.sf.daro.swing.SwingUtilitiesExt;
import org.apache.commons.lang.StringUtils;

/**
 * A base implementation of a page. The implementation provides handling of user
 * interface component, resource bundle and unique name.
 * 
 * @author daniel
 */
public abstract class AbstractPage implements Page {

    /**
	 * name
	 */
    private String name;

    /**
	 * icon
	 */
    private Icon icon;

    /**
	 * component
	 */
    private JComponent component;

    /**
	 * resource bundle
	 */
    private ResourceBundle resourceBundle;

    /**
	 * actions
	 */
    private Map<String, Action> actions = new HashMap<String, Action>();

    /**
	 * context
	 */
    private PageContext context;

    /**
	 * Creates a new AbstractPage with the class name as name.
	 */
    public AbstractPage() {
        this.name = getClass().getSimpleName();
    }

    /**
	 * Creates a new AbstractPage with the given name.
	 * 
	 * @param name
	 *            the name
	 */
    public AbstractPage(String name) {
        this(name, null);
    }

    /**
	 * Creates a new AbstractPage with the given name and resource bundle.
	 * 
	 * @param name
	 *            the name
	 * @param resourceBundle
	 *            the resource bundle
	 */
    public AbstractPage(String name, ResourceBundle resourceBundle) {
        this.name = name;
        this.resourceBundle = resourceBundle;
    }

    /**
	 * Returns the action with the given name.
	 * 
	 * @param name
	 *            the name of the action
	 * @return the action or <code>null</code> if no action is found
	 */
    protected Action getAction(String name) {
        return actions.get(name);
    }

    /**
	 * Sets the action with the given name. If the action parameter is
	 * <code>null</code> the action under the given name is removed.
	 * 
	 * @param name
	 *            the name of the action
	 * @param action
	 *            the action or <code>null</code>
	 */
    protected void setAction(String name, Action action) {
        if (action == null) {
            actions.remove(name);
        } else {
            actions.put(name, action);
        }
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see Page#getName()
	 */
    @Override
    public String getName() {
        return name;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * Overridden to retrieve the title from the resource bundle using the
	 * simple class name and &quot;.title&quot; as key. If the text is not found
	 * the key is returned.
	 * 
	 * @see Page#getTitle()
	 */
    @Override
    public String getTitle() {
        String title = null;
        try {
            title = getResourceBundle().getString(getClass().getSimpleName() + ".title");
        } catch (MissingResourceException e) {
            title = getClass().getSimpleName() + ".title";
        }
        return title;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * Overridden to retrieve the description from the resource bundle using the
	 * simple class name and &quot;.description&quot; as key. If the text is not
	 * found the key is returned.
	 * 
	 * @see Page#getDescription()
	 */
    @Override
    public String getDescription() {
        String description = null;
        try {
            description = getResourceBundle().getString(getClass().getSimpleName() + ".description");
        } catch (MissingResourceException e) {
            description = getClass().getSimpleName() + ".description";
        }
        return description;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see Page#getIcon()
	 */
    @Override
    public Icon getIcon() {
        if (icon != null) {
            return icon;
        }
        try {
            String iconName = getResourceBundle().getString(getClass().getSimpleName() + ".icon");
            URL iconUrl = getClass().getResource(iconName);
            if (iconUrl != null) {
                icon = new ImageIcon(iconUrl);
            }
        } catch (MissingResourceException e) {
            icon = null;
        }
        return icon;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see Page#getComponent()
	 */
    @Override
    public JComponent getComponent() {
        if (component == null) {
            component = createComponent();
        }
        return component;
    }

    /**
	 * Creates and returns a new component.
	 * 
	 * @return the created component
	 */
    protected abstract JComponent createComponent();

    /**
	 * Returns the resource bundle of the page.
	 * 
	 * @return the resource bundle
	 */
    public ResourceBundle getResourceBundle() {
        if (resourceBundle == null) {
            resourceBundle = createResourceBundle();
        }
        return resourceBundle;
    }

    /**
	 * Creates and returns a new resource bundle.
	 * 
	 * The default implementation uses the full-qualified class name and
	 * &quot;Resources&quot; as name for the resource bundle file.
	 * 
	 * @return the created resource bundle
	 * @throws MissingResourcesException
	 *             if the resource bundle file doesn't exist
	 */
    protected ResourceBundle createResourceBundle() {
        return ResourceBundle.getBundle(getClass().getName(), Locale.getDefault(), getClass().getClassLoader());
    }

    /**
	 * {@inheritDoc}
	 * 
	 * Overridden to initialize the actions from annotations.
	 * 
	 * @see Page#init()
	 */
    @Override
    public void init() throws PageException {
        for (Method method : getClass().getDeclaredMethods()) {
            ActionMethod annotation = method.getAnnotation(ActionMethod.class);
            if (annotation != null) {
                String actionCommand = StringUtils.defaultIfEmpty(annotation.command(), method.getName());
                boolean actionEnabled = annotation.enabled();
                Action action = new ActionMethodAction(this, method, CorePlugin.getInstance().getExecutorService());
                action.putValue(Action.ACTION_COMMAND_KEY, actionCommand);
                action.setEnabled(actionEnabled);
                SwingUtilitiesExt.configure(action, getResourceBundle(), actionCommand);
                setAction(actionCommand, action);
            }
        }
    }

    /**
	 * {@inheritDoc}
	 * 
	 * @see Page#dispose()
	 */
    @Override
    public void dispose() throws PageException {
    }

    /**
	 * Returns the context of the shown page.
	 * 
	 * @return the context
	 */
    public PageContext getContext() {
        return context;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * Overridden to store the page context. Subclasses must invoke
	 * super.showCallback(PageContext) to store the context while the page is
	 * shown.
	 * 
	 * @see Page#showCallback(PageContext)
	 */
    @Override
    public void showCallback(PageContext container) throws PageException {
        this.context = container;
    }

    /**
	 * {@inheritDoc}
	 * 
	 * Overridden to release the page context. Subclasses must invoke
	 * super.hideCallback(PageContext) to release the context while the page is
	 * hidden.
	 * 
	 * @see Page#hideCallback(PageContext)
	 */
    @Override
    public void hideCallback(PageContext container) throws PageException {
        this.context = null;
    }
}
