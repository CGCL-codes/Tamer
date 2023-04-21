package org.openmobster.core.mobileCloud.android.module.sync;

import org.openmobster.core.mobileCloud.android.module.mobileObject.MobileObject;

/**
 * 
 * @author openmobster@gmail.com
 *
 */
public final class TestSlowSync extends AbstractSyncTest {

    /**
	 * 
	 */
    public void runTest() {
        try {
            this.setUp("add");
            SyncService.getInstance().performSlowSync(service, service, false);
            this.assertRecordPresence("unique-1", "/TestSlowSync/add");
            this.assertRecordPresence("unique-2", "/TestSlowSync/add");
            this.assertRecordPresence("unique-3", "/TestSlowSync/add");
            this.assertRecordPresence("unique-4", "/TestSlowSync/add");
            this.tearDown();
            this.setUp("replace");
            SyncService.getInstance().performSlowSync(service, service, false);
            MobileObject replacedRecord = this.getRecord("unique-2");
            this.assertRecordPresence("unique-1", "/TestSlowSync/replace");
            this.assertRecordPresence("unique-2", "/TestSlowSync/replace");
            this.assertEquals(replacedRecord.getValue("message"), "<tag apos='apos' quote=\"quote\" ampersand='&'>unique-2/Updated/Client</tag>", "/TestSlowSync/replace/updated");
            this.tearDown();
            this.setUp("delete");
            SyncService.getInstance().performSlowSync(service, service, false);
            this.assertRecordPresence("unique-1", "/TestSlowSync/delete");
            this.assertRecordPresence("unique-2", "/TestSlowSync/delete");
            this.tearDown();
            SyncService.getInstance().performSlowSync(service, service, false);
            this.setUp("conflict");
            SyncService.getInstance().performSlowSync(service, service, false);
            MobileObject conflictedRecord = this.getRecord("unique-1");
            this.assertRecordPresence("unique-1", "/TestSlowSync/conflict");
            this.assertRecordPresence("unique-2", "/TestSlowSync/conflict");
            this.assertEquals(conflictedRecord.getValue("message"), "<tag apos='apos' quote=\"quote\" ampersand='&'>unique-1/Updated/Server</tag>", "/TestSlowSync/conflict/unique-1");
            this.tearDown();
        } catch (Exception e) {
            e.printStackTrace(System.out);
            throw new RuntimeException(e.toString());
        }
    }
}
