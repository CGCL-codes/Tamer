package ru.goldenforests.forum.faces;

import java.io.IOException;
import net.sf.hibernate.HibernateException;
import net.sf.hibernate.JDBCException;
import ru.goldenforests.forum.ForumMessage;
import ru.goldenforests.forum.AccessException;
import ru.goldenforests.forum.ForumPermission;
import ru.goldenforests.forum.beans.ConstForumPost;
import ru.goldenforests.forum.search.SearchIndex;
import ru.goldenforests.forum.util.PostUtils;

;

public class FollowUpFace extends AbstractForumFace {

    public static final long REQUIRED_PERMISSIONS = ForumPermission.FOLLOWUP;

    /** @return post associated with the message */
    public ConstForumPost postFollowUp(long parentId, ForumMessage message, SearchIndex searchIndex) throws IOException, HibernateException, PostUtils.InvalidForumException, AccessException {
        return PostUtils.postFollowUp(this, parentId, message, null, searchIndex);
    }
}
