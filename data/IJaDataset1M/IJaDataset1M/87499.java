package org.encog.ml.data.buffer;

import java.util.Iterator;
import org.encog.ml.data.MLDataPair;
import org.encog.ml.data.MLlDataError;
import org.encog.ml.data.basic.BasicMLDataPair;

/**
 * An iterator for the BufferedNeuralDataSet.
 */
public class BufferedDataSetIterator implements Iterator<MLDataPair> {

    /**
	 * The dataset being iterated over.
	 */
    private final BufferedNeuralDataSet data;

    /**
	 * The current record.
	 */
    private int current;

    /**
	 * Construct the iterator.
	 * 
	 * @param theData
	 *            The dataset to iterate over.
	 */
    public BufferedDataSetIterator(final BufferedNeuralDataSet theData) {
        this.data = theData;
        this.current = 0;
    }

    /**
	 * @return True if there is are more records to read.
	 */
    @Override
    public final boolean hasNext() {
        return this.current < this.data.getRecordCount();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public final MLDataPair next() {
        if (!hasNext()) {
            return null;
        }
        final MLDataPair pair = BasicMLDataPair.createPair(this.data.getInputSize(), this.data.getIdealSize());
        this.data.getRecord(this.current++, pair);
        return pair;
    }

    /**
	 * Not supported.
	 */
    @Override
    public final void remove() {
        throw new MLlDataError("Remove is not supported.");
    }
}
