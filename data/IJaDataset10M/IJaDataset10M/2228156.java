package de.dgrid.wisent.gridftp.xfer;

import java.io.File;
import java.io.IOException;
import java.util.*;
import de.dgrid.wisent.gridftp.test.BaseTestCase;

public class TestTransferLogger extends BaseTestCase {

    public void testAll() throws Exception {
        File tmpdir = getFile("tmpdir");
        if (!tmpdir.mkdir() && !tmpdir.isDirectory()) throw new IOException("Could not create tmpdir");
        try {
            TransferQueue queue = new TransferQueue();
            TransferLogger logger = new TransferLogger(tmpdir) {

                protected void handle(Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            };
            Transfer[] xfers = logger.restoreTransfers(queue);
            assertEquals(0, xfers.length);
            Transfer xfer = new Transfer(null, new String[] { "somedir", "otherdir", "smallfile.txt" }, new Date(), queue, logger, "gsiftp://srvgrid01.offis.uni-oldenburg.de:2811/data/nwp/input_files", "gsiftp://juggle-glob.fz-juelich.de/home/VO_SW_DIR/wisent/nwp/input_files", Transfer.MODE_ACTIVE, Transfer.MODE_PASSIVE, Transfer.OVERWRITE_APPEND);
            logger.saveTransfer(xfer);
            long size1 = 1024L * 1024L * 1024L;
            long size2 = 9 * size1;
            long size3 = 1024;
            TransferAction a1 = new TransferAction(xfer, "somedir/big;file1.bin", size1);
            TransferAction a2 = new TransferAction(xfer, "otherdir/bigfile2.bin", size2);
            TransferAction a3 = new TransferAction(xfer, "smallfile.txt", size3);
            xfer.setPrepared();
            logger.saveTransfer(xfer);
            a1 = queue.beginTransferAction();
            a2 = queue.beginTransferAction();
            a3 = queue.beginTransferAction();
            a1.setTransferredSize(1024 * 1024 * 512L, false);
            a1.hold(new Exception());
            a1.reschedule(System.currentTimeMillis());
            a2.hold();
            a3.setTransferredSize(a3.getTotalSize(), false);
            queue = new TransferQueue();
            xfers = logger.restoreTransfers(queue);
            assertEquals(1, xfers.length);
            Map actionMap = new HashMap();
            for (Iterator i = xfers[0].getActions().iterator(); i.hasNext(); ) {
                TransferAction action = (TransferAction) i.next();
                actionMap.put(action.getPath(), action);
            }
            assertEquals(3, actionMap.size());
            a1 = (TransferAction) actionMap.get("somedir/big;file1.bin");
            a2 = (TransferAction) actionMap.get("otherdir/bigfile2.bin");
            a3 = (TransferAction) actionMap.get("smallfile.txt");
            assertNotNull(a1);
            assertNotNull(a2);
            assertNotNull(a3);
            assertEquals(2, queue.getSize());
            assertEquals(TransferAction.STATE_SCHEDULED, a1.getState());
            assertEquals(TransferAction.STATE_HELD, a2.getState());
            assertEquals(TransferAction.STATE_FINISHED, a3.getState());
            assertEquals(1024 * 1024 * 512L, a1.getTransferredSize());
            assertEquals(0L, a2.getTransferredSize());
            assertEquals(1024L, a3.getTransferredSize());
            assertNull(logger.getFaultDescription(xfer, a2.getFaultDescrPath()));
            assertNotNull(logger.getFaultDescription(xfer, a1.getFaultDescrPath()));
            assertEquals("srvgrid01.offis.uni-oldenburg.de", xfer.getSourceHost());
            assertEquals("juggle-glob.fz-juelich.de", xfer.getTargetHost());
            logger.eraseTransfer(xfers[0]);
            File[] files = tmpdir.listFiles();
            int cnt = 0;
            for (int i = 0; i < files.length; i++) if (files[i].isFile()) cnt++;
            assertEquals(0, cnt);
        } finally {
            File[] files = tmpdir.listFiles();
            for (int i = 0; i < files.length; i++) if (files[i].isFile() && !files[i].delete()) throw new IOException("Could not delete " + files[i]);
            if (!tmpdir.delete()) throw new IOException("Could not delete " + tmpdir);
        }
    }
}
