package org.tolven.core;

import java.util.Date;
import java.util.List;
import org.tolven.core.entity.Notice;

/**
 * Services to manage system-side notices, typically displayed on a login page.
 */
public interface NoticeDAOLocal {

    /**
     * Create a new notice.
     * @param strNotice, bActive
     * @return A new Notice object
     */
    public Notice createNotice(String strNotice, Date dateShowFrom, Date dateShowTo, Date dateEffectiveDate, boolean bActive);

    public void updateNotice(Notice notice);

    /**
     * Find a notice for the given noticeid.
     * @param noticeId
     * @return the notice object
     */
    public Notice findNotice(long noticeId);

    public List<Notice> findActiveNotices();
}
