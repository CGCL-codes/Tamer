package org.pustefixframework.config.directoutputservice.parser;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Properties;
import org.pustefixframework.config.customization.CustomizationAwareParsingHandler;
import org.pustefixframework.config.directoutputservice.parser.internal.DirectOutputServiceConfigImpl;
import com.marsching.flexiparse.parser.HandlerContext;
import com.marsching.flexiparse.parser.exception.ParserException;

public class DirectOutputRegisterGlobalPropertiesParsingHandler extends CustomizationAwareParsingHandler {

    @Override
    protected void handleNodeIfActive(HandlerContext context) throws ParserException {
        DirectOutputServiceConfigImpl serviceConfig = context.getObjectTreeElement().getObjectsOfType(DirectOutputServiceConfigImpl.class).iterator().next();
        Properties properties = serviceConfig.getProperties();
        Collection<Properties> propertiesCollection = context.getObjectTreeElement().getObjectsOfType(Properties.class);
        for (Properties p : propertiesCollection) {
            Enumeration<?> en = p.propertyNames();
            while (en.hasMoreElements()) {
                String propName = (String) en.nextElement();
                String propValue = p.getProperty(propName);
                properties.setProperty(propName, propValue);
            }
        }
        serviceConfig.setProperties(properties);
    }
}
