package org.scribble.conversation.parser;

import org.scribble.extensions.*;
import org.scribble.model.*;
import org.scribble.model.admin.ModelIssue;
import org.scribble.model.admin.ModelListener;
import org.scribble.conversation.model.*;
import org.scribble.parser.*;

/**
 * This class provides the parser rule for the ConversationInteraction.
 * 
 */
@RegistryInfo(extension = ParserRule.class, notation = ConversationNotation.NOTATION_CODE)
public class ConversationInteractionParserRule extends org.scribble.parser.InteractionParserRule {

    /**
	 * The default constructor.
	 */
    public ConversationInteractionParserRule() {
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
        return (type.isAssignableFrom(ConversationInteraction.class));
    }

    /**
	 * This method creates a new interaction instance.
	 * 
	 * @return The interaction instance
	 */
    protected Interaction createInteraction() {
        return (new ConversationInteraction());
    }

    /**
	 * This method parses the set of tokens associated with an interaction
	 * based on the supplied keyword.
	 * 
	 * @param keyword The keyword
	 * @param interaction The interaction being constructed
	 * @param context The context
	 * @param l The model listener
	 * @return Whether the supplied keyword was processed
	 */
    protected boolean parseKeyword(String keyword, Interaction interaction, ParserContext context, ModelListener l) {
        boolean f_processed = false;
        if (keyword.equals("from")) {
            context.nextToken();
            Token roleOrVar = context.nextToken();
            Token atVar = null;
            if (roleOrVar.getType() == TokenType.Identifier) {
                Token t = context.lookahead(0);
                if (t.isToken("@", TokenType.Symbol)) {
                    context.nextToken();
                    atVar = context.lookahead(0);
                    if (atVar.getType() != TokenType.Identifier) {
                        atVar = null;
                    } else {
                        context.nextToken();
                    }
                }
                Object state = context.getState(roleOrVar.getText());
                if (state == null) {
                    l.error(new ModelIssue(roleOrVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.conversation.parser.Messages"), "_UNKNOWN_VARIABLE_OR_ROLE", new String[] { roleOrVar.getText() })));
                } else if (state instanceof Variable) {
                    ((ConversationInteraction) interaction).setFromVariable((Variable) state);
                    if (atVar != null) {
                        state = context.getState(atVar.getText());
                        if (state == null) {
                            l.error(new ModelIssue(atVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.parser.Messages"), "_UNKNOWN_ROLE", new String[] { atVar.getText() })));
                        } else if (state instanceof Role) {
                            Role r = new Role();
                            r.setName(atVar.getText());
                            r.derivedFrom(atVar);
                            ((ConversationInteraction) interaction).setFromRole(r);
                        } else {
                            l.error(new ModelIssue(atVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.parser.Messages"), "_REQUIRED_ROLE", new String[] { atVar.getText() })));
                        }
                    }
                } else if (atVar != null) {
                    l.error(new ModelIssue(roleOrVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.conversation.parser.Messages"), "_REQUIRED_VARIABLE", new String[] { roleOrVar.getText() })));
                } else if (state instanceof Role) {
                    Role r = new Role();
                    r.setName(roleOrVar.getText());
                    r.derivedFrom(roleOrVar);
                    interaction.setFromRole(r);
                    if (atVar != null) {
                        l.error(new ModelIssue(atVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.conversation.parser.Messages"), "_REQUIRED_VARIABLE", new String[] { atVar.getText() })));
                    }
                } else {
                    l.error(new ModelIssue(roleOrVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.conversation.parser.Messages"), "_REQUIRED_VARIABLE_OR_ROLE", new String[] { roleOrVar.getText() })));
                }
                f_processed = true;
            }
        } else if (keyword.equals("to")) {
            context.nextToken();
            Token roleOrVar = context.nextToken();
            Token atVar = null;
            if (roleOrVar.getType() == TokenType.Identifier) {
                Token t = context.lookahead(0);
                if (t.isToken("@", TokenType.Symbol)) {
                    context.nextToken();
                    atVar = context.lookahead(0);
                    if (atVar.getType() != TokenType.Identifier) {
                        atVar = null;
                    } else {
                        context.nextToken();
                    }
                }
                Object state = context.getState(roleOrVar.getText());
                if (state == null) {
                    l.error(new ModelIssue(roleOrVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.conversation.parser.Messages"), "_UNKNOWN_VARIABLE_OR_ROLE", new String[] { roleOrVar.getText() })));
                } else if (state instanceof Variable) {
                    ((ConversationInteraction) interaction).setToVariable((Variable) state);
                    if (atVar != null) {
                        state = context.getState(atVar.getText());
                        if (state == null) {
                            l.error(new ModelIssue(atVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.parser.Messages"), "_UNKNOWN_ROLE", new String[] { atVar.getText() })));
                        } else if (state instanceof Role) {
                            Role r = new Role();
                            r.setName(atVar.getText());
                            r.derivedFrom(atVar);
                            ((ConversationInteraction) interaction).setToRole(r);
                        } else {
                            l.error(new ModelIssue(atVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.parser.Messages"), "_REQUIRED_ROLE", new String[] { atVar.getText() })));
                        }
                    }
                } else if (atVar != null) {
                    l.error(new ModelIssue(roleOrVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.conversation.parser.Messages"), "_REQUIRED_VARIABLE", new String[] { roleOrVar.getText() })));
                } else if (state instanceof Role) {
                    Role r = new Role();
                    r.setName(roleOrVar.getText());
                    r.derivedFrom(roleOrVar);
                    interaction.setToRole(r);
                    if (atVar != null) {
                        l.error(new ModelIssue(atVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.conversation.parser.Messages"), "_REQUIRED_VARIABLE", new String[] { atVar.getText() })));
                    }
                } else {
                    l.error(new ModelIssue(roleOrVar, org.scribble.util.MessageUtil.format(java.util.PropertyResourceBundle.getBundle("org.scribble.conversation.parser.Messages"), "_REQUIRED_VARIABLE_OR_ROLE", new String[] { roleOrVar.getText() })));
                }
                f_processed = true;
            }
        } else {
            f_processed = super.parseKeyword(keyword, interaction, context, l);
        }
        return (f_processed);
    }
}
