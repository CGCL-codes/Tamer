package html.form;

import html.basic.AbstractHTMLTagElement;
import html.basic.HTMLUtils;

public class HTMLTextArea extends AbstractHTMLTagElement {

    String m_name;

    String m_value;

    int m_rows;

    public HTMLTextArea(String name, int rows, String value) {
        super("textarea");
        m_name = name;
        m_value = value;
        m_rows = rows;
    }

    public String getAttribs() {
        String strName, strRows, strNumerRows;
        strNumerRows = Integer.toString(m_rows);
        strName = HTMLUtils.stringAttrib("name", m_name);
        strRows = HTMLUtils.stringAttrib("rows", strNumerRows);
        return strName + " " + strRows + " " + "cols=\"80%\"";
    }

    public String getElements() {
        return m_value;
    }
}
