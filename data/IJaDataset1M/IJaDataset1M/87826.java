package org.scub.foundation.framework.base.paging;

/**
 * Super-classe pour les critères de recherche.
 * @author Anthony GUILLEMETTE (anthony.guillemette@scub.net)
 * @param <T> Type des critères de recherhe.
 */
public class RemotePagingCriteriasDto<T> extends RemotePagingDto {

    /** Serial version uid. */
    private static final long serialVersionUID = 3581063539790989661L;

    /** Critères de recherche. */
    private T criterias;

    /**
     * Constructeur par défaut.
     * @param criterias les critères de la recherche.
     * @param firstResult l'index du premier résultat.
     * @param maxResult le nombre de résultats à récupérer.
     */
    public RemotePagingCriteriasDto(T criterias, int firstResult, int maxResult) {
        super(firstResult, maxResult);
        this.criterias = criterias;
    }

    /**
	 * @return the criterias
	 */
    public T getCriterias() {
        return criterias;
    }

    /**
	 * @param criterias the criterias to set
	 */
    public void setCriterias(T criterias) {
        this.criterias = criterias;
    }
}
