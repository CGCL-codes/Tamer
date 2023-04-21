package org.wfp.rita.exception;

import java.sql.SQLException;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.NotNull;
import org.hibernate.PropertyValueException;
import org.wfp.rita.datafacade.DataFacade;
import org.wfp.rita.pojo.base.VersionedRecord;

/**
 * Thrown by {@link DataFacade#save} if the object that you tried to save
 * is missing a value for a required (not nullable) property.
 * @author Chris Wilson <chris+rita@aptivate.org>
 */
public class MissingValue extends ConstraintViolation {

    /**
     * Constructor which automatically generates a reasonable error message
     * from a {@link PropertyValueException} object.
     * 
     * @param operation The operation that was being attempted on the entity
     * as a verb, e.g. "save", "update", "delete"
     * @param entity The erroneous entity, e.g. a {@link VersionedRecord}. 
     * @param pve The {@link PropertyValueException} that was thrown by
     * Hibernate due to the invalid value of the entity's property.
     */
    public MissingValue(String operation, Object entity, PropertyValueException pve) {
        super(operation, entity, pve.getEntityName().replaceFirst(".*\\.", "") + " is missing required value for " + pve.getPropertyName(), pve);
    }

    /**
     * Generic constructor when a {@link SQLException} was thrown rather than
     * a {@link PropertyValueException}, which means that the error was not
     * detected by Hibernate but was detected by the database, so only a
     * database-specific error message is available.
     * 
     * <p>The SQLState is not very specific, e.g. 23000 is "Integrity
     * Constraint Violation" which includes both duplicate values in unique
     * keys, and null values in NOT NULL columns. We also can't parse the
     * message as it will vary between databases.
     * 
     * <p>So the best we can do is to show the error message returned
     * by the database directly to the user, and hope that it describes the
     * problem and identifies the column on which it occurred.
     * 
     * @param operation The operation that was being attempted on the entity
     * as a verb, e.g. "save", "update", "delete"
     * @param entity The erroneous entity, e.g. a {@link VersionedRecord}. 
     * @param cause The {@link SQLException} that was thrown by
     * the database due to the invalid value of the record's column.
     */
    public MissingValue(String operation, Object entity, SQLException cause) {
        super(operation, entity, cause.getMessage(), cause);
    }

    /**
     * Constructor for wrapping a
     * {@link javax.validation.ConstraintViolationException}, which is
     * thrown by Hibernate Validation before saving when a {@link NotNull}
     * constraint is violated by an entity.
     * 
     * <p>We automatically generate a reasonable error message from the
     * {@link ConstraintViolation} embedded in the exception. 
     * 
     * @param operation The operation that was being attempted on the entity
     * as a verb, e.g. "save", "update", "delete"
     * @param entity The erroneous entity, e.g. a {@link VersionedRecord}. 
     * @param cause The {@link SQLException} that was thrown by
     * the database due to the invalid value of the record's column.
     */
    public MissingValue(String operation, Object entity, ConstraintViolationException cause) {
        super(operation, entity, cause);
    }

    public String getErrorField() {
        return ((PropertyValueException) getCause()).getPropertyName();
    }
}
