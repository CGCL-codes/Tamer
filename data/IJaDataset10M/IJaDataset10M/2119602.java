package test.rockon.fuzzy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import de.rockon.fuzzy.controller.model.FuzzyController;
import de.rockon.fuzzy.controller.model.FuzzyVariable;
import de.rockon.fuzzy.controller.model.enums.VariableType;
import de.rockon.fuzzy.controller.util.ValidationUtils;
import de.rockon.fuzzy.exceptions.RuleMissingValuesException;
import de.rockon.fuzzy.exceptions.ValueOutOfDomainException;

/**
 * Testet alle Methoden der FuzzyVariable
 */
public class FuzzyControllerTest extends TestCase {

    private FuzzyController fuzzySetContainer;

    private FuzzyVariable badeTemperaturVariable;

    private FuzzyVariable luftTemperaturSets;

    private ArrayList<FuzzyVariable> shouldBe;

    private ArrayList<FuzzyVariable> is;

    private ArrayList<FuzzyVariable> is2;

    /**
	 * Vorbedingungen erstellen
	 */
    @Override
    protected void setUp() throws Exception {
        fuzzySetContainer = TestDataFactory.generateTestContainer();
        badeTemperaturVariable = fuzzySetContainer.getChild(0);
        luftTemperaturSets = fuzzySetContainer.getChild(1);
        shouldBe = new ArrayList<FuzzyVariable>();
        shouldBe.add(new FuzzyVariable("Winkel", VariableType.INPUT, "Grad", new double[] { -90, 90 }));
        shouldBe.add(new FuzzyVariable("Winkelgeschwindigkeit", VariableType.INPUT, "Grad/Sekunde", new double[] { -90, 90 }));
        shouldBe.add(new FuzzyVariable("Kraft", VariableType.OUTPUT, "Newton", new double[] { -250, 250 }));
        is = new ArrayList<FuzzyVariable>();
        is.add(new FuzzyVariable("Winkel", VariableType.INPUT, "Grad", new double[] { -90, 90 }));
        is.add(new FuzzyVariable("Winkelgeschwindigkeit", VariableType.INPUT, "Grad/Sekunde", new double[] { -90, 90 }));
        is.add(new FuzzyVariable("Kraft", VariableType.OUTPUT, "Newton", new double[] { -250, 250 }));
        is2 = new ArrayList<FuzzyVariable>();
        is2.add(new FuzzyVariable("Winkel", VariableType.INPUT, "Farenheit", new double[] { -90, 90 }));
        is2.add(new FuzzyVariable("Winkelgeschwindigkeit", VariableType.INPUT, "Grad/Sekunde", new double[] { -90, 90 }));
        is2.add(new FuzzyVariable("Kraft", VariableType.OUTPUT, "Newton", new double[] { -250, 250 }));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Ignore("not today")
    @Test
    public void testHandleProcessInput() {
        HashMap<FuzzyVariable, Double> param = new HashMap<FuzzyVariable, Double>();
        param.put(badeTemperaturVariable, 20.0);
        param.put(luftTemperaturSets, 30.0);
        try {
            HashMap<FuzzyVariable, Double> output = fuzzySetContainer.handleProcessInput(param);
            for (Entry<FuzzyVariable, Double> entry : output.entrySet()) {
                FuzzyVariable parent = entry.getKey();
                Double value = entry.getValue();
                System.out.println(" " + parent + " " + value);
            }
        } catch (ValueOutOfDomainException e) {
            e.printStackTrace();
        } catch (RuleMissingValuesException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testVerifySignature() {
        Assert.assertTrue(ValidationUtils.verifySignatures(shouldBe, is));
        Assert.assertFalse(ValidationUtils.verifySignatures(shouldBe, is2));
    }
}
