package org.marcont.rdf.utils;

import java.util.Iterator;
import java.util.List;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;

/**
 *  Klasa pomocnicza w drukowaniu dokumentów XML
 *
 * @author     skruk
 * @created    23 listopad 2003
 */
public class PrettyXML {

    /**  Jest to singelton - instancja klasy */
    private static PrettyXML instance = new PrettyXML();

    /**  Klasa ta jest singeltonem - prywatny konstruktor */
    private PrettyXML() {
    }

    /**
    *  Drukuje dokument XML jako stronę HTML
    *
    * @param  doc      Document do wydrukowania
    * @param  docName  Nazwa dokumentu do wydrukowania
    * @return          StringBuilder zawierający reprezentację HTML
    */
    public StringBuilder printDocument(Document doc, String docName) {
        StringBuilder sb = new StringBuilder("<html>\n<head>\n");
        sb.append("<meta http-equiv=\"author\" content=\"Sebastian R. Kruk\"/>\n");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html;  charset=UTF-8\"/>\n");
        sb.append("<title>Źródło dokumentu XML ").append(docName).append("</title>\n");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\"/>\n");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/addBook.css\"/>\n");
        sb.append("</head>\n<body class=\"main\">\n");
        sb.append(printElement(doc.getRootElement(), 0));
        sb.append("</body>\n</html>\n");
        return sb;
    }

    /**
    *  Drukuje dokument RDF jako stronę HTML
    *
    * @param  doc      Document do wydrukowania
    * @param  docName  Nazwa dokumentu do wydrukowania
    * @return          StringBuilder zawierający reprezentację HTML
    */
    public StringBuilder printRDF(Document doc, String docName) {
        StringBuilder sb = new StringBuilder("<html>\n<head>\n");
        sb.append("<meta http-equiv=\"author\" content=\"Sebastian R. Kruk\"/>\n");
        sb.append("<meta http-equiv=\"Content-Type\" content=\"text/html;  charset=UTF-8\"/>\n");
        sb.append("<title>Źródło bazy RDF ").append(docName).append("</title>\n");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/style.css\"/>\n");
        sb.append("<link rel=\"stylesheet\" type=\"text/css\" href=\"../css/addBook.css\"/>\n");
        sb.append("</head>\n<body class=\"main\">\n");
        sb.append(printElement(doc.getRootElement(), 0));
        sb.append("</body>\n</html>\n");
        return sb;
    }

    /**
    *  Drukuje drzewo XML jako stronę HTML
    *
    * @param  el  Element reprezentujący korzeń drzewa do wydrukowania
    * @return     StringBuilder zawierający reprezentację HTML
    */
    public StringBuilder printElement(Element el, int indent) {
        StringBuilder sb = new StringBuilder("<div class='element'>\n");
        for (int i = 0; i < indent + 1; i++) sb.append("&nbsp;&nbsp;");
        sb.append("&lt;");
        if (el.getNamespacePrefix().length() > 0) {
            sb.append("<span class='ns'>").append(el.getNamespacePrefix()).append("</span>:");
        }
        sb.append("<span class='element'>").append(el.getName()).append("</span>\n");
        List<?> attributes = el.getAttributes();
        Iterator it = attributes.iterator();
        while (it.hasNext()) {
            Attribute attr = (Attribute) it.next();
            if (attr.getNamespacePrefix().length() > 0) {
                sb.append("<span class='ns'>").append(attr.getNamespacePrefix()).append("</span>:");
            }
            sb.append("<span class='attribute'>").append(attr.getName()).append("</span>='");
            sb.append("<span class='value'>").append(this.reformat(attr.getValue())).append("</span>'&nbsp;");
        }
        boolean isEmpty = true;
        List<Element> elements = el.getChildren();
        it = elements.iterator();
        isEmpty = !((el.getTextTrim().length() > 0) || (it.hasNext()));
        if (!isEmpty) {
            sb.append("&gt;");
        }
        if (el.getTextTrim().length() > 0) {
            sb.append("<span class='text'>").append(this.reformat(el.getTextTrim())).append("</span>\n");
        }
        if (it.hasNext()) {
            sb.append("<br/>");
        }
        while (it.hasNext()) {
            Element elm = (Element) it.next();
            sb.append(this.printElement(elm, indent + 1));
        }
        if (!isEmpty) {
            for (int i = 0; i < indent + 1; i++) sb.append("&nbsp;&nbsp;");
            sb.append("&lt;/");
            if (el.getNamespacePrefix().length() > 0) {
                sb.append("<span class='ns'>").append(el.getNamespacePrefix()).append("</span>:");
            }
            sb.append("<span class='element'>").append(el.getName()).append("</span>\n&gt;");
        } else {
            sb.append("&nbsp;/&gt;");
        }
        sb.append("</div>\n");
        return sb;
    }

    /**
    *  Przetwarza napisy na bezpieczne dla LaTeXa
    *
    * @param  input  String do przetworzenia
    * @return        Wynikowy string
    */
    protected String latexReformat(String input) {
        StringBuilder bufor = new StringBuilder(input);
        for (int i = 0; i < bufor.length(); i++) {
            if ((bufor.charAt(i) == '$') || (bufor.charAt(i) == '&') || (bufor.charAt(i) == '%') || (bufor.charAt(i) == '#') || (bufor.charAt(i) == '_') || (bufor.charAt(i) == '{') || (bufor.charAt(i) == '}')) {
                bufor.insert(i, "\\");
                i++;
            } else if (bufor.charAt(i) == '\\') {
                bufor.insert(i + 1, "backslash$");
                bufor.insert(i, "$");
                i += 10;
            } else if (bufor.charAt(i) == '/') {
                bufor.setCharAt(i, '\\');
                bufor.insert(i + 1, "slash$");
                bufor.insert(i, "$");
                i += 6;
            }
        }
        String result = "";
        try {
            result = bufor.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    /**
    *  Przetwarza ciagi znakowe towrzac z elementow o nazwie <b>http...</b> linki typu <a>;
    *
    * @param  input  String do przetworzenia
    * @return        Wynikowy string
    */
    protected String reformat(String input) {
        String result = input;
        if (input.startsWith("http")) {
            StringBuilder sb = new StringBuilder("<a class='resource' target='_blank' href='");
            sb.append(input).append("'>").append(input).append("</a>");
            result = sb.toString();
        }
        return result;
    }

    /**
    *  Zwraca zawsze ten sam obiekt - pattern Singelton
    *
    * @return    Instancje Klasy
    */
    public static PrettyXML getInstance() {
        return instance;
    }
}
