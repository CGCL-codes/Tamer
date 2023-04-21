package javax.time.calendar.format;

/**
 * Pads the output to a fixed width.
 * <p>
 * PadPrinterParserDecorator is immutable and thread-safe.
 *
 * @author Stephen Colebourne
 */
final class PadPrinterParserDecorator implements DateTimePrinter, DateTimeParser {

    /**
     * The printer to decorate.
     */
    private final DateTimePrinter printer;

    /**
     * The parser to decorate.
     */
    private final DateTimeParser parser;

    /**
     * The width to pad the next field to.
     */
    private final int padWidth;

    /**
     * The character to pad the next field with.
     */
    private final char padChar;

    /**
     * Constructor.
     *
     * @param printer  the printer, may be null in which case print() must not be called
     * @param parser  the parser, may be null in which case parse() must not be called
     * @param padWidth  the width to pad to, 1 or greater
     * @param padChar  the pad character
     */
    PadPrinterParserDecorator(DateTimePrinter printer, DateTimeParser parser, int padWidth, char padChar) {
        this.printer = printer;
        this.parser = parser;
        this.padWidth = padWidth;
        this.padChar = padChar;
    }

    /** {@inheritDoc} */
    public void print(DateTimePrintContext context, StringBuilder buf) {
        int preLen = buf.length();
        printer.print(context, buf);
        int len = buf.length() - preLen;
        if (len > padWidth) {
            throw new CalendricalPrintException("Cannot print as output of " + len + " characters exceeds pad width of " + padWidth);
        }
        for (int i = 0; i < padWidth - len; i++) {
            buf.insert(preLen, padChar);
        }
    }

    /** {@inheritDoc} */
    public int parse(DateTimeParseContext context, String parseText, int position) {
        if (position > parseText.length()) {
            throw new IndexOutOfBoundsException();
        }
        int endPos = position + padWidth;
        if (endPos > parseText.length()) {
            return ~position;
        }
        int pos = position;
        while (pos < endPos && parseText.charAt(pos) == padChar) {
            pos++;
        }
        parseText = parseText.substring(0, endPos);
        int firstError = 0;
        while (pos >= position) {
            int resultPos = parser.parse(context, parseText, pos);
            if (resultPos < 0) {
                if (firstError == 0) {
                    firstError = resultPos;
                }
                pos--;
                continue;
            }
            if (resultPos != endPos) {
                return ~position;
            }
            return resultPos;
        }
        return firstError;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        String base = "Pad(";
        if (printer == parser) {
            base += printer;
        } else {
            base += (printer == null ? "" : printer) + "," + (parser == null ? "" : parser);
        }
        return base + "," + padWidth + (padChar == ' ' ? ")" : ",'" + padChar + "')");
    }
}
