package ontool.app.modelview;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JTextField;
import org.apache.log4j.Category;

class TextFieldEditor extends JTextField implements FocusListener, KeyListener {

    private Category cat = Category.getInstance(TextFieldEditor.class);

    private String originalText;

    private String lastText;

    private ModelPropertySheet ps;

    private Color colorOn = new Color(255, 255, 255);

    private Color colorOff = new Color(205, 205, 205);

    public TextFieldEditor(String t, ModelPropertySheet ps) {
        super(t);
        this.ps = ps;
        lastText = t;
        addKeyListener(this);
        addFocusListener(this);
        setBackground(colorOff);
        originalText = t;
    }

    public void setText(String t) {
        lastText = t;
        super.setText(t);
    }

    protected void commit() {
        lastText = getText();
        ps.apply();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            commit();
            transferFocus();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void focusGained(FocusEvent e) {
        setBackground(colorOn);
        selectAll();
    }

    public void focusLost(FocusEvent e) {
        commit();
        setBackground(colorOff);
    }

    /**
	 * Indicates if this property has been change since its creation.
	 * @return flag
	 */
    public boolean hasChanged() {
        return !originalText.equals(getText());
    }

    public void setDefaultText(String t) {
        originalText = t;
    }
}
