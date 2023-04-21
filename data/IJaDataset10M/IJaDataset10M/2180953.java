package todopad.ui.handler;

import java.util.*;
import javax.swing.text.*;

public class TabHandler {

    public TabHandler(JTextComponent aTextComponent, boolean isShiftDown) {
        String indent = "";
        String lSelectedText = aTextComponent.getSelectedText();
        if (lSelectedText != null && lSelectedText.indexOf('\n') >= 0) {
        } else {
            aTextComponent.replaceSelection("\t");
            return;
        }
        int lSelectionStart = aTextComponent.getSelectionStart();
        int lSelectionEnd = aTextComponent.getSelectionEnd();
        int blankLineEmptyChar = 0;
        String text = lSelectedText + "\n@@@@";
        StringTokenizer lStringTokenizer = new StringTokenizer(text, "\r\n", true);
        ArrayList<String> lines = new ArrayList<String>(lStringTokenizer.countTokens());
        String lastToken = "";
        while (lStringTokenizer.hasMoreElements()) {
            String temp = lStringTokenizer.nextToken();
            if (temp.equals("\n")) {
                if ("\n".equals(lastToken)) {
                    lines.add("");
                }
            } else if (temp.equals("\r")) {
                lStringTokenizer.nextToken();
                temp = "\n";
                if ("\n".equals(lastToken)) {
                    lines.add("");
                }
            } else {
                if (isShiftDown) {
                    if (temp.charAt(0) == '\t') {
                        lines.add(temp.substring(1));
                    } else {
                        lines.add(temp);
                    }
                } else {
                    lines.add("\t" + temp);
                }
            }
            lastToken = temp;
        }
        lines.remove(lines.size() - 1);
        String currentLine = "";
        int count = lSelectionStart;
        for (String l : lines) {
            int length = l.length() + 1;
            if (count < length) {
                currentLine = l;
                break;
            }
            count -= length;
        }
        StringBuffer lBuffer = new StringBuffer();
        for (String l : lines) {
            lBuffer.append(l + "\n");
        }
        lBuffer.deleteCharAt(lBuffer.length() - 1);
        aTextComponent.replaceSelection(lBuffer.toString());
        aTextComponent.setSelectionStart(lSelectionStart);
        aTextComponent.setSelectionEnd(lSelectionStart + lBuffer.length());
    }
}
