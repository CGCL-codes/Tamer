package org.scribble.parser;

import org.scribble.extensions.RegistryInfo;
import org.scribble.model.*;
import org.scribble.model.admin.ModelIssue;
import org.scribble.model.admin.ModelListener;

/**
 * This class provides the parser rule for the Model Name.
 */
@RegistryInfo(extension = ParserRule.class)
public class ModelNameParserRule extends AbstractParserRule {

    /**
	 * This is the default constructor.
	 */
    public ModelNameParserRule() {
        super(ParserRuleType.Clause);
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
        return (type.isAssignableFrom(LocatedName.class));
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
        LocatedName ret = null;
        Token t = context.lookahead(0);
        if (t.getType() == TokenType.Identifier) {
            int startpos = t.getSource().getStartPosition();
            int endpos = t.getSource().getEndPosition();
            context.nextToken();
            ret = new LocatedName();
            ret.setName(t.getText());
            t = context.lookahead(0);
            if (t.isToken(ModelReference.LOCATED_REFERENCE_SEPARATOR, TokenType.Symbol)) {
                context.nextToken();
                t = context.lookahead(0);
                if (t.getType() == TokenType.Identifier) {
                    endpos = t.getSource().getEndPosition();
                    context.nextToken();
                    Role p = new Role();
                    p.setName(t.getText());
                    p.getSource().setStartPosition(t.getSource().getStartPosition());
                    p.getSource().setEndPosition(t.getSource().getEndPosition());
                    ret.setRole(p);
                } else {
                    l.error(new ModelIssue(t, java.util.PropertyResourceBundle.getBundle("org.scribble.parser.Messages").getString("_EXPECTING_ROLE_NAME")));
                }
            }
            ret.getSource().setStartPosition(startpos);
            ret.getSource().setEndPosition(endpos);
        } else {
            l.error(new ModelIssue(t, java.util.PropertyResourceBundle.getBundle("org.scribble.parser.Messages").getString("_EXPECTING_IDENTIFIER")));
        }
        return (ret);
    }
}
