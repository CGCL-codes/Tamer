package rql4j.iodata;

import rql4j.domain.Element;
import rql4j.domain.IoData;
import java.util.List;

public class IoElements extends IoObject {

    public IoElements(IoData ioData) {
        super(ioData);
    }

    public Element getElementByName(String name) {
        if (this.ioData != null && this.ioData.getPage() != null && this.ioData.getPage().getElements() != null && this.ioData.getPage().getElements().getElementList() != null) {
            List<Element> elementList = this.ioData.getPage().getElements().getElementList();
            for (Element element : elementList) {
                if (element.getName().equals(name)) return element;
            }
        }
        return null;
    }
}
