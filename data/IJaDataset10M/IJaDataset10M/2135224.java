package org.mndacs.control;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.log4j.Logger;
import org.mndacs.datatobjects.CommandSet;
import org.mndacs.kernel.ExecuteCommandSet;
import org.mndacs.protocol.ProtocolInterface;
import org.mndacs.utilities.ProtocolIdent;

/**
 * handle incomming tcp messages
 * @author christopherwagner
 */
public class ServerComm implements Runnable {

    private static Logger logger = Logger.getLogger(ServerComm.class);

    private Socket server;

    ServerComm(Socket server) {
        this.server = server;
    }

    public void run() {
        CommandSet cmd = null;
        String line, inp = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(server.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(server.getOutputStream()));
            int c;
            while ((c = in.read()) != -1 && !inp.contains("<end>")) {
                inp += (char) c;
            }
            int first = inp.indexOf("<");
            int last = inp.indexOf(">") + 1;
            String header = inp.substring(first, last);
            ProtocolInterface proto = ProtocolIdent.identProtocol(header);
            cmd = proto.decode(inp);
            ExecuteCommandSet exec = new ExecuteCommandSet();
            boolean anwser = exec.execute(cmd);
            logger.debug("return anwser: " + anwser + " " + exec.getAnswer());
            if (anwser) {
                out.write(proto.encode(exec.getAnswer()));
                out.write("<end>\n");
                out.flush();
                server.getOutputStream().flush();
            }
            out.close();
            in.close();
            server.close();
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }
}
