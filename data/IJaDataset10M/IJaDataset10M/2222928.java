package br.ufal.ic.ptl.contextualanalyzer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import br.ufal.ic.ptl.abstractsyntaxtrees.ActivityStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.AnyTypeDenoter;
import br.ufal.ic.ptl.abstractsyntaxtrees.AssignmentStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.BinaryExpression;
import br.ufal.ic.ptl.abstractsyntaxtrees.BinaryOperatorDeclaration;
import br.ufal.ic.ptl.abstractsyntaxtrees.BooleanLiteral;
import br.ufal.ic.ptl.abstractsyntaxtrees.BooleanLiteralExpression;
import br.ufal.ic.ptl.abstractsyntaxtrees.BooleanTypeDenoter;
import br.ufal.ic.ptl.abstractsyntaxtrees.CharLiteralExpression;
import br.ufal.ic.ptl.abstractsyntaxtrees.CharacterLiteral;
import br.ufal.ic.ptl.abstractsyntaxtrees.ChatStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.Declaration;
import br.ufal.ic.ptl.abstractsyntaxtrees.EmptyStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.ErrorTypeDenoter;
import br.ufal.ic.ptl.abstractsyntaxtrees.ForStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.HighOperator;
import br.ufal.ic.ptl.abstractsyntaxtrees.Identifier;
import br.ufal.ic.ptl.abstractsyntaxtrees.IdentifierExpression;
import br.ufal.ic.ptl.abstractsyntaxtrees.IfStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.IntLiteralExpression;
import br.ufal.ic.ptl.abstractsyntaxtrees.IntLiteralTypeDenoter;
import br.ufal.ic.ptl.abstractsyntaxtrees.IntegerLiteral;
import br.ufal.ic.ptl.abstractsyntaxtrees.LowOperator;
import br.ufal.ic.ptl.abstractsyntaxtrees.NotExpression;
import br.ufal.ic.ptl.abstractsyntaxtrees.Operator;
import br.ufal.ic.ptl.abstractsyntaxtrees.Program;
import br.ufal.ic.ptl.abstractsyntaxtrees.ReadStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.RelationOperator;
import br.ufal.ic.ptl.abstractsyntaxtrees.ReuseStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.SendInfoStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.SequentialCharLiteral;
import br.ufal.ic.ptl.abstractsyntaxtrees.SequentialDeclaration;
import br.ufal.ic.ptl.abstractsyntaxtrees.SequentialExpression;
import br.ufal.ic.ptl.abstractsyntaxtrees.SequentialIdentifier;
import br.ufal.ic.ptl.abstractsyntaxtrees.SequentialStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.SignalOperator;
import br.ufal.ic.ptl.abstractsyntaxtrees.SimpleTypeDenoter;
import br.ufal.ic.ptl.abstractsyntaxtrees.SimpleVariableName;
import br.ufal.ic.ptl.abstractsyntaxtrees.StringTypeDenoter;
import br.ufal.ic.ptl.abstractsyntaxtrees.Terminal;
import br.ufal.ic.ptl.abstractsyntaxtrees.TypeDenoter;
import br.ufal.ic.ptl.abstractsyntaxtrees.UnaryExpression;
import br.ufal.ic.ptl.abstractsyntaxtrees.UnaryOperatorDeclaration;
import br.ufal.ic.ptl.abstractsyntaxtrees.VarDeclaration;
import br.ufal.ic.ptl.abstractsyntaxtrees.VariableNameExpression;
import br.ufal.ic.ptl.abstractsyntaxtrees.WhileStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.WriteStatement;
import br.ufal.ic.ptl.abstractsyntaxtrees.visitors.Visitor;
import br.ufal.ic.ptl.syntaticanalyzer.parser.ErrorReporter;

public class TypeChecker implements Visitor {

    private Logger logger;

    private BasicEnvironment environment;

    private ErrorReporter errorReporter;

    private IdentificationTable idTable;

    public TypeChecker(BasicEnvironment environment, ErrorReporter errorReporter, IdentificationTable idTable) {
        this.environment = environment;
        this.errorReporter = errorReporter;
        this.idTable = idTable;
        logger = Logger.getLogger(TypeChecker.class);
    }

