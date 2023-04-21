package gui.dialogs;

import java.awt.*;
import javax.swing.*;
import java.awt.Frame;
import opt.Options;
import dtm.*;

public class AddEditDatatypeDialog extends BaseDialog {

    /**
	 * 
	 */
    private static final long serialVersionUID = 6639276710030667842L;

    private JPanel contentPane;

    private JPanel PanelButtons;

    private JLabel LabelDatatype;

    private JLabel LabelBaseType;

    private JLabel LabelClass;

    private JLabel LabelBytesize;

    private JLabel LabelMaxPrec;

    private JLabel LabelMaxScale;

    private JLabel LabelForcedPrecision;

    private JLabel LabelForcedScale;

    private JTextField TextFieldDatatype;

    private JComboBox ComboBoxClass;

    private JTextField TextFieldBaseType;

    private JTextField TextFieldBytesize;

    private JTextField TextFieldMaxPrec;

    private JTextField TextFieldMaxScale;

    private JCheckBox CheckBoxForcedPrec;

    private JCheckBox CheckBoxForcedScale;

    private JPanel PanelFields;

    private SingleDatatype globalSDT;

    @SuppressWarnings("unused")
    private boolean addMode;

    /**
	 * @param owner
	 * @param localOpts
	 */
    public AddEditDatatypeDialog(Frame owner, Options localOpts, SingleDatatype sdt) {
        super(owner, localOpts);
        addMode = false;
        globalSDT = sdt;
        loadVariables();
    }

    /**
	 * @param owner
	 * @param modal
	 * @param localOpts
	 */
    public AddEditDatatypeDialog(Frame owner, boolean modal, Options localOpts) {
        super(owner, modal, localOpts);
    }

    /**
	 * @param owner
	 * @param title
	 * @param localOpts
	 */
    public AddEditDatatypeDialog(Frame owner, String title, Options localOpts) {
        super(owner, title, localOpts);
    }

    public void setAddMode(boolean isAdd) {
        addMode = isAdd;
    }

    protected void createPane() {
        contentPane = (JPanel) this.getContentPane();
        PanelButtons = new JPanel();
        LabelDatatype = new JLabel();
        LabelClass = new JLabel();
        LabelBaseType = new JLabel();
        LabelBytesize = new JLabel();
        LabelMaxPrec = new JLabel();
        LabelMaxScale = new JLabel();
        LabelForcedPrecision = new JLabel();
        LabelForcedScale = new JLabel();
        TextFieldDatatype = new JTextField();
        ComboBoxClass = new JComboBox();
        TextFieldBaseType = new JTextField();
        TextFieldBytesize = new JTextField();
        TextFieldMaxPrec = new JTextField();
        TextFieldMaxScale = new JTextField();
        CheckBoxForcedPrec = new JCheckBox();
        CheckBoxForcedScale = new JCheckBox();
        PanelFields = new JPanel();
        contentPane.setLayout(new BorderLayout(2, 2));
        contentPane.add(PanelButtons, BorderLayout.SOUTH);
        contentPane.add(PanelFields, BorderLayout.CENTER);
        PanelButtons.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
        PanelButtons.add(OkButton, 0);
        PanelButtons.add(CancelButton, 1);
        LabelDatatype.setHorizontalAlignment(SwingConstants.RIGHT);
        LabelDatatype.setText("Datatype");
        LabelClass.setHorizontalAlignment(SwingConstants.RIGHT);
        LabelClass.setText("Class");
        LabelBaseType.setHorizontalAlignment(SwingConstants.RIGHT);
        LabelBaseType.setText("Base Datatype");
        LabelBytesize.setHorizontalAlignment(SwingConstants.RIGHT);
        LabelBytesize.setText("Byte Size");
        LabelMaxPrec.setHorizontalAlignment(SwingConstants.RIGHT);
        LabelMaxPrec.setText("Max Precision");
        LabelMaxScale.setHorizontalAlignment(SwingConstants.RIGHT);
        LabelMaxScale.setText("Max Scale");
        LabelForcedPrecision.setHorizontalAlignment(SwingConstants.RIGHT);
        LabelForcedPrecision.setText("Forced Precision");
        LabelForcedScale.setHorizontalAlignment(SwingConstants.RIGHT);
        LabelForcedScale.setText("Forced Scale");
        for (String dtypes : this.opts.getDatatypeConverter().getDatatypeClasses()) ComboBoxClass.addItem(dtypes);
        PanelFields.setLayout(null);
        addComponent(PanelFields, LabelDatatype, 0, 6, 129, 25);
        addComponent(PanelFields, LabelClass, 1, 36, 129, 25);
        addComponent(PanelFields, LabelBaseType, 0, 66, 129, 25);
        addComponent(PanelFields, LabelBytesize, 0, 96, 129, 25);
        addComponent(PanelFields, LabelMaxPrec, 0, 126, 129, 25);
        addComponent(PanelFields, LabelMaxScale, 0, 156, 129, 25);
        addComponent(PanelFields, LabelForcedPrecision, 0, 186, 129, 25);
        addComponent(PanelFields, LabelForcedScale, 0, 216, 129, 25);
        addComponent(PanelFields, TextFieldDatatype, 141, 6, 228, 25);
        addComponent(PanelFields, ComboBoxClass, 141, 36, 228, 25);
        addComponent(PanelFields, TextFieldBaseType, 142, 66, 228, 25);
        addComponent(PanelFields, TextFieldBytesize, 141, 66 + 30, 228, 25);
        addComponent(PanelFields, TextFieldMaxPrec, 141, 66 + 60, 228, 25);
        addComponent(PanelFields, TextFieldMaxScale, 141, 66 + 90, 228, 25);
        addComponent(PanelFields, CheckBoxForcedPrec, 141, 66 + 90 + 30, 228, 25);
        addComponent(PanelFields, CheckBoxForcedScale, 141, 66 + 90 + 60, 228, 25);
        this.setTitle("Add / Edit Datatype");
        this.setModal(true);
        this.setResizable(false);
    }

