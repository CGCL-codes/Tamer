package logahawk.formatters;

import java.util.*;

/**
 * This class helps format individual objects (the arguments) provided to the log methods into simple {@link String}
 * messages that can be written to the log.
 *
 * Most complex objects do not provide helpful {@link String} representations when the "ToString" method is called.
 * These classes are designed to understand and interpret such objects and return {@link String} messages which can be
 * easily outputted by the logger.
 *
 * Note that it is possible to make a {@link ArgumentFormatter} that can handle multiple types of related objects.
 */
public interface ArgumentFormatter {

    /** This should return true only if this can provide a useful formatting for this type of object. */
    boolean canFormat(Object obj);

    /**
	 * This converts the object into a {@link String} that represent the object. The {@link String} may contain line
	 * breaks, but should not include a trailing line break. The first line of the result should NOT include any
	 * indentation, that should be handled by the calling class.
	 *
	 * If the object is complex and contains other objects, this {@link ArgumentFormatter} may use the provided list of
	 * {@link ArgumentFormatter} objects to find a more appropriate formatter.
	 *
	 * To make things look nice, the "indentLevel" is provided. The indent level should always be incremented by one when
	 * calling downward into other {@link ArgumentFormatter}s.
	 */
    String format(Object obj, Collection<ArgumentFormatter> formatters, int indentLevel);
}
