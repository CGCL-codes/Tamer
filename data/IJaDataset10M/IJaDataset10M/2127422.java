package testsuite;

import objectivehtml.htmlwidget.*;
import objectivehtml.htmlwidget.exception.*;
import objectivehtml.oms.*;

/**
 * OMSTestForm
 * This test the functionality of signals and slots.
 */
public class OMSTestForm extends HtmlForm implements ErrorHandler {

    public HtmlParagraph m_parUserMessage;

    public HtmlTable m_tblLayout;

    public HtmlSpan m_htmUserMessage;

    public HtmlText m_htmMale;

    public HtmlText m_htmFemale;

    public HtmlTextBox m_txtFirstName;

    public HtmlTextBox m_txtLastName;

    public HtmlRadioButton m_radMale;

    public HtmlRadioButton m_radFemale;

    public HtmlListBox m_selTitle;

    public HtmlPushButton m_btnSave;

    public HtmlPushButton m_btnReset;

    public HtmlPushButton m_btnConnect;

    public HtmlPushButton m_btnDisconnect;

    public HtmlTextArea m_txaStreet;

    public HtmlCheckBox m_cbxPostalAddress;

    public HtmlHiddenInput m_hidProxy;

    /**
	 * Constructor
	 */
    public OMSTestForm() throws Exception {
        super();
        setMethod("post");
        setName("customerform");
        m_parUserMessage = new HtmlParagraph(this);
        m_parUserMessage.setVisible(false);
        m_htmUserMessage = new HtmlSpan(m_parUserMessage);
        m_tblLayout = new HtmlTable(this, 7, 2);
        m_tblLayout.setBorder("0");
        m_tblLayout.setBgColor("#FCFADA");
        m_tblLayout.setClassAttribute("normal");
        m_tblLayout.getTableCell(0, 0).setText("Title *");
        m_selTitle = new HtmlListBox(m_tblLayout.getTableCell(0, 1), "m_selTitle");
        m_selTitle.addOption("", "- Choose Title -");
        m_selTitle.addOption("01", "Mr");
        m_selTitle.addOption("02", "Miss");
        m_selTitle.addOption("03", "Ms");
        m_selTitle.addOption("04", "Mrs");
        m_selTitle.setSelected(0, true);
        m_tblLayout.getTableCell(1, 0).setText("First Name *");
        m_txtFirstName = new HtmlTextBox(m_tblLayout.getTableCell(1, 1), "m_txtFirstName");
        m_txtFirstName.setAttribute("size", "20");
        m_tblLayout.getTableCell(2, 0).setText("Last Name *");
        m_txtLastName = new HtmlTextBox(m_tblLayout.getTableCell(2, 1), "m_txtLastName");
        m_txtLastName.setSize("20");
        m_tblLayout.getTableCell(3, 0).setText("Gender");
        m_htmMale = new HtmlText(m_tblLayout.getTableCell(3, 1));
        m_htmMale.setText("M");
        m_radMale = new HtmlRadioButton(m_tblLayout.getTableCell(3, 1), "m_radGender");
        m_radMale.setValue("M");
        m_radMale.setBooleanAttribute("checked", true);
        m_htmFemale = new HtmlText(m_tblLayout.getTableCell(3, 1));
        m_htmFemale.setText("F");
        m_radFemale = new HtmlRadioButton(m_tblLayout.getTableCell(3, 1), "m_radGender");
        m_radFemale.setValue("F");
        m_tblLayout.getTableCell(4, 0).setText("Street");
        m_txaStreet = new HtmlTextArea(m_tblLayout.getTableCell(4, 1), "m_txaStreet");
        m_txaStreet.setCols("40");
        m_txaStreet.setRows("3");
        m_tblLayout.getTableCell(5, 0).setText("Same as postal address?");
        m_cbxPostalAddress = new HtmlCheckBox(m_tblLayout.getTableCell(5, 1), "m_cbxPostalAddress");
        m_cbxPostalAddress.setValue("Y");
        m_tblLayout.getTableRow(6).setBgColor("#D7FFFF");
        m_tblLayout.getTableCell(6, 0).setColSpan(2);
        m_tblLayout.getTableCell(6, 0).setAlign("center");
        m_btnSave = new HtmlPushButton(m_tblLayout.getTableCell(6, 0), "m_btnSave");
        m_btnSave.setText("Save");
        m_btnSave.setButtonType("button");
        m_btnSave.setOnClick("customerform.m_hidProxy.value='save';customerform.submit();");
        m_btnReset = new HtmlPushButton(m_tblLayout.getTableCell(6, 0), "m_btnReset");
        m_btnReset.setText("Reset");
        m_btnReset.setButtonType("reset");
        m_btnConnect = new HtmlPushButton(m_tblLayout.getTableCell(6, 0), "m_btnConnect");
        m_btnConnect.setText("Connect");
        m_btnConnect.setButtonType("submit");
        m_btnDisconnect = new HtmlPushButton(m_tblLayout.getTableCell(6, 0), "m_btnDisconnect");
        m_btnDisconnect.setText("Disconnect");
        m_btnDisconnect.setButtonType("submit");
        m_hidProxy = new HtmlHiddenInput(m_tblLayout.getTableCell(6, 0), "m_hidProxy");
        m_hidProxy.setAutoUpdate(false);
        m_btnSave.setClickedProxy(m_hidProxy, "save");
        m_btnSave.setUpdateOrder(10);
        connect(this.slot("connectEvents()"), m_btnConnect.signal("clicked()"));
        connect(this.slot("disconnectEvents()"), m_btnDisconnect.signal("clicked()"));
        connect(this.slot("saveForm()"), m_btnSave.signal("clicked()"));
    }

