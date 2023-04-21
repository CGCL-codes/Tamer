package mini.java;

import java.util.HashMap;
import java.util.Map;
import mini.java.fa.NFAState;

public final class TestHelperV2 {

    public static final char INITIAL_STATE = 'A';

    public Map<Character, NFAState> _states = new HashMap<Character, NFAState>();

    /**
     * Helper function used to get the NFAState associated with the given character.
     */
    public NFAState getNFAState(Character c_) {
        return _states.get(c_);
    }

    /**
     * Helper function used to create NFA states from the string representations.
     * @param states_
     */
    public void addNFAStates(String states_) {
        assert (states_ != null);
        assert (!states_.isEmpty());
        for (Character c : states_.toCharArray()) {
            _states.put(c, new NFAState());
        }
    }

    /**
     * Helper function used to create transitions from the string representation.
     */
    public void addTransitions(String transitions_) {
        assert (transitions_ != null);
        for (String transition : transitions_.split(",")) {
            if (!transition.isEmpty()) {
                char[] C = transition.toCharArray();
                NFAState S0 = getNFAState(C[0]);
                NFAState S1 = getNFAState(C[1]);
                if (C.length == 3) {
                    S0.addTransition(S1, C[2]);
                } else {
                    S0.addTransition(S1);
                }
            }
        }
    }
}
