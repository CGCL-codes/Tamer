package plugin.pretokens.writer;

import pcgen.core.prereq.Prerequisite;
import pcgen.core.prereq.PrerequisiteOperator;
import pcgen.persistence.PersistenceLayerException;
import pcgen.persistence.lst.output.prereq.AbstractPrerequisiteWriter;
import pcgen.persistence.lst.output.prereq.PrerequisiteWriterInterface;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class PreSpellSchoolWriter extends AbstractPrerequisiteWriter implements PrerequisiteWriterInterface {

    public String kindHandled() {
        return "spell.school";
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
            writer.write("PRESPELLSCHOOL:");
            writer.write(prereq.getOperand());
            writer.write(',');
            writer.write(prereq.getKey());
            writer.write('=');
            writer.write(prereq.getSubKey());
        } catch (IOException e) {
            throw new PersistenceLayerException(e.getMessage());
        }
    }

    public boolean specialCase(Writer writer, Prerequisite prereq) throws IOException {
        if (checkForPremultOfKind(prereq, kindHandled(), true)) {
            if (prereq.getOperator().equals(PrerequisiteOperator.LT)) {
                writer.write('!');
            }
            writer.write("PRESPELLSCHOOL:");
            writer.write(prereq.getOperand());
            for (Iterator iter = prereq.getPrerequisites().iterator(); iter.hasNext(); ) {
                final Prerequisite element = (Prerequisite) iter.next();
                writer.write(',');
                writer.write(element.getKey());
                writer.write('=');
                writer.write(element.getSubKey());
            }
            return true;
        }
        return false;
    }
}
