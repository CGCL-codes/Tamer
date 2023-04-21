package org.dcm4chex.service;

import java.beans.PropertyEditorSupport;
import java.util.StringTokenizer;

/**
 * 
 * @author gunter.zeilinger@tiani.com
 * @version $Revision: 900 $ $Date: 2003-12-17 19:42:53 -0500 (Wed, 17 Dec 2003) $
 * @since 17.12.2003
 */
final class MillisecondArrayEditor extends PropertyEditorSupport {

    public String getAsText() {
        long[] a = (long[]) getValue();
        if (a == null || a.length == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < a.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(a[i]);
        }
        return sb.toString();
    }

    public void setAsText(String text) throws IllegalArgumentException {
        StringTokenizer stk = new StringTokenizer(text, ", \t\n\r");
        long[] a = new long[stk.countTokens()];
        for (int i = 0; i < a.length; i++) {
            a[i] = parseMilliseconds(stk.nextToken());
        }
        setValue(a);
    }

    private long parseMilliseconds(String text) {
        int len = text.length();
        long k = 1L;
        switch(text.charAt(len - 1)) {
            case 'd':
                k *= 24;
            case 'h':
                k *= 60;
            case 'm':
                k *= 60;
            case 's':
                k *= 1000;
                --len;
        }
        return k * Long.parseLong(text.substring(0, len));
    }
}
