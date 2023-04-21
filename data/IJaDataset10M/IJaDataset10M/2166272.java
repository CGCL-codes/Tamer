package be.smd.i18n.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

/**
 * A localizable box component with composed of the following elements:
 * 
 * <ol>
 * 	<li>A header with an optional localized text, an optional icon, and optional controls:</p>
 * 	<li>A panel in which to insert your content</li>
 * </ol>
 * 
 * @author Pierre Jeanmenne
 * @version 10-03-09
 */
public class I18nBox extends JPanel implements I18nComponent {

    /** The Serial Version UID for I18nBox */
    private static final long serialVersionUID = 2620088273656916198L;

    /**
	 * Indicates that the text and the icon of the box should be placed on the left of the box header,<br/>
	 * and consequently that the box controls should be disposed on the right.
	 */
    public static final int LEFT_TO_RIGHT = 1;

    /**
	 * Indicates that the text and the icon of the box should be placed on the right of the box header,<br/>
	 * and consequently that the box controls should be disposed on the left.
	 */
    public static final int RIGHT_TO_LEFT = 2;

    /** The header with the box localized text, icon and controls */
    protected BoxHeader header;

    /** The panel for the content of the box */
    protected JPanel contentPanel;

    /** Indicates if the box is active or not */
    protected boolean active;

    /**
	 * Creates an I18nBox without text, without icon,
	 * and where the controls will be disposed on the right of the box header.
	 */
    public I18nBox() {
        this(null, null, LEFT_TO_RIGHT);
    }

    /**
	 * Creates an I18nBox with a localized text disposed on the left,
	 * and where the controls will be disposed on the right of the box header
	 * 
	 * @param textBundleKey - the key used by the resource manager to find
	 * 						  the text to display on the left of the box header
	 */
    public I18nBox(String textBundleKey) {
        this(textBundleKey, null, LEFT_TO_RIGHT);
    }

    /**
	 * Creates an I18nBox with a localized text disposed according to the specified orientation,
	 * and where the controls will be disposed on the opposite side in the box header.
	 * 
	 * @param textBundleKey - the key used by the resource manager to find
	 * 						  the text to display in the box header
	 * @param orientation	- the orientation of the box header components, which should be
	 * 						  {@link I18nBox#LEFT_TO_RIGHT} or {@link I18nBox#RIGHT_TO_LEFT}
	 */
    public I18nBox(String textBundleKey, int orientation) {
        this(textBundleKey, null, orientation);
    }

    /**
	 * Creates an I18nBox with a localized text and an icon disposed on the left,
	 * and where the controls will be disposed on the right in the box header.
	 * 
	 * @param textBundleKey - the key used by the resource manager to find
	 * 						  the text to display in the box header
	 * @param icon 			- the icon to display in the box header
	 */
    public I18nBox(String textBundleKey, Icon icon) {
        this(textBundleKey, icon, LEFT_TO_RIGHT);
    }

    /**
	 * Creates an I18nBox with a localized text and an icon disposed according
	 * to the specified orientation, and where the controls will be disposed
	 * on the opposite side in the box header.
	 * 
	 * @param textBundleKey - the key used by the resource manager to find
	 * 						  the text to display in the box header
	 * @param icon 			- the box icon
	 * @param orientation	- the orientation of the box toolbar's components, which should be
	 * 						  {@link I18nBox#LEFT_TO_RIGHT} or {@link I18nBox#RIGHT_TO_LEFT}
	 */
    public I18nBox(String textBundleKey, Icon icon, int orientation) {
        createBoxHeader(textBundleKey, icon, orientation);
        createContentPanel();
        layoutComponents();
    }

