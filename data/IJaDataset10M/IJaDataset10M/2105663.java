package org.gcreator.pineapple.pinedl;

import java.util.List;
import java.util.Vector;
import org.gcreator.pineapple.pinedl.attributes.ComparisonType;
import org.gcreator.pineapple.pinedl.tree.*;

/**
 *
 * @author Luís Reis
 */
public final class Parser {

    private Library library;

    private List<Token> tokens;

    private DocumentNode document;

    public static List<Token.Type> accessModifier = new Vector<Token.Type>();

    static {
        accessModifier.add(Token.Type.PUBLIC);
        accessModifier.add(Token.Type.PROTECTED);
        accessModifier.add(Token.Type.PRIVATE);
    }

    public static final class Return<T extends Node> {

        public int i = 0;

        public T node = null;

        public Return() {
        }

        public Return(int i, T node) {
            this.i = i;
            this.node = node;
        }

        @Override
        public String toString() {
            return "Return[i=" + i + ", node=" + node + "]";
        }
    }

    public Parser(List<Token> tokens, Library library) {
        this.library = library;
        this.tokens = tokens;
        document = null;
    }

    public void parse() throws ParserException {
        document = new DocumentNode();
        if (tokens.isEmpty()) {
            return;
        }
        int i = 0;
        int tokensLength = tokens.size();
        while (i < tokensLength) {
            Return r;
            r = parseClass(i);
            if (r != null) {
                i += r.i;
                document.content.add(r.node);
                continue;
            }
            r = parseFunction(i);
            if (r != null) {
                i += r.i;
                document.content.add(r.node);
                continue;
            }
            throw buildException(tokens.get(i), "Unexpected token (" + i + ") " + tokens.get(i).type);
        }
    }

    public Return<ClassNode> parseClass(int i) throws ParserException {
        Token t = tokens.get(i);
        if (t.type == Token.Type.CLASS) {
            i++;
            ClassNode n = new ClassNode(t);
            t = demandToken(i++, Token.Type.WORD);
            n.name = t.text;
            t = demandToken(i);
            if (t.type == Token.Type.EXTENDS) {
                t = demandToken(++i, Token.Type.WORD);
                n.base.add(t.text);
                t = demandToken(i++);
                while (t.type == Token.Type.COMMA) {
                    t = demandToken(i++);
                    n.base.add(t.text);
                    t = demandToken(i++);
                }
            }
            Return<ClassContentNode> content = parseClassContent(i);
            i = content.i;
            n.content = content.node;
            return new Return<ClassNode>(i, n);
        }
        return null;
    }

    /**
     * @param i
     * @return the result. Never null.
     * @throws ParserException If matching failed
     */
    public Return<ClassContentNode> parseClassContent(int i) throws ParserException {
        ClassContentNode node = new ClassContentNode(demandToken(i++, Token.Type.BLKBEG));
        Token t;
        while (true) {
            t = demandToken(i);
            if (t.type == Token.Type.BLKEND) {
                return new Return<ClassContentNode>(i + 1, node);
            }
            Return<ConstructorNode> c = parseConstructor(i);
            if (c != null) {
                i = c.i;
                node.addConstructor(c.node);
                continue;
            }
            Return<MethodNode> m = parseMethod(i);
            if (m != null) {
                i = m.i;
                node.addMethod(m.node);
                continue;
            }
            Return<FieldNode> f = parseField(i);
            if (f != null) {
                i = f.i;
                node.addField(f.node);
                continue;
            }
            throw buildException(tokens.get(i), "Invalid class content");
        }
    }

