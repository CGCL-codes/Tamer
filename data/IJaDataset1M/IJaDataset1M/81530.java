package gr.ageorgiadis.signature;

import gr.ageorgiadis.util.HexStringHelper;
import gr.ageorgiadis.util.applet.Environment;
import gr.ageorgiadis.util.browser.BrowserKeyStoreFactory;
import gr.ageorgiadis.util.browser.FormParser;
import gr.ageorgiadis.util.browser.UserAgentBrowserDetector;
import gr.ageorgiadis.util.ini.Parser.MalformedException;
import gr.ageorgiadis.util.security.KeyStoreHelper;
import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import netscape.javascript.JSException;
import netscape.javascript.JSObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.sun.java.browser.dom.DOMAccessException;
import com.sun.java.browser.dom.DOMService;
import com.sun.java.browser.dom.DOMUnsupportedException;

/**
 * <p>Known issues:</p>
 * <ul>
 * <li>File upload fields don't pass the full filename from the value field
 * on Firefox 3.0</li>
 * <li>Null/empty values are by default excluded; configure this behavior
 * globally</li>
 * </ul>
 * 
 * @author ageorgiadis
 */
public class DSApplet extends JApplet {

    private static final long serialVersionUID = 3617857231362031733L;

    private static final String DSAPPLET_VERSION = "1.2.2-20090528";

    private static final Log logger = LogFactory.getLog(DSApplet.class);

    @Override
    public String[][] getParameterInfo() {
        return new String[][] { { "backgroundColor", "String", "background color code, in hexadecimal format; defaults to white (#FFFFFF)" }, { "formId", "String", "id of the HTML form to digitally sign" }, { "flags", "String, comma-delimited", "list of flags to pass to the signature strategy object" }, { "signatureAlgorithm", "String", "name of the signature algorithm; defaults to xmldsig" }, { "signatureElement", "String", "name of the element to store the signature" }, { "plaintextElement", "String", "name of the element to store the plaintext" }, { "serialNumberElement", "String", "name of the element to store the serial number" }, { "serialNumberInHexadecimal", "boolean", "true to store serial number in hexadecimal format; false otherwise (default)" }, { "excludedElements", "String, comma-delimited", "list of names of form elements to exclude" }, { "includedElements", "String, comma-delimited", "list of names of form elements to include" }, { "successJSFunction", "String", "name of JS function to execute on succesful signing; called with no arguments (i.e. onSuccess(); )" }, { "errorJSFunction", "String", "name of JS function to execute on failed signing; called with no arguments (i.e. onError(); )" }, { "expirationDateChecked", "boolean", "true to check certificate's expiration date (default); false otherwise" }, { "issuerNameRegex", "String", "regular expression to match issuer's name for acceptance" }, { "subjectNameRegex", "String", "regular expression to match certificate's name in subject" }, { "subjectFriendlyRegex", "String", "regular expression to match certificate's friendly name in subject" }, { "serialNumbersAllowed", "String, comma-delimited", "list of serial numbers to allow for selection" } };
    }

    private String backgroundColor = "#FFFFFF";

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    private String formId = null;

    public void setFormId(String formId) {
        this.formId = formId;
    }

    private String flags = null;

    public void setFlags(String flags) {
        this.flags = flags;
    }

    private String signatureAlgorithm = "SHA1withRSA";

