package org.gcreator.pineapple.pinec.parser;

import org.gcreator.pineapple.pinec.parser.support.Argument;
import org.gcreator.pineapple.pinec.CompilerError;
import org.gcreator.pineapple.pinec.lexer.Keyword;
import org.gcreator.pineapple.pinec.lexer.LexerHandler;
import org.gcreator.pineapple.pinec.lexer.Operator;
import org.gcreator.pineapple.pinec.lexer.Token;
import org.gcreator.pineapple.pinec.lexer.TokenType;
import org.gcreator.pineapple.pinec.parser.support.Variable;
import org.gcreator.pineapple.pinec.parser.tree.*;

/**
 *
 * @author luis
 */
public final class Parser {

    private final LexerHandler handler;

    public Parser(final LexerHandler handler) {
        this.handler = handler;
    }

    public Document run() {
        final Document d = new Document();
        final int size = handler.tokens.size();
        try {
            for (int i = 0; i < size; ) {
                CompilerReturnData<ClassConstant> clsConst = parseClass(i);
                if (clsConst != null) {
                    d.classes.add(clsConst.leaf);
                    i = clsConst.i;
                    continue;
                }
                final Token token = demandToken(i);
                handler.errors.add(buildUnexpectedTokenError(token));
                break;
            }
        } catch (final ParserException e) {
            e.printStackTrace();
        }
        return d;
    }

    public CompilerReturnData<ClassConstant> parseClass(int i) throws ParserException {
        final Token init = demandToken(i++);
        Token t = init;
        if (init.getType() == TokenType.WORD) {
            if (!init.getContent().equals("magic")) {
                return null;
            }
            t = requestToken(i++);
            if (t == null) {
                return null;
            }
        }
        if (t.getType() != TokenType.KEYWORD) {
            return null;
        }
        if (t.getKeyword() != Keyword.CLASS) {
            return null;
        }
        final Token name = demandToken(i++);
        if (name.getType() != TokenType.WORD) {
            return null;
        }
        final ClassConstant cls = new ClassConstant(init);
        cls.name = name.getContent();
        Token next = demandToken(i++);
        if ((next.getType() == TokenType.OPERATOR && next.getOperator() == Operator.COLON) || (next.getType() == TokenType.KEYWORD && next.getKeyword() == Keyword.EXTENDS)) {
            for (; ; ) {
                next = demandToken(i++, TokenType.WORD);
                cls.base.add(next.getContent());
                next = demandToken(i++, TokenType.OPERATOR);
                if (next.getOperator() != Operator.COMMA) {
                    break;
                }
            }
        }
        demandOperator(next, Operator.CBO);
        for (next = demandToken(i); !(next.getType() == TokenType.OPERATOR && next.getOperator() == Operator.CBC); next = demandToken(i)) {
            CompilerReturnData<Constructor> c = parseConstructor(i);
            if (c != null) {
                i = c.i;
                cls.constructors.add(c.leaf);
                continue;
            }
            CompilerReturnData<Function> f = parseMethod(i);
            if (f != null) {
                i = f.i;
                cls.functions.add(f.leaf);
                continue;
            }
            throw new NotYetImplementedException("Class content");
        }
        return new CompilerReturnData<ClassConstant>(i + 1, cls);
    }

    public CompilerReturnData<Constructor> parseConstructor(int i) throws ParserException {
        final Token init = demandToken(i++);
        Token t = init;
        if (t.getType() != TokenType.KEYWORD) {
            return null;
        }
        final Keyword k = t.getKeyword();
        if (k == Keyword.PRIVATE || k == Keyword.PROTECTED || k == Keyword.PUBLIC) {
            t = demandToken(i++);
            if (t.getType() != TokenType.KEYWORD) {
                return null;
            }
        } else {
            return null;
        }
        if (t.getKeyword() != Keyword.NEW) {
            return null;
        }
        Constructor c = new Constructor(init);
        demandToken(i, Operator.NBO);
        CompilerReturnData<ArgumentList> args = parseArgumentList(i);
        i = args.i;
        c.args = args.leaf;
        demandToken(i++, Operator.NBC);
        final CompilerReturnData<Statement> stmt = parseStatement(i);
        if (stmt == null) {
            fastError(true, t, "Expecting statement in constructor");
        }
        i = stmt.i;
        c.stmt = stmt.leaf;
        return new CompilerReturnData<Constructor>(i, c);
    }

