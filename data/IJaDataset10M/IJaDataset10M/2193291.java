package vista.graficador.comun;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import org.uml.diagrammanagement.GraphNode;
import org.uml.diagrammanagement.SimpleSemanticModelBridge;
import util.Util;
import vista.graficador.GrDiagrama;
import vista.graficador.GrNodoVisual;

/**
 * Represents a text element inside a diagram. It can be dragged and connected to other elements through
 * 
 */
public class Text extends GrNodoVisual {

    private MultiLineLabel label;

    GridBagLayout layout;

    /**
     * Basic constructor.
     * @param graph the visual graph (diagram) where this text will be inserted.
     * @param logicalNode the logical node.
     */
    public Text(GrDiagrama graph, GraphNode logicalNode) {
        super(graph, logicalNode);
        layout = new GridBagLayout();
        this.setLayout(layout);
        String sText = "texto nuevo";
        label = new MultiLineLabel(sText, MultiLineLabel.LEFT);
        label.setForeground(getForeColor());
        label.setBackground(getBackColor());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(2, 2, 2, 2);
        layout.setConstraints(label, c);
        add(label);
        setSize(getPreferredSize());
        setText(sText);
        updateNodoVisual();
    }

    /**
     * Sets the color of the text.
     * @param newColor the new color of the text.
     */
    public void setTextColor(Color newColor) {
        label.setForeground(newColor);
    }

    /**
     * Changes the content of this text.
     * @param p_text the new text to be set.
     */
    public void setText(String p_text) {
        label.setText(p_text);
        setSize(getPreferredSize());
    }

    /**
     * Get current contents of this text.
     * @return the current text.
     */
    public String getText() {
        return label.getText();
    }

    /**
     * Gets the minimum width this text shuold have.
     */
    protected int getMinimumWidth() {
        return 20;
    }

    /**
     * Repaints this text.
     */
    public void repaint() {
        super.repaint();
        if (label != null) label.repaint();
    }

    /**
     * Updates the visual appearance of this text, according to the logical node.
     */
    protected void updateNodoVisual() {
        try {
            SimpleSemanticModelBridge bridge = (SimpleSemanticModelBridge) modelo.getSemanticModel();
            if (bridge != null) setText(bridge.getTypeInfo());
        } catch (ClassCastException cce) {
            Util.printError("Error while updating visual graph node." + cce);
        }
    }

    protected void updateNodoLogico() {
        try {
            SimpleSemanticModelBridge bridge = (SimpleSemanticModelBridge) modelo.getSemanticModel();
            if (bridge != null) bridge.setTypeInfo(getText());
        } catch (ClassCastException cce) {
            Util.printError("Error while updating visual graph node." + cce);
        }
    }
}
