package org.ietr.preesm.mapper.abc.transaction;

import java.util.List;
import org.ietr.preesm.mapper.model.MapperDAG;
import org.ietr.preesm.mapper.model.MapperDAGEdge;

/**
 * A transaction that removes one edge in an implementation
 * 
 * @author mpelcat
 */
public class RemoveEdgeTransaction extends Transaction {

    /**
	 * Implementation DAG from which the edge is removed
	 */
    private MapperDAG implementation = null;

    /**
	 * edge removed
	 */
    private MapperDAGEdge edge = null;

    public RemoveEdgeTransaction(MapperDAGEdge edge, MapperDAG implementation) {
        super();
        this.edge = edge;
        this.implementation = implementation;
    }

    @Override
    public void execute(List<Object> resultList) {
        super.execute(resultList);
        implementation.removeEdge(edge);
    }

    @Override
    public String toString() {
        return ("RemoveEdge(" + edge.toString() + ")");
    }
}
