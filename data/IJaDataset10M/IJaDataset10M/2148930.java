package net.jarre_de_the.griffin.types.field;

import java.io.IOException;
import java.io.RandomAccessFile;
import net.jarre_de_the.griffin.object.File;
import net.jarre_de_the.griffin.types.data.WordData;

/**
 *
 * @author charly4711
 */
public class WordField extends BaseSimpleField {

    public WordField() {
        super(FieldType.WORD, new WordData());
    }

    public WordField(byte[] label, WordData dataOrDataOffset) {
        super(FieldType.WORD, dataOrDataOffset);
        setLabel(label);
    }

    public WordField(RandomAccessFile in, File file) throws IOException {
        super(in, file, WordData.class);
    }

    public WordData getValue() {
        return (WordData) getData();
    }

    @Override
    public AbstractField clone() {
        return new WordField(getLabel(), getValue().clone());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof WordField)) {
            return false;
        }
        WordField f = (WordField) o;
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
