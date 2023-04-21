package annas.graph.migrate.dot.parser;

import java.util.Arrays;

public class TokenReader {

    /** Input stream. */
    private final char[] stream;

    /** position in input stream. */
    private int position = 0;

    public TokenReader(String stream) {
        this.stream = stream.toCharArray();
    }

    /**
	 * Consume a semi-colon (or new line character) from the stream.
	 * 
	 */
    public void consumeSemiColon() {
        this.consumeWhitespace();
        while (this.position < this.stream.length && (this.stream[this.position] == ';' || this.stream[this.position] == '\n')) {
            this.position++;
        }
    }

    /**
	 * Consume and return a single String from the input stream.
	 * 
	 * @return a trimmed string
	 */
    public String consumeToken() {
        this.consumeWhitespace();
        int start = this.position;
        while (this.position < this.stream.length && this.stream[this.position] != ';' && !Character.isWhitespace(this.stream[this.position])) {
            this.position++;
        }
        return String.valueOf(Arrays.copyOfRange(this.stream, start, this.position));
    }

    /**
	 * Consume (and ignore) any whitespace at the current position in the
	 * stream.
	 */
    private void consumeWhitespace() {
        while (this.position < this.stream.length && this.stream[this.position] != ';' && Character.isWhitespace(this.stream[this.position])) {
            this.position++;
        }
    }

    /**
	 * Get current position in the stream
	 * 
	 * @return current index in stream.
	 */
    public int getCharNo() {
        return this.position;
    }

    /**
	 * 
	 * 
	 * @return true if the reader is at the end of an expression.
	 */
    public boolean hasEndStmt() {
        this.consumeWhitespace();
        return !this.hasMoreTokens() || this.stream[this.position] == ';' || this.stream[this.position] == '\n';
    }

    /**
	 * @return true if there reader has completed the input.
	 */
    public boolean hasMoreTokens() {
        this.consumeWhitespace();
        return this.position < this.stream.length - 1;
    }

    /**
	 * Return the next token (without consuming it)
	 * 
	 * @return next token in the stream.
	 */
    public String nextToken() {
        this.consumeWhitespace();
        int start = this.position;
        int end = start;
        while (end < this.stream.length && this.stream[end] != ';' && !Character.isWhitespace(this.stream[end])) {
            end++;
        }
        return String.valueOf(Arrays.copyOfRange(this.stream, start, end));
    }

    /**
	 * 
	 * 
	 * @return true if the next token is a semicolon
	 */
    public boolean nextTokenIsSemiColon() {
        this.consumeWhitespace();
        int start = this.position;
        int end = start;
        while (end < this.stream.length && !Character.isWhitespace(this.stream[end])) {
            end++;
        }
        return String.valueOf(Arrays.copyOfRange(this.stream, start, end)).equals(";");
    }

    @Override
    public String toString() {
        return new String(this.stream);
    }
}
