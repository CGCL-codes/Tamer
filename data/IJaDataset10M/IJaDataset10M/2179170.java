package html.basic;

import java.util.Vector;
import validaciones.Validar;

public class HTMLList extends AbstractHTMLTagElement {

    private String m_title;

    private Vector m_options;

    private Vector m_classOptions;

    private String m_classDefault;

    private HTMLList(String title, Vector options, Vector classOptions, String classDefault, String classULOpc) {
        super("ul");
        m_title = title;
        m_options = options;
        m_classOptions = classOptions;
        m_classDefault = classDefault;
        if (classULOpc != null) setClass(classULOpc);
    }

    public static HTMLList createList(String classULOpc) {
        Vector options, classOptions;
        String title, classDefault;
        title = null;
        classDefault = null;
        options = new Vector();
        classOptions = new Vector();
        return new HTMLList(title, options, classOptions, classDefault, classULOpc);
    }

    public static HTMLList creaMenuVertical(String title) {
        Vector options, classOptions;
        options = new Vector();
        classOptions = new Vector();
        return new HTMLList(title, options, classOptions, "listaVertical", null);
    }

    public static HTMLList creaMenuHorizontal() {
        Vector options, classOptions;
        String title;
        options = new Vector();
        title = null;
        classOptions = new Vector();
        return new HTMLList(title, options, classOptions, "listaHorizontal", null);
    }

    public void add(IHTMLElement option) {
        m_options.add(option);
        m_classOptions.add(m_classDefault);
    }

    public void addClass(IHTMLElement option, String classOption) {
        m_options.add(option);
        m_classOptions.add(classOption);
    }

    public String getAttribs() {
        return "";
    }

    static String i_elementLi(IHTMLElement option, String classText) {
        StringBuffer bufferLi;
        String htmlOption;
        htmlOption = option.getHTML();
        bufferLi = new StringBuffer();
        bufferLi.append("<li class=\"" + classText + "\">\n");
        bufferLi.append(htmlOption);
        bufferLi.append("</li>\n");
        return bufferLi.toString();
    }

    public String getElements() {
        StringBuffer optionsLi;
        int numOptions;
        String textLi;
        optionsLi = new StringBuffer();
        if (m_title != null) {
            textLi = i_elementLi(new HTMLText(m_title), "tituloMenu");
            optionsLi.append(textLi);
        }
        numOptions = m_options.size();
        Validar.assertion(numOptions == m_classOptions.size(), "numOptions == m_classOptions.size()");
        for (int i = 0; i < numOptions; i++) {
            IHTMLElement textOption;
            String classOption;
            textOption = (IHTMLElement) m_options.elementAt(i);
            classOption = (String) m_classOptions.elementAt(i);
            textLi = i_elementLi(textOption, classOption);
            optionsLi.append(textLi);
        }
        return optionsLi.toString();
    }
}
