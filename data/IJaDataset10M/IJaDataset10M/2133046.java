package org.pustefixframework.config.project.parser;

import java.util.regex.PatternSyntaxException;
import org.pustefixframework.config.customization.CustomizationAwareParsingHandler;
import org.pustefixframework.config.generic.ParsingUtils;
import org.w3c.dom.Element;
import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.parser.exception.ParserException;
import de.schlund.pfixxml.Tenant;

/**
 * 
 * @author mleidig@schlund.de
 *
 */
public class TenantHostParsingHandler extends CustomizationAwareParsingHandler {

    public void handleNodeIfActive(HandlerContext context) throws ParserException {
        Element elem = (Element) context.getNode();
        Tenant tenant = ParsingUtils.getFirstTopObject(Tenant.class, context, true);
        String host = elem.getTextContent().trim();
        if (host.length() == 0) throw new ParserException("Element '/project/tenant/host' must not be empty!");
        try {
            tenant.setHostPattern(host);
        } catch (PatternSyntaxException x) {
            throw new ParserException("Element '/project/tenant/host' has invalid value: " + host, x);
        }
    }
}
