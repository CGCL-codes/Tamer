package bot;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import util.Des;
import util.Logger;
import util.Xml;

public class Settings {

    public static final String dataDirName = "Data";

    private static final String dataFilename = dataDirName + File.separator + "settings.xml";

    public static String username = "";

    public static String password = "";

    public static String server = "";

    public static String serverType = "Normal";

    public static String tribe = "Romans";

    public static int pollRate = 15000;

    public static int randomPollRate = 15000;

    public static boolean heroEnabled = false;

    public static String heroType = "";

    public static boolean soundWarningEnabled = false;

    public static boolean smsWarningEnabled = false;

    public static boolean emailWarningEnabled = false;

    public static boolean retreatEnabled = false;

    public static String retreatX = "";

    public static String retreatY = "";

    public static boolean stealthModeEnabled = true;

    public static boolean loggingEnabled = false;

    public static String browserUserAgent = "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)";

    public static boolean proxyEnabled = false;

    public static String proxyServer = "";

    public static int proxyPort = 80;

    public static String id = "D";

    public static boolean minimalSet() {
        return ((!username.equals("")) && (!password.equals("")) && (!server.equals("")) && (!serverType.equals("")) && (!tribe.equals("")));
    }

    public static synchronized void save() {
        Logger.log("Saving settings to " + dataFilename);
        new File(Settings.dataDirName).mkdir();
        try {
            File settingsFile = new File(dataFilename);
            BufferedWriter fw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(settingsFile, false), "UTF-8"));
            fw.write("<?xml version=\"1.0\"?>");
            fw.newLine();
            fw.write("<!-- settings.xml -->");
            fw.newLine();
            fw.write("<settings>");
            fw.newLine();
            fw.write("\t<general>");
            fw.newLine();
            fw.write("\t\t<username>" + Xml.cdataEncap(username) + "</username>");
            fw.newLine();
            fw.write("\t\t<password>" + Xml.cdataEncap(Des.encrypt(password)) + "</password>");
            fw.newLine();
            fw.write("\t\t<server>" + Xml.cdataEncap(server) + "</server>");
            fw.newLine();
            fw.write("\t\t<serverType>" + serverType + "</serverType>");
            fw.newLine();
            fw.write("\t\t<tribe>" + tribe + "</tribe>");
            fw.newLine();
            fw.write("\t\t<pollRate>" + pollRate + "</pollRate> <!-- milliseconds -->");
            fw.newLine();
            fw.write("\t\t<randomPollRate>" + randomPollRate + "</randomPollRate> <!-- milliseconds -->");
            fw.newLine();
            fw.write("\t\t<browserUserAgent>" + Xml.cdataEncap(browserUserAgent) + "</browserUserAgent>");
            fw.newLine();
            fw.write("\t</general>");
            fw.newLine();
            fw.write("\t<stealthMode>");
            fw.newLine();
            fw.write("\t\t<enabled>" + stealthModeEnabled + "</enabled>");
            fw.newLine();
            fw.write("\t</stealthMode>");
            fw.newLine();
            fw.write("\t<logging>");
            fw.newLine();
            fw.write("\t\t<enabled>" + loggingEnabled + "</enabled>");
            fw.newLine();
            fw.write("\t</logging>");
            fw.newLine();
            fw.write("\t<proxy>");
            fw.newLine();
            fw.write("\t\t<enabled>" + proxyEnabled + "</enabled>");
            fw.newLine();
            fw.write("\t\t<server>" + Xml.cdataEncap(proxyServer) + "</server>");
            fw.newLine();
            fw.write("\t\t<port>" + proxyPort + "</port>");
            fw.newLine();
            fw.write("\t</proxy>");
            fw.newLine();
            fw.write("\t<hero>");
            fw.newLine();
            fw.write("\t\t<enabled>" + heroEnabled + "</enabled>");
            fw.newLine();
            fw.write("\t\t<type>" + heroType + "</type>");
            fw.newLine();
            fw.write("\t</hero>");
            fw.newLine();
            fw.write("</settings>");
            fw.newLine();
            fw.close();
        } catch (Exception e) {
            Logger.toFile(e.toString());
            e.printStackTrace();
        }
    }

    public static synchronized void load() {
        Logger.log("Loading settings from " + dataFilename);
        File settingsFile = new File(dataFilename);
        if (settingsFile.exists()) {
            try {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setIgnoringElementContentWhitespace(true);
                DocumentBuilder parser = factory.newDocumentBuilder();
                Document doc = parser.parse(settingsFile);
                NodeList rootNodes = doc.getElementsByTagName("settings");
                if (rootNodes.getLength() >= 1) {
                    NodeList settingsNodes = rootNodes.item(0).getChildNodes();
                    for (int i = 0; i < settingsNodes.getLength(); i++) {
                        if (settingsNodes.item(i).getNodeName().equals("general")) {
                            NodeList generalNodes = settingsNodes.item(i).getChildNodes();
                            for (int j = 0; j < generalNodes.getLength(); j++) {
                                if (generalNodes.item(j).getNodeName().equals("username")) {
                                    username = generalNodes.item(j).getTextContent();
                                } else if (generalNodes.item(j).getNodeName().equals("password")) {
                                    password = Des.decrypt(generalNodes.item(j).getTextContent());
                                } else if (generalNodes.item(j).getNodeName().equals("server")) {
                                    server = generalNodes.item(j).getTextContent();
                                } else if (generalNodes.item(j).getNodeName().equals("serverType")) {
                                    serverType = generalNodes.item(j).getTextContent();
                                } else if (generalNodes.item(j).getNodeName().equals("tribe")) {
                                    tribe = generalNodes.item(j).getTextContent();
                                } else if (generalNodes.item(j).getNodeName().equals("pollRate")) {
                                    try {
                                        pollRate = Integer.valueOf(generalNodes.item(j).getTextContent());
                                    } catch (NumberFormatException e) {
                                        Logger.toFile(e.toString());
                                        e.printStackTrace();
                                    }
                                } else if (generalNodes.item(j).getNodeName().equals("randomPollRate")) {
                                    try {
                                        randomPollRate = Integer.valueOf(generalNodes.item(j).getTextContent());
                                    } catch (NumberFormatException e) {
                                        Logger.toFile(e.toString());
                                        e.printStackTrace();
                                    }
                                } else if (generalNodes.item(j).getNodeName().equals("browserUserAgent")) {
                                    browserUserAgent = generalNodes.item(j).getTextContent();
                                }
                            }
                        } else if (settingsNodes.item(i).getNodeName().equals("stealthMode")) {
                            NodeList stealthNodes = settingsNodes.item(i).getChildNodes();
                            for (int j = 0; j < stealthNodes.getLength(); j++) {
                                if (stealthNodes.item(j).getNodeName().equals("enabled")) {
                                    stealthModeEnabled = Boolean.valueOf(stealthNodes.item(j).getTextContent());
                                }
                            }
                        } else if (settingsNodes.item(i).getNodeName().equals("logging")) {
                            NodeList loggingNodes = settingsNodes.item(i).getChildNodes();
                            for (int j = 0; j < loggingNodes.getLength(); j++) {
                                if (loggingNodes.item(j).getNodeName().equals("enabled")) {
                                    loggingEnabled = Boolean.valueOf(loggingNodes.item(j).getTextContent());
                                }
                            }
                        } else if (settingsNodes.item(i).getNodeName().equals("proxy")) {
                            NodeList proxyNodes = settingsNodes.item(i).getChildNodes();
                            for (int j = 0; j < proxyNodes.getLength(); j++) {
                                if (proxyNodes.item(j).getNodeName().equals("enabled")) {
                                    proxyEnabled = Boolean.valueOf(proxyNodes.item(j).getTextContent());
                                } else if (proxyNodes.item(j).getNodeName().equals("server")) {
                                    proxyServer = proxyNodes.item(j).getTextContent();
                                } else if (proxyNodes.item(j).getNodeName().equals("port")) {
                                    try {
                                        proxyPort = Integer.valueOf(proxyNodes.item(j).getTextContent());
                                    } catch (NumberFormatException e) {
                                        Logger.toFile(e.toString());
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } else if (settingsNodes.item(i).getNodeName().equals("hero")) {
                            NodeList heroNodes = settingsNodes.item(i).getChildNodes();
                            for (int j = 0; j < heroNodes.getLength(); j++) {
                                if (heroNodes.item(j).getNodeName().equals("enabled")) {
                                    heroEnabled = Boolean.valueOf(heroNodes.item(j).getTextContent());
                                } else if (heroNodes.item(j).getNodeName().equals("type")) {
                                    heroType = heroNodes.item(j).getTextContent();
                                }
                            }
                        }
                    }
                } else {
                    throw new Exception("Missing root node in " + dataFilename);
                }
            } catch (Exception e) {
                Logger.toFile(e.toString());
                e.printStackTrace();
            }
        } else {
            Logger.toFile("Non existing file " + dataFilename);
        }
    }
}