    private void reportUndeclared(Terminal leaf) {
        errorReporter.reportError("\"%\" n�o foi declarado", leaf.getSpelling(), leaf.getPosition());
    }

    public Object visitProgram(Program ast, Object o) {
        logger.debug("Entered visitProgram()");
        ast.getDeclarationAST().visit(this, null);
        ast.getStmtAST().visit(this, null);
        logger.debug("Exited visitProgram()");
        return null;
    }

    public Object visitIdentifier(Identifier ast, Object o) {
        logger.debug("Entered visitIdentifier()");
        Declaration binding = idTable.retrieve(ast.getSpelling());
        if (binding != null) {
            ast.setAstDeclaration(binding);
        } else {
            reportUndeclared(ast);
        }
        logger.debug("Exited visitIdentifier()");
        return binding;
    }

    public Object visitCharacterLiteral(CharacterLiteral ast, Object o) {
        logger.debug("Visited visitCharacterLiteral()");
        return environment.getStringType();
    }

    public Object visitIntegerLiteral(IntegerLiteral ast, Object o) {
        logger.debug("Visited visitIntegerLiteral()");
        return environment.getIntLiteralType();
    }

    public Object visitBooleanLiteral(BooleanLiteral ast, Object o) {
        logger.debug("Visited visitBooleanLiteral()");
        return environment.getBooleanType();
    }

    public Object visitVarDeclaration(VarDeclaration ast, Object o) {
        logger.debug("Entered visitVarDeclaration()");
        TypeDenoter typeDenoterAST = (TypeDenoter) ast.getTypeDenoterAST().visit(this, null);
        ast.setTypeDenoterAST(typeDenoterAST);
        String spelling = ast.getIdentifierAST().getSpelling();
        idTable.enter(spelling, ast);
        if (ast.isDuplicated()) errorReporter.reportError("identificador \"%\" j� declarado", ast.getIdentifierAST().getSpelling(), ast.position);
        logger.debug("Exited visitVarDeclaration()");
        return null;
    }

    public Object visitSequentialIdentifier(SequentialIdentifier ast, Object o) {
        logger.debug("Entered visitSequentialIdentifier()");
        ast.getIdentifierAST1().visit(this, null);
        ast.getIdentifierAST2().visit(this, null);
        logger.debug("Exited visitSequentialIdentifier()");
        return null;
    }

    public Object visitSequentialCharLiteral(SequentialCharLiteral ast, Object o) {
        logger.debug("Entered visitSequentialCharLiteral()");
        ast.getCharLiteralAST1().visit(this, null);
        ast.getCharLiteralAST2().visit(this, null);
        logger.debug("Exited visitSequentialCharLiteral()");
        return null;
    }

    public Object visitOperator(Operator ast, Object o) {
        logger.debug("Entered visitOperator()");
        Declaration binding = null;
        binding = idTable.retrieve(ast.getSpelling());
        if (o != null) {
            String operator = ast.getSpelling();
            if ("+".equals(operator)) binding = environment.getUnaryPlusExpr(); else if ("-".equals(operator)) binding = environment.getUnaryMinusExpr();
        }
        if (binding != null) {
            ast.setDeclarationAST(binding);
        }
        logger.debug("Exited visitOperator()");
        return binding;
    }

    public Object visitLowOperator(LowOperator ast, Object o) {
        logger.debug("Entered visitLowOperator()");
        return this.visitOperator(ast, o);
    }

    public Object visitHighOperator(HighOperator ast, Object o) {
        logger.debug("Entered visitHighOperator()");
        return this.visitOperator(ast, o);
    }

    public Object visitRelationOperator(RelationOperator ast, Object o) {
        logger.debug("Entered visitRelationOperator()");
        return this.visitOperator(ast, o);
    }

    public Object visitSignalOperator(SignalOperator ast, Object o) {
        logger.debug("Entered visitSignalOperator()");
        return this.visitOperator(ast, o);
    }

