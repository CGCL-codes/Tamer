package com.darkhonor.rage.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * JUnit 4 tests of the {@link Instructor} class and its methods
 *
 * @author Alexander Ackerman
 * 
 * @version 1.0.0
 */
public class InstructorTest {

    /**
     * No argument constructor
     */
    public InstructorTest() {
    }

    /**
     * Set up method for all of the method tests
     */
    @Before
    public void setUp() {
        instance = new Instructor("John", "Smith", "John.Smith");
        instance.setDomainAccount("John.Smith");
        instance.setPassword("qwerty");
        instance.setId(42L);
        instance.addSection(new Section("M1A"));
        instance.addSection(new Section("T4B"));
    }

    /**
     * Test of getId method
     */
    @Test
    public void getId() {
        System.out.println("getId");
        Long expResult = 42L;
        Long result = instance.getId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setId method
     */
    @Test
    public void setId() {
        System.out.println("setId");
        Long id = 69L;
        instance.setId(id);
        assertEquals(id, instance.getId());
    }

    /**
     * Test of getLastName method
     */
    @Test
    public void getLastName() {
        System.out.println("getLastName");
        String expResult = "Smith";
        assertEquals(expResult, instance.getLastName());
    }

    /**
     * Test of setLastName method
     */
    @Test
    public void setLastName() {
        System.out.println("setName");
        String newName = "Washington";
        instance.setLastName(newName);
        assertEquals(newName, instance.getLastName());
    }

    /**
     * Test of setLastName method (<code>null</code> last name {@link String})
     * 
     * @throws NullPointerException   when lastName is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void setNullLastName() {
        System.out.println("setNullLastName");
        String nullName = null;
        instance.setLastName(nullName);
    }

    /**
     * Test of setLastName method (blank last name)
     * 
     * @throws java.lang.IllegalArgumentException   When lastName is blank
     */
    @Test(expected = IllegalArgumentException.class)
    public void setBlankLastName() {
        System.out.println("setBlankLastName");
        String blankName = "";
        instance.setLastName(blankName);
    }

    /**
     * Test of getFirstName method
     */
    @Test
    public void getFirstName() {
        System.out.println("getFirstName");
        String expResult = "John";
        assertEquals(expResult, instance.getFirstName());
    }

    /**
     * Test of setFirstName method
     */
    @Test
    public void setFirstName() {
        System.out.println("setFirstName");
        String newName = "George";
        instance.setFirstName(newName);
        assertEquals(newName, instance.getFirstName());
    }

    /**
     * Test of setFirstName method (<code>null</code> first name {@link String})
     * 
     * @throws NullPointerException   When first name is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void setNullFirstName() {
        System.out.println("setNullFirstName");
        String nullName = null;
        instance.setFirstName(nullName);
    }

    /**
     * Test of setFirstName method (blank first name)
     * 
     * @throws java.lang.IllegalArgumentException   When first name is blank
     */
    @Test(expected = IllegalArgumentException.class)
    public void setBlankFirstName() {
        System.out.println("setBlankFirstName");
        String blankName = "";
        instance.setFirstName(blankName);
    }

    /**
     * Test of getWebID method
     */
    @Test
    public void getWebID() {
        System.out.println("getWebID");
        String expected = "John.Smith";
        assertEquals(expected, instance.getWebID());
    }

    /**
     * Test of setWebID method
     */
    @Test
    public void setWebID() {
        System.out.println("setWebID");
        String newWebID = "John.Booker";
        instance.setWebID(newWebID);
        assertEquals(newWebID, instance.getWebID());
    }

    /**
     * Test of setWebID method (<code>null</code> webId {@link String})
     * 
     * @throws NullPointerException   When webId is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void setNullWebID() {
        System.out.println("setNullWebID");
        String nullWebID = null;
        instance.setWebID(nullWebID);
    }

    /**
     * Test of setWebID method (blank webId {@link String})
     * 
     * @throws java.lang.IllegalArgumentException   When webId is blank
     */
    @Test(expected = IllegalArgumentException.class)
    public void setBlankWebID() {
        System.out.println("setBlankWebID");
        String blankWebID = "";
        instance.setWebID(blankWebID);
    }

    /**
     * Test of getDomainAccount method
     */
    @Test
    public void getDomainAccount() {
        System.out.println("getDomainAccount");
        String expected = "John.Smith";
        assertEquals(expected, instance.getDomainAccount());
    }

    /**
     * Test of setDomainAccount method
     */
    @Test
    public void setDomainAccount() {
        System.out.println("setDomainAccount");
        String newAccount = "Alice.Cooper";
        instance.setDomainAccount(newAccount);
        assertEquals(newAccount, instance.getDomainAccount());
    }

    /**
     * Test of setDomainAccount method (<code>null</code> domain account {@link String})
     * 
     * @throws NullPointerException   When domain account is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void setNullDomainAccount() {
        System.out.println("setNullDomainAccount");
        String nullAccount = null;
        instance.setDomainAccount(nullAccount);
    }

    /**
     * Test of setDomainAccount method (blank domain account {@link String})
     * 
     * @throws java.lang.IllegalArgumentException   When domain account is blank
     */
    @Test(expected = IllegalArgumentException.class)
    public void setBlankDomainAccount() {
        System.out.println("setBlankDomainAccount");
        String blankAccount = "";
        instance.setDomainAccount(blankAccount);
    }

    /**
     * Test of getPassword method
     */
    @Test
    public void getPassword() {
        System.out.println("getPassword");
        String expected = "qwerty";
        assertEquals(expected, instance.getPassword());
    }

    /**
     * Test of setPassword method
     */
    @Test
    public void setPassword() {
        System.out.println("setPassword");
        String newPass = "poiuyt";
        instance.setPassword(newPass);
        assertEquals(newPass, instance.getPassword());
    }

    /**
     * Test of setPassword method (<code>null</code> password {@link String})
     * 
     * @throws NullPointerException   When password is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void setNullPassword() {
        System.out.println("setNullPassword");
        String nullPass = null;
        instance.setPassword(nullPass);
    }

    /**
     * Test of setPassword method (blank password {@link String})
     * 
     * @throws java.lang.IllegalArgumentException   When password is blank
     */
    @Test(expected = IllegalArgumentException.class)
    public void setBlankPassword() {
        System.out.println("setBlankPassword");
        String blankPass = "";
        instance.setPassword(blankPass);
    }

    /**
     * Test of getSections method
     */
    @Test
    public void getSections() {
        System.out.println("getSections");
        List<Section> expected = new ArrayList<Section>();
        expected.add(new Section("M1A"));
        expected.add(new Section("T4B"));
        assertEquals(2, instance.getSections().size());
        assertEquals(expected, instance.getSections());
    }

    /**
     * Test of setSections method
     */
    @Test
    public void setSections() {
        System.out.println("setSections");
        List<Section> newSections = new ArrayList<Section>();
        newSections.add(new Section("M5A"));
        newSections.add(new Section("T3A"));
        newSections.add(new Section("T4A"));
        assertEquals(2, instance.getSections().size());
        instance.setSections(newSections);
        assertEquals(3, instance.getSections().size());
        assertEquals(newSections, instance.getSections());
    }

    /**
     * Test of setSections method (<code>null</code> {@link Set} of {@link Section} objects)
     * 
     * @throws NullPointerException   When set is <code>null</code>
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
        Section sect = new Section("M5A");
        instance.addSection(sect);
        assertEquals(3, instance.getSections().size());
        assertTrue(instance.getSections().contains(sect));
    }

    /**
     * Test of addSection method (<code>null</code> {@link Section})
     * 
     * @throws NullPointerException   When section is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void addNullSection() {
        System.out.println("addNullSection");
        instance.addSection(null);
    }

    /**
     * Test of addSection method (duplicate section)
     */
    @Test(expected = IllegalArgumentException.class)
    public void addDuplicateSection() {
        System.out.println("addDuplicateSection");
        instance.addSection(new Section("M1A"));
    }

    /**
     * Test of removeSection method
     */
    @Test
    public void removeSection() {
        System.out.println("removeSection");
        assertEquals(2, instance.getSections().size());
        Section sect = new Section("M1A");
        assertTrue(instance.removeSection(sect));
        assertEquals(1, instance.getSections().size());
    }

    /**
     * Test of removeSection method (<code>null</code> {@link Section})
     * 
     * @throws NullPointerException   When section is <code>null</code>
     */
    @Test(expected = NullPointerException.class)
    public void removeNullSection() {
        System.out.println("removeNullSection");
        instance.removeSection(null);
    }

    /**
     * Test of removeSection method (non-existent {@link Section})
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeNonExistentSection() {
        System.out.println("removeNonExistentSection");
        assertEquals(2, instance.getSections().size());
        assertFalse(instance.removeSection(new Section("M4B")));
        assertEquals(2, instance.getSections().size());
    }

    /**
     * Test of compareTo method (equal objects)
     */
    @Test
    public void compareToEquals() {
        System.out.println("compareToEquals");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (larger WebId).  No impact.
     */
    @Test
    public void compareToLargerId() {
        System.out.println("compareToLargerWebId");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(43L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.compareTo(other) == 0);
        assertTrue(other.compareTo(instance) == 0);
    }

    /**
     * Test of compareTo method (smaller id).  No impact
     */
    @Test
    public void compareToSmallerId() {
        System.out.println("compareToSmallerId");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(40L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.compareTo(other) == 0);
        assertTrue(other.compareTo(instance) == 0);
    }

    /**
     * Test of compareTo method (larger last name)
     */
    @Test
    public void compareToLargerLastName() {
        System.out.println("compareToLargerLastName");
        Instructor other = new Instructor("John", "Zhivago", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.compareTo(other) < 0);
        assertTrue(other.compareTo(instance) > 0);
    }

    /**
     * Test of compareTo method (smaller last name)
     */
    @Test
    public void compareToSmallerLastName() {
        System.out.println("compareToSmallerLastName");
        Instructor other = new Instructor("Homer", "Simpson", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.compareTo(other) > 0);
        assertTrue(other.compareTo(instance) < 0);
    }

    /**
     * Test of compareTo method (larger first name)
     */
    @Test
    public void compareToLargerFirstName() {
        System.out.println("compareToLargerFirstName");
        Instructor other = new Instructor("Robert", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.compareTo(other) < 0);
        assertTrue(other.compareTo(instance) > 0);
    }

    /**
     * Test of compareTo method (smaller first name)
     */
    @Test
    public void compareToSmallerFirstName() {
        System.out.println("compareToSmallerFirstName");
        Instructor other = new Instructor("Homer", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.compareTo(other) > 0);
        assertTrue(other.compareTo(instance) < 0);
    }

    /**
     * Test of compareTo method (larger webID)
     */
    @Test
    public void compareToLargerWebID() {
        System.out.println("compareToLargerWebID");
        Instructor other = new Instructor("John", "Smith", "Robert.Zhivago");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.compareTo(other) < 0);
        assertTrue(other.compareTo(instance) > 0);
    }

    /**
     * Test of compareTo method (smaller web Id)
     */
    @Test
    public void compareToSmallerWebID() {
        System.out.println("compareToSmallerWebID");
        Instructor other = new Instructor("John", "Smith", "Alexis.Robertson");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.compareTo(other) > 0);
        assertTrue(other.compareTo(instance) < 0);
    }

