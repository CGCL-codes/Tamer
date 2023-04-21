package net.cmp4oaw.ea_com.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import net.cmp4oaw.ea_com.connector.EA_Connector;
import net.cmp4oaw.ea_com.connector.EA_Dependency;
import net.cmp4oaw.ea_com.connector.EA_NoteLink;
import net.cmp4oaw.ea_com.element.EA_Class;
import net.cmp4oaw.ea_com.repository.EA_Package;
import org.junit.Test;

public class EA_PackageTest extends EA_BaseTest {

    @Test
    public void eaPackageTest() {
        try {
            EA_Package pkg = testPkg.findPackageByPath("Packages");
            EA_Class cls = (EA_Class) pkg.Elements.getByName("c1");
            EA_Package pkg1 = pkg.findPackageByPath("Package1");
            EA_Package pkg2 = pkg.findPackageByPath("Package2");
            assertNotNull("Package1 not found!", pkg1);
            assertNotNull("Package2 not found!", pkg2);
            assertNotNull("Class 'c1' not found!", cls);
            assertEquals("Not all dependencies of 'pkg1' found!", 3, pkg1.Connectors.count);
            assertEquals("Not all dependencies of 'pkg2' found!", 2, pkg2.Connectors.count);
            for (EA_Connector con : pkg.getAssociations()) {
                if (con.getName().equals("d1")) {
                    EA_Dependency d = (EA_Dependency) con;
                    assertEquals("Client end of 'd1' is not 'Package2'!", pkg2.ExtObj(), d.ClientEnd().getEnd().ExtObj());
                    assertEquals("Supplier end of 'd1' is not 'Package1'!", pkg1.ExtObj(), d.SupplierEnd().getEnd().ExtObj());
                    continue;
                } else if (con.getName().equals("d2")) {
                    EA_Dependency d = (EA_Dependency) con;
                    assertEquals("Client end of 'd2' is not 'c1'!", cls, d.ClientEnd().getEnd());
                    assertEquals("Supplier end of 'd2' is not 'Package1'!", pkg1.ExtObj(), d.SupplierEnd().getEnd().ExtObj());
                    continue;
                } else if (con.getName().equals("d3")) {
                    EA_Dependency d = (EA_Dependency) con;
                    assertEquals("Client end of 'd3' is not 'Package2'!", pkg2.ExtObj(), d.ClientEnd().getEnd().ExtObj());
                    assertEquals("Supplier end of 'd3' is not 'c1'!", cls, d.SupplierEnd().getEnd());
                    continue;
                } else if (con instanceof EA_NoteLink) {
                    continue;
                }
                assertFalse("Invalid dependency found! [" + con.getName() + "]", true);
            }
        } catch (Exception ex) {
            assertFalse("Error in eaPackageTest(): " + ex, true);
        }
    }
}
