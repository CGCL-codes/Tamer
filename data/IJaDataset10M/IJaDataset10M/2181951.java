package org.netbeans.cubeon.analyzer.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Anuradha G
 */
public abstract class StackTraceProvider {

    public final List<StackTrace> analyze() {
        List<StackTrace> traces = new ArrayList<StackTrace>();
        for (String text : getAnalyzableTexts()) {
            if (text == null) {
                continue;
            }
            List<StackTrace.Line> lines = new ArrayList<StackTrace.Line>();
            BufferedReader stackTrace = new BufferedReader(new StringReader(text));
            String line = null;
            try {
                while ((line = stackTrace.readLine()) != null) {
                    Matcher m = STACK_TRACE.matcher(line);
                    if (m.matches()) {
                        m = STACK_TRACE.matcher(line);
                        if (m.matches()) {
                            String pkg = m.group(3);
                            String filename = m.group(4);
                            String resource = pkg.replace('.', '/') + filename;
                            int lineNumber = Integer.parseInt(m.group(5));
                            lines.add(new StackTrace.Line(line.trim(), resource, lineNumber));
                        }
                    }
                    m = EXCEPTION_MESSAGE.matcher(line);
                    if (m.matches()) {
                        lines.add(new StackTrace.Line(line));
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(StackTraceProvider.class.getName()).warning(ex.getMessage());
            }
            if (!lines.isEmpty()) {
                traces.add(new StackTrace(lines));
            }
        }
        return traces;
    }

    /**
     *  Get String that may contain Stacktrace to Analyze
     * @return
     */
    public abstract List<String> getAnalyzableTexts();

    /**
     * Regexp matching one line (not the first) of a stack trace.
     * Captured groups:
     * <ol>
     * <li>package
     * <li>filename
     * <li>line number
     * </ol>
     */
    static final Pattern STACK_TRACE = Pattern.compile("(\\s)+(|catch )at ((?:[a-zA-Z_$][a-zA-Z0-9_$]*\\.)*)[a-zA-Z_$][a-zA-Z0-9_$]*\\.[a-zA-Z_$<][a-zA-Z0-9_$>]*\\(([a-zA-Z_$][a-zA-Z0-9_$]*\\.java):([0-9]+)\\)");

    /**
     * Regexp matching the first line of a stack trace, with the exception message.
     * Captured groups:
     * <ol>
     * <li>unqualified name of exception class plus possible message
     * </ol>
     */
    static final Pattern EXCEPTION_MESSAGE = Pattern.compile("(?:Exception in thread \"(?:main|Main Thread)\" )?(?:(?:[a-zA-Z_$][a-zA-Z0-9_$]*\\.)+)([a-zA-Z_$][a-zA-Z0-9_$]*(?:: .+)?)");
}
