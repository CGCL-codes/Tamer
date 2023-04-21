package org.vosao.business.impl.mq.subscriber;

import java.util.Set;
import org.vosao.business.impl.mq.AbstractSubscriber;
import org.vosao.business.mq.Message;
import org.vosao.business.mq.message.PageMessage;
import org.vosao.common.VosaoContext;
import org.vosao.entity.PageEntity;
import org.vosao.entity.helper.UserHelper;

/**
 * Reindex changed pages.
 * 
 * @author Alexander Oleynik
 *
 */
public class IndexChangedPages extends AbstractSubscriber {

    public void onMessage(Message message) {
        PageMessage msg = (PageMessage) message;
        try {
            VosaoContext.getInstance().setUser(UserHelper.ADMIN);
            for (Set<Long> pages : msg.getPages().values()) {
                for (Long pageId : pages) {
                    PageEntity page = getDao().getPageDao().getById(pageId);
                    if (page != null) {
                        getBusiness().getSearchEngine().updateIndex(page.getId());
                    }
                }
            }
            getBusiness().getSearchEngine().saveIndex();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
