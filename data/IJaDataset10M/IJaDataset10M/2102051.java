package vista.graficador.comun;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.border.AbstractBorder;
import org.uml.diagrammanagement.GraphNode;
import org.uml.diagrammanagement.SimpleSemanticModelBridge;
import vista.graficador.GrDiagrama;
import vista.graficador.GrNodoVisual;

/**
 * Represents a note inside a diagram. It can be dragged and connected to other
 * elements
 */
public final class Note extends GrNodoVisual {

    /**
     * Basic constructor.
     * 
     * @param graph
     *            the visual graph (diagram) where this note will be inserted.
     * @param logicalNode
     *            the logical node.
     */
    public Note(GrDiagrama graph, GraphNode logicalNode) {
        super(graph, logicalNode);
        layout = new GridBagLayout();
        setLayout(layout);
        border = new GrNoteBorder();
        setBorder(border);
        String sText = "new text";
        label = new MultiLineLabel(sText, MultiLineLabel.LEFT);
        label.setForeground(getForeColor());
        label.setBackground(Color.WHITE);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(11, 11, 11, 11);
        layout.setConstraints(label, c);
        add(label);
        setSize(getPreferredSize());
        setText(sText);
        updateNodoVisual();
    }

    /**
     * Sets the text of this note. The size of the note is also adjusted to
     * better fit its contents.
     * 
     * @param p_text
     *            the text.
     */
    public void setText(String p_text) {
        label.setText(p_text);
        setSize(getPreferredSize());
    }

    /**
     * Gets the text of this note.
     * 
     * @return the text of this note.
     */
    public String getText() {
        return label.getText();
    }

    /**
     * Gets the minimum width this note can have.
     */
    @Override
    protected int getMinimumWidth() {
        return 20;
    }

    private void print(String string) {
        System.out.println(string);
    }

    /**
     * Graphical component that contains the text of the note.
     */
    private final MultiLineLabel label;

    GrNoteBorder border;

    GridBagLayout layout;

    /**
     * This class draws the note border.
     * 
     * @author Daniel
     */
    class GrNoteBorder extends AbstractBorder {

        private final int EARSIZE = 10;

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(getBackColor());
            int[] earx = new int[6];
            int[] eary = new int[6];
            earx[0] = x;
            earx[1] = x + width - EARSIZE - 1;
            earx[2] = x + width - 1;
            earx[3] = x + width - 1;
            earx[4] = x;
            earx[5] = x;
            eary[0] = y;
            eary[1] = y;
            eary[2] = y + EARSIZE - 1;
            eary[3] = y + height - 1;
            eary[4] = y + height - 1;
            eary[5] = y;
            g.fillPolygon(earx, eary, 6);
            if (isSelected()) g.setColor(getSelectColor()); else g.setColor(getForeColor());
            g.drawLine(x, y, x + width - EARSIZE - 1, y);
            g.drawLine(x + width - EARSIZE - 1, y, x + width - 1, y + EARSIZE - 1);
            g.drawLine(x + width - 1, y + EARSIZE - 1, x + width - 1, y + height - 1);
            g.drawLine(x + width - 1, y + height - 1, x, y + height - 1);
            g.drawLine(x, y + height - 1, x, y);
            g.drawLine(x + width - EARSIZE - 1, y, x + width - EARSIZE - 1, y + EARSIZE - 1);
            g.drawLine(x + width - EARSIZE - 1, y + EARSIZE - 1, x + width - 1, y + EARSIZE - 1);
        }
    }

    @Override
    protected void updateNodoLogico() {
        try {
            SimpleSemanticModelBridge bridge = (SimpleSemanticModelBridge) modelo.getSemanticModel();
            if (bridge != null) bridge.setTypeInfo(getText());
        } catch (ClassCastException cce) {
            print("Error while updating visual graph node." + cce);
        }
    }

    @Override
    protected void updateNodoVisual() {
        try {
            SimpleSemanticModelBridge bridge = (SimpleSemanticModelBridge) modelo.getSemanticModel();
            if (bridge != null) setText(bridge.getTypeInfo());
        } catch (ClassCastException cce) {
            print("Error while updating visual graph node." + cce);
        }
    }
}
