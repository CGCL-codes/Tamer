package cruise.umple.compiler;

/**
 * NOT used right now
 */
public class PrimitiveType extends Type {

    private String typeName;

    public PrimitiveType(String aTypeName) {
        super();
        typeName = aTypeName;
    }

    public boolean setTypeName(String aTypeName) {
        boolean wasSet = false;
        typeName = aTypeName;
        wasSet = true;
        return wasSet;
    }

    public String getTypeName() {
        return typeName;
    }

    public void delete() {
        super.delete();
    }
}
