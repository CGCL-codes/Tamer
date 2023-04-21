package org.yccheok.jstock.engine;

/**
 *
 * @author yccheok
 */
public interface Observer<S, A> {

    public void update(S subject, A arg);
}