    public CompilerReturnData<Function> parseMethod(int i) throws ParserException {
        final Token init = demandToken(i++);
        Token t = init;
        if (t.getType() != TokenType.KEYWORD) {
            return null;
        }
        final Keyword k = t.getKeyword();
        if (k == Keyword.PRIVATE || k == Keyword.PROTECTED || k == Keyword.PUBLIC) {
            t = demandToken(i++);
            if (t.getType() != TokenType.KEYWORD) {
                return null;
            }
        } else {
            return null;
        }
        boolean isStatic = false;
        if (t.getKeyword() == Keyword.STATIC) {
            isStatic = true;
            t = demandToken(i++);
            if (t.getType() != TokenType.KEYWORD) {
                return null;
            }
        }
        if (t.getKeyword() != Keyword.FUNCTION) {
            return null;
        }
        final Token nameToken = demandToken(i++, TokenType.WORD);
        Function c = new Function(init, nameToken.getContent(), isStatic);
        demandToken(i, Operator.NBO);
        CompilerReturnData<ArgumentList> args = parseArgumentList(i);
        i = args.i;
        c.args = args.leaf;
        demandToken(i++, Operator.NBC);
        final CompilerReturnData<Statement> stmt = parseStatement(i);
        if (stmt == null) {
            fastError(true, t, "Expecting statement in constructor");
        }
        i = stmt.i;
        c.stmt = stmt.leaf;
        return new CompilerReturnData<Function>(i, c);
    }

    public CompilerReturnData<Statement> parseStatement(final int i) throws ParserException {
        CompilerReturnData stmt;
        if ((stmt = parseBlock(i)) != null) return stmt;
        if ((stmt = parseEmptyStatement(i)) != null) return stmt;
        if ((stmt = parseExpressionStatement(i)) != null) return stmt;
        if ((stmt = parseReturnStatement(i)) != null) return stmt;
        if ((stmt = parseVariableDeclaration(i)) != null) return stmt;
        if ((stmt = parseIfStatement(i)) != null) return stmt;
        return null;
    }

    public CompilerReturnData<BlockStatement> parseBlock(int i) throws ParserException {
        Token t = demandToken(i++);
        if (t.getType() != TokenType.OPERATOR || t.getOperator() != Operator.CBO) {
            return null;
        }
        final BlockStatement blk = new BlockStatement(t);
        t = demandToken(i);
        while (t.getType() != TokenType.OPERATOR || t.getOperator() != Operator.CBC) {
            final CompilerReturnData<Statement> stmt = parseStatement(i);
            if (stmt == null) {
                fastError(true, t, "Expected statement in block");
            }
            i = stmt.i;
            blk.stmts.add(stmt.leaf);
            t = demandToken(i);
        }
        return new CompilerReturnData<BlockStatement>(i + 1, blk);
    }

    public CompilerReturnData<EmptyStatement> parseEmptyStatement(final int i) {
        final Token t = requestToken(i);
        if (t == null || t.getType() != TokenType.OPERATOR || t.getOperator() != Operator.SEMICOLON) {
            return null;
        }
        return new CompilerReturnData<EmptyStatement>(i + 1, new EmptyStatement());
    }

    public CompilerReturnData<Expression> parseExpressionStatement(final int i) throws ParserException {
        final CompilerReturnData<Expression> expr = parseRightList(i);
        if (expr == null) return null;
        demandToken(expr.i, Operator.SEMICOLON);
        return new CompilerReturnData<Expression>(expr.i + 1, expr.leaf);
    }

