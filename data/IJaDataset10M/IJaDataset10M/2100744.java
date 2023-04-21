package jimo.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import jimo.impl.agents.ChatBot;
import jimo.impl.common.Constants;
import jimo.impl.common.UtilXMLReader;
import jimo.spi.log.LogUtil;

/**
 * TODO Introduce Thread and JIMOContainer as Thread Container
 * @author <a href="mailto:wgdo@msn.com">Mark</a>
 */
public class JIMO {

    private static JIMO jimo = null;

    private String currentVersion;

    private String latestVersion;

    private List agents = null;

    private ChatBot robot;

    private JIMO() {
        init();
    }

    public static JIMO getInstance() {
        if (jimo == null) {
            jimo = new JIMO();
        }
        return jimo;
    }

    public static void main(String[] args) {
        String javaVersion = System.getProperty("java.version");
        if (javaVersion.compareTo("1.4") < 0) {
            System.err.println("You are running Java version " + javaVersion + ".");
            System.err.println("JIMO requires Java 1.4 or later.");
            System.exit(1);
        }
        if (args.length == 0) {
            usage();
            System.exit(1);
        }
        jimo = JIMO.getInstance();
        String mode = "bot";
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg == null) {
                continue;
            } else if (arg.length() == 0) {
                args[i] = null;
            } else if (arg.startsWith("-")) {
                if (arg.equals("--usage") || arg.equals("--help") || arg.equals("-h")) {
                    usage();
                    System.exit(1);
                } else if (arg.equals("--version") || arg.equals("-v")) {
                    if (!jimo.latestVersion.equals("")) {
                        System.err.println("The latest version: " + jimo.latestVersion);
                        if (jimo.latestVersion.equals(jimo.currentVersion)) {
                            System.err.println("You're using the latest version.");
                        } else {
                            System.err.println("You're using " + jimo.currentVersion + ", may choose to update.");
                        }
                    } else {
                        System.err.println("You're using " + jimo.currentVersion);
                    }
                    System.exit(1);
                } else if (arg.equals("--bot") || arg.equals("-b")) {
                    mode = "bot";
                } else if (arg.equals("--user") || arg.equals("-u")) {
                    mode = "user";
                } else if (arg.equals("--username") && (i + 1) < args.length) {
                    Constants.username = args[i + 1];
                } else if (arg.equals("--password") && (i + 1) < args.length) {
                    Constants.password = args[i + 1];
                } else if (arg.equals("--protocol") && (i + 1) < args.length) {
                    Constants.prototol = args[i + 1];
                } else {
                    usage();
                    System.exit(1);
                }
            }
            args[i] = null;
        }
        LogUtil.console(Constants.officialSite);
        if (!(jimo.latestVersion).equals(jimo.currentVersion) && !(jimo.latestVersion).equals("")) {
            LogUtil.console("The latest version: " + jimo.latestVersion);
            LogUtil.console("You're using " + jimo.currentVersion + ", may choose to update.");
        }
        if (mode.equals("bot")) {
            LogUtil.console("Chatbot mode");
            jimo.loadJIMO();
        } else {
            usage();
            System.exit(1);
        }
    }

    private void init() {
        List agents = new ArrayList();
        Document doc = UtilXMLReader.loadXML(Constants.MAIN_CONFIG_FILE);
        if (doc != null) {
            doc.getDocumentElement().normalize();
            this.currentVersion = doc.getElementsByTagName("version").item(0).getFirstChild().getNodeValue();
            NodeList agentList = doc.getElementsByTagName("agents").item(0).getChildNodes();
            for (int i = 0; i < agentList.getLength(); i++) {
                Map agentMap = new HashMap();
                Node agentNode = (Node) agentList.item(i);
                if (agentNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element agent = (Element) agentNode;
                    agentMap.put("name", agent.getAttribute("name"));
                    agentMap.put("version", agent.getAttribute("version"));
                    agentMap.put("class", agent.getAttribute("class"));
                    if (agentMap != null) {
                        agents.add(agentMap);
                    }
                }
            }
        } else {
            this.currentVersion = "";
            Map agentMap = new HashMap();
            agentMap.put("name", "-");
            agentMap.put("version", "-");
            agentMap.put("class", "jimo.impl.agents.IMBot");
            agents.add(agentMap);
        }
        this.latestVersion = latestVersion();
        this.agents = agents;
    }

    public void loadJIMO() {
        for (Iterator itor = this.agents.iterator(); itor.hasNext(); ) {
            Map agentMap = (Map) itor.next();
            robot = null;
            Class cls = null;
            try {
                cls = Thread.currentThread().getContextClassLoader().loadClass(agentMap.get("class").toString());
            } catch (ClassNotFoundException cnfe) {
                cnfe.printStackTrace();
            }
            try {
                robot = (ChatBot) cls.newInstance();
            } catch (InstantiationException ie) {
                ie.printStackTrace();
            } catch (IllegalAccessException iae) {
                iae.printStackTrace();
            }
            if (robot != null) {
                LogUtil.console(agentMap.get("name") + " " + agentMap.get("version") + " " + agentMap.get("class") + " loaded!");
                robot.listen();
            }
        }
    }

    public final String latestVersion() {
        String latestVersion = "";
        try {
            URL url = new URL(Constants.officialSite + ":80/LatestVersion");
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            while ((str = in.readLine()) != null) {
                latestVersion = str;
            }
            in.close();
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return latestVersion;
    }

    public String getCurrentVersion() {
        return this.currentVersion;
    }

    public ChatBot getRobot() {
        return this.robot;
    }

    private static void usage() {
        System.err.println();
        System.err.println("Usage: java -jar JIMO.jar [options]");
        System.err.println("Options:");
        System.err.println("    -h, --help  	current display");
        System.err.println("    -b, --bot 		chatbot mode");
        System.err.println("    -u, --user 		user mode");
        System.err.println("    -v, --version 	check the latest version");
        System.err.println();
        System.err.println("Make sure you're using JIMO with JIMO.xml which has been changed to your account, or command line, ");
        System.err.println("java -jar JIMO.JAR --username <your name> --password <your password> [--protocol <your protocol>]");
    }
}
