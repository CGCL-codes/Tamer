package com.google.gwt.user.client.ui;

/**
 * 
 * @author georgopoulos.georgios(at)gmail.com
 */
public abstract class AbstractDecoratedPopupPanel extends DecoratedPopupPanel {

    /**
	 * The type of animation to use when opening the popup.
	 * 
	 * <ul>
	 * <li>CENTER - Expand from the center of the popup</li>
	 * <li>ONE_WAY_CORNER - Expand from the top left corner, do not animate hiding</li>
	 * <li>ROLL_DOWN - ?</li>
	 * </ul>
	 */
    public static enum AnimationType {

        /**
     * 
     */
        CENTER(PopupPanel.AnimationType.CENTER), /**
     * 
     */
        ONE_WAY_CORNER(PopupPanel.AnimationType.ONE_WAY_CORNER), /**
     * 
     */
        ROLL_DOWN(PopupPanel.AnimationType.ROLL_DOWN);

        private PopupPanel.AnimationType type;

        AnimationType(final PopupPanel.AnimationType type) {
            this.type = type;
        }

        /**
		 * @return a PopupPanel AnimationType
		 */
        public PopupPanel.AnimationType getType() {
            return this.type;
        }
    }

    /**
	 * Creates an empty decorated popup panel using the specified style names.
	 * 
	 * @param autoHide
	 *            <code>true</code> if the popup should be automatically hidden when the user clicks outside of it
	 * @param modal
	 *            <code>true</code> if keyboard or mouse events that do not target the PopupPanel or its children should be ignored
	 * @param prefix
	 *            the prefix applied to child style names
	 * @param type
	 *            the type of animation to use
	 */
    protected AbstractDecoratedPopupPanel(final boolean autoHide, final boolean modal, final String prefix, final AnimationType type) {
        super(autoHide, modal, prefix);
        this.setAnimationType(type.getType());
    }
}