    public CompilerReturnData<IfStatement> parseIfStatement(int i) throws ParserException {
        final Token t = demandToken(i++);
        if (!t.is(Keyword.IF)) {
            return null;
        }
        demandToken(i++, Operator.NBO);
        CompilerReturnData<Expression> e = parseSingleValue(i);
        if (e == null) {
            fastError(true, t, "Invalid condition in if case");
        }
        i = e.i;
        demandToken(i++, Operator.NBC);
        IfStatement ifStmt = new IfStatement();
        ifStmt.condition = e.leaf;
        CompilerReturnData<Statement> istmt = parseStatement(i);
        if (istmt == null) {
            fastError(true, t, "Expected statement after if case");
        }
        i = istmt.i;
        ifStmt.ifCase = istmt.leaf;
        Token eToken = demandToken(i);
        if (eToken.is(Keyword.ELSE)) {
            ++i;
            CompilerReturnData<Statement> estmt = parseStatement(i);
            if (estmt == null) {
                fastError(true, t, "Expected statement after else case");
            }
            i = estmt.i;
            ifStmt.elseCase = estmt.leaf;
        }
        return new CompilerReturnData<IfStatement>(i, ifStmt);
    }

    public CompilerReturnData<Statement> parseReturnStatement(int i) throws ParserException {
        final Token tStart = demandToken(i++);
        if (!tStart.is(Keyword.RETURN)) {
            return null;
        }
        final Token t = demandToken(i++, Operator.VARARGS);
        CompilerReturnData<Expression> expr = parseSingleValue(i);
        if (expr == null) {
            fastError(true, t, "Invalid return... statement");
        }
        i = expr.i;
        demandToken(i++, Operator.SEMICOLON);
        return new CompilerReturnData<Statement>(i, new ReturnVarargs(expr.leaf));
    }

    public CompilerReturnData<ArgumentList> parseArgumentList(int i) throws ParserException {
        Token t;
        final ArgumentList args = new ArgumentList();
        String lastId = "-1";
        for (t = demandToken(++i); t.getType() != TokenType.OPERATOR; t = demandToken(++i)) {
            final Token init = t;
            final Argument arg = new Argument();
            boolean isVar = true;
            if (init.getType() == TokenType.KEYWORD) {
                final Keyword k = init.getKeyword();
                if (k == Keyword.CONST) {
                    isVar = false;
                } else if (k != Keyword.VAR) {
                    handler.errors.add(buildUnexpectedTokenError(init));
                    throw new ParserException();
                }
                t = demandToken(++i);
            }
            arg.var = isVar;
            boolean requiresId = false;
            final TokenType tt = t.getType();
            final String id;
            if (tt == TokenType.NUMBER) {
                id = Double.toString(Double.parseDouble(t.getContent()));
                requiresId = true;
            } else {
                demandType(t, TokenType.WORD);
                id = t.getContent();
            }
            t = demandToken(++i);
            if (t.getType() == TokenType.OPERATOR && t.getOperator() == Operator.COLON) {
                lastId = id;
                t = demandToken(++i, TokenType.WORD);
                arg.idLoc = id;
                arg.name = t.getContent();
                t = demandToken(++i);
            } else {
                if (requiresId) {
                    fastError(true, t.getLine(), t.getCol(), "Numbers are not valid argument names");
                    throw new ParserException();
                }
                try {
                    arg.name = id;
                    Double d = Double.parseDouble(lastId);
                    d++;
                    arg.idLoc = lastId = d.toString();
                } catch (NumberFormatException e) {
                    fastError(true, t.getLine(), t.getCol(), "Can not inference argument id.");
                    throw new ParserException();
                }
            }
            if (t.getType() != TokenType.OPERATOR) {
                handler.errors.add(buildUnexpectedTokenError(init));
                throw new ParserException();
            }
            final Operator op = t.getOperator();
            if (op == Operator.ASSIGN) {
                throw new NotYetImplementedException("argument default values");
            }
            if (op != Operator.COMMA && op != Operator.NBC) {
                handler.errors.add(buildUnexpectedTokenError(init));
                throw new ParserException();
            }
            args.arguments.add(arg);
        }
        if (!args.arguments.isEmpty()) {
            i--;
        }
        t = demandToken(i);
        final Operator op = t.getOperator();
        if (op != Operator.NBC) {
            handler.errors.add(buildUnexpectedTokenError(t));
            throw new ParserException();
        }
        return new CompilerReturnData<ArgumentList>(i, args);
    }

