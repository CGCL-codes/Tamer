package de.hu_berlin.sam.mmunit.coverage.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.preference.IPreferenceStore;
import org.junit.BeforeClass;
import org.junit.Test;
import de.hu_berlin.sam.mmunit.coverage.AnalyzeCriteriaLoader;
import de.hu_berlin.sam.mmunit.coverage.automation.Activator;
import de.hu_berlin.sam.mmunit.coverage.cgc.ContextGeneralizationCoverageCriterion;
import de.hu_berlin.sam.mmunit.coverage.criteria.CriterionDefinition;
import de.hu_berlin.sam.mmunit.coverage.gc.GeneralizationCoverageCriterion;
import de.hu_berlin.sam.mmunit.coverage.report.MMCoverageReport;
import de.hu_berlin.sam.mmunit.execution.MMTest;

/**
 * Demonstrates the functionality of GC ({@link GeneralizationCoverageCriterion}) and CGC ({@link ContextGeneralizationCoverageCriterion}) 
 * w.r.t. the following mutation operations:
 * 
 * 1. remove/rename subclass  
 * 2. <strike>remove/rename superclass</strike>
 * 3. remove inheritance 
 * 4. remove superclass referencing feature (CGC only) 
 *
 * @category Test
 * @author Silvio Pohl
 *
 */
public class TestStateMachineMC {

    private static final String OUTPUT_DIRECTORY = "output";

    private static EPackage mmut = null;

    @BeforeClass
    public static void initialize() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        List<CriterionDefinition> defList = AnalyzeCriteriaLoader.load();
        for (CriterionDefinition def : defList) {
            boolean onlyClassCoverage = def.getName().matches("(Negative )?(Context )?Multiplicity.*");
            store.setValue(def.getName(), onlyClassCoverage);
            TestUtils.delete(OUTPUT_DIRECTORY + "/" + def.getName().replaceAll(" ", "_") + ".coverage");
        }
        MMTest test1 = new MMTest(IModelFiles.BEHAVIORAL_STATE_MACHINES_ECORE, IModelFiles.STATE_MACHINE_MMUNIT);
        mmut = test1.getMetamodel();
        assertTrue(test1.getErrorMessage(), test1.execute());
        MMTest test2 = new MMTest(IModelFiles.BEHAVIORAL_STATE_MACHINES_ECORE, IModelFiles.STATE_MACHINE_UPPER_BOUND_MMUNIT);
        assertTrue(test2.getErrorMessage(), test2.execute());
        MMTest test3 = new MMTest(IModelFiles.BEHAVIORAL_STATE_MACHINES_ECORE, IModelFiles.STATE_MACHINE_UPPER_BOUND_MMUNIT);
        assertTrue(test3.getErrorMessage(), test3.execute());
        MMCoverageReport.updateCoverage(OUTPUT_DIRECTORY);
    }

    @Test
    public void testRenameFeature() {
        Collection<EPackage> monitoredObjects = new ArrayList<EPackage>();
        monitoredObjects.add(mmut);
        ChangeRecorder recorder = new ChangeRecorder(monitoredObjects);
        EClass clazz = (EClass) mmut.getEClassifier("StateMachine");
        EStructuralFeature feature = clazz.getEStructuralFeature("region");
        feature.setName("regionn");
        MMTest test = new MMTest(mmut, IModelFiles.STATE_MACHINE_MMUNIT);
        assertFalse(test.getErrorMessage(), test.execute());
        recorder.endRecording().apply();
        MMTest.getExecutionLog().remove(test);
    }

    @Test
    public void testRemoveFeature() {
        Collection<EPackage> monitoredObjects = new ArrayList<EPackage>();
        monitoredObjects.add(mmut);
        ChangeRecorder recorder = new ChangeRecorder(monitoredObjects);
        EClass clazz = (EClass) mmut.getEClassifier("StateMachine");
        EStructuralFeature feature = clazz.getEStructuralFeature("region");
        clazz.getEStructuralFeatures().remove(feature);
        MMTest test = new MMTest(mmut, IModelFiles.STATE_MACHINE_MMUNIT);
        assertFalse(test.getErrorMessage(), test.execute());
        recorder.endRecording().apply();
        MMTest.getExecutionLog().remove(test);
    }

    @Test
    public void testDecreaseLowerBound() {
        EPackage newMmut = (EPackage) EcoreUtil.copy(mmut);
        EClass clazz = (EClass) newMmut.getEClassifier("Transition");
        EReference reference = (EReference) clazz.getEStructuralFeature("source");
        reference.setLowerBound(0);
        MMTest test = new MMTest(newMmut, IModelFiles.STATE_MACHINE_LOWER_BOUND_MMUNIT);
        assertFalse(test.getErrorMessage(), test.execute());
        MMTest.getExecutionLog().remove(test);
    }

    @Test
    public void testIncreaseUpperBound() throws IOException {
        EPackage newMmut = (EPackage) EcoreUtil.copy(mmut);
        EClass clazz = (EClass) newMmut.getEClassifier("Transition");
        EReference reference = (EReference) clazz.getEStructuralFeature("source");
        reference.setUpperBound(2);
        assertTrue("Reference is not a collection.", reference.isMany());
        MMTest test = new MMTest(newMmut, IModelFiles.STATE_MACHINE_UPPER_BOUND_MMUNIT);
        assertFalse(test.getErrorMessage(), test.execute());
        MMTest.getExecutionLog().remove(test);
    }

    @Test
    public void testIncreaseUpperBound2() throws IOException {
        EPackage newMmut = (EPackage) EcoreUtil.copy(mmut);
        EClass clazz = (EClass) newMmut.getEClassifier("Transition");
        EReference reference = (EReference) clazz.getEStructuralFeature("source");
        reference.setUpperBound(3);
        assertTrue("Reference is not a collection.", reference.isMany());
        MMTest test = new MMTest(newMmut, IModelFiles.STATE_MACHINE_UPPER_BOUND_MMUNIT);
        assertFalse(test.getErrorMessage(), test.execute());
        MMTest.getExecutionLog().remove(test);
    }

    @Test
    public void testIncreaseLowerBound() {
        EPackage newMmut = (EPackage) EcoreUtil.copy(mmut);
        EClass clazz = (EClass) newMmut.getEClassifier("StateMachine");
        EReference reference = (EReference) clazz.getEStructuralFeature("region");
        reference.setLowerBound(2);
        MMTest test = new MMTest(newMmut, IModelFiles.STATE_MACHINE_LOWER_BOUND_MMUNIT);
        assertFalse(test.getErrorMessage(), test.execute());
        MMTest.getExecutionLog().remove(test);
    }

    @Test
    public void testDecreaseUpperBound() {
        EPackage newMmut = (EPackage) EcoreUtil.copy(mmut);
        EClass clazz = (EClass) newMmut.getEClassifier("StateMachine");
        EReference reference = (EReference) clazz.getEStructuralFeature("region");
        reference.setUpperBound(4);
        MMTest test = new MMTest(newMmut, IModelFiles.STATE_MACHINE_DECREASE_UPPER_BOUND_MMUNIT);
        assertFalse(test.getErrorMessage(), test.execute());
        MMTest.getExecutionLog().remove(test);
    }
}
