package net.innig.sweetxml;

import java.io.IOException;

public abstract class AutoConverter extends Converter {

    @Override
    protected String convert() throws IOException {
        ConverterInput in = getInput();
        StringBuilder result = new StringBuilder();
        while (true) {
            int c = in.read();
            if (c == -1) return result.toString();
            if (c == '<') {
                in.unread();
                return convert(getXmlConverter(), in, result);
            }
            if (c == '#' || Patterns.charMatches(c, Patterns.xmlNameStartChar)) {
                in.unread();
                return convert(getSweetConverter(), in, result);
            }
            if (!Character.isWhitespace(c)) throw new SweetXmlParseException(in.getPosition(), SweetXmlMessage.CONVERSION_TYPE_UNKNOWN, SweetXmlMessage.formatChar(c));
        }
    }

    private String convert(Converter xmlConverter, ConverterInput in, StringBuilder result) throws IOException {
        if (xmlConverter == null) {
            int c;
            while ((c = in.read()) != -1) result.append((char) c);
        } else {
            xmlConverter.setInput(in);
            result.append(xmlConverter.getResult());
        }
        return result.toString();
    }

    protected abstract Converter getXmlConverter();

    protected abstract Converter getSweetConverter();
}
