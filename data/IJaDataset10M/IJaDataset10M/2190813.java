package com.fisoft.phucsinh.phucsinhsrv.service.group;

import com.fisoft.phucsinh.phucsinhsrv.service.common.RelationController;
import com.fisoft.phucsinh.phucsinhsrv.entity.*;
import java.util.Collection;

/**
 *
 * @author me
 */
public class Group_User_RelationController extends RelationController<GroupEntity, UserEntity> {

    public Group_User_RelationController(GroupEntity parentEntity, Collection<UserEntity> childrenToAdd, Collection<UserEntity> childrenToRemove) {
        super(parentEntity, childrenToAdd, childrenToRemove);
    }

    @Override
    protected Collection<UserEntity> getChildrenCollection() {
        return this.parentEntity.getUserEntityCollection();
    }

    @Override
    protected void setJoinProperty(UserEntity pChild) {
    }
}
