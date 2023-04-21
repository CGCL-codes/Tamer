package org.apache.http.nio.entity;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.nio.util.ContentInputBuffer;

/**
 * HTTP entity wrapper whose content is provided by a 
 * {@link ContentInputBuffer}. 
 *
 * @since 4.0
 */
public class ContentBufferEntity extends BasicHttpEntity {

    private HttpEntity wrappedEntity;

    /**
     * Creates new instance of ContentBufferEntity.
     * 
     * @param entity the original entity.
     * @param buffer the content buffer.
     */
    public ContentBufferEntity(final HttpEntity entity, final ContentInputBuffer buffer) {
        super();
        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }
        this.wrappedEntity = entity;
        setContent(new ContentInputStream(buffer));
    }

    @Override
    public boolean isChunked() {
        return this.wrappedEntity.isChunked();
    }

    @Override
    public long getContentLength() {
        return this.wrappedEntity.getContentLength();
    }

    @Override
    public Header getContentType() {
        return this.wrappedEntity.getContentType();
    }

    @Override
    public Header getContentEncoding() {
        return this.wrappedEntity.getContentEncoding();
    }
}
