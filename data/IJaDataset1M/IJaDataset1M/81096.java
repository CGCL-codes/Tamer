package de.flingelli.scrum.gui.options;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.xml.bind.JAXBException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.flingelli.sab.BugzillaConnector;
import de.flingelli.sab.Product;
import de.flingelli.scrum.Global;
import de.flingelli.scrum.datastructure.gui.BugzillaConnectionData;
import de.flingelli.scrum.language.JastTranslation;
import de.flingelli.scrum.observer.ProductPropertyChangeSupport;

/**
 * 
 * @author Markus Flingelli
 * 
 */
@SuppressWarnings("serial")
public final class BugzillaPanel extends AOptionsPanel {

    private static final int COMPONENT_WIDTH_100 = 100;

    private static final int COMPONENT_WIDTH_200 = 200;

    private static final int COMPONENT_WIDTH_175 = 175;

    private static final int COMPONENT_HEIGHT = 20;

    private static final Logger LOGGER = LoggerFactory.getLogger(BugzillaPanel.class);

    private static final String PREFIX = "de.flingelli.scrum.gui.options.BugzillaPanel-";

    private JTextField urlTextField;

    private JTextField loginTextField;

    private JPasswordField passwordTextField;

    private JTextField productTextField;

    private JLabel urlLabel;

    private JLabel loginLabel;

    private JLabel passwordLabel;

    private JLabel productLabel;

    private JButton testButton;

    private final JastTranslation translation;

    public BugzillaPanel() {
        translation = JastTranslation.getInstance();
        ProductPropertyChangeSupport.getInstance().addPropertyChangeListener(this);
        initialize();
        dispalyData();
        changeLanguage();
    }

    private void initialize() {
        setLayout(null);
        urlLabel = new JLabel("URL");
        urlLabel.setBounds(10, 5, COMPONENT_WIDTH_175, COMPONENT_HEIGHT);
        add(urlLabel);
        urlTextField = new JTextField();
        urlTextField.setName("url_textfield");
        urlTextField.setBounds(COMPONENT_WIDTH_200, 5, 300, COMPONENT_HEIGHT);
        add(urlTextField);
        urlTextField.setColumns(10);
        loginLabel = new JLabel("Login");
        loginLabel.setBounds(10, 35, COMPONENT_WIDTH_175, COMPONENT_HEIGHT);
        add(loginLabel);
        loginTextField = new JTextField();
        loginTextField.setName("login_textfield");
        loginTextField.setBounds(COMPONENT_WIDTH_200, 35, COMPONENT_WIDTH_100, COMPONENT_HEIGHT);
        add(loginTextField);
        loginTextField.setColumns(10);
        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 65, COMPONENT_WIDTH_175, COMPONENT_HEIGHT);
        add(passwordLabel);
        passwordTextField = new JPasswordField();
        passwordTextField.setName("password_textfield");
        passwordTextField.setBounds(COMPONENT_WIDTH_200, 65, COMPONENT_WIDTH_100, COMPONENT_HEIGHT);
        add(passwordTextField);
        passwordTextField.setColumns(10);
        productLabel = new JLabel("Product name");
        productLabel.setBounds(10, 95, COMPONENT_WIDTH_175, COMPONENT_HEIGHT);
        add(productLabel);
        productTextField = new JTextField();
        productTextField.setName("product_textfield");
        productTextField.setBounds(COMPONENT_WIDTH_200, 95, COMPONENT_WIDTH_200, COMPONENT_HEIGHT);
        add(productTextField);
        productTextField.setColumns(10);
        testButton = new JButton("Test Connection");
        testButton.setName("test_connection_button");
        testButton.setBounds(410, 95, 170, COMPONENT_HEIGHT);
        testButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                testBugzillaConnection();
            }
        });
        add(testButton);
    }

    private void dispalyData() {
        BugzillaConnectionData data = new BugzillaConnectionData();
        try {
            File file = new File(Global.getBugzillaSettingsFileName());
            if (file.exists()) {
                data.readXML(Global.getBugzillaSettingsFileName());
            }
            urlTextField.setText(data.getUrl());
            loginTextField.setText(data.getLogin());
            passwordTextField.setText(data.getPassword());
            productTextField.setText(data.getProduct());
        } catch (FileNotFoundException e) {
            LOGGER.error("File not found.", e);
        } catch (JAXBException e) {
            LOGGER.error("XML error.", e);
        }
    }

    private void testBugzillaConnection() {
        BugzillaConnectionData data = getData();
        BugzillaConnector connector = new BugzillaConnector(data.getUrl(), data.getLogin(), data.getPassword());
        List<Product> products = connector.getProducts();
        if (products.size() > 0) {
            JOptionPane.showMessageDialog(null, translation.getValue(PREFIX + "ConnectionSuccessfulText"), translation.getValue(PREFIX + "ConnectionSuccessfulTitle"), JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, translation.getValue(PREFIX + "ConnectionErrorText"), translation.getValue(PREFIX + "ConnectionErrorTitle"), JOptionPane.ERROR_MESSAGE);
        }
    }

    BugzillaConnectionData getData() {
        BugzillaConnectionData data = new BugzillaConnectionData();
        data.setLogin(loginTextField.getText());
        data.setProduct(productTextField.getText());
        data.setUrl(urlTextField.getText().trim());
        if (!data.getUrl().endsWith("/")) {
            data.setUrl(data.getUrl() + "/");
            data.setUrl(data.getUrl().trim());
        }
        StringBuffer buffer = new StringBuffer();
        buffer.append(passwordTextField.getPassword());
        data.setPassword(buffer.toString());
        return data;
    }

    @Override
    protected void changeLanguage() {
        urlLabel.setText(translation.getValue(PREFIX + "URL"));
        loginLabel.setText(translation.getValue(PREFIX + "Login"));
        passwordLabel.setText(translation.getValue(PREFIX + "Password"));
        productLabel.setText(translation.getValue(PREFIX + "Product"));
        testButton.setText(translation.getValue(PREFIX + "TestConnection"));
    }
}
