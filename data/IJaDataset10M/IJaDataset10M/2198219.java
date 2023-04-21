package uk.ac.roslin.ensembl.model;

import junit.framework.TestCase;
import org.jmock.Expectations;
import org.jmock.Mockery;
import uk.ac.roslin.ensembl.config.FeatureType;
import uk.ac.roslin.ensembl.model.Coordinate.Strand;

/**
 *
 * @author tpaterso
 */
public class MappingTest extends TestCase {

    private Mockery context = new Mockery();

    public MappingTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of addReverseMapping method, of class Mapping.
     */
    public void testAddReverseMapping() {
        System.out.println("addReverseMapping");
        Mapping originalMapping = new Mapping();
        assertFalse(Mapping.addReverseMapping(originalMapping));
        MO source = new MO();
        MO target = new MO();
        originalMapping.setSource(source);
        originalMapping.setTarget(target);
        assertTrue(Mapping.addReverseMapping(originalMapping));
        assertEquals(target.getLoadedMappings().first().getReverseMapping(), originalMapping);
        assertEquals(target.getLoadedMappings().first().getTarget(), source);
        assertEquals(target.getLoadedMappings().first().getSource(), target);
        assertEquals(target.getLoadedMappings().first(), originalMapping.getReverseMapping());
    }

    /**
     * Test of setSource method, of class Mapping.
     */
    public void testSetAndGetSource() {
        System.out.println("setSource");
        final MappableObject source = this.context.mock(MappableObject.class, "obj1");
        Mapping instance = new Mapping();
        assertNull(instance.getSource());
        instance.setSource(source);
        assertEquals(source, instance.getSource());
    }

    /**
     * Test of setTarget method, of class Mapping.
     */
    public void testSetAndGetTarget() {
        System.out.println("setTarget");
        final MappableObject target = this.context.mock(MappableObject.class, "obj1");
        Mapping instance = new Mapping();
        assertNull(instance.getTarget());
        instance.setTarget(target);
        assertEquals(target, instance.getTarget());
    }

    /**
     * Test of getTargetType method, of class Mapping.
     */
    public void testGetTargetType() {
        System.out.println("getTargetType");
        Mapping instance = new Mapping();
        assertNull(instance.getTargetType());
        final MappableObject target = this.context.mock(MappableObject.class, "obj1");
        this.context.checking(new Expectations() {

            {
                (one(target)).getType();
                will(returnValue(FeatureType.gene));
            }
        });
        instance.setTarget(target);
        assertTrue(instance.getTargetType() == FeatureType.gene);
        context.assertIsSatisfied();
    }

    /**
     * Test of getSourceType method, of class Mapping.
     */
    public void testGetSourceType() {
        System.out.println("getSourceType");
        Mapping instance = new Mapping();
        assertNull(instance.getSourceType());
        final MappableObject source = this.context.mock(MappableObject.class, "obj1");
        this.context.checking(new Expectations() {

            {
                (one(source)).getType();
                will(returnValue(FeatureType.gene));
            }
        });
        instance.setSource(source);
        assertTrue(instance.getSourceType() == FeatureType.gene);
        context.assertIsSatisfied();
    }

    /**
     * Test of getTargetHashID method, of class Mapping.
     */
    public void testGetTargetHashID() {
        System.out.println("getTargetHashID");
        Mapping instance = new Mapping();
        String result = instance.getTargetHashID();
        assertEquals("", result);
        final MappableObject target = this.context.mock(MappableObject.class, "obj1");
        this.context.checking(new Expectations() {

            {
                (one(target)).getHashID();
                will(returnValue("bob"));
            }
        });
        instance.setTarget(target);
        result = instance.getTargetHashID();
        assertEquals("bob", result);
        context.assertIsSatisfied();
    }

    /**
     * Test of getSourceHashID method, of class Mapping.
     */
    public void testGetSourceHashID() {
        System.out.println("getSourceHashID");
        Mapping instance = new Mapping();
        String result = instance.getSourceHashID();
        assertEquals("", result);
        final MappableObject source = this.context.mock(MappableObject.class, "obj1");
        this.context.checking(new Expectations() {

            {
                (one(source)).getHashID();
                will(returnValue("bob"));
            }
        });
        instance.setSource(source);
        result = instance.getSourceHashID();
        assertEquals("bob", result);
        context.assertIsSatisfied();
    }