    /**
     * Test of compareTo method (larger domain account)
     */
    @Test
    public void compareToLargerDomainAccount() {
        System.out.println("compareToLargerDomainAccount");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("Leeroy.Jenkins");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (smaller domain account)
     */
    @Test
    public void compareToSmallerDomainAccount() {
        System.out.println("compareToSmallerDomainAccount");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("Alice.Cooper");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (larger password)
     */
    @Test
    public void compareToLargerPass() {
        System.out.println("compareToLargerPass");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("zxcvb");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (smaller password)
     */
    @Test
    public void compareToSmallerPass() {
        System.out.println("compareToSmallerPass");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("asdfg");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of compareTo method (different sections)
     */
    @Test
    public void compareToDiffSections() {
        System.out.println("compareToDiffSections");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("T6H"));
        assertEquals(0, instance.compareTo(other));
        assertEquals(0, other.compareTo(instance));
    }

    /**
     * Test of hashCode methode (equal objects)
     */
    @Test
    public void hashCodeEquals() {
        System.out.println("hashCodeEquals");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertEquals(instance.hashCode(), other.hashCode());
    }

    /**
     * Test of hashCode methode (different id).  No difference
     */
    @Test
    public void hashCodeDiffId() {
        System.out.println("hashCodeDiffId");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(69L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        Integer hash1 = instance.hashCode();
        Integer hash2 = other.hashCode();
        assertEquals(hash1, hash2);
    }

    /**
     * Test of hashCode methode (different first name)
     */
    @Test
    public void hashCodeDiffFirstName() {
        System.out.println("hashCodeDiffFirstName");
        Instructor other = new Instructor("Alex", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        Integer hash1 = instance.hashCode();
        Integer hash2 = other.hashCode();
        assertFalse(hash1.equals(hash2));
    }

    /**
     * Test of hashCode methode (different last name)
     */
    @Test
    public void hashCodeDiffLastName() {
        System.out.println("hashCodeDiffLastName");
        Instructor other = new Instructor("John", "Capone", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        Integer hash1 = instance.hashCode();
        Integer hash2 = other.hashCode();
        assertFalse(hash1.equals(hash2));
    }

    /**
     * Test of hashCode methode (different web id)
     */
    @Test
    public void hashCodeDiffWebID() {
        System.out.println("hashCodeDiffWebID");
        Instructor other = new Instructor("John", "Smith", "Albert.Capone");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        Integer hash1 = instance.hashCode();
        Integer hash2 = other.hashCode();
        assertFalse(hash1.equals(hash2));
    }

    /**
     * Test of hashCode methode (different domain account)
     */
    @Test
    public void hashCodeDiffDomainAccount() {
        System.out.println("hashCodeDiffDomainAccount");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("Leeroy.Jenkins");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertEquals(instance.hashCode(), other.hashCode());
    }

    /**
     * Test of hashCode methode (different password)
     */
    @Test
    public void hashCodeDiffPassword() {
        System.out.println("hashCodeDiffPassword");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("poiuyt");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertEquals(instance.hashCode(), other.hashCode());
    }

    /**
     * Test of hashCode method (different Sections)
     */
    @Test
    public void hashCodeDiffSections() {
        System.out.println("hashCodeDiffSections");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M5G"));
        other.addSection(new Section("T6D"));
        assertEquals(instance.hashCode(), other.hashCode());
    }

    /**
     * Test of equals method (equal objects)
     */
    @Test
    public void equals() {
        System.out.println("equals");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
    }

    /**
     * Test of equals method (different id).  No difference.
     */
    @Test
    public void equalsDiffId() {
        System.out.println("equalsDiffId");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(69L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
    }

    /**
     * Test of equals method (different last name)
     */
    @Test
    public void equalsDiffLastName() {
        System.out.println("equalsDiffLastName");
        Instructor other = new Instructor("John", "Robertson", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertFalse(instance.equals(other));
        assertFalse(other.equals(instance));
    }

    /**
     * Test of equals method (different first name)
     */
    @Test
    public void equalsDiffFirstName() {
        System.out.println("equalsDiffFirstName");
        Instructor other = new Instructor("Alexis", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertFalse(instance.equals(other));
        assertFalse(other.equals(instance));
    }

    /**
     * Test of equals method (different web id)
     */
    @Test
    public void equalsDiffWebID() {
        System.out.println("equalsDiffWebID");
        Instructor other = new Instructor("John", "Smith", "Alice.Cooper");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertFalse(instance.equals(other));
        assertFalse(other.equals(instance));
    }

    /**
     * Test of equals method (different domain account)
     */
    @Test
    public void equalsDiffDomainAccount() {
        System.out.println("equalsDiffDomainAccount");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("Leeroy.Jenkins");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
    }

    /**
     * Test of equals method (different password)
     */
    @Test
    public void equalsDiffPassword() {
        System.out.println("equalsDiffPassword");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("zxcvbbnn");
        other.setId(42L);
        other.addSection(new Section("M1A"));
        other.addSection(new Section("T4B"));
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
    }

    /**
     * Test of equals method (different Sections)
     */
    @Test
    public void equalsDiffSections() {
        System.out.println("equalsDiffSections");
        Instructor other = new Instructor("John", "Smith", "John.Smith");
        other.setDomainAccount("John.Smith");
        other.setPassword("qwerty");
        other.setId(42L);
        other.addSection(new Section("M1C"));
        other.addSection(new Section("M7B"));
        other.addSection(new Section("T6H"));
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
    }

    /**
     * Test of toString method
     */
    @Test
    public void testToString() {
        System.out.println("testToString");
        assertEquals("Smith, John", instance.toString());
    }

    private Instructor instance;
}
