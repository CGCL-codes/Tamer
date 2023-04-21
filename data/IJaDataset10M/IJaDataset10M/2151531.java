package org.kablink.teaming.module.definition.export;

import org.dom4j.Element;
import org.kablink.teaming.domain.DefinableEntity;
import org.kablink.teaming.domain.FileAttachment;
import org.kablink.teaming.util.AllModulesInjected;

/**
 *
 * @author Brian Kim
 */
public interface ElementBuilder {

    public static interface BuilderContext extends AllModulesInjected {

        void handleAttachment(FileAttachment att, String webUrl);
    }

    public boolean buildElement(Element element, DefinableEntity entity, String dataElemType, String dataElemName, BuilderContext context);
}