    public Return<ConstructorNode> parseConstructor(int i) throws ParserException {
        Token accessToken = demandToken(i++);
        Token t = accessToken;
        if (accessModifier.contains(t.type)) {
            t = demandToken(i++);
        }
        if (t.type != Token.Type.THIS) {
            return null;
        }
        ConstructorNode constr = new ConstructorNode(t);
        constr.accessModifier = accessToken;
        ArgumentListNode arglist = new ArgumentListNode(demandToken(i++, Token.Type.LPAREN));
        constr.arguments = arglist;
        boolean isFirst = true;
        t = demandToken(i++);
        while (true) {
            if (t.type == Token.Type.RPAREN) {
                break;
            }
            if (!isFirst) {
                demandType(t, Token.Type.COMMA);
                t = demandToken(i++, Token.Type.WORD);
            } else {
                demandType(t, Token.Type.WORD);
            }
            isFirst = false;
            ArgumentListNode.Argument arg = new ArgumentListNode.Argument();
            arg.name = t.text;
            arglist.arguments.add(arg);
            t = demandToken(i++);
            if (t.type == Token.Type.EQUAL) {
                throw todo("Default arguments not yet implemented");
            }
            if (t.type == Token.Type.VARARGS) {
                arg.varargs = true;
                t = demandToken(i++, Token.Type.RPAREN);
                break;
            }
        }
        StatementContext c = new StatementContext();
        c.firstInConstructor = true;
        Return<StatementNode> r = parseStatement(i, c);
        if (r == null) {
            throw buildException(t, "Invalid constructor content");
        }
        i = r.i;
        constr.content = r.node;
        return new Return<ConstructorNode>(i, constr);
    }

    public Return<ConstantNode> parseConstant(int i) throws ParserException {
        Token constToken = demandToken(i++);
        if (constToken.type == Token.Type.TRUE || constToken.type == Token.Type.FALSE) {
            return new Return<ConstantNode>(i, new BooleanConstant(constToken));
        }
        if (constToken.type == Token.Type.CHARCONST) {
            return new Return<ConstantNode>(i, new CharConstant(constToken));
        }
        if (constToken.type == Token.Type.STRINGCONST) {
            return new Return<ConstantNode>(i, new StringConstant(constToken));
        }
        if (constToken.type == Token.Type.INTCONST || constToken.type == Token.Type.FLOATCONST) {
            return new Return<ConstantNode>(i, new NumericConstant(constToken));
        }
        return null;
    }

    public Return<Reference> parseReference(int i) throws ParserException {
        Token ref = demandToken(i++);
        if (ref.type != Token.Type.WORD) {
            return null;
        }
        String name = ref.text;
        if (!hasTokensLeft(i)) {
            return new Return<Reference>(i, new VariableReference(ref));
        }
        Token t = demandToken(i++);
        if (t.type == Token.Type.LPAREN) {
            throw todo("parseReference: function");
        }
        return new Return<Reference>(i - 1, new VariableReference(ref));
    }

    public Return<Reference> parseComposedReference(int i) throws ParserException {
        Return<ConstantNode> c = parseConstant(i);
        Reference r = null;
        if (c == null) {
            Return<Reference> ref = parseReference(i);
            if (ref == null) {
                return null;
            }
            i = ref.i;
            r = ref.node;
        } else {
            r = c.node;
            i = c.i;
        }
        while (true) {
            if (!hasTokensLeft(i)) {
                return new Return<Reference>(i, r);
            }
            Token t = demandToken(i);
            if (t.type != Token.Type.DOT) {
                return new Return<Reference>(i, r);
            }
            i++;
            throw todo("parseComposedReference");
        }
    }

    public Return<ExpressionNode> parsePrimitive(int i) throws ParserException {
        Token t = demandToken(i);
        if (t.type == Token.Type.PLUS) {
            i++;
        } else if (t.type == Token.Type.MINUS) {
            throw todo("negation");
        } else if (t.type == Token.Type.LPAREN) {
            i++;
            Return<ExpressionNode> exp = parseExpression(i);
            if (exp == null) {
                throw buildException(t, "Invalid expression");
            }
            i = exp.i;
            demandToken(i++, Token.Type.RPAREN);
            return new Return<ExpressionNode>(i, exp.node);
        }
        Return<Reference> ref = parseComposedReference(i);
        if (ref != null) {
            return (Return) ref;
        }
        return null;
    }

