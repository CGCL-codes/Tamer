package org.in4j.search.query.parser;

import java.util.Iterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author 杨帆
 * @version 1.0 2007 六月 1 17:45:41,创建
 */
public class MyTokenIterator implements Iterator<MyToken> {

    @SuppressWarnings("unused")
    private static final Log log = LogFactory.getLog(MyTokenIterator.class);

    private final String input;

    private int pos;

    private final int count;

    public MyTokenIterator(String input) {
        this.input = input;
        pos = 0;
        count = input.length();
    }

    public boolean hasNext() {
        return pos < count;
    }

    public MyToken next() {
        final StringBuffer sb = new StringBuffer();
        for (; pos < count; pos++) {
            char ch = input.charAt(pos);
            if (isSpace(ch)) {
                pos++;
                break;
            }
            sb.append(ch);
        }
        MyToken token = new MyToken(sb.toString());
        return token;
    }

    private boolean isSpace(char ch) {
        return ch == ' ';
    }

    public void remove() {
        if (true) throw new UnsupportedOperationException();
    }
}
