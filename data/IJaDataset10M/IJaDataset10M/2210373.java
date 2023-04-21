package logahawk.listeners;

import net.jcip.annotations.*;
import logahawk.formatters.*;

/**
 * A helper {@link Listener} that can write to a {@link StringBuilder}.
 *
 * This class does not add anything over {@link AppendableListener}, but exists because more people are familiar with
 * the {@link StringBuilder} interface than the new {@link Appendable} interface.
 */
@ThreadSafe
public class StringBuilderListener extends AppendableListener {

    public StringBuilderListener() {
        this(new StringBuilder(256), new StandardMessageFormatter(true));
    }

    public StringBuilderListener(StringBuilder builder) {
        this(builder, new StandardMessageFormatter(true));
    }

    public StringBuilderListener(MessageFormatter formatter) {
        this(new StringBuilder(256), formatter);
    }

    public StringBuilderListener(StringBuilder builder, MessageFormatter formatter) {
        super(builder, formatter);
    }

    public StringBuilder getStringBuilder() {
        return (StringBuilder) this.appendable;
    }
}
