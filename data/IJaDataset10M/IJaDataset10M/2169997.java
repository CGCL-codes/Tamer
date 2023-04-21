package org.tm4j.topicmap.tmdm.basic;

import java.util.Set;
import java.util.Iterator;
import org.tm4j.net.Locator;
import org.tm4j.topicmap.tmdm.*;

/**
	@author <a href="mailto:xuan--2007.05--org.tm4j.topicmap.tmdm--tm4j.org@public.software.baldauf.org">Xuân Baldauf</a>
*/
public class BasicAssociation extends BasicScopeable implements Association {

    BasicTopicMap parent;

    BasicTopic type;

    Set<BasicAssociationRole> roles;

    protected BasicAssociation(BasicTopicMap parent, BasicTopic type, BasicScope scope) {
        super(scope);
        this.parent = parent;
        this.type = type;
        this.roles = createSet();
    }

    protected BasicTopicMap getContainingTopicMap() {
        return getParent();
    }

    public BasicTopicMap getParent() {
        return parent;
    }

    @TMDM
    @TMAPI
    public void setType(Topic type) {
        setType((BasicTopic) type);
    }

    public void setType(BasicTopic type) {
        assert type.getContainingTopicMap() == getContainingTopicMap();
        BasicTopic oldType = this.type;
        this.type = type;
        getEventListener().notifyAssociationTypeChanged(getContainingTopicMap(), this, oldType, type);
    }

    @TMDM
    @TMAPI
    public BasicTopic getType() {
        return type;
    }

    /**
		Warning: player and type are reversed here.
	*/
    @TMAPI
    public BasicAssociationRole createAssociationRole(Topic player, Topic type) {
        return createRole((BasicTopic) type, (BasicTopic) player);
    }

    @TMDM
    public BasicAssociationRole createRole(Topic type, Topic player) {
        return createRole((BasicTopic) type, (BasicTopic) player);
    }

    public BasicAssociationRole createRole(BasicTopic type, BasicTopic player) {
        assert type.getContainingTopicMap() == getContainingTopicMap();
        assert player.getContainingTopicMap() == getContainingTopicMap() : player + ".getContainingTopicMap()=" + player.getContainingTopicMap() + " while " + this + ".getContainingTopicMap()=" + getContainingTopicMap() + ".";
        BasicAssociationRole role = new BasicAssociationRole(this, type, player);
        roles.add(role);
        getEventListener().notifyAssociationRoleCreated(getContainingTopicMap(), this, role);
        return role;
    }

    @TMDM
    public Set<? extends AssociationRole> getRoles() {
        return maybeWrapUnmodifiable(roles);
    }

    @TMAPI
    public Set<? extends AssociationRole> getAssociationRoles() {
        return getRoles();
    }

    protected void internalRemove(BasicAssociationRole role) {
        boolean success = roles.remove(role);
        assert success;
        getEventListener().notifyAssociationRoleRemoved(getContainingTopicMap(), this, role);
    }

    public void removeAllAssociationRoles() {
        if (roles != null) {
            Iterator<BasicAssociationRole> i = roles.iterator();
            while (i.hasNext()) {
                BasicAssociationRole role = i.next();
                role.internalPreRemove();
                i.remove();
                getEventListener().notifyAssociationRoleRemoved(getContainingTopicMap(), this, role);
                role.internalPostRemove();
            }
        }
    }

    /**
		Remove as much as possible without conflicting with invariants after this call. 
	*/
    protected void internalPreRemove() {
        removeAllAssociationRoles();
        super.internalPreRemove();
    }

    protected void removeAtParent() {
        getParent().internalRemove(this);
    }

    public String toString() {
        return "BasicAssociation[type=" + type + ",roles=" + roles + "]";
    }
}
