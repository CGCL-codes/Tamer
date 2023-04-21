package at.ac.tuwien.infosys.www.pixy.conversion.nodes;

import java.util.Collections;
import java.util.List;
import at.ac.tuwien.infosys.www.phpparser.*;
import at.ac.tuwien.infosys.www.pixy.conversion.Variable;

public class CfgNodeExit extends CfgNode {

    public CfgNodeExit() {
        super();
    }

    public CfgNodeExit(ParseNode node) {
        super(node);
    }

    public List<Variable> getVariables() {
        return Collections.emptyList();
    }

    public void replaceVariable(int index, Variable replacement) {
    }
}
