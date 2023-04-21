package org.kablink.teaming.module.definition.ws;

import java.util.Set;
import java.util.Map;
import org.dom4j.Element;
import org.kablink.teaming.domain.CustomAttribute;
import org.kablink.teaming.module.folder.index.IndexUtils;
import org.kablink.teaming.module.shared.EntityIndexUtils;
import org.kablink.teaming.search.BasicIndexUtils;

/**
 *
 * @author Jong Kim
 */
public class ElementBuilderProfileNames extends AbstractElementBuilder {

    protected boolean build(Element element, CustomAttribute attribute) {
        return true;
    }
}
