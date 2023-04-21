package com.iver.cit.gvsig.fmap.edition;

import com.iver.cit.gvsig.fmap.core.IRow;

public interface IRowEdited extends IRow {

    public static final int STATUS_ORIGINAL = 0;

    public static final int STATUS_MODIFIED = 1;

    public static final int STATUS_DELETED = 2;

    public static final int STATUS_ADDED = 3;

    IRow getLinkedRow();

    /**
	     * Por ejemplo:
	     * 1=> La fila linkada (la original) ha sido borrada.
	     * 2=> Es una modificaci�n. Esta fila sustituye a la anterior.
	     * 3=> Es una nueva fila a�adida. 
	     */
    int getStatus();

    /**
	     * @return the "external index" of this feature.
	     * The 0 based index of the feature.
	     */
    int getIndex();
}
