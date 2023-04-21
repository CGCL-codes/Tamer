package su.msu.cs.lvk.xml2pixy.transform.pyvisitor;

import at.ac.tuwien.infosys.www.phpparser.ParseNode;
import at.ac.tuwien.infosys.www.phpparser.PhpSymbols;
import su.msu.cs.lvk.xml2pixy.ast.python.AssListNode;
import su.msu.cs.lvk.xml2pixy.ast.python.AssNameNode;
import su.msu.cs.lvk.xml2pixy.ast.python.PythonNode;
import su.msu.cs.lvk.xml2pixy.transform.astvisitor.VisitorException;

/**
 * User: klimov
 * Date: 19.01.2009
 */
public class AssListNodeVisitor extends PythonNodeVisitor {

    public void visit(PythonNode node) throws VisitorException {
        AssListNode assList = (AssListNode) node;
        ParseNode phpAssList = null;
        for (PythonNode child : assList.getNodes()) {
            if (child.getPhpNode() != null && (child instanceof AssListNode || child instanceof AssNameNode)) {
                ParseNode assListElem = helper.create(PhpSymbols.assignment_list_element);
                ParseNode tmpList = helper.create(PhpSymbols.assignment_list);
                if (phpAssList != null) {
                    helper.addChild(tmpList, phpAssList).addChild(helper.create(PhpSymbols.T_COMMA, ",", child.getLineno()));
                }
                phpAssList = tmpList;
                phpAssList.addChild(assListElem);
                if (child instanceof AssListNode) {
                    helper.addChild(assListElem, helper.create(PhpSymbols.T_LIST, "list", child.getLineno())).addChild(helper.create(PhpSymbols.T_OPEN_BRACES, "(", child.getLineno())).addChild(child.getPhpNode()).addChild(helper.create(PhpSymbols.T_CLOSE_BRACES, ")", child.getLineno()));
                } else {
                    assListElem.addChild(makeCvar(child.getPhpNode()));
                }
            }
        }
        if (phpAssList != null) {
            node.setPhpNode(phpAssList);
        }
    }
}
