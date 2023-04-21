package student.configurationwizard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import pdc.admincontrolserver.Protocol;
import pdc.admincontrolserver.ProtocolCommand;
import pdc.admincontrolserver.ProtocolMessage;
import pdc.admincontrolserver.ProtocolMessageType;
import pdc.admincontrolserver.SQLConnection;
import pdc.admincontrolserver.SQLConnection.EmailClientColumn;
import pdc.admincontrolserver.SQLConnection.ShortcutColumn;
import pdc.admincontrolserver.SQLConnection.StudentColumn;
import pdc.xml.User;
import pdc.xml.configuration.BasicConfiguration;
import pdc.xml.logincommand.Logincommand;
import pdc.xml.serveraddress.ServerAddress;
import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QCheckBox;
import com.trolltech.qt.gui.QDialog;
import com.trolltech.qt.gui.QMainWindow;
import com.trolltech.qt.gui.QSpacerItem;
import com.trolltech.qt.gui.QVBoxLayout;
import com.trolltech.qt.gui.QDialog.DialogCode;

/**
 * Connects to the server, loads the configuration file and applies the configuration to the machine.
 * @version   1.0
 * @author   Simon Jarke
 */
public class Wizard {

    /** The config. */
    private ClientConfig config;

    /** The base configuration. */
    private Document baseConfiguration;

    /** The connection established. */
    private boolean connectionEstablished = false;

    /** The ui. */
    private Ui_MainWindow ui;

    /** The Server config. */
    private ServerAddress serverConfig;

    /** The server connection. */
    private SSLSocket serverConnection;

    private QDialog qloginDlg;

    private Ui_LoginDlg loginDlg;

    private QMainWindow MainWindow;

    private boolean loggedIn;

    private String userPassword;

    private File receivedConfig;

    private String name;

    private String program;

    private String year;

    private String email;

    private QVBoxLayout softwareLayouter;

    private class SoftwareSelection {

        String name;

        String file;

        String path;

        QCheckBox checkBox;
    }

    private ArrayList<SoftwareSelection> softwareCheckBoxes = new ArrayList<SoftwareSelection>(10);

    /**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * 
	 * @throws Exception
	 *             the exception
	 */
    public static void main(String[] args) {
        Wizard w = new Wizard();
    }

