package lug.serenity.npc.model.equipment;

import org.exolab.castor.mapping.FieldHandler;
import org.exolab.castor.mapping.ValidityException;

/**
 * @author Luggy
 *
 */
public class EquipmentTypeFieldHandler implements FieldHandler {

    public void checkValidity(Object arg0) throws ValidityException, IllegalStateException {
    }

    public Object getValue(Object arg0) throws IllegalStateException {
        if (!(arg0 instanceof Equipment)) {
            throw new IllegalStateException("Argument is not Equipment");
        }
        return ((Equipment) arg0).getType().getName();
    }

    public Object newInstance(Object arg0) throws IllegalStateException {
        return null;
    }

    public void resetValue(Object arg0) throws IllegalStateException, IllegalArgumentException {
    }

    public void setValue(Object arg0, Object arg1) throws IllegalStateException, IllegalArgumentException {
        if (!(arg0 instanceof Equipment)) {
            throw new IllegalArgumentException("Target object not of type equipment.");
        }
        if (!(arg1 instanceof String)) {
            throw new IllegalArgumentException("Target value is not of type string");
        }
        ((Equipment) arg0).setType(EquipmentType.getForName((String) arg1));
    }
}
