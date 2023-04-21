package jmri.jmrix.nce;

import javax.swing.JOptionPane;
import jmri.jmrix.ConnectionStatus;

public class NceAIUChecker implements NceListener {

    private static final int MEM_AIU = 0xDC15;

    private static final int REPLY_LEN = 1;

    private boolean EXPECT_REPLY = false;

    private NceTrafficController tc = null;

    public NceAIUChecker(NceTrafficController t) {
        super();
        this.tc = t;
    }

    public NceMessage nceAiuPoll() {
        if (tc.getCommandOptions() <= NceTrafficController.OPTION_1999) return null;
        if (tc.getUsbSystem() != NceTrafficController.USB_SYSTEM_NONE) return null;
        byte[] bl = NceBinaryCommand.accMemoryRead1(MEM_AIU);
        NceMessage m = NceMessage.createBinaryMessage(tc, bl, REPLY_LEN);
        EXPECT_REPLY = true;
        return m;
    }

    public void message(NceMessage m) {
        if (log.isDebugEnabled()) {
            log.debug("unexpected message");
        }
    }

    public void reply(NceReply r) {
        if (!EXPECT_REPLY && log.isDebugEnabled()) {
            log.debug("Unexpected reply in AIU broadcast checker");
            return;
        }
        EXPECT_REPLY = false;
        if (r.getNumDataElements() == REPLY_LEN) {
            byte AIUstatus = (byte) r.getElement(0);
            if (AIUstatus > 1) {
                log.warn("AIU check broadcast return value is out of range");
            }
            if (AIUstatus == 1) {
                log.warn("AIU broadcasts are enabled");
                ConnectionStatus.instance().setConnectionState(tc.getPortName(), ConnectionStatus.CONNECTION_DOWN);
                JOptionPane.showMessageDialog(null, "JMRI has detected that AIU broadcasts are enabled. \n" + "You must disable AIU broadcasts for proper operation of this program. \n" + "For more information, see Setup Command Station in your NCE System Reference Manual.", "Warning", JOptionPane.INFORMATION_MESSAGE);
            }
        } else log.warn("wrong number of read bytes for revision check");
    }

    static org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(NceAIUChecker.class.getName());
}
