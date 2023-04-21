package com.ask.FSD;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.netbeans.api.project.Project;
import org.netbeans.api.project.ProjectUtils;
import org.netbeans.spi.project.AuxiliaryConfiguration;
import org.openide.awt.StatusDisplayer;
import org.openide.filesystems.FileObject;
import org.openide.util.Exceptions;
import org.openide.windows.IOColorLines;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.openide.windows.OutputWriter;

/**
 *
 * @author dany
 */
public class Settings {

    private static String Host = "";

    private static String User = "";

    private static String Pass = "";

    private static String Path = "";

    private static String NomeProg = "";

    private static Project prog = null;

    private static InputOutput io = IOProvider.getDefault().getIO("ASK FTP Site Deployer", true);

    private static int Port = 21;

    public static boolean Salva() {
        boolean b = false;
        try {
            DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbfac.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement("root");
            doc.appendChild(root);
            Element child = doc.createElement("FtpSiteDeployer");
            child.setAttribute("user", User);
            child.setAttribute("pass", Pass);
            child.setAttribute("host", Host);
            child.setAttribute("port", "" + Port);
            root.appendChild(child);
            File file = new File(Path);
            Result result = new StreamResult(file);
            Source source = new DOMSource(doc);
            Transformer xformer = TransformerFactory.newInstance().newTransformer();
            xformer.transform(source, result);
            b = true;
            out("Configuration saved for " + NomeProg, 1);
        } catch (java.lang.Exception ex) {
            out("Error saving configuration for : " + ex.toString());
            b = false;
        }
        return b;
    }

    public static void setMainProject(Project mainP) {
        prog = mainP;
        FileObject root = prog.getProjectDirectory();
        Path = root.getPath() + File.separator + "AskFtpSiteDeployer.xml";
        NomeProg = "" + mainP.getProjectDirectory().getName();
        Leggi();
    }

    public static Project getMainProject() {
        return prog;
    }

    private static boolean Leggi() {
        boolean b = false;
        File f = new File(Path);
        if (f.exists()) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(f);
                Element ele = (Element) document.getElementsByTagName("FtpSiteDeployer").item(0);
                Host = ele.getAttribute("host");
                User = ele.getAttribute("user");
                Pass = ele.getAttribute("pass");
                try {
                    Port = Integer.parseInt(ele.getAttribute("port"));
                } catch (java.lang.NumberFormatException ex) {
                    Port = 21;
                }
                b = true;
                out("Configuration loaded for " + NomeProg);
            } catch (java.lang.Exception ex) {
                b = false;
                out("Error - configuration file not recognized for " + NomeProg + " : " + ex.toString(), 4);
            }
        } else {
            out("Error - configuration file not found.", 4);
            b = false;
        }
        return b;
    }

    public static int getPort() {
        return Port;
    }

    public static void setPort(int iPort) {
        Port = iPort;
    }

    /**
     * @return the Host
     */
    public static String getHost() {
        return Host;
    }

    /**
     * @param Host the Host to set
     */
    public static void setHost(String sHost) {
        Host = sHost;
    }

    /**
     * @return the User
     */
    public static String getUser() {
        return User;
    }

    /**
     * @param User the User to set
     */
    public static void setUser(String sUser) {
        User = sUser;
    }

    /**
     * @return the Pass
     */
    public static String getPass() {
        return Pass;
    }

    /**
     * @param Pass the Pass to set
     */
    public static void setPass(String sPass) {
        Pass = sPass;
    }

    public static void out(String msg) {
        StatusDisplayer.getDefault().setStatusText("ASK Fsd - " + msg);
        io.getOut().println(msg);
    }

    public static void inLinePrint(String msg) {
        StatusDisplayer.getDefault().setStatusText("ASK Fsd - " + msg);
        io.getOut().print(msg);
    }

    public static void out(String msg, int level) {
        try {
            Color color = new Color(0, 200, 0);
            switch(level) {
                case 1:
                    color = new Color(0, 0x7c, 0);
                    break;
                case 2:
                    color = Color.gray;
                    break;
                case 3:
                    color = Color.blue;
                    break;
                case 4:
                    color = Color.red;
                    break;
                default:
                    color = Color.red;
            }
            IOColorLines.println(io, msg, color);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
            io.getOut().println(msg);
        }
    }
}
