package com.kamkor.interviewer.client.presenter;

import com.google.gwt.user.client.ui.HasWidgets;
import com.kamkor.interviewer.client.security.Authentication;

/**
 * When go is called, user is being authenticated by Authenticated Impl. If authentication succeeded then
 * goAuthenticated method is called. Implement this method just like regular go,
 * for example: bind, clear, add widget. 
 * 
 * If authentication failed then authenticationFailed is called - display error page,
 * revert history etc. 
 * 
 * @author kamkor
 *
 */
public abstract class SecuredPresenter implements SecurePresenter {

    /** It authenticates user and calls this object authenticationSucceded method */
    private Authentication authentication;

    /** Widgets container, initialized in go method */
    private HasWidgets container;

    private Long interviewId;

    private Long key;

    /**
	 * 
	 * @param authentication authenticates user and calls this object authenticationSucceded method
	 * @param interviewId
	 * @param key
	 */
    public SecuredPresenter(Authentication authentication, Long interviewId, Long key) {
        this.authentication = authentication;
        this.interviewId = interviewId;
        this.key = key;
    }

    @Override
    public final void go(HasWidgets container) {
        this.container = container;
        authentication.authenticate(interviewId, key, this);
    }

    @Override
    public final void authenticationSucceeded() {
        goAuthenticated(container);
    }

    protected Long getInterviewId() {
        return interviewId;
    }

    protected Long getKey() {
        return key;
    }

    protected abstract void goAuthenticated(HasWidgets container);
}
