package net.sourceforge.freejava.fsm.base;

import java.io.Serializable;

/**
 * State Context
 */
public interface StateGraph extends Serializable {

    Object context();

    State current();

    boolean isAccepted();

    boolean isErrored();

    State recv(Object message);

    void jump(State state);

    void push(State state);

    State pop();

    State get(Object key);

    Object find(State state);

    void add(Object key, State state);

    void remove(Object key);
}
