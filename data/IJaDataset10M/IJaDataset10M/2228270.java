package monitor.terminal;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import net.AddressException;
import monitor.utils.AbstractIpAddress;
import de.mud.telnet.TelnetWrapper;

public class TelnetHelper extends TerminalQueryHandler {

    TelnetWrapper telnet = null;

    public TelnetHelper(AbstractIpAddress host, String username, String passwd) throws TerminalException {
        super(host, username, passwd);
        telnet = new TelnetWrapper();
        this.port = 23;
    }

    @Override
    protected void setUp() {
        try {
            telnet.connect(host.toInetAddress(), port);
            telnet.login(username, passwd);
            System.err.println("Telnet connection established.");
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void tearDown() {
        try {
            telnet.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void executeQuery() throws TerminalException {
        setUp();
        telnet.setPromptWaitingTimeOut(100);
        telnet.determinePrompt();
        String prompt = telnet.getPrompt();
        for (Entry<String, String> entry : this.queryResponseMap.entrySet()) {
            String queryCommand = entry.getKey();
            try {
                String response = telnet.send(queryCommand);
                int index1 = response.indexOf(queryCommand);
                if (index1 == -1) {
                    throw new TerminalException("terminal text process ERROR!");
                }
                int index2 = response.indexOf("\r\n", index1);
                int index3 = response.indexOf(prompt, index2);
                if (index3 == -1) {
                    throw new TerminalException("terminal text process ERROR!");
                }
                int index4 = response.lastIndexOf("\r\n");
                if (index4 > index3) {
                    throw new TerminalException("terminal text process ERROR!");
                }
                String cleanresponse;
                if (index2 == index4) {
                    cleanresponse = "";
                } else {
                    cleanresponse = response.substring(index2 + 2, index4);
                }
                entry.setValue(cleanresponse);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tearDown();
    }

    public static void main(String[] args) {
        try {
            TelnetHelper th;
            th = new TelnetHelper(AbstractIpAddress.createAddress("10.0.0.15"), "", "xxxxxx");
            HashMap<String, String> comm = new HashMap<String, String>();
            comm.put("show ip interface brief", "");
            comm.put("show ip interface e0/0", "");
            th.setCommands(comm);
            th.executeQuery();
            HashMap<String, String> resp = th.getQueryResponseMap();
            for (Entry entry : resp.entrySet()) {
                System.out.println(entry.getKey());
                System.out.println(entry.getValue());
                System.out.println("----------------------------------------------------------");
            }
        } catch (TerminalException e) {
            e.printStackTrace();
        } catch (AddressException e) {
            e.printStackTrace();
        }
    }
}
