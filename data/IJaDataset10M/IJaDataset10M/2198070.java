package flit;

import java.util.Vector;
import util.Xml;

public class FlitVec {

    private Vector flitVec;

    public FlitVec() {
        flitVec = new Vector();
    }

    public FlitVec(int size) {
        flitVec = new Vector(size);
    }

    public void add(Flit f) {
        flitVec.addElement(f);
    }

    public Flit elementAt(int i) {
        return (Flit) (flitVec.elementAt(i));
    }

    public int size() {
        return flitVec.size();
    }

    public boolean isEmpty() {
        return flitVec.isEmpty();
    }

    public boolean isFull() {
        if (flitVec.capacity() == flitVec.size()) {
            return true;
        } else return false;
    }

    public void trimToSize() {
        flitVec.trimToSize();
    }

    public void destroy() {
        flitVec.removeAllElements();
    }

    public void toXml() {
        Xml.opnBlk("flits");
        for (int i = 0; i < flitVec.size(); i++) {
            this.elementAt(i).toXml();
        }
        Xml.clsBlk();
    }
}
