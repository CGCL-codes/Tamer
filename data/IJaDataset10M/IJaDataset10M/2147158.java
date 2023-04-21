package org.echarts.test.sip.matchers;

import org.echarts.test.sip.AbstractInviteAgent;
import org.echarts.test.sip.BaseState;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class StateMatcher extends TypeSafeMatcher<AbstractInviteAgent> {

    private BaseState[] states;

    private AbstractInviteAgent agent;

    public StateMatcher(BaseState... states) {
        this.states = states;
    }

    @Override
    public boolean matchesSafely(AbstractInviteAgent agent) {
        this.agent = agent;
        for (BaseState state : states) {
            if (agent.getCurrentState() == state) {
                return true;
            }
        }
        return false;
    }

    public void describeTo(Description description) {
        description.appendText("agent " + agent.getName() + " is in one of these states - ");
        for (BaseState state : states) {
            description.appendText(state + " ");
        }
        description.appendText(" curr state = " + agent.getCurrentState());
    }
}
