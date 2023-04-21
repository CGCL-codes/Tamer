package org.kwantu.m2.model;

import static org.testng.Assert.*;
import org.testng.annotations.Test;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kwantu.m2.KwantuFaultException;
import java.util.Set;
import java.util.HashSet;

public class AbstractBusinessSetTest {

    private static final Log LOG = LogFactory.getLog(AbstractBusinessSetTest.class);

    @Test
    public void stringKey() {
        AbstractBusinessSet<KwantuClass, String> bSet = new AbstractBusinessSet<KwantuClass, String>() {

            public String getKey(KwantuClass kc) {
                return kc.getName();
            }
        };
        assertEquals(bSet.getBusinessSet().size(), 0, "Set should exist and be empty.");
        assertEquals(bSet.findItem("testClass"), null, "No entry should be found.");
        KwantuClass kc1 = new KwantuClass(null, "classOne");
        bSet.addItem(kc1);
        KwantuClass kc2 = new KwantuClass(null, "classTwo");
        bSet.addItem(kc2);
        KwantuClass kc3 = new KwantuClass(null, "classThree");
        bSet.addItem(kc3);
        KwantuClass kc4 = new KwantuClass(null, "classThree");
        try {
            bSet.addItem(kc4);
            fail("Should not allow insertion of an item with a duplicate key.");
        } catch (KwantuFaultException e) {
        }
        assertEquals(bSet.getBusinessSet().size(), 3, "Set should contain three items.");
        assertEquals(bSet.findItem("classOne"), kc1, "Item found is not the correct one.");
        assertEquals(bSet.findItem("classTwo"), kc2, "Item found is not the correct one.");
        assertEquals(bSet.findItem("classFour"), null, "No entry should be found for this key.");
        bSet.removeItem(kc1);
        assertEquals(bSet.findItem("classOne"), null, "No entry should be found for this key.");
        bSet.removeItemByKey("classTwo");
        assertEquals(bSet.findItem("classTwo"), null, "No entry should be found for this key.");
        assertEquals(bSet.findItem("classThree"), kc3, "An entry should be found for this key.");
        Set<KwantuClass> setWithDups = new HashSet<KwantuClass>();
        setWithDups.add(kc1);
        setWithDups.add(kc2);
        setWithDups.add(kc3);
        setWithDups.add(kc4);
    }

    /**
     * Important problem: if you define a compound key as a string[]
     * and forget to override the hasKey method the set will appear to
     * work but the String[].equals(String[]) will not produce the right
     * result.
     * this is still broken for setting a set containing duplicates
     */
    @Test(enabled = false)
    public void compoundKey() {
        AbstractBusinessSet<KwantuDependency, String[]> bSet = new AbstractBusinessSet<KwantuDependency, String[]>() {

            public String[] getKey(KwantuDependency kd) {
                return new String[] { kd.getGroupId(), kd.getArtifactId() };
            }

            public boolean hasKey(final KwantuDependency kd, final String[] key) {
                boolean result = kd.getGroupId().equals(key[0]) && kd.getArtifactId().equals(key[1]);
                LOG.info("Calling Dependency haskey with " + key[0] + " and " + key[1] + ".  Result " + result + ".");
                return result;
            }
        };
        assertEquals(bSet.getBusinessSet().size(), 0, "Set should exist and be empty.");
        assertEquals(bSet.findItem(new String[] { "GroupId", "ArtifactId" }), null, "No entry should be found.");
        KwantuDependency kd1 = new KwantuDependency(null, "org.kwantu", "artifact01", "1.0");
        bSet.addItem(kd1);
        KwantuDependency kd2 = new KwantuDependency(null, "org.kwantu", "artifact01", "2.0");
        try {
            bSet.addItem(kd2);
            fail("Should not allow insertion of an item with a duplicate key.");
        } catch (KwantuFaultException e) {
            assert true;
        }
        assertEquals(bSet.getBusinessSet().size(), 1, "Set should contain one item.");
        assertEquals(bSet.findItem(new String[] { "org.kwantu", "artifact01" }), kd1, "Item found is not the correct one (or is null).");
        assertEquals(bSet.getItem(new String[] { "org.kwantu", "artifact01" }), kd1, "Item got is not correct (or is null).");
        assertEquals(bSet.findItem(new String[] { "org.kwantu", "artifact02" }), null, "No entry should be found for this artifact id.");
        assertEquals(bSet.findItem(new String[] { "org.apache", "artifact01" }), null, "No entry should be found for this group id.");
        Set<KwantuDependency> setWithDups = new HashSet<KwantuDependency>();
        setWithDups.add(kd1);
        setWithDups.add(kd2);
        try {
            LOG.info("adding a set containing duplicate keys to a keyed business set");
            bSet.setBusinessSet(setWithDups);
            fail("Should not allow insertion of a collection with duplicate keys.");
        } catch (KwantuFaultException e) {
            assert true;
        }
    }
}
