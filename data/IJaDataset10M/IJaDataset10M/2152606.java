package org.mobicents.diameter.stack.functional;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 */
public class StateChange<T> {

    private T oldState;

    private T newState;

    /**
   * @param oldState
   * @param newState
   */
    public StateChange(T oldState, T newState) {
        super();
        this.oldState = oldState;
        this.newState = newState;
    }

    public T getOldState() {
        return oldState;
    }

    public T getNewState() {
        return newState;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((newState == null) ? 0 : newState.hashCode());
        result = prime * result + ((oldState == null) ? 0 : oldState.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        StateChange other = (StateChange) obj;
        if (newState == null) {
            if (other.newState != null) return false;
        } else if (!newState.equals(other.newState)) return false;
        if (oldState == null) {
            if (other.oldState != null) return false;
        } else if (!oldState.equals(other.oldState)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "StateChange [oldState=" + oldState + ", newState=" + newState + "]";
    }
}
