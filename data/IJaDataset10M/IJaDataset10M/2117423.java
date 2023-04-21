package org.pustefixframework.xmlgenerator.config.parser;

import org.pustefixframework.config.generic.ParsingUtils;
import org.pustefixframework.xmlgenerator.config.model.Configuration;
import org.pustefixframework.xmlgenerator.config.model.Page;
import org.pustefixframework.xmlgenerator.config.model.XMLExtension;
import org.w3c.dom.Element;
import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.parser.ParsingHandler;
import com.marsching.flexiparse.parser.exception.ParserException;

/**
 * 
 * @author mleidig@schlund.de
 *
 */
public class PageParsingHandler implements ParsingHandler {

    public void handleNode(HandlerContext context) throws ParserException {
        Element element = (Element) context.getNode();
        ParsingUtils.checkAttributes(element, new String[] { "name", "handler" }, new String[] { "accesskey" });
        String name = element.getAttribute("name");
        String handler = element.getAttribute("handler");
        String accessKey = element.getAttribute("accesskey");
        if (accessKey.equals("")) accessKey = null;
        Page page = new Page(name, handler, accessKey);
        Page parentPage = ParsingUtils.getFirstTopObject(Page.class, context, false);
        if (parentPage != null) {
            parentPage.addChildPage(page);
        } else {
            Configuration configuration = ParsingUtils.getFirstTopObject(Configuration.class, context, false);
            if (configuration != null) {
                configuration.addPage(page);
            } else {
                XMLExtension<Page> ext = XMLExtensionParsingUtils.getListExtension(context);
                ext.add(page);
            }
        }
        context.getObjectTreeElement().addObject(page);
    }
}
