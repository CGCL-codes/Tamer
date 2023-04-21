package org.objectwiz.ui;

/**
 *
 * @author Benoît Del Basso <benoit.delbasso at helmet.fr>
 */
public interface ValueConverter<E, F> {

    public F convert(E object) throws Exception;

    public E unconvert(F object) throws Exception;
}
