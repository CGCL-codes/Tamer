package com.completex.objective.components.persistency.test.oracle.gen;

import com.completex.objective.components.persistency.*;
import java.sql.SQLException;

public class CpxTestSlave extends com.completex.objective.components.persistency.test.oracle.gen.AliasTestSlave implements ICpxTestSlave, java.io.Externalizable {

    public CpxTestSlave() {
    }

    public CpxTestSlave(Long aliasTestMasterId, Long aliasTestSlaveId) {
        super(aliasTestMasterId, aliasTestSlaveId);
    }

    public static final String CPX_CHILD_MASTER2 = "master2";

    private ICpxTestMaster2 master2;

    protected static final int CPX_INDEX_MASTER2 = 0;

    public boolean complex() {
        return true;
    }

    protected Link[] links(QueryFactory queryFactory) {
        try {
            Link[] links = new Link[1];
            Query query;
            Link link;
            query = queryFactory.newQuery((PersistentObject) cpxFactoryMaster2());
            link = new ChainedLink(query, new int[] { CpxTestSlave.ICOL_ALIAS_TEST_MASTER2_ID }, new int[] { CpxTestMaster2.ICOL_ALIAS_TEST_MASTER_ID }, CPX_CHILD_MASTER2);
            link.setLazyRetrieval(true);
            link.setCascadeInsert(true);
            link.setCascadeUpdate(true);
            link.setCascadeDelete(false);
            link.setInsertBeforeParent(true);
            links[CPX_INDEX_MASTER2] = link;
            return links;
        } catch (Exception e) {
            throw new OdalRuntimePersistencyException(e);
        }
    }

    /**
     *
     * @return Child factory
     */
    protected CpxTestMaster2 cpxFactoryMaster2() {
        return new CpxTestMaster2();
    }

    public ICpxTestMaster2 getMaster2() {
        return (this.master2 = (ICpxTestMaster2) getFirstChildObject(CPX_CHILD_MASTER2, this.master2));
    }

    public ICpxTestMaster2 _getMaster2() {
        return (this.master2 = (ICpxTestMaster2) getFirstChildObjectStraight(CPX_CHILD_MASTER2, this.master2));
    }

    public void setMaster2(ICpxTestMaster2 master2) {
        this.master2 = (ICpxTestMaster2) setChildObjectSafe(CPX_CHILD_MASTER2, master2);
    }

    protected void doUnflatten() {
        unflatten(toLink(), CPX_CHILD_MASTER2, (PersistentObject) master2);
    }

    protected void doFlatten() {
        flatten((PersistentObject) master2);
    }

    protected void doPreFlatten() {
        _getMaster2();
    }

    public boolean compound() {
        return false;
    }
}
