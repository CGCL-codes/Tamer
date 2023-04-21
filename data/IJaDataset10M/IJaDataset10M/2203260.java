package editor.controller.filters;

import java.util.ArrayList;
import java.util.List;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import editor.model.DocumentBase;
import editor.view.EditorViewConstants;

/**
 * Code filter for the editor
 * 
 * @author hao sun
 * @version $Revision: 1.1 $
 * @since version 1.1
 */
public class IndentationKeyFilter implements IKeyFilter, EditorViewConstants {

    private List<KeyStroke> keys;

    private DocumentBase doc;

    /**
     * Forbbit constructing without params
     * 
     */
    private IndentationKeyFilter() {
        keys = new ArrayList<KeyStroke>();
    }

    /**
     * Construct filter by keyStrokes
     * 
     * @param key
     * @param doc
     */
    public IndentationKeyFilter(DocumentBase doc) {
        this();
        this.keys.add(KeyStroke.getKeyStroke('\n'));
        this.doc = doc;
    }

    public List<KeyStroke> getKeyStrokes() {
        return keys;
    }

    /**
     * Perform filter action
     * 
     * @param input
     * @param doc
     */
    private void performFilter(KeyStroke input, DocumentBase doc) {
        int indentation = findIndentation();
        StringBuffer indentationString = new StringBuffer("");
        for (int i = 0; i < indentation; i++) {
            indentationString.append(" ");
        }
        doc.insertString(doc.getCurrentCaretEndPostion(), indentationString.toString());
    }

    public boolean performFilter(KeyStroke input) {
        boolean canBeFilted = false;
        canBeFilted = keyChecker(input);
        if (canBeFilted) performFilter(input, doc);
        return canBeFilted;
    }

    /**
     * Check current key is match the filter condition or not
     * 
     * @param input
     * @return
     */
    private boolean keyChecker(KeyStroke input) {
        return (keys.contains(input));
    }

    /**
     * Find Indentation for current input
     * @return
     */
    private int findIndentation() {
        Element lastLine = doc.getDocument().getDefaultRootElement().getElement(doc.getDocument().getDefaultRootElement().getElementIndex(doc.getCurrentCaretEndPostion()) - 1);
        String tempLineString = "";
        try {
            tempLineString = doc.getDocument().getText(lastLine.getStartOffset(), lastLine.getEndOffset() - lastLine.getStartOffset());
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        int result = 0;
        for (int i = 0; i < tempLineString.length(); i++) {
            if (tempLineString.charAt(i) == ' ') result++; else if (tempLineString.charAt(i) == '\t') result += DEFAULT_TABKEYINCREMENT; else break;
        }
        return result;
    }
}
