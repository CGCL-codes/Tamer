package uk.org.ogsadai.resource.property;

import java.io.File;
import org.w3c.dom.Element;
import uk.org.ogsadai.context.OGSADAIConstants;
import uk.org.ogsadai.context.OGSADAIContext;
import uk.org.ogsadai.config.Key;
import uk.org.ogsadai.resource.OnDemandResourcePropertyCallback;
import uk.org.ogsadai.resource.OnDemandResourcePropertyCallbackException;
import uk.org.ogsadai.resource.ResourcePropertyName;
import uk.org.ogsadai.resource.ResourcePropertyValue;
import uk.org.ogsadai.resource.SimpleResourcePropertyValue;
import uk.org.ogsadai.resource.dataresource.DataResource;
import uk.org.ogsadai.util.xml.XML;

/**
 * Example of on-demand resource property that's backed up by
 * an XML file that provides its value. Assumes the file path
 * is relative to the OGSA-DAI server configuration files directory as
 * stored in the OGSA-DAI context with key 
 * <code>OGSADAIConstants.CONFIG_DIR</code>.
 * 
 * @author The OGSA-DAI Project Team.
 */
public class FileBasedResourcePropertyExample implements OnDemandResourcePropertyCallback {

    /** Copyright. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2008.";

    /** 
     * Key for file configuration property. 
     * (<code>example.property.file</code>). 
     */
    public static final Key FILE_KEY = new Key("example.property.file");

    /** Parent data resource. */
    private DataResource mDataResource;

    /** Resource property name. */
    private ResourcePropertyName mName;

    /** Content of property file. */
    private File mPropertyFile;

    /**
     * Constructor.
     *
     * @param dataResource
     *     Parent data resource.
     * @param name
     *     Resource property name.
     * @throws IllegalArgumentException
     *     If either argument is <code>null</code>.
     */
    public FileBasedResourcePropertyExample(DataResource dataResource, ResourcePropertyName name) {
        if (dataResource == null) {
            throw new IllegalArgumentException("dataResource must not be null");
        }
        if (name == null) {
            throw new IllegalArgumentException("name must not be null");
        }
        mName = name;
        mDataResource = dataResource;
        String fileName = (String) (mDataResource.getState().getConfiguration().get(FILE_KEY));
        File configDir = (File) OGSADAIContext.getInstance().get(OGSADAIConstants.CONFIG_DIR);
        mPropertyFile = new File(configDir, fileName);
    }

    /**
     * {@inheritDoc}
     *
     * Reads in the file content and returns as a {@link
     * org.w3c.dom.Node} object.
     */
    public ResourcePropertyValue getResourcePropertyValue(ResourcePropertyName name) {
        if (!mName.equals(name)) {
            return null;
        }
        Element fileContent = null;
        SimpleResourcePropertyValue value = null;
        try {
            fileContent = XML.fileToDocument(mPropertyFile.getPath()).getDocumentElement();
            value = new SimpleResourcePropertyValue(fileContent);
        } catch (Throwable e) {
            throw new OnDemandResourcePropertyCallbackException(mDataResource.getResourceID(), mName);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     *
     * This is a no-op.
     */
    public void setResourcePropertyValue(ResourcePropertyName name, Object value) {
    }
}
