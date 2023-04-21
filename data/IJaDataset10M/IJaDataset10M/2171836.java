package org.deved.antlride.stringtemplate.internal.ui.text.rules;

import org.deved.antlride.common.ui.text.partitions.rules.AntlrBlockPartition;
import org.deved.antlride.stringtemplate.internal.ui.text.partitions.StringTemplatePartitions;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class StringTemplateTemplateRule extends AntlrBlockPartition {

    public StringTemplateTemplateRule(IToken successToken) {
        super(successToken, '<', '>');
    }

    @Override
    protected IToken doEvaluate(ICharacterScanner scanner) {
        consumeOne(scanner);
        String text = getTextReaded();
        if (text.equals("<")) {
            consumeWS(scanner);
            consumeOne(scanner);
            text = getTextReaded();
            if (!text.equals("\\") && !text.equals("<") && !text.equals("@")) {
                IToken block = doBlock(scanner);
                return block;
            }
        }
        unread(scanner);
        return Token.UNDEFINED;
    }

    @Override
    protected String[] isInIgnoreMode(String ch, ICharacterScanner scanner) {
        if (ch.equals("\"")) {
            consumeOne(scanner);
            String nextChar = getTextReaded();
            unread(scanner, 1);
            if (nextChar.equals("<")) {
                return new String[] { "\"<", ">\"", null };
            }
        }
        String[] pattern = super.isInIgnoreMode(ch, scanner);
        if (pattern != null) return pattern;
        if (ch.equals("<")) {
            consumeOne(scanner);
            String nextChar = getTextReaded();
            unread(scanner, 1);
            if (nextChar.equals("!")) {
                return new String[] { "<!", "!>", null };
            }
        }
        if (ch.equals("$")) {
            consumeOne(scanner);
            String nextChar = getTextReaded();
            unread(scanner, 1);
            if (nextChar.equals("!")) {
                return new String[] { "$!", "!$", null };
            }
        }
        return null;
    }

    protected String getPartitionName() {
        return StringTemplatePartitions.TEMPLATE_BODY;
    }
}
