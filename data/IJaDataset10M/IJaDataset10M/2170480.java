package irc.style;

import java.util.*;

/**
 * Multiple word catcher.
 */
public class MultipleWordCatcher implements WordCatcher {

    private Vector _recognizers;

    /**
   * Create a new MultipleWordCatcher.
   */
    public MultipleWordCatcher() {
        _recognizers = new Vector();
    }

    /**
   * Add a recognizer in the list.
   * @param wr recognizer to add.
   */
    public void addRecognizer(WordRecognizer wr) {
        _recognizers.insertElementAt(wr, _recognizers.size());
    }

    public String getType(String word) {
        Enumeration e = _recognizers.elements();
        while (e.hasMoreElements()) {
            WordRecognizer wr = (WordRecognizer) e.nextElement();
            if (wr.recognize(word)) return wr.getType();
        }
        return null;
    }
}
