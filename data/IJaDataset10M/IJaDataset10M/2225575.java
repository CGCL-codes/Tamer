package com.ideals.weavec.cpr.cprbuilding.cprtree;

import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: ByelasH
 * Date: 5-apr-2005
 * Time: 16:27:18
 * To change this template use File | Settings | File Templates.
 */
public class IfElseNode extends BranchNode {

    private String type = "IF-ELSE";

    public IfElseNode(Vector vec, int id, int iType) {
        super(vec, id, iType);
        branch = true;
    }

    public String getType() {
        return type;
    }
}
