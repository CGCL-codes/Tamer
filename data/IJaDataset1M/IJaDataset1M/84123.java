package org.deveel.csharpcc.parser;

import java.util.*;

public class Nfa {

    NfaState start;

    NfaState end;

    public Nfa() {
        start = new NfaState();
        end = new NfaState();
    }

    public Nfa(NfaState startGiven, NfaState finalGiven) {
        start = startGiven;
        end = finalGiven;
    }
}
