package view;

import java.awt.Point;
import javax.swing.JTextPane;
import javax.swing.text.Caret;
import javax.swing.text.Document;
import javax.swing.text.Style;

public class GuiConsole extends JTextPane {

    private String inputText = "";

    private boolean canWrite = true;

    private Caret caret;

    /**
	 * Don't know why this is necessary. But it is.
	 */
    private static final long serialVersionUID = 1L;

    public GuiConsole() {
        super();
        caret = getCaret();
        caret.setVisible(false);
    }

    public synchronized String loadInput() throws Exception {
        canWrite = false;
        int tries = 0;
        boolean success = false;
        while (!success && tries < 50) {
            try {
                setEditable(true);
                setCaretPosition(getText().length());
                caret.setVisible(true);
                success = true;
            } catch (Exception e) {
                tries++;
            }
        }
        tries = 0;
        int maxValue = 0;
        try {
            maxValue = FrmMain.getInstance().getTextScroller().getVerticalScrollBar().getMaximum();
            requestFocus();
        } catch (Exception e) {
        }
        while ((FrmMain.getInstance().getTextScroller().getViewport().getViewPosition() != (new Point(0, maxValue)) && (FrmMain.getInstance().getTextScroller().getVerticalScrollBar().getValue() != maxValue)) && tries < 100) {
            try {
                FrmMain.getInstance().getTextScroller().getViewport().setViewPosition(new Point(0, maxValue));
                FrmMain.getInstance().getTextScroller().getVerticalScrollBar().setValue(maxValue);
            } catch (Exception e) {
            }
            tries++;
        }
        while (inputText.isEmpty()) {
            Thread.yield();
        }
        String result = inputText;
        System.out.println(result);
        inputText = "";
        canWrite = true;
        caret.setVisible(false);
        setEditable(false);
        return result;
    }

    public void cancelLoad() {
        canWrite = true;
        setEditable(false);
        caret.setVisible(false);
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public static synchronized void print(String style, String text) {
        print(style, text, true);
    }

    public static synchronized void println() {
        print(StylesManager.NORMAL, "", true);
    }

    public static synchronized void print(String style, String text, boolean br) {
        GuiConsole cons = FrmMain.getInstance().getConsole();
        while (!cons.canWrite) {
            Thread.yield();
        }
        Document document = cons.getDocument();
        Style s;
        if (style == null) {
            s = cons.getStyle(StylesManager.NORMAL);
        } else {
            s = cons.getStyle(style);
        }
        if (text != null) {
            String t = null;
            if (br) t = text.trim() + "\n"; else t = text;
            boolean success = false;
            int count = 0;
            while (!success && count < 50) {
                try {
                    document.insertString(cons.getCaretPosition(), t, s);
                    success = true;
                } catch (Exception e) {
                    try {
                        cons.setCaret(new JTextPane().getCaret());
                        document.insertString(document.getLength(), t, s);
                        success = true;
                    } catch (Exception e1) {
                        count++;
                    }
                }
            }
            if (!success) {
                cons.setStyledDocument(new JTextPane().getStyledDocument());
                StylesManager.addAllStyles(cons.getStyledDocument());
            }
        }
        boolean success = false;
        int count = 0;
        while (!success && count < 50) {
            try {
                cons.setCaretPosition(document.getLength());
                success = true;
            } catch (Exception e) {
                count++;
            }
        }
    }

    public void setCaret(Caret caret) {
        super.setCaret(caret);
        this.caret = caret;
    }
}
