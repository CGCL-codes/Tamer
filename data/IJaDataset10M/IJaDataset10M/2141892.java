package watij.elements;

import org.w3c.dom.Element;
import watij.finders.TagFinder;
import java.util.List;

public class Forms extends HtmlElementCollections<Form> {

    public Forms(List<Element> list, HtmlElementFactory htmlElementFactory) throws Exception {
        super(list, htmlElementFactory);
    }

    protected Form get(Element element) throws Exception {
        return htmlElementFactory().form(element);
    }

    public boolean matches(Element element) throws Exception {
        return new TagFinder("form").matches(element);
    }
}
