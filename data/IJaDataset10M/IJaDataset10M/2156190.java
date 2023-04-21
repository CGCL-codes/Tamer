package net.sf.clairv.index.processor;

import net.sf.clairv.index.document.DocumentFactory;
import net.sf.clairv.index.resource.Resource;

/**
 * The index builder which is responsible for writing the documents.
 * 
 * @author qiuyin
 * 
 */
public interface ResourceProcessor {

    /**
	 * Closes the processor. After closing, this processor may not be used
	 * any more and all pending changes should be immediately committed.
	 * 
	 */
    public void close();

    /**
	 * Processes the specified <code>Resource</code> object. A
	 * <code>ResourceProcessor</code> object can process more than one
	 * <code>Resource</code>s before it gets closed.
	 * 
	 * @param resource
	 *            the <code>Resource</code> object to be handled
	 * @return the number of extracted documents; or -1 on error
	 * @see net.sf.clairv.index.resource.Resource#extractDocuments(ResourceProcessor);
	 */
    public int process(Resource resource);

    public DocumentHolder getDocumentHolder();

    public DocumentFactory getDocumentFactory();
}
