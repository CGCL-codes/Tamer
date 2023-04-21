package moxie;

import org.hamcrest.Description;
import org.hamcrest.SelfDescribing;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;

class SimpleDescription implements Description {

    private final PrintWriter printWriter;

    private final StringWriter stringWriter;

    public SimpleDescription() {
        stringWriter = new StringWriter();
        printWriter = new PrintWriter(stringWriter);
    }

    public Description appendText(String s) {
        printWriter.print(s);
        return this;
    }

    public Description appendDescriptionOf(SelfDescribing selfDescribing) {
        selfDescribing.describeTo(this);
        return this;
    }

    public Description appendValue(Object o) {
        String value;
        try {
            value = String.valueOf(o);
        } catch (Throwable e) {
            value = String.format("%s@%x", o.getClass().getName(), System.identityHashCode(o));
        }
        printWriter.print(value);
        return this;
    }

    public <T> Description appendValueList(String start, String separator, String end, T... values) {
        return appendValueList(start, separator, end, values != null ? Arrays.asList(values) : Collections.EMPTY_LIST);
    }

    public <T> Description appendValueList(String start, String separator, String end, Iterable<T> values) {
        printWriter.print(start);
        boolean first = true;
        for (T value : values) {
            if (!first) {
                printWriter.print(separator);
            }
            first = false;
            appendValue(value);
        }
        printWriter.print(end);
        return this;
    }

    public Description appendList(String start, String separator, String end, Iterable<? extends SelfDescribing> values) {
        printWriter.print(start);
        boolean first = true;
        for (SelfDescribing value : values) {
            if (!first) {
                printWriter.print(separator);
            }
            first = false;
            value.describeTo(this);
        }
        printWriter.print(end);
        return this;
    }

    @Override
    public String toString() {
        return stringWriter.toString();
    }
}
