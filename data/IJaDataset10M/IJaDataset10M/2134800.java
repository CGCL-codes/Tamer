package org.hardtokenmgmt.admin.ui.panels.editglprops;

import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import org.hardtokenmgmt.admin.ui.AdminUIUtils;
import org.hardtokenmgmt.common.Constants;
import org.hardtokenmgmt.core.ui.UIHelper;
import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Add global property manually dialog
 * 
 * 
 * @author Philip Vendil 15 mar 2009
 *
 * @version $Id$
 */
public class ShowTemplateHeaderDialog extends JDialog {

    private Dimension DIALOG_SIZE = new Dimension(600, 250);

    private JTextField nameField;

    private JEditorPane descriptionEditorPane;

    private static final long serialVersionUID = 1L;

    private Properties props;

    private String fileName;

    private boolean confirmed = false;

    public ShowTemplateHeaderDialog() {
        super();
        initComponents();
    }

    public ShowTemplateHeaderDialog(String fileName, Properties props, Window owner) {
        super(owner, ModalityType.APPLICATION_MODAL);
        this.fileName = fileName;
        this.props = props;
        initComponents();
    }

    private void initComponents() {
        setSize(DIALOG_SIZE);
        getContentPane().setLayout(new FormLayout(new ColumnSpec[] { ColumnSpec.decode("11dlu"), ColumnSpec.decode("69dlu"), ColumnSpec.decode("11dlu"), ColumnSpec.decode("74dlu"), FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("122dlu") }, new RowSpec[] { RowSpec.decode("30dlu"), FormFactory.DEFAULT_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, RowSpec.decode("30dlu"), FormFactory.UNRELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC }));
        setTitle(UIHelper.getText("editglobprops.templateheader"));
        setIconImage(AdminUIUtils.getAdminWindowIcon());
        final JButton cancelButton = new JButton();
        cancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                dispose();
            }
        });
        cancelButton.setText(UIHelper.getText("cancel"));
        cancelButton.setIcon(UIHelper.getImage("disable.png"));
        getContentPane().add(cancelButton, new CellConstraints(6, 8, CellConstraints.LEFT, CellConstraints.DEFAULT));
        final JButton saveButton = new JButton();
        saveButton.addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                confirmed = true;
                dispose();
            }
        });
        saveButton.setText(UIHelper.getText("ok"));
        saveButton.setIcon(UIHelper.getImage("enable.png"));
        getContentPane().add(saveButton, new CellConstraints(4, 8, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        final JLabel descriptionLabel = new JLabel();
        descriptionLabel.setText(UIHelper.getText("editglobprops.description") + ":");
        descriptionLabel.setFont(UIHelper.getLabelFontBold());
        getContentPane().add(descriptionLabel, new CellConstraints(2, 5, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        descriptionEditorPane = new JEditorPane();
        descriptionEditorPane.setEditable(false);
        if (props.getProperty(Constants.GLOBALPROPERTY_TEMPLATE_DESCRIPTION) == null) {
            descriptionEditorPane.setText(UIHelper.getText("editglobprops.nodescription"));
        } else {
            descriptionEditorPane.setText(props.getProperty(Constants.GLOBALPROPERTY_TEMPLATE_DESCRIPTION));
        }
        getContentPane().add(new JScrollPane(descriptionEditorPane), new CellConstraints(4, 5, 3, 2));
        final JLabel nameLabel = new JLabel();
        nameLabel.setText(UIHelper.getText("editglobprops.name") + ":");
        nameLabel.setFont(UIHelper.getLabelFontBold());
        getContentPane().add(nameLabel, new CellConstraints(2, 2, CellConstraints.RIGHT, CellConstraints.DEFAULT));
        nameField = new JTextField();
        nameField.setEditable(false);
        if (props.getProperty(Constants.GLOBALPROPERTY_TEMPLATE_NAME) == null) {
            nameField.setText(fileName.toLowerCase().replace(".properties", ""));
        } else {
            nameField.setText(props.getProperty(Constants.GLOBALPROPERTY_TEMPLATE_NAME));
        }
        getContentPane().add(nameField, new CellConstraints(4, 2, 3, 1));
        final JLabel titleLabel = new JLabel();
        titleLabel.setText(UIHelper.getText("editglobprops.templateheader"));
        titleLabel.setFont(UIHelper.getTitleFont());
        getContentPane().add(titleLabel, new CellConstraints(4, 1, 3, 1));
        final JLabel logoLabel = new JLabel();
        logoLabel.setIcon(UIHelper.getImage("addmultipleglobalprops.png"));
        logoLabel.setText("");
        getContentPane().add(logoLabel, new CellConstraints(2, 1));
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
