package org.jmetis.template.library;

import org.jmetis.template.handler.ITemplateHandler;
import org.jmetis.template.model.ITemplateNode;

/**
 * {@code ITemplateHandlerLibrary} represents a library of {@link ITemplateNode}
 * s associated with one or more name spaces.
 * 
 * @author aerlach
 */
public interface ITemplateHandlerLibrary {

    /**
	 * Create a instance of a {@link ITemplateHandler} using the given {@code
	 * namespace}, and {@code}.
	 * 
	 * @param namespace
	 * @param localName
	 * @return a {@link ITemplateHandler} instance using the given {@code
	 *         namespace}, and {@code}
	 */
    ITemplateHandler createTemplateHandler(String namespace, String localName);
}
