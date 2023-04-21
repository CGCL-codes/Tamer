package tests;

import java.util.LinkedList;
import java.util.List;
import org.smepp.logger.Logger;
import tests.utils.Launcher;
import tests.utils.Starter;
import junit.framework.TestCase;

public class SecurityFunctionalityTests extends TestCase {

    List<Launcher> threads = new LinkedList<Launcher>();

    List<Integer> expValues = new LinkedList<Integer>();

    protected static String dir = "output/";

    protected static String confDir = "configs/";

    protected static String cmdsDir = "command_files/CryptoTests/";

    private Logger log = Logger.getLogger(this.getClass().getName());

    /**
	 * 
	 */
    protected void setUp() {
        log.setLevel(TestsConstants.loggingLevelClient);
    }

    /**
	 * 
	 */
    public void testSendGroupMessageToPeerPublicPrivateKey() {
        log.info(" -> Start testSendGroupMessageToPeer()");
        List<String> params = new LinkedList<String>();
        params.add(confDir + "peer0.config");
        params.add(cmdsDir + "send_simple_group_message_to_peer_public_private.peer0.cmds");
        threads.add(new Launcher(params, "peer0", dir + "testSendGroupMessageToPeerPublicPrivateKey.peer0"));
        params.clear();
        params.add(confDir + "peer1.config");
        params.add(cmdsDir + "send_simple_group_message_to_peer_public_private.peer1.cmds");
        threads.add(new Launcher(params, "peer1", dir + "testSendGroupMessageToPeerPublicPrivateKey.peer1"));
        expValues.add(new Integer(0));
        expValues.add(new Integer(0));
        Starter.runThreads(threads, expValues);
    }
}
