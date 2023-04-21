package fedora.services.sipcreator;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import beowulf.gui.HideablePanel;
import beowulf.gui.Utility;
import fedora.services.sipcreator.metadata.Metadata;
import fedora.services.sipcreator.metadata.MetadataPanel;

/**
 * This class is the UI view class which should be used to display and edit
 * all Metadata objects.
 * <br><br>
 * @author Andy Scukanec - (ags at cs dot cornell dot edu)
 */
public class MetadataPanelWrapper extends JPanel implements ActionListener, DocumentListener {

    /** */
    private static final long serialVersionUID = 3257002172494263094L;

    /** The metadata object that this UI view is displaying */
    private Metadata metadata;

    /** The metadata panel contained in this UI view */
    private MetadataPanel metadataPanel;

    /** The text field where the label may be edited for the metadata object */
    private JTextField metadataLabelField = new JTextField();

    /** The combo box where the type may be changed for the metadata object */
    private JComboBox metadataTypeBox;

    /** The text field that displays the name of this metadata object */
    private JTextField metadataNameField = new JTextField();

    /** The containing sip creator environment */
    private SIPCreator creator;

    /**
     * This constructor takes in the metadata panel and the SIP Creator object
     * that will display the resulting UI widget.  The SIP creator is necessary
     * to provide a communication link between this UI component and the
     * conversion rules currently in use, which affect the type of the
     * represented metadata.
     * <br><br>
     * @param newMetadata The metadata to be displayed.
     * @param newCreator The SIP Creator in which this UI component will live.
     */
    public MetadataPanelWrapper(Metadata newMetadata, SIPCreator newCreator) {
        super(new BorderLayout());
        creator = newCreator;
        metadataTypeBox = new JComboBox(creator.getConversionRulesTask().getRules().getDatastreamComboBoxModel());
        metadata = newMetadata;
        metadataPanel = newMetadata.getPanel();
        JPanel temp = new JPanel(new GridLayout(3, 1, 5, 5));
        temp.add(Utility.addLabelLeft("Label: ", metadataLabelField));
        temp.add(Utility.addLabelLeft("Type: ", metadataTypeBox));
        temp.add(Utility.addLabelLeft("ID: ", metadataNameField));
        HideablePanel htemp = new HideablePanel(temp, "Common Metadata Attributes");
        htemp.setResizeable(false);
        htemp.setBorder(null);
        add(htemp, BorderLayout.NORTH);
        add(metadataPanel, BorderLayout.CENTER);
        metadataTypeBox.setEditable(true);
        metadataNameField.setEnabled(false);
        updateFromMetadata();
        metadataLabelField.getDocument().addDocumentListener(this);
        metadataTypeBox.addActionListener(this);
    }

    /**
     * Returns the metadata represented by this UI component.
     * <br><br>
     * @return The metadata represented by this UI component.
     */
    public Metadata getMetadata() {
        return metadata;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == metadataTypeBox && ae.getActionCommand().equals("comboBoxEdited") && metadataTypeBox.getSelectedIndex() == -1) {
            int choice = JOptionPane.showConfirmDialog(creator, "Would you like to add \"" + metadataTypeBox.getSelectedItem() + "\" as a new datastream template?", "Add New Template", JOptionPane.YES_NO_OPTION);
            if (choice != JOptionPane.YES_OPTION) return;
            ConversionRules.DatastreamTemplate newDT = new ConversionRules.DatastreamTemplate(metadataTypeBox.getSelectedItem().toString());
            creator.getConversionRulesTask().getRules().addDatastreamTemplate(newDT);
            creator.getConversionRulesTask().updateRules();
            metadataTypeBox.setSelectedItem(newDT);
        } else if (ae.getSource() == metadataTypeBox) {
            SelectableEntry entry = getMetadata().getEntry();
            for (int ctr = 0; ctr < entry.getMetadataCount(); ctr++) {
                if (entry.getMetadata(ctr) == getMetadata()) continue;
                if (entry.getMetadata(ctr).getType().equals(metadataTypeBox.getSelectedItem().toString())) {
                    String msg = "There is already a node in the current entry with a\n" + "type of \"" + getMetadata().getType() + "\".  We recommend\n" + "each metadata type unique per entry.";
                    JOptionPane.showMessageDialog(creator, msg);
                    break;
                }
            }
        }
        updateMetadata();
    }

    /**
     * This method refreshes the UI view from the metadata data model. 
     */
    public void updateFromMetadata() {
        ConversionRules rules = creator.getConversionRulesTask().getRules();
        Object template = rules.getDatastreamTemplate(metadata.getType());
        if (template != null) {
            metadataTypeBox.setSelectedItem(template);
        } else {
            String type = metadata.getType();
            int choice = JOptionPane.showConfirmDialog(creator, "Would you like to add \"" + type + "\" as a new datastream template?", "Add New Template", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                ConversionRules.DatastreamTemplate newDT = new ConversionRules.DatastreamTemplate(type);
                rules.addDatastreamTemplate(newDT);
                metadataTypeBox.setSelectedItem(newDT);
            }
        }
        metadataLabelField.setText(metadata.getLabel());
        metadataNameField.setText(metadata.getID());
        metadataPanel.updateFromMetadata();
    }

    /**
     * This method causes all unpushed metadata in the UI view to be pushed
     * to the underlying metadata model.
     */
    public void updateMetadata() {
        getMetadata().setLabel(metadataLabelField.getText());
        if (metadataTypeBox.getSelectedItem() != null) {
            getMetadata().setType(metadataTypeBox.getSelectedItem().toString());
        } else {
            getMetadata().setType("");
        }
        metadataPanel.updateMetadata();
    }

    public void changedUpdate(DocumentEvent e) {
        updateMetadata();
        JTabbedPane parent = (JTabbedPane) getParent();
        parent.setTitleAt(parent.getSelectedIndex(), getMetadata().getShortName());
        parent.setToolTipTextAt(parent.getSelectedIndex(), getMetadata().getDescriptiveName());
    }

    public void insertUpdate(DocumentEvent e) {
        updateMetadata();
        JTabbedPane parent = (JTabbedPane) getParent();
        parent.setTitleAt(parent.getSelectedIndex(), getMetadata().getShortName());
        parent.setToolTipTextAt(parent.getSelectedIndex(), getMetadata().getDescriptiveName());
    }

    public void removeUpdate(DocumentEvent e) {
        updateMetadata();
        JTabbedPane parent = (JTabbedPane) getParent();
        parent.setTitleAt(parent.getSelectedIndex(), getMetadata().getShortName());
        parent.setToolTipTextAt(parent.getSelectedIndex(), getMetadata().getDescriptiveName());
    }
}
