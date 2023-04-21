package de.carne.fs.core.transfer;

import java.util.LinkedList;

/**
 * Utility class used to build a single or multiple <code>TextData</code> lines.
 */
public class TextDataBuffer {

    private LinkedList<TextData> lineBuffer = new LinkedList<TextData>();

    private final LinkedList<LinkedList<TextData>> linesBuffer = new LinkedList<LinkedList<TextData>>();

    /**
	 * Construct <code>TextDataBuffer</code>.
	 */
    public TextDataBuffer() {
        this.linesBuffer.add(this.lineBuffer);
    }

    /**
	 * Append a text of a specific type.
	 * 
	 * @param text The text to append.
	 * @param type The type of the text.
	 * @return The updated buffer.
	 */
    public TextDataBuffer append(String text, TextDataType type) {
        assert text != null;
        assert type != null;
        final int lineBufferSize = this.lineBuffer.size();
        if (lineBufferSize > 0) {
            final TextData last = this.lineBuffer.getLast();
            if (last.type().equals(type)) {
                this.lineBuffer.set(lineBufferSize - 1, new TextData(last.text() + text, last.type()));
            } else {
                this.lineBuffer.add(new TextData(text, type));
            }
        } else {
            this.lineBuffer.add(new TextData(text, type));
        }
        return this;
    }

    /**
	 * Append a <code>TextData</code> element.
	 * 
	 * @param element The <code>TextData</code> element to append.
	 * @return The updated buffer.
	 */
    public TextDataBuffer append(TextData element) {
        assert element != null;
        final int lineBufferSize = this.lineBuffer.size();
        if (lineBufferSize > 0) {
            final TextData last = this.lineBuffer.getLast();
            if (last.type().equals(element.type())) {
                this.lineBuffer.set(lineBufferSize - 1, new TextData(last.text() + element.text(), last.type()));
            } else {
                this.lineBuffer.add(element);
            }
        } else {
            this.lineBuffer.add(element);
        }
        return this;
    }

    /**
	 * Append a TextDataBuffer.
	 * 
	 * @param buffer The buffer to append.
	 * @return The updated buffer.
	 */
    public TextDataBuffer append(TextDataBuffer buffer) {
        for (final LinkedList<TextData> line : buffer.linesBuffer) {
            this.lineBuffer.addAll(line);
            if (line != buffer.lineBuffer) {
                appendLine();
            }
        }
        return this;
    }

    /**
	 * Append a value text.
	 * 
	 * @param value The value text to append.
	 * @return The updated buffer.
	 */
    public TextDataBuffer appendValue(String value) {
        assert value != null;
        return append(new TextData(value, TextDataType.VALUE));
    }

    /**
	 * Append a symbol text.
	 * 
	 * @param symbol The symbol text to append.
	 * @return The updated buffer.
	 */
    public TextDataBuffer appendSymbol(String symbol) {
        assert symbol != null;
        return append(new TextData(symbol, TextDataType.SYMBOL));
    }

    /**
	 * Append a keyword text.
	 * 
	 * @param keyword The value keyword to append.
	 * @return The updated buffer.
	 */
    public TextDataBuffer appendKeyword(String keyword) {
        assert keyword != null;
        return append(new TextData(keyword, TextDataType.KEYWORD));
    }

    /**
	 * Append a operator text.
	 * 
	 * @param operator The operator text to append.
	 * @return The updated buffer.
	 */
    public TextDataBuffer appendOperator(String operator) {
        assert operator != null;
        return append(new TextData(operator, TextDataType.OPERATOR));
    }

    /**
	 * Append a label text.
	 * 
	 * @param label The label text to append.
	 * @return The updated buffer.
	 */
    public TextDataBuffer appendLabel(String label) {
        assert label != null;
        return append(new TextData(label, TextDataType.LABEL));
    }

    /**
	 * Append a comment text.
	 * 
	 * @param comment The comment text to append.
	 * @return The updated buffer.
	 */
    public TextDataBuffer appendComment(String comment) {
        assert comment != null;
        return append(new TextData(comment, TextDataType.COMMENT));
    }

    /**
	 * Append a error text.
	 * 
	 * @param error The error text to append.
	 * @return The updated buffer.
	 */
    public TextDataBuffer appendError(String error) {
        assert error != null;
        return append(new TextData(error, TextDataType.ERROR));
    }

    /**
	 * Append a new line of TextData elements.
	 * 
	 * @return The updated buffer.
	 */
    public TextDataBuffer appendLine() {
        this.lineBuffer = new LinkedList<TextData>();
        this.linesBuffer.add(this.lineBuffer);
        return this;
    }

    /**
	 * Get the number of lines already in the buffer.
	 * 
	 * @return The number of lines already in the buffer.
	 */
    public int linesCount() {
        return (this.lineBuffer.size() > 0 ? this.linesBuffer.size() : this.linesBuffer.size() - 1);
    }

    /**
	 * Get the number of <code>TextData</code> elements already in the current line.
	 * 
	 * @return The number of <code>TextData</code> elements already in the current line.
	 */
    public int lineDataCount() {
        return this.lineBuffer.size();
    }

    /**
	 * Get the current TextData line.
	 * 
	 * @return The current TextData line.
	 */
    public TextData[] toLine() {
        return this.lineBuffer.toArray(new TextData[this.lineBuffer.size()]);
    }

    /**
	 * Get the current TextData lines.
	 * 
	 * @return The current TextData lines.
	 */
    public TextData[][] toLines() {
        TextData[][] lines;
        int lineIndex = 0;
        if (this.linesBuffer.size() == 1 || this.lineBuffer.size() > 0) {
            lines = new TextData[this.linesBuffer.size()][];
            for (final LinkedList<TextData> buffer : this.linesBuffer) {
                lines[lineIndex++] = buffer.toArray(new TextData[buffer.size()]);
            }
        } else {
            lines = new TextData[this.linesBuffer.size() - 1][];
            for (final LinkedList<TextData> buffer : this.linesBuffer) {
                if (buffer != this.lineBuffer) {
                    lines[lineIndex++] = buffer.toArray(new TextData[buffer.size()]);
                }
            }
        }
        return lines;
    }

    /**
	 * Clear the buffer and discard all previously appended TextData elements.
	 */
    public void clear() {
        this.linesBuffer.clear();
        this.lineBuffer.clear();
        this.linesBuffer.add(this.lineBuffer);
    }
}
