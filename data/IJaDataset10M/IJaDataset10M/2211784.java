package testsuite;

import objectivehtml.htmlwidget.*;
import objectivehtml.htmlwidget.exception.*;

/**
 * Tests that the control names are property created and deleted.
 */
public class ControlNameTestForm extends HtmlForm {

    private static final String CONTROLNAME = "testname";

    public HtmlParagraph m_parButtons;

    public HtmlPushButton m_btnAddCheckBox;

    public HtmlPushButton m_btnAddTextBox;

    public HtmlPushButton m_btnAddTextArea;

    public HtmlPushButton m_btnAddListBox;

    public HtmlPushButton m_btnAddRadioButton;

    public HtmlPushButton m_btnAddPushButton;

    public HtmlPushButton m_btnRemove;

    public ControlNameTestForm() throws Exception {
        super();
        setMethod("post");
        m_parButtons = new HtmlParagraph(this);
        m_btnAddCheckBox = new HtmlPushButton(m_parButtons, "m_btnAddCheckBox");
        m_btnAddCheckBox.setText("Add CheckBox");
        m_btnAddCheckBox.setButtonType("submit");
        m_btnAddTextBox = new HtmlPushButton(m_parButtons, "m_btnAddTextBox");
        m_btnAddTextBox.setText("Add TextBox");
        m_btnAddTextBox.setButtonType("submit");
        m_btnAddTextArea = new HtmlPushButton(m_parButtons, "m_btnAddTextArea");
        m_btnAddTextArea.setText("Add TextArea");
        m_btnAddTextArea.setButtonType("submit");
        m_btnAddListBox = new HtmlPushButton(m_parButtons, "m_btnAddListBox");
        m_btnAddListBox.setText("Add ListBox");
        m_btnAddListBox.setButtonType("submit");
        m_btnAddRadioButton = new HtmlPushButton(m_parButtons, "m_btnAddRadioButton");
        m_btnAddRadioButton.setText("Add RadioButton");
        m_btnAddRadioButton.setButtonType("submit");
        m_btnAddPushButton = new HtmlPushButton(m_parButtons, "m_btnAddPushButton");
        m_btnAddPushButton.setText("Add PushButton");
        m_btnAddPushButton.setButtonType("submit");
        m_btnRemove = new HtmlPushButton(m_parButtons, "m_btnRemove");
        m_btnRemove.setText("Remove Object");
        m_btnRemove.setButtonType("submit");
    }

    public void addCheckBox() throws Exception {
        HtmlCheckBox obj = new HtmlCheckBox(this, CONTROLNAME);
        insertChild(obj, 0);
    }

    public void addTextBox() throws Exception {
        HtmlTextBox obj = new HtmlTextBox(this, CONTROLNAME);
        insertChild(obj, 0);
    }

    public void addTextArea() throws Exception {
        HtmlTextArea obj = new HtmlTextArea(this, CONTROLNAME);
        insertChild(obj, 0);
    }

    public void addListBox() throws Exception {
        HtmlListBox obj = new HtmlListBox(this, CONTROLNAME);
        insertChild(obj, 0);
    }

    public void addRadioButton() throws Exception {
        HtmlRadioButton obj = new HtmlRadioButton(this, CONTROLNAME);
        insertChild(obj, 0);
    }

    public void addPushButton() throws Exception {
        HtmlPushButton obj = new HtmlPushButton(this, CONTROLNAME);
        insertChild(obj, 0);
    }

    public void removeObject() throws Exception {
        if (getChildrenCount() > 1) {
            HtmlWidget obj = getChildWidget(0);
            if (obj instanceof HtmlControlWidget) ((HtmlControlWidget) obj).destroy();
        }
    }
}
