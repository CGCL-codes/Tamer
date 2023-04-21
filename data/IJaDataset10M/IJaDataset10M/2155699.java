package org.scub.foundation.framework.base.paging;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Object pour le transfert d'information de pagination.
 * @author Goumard Stephane (stephane.goumard@scub.net)
 */
public class RemotePagingDto implements Serializable {

    /** Serial version uid. */
    private static final long serialVersionUID = 7024628701074372655L;

    /** Debut pagination. */
    private int firstResult;

    /** Nombre de resultat de la pagination. */
    private int maxResult;

    /**
     * Tri.
     */
    private List<RemotePagingSort> listeSorts;

    /**
     * Constructeur par defaut.
     * @param firstResult debut pagination.
     * @param maxResult nombre de resultat de la pagination.
     */
    public RemotePagingDto(int firstResult, int maxResult) {
        this.firstResult = firstResult;
        this.maxResult = maxResult;
        this.listeSorts = new ArrayList<RemotePagingSort>();
    }

    /**
     * Constructeur par defaut.
     */
    public RemotePagingDto() {
    }

    /**
     * Get the firstResult value.
     * @return the firstResult
     */
    public int getFirstResult() {
        return firstResult;
    }

    /**
     * Set the firstResult value.
     * @param firstResult the firstResult to set
     */
    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    /**
     * Get the maxResult value.
     * @return the maxResult
     */
    public int getMaxResult() {
        return maxResult;
    }

    /**
     * Set the maxResult value.
     * @param maxResult the maxResult to set
     */
    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    /**
	 * @return the listeSorts
	 */
    public List<RemotePagingSort> getListeSorts() {
        return listeSorts;
    }

    /**
	 * @param listeSorts the listeSorts to set
	 */
    public void setListeSorts(List<RemotePagingSort> listeSorts) {
        this.listeSorts = listeSorts;
    }
}
