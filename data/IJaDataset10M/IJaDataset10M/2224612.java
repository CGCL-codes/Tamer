package traffic.object;

import java.util.HashSet;
import java.util.Iterator;
import rescuecore.RescueConstants;

public class Building extends PointObject {

    public Building(int id) {
        super(id);
    }

    public int type() {
        return RescueConstants.TYPE_BUILDING;
    }

    private int[] m_entrances;

    private HashSet m_entranceSet = null;

    private int fbCount = 0;

    public HashSet entrances() {
        if (m_entranceSet != null) return m_entranceSet;
        m_entranceSet = new HashSet(m_entrances.length, 1);
        for (int i = 0; i < m_entrances.length; i++) m_entranceSet.add(WORLD.get(m_entrances[i]));
        m_entrances = null;
        return m_entranceSet;
    }

    public void setEntrances(int[] value) {
        if (m_entranceSet == null && m_entrances == null) m_entrances = value;
    }

    public void input(int property, int[] value) {
        switch(property) {
            default:
                super.input(property, value);
                break;
            case RescueConstants.PROPERTY_ENTRANCES:
                setEntrances(value);
                break;
        }
    }

    public boolean isAdjacentTo(MotionlessObject obj) {
        return entrances().contains(obj);
    }

    public HashSet links() {
        return entrances();
    }

    public boolean cannotBeEnteredByFB() {
        if (this instanceof Refuge || this instanceof FireStation || this instanceof AmbulanceCenter || this instanceof PoliceOffice) return false;
        for (Iterator iter = WORLD.fireBrigadeList.iterator(); iter.hasNext(); ) {
            FireBrigade fb = (FireBrigade) iter.next();
            if (fb.motionlessPosition().id == this.id) return true;
        }
        return false;
    }
}
