package edu.kit.cm.kitcampusguide.service.user;

import java.util.Collection;
import edu.kit.cm.kitcampusguide.model.Group;
import edu.kit.cm.kitcampusguide.model.Member;

public interface MemberService {

    Member getUser(Object uid);

    void saveUser(Member member);

    Collection<Group> getGroups();

    Group getGroup(Object uid);

    void saveGroup(Group group);
}
