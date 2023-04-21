package issrg.acm.extensions;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import javax.swing.*;
import issrg.ac.ACCreationException;
import issrg.ac.AttributeCertificate;
import issrg.acm.DefaultSavingUtility;
import issrg.utils.EnvironmentalVariables;
import issrg.utils.gui.ACViewer;
import issrg.utils.SpringUtilities;
import issrg.utils.WebdavUtil;
import issrg.utils.gui.ACViewer;
import issrg.utils.gui.repository.WebDAV_DIT;
import issrg.utils.gui.repository.WebDAV_DIT_Event;
import issrg.utils.gui.repository.WebDAV_DIT_Listener;
import issrg.utils.webdav.*;
import issrg.ac.extensions.*;

/**
 * This is the WebDAV saving utility.
 *
 * <p>This utility saves the given Attribute Certificate to a WebDAV Repository,
 * of which the Host and Port can be dynamically changed in the dialog.
 *
 * The DN to save to is obtained from the AC to save;
 * the DN to load from is obtained from the DIT selection.
 *
 * <p>SSL client authentication to the WebDAV server is required (HTTPS).
 *
 * <p>The utility was tested on Apache 2.2.3 mod_dav only.
 *
 * @author Sean Antony
 * @version 11/03/2007
 */
public class WebDAVSavingUtility extends DefaultSavingUtility implements ActionListener, WebDAV_DIT_Listener {

    public static final String policyPrefix = "Policy=";

    private JPanel content = new JPanel(new BorderLayout());

    private JTextField host = new JTextField("");

    private JTextField port = new JTextField(5);

    private JTextField dn = new JTextField("");

    private JButton connect = new JButton("Connect...");

    private JPanel dit = new JPanel(new BorderLayout());

    private WebDAVSocket socket;

    private WebDAV_DIT $WebDAV_DIT = new WebDAV_DIT(null, null, 0);

    private ACViewer $ACViewer = new ACViewer();

    private String acName;

    private final String portError = "Invalid WebDAV Port Number: ";

    private boolean forceWebdav;

    private Map env;

    private String serial;

    /**
     * Load with an AC Name prespecified for the case of the PE, otherwise will
     * prompt the user for one.
     */
    public WebDAVSavingUtility(String acName) {
        this(acName, false, null);
    }

    public WebDAVSavingUtility(String acName, boolean forceWebdav, Map env) {
        super();
        this.acName = acName;
        this.forceWebdav = forceWebdav;
        this.env = env;
        content = buildDialog();
    }

    private JPanel buildDialog() {
        JPanel connectionInfo = new JPanel(new SpringLayout());
        JLabel hostLabel = new JLabel("WebDAV Host", JLabel.TRAILING);
        connectionInfo.add(hostLabel);
        hostLabel.setLabelFor(host);
        connectionInfo.add(host);
        JLabel portLabel = new JLabel("WebDAV Port", JLabel.TRAILING);
        connectionInfo.add(portLabel);
        portLabel.setLabelFor(port);
        connectionInfo.add(port);
        JLabel dnLabel = new JLabel("DN", JLabel.TRAILING);
        connectionInfo.add(dnLabel);
        dnLabel.setLabelFor(dn);
        connectionInfo.add(dn);
        dn.setEditable(false);
        JLabel connectLabel = new JLabel("", JLabel.TRAILING);
        connectionInfo.add(connectLabel);
        connectLabel.setLabelFor(connect);
        connectionInfo.add(connect);
        connect.addActionListener(this);
        SpringUtilities.makeCompactGrid(connectionInfo, 4, 2, 6, 6, 6, 6);
        JPanel center = new JPanel(new BorderLayout());
        center.add(new JLabel("  "), BorderLayout.NORTH);
        center.add($ACViewer, BorderLayout.CENTER);
        $ACViewer.setPreferredSize(new Dimension(375, 290));
        dit.add(new JLabel("  WebDAV Entries:"), BorderLayout.NORTH);
        dit.add($WebDAV_DIT, BorderLayout.CENTER);
        $WebDAV_DIT.addMyEventListener(this);
        $WebDAV_DIT.setPreferredSize(new Dimension(220, 290));
        JPanel content = new JPanel(new BorderLayout());
        content.add(connectionInfo, BorderLayout.PAGE_START);
        content.add(dit, BorderLayout.LINE_START);
        content.add(center, BorderLayout.CENTER);
        if (forceWebdav) {
            host.setEnabled(false);
            port.setEnabled(false);
        }
        return content;
    }

