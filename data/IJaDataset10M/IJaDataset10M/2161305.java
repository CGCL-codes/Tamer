package org.geonetwork.gaap.domain.web.response;

import org.geonetwork.gaap.domain.user.User;
import java.util.List;

/**
 * Get users response
 *
 * @author Jose
 */
public class GetUsersResponse extends BaseResponse {

    List<User> users;

    int nextPosition;

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getNextPosition() {
        return nextPosition;
    }

    public void setNextPosition(int nextPosition) {
        this.nextPosition = nextPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        if (o == null || getClass() != o.getClass()) return false;
        GetUsersResponse that = (GetUsersResponse) o;
        if (nextPosition != that.nextPosition) return false;
        if (users != null ? !users.equals(that.users) : that.users != null) return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (users != null ? users.hashCode() : 0);
        result = prime * result + nextPosition;
        return result;
    }
}
