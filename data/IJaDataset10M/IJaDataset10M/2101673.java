package su.msu.cs.lvk.xml2pixy.transform.astvisitor;

import org.jdom.Element;
import su.msu.cs.lvk.xml2pixy.transform.Node;
import su.msu.cs.lvk.xml2pixy.transform.SymbolTable;
import su.msu.cs.lvk.xml2pixy.transform.function.Function;
import su.msu.cs.lvk.xml2pixy.transform.function.FunctionManager;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gaklimov
 * Date: 13.11.2007
 * Time: 15:00:43
 */
public class FunctionVisitor extends ASTVisitor {

    public FunctionVisitor(SymbolTable symbolTable) {
        super(symbolTable);
    }

    public FunctionVisitor() {
        super();
    }

    public void visit(Node node, String currentFile, int lineno, String module) throws VisitorException {
        Node code = getFirstChild(getFirstChild(node, "code"), null);
        if (code.getParseNode() != null) {
            Element from = node.getJdomElement().getParentElement().getParentElement().getParentElement().getParentElement();
            if ("Class".equals(from.getName())) {
                return;
            }
            Function function = new Function();
            String params = getFirstChild(node, "argnames").getJdomElement().getTextTrim();
            String origName = node.getJdomElement().getAttributeValue("name");
            String name = module.replaceAll("\\.", "__") + "__" + origName;
            String[] args = params.split(",");
            for (int i = 0; i < args.length; ++i) {
                if (!"".equals(args[i])) {
                    args[i] = "&" + args[i];
                }
            }
            function.setArguments(args);
            function.setSource(node);
            function.setCurrentFile(currentFile);
            function.setBody(code);
            function.setModule(module);
            function.setOriginalName(origName);
            function.setName(name);
            List<Node> defaults = getFirstChild(node, "defaults").getChildren(null);
            for (int i = 0; i < defaults.size(); i++) {
                String argName = function.getArguments()[function.getArguments().length - 1 - i];
                Node def = defaults.get(defaults.size() - 1 - i);
                function.getDefaults().put(argName, def);
            }
            FunctionManager.getInstance().addFunction(name, function);
        }
    }
}
