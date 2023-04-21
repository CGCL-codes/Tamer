package com.sitescape.team.module.definition.notify;

import java.util.List;
import java.util.Map;
import org.dom4j.Element;
import com.sitescape.team.domain.DefinableEntity;
import com.sitescape.team.domain.FileAttachment;
import com.sitescape.team.domain.FolderEntry;
import com.sitescape.team.web.WebKeys;
import com.sitescape.team.web.util.WebUrlUtil;

/**
* Handle unnamed attachments in mail notification.  This implememtation will
* send the file name only in a notification for both summary and full types. 
* See <code>NotifyBuilderAttachmentsSend</code>to send the actual file.
* @author Janet McCann
*/
public class NotifyBuilderAttachments extends AbstractNotifyBuilder {

    protected boolean build(Element element, Notify notifyDef, DefinableEntity entity, String dataElemName, Map args) {
        List atts = entity.getFileAttachments();
        for (int i = 0; i < atts.size(); ++i) {
            Element value = element.addElement("file");
            FileAttachment att = (FileAttachment) atts.get(i);
            if (att != null && att.getFileItem() != null) {
                value.setText(att.getFileItem().getName());
                if (notifyDef.isAttachmentsIncluded()) notifyDef.addAttachment(att); else if (entity instanceof FolderEntry) {
                    FolderEntry fEntry = (FolderEntry) entity;
                    String webUrl = WebUrlUtil.getServletRootURL() + WebKeys.SERVLET_VIEW_FILE + "?" + WebKeys.URL_BINDER_ID + "=" + fEntry.getParentFolder().getId().toString() + "&" + WebKeys.URL_ENTRY_ID + "=" + fEntry.getId().toString() + "&" + WebKeys.URL_FILE_ID + "=" + att.getId();
                    value.addAttribute("href", webUrl);
                }
            }
        }
        return true;
    }
}