    public CompilerReturnData<VariableDeclaration> parseVariableDeclaration(int i) throws ParserException {
        Token t;
        final VariableDeclaration decl = new VariableDeclaration();
        boolean isVar = true;
        t = demandToken(i);
        if (t.getType() != TokenType.KEYWORD) {
            return null;
        }
        Keyword ik = t.getKeyword();
        if (ik == Keyword.VAR) {
            isVar = true;
        } else if (ik == Keyword.CONST) {
            isVar = false;
        } else {
            return null;
        }
        boolean first = true;
        for (t = demandToken(++i); t.getType() != TokenType.OPERATOR; t = demandToken(++i)) {
            final Token init = t;
            final Variable var = new Variable();
            System.out.println(init.getContent());
            if (init.getType() == TokenType.KEYWORD) {
                final Keyword k = init.getKeyword();
                if (!first && k == Keyword.CONST) {
                    isVar = false;
                } else if (k != Keyword.VAR || first) {
                    handler.errors.add(buildUnexpectedTokenError(init));
                    throw new ParserException();
                } else {
                    isVar = true;
                }
                t = demandToken(++i);
            }
            first = false;
            var.var = isVar;
            demandType(t, TokenType.WORD);
            var.name = t.getContent();
            Token n = demandToken(++i, TokenType.OPERATOR);
            if (!n.is(Operator.COMMA)) {
                --i;
            }
            decl.arguments.add(var);
        }
        demandOperator(t, Operator.SEMICOLON);
        ++i;
        if (decl.arguments.isEmpty()) {
            fastError(true, t.getLine(), t.getCol(), "Empty variable/constant declaration");
        }
        return new CompilerReturnData<VariableDeclaration>(i, decl);
    }

    public CompilerReturnData<BooleanConstant> parseBoolean(final int i) {
        final Token t = requestToken(i);
        if (t == null || t.getType() != TokenType.KEYWORD) {
            return null;
        }
        final Keyword k = t.getKeyword();
        boolean value = true;
        if (k == Keyword.FALSE) {
            value = false;
        } else if (k != Keyword.TRUE) {
            return null;
        }
        return new CompilerReturnData<BooleanConstant>(i + 1, new BooleanConstant(t));
    }

    public CompilerReturnData<StringConstant> parseString(final int i) throws ParserException {
        final Token t = demandToken(i);
        if (t.getType() == TokenType.STRING) {
            return new CompilerReturnData<StringConstant>(i + 1, new StringConstant(t));
        }
        return null;
    }

    public CompilerReturnData<NumericConstant> parseNumber(final int i) throws ParserException {
        final Token t = demandToken(i);
        if (t.getType() == TokenType.NUMBER) {
            return new CompilerReturnData<NumericConstant>(i + 1, new NumericConstant(t));
        }
        return null;
    }

    public CompilerReturnData<Constant> parseConstant(final int i) throws ParserException {
        CompilerReturnData stmt;
        if ((stmt = parseBoolean(i)) != null) return stmt;
        if ((stmt = parseString(i)) != null) return stmt;
        if ((stmt = parseNumber(i)) != null) return stmt;
        return null;
    }

    public CompilerReturnData<ThisReference> parseThis(final int i) {
        final Token t = requestToken(i);
        if (t == null || t.getType() != TokenType.KEYWORD) {
            return null;
        }
        final Keyword k = t.getKeyword();
        if (k != Keyword.THIS) {
            return null;
        }
        return new CompilerReturnData<ThisReference>(i + 1, new ThisReference(t));
    }

