package iogl.parser;

public class ASTIO extends SimpleNode {

    String name;

    String ioid;

    public String getIoid() {
        return ioid;
    }

    public void setIoid(String pvIoid) {
        ioid = pvIoid;
    }

    public String getName() {
        return name;
    }

    public void setName(String pvName) {
        name = pvName;
    }

    public ASTIO(int id) {
        super(id);
    }

    public ASTIO(IOgl p, int id) {
        super(p, id);
    }

    /** Accept the visitor. **/
    public Object jjtAccept(IOglVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
