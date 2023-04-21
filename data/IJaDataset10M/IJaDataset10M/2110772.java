package org.globaltester.testmanager.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ResultListenerThread implements Runnable {

    int port = 6788;

    int timeout = 60000;

    ServerSocket server = null;

    Socket socket = null;

    String result = null;

    IPreferenceStore store;

    public void run() {
        result = "null";
        store = org.globaltester.testmanager.Activator.getDefault().getPreferenceStore();
        port = store.getInt(org.globaltester.testmanager.preferences.PreferenceConstants.P_RESULTLISTENERPORT);
        timeout = store.getInt(org.globaltester.testmanager.preferences.PreferenceConstants.P_RESULTLISTENERTIMEOUT);
        System.out.println("Starting Result Listener Thread...");
        try {
            server = new ServerSocket(port);
            server.setSoTimeout(timeout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket = server.accept();
            handleConnection(socket);
        } catch (InterruptedIOException e) {
            System.err.println("Timeout!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server closed");
    }

    private void handleConnection(Socket client) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        String s = in.readLine();
        result = extractXMLFile(s);
        System.out.println("Result from upper tester: " + result);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        out.write("RECEIVED");
        out.newLine();
        out.flush();
    }

    public String getResult() {
        return result;
    }

    private static String extractXMLFile(String s) {
        String testresult = null;
        SAXBuilder builder = new SAXBuilder(false);
        Document doc = null;
        try {
            StringReader in = new StringReader(s);
            doc = builder.build(in);
            Element root = doc.getRootElement();
            testresult = root.getTextTrim();
            System.out.println("Test result: " + testresult);
            return testresult;
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testresult;
    }
}
