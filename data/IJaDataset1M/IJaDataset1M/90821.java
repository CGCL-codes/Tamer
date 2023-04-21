package biologicalObjects.edges;

import java.awt.Color;
import biologicalElements.GraphElementAbstract;
import edu.uci.ics.jung.graph.Edge;

public class BiologicalEdgeAbstract extends GraphElementAbstract {

    private Edge edge;

    private KEGGEdge keggEdge;

    ReactionPairEdge reactionPairEdge;

    private boolean isDirected;

    private boolean isWeighted = false;

    public boolean isWeighted() {
        return isWeighted;
    }

    public void setWeighted(boolean isWeighted) {
        this.isWeighted = isWeighted;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    private int weight = 0;

    private SBMLEdge sbml = new SBMLEdge();

    public SBMLEdge getSbml() {
        return sbml;
    }

    public void setSbml(SBMLEdge sbml) {
        this.sbml = sbml;
    }

    public BiologicalEdgeAbstract(Edge edge, String label, String name) {
        this.edge = edge;
        super.setName(name);
        super.setLabel(label);
        super.setIsEdge(true);
        sbml.setName(name);
        sbml.setEdge(edge.toString());
        sbml.setLabel(label);
        sbml.setIsAbstract("false");
        sbml.setIsDirected("false");
        sbml.setFrom(edge.getEndpoints().getFirst().toString());
        sbml.setTo(edge.getEndpoints().getSecond().toString());
    }

    public Edge getEdge() {
        return edge;
    }

    public void setEdge(Edge edge) {
        this.edge = edge;
    }

    public boolean isDirected() {
        return isDirected;
    }

    public void setDirected(boolean isDirected) {
        this.isDirected = isDirected;
    }

    public KEGGEdge getKeggEdge() {
        return keggEdge;
    }

    public void setKeggEdge(KEGGEdge keggEdge) {
        this.keggEdge = keggEdge;
    }

    @Override
    public Color getColor() {
        if (super.isReference()) {
            return Color.LIGHT_GRAY;
        } else {
            return super.getColor();
        }
    }

    public void setReactionPairEdge(ReactionPairEdge reactPEdge) {
        this.reactionPairEdge = reactPEdge;
    }

    public ReactionPairEdge getReactionPairEdge() {
        return reactionPairEdge;
    }
}