    public Object visitSimpleTypeDenoter(SimpleTypeDenoter ast, Object o) {
        logger.debug("Entered visitSimpleTypeDenoter()");
        Declaration binding = (Declaration) ast.getIdentifierAST().visit(this, null);
        if (binding == null) {
            reportUndeclared(ast.getIdentifierAST());
            return environment.getErrorType();
        } else if (!(binding instanceof VarDeclaration)) {
            errorReporter.reportError("\"%\" n�o � um identificador de tipo", ast.getIdentifierAST().getSpelling(), ast.getIdentifierAST().position);
            return environment.getErrorType();
        }
        return ((VarDeclaration) binding).getTypeDenoterAST();
    }

    public Object visitErrorTypeDenoter(ErrorTypeDenoter ast, Object o) {
        logger.debug("Entered visitErrorTypeDenoter()");
        return environment.getErrorType();
    }

    public Object visitIntLiteralTypeDenoter(IntLiteralTypeDenoter ast, Object o) {
        logger.debug("Entered visitIntLiteralTypeDenoter()");
        return environment.getIntLiteralType();
    }

    public Object visitBooleanTypeDenoter(BooleanTypeDenoter ast, Object o) {
        logger.debug("Entered visitBooleanTypeDenoter()");
        return environment.getBooleanType();
    }

    public Object visitStringTypeDenoter(StringTypeDenoter ast, Object o) {
        logger.debug("Entered visitStringTypeDenoter()");
        return environment.getStringType();
    }

    public Object visitAnyTypeDenoter(AnyTypeDenoter ast, Object o) {
        logger.debug("Entered visitAnyTypeDenoter()");
        return environment.getAnyType();
    }

    public Object visitSequentialStatement(SequentialStatement ast, Object o) {
        logger.debug("Entered visitSequentialStatement()");
        ast.getStatementAST1().visit(this, null);
        ast.getStatementAST2().visit(this, null);
        logger.debug("Exited visitSequentialStatement()");
        return null;
    }

    public Object visitWhileStatement(WhileStatement ast, Object o) {
        logger.debug("Entered visitWhileStatement()");
        TypeDenoter evaluatedType = (TypeDenoter) ast.getExprAST().visit(this, null);
        TypeDenoter expectedType = environment.getBooleanType();
        if (!evaluatedType.equals(expectedType)) errorReporter.reportError("express�o booleana esperada", "", ast.getExprAST().getPosition());
        ast.getStmtAST().visit(this, null);
        logger.debug("Exited visitWhileStatement()");
        return null;
    }

    public Object visitIfStatement(IfStatement ast, Object o) {
        logger.debug("Entered visitIfStatement()");
        TypeDenoter evaluatedType = (TypeDenoter) ast.getExpressionAST().visit(this, null);
        TypeDenoter expectedType = environment.getBooleanType();
        if (!evaluatedType.equals(expectedType)) errorReporter.reportError("express�o booleana esperada", "", ast.getExpressionAST().getPosition());
        ast.getStatementAST1().visit(this, null);
        ast.getStatementAST2().visit(this, null);
        logger.debug("Exited visitIfStatement()");
        return null;
    }

    public Object visitAssignmentStatement(AssignmentStatement ast, Object o) {
        logger.debug("Entered visitAssignmentStatement()");
        TypeDenoter variableType = (TypeDenoter) ast.getVarNameAST().visit(this, null);
        TypeDenoter expressionType = (TypeDenoter) ast.getENameAST().visit(this, null);
        if (!ast.getVarNameAST().isVariable()) errorReporter.reportError("o lado esquerdo da atribuicao nao � uma variavel", "", ast.getVarNameAST().getPosition());
        if (!expressionType.equals(variableType)) errorReporter.reportError("incompatibilidade de atribuicao", "", ast.getPosition());
        logger.debug("Exited visitAssignmentStatement()");
        return null;
    }

    public Object visitEmptyStatement(EmptyStatement ast, Object o) {
        logger.debug("Entered visitEmptyStatement()");
        return null;
    }

    public Object visitReadStatement(ReadStatement ast, Object o) {
        logger.debug("Entered visitReadStatement()");
        ast.getIdentifierListAST().visit(this, null);
        return null;
    }

