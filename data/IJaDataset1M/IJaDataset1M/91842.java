package su.msu.cs.lvk.xml2pixy.transform.astvisitor;

import at.ac.tuwien.infosys.www.phpparser.ParseNode;
import at.ac.tuwien.infosys.www.phpparser.PhpSymbols;
import at.ac.tuwien.infosys.www.pixy.MyOptions;
import org.apache.log4j.Logger;
import org.jdom.Attribute;
import org.jdom.DataConversionException;
import org.jdom.Element;
import su.msu.cs.lvk.xml2pixy.Converter;
import su.msu.cs.lvk.xml2pixy.Utils;
import su.msu.cs.lvk.xml2pixy.jdom.ModuleHandler;
import su.msu.cs.lvk.xml2pixy.parser.ParseNodeHelper;
import su.msu.cs.lvk.xml2pixy.simple.classes.ClassUtils;
import su.msu.cs.lvk.xml2pixy.transform.Node;
import su.msu.cs.lvk.xml2pixy.transform.Symbol;
import su.msu.cs.lvk.xml2pixy.transform.SymbolTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Base transformation vistitor for ast nodes.
 * Contains lots of helper methods for parsenode building.
 */
public class ASTVisitor {

    protected static Logger log = Logger.getLogger(ASTVisitor.class.getName());

    protected ParseNodeHelper helper;

    protected SymbolTable symbolTable;

    public ASTVisitor() {
        this(new SymbolTable());
    }

