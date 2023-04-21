package watij.finders;

import org.w3c.dom.Element;

public class NameFinder extends BaseFinder {

    String value;

    public NameFinder(String value) {
        this.value = value;
    }

    public boolean matches(Element element) throws Exception {
        return element.getAttribute("name").equals(value);
    }
}
