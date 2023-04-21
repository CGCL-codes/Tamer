package sipinspector.ScenarioEntries;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.JOptionPane;
import sipinspector.Threads.RTPSenderThread;
import sipinspector.SIPCall;
import sipinspector.ScenarioProgressDialog;

/**
 *
 * @author Zarko Coklin
 */
public class CommandPlayPcapEntry extends ScenarioEntry {

    public CommandPlayPcapEntry(String content) {
        super(TYPE.COMMAND_PLAYPCAP, DIRECTION.NONE);
        String str;
        path = "";
        filename = "";
        continuousRTP = false;
        BufferedReader reader = new BufferedReader(new StringReader(content));
        try {
            while ((str = reader.readLine()) != null) {
                if (str.startsWith("//") == true) {
                    continue;
                }
                if (str.startsWith("cmd_PlayPcap=") == true) {
                    setValues(str);
                    break;
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e, "Update Play Pcap", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setValues(String command) {
        String[] arguments = command.split(";");
        int pos = arguments[0].indexOf('=');
        if (pos == -1) {
            return;
        } else {
            path = arguments[0].substring(pos + 1);
            filename = getFilename(path);
        }
        if (arguments.length == 2) {
            if (arguments[1].equals("continuous=false") == true) {
                continuousRTP = false;
                return;
            }
        }
        continuousRTP = true;
    }

    public static String getFilename(String filePath) {
        int posSeparator;
        posSeparator = filePath.lastIndexOf(File.separatorChar);
        if (posSeparator == -1) {
            return "";
        }
        return filePath.substring(posSeparator + 1);
    }

    @Override
    public boolean processEntry(SIPCall call, ScenarioProgressDialog dialog) throws ScenarioException {
        this.increaseExpectedCnt();
        dialog.updateStats(this);
        dialog.getRTPThread().addRemoteClient(call.getRemoteClient());
        if (dialog.getRTPThread().getRunningFlag() == false) {
            dialog.startRTPThread(path, continuousRTP);
        }
        call.nextEntry();
        return true;
    }

    @Override
    public String getTreeText() {
        if (continuousRTP == true) {
            return (" ---- Play pcap=" + filename + ";cont=true");
        } else {
            return (" ---- Play pcap=" + filename);
        }
    }

    @Override
    public String getScenarioScreenText() {
        StringBuilder txt = new StringBuilder(System.getProperty("line.separator") + "----------" + System.getProperty("line.separator") + "cmd_PlayPcap=" + path);
        if (continuousRTP == true) {
            txt.append(";continuous=true");
        } else {
            txt.append(";continuous=false");
        }
        txt.append(System.getProperty("line.separator"));
        return txt.toString();
    }

    @Override
    public String getShortCode() {
        if (continuousRTP == true) {
            return "Play pcap=" + filename + ";cont=true";
        } else {
            return "Play pcap=" + filename + ";cont=false";
        }
    }

    @Override
    public String getScenarioProgressScreenText() {
        return " ---- " + getShortCode();
    }

    @Override
    public void stop() {
        rtpThread.interrupt();
    }

    private String path;

    private String filename;

    RTPSenderThread rtpThread;

    boolean continuousRTP;
}
