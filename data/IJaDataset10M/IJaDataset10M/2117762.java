package org.codehaus.jam.annotation;

import com.sun.javadoc.Tag;
import org.codehaus.jam.mutable.MAnnotatedElement;
import org.codehaus.jam.mutable.MAnnotation;
import java.io.StringWriter;
import java.util.StringTokenizer;

/**
 * <p>Attempts to parse tag contents as a series of line-delimited name-value
 * pairs.</p>
 *
 * @author Patrick Calahan &lt;email: pcal-at-bea-dot-com&gt;
 */
public class LineDelimitedTagParser extends JavadocTagParser {

    private static final String VALUE_QUOTE = "\"";

    private static final String LINE_DELIMS = "\n\f\r";

    public void parse(MAnnotatedElement target, Tag tag) {
        if (target == null) throw new IllegalArgumentException("null tagText");
        if (tag == null) throw new IllegalArgumentException("null tagName");
        MAnnotation[] anns = createAnnotations(target, tag);
        String tagText = tag.text();
        StringTokenizer st = new StringTokenizer(tagText, LINE_DELIMS);
        while (st.hasMoreTokens()) {
            String pair = st.nextToken();
            int eq = pair.indexOf('=');
            if (eq <= 0) continue;
            String name = pair.substring(0, eq).trim();
            if (eq < pair.length() - 1) {
                String value = pair.substring(eq + 1).trim();
                if (value.startsWith(VALUE_QUOTE)) {
                    value = parseQuotedValue(value.substring(1), st);
                }
                setValue(anns, name, value);
            }
        }
    }

    private String parseQuotedValue(String line, StringTokenizer st) {
        StringWriter out = new StringWriter();
        while (true) {
            int endQuote = line.indexOf(VALUE_QUOTE);
            if (endQuote == -1) {
                out.write(line);
                if (!st.hasMoreTokens()) return out.toString();
                out.write('\n');
                line = st.nextToken().trim();
                continue;
            } else {
                out.write(line.substring(0, endQuote).trim());
                return out.toString();
            }
        }
    }
}
