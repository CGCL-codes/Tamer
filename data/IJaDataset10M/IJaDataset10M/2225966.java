package de.ufinke.cubaja.csv;

import java.io.IOException;
import java.io.Writer;

/**
 * Default row formatter.
 * <p>
 * Separates columns by the separator character as provided by configuration.
 * If there is an escape character defined with the configuration,
 * the content of all columns is delimited by this escape character;
 * an escape character within the content is doubled.
 * Separates rows (lines) with the rowSeparator as provided by configuration.
 * @author Uwe Finke
 */
public class DefaultRowFormatter implements RowFormatter {

    private Writer writer;

    private boolean firstColumn;

    private String lineSeparator;

    private char separator;

    private char escapeChar;

    private boolean hasEscape;

    /**
   * Constructor.
   */
    public DefaultRowFormatter() {
        firstColumn = true;
    }

    public void init(Writer out, CsvConfig config) {
        writer = out;
        lineSeparator = config.getRowSeparator();
        separator = config.getSeparator();
        hasEscape = (config.getEscapeChar() != null);
        if (hasEscape) {
            escapeChar = config.getEscapeChar();
        }
    }

    public void writeRow() throws IOException {
        writer.write(lineSeparator);
        firstColumn = true;
    }

    public void writeColumn(String content) throws IOException {
        if (content == null) {
            content = "";
        }
        if (firstColumn) {
            firstColumn = false;
        } else {
            writer.write(separator);
        }
        if (hasEscape) {
            writeEscaped(content);
        } else {
            writer.write(content);
        }
    }

    private void writeEscaped(String input) throws IOException {
        writer.write(escapeChar);
        if (input.indexOf(escapeChar) < 0) {
            writer.write(input);
        } else {
            for (int i = 0; i < input.length(); i++) {
                char c = input.charAt(i);
                if (c == escapeChar) {
                    writer.write(escapeChar);
                }
                writer.write(c);
            }
        }
        writer.write(escapeChar);
    }

    public void finish() {
    }
}
