package console;

import java.awt.Color;
import org.gjt.sp.jedit.View;
import errorlist.DefaultErrorSource;
import errorlist.DefaultErrorSource.DefaultError;
import errorlist.ErrorSource;

public class CommandOutputParser {

    DirectoryStack directoryStack = new DirectoryStack();

    Output output;

    protected DefaultError lastError = null;

    View view;

    DefaultErrorSource errorSource;

    ErrorListModel errorMatchers = ErrorListModel.load();

    ErrorMatcher lastMatcher;

    protected Console console;

    Color defaultColor;

    Color color;

    /**
	 * Creates an instance of an output parser.
	 * An output parser will send coloured output to the Shell of the
	 * given View.
	 *
	 * @param v - the current View
	 * @param es - An ErrorSource which corresponds to the plugin which is generating the errors.
	 * @param defaultColor - the default color to use when errors are not found
	 * 
	 * TODO: Use the es to determine which errormatchers to look at?
	 */
    public CommandOutputParser(View v, DefaultErrorSource es, Color defaultColor) {
        console = ConsolePlugin.getConsole(v);
        output = console.getOutput();
        this.defaultColor = defaultColor;
        this.color = defaultColor;
        lastMatcher = null;
        view = v;
        errorSource = es;
    }

    /** 
	 * Processes a line without displaying it to the Output
	 */
    public final int processLine(String text) {
        return processLine(text, false);
    }

    /**
	 * Process a line of input. Checks all the enabled ErrorMatchers'
	 *  regular expressions, sets the  proper current color,
	 *  changes directories if there are chdir patterns found.
	 * Adds errors to the ErrorList plugin if necessary.
	 *
	 * @param text a line of text
	 * @param disp if true, will also send to the Output.
	 * @return -1 if there is no error, or ErrorSource.WARNING,
	 *       or ErrorSource.ERROR if there is a warning or an error found in text.
	 */
    public int processLine(String text, boolean disp) {
        int retval = -1;
        if (text == null) return -1;
        if (directoryStack.processLine(text)) {
            if (disp) display(color, text);
            return ErrorSource.WARNING;
        }
        String directory = directoryStack.current();
        if (lastError != null) {
            String message = null;
            if (lastMatcher != null && lastMatcher.match(view, text, directory, errorSource) == null) message = lastMatcher.matchExtra(text);
            if (message != null) {
                lastError.addExtraMessage(message);
                return lastError.getErrorType();
            } else {
                if (errorSource.getErrorCount() == 0) ErrorSource.registerErrorSource(errorSource);
                errorSource.addError(lastError);
                lastMatcher = null;
                lastError = null;
            }
        }
        color = defaultColor;
        for (ErrorMatcher m : errorMatchers.m_matchers) {
            DefaultError error = m.match(view, text, directory, errorSource);
            if (error != null) {
                lastError = error;
                lastMatcher = m;
                int type = lastError.getErrorType();
                if (type == ErrorSource.ERROR) {
                    color = console.getErrorColor();
                } else if (type == ErrorSource.WARNING) {
                    color = console.getWarningColor();
                }
                break;
            }
        }
        if (disp) display(text);
        return retval;
    }

    public Color getColor() {
        return color;
    }

    public void setDirectory(String currentDirectory) {
        directoryStack.push(currentDirectory);
    }

    protected void display(Color c, String text) {
        if (text == null) return;
        output.writeAttrs(ConsolePane.colorAttributes(c), text + "\n");
    }

    protected void display(String text) {
        if (text == null) return;
        output.writeAttrs(ConsolePane.colorAttributes(color), text + "\n");
    }

    public void finishErrorParsing() {
        if (lastError != null) {
            errorSource.addError(lastError);
            lastError = null;
            lastMatcher = null;
        }
    }
}
