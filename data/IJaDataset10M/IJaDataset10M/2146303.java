package org.yactu.tools.stack;

import java.util.Collection;
import java.util.List;

/**
 * This is a wrapper of the Stack class, with less exceptions to take care
 * about. To be used where you are sure you wont get any exception that may be
 * thrown underneath.
 * 
 * @author ploix
 *  
 */
public class SafeStack implements Stack {

    private Stack __stack = null;

    public SafeStack(Stack _stack) {
        __stack = _stack;
    }

    public SafeStack(boolean _isFifo) {
        this((Stack) new NormalStack(_isFifo));
    }

    public boolean isEmpty() {
        return __stack.isEmpty();
    }

    public boolean remove(Object _o) {
        return __stack.remove(_o);
    }

    public boolean waitingRemove(Object _o) {
        return __stack.waitingRemove(_o);
    }

    public void addStackEventListener(StackEventListener _listener) {
        __stack.addStackEventListener(_listener);
    }

    public void removeStackEventListener(StackEventListener _listener) {
        __stack.removeStackEventListener(_listener);
    }

    public boolean isFifo() {
        return __stack.isFifo();
    }

    public Object push(Object _pObj) {
        try {
            return __stack.push(_pObj);
        } catch (TooManyInStackException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public Object pop() {
        return __stack.pop();
    }

    public Object peek() {
        return __stack.peek();
    }

    public Object waitingPush(Object _o) {
        try {
            return __stack.waitingPush(_o);
        } catch (StackException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public Object waitingPushTimeout(Object _o, long _t) {
        try {
            return __stack.waitingPushTimeout(_o, _t);
        } catch (StackException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public Object waitingPop() {
        try {
            return __stack.waitingPop();
        } catch (AlreadyPopingException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public Object multiWaitingPop() {
        try {
            return __stack.multiWaitingPop();
        } catch (StackException e) {
            e.printStackTrace(System.err);
            return null;
        }
    }

    public Object multiWaitingPop(long _timeout) throws StackException {
        return __stack.multiWaitingPop(_timeout);
    }

    public Object waitingPop(long _timeout) throws PopTimeOutException, PopStoppedException {
        try {
            return __stack.waitingPop(_timeout);
        } catch (AlreadyPopingException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }

    public void setMaxInStack(long _nb) {
        __stack.setMaxInStack(_nb);
    }

    public List getAsList() {
        return __stack.getAsList();
    }

    public boolean contains(Object _o) {
        return this.getAsList().contains(_o);
    }

    public boolean containsAll(Collection _c) {
        return this.getAsList().containsAll(_c);
    }

    public List popAll() {
        return __stack.popAll();
    }
}
