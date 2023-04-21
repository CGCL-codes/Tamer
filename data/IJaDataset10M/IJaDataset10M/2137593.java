package com.darkhonor.rage.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit 4 tests of the {@link Course} class and its methods.
 *
 * @author Alexander Ackerman
 * 
 * @version 0.8.0
 */
public class CourseTest {

    /**
     * No argument constructor
     */
    public CourseTest() {
    }

    /**
     * Set up method for all of the method tests
     */
    @Before
    public void setUp() {
        instance = new Course("CS110");
        instance.setId(42L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        instance.setCourseDirector(cd);
        instance.addSection(new Section("M1A"));
        instance.addSection(new Section("M5B"));
        instance.addInstructor(new Instructor("Smith", "Bob", "Bob.Smith"));
        instance.addInstructor(new Instructor("Riley", "Robert", "Robert.Riley"));
    }

    /**
     * Test of getId method, of class Course.
     */
    @Test
    public void testGetId() {
        System.out.println("getId");
        Long expResult = 42L;
        Long result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method, of class Course.
     */
    @Test
    public void testSetId() {
        System.out.println("setId");
        Long id = 69L;
        instance.setId(id);
        assertEquals(id, instance.getId());
    }

    /**
     * Test of getName method.
     */
    @Test
    public void getName() {
        System.out.println("getName");
        String expected = "CS110";
        assertEquals(expected, instance.getName());
    }

    /**
     * Test of setName method
     */
    @Test
    public void setName() {
        System.out.println("setName");
        String newName = "CS100";
        instance.setName(newName);
        assertEquals(newName, instance.getName());
    }

    /**
     * Test of setName method (<code>null</code> name)
     * 
     * @throws java.lang.IllegalArgumentException   When the name is <code>null</code>
     */
    @Test(expected = IllegalArgumentException.class)
    public void setNullName() {
        System.out.println("setNullName");
        String nullName = null;
        instance.setName(nullName);
    }

    /**
     * Test of setName method (blank name)
     * 
     * @throws java.lang.IllegalArgumentException   When the name is blank.
     */
    @Test(expected = IllegalArgumentException.class)
    public void setBlankName() {
        System.out.println("setBlankName");
        String blankName = "";
        instance.setName(blankName);
    }

    /**
     * Test of getCourseDirector method
     */
    @Test
    public void getCourseDirector() {
        System.out.println("getCourseDirector");
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        assertEquals(cd, instance.getCourseDirector());
    }

    /**
     * Test of setCourseDirector method
     */
    @Test
    public void setCourseDirector() {
        System.out.println("setCourseDirector");
        Instructor cd = new Instructor("Leeroy", "Jenkins", "Leeroy.Jenkins");
        cd.setId(23333L);
        instance.setCourseDirector(cd);
        assertEquals(cd, instance.getCourseDirector());
    }

    /**
     * Test of getInstructors method
     */
    @Test
    public void getInstructors() {
        System.out.println("getInstructors");
        List<Instructor> testInstructors = new ArrayList<Instructor>();
        testInstructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        testInstructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        assertEquals(testInstructors.size(), instance.getInstructors().size());
        assertEquals(testInstructors, instance.getInstructors());
    }

    /**
     * Test of setInstructors method
     */
    @Test
    public void setInstructors() {
        System.out.println("setInstructors");
        List<Instructor> newInstructors = new ArrayList<Instructor>();
        newInstructors.add(new Instructor("Smith", "Patty", "Patricia.Smith"));
        assertEquals(2, instance.getInstructors().size());
        instance.setInstructors(newInstructors);
        assertEquals(1, instance.getInstructors().size());
    }

    /**
     * Test of setInstructors method (<code>null</code> set)
     * 
     * @throws java.lang.IllegalArgumentException   When the set is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void setNullInstructors() {
        System.out.println("setNullInstructors");
        instance.setInstructors(null);
    }

    /**
     * Test of addInstructor method
     */
    @Test
    public void addInstructor() {
        System.out.println("addInstructor");
        assertEquals(2, instance.getInstructors().size());
        assertTrue(instance.addInstructor(new Instructor("Wells", "Jack", "Jack.Wells")));
        assertEquals(3, instance.getInstructors().size());
    }

    @Test
    public void addDuplicateInstructor() {
        System.out.println("addDuplicateInstructor");
        assertEquals(2, instance.getInstructors().size());
        assertTrue(instance.addInstructor(new Instructor("Wells", "Jack", "Jack.Wells")));
        assertFalse(instance.addInstructor(new Instructor("Wells", "Jack", "Jack.Wells")));
        assertEquals(3, instance.getInstructors().size());
    }

    /**
     * Test of addInstructor method (<code>null</code> {@link Instructor})
     * 
     * @throws java.lang.IllegalArgumentException   When the Instructor is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void addNullInstructor() {
        System.out.println("addNullInstructor");
        instance.addInstructor(null);
    }

    /**
     * Test of addInstructor method (Instructor name is blank)
     * 
     * @throws java.lang.IllegalArgumentException   When the Instructor is <code>null</code>
     */
    @Test(expected = IllegalArgumentException.class)
    public void addBlankInstructor() {
        System.out.println("addBlankInstructor");
        instance.addInstructor(new Instructor("", "", ""));
    }

    /**
     * Test of removeInstructor method
     */
    @Test
    public void removeInstructor() {
        System.out.println("removeInstructor");
        assertEquals(2, instance.getInstructors().size());
        instance.removeInstructor(new Instructor("Smith", "Bob", "Bob.Smith"));
        assertEquals(1, instance.getInstructors().size());
    }

    /**
     * Test of removeInstructor method (<code>null</code> {@link Instructor})
     * 
     * @throws java.lang.IllegalArgumentException   When the Instructor is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void removeNullInstructor() {
        System.out.println("removeNullInstructor");
        instance.removeInstructor(null);
    }

    /**
     * Test of removeInstructor method (non-existent {@link Instructor})
     */
    @Test
    public void removeNonExistInstructor() {
        System.out.println("removeNonExistInstructor");
        Instructor ins = new Instructor("Mouse", "Mickey", "Mickey.Mouse");
        assertFalse(instance.removeInstructor(ins));
    }

    /**
     * Test of removeInstructor method (blank Instructor name)
     * 
     * @throws java.lang.IllegalArgumentException   When the Instructor is <code>null</code>
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeBlankInstructor() {
        System.out.println("removeBlankInstructor");
        instance.removeInstructor(new Instructor("", "", ""));
    }

    /**
     * Test of getSections method
     */
    @Test
    public void getSections() {
        System.out.println("getSections");
        List<Section> test_sect = new ArrayList<Section>();
        test_sect.add(new Section("M1A"));
        test_sect.add(new Section("M5B"));
        assertEquals(test_sect.size(), instance.getSections().size());
        assertEquals(test_sect, instance.getSections());
    }

    /**
     * Test of setSections method
     */
    @Test
    public void setSections() {
        System.out.println("setSections");
        List<Section> newSections = new ArrayList<Section>();
        newSections.add(new Section("M2B"));
        newSections.add(new Section("M4G"));
        newSections.add(new Section("T2E"));
        assertEquals(2, instance.getSections().size());
        instance.setSections(newSections);
        assertEquals(3, instance.getSections().size());
        assertEquals(newSections, instance.getSections());
    }

    /**
     * Test of setSections method (<code>null</code> set of {@link Section} objects
     * 
     * @throws java.lang.IllegalArgumentException   When the set is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void setNullSections() {
        System.out.println("setNullSections");
        instance.setSections(null);
    }

    /**
     * Test of addSection method
     */
    @Test
    public void addSection() {
        System.out.println("addSection");
        assertEquals(2, instance.getSections().size());
        assertTrue(instance.addSection(new Section("T1B")));
        assertEquals(3, instance.getSections().size());
    }

    @Test
    public void addDuplicateSection() {
        System.out.println("addDuplicateSection");
        assertEquals(2, instance.getSections().size());
        assertTrue(instance.addSection(new Section("T1B")));
        assertFalse(instance.addSection(new Section("T1B")));
        assertEquals(3, instance.getSections().size());
    }

    /**
     * Test of addSection method (<code>null</code> {@link Section})
     * 
     * @throws java.lang.IllegalArgumentException   When the Section is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void addNullSection() {
        System.out.println("addNullSection");
        instance.addSection(null);
    }

    /**
     * Test of addSection method (<code>Section.name</code> is blank)
     * 
     * @throws java.lang.IllegalArgumentException   When the Section name is blank
     */
    @Test(expected = IllegalArgumentException.class)
    public void addBlankSection() {
        System.out.println("addBlankSection");
        Section section = new Section("");
        instance.addSection(section);
    }

    /**
     * Test of removeSection method
     */
    @Test
    public void removeSection() {
        System.out.println("removeSection");
        assertEquals(2, instance.getSections().size());
        assertTrue(instance.removeSection(new Section("M5B")));
        assertEquals(1, instance.getSections().size());
    }

    /**
     * Test of removeSection method (<code>null</code> {@link Section})
     * 
     * @throws java.lang.IllegalArgumentException   When the Section is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void removeNullSection() {
        System.out.println("removeNullSection");
        instance.removeSection(null);
    }

    /**
     * Test of removeSection method ({@link Section} name is blank)
     * 
     * @throws java.lang.IllegalArgumentException   When the Section name is blank
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeBlankSection() {
        System.out.println("removeBlankSection");
        instance.removeSection(new Section(""));
    }

    /**
     * Test of compareTo method (equal objects)
     */
    @Test
    public void compareToEquals() {
        System.out.println("compareToEquals");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        instance.addSection(new Section("M1A"));
        instance.addSection(new Section("M5B"));
        instance.addInstructor(new Instructor("Smith", "Bob", "Bob.Smith"));
        instance.addInstructor(new Instructor("Riley", "Robert", "Robert.Riley"));
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (larger <code>Course.id</code>)
     */
    @Test
    public void compareToLargerId() {
        System.out.println("compareToLargerId");
        Course other = new Course("CS110");
        other.setId(43L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        instance.addSection(new Section("M1A"));
        instance.addSection(new Section("M5B"));
        instance.addInstructor(new Instructor("Smith", "Bob", "Bob.Smith"));
        instance.addInstructor(new Instructor("Riley", "Robert", "Robert.Riley"));
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (smaller <code>Course.id</code>)
     */
    @Test
    public void compareToSmallerId() {
        System.out.println("compareToSmallerId");
        Course other = new Course("CS110");
        other.setId(40L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (larger <code>Course.name</code>)
     */
    @Test
    public void compareToLargerName() {
        System.out.println("compareToLargerName");
        Course other = new Course("CS483");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertTrue(instance.compareTo(other) < 0);
        assertTrue(other.compareTo(instance) > 0);
    }

    /**
     * Test of compareTo method (smaller <code>Course.name</code>)
     */
    @Test
    public void compareToSmallerName() {
        System.out.println("compareToSmallerName");
        Course other = new Course("AERO215");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertTrue(instance.compareTo(other) > 0);
        assertTrue(other.compareTo(instance) < 0);
    }

    /**
     * Test of compareTo method (larger <code>Course.courseDirector.firstName</code>)
     */
    @Test
    public void compareToLargerCDFirstName() {
        System.out.println("compareToLargerCDFirstName");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("Karl", "Smith", "John.Smith");
        cd.setId(1234L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (Larger <code>Course.courseDirector.lastName</code>)
     */
    @Test
    public void compareToLargerCDLastName() {
        System.out.println("compareToLargerCDLastName");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Zhivago", "John.Smith");
        cd.setId(1234L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (smaller <code>Course.courseDirector.firstName</code>)
     */
    @Test
    public void compareToSmallerCDFirstName() {
        System.out.println("compareToSmallerCDFirstName");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("Alex", "Smith", "John.Smith");
        cd.setId(12L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (smaller <code>Course.courseDirector.lastName</code>)
     */
    @Test
    public void compareToSmallerCDLastName() {
        System.out.println("compareToSmallerCDLastName");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Jenkins", "John.Smith");
        cd.setId(12L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of hashCode method (equal objects)
     */
    @Test
    public void hashCodeSame() {
        System.out.println("hashCodeSame");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        other.setCourseDirector(cd);
        assertEquals(instance.hashCode(), other.hashCode());
    }

    /**
     * Test of hashCode method (different <code>Course.id</code>)
     */
    @Test
    public void hashCodeDiffId() {
        System.out.println("hashCodeDiffId");
        Course other = new Course("CS110");
        other.setId(69L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertEquals(instance.hashCode(), other.hashCode());
    }

    /**
     * Test of hashCode method (different <code>Course.name</code>)
     */
    @Test
    public void hashCodeDiffName() {
        System.out.println("hashCodeDiffName");
        Course other = new Course("CS483");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        Integer hash1 = instance.hashCode();
        Integer hash2 = other.hashCode();
        assertFalse(hash1.equals(hash2));
    }

    /**
     * Test of hashCode method (different <code>Course.courseDirector.firstName</code>)
     */
    @Test
    public void hashCodeDiffCDFirstName() {
        System.out.println("hashCodeDiffCDFirstName");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("Leeroy", "Smith", "John.Smith");
        cd.setId(423333L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertEquals(instance.hashCode(), other.hashCode());
    }

    /**
     * Test of hashCode method (different <code>Course.courseDirector.lastName</code>)
     */
    @Test
    public void hashCodeDiffCDLastName() {
        System.out.println("hashCodeDiffCDLastName");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Jenkins", "John.Smith");
        cd.setId(423333L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertEquals(instance.hashCode(), other.hashCode());
    }

    /**
     * Test of equals method (equal objects)
     */
    @Test
    public void equals() {
        System.out.println("equals");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertTrue(instance.equals(other));
    }

    /**
     * Test of equals method (different <code>Course.id</code>)
     */
    @Test
    public void equalsDiffId() {
        System.out.println("equalsDiffId");
        Course other = new Course("CS110");
        other.setId(69L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertTrue(instance.equals(other));
    }

    /**
     * Test of equals method (different <code>Course.name</code>)
     */
    @Test
    public void equalsDiffName() {
        System.out.println("equalsDiffName");
        Course other = new Course("CS483");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertFalse(instance.equals(other));
    }

    /**
     * Test of equals method (different <code>Course.courseDirector.firstName</code>)
     */
    @Test
    public void equalsDiffCDFirstName() {
        System.out.println("equalsDiffFirstCD");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("Leeroy", "Smith", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertTrue(instance.equals(other));
    }

    /**
     * Test of equals method (different <code>Course.courseDirector.lastName</code>)
     */
    @Test
    public void equalsDiffCDLastName() {
        System.out.println("equalsDiffLastCD");
        Course other = new Course("CS110");
        other.setId(42L);
        Instructor cd = new Instructor("John", "Jenkins", "John.Smith");
        cd.setId(69L);
        other.setCourseDirector(cd);
        List<Section> sections = new ArrayList<Section>();
        sections.add(new Section("M1A"));
        sections.add(new Section("M5B"));
        instance.setSections(sections);
        List<Instructor> instructors = new ArrayList<Instructor>();
        instructors.add(new Instructor("Smith", "Bob", "Bob.Smith"));
        instructors.add(new Instructor("Riley", "Robert", "Robert.Riley"));
        instance.setInstructors(instructors);
        assertTrue(instance.equals(other));
    }

    /**
     * Test of toString method
     */
    @Test
    public void testToString() {
        System.out.println("testToString");
        assertEquals("CS110", instance.toString());
    }

    private Course instance;
}
