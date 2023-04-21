package html.gadget;

import java.util.Enumeration;
import html.basic.IHTMLElement;
import html.basic.HTMLTable;
import html.basic.HTMLTableRow;

public class HTMLPanel implements IHTMLElement {

    HTMLLayout m_layout;

    boolean m_completo;

    public HTMLPanel(HTMLLayout layout) {
        m_layout = layout;
        m_completo = false;
    }

    public void setCompleto() {
        m_completo = true;
    }

    public String getAttribs() {
        return "";
    }

    public String getElements() {
        return "";
    }

    public String getHTML() {
        HTMLTable table;
        HTMLTableRow elements[];
        elements = m_layout.getHTML();
        table = new HTMLTable();
        if (m_completo == true) table.setWidth(100, HTMLTable.PORCENTAJE);
        for (int i = 0; i < elements.length; i++) table.add(elements[i]);
        return table.getHTML();
    }

    public Enumeration elements() {
        return m_layout.elements();
    }
}