    public CompilerReturnData<SuperReference> parseSuper(final int i) {
        final Token t = requestToken(i);
        if (t == null || t.getType() != TokenType.KEYWORD) {
            return null;
        }
        final Keyword k = t.getKeyword();
        if (k != Keyword.SUPER) {
            return null;
        }
        return new CompilerReturnData<SuperReference>(i + 1, new SuperReference(t));
    }

    public CompilerReturnData<VariableReference> parseVariable(final int i) {
        final Token t = requestToken(i);
        if (t == null || t.getType() != TokenType.WORD) {
            return null;
        }
        return new CompilerReturnData<VariableReference>(i + 1, new VariableReference(t));
    }

    public CompilerReturnData<Reference> parseTrivialLeftReference(final int i) throws ParserException {
        CompilerReturnData stmt;
        if ((stmt = parseThis(i)) != null) return stmt;
        if ((stmt = parseSuper(i)) != null) return stmt;
        if ((stmt = parseVariable(i)) != null) return stmt;
        if ((stmt = parseConstant(i)) != null) return stmt;
        return null;
    }

    public CompilerReturnData<Expression> parsePrimitive(final int i) throws ParserException {
        CompilerReturnData stmt = null;
        if ((stmt = parseConstant(i)) != null) return stmt;
        if ((stmt = parseTrivialLeftReference(i)) != null) return stmt;
        final Token t = demandToken(i);
        if (t.getType() == TokenType.OPERATOR && t.getOperator() == Operator.NBO) {
            stmt = parseRightList(i + 1);
            if (stmt == null) {
                fastError(true, t, "Invalid statements in brackets");
            }
            demandToken(stmt.i, Operator.NBC);
            return new CompilerReturnData(stmt.i + 1, stmt.leaf);
        }
        return null;
    }

    public CompilerReturnData<Expression> parseReference(int i) throws ParserException {
        final CompilerReturnData primitive = parsePrimitive(i);
        if (primitive == null) {
            return null;
        }
        Expression e = (Expression) primitive.leaf;
        i = primitive.i;
        while (true) {
            final Token t = demandToken(i++);
            if (t.is(Operator.NBO)) {
            }
            return new CompilerReturnData(--i, e);
        }
    }

    public CompilerReturnData<Expression> parsePrePostFixOperator(int i) throws ParserException {
        final Token first = demandToken(i);
        if (first.getType() == TokenType.OPERATOR) {
            final Operator op = first.getOperator();
            final boolean inc = op == Operator.INCREMENT;
            if (inc || op == Operator.DECREMENT) {
                final CompilerReturnData<Expression> stmt = parseReference(i + 1);
                if (stmt == null) {
                    fastError(true, first, "Invalid increment/decrement expression");
                }
                i = stmt.i;
                final PrePostFixExpression expr = new PrePostFixExpression();
                expr.base = stmt.leaf;
                expr.prefix = true;
                expr.increment = inc;
                return new CompilerReturnData<Expression>(i, expr);
            }
        }
        final CompilerReturnData<Expression> stmt = parseReference(i);
        if (stmt == null) {
            return null;
        }
        i = stmt.i;
        final Token t = demandToken(i);
        if (t.getType() == TokenType.OPERATOR) {
            final Operator op = t.getOperator();
            final boolean inc = op == Operator.INCREMENT;
            if (inc || op == Operator.DECREMENT) {
                final PrePostFixExpression expr = new PrePostFixExpression();
                expr.base = stmt.leaf;
                expr.prefix = false;
                expr.increment = inc;
                return new CompilerReturnData<Expression>(++i, expr);
            }
        }
        return stmt;
    }

