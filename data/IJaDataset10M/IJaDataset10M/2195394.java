package physicssite.util;

import physicssite.serialization.SIM;
import physicssite.ui.Editor;
import physicssite.util.Language.Strings;

/**
 * Beschreibt die Physik eines Objekts oder der ganzen Simulation.<br>
 * Die Werte werden geerbt, wenn kein Wert (-1) gegeben ist.
 * 
 * @author Peter Güttinger
 */
public class Physics implements Cloneable {

    public enum Values {

        DENSITY(1, -1, true, Strings.density, SIM.d_density), FRICTION(0.5f, 0, true, Strings.friction, SIM.d_friction), RESTITUTION(0.25f, 1, true, Strings.restitution, SIM.d_restitution), LINEARDAMPING(0.1f, 0, true, Strings.lineardamping, SIM.d_lineardamping), ANGULARDAMPING(0.5f, 0, true, Strings.angulardamping, SIM.d_angulardamping), FIXEDROTATION(0, -1, false, Strings.fixedrotation, SIM.d_fixedrotation);

        String id;

        private float std;

        private float ideal;

        private boolean isNumber;

        private Strings name;

        public short tag;

        private Values(final float d, final float ideal, final boolean isNum, final Strings s, final short tag) {
            std = d;
            this.ideal = ideal;
            isNumber = isNum;
            name = s;
            this.tag = tag;
        }

        public final float getDefault() {
            return std;
        }

        public final float getIdeal() {
            return ideal;
        }

        public final boolean isNumber() {
            return isNumber;
        }

        @Override
        public String toString() {
            return "" + name;
        }
    }

    public static final int NUMVALUES = Values.values().length;

    private float[] values = new float[NUMVALUES];

    /**
	 * 
	 * @param d true für standardwerte, sonst wird alles geerbt.
	 */
    public Physics(final boolean d) {
        if (d) {
            setDefault();
        } else {
            for (int i = 0; i < NUMVALUES; i++) {
                values[i] = -1;
            }
        }
    }

    /**
	 * kopiert die Einstellungen aus <code>data</code>
	 * 
	 * @param data
	 */
    public Physics(final Physics data) {
        for (int i = 0; i < NUMVALUES; i++) {
            values[i] = data.values[i];
        }
    }

    /**
	 * setzt die Einstellungen auf die Standardwerte<br>Dies bedeutet, dass nichts mehr geerbt wird
	 */
    private void setDefault() {
        for (int i = 0; i < NUMVALUES; i++) {
            values[i] = Values.values()[i].getDefault();
        }
    }

    public final float getActualValue(final Values v) {
        return getActualValue(v.ordinal());
    }

    private float getActualValue(final int i) {
        final float value;
        if ((value = getValue(i)) != -1) return value;
        return Editor.simulation.defaults.getValue(i);
    }

    public final float getValue(final Values v) {
        return getValue(v.ordinal());
    }

    public final float getValue(final int i) {
        return values[i];
    }

    public final void setValue(final Values v, final float val) {
        setValue(v.ordinal(), val);
    }

    public final void setValue(final int i, final float val) {
        values[i] = val;
    }

    /**
	 * @return <code>true</code>, wenn alles geerbt wird (dh alle Werte sind -1)
	 */
    public final boolean isEmpty() {
        for (final float val : values) {
            if (val != -1) return false;
        }
        return true;
    }

    @Override
    public final Physics clone() {
        try {
            final Physics p = (Physics) super.clone();
            p.values = this.values.clone();
            return p;
        } catch (final CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
