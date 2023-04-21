package edu.upmc.opi.caBIG.caTIES.middletier;

import edu.upmc.opi.caBIG.caTIES.database.domain.impl.PatientImpl;

public class CaTIES_PatientImpl implements Comparable {

    /**
     * A public handle to an instance of CaTIES_PatientImpl.
     */
    public PatientImpl obj;

    String displayText = "";

    public CaTIES_PatientImpl() {
        this(new PatientImpl());
    }

    public CaTIES_PatientImpl(PatientImpl obj) {
        this(obj, obj.getUuid());
    }

    public CaTIES_PatientImpl(PatientImpl obj, String displayText) {
        super();
        this.obj = obj;
        this.displayText = displayText;
    }

    public String toString() {
        return displayText;
    }

    public boolean equals(Object o) {
        if (o instanceof CaTIES_PatientImpl) return obj.equals(((CaTIES_PatientImpl) o).obj); else return false;
    }

    public int compareTo(Object o) {
        if (o instanceof CaTIES_PatientImpl) {
            return this.obj.getUuid().compareToIgnoreCase(((CaTIES_PatientImpl) o).obj.getUuid());
        } else return 1;
    }
}