    /**
	 * Creates the box header component
	 * @param textBundleKey - the key used by the resource manager to find
	 * 						  the text to display in the box header
	 * @param icon 			- the box icon
	 * @param orientation	- The orientation of the box header's components<br/>
	 * 						  <p><code>I18nBox.LEFT_TO_RIGHT</code> if you want to place
	 * 						  the icon and the text for the box on the left.<br/>
	 * 						  <code>I18nBox.RIGHT_TO_LEFT</code> if you want to place
	 * 						  the icon and the text for the box on the right.</p>
	 * @throws RuntimeException - if the value for the orientation isn't
	 * 						 <code> I18nBox.RIGHT_TO_LEFT</code> or <code>I18nBox.RIGHT_TO_LEFT</code>.
	 */
    private void createBoxHeader(String textBundleKey, Icon icon, int orientation) {
        if (orientation != LEFT_TO_RIGHT && orientation != RIGHT_TO_LEFT) throw new IllegalArgumentException("The value '" + orientation + "' is not permitted for the box header orientation.\n," + "use I18nBox.LEFT_TO_RIGHT or I18nBox.RIGHT_TO_LEFT instead");
        header = new BoxHeader(textBundleKey, icon, orientation);
    }

    /** Creates the panel for the box content */
    private void createContentPanel() {
        contentPanel = new JPanel() {

            /** The Serial Version UID for this panel */
            private static final long serialVersionUID = 4487276928515019923L;

            @Override
            protected void paintBorder(Graphics g) {
                g.setColor(Color.gray);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        contentPanel.setBackground(Color.white);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
    }

    /** Layouts the components of the box using a {@link BorderLayout} as layout manager. */
    private void layoutComponents() {
        setBorder(new EmptyBorder(3, 3, 3, 3));
        setLayout(new BorderLayout());
        add(header, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }

    /**
	 * Adds the given action as control to the box header
	 * @param actionToAdd {@link Action} the action to add
	 */
    public void addAction(Action actionToAdd) {
        addButton(new I18nButton(actionToAdd));
    }

    /**
	 * Adds the given button on the tool bar
	 * @param button {@link JButton} the button to add
	 */
    protected void addButton(I18nButton button) {
        button.setTextBundleKey(null);
        button.setPreferredSize(new Dimension(24, 24));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        header.addButton(button);
    }

    /**
	 * Sets the icon to display on the box header when the box is disabled
	 * @param disabledIcon - the box header disabled icon
	 */
    public void setDisabledIcon(Icon disabledIcon) {
        header.boxHeaderLabel.setDisabledIcon(disabledIcon);
    }

    /**
	 * Activates or unactivates the box
	 * @param active <code>true</code> if the box must be activated, <code>false</code> otherwise
	 */
    public void setActive(boolean active) {
        this.active = active;
    }

    /**
	 * Returns the active state of the box.
	 * @return <code>true</code> if the box is active, <code>false</code> otherwise.
	 */
    public boolean isActive() {
        return active;
    }

    /** Resets the box */
    public void reset() {
    }

    /**
     * Gets the key used by the resource manager to find the text to display on the component in a resource bundle
     * @return - the requested bundle key
     */
    public String getTextBundleKey() {
        return header.getToolbarLabel().getTextBundleKey();
    }

    /**
     * Gets the key used by the resource manager to find the tooltip text to display on the component in a resource bundle
     * @return - the requested bundle key
     */
    public String getTooltipTextBundleKey() {
        return header.getToolbarLabel().getTooltipTextBundleKey();
    }

    /**
     * <p>Sets the key that should be used to find the text to display on the component in a resource bundle.</p>
     * @param textBundleKey - the bundle key to set
     */
    public void setTextBundleKey(String textBundleKey) {
        header.getToolbarLabel().setTextBundleKey(textBundleKey);
    }

    /**
	 * <p>Sets the key that should be used to find the tooltip text to display on the component in a resource bundle.</p>
	 * @param tooltipTextBundleKey - the bundle key to set
	 */
    public void setTooltipTextBundleKey(String tooltipTextBundleKey) {
        header.getToolbarLabel().setTooltipTextBundleKey(tooltipTextBundleKey);
    }

    /**
	 * <p>Sets the substitution values for the localized text placeholders.</p>
	 * <p>This method should be used only if the text bundle key points to a message with placeholders</p>
	 * @param textParameters -  an array with substitution values for the text placeholders
	 */
    public void setTextParameters(Object... textParameters) {
        header.getToolbarLabel().setTextParameters(textParameters);
    }

    /**
	 * <p>Sets the substitution values for the localized tooltip text placeholders.</p>
	 * <p>This method should be used only if the tooltip text key points to a message with placeholders</p>
	 * @param tooltipTextParameters -  an array with substitution values for the tooltip text placeholders
	 */
    public void setTooltipTextParameters(Object... tooltipTextParameters) {
        header.getToolbarLabel().setTooltipTextParameters(tooltipTextParameters);
    }

    /** Paints a double rounded border on the panel */
    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(new Color(230, 230, 230));
        g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 8, 8);
        g2d.setPaint(Color.gray);
        g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
    }

    /**
	 * The header component for the I18nBox which contains the box text,
	 * the box icon and the box controls.
	 * 
	 * @author Pierre Jeanmenne
	 * @version 10-03-09
	 */
    protected class BoxHeader extends JPanel {

        /** The Serial Version UID for BoxToolbar */
        private static final long serialVersionUID = -373365403970211986L;

        /** The component which holds the label to display for the tool bar  */
        private I18nLabel boxHeaderLabel;

        /** The panel for the action buttons */
        private JPanel boxHeaderControlsPanel;

        /** Defines how the header's components should be disposed */
        private int orientation;

        /**
		 * Creates the BoxHeader with a localized text, an icon,
		 * and a specified orientation to dispose its components.
		 * 
		 * @param textBundleKey - the key used by the resource manager to find
		 * 						  the text to display on the left of the box toolbar
		 * @param icon 			- the icon to display on the left of the toolbar's text
		 * @param orientation	- The orientation of the box header's components<br/>
		 * 						  <p><code>I18nBox.LEFT_TO_RIGHT</code> if you want to place
		 * 						  the icon and the text for the box on the left.<br/>
		 * 						  <code>I18nBox.RIGHT_TO_LEFT</code> if you want to place
		 * 						  the icon and the text for the box on the right.</p>
		 */
        public BoxHeader(String textBundleKey, Icon icon, int orientation) {
            this.orientation = orientation;
            createBoxLabel(textBundleKey, icon);
            createBoxControlsPanel();
            layoutComponents();
        }

        private void createBoxControlsPanel() {
            boxHeaderControlsPanel = new JPanel();
            boxHeaderControlsPanel.setLayout(orientation == LEFT_TO_RIGHT ? new FlowLayout(FlowLayout.RIGHT, 0, 0) : new FlowLayout(FlowLayout.LEFT, 0, 0));
            boxHeaderControlsPanel.setOpaque(false);
        }

        private void layoutComponents() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(I18nBox.this.getPreferredSize().width, 24));
            if (orientation == LEFT_TO_RIGHT) {
                add(boxHeaderControlsPanel, BorderLayout.EAST);
                add(boxHeaderLabel, BorderLayout.WEST);
            } else {
                add(boxHeaderControlsPanel, BorderLayout.WEST);
                add(boxHeaderLabel, BorderLayout.EAST);
            }
        }

