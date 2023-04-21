package org.eclipse.help.internal.xhtml;

import org.eclipse.help.internal.UAElement;
import org.eclipse.help.internal.dynamic.ProcessorHandler;

public class XHTMLCharsetHandler extends ProcessorHandler {

    private static final String ELEMENT_META = "meta";

    private static final String ATTRIBUTE_CONTENT = "content";

    private static final String PREFIX_CHARSET = "text/html; charset=";

    private static final String ENCODING_UTF8 = "UTF-8";

    public short handle(UAElement element, String id) {
        if (ELEMENT_META.equals(element.getElementName())) {
            String content = element.getAttribute(ATTRIBUTE_CONTENT);
            if (content != null && content.startsWith(PREFIX_CHARSET)) {
                element.setAttribute(ATTRIBUTE_CONTENT, PREFIX_CHARSET + ENCODING_UTF8);
                return HANDLED_CONTINUE;
            }
        }
        return UNHANDLED;
    }
}
