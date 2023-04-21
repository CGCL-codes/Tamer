package org.hip.vif.bom.impl.test;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import junit.framework.TestCase;
import org.hip.kernel.bitmap.IDPosition;
import org.hip.kernel.bitmap.IDPositions;
import org.hip.kernel.bom.DomainObject;
import org.hip.kernel.exc.VException;
import org.hip.vif.bom.LinkPermissionRoleHome;

/**
 * @author Benno Luthiger
 */
public class LinkPermissionRoleHomeImplTest extends TestCase {

    DataHouseKeeper data;

    /**
	 * Constructor for LinkPermissionRoleHomeImplTest.
	 * @param name
	 */
    public LinkPermissionRoleHomeImplTest(String name) {
        super(name);
        data = DataHouseKeeper.getInstance();
    }

    public void testGetEntry() {
        try {
            LinkPermissionRoleHome lHome = data.getLinkPermissionRoleHome();
            IDPositions lPositions = data.create2Permissions3Links();
            IDPosition lEntry1 = (IDPosition) lPositions.get(0);
            assertEquals("number of links 1", 3, lHome.getCount());
            DomainObject lLink = lHome.getEntry(lEntry1);
            assertEquals("PermissionID", lEntry1.getRowID(), String.valueOf(lLink.get("PermissionID")));
            assertEquals("RoleID", lEntry1.getColumnID(), String.valueOf(lLink.get("RoleID")));
            lLink.delete(true);
            assertEquals("number of links 2", 2, lHome.getCount());
        } catch (VException exc) {
            fail(exc.getMessage());
        } catch (SQLException exc) {
            fail(exc.getMessage());
        } finally {
            data.deleteAllInAll();
        }
    }

    public void testAddAndDelete() {
        try {
            LinkPermissionRoleHome lHome = data.getLinkPermissionRoleHome();
            Collection<IDPosition> lPositions1 = new Vector<IDPosition>();
            IDPosition lPosition = new IDPosition("10", "1");
            lPositions1.add(lPosition);
            lPosition = new IDPosition("12", "1");
            lPositions1.add(lPosition);
            lPosition = new IDPosition("13", "4");
            lPositions1.add(lPosition);
            assertEquals("number 1", 0, lHome.getCount());
            lHome.create(lPositions1);
            assertEquals("number 2", 3, lHome.getCount());
            Collection<IDPosition> lPositions2 = new Vector<IDPosition>();
            lPosition = new IDPosition("13", "5");
            lPositions2.add(lPosition);
            lHome.create(lPositions2);
            assertEquals("number 3", 4, lHome.getCount());
            lHome.delete(lPositions1);
            assertEquals("number 4", 1, lHome.getCount());
            lHome.delete(lPositions2);
            assertEquals("number 5", 0, lHome.getCount());
        } catch (VException exc) {
            fail(exc.getMessage());
        } catch (SQLException exc) {
            fail(exc.getMessage());
        } finally {
            data.deleteAllInAll();
        }
    }
}
