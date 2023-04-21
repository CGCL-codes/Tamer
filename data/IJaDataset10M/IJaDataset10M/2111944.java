package com.volantis.map.sti.mime;

import com.volantis.map.operation.ResourceDescriptor;

/**
 * Retrieves MIME type of the resource to be transcoded from specified
 * ResourceDescriptor.
 * 
 * @mock.generate
 */
public interface MimeTypeRetriever {

    /**
     * Retrieves and returns MIME type of the resource to be transcoded from
     * specified ResourceDescriptor.
     * 
     * @param descriptor The resource descriptor.
     * @return The MIME type of the resource.
     * @throws MimeTypeRetrieverException in case MIME type can not be retrieved.
     */
    public String retrieveMIMEType(ResourceDescriptor descriptor) throws MimeTypeRetrieverException;
}
