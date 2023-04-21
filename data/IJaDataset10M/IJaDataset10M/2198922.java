package de.hu_berlin.sam.mmunit.coverage.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.junit.BeforeClass;
import org.junit.Test;
import de.hu_berlin.sam.mmunit.execution.MMTest;

public class TestGC {

    private static EPackage mmut;

    @BeforeClass
    public static void loadMetamodel() {
        ResourceSet resSet = new ResourceSetImpl();
        resSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
        URI mmutURI = URI.createFileURI(IModelFiles.BEHAVIORAL_STATE_MACHINES_ECORE);
        Resource mmutRes = resSet.getResource(mmutURI, true);
        mmut = (EPackage) mmutRes.getContents().get(0);
    }

    @Test
    public void test1() {
        Collection<EPackage> monitoredObjects = new ArrayList<EPackage>();
        monitoredObjects.add(mmut);
        ChangeRecorder recorder = new ChangeRecorder(monitoredObjects);
        EClass superClass = (EClass) mmut.getEClassifier("Behavior");
        EClass subClass = (EClass) mmut.getEClassifier("StateMachine");
        subClass.getESuperTypes().remove(superClass);
        MMTest test = new MMTest(mmut, IModelFiles.STATE_MACHINE_EXAMPLE_MMUNIT);
        boolean testResult = test.execute();
        assertTrue(test.getErrorMessage(), testResult);
        recorder.endRecording().apply();
    }

    @Test
    public void test2() {
        Collection<EPackage> monitoredObjects = new ArrayList<EPackage>();
        monitoredObjects.add(mmut);
        ChangeRecorder recorder = new ChangeRecorder(monitoredObjects);
        EClass superClass = (EClass) mmut.getEClassifier("Behavior");
        EClass subClass = (EClass) mmut.getEClassifier("StateMachine");
        subClass.getESuperTypes().remove(superClass);
        MMTest test = new MMTest(mmut, IModelFiles.STATE_MACHINE_EXAMPLE_MMUNIT);
        boolean testResult = test.execute();
        assertTrue(test.getErrorMessage(), testResult);
        recorder.endRecording().apply();
    }

    @Test
    public void removeUncoveredReference() {
        Collection<EPackage> monitoredObjects = new ArrayList<EPackage>();
        monitoredObjects.add(mmut);
        ChangeRecorder recorder = new ChangeRecorder(monitoredObjects);
        EClass refencingClass = (EClass) mmut.getEClassifier("State");
        EStructuralFeature feature = refencingClass.getEStructuralFeature("doActivity");
        refencingClass.getEStructuralFeatures().remove(feature);
        MMTest test = new MMTest(mmut, IModelFiles.STATE_MACHINE_EXAMPLE_GC_MMUNIT);
        boolean testResult = test.execute();
        assertTrue(test.getErrorMessage(), testResult);
        recorder.endRecording().apply();
    }

    @Test
    public void removeCoveredReference() {
        Collection<EPackage> monitoredObjects = new ArrayList<EPackage>();
        monitoredObjects.add(mmut);
        ChangeRecorder recorder = new ChangeRecorder(monitoredObjects);
        EClass refencingClass = (EClass) mmut.getEClassifier("State");
        EStructuralFeature feature = refencingClass.getEStructuralFeature("entry");
        refencingClass.getEStructuralFeatures().remove(feature);
        MMTest test = new MMTest(mmut, IModelFiles.STATE_MACHINE_EXAMPLE_GC_MMUNIT);
        boolean testResult = test.execute();
        assertFalse(test.getErrorMessage(), testResult);
        recorder.endRecording().apply();
    }

    @Test
    public void renamedCoveredReference() {
        Collection<EPackage> monitoredObjects = new ArrayList<EPackage>();
        monitoredObjects.add(mmut);
        ChangeRecorder recorder = new ChangeRecorder(monitoredObjects);
        EClass refencingClass = (EClass) mmut.getEClassifier("State");
        refencingClass.getEStructuralFeature("entry").setName("entryy");
        MMTest test = new MMTest(mmut, IModelFiles.STATE_MACHINE_EXAMPLE_GC_MMUNIT);
        boolean testResult = test.execute();
        assertTrue(test.getErrorMessage(), testResult);
        recorder.endRecording().apply();
    }
}