    public Return<ExpressionNode> parsePrePostOperator(int i) throws ParserException {
        Token t = demandToken(i++);
        if (t.type == Token.Type.INCREMENT) {
            Return<ExpressionNode> primitive = parsePrimitive(i);
            if (primitive == null) {
                throw buildException(t, "Invalid expression");
            }
            PrePostOperator op = new PrePostOperator(t);
            op.inc = true;
            op.pre = true;
            op.exp = primitive.node;
            return new Return<ExpressionNode>(primitive.i, op);
        } else if (t.type == Token.Type.DECREMENT) {
            Return<ExpressionNode> primitive = parsePrimitive(i);
            if (primitive == null) {
                throw buildException(t, "Invalid expression");
            }
            PrePostOperator op = new PrePostOperator(t);
            op.inc = false;
            op.pre = true;
            op.exp = primitive.node;
            return new Return<ExpressionNode>(primitive.i, op);
        }
        i--;
        Return<ExpressionNode> primitive = parsePrimitive(i);
        if (primitive == null) {
            return null;
        }
        i = primitive.i;
        t = demandToken(i++);
        if (t.type == Token.Type.INCREMENT) {
            PrePostOperator op = new PrePostOperator(t);
            op.inc = true;
            op.pre = false;
            op.exp = primitive.node;
            return new Return<ExpressionNode>(i, op);
        } else if (t.type == Token.Type.DECREMENT) {
            PrePostOperator op = new PrePostOperator(t);
            op.inc = false;
            op.pre = false;
            op.exp = primitive.node;
            return new Return<ExpressionNode>(i, op);
        }
        return primitive;
    }

    public Return<ExpressionNode> parseNotCast(int i) throws ParserException {
        Token t = demandToken(i++);
        if (t.type == Token.Type.LOGICAL_NOT) {
            Return<ExpressionNode> prepost = parsePrePostOperator(i);
            LogicalNotNode n = new LogicalNotNode(t);
            n.exp = prepost.node;
            return new Return(prepost.i, n);
        } else if (t.type == Token.Type.LPAREN) {
        }
        --i;
        return parsePrePostOperator(i);
    }

    public Return<ExpressionNode> parseMult(int i) throws ParserException {
        Return<ExpressionNode> ret = parseNotCast(i);
        if (ret == null) {
            return null;
        }
        i = ret.i;
        ExpressionNode left = ret.node;
        while (true) {
            Token t = demandToken(i++);
            BinaryOperatorNode bin;
            if (t.type == Token.Type.MULT) {
                bin = new MultNode(t);
            } else if (t.type == Token.Type.DIV) {
                bin = new DivNode(t);
            } else if (t.type == Token.Type.MOD) {
                bin = new ModNode(t);
            } else {
                return ret;
            }
            bin.left = left;
            ret = parseNotCast(i);
            if (ret == null) {
                throw buildException(t, "Invalid expression");
            }
            i = ret.i;
            bin.right = ret.node;
            ret = new Return(i, bin);
            left = bin;
        }
    }

    public Return<ExpressionNode> parseSum(int i) throws ParserException {
        Return<ExpressionNode> ret = parseMult(i);
        if (ret == null) {
            return null;
        }
        i = ret.i;
        ExpressionNode left = ret.node;
        while (true) {
            Token t = demandToken(i++);
            boolean add = true;
            if (t.type == Token.Type.PLUS) {
            } else if (t.type == Token.Type.MINUS) {
                add = false;
            } else {
                return ret;
            }
            SumNode sum = new SumNode(t);
            sum.add = add;
            sum.left = left;
            ret = parseMult(i);
            if (ret == null) {
                throw buildException(t, "Invalid expression");
            }
            i = ret.i;
            sum.right = ret.node;
            ret = new Return(i, sum);
            left = sum;
        }
    }

