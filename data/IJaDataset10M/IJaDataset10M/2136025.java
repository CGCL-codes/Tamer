package Interface.JGraph_Diagrama;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;
import Mapeamento.Atributos.Atributo;
import org.jgraph.graph.Port;

/**
 *
 * @author Tiago Falcao
 */
public class BlocoGraphCell extends DefaultGraphCell {

    DefaultPort port;

    /** Creates a new instance of BlocoGraphCell */
    public BlocoGraphCell(String Nome) {
        this(20, 20, Nome);
    }

    public BlocoGraphCell(double x, double y, String Nome) {
        this(x, y, 80, 40, Color.white, Nome);
    }

    public BlocoGraphCell(double x, double y, double w, double h, Color bg, String Nome) {
        this(x, y, w, h, bg, Nome, "");
    }

    public BlocoGraphCell(double x, double y, double w, double h, Color bg, String Nome, String viewClass) {
        super(Nome);
        if (viewClass != null) JGraphCellViewFactory.setViewClass(this.getAttributes(), viewClass);
        GraphConstants.setBounds(this.getAttributes(), new Rectangle2D.Double(x, y, w, h));
        if (bg != null) {
            GraphConstants.setBackground(this.getAttributes(), bg);
            GraphConstants.setOpaque(this.getAttributes(), true);
        }
        GraphConstants.setBorderColor(this.getAttributes(), Color.black);
        this.addPort();
    }

    /**
     * Utility method to create a port for this cell. This method adds
     * a floating port.
     */
    public void addPort() {
        addPort(null);
    }

    /**
     * Utility method to create a port for this cell. The method adds a port
     * at a fixed relative offset within the cell. If the offset is null
     * then a floating port is added.
     * @param offset the offset of the port within the cell
     */
    public void addPort(Point2D offset) {
        addPort(offset, this);
    }

    /**
     * Utility method to create a port for this cell. The method adds a port
     * at a fixed relative offset within the cell. If the offset is null
     * then a floating port is added.
     * @param offset the offset of the port within the cell
     * @param userObject the user object of the port cell
     */
    public void addPort(Point2D offset, Object userObject) {
        port = new DefaultPort(userObject);
        if (offset == null) {
            add(port);
        } else {
            GraphConstants.setOffset(port.getAttributes(), offset);
            add(port);
        }
    }

    public DefaultPort getPort() {
        return (DefaultPort) this.getChildAt(0);
    }
}
