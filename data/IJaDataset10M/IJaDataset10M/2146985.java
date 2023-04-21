package pcgen.persistence.lst.output.prereq;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.persistence.PersistenceLayerException;
import java.io.IOException;
import java.io.Writer;

public class PrerequisiteArmorProficiencyWriter extends AbstractPrerequisiteWriter implements PrerequisiteWriterInterface {

    public String kindHandled() {
        return "ARMORPROF";
    }

    public PrerequisiteOperator[] operatorsHandled() {
        return new PrerequisiteOperator[] { PrerequisiteOperator.GTEQ, PrerequisiteOperator.LT };
    }

    public void write(Writer writer, Prerequisite prereq) throws PersistenceLayerException {
        checkValidOperator(prereq, operatorsHandled());
        try {
            if (prereq.getOperator().equals(PrerequisiteOperator.LT)) {
                writer.write('!');
            }
            writer.write("PREARMORPROF:1,");
            writer.write(prereq.getKey());
        } catch (IOException e) {
            throw new PersistenceLayerException(e.getMessage());
        }
    }
}
