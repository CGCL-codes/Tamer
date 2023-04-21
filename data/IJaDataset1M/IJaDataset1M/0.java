package logahawk.formatters;

import java.util.*;

/** Formats {@link UUID} instances; */
public class UuidArgFormatter implements ArgumentFormatter {

    public boolean canFormat(final Object obj) {
        return obj instanceof UUID;
    }

    public String format(final Object obj, final Collection<ArgumentFormatter> formatters, final int indentLevel) {
        return ((UUID) obj).toString();
    }
}
