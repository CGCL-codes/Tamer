package jgroups.html_parser;

/**
 * This represents a block of text.
 * @see HTMLTokenizer
 * @author <a href="http://www.strath.ac.uk/~ras97108/">David McNicol</a>
 */
public class TextToken {

    /** The content of the token. */
    private StringBuffer text;

    /**
	 * Constructs a new token.
	 */
    public TextToken() {
        text = new StringBuffer();
    }

    /**
	 * Sets the content of the Token.
	 * @param newText the new content of the Token.
	 */
    public void setText(String newText) {
        text = new StringBuffer(newText);
    }

    /**
	 * Sets the content of the Token.
	 * @param newText the new content of the Token.
	 */
    public void setText(StringBuffer newText) {
        text = newText;
    }

    /**
	 * Appends some content to the token.
	 * @param more the new content to add.
	 */
    public void appendText(String more) {
        text.append(more);
    }

    /**
	 * Returns the contents of the token.
	 */
    public String getText() {
        return new String(text);
    }

    /** Returns a string version of the TextToken. */
    public String toString() {
        return text.toString();
    }
}
