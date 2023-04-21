package de.goddchen.gbouncer.identd;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import de.goddchen.gbouncer.common.Constants;

public class Identd implements IIdentd {

    private static Logger logger = Logger.getLogger(Identd.class);

    private String ident;

    private static ResourceBundle langRes = ResourceBundle.getBundle("lang/lang", Constants.currentLocale);

    public Identd() {
        ident = "";
        try {
            LocateRegistry.getRegistry().rebind("Identd", UnicastRemoteObject.exportObject(this, 0));
            logger.debug(langRes.getString("IDENTD_BOUND"));
        } catch (AccessException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setIdent(String ident) throws RemoteException {
        logger.debug(langRes.getString("IDENTD_SET_NEW_IDENT") + " " + ident);
        this.ident = ident;
    }

    public void startIdentd() throws RemoteException {
        new Thread(new Runnable() {

            public void run() {
                try {
                    ServerSocket serverSocket = new ServerSocket(113);
                    logger.info(langRes.getString("IDENTD_LISTENING"));
                    Socket clientSocket = serverSocket.accept();
                    String request = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())).readLine();
                    logger.debug(langRes.getString("IDENTD_RECEIVED_REQUEST") + " (" + request + ")");
                    StringTokenizer tokenizer = new StringTokenizer(request, " ,");
                    String portOnServer = tokenizer.nextToken();
                    String portOnClient = tokenizer.nextToken();
                    String response = portOnServer + " , " + portOnClient + " : USERID : gBouncer : " + ident;
                    logger.debug(langRes.getString("IDENTD_SENDING_RESPONSE") + " (" + response + ")");
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                    writer.write(response + "\r\n");
                    writer.flush();
                    clientSocket.close();
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
