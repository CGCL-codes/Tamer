package com.google.code.rees.scope.struts2;

import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.StrutsStatics;
import com.google.code.rees.scope.ScopeAdapterFactory;
import com.google.code.rees.scope.conversation.ConversationAdapter;
import com.google.code.rees.scope.conversation.context.ConversationContextManager;
import com.google.code.rees.scope.conversation.context.HttpConversationContextManagerFactory;
import com.google.code.rees.scope.session.SessionAdapter;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;

/**
 * Struts2 implementation of the {@link ScopeAdapterFactory}.
 * 
 * @author rees.byars
 */
public class StrutsScopeAdapterFactory implements ScopeAdapterFactory {

    private static final long serialVersionUID = -4595690103120891078L;

    protected HttpConversationContextManagerFactory conversationContextManagerFactory;

    @Inject(StrutsScopeConstants.CONVERSATION_CONTEXT_MANAGER_FACTORY)
    public void setHttpConversationContextManagerFactory(HttpConversationContextManagerFactory conversationContextManagerFactory) {
        this.conversationContextManagerFactory = conversationContextManagerFactory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SessionAdapter createSessionAdapter() {
        return new StrutsSessionAdapter(ActionContext.getContext().getActionInvocation());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ConversationAdapter createConversationAdapter() {
        ActionContext actionContext = ActionContext.getContext();
        HttpServletRequest request = (HttpServletRequest) actionContext.get(StrutsStatics.HTTP_REQUEST);
        ConversationContextManager contextManager = this.conversationContextManagerFactory.getManager(request);
        ActionInvocation invocation = actionContext.getActionInvocation();
        return new StrutsConversationAdapter(invocation, contextManager);
    }
}
