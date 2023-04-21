package compiler.parsing;

import compiler.*;
import compiler.compilers.*;
import compiler.evaluators.*;
import compiler.tokens.SourceToken;
import compiler.tokens.Token;
import compiler.tokens.TokenException;
import main.SystemConstants;
import java.util.ArrayList;
import java.util.Arrays;
import static compiler.ErrorMessage.*;
import static compiler.TokenType.*;

public class Parser {

    private TokenBuffer input;

    private Token matchedToken;

    private CompilerContext context;

    private Parser() {
    }

    public Parser(LexicalAnalyzer input, int depth) throws TokenException {
        this.input = new TokenBuffer(input, depth);
        context = new CompilerContext();
    }

    public void parse() throws TokenException {
        TokenType[] mainTokenTypes = new TokenType[] { TT_LABEL_DEFINITION, TT_DIRECTIVE, TT_INSTRUCTION, TT_COMMENT, TT_EOL };
        ArrayList<TokenType> mainTokens = new ArrayList<TokenType>(Arrays.asList(mainTokenTypes));
        int tokenTypesSize = mainTokens.size();
        int rest;
        while (LA(1) != TT_EOF) {
            rest = 0;
            if (LA(1) == TT_LABEL_DEFINITION) {
                parseElement(TT_LABEL_DEFINITION);
                rest++;
            }
            switch(LA(1)) {
                case TT_DIRECTIVE_NAME:
                case TT_INSTRUCTION_NAME:
                    parseElement(LA(1));
                    rest += 2;
            }
            if (LA(1) == TT_COMMENT) {
                parseElement(TT_COMMENT);
                rest++;
            }
            if (LA(1) == TT_EOL) {
                match(TT_EOL);
            } else {
                reportError(E_UNEXPECTED_TOKEN, LT(1), mainTokens.subList(rest, tokenTypesSize).toArray(new TokenType[0]));
                return;
            }
        }
    }

    public CompilerContext context() {
        return context;
    }

    private void parseElement(TokenType type) throws TokenException {
        ElementCompiler compiler = null;
        Token token = LT(1);
        SourcePosition start = new SourcePosition(token.getSourceLineNumber(), token.getSourceColumnNumber());
        switch(type) {
            case TT_COMMENT:
                compiler = comment();
                break;
            case TT_LABEL_DEFINITION:
                compiler = labelDefinition();
                break;
            case TT_DIRECTIVE_NAME:
                compiler = directive();
                break;
            case TT_INSTRUCTION_NAME:
                compiler = instruction();
                break;
            default:
                throw new IllegalArgumentException("Illegal token type for ParseElement: " + type);
        }
        SourcePosition end = new SourcePosition(matchedToken.getSourceLineNumber(), matchedToken.getSourceColumnNumber() + matchedToken.getContentLength() - 1);
        compiler.positionInSource(start, end);
        try {
            context.addElement(compiler);
        } catch (MemoryAddressRangeOverlapException e) {
            reportError(E_MEMORY_OVERLAP, token, false);
        }
    }

    private ElementCompiler instruction() throws TokenException {
        ArrayList<EvaluatorToLong> operands = new ArrayList<EvaluatorToLong>();
        InstructionDefinition instruction;
        EvaluatorToLong operand;
        char[] operandTypes;
        TokenType operandType;
        Token instructionName;
        InstructionCompiler compiler;
        match(TT_INSTRUCTION_NAME);
        instructionName = matchedToken;
        instruction = Instructions.getInstructionDefinition(instructionName.getContent());
        operandTypes = instruction.getOperandTypes();
        if (operandTypes.length > 0) {
            for (int i = 0; i < operandTypes.length - 1; i++) {
                operandType = instruction.getOperandType(operandTypes[i]);
                instructionOperand(operandType, instruction, operands);
                match(TT_COMMA);
            }
            operandType = instruction.getOperandType(operandTypes[operandTypes.length - 1]);
            instructionOperand(operandType, instruction, operands);
        }
        return new ElementCompiler(InstructionCompiler.createCompiler(instructionName.getContent(), operands));
    }

