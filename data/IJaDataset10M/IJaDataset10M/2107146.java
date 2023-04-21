package com.tysanclan.site.projectewok.pages.member.admin;

import com.tysanclan.site.projectewok.auth.TysanMemberSecured;
import com.tysanclan.site.projectewok.beans.RoleService;
import com.tysanclan.site.projectewok.entities.Role.RoleType;
import com.tysanclan.site.projectewok.entities.User;

@TysanMemberSecured
public class HeraldTransferPage extends AbstractRoleTransferPage {

    public HeraldTransferPage() {
        super(RoleType.HERALD);
    }

    @Override
    public User getRole(RoleService service) {
        return service.getHerald();
    }
}
