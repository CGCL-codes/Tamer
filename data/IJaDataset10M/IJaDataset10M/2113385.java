package net.groovysips.jdiff;

import java.util.Date;
import junit.framework.TestCase;
import net.groovysips.jdiff.delta.DefaultDeltaBuilder;
import net.groovysips.jdiff.delta.DeltaPrinter;
import net.groovysips.jdiff.delta.NullReturnDelta;
import net.groovysips.jdiff.delta.VisitingDeltaMerger;
import net.groovysips.jdiff.delta.MappedFinderCriteriaFactory;
import net.groovysips.jdiff.delta.MappedItemAppenderFactory;

public class SimpleObjectDiffTests extends TestCase {

    private DeltaCalculationService dcs;

    protected void setUp() {
        DefaultDeltaBuilder dBuilder = new DefaultDeltaBuilder();
        dBuilder.setFinderCriteriaFactory(new MappedFinderCriteriaFactory());
        DeltaMerger dMerger = new VisitingDeltaMerger(new MappedItemAppenderFactory());
        dcs = new DefaultDeltaCalculationService(dBuilder, dMerger);
    }

    public void testGraphDelta() {
        Person original = createSingleObject();
        Person modified = createSingleObject();
        modified.setName("Viktor Orban");
        modified.setDob(new Date(1973, 3, 26));
        modified.setSsn("000-00-0001");
        modified.setYearsInSchool(21);
        Delta delta = dcs.diff(original, modified);
        delta.visit(new DeltaPrinter(System.err));
        Object result = dcs.apply(original, delta);
        assertTrue(result.equals(modified));
    }

    public void testDeltaVisitor() {
        Person original = createSingleObject();
        Person modified = createSingleObject();
        modified.setName("Viktor Orban");
        modified.setDob(new Date(1973, 3, 26));
        modified.setSsn("000-00-0001");
        modified.setYearsInSchool(21);
        modified.setSpouse(createSpouse());
        Delta delta = dcs.diff(original, modified);
        delta.visit(new DeltaPrinter(System.err));
        Object result = dcs.apply(original, delta);
        assertTrue(result.equals(modified));
    }

    public void testNullOriginalGraphDelta() {
        Person original = null;
        Person modified = createMarriedWithFirstChildObject();
        Delta delta = dcs.diff(original, modified);
        delta.visit(new DeltaPrinter(System.err));
        Object result = dcs.apply(original, delta);
        System.err.println(result);
        assertTrue(result.equals(modified));
    }

    public void testNullOriginalGraphDelta_1() {
        Person original = null;
        Person modified = createMarriedObject();
        Delta delta = dcs.diff(original, modified);
        delta.visit(new DeltaPrinter(System.err));
        Object result = dcs.apply(original, delta);
        System.err.println(result);
        assertTrue(result.equals(modified));
    }

    public void testNullReturnDelta() {
        Person original = createSingleObject();
        Delta delta = dcs.diff(original, null);
        assertTrue(delta instanceof NullReturnDelta);
        NullReturnDelta nrDelta = (NullReturnDelta) delta;
        assertNull(nrDelta.getPropertyName());
        Object result = dcs.apply(original, nrDelta);
        assertNull(result);
    }

    public void testNullReturnValueEmbedded() {
        Person original = createMarriedObject();
        Person modified = createSingleObject();
        Delta delta = dcs.diff(original, modified);
        delta.visit(new DeltaPrinter(System.err));
        dcs.apply(modified, delta);
        assertNull(((Person) modified).getSpouse());
    }

    private Person createSingleObject() {
        Person result = new Person();
        result.setDob(new Date());
        result.setName("Alex Shneyderman");
        result.setSsn(null);
        result.setYearsInSchool(23);
        return result;
    }

    private Person createSpouse() {
        Person result = createSingleObject();
        result.setName("Spouse");
        result.setYearsInSchool(52);
        return result;
    }

    private Person createMarriedObject() {
        Person result = createSingleObject();
        Person spouse = createSpouse();
        result.setSpouse(spouse);
        return result;
    }

    private Person createMutuallyMarriedObject() {
        Person result = createSingleObject();
        Person spouse = createSpouse();
        result.setSpouse(spouse);
        spouse.setSpouse(result);
        return result;
    }

    private Person createMarriedWithFirstChildObject() {
        Person result = createSingleObject();
        Person spouse = createSpouse();
        Person firstChild = createSingleObject();
        result.setSpouse(spouse);
        firstChild.setName("First-born");
        result.setFirstChild(firstChild);
        spouse.setFirstChild(firstChild);
        return result;
    }

    private Person createWithKids() {
        Person result = new Person();
        result.setDob(new Date());
        result.setName("Alex Shneyderman");
        result.setSsn(null);
        result.setYearsInSchool(23);
        return result;
    }
}
