package watij.finders;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import java.util.List;

public class DecendantFinder implements Finder {

    public List<Element> find(List<Element> elements) throws Exception {
        List<Element> newList = new ArrayList<Element>();
        for (Element element : elements) {
            newList.addAll(find(element));
        }
        return newList;
    }

    public List<Element> find(Element element) throws Exception {
        List<Element> filteredList = new ArrayList<Element>();
        List<Element> decendantElements = decendantElements(element);
        filteredList.addAll(decendantElements);
        return filteredList;
    }

    private List<Element> decendantElements(Element element) throws Exception {
        List<Element> list = new ArrayList<Element>();
        doDecendants(list, element);
        return list;
    }

    private void doDecendants(List<Element> list, Node node) {
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            list.add((Element) node);
        }
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            doDecendants(list, nodeList.item(i));
        }
    }
}