    public void approveSelection() {
        if ($ACViewer.getSelectedAC() == null && dialogMode != SAVE_MODE && $ACViewer.getSelectedIndex() == -1) {
            issrg.utils.Util.bewail("X.509 Attribute Certificate could not be loaded: " + "No AC was selected", null, dialog);
        } else if ($ACViewer.getSelectedAC() == null && dialogMode == LOAD_MODE) {
            issrg.utils.Util.bewail("Broken X.509 Attribute Certificate could not be loaded", null, dialog);
        } else {
            if (approveWebDAVRepository()) {
                super.approveSelection();
            }
        }
    }

    /**
     * Checks if the WebDAV repository can accept the AC to be saved based on
     * user input.
     */
    private boolean approveWebDAVRepository() {
        if (dialogMode == SAVE_MODE) {
            $WebDAV_DIT.convertDN2URI(dn.getText());
            HTTPMessage msg;
            int depth = -1;
            String uri;
            do {
                depth++;
                uri = HTTPMessage.FS;
                for (int i = depth; i < $WebDAV_DIT.getSplitDN().length; i++) {
                    uri = HTTPMessage.FS + $WebDAV_DIT.getSplitDN()[i] + uri;
                }
                try {
                    msg = new HEAD(socket, host.getText(), Integer.parseInt(port.getText()), uri.replaceAll("//", "/"));
                    msg.transceive();
                } catch (HTTPMessageException e) {
                    issrg.utils.Util.bewail(e.getErrorMessage(), e, dialog);
                    return false;
                } catch (NumberFormatException e) {
                    issrg.utils.Util.bewail(portError + port.getText(), e, dialog);
                    return false;
                }
            } while (!msg.isResponseSuccessful() && depth < $WebDAV_DIT.getSplitDN().length - 1);
            if (depth > 0) {
                for (int j = 0; j <= depth; j++) {
                    uri = HTTPMessage.FS;
                    for (int i = depth - j; i < $WebDAV_DIT.getSplitDN().length; i++) {
                        uri = HTTPMessage.FS + $WebDAV_DIT.getSplitDN()[i] + uri;
                    }
                    try {
                        msg = new MKCOL(socket, host.getText(), Integer.parseInt(port.getText()), uri);
                        msg.transceive();
                    } catch (HTTPMessageException e) {
                        issrg.utils.Util.bewail(e.getErrorMessage(), e, dialog);
                        return false;
                    } catch (NumberFormatException e) {
                        issrg.utils.Util.bewail(portError + port.getText(), e, dialog);
                        return false;
                    }
                }
            } else if (depth == 0) {
            }
            if (acName == null) {
                while (true) {
                    try {
                        while (true) {
                            if (!forceWebdav) {
                                try {
                                    acName = askACFileName("");
                                    if (acName.equals("")) {
                                        return false;
                                    }
                                    break;
                                } catch (HTTPMessageException e) {
                                    issrg.utils.Util.bewail(e.getErrorMessage(), e, dialog);
                                }
                            } else {
                                for (int i = depth; i < $WebDAV_DIT.getSplitDN().length; i++) {
                                    if ($WebDAV_DIT.getSplitDN()[i].toLowerCase().contains("cn=")) {
                                        acName = $WebDAV_DIT.getSplitDN()[i];
                                    } else {
                                    }
                                }
                                acName = acName + "+SN=" + serial + $WebDAV_DIT.ace;
                                break;
                            }
                        }
                        if (doesFileExist(msg)) {
                            if (confirmOverwrite() == JOptionPane.YES_OPTION) {
                                break;
                            } else {
                                acName = null;
                            }
                        } else {
                            break;
                        }
                    } catch (HTTPMessageException e) {
                        issrg.utils.Util.bewail(e.getErrorMessage(), e, dialog);
                        return false;
                    }
                }
            } else if (acName.startsWith(policyPrefix)) {
                if (acName.equals(policyPrefix)) {
                    issrg.utils.Util.bewail("Policy Name cannot be empty!", null, dialog);
                    return false;
                }
                if (acName.contains(HTTPMessage.FS)) {
                    issrg.utils.Util.bewail("Invalid character \"/\" found in Policy name.", null, dialog);
                    return false;
                }
                if (!acName.endsWith(WebDAV_DIT.ace)) {
                    acName += WebDAV_DIT.ace;
                }
                try {
                    while (true) {
                        if (doesFileExist(msg)) {
                            if (confirmOverwrite() == JOptionPane.NO_OPTION) {
                                return false;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                } catch (HTTPMessageException e) {
                    issrg.utils.Util.bewail(e.getErrorMessage(), e, dialog);
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Asks the user to name the AC to be saved.
     */
    private String askACFileName(String prefix) throws HTTPMessageException {
        int acNameMaxLength = 128;
        String acName = (String) JOptionPane.showInputDialog(dialog, "Enter file name", "Save Attribute Certificate as...", JOptionPane.OK_CANCEL_OPTION);
        if (acName == null) {
            return prefix;
        }
        if (acName.contains(HTTPMessage.FS)) {
            throw new HTTPMessageException("Invalid character \"/\" found in AC name.");
        } else if (acName.length() == 0) {
            throw new HTTPMessageException("AC name cannot be empty.");
        }
        acName = prefix + acName + WebDAV_DIT.ace;
        if (acName.length() > acNameMaxLength) {
            throw new HTTPMessageException("AC name too long, cannot exceed " + acNameMaxLength + " characters.");
        }
        return acName;
    }

    /**
     * Confirms if the user wants to overwrite the file with the new AC.
     */
    private int confirmOverwrite() {
        return JOptionPane.showConfirmDialog(dialog, "Attribute Certificate with this name already exists. Overwrite?", "Confirm", JOptionPane.YES_NO_OPTION);
    }

    /**
     * Checks if a file exists.
     */
    private boolean doesFileExist(HTTPMessage msg) throws HTTPMessageException {
        try {
            String filename = $WebDAV_DIT.getDnToURI() + HTTPMessage.FS + acName;
            filename = filename.replaceAll("//", "/");
            msg = new HEAD(socket, host.getText(), Integer.parseInt(port.getText()), filename);
            msg.transceive();
        } catch (HTTPMessageException e) {
            throw new HTTPMessageException(e.getErrorMessage());
        } catch (NumberFormatException e) {
            throw new HTTPMessageException(portError + port.getText());
        }
        if (msg.isResponseSuccessful()) {
            return true;
        }
        return false;
    }

    /**
     * Loads the currently selected AC in the ACViewer.
     */
    public AttributeCertificate load() throws ACCreationException {
        AttributeCertificate ac = $ACViewer.getSelectedAC();
        if (ac == null && $ACViewer.getSelectedIndex() == -1) {
            throw new ACCreationException("No AC was selected");
        }
        return ac;
    }

    public AttributeCertificate revoke() throws ACCreationException {
        return load();
    }

    /**
     * Saves the AC to the WebDAV repository.
     */
    public void save(byte[] ac) throws ACCreationException {
        HTTPMessage msg = null;
        try {
            String entityBody = new sun.misc.BASE64Encoder().encode(ac);
            AttributeCertificate aci = AttributeCertificate.guessEncoding(ac);
            if (aci.getACInfo().getExtensions() != null) {
                Vector exts = aci.getACInfo().getExtensions().getValues();
                ArrayList webdavcertHosts = new ArrayList();
                for (int i = 0; i < exts.size(); i++) {
                    if (exts.get(i) instanceof AuthorityInformationAccess) {
                        ArrayList extensions = ((AuthorityInformationAccess) exts.get(i)).getValues();
                        for (int j = 0; j < extensions.size(); j++) {
                            if (extensions.get(j) instanceof WebdavCertificate) {
                                WebdavCertificate certi = (WebdavCertificate) extensions.get(j);
                                String certHost = WebdavUtil.getHostFromURL(certi.getLocation());
                                String certPort = WebdavUtil.getPortFromURL(certi.getLocation());
                                String certPath = WebdavUtil.getURIFromURL(certi.getLocation());
                                String certName = WebdavUtil.getNameFromURL(certi.getLocation());
                                String[] loc = { certHost, certPort, certPath, certName };
                                webdavcertHosts.add(loc);
                            }
                        }
                    }
                }
                if (webdavcertHosts.size() > 0) {
                    for (int i = 0; i < webdavcertHosts.size(); i++) {
                        String[] certLoc = (String[]) webdavcertHosts.get(i);
                        certLoc[2] = certLoc[2].replaceAll("//", "/");
                        System.out.println("Host:" + certLoc[0] + " port:" + certLoc[1] + " path:" + certLoc[2] + " name:" + certLoc[3]);
                        msg = new PUT(socket, certLoc[0], Integer.parseInt(certLoc[1]), certLoc[2] + certLoc[3], entityBody);
                        msg.transceive();
                        if (!msg.isResponseSuccessful()) {
                            throw new ACCreationException(msg.getResponseParagraph());
                        }
                    }
                }
            }
            if (msg == null) {
                String web_host = host.getText();
                String web_port = port.getText();
                String web_URI = $WebDAV_DIT.getDnToURI();
                String web_name = HTTPMessage.FS + acName;
                msg = new PUT(socket, web_host, Integer.parseInt(web_port), web_URI + web_name, entityBody);
                msg.transceive();
                if (!msg.isResponseSuccessful()) {
                    throw new ACCreationException(msg.getResponseParagraph());
                }
            }
        } catch (HTTPMessageException e) {
            throw new ACCreationException(e.getErrorMessage(), e);
        } catch (NumberFormatException e) {
            throw new ACCreationException(portError + port.getText(), e);
        } catch (iaik.asn1.CodingException ce) {
            throw new ACCreationException("Error decoding attribute certificate:" + ce.getMessage());
        } finally {
            acName = null;
        }
    }

    /**
     * Deletes the currently selected AC in the ACViewer.
     */
    public void delete() throws ACCreationException {
        if ($ACViewer.getSelectedIndex() == -1) {
            throw new ACCreationException("No AC was selected");
        }
        $WebDAV_DIT.convertDN2URI(dn.getText());
        acName = $ACViewer.getACName($ACViewer.getSelectedIndex());
        HTTPMessage msg;
        try {
            AttributeCertificate ac = $ACViewer.getSelectedAC();
            if (ac.getACInfo().getExtensions() != null) {
                Vector exts = ac.getACInfo().getExtensions().getValues();
                ArrayList webdavrevHosts = new ArrayList();
                for (int i = 0; i < exts.size(); i++) {
                    if (exts.get(i) instanceof AuthorityInformationAccess) {
                        ArrayList extensions = ((AuthorityInformationAccess) exts.get(i)).getValues();
                        for (int j = 0; j < extensions.size(); j++) {
                            if (extensions.get(j) instanceof WebdavRevocation) {
                                WebdavRevocation revo = (WebdavRevocation) extensions.get(j);
                                String revHost = WebdavUtil.getHostFromURL(revo.getLocation());
                                String revPort = WebdavUtil.getPortFromURL(revo.getLocation());
                                String revPath = WebdavUtil.getURIFromURL(revo.getLocation());
                                String revName = WebdavUtil.getNameFromURL(revo.getLocation());
                                String[] loc = { revHost, revPort, revPath, revName };
                                webdavrevHosts.add(loc);
                            }
                        }
                    }
                }
                if (webdavrevHosts.size() > 0) {
                    for (int i = 0; i < webdavrevHosts.size(); i++) {
                        String[] revLoc = (String[]) webdavrevHosts.get(i);
                        String entityBody = new sun.misc.BASE64Encoder().encode(ac.getEncoded());
                        try {
                            msg = new MKCOL(socket, host.getText(), Integer.parseInt(port.getText()), revLoc[2]);
                            msg.transceive();
                        } catch (HTTPMessageException e) {
                            issrg.utils.Util.bewail(e.getErrorMessage(), e, dialog);
                        }
                        msg = new PUT(socket, revLoc[0], Integer.parseInt(revLoc[1]), revLoc[2] + revLoc[3], entityBody);
                        msg.transceive();
                        if (!msg.isResponseSuccessful()) {
                            throw new ACCreationException(msg.getResponseParagraph());
                        }
                    }
                }
            }
            msg = new DELETE(socket, host.getText(), Integer.parseInt(port.getText()), dn.getText() + HTTPMessage.FS + acName);
            msg.transceive();
            if (!msg.isResponseSuccessful()) {
                throw new ACCreationException(msg.getResponseParagraph());
            }
        } catch (HTTPMessageException e) {
            throw new ACCreationException(e.getErrorMessage(), e);
        } catch (NumberFormatException e) {
            throw new ACCreationException(portError + port.getText(), e);
        } catch (iaik.asn1.CodingException ce) {
        }
    }

    /**
     * Reads in the environment settings: cfg variables, and sets the DN, Host,
     * Port, and P12 Filename & Password values.
     * <p>Establishes an SSL connection to Host:Port, authenticating itself with
     * the P12 key & certificate file.</p>
     * Calls <code>issrg.utils.Util.bewail()</code> if connection throws an
     * error, with the error message returned.
     */
    public Component getContentPane(Map env, int dialogMode) {
        String dn = (String) env.get(EnvironmentalVariables.HOLDER_NAME_STRING);
        String host = (String) env.get(EnvironmentalVariables.WEBDAV_HOST);
        String port = (String) env.get(EnvironmentalVariables.WEBDAV_PORT);
        String protocol = (String) env.get(EnvironmentalVariables.WEBDAV_PROTOCOL);
        String p12filename = (String) env.get(EnvironmentalVariables.WEBDAV_P12FILENAME);
        String p12password = (String) env.get(EnvironmentalVariables.WEBDAV_P12PASSWORD);
        serial = (String) env.get(EnvironmentalVariables.SERIAL_NUMBER_STRING);
        try {
            if (protocol != null && protocol.equals("HTTPS")) {
                socket = new WebDAVSocketHTTPS(p12filename, p12password);
            } else {
                socket = new WebDAVSocketHTTP();
            }
            $WebDAV_DIT.setSocket(socket);
        } catch (HTTPMessageException e) {
            issrg.utils.Util.bewail(e.getErrorMessage(), e, dialog);
        }
        return getDialog(dialogMode != SAVE_MODE ? "" : dn, host, port, dialogMode);
    }

    /**
     * Constructs the dialog panel depending on the dialog mode.
     * <p>If in Save mode hide DIT, AC Viewer and Connect button otherwise
     * show everything.</p>
     */
    private Component getDialog(String dn, String host, String port, int dialogMode) {
        this.dn.setText(dn);
        this.dn.setEditable(false);
        this.host.setText(host == null ? "" : host);
        this.port.setText(port == null ? "" : port);
        if (host != null && port != null) {
            refreshDIT();
        }
        dit.setVisible(dialogMode != SAVE_MODE);
        $ACViewer.setVisible(dialogMode != SAVE_MODE);
        connect.setVisible(dialogMode != SAVE_MODE);
        return attachControlButtons(content, dialogMode);
    }

    /**
     * Refreshes the DIT with the connection info in the dialog.
     * <p>Calls <code>issrg.utils.Util.bewail</code> if connection throws an
     * error, with the error message returned.
     */
    private void refreshDIT() {
        try {
            if (this.dialogMode != SAVE_MODE) {
                $WebDAV_DIT.setHost(host.getText());
                $WebDAV_DIT.setPort(Integer.parseInt(port.getText()));
                $WebDAV_DIT.updateCollections();
            }
        } catch (HTTPMessageException e) {
            $WebDAV_DIT.clearCollections();
            $ACViewer.setACs(null, null);
            issrg.utils.Util.bewail(e.getErrorMessage(), e, dialog);
        } catch (NumberFormatException e) {
            $WebDAV_DIT.clearCollections();
            $ACViewer.setACs(null, null);
            issrg.utils.Util.bewail(portError + port.getText(), e, dialog);
        }
    }

    /**
     * Handles the event of a user clicking the Connect button.
     * <p>Calls the <code>refreshDIT()</code> method.
     */
    public void actionPerformed(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(connect)) {
            refreshDIT();
        }
    }

    /**
     * Handles the event of a user changing the selection of a node in the DIT,
     * representing a DN.
     * <p>Loads any ACs found under that DN into the ACViewer, with it's unique
     * name.</p>
     */
    public void valueChanged_WebDAV_DIT(WebDAV_DIT_Event evt) {
        WebDAV_DIT evtSrc = (WebDAV_DIT) evt.getSource();
        if (evtSrc.getErrorMessage().equals("")) {
            this.dn.setText(evtSrc.getDn());
            $ACViewer.setACs(evtSrc.getACs(), evtSrc.get$ACNames());
        } else {
            issrg.utils.Util.bewail(evtSrc.getErrorMessage(), null, dialog);
        }
    }
}
