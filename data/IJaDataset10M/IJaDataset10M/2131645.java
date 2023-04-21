package org.springframework.webflow;

import org.springframework.core.enums.StaticLabeledEnum;

/**
 * Supported scope types for the web flow system.
 * 
 * @author Keith Donald
 * @author Erwin Vervaet
 */
public abstract class ScopeType extends StaticLabeledEnum {

    /**
	 * Constant indicating request scope. Data in request scope lives for the
	 * life of a request submitted to a flow execution for processing.
	 */
    public static final ScopeType REQUEST = new ScopeType(0, "Request") {

        public AttributeMap getScope(RequestContext context) {
            return context.getRequestScope();
        }
    };

    /**
	 * Constant indicating flow scope. Data in flow scope is shared by all
	 * artifacts of exactly one flow definition (actions, view, states, etc.)
	 * and lives locally for the life of a executing flow session.
	 */
    public static final ScopeType FLOW = new ScopeType(1, "Flow") {

        public AttributeMap getScope(RequestContext context) {
            return context.getFlowScope();
        }
    };

    /**
	 * Constant indicating conversation scope. Data in conversation scope is
	 * shared by all flow sessions associated with a flow execution, and lives
	 * for the life of the entire flow execution (repersenting a single logical
	 * conversation).
	 */
    public static final ScopeType CONVERSATION = new ScopeType(2, "Conversation") {

        public AttributeMap getScope(RequestContext context) {
            return context.getConversationScope();
        }
    };

    /**
	 * Private constructor because this is a typesafe enum!
	 */
    private ScopeType(int code, String label) {
        super(code, label);
    }

    public Class getType() {
        return ScopeType.class;
    }

    /**
	 * Returns the <code>Scope</code> associated with this scope type for an
	 * executing flow execution request.
	 * @param context a flow execution request context
	 * @return the scope
	 */
    public abstract AttributeMap getScope(RequestContext context);
}
