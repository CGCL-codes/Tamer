package itGraph.UMLElements;

import java.awt.geom.Rectangle2D;
import itGraph.BasicCell;
import javax.swing.ImageIcon;
import org.jdom.Element;
import org.jgraph.graph.GraphConstants;

public class FinalStateCell extends BasicCell {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4665150982375755479L;

    public FinalStateCell(Element data) {
        super("");
        this.data = data;
        GraphConstants.setAutoSize(attributes, true);
        setVisual();
    }

    public void setVisual() {
        Element graphics = data.getChild("graphics");
        ImageIcon icon = new ImageIcon("resources/images/" + graphics.getChildText("icon"));
        GraphConstants.setIcon(getAttributes(), icon);
        int x = Integer.parseInt(graphics.getChild("position").getAttributeValue("x"));
        int y = Integer.parseInt(graphics.getChild("position").getAttributeValue("y"));
        int width = Integer.parseInt(graphics.getChild("size").getAttributeValue("width"));
        int height = Integer.parseInt(graphics.getChild("size").getAttributeValue("height"));
        GraphConstants.setBounds(getAttributes(), new Rectangle2D.Double(x, y, width, height));
    }
}
