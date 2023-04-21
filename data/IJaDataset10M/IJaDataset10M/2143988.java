package KFramework30.Printing;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import KFramework30.Base.*;

public class summaryActionListenerClass implements ActionListener, ListSelectionListener {

    private KConfigurationClass configuration;

    private KLogClass log;

    private javax.swing.JList fieldList;

    private javax.swing.JLabel fieldNameLabel;

    private javax.swing.JComboBox operationComboBox;

    private javax.swing.JTextField prefixTextField;

    private javax.swing.JTextField suffixTextField;

    private javax.swing.JComboBox precisionComboBox;

    private java.awt.Window parentWindow;

    private customSummaryOperationClass listFiller;

    /** Creates new summaryActionListenerClass */
    public summaryActionListenerClass(KConfigurationClass configurationParam, KLogClass logParam, java.awt.Window parentWindowParam, JList fieldListParam, JPanel panelParam, KPrintDataTableClass DBPrinterParam) throws KExceptionClass {
        configuration = configurationParam;
        log = logParam;
        fieldList = fieldListParam;
        Component[] componentArray = panelParam.getComponents();
        parentWindow = parentWindowParam;
        int i;
        fieldNameLabel = null;
        for (i = 0; i < componentArray.length; i++) if (componentArray[i].getName() == "fieldNameLabel") {
            fieldNameLabel = (JLabel) componentArray[i];
            break;
        }
        operationComboBox = null;
        for (i = 0; i < componentArray.length; i++) if (componentArray[i].getName() == "operationComboBox") {
            operationComboBox = (JComboBox) componentArray[i];
            break;
        }
        prefixTextField = null;
        for (i = 0; i < componentArray.length; i++) if (componentArray[i].getName() == "prefixTextField") {
            prefixTextField = (JTextField) componentArray[i];
            break;
        }
        suffixTextField = null;
        for (i = 0; i < componentArray.length; i++) if (componentArray[i].getName() == "suffixTextField") {
            suffixTextField = (JTextField) componentArray[i];
            break;
        }
        precisionComboBox = null;
        for (i = 0; i < componentArray.length; i++) if (componentArray[i].getName() == "precisionComboBox") {
            precisionComboBox = (JComboBox) componentArray[i];
            break;
        }
        listFiller = new customSummaryOperationClass(configuration, log, fieldList, DBPrinterParam);
        listFiller.initializeLists();
        fieldList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultListModel fieldListModel = (DefaultListModel) fieldList.getModel();
        if (fieldListModel.getSize() != 0) fieldList.setSelectedIndex(0);
        setOperationPanel();
    }

    public void actionPerformed(java.awt.event.ActionEvent e) {
        String command = e.getActionCommand();
        if (command.equals("apply")) sumApplyButtonActionPerformed(e);
    }

    private void sumApplyButtonActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (!fieldList.isSelectionEmpty()) {
                int index = fieldList.getSelectedIndex();
                printingFieldInfoClass fieldInfo = listFiller.getFieldInfo(index);
                fieldInfo.prefix = prefixTextField.getText();
                fieldInfo.suffix = suffixTextField.getText();
                fieldInfo.operation = operationComboBox.getSelectedIndex();
                fieldInfo.precision = precisionComboBox.getSelectedIndex();
                listFiller.updateVisualFieldList(index);
            }
        } catch (Exception error) {
            log.log(this, KMetaUtilsClass.getStackTrace(error));
            KMetaUtilsClass.showErrorMessageFromException(parentWindow, error);
        }
        ;
    }

    public void setDefaultPrintingFields() {
        listFiller.setDefaultPrintingFields();
    }

    /** Process for list selectoin change */
    public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
        try {
            setOperationPanel();
        } catch (Exception error) {
            log.log(this, KMetaUtilsClass.getStackTrace(error));
            KMetaUtilsClass.showErrorMessageFromException(parentWindow, error);
        }
        ;
    }

    private void setOperationPanel() throws KExceptionClass {
        printingFieldInfoClass fieldInfo = new printingFieldInfoClass();
        if (!fieldList.isSelectionEmpty()) {
            int index = fieldList.getSelectedIndex();
            fieldInfo = listFiller.getFieldInfo(index);
        }
        fieldNameLabel.setText(fieldInfo.readableName);
        prefixTextField.setText(fieldInfo.prefix);
        suffixTextField.setText(fieldInfo.suffix);
        operationComboBox.setSelectedIndex(fieldInfo.operation);
        precisionComboBox.setSelectedIndex(fieldInfo.precision);
    }
}
