package org.genos.gmf.form;

import org.genos.utils.Links;

/**
 * Form button representation.
 */
public class Button {

    public static final String BUTTON_SUBMIT = "submit";

    public static final String BUTTON_CANCEL = "cancel";

    public static final String BUTTON_RESET = "reset";

    public static final String BUTTON_BUTTON = "button";

    public static final String BUTTON_IMAGE = "image";

    /**
     * Button type. It must be one of the BUTTON_* constants.
     */
    private String type;

    /**
     * Button caption
     */
    private String caption;

    /**
     * For submit buttons, target of the form.
     */
    private String action = null;

    /**
     * For submit to reload
     */
    private String actionToReload = null;

    /**
     * For submit or image buttons, javascript code to be executed when clicked
     */
    private String onclick = null;

    /**
     * For image buttons, source of the image
     */
    private String image = null;

    /**
     * Stylesheet class that will be applied to the button.
     * If it's not specified, default classes will be used.
     */
    private String cssClass = null;

    /**
     * Constructor
     * @param type      Button type.
     * @param caption   Caption.
     */
    public Button(String _type, String _caption, String _action) throws Exception {
        if (!_type.equals(BUTTON_SUBMIT) && !_type.equals(BUTTON_CANCEL) && !_type.equals(BUTTON_RESET) && !_type.equals(BUTTON_BUTTON)) throw new Exception("Button(): unknown button type.");
        type = _type;
        caption = _caption;
        if (_action != null) {
            action = Links.buildUrl(_action + "&usercommit=1");
            actionToReload = Links.buildUrl(_action);
        }
    }

    /**
     * Constructor
     * @param type      Button type.
     * @param caption   Caption.
     * @param action    Form action (if button is a submit).
     * @param image     Image file.
     */
    public Button(String _type, String _caption, String _action, String _image) throws Exception {
        if (!_type.equals(BUTTON_IMAGE)) throw new Exception("Button(): invalid button type: " + _type);
        type = _type;
        caption = _caption;
        if (_action != null) {
            action = Links.buildUrl(_action + "&usercommit=1");
            actionToReload = Links.buildUrl(_action);
        }
        image = _image;
    }

    /**
     * Sets the onClick property.
     * @param _onclick   Javascript code to be executed when the button is clickec.
     */
    public void setOnClick(String _onclick) {
        onclick = _onclick;
    }

    /**
     * Sets the stylesheet class that will be applied to this button.
     * @param _css  Stylesheet class.
     */
    public void setCssClass(String _css) {
        cssClass = _css;
    }

    /**
     * Gets button type.
     * @return  Button type string.
     */
    public String getType() {
        return type;
    }

    /**
     * Gets button caption.
     * @return  Button caption string.
     */
    public String getCaption() {
        return caption;
    }

    /**
     * Gets button action.
     * @return  Action.
     */
    public String getAction() {
        return action;
    }

    /**
     * Gets button action to Reload
     * @return  Action.
     */
    public String getActionToReload() {
        return actionToReload;
    }

    /**
     * Gets the onClick javascript code.
     * @return  onClick code.
     */
    public String getOnClick() {
        return onclick;
    }

    /**
     * Gets the source of the image for image buttons.
     * @return  Image source.
     */
    public String getImage() {
        return image;
    }

    /**
     * Gets the stylesheet to be applied to this button.
     * @return  Button style
     */
    public String getCssClass() {
        return cssClass;
    }
}
