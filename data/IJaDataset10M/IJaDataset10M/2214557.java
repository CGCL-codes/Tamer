package com.restfb.types;

import static com.restfb.util.DateUtils.toDateFromLongFormat;
import java.util.Date;
import com.restfb.Facebook;

/**
 * Represents the <a
 * href="http://developers.facebook.com/docs/reference/api/event">Comment Graph
 * API type</a>.
 * 
 * @author <a href="http://restfb.com">Mark Allen</a>
 * @since 1.5
 */
public class Comment extends FacebookType {

    @Facebook
    private NamedFacebookType from;

    @Facebook
    private String message;

    @Facebook("created_time")
    private String createdTime;

    @Facebook
    private Long likes;

    private static final long serialVersionUID = 1L;

    /**
   * User who posted the comment.
   * 
   * @return User who posted the comment.
   */
    public NamedFacebookType getFrom() {
        return from;
    }

    /**
   * Text contents of the comment.
   * 
   * @return Text contents of the comment.
   */
    public String getMessage() {
        return message;
    }

    /**
   * Date on which the comment was created.
   * 
   * @return Date on which the comment was created.
   */
    public Date getCreatedTime() {
        return toDateFromLongFormat(createdTime);
    }

    /**
   * The number of likes on this comment.
   * 
   * @return The number of likes on this comment.
   */
    public Long getLikes() {
        return likes;
    }
}
