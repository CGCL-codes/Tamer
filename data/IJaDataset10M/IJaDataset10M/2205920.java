package org.fb4j.groups;

import java.util.Set;
import org.fb4j.FacebookObject;

/**
 * @author Mino Togna
 * 
 */
public interface GroupMembers extends FacebookObject {

    Set<Long> getMembers();

    Set<Long> getAdmins();

    Set<Long> getOfficers();

    Set<Long> getNotReplied();
}