    public CompilerReturnData<Expression> parseLogicalNot(final int i) throws ParserException {
        final CompilerReturnData<Expression> stmt = parsePrePostFixOperator(i);
        if (stmt != null) {
            return stmt;
        }
        final Token t = demandToken(i);
        if (t.is(Operator.LOGNOT)) {
            final CompilerReturnData<Expression> stmt2 = parsePrePostFixOperator(i + 1);
            if (stmt2 == null) {
                fastError(true, t, "Invalid logical not statement");
            }
            final LogicalNotExpression e = new LogicalNotExpression(stmt2.leaf);
            return new CompilerReturnData<Expression>(stmt2.i, e);
        }
        return null;
    }

    public CompilerReturnData<Expression> parseMultOperator(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseLogicalNot(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.MULT && op != Operator.DIV && op != Operator.MOD) {
                break;
            }
            final CompilerReturnData<Expression> f = parseLogicalNot(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            if (op == Operator.MULT) {
                e = new MultOperation(e, f.leaf);
            } else if (op == Operator.DIV) {
                e = new DivOperation(e, f.leaf);
            } else if (op == Operator.MOD) {
                e = new ModOperation(e, f.leaf);
            }
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseSumOperator(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseMultOperator(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.PLUS && op != Operator.MINUS) {
                break;
            }
            final CompilerReturnData<Expression> f = parseMultOperator(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            if (op == Operator.PLUS) {
                e = new SumOperation(e, f.leaf);
            } else if (op == Operator.MINUS) {
                e = new MinusOperation(e, f.leaf);
            }
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseBitwiseShiftOperator(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseSumOperator(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.LSHIFT && op != Operator.RSHIFT) {
                break;
            }
            final CompilerReturnData<Expression> f = parseSumOperator(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            e = new BitwiseShiftOperation(e, f.leaf, op == Operator.LSHIFT);
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseComparison1(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseBitwiseShiftOperator(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.GREATER && op != Operator.SMALLER && op != Operator.GTE && op != Operator.SME) {
                break;
            }
            final CompilerReturnData<Expression> f = parseBitwiseShiftOperator(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            if (op == Operator.SMALLER) {
                e = new LessThanOperator(e, f.leaf, false);
            } else if (op == Operator.SME) {
                e = new LessThanOperator(e, f.leaf, true);
            } else if (op == Operator.GREATER) {
                e = new GreaterThanOperator(e, f.leaf, false);
            } else if (op == Operator.GTE) {
                e = new GreaterThanOperator(e, f.leaf, true);
            }
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseComparison2(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseComparison1(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.EQUALS && op != Operator.NOTEQUAL) {
                break;
            }
            final CompilerReturnData<Expression> f = parseComparison1(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            e = new EqualsOperator(e, f.leaf, op == Operator.NOTEQUAL);
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseBitwiseAndOperator(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseComparison2(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.BITAND) {
                break;
            }
            final CompilerReturnData<Expression> f = parseComparison2(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            e = new BitwiseAndOperation(e, f.leaf);
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseBitwiseOrOperator(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseBitwiseAndOperator(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.BITOR) {
                break;
            }
            final CompilerReturnData<Expression> f = parseBitwiseAndOperator(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            e = new BitwiseOrOperation(e, f.leaf);
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseBitwiseXorOperator(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseBitwiseOrOperator(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.BITXOR) {
                break;
            }
            final CompilerReturnData<Expression> f = parseBitwiseOrOperator(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            e = new BitwiseXorOperation(e, f.leaf);
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseLogicalAndOperator(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseBitwiseXorOperator(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.LOGAND) {
                break;
            }
            final CompilerReturnData<Expression> f = parseBitwiseXorOperator(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            e = new LogicalAndOperation(e, f.leaf);
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseLogicalOrOperator(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseLogicalAndOperator(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.LOGOR) {
                break;
            }
            final CompilerReturnData<Expression> f = parseLogicalAndOperator(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            e = new LogicalOrOperation(e, f.leaf);
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseTernaryConditional(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseLogicalOrOperator(i);
        if (base == null) {
            return null;
        }
        i = base.i;
        Expression e = base.leaf;
        for (; ; ) {
            final Token t = demandToken(i++);
            if (t.getType() != TokenType.OPERATOR) {
                break;
            }
            final Operator op = t.getOperator();
            if (op != Operator.QMARK) {
                break;
            }
            final CompilerReturnData<Expression> f = parseLogicalOrOperator(i);
            if (f == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = f.i;
            demandToken(i++, Operator.COLON);
            final CompilerReturnData<Expression> g = parseLogicalOrOperator(i);
            if (g == null) {
                fastError(true, t, "Unexpected statement on right side of operator");
            }
            i = g.i;
            e = new TernaryConditionalOperation(e, f.leaf, g.leaf);
        }
        --i;
        return new CompilerReturnData<Expression>(i, e);
    }

    public CompilerReturnData<Expression> parseSingleValue(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseTernaryConditional(i);
        return base;
    }

    public CompilerReturnData<Expression> parseRightList(int i) throws ParserException {
        final CompilerReturnData<Expression> base = parseSingleValue(i);
        return base;
    }

    public void fastError(boolean fatal, int line, int col, String message) throws ParserException {
        handler.errors.add(new CompilerError(fatal, line, col, message));
        throw new ParserException();
    }

    public void fastError(boolean fatal, Token t, String message) throws ParserException {
        fastError(fatal, t.getLine(), t.getCol(), message);
    }

    public static CompilerError buildUnexpectedTokenError(final Token token) {
        final StringBuilder msg = new StringBuilder();
        msg.append("Unexpected ");
        final TokenType type = token.getType();
        if (type == TokenType.KEYWORD) {
            msg.append(token.getKeyword().getKeyword());
            msg.append(" (KEYWORD)");
        } else if (type == TokenType.OPERATOR) {
            msg.append(token.getOperator().getStringRepresentation());
            msg.append(" (OPERATOR)");
        } else if (type == TokenType.WORD) {
            msg.append(token.getContent());
            msg.append(" (WORD)");
        } else {
            msg.append(token.getType());
        }
        return new CompilerError(true, token.getLine(), token.getCol(), msg.toString());
    }

    public Token requestToken(final int i) {
        if (i >= handler.tokens.size()) {
            return null;
        }
        return handler.tokens.get(i);
    }

    public Token demandToken(final int i) throws ParserException {
        if (i >= handler.tokens.size()) {
            handler.errors.add(new CompilerError(true, -1, -1, "Unexpected end of file"));
            throw new ParserException();
        }
        return handler.tokens.get(i);
    }

    public Token demandToken(final int i, final TokenType type) throws ParserException {
        final Token t = demandToken(i);
        demandType(t, type);
        return t;
    }

    public Token demandToken(final int i, final Operator operator) throws ParserException {
        final Token t = demandToken(i);
        demandOperator(t, operator);
        return t;
    }

    public void demandType(final Token token, final TokenType type) throws ParserException {
        final TokenType foundType = token.getType();
        if (foundType != type) {
            handler.errors.add(new CompilerError(true, token.getLine(), token.getCol(), "Expected " + type + " found " + foundType));
            throw new ParserException();
        }
    }

    public void demandKeyword(final Token token, final Keyword key) throws ParserException {
        demandType(token, TokenType.KEYWORD);
        final Keyword foundKey = token.getKeyword();
        if (foundKey != key) {
            handler.errors.add(new CompilerError(true, token.getLine(), token.getCol(), "Expected " + key.getKeyword() + " found " + foundKey.getKeyword()));
            throw new ParserException();
        }
    }

    public void demandOperator(final Token token, final Operator operator) throws ParserException {
        demandType(token, TokenType.OPERATOR);
        final Operator foundOp = token.getOperator();
        if (foundOp != operator) {
            handler.errors.add(new CompilerError(true, token.getLine(), token.getCol(), "Expected " + operator.getStringRepresentation() + " found " + foundOp.getStringRepresentation()));
            throw new ParserException();
        }
    }
}
