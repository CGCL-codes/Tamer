package org.hswgt.teachingbox.core.ml.lwlr;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.hswgt.teachingbox.core.rl.datastructures.VectorMapper;
import org.hswgt.teachingbox.core.rl.env.Action;
import org.hswgt.teachingbox.core.rl.env.State;
import weka.core.Instances;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;

/**
 * WEKA does not make vector predictions. We need n classifiers to predict an Vector
 * of length n (see {@link org.hswgt.teachingbox.pbd.lwlr.LWLR_VectorPrediction 
 * LWLR_VectorPrediction} for further information). This class helps writing training 
 * data on n ARFF files (one data set file for each vector variable to predict). Writing
 * CSV files would maybe be easier to maintain, but ARFF files are also really simple and
 * self-explanatory. Further, in the weka mailing-list archive, one can found following:
 * <i>"A CSV file needs to be loaded fully into memory and then analyzed (each element in 
 * the matrix!), since Weka needs to determine the types of the attributes. Converting it 
 * to ARFF (and gzipping it to *.arff.gz) will making loading a lot quicker.</i>"
 * 
 * @author Richard Cubek
 *
 */
public class LWLR_VectorDatasetWriter {

    protected String arffHeader = null;

    protected Instances dataset = null;

    protected List<BufferedWriter> writerList = new LinkedList<BufferedWriter>();

    /**
	 * VectorMapper for input and output vector mappings. 
	 * See {@link org.hswgt.teachingbox.datastructures.VectorMapper VectorMapper} for 
	 * detailed information.
	 */
    protected VectorMapper inputVectorMapper = null;

    protected VectorMapper outputVectorMapper = null;

    /** round to n positions */
    protected int roundPositions = 3;

    /**
	 * Constructor. Initializes the common ARFF file header for all data sets to write.
	 * @param instances The empty instances, defining ARFF header.
	 */
    public LWLR_VectorDatasetWriter(Instances instances) {
        this.arffHeader = instances.toString();
        this.dataset = instances;
    }

    /**
	 * Initializes a BufferedWriter for the later to be predicted class variable.  
	 * @param path The full path of the ARFF file to write
	 * (i.e. "/home/user/project/data_1.arff").
	 * @throws IOException 
	 */
    public void initDatasetWriter(String path) throws IOException {
        writerList.add(new BufferedWriter(new FileWriter(path)));
        writerList.get(writerList.size() - 1).append(arffHeader);
    }

    /**
	 * Set a VectorMapper to map input vectors to vectors containing variables relevant
	 * for Locally Weighted Regression (regarding this special problem to solve). See {@link 
	 * org.hswgt.teachingbox.datastructures.VectorMapper VectorMapper} for detailed information.
	 * @param vectorMapper The VectorMapper to be used.
	 */
    public void setInputVectorMapper(VectorMapper vectorMapper) {
        this.inputVectorMapper = vectorMapper;
    }

    /**
	 * Set a VectorMapper to map output vectors to vectors, containing class variables relevant
	 * for this special problem to solve. See {@link 
	 * org.hswgt.teachingbox.datastructures.VectorMapper VectorMapper} for detailed information.
	 * @param vectorMapper The VectorMapper to be used.
	 */
    public void setOutputVectorMapper(VectorMapper vectorMapper) {
        this.outputVectorMapper = vectorMapper;
    }

    /**
	 * Write an input vector and the corresponding variable from the output vector to
	 * each data set file.
	 * @param inputVector The input vector.
	 * @param outputVector The output vector, has to be of same length ass added class variables.
	 * @throws Exception
	 */
    public void writeVectors(DenseDoubleMatrix1D inputVector, DenseDoubleMatrix1D outputVector) throws Exception {
        if (inputVectorMapper != null) {
            inputVector = inputVectorMapper.getMappedVector(inputVector);
        }
        if (outputVectorMapper != null) {
            outputVector = outputVectorMapper.getMappedVector(outputVector);
        }
        if (outputVector.size() != writerList.size()) {
            throw new Exception("Output vector has different length than amount of added dataset writers!");
        }
        for (int i = 0; i < writerList.size(); i++) {
            for (int j = 0; j < inputVector.size(); j++) {
                if (dataset.attribute(j).isNominal()) {
                    writerList.get(i).append(dataset.attribute(j).value((int) inputVector.get(j)) + ",");
                } else {
                    writerList.get(i).append(round(inputVector.get(j)) + ",");
                }
            }
            writerList.get(i).append(round(outputVector.get(i)) + "\n");
        }
    }

    /**
	 * Write an input vector consisting of state + action and the corresponding variable from the
	 * output vector to each data set file.
	 * @param state Input state in time step t. 
	 * @param action Action applied to the input state.
	 * @param nextState Resulting successor state in time step t+1.
	 * @param outputVector The output vector, has to be of same length ass added class variables.
	 * @throws Exception
	 */
    public void writeVectors(State state, Action action, State nextState) throws Exception {
        DenseDoubleMatrix1D inputVector = new DenseDoubleMatrix1D(state.size() + action.size());
        for (int i = 0; i < state.size(); i++) {
            inputVector.set(i, round(state.get(i)));
        }
        for (int i = 0; i < action.size(); i++) {
            inputVector.set(state.size() + i, round(action.get(i)));
        }
        writeVectors(inputVector, nextState);
    }

    /**
	 * Close the BufferedWriters.
	 * @throws IOException
	 */
    public void close() throws IOException {
        for (int i = 0; i < writerList.size(); i++) {
            writerList.get(i).close();
        }
    }

    /**
	 * On how much positions to round. Default value is 3 (i.e. 1.234).
	 * @param positions
	 */
    public void setRoundPositions(int positions) {
        roundPositions = positions;
    }

    private double round(double val) {
        long factor = (long) Math.pow(10, roundPositions);
        val = val * factor;
        long tmp = Math.round(val);
        return (double) tmp / factor;
    }
}
