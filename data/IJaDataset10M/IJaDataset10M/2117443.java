package net.jarre_de_the.griffin.types.field;

import java.io.IOException;
import java.io.RandomAccessFile;
import net.jarre_de_the.griffin.object.File;
import net.jarre_de_the.griffin.types.data.FloatData;

/**
 *
 * @author charly4711
 */
public class FloatField extends BaseSimpleField {

    public FloatField() {
        super(FieldType.FLOAT, new FloatData());
    }

    public FloatField(byte[] label, FloatData dataOrDataOffset) {
        super(FieldType.FLOAT, dataOrDataOffset);
        setLabel(label);
    }

    public FloatField(RandomAccessFile in, File file) throws IOException {
        super(in, file, FloatData.class);
    }

    @Override
    public FloatData getValue() {
        return (FloatData) getData();
    }

    @Override
    public AbstractField clone() {
        return new FloatField(getLabel(), getValue().clone());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof FloatField)) {
            return false;
        }
        FloatField f = (FloatField) o;
        if (f.getValue().getValueAsBitField() == this.getValue().getValueAsBitField() && f.getType().equals(this.getType())) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.getValue().hashCode();
    }
}
