package org.sf.xrime.algorithms.MST.MSTLabel;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;
import org.apache.hadoop.io.WritableFactory;
import org.sf.xrime.model.label.Labels;

/**
 * This label is used to accommodate the status of a certain vertex's edges in MST algorithm
 * @author YangYin
 * @see org.apache.hadoop.io.Writable
 */
public class MSTEdgeStatesLabel implements Cloneable, Writable {

    public static final String mstEdgeStatesLabel = "xrime.algorithm.MST.edge.states.label";

    /**
   * Edges in MST algorithm have three kind of status which are implemented by Labels,
   * and the variable of status is implemented by MSTEdgeStateVariable
   * @see org.sf.xrime.algorithms.MST.MSTLabel.MSTEdgeStateVariable
   */
    Labels edgeStates;

    static {
        WritableFactories.setFactory(MSTEdgeStatesLabel.class, new WritableFactory() {

            public Writable newInstance() {
                return new MSTEdgeStatesLabel();
            }
        });
    }

    public MSTEdgeStatesLabel() {
        edgeStates = new Labels();
    }

    public MSTEdgeStatesLabel(MSTEdgeStatesLabel mstEdgeStatesLabel) {
        edgeStates = new Labels(mstEdgeStatesLabel.getEdgeStates());
    }

    public void setEdgeStates(Labels edges) {
        edgeStates = edges;
    }

    public MSTEdgeStateVariable getEdgeState(String vertexId) {
        if (edgeStates == null) return null; else return (MSTEdgeStateVariable) edgeStates.getLabel(vertexId);
    }

    public Labels getEdgeStates() {
        return edgeStates;
    }

    public void setEdgeState(String vertexId, MSTEdgeStateVariable edgeStateVariable) {
        if (edgeStates == null) edgeStates = new Labels();
        edgeStates.setLabel(vertexId, edgeStateVariable);
    }

    public void addEdgeStates(Labels extraEdgeStates) {
        if (edgeStates == null) edgeStates = new Labels();
        if (extraEdgeStates == null) return;
        Set<String> keySet = extraEdgeStates.getLabels().keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            MSTEdgeStateVariable value = (MSTEdgeStateVariable) extraEdgeStates.getLabel(key);
            edgeStates.setLabel(key, value);
        }
    }

    public String toString() {
        String ret = "";
        ret = edgeStates.toString();
        return ret;
    }

    public Object clone() {
        return new MSTEdgeStatesLabel(this);
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        edgeStates.readFields(in);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        edgeStates.write(out);
    }
}
