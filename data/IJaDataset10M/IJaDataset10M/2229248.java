package com.aptech.fpt.c0810g.newspj.business;

import com.aptech.fpt.c0810g.newspj.entity.News;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author zang
 */
public class DanTriDataProcess implements IDataProcess {

    @Override
    public NodeList getNodeList(String url, String pClassName) {
        NodeList list = null;
        try {
            URL hp = new URL(url);
            final HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setNamespacesAware(false);
            props.setUseEmptyElementTags(true);
            props.setTreatUnknownTagsAsContent(true);
            Document doc = null;
            TagNode tagNode = null;
            try {
                tagNode = cleaner.clean(hp);
                DomSerializer sss = new DomSerializer(cleaner.getProperties(), true);
                doc = sss.createDOM(tagNode);
            } catch (Exception e) {
            }
            XPathFactory xpathFactory = XPathFactory.newInstance();
            XPath xpath = xpathFactory.newXPath();
            list = (NodeList) xpath.evaluate("//div[@class='" + pClassName + "']", doc, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(DanTriDataProcess.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MalformedURLException ex) {
            Logger.getLogger(DanTriDataProcess.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }

    @Override
    public ArrayList<News> getTextContent(NodeList node) {
        ArrayList<News> array = new ArrayList<News>();
        for (int ii = 0; ii < node.getLength(); ii++) {
            Node n = node.item(ii);
            News news = new News();
            if (n.getChildNodes().item(1).getChildNodes().getLength() > 0) {
                news.setPhoto(n.getChildNodes().item(1).getChildNodes().item(0).getAttributes().getNamedItem("src").getTextContent());
            }
            news.setTitle(n.getChildNodes().item(1).getAttributes().getNamedItem("title").getTextContent());
            news.setSubContent(n.getChildNodes().item(3).getChildNodes().item(3).getTextContent());
            String sourceLink = "http://dantri.com.vn" + n.getChildNodes().item(1).getAttributes().getNamedItem("href").getTextContent();
            news.setSourceLink(sourceLink);
            array.add(news);
        }
        return array;
    }

    public News getTextContent2(NodeList node) {
        News newsItem = new News();
        for (int ii = 0; ii < node.getLength(); ii++) {
            Node n = node.item(ii);
            News news = new News();
        }
        return newsItem;
    }

    @Override
    public String xMLToHTML(Node node) {
        StringWriter sw = new StringWriter();
        try {
            javax.xml.transform.Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException te) {
        }
        return sw.toString();
    }

    @Override
    public void getMainContent(ArrayList<News> array, NodeList node) {
        String noneName = node.item(0).getTextContent();
    }
}
