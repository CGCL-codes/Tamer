package org.exist.dom;

import java.util.Map;
import org.w3c.dom.Element;

/**
 * @author <a href="mailto:shabanovd@gmail.com">Dmitriy Shabanov</a>
 *
 */
public interface ElementAtExist extends NodeAtExist, Element {

    public Map<String, String> getNamespaceMap();
}
