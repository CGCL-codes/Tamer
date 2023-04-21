package ghm.followgui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Action which closes the currently followed file.
 * @author <a href="mailto:greghmerrill@yahoo.com">Greg Merrill</a>
 * @author <a href="mailto:murali_ca_us@hotmail.com">Murali Krishnan</a>
 */
class ExternalEditor extends Object {

    ExternalEditor(final String string) {
        _cmdString = string;
    }

    private String _cmdString = "";

    protected String getCmdString() {
        return (_cmdString);
    }

    protected String[] toCmdArray(final String file) {
        String string = (getCmdString() == null) ? "" : getCmdString().trim();
        String[] result = new String[0];
        if (!string.equals("")) {
            string = string + " ";
            final List list = new ArrayList();
            boolean inQuoteSingle = false;
            boolean inQuoteDouble = false;
            boolean inWhitespace = false;
            StringBuffer buffer = new StringBuffer();
            final char[] chArray = string.toCharArray();
            for (int i = 0; i < chArray.length; i++) {
                final char ch = chArray[i];
                if (inQuoteSingle) {
                    if (ch == '\'') {
                        inQuoteSingle = false;
                    } else {
                        buffer.append(ch);
                    }
                } else if (inQuoteDouble) {
                    if (ch == '"') {
                        inQuoteDouble = false;
                    } else {
                        buffer.append(ch);
                    }
                } else if (inWhitespace) {
                    if (!Character.isWhitespace(ch)) {
                        inWhitespace = false;
                        --i;
                    }
                } else {
                    if (ch == '\'') {
                        inQuoteSingle = true;
                    } else if (ch == '"') {
                        inQuoteDouble = true;
                    } else if (Character.isWhitespace(ch)) {
                        inWhitespace = true;
                        list.add(buffer.toString());
                        buffer = new StringBuffer();
                    } else {
                        buffer.append(ch);
                    }
                }
            }
            list.add(file);
            result = (String[]) list.toArray(result);
        }
        return (result);
    }

    public void exec(final File file) {
        final String fullPath = file.getAbsolutePath();
        final String[] cmd = toCmdArray(fullPath);
        System.out.println("Exec'ing " + Arrays.asList(cmd) + ".");
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (final IOException ioe) {
            final String errmsg = "Could not exec [" + getCmdString() + "] with [" + fullPath + "].";
            System.err.println(errmsg);
            ioe.printStackTrace();
        }
    }
}
