package com.ideals.weavec.cpr.cprbuilding.cprautomata;

import com.ideals.weavec.cpr.cprbuilding.cprtree.CPRNodeFactory;

/**
 * Created by IntelliJ IDEA.
 * User: ByelasH
 * Date: 6-apr-2005
 * Time: 14:29:19
 * To change this template use File | Settings | File Templates.
 */
public class ForPushDown extends CPRPushDown {

    public ForPushDown(CPRPushDown cprPushDown) {
        super(cprPushDown);
    }

    public boolean process() throws Exception {
        if (current.isVisited()) {
            current = findNewCurrent();
            if (current != null) return false;
            return true;
        }
        create(CPRNodeFactory.FOR_NODE);
        setCurrentBranchSituation();
        return false;
    }
}
