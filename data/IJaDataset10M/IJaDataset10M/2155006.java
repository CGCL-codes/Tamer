package com.google.clearsilver.jsilver.functions.escape;

import com.google.clearsilver.jsilver.functions.TextFilter;
import java.io.IOException;

/**
 * This function will be used to sanitize variables introduced into javascript that are not string
 * literals. e.g. <script> var x = <?cs var: x ?> </script>
 * 
 * Currently it only accepts boolean and numeric literals. All other values are replaced with a
 * 'null'. This behavior may be extended if required at a later time. This replicates the
 * autoescaping behavior of Clearsilver.
 */
public class JsValidateUnquotedLiteral implements TextFilter {

    public void filter(String in, Appendable out) throws IOException {
        if (in.equals("true") || in.equals("false")) {
            out.append(in);
            return;
        }
        boolean valid = true;
        if (in.startsWith("0x") || in.startsWith("0X")) {
            for (int i = 2; i < in.length(); i++) {
                char c = in.charAt(i);
                if (!((c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F') || (c >= '0' && c <= '9'))) {
                    valid = false;
                    break;
                }
            }
        } else {
            for (int i = 0; i < in.length(); i++) {
                char c = in.charAt(i);
                if (!((c >= '0' && c <= '9') || c == '+' || c == '-' || c == '.' || c == 'e' || c == 'E')) {
                    valid = false;
                    break;
                }
            }
        }
        if (valid) {
            out.append(in);
        } else {
            out.append("null");
        }
    }
}
