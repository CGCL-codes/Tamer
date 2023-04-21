package push2web;

import java.net.*;
import java.io.*;
import javax.servlet.*;

public class HandleSocket {

    PrintWriter out;

    ServletContext sc;

    PrintWriter[] outputList[];

    public HandleSocket(ServletContext current_sc, Socket s) {
        sc = current_sc;
        ReadFromWeb in = new ReadFromWeb(s, sc);
        in.start();
        sc.log("HandleSocket: init successful");
    }

    public void write(String outString) throws IOException {
        Socket[] socketList = (Socket[]) sc.getAttribute("currentSockets");
        for (int i = 0; i < socketList.length; i++) {
            if (socketList[i] != null) {
                out = new PrintWriter(socketList[i].getOutputStream());
                sc.log("HS - sending to browser: " + outString);
                out.println(outString);
                out.flush();
            }
        }
    }

    public void writeOne(Socket s, String outString) throws IOException {
        sc.log("HandleSocket.writeOne: " + outString);
        OutputStream os = s.getOutputStream();
        byte[] outBytes = outString.getBytes();
        os.write(outBytes);
        os.flush();
    }
}
