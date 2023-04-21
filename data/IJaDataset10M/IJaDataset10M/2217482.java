package org.t2framework.lucy.config.stax;

import javax.xml.stream.XMLResolver;
import javax.xml.stream.XMLStreamException;
import org.t2framework.commons.util.ResourceUtil;

/**
 * <#if locale="en">
 * <p>
 * The resolver class for lucy-config.dtd.The resolve entity should not do
 * remote access for lucy-config.dtd, instead of remote access, just returns one
 * which we keep in jar.
 * 
 * </p>
 * <#else>
 * <p>
 * 
 * </p>
 * </#if>
 * 
 * @author shot
 * 
 */
public class LucyXmlResolver implements XMLResolver {

    public static final String PUBLIC_ID = "-//LUCY//DTD LUCY//EN";

    public static final String DTD_PATH = "org/t2framework/lucy/config/stax/lucy-config.dtd";

    public LucyXmlResolver() {
    }

    @Override
    public Object resolveEntity(String publicID, String systemID, String baseURI, String namespace) throws XMLStreamException {
        if (PUBLIC_ID.equals(publicID)) {
            return ResourceUtil.getResourceAsStream(DTD_PATH);
        }
        return null;
    }
}
