package com.leantell.lp;

import java.util.Iterator;

/**
 * @author ozgur.tumer@gmail.com
 */
public class AlternativeParseNode extends ParseNode {

    private int runningConsumedTokensSize = 0;

    public AlternativeParseNode(Pattern pattern) {
        super(pattern);
    }

    @Override
    public String toString() {
        return "AlternativeParseNode - " + super.toString();
    }

    @Override
    public void addChild(ParseNode childToAdd) {
        int consumedTokensSize = childToAdd.getConsumedTokens().size();
        if (consumedTokensSize >= runningConsumedTokensSize) {
            runningConsumedTokensSize = Math.max(runningConsumedTokensSize, consumedTokensSize);
            clearUselessAlternatives(runningConsumedTokensSize);
            children.add(childToAdd);
        }
    }

    private void clearUselessAlternatives(int runningConsumedTokensSize) {
        for (Iterator iterator = children.iterator(); iterator.hasNext(); ) {
            ParseNode child = (ParseNode) iterator.next();
            if (child.getConsumedTokens().size() < runningConsumedTokensSize) iterator.remove();
        }
    }

    @Override
    public boolean isIncomplete() {
        return true;
    }
}
