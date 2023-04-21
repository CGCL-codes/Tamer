package sidekick.ecmascript.parser;

public interface EcmaScriptTreeConstants {

    public int JJTVOID = 0;

    public int JJTTHISREFERENCE = 1;

    public int JJTPARENEXPRESSION = 2;

    public int JJTLITERAL = 3;

    public int JJTIDENTIFIER = 4;

    public int JJTARRAYLITERAL = 5;

    public int JJTOBJECTLITERAL = 6;

    public int JJTLITERALFIELD = 7;

    public int JJTCOMPOSITEREFERENCE = 8;

    public int JJTALLOCATIONEXPRESSION = 9;

    public int JJTPROPERTYVALUEREFERENCE = 10;

    public int JJTPROPERTYIDENTIFIERREFERENCE = 11;

    public int JJTFUNCTIONCALLPARAMETERS = 12;

    public int JJTPOSTFIXEXPRESSION = 13;

    public int JJTOPERATOR = 14;

    public int JJTUNARYEXPRESSION = 15;

    public int JJTBINARYEXPRESSIONSEQUENCE = 16;

    public int JJTANDEXPRESSIONSEQUENCE = 17;

    public int JJTOREXPRESSIONSEQUENCE = 18;

    public int JJTCONDITIONALEXPRESSION = 19;

    public int JJTASSIGNMENTEXPRESSION = 20;

    public int JJTEXPRESSIONLIST = 21;

    public int JJTBLOCK = 22;

    public int JJTSTATEMENTLIST = 23;

    public int JJTVARIABLESTATEMENT = 24;

    public int JJTVARIABLEDECLARATIONLIST = 25;

    public int JJTVARIABLEDECLARATION = 26;

    public int JJTEMPTYEXPRESSION = 27;

    public int JJTEMPTYSTATEMENT = 28;

    public int JJTEXPRESSIONSTATEMENT = 29;

    public int JJTIFSTATEMENT = 30;

    public int JJTDOSTATEMENT = 31;

    public int JJTWHILESTATEMENT = 32;

    public int JJTFORSTATEMENT = 33;

    public int JJTFORVARSTATEMENT = 34;

    public int JJTFORVARINSTATEMENT = 35;

    public int JJTFORINSTATEMENT = 36;

    public int JJTCONTINUESTATEMENT = 37;

    public int JJTBREAKSTATEMENT = 38;

    public int JJTRETURNSTATEMENT = 39;

    public int JJTWITHSTATEMENT = 40;

    public int JJTSWITCHSTATEMENT = 41;

    public int JJTCASEGROUPS = 42;

    public int JJTCASEGROUP = 43;

    public int JJTCASEGUARD = 44;

    public int JJTTHROWSTATEMENT = 45;

    public int JJTTRYSTATEMENT = 46;

    public int JJTCATCHCLAUSE = 47;

    public int JJTFINALLYCLAUSE = 48;

    public int JJTFUNCTIONDECLARATION = 49;

    public int JJTFORMALPARAMETERLIST = 50;

    public int JJTFUNCTIONEXPRESSION = 51;

    public int JJTPROGRAM = 52;

    public String[] jjtNodeName = { "void", "ThisReference", "ParenExpression", "Literal", "Identifier", "ArrayLiteral", "ObjectLiteral", "LiteralField", "CompositeReference", "AllocationExpression", "PropertyValueReference", "PropertyIdentifierReference", "FunctionCallParameters", "PostfixExpression", "Operator", "UnaryExpression", "BinaryExpressionSequence", "AndExpressionSequence", "OrExpressionSequence", "ConditionalExpression", "AssignmentExpression", "ExpressionList", "Block", "StatementList", "VariableStatement", "VariableDeclarationList", "VariableDeclaration", "EmptyExpression", "EmptyStatement", "ExpressionStatement", "IfStatement", "DoStatement", "WhileStatement", "ForStatement", "ForVarStatement", "ForVarInStatement", "ForInStatement", "ContinueStatement", "BreakStatement", "ReturnStatement", "WithStatement", "SwitchStatement", "CaseGroups", "CaseGroup", "CaseGuard", "ThrowStatement", "TryStatement", "CatchClause", "FinallyClause", "FunctionDeclaration", "FormalParameterList", "FunctionExpression", "Program" };
}
