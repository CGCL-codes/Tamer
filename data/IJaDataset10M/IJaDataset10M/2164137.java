package com.poltman.dscentral.communities;

import org.apache.log4j.Logger;
import org.zkforge.ckez.CKeditor;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Messagebox;
import com.poltman.dspace.db.service.dspace.CommunityService;
import com.poltman.zk.common.Context;
import com.poltman.zk.common.dspace.Communities;

/**
 * 
 * @author z.ciok@poltman.com
 *
 */
public class SaveCommunityCopyrightText extends SelectorComposer<CKeditor> {

    private static final long serialVersionUID = -2797640356249044270L;

    private static Logger log = Logger.getLogger(SaveCommunityCopyrightText.class);

    @Wire
    Intbox communityIdIntbox;

    @Wire
    CKeditor edCopyrightText;

    /**
	 * 
	 */
    @Listen("onSave = ckeditor#edCopyrightText")
    public void saveCopyrightText() {
        try {
            if (communityIdIntbox.getValue() == null) {
                Messagebox.show("Select community.", "DSCentral", 0, Messagebox.ERROR);
                return;
            } else {
                CommunityService communityService = (CommunityService) SpringUtil.getBean("communityService");
                if ((edCopyrightText.getValue() == null) || (edCopyrightText.getValue().isEmpty())) {
                    Messagebox.show("Copyright Text can not be null.", "DSCentral", 0, Messagebox.ERROR);
                    return;
                } else {
                    communityService.setFixedCopyrightTextFor(edCopyrightText.getValue(), communityIdIntbox.getValue());
                }
            }
        } catch (Exception e) {
            log.error("Error: saveCopyrightText; ", e);
            Messagebox.show("Copyright Text save error...", "DSCentral", 0, Messagebox.ERROR);
            return;
        }
        Context context = (Context) SpringUtil.getBean("context");
        Communities communities = (Communities) context.getAttribute("Communities");
        try {
            CommunityUtils.refreshCommunityTree(communities);
        } catch (Exception e) {
            log.error("saveCommunityCopyrightText (refresh tree) ", e);
        }
        Messagebox.show("Copyright Text has been saved...", "DSCentral", 0, Messagebox.INFORMATION);
    }
}
