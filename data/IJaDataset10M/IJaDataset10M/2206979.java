package macaw.presentationLayer;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellRenderer;
import macaw.MacawMessages;
import macaw.system.Log;
import macaw.system.SessionProperties;
import macaw.system.UserInterfaceFactory;

public class BasketItemRenderer extends JLabel implements TreeCellRenderer {

    /**
     * Is the value currently selected.
     */
    protected boolean selected;

    /**
     * True if has focus.
     */
    protected boolean hasFocus;

    /**
     * True if draws focus border around icon as well.
     */
    protected transient Icon closedIcon;

    protected transient Icon openIcon;

    protected transient Icon leafIcon;

    protected transient ImageIcon basketVariableIcon;

    private Color selectedColour;

    private boolean isSelected;

    private UserInterfaceFactory userInterfaceFactory;

    /**
     * Returns a new instance of DefaultTreeCellRenderer.  Alignment is
     * set to left aligned. Icons and text color are determined from the
     * UIManager.
     */
    public BasketItemRenderer(String name, SessionProperties sessionProperties) {
        super(name);
        init(sessionProperties);
    }

    public BasketItemRenderer(SessionProperties sessionProperties) {
        init(sessionProperties);
    }

    private void init(SessionProperties sessionProperties) {
        this.userInterfaceFactory = sessionProperties.getUserInterfaceFactory();
        userInterfaceFactory.setComponentProperties(this);
        selectedColour = new Color(60, 230, 25);
        setHorizontalAlignment(JLabel.LEFT);
        StringBuilder iconDirectoryFilePath = new StringBuilder();
        iconDirectoryFilePath.append(".");
        iconDirectoryFilePath.append(File.separator);
        iconDirectoryFilePath.append("icons");
        iconDirectoryFilePath.append(File.separator);
    }

    private ImageIcon createIcon(String iconFileName, String directory) throws MalformedURLException {
        URL iconDirectoryURL = MacawMessages.class.getResource(directory);
        StringBuilder iconFileResource = new StringBuilder();
        iconFileResource.append(iconDirectoryURL.toString());
        iconFileResource.append(iconFileName);
        ImageIcon imageIcon = new ImageIcon(iconFileResource.toString());
        return imageIcon;
    }

    /**
     * Returns the default icon, for the current laf, that is used to
     * represent non-leaf nodes that are expanded.
     */
    public Icon getDefaultOpenIcon() {
        return openIcon;
    }

    /**
     * Returns the default icon, for the current laf, that is used to
     * represent non-leaf nodes that are not expanded.
     */
    public Icon getDefaultClosedIcon() {
        return closedIcon;
    }

    /**
     * Returns the default icon, for the current laf, that is used to
     * represent leaf nodes.
     */
    public Icon getDefaultLeafIcon() {
        return leafIcon;
    }

    /**
     * Sets the icon used to represent non-leaf nodes that are expanded.
     */
    public void setOpenIcon(ImageIcon newIcon) {
        openIcon = newIcon;
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are expanded.
     */
    public Icon getOpenIcon() {
        return openIcon;
    }

    /**
     * Sets the icon used to represent non-leaf nodes that are not expanded.
     */
    public void setClosedIcon(ImageIcon newIcon) {
        closedIcon = newIcon;
    }

    /**
     * Returns the icon used to represent non-leaf nodes that are not
     * expanded.
     */
    public Icon getClosedIcon() {
        return closedIcon;
    }

    /**
     * Sets the icon used to represent leaf nodes.
     */
    public void setLeafIcon(ImageIcon newIcon) {
        leafIcon = newIcon;
    }

    /**
     * Returns the icon used to represent leaf nodes.
     */
    public Icon getLeafIcon() {
        return leafIcon;
    }

    public void setFont(Font font) {
        if (font instanceof FontUIResource) font = null;
        super.setFont(font);
    }

    public void setBackground(Color color) {
        if (color instanceof ColorUIResource) color = null;
        super.setBackground(color);
    }

    /**
     * sets selected.  A selected node appears in green.
     *
     * @param isSelected true if the node is selected; otherwise false
     */
    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
        if (value.getClass() == DefaultMutableTreeNode.class) {
            return this;
        }
        BasketNode node = (BasketNode) value;
        if (node.isBasketVariable() == false) {
            if (!tree.isEnabled()) {
                setEnabled(false);
                if (leaf) {
                    setDisabledIcon(getLeafIcon());
                } else if (expanded) {
                    setDisabledIcon(getOpenIcon());
                } else {
                    setDisabledIcon(getClosedIcon());
                }
            } else {
                setEnabled(true);
                if (leaf) {
                    setIcon(getLeafIcon());
                } else if (expanded) {
                    setIcon(getOpenIcon());
                } else {
                    setIcon(getClosedIcon());
                }
            }
        } else {
            setIcon(basketVariableIcon);
        }
        this.hasFocus = hasFocus;
        setText(node.toString());
        setComponentOrientation(tree.getComponentOrientation());
        setSelected(selected);
        return this;
    }