    public ASTVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.helper = new ParseNodeHelper();
    }

    public void setCurrentFile(String file) {
        this.helper.setCurrentFile(file);
    }

    public void visit(Node node, String currentFile1, int lineno, String module) throws VisitorException {
        String nodeName = node.getJdomElement().getName();
        String currentFile = helper.getCurrentFile();
        if (Character.isUpperCase(node.getJdomElement().getName().charAt(0))) {
            if (currentFile.length() > 4 && ".xml".equals(currentFile.substring(currentFile.length() - 4, currentFile.length()))) {
                String file = currentFile.substring(0, currentFile.length() - 4);
                log.warn("WARNING: Node " + nodeName + " is currently unsupported (line: " + file + ':' + lineno + ")");
            } else {
                log.warn("WARNING: Node " + nodeName + " is currently unsupported (line: " + currentFile + ':' + lineno + ")");
            }
        }
    }

    protected Node getFirstChild(Node node, String name) {
        if (node == null) return null;
        List<Node> children = node.getChildren(name);
        return children.size() > 0 ? children.get(0) : null;
    }

    protected int getLineno(Element elem) {
        Attribute lineno = elem.getAttribute("lineno");
        try {
            return lineno == null ? -1 : lineno.getIntValue();
        } catch (DataConversionException e) {
            return -1;
        }
    }

    protected Integer getInteger(Element elem, String name) {
        try {
            Attribute attr = elem.getAttribute(name);
            return (attr != null) ? attr.getIntValue() : null;
        } catch (DataConversionException e) {
            return null;
        }
    }

    protected Double getDouble(Element elem, String name) {
        try {
            Attribute attr = elem.getAttribute(name);
            return (attr != null) ? elem.getAttribute(name).getDoubleValue() : null;
        } catch (DataConversionException e) {
            return null;
        }
    }

    protected ParseNode makeBinary(Node node, int lineno) throws VisitorException {
        Node leftNode = getFirstChild(node, "left"), rightNode = getFirstChild(node, "right");
        Node leftChild = getFirstChild(leftNode, null), rightChild = getFirstChild(rightNode, null);
        if (leftChild.getParseNode() != null && rightChild.getParseNode() != null) {
            ParseNode left = makeExprInBraces(leftChild.getParseNode());
            ParseNode right = makeExprInBraces(rightChild.getParseNode());
            ParseNode expr_wo_var = helper.create(PhpSymbols.expr_without_variable, left, getBinaryOperation(node.getJdomElement().getName(), lineno), right);
            return helper.create(PhpSymbols.expr, expr_wo_var);
        }
        return null;
    }

    protected ParseNode getBinaryOperation(String name, int lineno) throws VisitorException {
        int symbol;
        String lexeme;
        if (name.equals("Add")) {
            symbol = PhpSymbols.T_PLUS;
            lexeme = "+";
        } else if (name.equals("Sub")) {
            symbol = PhpSymbols.T_MINUS;
            lexeme = "-";
        } else if (name.equals("Mul")) {
            symbol = PhpSymbols.T_MULT;
            lexeme = "*";
        } else if (name.equals("Div")) {
            symbol = PhpSymbols.T_DIV;
            lexeme = "/";
        } else if (name.equals("Mod")) {
            symbol = PhpSymbols.T_MODULO;
            lexeme = "%";
        } else if (name.equals("RightShift")) {
            symbol = PhpSymbols.T_SR;
            lexeme = ">>";
        } else if (name.equals("LeftShift")) {
            symbol = PhpSymbols.T_SL;
            lexeme = "<<";
        } else {
            throw new VisitorException("Unknown binary operation \"" + name + "\" at " + helper.getCurrentFile() + ":" + lineno);
        }
        return helper.create(symbol, lexeme, lineno);
    }

    protected ParseNode makeUnary(Node node, int lineno) throws VisitorException {
        Node exprNode = getFirstChild(node, "expr");
        Node exprChild = getFirstChild(exprNode, null);
        ParseNode expression = (exprChild != null && exprChild.getParseNode() != null) ? exprChild.getParseNode() : null;
        if (expression != null) {
            ParseNode expr_wo_var = helper.create(PhpSymbols.expr_without_variable, getUnaryOperation(node.getJdomElement().getName(), lineno), makeExprInBraces(expression));
            return helper.create(PhpSymbols.expr, expr_wo_var);
        }
        return null;
    }

    protected ParseNode makeNumberExpr(int value, int lineno) {
        return helper.createChain(new int[] { PhpSymbols.expr, PhpSymbols.expr_without_variable, PhpSymbols.scalar, PhpSymbols.common_scalar }, helper.create(PhpSymbols.T_LNUMBER, String.valueOf(value), lineno));
    }

    protected ParseNode getUnaryOperation(String name, int lineno) throws VisitorException {
        int symbol;
        String lexeme;
        if (name.equals("UnaryAdd")) {
            return null;
        } else if (name.equals("UnarySub")) {
            symbol = PhpSymbols.T_MINUS;
            lexeme = "-";
        } else if (name.equals("Not")) {
            symbol = PhpSymbols.T_NOT;
            lexeme = "!";
        } else if (name.equals("Invert")) {
            symbol = PhpSymbols.T_BITWISE_NOT;
            lexeme = "~";
        } else {
            throw new VisitorException("Unknown unary operation \"" + name + "\" at " + helper.getCurrentFile() + ":" + lineno);
        }
        return helper.create(symbol, lexeme, lineno);
    }

    protected ParseNode makeNary(Node node, int lineno) throws VisitorException {
        Node nodes = getFirstChild(node, "nodes");
        boolean ready = true;
        for (Node child : nodes.getChildren()) {
            if (child.getParseNode() == null) {
                ready = false;
                break;
            }
        }
        if (ready) {
            ParseNode root = null;
            for (Node child : nodes.getChildren()) {
                if (root == null) {
                    root = child.getParseNode();
                } else {
                    root = helper.create(PhpSymbols.expr, helper.create(PhpSymbols.expr_without_variable, makeExprInBraces(root), getNaryOperation(node.getJdomElement().getName(), lineno), makeExprInBraces(child.getParseNode())));
                }
            }
            return root;
        }
        return null;
    }

    protected ParseNode getNaryOperation(String name, int lineno) throws VisitorException {
        int symbol;
        String lexeme;
        if (name.equals("And")) {
            symbol = PhpSymbols.T_BOOLEAN_AND;
            lexeme = "&&";
        } else if (name.equals("Or")) {
            symbol = PhpSymbols.T_BOOLEAN_OR;
            lexeme = "||";
        } else if (name.equals("Bitand")) {
            symbol = PhpSymbols.T_BITWISE_AND;
            lexeme = "&";
        } else if (name.equals("Bitor")) {
            symbol = PhpSymbols.T_BITWISE_OR;
            lexeme = "|";
        } else if (name.equals("Bitxor")) {
            symbol = PhpSymbols.T_BITWISE_XOR;
            lexeme = "^";
        } else {
            throw new VisitorException("Unknown binary operation \"" + name + "\" at " + helper.getCurrentFile() + ":" + lineno);
        }
        return helper.create(symbol, lexeme, lineno);
    }

    protected ParseNode makeExprInBraces(ParseNode expression) {
        int linenoLeft = Utils.getLinenoLeft(expression);
        int linenoRight = Utils.getLinenoRight(expression, linenoLeft);
        return helper.create(PhpSymbols.expr, helper.create(PhpSymbols.expr_without_variable, helper.create(PhpSymbols.T_OPEN_BRACES, "(", linenoLeft), makeExpr(expression), helper.create(PhpSymbols.T_CLOSE_BRACES, ")", linenoRight)));
    }

    protected ParseNode makeCompare(String operation, int lineno) {
        int symbol;
        String lexeme;
        if (Utils.isBlank(operation)) return makeEpsilon(); else if (operation.equals("==")) {
            symbol = PhpSymbols.T_IS_EQUAL;
            lexeme = "==";
        } else if (operation.equals(">")) {
            symbol = PhpSymbols.T_IS_GREATER;
            lexeme = ">";
        } else if (operation.equals(">=")) {
            symbol = PhpSymbols.T_IS_GREATER_OR_EQUAL;
            lexeme = ">=";
        } else if (operation.equals("!=")) {
            symbol = PhpSymbols.T_IS_NOT_EQUAL;
            lexeme = "!=";
        } else if (operation.equals("<")) {
            symbol = PhpSymbols.T_IS_SMALLER;
            lexeme = "<";
        } else if (operation.equals("<=")) {
            symbol = PhpSymbols.T_IS_SMALLER_OR_EQUAL;
            lexeme = "<=";
        } else if (operation.equals("is")) {
            symbol = PhpSymbols.T_IS_IDENTICAL;
            lexeme = "===";
        } else if (operation.equals("is not")) {
            symbol = PhpSymbols.T_IS_NOT_IDENTICAL;
            lexeme = "!==";
        } else {
            return null;
        }
        return helper.create(symbol, lexeme, lineno);
    }

    protected ParseNode top2innerStatement(ParseNode node) {
        if (node.getSymbol() != PhpSymbols.top_statement_list) {
            return node;
        } else {
            List<ParseNode> topNodes = new ArrayList<ParseNode>();
            ParseNode current = node;
            while (current.getSymbol() == PhpSymbols.top_statement_list) {
                topNodes.add(current);
                current = current.getChild(0);
                if (current != node && current.getParent() == null) {
                    throw new RuntimeException("No parent");
                }
            }
            Collections.reverse(topNodes);
            ParseNode prevNode = current;
            for (ParseNode topNode : topNodes) {
                ParseNode newNode = helper.create(PhpSymbols.inner_statement_list, prevNode);
                ParseNode stmt = topNode.getChildren().size() > 1 ? topNode.getChild(1) : null;
                if (stmt != null) {
                    ParseNode[] children = new ParseNode[stmt.getNumChildren()];
                    stmt.getChildren().toArray(children);
                    ParseNode newStmt = helper.create(PhpSymbols.inner_statement, children);
                    newNode.addChild(newStmt);
                }
                prevNode = newNode;
            }
            return prevNode;
        }
    }

    protected ParseNode makeInnerStatementListInBraces(ParseNode stmt, int lineno) {
        int linenoRight = Utils.getLinenoRight(stmt, lineno);
        return helper.create(PhpSymbols.statement, helper.create(PhpSymbols.unticked_statement, helper.create(PhpSymbols.T_OPEN_CURLY_BRACES, "{", lineno), stmt, helper.create(PhpSymbols.T_CLOSE_CURLY_BRACES, "}", linenoRight)));
    }

    protected ParseNode makeAugAssign(String op, int lineno) {
        int symbol;
        String lexeme;
        if (op.equals("+=")) {
            symbol = PhpSymbols.T_PLUS_EQUAL;
            lexeme = "+=";
        } else if (op.equals("-=")) {
            symbol = PhpSymbols.T_MINUS_EQUAL;
            lexeme = "-=";
        } else if (op.equals("*=")) {
            symbol = PhpSymbols.T_MUL_EQUAL;
            lexeme = "*=";
        } else if (op.equals("/=")) {
            symbol = PhpSymbols.T_DIV_EQUAL;
            lexeme = "/=";
        } else if (op.equals("%=")) {
            symbol = PhpSymbols.T_MOD_EQUAL;
            lexeme = "%=";
        } else if (op.equals("&=")) {
            symbol = PhpSymbols.T_AND_EQUAL;
            lexeme = "&=";
        } else if (op.equals("|=")) {
            symbol = PhpSymbols.T_OR_EQUAL;
            lexeme = "|=";
        } else if (op.equals("^=")) {
            symbol = PhpSymbols.T_XOR_EQUAL;
            lexeme = "^=";
        } else if (op.equals(">>=")) {
            symbol = PhpSymbols.T_SR_EQUAL;
            lexeme = ">>=";
        } else if (op.equals("<<=")) {
            symbol = PhpSymbols.T_SL_EQUAL;
            lexeme = "<<=";
        } else {
            return makeEpsilon();
        }
        return helper.create(symbol, lexeme, lineno);
    }

    protected ParseNode makeExpr(ParseNode cvar) {
        cvar = makeCvar(cvar);
        if (cvar.getSymbol() == PhpSymbols.cvar) {
            return helper.create(PhpSymbols.expr, helper.create(PhpSymbols.r_cvar, cvar));
        } else if (cvar.getSymbol() == PhpSymbols.expr_without_variable) {
            return helper.create(PhpSymbols.expr, cvar);
        } else {
            return cvar;
        }
    }

    protected ParseNode makeReferenceVariable(ParseNode tString) {
        if (tString.isToken()) {
            String lexeme = tString.getLexeme();
            if (!lexeme.startsWith("$")) lexeme = '$' + lexeme;
            return helper.create(PhpSymbols.reference_variable, helper.create(PhpSymbols.compound_variable, helper.create(PhpSymbols.T_VARIABLE, lexeme, tString.getLineno())));
        }
        return tString;
    }

    protected ParseNode makeReferenceVariableByName(String name, int lineno) {
        return makeReferenceVariable(helper.create(PhpSymbols.T_VARIABLE, "$" + name, lineno));
    }

    protected ParseNode makeCvar(ParseNode reference_variable) {
        reference_variable = makeReferenceVariable(reference_variable);
        if (reference_variable.getSymbol() == PhpSymbols.reference_variable) {
            return helper.create(PhpSymbols.cvar, helper.create(PhpSymbols.cvar_without_objects, reference_variable));
        } else {
            return reference_variable;
        }
    }

    protected ParseNode removeSemicolon(ParseNode node) {
        if (node.getSymbol() != PhpSymbols.top_statement_list) return node; else {
            return null;
        }
    }

    public static void reset() {
        if (System.getProperty("pixy.home") == null) {
            System.setProperty("pixy.home", ".");
        }
        Converter.readModulesConfig(MyOptions.pixy_home + "/" + MyOptions.configDir + "/" + "modules.xml");
        ImportVisitor.imported = new ArrayList<String>();
        for (Object obj : Converter.modulesConfig.getRootElement().getChildren("Module")) {
            Element module = (Element) obj;
            ImportVisitor.imported.add(module.getAttributeValue("name"));
        }
        ImportVisitor.builtins = ImportVisitor.imported.size();
        ModuleHandler.init();
        ClassUtils.init();
    }

    public ParseNode makeEpsilon() {
        return helper.create(PhpSymbols.T_EPSILON, "epsilon", -2);
    }

    protected ParseNode renderStringConstant(int lineno, String value) {
        return helper.create(PhpSymbols.expr, helper.create(PhpSymbols.expr_without_variable, helper.create(PhpSymbols.scalar, helper.create(PhpSymbols.common_scalar, helper.create(PhpSymbols.T_CONSTANT_ENCAPSED_STRING, '"' + value + '"', lineno)))));
    }

    protected ParseNode emptyStatementList() {
        return helper.create(PhpSymbols.top_statement_list, makeEpsilon());
    }

    protected ParseNode makeFunctionCall(String function, ParseNode[] args, ParseNode firstArg, int lineno) {
        ParseNode function_call_parameter_list = makeFunctionCallParameterList(args, lineno, firstArg);
        int linenoRight = Utils.getLinenoRight(function_call_parameter_list, lineno);
        ParseNode function_call = helper.create(PhpSymbols.function_call, helper.create(PhpSymbols.T_STRING, function, lineno), helper.create(PhpSymbols.T_OPEN_BRACES, "(", lineno), function_call_parameter_list, helper.create(PhpSymbols.T_CLOSE_BRACES, ")", linenoRight));
        return helper.create(PhpSymbols.expr, helper.create(PhpSymbols.expr_without_variable, function_call));
    }

    protected ParseNode addFirstTopStatement(ParseNode where, ParseNode what) {
        if (where.getSymbol() != PhpSymbols.top_statement_list || (what.getSymbol() != PhpSymbols.statement && what.getSymbol() != PhpSymbols.declaration_statement)) {
            return where;
        }
        ParseNode top_statement_list = helper.create(PhpSymbols.top_statement_list);
        ParseNode top_statement = helper.create(PhpSymbols.top_statement);
        if (where.getNumChildren() == 1) {
            top_statement_list.addChild(where);
            top_statement_list.addChild(top_statement);
            top_statement.addChild(what);
            return top_statement_list;
        } else if (where.getNumChildren() == 2) {
            ParseNode dest = where;
            while (where.getSymbol() == PhpSymbols.top_statement_list) {
                where = where.getChild(0);
            }
            where = where.getParent().getParent();
            top_statement_list.addChild(where.getChild(0));
            where.getChildren().set(0, top_statement_list);
            top_statement_list.setParent(where);
            top_statement_list.addChild(top_statement);
            top_statement.addChild(what);
            return dest;
        } else {
            return where;
        }
    }

    protected ParseNode addLastTopStatement(ParseNode where, ParseNode what) {
        if (where.getSymbol() != PhpSymbols.top_statement_list || (what.getSymbol() != PhpSymbols.statement && what.getSymbol() != PhpSymbols.declaration_statement)) {
            return where;
        }
        return helper.create(PhpSymbols.top_statement_list, where, helper.create(PhpSymbols.top_statement, what));
    }

    protected ParseNode[] getArgs(String functionName, Node argsNode, int lineno) throws VisitorException {
        List<Node> nodes = argsNode.getChildren();
        ParseNode[] args;
        Symbol fun = symbolTable.getSymbol(functionName);
        if (fun == null || !fun.isFunction()) {
            args = new ParseNode[nodes.size()];
            int i = 0;
            for (Node child : nodes) {
                args[i++] = child.getParseNode();
            }
            return args;
        }
        if (fun.isFunction() && fun.getArgs() != null) {
            args = new ParseNode[fun.getArgs().size()];
        } else {
            args = new ParseNode[nodes.size()];
        }
        int i = 0;
        boolean keyword = false;
        int factIndex = 0;
        for (Node child : nodes) {
            if (child.getJdomElement().getName().equals("Keyword")) {
                String arg = child.getJdomElement().getAttributeValue("name");
                keyword = true;
                Node expr = getFirstChild(getFirstChild(child, "expr"), null);
                int index = fun.getArgIndex(arg);
                if (index < 0) {
                    log.warn("Unresolved argument " + arg + " in function " + fun.getModule() + "." + fun.getName() + " (" + helper.getCurrentFile() + ":" + lineno + ")");
                    args[factIndex] = expr.getParseNode();
                } else {
                    args[index] = expr.getParseNode();
                }
            } else {
                if (keyword) throw new VisitorException("Syntax error: non-keyword arg after keyword args (" + child.getParseNode().getFileName() + ":" + child.getParseNode().getLinenoLeft());
                args[i++] = child.getParseNode();
            }
            factIndex = 0;
            for (ParseNode tmp : args) {
                if (tmp == null) break;
                factIndex++;
            }
        }
        for (i = 0; i < args.length; i++) {
            if (args[i] == null) {
                ParseNode defaultValue = fun.getArg(i);
                if (defaultValue == null) {
                    log.warn("Couldn't find value for argument #" + i + " of function " + fun.getModule() + "." + fun.getName());
                } else {
                    args[i] = makeNonStatic(defaultValue, lineno);
                }
            }
        }
        return args;
    }

    protected ParseNode makeNonStatic(ParseNode node, int lineno) {
        ParseNode result;
        switch(node.getChild(0).getSymbol()) {
            case PhpSymbols.common_scalar:
            case PhpSymbols.T_STRING:
                result = helper.create(PhpSymbols.expr_without_variable, helper.create(PhpSymbols.scalar, duplicate(node.getChild(0))));
                break;
            case PhpSymbols.T_PLUS:
            case PhpSymbols.T_MINUS:
                result = helper.create(PhpSymbols.expr_without_variable, duplicate(node.getChild(0)), helper.create(PhpSymbols.expr, makeNonStatic(node.getChild(1), lineno)));
                break;
            case PhpSymbols.T_ARRAY:
                result = helper.create(PhpSymbols.expr_without_variable, helper.create(PhpSymbols.T_ARRAY, "array", lineno), helper.create(PhpSymbols.T_OPEN_BRACES, "(", lineno), makeNonStaticArrayPairList(node.getChild(2), lineno), helper.create(PhpSymbols.T_CLOSE_BRACES, ")", lineno));
                break;
            default:
                result = null;
        }
        return result;
    }

    protected ParseNode duplicate(ParseNode node) {
        return helper.deepCopy(node, new HashMap<String, String>());
    }

    protected ParseNode makeNonStaticArrayPairList(ParseNode original, int lineno) {
        if (original.getNumChildren() < 2) {
            return helper.create(PhpSymbols.array_pair_list, makeEpsilon());
        } else {
            return helper.create(PhpSymbols.array_pair_list, makeNonStaticNonEmptyArrayPairList(original.getChild(0), lineno), duplicate(original.getChild(1)));
        }
    }

    protected ParseNode makeNonStaticNonEmptyArrayPairList(ParseNode original, int lineno) {
        ParseNode non_empty_array_pair_list = helper.create(PhpSymbols.non_empty_array_pair_list);
        if (original.getNumChildren() == 1) {
            non_empty_array_pair_list.addChild(makeNonStatic(original.getChild(0), lineno));
        } else if (original.getNumChildren() == 5) {
            helper.addChild(non_empty_array_pair_list, makeNonStaticNonEmptyArrayPairList(original.getChild(0), lineno)).addChild(helper.create(PhpSymbols.T_COMMA, ",", lineno)).addChild(makeExpr(original.getChild(2))).addChild(helper.create(PhpSymbols.T_DOUBLE_ARROW, "=>", lineno)).addChild(makeExpr(original.getChild(4)));
        } else {
            switch(original.getChild(0).getSymbol()) {
                case PhpSymbols.non_empty_array_pair_list:
                    helper.addChild(non_empty_array_pair_list, makeNonStaticNonEmptyArrayPairList(original.getChild(0), lineno)).addChild(helper.create(PhpSymbols.T_COMMA, ",", lineno)).addChild(makeExpr(original.getChild(2)));
                    break;
                case PhpSymbols.static_scalar:
                    helper.addChild(non_empty_array_pair_list, makeExpr(original.getChild(0))).addChild(helper.create(PhpSymbols.T_DOUBLE_ARROW, "=>", lineno)).addChild(makeExpr(original.getChild(2)));
                    break;
                default:
                    log.warn("Wrong static array (" + original.getFileName() + ":" + original.getLinenoLeft() + ")");
                    break;
            }
        }
        return non_empty_array_pair_list;
    }

    protected ParseNode makeFunctionCallParameterList(ParseNode[] args, int lineno, ParseNode firstParam) {
        ParseNode function_call_parameter_list = helper.create(PhpSymbols.function_call_parameter_list);
        if (args.length == 0 && firstParam == null) {
            function_call_parameter_list.addChild(makeEpsilon());
        } else {
            ParseNode non_empty_function_call_parameter_list = null;
            if (firstParam != null) {
                non_empty_function_call_parameter_list = helper.create(PhpSymbols.non_empty_function_call_parameter_list);
                if (firstParam.getSymbol() == PhpSymbols.expr) {
                    firstParam = firstParam.getChild(0);
                }
                non_empty_function_call_parameter_list.addChild(firstParam);
            }
            for (ParseNode child : args) {
                if (non_empty_function_call_parameter_list == null) {
                    non_empty_function_call_parameter_list = helper.create(PhpSymbols.non_empty_function_call_parameter_list);
                } else {
                    non_empty_function_call_parameter_list = helper.create(PhpSymbols.non_empty_function_call_parameter_list, non_empty_function_call_parameter_list, helper.create(PhpSymbols.T_COMMA, ",", lineno));
                }
                if (child.getSymbol() == PhpSymbols.expr) {
                    child = child.getChild(0);
                    if (child.getSymbol() == PhpSymbols.r_cvar) {
                        child = child.getChild(0);
                    }
                }
                non_empty_function_call_parameter_list.addChild(makeCvar(child));
            }
            function_call_parameter_list.addChild(non_empty_function_call_parameter_list);
        }
        return function_call_parameter_list;
    }

    protected ParseNode makeStatementFromExpr(ParseNode expr, int lineno) {
        if (expr.getSymbol() != PhpSymbols.expr) {
            return expr;
        } else {
            return helper.create(PhpSymbols.statement, helper.create(PhpSymbols.unticked_statement, expr, helper.create(PhpSymbols.T_SEMICOLON, ";", lineno)));
        }
    }

    protected ParseNode makeStaticScalar(Node node) {
        ParseNode static_scalar = helper.create(PhpSymbols.static_scalar);
        String nodeName = node.getJdomElement().getName();
        if (nodeName.equals("Name")) {
            String name = node.getJdomElement().getAttributeValue("name");
            if (!name.equals("None")) {
                log.warn("WARNING: identifiers is not supported as default values of function params");
                return null;
            } else {
                static_scalar.addChild(helper.create(PhpSymbols.T_STRING, "null", getLineno(node.getJdomElement())));
            }
        } else if (nodeName.equals("Const")) {
            ParseNode common_scalar = makeCommonScalar(node);
            if (common_scalar == null) return null;
            static_scalar.addChild(common_scalar);
        } else if (nodeName.equals("UnaryAdd")) {
            ParseNode arg = makeStaticScalar(getFirstChild(getFirstChild(node, "expr"), null));
            if (arg == null) {
                log.warn("WARNING: got not prepared default value in function def (" + helper.getCurrentFile() + ":" + getLineno(node.getJdomElement()) + ")");
                return null;
            }
            helper.addChild(static_scalar, helper.create(PhpSymbols.T_PLUS, "+", getLineno(node.getJdomElement()))).addChild(arg);
        } else if (nodeName.equals("UnarySub")) {
            ParseNode arg = makeStaticScalar(getFirstChild(getFirstChild(node, "expr"), null));
            if (arg == null) {
                log.warn("WARNING: got not prepared default value in function def (" + helper.getCurrentFile() + ":" + getLineno(node.getJdomElement()) + ")");
                return null;
            }
            helper.addChild(static_scalar, helper.create(PhpSymbols.T_MINUS, "-", getLineno(node.getJdomElement()))).addChild(arg);
        } else if (nodeName.equals("List") || nodeName.equals("Tuple")) {
            static_scalar = makeStaticArray(node);
        } else if (nodeName.equals("Dict")) {
            static_scalar = makeStaticDictionary(node);
        } else {
            log.warn("WARNING: doesn't support default value with node " + nodeName + " (" + helper.getCurrentFile() + ":" + getLineno(node.getJdomElement()) + ")");
            return null;
        }
        return static_scalar;
    }

    protected ParseNode makeStaticArray(Node node) {
        int lineno = getLineno(node.getJdomElement());
        Node nodes = getFirstChild(node, "nodes");
        ParseNode static_array_pair_list = helper.create(PhpSymbols.static_array_pair_list);
        if (nodes.getChildren().isEmpty()) {
            static_array_pair_list.addChild(makeEpsilon());
        } else {
            ParseNode non_empty_static_array_pair_list = null;
            ParseNode possible_comma = helper.create(PhpSymbols.possible_comma, makeEpsilon());
            for (Node child : nodes.getChildren()) {
                if (non_empty_static_array_pair_list == null) {
                    non_empty_static_array_pair_list = helper.create(PhpSymbols.non_empty_static_array_pair_list);
                } else {
                    non_empty_static_array_pair_list = helper.create(PhpSymbols.non_empty_static_array_pair_list, non_empty_static_array_pair_list, helper.create(PhpSymbols.T_COMMA, ",", lineno));
                }
                ParseNode elem = makeStaticScalar(child);
                if (elem == null) return null;
                non_empty_static_array_pair_list.addChild(elem);
            }
            helper.addChild(static_array_pair_list, non_empty_static_array_pair_list).addChild(possible_comma);
        }
        return helper.create(PhpSymbols.static_scalar, helper.create(PhpSymbols.T_ARRAY, "array", lineno), helper.create(PhpSymbols.T_OPEN_BRACES, "(", lineno), static_array_pair_list, helper.create(PhpSymbols.T_CLOSE_BRACES, ")", lineno));
    }

    protected ParseNode makeStaticDictionary(Node node) {
        int lineno = getLineno(node.getJdomElement());
        Node items = getFirstChild(node, "items");
        ParseNode static_array_pair_list = helper.create(PhpSymbols.static_array_pair_list);
        if (items.getChildren().isEmpty()) {
            static_array_pair_list.addChild(makeEpsilon());
        } else {
            ParseNode non_empty_static_array_pair_list = null;
            ParseNode possible_comma = helper.create(PhpSymbols.possible_comma, makeEpsilon());
            for (int i = 0; i < items.getChildren().size(); i += 2) {
                Node key = items.getChildren().get(i);
                Node value = items.getChildren().get(i + 1);
                if (non_empty_static_array_pair_list == null) {
                    non_empty_static_array_pair_list = helper.create(PhpSymbols.non_empty_static_array_pair_list);
                } else {
                    non_empty_static_array_pair_list = helper.create(PhpSymbols.non_empty_static_array_pair_list, non_empty_static_array_pair_list, helper.create(PhpSymbols.T_COMMA, ",", lineno));
                }
                ParseNode keypn = makeStaticScalar(key);
                ParseNode valuepn = makeStaticScalar(value);
                if (keypn == null || valuepn == null) return null;
                helper.addChild(non_empty_static_array_pair_list, keypn).addChild(helper.create(PhpSymbols.T_DOUBLE_ARROW, "=>", lineno)).addChild(valuepn);
            }
            helper.addChild(static_array_pair_list, non_empty_static_array_pair_list).addChild(possible_comma);
        }
        return helper.create(PhpSymbols.static_scalar, helper.create(PhpSymbols.T_ARRAY, "array", lineno), helper.create(PhpSymbols.T_OPEN_BRACES, "(", lineno), static_array_pair_list, helper.create(PhpSymbols.T_CLOSE_BRACES, ")", lineno));
    }

    protected ParseNode makeCommonScalar(Node node) {
        int lineno = getLineno(node.getJdomElement());
        String value;
        Element element = node.getJdomElement();
        if (element.getAttribute("value") != null) {
            value = element.getAttribute("value").getValue();
        } else if (element.getChild("value") != null) {
            value = element.getChild("value").getText();
        } else {
            log.error("Constant without value found in (" + helper.getCurrentFile() + ":" + lineno + ")");
            return null;
        }
        Integer intval = getInteger(node.getJdomElement(), "value");
        Double dval = getDouble(node.getJdomElement(), "value");
        ParseNode constant;
        if (intval != null) {
            constant = helper.create(PhpSymbols.T_LNUMBER, value, lineno);
        } else if (dval != null) {
            constant = helper.create(PhpSymbols.T_DNUMBER, value, lineno);
        } else {
            constant = helper.create(PhpSymbols.T_CONSTANT_ENCAPSED_STRING, '"' + value + '"', lineno);
        }
        return helper.create(PhpSymbols.common_scalar, constant);
    }
}
