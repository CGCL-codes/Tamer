package edu.kit.cm.kitcampusguide.service.user;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import edu.kit.cm.kitcampusguide.model.Group;
import edu.kit.cm.kitcampusguide.model.Member;
import edu.kit.cm.kitcampusguide.model.MemberToGroupMapping;

/**
 * User details containing Member information. Thus provides access to the
 * groups.
 */
public class MemberUserDetails extends Member implements UserDetails {

    private static final long serialVersionUID = 501915223740136933L;

    private Collection<? extends GrantedAuthority> authorities = null;

    public MemberUserDetails(Member member, Collection<? extends GrantedAuthority> collection) {
        super(member);
        this.authorities = collection;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getUsername() {
        return this.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public boolean hasAdminRoleFor(Group group) {
        return hasAdminRoleForGroupWithId(group.getId());
    }

    public boolean hasUserRoleFor(Group group) {
        return hasUserRoleForGroupWithId(group.getId());
    }

    public boolean hasAdminRoleForGroupWithId(String groupId) {
        return getRoleOfUserInGroupWithId(groupId).equalsIgnoreCase(MemberToGroupMapping.ROLES.ADMIN.toString());
    }

    public boolean hasUserRoleForGroupWithId(String groupId) {
        return getRoleOfUserInGroupWithId(groupId).equalsIgnoreCase(MemberToGroupMapping.ROLES.USER.toString());
    }

    public String getRoleOfUserInGroupWithId(String groupIdToGetRoleFor) {
        String roleOfUserInGroup = "";
        for (MemberToGroupMapping mapping : getGroupMappings()) {
            if (mapping.getGroup().getId().equals(groupIdToGetRoleFor)) {
                roleOfUserInGroup = mapping.getRole();
                break;
            }
        }
        return roleOfUserInGroup;
    }
}
