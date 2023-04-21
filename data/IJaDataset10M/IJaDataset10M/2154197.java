package org.plantstreamer.xml;

import org.opcda2out.output.database.nodes.PersistentCompositeNodeInfo;
import org.opcda2out.output.database.nodes.PersistentTreeInfo;
import org.opcda2out.output.database.nodes.PersistentOPCItemInfo;
import java.awt.SystemTray;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.plantstreamer.Main;
import org.plantstreamer.TagSelection;
import org.plantstreamer.opc.OPCConnectionDialog;
import org.plantstreamer.output.OutputTypeHandler;
import org.plantstreamer.treetable.OPCTreeTableModel;
import swingextras.gui.password.PasswordGui;

/**
 * A class used for loading the configurations from an XML DOM tree.
 * 
 * @author Joao Leal
 */
public class DocumentLoader {

    private static final Logger logger = Logger.getLogger(DocumentLoader.class.getName(), "org/plantstreamer/i18n/logging");

    /**
     * Loads the information from the root of the Plantstreamer configuration
     * file
     * 
     * @param plantstreamerEl The root element of the Plantstreamer
     * configuration DOM
     */
    protected static void read(Element plantstreamerEl) {
        Main.db.conListInfo.removeAll();
        Main.opc.conListInfo.removeAll();
        Main.crypto.clear();
        PasswordGui passwordGui = new PasswordGui();
        String masterKey = plantstreamerEl.getAttribute("masterKey");
        if (masterKey != null && !masterKey.isEmpty()) {
            Main.crypto.setMainEcryptedPassword(masterKey);
            passwordGui.askForPassword(Main.crypto, Main.mainWindow);
        }
        NodeList optionNode = plantstreamerEl.getElementsByTagName("options");
        if (optionNode.getLength() == 1) {
            NodeList nodes = optionNode.item(0).getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    if (node.getNodeName().equals("autoreconnect")) {
                        Main.options.autoReconnect.setSelected(Boolean.parseBoolean(node.getTextContent()));
                    } else if (node.getNodeName().equals("autoFetchOPCItems")) {
                        Main.options.autoFetchOPCTree.setSelected(Boolean.parseBoolean(node.getTextContent()));
                    } else if (node.getNodeName().equals("minimize2SysTray")) {
                        if (SystemTray.isSupported()) {
                            Main.options.minimize2SysTray.setSelected(Boolean.parseBoolean(node.getTextContent()));
                        }
                    } else if (node.getNodeName().equals("savePasswords")) {
                        String txt = node.getTextContent();
                        if (txt.equals("auto")) {
                            Main.options.savePasswordAuto.setSelected(true);
                        } else if (txt.equals("encrypted")) {
                            Main.options.savePasswordAlwaysEnc.setSelected(true);
                        } else if (txt.equals("plaintext")) {
                            Main.options.savePasswordAlwaysPlain.setSelected(true);
                        } else if (txt.equals("no")) {
                            Main.options.savePasswordNever.setSelected(true);
                        }
                    }
                }
            }
        }
        NodeList opc2outNode = plantstreamerEl.getElementsByTagName("opc2out");
        if (opc2outNode.getLength() == 1) {
            TagSelection ts = Main.mainWindow.tagSelection;
            Element e = ((Element) opc2outNode.item(0));
            String s = e.getAttribute("asynchronous");
            boolean asynchronous = true;
            if (!s.isEmpty()) {
                asynchronous = Boolean.parseBoolean(s);
            }
            Main.options.asynchronous.setSelected(asynchronous);
            s = e.getAttribute("saveItemProperties");
            boolean saveProps = false;
            if (!s.isEmpty()) {
                saveProps = Boolean.parseBoolean(s);
            }
            Main.options.saveProperties.setSelected(saveProps);
            NodeList nodes = e.getChildNodes();
            for (int i = 0; i < nodes.getLength(); i++) {
                Node node = nodes.item(i);
                if (node.getNodeType() != Node.ELEMENT_NODE) {
                    continue;
                }
                if (node.getNodeName().equals("outputs")) {
                    loadOutputs((Element) node, passwordGui);
                } else if (node.getNodeName().equals("samplingrate")) {
                    ts.setSamplingRate(Integer.parseInt(node.getTextContent()));
                } else if (node.getNodeName().equals("items")) {
                    loadItemList((Element) node);
                }
            }
        }
        NodeList OPCconnectionsNode = plantstreamerEl.getElementsByTagName("opcConnections");
        if (OPCconnectionsNode.getLength() == 1) {
            loadOPCConnections((Element) OPCconnectionsNode.item(0));
        }
    }

    private static void loadItemList(Element itemsEl) {
        NodeList itemNodes = itemsEl.getChildNodes();
        int n = itemNodes.getLength();
        List<PersistentOPCItemInfo> opcItems = new ArrayList<PersistentOPCItemInfo>(n);
        List<PersistentCompositeNodeInfo> compItems = new ArrayList<PersistentCompositeNodeInfo>(n);
        Map<String, List<int[]>> arrayIndexes = new ConcurrentHashMap<String, List<int[]>>(n / 3 + 1);
        for (int j = 0; j < n; j++) {
            Node nodeItem = itemNodes.item(j);
            if (nodeItem.getNodeType() == Node.ELEMENT_NODE) {
                if (nodeItem.getNodeName().equals("item")) {
                    PersistentOPCItemInfo info = PersistentOPCItemInfo.loadOpcItem((Element) nodeItem, arrayIndexes);
                    if (info != null) {
                        opcItems.add(info);
                    }
                } else if (nodeItem.getNodeName().equals("composite")) {
                    PersistentCompositeNodeInfo info = PersistentCompositeNodeInfo.load((Element) nodeItem, Main.SCRIPTING);
                    if (info != null) {
                        compItems.add(info);
                    }
                }
            }
        }
        PersistentOPCItemInfo[] items = null;
        if (opcItems.size() > 0) {
            items = opcItems.toArray(new PersistentOPCItemInfo[opcItems.size()]);
        }
        PersistentCompositeNodeInfo[] composite = null;
        if (opcItems.size() > 0) {
            composite = compItems.toArray(new PersistentCompositeNodeInfo[compItems.size()]);
        }
        OPCTreeTableModel ttmodel = Main.mainWindow.tagSelection.getTreeTableModel();
        ttmodel.setItems2Select(new PersistentTreeInfo(items, arrayIndexes, composite));
    }

    private static void loadOutputs(Element outputsEl, PasswordGui passwordGui) {
        TagSelection ts = Main.mainWindow.tagSelection;
        OutputTypeHandler[] ohandlers = ts.getOutputHandlers();
        String[] handlerElName = new String[ohandlers.length];
        for (int i = 0; i < ohandlers.length; i++) {
            handlerElName[i] = XMLSupport.createElementName(ohandlers[i].getOutputTypeName());
        }
        String selected = outputsEl.getAttribute("selected");
        for (int i = 0; i < ohandlers.length; i++) {
            if (handlerElName[i].equals(selected)) {
                ts.setSelectedOutputTypeHandler(ohandlers[i]);
                break;
            }
        }
        NodeList list = outputsEl.getChildNodes();
        for (int k = 0; k < list.getLength(); k++) {
            Node n = list.item(k);
            if (n.getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String output = n.getNodeName();
            boolean isSelected = output.equalsIgnoreCase(selected);
            for (int i = 0; i < ohandlers.length; i++) {
                if (handlerElName[i].equalsIgnoreCase(output)) {
                    OutputLoadInfo loadInfo = new OutputLoadInfo((Element) n, isSelected, passwordGui);
                    ohandlers[i].loadOptions(loadInfo);
                    break;
                }
            }
        }
    }

    private static void loadOPCConnections(Element OPCConEl) {
        NodeList OPCconNodes = OPCConEl.getChildNodes();
        for (int c = 0; c < OPCconNodes.getLength(); c++) {
            Node connection = OPCconNodes.item(c);
            if (connection.getNodeType() == Node.ELEMENT_NODE && connection.getNodeName().equals("connection")) {
                NodeList nodes = connection.getChildNodes();
                ConnectionInformation ci = new ConnectionInformation();
                ci.setUser("");
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node n = nodes.item(i);
                    if (n.getNodeType() == Node.ELEMENT_NODE) {
                        if (n.getNodeName().equals("username")) {
                            ci.setUser(n.getTextContent());
                        } else if (n.getNodeName().equals("password")) {
                            String xmlpass = n.getTextContent();
                            String pass = null;
                            if (xmlpass != null && !xmlpass.isEmpty()) {
                                if (isMasterPasswordUsed()) {
                                    String encrypted = xmlpass;
                                    if (Main.crypto.isInitialized()) {
                                        try {
                                            pass = Main.crypto.dencrypt(encrypted);
                                        } catch (Exception ex) {
                                            logger.log(Level.SEVERE, "A_password_will_not_be_loaded_due_to_a_decryption_exception_{0}", ex.getMessage());
                                        }
                                    }
                                } else {
                                    pass = xmlpass;
                                }
                            } else {
                                pass = "";
                            }
                            ci.setPassword(pass);
                        } else if (n.getNodeName().equals("host")) {
                            ci.setHost(n.getTextContent());
                        } else if (n.getNodeName().equals("domain")) {
                            ci.setDomain(n.getTextContent());
                        } else if (n.getNodeName().equals("progId")) {
                            ci.setProgId(n.getTextContent());
                        }
                    }
                }
                Main.opc.conListInfo.add(ci);
            }
        }
        Main.opc.conListInfo.moved2MainConnectionInfo();
        ConnectionInformation ci = Main.opc.conListInfo.getInfo();
        boolean connected = false;
        if (ci != null && ci.getPassword() != null) {
            connected = Main.opc.connect(ci);
        }
        if (!connected) {
            OPCConnectionDialog.openConnectionWindow(Main.mainWindow, Main.opc);
        }
    }

    /**
     * Determines whether or not a master password is used to encrypt all others
     *
     * @return <code>true</code> if the master password is used,
     *         <code>false</code> otherwise
     */
    public static boolean isMasterPasswordUsed() {
        String pass = Main.crypto.getMasterEncryptedPassword();
        return pass != null && !pass.isEmpty();
    }
}
