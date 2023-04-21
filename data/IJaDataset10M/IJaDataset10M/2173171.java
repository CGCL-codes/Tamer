package ru.curs.showcase.model.html.xform;

import java.io.StringWriter;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;
import ru.curs.showcase.runtime.AppInfoSingleton;
import ru.curs.showcase.util.xml.GeneralXMLHelper;

/**
 * Класс, преобразующий документ в HTML-код XForm.
 */
public final class XFormProducer extends GeneralXMLHelper {

    public static final String XF_INSTANCE = "xf:instance";

    public static final String XSLTFORMS_XSL = "xsltforms.xsl";

    public static final String XFORMS_URI = "http://www.w3.org/2002/xforms";

    public static final String EVENTS_URI = "http://www.w3.org/2001/xml-events";

    private static final int DEFAULT_BUFFER_SIZE = 1024;

    private static final String MAIN_INSTANCE = "mainInstance";

    private static final String INSTANCE = "instance";

    private XFormProducer() {
        throw new UnsupportedOperationException();
    }

    /**
	 * Возвращает org.w3c.dom.Document с пустыми данными, полученными на основе
	 * шаблона.
	 * 
	 * @param template
	 *            Шаблон
	 * 
	 * @return org.w3c.dom.Document
	 * @throws ParserConfigurationException
	 * 
	 */
    public static org.w3c.dom.Document getEmptyData(final org.w3c.dom.Document template) throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        org.w3c.dom.Document doc = db.newDocument();
        Node n = getMainInstance(template).getFirstChild();
        while (n != null) {
            if (n.getNodeType() == Node.ELEMENT_NODE) {
                break;
            }
            n = n.getNextSibling();
        }
        n = doc.importNode(n, true);
        doc.appendChild(n);
        return doc;
    }

    /**
	 * Возвращает ноду xf:instance c id=mainInstance. Такая нода должна
	 * существовать в шаблоне для того, чтобы он работал с Showcase.
	 * 
	 * @param doc
	 *            - документ шаблона.
	 * @return - элемент mainInstance.
	 */
    public static Node getMainInstance(final org.w3c.dom.Document doc) {
        NodeList l = doc.getElementsByTagName(XF_INSTANCE);
        for (int i = 0; i < l.getLength(); i++) {
            Node n = l.item(i).getAttributes().getNamedItem(ID_TAG);
            if ((n != null) && (MAIN_INSTANCE.equals(n.getTextContent()))) {
                return l.item(i);
            }
        }
        return null;
    }

    /**
	 * Возвращает строку, являющуюся HTML-фрагментом для отображения документа в
	 * виде XForm.
	 * 
	 * @param xml
	 *            документ
	 * @param tempData
	 *            временные данные документа (если эта переменная не равна null,
	 *            эти данные подставляются в MainInstance). Необходимо для
	 *            просмотра проимпортированного содержимого формы.
	 * 
	 * @return HTML-фрагмент, пригодный для отображения в браузере
	 * @throws TransformerException
	 * 
	 */
    public static String getHTML(final org.w3c.dom.Document xml, final org.w3c.dom.Document tempData) throws TransformerException {
        insertActualData(xml, tempData);
        return transform(xml);
    }

    private static String transform(final org.w3c.dom.Document xml) throws TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer tr = tf.newTransformer(new StreamSource(XFormProducer.class.getResourceAsStream(XSLTFORMS_XSL)));
        tr.setParameter("baseuri", "xsltforms/");
        tr.setParameter("xsltforms_home", AppInfoSingleton.getAppInfo().getWebAppPath() + "/xsltforms/");
        StringWriter sw = new StringWriter(DEFAULT_BUFFER_SIZE);
        tr.transform(new DOMSource(xml), new StreamResult(sw));
        String ret = sw.toString();
        return ret;
    }

    private static void insertActualData(final org.w3c.dom.Document xml, final org.w3c.dom.Document tempData) {
        if (tempData != null) {
            NodeList nl = xml.getElementsByTagNameNS(XFORMS_URI, INSTANCE);
            for (int i = 0; i < nl.getLength(); i++) {
                Node n = nl.item(i);
                if (MAIN_INSTANCE.equals(n.getAttributes().getNamedItem(ID_TAG).getTextContent())) {
                    n.setTextContent("");
                    Node nn = xml.importNode(tempData.getDocumentElement(), true);
                    n.appendChild(nn);
                    break;
                }
            }
        }
    }
}
