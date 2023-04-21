package org.jnormalform;

import org.jnormalform.ui.IUser;

public class HappyUser implements IUser {

    private final String answer;

    public HappyUser(String answer) {
        super();
        this.answer = answer;
    }

    public String askWhatCodeDoes(final String code) {
        return answer;
    }
}
