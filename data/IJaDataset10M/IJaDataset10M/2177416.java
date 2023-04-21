package gui.objects;

import javax.swing.JTextArea;
import java.util.Date;
import java.awt.Font;
import java.text.SimpleDateFormat;
import utils.StringUtils;

public class JLogTextArea extends JTextArea {

    private SimpleDateFormat dateFormat;

    private String newline;

    private String clearMessage;

    private int indent;

    private boolean printDate;

    private static String spaces;

    private static final int tabOut = 3;

    private static final long serialVersionUID = 2976366122376675229L;

    /**
	 * 
	 */
    public JLogTextArea() {
        init();
    }

    public JLogTextArea(int height, int width) {
        super(height, width);
        init();
    }

    private void init() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        newline = "\n";
        clearMessage = "";
        setEditable(false);
        setFont(new Font("Monospaced", Font.PLAIN, 12));
        spaces = StringUtils.repeat(80, ' ');
        setUseDate(false);
        setIndent(0);
    }

    public void setClearMessage(String message) {
        clearMessage = message;
    }

    public String SYSDATE() {
        Date now = new java.util.Date();
        String date = dateFormat.format(now);
        return (date);
    }

    public void appendtextln(String text) {
        printtext(text + newline);
    }

    public void println(String text) {
        printtextln(text);
    }

    public void printtextln(String text) {
        printtext(text + newline);
    }

    public void printDivider() {
        printtextln("--------------------------------");
    }

    public void printtext(String text) {
        printtext(printDate, text);
    }

    public void printtext(boolean includeSysdate, String text) {
        append(indentText(text, includeSysdate));
        setCaretPosition(this.getDocument().getLength());
    }

    public void clear() {
        setText("");
        if (clearMessage.length() > 0) println(clearMessage);
        repaint();
    }

    /**
	 * Indent text after every new-line.
	 * Note: uses the indentation set by setIndent(###) to calculate indentation
	 * Note: it ALSO sets up SYSDATE if requested, even if INDENT is zero
	 * @param String text
	 * @return String text
	 *
	 */
    private String indentText(String text, boolean useDate) {
        int ind = getIndent();
        String dateStr = SYSDATE() + " ";
        if (useDate == false) dateStr = "";
        if (ind == 0) return (dateStr + text);
        return (dateStr + spaces.substring(0, ind * tabOut) + text.replaceAll("\n", "\n" + dateStr + spaces.substring(0, ind * tabOut)));
    }

    public void setIndent(int indentSpacing) {
        indent = indentSpacing;
    }

    public int getIndent() {
        return (indent);
    }

    public void setUseDate(boolean useDate) {
        printDate = useDate;
    }
}
