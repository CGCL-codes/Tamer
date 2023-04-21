package com.ideals.weavec.cpr.cprbuilding.cprtree;

import com.ideals.weavec.codeanalysis.cfg.IntraCFNode;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: ByelasH
 * Date: 31-mrt-2005
 * Time: 15:27:10
 * To change this template use File | Settings | File Templates.
 */
public class IfNode extends BranchNode {

    private String type = "IF";

    public IfNode(Vector vec, int id, int iType) {
        super(vec, id, iType);
        branch = true;
    }

    public String getType() {
        return type;
    }
}
