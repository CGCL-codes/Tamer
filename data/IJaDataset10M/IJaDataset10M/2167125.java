package yoichiro.eclipse.plugins.translationview.internal.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * ���̑��p�����[�^���͂��邽�߂̃_�C�A���O�N���X�ł��B
 * @author Yoichiro Tanaka
 */
public class OtherParameterInputDialog extends Dialog {

    /** ���O�t�B�[���h */
    private Text txtfName;

    /** �l�t�B�[���h */
    private Text txtfValue;

    /** ���O */
    private String name;

    /** �l */
    private String value;

    /**
     * ���̃I�u�W�F�N�g�����������Ƃ��ɌĂяo����܂��B
     * @param parentShell �e�̃V�F��
     */
    protected OtherParameterInputDialog(Shell parentShell) {
        super(parentShell);
    }

    /**
     * GUI��z�u���܂��B
     * @param parent �e�̃R���e�i
     * @return �z�u��̃R���g���[��
     * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
     */
    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        composite.setLayout(new FormLayout());
        Label lblName = new Label(composite, SWT.NONE);
        lblName.setText("Name:");
        FormData frmDataLblName = new FormData();
        frmDataLblName.top = new FormAttachment(0, 7);
        frmDataLblName.left = new FormAttachment(0, 5);
        lblName.setLayoutData(frmDataLblName);
        txtfName = new Text(composite, SWT.SINGLE | SWT.BORDER);
        FormData frmDataTxtfName = new FormData();
        frmDataTxtfName.top = new FormAttachment(0, 5);
        frmDataTxtfName.right = new FormAttachment(100, -5);
        frmDataTxtfName.left = new FormAttachment(lblName, 5);
        txtfName.setLayoutData(frmDataTxtfName);
        Label lblValue = new Label(composite, SWT.NONE);
        lblValue.setText("Value:");
        FormData frmDataLblValue = new FormData();
        frmDataLblValue.bottom = new FormAttachment(100, -8);
        frmDataLblValue.left = new FormAttachment(0, 5);
        lblValue.setLayoutData(frmDataLblValue);
        txtfValue = new Text(composite, SWT.SINGLE | SWT.BORDER);
        FormData frmDataTxtfValue = new FormData();
        frmDataTxtfValue.bottom = new FormAttachment(100, -5);
        frmDataTxtfValue.right = new FormAttachment(100, -5);
        frmDataTxtfValue.left = new FormAttachment(lblValue, 5);
        txtfValue.setLayoutData(frmDataTxtfValue);
        return composite;
    }

    /**
     * �w�肳�ꂽ�V�F���ɑ΂���ݒ���s���܂��B
     * @param newShell �V�F��
     * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
     */
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Add Other Parameter");
    }

    /**
     * ���̃_�C�A���O�̏���T�C�Y��Ԃ��܂��B
     * @return �_�C�A���O�̏���T�C�Y
     * @see org.eclipse.jface.window.Window#getInitialSize()
     */
    protected Point getInitialSize() {
        return new Point(400, 120);
    }

    /**
     * ��͂��ꂽ���O��Ԃ��܂��B
     * @return ���O
     */
    public String getName() {
        return name;
    }

    /**
     * ��͂��ꂽ�l��Ԃ��܂��B
     * @return �l
     */
    public String getValue() {
        return value;
    }

    /**
     * OK�{�^���������ꂽ�Ƃ��ɌĂяo����܂��B
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     */
    protected void okPressed() {
        name = txtfName.getText();
        name = (name == null) ? name : name.trim();
        if ((name == null) || (name.length() == 0)) {
            MessageDialog.openError(getShell(), "Error", "Please input the name.");
            return;
        }
        value = txtfValue.getText();
        if ((value == null) || (value.length() == 0)) {
            MessageDialog.openError(getShell(), "Error", "Please input the value.");
            return;
        }
        super.okPressed();
    }
}
