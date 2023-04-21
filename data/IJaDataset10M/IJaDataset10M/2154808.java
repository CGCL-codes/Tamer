package unbbayes.prs.mebn.entity;

/**
 * This class link a resident node to a state. This is necessary because the state
 * have special attributes for each node where it is a state. 
 * 
 * @author Laecio Lima dos Santos
 */
public class StateLink {

    private Entity state;

    private boolean globallyExclusive = false;

    public StateLink(Entity state) {
        this.state = state;
    }

    public boolean isGloballyExclusive() {
        return globallyExclusive;
    }

    public void setGloballyExclusive(boolean globallyExclusive) {
        this.globallyExclusive = globallyExclusive;
    }

    public Entity getState() {
        return state;
    }

    public void setState(Entity state) {
        this.state = state;
    }

    public String toString() {
        return state.getName();
    }
}
