package eu.larkc.iris.storage;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.mapred.InputSplit;

public abstract class FactsInputSplit implements InputSplit {

    public FactsInputSplit() {
    }

    /** {@inheritDoc} */
    public String[] getLocations() throws IOException {
        return new String[] {};
    }

    /** {@inheritDoc} */
    public void readFields(DataInput input) throws IOException {
    }

    /** {@inheritDoc} */
    public void write(DataOutput output) throws IOException {
    }
}
