package plugin.pretokens.writer;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.output.prereq.AbstractPrerequisiteWriter;
import pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface;
import java.io.IOException;
import java.io.Writer;

public class PreHPWriter extends AbstractPrerequisiteWriter implements PrerequisiteWriterInterface {

    public String kindHandled() {
        return "HP";
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
            writer.write("PREHP:");
            writer.write(prereq.getOperand());
        } catch (IOException e) {
            throw new PersistenceLayerException(e.getMessage());
        }
    }
}
