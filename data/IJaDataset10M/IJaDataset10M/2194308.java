package org.objectstyle.wolips.datasets.pattern;

import java.util.Iterator;

/**
 * @author Harald Niesche
 * @version 1.0 QuotedStringTokenizer -- tokenize string ignoring quoted
 *          separators
 */
public class QuotedStringTokenizer implements Iterator {

    /**
	 * construct a string tokenizer for String <i>string </i>, using ' ' (blank)
	 * as separator
	 * 
	 * @param string
	 */
    public QuotedStringTokenizer(String string) {
        this._s = string;
        this._sep = ' ';
        this._nextStart = 0;
    }

    /**
	 * construct a string tokenizer for String <i>string </i>, using sep as the
	 * separator
	 * 
	 * @param string
	 * @param sep
	 */
    public QuotedStringTokenizer(String string, int sep) {
        this._s = string;
        this._sep = sep;
        this._nextStart = 0;
    }

    /**
	 * checks whether more tokens are available
	 */
    public boolean hasNext() {
        return (this._s.length() >= this._nextStart);
    }

    /**
	 * @return
	 */
    public boolean hasMoreTokens() {
        return (hasNext());
    }

    /**
	 * Iterator interface: obtains the next token as Object
	 */
    public Object next() {
        return nextToken(this._sep);
    }

    /**
	 * Iterator interface: remove current token (skips it, actually)
	 */
    public void remove() {
        nextToken(this._sep);
    }

    /**
	 * obtain current token as String
	 * @return
	 */
    public String nextToken() {
        return (nextToken(this._sep));
    }

    /**
	 * obtain current token as String, using sep as separator
	 * @param sep
	 * @return
	 */
    public String nextToken(int sep) {
        if (this._s.length() < this._nextStart) {
            return (null);
        }
        if (this._s.length() == this._nextStart) {
            ++this._nextStart;
            return ("");
        }
        StringBuffer buffer = new StringBuffer();
        int end;
        int tmp;
        int restStart = this._nextStart;
        if ('"' == this._s.charAt(this._nextStart)) {
            ++this._nextStart;
            restStart = this._nextStart;
            tmp = this._s.indexOf('"', this._nextStart);
            while ((tmp != -1) && (tmp + 1 < this._s.length()) && (this._s.charAt(tmp + 1) == '"')) {
                buffer.append(this._s.substring(restStart, tmp + 1));
                restStart = tmp + 2;
                tmp = this._s.indexOf('"', tmp + 2);
            }
            if (tmp == -1) {
                tmp = this._s.length();
            }
        } else {
            tmp = this._nextStart;
        }
        int end1 = this._s.indexOf(sep, tmp);
        int end2 = this._s.length();
        if (-1 == end1) {
            end1 = this._s.length();
        }
        if (end1 < end2) {
            end = end1;
        } else {
            end = end2;
        }
        if (restStart < end) {
            if (this._s.charAt(end - 1) == '"') {
                buffer.append(this._s.substring(restStart, end - 1));
            } else {
                buffer.append(this._s.substring(restStart, end));
            }
        }
        this._nextStart = end + 1;
        String result = buffer.toString();
        if (result == null) {
            result = "";
        }
        return (result);
    }

    private String _s;

    private int _sep;

    private int _nextStart;
}
