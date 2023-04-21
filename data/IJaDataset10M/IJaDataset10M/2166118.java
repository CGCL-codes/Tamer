package uk.ac.lkl.server.objectify;

import com.googlecode.objectify.annotation.Cached;
import com.googlecode.objectify.annotation.Indexed;
import com.googlecode.objectify.annotation.Unindexed;

/**
 * @author Ken Kahn
 *
 */
@Unindexed
@Cached
public class TiedNumberValueEvent extends ServerEvent {

    @Indexed
    private int value;

    public TiedNumberValueEvent(String id, int value, String projectKey) {
        super(id, projectKey);
        this.value = value;
    }

    public TiedNumberValueEvent() {
    }

    public int getValue() {
        return value;
    }
}