    public Return<ExpressionNode> parseShift(int i) throws ParserException {
        Return<ExpressionNode> ret = parseSum(i);
        if (ret == null) {
            return null;
        }
        i = ret.i;
        ExpressionNode left = ret.node;
        while (true) {
            Token t = demandToken(i++);
            boolean lshift = true;
            if (t.type == Token.Type.LSHIFT) {
            } else if (t.type == Token.Type.RSHIFT) {
                lshift = false;
            } else {
                return ret;
            }
            ShiftNode sum = new ShiftNode(t);
            sum.lshift = lshift;
            sum.left = left;
            ret = parseSum(i);
            if (ret == null) {
                throw buildException(t, "Invalid expression");
            }
            i = ret.i;
            sum.right = ret.node;
            ret = new Return(i, sum);
            left = sum;
        }
    }

    public Return<ExpressionNode> parseComparison(int i) throws ParserException {
        Return<ExpressionNode> left = parseShift(i);
        if (left == null) {
            return null;
        }
        i = left.i;
        ExpressionNode node = left.node;
        Token next = demandToken(i);
        ComparisonType type = null;
        if (next.type == Token.Type.GREATER) {
            i++;
            type = ComparisonType.GREATER;
        } else if (next.type == Token.Type.GREATEREQUAL) {
            i++;
            type = ComparisonType.GREATER_EQUAL;
        } else if (next.type == Token.Type.LOWER) {
            i++;
            type = ComparisonType.LOWER;
        } else if (next.type == Token.Type.LOWEREQUAL) {
            i++;
            type = ComparisonType.LOWER_EQUAL;
        } else {
            return left;
        }
        ComparisonNode comp = new ComparisonNode(next);
        comp.type = type;
        comp.left = node;
        Return<ExpressionNode> exp = parseShift(i);
        if (exp == null) {
            throw buildException(demandToken(i), "Invalid expression");
        }
        i = exp.i;
        comp.right = exp.node;
        return new Return<ExpressionNode>(i, comp);
    }

    public Return<ExpressionNode> parseComparison2(int i) throws ParserException {
        Return<ExpressionNode> left = parseComparison(i);
        if (left == null) return null;
        i = left.i;
        ExpressionNode node = left.node;
        Token next = demandToken(i);
        ComparisonType type = null;
        if (next.type == Token.Type.EQUALS) {
            i++;
            type = ComparisonType.EQUAL;
        } else if (next.type == Token.Type.NOTEQUAL) {
            i++;
            type = ComparisonType.NOT_EQUAL;
        } else {
            return left;
        }
        ComparisonNode comp = new ComparisonNode(next);
        comp.type = type;
        comp.left = node;
        Return<ExpressionNode> exp = parseComparison(i);
        if (exp == null) {
            throw buildException(demandToken(i), "Invalid expression");
        }
        i = exp.i;
        comp.right = exp.node;
        return new Return<ExpressionNode>(i, comp);
    }

    public Return<ExpressionNode> parseBitwiseAnd(int i) throws ParserException {
        Return<ExpressionNode> ret = parseComparison2(i);
        if (ret == null) {
            return null;
        }
        i = ret.i;
        ExpressionNode left = ret.node;
        while (true) {
            Token t = demandToken(i++);
            if (t.type != Token.Type.BITWISE_AND) {
                return ret;
            }
            BitwiseAndOperator sum = new BitwiseAndOperator(t);
            sum.left = left;
            ret = parseComparison2(i);
            if (ret == null) {
                throw buildException(t, "Invalid expression");
            }
            i = ret.i;
            sum.right = ret.node;
            ret = new Return(i, sum);
            left = sum;
        }
    }

    public Return<ExpressionNode> parseBitwiseOr(int i) throws ParserException {
        Return<ExpressionNode> ret = parseBitwiseAnd(i);
        if (ret == null) {
            return null;
        }
        i = ret.i;
        ExpressionNode left = ret.node;
        while (true) {
            Token t = demandToken(i++);
            if (t.type != Token.Type.BITWISE_OR) {
                return ret;
            }
            BitwiseOrOperator sum = new BitwiseOrOperator(t);
            sum.left = left;
            ret = parseBitwiseAnd(i);
            if (ret == null) {
                throw buildException(t, "Invalid expression");
            }
            i = ret.i;
            sum.right = ret.node;
            ret = new Return(i, sum);
            left = sum;
        }
    }

