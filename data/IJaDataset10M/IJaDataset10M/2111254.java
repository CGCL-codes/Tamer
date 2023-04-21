package org.scribble.conversation.parser;

import org.scribble.extensions.RegistryInfo;
import org.scribble.model.*;
import org.scribble.model.admin.ModelListener;
import org.scribble.parser.*;
import org.scribble.conversation.model.*;

/**
 * This class provides the parser rule for the TryEscape construct.
 * 
 */
@RegistryInfo(extension = ParserRule.class, notation = ConversationNotation.NOTATION_CODE)
public class TryEscapeParserRule extends org.scribble.parser.AbstractParserRule {

    /**
	 * The default constructor.
	 */
    public TryEscapeParserRule() {
        super(ParserRuleType.Group);
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
        return (type.isAssignableFrom(TryEscape.class));
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
        TryEscape ret = null;
        Token t = context.lookahead(0);
        if (t.isToken("try", TokenType.Keyword)) {
            int startpos = t.getSource().getStartPosition();
            context.nextToken();
            ret = new TryEscape();
            ret.getSource().setStartPosition(startpos);
            ret.getBlock().getSource().setStartPosition(t.getSource().getStartPosition());
            parseGroup(context, l, ret.getBlock(), true);
            t = context.lookahead(0);
            ret.getBlock().getSource().setEndPosition(t.getSource().getStartPosition());
            EscapeBlock eb = null;
            while ((eb = (EscapeBlock) context.parse(EscapeBlock.class, l)) != null) {
                ret.getEscapeBlocks().add(eb);
            }
            t = context.lookahead(0);
            ret.getSource().setEndPosition(t.getSource().getStartPosition());
        }
        return (ret);
    }
}
