package com.avaje.ebean.server.deploy.jointree;

import com.avaje.ebean.server.deploy.BeanDescriptor;
import com.avaje.ebean.server.deploy.BeanPropertyAssocMany;
import com.avaje.ebean.server.deploy.BeanPropertyAssocOne;
import com.avaje.ebean.server.deploy.TableJoin;
import com.avaje.ebean.server.deploy.jointree.DeployPropertyFactory.DeployPropertyRequest;

/**
 * A JoinNode for associated many beans (1-M and M-M).
 */
public class JoinNodeList extends JoinNode {

    final BeanPropertyAssocMany manyProperty;

    final String extraWhere;

    /**
	 * Create for a given assoc many property.
	 */
    public JoinNodeList(JoinNode parent, TableJoin tableJoin, BeanDescriptor desc, String propertyPrefix, DeployPropertyRequest deployPropertyRequest, BeanPropertyAssocMany listProp) {
        super(Type.LIST, listProp.getName(), parent, tableJoin, desc, propertyPrefix, deployPropertyRequest);
        this.manyProperty = listProp;
        this.outerJoin = true;
        this.extraWhere = convertExtraWhere(manyProperty.getExtraWhere());
    }

    /**
	 * If true this is a join to a bean that has OneToMany Association.
	 */
    public boolean isManyJoin() {
        return true;
    }

    public String getExtraWhere() {
        return extraWhere;
    }

    /**
	 * Return the AssocMany BeanProperty.
	 */
    @Override
    public BeanPropertyAssocMany getManyProp() {
        return manyProperty;
    }

    @Override
    public BeanPropertyAssocOne getBeanProp() {
        return null;
    }

    protected void appendDescription(StringBuffer sb) {
        sb.append("list: ");
        sb.append(manyProperty.getTargetType().getName());
    }
}