    public Return<ExpressionNode> parseBitwiseXor(int i) throws ParserException {
        Return<ExpressionNode> ret = parseBitwiseOr(i);
        if (ret == null) {
            return null;
        }
        i = ret.i;
        ExpressionNode left = ret.node;
        while (true) {
            Token t = demandToken(i++);
            if (t.type != Token.Type.BITWISE_XOR) {
                return ret;
            }
            BitwiseXorOperator sum = new BitwiseXorOperator(t);
            sum.left = left;
            ret = parseBitwiseOr(i);
            if (ret == null) {
                throw buildException(t, "Invalid expression");
            }
            i = ret.i;
            sum.right = ret.node;
            ret = new Return(i, sum);
            left = sum;
        }
    }

    public Return<ExpressionNode> parseLogicalAnd(int i) throws ParserException {
        Return<ExpressionNode> ret = parseBitwiseXor(i);
        if (ret == null) {
            return null;
        }
        i = ret.i;
        ExpressionNode left = ret.node;
        while (true) {
            Token t = demandToken(i++);
            if (t.type != Token.Type.LOGICAL_AND) {
                return ret;
            }
            LogicalAndOperator sum = new LogicalAndOperator(t);
            sum.left = left;
            ret = parseBitwiseXor(i);
            if (ret == null) {
                throw buildException(t, "Invalid expression");
            }
            i = ret.i;
            sum.right = ret.node;
            ret = new Return(i, sum);
            left = sum;
        }
    }

    public Return<ExpressionNode> parseLogicalOr(int i) throws ParserException {
        Return<ExpressionNode> ret = parseLogicalAnd(i);
        if (ret == null) {
            return null;
        }
        i = ret.i;
        ExpressionNode left = ret.node;
        while (true) {
            Token t = demandToken(i++);
            if (t.type != Token.Type.LOGICAL_OR) {
                return ret;
            }
            LogicalOrOperator sum = new LogicalOrOperator(t);
            sum.left = left;
            ret = parseLogicalAnd(i);
            if (ret == null) {
                throw buildException(t, "Invalid expression");
            }
            i = ret.i;
            sum.right = ret.node;
            ret = new Return(i, sum);
            left = sum;
        }
    }

    public Return<ExpressionNode> parseTernaryConditional(int i) throws ParserException {
        Return<ExpressionNode> ret = parseLogicalOr(i);
        if (ret == null) {
            return null;
        }
        i = ret.i;
        Token t = demandToken(i++);
        if (t.type != Token.Type.QUESTIONMARK) {
            return ret;
        }
        Return<ExpressionNode> left = parseExpression(i);
        if (left == null) {
            throw buildException(t, "Invalid expression");
        }
        i = left.i;
        demandToken(i++, Token.Type.COLON);
        Return<ExpressionNode> right = parseExpression(i);
        if (right == null) {
            throw buildException(t, "Invalid expression");
        }
        i = right.i;
        TernaryConditionalOperator ternary = new TernaryConditionalOperator(t);
        ternary.condition = ret.node;
        ternary.trueValue = left.node;
        ternary.falseValue = right.node;
        return new Return(i, ternary);
    }

    public Return<ExpressionNode> parseAssign(int i) throws ParserException {
        Return<ExpressionNode> r1 = parseTernaryConditional(i);
        if (r1 == null) {
            return null;
        }
        i = r1.i;
        ExpressionNode lvalue = r1.node;
        Token t = demandToken(i++);
        if (t.type == Token.Type.EQUAL) {
            AssignNode node = new AssignNode(t);
            Return<ExpressionNode> r2 = parseAssign(i);
            if (r2 == null) {
                return null;
            }
            i = r2.i;
            ExpressionNode rvalue = r2.node;
            node.lvalue = lvalue;
            node.rvalue = rvalue;
            return new Return<ExpressionNode>(i, node);
        }
        i--;
        return r1;
    }

    public Return<ExpressionNode> parseExpression(int i) throws ParserException {
        return parseAssign(i);
    }

