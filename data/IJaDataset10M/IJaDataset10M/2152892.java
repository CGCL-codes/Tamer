package net.sf.adatagenerator.ex.healthreg.bean;

import junit.framework.TestCase;
import net.sf.adatagenerator.api.FieldDependencyManager;
import net.sf.adatagenerator.api.GenerationException;
import net.sf.adatagenerator.core.DefaultFieldDependencyManager;
import net.sf.adatagenerator.ex.healthreg.bean.fields.MrnGenerator;
import net.sf.adatagenerator.ex.healthreg.util.MedicalRecordNumbers;

public class MrnGeneratorTest extends TestCase {

    public static final String FIELD_NAME = "nonsense";

    public void testMrnGeneratorStringFieldDependencyManager() {
        FieldDependencyManager<Object> fdm = new DefaultFieldDependencyManager<Object>();
        MrnGenerator<Object> idg = new MrnGenerator<Object>(fdm);
        try {
            String id = idg.generateValue(null);
            assertTrue(id != null);
            assertTrue(id.length() == MedicalRecordNumbers.STRING_LENGTH);
            for (int i = 1; i < id.length(); i++) {
                assertTrue(Character.isDigit(id.charAt(i)));
            }
        } catch (GenerationException e) {
            fail(e.toString());
        }
    }
}
