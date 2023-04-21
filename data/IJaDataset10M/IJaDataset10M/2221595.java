package org.ensembl.datamodel;

/**
 * Repetitive sequence on genome. I am a type of FeaturePair.
**/
public interface RepeatFeature extends FeaturePair {

    /**
   * Returns repeat consensus internal id, this is the same
   * as repeatConsensus.internalID if repeatConsensus is available.
   *
   * @return internalID of the cloneFragment.
   */
    long getRepeatConsensusInternalID();

    /**
   * Sets repeatConsensus internal id, also sets the
   * repeatConsensus.internalID if repeatConsensus is available.
   */
    void setRepeatConsensusInternalID(long internalID);

    /**
   * @return RepeatConsensus this repeat is a partial instance of.`
   */
    RepeatConsensus getRepeatConsensus();

    void setRepeatConsensus(RepeatConsensus repeatConsensus);
}
