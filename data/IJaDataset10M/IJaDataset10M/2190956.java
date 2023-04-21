package jung.ext.elements;

import vilaug.components.PanelFactory;
import edu.uci.ics.jung.graph.Edge;
import edu.uci.ics.jung.graph.Vertex;
import edu.uci.ics.jung.graph.Graph;
import jung.ext.registry.GraphDecorator;
import jung.ext.utils.BasicUtils;
import java.util.Set;
import java.util.HashSet;

public class EmptyElementDialog implements ElementDialog, GraphDecorator {

    private static final String ID = EmptyElementDialog.class.getName();

    private static int globalIndex = -1;

    private final int index;

    private PanelFactory panelFactory;

    private DialogMode mode;

    private Graph graph;

    public EmptyElementDialog() {
        index = globalIndex++;
    }

    public void setPanelFactory(PanelFactory panelFactory) {
        this.panelFactory = panelFactory;
    }

    public PanelFactory getPanelFactory() {
        return panelFactory;
    }

    public boolean createVertex() {
        return true;
    }

    public boolean changeVertex(Vertex vertex) {
        return true;
    }

    public boolean createEdge(Vertex some, Vertex other) {
        return true;
    }

    public boolean removeVertex(Vertex vertex) {
        return true;
    }

    public boolean removeEdge(Edge edge) {
        return true;
    }

    public void setMode(DialogMode mode) {
        this.mode = mode;
    }

    public DialogMode getMode() {
        return mode;
    }

    public void setCargo(Object cargo) {
    }

    public Object getCargo() {
        return null;
    }

    public void removeCargo() {
    }

    public void setGraph(Graph graph) {
        this.graph = graph;
    }

    public Graph getGraph() {
        return graph;
    }

    public boolean hasGraph() {
        return (graph != null);
    }

    public String getName() {
        return BasicUtils.afterDot(ID);
    }

    public String getBaseClass() {
        return ID;
    }

    protected Set getAdditionalInfo() {
        Set info = new HashSet(4);
        info.add("MODE=" + getMode());
        return info;
    }

    public String toString() {
        return getName() + "[" + index + "]" + " {" + BasicUtils.deTokenizer(getAdditionalInfo(), ", ") + "}";
    }
}
