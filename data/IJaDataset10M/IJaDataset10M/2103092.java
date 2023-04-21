package rra.model;

import org.apache.commons.collections.Predicate;

public class RRAPredicate implements Predicate {

    private Object toFind = null;

    public RRAPredicate(Object toFind) {
        this.toFind = toFind;
    }

    @Override
    public boolean evaluate(Object arg0) {
        if (toFind instanceof Combat) {
            if (!(arg0 instanceof Combat)) return false;
            Combat ctf = (Combat) toFind;
            Combat carg0 = (Combat) arg0;
            if (ctf.getId() == carg0.getId()) return true;
            if (ctf.getName().equalsIgnoreCase(carg0.getName())) return true;
        }
        if (toFind instanceof Participant) {
            if (!(arg0 instanceof Participant)) return false;
            Participant ptf = (Participant) toFind;
            Participant parg0 = (Participant) arg0;
            if (ptf.getName().equalsIgnoreCase(parg0.getName())) return true;
        }
        if (toFind instanceof DPSStats) {
            if (!(arg0 instanceof DPSStats)) return false;
            DPSStats t1 = (DPSStats) toFind;
            DPSStats t2 = (DPSStats) arg0;
            if (t1.getAbilityName().equalsIgnoreCase(t2.getAbilityName())) return true;
        }
        if (toFind instanceof HPSStats) {
            if (!(arg0 instanceof HPSStats)) return false;
            HPSStats t1 = (HPSStats) toFind;
            HPSStats t2 = (HPSStats) arg0;
            if (t1.getAbilityName().equalsIgnoreCase(t2.getAbilityName())) return true;
        }
        if (toFind instanceof InterruptStats) {
            if (!(arg0 instanceof InterruptStats)) return false;
            InterruptStats t1 = (InterruptStats) toFind;
            InterruptStats t2 = (InterruptStats) arg0;
            if (t1.getAbilityName().equalsIgnoreCase(t2.getAbilityName())) return true;
        }
        return false;
    }
}
