package reasoning;

import org.tzi.use.parser.SrcPos;

public class SrcEMF extends SrcPos {

    private String elName;

    String elementType;

    public SrcEMF(String name, String eltype) {
        super(name, 0, 0);
        elName = name;
        elementType = eltype;
    }

    public String toString() {
        return elementType + ":" + elName;
    }
}