    public Return<FieldNode> parseField(int i) throws ParserException {
        Token accessToken = demandToken(i++);
        Token t = accessToken;
        if (accessModifier.contains(accessToken.type)) {
            t = demandToken(i++);
        }
        if (t.type != Token.Type.VAR && t.type != Token.Type.CONST) {
            return null;
        }
        FieldNode f = new FieldNode(t);
        f.accessModifier = accessToken;
        f.name = demandToken(i++, Token.Type.WORD).text;
        t = demandToken(i++);
        if (t.type == Token.Type.EQUAL) {
            Return<ExpressionNode> exp = parseExpression(i);
            if (exp == null) {
                throw buildException(t, "Invalid value for field " + f.name);
            }
            i = exp.i;
            f.value = exp.node;
            demandToken(i++, Token.Type.SEMICOLON);
            return new Return<FieldNode>(i, f);
        } else if (t.type == Token.Type.SEMICOLON) {
            if (f.isConst) {
                throw buildException(t, "Constant fields must have a value");
            }
            return new Return<FieldNode>(i, f);
        }
        throw buildException(t, "Unexpected token of type " + t.type);
    }

    public Return<MethodNode> parseMethod(int i) throws ParserException {
        Token accessToken = demandToken(i++);
        Token t = accessToken;
        if (accessModifier.contains(accessToken.type)) {
            t = demandToken(i++);
        }
        if (t.type != Token.Type.FUNCTION) {
            return null;
        }
        t = demandToken(i++, Token.Type.WORD);
        MethodNode method = new MethodNode(t);
        method.name = t.text;
        method.accessModifier = accessToken;
        ArgumentListNode arglist = new ArgumentListNode(demandToken(i++, Token.Type.LPAREN));
        method.arguments = arglist;
        boolean isFirst = true;
        t = demandToken(i++);
        while (true) {
            if (t.type == Token.Type.RPAREN) {
                break;
            }
            if (!isFirst) {
                demandType(t, Token.Type.COMMA);
                t = demandToken(i++, Token.Type.WORD);
            } else {
                demandType(t, Token.Type.WORD);
            }
            isFirst = false;
            ArgumentListNode.Argument arg = new ArgumentListNode.Argument();
            arg.name = t.text;
            arglist.arguments.add(arg);
            t = demandToken(i++);
            if (t.type == Token.Type.EQUAL) {
                throw todo("Default arguments not yet implemented");
            }
            if (t.type == Token.Type.VARARGS) {
                arg.varargs = true;
                t = demandToken(i++, Token.Type.RPAREN);
                break;
            }
        }
        StatementContext c = new StatementContext();
        c.firstInConstructor = true;
        Return<StatementNode> r = parseStatement(i, c);
        if (r == null) {
            throw buildException(t, "Invalid constructor content");
        }
        i = r.i;
        method.content = r.node;
        return new Return<MethodNode>(i, method);
    }

    public Return<StatementNode> parseStatement(int i, StatementContext context) throws ParserException {
        Return r;
        if ((r = parseBlockStatement(i, context)) != null) return r;
        if ((r = parseDeclaration(i)) != null) return r;
        if ((r = parseIfStatement(i, context)) != null) return r;
        if ((r = parseWhileStatement(i, context)) != null) return r;
        if ((r = parseForStatement(i, context)) != null) return r;
        Token t = demandToken(i++);
        if (t.type == Token.Type.BREAK) {
            demandToken(i++, Token.Type.SEMICOLON);
            return new Return<StatementNode>(i, new Break(t));
        }
        if (t.type == Token.Type.CONTINUE) {
            if (!context.inLoop) {
                throw buildException(t, "continue statements must be in loops.");
            }
            demandToken(i++, Token.Type.SEMICOLON);
            return new Return<StatementNode>(i, new Continue(t));
        }
        i--;
        Return<ExpressionNode> exp = parseExpression(i);
        if (exp != null) {
            i = exp.i;
            demandToken(i++, Token.Type.SEMICOLON);
            return new Return(i, exp.node);
        }
        throw todo("parseStatement " + demandToken(i));
    }