    /**
     * Test of getSourceCoordinates method, of class Mapping.
     */
    public void testGetAndSetSourceCoordinates() {
        System.out.println("getSourceCoordinates, setSourceCoordinates");
        Mapping instance = new Mapping();
        Coordinate expResult = null;
        Coordinate result = instance.getSourceCoordinates();
        assertEquals(expResult, result);
        expResult = new Coordinate();
        instance.setSourceCoordinates(expResult);
        result = instance.getSourceCoordinates();
        assertEquals(expResult, result);
        expResult = new Coordinate(1, 10, 1);
        instance.setSourceCoordinates(1, 10, 1);
        result = instance.getSourceCoordinates();
        assertEquals(result.compareTo(expResult), 0);
        expResult = new Coordinate(2, 20, Strand.FORWARD_STRAND);
        instance.setSourceCoordinates(2, 20, Strand.FORWARD_STRAND);
        result = instance.getSourceCoordinates();
        assertEquals(result.compareTo(expResult), 0);
    }

    /**
     * Test of getTargetCoordinates method, of class Mapping.
     */
    public void testGetAndSetTargetCoordinates() {
        System.out.println("getTargetCoordinates, setTargetCoordinates");
        Mapping instance = new Mapping();
        Coordinate expResult = null;
        Coordinate result = instance.getTargetCoordinates();
        assertEquals(expResult, result);
        expResult = new Coordinate();
        instance.setTargetCoordinates(expResult);
        result = instance.getTargetCoordinates();
        assertEquals(expResult, result);
        expResult = new Coordinate(1, 10, 1);
        instance.setTargetCoordinates(1, 10, 1);
        result = instance.getTargetCoordinates();
        assertEquals(result.compareTo(expResult), 0);
        expResult = new Coordinate(2, 20, Strand.FORWARD_STRAND);
        instance.setTargetCoordinates(2, 20, Strand.FORWARD_STRAND);
        result = instance.getTargetCoordinates();
        assertEquals(result.compareTo(expResult), 0);
    }

