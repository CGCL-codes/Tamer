package org.encog.ml.data.buffer.codec;

/**
 * A CODEC is used to encode and decode data. The DataSetCODEC is designed to
 * move data to/from the Encog binary training file format, used by
 * BufferedNeuralDataSet. CODECs are provided for such items as CSV files,
 * arrays and many other sources.
 * 
 */
public interface DataSetCODEC {

    /**
	 * Read one record of data from an external source.
	 * 
	 * @param input
	 *            The input data array.
	 * @param ideal
	 *            The ideal data array.
	 * @param signidicance
	 * 			 The significance.  The first element will be modified 
	 * to hold the significance.
	 * @return True, if there is more data to be read.
	 */
    boolean read(double[] input, double[] ideal, double[] significance);

    /**
	 * Write one record of data to an external destination.
	 * 
	 * @param input
	 *            The input data array.
	 * @param ideal
	 *            The ideal data array.
	 */
    void write(double[] input, double[] ideal, double significance);

    /**
	 * Prepare to write to an external data destination.
	 * 
	 * @param recordCount
	 *            The total record count, that will be written.
	 * @param inputSize
	 *            The input size.
	 * @param idealSize
	 *            The ideal size.
	 */
    void prepareWrite(int recordCount, int inputSize, int idealSize);

    /**
	 * Prepare to read from an external data source.
	 */
    void prepareRead();

    /**
	 * @return The size of the input data.
	 */
    int getInputSize();

    /**
	 * @return The size of the ideal data.
	 */
    int getIdealSize();

    /**
	 * Close any open files.
	 */
    void close();
}