    private ElementCompiler directive() throws TokenException {
        ArrayList<IntegerExpressionEvaluator> integerOperands = new ArrayList<IntegerExpressionEvaluator>();
        ArrayList<FloatingPointEvaluator> floatOperands = new ArrayList<FloatingPointEvaluator>();
        DirectiveDefinition directive;
        Token directiveName;
        TokenCompiler compiler = null;
        TokenType type;
        match(TT_DIRECTIVE_NAME);
        directiveName = matchedToken;
        directive = Directives.getDirectiveDefinition(matchedToken.getContent());
        type = directive.getType();
        switch(type) {
            case DT_CODE:
            case DT_DATA:
                memoryBlockSpecifiers(type);
                compiler = new EmptyCompiler(directiveName, matchedToken);
                break;
            case DT_ALIGN:
                match(TT_INTEGER);
                context.setAlignment(Long.decode(matchedToken.getContent()));
                compiler = new EmptyCompiler(directiveName, matchedToken);
                break;
            case DT_SPACE:
                EvaluatorToLong expression = integerExpression();
                long space = 0;
                try {
                    space = expression.evaluate();
                } catch (LabelNotDefinedException e) {
                    reportError(E_LABEL_NOT_DEFINED_PRIOR_USE, e.getToken());
                }
                compiler = new SpaceDirectiveCompiler(new SpaceEvaluator(space));
                break;
            case DT_BYTE:
                integerOperands(integerOperands);
                compiler = IntegerExpressionDirectiveCompiler.createCompiler(8, integerOperands);
                break;
            case DT_WORD:
                integerOperands(integerOperands);
                compiler = IntegerExpressionDirectiveCompiler.createCompiler(32, integerOperands);
                break;
            case DT_FLOAT:
                floatOperands(floatOperands, false);
                compiler = FloatingPointDirectiveCompiler.createCompiler(32, floatOperands);
                break;
            case DT_DOUBLE:
                floatOperands(floatOperands, true);
                compiler = FloatingPointDirectiveCompiler.createCompiler(64, floatOperands);
                break;
            case DT_ASCII:
                match(TT_STRING);
                compiler = new StringDirectiveCompiler(matchedToken);
                break;
            case DT_ASCIIZ:
                match(TT_STRING);
                Token string = new SourceToken(matchedToken);
                string.setContent(string.getContent() + '\0');
                compiler = new StringDirectiveCompiler(string);
                break;
            default:
                reportError(E_NOT_IMPLEMENTED_DIRECTIVE, directiveName);
        }
        return new ElementCompiler(compiler);
    }

