package uips.uipserver;

import org.junit.Test;
import uips.communication.uip.impl.xml.XmlServerListener;
import uips.communication.uip.interfaces.IUipServerListener;
import uips.support.localization.Messages;
import uips.support.logging.Log;
import uips.support.settings.Settings;
import uips.support.storage.UipFilesAccess;

/**
 * Test class for <code>XmlServerListener</code>
 *
 * @author Jindrich Basek (basekjin@fit.cvut.cz, CTU Prague, FIT)
 */
public class XmlServerListenerTest {

    public void testRun() throws Exception {
        System.out.println("run");
        Messages.init();
        Log.open();
        Settings.load();
        UipFilesAccess.open();
        IUipServerListener instance = new XmlServerListener();
        Thread.sleep(100000);
        instance.dispose();
        System.out.println("stop");
        UipFilesAccess.close();
        Log.close();
    }

    @Test
    public void plain() {
        System.out.println("XmlServerListener");
    }
}
