package wsl.mdn.admin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JPanel;
import pv.jfcx.JPVPassword;
import javax.swing.JCheckBox;
import wsl.fw.util.Util;
import wsl.fw.gui.GuiConst;
import wsl.fw.gui.PropertiesPanel;
import wsl.fw.gui.WslTextField;
import wsl.fw.gui.WslTextArea;
import wsl.fw.gui.WslComboBox;
import wsl.fw.resource.ResId;
import wsl.fw.help.HelpId;
import wsl.fw.datasource.Field;
import wsl.mdn.dataview.FieldDobj;

/**
 *
 */
public class FieldPropPanel extends PropertiesPanel implements ActionListener {

    public static final ResId LABEL_NAME = new ResId("FieldPropPanel.label.Name");

    public static final ResId LABEL_DESCRIPTION = new ResId("FieldPropPanel.label.Description");

    public static final ResId LABEL_TYPE = new ResId("FieldPropPanel.label.Type");

    public static final ResId LABEL_UNIQUE_KEY = new ResId("FieldPropPanel.label.UniqueKey");

    public static final ResId LABEL_READ_ONLY = new ResId("FieldPropPanel.label.ReadOnly");

    public static final ResId LABEL_SIZE = new ResId("FieldPropPanel.label.size");

    public static final HelpId HID_FIELD = new HelpId("mdn.admin.FieldPropPanel");

    public static final ResId FT_STRING = new ResId("FieldPropPanel.fieldtype.String");

    public static final ResId FT_INTEGER = new ResId("FieldPropPanel.fieldtype.Integer");

    public static final ResId FT_DECIMAL = new ResId("FieldPropPanel.fieldtype.Decimal");

    public static final ResId FT_DATETIME = new ResId("FieldPropPanel.fieldtype.DateTime");

    public static final ResId FT_CURRENCY = new ResId("FieldPropPanel.fieldtype.Currency");

    private WslTextField _txtName = new WslTextField(200);

    private WslComboBox _cmbType = new WslComboBox(200);

    private JCheckBox _chkUniqueKey = new JCheckBox(LABEL_UNIQUE_KEY.getText());

    private JCheckBox _chkReadOnly = new JCheckBox(LABEL_READ_ONLY.getText());

    private WslTextField _txtDescription = new WslTextField(300);

    private JLabel _lblSize = new JLabel(LABEL_SIZE.getText());

    private WslTextField _txtSize = new WslTextField(100);

    /**
     * Default constructor.
     */
    public FieldPropPanel() {
        initFieldPropPanelControls();
        buildTypeCombo();
    }

