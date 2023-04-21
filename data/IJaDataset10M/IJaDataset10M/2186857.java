package agitar.uk.ac.ebi.intact.modelt;

import uk.ac.ebi.intact.model.*;
import com.agitar.lib.junit.AgitarTestCase;
import com.agitar.lib.mockingbird.Mockingbird;
import uk.ac.ebi.intact.model.util.AnnotatedObjectUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class InteractionImplAgitarTest extends AgitarTestCase {

    static Class TARGET_CLASS = InteractionImpl.class;

    public void testConstructor() throws Throwable {
        CvInteractionType type = new CvInteractionType(null, "testInteractionImplShortLabel");
        Institution owner = new Institution("testInteractionImplShortLabel");
        Collection experiments = new ArrayList(100);
        InteractionImpl interactionImpl = new InteractionImpl(experiments, type, "testInteractionImplShortLabel", owner);
        assertEquals("interactionImpl.xrefs.size()", 0, interactionImpl.xrefs.size());
        assertEquals("interactionImpl.getAliases().size()", 0, interactionImpl.getAliases().size());
        assertEquals("interactionImpl.getEvidences().size()", 0, interactionImpl.getEvidences().size());
        assertEquals("interactionImpl.shortLabel", "testInteractionImplS", interactionImpl.getShortLabel());
        assertEquals("interactionImpl.getComponents().size()", 0, interactionImpl.getComponents().size());
        assertSame("interactionImpl.getExperiments()", experiments, interactionImpl.getExperiments());
        assertSame("interactionImpl.getCvInteractionType()", type, interactionImpl.getCvInteractionType());
        assertEquals("interactionImpl.annotations.size()", 0, interactionImpl.annotations.size());
        assertSame("interactionImpl.getOwner()", owner, interactionImpl.getOwner());
        assertEquals("interactionImpl.references.size()", 0, interactionImpl.references.size());
        assertEquals("interactionImpl.getActiveInstances().size()", 0, interactionImpl.getActiveInstances().size());
        assertNull("interactionImpl.getCvInteractorType()", interactionImpl.getCvInteractorType());
    }

    public void testConstructor1() throws Throwable {
        Institution owner = new Institution("testInteractionImplShortLabel");
        CvInteractionType type = new CvInteractionType(owner, "testInteractionImplShortLabel");
        Collection experiments = new ArrayList(100);
        Collection components = new ArrayList(1000);
        CvInteractorType interactorType = new CvInteractorType(new Institution("testInteractionImplShortLabel1"), "testInteractionImplShortLabel");
        InteractionImpl interactionImpl = new InteractionImpl(experiments, components, type, interactorType, "testInteractionImplShortLabel", owner);
        assertEquals("interactionImpl.xrefs.size()", 0, interactionImpl.xrefs.size());
        assertEquals("interactionImpl.getAliases().size()", 0, interactionImpl.getAliases().size());
        assertEquals("interactionImpl.getEvidences().size()", 0, interactionImpl.getEvidences().size());
        assertEquals("interactionImpl.shortLabel", "testInteractionImplS", interactionImpl.getShortLabel());
        assertSame("interactionImpl.getComponents()", components, interactionImpl.getComponents());
        assertSame("interactionImpl.getExperiments()", experiments, interactionImpl.getExperiments());
        assertSame("interactionImpl.getCvInteractionType()", type, interactionImpl.getCvInteractionType());
        assertEquals("interactionImpl.annotations.size()", 0, interactionImpl.annotations.size());
        assertSame("interactionImpl.getOwner()", owner, interactionImpl.getOwner());
        assertEquals("interactionImpl.references.size()", 0, interactionImpl.references.size());
        assertEquals("interactionImpl.getActiveInstances().size()", 0, interactionImpl.getActiveInstances().size());
        assertSame("interactionImpl.getCvInteractorType()", interactorType, interactionImpl.getCvInteractorType());
    }

    public void testConstructor2() throws Throwable {
        Institution owner = new Institution("testInteractionImplShortLabel1");
        CvInteractorType interactorType = new CvInteractorType(new Institution("testInteractionImplShortLabel"), "testInteractionImplShortLabel");
        Collection experiments = new ArrayList(100);
        CvInteractionType type = new CvInteractionType(new Institution("testInteractionImplShortLabel2"), "testInteractionImplShortLabel");
        InteractionImpl interactionImpl = new InteractionImpl(experiments, type, interactorType, "testInteractionImplShortLabel", owner);
        assertEquals("interactionImpl.xrefs.size()", 0, interactionImpl.xrefs.size());
        assertEquals("interactionImpl.getAliases().size()", 0, interactionImpl.getAliases().size());
        assertEquals("interactionImpl.getEvidences().size()", 0, interactionImpl.getEvidences().size());
        assertEquals("interactionImpl.shortLabel", "testInteractionImplS", interactionImpl.getShortLabel());
        assertSame("interactionImpl.getExperiments()", experiments, interactionImpl.getExperiments());
        assertEquals("interactionImpl.getComponents().size()", 0, interactionImpl.getComponents().size());
        assertSame("interactionImpl.getCvInteractionType()", type, interactionImpl.getCvInteractionType());
        assertEquals("interactionImpl.annotations.size()", 0, interactionImpl.annotations.size());
        assertSame("interactionImpl.getOwner()", owner, interactionImpl.getOwner());
        assertEquals("interactionImpl.references.size()", 0, interactionImpl.references.size());
        assertSame("interactionImpl.getCvInteractorType()", interactorType, interactionImpl.getCvInteractorType());
        assertEquals("interactionImpl.getActiveInstances().size()", 0, interactionImpl.getActiveInstances().size());
    }

    public void testConstructor3() throws Throwable {
        Collection components = new ArrayList(100);
        Collection experiments = new ArrayList(1000);
        Institution owner = new Institution("testInteractionImplShortLabel");
        CvInteractionType type = new CvInteractionType(owner, "testInteractionImplShortLabel");
        InteractionImpl interactionImpl = new InteractionImpl(experiments, components, type, "testInteractionImplShortLabel", owner);
        assertEquals("interactionImpl.xrefs.size()", 0, interactionImpl.xrefs.size());
        assertEquals("interactionImpl.getAliases().size()", 0, interactionImpl.getAliases().size());
        assertEquals("interactionImpl.getEvidences().size()", 0, interactionImpl.getEvidences().size());
        assertEquals("interactionImpl.shortLabel", "testInteractionImplS", interactionImpl.getShortLabel());
        assertSame("interactionImpl.getExperiments()", experiments, interactionImpl.getExperiments());
        assertSame("interactionImpl.getComponents()", components, interactionImpl.getComponents());
        assertSame("interactionImpl.getCvInteractionType()", type, interactionImpl.getCvInteractionType());
        assertEquals("interactionImpl.annotations.size()", 0, interactionImpl.annotations.size());
        assertSame("interactionImpl.getOwner()", owner, interactionImpl.getOwner());
        assertEquals("interactionImpl.references.size()", 0, interactionImpl.references.size());
        assertNull("interactionImpl.getCvInteractorType()", interactionImpl.getCvInteractorType());
        assertEquals("interactionImpl.getActiveInstances().size()", 0, interactionImpl.getActiveInstances().size());
    }

    public void testClone() throws Throwable {
        Collection experiments = (Collection) Mockingbird.getProxyObject(Collection.class);
        InteractionImpl interactionImpl = new InteractionImpl(experiments, (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class), "testInteractionImplShortLabel", (Institution) Mockingbird.getProxyObject(Institution.class));
        Mockingbird.enterTestMode();
        InteractionImpl result = (InteractionImpl) interactionImpl.clone();
        assertNull("result.getCvInteractionTypeAc()", result.getCvInteractionTypeAc());
        assertEquals("interactionImpl.shortLabel", "testInteractionImplS", interactionImpl.getShortLabel());
        assertEquals("interactionImpl.getComponents().size()", 0, interactionImpl.getComponents().size());
        assertSame("interactionImpl.getExperiments()", experiments, interactionImpl.getExperiments());
        assertEquals("interactionImpl.annotations.size()", 0, interactionImpl.annotations.size());
    }

    public void testEquals() throws Throwable {
        InteractionImpl interactionImpl = new InteractionImpl(new ArrayList(), (CvInteractionType) null, new CvInteractorType(new Institution("testInteractionImplShortLabel"), "testInteractionImplShortLabel"), "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel1"));
        boolean result = interactionImpl.equals("");
        assertFalse("result", result);
        assertEquals("interactionImpl.getActiveInstances().size()", 0, interactionImpl.getActiveInstances().size());
    }

    public void testEquals1() throws Throwable {
        InteractionImpl interactionImpl = new InteractionImpl(new ArrayList(100), new CvInteractionType(null, "testInteractionImplShortLabel"), new CvInteractorType(new Institution("testInteractionImplShortLabel"), "testInteractionImplShortLabel"), "testString", new Institution("testInteractionImplShortLabel1"));
        boolean result = interactionImpl.equals(new InteractionImpl(new ArrayList(1000), new CvInteractionType(null, "testInteractionImplShortLabel1"), "testString", null));
        assertTrue("result", result);
        assertEquals("interactionImpl.getActiveInstances().size()", 0, interactionImpl.getActiveInstances().size());
    }

    public void testGetBait() throws Throwable {
        Collection components = new ArrayList(1000);
        InteractionImpl interactionImpl = new InteractionImpl(new ArrayList(100), components, new CvInteractionType(new Institution("testInteractionImplShortLabel"), "testInteractionImplShortLabel"), new CvInteractorType(new Institution("testInteractionImplShortLabel1"), "testInteractionImplShortLabel"), "testInteractionImplShortLabel", null);
        Component result = interactionImpl.getBait();
        assertNull("result", result);
        assertSame("interactionImpl.getComponents()", components, interactionImpl.getComponents());
    }

    public void testHashCode() throws Throwable {
        CvInteractionType type = new CvInteractionType(null, "testInteractionImplShortLabel");
        InteractionImpl interactionImpl = new InteractionImpl(new ArrayList(100), type, "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel"));
        int result = interactionImpl.hashCode();
        assertEquals("result", -675099413, result);
        assertEquals("interactionImpl.xrefs.size()", 0, interactionImpl.xrefs.size());
        assertNull("interactionImpl.getBioSource()", interactionImpl.getBioSource());
        assertEquals("interactionImpl.shortLabel", "testInteractionImplS", interactionImpl.getShortLabel());
        assertSame("interactionImpl.getCvInteractionType()", type, interactionImpl.getCvInteractionType());
        assertNull("interactionImpl.ac", interactionImpl.getAc());
        assertNull("interactionImpl.fullName", interactionImpl.getFullName());
        assertNull("interactionImpl.getCvInteractorType()", interactionImpl.getCvInteractorType());
    }

    public void testHashCode1() throws Throwable {
        InteractionImpl interactionImpl = new InteractionImpl((Collection) Mockingbird.getProxyObject(Collection.class), null, "testInteractionImplShortLabel", (Institution) Mockingbird.getProxyObject(Institution.class));
        interactionImpl.setKD(new Float(0.0F));
        Mockingbird.enterTestMode();
        int result = interactionImpl.hashCode();
        assertEquals("result", 696940332, result);
        assertEquals("interactionImpl.xrefs.size()", 0, interactionImpl.xrefs.size());
        assertNull("interactionImpl.getBioSource()", interactionImpl.getBioSource());
        assertEquals("interactionImpl.shortLabel", "testInteractionImplS", interactionImpl.getShortLabel());
        assertNull("interactionImpl.ac", interactionImpl.getAc());
        assertNull("interactionImpl.fullName", interactionImpl.getFullName());
        assertNull("interactionImpl.getCvInteractorType()", interactionImpl.getCvInteractorType());
    }

    public void testRemoveComponent1() throws Throwable {
        InteractionImpl interactionImpl = new InteractionImpl((Collection) Mockingbird.getProxyObject(Collection.class), (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class), "testInteractionImplShortLabel", (Institution) Mockingbird.getProxyObject(Institution.class));
        Component component = (Component) Mockingbird.getProxyObject(Component.class);
        Mockingbird.enterTestMode();
        interactionImpl.removeComponent(component);
        assertEquals("interactionImpl.getComponents().size()", 0, interactionImpl.getComponents().size());
    }

    public void testSetComponents() throws Throwable {
        InteractionImpl interactionImpl = new InteractionImpl(new ArrayList(100), new ArrayList(1000), new CvInteractionType(new Institution("testInteractionImplShortLabel"), "testInteractionImplShortLabel"), "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel1"));
        Collection someComponent = new ArrayList(0);
        interactionImpl.setComponents(someComponent);
        assertSame("interactionImpl.getComponents()", someComponent, interactionImpl.getComponents());
    }

    public void testSetCvInteractionType() throws Throwable {
        InteractionImpl interactionImpl = new InteractionImpl((Collection) Mockingbird.getProxyObject(Collection.class), (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class), "testInteractionImplShortLabel", (Institution) Mockingbird.getProxyObject(Institution.class));
        CvInteractionType cvInteractionType = (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class);
        Mockingbird.enterTestMode();
        interactionImpl.setCvInteractionType(cvInteractionType);
        assertSame("interactionImpl.getCvInteractionType()", cvInteractionType, interactionImpl.getCvInteractionType());
    }

    public void testSetCvInteractionTypeAc() throws Throwable {
        Collection experiments = (Collection) Mockingbird.getProxyObject(Collection.class);
        InteractionImpl interactionImpl = new InteractionImpl(experiments, (Collection) Mockingbird.getProxyObject(Collection.class), (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class), "testInteractionImplShortLabel", (Institution) Mockingbird.getProxyObject(Institution.class));
        Mockingbird.enterTestMode();
        interactionImpl.setCvInteractionTypeAc("testInteractionImplAc");
        assertEquals("interactionImpl.getCvInteractionTypeAc()", "testInteractionImplAc", interactionImpl.getCvInteractionTypeAc());
    }

    public void testSetExperiments() throws Throwable {
        Collection someExperiment = new ArrayList(100);
        InteractionImpl interactionImpl = new InteractionImpl(new ArrayList(1000), new ArrayList(0), new CvInteractionType(new Institution("testInteractionImplShortLabel"), "testInteractionImplShortLabel"), "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel1"));
        interactionImpl.setExperiments(someExperiment);
        assertSame("interactionImpl.getExperiments()", someExperiment, interactionImpl.getExperiments());
    }

    public void testSetKD() throws Throwable {
        InteractionImpl interactionImpl = new InteractionImpl((Collection) Mockingbird.getProxyObject(Collection.class), (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class), "testInteractionImplShortLabel", (Institution) Mockingbird.getProxyObject(Institution.class));
        Mockingbird.enterTestMode();
        interactionImpl.setKD(new Float(1.0F));
        assertEquals("interactionImpl.getKD()", 1.0F, interactionImpl.getKD().floatValue(), 1.0E-6F);
    }

    public void testSetReleased() throws Throwable {
        Collection experiments = (Collection) Mockingbird.getProxyObject(Collection.class);
        InteractionImpl interactionImpl = new InteractionImpl(experiments, (Collection) Mockingbird.getProxyObject(Collection.class), null, (CvInteractorType) Mockingbird.getProxyObject(CvInteractorType.class), "testInteractionImplShortLabel", (Institution) Mockingbird.getProxyObject(Institution.class));
        Collection someReleased = (Collection) Mockingbird.getProxyObject(Collection.class);
        Mockingbird.enterTestMode();
        interactionImpl.setReleased(someReleased);
        assertSame("interactionImpl.getReleased()", someReleased, interactionImpl.getReleased());
    }

    public void testConstructorThrowsIllegalArgumentException() throws Throwable {
        Collection experiments = (Collection) Mockingbird.getProxyObject(Collection.class);
        CvInteractionType type = (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class);
        Institution owner = (Institution) Mockingbird.getProxyObject(Institution.class);
        Mockingbird.enterTestMode();
        try {
            new InteractionImpl(experiments, type, "", owner);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Must define a non empty short label", ex.getMessage());
            assertThrownBy(AnnotatedObjectUtils.class, ex);
        }
    }

    public void testConstructorThrowsIllegalArgumentException1() throws Throwable {
        Collection components = (Collection) Mockingbird.getProxyObject(Collection.class);
        CvInteractionType type = (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class);
        CvInteractorType interactorType = (CvInteractorType) Mockingbird.getProxyObject(CvInteractorType.class);
        Institution owner = (Institution) Mockingbird.getProxyObject(Institution.class);
        Mockingbird.enterTestMode();
        try {
            new InteractionImpl(null, components, type, interactorType, "", owner);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Must define a non empty short label", ex.getMessage());
            assertThrownBy(AnnotatedObjectUtils.class, ex);
        }
    }

    public void testConstructorThrowsIllegalArgumentException2() throws Throwable {
        Collection experiments = (Collection) Mockingbird.getProxyObject(Collection.class);
        CvInteractionType type = (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class);
        CvInteractorType interactorType = (CvInteractorType) Mockingbird.getProxyObject(CvInteractorType.class);
        Institution owner = (Institution) Mockingbird.getProxyObject(Institution.class);
        Mockingbird.enterTestMode();
        try {
            new InteractionImpl(experiments, type, interactorType, "", owner);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Must define a non empty short label", ex.getMessage());
            assertThrownBy(AnnotatedObjectUtils.class, ex);
        }
    }

    public void testConstructorThrowsIllegalArgumentException3() throws Throwable {
        Collection experiments = (Collection) Mockingbird.getProxyObject(Collection.class);
        Collection components = (Collection) Mockingbird.getProxyObject(Collection.class);
        CvInteractionType type = (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class);
        Institution owner = (Institution) Mockingbird.getProxyObject(Institution.class);
        Mockingbird.enterTestMode();
        try {
            new InteractionImpl(experiments, components, type, "", owner);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("ex.getMessage()", "Must define a non empty short label", ex.getMessage());
            assertThrownBy(AnnotatedObjectUtils.class, ex);
        }
    }

    public void testConstructorThrowsNullPointerException() throws Throwable {
        try {
            new InteractionImpl(null, new CvInteractionType(new Institution("testInteractionImplShortLabel1"), "testInteractionImplShortLabel"), "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel"));
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("ex.getMessage()", "Cannot create an Interaction without an Experiment!", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
        }
    }

    public void testConstructorThrowsNullPointerException1() throws Throwable {
        Collection experiments = (Collection) Mockingbird.getProxyObject(Collection.class);
        Collection components = (Collection) Mockingbird.getProxyObject(Collection.class);
        CvInteractionType type = (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class);
        CvInteractorType interactorType = (CvInteractorType) Mockingbird.getProxyObject(CvInteractorType.class);
        Institution owner = (Institution) Mockingbird.getProxyObject(Institution.class);
        Mockingbird.enterTestMode();
        try {
            new InteractionImpl(experiments, components, type, interactorType, null, owner);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("ex.getMessage()", "Must define a non null short label", ex.getMessage());
            assertThrownBy(AnnotatedObjectUtils.class, ex);
        }
    }

    public void testConstructorThrowsNullPointerException2() throws Throwable {
        Collection components = (Collection) Mockingbird.getProxyObject(Collection.class);
        CvInteractionType type = (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class);
        CvInteractorType interactorType = (CvInteractorType) Mockingbird.getProxyObject(CvInteractorType.class);
        Institution owner = (Institution) Mockingbird.getProxyObject(Institution.class);
        Mockingbird.enterTestMode();
        try {
            new InteractionImpl(null, components, type, interactorType, "testInteractionImplShortLabel", owner);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("ex.getMessage()", "Cannot create an Interaction without an Experiment!", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
        }
    }

    public void testConstructorThrowsNullPointerException3() throws Throwable {
        Collection experiments = (Collection) Mockingbird.getProxyObject(Collection.class);
        CvInteractionType type = (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class);
        CvInteractorType interactorType = (CvInteractorType) Mockingbird.getProxyObject(CvInteractorType.class);
        Institution owner = (Institution) Mockingbird.getProxyObject(Institution.class);
        Mockingbird.enterTestMode();
        try {
            new InteractionImpl(experiments, null, type, interactorType, "testInteractionImplShortLabel", owner);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("ex.getMessage()", "Cannot create an Interaction without any Components!", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
        }
    }

    public void testConstructorThrowsNullPointerException4() throws Throwable {
        CvInteractionType type = (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class);
        CvInteractorType interactorType = (CvInteractorType) Mockingbird.getProxyObject(CvInteractorType.class);
        Institution owner = (Institution) Mockingbird.getProxyObject(Institution.class);
        Mockingbird.enterTestMode();
        try {
            new InteractionImpl((Collection) null, type, interactorType, "testInteractionImplShortLabel", owner);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("ex.getMessage()", "Cannot create an Interaction without an Experiment!", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
        }
    }

    public void testConstructorThrowsNullPointerException5() throws Throwable {
        Institution owner = new Institution("testInteractionImplShortLabel");
        try {
            new InteractionImpl((Collection) null, new ArrayList(100), new CvInteractionType(owner, "testInteractionImplShortLabel"), "testInteractionImplShortLabel", owner);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("ex.getMessage()", "Cannot create an Interaction without an Experiment!", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
        }
    }

    public void testAddComponentThrowsNullPointerException() throws Throwable {
        Collection components = new ArrayList(1000);
        InteractionImpl interactionImpl = new InteractionImpl(new ArrayList(100), components, new CvInteractionType(new Institution("testInteractionImplShortLabel"), "testInteractionImplShortLabel"), "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel1"));
        try {
            interactionImpl.addComponent(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertSame("interactionImpl.getComponents()", components, interactionImpl.getComponents());
            assertNull("interactionImpl.getComponents().get(0)", ((List) interactionImpl.getComponents()).get(0));
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
        }
    }

    public void testAddReleasedThrowsNullPointerException() throws Throwable {
        InteractionImpl interactionImpl = new InteractionImpl(new ArrayList(100), new ArrayList(1000), new CvInteractionType(new Institution("testInteractionImplShortLabel"), "testInteractionImplShortLabel"), "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel1"));
        try {
            interactionImpl.addReleased(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
            assertNull("interactionImpl.getReleased()", interactionImpl.getReleased());
        }
    }

    public void testCloneThrowsNullPointerException() throws Throwable {
        Collection experiments = new ArrayList(100);
        Collection components = new ArrayList(1000);
        InteractionImpl interactionImpl = new InteractionImpl(experiments, components, new CvInteractionType(new Institution("testInteractionImplShortLabel"), "testInteractionImplShortLabel"), "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel1"));
        interactionImpl.addAnnotation(null);
        try {
            interactionImpl.clone();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(AnnotatedObjectImpl.class, ex);
            assertEquals("interactionImpl.shortLabel", "testInteractionImplS", interactionImpl.getShortLabel());
            assertSame("interactionImpl.getComponents()", components, interactionImpl.getComponents());
            assertSame("interactionImpl.getExperiments()", experiments, interactionImpl.getExperiments());
            assertEquals("interactionImpl.annotations.size()", 1, interactionImpl.annotations.size());
        }
    }

    public void testRemoveComponentThrowsNullPointerException() throws Throwable {
        Collection someComponent = new ArrayList(100);
        someComponent.add(null);
        InteractionImpl interactionImpl = new InteractionImpl(new ArrayList(1000), new ArrayList(0), new CvInteractionType(null, "testInteractionImplShortLabel"), "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel"));
        interactionImpl.setComponents(someComponent);
        try {
            interactionImpl.removeComponent(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertSame("interactionImpl.getComponents()", someComponent, interactionImpl.getComponents());
            assertFalse("interactionImpl.getComponents().contains(null)", interactionImpl.getComponents().contains(null));
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
        }
    }

    public void testRemoveReleasedThrowsNullPointerException1() throws Throwable {
        InteractionImpl interactionImpl = new InteractionImpl((Collection) Mockingbird.getProxyObject(Collection.class), (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class), "testInteractionImplShortLabel", (Institution) Mockingbird.getProxyObject(Institution.class));
        Mockingbird.enterTestMode();
        try {
            interactionImpl.removeReleased(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
            assertNull("interactionImpl.getReleased()", interactionImpl.getReleased());
        }
    }

    public void testSetComponentsThrowsNullPointerException() throws Throwable {
        Collection experiments = (Collection) Mockingbird.getProxyObject(Collection.class);
        Collection components = (Collection) Mockingbird.getProxyObject(Collection.class);
        InteractionImpl interactionImpl = new InteractionImpl(experiments, components, (CvInteractionType) Mockingbird.getProxyObject(CvInteractionType.class), "testInteractionImplShortLabel", (Institution) Mockingbird.getProxyObject(Institution.class));
        Mockingbird.enterTestMode();
        try {
            interactionImpl.setComponents(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("ex.getMessage()", "Cannot create an Interaction without any Components!", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
            assertSame("interactionImpl.getComponents()", components, interactionImpl.getComponents());
        }
    }

    public void testSetExperimentsThrowsNullPointerException() throws Throwable {
        Collection experiments = new ArrayList(100);
        InteractionImpl interactionImpl = new InteractionImpl(experiments, new CvInteractionType(new Institution("testInteractionImplShortLabel"), "testInteractionImplShortLabel"), "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel1"));
        try {
            interactionImpl.setExperiments(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertEquals("ex.getMessage()", "Cannot create an Interaction without an Experiment!", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
            assertSame("interactionImpl.getExperiments()", experiments, interactionImpl.getExperiments());
        }
    }

    public void testToStringThrowsNullPointerException() throws Throwable {
        Collection someComponent = new ArrayList(100);
        someComponent.add(null);
        InteractionImpl interactionImpl = new InteractionImpl(new ArrayList(1000), new ArrayList(0), new CvInteractionType(new Institution("testInteractionImplShortLabel1"), "testInteractionImplShortLabel"), "testInteractionImplShortLabel", new Institution("testInteractionImplShortLabel"));
        interactionImpl.setComponents(someComponent);
        try {
            interactionImpl.toString();
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException ex) {
            assertNull("ex.getMessage()", ex.getMessage());
            assertThrownBy(InteractionImpl.class, ex);
        }
    }
}
