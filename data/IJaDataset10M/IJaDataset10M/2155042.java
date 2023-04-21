package net.sf.traser.configtool;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout.Alignment;
import javax.swing.text.JTextComponent;
import net.sf.traser.common.DistinguishedName;
import net.sf.traser.common.KeystoreFault;
import net.sf.traser.common.KeystoreManager;
import net.sf.traser.common.LabelManager;

/**
 * Key generator frame, allowing the input of the parameters
 * required by a self signed RSA key. Allows
 * the user to type in the components of a distinguished name.
 * For details, @see DistinguishedName class
 * @author karnokd, 2007.12.07.
 * @version $Revision 1.0$
 */
public class KeygenFrame extends JDialog implements ActionListener {

    /**
	 * The logger object.
	 */
    private static final Logger LOG = Logger.getLogger(KeygenFrame.class.getName());

    /**
	 * 
	 */
    private static final long serialVersionUID = -7639261386059718851L;

    /**
	 * The keystore manager.
	 */
    private transient KeystoreManager mgr;

    /** The alias field. */
    private JTextField alias;

    /** The password field. */
    private JPasswordField password;

    /** The common name field. */
    private JTextField commonName;

    /** The organization unit field. */
    private JTextField organizationUnit;

    /** The organization field. */
    private JTextField organization;

    /** The location field. */
    private JTextField location;

    /** The state field. */
    private JTextField state;

    /** The country field. */
    private JTextField country;

    /** The generate button. */
    private JButton generateButton;

    /** The cancel button. */
    private JButton cancelButton;

    /** The domain name. */
    private JTextField domain;

    /** The label manager. */
    private LabelManager labels;

    /** The password re-entered. */
    private JPasswordField passwordAgain;

    /**
	 * Constructor. Initializes the private fields and creates
	 * the GUI layout.
	 * @param mgr the keystore manager object, cannot be null and must be initialized
	 */
    public KeygenFrame(KeystoreManager mgr) {
        this(mgr, LabelManager.getLabelManager());
    }

    /**
	 * Constructor. Initializes the private fields and the GUI.
	 * @param mgr the keystore manager
	 * @param labels the GUI labels
	 */
    public KeygenFrame(KeystoreManager mgr, LabelManager labels) {
        super();
        this.mgr = mgr;
        this.labels = labels;
        initialize();
    }

