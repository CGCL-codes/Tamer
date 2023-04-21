package org.kablink.teaming.module.definition.ws;

import java.util.Set;
import java.util.Map;
import org.dom4j.Element;
import org.kablink.teaming.domain.CustomAttribute;
import org.kablink.teaming.module.profile.index.ProfileIndexUtils;

/**
 * Implement here cause not all Elements are included on every form
 * @author Jong Kim
 */
public class ElementBuilderProfileElement extends AbstractElementBuilder {

    protected boolean build(Element element, CustomAttribute attribute) {
        return true;
    }
}
