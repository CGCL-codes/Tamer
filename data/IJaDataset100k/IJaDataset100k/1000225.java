package musite.misc.nr;

import java.util.List;
import musite.Protein;

/**
 *
 * @author Jianjiong Gao
 */
public interface ProteinSelector {

    /**
     * Select a protein from a protein clust.
     * @param proteinCluster protein cluster.
     * @return the selected protein.
     */
    public Protein select(List<Protein> proteinCluster);
}
