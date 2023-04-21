package org.openuss.discussion;

import java.io.Serializable;
import java.util.Comparator;

/**
 * @author Sebastian Roekens
 *
 */
public class PostInfoComparator implements Comparator<PostInfo>, Serializable {

    private static final long serialVersionUID = 7539148325827130253L;

    /**
	 * compares dates of 2 PostInfo objects
	 */
    public int compare(PostInfo postInfo1, PostInfo postInfo2) {
        if (postInfo1 == null || postInfo1.getCreated() == null) {
            return 0;
        }
        if (postInfo2 == null || postInfo2.getCreated() == null) {
            return 0;
        }
        return (postInfo1.getCreated().compareTo(postInfo2.getCreated()));
    }
}
