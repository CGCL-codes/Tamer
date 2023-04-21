package src;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.io.*;
import java.util.*;

public class DeskTopResponder extends JApplet {

    Container contentPane = getContentPane();

    JTextArea outputText;

    String dataString = "";

    String newline = "\n";

    ArrayList<String> RequestParameters = new ArrayList<String>(10);

    String returnString;

    String delimiter;

    String requestDataString;

    String attempts;

    String gJavascriptFilename;

    boolean gJavascriptCalled = false;

    boolean gJavascriptRetry = false;

    boolean finished = false;

    public void init() {
        ArrayList<String> RequestParameters = new ArrayList<String>(10);
        String[] requestData = new String[1];
        JPanel outputPanel = new JPanel();
        Border border = BorderFactory.createLoweredBevelBorder();
        Border titled = BorderFactory.createTitledBorder(border, "Output");
        outputPanel.setBorder(titled);
        outputText = new JTextArea(20, 40);
        JScrollPane outputPane = new JScrollPane(outputText);
        outputPanel.add(outputPane);
        contentPane.add(outputPanel, BorderLayout.SOUTH);
        returnString = "BEGIN";
        try {
            Thread javascriptListener = new Thread() {

                public void run() {
                    while (!finished) {
                        if (gJavascriptCalled) {
                            try {
                                gJavascriptCalled = false;
                                returnString = respondToString(requestDataString, delimiter);
                            } catch (Exception e) {
                                StringWriter errout = new StringWriter();
                                returnString = "EXCEPTION IN RESPOND" + e.getMessage();
                                e.printStackTrace(new PrintWriter(errout));
                                returnString = returnString + "\n STACK TRACE:\n" + errout.toString();
                                finished = true;
                            }
                            finished = true;
                        }
                        if (gJavascriptRetry) {
                            try {
                                gJavascriptRetry = false;
                                gJavascriptFilename = gJavascriptFilename.replace("|", File.separator);
                                returnString = WriteFile(attempts, gJavascriptFilename);
                            } catch (Exception e) {
                                StringWriter errout = new StringWriter();
                                returnString = "EXCEPTION IN RETRY" + e.getMessage();
                                e.printStackTrace(new PrintWriter(errout));
                                returnString = returnString + "\n STACK TRACE:\n" + errout.toString();
                                finished = true;
                            }
                            finished = true;
                        }
                        try {
                            sleep(300);
                        } catch (InterruptedException e) {
                            returnString = "Listener Interrupted";
                        }
                    }
                }
            };
            javascriptListener.start();
        } catch (Exception e) {
            System.out.println("Exception in init e = " + e);
            e.printStackTrace();
            returnString = "Exception in Init";
        }
    }

    public String setRetry(String tries, String filename) {
        try {
            gJavascriptRetry = true;
            gJavascriptFilename = filename;
            attempts = tries;
            while (!finished) {
                Thread.sleep(300);
            }
            System.out.println(returnString);
        } catch (Exception e) {
            returnString = "Exception in setRetry " + e.getMessage();
        }
        return returnString;
    }

    public String getResponse(String reqData, String delim) {
        try {
            requestDataString = reqData;
            delimiter = delim;
            gJavascriptCalled = true;
            while (!finished) {
                Thread.sleep(300);
            }
            System.out.println(returnString);
        } catch (Exception e) {
            returnString = "Exception in getResponse";
        }
        return returnString;
    }

    public String respond(String[] requestData) {
        ResponderCaller = new ResponderCallerDT();
        String response = ResponderCaller.run(requestData);
        outputText.setText(response);
        return response;
    }

    public String respondToString(String requestDataString, String delimiter) {
        ResponderCaller = new ResponderCallerDT();
        String[] requestData = gqtiv2.qtiv2utils.parseToArray(requestDataString, delimiter);
        String[] OutputHTMDirectorydata = gqtiv2.qtiv2utils.getRequestParameter("HTMFileDirectory", requestData);
        String OutputHTMDirectory = OutputHTMDirectorydata[0];
        String[] ResponseFileNamedata = gqtiv2.qtiv2utils.getRequestParameter("ResponseFileName", requestData);
        String ResponseFileName = ResponseFileNamedata[0];
        String response = ResponderCaller.run(requestData);
        outputText.setText(response);
        ResponseFileName = OutputHTMDirectory + File.separator + ResponseFileName;
        response = WriteHTMFile(response, ResponseFileName);
        response = response.replace(java.io.File.separator, "/");
        return response;
    }

    public String WriteHTMFile(String htm, String HTMFilename) {
        try {
            PrintWriter HTMFileout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(HTMFilename), "UTF8")));
            HTMFileout.print(htm);
            HTMFileout.close();
            return HTMFilename;
        } catch (Exception e) {
            return "Cannot write " + HTMFilename + "/n" + e.getMessage();
        }
    }

    public String WriteFile(String contents, String Filename) {
        try {
            PrintWriter Fileout = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Filename), "UTF8")));
            Fileout.println(contents);
            System.out.println("CONENTS" + contents + "END");
            Fileout.close();
            return Filename;
        } catch (Exception e) {
            return "Cannot write " + Filename + "/n" + e.getMessage();
        }
    }

    private String[] theString;

    private int x;

    private int y;

    ResponderCallerDT ResponderCaller;
}
