package ch.superj.console;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import ch.superj.core.SuperjException;

/**
 * 
 * @author Markus A. Kobold &lt;mkobold at sprintpcs dot com&gt;
 */
public class JConsole extends JComponent implements AdjustmentListener {

    public static final String CATEGORY_SYSTEM_OUT = "system";

    public static final String CATEGORY_SYSTEM_ERR = "err";

    private JTextPane console = new JTextPane();

    private JScrollPane container = new JScrollPane(console);

    private String TSPattern = null;

    private SimpleDateFormat sdf = null;

    private Color clrErrorCat = Color.RED;

    private String defaultText = null;

    private boolean autoScroll = true;

    public JConsole() {
        this("");
    }

    public JConsole(String defaultText) {
        this.defaultText = defaultText;
        setLayout(new BorderLayout());
        add(container, BorderLayout.CENTER);
        console.setEditable(false);
    }

    public void adjustmentValueChanged(AdjustmentEvent e) {
        autoScroll = !e.getValueIsAdjusting();
    }

    public Document getDocument() {
        return console.getDocument();
    }

    public String getText() {
        return console.getText();
    }

    public void append(String category, String s) {
        SimpleAttributeSet sas = new SimpleAttributeSet();
        if (s == null) return;
        if (s.trim().length() == 0) return;
        Document doc = getDocument();
        synchronized (doc) {
            if (category == null) category = CATEGORY_SYSTEM_OUT;
            StyleConstants.setFontFamily(sas, getFont().getFamily());
            StyleConstants.setFontSize(sas, getFont().getSize());
            StyleConstants.setBold(sas, getFont().isBold());
            StyleConstants.setItalic(sas, getFont().isItalic());
            if (TSPattern != null && sdf != null && !s.equalsIgnoreCase(defaultText)) {
                String ts = sdf.format(new Date());
                StyleConstants.setForeground(sas, getForeground());
                ts = ts.concat(" ");
                try {
                    doc.insertString(doc.getLength(), ts, sas);
                } catch (Exception e) {
                }
            }
            if (CATEGORY_SYSTEM_ERR.equals(category)) StyleConstants.setForeground(sas, clrErrorCat);
            try {
                doc.insertString(doc.getLength(), s, sas);
            } catch (Exception e) {
                throw new SuperjException("console error");
            }
        }
        if (autoScroll) {
            try {
                int length = doc.getLength();
                console.setCaretPosition(length);
            } catch (Exception e) {
                throw new SuperjException("console error", e);
            }
            JScrollBar vs = container.getVerticalScrollBar();
            vs.setValue(vs.getMaximum());
        }
        console.invalidate();
        repaint();
    }

    public void append(String s) {
        append(CATEGORY_SYSTEM_OUT, s);
    }

    public void setText(String text) {
        if (text == null) return;
        try {
            getDocument().remove(0, getDocument().getLength());
            if (text.trim().length() == 0) {
                append((defaultText == null ? "" : defaultText));
            } else {
                append(text);
            }
        } catch (Exception e) {
        }
    }
}
