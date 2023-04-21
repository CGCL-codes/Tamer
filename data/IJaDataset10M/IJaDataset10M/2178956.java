package org.granite.tide.jcdi;

import java.io.Serializable;
import javax.enterprise.context.ConversationScoped;

/**
 * @author William DRAI
 */
@ConversationScoped
public class ConversationState implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean firstCall;

    public boolean isFirstCall() {
        return firstCall;
    }

    public void setFirstCall(boolean firstCall) {
        this.firstCall = firstCall;
    }
}
