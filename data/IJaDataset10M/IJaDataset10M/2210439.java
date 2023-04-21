package org.scribble.protocol.parser;

import org.scribble.extensions.RegistryInfo;
import org.scribble.model.*;
import org.scribble.model.admin.ModelIssue;
import org.scribble.model.admin.ModelListener;
import org.scribble.parser.*;
import org.scribble.protocol.model.*;

/**
 * This class provides the parser rule for the Recur construct.
 * 
 */
@RegistryInfo(extension = ParserRule.class, notation = ProtocolNotation.NOTATION_CODE)
public class RecurParserRule extends org.scribble.parser.AbstractParserRule {

    /**
	 * The default constructor.
	 */
    public RecurParserRule() {
        super(ParserRuleType.Line);
    }

    /**
	 * This method determines whether the parser rules is
	 * appropriate to return a model object of the required
	 * type.
	 * 
	 * @param type The required type
	 * @return Whether the parser rule returns a
	 * 				model object of the required type
	 */
    public boolean isSupported(Class<?> type) {
        return (type.isAssignableFrom(Recur.class));
    }

    /**
	 * This method parses the description to obtain
	 * a model object of the appropriate type.
	 * 
	 * @param context The context
	 * @param l The listener
	 * @return The model object
	 */
    public Object parse(ParserContext context, ModelListener l) {
        Recur ret = null;
        Token t = context.lookahead(0);
        if (t.isToken("recur", TokenType.Keyword)) {
            int startpos = t.getSource().getStartPosition();
            ret = new Recur();
            context.nextToken();
            t = context.lookahead(0);
            if (t.getType() == TokenType.Identifier) {
                Object defn = context.getState(t.getText());
                if (defn instanceof RecursionBlock) {
                    context.nextToken();
                    ret.setRecursionBlock((RecursionBlock) defn);
                } else {
                    l.error(new ModelIssue(t, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.protocol.parser.Messages"), "_UNKNOWN_RECUR_LABEL", new String[] { t.getText() })));
                }
            } else {
                l.error(new ModelIssue(t, java.util.PropertyResourceBundle.getBundle("org.scribble.parser.Messages").getString("_EXPECTING_IDENTIFIER")));
            }
            ret.getSource().setStartPosition(startpos);
            ret.getSource().setEndPosition(t.getSource().getEndPosition());
        }
        return (ret);
    }
}
