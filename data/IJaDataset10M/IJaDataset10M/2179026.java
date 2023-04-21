package eibstack;

public class PropertyDescr {

    public int physAddr;

    public int objectIdx;

    public int propertyID;

    public int propertyIdx;

    public int type;

    public int maxNoElems;

    public int readLevel;

    public int writeLevel;

    public PropertyDescr(int da, int objIdx, int propID, int propIdx) {
        physAddr = da;
        objectIdx = objIdx;
        propertyID = propID;
        propertyIdx = propIdx;
        type = -1;
        maxNoElems = -1;
        readLevel = -1;
        writeLevel = -1;
    }

    public PropertyDescr(int da, int objIdx, int propID, int propIdx, int type, int maxNoElems, int readLevel, int writeLevel) {
        physAddr = da;
        objectIdx = objIdx;
        propertyID = propID;
        propertyIdx = propIdx;
        this.type = type;
        this.maxNoElems = maxNoElems;
        this.readLevel = readLevel;
        this.writeLevel = writeLevel;
    }

    public boolean equals(Object o) {
        if (o instanceof PropertyDescr) {
            PropertyDescr p = (PropertyDescr) o;
            return ((p.physAddr == physAddr) && (p.objectIdx == objectIdx) && ((p.propertyID == propertyID) || (propertyID == 0)) && ((p.propertyIdx == propertyIdx) || (propertyIdx == 0)));
        } else {
            return false;
        }
    }
}