    /**
	 * Instantiates a new wizard.
	 * 
	 */
    public Wizard() {
        String[] args = {};
        config = new ClientConfig("xmlinstances/clientConfig.xml");
        JAXBContext context;
        try {
            context = JAXBContext.newInstance("pdc.xml.serveraddress");
            serverConfig = (ServerAddress) context.createUnmarshaller().unmarshal(new File("xmlinstances/admincontrolserver.xml"));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        if (!isServerreachable()) {
        }
        QApplication.initialize(args);
        MainWindow = new QMainWindow();
        ui = new Ui_MainWindow();
        ui.setupUi(MainWindow);
        softwareLayouter = new QVBoxLayout(ui.groupBox_addSoftware);
        ui.OkButton.clicked.connect(this, "OkButtonClicked();");
        ui.ApplyButton.clicked.connect(this, "applyButtonClicked()");
        if (connectToServer()) {
            MainWindow.show();
            switch(DialogCode.resolve(showLoginDlg())) {
                case Accepted:
                    QApplication.exec();
                    break;
                case Rejected:
                    QApplication.instance().dispose();
                    break;
            }
            disconnectFromServer();
        }
    }

    /**
	 * Shows the login dialog
	 * 
	 * 
	 * @return true, if successful
	 */
    protected int showLoginDlg() {
        if (this.loginDlg == null) this.loginDlg = new Ui_LoginDlg();
        if (this.qloginDlg == null) {
            this.qloginDlg = new QDialog(this.MainWindow);
            this.qloginDlg.setModal(true);
            this.loginDlg.setupUi(this.qloginDlg);
            this.loginDlg.buttonbox_okCancel.accepted.connect(this, "login()");
            if (connectionEstablished && !this.loginDlg.buttonbox_okCancel.isEnabled()) {
                this.loginDlg.buttonbox_okCancel.setEnabled(true);
            }
        }
        return qloginDlg.exec();
    }

    private void login() {
        final Protocol prtcl = Protocol.instance;
        Logincommand logincmd = new Logincommand();
        User u = new User();
        u.setName(loginDlg.lineedit_name.text());
        email = prtcl.generateEmailFromName(u.getName());
        u.setEmail(email);
        logincmd.setCurrentUser(u);
        logincmd.setSHA1Password(prtcl.encryptSHA1(loginDlg.lineedit_password.text()));
        userPassword = loginDlg.lineedit_password.text();
        loginDlg.lineedit_password.setText("");
        try {
            prtcl.serializeMessageObject(serverConnection.getOutputStream(), ProtocolCommand.LoginToServer, ProtocolMessageType.Request, logincmd, "pdc.xml.logincommand");
            ProtocolMessage msg = prtcl.deserializeMessageReader(serverConnection.getInputStream());
            if (msg.getProtocolCommand() == ProtocolCommand.LoginToServer && msg.getType() == ProtocolMessageType.Response) {
                BufferedReader br = msg.getDataStream();
                loggedIn = new Boolean(br.readLine());
                System.out.println("loggedIn? " + loggedIn);
                if (loggedIn) {
                    qloginDlg.done(DialogCode.Accepted.value());
                    receivedConfig = new File("receivedConfiguration.xml");
                    BufferedWriter conf_writer = new BufferedWriter(new FileWriter(receivedConfig));
                    conf_writer.write(prtcl.readOutXML(msg.getDataStream()).toString());
                    conf_writer.close();
                    parseConfigScript(getConfigScript());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Ok button clicked.
	 */
    private void OkButtonClicked() {
        applyConfiguration();
        this.MainWindow.disposeLater();
    }

    /**
	 * Applies the received configuration to the local machine. The connection to the 
	 * server has to be established, otherwise a exception is thrown.
	 */
    protected void applyConfiguration() {
        try {
            File f = File.createTempFile("mainConfigScript", ".cmd");
            FileWriter fr = new FileWriter(f);
            fr.write("@echo off\r\n");
            List list;
            String username = email.substring(0, email.indexOf('@')) + "@stu.i-u.de";
            if (ui.checkBox_netdrives.isChecked()) {
                list = baseConfiguration.selectNodes("/" + BasicConfiguration.rootTag + "/" + BasicConfiguration.Tags.NetworkDrive);
                for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                    Element e = (Element) iter.next();
                    fr.write("cscript " + System.getProperty("user.dir") + "\\scripts\\ConnectNetworkDrive.js ");
                    for (Iterator subiter = e.elementIterator(); subiter.hasNext(); ) {
                        Element j = (Element) subiter.next();
                        fr.write(j.getStringValue() + ": ");
                        j = (Element) subiter.next();
                        fr.write(j.getStringValue() + " ");
                        fr.write(email + " ");
                        fr.write(userPassword);
                    }
                    fr.write("\r\n");
                }
            }
            if (ui.checkBox_printers.isChecked()) {
                list = baseConfiguration.selectNodes("/" + BasicConfiguration.rootTag + "/" + BasicConfiguration.Tags.Printer);
                for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                    Element e = (Element) iter.next();
                    fr.write("cscript " + System.getProperty("user.dir") + "\\scripts\\ConnectPrinter.js ");
                    for (Iterator subiter = e.elementIterator(); subiter.hasNext(); ) {
                        Element j = (Element) subiter.next();
                        fr.write(j.getStringValue() + ": ");
                    }
                    fr.write("\r\n");
                }
            }
            if (ui.checkBox_homepage.isChecked()) {
                String url = baseConfiguration.selectSingleNode("/" + BasicConfiguration.rootTag + "/" + BasicConfiguration.Tags.BrowserPage + "/" + SQLConnection.BrowserPageColumn.URL).getStringValue();
                fr.write("cscript " + System.getProperty("user.dir") + "\\scripts\\StartPageIE.vbs " + url + "\r\n");
            }
            if (ui.checkBox_outlook.isChecked()) {
                String popServer = baseConfiguration.selectSingleNode("/" + BasicConfiguration.rootTag + "/" + BasicConfiguration.Tags.EmailClient + "/" + EmailClientColumn.PopServer).getStringValue();
                String smtpServer = baseConfiguration.selectSingleNode("/" + BasicConfiguration.rootTag + "/" + BasicConfiguration.Tags.EmailClient + "/" + EmailClientColumn.SmtpServer).getStringValue();
                fr.write("cscript " + System.getProperty("user.dir") + "\\scripts\\ConfigureOE.vbs " + "\"" + name + "\" " + username + " " + email + " " + popServer + " " + smtpServer + "\r\n");
            }
            if (ui.checkBox_shortcut.isChecked()) {
                list = baseConfiguration.selectNodes("/" + BasicConfiguration.rootTag + "/" + BasicConfiguration.Tags.Shortcut);
                for (Iterator iter = list.iterator(); iter.hasNext(); ) {
                    Element e = (Element) iter.next();
                    fr.write("cscript " + System.getProperty("user.dir") + "\\scripts\\Shortcut.vbs ");
                    for (Iterator subiter = e.elementIterator(); subiter.hasNext(); ) {
                        Element j = (Element) subiter.next();
                        fr.write("\"" + j.getStringValue() + "\" ");
                        j = (Element) subiter.next();
                        fr.write(j.getStringValue());
                    }
                    fr.write("\r\n");
                }
            }
            for (SoftwareSelection ssl : softwareCheckBoxes) {
                if (ssl.checkBox.isChecked()) {
                    fr.write("cscript " + System.getProperty("user.dir") + "\\scripts\\Execute.js " + ssl.path + " " + ssl.file + "\r\n");
                }
            }
            fr.flush();
            fr.close();
            Runtime.getRuntime().exec("cmd /c start " + f.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Apply button clicked.
	 */
    private void applyButtonClicked() {
        applyConfiguration();
    }

    /**
	 * Establishes a SSL connection to the server. The server's address and TCP port 
	 * is taken from the server configuration file
	 * 
	 * @see pdc.xml.serveraddress
	 */
    protected boolean connectToServer() {
        if (!connectionEstablished) {
            final Protocol ptrcl = Protocol.instance;
            try {
                ptrcl.populateTrustManager("servertrust", "iuautoconfig");
                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, ptrcl.getTrustManagerFactory().getTrustManagers(), null);
                SSLSocketFactory ssf = sslContext.getSocketFactory();
                serverConnection = (SSLSocket) ssf.createSocket(serverConfig.getIp(), serverConfig.getStudentPort());
                serverConnection.startHandshake();
            } catch (Exception e) {
                e.printStackTrace();
                Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).log(Level.SEVERE, e.getMessage());
            }
            connectionEstablished = true;
        }
        return connectionEstablished;
    }

    /**
	 * Closes the SSL connection to the server
	 */
    protected void disconnectFromServer() {
        if (serverConnection.isConnected()) {
            System.out.println("Disconnect!");
            try {
                Protocol.instance.serializeMessageString(serverConnection.getOutputStream(), ProtocolCommand.Disconnect, ProtocolMessageType.Request, "Disconnect");
                serverConnection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
	 * Checks if is serverreachable.
	 * 
	 * @return true, if is serverreachable
	 */
    private boolean isServerreachable() {
        try {
            return !InetAddress.getByName(serverConfig.getIp()).isReachable(3000);
        } catch (IOException e) {
            return true;
        }
    }

    /**
	 * Loads the configuration file from the server
	 * 
	 * @return configuration file
	 */
    private File getConfigScript() {
        return (this.receivedConfig == null ? new File("xmlinstances/DefaultConfiguration.xml") : this.receivedConfig);
    }

    /**
	 * Opens and parses the configration file
	 * 
	 * @param configration file
	 *        
	 * @return true, if successful
	 */
    protected boolean parseConfigScript(File xml_file) {
        try {
            SAXReader reader = new SAXReader();
            baseConfiguration = reader.read(xml_file);
            String xml_root = BasicConfiguration.rootTag;
            name = baseConfiguration.selectSingleNode("/" + xml_root + "/" + BasicConfiguration.Tags.UserInformation + "/" + SQLConnection.StudentColumn.Name).getText();
            program = baseConfiguration.selectSingleNode("/" + xml_root + "/" + BasicConfiguration.Tags.UserInformation + "/" + SQLConnection.StudentColumn.Program).getText();
            year = baseConfiguration.selectSingleNode("/" + xml_root + "/" + BasicConfiguration.Tags.UserInformation + "/" + SQLConnection.StudentColumn.Year).getText();
            System.out.println("Name: " + name + " Prgm: " + program + " Year: " + year);
            final List<Element> software = baseConfiguration.selectNodes("/" + xml_root + "/" + BasicConfiguration.Tags.Program);
            for (Element s : software) {
                SoftwareSelection softsel = new SoftwareSelection();
                softsel.name = s.elementText(SQLConnection.ProgramColumn.Name.toString());
                softsel.checkBox = new QCheckBox(ui.groupBox_addSoftware);
                softsel.checkBox.setText(softsel.name);
                softsel.checkBox.setChecked(true);
                softsel.file = s.elementText(SQLConnection.ProgramColumn.File.toString());
                softsel.path = s.elementText(SQLConnection.ProgramColumn.Path.toString());
                softwareLayouter.addWidget(softsel.checkBox);
                softwareCheckBoxes.add(softsel);
            }
            softwareLayouter.addItem(new QSpacerItem(20, 40, com.trolltech.qt.gui.QSizePolicy.Policy.Minimum, com.trolltech.qt.gui.QSizePolicy.Policy.Expanding));
            ui.input_name.setText(name);
            ui.input_program.setText(program);
            ui.input_year.setText(year);
            ui.tab_2.activateWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
	 * First setup.
	 * 
	 * @return true, if successful
	 */
    private boolean firstSetup() {
        try {
            Runtime.getRuntime().exec(config.getFirstConfigScript());
        } catch (Exception e) {
            System.err.println("Error: Can't start config script.");
        }
        File macAdresses = new File(config.getMacAddressFileName());
        if (macAdresses.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
