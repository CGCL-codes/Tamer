package net.sf.css4j;

import java.io.IOException;
import org.dom4j.Element;

public class AttributeSelector {

    static final int EXISTS = 0;

    static final int MATCHES = '=';

    static final int CONTAINS = '~';

    static final int CONTAINS_LANG = '|';

    String name;

    String value;

    int operator = MATCHES;

    public AttributeSelector() {
    }

    public AttributeSelector(String name, int opertor, String value) {
        this.name = name;
        this.value = value;
        this.operator = operator;
    }

    public void parse(CssTokenizer st) throws IOException {
        st.nextToken();
        name = st.sval;
        switch(st.nextToken()) {
            case ']':
                operator = EXISTS;
                break;
            case '=':
                operator = MATCHES;
                break;
            case '~':
                operator = CONTAINS;
                st.nextToken();
                break;
            case '|':
                operator = CONTAINS_LANG;
                st.nextToken();
                break;
        }
        if (operator != EXISTS) {
            if (st.ttype != '=') {
                System.err.println("= expected while parsing attribute");
            }
            st.nextToken();
            value = st.sval;
            st.nextToken();
        }
        st.require(']');
        st.nextToken();
    }

    boolean match(Element element) {
        String v = element.attributeValue(name);
        if (v == null || v.length() == 0) return false;
        switch(operator) {
            case EXISTS:
                return true;
            case CONTAINS:
                return (" " + v + " ").indexOf(value) != -1;
            case CONTAINS_LANG:
                throw new RuntimeException("TBD");
            default:
                return v.equals(value);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        switch(operator) {
            case EXISTS:
                return sb.toString();
            case CONTAINS:
                sb.append('~');
                break;
            case CONTAINS_LANG:
                sb.append('|');
                break;
        }
        sb.append('=');
        sb.append(value);
        return sb.toString();
    }
}
