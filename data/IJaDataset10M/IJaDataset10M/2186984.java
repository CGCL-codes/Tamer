package org.deved.antlride.antlr.internal.runtime3_1;

import org.antlr.tool.ANTLRLexer;
import org.deved.antlride.core.AntlrConstants;
import org.deved.antlride.core.model.IAntlrCodeModel;
import org.deved.antlride.core.model.IAntlrToken;
import antlr.Token;
import antlr.TokenWithIndex;

public class AntlrLexer31 extends ANTLRLexer {

    private IAntlrToken previous;

    private IAntlrCodeModel codeModel;

    private int internalLine = -1;

    public AntlrLexer31(IAntlrCodeModel codeModel) {
        super(codeModel.getReader());
        setTokenObjectClass("antlr.TokenWithIndex");
        this.codeModel = codeModel;
    }

    @Override
    public void setLine(int line) {
        if (internalLine < 0) {
            internalLine = getLine();
        }
        super.setLine(line);
    }

    protected Token createToken(int type) {
        try {
            TokenWithIndex oToken = new TokenWithIndex();
            IAntlrToken token = (IAntlrToken) oToken;
            if (previous != null) {
                previous.setNext(token);
            }
            token.setPrevious(previous);
            previous = token;
            token.setType(type);
            if (type == SL_COMMENT || type == COMMENT) {
                token.setPartition(AntlrConstants.SINGLE_LINE_PARTITION);
            } else if (type == ML_COMMENT) {
                token.setPartition(AntlrConstants.MULTI_LINE_PARTITION);
            } else if (type == DOC_COMMENT) {
                token.setPartition(AntlrConstants.DOC_PARTITION);
            }
            token.setColumn(inputState.getTokenStartColumn());
            token.setLine(inputState.getTokenStartLine());
            return oToken;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return Token.badToken;
    }

    @Override
    protected Token makeToken(int type) {
        Token token = createToken(type);
        if (token != Token.badToken) {
            int offset = 0;
            int line;
            if (internalLine > 0) {
                line = codeModel.currentLine();
            } else {
                line = inputState.getTokenStartLine();
            }
            if (line > 1) {
                offset = codeModel.lineLength(line);
            }
            ((IAntlrToken) token).setOffset(offset);
        }
        return token;
    }
}
