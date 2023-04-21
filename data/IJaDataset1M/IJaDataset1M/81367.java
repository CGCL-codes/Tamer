package org.jabusuite.webclient.cms.news;

import java.io.Serializable;
import org.jabusuite.cms.news.CmsNewsEntry;
import org.jabusuite.webclient.dataediting.FmEditJbsBaseObject;
import org.jabusuite.webclient.main.JbsL10N;

/**
 *
 * @author hilwers
 */
public class FmCmsNewsEntryEdit extends FmEditJbsBaseObject implements Serializable {

    private static final long serialVersionUID = 7527491406050754768L;

    public FmCmsNewsEntryEdit() {
        super(JbsL10N.getString("CmsNewsEntry.formTitle"));
        this.setPnEditJbsObject(new PnCmsNewsEntryEdit());
    }

    @Override
    public void createJbsBaseObject() {
        this.setJbsBaseObject(new CmsNewsEntry());
    }
}