    private void drawNormalBorder(Graphics graphics) {
        Graphics2D graphics2D = (Graphics2D) graphics;
        int imageOffset = getLabelStart();
        graphics2D.setColor(Color.black);
        drawRectangle(graphics2D, imageOffset);
    }

    private void drawRectangle(Graphics graphics, int imageOffset) {
        graphics.drawRect(0 + imageOffset, 0, getWidth() - 1 - imageOffset, getHeight() - 1);
    }

    public void determineTextAppearance() {
        Font font = getFont();
        Font plainFont = font.deriveFont(Font.PLAIN);
        Font italicFont = font.deriveFont(Font.ITALIC);
        setForeground(Color.black);
        setFont(plainFont);
    }

    private void fillButton(Graphics graphics, Color colour) {
        int imageOffset = getLabelStart();
        graphics.setColor(colour);
        graphics.fillRect(0 + imageOffset, 0, getWidth() - 1 - imageOffset, getHeight());
    }

    private void showSelected(Graphics graphics) {
        fillButton(graphics, selectedColour);
        drawNormalBorder(graphics);
        determineTextAppearance();
        super.paint(graphics);
    }

    private void showNormalState(Graphics graphics) {
        determineTextAppearance();
        super.paint(graphics);
    }

    /**
     * Paints the value.  The background is filled based on selected.
     */
    public void paint(Graphics graphics) {
        if (isSelected == true) {
            showSelected(graphics);
        } else {
            showNormalState(graphics);
        }
    }

    private int getLabelStart() {
        Icon currentI = getIcon();
        if (currentI != null && getText() != null) {
            return currentI.getIconWidth() + Math.max(0, getIconTextGap() - 1);
        }
        return 0;
    }

    public Dimension getPreferredSize() {
        Dimension retDimension = super.getPreferredSize();
        if (retDimension != null) retDimension = new Dimension(retDimension.width + 3, retDimension.height);
        return retDimension;
    }

    public void validate() {
    }

    public void revalidate() {
    }

    public void repaint(long tm, int x, int y, int width, int height) {
    }

    public void repaint(Rectangle r) {
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (propertyName == "text") super.firePropertyChange(propertyName, oldValue, newValue);
    }

    public void firePropertyChange(String propertyName, byte oldValue, byte newValue) {
    }

    public void firePropertyChange(String propertyName, char oldValue, char newValue) {
    }

    public void firePropertyChange(String propertyName, short oldValue, short newValue) {
    }

    public void firePropertyChange(String propertyName, int oldValue, int newValue) {
    }

    public void firePropertyChange(String propertyName, long oldValue, long newValue) {
    }

    public void firePropertyChange(String propertyName, float oldValue, float newValue) {
    }

    public void firePropertyChange(String propertyName, double oldValue, double newValue) {
    }

    public void firePropertyChange(String propertyName, boolean oldValue, boolean newValue) {
    }
}