    public Object visitWriteStatement(WriteStatement ast, Object o) {
        logger.debug("Entered visitWriteStatement()");
        ast.getExprListAST().visit(this, null);
        return null;
    }

    public Object visitReuseStatement(ReuseStatement ast, Object o) {
        logger.debug("Entered visitReuseStatement()");
        ast.getCharLiteralAST1().visit(this, null);
        ast.getCharLiteralAST2().visit(this, null);
        ast.getCharLiteralAST3().visit(this, null);
        ast.getCharLiteralAST4().visit(this, null);
        ast.getIntLiteralAST().visit(this, null);
        return null;
    }

    public Object visitChatStatement(ChatStatement ast, Object o) {
        logger.debug("Entered visitChatStatement()");
        ast.getCharAST1().visit(this, null);
        ast.getCharAST2().visit(this, null);
        ast.getCharAST3().visit(this, null);
        ast.getIntParamAST().visit(this, null);
        ast.getListAST().visit(this, null);
        return null;
    }

    public Object visitSendInfoStatement(SendInfoStatement ast, Object o) {
        logger.debug("Entered visitSendInfoStatement()");
        ast.getCharLiteralAST1().visit(this, null);
        ast.getCharLiteralAST2().visit(this, null);
        ast.getListCharLiteralAST1().visit(this, null);
        ast.getListCharLiteralAST2().visit(this, null);
        return null;
    }

    public Object visitActivityStatement(ActivityStatement ast, Object o) {
        logger.debug("Entered visitActivityStatement()");
        return null;
    }

    public Object visitNotExpression(NotExpression ast, Object o) {
        return ast.getExprAST().visit(this, null);
    }

    public Object visitUnaryExpression(UnaryExpression ast, Object o) {
        logger.debug("Entered visitUnaryExpression()");
        TypeDenoter eType = (TypeDenoter) ast.getExprAST().visit(this, null);
        Declaration binding = (Declaration) ast.getOperatorAST().visit(this, Object.class);
        if (binding == null) {
            reportUndeclared(ast.getOperatorAST());
            ast.setType(environment.getErrorType());
        } else if (!(binding instanceof UnaryOperatorDeclaration)) errorReporter.reportError("\"%\" nao � um operador unario", ast.getOperatorAST().getSpelling(), ast.getOperatorAST().getPosition()); else {
            UnaryOperatorDeclaration ubinding = (UnaryOperatorDeclaration) binding;
            if (!eType.equals(ubinding.getArgAST())) errorReporter.reportError("tipo de argumento invalido para o operador \"%\"", ast.getOperatorAST().getSpelling(), ast.getOperatorAST().getPosition());
            ast.setType(ubinding.getResultAST());
        }
        logger.debug("Exited visitUnaryExpression()");
        return ast.getType();
    }

    public Object visitBinaryExpression(BinaryExpression ast, Object o) {
        logger.debug("Entered visitBinaryExpression()");
        TypeDenoter e1Type = (TypeDenoter) ast.getExprAST1().visit(this, null);
        TypeDenoter e2Type = (TypeDenoter) ast.getExprAST2().visit(this, null);
        Declaration binding = (Declaration) ast.getOperatorAST().visit(this, null);
        if (binding == null) {
            reportUndeclared(ast.getOperatorAST());
        } else {
            if (!(binding instanceof BinaryOperatorDeclaration)) errorReporter.reportError("\"%\" nao � um operador binario", ast.getOperatorAST().getSpelling(), ast.getOperatorAST().getPosition());
            BinaryOperatorDeclaration bbinding = (BinaryOperatorDeclaration) binding;
            if (bbinding.getArgAST1() == environment.getAnyType()) {
                if (!e1Type.equals(e2Type)) errorReporter.reportError("tipos incompativeis de argumento para \"%\"", ast.getOperatorAST().getSpelling(), ast.getPosition());
            } else if (!e1Type.equals(bbinding.getArgAST1())) errorReporter.reportError("tipo de argumento invalido para o operador \"%\"", ast.getOperatorAST().getSpelling(), ast.getExprAST1().getPosition()); else if (!e2Type.equals(bbinding.getArgAST2())) errorReporter.reportError("tipo de argumento invalido para o operador \"%\"", ast.getOperatorAST().getSpelling(), ast.getExprAST2().getPosition());
            ast.setType(bbinding.getResultAST());
        }
        logger.debug("Exited visitBinaryExpression()");
        return ast.getType();
    }

