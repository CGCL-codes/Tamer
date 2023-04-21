package de.fleckowarsky.yading.mainwindow;

public class HTMLizer {

    private HTMLizer() {
    }

    public static String htmlize(String searchResult, String searchTerm) {
        if (UIParameters.getInstance().getHTML()) {
            String resultText = replaceAll(searchResult, ">", "&gt;");
            resultText = replaceAll(resultText, "<", "&lt;");
            if (searchTerm != null) {
                resultText = insertMarks(resultText, searchTerm, "<span style=\"font-weight:bold\">", "</span>");
            }
            return "<html><head></head><body><pre style=\"font-family:monospace; margin:0px; line-heigth:90%\">" + resultText + "</pre></body></html>";
        } else {
            return searchResult;
        }
    }

    private static String insertMarks(String text, String marker, String markBefore, String markAfter) {
        int pos;
        int lastPos = 0;
        StringBuffer work = new StringBuffer(text);
        while ((pos = text.substring(lastPos).toLowerCase().indexOf(marker.toLowerCase())) >= 0) {
            work.insert(pos + lastPos, markBefore);
            work.insert(pos + lastPos + marker.length() + markBefore.length(), markAfter);
            lastPos = pos + lastPos + markBefore.length() + markAfter.length() + marker.length();
            text = new String(work);
        }
        return new String(work);
    }

    private static String replaceAll(String text, String marker, String replacement) {
        int pos;
        StringBuffer work = new StringBuffer(text);
        while ((pos = text.indexOf(marker)) >= 0) {
            work.replace(pos, pos + marker.length(), replacement);
            text = new String(work);
        }
        return new String(work);
    }
}
