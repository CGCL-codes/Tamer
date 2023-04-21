package oext.gui;

import java.awt.*;
import javax.swing.*;
import oext.model.NodeModel;
import oext.model.rules.PcDataEquality;
import oext.model.rules.ThreshholdAttributeEquality;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * The Node Panel is used as TableCellRenderer in the two tables on the main Screen
 * It is used to represent the informations of a NodeModel as text in one table cell(line) 
 *  
 * @since 18:07:05 06.05.2007
 * @version 1.0
 * @author oliver
 */
public class NodePanel extends JPanel {

    /**
   * 
   */
    private static final long serialVersionUID = -1776193009448474664L;

    private NodeModel model = null;

    private boolean selected = false;

    private boolean rigthSide = false;

    /**
   * Constructor doing nothing else then calling the construcor of the base
   * class
   */
    public NodePanel() {
        super();
    }

    /**
   * Capsulates all necessary settings for simply drawing a string 
   * 
   * @param str The string to draw
   * @param x The x position where to draw the string
   * @param y The y position where to draw the string
   * @param g the graphics context used for drawing the string
   * @return the x position after the string for concating many strings to one line
   */
    protected int drawString(String str, int x, int y, Graphics g) {
        FontMetrics metrics = g.getFontMetrics();
        g.drawBytes(str.getBytes(), 0, str.length(), x, y + metrics.getHeight() - metrics.getDescent());
        return x + metrics.bytesWidth(str.getBytes(), 0, str.length());
    }

    /**
   * overwritten paint method 
   */
    public void paint(Graphics g) {
        Rectangle bounds = getBounds();
        int x = model.getLevel() * 15;
        int i = 0;
        int length = 0;
        float equality = 1.0f;
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, x - 1, bounds.height);
        if (rigthSide) {
            equality = 0.0f;
            if ((model.getNode() != null) && (model.getLinkedModel() != null) && (model.getLinkedModel().getNode() != null)) {
                ThreshholdAttributeEquality rule = new ThreshholdAttributeEquality();
                PcDataEquality rule2 = new PcDataEquality();
                rule.setThreshhold(0.1f);
                equality = rule.equality(model, model.getLinkedModel());
                equality += rule2.equality(model, model.getLinkedModel());
                equality /= 2;
            }
        }
        if (equality < 0.99f) {
            g.setColor(new Color(255, 200, 200));
            g.fillRect(x, 0, bounds.width - x, bounds.height);
        } else {
            g.setColor(new Color(225, 225, 225));
            g.fillRect(x, 0, bounds.width - x, bounds.height);
        }
        if (isSelected()) {
            g.setColor(Color.BLUE);
            g.drawRect(0, 0, bounds.width - 1, bounds.height - 1);
        }
        if (model.getNode() == null) {
        } else {
            g.setColor(Color.BLACK);
            x = drawString(model.getNodeName(), x + 1, 0, g);
            NamedNodeMap map = model.getNode().getAttributes();
            length = map.getLength();
            String pcData = model.getPcData();
            if (null == pcData) {
            } else {
                pcData = pcData.trim();
                if (0 != pcData.length()) {
                    x = drawString(" = \"", x + 1, 0, g);
                    g.setColor(Color.BLUE);
                    if (pcData.length() > 12) {
                        pcData = pcData.substring(0, 10) + "..";
                    }
                    pcData = pcData.replace('\r', '_');
                    pcData = pcData.replace('\n', '_');
                    x = drawString(pcData, x + 1, 0, g);
                    g.setColor(Color.BLACK);
                    x = drawString("\" ", x + 1, 0, g);
                }
            }
            if (length > 0) {
                x = drawString(" (", x + 1, 0, g);
                for (i = 0; i < length; i++) {
                    Node attribute = map.item(i);
                    String name = attribute.getNodeName();
                    String value = attribute.getNodeValue();
                    if ((name != null) && (value != null)) {
                        if (i > 0) {
                            g.setColor(Color.BLACK);
                            x = drawString(", ", x + 1, 0, g);
                        }
                        x = drawString(name, x + 1, 0, g);
                        x = drawString(" = \"", x + 1, 0, g);
                        g.setColor(Color.BLUE);
                        if (value.length() > 12) {
                            value = value.substring(0, 10) + "..";
                        }
                        value = value.replace('\r', '_');
                        value = value.replace('\n', '_');
                        x = drawString(value, x + 1, 0, g);
                        g.setColor(Color.BLACK);
                        x = drawString("\"", x + 1, 0, g);
                    }
                }
                x = drawString(" )", x + 1, 0, g);
            }
        }
    }

    /**
   * Calculates the PreferredSize of panel. This is simply a rough guess. 
   * MayBe I should work this over sometimes.
   */
    public Dimension getPreferredSize() {
        int maxX = model.toString().length() * 10;
        return new Dimension(maxX, 15);
    }

    /**
   * gives access to the model which this panel represents
   *  
   * @return model this panel represents
   */
    public NodeModel getModel() {
        return model;
    }

    /**
   * setst the model this panel represents
   * 
   * @param node new model for representing
   */
    public void setModel(NodeModel node) {
        this.model = node;
    }

    /**
   * it has to be visible whether the model is selected or not. Many
   * operations depend on that
   * 
   * @return true when panel is selected 
   */
    public boolean isSelected() {
        return selected;
    }

    /**
   * sets the selection state of the panel
   * 
   * @param selected new selection state
   */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
   * gets whether this panel is shown on the right side of the screen or on the left
   * 
   * @return true when this panel belongs to the rigth side
   */
    public boolean isRigthSide() {
        return rigthSide;
    }

    /**
   * sets whether this panel is shown on the right side of the screen or on the left
   * 
   * @param rigthSide true when this panel belongs to the rigth side
   */
    public void setRigthSide(boolean rigthSide) {
        this.rigthSide = rigthSide;
    }
}
