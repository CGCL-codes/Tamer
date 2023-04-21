package net.sf.saxon.regex;

import net.sf.saxon.om.SequenceIterator;
import net.sf.saxon.om.Item;
import net.sf.saxon.value.StringValue;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
* A JTokenIterator is an iterator over the strings that result from tokenizing a string using a regular expression
*/
public class JTokenIterator implements SequenceIterator {

    private CharSequence input;

    private Pattern pattern;

    private Matcher matcher;

    private CharSequence current;

    private int position = 0;

    private int prevEnd = 0;

    /**
    * Construct a JTokenIterator.
    */
    public JTokenIterator(CharSequence input, Pattern pattern) {
        this.input = input;
        this.pattern = pattern;
        matcher = pattern.matcher(input);
        prevEnd = 0;
    }

    public Item next() {
        if (prevEnd < 0) {
            current = null;
            position = -1;
            return null;
        }
        if (matcher.find()) {
            current = input.subSequence(prevEnd, matcher.start());
            prevEnd = matcher.end();
        } else {
            current = input.subSequence(prevEnd, input.length());
            prevEnd = -1;
        }
        position++;
        return StringValue.makeStringValue(current);
    }

    public Item current() {
        return (current == null ? null : StringValue.makeStringValue(current));
    }

    public int position() {
        return position;
    }

    public SequenceIterator getAnother() {
        return new JTokenIterator(input, pattern);
    }

    /**
     * Get properties of this iterator, as a bit-significant integer.
     *
     * @return the properties of this iterator. This will be some combination of
     *         properties such as {@link #GROUNDED}, {@link #LAST_POSITION_FINDER},
     *         and {@link #LOOKAHEAD}. It is always
     *         acceptable to return the value zero, indicating that there are no known special properties.
     *         It is acceptable for the properties of the iterator to change depending on its state.
     */
    public int getProperties() {
        return 0;
    }
}
