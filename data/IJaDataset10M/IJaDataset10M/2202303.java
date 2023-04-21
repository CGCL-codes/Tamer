package vqwiki.plugin.note;

import vqwiki.lex.ExternalLex;

/**
 * Lexer to display notes.
 *
 * @author Martijn van der Kleijn
 * @version $Revision: $ $Date: $
 */
public class NoteLex implements ExternalLex {

    /**
   * Convert the contents of the note plugin to suitable XHTML codes.
   *
   * @param text Name of topic
   * @return string Text of topic
   */
    public String process(String text) {
        try {
            return format(text);
        } catch (Exception e) {
            return "???" + e.getMessage() + "??? NOTE ???";
        }
    }

    private String format(String text) {
        return "<div class=\"note_boxparent\"><span id=\"note_title\">Note</span><p class=\"note_box\" id=\"note_content\">" + text + "</p></div>";
    }
}
