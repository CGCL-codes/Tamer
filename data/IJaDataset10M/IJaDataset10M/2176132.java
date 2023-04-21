package com.sitescape.team.module.ical.remoting.ws.attachments;

import org.apache.axis.attachments.AttachmentPart;
import com.sitescape.team.remoting.RemotingException;
import com.sitescape.team.remoting.ws.util.attachments.AttachmentsHelper;
import com.sitescape.team.util.stringcheck.StringCheckUtil;

public class IcalServiceImpl extends com.sitescape.team.module.ical.remoting.ws.IcalServiceImpl {

    /** 
	 * Extend basic support in {@link com.sitescape.team.module.ical.remoting.ws.IcalServiceImpl#uploadCalendarEntries(long, String)} 
	 * to include importing calendar entries from attachments.
	 */
    public void uploadCalendarEntries(long folderId, String iCalDataAsXML) {
        iCalDataAsXML = StringCheckUtil.check(iCalDataAsXML);
        super.uploadCalendarEntries(folderId, iCalDataAsXML);
        try {
            for (AttachmentPart part : AttachmentsHelper.getMessageAttachments()) {
                getIcalModule().parseToEntries(folderId, part.getDataHandler().getInputStream());
            }
        } catch (Exception e) {
            throw new RemotingException(e);
        }
    }
}
