package parser;

import java.util.HashSet;
import java.util.Set;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import aau.Resource;
import aau.User;

/**
 * 
 * This class returns the content of a Html document.
 */
public class KiWiDeliParser {

    public static void main(String[] args) {
        KiWiDeliParser kiWiDeliParser = new KiWiDeliParser();
        Set<Resource> kiwiAllResources = kiWiDeliParser.getKiWiDeliData("http://delicious.com/tag/programming+java+software");
        kiwiAllResources.addAll(kiWiDeliParser.getKiWiDeliData("http://delicious.com/tag/toolbox", 8));
        kiwiAllResources.addAll(kiWiDeliParser.getKiWiDeliData("http://delicious.com/tag/software", 8));
        kiwiAllResources.addAll(kiWiDeliParser.getKiWiDeliData("http://delicious.com/tag/life", 8));
        kiwiAllResources.addAll(kiWiDeliParser.getKiWiDeliData("http://delicious.com/tag/audio", 2));
        kiwiAllResources.addAll(kiWiDeliParser.getKiWiDeliData("http://delicious.com/tag/house", 8));
        System.out.println(kiwiAllResources.size());
    }

    public boolean persit(Tag tag) {
        boolean addContent = false;
        if (tag != null) {
            String idAttribute = tag.getAttribute("class");
            if (idAttribute != null && idAttribute.equals("taggedlink ")) {
                addContent = true;
            }
        }
        return addContent;
    }

    public Set<Resource> getKiWiDeliData(String url, int size) {
        Set<Resource> kiwiAllResources = getKiWiDeliData(url);
        Set<Resource> kiwiAllResourcesFinal = new HashSet<Resource>();
        for (int i = 0; i < size; i++) {
            kiwiAllResourcesFinal.add((Resource) kiwiAllResources.toArray()[i]);
        }
        return kiwiAllResourcesFinal;
    }

    public Set<Resource> getKiWiDeliData(String url) {
        Resource kiWiResource = null;
        Set<Resource> kiwiAllResources = new HashSet<Resource>();
        try {
            Parser parse = new Parser(url);
            NodeFilter filtro = new NodeClassFilter(LinkTag.class);
            NodeList nodeList = parse.extractAllNodesThatMatch(filtro);
            if (nodeList != null && nodeList.size() > 0) {
                LinkTag tag = null;
                for (int itr = 0; itr < nodeList.size(); itr++) {
                    tag = (LinkTag) nodeList.elementAt(itr);
                    String idAttribute = tag.getAttribute("class");
                    if (idAttribute != null && idAttribute.equals("taggedlink ")) {
                        kiWiResource = new Resource();
                        kiWiResource.setDescription(tag.getStringText());
                        kiWiResource.setLink(tag.getLink());
                        kiWiResource.setUri(tag.getLink());
                    }
                    if (idAttribute != null && (idAttribute.equals("user") || idAttribute.contains("user user-tag"))) {
                        User user = new User();
                        if (idAttribute.equals("user")) {
                            user.setName(tag.getChild(1).getText().trim());
                        } else {
                            Span span = (Span) tag.getFirstChild();
                            user.setName((span.getFirstChild().getText().trim()));
                        }
                        user.setEmail(user.getName() + "@email.com".trim());
                        if (kiWiResource != null && user != null) {
                            kiWiResource.setAuthor(user);
                        }
                    }
                    if (idAttribute != null && idAttribute.contains("tag-chain-item-link  noplay")) {
                        Span span = (Span) tag.getFirstChild();
                        kiWiResource.addTag(span.getFirstChild().getText().trim());
                    }
                    if (kiWiResource != null && kiWiResource.getAuthor() != null && kiWiResource.getTags() != null && !kiWiResource.getTags().isEmpty() && ((itr + 1 < nodeList.size()) && persit((LinkTag) nodeList.elementAt(itr + 1))) || (itr + 1 == nodeList.size())) {
                        kiwiAllResources.add(kiWiResource);
                    }
                }
            }
        } catch (ParserException e) {
            e.printStackTrace();
        }
        return kiwiAllResources;
    }
}
