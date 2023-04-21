package net.sf.ideoreport.api.datastructure.containers.data;

/**
 * Impl�mentation d'un recordset que l'on peut parcourir dans les DEUX sens.
 *
 * @author jbeausseron
 * @since 1.3.0
 */
public interface IScrollableRecordset extends IDataRecordset {

    /**
     * Renvoie le nombre d'enregistrement pr�sents dans le recorset.
     * @return le nombre d'enregistrements
     */
    int getRecordCount();

    /**
     * D�place le curseur du recordset vers le premier �l�ment.
     * @return <code>true</code> si le d�placement a pu �tre effectu�, <code>false</code> sinon
     */
    boolean moveFirst();

    /**
     * D�place le curseur du recordset vers le dernier �l�ment.
     * @return <code>true</code> si le d�placement a pu �tre effectu�, <code>false</code> sinon
     */
    boolean moveLast();

    /**
     * Renvoie le fait que l'on est � la fin du recordset ou non.
     * @return <code>true</code> si on est � la fin du recordset, <code>false</code> sinon
     */
    boolean isEOF();
}
