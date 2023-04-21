package test;

import static org.junit.Assert.assertEquals;
import javax.swing.JDialog;
import org.junit.Before;
import org.junit.Test;
import riaf.controller.RiafMgr;
import riafswing.RDialog;

public class RDialogTest extends RiafTestCase {

    RDialog dialog;

    @Before
    public void init() {
        dialog = new RDialog("test", RiafMgr.global().searchPageHolder("main").getContainer());
    }

    @Test
    public void testTitle() throws Exception {
        dialog.setTitle("test");
        assertEquals("test", ((JDialog) dialog.getImpl()).getTitle());
    }
}
