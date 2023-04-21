package fitnesse.revisioncontrol.clearcase;

import static fitnesse.revisioncontrol.RevisionControlOperation.ADD;
import static fitnesse.revisioncontrol.RevisionControlOperation.CHECKIN;
import static fitnesse.revisioncontrol.RevisionControlOperation.CHECKOUT;
import static fitnesse.revisioncontrol.RevisionControlOperation.DELETE;
import static fitnesse.revisioncontrol.RevisionControlOperation.REVERT;
import static fitnesse.revisioncontrol.RevisionControlOperation.UPDATE;
import java.util.HashMap;
import java.util.Map;
import fitnesse.revisioncontrol.RevisionControlOperation;
import fitnesse.revisioncontrol.State;
import fitnesse.revisioncontrol.StateRepositoryFactory;
import fitnesse.wiki.WikiPage;

public abstract class CCState implements State {

    String state;

    public static final State CHECKED_IN = new CheckedIn("Versioned");

    public static final State CHECKED_OUT = new CheckedOut("CheckedOut");

    public static final State UNKNOWN = new Unknown("Unknown");

    private static final Map<String, State> states = new HashMap<String, State>();

    static {
        states.put("Versioned", CHECKED_IN);
        states.put("CheckedOut", CHECKED_OUT);
        states.put("Unknown", UNKNOWN);
    }

    protected CCState(String state) {
        this.state = state;
    }

    public boolean isNotUnderRevisionControl() {
        return this == UNKNOWN;
    }

    public boolean isCheckedOut() {
        return this == CHECKED_OUT;
    }

    public boolean isCheckedIn() {
        return this == CHECKED_IN;
    }

    public void persist(WikiPage page) throws Exception {
        StateRepositoryFactory.getInstance().persist(page, this);
    }

    public String toString() {
        return state;
    }

    public static State instance(String state) {
        State revisionControlState = states.get(state);
        if (revisionControlState == null) revisionControlState = UNKNOWN;
        return revisionControlState;
    }
}

class CheckedIn extends CCState {

    protected CheckedIn(String state) {
        super(state);
    }

    public RevisionControlOperation[] operations() {
        return new RevisionControlOperation[] { CHECKOUT, UPDATE, DELETE };
    }
}

class CheckedOut extends CCState {

    protected CheckedOut(String state) {
        super(state);
    }

    public RevisionControlOperation[] operations() {
        return new RevisionControlOperation[] { CHECKIN, REVERT };
    }
}

class Unknown extends CCState {

    protected Unknown(String state) {
        super(state);
    }

    public RevisionControlOperation[] operations() {
        return new RevisionControlOperation[] { ADD };
    }

    @Override
    public void persist(WikiPage page) throws Exception {
        StateRepositoryFactory.getInstance().delete(page);
    }
}