    public Return<DeclarationNode> parseDeclaration(int i) throws ParserException {
        Token t = demandToken(i++);
        if (t.type != Token.Type.VAR && t.type != Token.Type.CONST) {
            return null;
        }
        DeclarationNode decl = new DeclarationNode(t);
        Token name = demandToken(i++, Token.Type.WORD);
        decl.name = name.text;
        Token next = demandToken(i++);
        if (next.type == Token.Type.SEMICOLON) {
            if (t.type == Token.Type.CONST) {
                throw buildException(next, "Constant declarations must have an associated value");
            }
            return new Return<DeclarationNode>(i, decl);
        }
        demandType(next, Token.Type.EQUAL);
        Return<ExpressionNode> e = parseExpression(i);
        if (e == null) {
            throw buildException(next, "Invalid expression");
        }
        i = e.i;
        decl.defaultValue = e.node;
        demandToken(i++, Token.Type.SEMICOLON);
        return new Return<DeclarationNode>(i, decl);
    }

    public Return<IfStatement> parseIfStatement(int i, StatementContext context) throws ParserException {
        Token t = demandToken(i++);
        IfStatement ifStmt = new IfStatement(t);
        if (t.type != Token.Type.IF) {
            return null;
        }
        demandToken(i++, Token.Type.LPAREN);
        Return<ExpressionNode> condition = parseExpression(i);
        if (condition == null) {
            throw buildException(t, "Invalid condition");
        }
        i = condition.i;
        ifStmt.condition = condition.node;
        demandToken(i++, Token.Type.RPAREN);
        Return<StatementNode> stmt = parseStatement(i, context.notFirst());
        if (stmt == null) {
            throw buildException(t, "Expected statement after if case");
        }
        i = stmt.i;
        ifStmt.then = stmt.node;
        t = demandToken(i);
        if (t.type == Token.Type.ELSE) {
            i++;
            stmt = parseStatement(i, context.notFirst());
            if (stmt == null) {
                throw buildException(t, "Expected statement after else case");
            }
            i = stmt.i;
            ifStmt.elseCase = stmt.node;
        }
        return new Return(i, ifStmt);
    }

    public Return<WhileStatement> parseWhileStatement(int i, StatementContext context) throws ParserException {
        Token t = demandToken(i++);
        WhileStatement whileStmt = new WhileStatement(t);
        if (t.type != Token.Type.WHILE) {
            return null;
        }
        demandToken(i++, Token.Type.LPAREN);
        Return<ExpressionNode> condition = parseExpression(i);
        if (condition == null) {
            throw buildException(t, "Invalid condition");
        }
        i = condition.i;
        whileStmt.condition = condition.node;
        demandToken(i++, Token.Type.RPAREN);
        Return<StatementNode> stmt = parseStatement(i, context.notFirst().inLoop());
        if (stmt == null) {
            throw buildException(t, "Expected statement after while case");
        }
        i = stmt.i;
        whileStmt.then = stmt.node;
        return new Return(i, whileStmt);
    }

    public Return<ForStatement> parseForStatement(int i, StatementContext context) throws ParserException {
        Token t = demandToken(i++);
        ForStatement forStmt = new ForStatement(t);
        if (t.type != Token.Type.FOR) {
            return null;
        }
        demandToken(i++, Token.Type.LPAREN);
        Return<StatementNode> first = parseStatement(i, context.notFirst().inLoop());
        if (first == null) {
            throw buildException(t, "Invalid statement");
        } else if (!(first.node instanceof ExpressionNode) && !(first.node instanceof DeclarationNode)) {
            throw buildException(t, "Invalid statement (" + first.node.getClass().getCanonicalName() + ")");
        }
        i = first.i;
        Return<ExpressionNode> condition = parseExpression(i);
        if (condition == null) {
            throw buildException(t, "Invalid condition");
        }
        i = condition.i;
        demandToken(i++, Token.Type.SEMICOLON);
        Return<ExpressionNode> exec = parseExpression(i);
        if (exec != null) {
            i = exec.i;
            forStmt.loopExec = exec.node;
        }
        forStmt.start = first.node;
        forStmt.condition = condition.node;
        demandToken(i++, Token.Type.RPAREN);
        Return<StatementNode> stmt = parseStatement(i, context.notFirst().inLoop());
        if (stmt == null) {
            throw buildException(t, "Expected statement after for case");
        }
        i = stmt.i;
        forStmt.then = stmt.node;
        return new Return(i, forStmt);
    }

