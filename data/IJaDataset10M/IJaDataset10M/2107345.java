package test.com.ivis.xprocess.multiuser;

import test.com.ivis.xprocess.XprocessMultiUserTest;
import com.ivis.xprocess.framework.vcs.exceptions.VCSException;

public class TestSchedulerServer extends XprocessMultiUserTest {

    public void testSettingTheServerScheduler() throws VCSException {
        assertEquals(true, ds1.makeSchedulerServer("DS1 is server scheduler", true));
        ds1.getVcsProvider().commit();
        assertTrue(ds1.isServerScheduler());
        ds2.getVcsProvider().update();
        assertFalse(ds2.isServerScheduler());
        assertEquals("DS1 is server scheduler", ds1.getDescriptor().getSchedulerServerName());
        assertEquals("DS1 is server scheduler", ds2.getDescriptor().getSchedulerServerName());
        assertEquals(false, ds2.makeSchedulerServer("DS2 is server scheduler", false));
        ds2.getVcsProvider().commit();
        ds1.getVcsProvider().update();
        assertTrue(ds1.isServerScheduler());
        assertFalse(ds2.isServerScheduler());
        assertEquals("DS1 is server scheduler", ds1.getDescriptor().getSchedulerServerName());
        assertEquals("DS1 is server scheduler", ds2.getDescriptor().getSchedulerServerName());
        ds2.makeSchedulerServer("DS2 is server scheduler", true);
        ds2.getVcsProvider().commit();
        ds1.getVcsProvider().update();
        assertFalse(ds1.isServerScheduler());
        assertTrue(ds2.isServerScheduler());
        assertEquals("DS2 is server scheduler", ds1.getDescriptor().getSchedulerServerName());
        assertEquals("DS2 is server scheduler", ds2.getDescriptor().getSchedulerServerName());
    }

    public void processInfo(String info) {
    }
}
