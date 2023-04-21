package com.compomics.acromics.mapreduce.io.writables;

import org.apache.hadoop.io.Text;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Date: 11/02/11
 * Time: 13:52
 */
public class SAMWritable implements CoordinateExtensionWritable {

    private Text iCigar = new Text();

    private Text iSequence = new Text();

    public SAMWritable() {
        super();
    }

    /**
     * Serialize the fields of this object to <code>out</code>.
     *
     * @param out <code>DataOuput</code> to serialize this object into.
     * @throws java.io.IOException
     */
    @Override
    public void write(DataOutput out) throws IOException {
        iCigar.write(out);
        iSequence.write(out);
    }

    /**
     * Deserialize the fields of this object from <code>in</code>.
     * <p/>
     * <p>For efficiency, implementations should attempt to re-use storage in the
     * existing object where possible.</p>
     *
     * @param in <code>DataInput</code> to deseriablize this object from.
     * @throws java.io.IOException
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        iCigar.readFields(in);
        iSequence.readFields(in);
    }

    public String getCigar() {
        return iCigar.toString();
    }

    public String getSequence() {
        return iSequence.toString();
    }

    public void setCigar(String aCigar) {
        iCigar.set(aCigar);
    }

    public void setSequence(String aSequence) {
        iSequence.set(aSequence);
    }
}