    /**
	 * Initialize the GUI.
	 */
    private void initialize() {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setModalityType(ModalityType.APPLICATION_MODAL);
        setTitle(labels.get("keystoremanager_genkey_title"));
        Container c = getContentPane();
        GroupLayout gl = new GroupLayout(c);
        c.setLayout(gl);
        gl.setAutoCreateContainerGaps(true);
        gl.setAutoCreateGaps(true);
        alias = new JTextField(15);
        password = new JPasswordField(15);
        passwordAgain = new JPasswordField(15);
        commonName = new JTextField(30);
        organizationUnit = new JTextField(30);
        organization = new JTextField(30);
        location = new JTextField(30);
        state = new JTextField(30);
        country = new JTextField(30);
        domain = new JTextField(30);
        JLabel lblAlias = new JLabel(labels.get("keystoremanager_genkey_alias"));
        lblAlias.setLabelFor(alias);
        JLabel lblPassword = new JLabel(labels.get("keystoremanager_genkey_password"));
        lblPassword.setLabelFor(password);
        JLabel lblPasswordAgain = new JLabel(labels.get("keystoremanager_genkey_password_again"));
        lblPasswordAgain.setLabelFor(passwordAgain);
        JLabel lblCommonName = new JLabel(labels.get("keystoremanager_genkey_commonname"));
        lblCommonName.setLabelFor(commonName);
        JLabel lblOrganizationUnit = new JLabel(labels.get("keystoremanager_genkey_orgunit"));
        lblOrganizationUnit.setLabelFor(organizationUnit);
        JLabel lblOrganization = new JLabel(labels.get("keystoremanager_genkey_org"));
        lblOrganization.setLabelFor(organization);
        JLabel lblLocation = new JLabel(labels.get("keystoremanager_genkey_location"));
        lblLocation.setLabelFor(location);
        JLabel lblState = new JLabel(labels.get("keystoremanager_genkey_state"));
        lblState.setLabelFor(state);
        JLabel lblCountry = new JLabel(labels.get("keystoremanager_genkey_country"));
        lblCountry.setLabelFor(country);
        JLabel lblDomain = new JLabel(labels.get("keystoremanager_genkey_domain"));
        lblDomain.setLabelFor(domain);
        algorithm = new JTextField("RSA", 15);
        algorithm.setEditable(false);
        algorithm.setFocusable(false);
        keysize = new JFormattedTextField(1024);
        keysize.setColumns(15);
        keysize.setEditable(false);
        keysize.setFocusable(false);
        JLabel lblAlgorithm = new JLabel(labels.get("keystoremanager_genkey_algorithm"));
        lblAlgorithm.setDisplayedMnemonic('g');
        lblAlgorithm.setLabelFor(algorithm);
        JLabel lblKeysize = new JLabel(labels.get("keystoremanager_genkey_keysize"));
        lblKeysize.setDisplayedMnemonic('k');
        lblKeysize.setLabelFor(keysize);
        generateButton = new JButton(labels.get("keystoremanager_genkey_generate"));
        generateButton.addActionListener(this);
        cancelButton = new JButton(labels.get("keystoremanager_genkey_cancel"));
        cancelButton.addActionListener(this);
        gl.setHorizontalGroup(gl.createParallelGroup(Alignment.CENTER).addGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(lblAlias).addComponent(lblPassword).addComponent(lblPasswordAgain).addComponent(lblAlgorithm).addComponent(lblKeysize).addComponent(lblCommonName).addComponent(lblOrganizationUnit).addComponent(lblOrganization).addComponent(lblLocation).addComponent(lblState).addComponent(lblCountry).addComponent(lblDomain)).addGroup(gl.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(alias, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(password, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(passwordAgain, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(algorithm, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(keysize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(commonName, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(organizationUnit, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(organization, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(location, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(state, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(country, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE).addComponent(domain, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).addGroup(gl.createSequentialGroup().addComponent(generateButton).addComponent(cancelButton)));
        gl.setVerticalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblAlias).addComponent(alias)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblPassword).addComponent(password)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblPasswordAgain).addComponent(passwordAgain)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblAlgorithm).addComponent(algorithm)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblKeysize).addComponent(keysize)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblCommonName).addComponent(commonName)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblOrganizationUnit).addComponent(organizationUnit)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblOrganization).addComponent(organization)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblLocation).addComponent(location)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblState).addComponent(state)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblCountry).addComponent(country)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(lblDomain).addComponent(domain)).addGroup(gl.createParallelGroup(Alignment.BASELINE).addComponent(generateButton).addComponent(cancelButton)));
        gl.linkSize(SwingConstants.HORIZONTAL, generateButton, cancelButton);
        pack();
    }

    /**
	 * Sample program.
	 * @param args the arguments
	 */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                KeystoreManager km = new KeystoreManager();
                km.load("conf/service.jks", "apache".toCharArray());
                KeygenFrame f = new KeygenFrame(km);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            }
        });
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancelButton) {
            accepted = false;
            dispose();
            return;
        }
        if (e.getSource() == generateButton) {
            if (generateKeypair()) {
                accepted = true;
                dispose();
            }
            return;
        }
    }

    /**
	 * Did the user click on generate button?
	 */
    private boolean accepted;

    /** The algorithm field. */
    private JTextField algorithm;

    /** The keysize field. */
    private JFormattedTextField keysize;

    /**
	 * Show the dialog and return the user selection results.
	 * @return boolean did the user manage to generate a new keypair?
	 */
    public boolean showModal() {
        clear();
        setVisible(true);
        return accepted;
    }

    /**
	 * Show the dialog and return the user selection results.
	 * @param name the fixed user name to use
	 * @return boolean did the user manage to generate a new keypair?
	 */
    public boolean showModal(String name) {
        clear();
        alias.setText(name);
        alias.setEditable(false);
        setVisible(true);
        return accepted;
    }

    /**
	 * Clear the input field values.
	 */
    private void clear() {
        accepted = false;
        alias.setText(null);
        alias.setEditable(true);
        password.setText(null);
        passwordAgain.setText(null);
        commonName.setText(null);
        organizationUnit.setText(null);
        organization.setText(null);
        location.setText(null);
        state.setText(null);
        country.setText(null);
    }

    /**
	 * @return did the user manage to create a keypair?
	 */
    public boolean isAccepted() {
        return accepted;
    }

    /**
	 * Generate the RSA key pair and save it into the keystore.
	 * Error conditions are presented as error boxes allowing
	 * the user to edit the fields or cancel the entire operation  
	 * @return true if the operation succeded without any error
	 */
    private boolean generateKeypair() {
        if (alias.getText() == null || alias.getText().isEmpty()) {
            int opt = JOptionPane.showConfirmDialog(this, labels.get("keystoremanager_genkey_noalias"), getTitle(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            if (opt != JOptionPane.OK_OPTION) {
                cancelButton.doClick();
            }
            return false;
        }
        char[] password1 = password.getPassword();
        char[] password2 = passwordAgain.getPassword();
        if (!Arrays.equals(password1, password2)) {
            JOptionPane.showMessageDialog(this, labels.get("keystoremanager_genkey_password_diff"), getTitle(), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (isEmpty(commonName) && isEmpty(organizationUnit) && isEmpty(organization) && isEmpty(location) && isEmpty(state) && isEmpty(country)) {
            int opt = JOptionPane.showConfirmDialog(this, labels.get("keystoremanager_genkey_dnerror"), getTitle(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            if (opt != JOptionPane.OK_OPTION) {
                cancelButton.doClick();
            }
            return false;
        }
        if (country.getText().length() > 0 && country.getText().length() != 2) {
            int opt = JOptionPane.showConfirmDialog(this, labels.get("keystoremanager_genkey_sterror"), getTitle(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            if (opt != JOptionPane.OK_OPTION) {
                cancelButton.doClick();
            }
            return false;
        }
        String dn = new DistinguishedName(commonName.getText(), organizationUnit.getText(), organization.getText(), location.getText(), state.getText(), country.getText()).toString();
        try {
            mgr.generateRSACert(alias.getText(), dn, dn, domain.getText(), password.getPassword());
        } catch (KeystoreFault ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
            String message = null;
            if (ex.getCause() != null) {
                message = labels.format("keystoremanager_genkey_operror", ex.toString());
            } else {
                message = labels.format("keystoremanager_genkey_operror_cause", ex.toString(), ex.getCause().toString());
            }
            int opt = JOptionPane.showConfirmDialog(this, message, getTitle(), JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE);
            if (opt != JOptionPane.OK_OPTION) {
                cancelButton.doClick();
            }
            return false;
        }
        return true;
    }

    /**
	 * @return the preset password.
	 */
    public char[] getPassword() {
        return password.getPassword();
    }

    /**
	 * Check if the given editor field is empty.
	 * @param field the editor
	 * @return true if it is empty
	 */
    protected boolean isEmpty(JTextComponent field) {
        return field.getText() == null || field.getText().isEmpty();
    }
}
