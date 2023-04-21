package com.handcoded.xml.resolver;

import java.util.Stack;
import org.xml.sax.SAXException;

/**
 * Defines a standard API implemented by catalog entry types that map
 * entities.
 * 
 * @author	BitWise
 * @version	$Id: EntityRule.java 492 2011-03-20 17:58:55Z andrew_jacobs $
 * @since	TFP 1.1
 */
interface EntityRule {

    /**
	 * Applies the rule instance to the public or system identifier in an
	 * attempt to locate the URI of a resource with can provide the required
	 * information.
	 *
	 * @param	publicId		The public identifier of the external entity
	 *							being referenced, or null if none was supplied.
	 * @param	systemId		The system identifier of the external entity
	 *							being referenced.
	 * @param	catalogs		The stack of catalogs being processed
	 * @return	A new URI if the rule was successfully applied, otherwise
	 *			<CODE>null</CODE>.
	 * @throws	SAXException If an occur was detected during processing.
	 * @since	TFP 1.0
	 */
    public abstract String applyTo(final String publicId, final String systemId, Stack<GroupEntry> catalogs) throws SAXException;
}
