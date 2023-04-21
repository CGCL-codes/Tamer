package self.amigo.editor.sheet;

import java.awt.BorderLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import self.amigo.editor.APropertySheet;
import self.amigo.editor.cmd.PropertiesChange;
import self.amigo.elem.ButtonView;
import self.amigo.elem.InputBoxView;
import self.awt.layout.VerticalFlowLayout;
import self.gee.IEditableElement;
import self.gee.editor.IPropertySheet;

public class InputBoxSheet extends APropertySheet implements IPropertySheet {

    /**
	 * If the internal state of this class ever changes in such a way that it can't be defaulted,
	 * then the {@link #serialVersionUID} should be incremented to ensure serialized instances cleanly fail.  
	 */
    private static final long serialVersionUID = 1;

    JTabbedPane propertySheet = new JTabbedPane();

    BorderLayout contentPaneLayout = new BorderLayout();

    JPanel generalTab = new JPanel();

    VerticalFlowLayout generalPaneLayout = new VerticalFlowLayout();

    JPanel generalPane = new JPanel();

    JScrollPane generalScroller = new JScrollPane();

    BorderLayout generalLayout = new BorderLayout();

    JLabel textLabel = new JLabel();

    JTextArea text = new JTextArea();

    JScrollPane textScroller = new JScrollPane();

    ButtonGroup btnGroup = new ButtonGroup();

    JRadioButton singleLineEditType = new JRadioButton();

    JRadioButton comboType = new JRadioButton();

    JRadioButton memoType = new JRadioButton();

    JCheckBox horizBar = new JCheckBox();

    JCheckBox vertBar = new JCheckBox();

    JLabel aidLinkLabel = new JLabel();

    JTextField aidLink = new JTextField();

    private IEditableElement view;

    public InputBoxSheet() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        this.setLayout(contentPaneLayout);
        aidLinkLabel.setDisplayedMnemonic('0');
        aidLinkLabel.setText("aid");
        textLabel.setText("edit text");
        textLabel.setDisplayedMnemonic('T');
        textLabel.setLabelFor(text);
        text.setRows(10);
        text.setWrapStyleWord(true);
        btnGroup.add(singleLineEditType);
        btnGroup.add(comboType);
        btnGroup.add(memoType);
        singleLineEditType.setSelected(true);
        singleLineEditType.setText("single line edit");
        comboType.setText("combo box");
        memoType.setText("memo edit");
        horizBar.setText("show horizontal scrollbar");
        vertBar.setText("show verical scrollbar");
        textScroller.getViewport().add(text, null);
        generalPane.setLayout(generalPaneLayout);
        generalPane.add(textLabel, null);
        generalPane.add(textScroller, null);
        generalPane.add(singleLineEditType, null);
        generalPane.add(comboType, null);
        generalPane.add(memoType, null);
        generalPane.add(horizBar, null);
        generalPane.add(vertBar, null);
        generalPane.add(aidLinkLabel, null);
        generalPane.add(aidLink, null);
        generalScroller.getViewport().add(generalPane, null);
        generalTab.setLayout(generalLayout);
        generalTab.add(generalScroller, BorderLayout.CENTER);
        this.add(propertySheet, BorderLayout.CENTER);
        propertySheet.add(generalTab, "General");
    }

    public void inspect(Object toDisplay) {
        if (toDisplay instanceof InputBoxView) {
            view = (IEditableElement) toDisplay;
            revert();
        } else throw new RuntimeException("Invalid Graphic element to display:" + toDisplay);
    }

    public void accept() {
        if (view == null) return;
        store.put(InputBoxView.TEXT_PROP, text.getText());
        store.put(InputBoxView.TYPE_PROP, new Integer(getType()));
        store.put(InputBoxView.SCROLLBAR_PROP, new Integer(getScrollBars()));
        store.put(InputBoxView.AID_PROP, aidLink.getText());
        PropertiesChange cmd = new PropertiesChange(editor, view, store);
        cmd.executeIfRequired();
    }

    public void revert() {
        if (view == null) return;
        store.clear();
        view.getProperties(store);
        text.setText((String) store.get(InputBoxView.TEXT_PROP));
        setType(((Integer) store.get(InputBoxView.TYPE_PROP)).intValue());
        setScrollBars(((Integer) store.get(InputBoxView.SCROLLBAR_PROP)).intValue());
        aidLink.setText((String) store.get(ButtonView.AID_PROP));
    }

    public Object getObjectUnderInspection() {
        return view;
    }

    private int getType() {
        if (singleLineEditType.isSelected()) return InputBoxView.SINGLE_LINE_EDIT;
        if (comboType.isSelected()) return InputBoxView.COMBO_BOX;
        return InputBoxView.MEMO_EDIT;
    }

    private int getScrollBars() {
        boolean h = horizBar.isSelected();
        boolean v = vertBar.isSelected();
        if (h && v) return InputBoxView.BOTH_SCROLLBARS;
        if (h) return InputBoxView.HORIZ_SCROLLBAR;
        if (v) return InputBoxView.VERT_SCROLLBAR;
        return InputBoxView.NO_SCROLLBARS;
    }

    private void setScrollBars(int val) {
        if (val == InputBoxView.NO_SCROLLBARS) {
            horizBar.setSelected(false);
            vertBar.setSelected(false);
            return;
        }
        horizBar.setSelected((val == InputBoxView.HORIZ_SCROLLBAR) || (val == InputBoxView.BOTH_SCROLLBARS));
        vertBar.setSelected((val == InputBoxView.VERT_SCROLLBAR) || (val == InputBoxView.BOTH_SCROLLBARS));
    }

    private void setType(int val) {
        switch(val) {
            case InputBoxView.MEMO_EDIT:
                memoType.setSelected(true);
                break;
            case InputBoxView.COMBO_BOX:
                comboType.setSelected(true);
                break;
            default:
                singleLineEditType.setSelected(true);
                break;
        }
    }
}
