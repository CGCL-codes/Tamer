package kr.ac.ssu.imc.whitehole.report.designer.items;

public class RDVirtualObject {

    protected int nId;

    public RDVirtualObject(int nId) {
        this.nId = nId;
    }

    public int getId() {
        return nId;
    }

    public void setId(int nValue) {
        nId = nValue;
    }
}
