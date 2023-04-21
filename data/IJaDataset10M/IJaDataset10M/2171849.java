package org.jbeanmapper.configurator;

import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;
import java.util.ArrayList;

/**
 * <code>Digester</code> rule for handling &lt;bean-mapping&gt; elements.
 *
 * @author Brian Pugh
 */
public class BeanMappingsRule extends Rule {

    /**
   * Process the &lt;bean-mapping&gt; element
   *
   * @param namespace  the namespace URI of the matching element, or an empty string if the parser is not namespace
   *                   aware or the element has no namespace
   * @param name       the local name if the parser is namespace aware, or just the element name otherwise
   * @param attributes The attribute list of this element
   * @throws Exception if the rule cannot be processed.
   */
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        getDigester().push(new ArrayList());
    }
}
