package ossobook2010.gui.components.content.input_fields.specific;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JPanel;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.components.content.input_fields.interfaces.FocusableInputField;
import ossobook2010.gui.components.sidebar.elements.GenBankNrBar;
import ossobook2010.gui.stylesheet.Colors;

public class GenBankNrButton extends FocusableInputField implements MouseListener {

    /** The neccessary serial version ID. */
    private static final long serialVersionUID = 5916875317431288895L;

    /** The basic JPanel object holding the intrinsic content. */
    private JPanel panel;

    /** The JButton object. */
    private JButton inputField;

    /** The text which is displayed as a tooltip */
    private String tooltip;

    private ArrayList<String> data;

    private GenBankNrBar focusSidebar;

    /**
	 *
	 *
	 * @param mainFrame The basic MainFrame object.
	 * @param width The width of the input field.
	 * @param height The height of the input field.
	 * @param title The description of the input field.
	 * @param dbColumnName The correspondenting database column
	 *                     name of the input field
	 * @param buttonLabel The label of the button.
	 * @param tooltip The tooltip which should be displayed on hover.
	 * @param focusSidebar Hold the JPanel with the content which is
	 * 					   displayed when the input field get the focus.
	 */
    public GenBankNrButton(MainFrame mainFrame, int width, int height, String title, String dbColumnName, String buttonLabel, String tooltip, JPanel focusSidebar) {
        super(mainFrame, width, height, title, dbColumnName, focusSidebar);
        this.mainFrame = mainFrame;
        this.tooltip = tooltip;
        this.focusSidebar = (GenBankNrBar) focusSidebar;
        data = new ArrayList<String>();
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        inputField = new JButton(buttonLabel);
        inputField.addMouseListener(this);
        inputField.addFocusListener(this);
        panel.add(BorderLayout.CENTER, inputField);
        setContent(panel);
    }

    @Override
    public String getValue() {
        return null;
    }

    public ArrayList<String> getGenBankNr() {
        return data;
    }

    @Override
    public void setValue(String newValue) {
    }

    public void setValue(ArrayList<String> newValues) {
        focusSidebar.setValues(newValues);
    }

    @Override
    public void setValue(Object newValue) {
        setValue((ArrayList<String>) newValue);
    }

    @Override
    public Boolean isChecked() {
        return getCheckBox().isSelected();
    }

    @Override
    public void clearValue() {
        focusSidebar.clear();
    }

    @Override
    public void setEditable(boolean status) {
        inputField.setEnabled(status);
    }

    @Override
    public void mouseEntered(MouseEvent event) {
        mainFrame.displayTooltip(tooltip);
    }

    @Override
    public void mouseExited(MouseEvent event) {
        mainFrame.getFooter().removeText();
    }

    @Override
    public void focusGained(FocusEvent event) {
        super.focusGained(event);
        inputField.setBackground(Colors.INFOBAR_BACKGROUND);
    }

    @Override
    public void focusLost(FocusEvent event) {
        super.focusGained(event);
        inputField.setBackground(Color.WHITE);
    }

    @Deprecated
    public void mouseClicked(MouseEvent event) {
    }

    @Deprecated
    public void mousePressed(MouseEvent event) {
    }

    @Deprecated
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void setEditableField(boolean b) {
        inputField.setEnabled(false);
    }
}
