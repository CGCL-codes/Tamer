package com.alesj.newsfeed.file;

import java.util.List;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public interface AttachmentHandler<T> {

    void saveAttachments(String rootId, List<? extends ByteAdapter> files) throws Exception;

    void removeAttachments();

    T[] getAttachments(String rootId);
}
