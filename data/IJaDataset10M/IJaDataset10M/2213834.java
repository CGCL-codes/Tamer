package de.shandschuh.jaolt.gui.dialogs.errorreport;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import com.jgoodies.forms.builder.PanelBuilder;
import de.shandschuh.jaolt.core.Language;
import de.shandschuh.jaolt.core.Member;
import de.shandschuh.jaolt.core.exception.CommonException;
import de.shandschuh.jaolt.gui.FormManager;
import de.shandschuh.jaolt.gui.core.ClipBoardJPopupMenu;
import de.shandschuh.jaolt.gui.error.SimpleErrorReporter;
import de.shandschuh.jaolt.tools.log.Logger;

public class ErrorReportFormManager extends FormManager {

    private JTextField summaryJTextField;

    private JTextArea descriptionJTextArea;

    private JTextField nameJTextField;

    private JTextField emailJTextField;

    private JCheckBox attachErrorLog;

    private Throwable exception;

    private JDialog dialog;

    public ErrorReportFormManager(JDialog dialog, Throwable exception) {
        this.dialog = dialog;
        this.exception = exception;
        summaryJTextField = new JTextField(15);
        ClipBoardJPopupMenu.install(summaryJTextField);
        descriptionJTextArea = new JTextArea(10, 40);
        descriptionJTextArea.setLineWrap(true);
        ClipBoardJPopupMenu.install(descriptionJTextArea);
        nameJTextField = new JTextField(System.getProperty(Member.PROPERTY_NAME, ""), 15);
        ClipBoardJPopupMenu.install(nameJTextField);
        emailJTextField = new JTextField(System.getProperty(Member.PROPERTY_EMAIL, ""), 15);
        ClipBoardJPopupMenu.install(emailJTextField);
        if (exception != null) {
            summaryJTextField.setText(exception.toString());
            descriptionJTextArea.setText("\n\n\n<" + Language.translateStatic("MESSAGE_ERRORINFORMATIONATTACHED") + ">");
        }
        attachErrorLog = new JCheckBox(Language.translateStatic("ATTACH_ERRORLOGFILE").replace("$filename", Logger.ERROR_LOG.toString()));
    }

    @Override
    protected void addPanelBuilderComponents(PanelBuilder panelBuilder) {
        panelBuilder.addSeparator(Language.translateStatic("REQUIRED"), getCellConstraints(1, 1, 5));
        panelBuilder.add(new JLabel(Language.translateStatic("SUMMARY")), getCellConstraints(2, 3));
        panelBuilder.add(summaryJTextField, getCellConstraints(4, 3));
        panelBuilder.add(new JLabel(Language.translateStatic("DETAILED_DESCRIPTION")), getCellConstraints(2, 5));
        panelBuilder.add(new JScrollPane(descriptionJTextArea), getCellConstraints(4, 5));
        panelBuilder.addSeparator(Language.translateStatic("OPTIONAL"), getCellConstraints(1, 7, 5));
        panelBuilder.add(new JLabel(Language.translateStatic("NAME")), getCellConstraints(2, 9));
        panelBuilder.add(nameJTextField, getCellConstraints(4, 9));
        panelBuilder.add(new JLabel(Language.translateStatic("EMAIL")), getCellConstraints(2, 11));
        panelBuilder.add(emailJTextField, getCellConstraints(4, 11));
        panelBuilder.add(attachErrorLog, getCellConstraints(2, 13, 3));
    }

    @Override
    protected String getColumnLayout() {
        return "5dlu, p, 4dlu, fill:3dlu:grow, 4dlu";
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    protected String getRowLayout() {
        return "p, 3dlu, p, 3dlu, fill:3dlu:grow, 8dlu, p, 3dlu, p, 3dlu, p, 3dlu, p";
    }

    @Override
    public boolean rebuildNeeded() {
        return false;
    }

    @Override
    protected void reloadLocal(boolean rebuild) {
    }

    @Override
    protected void saveLocal() throws Exception {
        if (summaryJTextField.getText().trim().length() == 0 || descriptionJTextArea.getText().trim().length() == 0) {
            throw new CommonException(Language.translateStatic("ERROR_MISSINGINFORMATION"));
        }
        System.setProperty(Member.PROPERTY_EMAIL, emailJTextField.getText().trim());
        System.setProperty(Member.PROPERTY_NAME, nameJTextField.getText().trim());
        SimpleErrorReporter errorReporter = new SimpleErrorReporter(dialog);
        if (emailJTextField.getText().trim().length() > 0) {
            errorReporter.setSender(emailJTextField.getText().trim(), nameJTextField.getText().trim());
        }
        errorReporter.reportError(summaryJTextField.getText().trim(), descriptionJTextArea.getText().trim(), exception, attachErrorLog.isSelected());
    }

    @Override
    protected void validateLocal() throws Exception {
    }
}
