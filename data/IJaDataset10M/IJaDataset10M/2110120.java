package org.equanda.test.validator;

import org.equanda.persistence.EquandaConstraintViolation;
import org.equanda.persistence.EquandaDowngradeException;
import org.equanda.persistence.EquandaEntity;
import org.equanda.test.TestExceptionCodes;
import org.equanda.test.dm.server.pm.DMVehicleWithWings;
import org.equanda.validation.FieldValidatorAdapter;

/**
 * Verify whether speed is possible for
 *
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class VehicleSpeedValidator extends FieldValidatorAdapter<Integer, EquandaEntity> {

    public Integer validate(Integer speed, Integer prevValue, EquandaEntity equandaEntity) throws EquandaConstraintViolation {
        if (speed < 100 && equandaEntity instanceof DMVehicleWithWings) {
            throw new EquandaDowngradeException(TestExceptionCodes.EDE_TOO_SLOW_FOR_A_VEHICLE_WITH_WINGS, Integer.toString(speed));
        }
        return speed;
    }
}
