package au.jSummit.Server;

import au.jSummit.Core.*;
import java.io.*;
import java.net.*;

class SIRSHandler extends Thread implements Serializable {

    Socket sClient;

    ObjectInputStream oisIn;

    ObjectOutputStream oosOut;

    SummitInfo si;

    public SIRSHandler(Socket theClient, SummitInfo newSi) {
        sClient = theClient;
        si = newSi;
        try {
            sClient.setSoTimeout(5000);
            oisIn = new ObjectInputStream(sClient.getInputStream());
        } catch (Exception e) {
            System.err.println("SIRHandlerConstructor: " + e.toString());
        }
    }

    public void run() {
        try {
            String sInput = (String) oisIn.readObject();
            if (sInput.equals(Globals.S_SIREQ)) {
                oosOut = new ObjectOutputStream(sClient.getOutputStream());
                oosOut.writeObject(si);
            }
            sClient.close();
        } catch (Exception e) {
            System.err.println("SIReqServerHandler: " + e.toString());
        }
    }
}