    public void setSignatureAlgorithm(String signatureAlgorithm) {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    private String signatureElement;

    public void setSignatureElement(String signatureElement) {
        this.signatureElement = signatureElement;
    }

    public String getSignatureElement() {
        return signatureElement;
    }

    private String plaintextElement;

    public void setPlaintextElement(String plaintextElement) {
        this.plaintextElement = plaintextElement;
    }

    public String getPlaintextElement() {
        return plaintextElement;
    }

    private String serialNumberElement;

    public void setSerialNumberElement(String serialNumberElement) {
        this.serialNumberElement = serialNumberElement;
    }

    public String getSerialNumberElement() {
        return serialNumberElement;
    }

    private boolean serialNumberInHexadecimal = false;

    public void setSerialNumberInHexadecimal(boolean serialNumberInHexadecimal) {
        this.serialNumberInHexadecimal = serialNumberInHexadecimal;
    }

    private String excludedElements = null;

    public void setExcludedElements(String excludedElements) {
        this.excludedElements = excludedElements;
    }

    private String includedElements = null;

    public void setIncludedElements(String includedElements) {
        this.includedElements = includedElements;
    }

    private Set<String> excludedElementsSet = null;

    private Set<String> includedElementsSet = null;

    public boolean isElementExcluded(String element) {
        if (excludedElementsSet == null) {
            excludedElementsSet = new HashSet<String>();
            if (excludedElements != null) {
                String[] names = excludedElements.split(",");
                for (String name : names) {
                    excludedElementsSet.add(name.trim());
                }
            }
        }
        if (includedElementsSet == null) {
            includedElementsSet = new HashSet<String>();
            if (includedElements != null) {
                String[] names = includedElements.split(",");
                for (String name : names) {
                    includedElementsSet.add(name.trim());
                }
            }
        }
        return excludedElementsSet.contains(element) || (!includedElementsSet.isEmpty() && !includedElementsSet.contains(element));
    }

    private String successJSFunction = null;

    public String getSuccessJSFunction() {
        return successJSFunction;
    }

    public void setSuccessJSFunction(String submitFunction) {
        this.successJSFunction = submitFunction;
    }

    private String errorJSFunction = null;

    public String getErrorJSFunction() {
        return errorJSFunction;
    }

    public void setErrorJSFunction(String errorFunction) {
        this.errorJSFunction = errorFunction;
    }

    private boolean expirationDateChecked = true;

    public void setExpirationDateChecked(boolean expirationDateChecked) {
        this.expirationDateChecked = expirationDateChecked;
    }

    private String issuerNameRegex = null;

    public void setIssuerNameRegex(String issuerNameRegex) {
        this.issuerNameRegex = issuerNameRegex;
    }

    private Pattern issuerNamePattern = null;

    private Pattern getIssuerNamePattern() {
        if (issuerNamePattern == null && issuerNameRegex != null) {
            issuerNamePattern = Pattern.compile(issuerNameRegex);
        }
        return issuerNamePattern;
    }

    private String subjectNameRegex = null;

    public void setSubjectNameRegex(String subjectNameRegex) {
        this.subjectNameRegex = subjectNameRegex;
    }

    private String subjectFriendlyRegex = null;

    public void setSubjectFriendlyRegex(String subjectFriendlyRegex) {
        this.subjectFriendlyRegex = subjectFriendlyRegex;
    }

    private String serialNumbersAllowed = null;

    public void setSerialNumbersAllowed(String serialNumbersAllowed) {
        this.serialNumbersAllowed = serialNumbersAllowed;
    }

    private Set<BigInteger> serialNumbersAllowedSet = null;

    public Set<BigInteger> getSerialNumbersAllowedSet() {
        if (serialNumbersAllowedSet == null && serialNumbersAllowed != null) {
            String[] serialNumbers = serialNumbersAllowed.split(",");
            serialNumbersAllowedSet = new HashSet<BigInteger>();
            for (String serialNumber : serialNumbers) {
                serialNumbersAllowedSet.add(new BigInteger(serialNumber));
            }
        }
        return serialNumbersAllowedSet;
    }

    private ResourceBundle messages = ResourceBundle.getBundle("messages", ResourceBundle.Control.getControl(ResourceBundle.Control.FORMAT_PROPERTIES));

    @Override
    public String getAppletInfo() {
        return "Digital Signature Applet - " + DSAPPLET_VERSION + "\nhttp://dsig.sourceforge.net";
    }

    private UserAgentBrowserDetector browserDetector = new UserAgentBrowserDetector();

    private void initSwing() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            logger.warn("UIManager.setLookAndFeel() failed", e);
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
        panel.setBackground(Color.decode(backgroundColor));
        add(panel);
        boolean lockPrinted = false;
        if (formId != null) {
            Icon lockIcon = new ImageIcon(getClass().getResource("/icons/lock.png"));
            lockPrinted = true;
            JButton button = new JButton("Sign", lockIcon);
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    try {
                        signInternal(formId);
                    } catch (Exception ex) {
                    }
                }
            });
            panel.add(button);
        }
        Icon infoIcon = new ImageIcon(getClass().getResource(lockPrinted ? "/icons/info.png" : "/icons/lock.png"));
        JLabel infoLabel = new JLabel(infoIcon);
        infoLabel.addMouseListener(new MouseListener() {

            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, printInfoMessage());
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }
        });
        panel.add(infoLabel);
    }

    @Override
    public void init() {
        super.init();
        if (Boolean.getBoolean("debug")) {
            System.out.println("\n*** Debug log enabled ***");
            Logger.getLogger("").getHandlers()[0].setLevel(Level.FINEST);
            Logger.getLogger("").setLevel(Level.INFO);
            Logger.getLogger("gr.ageorgiadis").setLevel(Level.FINEST);
        }
        Environment.getSingleton().setApplet(this);
        Environment.getSingleton().init(this);
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    initSwing();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            browserDetector.initialize(JSObject.getWindow(DSApplet.this));
        } catch (JSException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
                showStatus("Digital Signature Applet - " + DSAPPLET_VERSION);
            }
        }).start();
    }

    public boolean sign(final String formId) {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    if (!signInternal(formId)) {
                        throw new RuntimeException("So that catch() below is invoked");
                    }
                }
            });
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean signInternal(final String formId) throws RuntimeException {
        try {
            final KeyStoreHelper ksh;
            final Map<String, X509Certificate[]> aliasX509CertificateChainPair = new HashMap<String, X509Certificate[]>();
            final KeyStore ks = BrowserKeyStoreFactory.getInstance().createKeyStore(browserDetector);
            ksh = new KeyStoreHelper(ks);
            Set<String> aliases = ksh.aliases();
            for (String alias : aliases) {
                X509Certificate[] certificateChain = ksh.getX509CertificateChain(alias);
                if (certificateChain == null || certificateChain.length == 0) {
                    logger.warn("Null certificate chain returned; alias=" + alias);
                    continue;
                }
                X509Certificate certificate = certificateChain[0];
                String subjectName = certificate.getSubjectX500Principal().getName();
                String issuerName = certificate.getIssuerX500Principal().getName();
                BigInteger serialNumber = certificate.getSerialNumber();
                if (getIssuerNamePattern() != null && !getIssuerNamePattern().matcher(issuerName).matches()) {
                    logger.info("Issuer does not match; skipping" + ": certificate.subject=" + subjectName + ", certificate.serialNumber=" + serialNumber);
                    continue;
                }
                if (getSerialNumbersAllowedSet() != null && !getSerialNumbersAllowedSet().contains(serialNumber)) {
                    logger.info("Serial number is not allowed; skipping" + ": certificate.subject=" + subjectName + ", certificate.serialNumber=" + serialNumber);
                    continue;
                }
                if (!ksh.isKeyEntry(alias)) {
                    logger.info("Private key not found; skipping" + ": certificate.subject=" + subjectName + ", certificate.serialNumber=" + serialNumber);
                    continue;
                }
                aliasX509CertificateChainPair.put(alias, ksh.getX509CertificateChain(alias));
            }
            SelectCertificateDialog scd = new SelectCertificateDialog(new CertificateTableModel(aliasX509CertificateChainPair, subjectNameRegex, subjectFriendlyRegex), ks.getProvider().getName(), expirationDateChecked);
            scd.setModalityType(ModalityType.APPLICATION_MODAL);
            scd.setVisible(true);
            String alias = scd.getSelectedAlias();
            if (alias == null) {
                return false;
            }
            SignatureStrategy strategy = SignatureStrategy.getInstance(signatureAlgorithm);
            strategy.setFlags(flags);
            strategy.setPrivateKey(ksh.getPrivateKey(alias, null));
            strategy.setX509Certificate(scd.getSelectedX509Certificate());
            FormParser parser = new FormParser(this, formId);
            parser.setElementHandler(strategy.getElementHandler());
            DOMService.getService(this).invokeAndWait(parser.getParsingDOMAction());
            JSObject win = JSObject.getWindow(DSApplet.this);
            if (plaintextElement != null) {
                String command = getDOMExpression(formId, plaintextElement) + ".value = \"" + strategy.getPlaintext() + "\";";
                win.eval(command);
            } else {
                logger.warn("plaintextElement not set");
            }
            String serialNumberAsString = serialNumberInHexadecimal ? HexStringHelper.toHexString(scd.getSelectedX509Certificate().getSerialNumber().toByteArray()) : "" + scd.getSelectedX509Certificate().getSerialNumber();
            if (serialNumberElement != null) {
                String command = getDOMExpression(formId, serialNumberElement) + ".value = '" + serialNumberAsString + "';";
                win.eval(command);
            } else {
                logger.warn("serialNumberElement not set");
            }
            if (signatureElement != null) {
                String command = getDOMExpression(formId, signatureElement) + ".value = '" + strategy.getSignature() + "';";
                win.eval(command);
            } else {
                logger.warn("signatureElement not set");
            }
            if (getSuccessJSFunction() != null) {
                win.eval(getSuccessJSFunction() + "();");
            } else {
                logger.debug("successJSFunction not set");
            }
            return true;
        } catch (KeyStoreException ex) {
            handleError("DSA0001", ex);
        } catch (NoSuchProviderException ex) {
            handleError("DSA0002", ex);
        } catch (NoSuchAlgorithmException ex) {
            handleError("DSA0003", ex);
        } catch (CertificateException ex) {
            handleError("DSA0004", ex);
        } catch (IOException ex) {
            handleError("DSA0005", ex);
        } catch (IllegalArgumentException ex) {
            handleError("DSA0006", ex);
        } catch (IllegalAccessException ex) {
            handleError("DSA0006", ex);
        } catch (UnrecoverableKeyException ex) {
            handleError("DSA0007", ex);
        } catch (DOMUnsupportedException ex) {
            handleError("DSA0008", ex);
        } catch (DOMAccessException ex) {
            handleError("DSA0009", ex);
        } catch (SignatureException ex) {
            handleError(ex.getErrorCode(), ex.getCause());
        } catch (MalformedException ex) {
            handleError("DSA9999", ex);
        } catch (RuntimeException ex) {
            handleError("DSA9999", ex);
        }
        return false;
    }

    private String printInfoMessage() {
        StringBuilder sb = new StringBuilder();
        sb.append(getAppletInfo());
        sb.append("\n\n");
        sb.append(System.getProperty("java.vm.name") + "; " + System.getProperty("java.vm.version") + ", " + System.getProperty("java.vm.vendor") + "\n");
        sb.append("\nOS detected: " + System.getProperty("os.name") + " " + System.getProperty("os.arch") + " (" + System.getProperty("os.version") + ")\n");
        sb.append("Browser detected: " + browserDetector.getBrowser() + "\n");
        sb.append("Signature Strategy: " + signatureAlgorithm + "\n");
        sb.append("\nIcons provided by: FAMFAMFAM\n");
        return sb.toString();
    }

    private String getDOMExpression(String formId, String element) {
        return "document.forms[\"" + formId + "\"].elements[\"" + element + "\"]";
    }

    private void handleError(String errorCode, Throwable cause) {
        ErrorDialog ed = new ErrorDialog(errorCode, cause, messages);
        ed.setModalityType(ModalityType.APPLICATION_MODAL);
        ed.setVisible(true);
        JSObject win = JSObject.getWindow(DSApplet.this);
        if (getErrorJSFunction() != null) {
            win.eval(getErrorJSFunction() + "(" + errorCode + ");");
        } else {
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else {
                throw new RuntimeException(cause);
            }
        }
    }
}