    /**
     * Init the panel's controls.
     */
    private void initFieldPropPanelControls() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel lbl = new JLabel(LABEL_NAME.getText());
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets.left = GuiConst.DEFAULT_INSET;
        gbc.insets.top = GuiConst.DEFAULT_INSET;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 0.2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(lbl, gbc);
        gbc.gridx = 1;
        gbc.weightx = 0.8;
        gbc.insets.right = GuiConst.DEFAULT_INSET;
        add(_txtName, gbc);
        addMandatory(LABEL_NAME.getText(), _txtName);
        lbl = new JLabel(LABEL_TYPE.getText());
        gbc.insets.right = 0;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(lbl, gbc);
        gbc.gridx = 1;
        gbc.insets.right = GuiConst.DEFAULT_INSET;
        add(_cmbType, gbc);
        _cmbType.addActionListener(this);
        gbc.insets.right = 0;
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(_lblSize, gbc);
        gbc.gridx = 1;
        gbc.insets.right = GuiConst.DEFAULT_INSET;
        add(_txtSize, gbc);
        JPanel pnlFlags = new JPanel();
        pnlFlags.setLayout(new GridBagLayout());
        gbc.insets.left = 0;
        gbc.insets.top = 0;
        gbc.insets.right = 0;
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(pnlFlags, gbc);
        gbc.insets.left = GuiConst.DEFAULT_INSET;
        gbc.insets.top = GuiConst.DEFAULT_INSET;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        pnlFlags.add(_chkUniqueKey, gbc);
        gbc.gridx = 1;
        pnlFlags.add(_chkReadOnly, gbc);
        gbc.gridx = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        pnlFlags.add(new JLabel(""), gbc);
        lbl = new JLabel(LABEL_DESCRIPTION.getText());
        gbc.insets.left = GuiConst.DEFAULT_INSET;
        gbc.insets.top = GuiConst.DEFAULT_INSET;
        gbc.insets.right = 0;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1;
        add(lbl, gbc);
        gbc.gridx = 1;
        gbc.insets.right = GuiConst.DEFAULT_INSET;
        gbc.insets.bottom = GuiConst.DEFAULT_INSET;
        gbc.weighty = 1;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        add(_txtDescription, gbc);
    }

    /**
     * Build type combo
     */
    private void buildTypeCombo() {
        _cmbType.addItem(FT_STRING.getText());
        _cmbType.addItem(FT_INTEGER.getText());
        _cmbType.addItem(FT_DECIMAL.getText());
        _cmbType.addItem(FT_DATETIME.getText());
        _cmbType.addItem(FT_CURRENCY.getText());
        _cmbType.selectItem(FT_STRING.getText());
    }

    /**
     * Listen to field type changes
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(_cmbType)) {
            String strType = (String) _cmbType.getSelectedItem();
            boolean bShowSize = strType.equalsIgnoreCase(FT_STRING.getText());
            _lblSize.setVisible(bShowSize);
            _txtSize.setVisible(bShowSize);
        }
    }

    /**
     * Transfer data between the DataObject and panel controls.
     * @param toDataObject, determines the direction of the transfer.
     */
    public void transferData(boolean toDataObject) {
        FieldDobj dobj = (FieldDobj) getDataObject();
        Util.argCheckNull(dobj);
        if (toDataObject) {
            dobj.setName(_txtName.getText());
            dobj.setDescription(_txtDescription.getText());
            int type = Field.FT_STRING;
            String strType = (String) _cmbType.getSelectedItem();
            if (strType.equalsIgnoreCase(FT_STRING.getText())) type = Field.FT_STRING; else if (strType.equalsIgnoreCase(FT_INTEGER.getText())) type = Field.FT_INTEGER; else if (strType.equalsIgnoreCase(FT_DECIMAL.getText())) type = Field.FT_DECIMAL; else if (strType.equalsIgnoreCase(FT_DATETIME.getText())) type = Field.FT_DATETIME; else if (strType.equalsIgnoreCase(FT_CURRENCY.getText())) type = Field.FT_CURRENCY;
            dobj.setType(type);
            int colSize = -1;
            colSize = Integer.parseInt(_txtSize.getText());
            if (colSize <= 0) colSize = 50;
            dobj.setColumnSize(colSize);
            int flags = Field.FF_NONE;
            if (_chkUniqueKey.isSelected()) flags = flags | Field.FF_UNIQUE_KEY;
            if (_chkReadOnly.isSelected()) flags = flags | Field.FF_READ_ONLY;
            dobj.setFlags(flags);
        } else {
            _txtName.setText(dobj.getName());
            _txtDescription.setText(dobj.getDescription());
            int type = dobj.getType();
            String strType = "";
            switch(type) {
                case Field.FT_STRING:
                    strType = FT_STRING.getText();
                    break;
                case Field.FT_INTEGER:
                    strType = FT_INTEGER.getText();
                    break;
                case Field.FT_DECIMAL:
                    strType = FT_DECIMAL.getText();
                    break;
                case Field.FT_DATETIME:
                    strType = FT_DATETIME.getText();
                    break;
                case Field.FT_CURRENCY:
                    strType = FT_CURRENCY.getText();
                    break;
            }
            _cmbType.selectItem(strType);
            _txtSize.setText(String.valueOf(dobj.getColumnSize()));
            int flags = dobj.getFlags();
            _chkUniqueKey.setSelected((flags & Field.FF_UNIQUE_KEY) > 0);
            _chkReadOnly.setSelected((flags & Field.FF_READ_ONLY) > 0);
        }
    }

    /**
     * Return the preferred size
     */
    public Dimension getPreferredSize() {
        return new Dimension(400, 200);
    }

    /**
     * If the subclass has help override this to specify the HelpId.
     * This help is displayed using the parent wizards's help button.
     * @return the HelpId of the help to display, if null the help button is not
     *   displayed.
     */
    public HelpId getHelpId() {
        return HID_FIELD;
    }
}