    public void handleError(SlotId objSlotId, Signal objSignal, Throwable objError) {
        System.out.println("handling the error...");
        System.out.println(objError.toString());
        System.out.println("slot = " + objSlotId.getMethod().getName());
        System.out.println("signal = " + objSignal.getSignalId().getMethod().getName());
    }

    public void connectEvents() {
        try {
            connect(this.slot("processValueChanged2(String,String)"), m_txtFirstName.signal("valueChanged(String,String)"));
            connect(this.slot("processValueChanged(String,String)"), m_txtFirstName.signal("valueChanged(String,String)"));
            connect(this.slot("processValueChanged(String)"), m_txtFirstName.signal("valueChanged(String,String)"));
            connect(this.slot("processDataSubmitted(String)"), m_txtFirstName.signal("dataSubmitted(String)"));
            connect(this.slot("processDataSubmitted(String[])"), m_selTitle.signal("dataSubmitted(String[])"));
            connect(this.slot("processOptionSelected(int)"), m_selTitle.signal("optionSelected(int)"));
            connect(this.slot("processSelectionUpdated(int[], int[])"), m_selTitle.signal("selectionUpdated(int[], int[])"));
            connect(this.slot("processOptionSelected(String)"), m_selTitle.signal("optionSelected(String)"));
            connect(this.slot("processSelectionUpdated(String[], String[])"), m_selTitle.signal("selectionUpdated(String[], String[])"));
            connect(this.slot("processCheckBoxChanged(boolean)"), m_cbxPostalAddress.signal("selectionChanged(boolean)"));
            connect(this.slot("processRadioButtonChanged(boolean)", this), m_radMale.signal("selectionChanged(boolean)"));
            connect(m_cbxPostalAddress.slot("selectionChanged(boolean)"), m_radFemale.signal("selectionChanged(boolean)"));
            connect(this.slot("processTextAreaChanged(String,String)"), m_txaStreet.signal("textChanged(String,String)"));
            connect(this.slot("processBeforeUpdateData()"), this.signal("beforeUpdateData(javax.servlet.http.HttpServletRequest)"));
            connect(this.slot("processAfterUpdateData()"), this.signal("afterUpdateData(javax.servlet.http.HttpServletRequest)"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void disconnectEvents() {
        try {
            disconnect(this.slot("processValueChanged(String,String)"), m_txtFirstName.signal("valueChanged(String,String)"));
            disconnect(this.slot("processValueChanged(String,String)"), m_txtFirstName.signal("valueChanged(String,String)"));
            disconnect(this.slot("processValueChanged(String)"), m_txtFirstName.signal("valueChanged(String,String)"));
            disconnect(this.slot("processOptionSelected(int)"), m_selTitle.signal("optionSelected(int)"));
            disconnect(this.slot("processSelectionUpdated(int[], int[])"), m_selTitle.signal("selectionUpdated(int[], int[])"));
            disconnect(this.slot("processOptionSelected(String)"), m_selTitle.signal("optionSelected(String)"));
            disconnect(this.slot("processSelectionUpdated(String[], String[])"), m_selTitle.signal("selectionUpdated(String[], String[])"));
            disconnect(this.slot("processCheckBoxChanged(boolean)"), m_cbxPostalAddress.signal("selectionChanged(boolean)"));
            disconnect(this.slot("processRadioButtonChanged(boolean)"), m_radMale.signal("selectionChanged(boolean)"));
            disconnect(this.slot("processTextAreaChanged(String,String)"), m_txaStreet.signal("textChanged(String,String)"));
            disconnect(this.slot("processBeforeUpdateData()"), this.signal("beforeUpdateData(javax.servlet.http.HttpServletRequest)"));
            disconnect(this.slot("processAfterUpdateData()"), this.signal("afterUpdateData(javax.servlet.http.HttpServletRequest)"));
            disconnect(this.slot("processDataSubmitted(String)"), m_txtFirstName.signal("dataSubmitted(String)"));
            disconnect(this.slot("processDataSubmitted(String[])"), m_selTitle.signal("dataSubmitted(String[])"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processBeforeUpdateData() {
        System.out.println("Before update.");
    }

    public void processAfterUpdateData() {
        System.out.println("After update.");
    }

    public void processTextAreaChanged(String sNewText, String sOldText) {
        System.out.println("TextArea New Text = " + sNewText);
        System.out.println("TextArea Old Text = " + sOldText);
    }

    public void processRadioButtonChanged(boolean bChecked) {
        System.out.println("RadioButton Checked = " + bChecked);
        String sTemp = null;
        sTemp.length();
    }

    public void processCheckBoxChanged(boolean bChecked) {
        System.out.println("CheckBox Checked = " + bChecked);
    }

    public void processDataSubmitted(String sNewValue1) {
        System.out.println("data submitted = " + sNewValue1);
    }

    public void processDataSubmitted(String[] asDataSubmitted) {
        for (int i = 0; i < asDataSubmitted.length; i++) System.out.println("asDataSubmitted[" + i + "] = " + asDataSubmitted[i]);
    }

    public void processValueChanged(String sNewValue, String sOldValue) {
        System.out.println("sNewValue = " + sNewValue);
        System.out.println("sOldValue = " + sOldValue);
    }

    public void processValueChanged2(String sNewValue, String sOldValue) {
        System.out.println("sNewValue2 = " + sNewValue);
        System.out.println("sOldValue2 = " + sOldValue);
    }

    public void processValueChanged(String sNewValue1) {
        System.out.println("sNewValue1 = " + sNewValue1);
    }

    public void processOptionSelected(int nIndex) {
        System.out.println("nIndex = " + nIndex);
    }

    public void processSelectionUpdated(int[] anNewIndicies, int[] anOldIndicies) {
        for (int i = 0; i < anNewIndicies.length; i++) System.out.println("anNewIndicies[" + i + "] = " + anNewIndicies[i]);
        for (int i = 0; i < anOldIndicies.length; i++) System.out.println("anOldIndicies[" + i + "] = " + anOldIndicies[i]);
    }

    public void processOptionSelected(String sValue) {
        System.out.println("sValue = " + sValue);
    }

    public void processSelectionUpdated(String[] asNewIndicies, String[] asOldIndicies) {
        for (int i = 0; i < asNewIndicies.length; i++) System.out.println("asNewValues[" + i + "] = " + asNewIndicies[i]);
        for (int i = 0; i < asOldIndicies.length; i++) System.out.println("asOldValues[" + i + "] = " + asOldIndicies[i]);
    }

    /**
	 * This method should be called when the save action is triggered.
	 */
    public void saveForm() throws InvalidAttributeException {
        m_tblLayout.setAttribute("class", null, true);
        m_tblLayout.setAttribute("class", "normal");
        m_htmUserMessage.setText("Saved.");
        m_parUserMessage.setVisible(true);
        m_parUserMessage.setClassAttribute("info");
    }
}