    protected void setWidthHeight() {
        width = 386;
        height = 370;
    }

    protected void loadVariables() {
        if (globalSDT == null) return;
        this.TextFieldDatatype.setText(globalSDT.getType());
        this.ComboBoxClass.setSelectedItem(globalSDT.getTypeClass());
        this.TextFieldBaseType.setText(globalSDT.getBaseType());
        this.TextFieldBytesize.setText(globalSDT.getByteSize());
        this.TextFieldMaxPrec.setText(globalSDT.getMaxPrec());
        this.TextFieldMaxScale.setText(globalSDT.getMaxScale());
        this.CheckBoxForcedPrec.setSelected(globalSDT.getForcedPrecision());
        this.CheckBoxForcedScale.setSelected(globalSDT.getForcedScale());
    }

    @SuppressWarnings("unused")
    protected boolean checkValidOptions() {
        String dtype = TextFieldDatatype.getText();
        String typeClass = (String) this.ComboBoxClass.getSelectedItem();
        String baseType = this.TextFieldBaseType.getText();
        String byteSize = this.TextFieldBytesize.getText();
        String maxPrec = this.TextFieldMaxPrec.getText();
        String maxScale = this.TextFieldMaxScale.getText();
        String message = "";
        if (dtype.length() == 0) message += "A datatype MUST be specified.";
        if (baseType.length() == 0) message = addToMessage(message, "A BASE class must be chosen.");
        if (typeClass.length() == 0) message = addToMessage(message, "A type class must be specified.");
        if (maxPrec.length() == 0) message = addToMessage(message, "Max Precision must be specified.");
        if (maxScale.length() == 0) message = addToMessage(message, "Max Scale must be specified.");
        if (message.length() == 0) return (true);
        new showMessage(this, "Errors", message);
        return (false);
    }

    private String addToMessage(String message, String newMessage) {
        if (message.length() > 0) message += "\n";
        return (message + newMessage);
    }

    protected void saveVariables() {
        if (globalSDT == null) return;
        String dtype = TextFieldDatatype.getText();
        String typeClass = (String) ComboBoxClass.getSelectedItem();
        String baseType = this.TextFieldBaseType.getText();
        String byteSize = this.TextFieldBytesize.getText();
        String maxPrec = this.TextFieldMaxPrec.getText();
        String maxScale = this.TextFieldMaxScale.getText();
        boolean forcedPrec = this.CheckBoxForcedPrec.isSelected();
        boolean forcedScale = this.CheckBoxForcedScale.isSelected();
        globalSDT.setType(dtype);
        globalSDT.setTypeClass(typeClass);
        globalSDT.setBaseType(baseType);
        globalSDT.setByteSize(byteSize);
        globalSDT.setMaxPrec(maxPrec);
        globalSDT.setMaxScale(maxScale);
        globalSDT.setForcedPrecision(forcedPrec);
        globalSDT.setForcedScale(forcedScale);
    }
}
