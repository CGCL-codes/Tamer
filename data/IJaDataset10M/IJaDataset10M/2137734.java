package PrologPlusCG;

import java.util.Enumeration;
import java.util.Vector;

public class CRegle extends Vector implements java.io.Serializable {

    CRegle() {
        super(5, 2);
    }

    public CTerme getS(int i) {
        return (CTerme) elementAt(i);
    }

    public void Detruit() {
        if (this.size() == 0) {
            return;
        } else {
            CTerme unTerm;
            for (Enumeration e = this.elements(); e.hasMoreElements(); ) {
                unTerm = (CTerme) e.nextElement();
                unTerm.Detruit();
            }
            ;
            this.removeAllElements();
        }
        ;
    }

    public void add(CTerme terme) {
        addElement(terme);
    }
}