    /**
     * Test of equals method, of class Mapping.
     */
    public void testEquals() {
        System.out.println("equals");
        Mapping other = null;
        Mapping instance = new Mapping();
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(other));
        final MappableObject source1 = this.context.mock(MappableObject.class, "obj1");
        final MappableObject source2 = this.context.mock(MappableObject.class, "obj2");
        final MappableObject target1 = this.context.mock(MappableObject.class, "obj4");
        final MappableObject target2 = this.context.mock(MappableObject.class, "obj5");
        this.context.checking(new Expectations() {

            {
                atLeast(1).of(source1).getHashID();
                will(returnValue("s1"));
                atLeast(1).of(source2).getHashID();
                will(returnValue("s2"));
                atLeast(1).of(target1).getHashID();
                will(returnValue("t1"));
                atLeast(1).of(target2).getHashID();
                will(returnValue("t2"));
            }
        });
        Mapping instance1 = new Mapping();
        Mapping instance2 = new Mapping();
        Mapping instance3 = new Mapping();
        instance1.setSource(source1);
        instance2.setSource(source1);
        instance1.setTarget(target1);
        instance2.setTarget(target1);
        instance3.setSource(source2);
        instance3.setTarget(target2);
        assertTrue(instance1.equals(instance2));
        assertTrue(instance2.equals(instance1));
        assertFalse(instance1.equals(instance3));
        assertFalse(instance3.equals(instance1));
        assertFalse(instance2.equals(instance3));
        assertFalse(instance3.equals(instance2));
        context.assertIsSatisfied();
    }

    /**
     * Test of hashCode method, of class Mapping.
     */
    public void testHashCode() {
        System.out.println("hashCode");
        Mapping instance = new Mapping();
        assertTrue(instance.hashCode() == 0);
        final MappableObject source1 = this.context.mock(MappableObject.class, "obj1");
        final MappableObject source2 = this.context.mock(MappableObject.class, "obj2");
        final MappableObject target1 = this.context.mock(MappableObject.class, "obj4");
        final MappableObject target2 = this.context.mock(MappableObject.class, "obj5");
        this.context.checking(new Expectations() {

            {
                atLeast(1).of(source1).getHashID();
                will(returnValue("s1"));
                atLeast(1).of(source2).getHashID();
                will(returnValue("s2"));
                atLeast(1).of(target1).getHashID();
                will(returnValue("t1"));
                atLeast(1).of(target2).getHashID();
                will(returnValue("t2"));
            }
        });
        Mapping instance1 = new Mapping();
        Mapping instance2 = new Mapping();
        Mapping instance3 = new Mapping();
        instance1.setSource(source1);
        instance2.setSource(source1);
        instance1.setTarget(target1);
        instance2.setTarget(target1);
        instance3.setSource(source2);
        instance3.setTarget(target2);
        assertTrue(instance1.hashCode() != 0);
        assertTrue(instance3.hashCode() != 0);
        assertTrue(instance3.hashCode() != instance1.hashCode());
        assertTrue(instance2.hashCode() == instance1.hashCode());
    }

    /**
     * Test of getReverseMapping method, of class Mapping.
     */
    public void testGetAndSetReverseMapping() {
        System.out.println("getReverseMapping, setReverseMapping");
        Mapping instance = new Mapping();
        Mapping expResult = null;
        Mapping result = instance.getReverseMapping();
        assertEquals(expResult, result);
        expResult = new Mapping();
        instance.setReverseMapping(expResult);
        result = instance.getReverseMapping();
        assertEquals(expResult, result);
    }

    public void testMappingOnSourceComparator() {
        Mapping m1 = new Mapping();
        Mapping m2 = new Mapping();
        assertTrue(Mapping.mappingOnSourceComparator.compare(null, null) == 0);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, null) == 1);
        assertTrue(Mapping.mappingOnSourceComparator.compare(null, m1) == -1);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, m1) == 0);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, m2) == 0);
        Coordinate c1 = new Coordinate(1, 2);
        m1.setSourceCoordinates(c1);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, null) == 1);
        assertTrue(Mapping.mappingOnSourceComparator.compare(null, m1) == -1);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, m1) == 0);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, m2) == 1);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m2, m1) == -1);
        Coordinate c2 = new Coordinate(10, 20);
        m2.setSourceCoordinates(c2);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, m2) == -1);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m2, m1) == 1);
        m2.setSourceCoordinates(c1);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m2, m1) == 0);
        MO o1 = new MO();
        m1.setTarget(o1);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, m2) == 0);
        o1.setId(5);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, m2) != 0);
        MO o2 = new MO();
        o2.setId(5);
        m2.setTarget(o2);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, m2) == 0);
        m2.setSource(o1);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, m2) != 0);
        m1.setSource(o2);
        assertTrue(Mapping.mappingOnSourceComparator.compare(m1, m2) == 0);
    }

    public void testMappingOnTargetComparator() {
        Mapping m1 = new Mapping();
        Mapping m2 = new Mapping();
        assertTrue(Mapping.mappingOnTargetComparator.compare(null, null) == 0);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, null) == 1);
        assertTrue(Mapping.mappingOnTargetComparator.compare(null, m1) == -1);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, m1) == 0);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, m2) == 0);
        Coordinate c1 = new Coordinate(1, 2);
        m1.setTargetCoordinates(c1);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, null) == 1);
        assertTrue(Mapping.mappingOnTargetComparator.compare(null, m1) == -1);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, m1) == 0);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, m2) == 1);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m2, m1) == -1);
        Coordinate c2 = new Coordinate(10, 20);
        m2.setTargetCoordinates(c2);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, m2) == -1);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m2, m1) == 1);
        m2.setTargetCoordinates(c1);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m2, m1) == 0);
        MO o1 = new MO();
        m1.setTarget(o1);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, m2) == 0);
        o1.setId(5);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, m2) != 0);
        MO o2 = new MO();
        o2.setId(5);
        m2.setTarget(o2);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, m2) == 0);
        m2.setSource(o1);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, m2) != 0);
        m1.setSource(o2);
        assertTrue(Mapping.mappingOnTargetComparator.compare(m1, m2) == 0);
    }
}

class MO implements MappableObject {

    MappingSet mappings = new MappingSet();

    Integer id = null;

    public MappingSet getLoadedMappings(ObjectType targetType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public MappingSet getLoadedMappings() {
        return mappings;
    }

    public Boolean addMapping(Mapping mapping) {
        return mappings.add(mapping);
    }

    public void clearAllMappings() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ObjectType getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getHashID() {
        return (id != null) ? id.toString() : "";
    }

    public String getSchemaVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getDBVersion() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
