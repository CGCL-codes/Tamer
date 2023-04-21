package pcgen.persistence.lst.output.prereq;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.persistence.PersistenceLayerException;
import java.io.IOException;
import java.io.Writer;

public class PrerequisiteSpellBookWriter extends AbstractPrerequisiteWriter implements PrerequisiteWriterInterface {

    public String kindHandled() {
        return "spellbook";
    }

    public PrerequisiteOperator[] operatorsHandled() {
        return new PrerequisiteOperator[] { PrerequisiteOperator.EQ, PrerequisiteOperator.NEQ };
    }

    public void write(Writer writer, Prerequisite prereq) throws PersistenceLayerException {
        checkValidOperator(prereq, operatorsHandled());
        try {
            if (prereq.getOperator().equals(PrerequisiteOperator.NEQ)) {
                writer.write('!');
            }
            writer.write("PRESPELLBOOK:");
            writer.write(prereq.getKey());
        } catch (IOException e) {
            throw new PersistenceLayerException(e.getMessage());
        }
    }
}
