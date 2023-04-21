package org.equanda.test.validator;

import org.equanda.client.EquandaException;
import org.equanda.test.dm.server.pm.DMPriorityFields;
import org.equanda.validation.FieldValidatorAdapter;

/**
 * Validator for PriorityFields test.
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class PriorityValidator3 extends FieldValidatorAdapter<Boolean, DMPriorityFields> {

    public Boolean validate(Boolean value, Boolean prevValue, DMPriorityFields entity) throws EquandaException {
        String start = entity.getTest();
        if (start == null) start = "";
        entity.setTest(start + "3");
        return value;
    }
}
