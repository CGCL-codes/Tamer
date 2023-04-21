package android.text.method;

import android.view.KeyEvent;
import android.view.View;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

/**
 * For numeric text entry
 */
public abstract class NumberKeyListener extends BaseKeyListener implements InputFilter {

    /**
     * You can say which characters you can accept.
     */
    protected abstract char[] getAcceptedChars();

    protected int lookup(KeyEvent event, Spannable content) {
        return event.getMatch(getAcceptedChars(), getMetaState(content));
    }

    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        char[] accept = getAcceptedChars();
        boolean filter = false;
        int i;
        for (i = start; i < end; i++) {
            if (!ok(accept, source.charAt(i))) {
                break;
            }
        }
        if (i == end) {
            return null;
        }
        if (end - start == 1) {
            return "";
        }
        SpannableStringBuilder filtered = new SpannableStringBuilder(source, start, end);
        i -= start;
        end -= start;
        int len = end - start;
        for (int j = end - 1; j >= i; j--) {
            if (!ok(accept, source.charAt(j))) {
                filtered.delete(j, j + 1);
            }
        }
        return filtered;
    }

    protected static boolean ok(char[] accept, char c) {
        for (int i = accept.length - 1; i >= 0; i--) {
            if (accept[i] == c) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(View view, Editable content, int keyCode, KeyEvent event) {
        int selStart, selEnd;
        {
            int a = Selection.getSelectionStart(content);
            int b = Selection.getSelectionEnd(content);
            selStart = Math.min(a, b);
            selEnd = Math.max(a, b);
        }
        if (selStart < 0 || selEnd < 0) {
            selStart = selEnd = 0;
            Selection.setSelection(content, 0);
        }
        int i = event != null ? lookup(event, content) : 0;
        int repeatCount = event != null ? event.getRepeatCount() : 0;
        if (repeatCount == 0) {
            if (i != 0) {
                if (selStart != selEnd) {
                    Selection.setSelection(content, selEnd);
                }
                content.replace(selStart, selEnd, String.valueOf((char) i));
                adjustMetaAfterKeypress(content);
                return true;
            }
        } else if (i == '0' && repeatCount == 1) {
            if (selStart == selEnd && selEnd > 0 && content.charAt(selStart - 1) == '0') {
                content.replace(selStart - 1, selEnd, String.valueOf('+'));
                adjustMetaAfterKeypress(content);
                return true;
            }
        }
        adjustMetaAfterKeypress(content);
        return super.onKeyDown(view, content, keyCode, event);
    }
}
