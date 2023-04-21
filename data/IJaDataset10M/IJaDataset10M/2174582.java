package jsyntaxpane.actions;

import java.awt.event.ActionEvent;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.text.JTextComponent;
import jsyntaxpane.SyntaxDocument;

/**
 * This action will toggle comments on or off on selected whole lines.
 * 
 * @author Ayman Al-Sairafi
 */
public class ToggleCommentsAction extends DefaultSyntaxAction {

    protected String lineCommentStart = "// ";

    protected Pattern lineCommentPattern = null;

    /**
     * creates new JIndentAction.
     * Initial Code contributed by ser... AT mail.ru
     */
    public ToggleCommentsAction() {
        super("toggle-comment");
    }

    /**
     * {@inheritDoc}
     * @param e 
     */
    @Override
    public void actionPerformed(JTextComponent target, SyntaxDocument sDoc, int dot, ActionEvent e) {
        if (lineCommentPattern == null) {
            lineCommentPattern = Pattern.compile("(^" + lineCommentStart + ")(.*)");
        }
        String[] lines = ActionUtils.getSelectedLines(target);
        int start = target.getSelectionStart();
        StringBuffer toggled = new StringBuffer();
        for (int i = 0; i < lines.length; i++) {
            Matcher m = lineCommentPattern.matcher(lines[i]);
            if (m.find()) {
                toggled.append(m.replaceFirst("$2"));
            } else {
                toggled.append(lineCommentStart);
                toggled.append(lines[i]);
            }
            toggled.append('\n');
        }
        target.replaceSelection(toggled.toString());
        target.select(start, start + toggled.length());
    }

    public void setLineComments(String value) {
        lineCommentStart = value.replace("\"", "");
    }
}
