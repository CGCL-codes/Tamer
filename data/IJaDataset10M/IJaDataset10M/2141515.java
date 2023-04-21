package lablayer.view.state;

/**
 * @author Darkness
 */
public interface StateInterface {

    public static final int STANDARD_STATE = 0;

    public static final int COMPONENT_STATE = 1;

    public static final int LINK_STATE = 2;

    public int getStateId();

    public void setStateId(int stateId);

    public void addState(int stateId);

    public void removeState(int stateId);
}
