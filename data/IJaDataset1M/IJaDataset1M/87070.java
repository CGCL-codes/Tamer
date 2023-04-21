package org.jxul.factories;

import org.jxul.XulElement;

/**
 * @author Will Etson
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface Factory {

    XulElement create(XulElement parent, XulElement child);
}
