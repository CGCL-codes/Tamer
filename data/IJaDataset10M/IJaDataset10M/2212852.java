package orbgate;

import java.util.Vector;
import org.omg.CORBA.*;

final class NVListImpl extends org.omg.CORBA.NVList {

    private org.omg.CORBA.ORB orb_;

    private java.util.Vector items_;

    public NVListImpl(org.omg.CORBA.ORB orb, int count) {
        orb_ = orb;
        items_ = new Vector(count);
    }

    public int count() {
        return items_.size();
    }

    public NamedValue add(int flags) {
        return add_value("", null, flags);
    }

    public NamedValue add_item(String item_name, int flags) {
        return add_value(item_name, null, flags);
    }

    public NamedValue add_value(String item_name, Any val, int flags) {
        NamedValue nv = orb_.create_named_value(item_name, val, flags);
        items_.addElement(nv);
        return nv;
    }

    public NamedValue item(int index) throws org.omg.CORBA.Bounds {
        try {
            return (NamedValue) (items_.elementAt(index));
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new org.omg.CORBA.Bounds();
        }
    }

    public void remove(int index) throws org.omg.CORBA.Bounds {
        try {
            items_.removeElementAt(index);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new org.omg.CORBA.Bounds();
        }
    }
}
