package crl.blackberry.pdfwriter;

import java.util.Vector;

public class Body extends List {

    private int mByteOffsetStart;

    private int mObjectNumberStart;

    private int mGeneratedObjectsCount;

    private Vector mObjectsList;

    public Body() {
        super();
        clear();
    }

    public int getObjectNumberStart() {
        return mObjectNumberStart;
    }

    public void setObjectNumberStart(int Value) {
        mObjectNumberStart = Value;
    }

    public int getByteOffsetStart() {
        return mByteOffsetStart;
    }

    public void setByteOffsetStart(int Value) {
        mByteOffsetStart = Value;
    }

    public int getObjectsCount() {
        return mObjectsList.size();
    }

    private int getNextAvailableObjectNumber() {
        return ++mGeneratedObjectsCount + mObjectNumberStart;
    }

    public IndirectObject getNewIndirectObject() {
        return getNewIndirectObject(getNextAvailableObjectNumber(), 0, true);
    }

    public IndirectObject getNewIndirectObject(int Number, int Generation, boolean InUse) {
        IndirectObject iobj = new IndirectObject();
        iobj.setNumberID(Number);
        iobj.setGeneration(Generation);
        iobj.setInUse(InUse);
        return iobj;
    }

    public IndirectObject getObjectByNumberID(int Number) {
        IndirectObject iobj;
        int x = 0;
        while (x < mObjectsList.size()) {
            iobj = ((IndirectObject) mObjectsList.elementAt(x));
            if (iobj.getNumberID() == Number) return iobj;
            x++;
        }
        return null;
    }

    public void includeIndirectObject(IndirectObject iobj) {
        mObjectsList.addElement(iobj);
    }

    private String render() {
        int x = 0;
        int offset = mByteOffsetStart;
        while (x < mObjectsList.size()) {
            IndirectObject iobj = getObjectByNumberID(++x);
            String s = "";
            if (iobj != null) s = iobj.toPDFString() + "\n";
            mList.addElement(s);
            iobj.setByteOffset(offset);
            offset += s.length();
        }
        return renderList();
    }

    public String toPDFString() {
        return render();
    }

    public void clear() {
        super.clear();
        mByteOffsetStart = 0;
        mObjectNumberStart = 0;
        mGeneratedObjectsCount = 0;
        mObjectsList = new Vector();
    }
}
