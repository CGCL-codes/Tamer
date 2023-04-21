package org.vesuf.tools.xmi;

import org.vesuf.model.uml.foundation.core.*;
import org.vesuf.model.uml.foundation.extensions.*;
import org.w3c.dom.*;
import java.util.*;

/**
 *  Parser for tagged values.
 */
public class TaggedValueParser extends ModelElementParser {

    /**
	 *  Create the model element.
	 *  @param node	The xmi node.
	 *  @param context	The parsing context.
	 *  @return The created model element.
	 */
    protected IModelElement createModelElement(Node node, IParseContext context) {
        String name = getName(node);
        String tag = getAttribute("tag", node).toLowerCase();
        String value = getAttribute("value", node);
        Namespace namespace = (Namespace) getReference("namespace", node, context);
        ModelElement modelelement = (ModelElement) getReference("modelElement", node, context);
        TaggedValue tv = new TaggedValue(name, namespace, modelelement, tag, value);
        return tv;
    }
}
