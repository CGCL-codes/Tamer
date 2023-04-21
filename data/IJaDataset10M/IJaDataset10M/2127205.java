package astyleplugin;

import astyle.ASSourceIterator;
import beauty.beautifiers.Beautifier;
import beauty.parsers.ParserException;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.GUIUtilities;
import org.gjt.sp.jedit.options.BeanHelper;
import org.gjt.sp.util.Log;

/**
 * danson, Added this class so AStyle can work with Beauty.
 */
public class AStyleBeautifier extends Beautifier {

    private static BeanHelper beanHelper = null;

    private static Formatter formatter = null;

    private String toFormat = null;

    public String beautify(String text) throws ParserException {
        toFormat = text;
        String ls = getLineSeparator();
        initFormatter();
        StringBuffer result = new StringBuffer(text.length() + 1000);
        while (formatter.hasMoreLines()) {
            result.append(formatter.nextLine());
            if (formatter.hasMoreLines()) result.append(ls);
        }
        return result.toString();
    }

    private void initFormatter() {
        if (beanHelper == null) {
            beanHelper = new BeanHelper("astyleplugin", "astyleplugin.Formatter", this.getClass().getClassLoader());
            formatter = (Formatter) beanHelper.createBean();
        } else {
            beanHelper.initBean(formatter);
        }
        String mode = getEditMode();
        int tabSize = getTabWidth();
        int indentSize = getIndentWidth();
        boolean noTabs = getUseSoftTabs();
        boolean assumeCStyle = mode.equals("c") || mode.equals("c++") || mode.equals("cplusplus");
        if (assumeCStyle) {
            Log.log(Log.DEBUG, this, "assuming C/C++ style, because mode name is 'c', 'c++' or 'cplusplus'");
            formatter.setCStyle(true);
        } else {
            Log.log(Log.DEBUG, this, "assuming Java style, because mode name is not 'c', 'c++' or 'cplusplus'");
            formatter.setCStyle(false);
        }
        formatter.setTabIndentation(tabSize);
        formatter.setSpaceIndentation(indentSize);
        formatter.setUseTabs(!noTabs);
        formatter.setTabSpaceConversionMode(noTabs);
        formatter.init(new StringIterator());
    }

    class StringIterator implements ASSourceIterator {

        String[] lines = null;

        int index = 0;

        public StringIterator() {
            lines = toFormat.split("([\\r])|(\\n)|(\\r\\n)");
        }

        public boolean hasMoreLines() {
            return index < lines.length;
        }

        public String nextLine() {
            String line = "";
            if (hasMoreLines()) {
                line = lines[index];
                ++index;
            }
            return line;
        }
    }
}
