package org.deved.antlride.internal.core.antlr;

import org.antlr.tool.ANTLRTokenTypes;
import org.deved.antlride.internal.core.util.AntlrTextHelper;
import antlr.TokenWithIndex;

public class AntlrToken extends TokenWithIndex implements ANTLRTokenTypes {

    private int offset;

    int partition;

    AntlrToken previous;

    AntlrToken next;

    public AntlrToken getPrevious() {
        return getPrevious(true);
    }

    public AntlrToken getNext() {
        return getNext(true);
    }

    public boolean isIgnored() {
        return isIgnored(getType());
    }

    public static boolean isIgnored(int type) {
        return type == WS || type == ML_COMMENT || type == SL_COMMENT || type == COMMENT || type == SRC;
    }

    public AntlrToken getPrevious(boolean discardIgnored) {
        if (discardIgnored) {
            AntlrToken t = previous;
            while (t != null && t.isIgnored()) {
                t = t.previous;
            }
            return t;
        }
        return previous;
    }

    public AntlrToken getNext(boolean discardIgnored) {
        if (discardIgnored) {
            AntlrToken t = next;
            while (t != null && t.isIgnored()) {
                t = t.next;
            }
            return t;
        }
        return next;
    }

    public AntlrToken(int index, String text) {
        super(index, text);
    }

    public AntlrToken() {
    }

    public static AntlrToken clone(AntlrToken token) {
        AntlrToken clone = new AntlrToken();
        clone.setColumn(token.getColumn());
        clone.setFilename(token.getFilename());
        clone.setIndex(token.getIndex());
        clone.setLine(token.getLine());
        clone.setText(token.getText());
        clone.setType(token.getType());
        return clone;
    }

    public AntlrToken clone() {
        return clone(this);
    }

    public int getOffset() {
        return offset + getColumn();
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int sourceStart() {
        return getOffset();
    }

    public int sourceEnd() {
        String text = getText();
        if (text == null) {
            text = "";
        }
        return sourceStart() + text.length();
    }

    public int getPartition() {
        return partition;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("p=");
        builder.append(AntlrTextHelper.getPartitionName(getPartition()));
        builder.append("; t=");
        builder.append(AntlrParser._tokenNames[getType()]);
        builder.append("; i=");
        builder.append(getIndex());
        builder.append("; l=");
        builder.append(getLine());
        builder.append("; c=");
        builder.append(getColumn());
        builder.append("; [");
        builder.append(sourceStart());
        builder.append("...");
        builder.append(sourceEnd());
        builder.append("]->");
        builder.append(getText().trim());
        return builder.toString();
    }
}
