package org.fudaa.ebli.commun;

/**
 * Interface d'�coute des erreurs g�n�r�es par TableModelModeleAdapter.
 * @author Emmanuel MARTIN
 * @version $Id: TableModelModeleAdapterErrorListener.java 4405 2009-01-27 14:45:39Z emmanuel_martin $
 */
public interface TableModelModeleAdapterErrorListener {

    /**
   * Appel� quand une erreur est d�tect� dans les donn�es.
   */
    public void modeleAdapterError(String _message);

    /**
   * Appel� quand l'erreur n'a plus de raison d'�tre affich�e.
   */
    public void modeleAdpaterNoError();
}
