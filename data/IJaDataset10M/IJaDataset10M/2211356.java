package tk.bot;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;

public class BotTextWrapper implements BotTextDisplay {

    public static final int FIELD = 1;

    public static final int AREA = 1 << 1;

    public static final int JFIELD = 1 << 2;

    public static final int JAREA = 1 << 3;

    public static final int JPANE = 1 << 4;

    Object textDisplay;

    int indicator;

    BotColorizer colorizer;

    public BotTextWrapper(TextField field) {
        this.textDisplay = field;
        ;
        indicator = FIELD;
    }

    public BotTextWrapper(TextArea area) {
        this.textDisplay = area;
        indicator = AREA;
    }

    public BotTextWrapper(JTextField jField) {
        this.textDisplay = jField;
        indicator = JFIELD;
    }

    public BotTextWrapper(JTextArea jArea) {
        this.textDisplay = jArea;
        indicator = JAREA;
    }

    public BotTextWrapper(JTextPane jPane) {
        this.textDisplay = jPane;
        indicator = JPANE;
    }

    public void setText(String line) {
        if (textDisplay instanceof TextComponent) {
            TextComponent comp = (TextComponent) textDisplay;
            if (comp.isVisible()) comp.setText(line);
        }
        if (textDisplay instanceof JTextComponent) {
            JTextComponent comp = (JTextComponent) textDisplay;
            if (comp.isVisible()) comp.setText(line);
        }
    }

    public String getText() {
        if (textDisplay instanceof TextComponent) {
            TextComponent comp = (TextComponent) textDisplay;
            return comp.getText();
        }
        if (textDisplay instanceof JTextComponent) {
            JTextComponent comp = (JTextComponent) textDisplay;
            return comp.getText();
        }
        return "";
    }

    public void append(String line) {
        if (textDisplay instanceof TextArea) {
            TextArea comp = (TextArea) textDisplay;
            if (comp.isVisible()) comp.append(line);
        }
        if (textDisplay instanceof JTextArea) {
            JTextArea comp = (JTextArea) textDisplay;
            if (comp.isVisible()) {
                comp.append(line);
                comp.setCaretPosition(comp.getText().length());
            }
        }
        if (textDisplay instanceof JTextPane) {
            JTextPane comp = (JTextPane) textDisplay;
            if (comp.isVisible()) {
                if (!colorizer.append(line)) {
                    try {
                        comp.getDocument().insertString(comp.getDocument().getLength(), line, new SimpleAttributeSet());
                        comp.setCaretPosition(comp.getDocument().getLength());
                    } catch (BadLocationException e) {
                        if (TKBot.DEBUG > 3) System.out.println(e);
                    }
                }
            }
        }
    }

    public void activate() {
        if (indicator == JPANE) {
            colorizer = new BotColorizer(this);
            colorizer.start();
        }
    }

    public void inactivate() {
        if (indicator == JPANE) {
            if (colorizer != null) colorizer.shutdown();
        }
    }

    public void dispose() {
        inactivate();
    }

    public void clear() {
        setText("");
    }
}
