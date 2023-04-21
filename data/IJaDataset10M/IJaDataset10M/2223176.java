package org.ensembl.datamodel;

import java.util.Set;

public interface StableIDEvent extends Persistent {

    final String GENE = "gene";

    final String TRANSCRIPT = "transcript";

    final String TRANSLATION = "translation";

    void setSession(MappingSession session);

    String getStableID();

    /**
   * If relatedStableID is "null" then it is not added but deleted is set to
   * true.
   * @param relatedStableID stableID of the related item
   * @param version version of the related item
   * @return whether relatedStableID was added.
   */
    boolean addRelated(String relatedStableID, int version);

    /**
   * Items can relate to not just several other items but also to several versions of those items. 
   * This method provides access to these versions. 
   * 
   * 1*stableID -> N*relatedStableID -> M*version.
   * 
   * @param relatedStableID
   * @return array of ints corresponding to the versions of the item the relatedStableID
   * refers to.
   */
    public int[] getRelatedVersions(String relatedStableID);

    /**
   * @return whether relatedStableID was removed.
   */
    boolean removeRelated(String relatedStableID);

    Set getRelatedStableIDs();

    void setStableID(String stableID);

    void setStableIDVersion(int version);

    int getStableIDVersion();

    MappingSession getSession();

    boolean isSplit();

    boolean isMerged();

    boolean isCreated();

    boolean isDeleted();

    void setDeleted(boolean deleted);

    void setCreated(boolean created);

    /**
   * Type of event; gene, transcript or translaton.
   * @return type which is one of StableIDEvent.GENE, 
   * StableIDEvent.TRANSCRIPT or StableIDEvent.TRANSLATION/
   */
    String getType();

    /**
	 * Type of event; gene, transcript or translaton.
	 * @param type should be one of StableIDEvent.GENE, 
	 * StableIDEvent.TRANSCRIPT or StableIDEvent.TRANSLATION/
	 */
    void setType(String type);
}
