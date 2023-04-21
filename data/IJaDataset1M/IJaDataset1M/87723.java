package filho.heliton.gman.config;

import filho.heliton.gman.feed.Account;
import filho.heliton.gman.feed.Label;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import javax.swing.JOptionPane;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class ConfigLoader {

    private Element root;

    public static final String configFileName = "config.xml";

    public static final String configFilePath = System.getProperty("user.home") + "/.config/gman/";

    public void loadConfig() throws Exception {
        checkConfigFile();
        SAXBuilder saxBuilder = new SAXBuilder();
        Document doc = saxBuilder.build(new File(configFilePath + configFileName));
        root = doc.getRootElement();
        loadAccounts();
        loadSystem();
    }

    private boolean checkConfigFile() throws Exception {
        File configFile = new File(configFilePath + configFileName);
        if (configFile.exists() == false) {
            createConfigFile();
            String message = "Please fill the required fields in the configuration file found at [" + configFilePath + configFileName + "] then restart the application.";
            JOptionPane.showMessageDialog(null, message, "First run", JOptionPane.WARNING_MESSAGE);
            throw new NoSuchElementException(message);
        }
        return true;
    }

    private void createConfigFile() throws Exception {
        InputStream defaultConfigStream = ConfigLoader.class.getResourceAsStream(configFileName);
        File userConfigFile = new File(configFilePath + configFileName);
        new File(configFilePath).mkdirs();
        userConfigFile.createNewFile();
        OutputStream userConfigStream = new FileOutputStream(userConfigFile);
        byte buffer[] = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = defaultConfigStream.read(buffer)) > 0) {
            userConfigStream.write(buffer, 0, bytesRead);
        }
        defaultConfigStream.close();
        userConfigStream.close();
    }

    private void loadAccounts() {
        Element accountsElement = getChild(root, "accounts");
        List<Element> accountsElements = getChildren(accountsElement, "account");
        Account account = null;
        List<Account> accounts = new ArrayList<Account>();
        for (Element accountElement : accountsElements) {
            account = new Account();
            account.setName(getChildText(accountElement, "name"));
            account.setUsername(getChildText(accountElement, "username"));
            account.setPassword(getChildText(accountElement, "password"));
            account.setEnabled(Boolean.parseBoolean(getChildText(accountElement, "enabled")));
            account.setSeparateTrayIcon(Boolean.parseBoolean(getChildText(accountElement, "separateTrayIcon")));
            loadLabels(account, accountElement);
            accounts.add(account);
        }
        SystemConfig.accounts = accounts;
    }

    private void loadLabels(Account account, Element accountElement) {
        Element labelsElement = getChild(accountElement, "labels");
        if (labelsElement == null) {
            account.getLabels().add(new Label("Inbox"));
            return;
        }
        List labelElements = getChildren(labelsElement, "label");
        Label label = null;
        for (Object labelElement : labelElements) {
            label = new Label();
            label.setName(getChildText((Element) labelElement, "name"));
            account.getLabels().add(label);
        }
    }

    private void loadSystem() {
        Element systemElement = getChild(root, "system");
        if ("HTTPS".equalsIgnoreCase(getChildText(systemElement, "checkDelayInSeconds"))) {
            SystemConfig.httpProtocol = SystemConfig.HTTP_PROTOCOL.HTTPS;
        } else {
            SystemConfig.httpProtocol = SystemConfig.HTTP_PROTOCOL.HTTP;
        }
        SystemConfig.checkDelayInSeconds = getChildInteger(systemElement, "checkDelayInSeconds");
        SystemConfig.timeoutInSeconds = getChildInteger(systemElement, "timeoutInSeconds");
    }

    private String getChildText(Element element, String childName) {
        return element.getChildText(childName, root.getNamespace());
    }

    private int getChildInteger(Element element, String childName) {
        return Integer.parseInt(getChildText(element, childName));
    }

    private Element getChild(Element element, String childName) {
        return element.getChild(childName, root.getNamespace());
    }

    private List getChildren(Element element, String childName) {
        return element.getChildren(childName, root.getNamespace());
    }
}