    private void memoryBlockSpecifiers(TokenType type) throws TokenException {
        String memoryBlock = "";
        long address = 0;
        switch(type) {
            case DT_CODE:
                memoryBlock = "CODE";
                address = SystemConstants.DEFAULT_CODE_START;
                break;
            case DT_DATA:
                memoryBlock = "DATA";
                address = SystemConstants.DEFAULT_DATA_START;
                break;
            default:
                reportError(E_NOT_IMPLEMENTED_DIRECTIVE, null, type);
        }
        if (LA(1) == TT_INTEGER) {
            match(TT_INTEGER);
            try {
                address = Integer.decode(matchedToken.getContent());
                if (address < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                reportError(E_ADDRESS_OUT_OF_RANGE, matchedToken);
            }
        }
        context.setCurrentMemoryBlock(memoryBlock);
        context.setCurrentAddress(address);
    }

    private void integerOperands(ArrayList<IntegerExpressionEvaluator> operands) throws TokenException {
        IntegerExpressionEvaluator operand = integerExpression();
        operands.add(operand);
        while (true) {
            if (LA(1) == TT_COMMA) {
                match(TT_COMMA);
                operand = integerExpression();
                operands.add(operand);
            } else {
                break;
            }
        }
    }

    private void floatOperands(ArrayList<FloatingPointEvaluator> operands, boolean doubleValue) throws TokenException {
        FloatingPointEvaluator operand = null;
        operand = matchFloat(doubleValue);
        operands.add(operand);
        while (true) {
            if (LA(1) == TT_COMMA) {
                match(TT_COMMA);
                operand = matchFloat(doubleValue);
                operands.add(operand);
            } else {
                break;
            }
        }
    }

    @SuppressWarnings("fallthrough")
    private FloatingPointEvaluator matchFloat(boolean doubleValue) throws TokenException {
        boolean negative = false;
        Token token;
        switch(LA(1)) {
            case TT_MINUS:
                negative = true;
            case TT_PLUS:
                match(LA(1));
        }
        switch(LA(1)) {
            case TT_FLOATING_POINT:
            case TT_INTEGER:
                match(LA(1));
                break;
            default:
                reportError(E_UNEXPECTED_TOKEN, LT(1), TT_FLOATING_POINT);
        }
        if (negative) {
            token = new SourceToken(matchedToken);
            token.setContent("-" + token.getContent());
        } else {
            token = matchedToken;
        }
        if (doubleValue) {
            return new DoubleFloatEvaluator(token);
        } else {
            return new SingleFloatEvaluator(token);
        }
    }

    private ElementCompiler labelDefinition() throws TokenException {
        match(TT_LABEL_DEFINITION);
        try {
            context.addLabel(matchedToken.getContent());
        } catch (IllegalArgumentException e) {
            reportError(E_LABEL_ALREADY_DEFINED, matchedToken, false);
        }
        return new ElementCompiler(new EmptyCompiler(matchedToken));
    }

    private ElementCompiler comment() throws TokenException {
        match(TT_COMMENT);
        return new ElementCompiler(new EmptyCompiler(matchedToken));
    }

    private void instructionOperand(TokenType type, InstructionDefinition instruction, ArrayList<EvaluatorToLong> operands) throws TokenException {
        switch(type) {
            case TT_GPR:
            case TT_SFPR:
            case TT_DFPR:
            case TT_VCR:
            case TT_VM:
            case TT_VLR:
                match(type);
                operands.add(new RegisterEvaluator(matchedToken));
                break;
            case TT_SIGNED_EXPRESSION:
            case TT_UNSIGNED_EXPRESSION:
                operands.add(integerExpression());
                break;
            case TT_OFFSET:
                IntegerEvaluator address = new IntegerEvaluator("" + (context.getCurrentAddress() + instruction.getCompiledLength()));
                operands.add(new OffsetEvaluator(integerExpression(), address));
                break;
            case TT_ADDRESS:
                address(operands);
                break;
            default:
                reportError(E_UNKNOWN_OPERAND, LT(1), true);
        }
    }

    private void address(ArrayList<EvaluatorToLong> operands) throws TokenException {
        EvaluatorToLong expressionPart = null;
        EvaluatorToLong registerPart = null;
        registerPart = registerIndirect();
        if (registerPart == null) {
            expressionPart = expressionInAddress();
        }
        if ((registerPart == null) && (expressionPart == null)) {
            reportError(E_UNEXPECTED_TOKEN, LT(1), TT_ADDRESS);
        }
        if ((registerPart != null) && (expressionPart == null)) {
            expressionPart = expressionInAddress();
        }
        if ((expressionPart != null) && (registerPart == null)) {
            registerPart = registerIndirect();
        }
        if (expressionPart == null) {
            expressionPart = new IntegerEvaluator("0");
        }
        if (registerPart == null) {
            registerPart = new IntegerEvaluator("0");
        }
        operands.add(expressionPart);
        operands.add(registerPart);
    }

    private EvaluatorToLong registerIndirect() throws TokenException {
        if ((LAcheck(1, TT_LEFT_PARENTHESIS)) && (LAcheck(2, TT_GPR))) {
            Token registerToken;
            match(TT_LEFT_PARENTHESIS);
            match(TT_GPR);
            registerToken = matchedToken;
            match(TT_RIGHT_PARENTHESIS);
            return new RegisterEvaluator(registerToken);
        }
        return null;
    }

    private EvaluatorToLong expressionInAddress() throws TokenException {
        switch(LA(1)) {
            case TT_PLUS:
            case TT_MINUS:
            case TT_HASH:
            case TT_IDENTIFIER:
            case TT_LEFT_PARENTHESIS:
            case TT_INTEGER:
                return integerExpression();
        }
        return null;
    }

    private IntegerExpressionEvaluator integerExpression() throws TokenException {
        Token first = LT(1);
        EvaluatorToLong result = integerExpressionRest(integerFactor());
        Token last = matchedToken;
        return new IntegerExpressionEvaluator(result, first, last);
    }

    private EvaluatorToLong integerExpressionRest(EvaluatorToLong temp) throws TokenException {
        EvaluatorToLong result;
        switch(LA(1)) {
            case TT_PLUS:
            case TT_MINUS:
            case TT_BINARY_AND:
            case TT_BINARY_OR:
            case TT_BINARY_XOR:
            case TT_MODULO:
                match(LA(1));
                result = integerExpressionRest(new BinaryOperator(matchedToken, temp, integerTerm()));
                break;
            default:
                result = integerTermRest(temp);
        }
        return result;
    }

    private EvaluatorToLong integerTerm() throws TokenException {
        return integerTermRest(integerFactor());
    }

    private EvaluatorToLong integerTermRest(EvaluatorToLong temp) throws TokenException {
        EvaluatorToLong result;
        switch(LA(1)) {
            case TT_TIMES:
            case TT_DIVIDE:
            case TT_LEFT_SHIFT:
            case TT_RIGHT_SHIFT:
                match(LA(1));
                result = integerExpressionRest(new BinaryOperator(matchedToken, temp, integerFactor()));
                break;
            default:
                result = temp;
        }
        return result;
    }

    private EvaluatorToLong integerFactor() throws TokenException {
        EvaluatorToLong result = null;
        switch(LA(1)) {
            case TT_LEFT_PARENTHESIS:
                match(TT_LEFT_PARENTHESIS);
                result = integerExpression();
                match(TT_RIGHT_PARENTHESIS);
                break;
            case TT_IDENTIFIER:
                match(LA(1));
                result = new IdentifierEvaluator(matchedToken, context);
                break;
            case TT_HASH:
            case TT_PLUS:
            case TT_MINUS:
            case TT_INTEGER:
                result = integer();
                break;
            default:
                reportError(E_UNEXPECTED_TOKEN, LT(1), TT_INTEGER, TT_IDENTIFIER, TT_LEFT_PARENTHESIS);
        }
        return result;
    }

    private EvaluatorToLong integer() throws TokenException {
        EvaluatorToLong result = null;
        switch(LA(1)) {
            case TT_HASH:
                match(TT_HASH);
                result = integer();
                break;
            case TT_PLUS:
                match(TT_PLUS);
                result = integerFactor();
                break;
            case TT_MINUS:
                match(TT_MINUS);
                result = new NegatingEvaluator(integerFactor());
                break;
            case TT_INTEGER:
                match(TT_INTEGER);
                result = new IntegerEvaluator(matchedToken);
                break;
            default:
                reportError(E_UNEXPECTED_TOKEN, LT(1), TT_INTEGER);
        }
        return result;
    }

    private void match(TokenType type) throws TokenException {
        if (LAcheck(1, type)) {
            matchedToken = LT(1);
            consume();
            return;
        }
        reportError(E_UNEXPECTED_TOKEN, LT(1), type);
    }

    private void matchInto(TokenType type, ArrayList<Token> tokens) throws TokenException {
        match(type);
        tokens.add(matchedToken);
    }

    private TokenType LA(int i) {
        return input.LA(i);
    }

    private boolean LAcheck(int i, TokenType type) {
        if (type == LA(i)) {
            return true;
        }
        if (Keywords.isKeywordType(LT(i).getContent(), type)) {
            return true;
        }
        return false;
    }

    private Token LT(int i) {
        return input.LT(i);
    }

    private void consume() throws TokenException {
        input.consume();
    }

    private void reportError(ErrorMessage error, Token token, boolean includeTokenString) throws TokenException {
        throw new ParsingException(error, token, includeTokenString);
    }

    private void reportError(ErrorMessage error, Token token, TokenType... expectedTokens) throws TokenException {
        StringBuffer errorString = new StringBuffer();
        errorString.append(error);
        int length = expectedTokens.length;
        if (length > 0) {
            errorString.append(", expecting one of:\n-- ");
            for (int i = 0; i < length - 1; i++) {
                errorString.append(expectedTokens[i]);
                errorString.append("\n-- ");
            }
            errorString.append(expectedTokens[length - 1]);
            errorString.append("\n  ");
        }
        throw new ParsingException(errorString.toString(), token);
    }
}
