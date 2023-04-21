package org.openconcerto.utils.checks;

/**
 * Un objet pouvant être vide ou non.
 * 
 * @author Sylvain CUAZ
 */
public interface EmptyObj {

    public boolean isEmpty();

    public void addEmptyListener(EmptyListener l);
}
