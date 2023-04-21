package org.sf.xrime.algorithms.setBFS;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.ToolRunner;
import org.sf.xrime.ProcessorExecutionException;
import org.sf.xrime.algorithms.transform.vertex.Vertex2LabeledTransformer;
import org.sf.xrime.model.Element;
import org.sf.xrime.model.label.LabelAdder;
import org.sf.xrime.model.label.Labelable;
import org.sf.xrime.model.vertex.LabeledAdjSetVertex;

/**
 * Transformer to add SetBFS label to LabeledAdjVertex
 * @author Cai Bin
 */
public class SetBFSLabelTransformer extends Vertex2LabeledTransformer {

    @Override
    public void setArguments(String[] params) throws ProcessorExecutionException {
        if (params.length != 2) {
            throw new ProcessorExecutionException("Wrong number of parameters: " + params.length + " instead of 2.");
        }
        setSrcPath(new Path(params[0]));
        setDestPath(new Path(params[1]));
    }

    /**
   * Add BFS label to LabeledAdjVertex.
   * @see org.sf.xrime.algorithms.transform.vertex.Vertex2LabeledTransformer#execute()
   */
    public void execute() throws ProcessorExecutionException {
        setLabelAdderClass(SetBFSLabelAdder.class);
        setOutputValueClass(LabeledAdjSetVertex.class);
        super.execute();
    }

    /**
   * BFS label adder for Vertex2LabeledTransformer.
   * @author Cai Bin
   */
    public static class SetBFSLabelAdder extends LabelAdder {

        /**
     * Add the label.
     * @see org.sf.xrime.model.label.LabelAdder#addLabels(org.sf.xrime.model.label.Labelable, org.sf.xrime.model.Element)
     */
        @Override
        public void addLabels(Labelable labels, Element element) {
            SetBFSLabel label = (SetBFSLabel) labels.getLabel(SetBFSLabel.setBFSLabelPathsKey);
            if (label == null) {
                label = new SetBFSLabel();
                labels.setLabel(SetBFSLabel.setBFSLabelPathsKey, label);
            }
        }
    }

    /**
   * Main method for this transformer.
   * @param args input arguments
   */
    public static void main(String[] args) {
        try {
            int res = ToolRunner.run(new SetBFSLabelTransformer(), args);
            System.exit(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
