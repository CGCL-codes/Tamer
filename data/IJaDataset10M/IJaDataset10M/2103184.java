package util.attribs;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class IntAttribute extends ScalarAttribute<Integer> {

    public IntAttribute(final String name, final boolean exp, final Integer min, final Integer max, final Integer initial) {
        super(name, exp, min, max, initial, "", false);
    }

    public IntAttribute(final String name, final boolean exp, final Integer min, final Integer max, final Integer initial, final boolean clamp) {
        super(name, exp, min, max, initial, "", clamp);
    }

    public IntAttribute(final String name, final boolean exp, final Integer min, final Integer max, final Integer initial, final String unit) {
        super(name, exp, min, max, initial, unit, false);
    }

    public IntAttribute(final String name, final boolean exp, final Integer min, final Integer max, final Integer initial, final String unit, final boolean clamp) {
        super(name, exp, min, max, initial, unit, clamp);
    }

    @Override
    public void write(final DataOutput out) throws IOException {
        super.write(out);
        out.writeInt(val);
    }

    @Override
    public void read(final DataInput in) throws IOException {
        val = in.readInt();
        if (val < min) {
            val = min;
        } else if (val > max) {
            val = max;
        }
    }
}
