package org.rgse.wikiinajar.helpers.wiki.render.filters;

/**
 * @author jens DOT fauth AT gmx DOT net
 * completly rewritten, original version has many problems in tables
 */
public class UrlFilter implements Filter {

    private String coStrUrlHTTP = "http://";

    private String coStrUrlHTTPS = "https://";

    private String coStrUrlFTP = "ftp://";

    private int m_iLinkNumber = 0;

    public String filter(String text) {
        m_iLinkNumber = 1;
        text = HandleExternalUrl(coStrUrlHTTP, text);
        text = HandleExternalUrl(coStrUrlHTTPS, text);
        text = HandleExternalUrl(coStrUrlFTP, text);
        return text;
    }

    private String HandleExternalUrl(String coStrUrl, String text) {
        String result = text;
        int iIndex = result.indexOf(coStrUrl);
        while (iIndex >= 0) {
            String strCode = "";
            if (iIndex > 0) {
                if (result.charAt(iIndex - 1) == '[') {
                    int iIndexEnd = result.indexOf("]", iIndex);
                    int iTempIndex = result.indexOf("\n", iIndex);
                    if (iTempIndex < iIndexEnd) {
                        iIndexEnd = iIndex;
                    }
                    if (iIndexEnd > iIndex) {
                        String strUrl = result.substring(iIndex - 1, iIndexEnd + 1);
                        String strName = "";
                        int iIndexSpace = strUrl.indexOf(" ");
                        if (iIndexSpace > 0) {
                            strName = strUrl.substring(iIndexSpace, strUrl.length() - 1).trim();
                            strUrl = strUrl.substring(1, iIndexSpace).trim();
                        } else {
                            strUrl = strUrl.substring(1, strUrl.length() - 1);
                            strName = "[" + String.valueOf(m_iLinkNumber++) + "]";
                        }
                        strCode += "<a href='";
                        strCode += strUrl;
                        strCode += "'>";
                        strCode += strName;
                        strCode += "</a>";
                    } else {
                        strCode = "<b>Error in external link: external links that start with \"[\" have to be closed with \"]\"</b>";
                        iIndexEnd = iIndex;
                    }
                    String start = result.substring(0, iIndex - 1);
                    String end = result.substring(iIndexEnd + 1);
                    result = start + strCode + end;
                    iIndex = result.indexOf(coStrUrl, iIndex + strCode.length());
                    continue;
                }
            }
            int iIndexEnd = FindEndOfURL(result, iIndex);
            strCode += "<a href='";
            String strUrl = result.substring(iIndex, iIndexEnd).trim();
            strCode += strUrl;
            strCode += "'>";
            strCode += strUrl;
            strCode += "</a>";
            String start = result.substring(0, iIndex);
            String end = result.substring(iIndexEnd);
            result = start + strCode + end;
            iIndex = result.indexOf(coStrUrl, iIndex + strCode.length());
            continue;
        }
        return result;
    }

    private int FindEndOfURL(String text, int iIndex) {
        int iIndexPos = iIndex;
        String strNoURLChars = " \n\t<>,*'�`)(\"�{[]}";
        for (; ; ) {
            if (strNoURLChars.indexOf(text.charAt(iIndexPos)) >= 0) return iIndexPos;
            if (iIndexPos >= text.length()) return text.length();
            ++iIndexPos;
        }
    }
}
