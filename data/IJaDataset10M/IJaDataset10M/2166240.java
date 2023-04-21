package net.sourceforge.freejava.regex;

public class QuotedStringProcessor extends PatternProcessor {

    protected final QuoteFormat quoteFormat;

    public QuotedStringProcessor(QuoteFormat quoteFormat) {
        super(quoteFormat.quotePattern);
        this.quoteFormat = quoteFormat;
    }

    public QuotedStringProcessor(char quoteChar) {
        this(new QuoteFormat(quoteChar));
    }

    public QuotedStringProcessor(char... quoteChars) {
        this(new QuoteFormat(quoteChars));
    }

    @Override
    protected void matched(String part) {
        String dequoted = processQuotedText(part);
        print(dequoted);
    }

    @Override
    protected void unmatched(String part) {
        String s = processNormalText(part);
        print(s);
    }

    protected String processNormalText(String s) {
        return s;
    }

    protected String processQuotedText(String quotedText) {
        String body = quotedText.substring(quoteFormat.quoteOpenLen, quotedText.length() - quoteFormat.quoteCloseLen);
        return Unescape.unescape(body);
    }
}
