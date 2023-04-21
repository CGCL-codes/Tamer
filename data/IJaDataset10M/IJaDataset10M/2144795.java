package ngs.architecture.centralised;

import ngs.*;
import ngs.architecture.*;
import java.io.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import junit.framework.JUnit4TestAdapter;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;

public class CentralisedArchitectureTest extends ArchitectureTest {

    private CentralisedArchitecture a = null;

    private TestCentralisedNode n[] = null;

    private TestCentralisedNode server[] = null;

    private TestCentralisedNode clients[] = null;

    private TestALR alr = null;

    @Before
    public void setup() {
        super.setup();
        a = new CentralisedArchitecture(5.0, false) {
        };
        n = new TestCentralisedNode[5];
        server = new TestCentralisedNode[1];
        clients = new TestCentralisedNode[4];
        alr = new TestALR();
    }

    @Test
    public void setNodes() throws Exception {
        a.setNodes(n, server, clients);
        assertEquals(n, a.getNodes());
    }

    @Test(expected = Exception.class)
    public void setNodesTwice() throws Exception {
        a.setNodes(n, server, clients);
        a.setNodes(n, server, clients);
    }

    @Test
    public void checkCapacity() throws Exception {
        final int size = 53;
        final int delay = 1;
        final int delayRange = 1;
        final double times[] = { 0.5, 1.5, 2.5, 2.6, 3.5, 4.5 };
        a.setNodes(n, server, clients);
        server[0] = n[0] = new TestCentralisedNode(5.0, false, false);
        for (int i = 1; i < n.length; i++) {
            n[i] = clients[i - 1] = new TestCentralisedNode(5.0, false, false);
        }
        for (int i = 0; i < times.length; i++) {
            for (int j = 1; j < n.length; j++) {
                Message m1 = new Message(size, times[i], n[j]);
                GameStateMessage m2 = new GameStateMessage(size, times[i], n[j], delay, delayRange);
                alr.sendMessage(server[0], m1);
                alr.sendMessage(server[0], m2);
            }
        }
        PrintStream console = System.out;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        System.setOut(new PrintStream(bos));
        a.outputMetrics();
        bos.flush();
        assertEquals(baos.toString(), ("5.0 5 178.56 178.56 1488 372 0.4 2 0.2 1 892.8 0.0 1488 0 0.0 223.2 0 372 "));
        System.setOut(console);
    }

    public static junit.framework.Test suite() {
        return new JUnit4TestAdapter(CentralisedArchitectureTest.class);
    }
}
