package org.sf.xrime.algorithms.MST.MSTLabel;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.io.WritableFactories;
import org.apache.hadoop.io.WritableFactory;

/**
 * Label for MST 'Test' message
 * This label carries two parameter which are:
 *   the level of the current vertex's located fragment,
 *   the identity of the current vertex's located fragment.
 * @author YangYin
 * @see org.apache.hadoop.io.Writable
 */
public class MSTMessageTestLabel implements Cloneable, Writable {

    public static final String mstMessageTestLabel = "xrime.algorithem.MST.message.test.label";

    /**
   * the level parameter in the initiate message
   */
    private int fragLevel = 0;

    /**
   * the fragment identity in the initiate message
   */
    private String fragIdentity = "";

    public MSTMessageTestLabel() {
    }

    public MSTMessageTestLabel(MSTMessageTestLabel mstMessageTestLabel) {
        fragLevel = mstMessageTestLabel.getFragLevel();
        fragIdentity = mstMessageTestLabel.getFragIdentity();
    }

    static {
        WritableFactories.setFactory(MSTMessageTestLabel.class, new WritableFactory() {

            public Writable newInstance() {
                return new MSTMessageTestLabel();
            }
        });
    }

    public int getFragLevel() {
        return fragLevel;
    }

    public void setFragLevel(int fragLevel) {
        this.fragLevel = fragLevel;
    }

    public String getFragIdentity() {
        return fragIdentity;
    }

    public void setFragIdentity(String fragIdentity) {
        this.fragIdentity = fragIdentity;
    }

    public Object clone() {
        return new MSTMessageTestLabel(this);
    }

    public String toString() {
        String ret = "<";
        ret = ret + fragLevel + ", " + fragIdentity;
        ret = ret + ">";
        return ret;
    }

    @Override
    public void readFields(DataInput in) throws IOException {
        fragLevel = in.readInt();
        fragIdentity = Text.readString(in);
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeInt(fragLevel);
        Text.writeString(out, fragIdentity);
    }
}
