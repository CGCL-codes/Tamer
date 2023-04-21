package annone.engine.binary;

import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import annone.util.Checks;

public abstract class CComponent extends CElement {

    private final String qualifiedName;

    private final CGeneric[] generics;

    public CComponent(String nodeName, String qualifiedName, CGeneric[] generics) {
        super(nodeName);
        Checks.notEmpty("qualifiedName", qualifiedName);
        this.qualifiedName = qualifiedName;
        this.generics = generics;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; else if (obj instanceof CComponent) return super.equals(obj) && qualifiedName.equals(((CComponent) obj).qualifiedName) && Arrays.equals(generics, ((CComponent) obj).generics); else return false;
    }

    @Override
    public void writeTo(DataOutput out, ComponentFile componentFile) throws IOException {
        super.writeTo(out, componentFile);
        writeIndex(out, componentFile.getStringIndex(qualifiedName));
        writeSymbols(out, componentFile, generics);
    }
}
