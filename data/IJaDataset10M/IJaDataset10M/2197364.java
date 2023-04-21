package annone.engine.binary;

import java.io.DataOutput;
import java.io.IOException;
import annone.util.Checks;

public class CField extends CMember {

    private final CElement type;

    public CField(String name, CElement type) {
        super("Field", name);
        Checks.notNull("type", type);
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; else if (obj instanceof CField) return super.equals(obj) && type.equals(((CField) obj).type); else return false;
    }

    @Override
    public void writeTo(DataOutput out, ComponentFile componentFile) throws IOException {
        super.writeTo(out, componentFile);
        writeIndex(out, componentFile.getSymbolIndex(type));
    }
}
