package br.org.bertol.mestrado.engine.optimisation.moo;

import java.util.Comparator;

/**
 * @author contaqualquer
 * @param <T>
 */
public abstract class AbstractParetoDominace<T> implements Comparator<T> {

    /***/
    public static final transient int LEFT_DOMINE = -1;

    /***/
    public static final transient int RIGHT_DOMINE = 1;

    /***/
    public static final transient int LEFT_DONT_DOMINE = 0;

    /**
     */
    protected AbstractParetoDominace() {
        super();
    }
}
