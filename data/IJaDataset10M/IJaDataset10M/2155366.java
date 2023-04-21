package com.sitescape.team.module.definition.ws;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.dom4j.Element;
import com.sitescape.team.domain.CustomAttribute;
import com.sitescape.team.domain.DefinableEntity;
import com.sitescape.team.domain.FileAttachment;
import com.sitescape.team.domain.FolderEntry;
import com.sitescape.team.module.definition.DefinitionUtils;
import com.sitescape.team.web.WebKeys;
import com.sitescape.team.web.util.WebUrlUtil;

/**
* Handle file field in mail notification.
* @author Janet McCann
*/
public class AbstractElementBuilderFile extends AbstractElementBuilder {

    protected void generateValues(Collection files, Element element, FolderEntry fEntry, String elementName) {
        for (Iterator iter = files.iterator(); iter.hasNext(); ) {
            Element value = element.addElement(elementName);
            FileAttachment att = (FileAttachment) iter.next();
            if (att != null && att.getFileItem() != null) {
                value.setText(att.getFileItem().getName());
                String webUrl = DefinitionUtils.getViewURL(fEntry, att);
                value.addAttribute("href", webUrl);
                context.handleAttachment(att, webUrl);
            }
        }
    }
}
