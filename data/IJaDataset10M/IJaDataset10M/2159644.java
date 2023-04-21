package com.apelon.common.xml;

import com.apelon.common.log4j.Categories;
import java.util.Enumeration;
import org.xml.sax.InputSource;

/**
 * <p>Less strict redirecting entity resolver. </p>
 * <p>Description:  This class should be used sparingly.  Only in those cases
 * where it is necessary to maintain backward compatibility with old XML files
 * that do not use the URI format of the DTD file.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Apelon, Inc.</p>
 * @author Richard Corzo
 */
public class ForgivingEntityResolver extends RedirEntityRes {

    public ForgivingEntityResolver(String fullUri, Class resourceClass, String resourceName) {
        super(fullUri, resourceClass, resourceName);
    }

    protected InputSource handleEntityNotFound(String publicId, String systemId) {
        String message = "System ID: " + systemId + " is no longer supported.";
        message += "  The document type definition in the XML file should use";
        message += " the URI form of the DTD.  Will use the first available DTD in";
        message += " the catalog.";
        Categories.dataXml().warn(message);
        Enumeration keys = catalog().keys();
        if (keys.hasMoreElements()) {
            String curKey = (String) keys.nextElement();
            Categories.dataXml().debug("curkey == " + curKey);
            EntityLoader loader = (EntityLoader) catalog().get(curKey);
            InputSource source = loader.load();
            return source;
        }
        return null;
    }
}
