package ossobook2010.gui.components.content.input_fields.basic;

import ossobook2010.gui.components.content.input_fields.interfaces.FocusableInputField;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import ossobook2010.exceptions.IsAMandatoryFieldException;
import ossobook2010.gui.MainFrame;
import ossobook2010.gui.stylesheet.Colors;

/**
 * A general object for the OssoBook input fields which contains a textarea.
 * 
 * This class extends the FocusInputField class, so it inherbit all
 * standard elements of the input fields (e.g. checkbox, title).
 * 
 * @author Daniel Kaltenthaler
 */
public class OTextArea extends FocusableInputField implements MouseListener {

    /** The neccessary serial version ID. */
    private static final long serialVersionUID = 2298121422227584774L;

    /** The basic JPanel object holding the intrinsic content. */
    private JPanel panel;

    /** The JTextArea object. */
    private JTextArea textArea;

    /** The text which is displayed as a tooltip */
    private String tooltip;

    /**
	 * Constructor of the OTextArea class.
	 * 
	 * Initializes and sets up the OTextArea object and add it to the
	 * BasicFieldWrapper object.
	 * 
	 * @param mainFrame The basic MainFrame object.
	 * @param width The width of the input field.
	 * @param height The height of the input field.
	 * @param title The description of the input field.
	 * @param dbColumnName The correspondenting database column
	 *                     name of the input field
	 * @param tooltip The tooltip which should be displayed on hover.
	 * @param focusSidebar Hold the JPanel with the content which is
	 * 					   displayed when the input field get the focus.
	 */
    public OTextArea(MainFrame mainFrame, int width, int height, String title, String dbColumnName, String tooltip, JPanel focusSidebar) {
        super(mainFrame, width, height, title, dbColumnName, focusSidebar);
        this.mainFrame = mainFrame;
        this.tooltip = tooltip;
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        textArea = new JTextArea();
        textArea.addMouseListener(this);
        textArea.addFocusListener(this);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(BorderFactory.createLineBorder(new Color(171, 173, 179)));
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        panel.add(BorderLayout.CENTER, scrollPane);
        setContent(panel);
    }

    @Override
    public String getValue() throws IsAMandatoryFieldException {
        if (isMandatoryField && textArea.getText().equals("")) {
            throw new IsAMandatoryFieldException();
        }
        System.out.println("textnull?:" + textArea.getText() == null);
        return textArea.getText();
    }

    @Override
    public Boolean isChecked() {
        return getCheckBox().isSelected();
    }

    @Override
    public void clearValue() {
        textArea.setText("");
    }

    @Override
    public String getDbColumnName() {
        return dbColumnName;
    }

    @Override
    public void setValue(String newValue) {
        textArea.setText((String) newValue);
    }

    @Override
    public void setValue(Object newValue) {
    }

    @Override
    public void setEditable(boolean status) {
        textArea.setEnabled(status);
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        mainFrame.displayTooltip(tooltip);
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        mainFrame.getFooter().removeText();
    }

    @Override
    public void focusGained(FocusEvent event) {
        super.focusGained(event);
        textArea.setBackground(Colors.INFOBAR_BACKGROUND);
    }

    @Override
    public void focusLost(FocusEvent event) {
        super.focusGained(event);
        textArea.setBackground(Color.WHITE);
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
    }
}
