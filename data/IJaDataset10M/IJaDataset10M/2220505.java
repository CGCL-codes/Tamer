package org.equanda.test.validator;

import org.equanda.persistence.EquandaConstraintViolation;
import org.equanda.persistence.EquandaEntity;
import org.equanda.test.TestExceptionCodes;
import org.equanda.validation.FieldValidatorAdapter;

/**
 * Verify that the value to check is a multiple of 100.
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class MultipleOf100Validator extends FieldValidatorAdapter<Integer, EquandaEntity> {

    public Integer validate(Integer value, Integer prevValue, EquandaEntity equandaEntity) throws EquandaConstraintViolation {
        if (value % 100 != 0) {
            throw new EquandaConstraintViolation(TestExceptionCodes.ECV_NUMBER_NOT_MULTIPLE_OF_100, Integer.toString(value));
        }
        return value;
    }
}
