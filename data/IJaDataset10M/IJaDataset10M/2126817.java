package org.databene.mad4db.cmd;

import org.databene.commons.ProgrammerError;
import org.databene.commons.xml.SimpleXMLWriter;
import org.databene.jdbacl.model.DBCheckConstraint;
import org.databene.jdbacl.model.DBConstraint;
import org.databene.jdbacl.model.DBForeignKeyConstraint;
import org.databene.jdbacl.model.DBUniqueConstraint;
import org.databene.mad4db.ChangeSeverity;

/**
 * {@link StructuralChange} implementation that represents the addition of table columns 
 * (which are already present in the according table) to a database constraint.<br/><br/>
 * Created: 25.05.2011 17:08:01
 * @since 0.1
 * @author Volker Bergmann
 */
public class ConstraintColumnsAddition extends ColumnsAddition<DBConstraint> {

    public ConstraintColumnsAddition(DBConstraint constraint, String... columnNames) {
        super(constraint, severity(constraint), columnNames);
    }

    private static ChangeSeverity severity(DBConstraint constraint) {
        if (constraint instanceof DBUniqueConstraint) return ChangeSeverity.EASE; else if (constraint instanceof DBForeignKeyConstraint) return ChangeSeverity.RESTRICTION; else if (constraint instanceof DBCheckConstraint) return ChangeSeverity.RESTRICTION; else throw new ProgrammerError("Unexpected constraint type: " + constraint);
    }

    @Override
    public boolean createCheck(SimpleXMLWriter writer) {
        return true;
    }
}
