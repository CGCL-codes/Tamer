package issrg.editor2.configurations;

import issrg.utils.gui.AbstractConfigComponent;
import issrg.utils.gui.xml.XMLChangeEvent;
import issrg.utils.gui.xml.XMLChangeListener;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import org.w3c.dom.Element;

/**
 *
 * @author Christian Azzopardi
 */
public class WSDLFileConfiguration extends JPanel implements XMLChangeListener, ItemListener, ActionListener {

    private AbstractConfigComponent xmlED;

    private JLabel gt4DescLabel;

    private JCheckBox gridRbutton;

    private JTextField localTextField;

    private JButton browseButton;

    private WSDLFilesListConfig wsdlFilesList;

    ResourceBundle rbl = ResourceBundle.getBundle("issrg/editor2/PEComponent_i18n");

    String WSDLBorderCaption = rbl.getString("Application_Preferences_WSDL_Border");

    String TreeBorderCaption = rbl.getString("WSDLFile_Configurations_Tree_Panel");

    String GT4_Option = rbl.getString("WSDLFile_GT4_Option");

    String GT4_Browse = rbl.getString("WSDLFile_GT4_Browse_FileStore");

    String GT4_Panel = rbl.getString("WSDLFile_GT4_Border");

    String GT4_Option_Description = rbl.getString("WSDLFile_GT4_Option_Description");

    String notValidFileCaption = rbl.getString("WSDLFile_Option_Local_Error");

    String errorHeader = rbl.getString("ErrorHeader");

    String WSDL_Panel = rbl.getString("WSDLFile_WSDL_Border");

    /** 
     * Creates a new instance of WSDLFileConfiguration 
     */
    public WSDLFileConfiguration(AbstractConfigComponent xmlED) {
        this.xmlED = xmlED;
        this.setLayout(new BorderLayout());
        this.add(getContentPanel());
        xmlED.addXMLChangeListener(this);
    }

    public JPanel getContentPanel() {
        JPanel wsdlConfig = new JPanel(new GridBagLayout());
        gt4DescLabel = new JLabel(GT4_Option_Description);
        gridRbutton = new JCheckBox(GT4_Option);
        gridRbutton.addItemListener(this);
        gridRbutton.setEnabled(false);
        browseButton = new JButton(GT4_Browse);
        browseButton.setEnabled(false);
        browseButton.addActionListener(this);
        browseButton.setActionCommand("BROWSE");
        localTextField = new JTextField(30);
        localTextField.setEnabled(false);
        wsdlFilesList = new WSDLFilesListConfig(xmlED);
        JPanel gt4Panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(0, 0, 0, 0);
        gt4Panel.add(gridRbutton, constraints);
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(0, 20, 0, 0);
        gt4Panel.add(gt4DescLabel, constraints);
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(3, 20, 0, 0);
        gt4Panel.add(localTextField, constraints);
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        constraints.insets = new Insets(3, 20, 0, 0);
        gt4Panel.add(browseButton, constraints);
        gt4Panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), GT4_Panel));
        wsdlFilesList.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED), WSDL_Panel));
        wsdlFilesList.setPreferredSize(new Dimension(300, 280));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        constraints.fill = GridBagConstraints.NONE;
        wsdlConfig.add(gt4Panel, constraints);
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        constraints.weightx = 1;
        constraints.weighty = 1;
        constraints.anchor = GridBagConstraints.LINE_START;
        constraints.fill = GridBagConstraints.NONE;
        wsdlConfig.add(wsdlFilesList, constraints);
        return wsdlConfig;
    }

    public void XMLChanged(XMLChangeEvent e) {
        refresh();
    }

    public void refresh() {
        if (getGT4Element() != null) {
            Element child = (Element) getGT4Element().getElementsByTagName("GT4Directory").item(0);
            if (child != null) {
                gridRbutton.setSelected(true);
                localTextField.setEnabled(true);
                browseButton.setEnabled(true);
                localTextField.setText(child.getAttribute("Path"));
            }
        }
    }

    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == gridRbutton && gridRbutton.isSelected()) {
            localTextField.setEnabled(true);
            browseButton.setEnabled(true);
        } else if (e.getSource() == gridRbutton && !gridRbutton.isSelected()) {
            localTextField.setEnabled(false);
            browseButton.setEnabled(false);
            Element child = (Element) getGT4Element().getElementsByTagName("GT4Directory").item(0);
            if (child != null) {
                xmlED.deleteItem(child, getGT4Element());
                localTextField.setText("");
            }
        }
    }

    FileFilter ff = new FileFilter() {

        public boolean accept(File f) {
            return f.isDirectory();
        }

        public String getDescription() {
            return "All Folders";
        }
    };

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().intern().equals("BROWSE")) {
            JFileChooser fc = new JFileChooser();
            JDialog.setDefaultLookAndFeelDecorated(true);
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            fc.setCurrentDirectory(new File("."));
            fc.setFileFilter(ff);
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String filename = fc.getSelectedFile().getAbsolutePath();
                if (isGlobusDirectory(filename)) {
                    localTextField.setText(filename);
                    Element child = xmlED.DOM.createElement("GT4Directory");
                    child.setAttribute("Path", filename);
                    xmlED.addItem(child, getGT4Element());
                } else {
                    JOptionPane.showMessageDialog(this, notValidFileCaption, errorHeader, JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }
    }

    public Element getApplicationSettings() {
        try {
            return (Element) xmlED.DOM.getElementsByTagName("ApplicationSettings").item(0);
        } catch (Exception e) {
            return null;
        }
    }

    public Element getGT4Element() {
        try {
            return (Element) getApplicationSettings().getElementsByTagName("GT4").item(0);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isGlobusDirectory(String globusFilePath) {
        File share = new File(globusFilePath + "\\share");
        File etc = new File(globusFilePath + "\\etc\\globus_wsrf_core");
        if (share.isDirectory() && etc.isDirectory()) return true; else return false;
    }
}
