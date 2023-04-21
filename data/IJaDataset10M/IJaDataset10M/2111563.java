package BisnessLogic.Connection;

import java.net.Socket;
import java.io.*;
import util.*;

public class Connector extends Thread {

    public StreamAnalyzer analyzer;

    public int curThread;

    public boolean signal = false;

    public Connector(Socket _socket) {
        super();
        socket = _socket;
    }

    @Override
    public void run() {
        try {
            if (analyzer == null) analyzer = new StreamAnalyzer(new BufferedInputStream(socket.getInputStream()), new BufferedOutputStream(socket.getOutputStream()));
            SerialyzableContainer cont;
            while ((cont = analyzer.getRequest()) != null) {
                if (cont.getMethodName().compareTo("name") == 0) {
                    this.setName(cont.getValue());
                    analyzer.setReport(new SerialyzableContainer("add", "Hello, " + cont.getValue() + "!"));
                } else {
                    if (signal) {
                        cont = CommandAnalyser.Analyse(getUserName(), cont);
                        cont.setMethodName("1");
                        cont.setValue(systemMessage + " " + cont.getValue());
                        analyzer.setReport(cont);
                        signal = false;
                    } else analyzer.setReport(CommandAnalyser.Analyse(getUserName(), cont));
                }
            }
        } catch (IOException ex) {
        }
    }

    public int get_Id() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void SetName() {
        try {
            analyzer = new StreamAnalyzer(new BufferedInputStream(socket.getInputStream()), new BufferedOutputStream(socket.getOutputStream()));
            SerialyzableContainer cont;
            if ((cont = analyzer.getRequest()) != null) {
                if (cont.getMethodName().compareTo("name") == 0) {
                    setUserName(cont.getValue());
                    String s = cont.getValue();
                    analyzer.setReport(new SerialyzableContainer("add", "Hello, " + cont.getValue() + "!"));
                }
            }
        } catch (IOException e) {
            analyzer = null;
        }
    }

    @Override
    public String toString() {
        return userName;
    }

    public class StreamAnalyzer {

        XMLSerialyzer xml;

        BufferedInputStream input;

        BufferedOutputStream output;

        BufferedReader reader;

        public StreamAnalyzer(InputStream in, OutputStream out) {
            input = new BufferedInputStream(in);
            output = new BufferedOutputStream(out);
            reader = new BufferedReader(new InputStreamReader(in));
        }

        public SerialyzableContainer getRequest() {
            String readRes;
            StringBuffer sb = new StringBuffer();
            try {
                while ((readRes = reader.readLine()).compareTo("EndRequest") != 0) {
                    sb.append(readRes);
                    sb.append("\n");
                }
            } catch (IOException ex) {
            }
            xml = new XMLSerialyzer(new ByteArrayInputStream(sb.toString().getBytes()), output);
            SerialyzableContainer cont = null;
            cont = (SerialyzableContainer) xml.decode();
            return cont;
        }

        public void setReport(SerialyzableContainer container) {
            xml = new XMLSerialyzer(input, output);
            xml.encode(container);
            try {
                output.write("</java>\n".getBytes());
                output.write("EndRequest\n".getBytes());
                output.flush();
            } catch (IOException ex) {
            }
        }
    }

    public String getSystemMessage() {
        return systemMessage;
    }

    public void setSystemMessage(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    private int id = 0;

    private String userName;

    private Socket socket;

    private String systemMessage = "";
}