        /**
		 * Initializes the tool bar components
		 * @param textBundleKey {@link String} the label to display on the left of the tool bar
		 * @param icon	{@link Icon} the icon to display on the left of the tool bar's label
		 */
        private void createBoxLabel(String textBundleKey, Icon icon) {
            if (orientation == LEFT_TO_RIGHT) {
                boxHeaderLabel = new I18nLabel(textBundleKey, icon, I18nLabel.LEFT);
                boxHeaderLabel.setBorder(BorderFactory.createEmptyBorder(0, 7, 0, 0));
            } else {
                boxHeaderLabel = new I18nLabel(textBundleKey, icon, I18nLabel.RIGHT);
                boxHeaderLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 7));
                boxHeaderLabel.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            }
        }

        /**
		 * Adds a button to the buttons panel
		 * @param buttonToAdd {@link JButton} the button to add
		 */
        public void addButton(JButton buttonToAdd) {
            boxHeaderControlsPanel.add(buttonToAdd);
        }

        /**
		 * Returns the button for the given index.
		 * Be careful that the buttons are added from right to left.
		 * So, the index 0 will return the button aligned to the right of the tool bar
		 * @param index int the index of the button
		 * @return {@link JButton} the requested button
		 */
        public JButton getActionButton(int index) {
            return (JButton) boxHeaderControlsPanel.getComponent(index);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setPaint(new GradientPaint(0, 0, Color.white, 0, getHeight(), new Color(230, 230, 230)));
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        public I18nLabel getToolbarLabel() {
            return boxHeaderLabel;
        }
    }
}
