package net.sourceforge.cruisecontrol.dashboard;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class ModificationKey {

    private final String comment;

    private final String user;

    private static final int HASH_SEED = 31;

    public ModificationKey(String comment, String user) {
        this.comment = comment;
        this.user = user;
    }

    public String getComment() {
        return comment;
    }

    public String getUser() {
        return user;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ModificationKey other = (ModificationKey) o;
        return (StringUtils.equals(comment, other.getComment())) && StringUtils.equals(user, other.getUser());
    }

    public int hashCode() {
        int result;
        result = (comment != null ? comment.hashCode() : 0);
        result = HASH_SEED * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
