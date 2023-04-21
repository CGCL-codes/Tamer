package edu.unibi.agbi.dawismd.entities.biodwh.go;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GraphPath2termId implements java.io.Serializable {

    private static final long serialVersionUID = 2348390802479215911L;

    private int graphPathId;

    private int termId;

    private int rank;

    public GraphPath2termId() {
    }

    public GraphPath2termId(int graphPathId, int termId, int rank) {
        this.graphPathId = graphPathId;
        this.termId = termId;
        this.rank = rank;
    }

    @Column(name = "graph_path_id", nullable = false)
    public int getGraphPathId() {
        return this.graphPathId;
    }

    public void setGraphPathId(int graphPathId) {
        this.graphPathId = graphPathId;
    }

    @Column(name = "term_id", nullable = false)
    public int getTermId() {
        return this.termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    @Column(name = "rank", nullable = false)
    public int getRank() {
        return this.rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public boolean equals(Object other) {
        if ((this == other)) return true;
        if ((other == null)) return false;
        if (!(other instanceof GraphPath2termId)) return false;
        GraphPath2termId castOther = (GraphPath2termId) other;
        return (this.getGraphPathId() == castOther.getGraphPathId()) && (this.getTermId() == castOther.getTermId()) && (this.getRank() == castOther.getRank());
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getGraphPathId();
        result = 37 * result + this.getTermId();
        result = 37 * result + this.getRank();
        return result;
    }
}
