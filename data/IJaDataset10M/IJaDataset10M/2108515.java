package issrg.policywizard.slides;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ResourceBundle;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import issrg.policywizard.*;

/**
 * This class Contains the first slide the Policy Wizard Shows. 
 * <p> 
 * It just prompts the user for the type of policy that will be created. 
 * Either a Delegation Policy, or a Service Provider Policy.
 *
 * @author Christian Azzopardi
 */
public class PWizardPanel1 extends JPanel {

    private GridBagConstraints constraints;

    public JRadioButton delegationPolicy;

    public JRadioButton serviceProviderPolicy;

    ButtonGroup radioGroup;

    JLabel titleLabel;

    JLabel explainationDelegation;

    JLabel explainationServiceProvider;

    ResourceBundle rbl = ResourceBundle.getBundle("issrg/policywizard/PWApplication_i18n");

    String slideTitle = rbl.getString("Slide1_Title");

    String delegationCaption = rbl.getString("Slide1_RadioButton_Delegation");

    String serviceProviderCaption = rbl.getString("Slide1_RadioButton_Service_Provider");

    String delegationExplaination = rbl.getString("Slide1_Description_Delegation");

    String serviceProviderExplaination = rbl.getString("Slide1_Description_Service_Provider");

    char firstDelegation = rbl.getString("Slide1_RadioButton_Delegation").charAt(0);

    char firstServiceProvider = rbl.getString("Slide1_RadioButton_Service_Provider").charAt(0);

    /** 
     * Creates a new instance of PWizardPanel1 
     * <p>
     * Sets the proper labels, and Background colours.
     */
    public PWizardPanel1() {
        delegationPolicy = new JRadioButton(delegationCaption);
        delegationPolicy.setBackground(PWizard.bgcolor);
        delegationPolicy.setMnemonic(firstDelegation);
        serviceProviderPolicy = new JRadioButton(serviceProviderCaption);
        serviceProviderPolicy.setBackground(PWizard.bgcolor);
        serviceProviderPolicy.setMnemonic(firstServiceProvider);
        titleLabel = new JLabel("<HTML><B>" + slideTitle + "</B></HTML>");
        explainationDelegation = new JLabel(delegationExplaination);
        explainationServiceProvider = new JLabel(serviceProviderExplaination);
        radioGroup = new ButtonGroup();
        radioGroup.add(delegationPolicy);
        radioGroup.add(serviceProviderPolicy);
        this.setLayout(new BorderLayout());
        this.add(new PermisPolicyWizard_WestPanel(), BorderLayout.WEST);
        this.add(getContentPanel(), BorderLayout.CENTER);
    }

    /**
     * Creates a panel with the main content this slide contains.
     *
     * @return  a JPanel with the GUI components that will be displayed when 
     *          this slide is shown
     */
    public JPanel getContentPanel() {
        JPanel slide1 = new JPanel(new GridBagLayout());
        slide1.setBackground(PWizard.bgcolor);
        constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.LAST_LINE_START;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(20, 40, 0, 0);
        slide1.add(titleLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(30, 50, 0, 0);
        slide1.add(delegationPolicy, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 70, 0, 0);
        slide1.add(explainationDelegation, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(30, 50, 0, 0);
        slide1.add(serviceProviderPolicy, constraints);
        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 0;
        constraints.anchor = GridBagConstraints.CENTER;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(0, 70, 0, 0);
        slide1.add(explainationServiceProvider, constraints);
        return slide1;
    }
}
