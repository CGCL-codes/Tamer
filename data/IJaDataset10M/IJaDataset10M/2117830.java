package net.sf.echopm.panel.editor;

import java.util.Locale;
import net.sf.echopm.ResourceBundleHelper;
import net.sf.echopm.EchoPMApp;
import net.sf.echopm.navigation.event.EventDispatcher;
import nextapp.echo2.app.Column;

/**
 * @author ron
 */
public abstract class AbstractComponent extends Column implements EditMode {

    static final long serialVersionUID = 0L;

    /**
	 * The default style from the stylesheet.
	 */
    public static final String STYLE_NAME_DEFAULT = "Default";

    /**
	 * The style name to use for plain controls.
	 */
    public static final String STYLE_NAME_PLAIN = "Plain";

    private ResourceBundleHelper resourceBundleHelper;

    private final EditMode editMode;

    protected AbstractComponent(EditMode editMode) {
        this.editMode = editMode;
    }

    protected AbstractComponent(EditMode editMode, ResourceBundleHelper resourceBundleHelper) {
        this(editMode);
        setResourceBundleHelper(resourceBundleHelper);
    }

    protected ResourceBundleHelper getResourceBundleHelper(Locale locale) {
        resourceBundleHelper.setLocale(locale);
        return resourceBundleHelper;
    }

    protected void setResourceBundleHelper(ResourceBundleHelper resourceBundleHelper) {
        this.resourceBundleHelper = resourceBundleHelper;
    }

    protected EditMode getEditMode() {
        return editMode;
    }

    /**
	 * This method should be overloaded to check permissions of the user making
	 * the edits and return true if the user only has read permissions.<br>
	 * use getApp().getUser() to get the user.
	 * 
	 * @return
	 */
    public boolean isReadOnlyMode() {
        return editMode.isReadOnlyMode();
    }

    public boolean isStateEdited() {
        return editMode.isStateEdited();
    }

    public void setStateEdited(boolean stateEdited) {
        editMode.setStateEdited(stateEdited);
    }

    /**
	 * @return
	 */
    public EventDispatcher getEventDispatcher() {
        return getApp().getEventDispatcher();
    }

    /**
	 * @return
	 */
    public EchoPMApp getApp() {
        return EchoPMApp.getApp();
    }
}
