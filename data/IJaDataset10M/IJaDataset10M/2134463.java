package org.xblackcat.rojac.service.janus.data;

import org.xblackcat.rojac.data.Version;
import ru.rsdn.Janus.JanusMessageInfo;
import ru.rsdn.Janus.JanusModerateInfo;
import ru.rsdn.Janus.JanusRatingInfo;

/**
 * @author ASUS
 */
public class NewData extends TopicMessages {

    private final int ownUserId;

    private final Version forumRowVersion;

    private final Version ratingRowVersion;

    private final Version moderateRowVersion;

    public NewData(int ownUserId, Version forumRowVersion, Version ratingRowVersion, Version moderateRowVersion, JanusMessageInfo[] mes, JanusModerateInfo[] mod, JanusRatingInfo[] r) {
        super(mes, mod, r);
        this.ownUserId = ownUserId;
        this.forumRowVersion = forumRowVersion;
        this.ratingRowVersion = ratingRowVersion;
        this.moderateRowVersion = moderateRowVersion;
    }

    public int getOwnUserId() {
        return ownUserId;
    }

    public Version getForumRowVersion() {
        return forumRowVersion;
    }

    public Version getRatingRowVersion() {
        return ratingRowVersion;
    }

    public Version getModerateRowVersion() {
        return moderateRowVersion;
    }
}