    public Return<BlockNode> parseBlockStatement(int i, StatementContext context) throws ParserException {
        Token t = demandToken(i++);
        if (t.type != Token.Type.BLKBEG) {
            return null;
        }
        BlockNode blk = new BlockNode(t);
        while (true) {
            t = demandToken(i);
            if (t.type == Token.Type.BLKEND) {
                return new Return<BlockNode>(i + 1, blk);
            }
            Return<StatementNode> r = parseStatement(i, context);
            if (r == null) {
                throw buildException(t, "Invalid expression.");
            }
            i = r.i;
            blk.statements.add(r.node);
            context = context.notFirst();
        }
    }

    public Return<FunctionNode> parseFunction(int i) throws ParserException {
        Token t = demandToken(i++);
        if (t.type != Token.Type.FUNCTION) {
            return null;
        }
        t = demandToken(i++, Token.Type.WORD);
        FunctionNode func = new FunctionNode(t);
        func.name = t.text;
        ArgumentListNode arglist = new ArgumentListNode(demandToken(i++, Token.Type.LPAREN));
        func.arguments = arglist;
        boolean isFirst = true;
        t = demandToken(i++);
        while (true) {
            if (t.type == Token.Type.RPAREN) {
                break;
            }
            if (!isFirst) {
                demandType(t, Token.Type.COMMA);
                t = demandToken(i++, Token.Type.WORD);
            } else {
                demandType(t, Token.Type.WORD);
            }
            isFirst = false;
            ArgumentListNode.Argument arg = new ArgumentListNode.Argument();
            arg.name = t.text;
            arglist.arguments.add(arg);
            t = demandToken(i++);
            if (t.type == Token.Type.EQUAL) {
                throw todo("Default arguments not yet implemented");
            }
            if (t.type == Token.Type.VARARGS) {
                arg.varargs = true;
                t = demandToken(i++, Token.Type.RPAREN);
                break;
            }
        }
        StatementContext c = new StatementContext();
        c.firstInConstructor = true;
        Return<StatementNode> r = parseStatement(i, c);
        if (r == null) {
            throw buildException(t, "Invalid constructor content");
        }
        i = r.i;
        func.content = r.node;
        return new Return<FunctionNode>(i, func);
    }

    public ParserException todo(String message) {
        return new ParserException("[TODO] " + message);
    }

    public boolean hasTokensLeft(int i) {
        return i < tokens.size();
    }

    public Token demandToken(int i) throws ParserException {
        if (i >= tokens.size()) {
            throw new ParserException("Unexpected end of file");
        }
        return tokens.get(i);
    }

    public Token demandToken(int i, Token.Type type) throws ParserException {
        if (i >= tokens.size()) {
            throw new ParserException("Unexpected end of file");
        }
        Token t = tokens.get(i);
        demandType(t, type);
        return t;
    }

    public Token demandToken(int i, List<Token.Type> types) throws ParserException {
        if (i >= tokens.size()) {
            throw new ParserException("Unexpected end of file");
        }
        Token t = tokens.get(i);
        demandType(t, types);
        return t;
    }

    public void demandType(Token token, Token.Type type) throws ParserException {
        Token.Type ttype = token.type;
        if (ttype != type) {
            throw buildException(token, "Expected token of type " + type + ". Instead, got " + token);
        }
    }

    public void demandType(Token token, List<Token.Type> types) throws ParserException {
        Token.Type ttype = token.type;
        if (!types.contains(ttype)) {
            throw buildException(token, "Wrong token type " + token);
        }
    }

    public ParserException buildException(Token token, String message) {
        return new ParserException("In line " + token.line + ":\n\t" + message);
    }

    public DocumentNode getDocumentNode() {
        return document;
    }
}
