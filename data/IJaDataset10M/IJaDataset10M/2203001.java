package mosinstaller.awt;

import java.awt.Color;
import java.awt.Image;
import java.awt.TextArea;
import java.util.ResourceBundle;
import mosinstaller.MOSI;
import mosinstaller.TextWizardPanel;

/**
 * @author j
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class AbstractATextWizardPanel extends AWizardPanel implements TextWizardPanel {

    protected TextArea text = new TextArea();

    protected static ResourceBundle messages;

    static {
        try {
            messages = AWizardPanel.messages.getBundle("mosinstaller.awt.ATextWizardPanel", MOSI.getLocale());
        } catch (Exception e) {
            messages = AWizardPanel.messages.getBundle("mosinstaller.awt.ATextWizardPanel");
        }
    }

    public AbstractATextWizardPanel() {
        _isBackEnabled = false;
        text.setEditable(false);
    }

    public AbstractATextWizardPanel(String t) {
        this();
        setText(t);
    }

    public void setText(String t) {
        text.setText(t);
    }

    public String getText() {
        return text.getText();
    }

    public String getTopText() {
        return getString("topText");
    }

    public String getTopTitle() {
        return getString("topTitle");
    }

    public Image getTopImage() {
        java.net.URL imageURL = getClass().getResource(getString("topImage"));
        if (imageURL != null) {
            return getToolkit().getImage(imageURL);
        }
        return null;
    }

    public static String getString(String s) {
        if (messages == null || s == null) return null;
        return messages.getString(s);
    }
}
