package com.bradrydzewski.tinyreport.birt.html;

import com.bradrydzewski.tinyreport.birt.xpath.XMLDataObject;
import com.bradrydzewski.tinyreport.html.Element;
import com.bradrydzewski.tinyreport.html.Page;
import java.util.ArrayList;

/**
 *
 * @author Brad Rydzewski
 */
public class PageBuilder {

    public Page getPage(XMLDataObject xml) {
        Page page = new Page();
        page.setChildElements(new ArrayList<Element>());
        for (XMLDataObject object : xml.getChildNodes()) {
            ElementBuilder builder = ElementBuilderFactory.get().getBuilder(object);
            if (builder != null) {
                Element element = builder.getElement(object);
                System.out.println("adding element of type " + element.getClass().getCanonicalName());
                page.getChildElements().add(element);
            } else {
                System.out.println("Null builder returned for child element of page");
            }
        }
        return page;
    }
}
