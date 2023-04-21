package net.sf.fb4j.model;

import net.sf.fb4j.FacebookField;
import net.sf.fb4j.FacebookObject;

/**
 * @author Mino Togna
 * @author Gino Miceli
 */
public class NetworkAffiliation extends FacebookObject<NetworkAffiliation.Field> {

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return getLong(Field.NID);
    }

    public String getName() {
        return getString(Field.NAME);
    }

    public Integer getYear() {
        return getInteger(Field.YEAR);
    }

    public String getGraduationStatus() {
        return getString(Field.STATUS);
    }

    public String getType() {
        return getString(Field.TYPE);
    }

    @Override
    protected Field fieldForName(String fieldName) {
        return Field.valueOf(fieldName.toUpperCase());
    }

    public enum Field implements FacebookField {

        NID, NAME, TYPE, YEAR, STATUS;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }
}
