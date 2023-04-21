package org.pagger.data.picture.xmp;

import org.pagger.data.AbstractAccessProperty;
import org.w3c.dom.Node;

/**
 * @author Gerd Saurer
 */
public class XmpProperty<T> extends AbstractAccessProperty<Node, T> {

    public XmpProperty(final Class<T> type, final String id, final String name, final String description, final XmpMetadataAccess<T> access) {
        super(type, id, name, description, access);
    }
}
