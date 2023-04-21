package layout.parser;

import java.util.StringTokenizer;
import layout.ParseException;
import org.w3c.dom.Node;

public final class P_short extends Parser {

    @Override
    public Object parse(Node n) throws ParseException {
        return parse(toString(n.getChildNodes()));
    }

    public Object parseArray(Node n) throws ParseException {
        StringTokenizer tok = new StringTokenizer(toString(n.getChildNodes()), getDelimiter(n));
        int size = tok.countTokens();
        short[] list = new short[size];
        for (int i = 0; i < size; i++) {
            list[i] = parse(tok.nextToken());
        }
        return list;
    }

    public static short parse(String str) throws ParseException {
        try {
            return Short.parseShort(str.trim());
        } catch (NumberFormatException e) {
            throw new ParseException("Could not parse '" + str + "' into an short value");
        }
    }

    @Override
    public Class<?> getTargetClass() {
        return short.class;
    }

    public Class<?> getTargetArrayClass() {
        return short[].class;
    }
}