    public Object visitCharacterExpression(CharLiteralExpression ast, Object o) {
        logger.debug("Entered visitCharacterExpression()");
        ast.setType(environment.getStringType());
        return ast.getType();
    }

    public Object visitBooleanExpression(BooleanLiteralExpression ast, Object o) {
        logger.debug("Entered visitBooleanExpression()");
        ast.setType(environment.getBooleanType());
        return ast.getType();
    }

    public Object visitIntLiteralExpression(IntLiteralExpression ast, Object o) {
        logger.debug("Entered visitIntLiteralExpression()");
        ast.setType(environment.getIntLiteralType());
        return ast.getType();
    }

    public Object visitVariableNameExpression(VariableNameExpression ast, Object o) {
        logger.debug("Entered visitVariableNameExpression()");
        return ast.getType();
    }

    public Object visitSequentialExpression(SequentialExpression ast, Object o) {
        logger.debug("Entered visitSequentialExpression()");
        ast.getExprAST1().visit(this, null);
        ast.getExprAST2().visit(this, null);
        logger.debug("Exited visitSequentialExpression()");
        return null;
    }

    public Object visitIdentifierExpression(IdentifierExpression ast, Object o) {
        logger.debug("Entered visitIdentifierExpression()");
        Declaration dType = (Declaration) ast.getIdentifierAST().visit(this, null);
        if (dType != null) {
            ast.setType(((VarDeclaration) dType).getTypeDenoterAST());
        } else {
            ast.setType(environment.getAnyType());
        }
        return ast.getType();
    }

    public Object visitSimpleVariableName(SimpleVariableName ast, Object o) {
        logger.debug("Entered visitSimpleVariableName()");
        ast.setVariable(false);
        ast.setTypedenoterAST(environment.getErrorType());
        Declaration binding = (Declaration) ast.getIdentifierAST().visit(this, null);
        if (binding == null) reportUndeclared(ast.getIdentifierAST()); else if (binding instanceof VarDeclaration) {
            ast.setTypedenoterAST(((VarDeclaration) binding).getTypeDenoterAST());
            ast.setVariable(true);
        } else errorReporter.reportError("\"%\" nao � um identificador de variavel v�lido", ast.getIdentifierAST().getSpelling(), ast.getIdentifierAST().getPosition());
        return ast.getTypedenoterAST();
    }

    public Object visitSequentialDeclaration(SequentialDeclaration ast, Object o) {
        logger.debug("Entered visitSequentialDeclaration()");
        ast.getDeclarationAST1().visit(this, null);
        ast.getDeclarationAST2().visit(this, null);
        logger.debug("Exited visitSequentialDeclaration()");
        return null;
    }

    public Object visitBinaryOperatorDeclaration(BinaryOperatorDeclaration ast, Object o) {
        logger.debug("Entered visitBinaryOperatorDeclaration()");
        return null;
    }

    public Object visitUnaryOperatorDeclaration(UnaryOperatorDeclaration ast, Object o) {
        logger.debug("Entered visitUnaryOperatorDeclaration()");
        return null;
    }

    public Object visitForStatement(ForStatement ast, Object o) {
        logger.debug("Entered visitForStatement()");
        ast.getParam1AST().visit(this, null);
        TypeDenoter evaluatedType = (TypeDenoter) ast.getExprParamAST().visit(this, null);
        TypeDenoter expectedType = environment.getBooleanType();
        if (!evaluatedType.equals(expectedType)) errorReporter.reportError("express�o booleana esperada", "", ast.getExprParamAST().getPosition());
        ast.getParam2AST().visit(this, null);
        ast.getParam3AST().visit(this, null);
        logger.debug("Exited visitForStatement()");
        return null;
    }
}
