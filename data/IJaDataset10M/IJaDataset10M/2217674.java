package org.jcvi.common.core.seq.read.trace.sanger.chromat.scf.header;

import org.jcvi.common.core.seq.read.trace.sanger.chromat.scf.section.Section;

/**
 * An SCF Header  describes the location and
 * size of the different {@link Section}s of an {@link SCFChromatogram}.
 * @author dkatzel
 *
 *
 */
public interface SCFHeader {

    /**
     * @return the numberOfSamples
     */
    int getNumberOfSamples();

    /**
     * @param numberOfSamples the numberOfSamples to set
     */
    void setNumberOfSamples(int numberOfSamples);

    /**
     * @return the sampleOffset
     */
    int getSampleOffset();

    /**
     * @param sampleOffset the sampleOffset to set
     */
    void setSampleOffset(int sampleOffset);

    /**
     * @return the numberOfBases
     */
    int getNumberOfBases();

    /**
     * @param numberOfBases the numberOfBases to set
     */
    void setNumberOfBases(int numberOfBases);

    /**
     * @return the commentSize
     */
    int getCommentSize();

    /**
     * @param commentSize the commentSize to set
     */
    void setCommentSize(int commentSize);

    /**
     * @return the commentOffset
     */
    int getCommentOffset();

    /**
     * @param commentOffset the commentOffset to set
     */
    void setCommentOffset(int commentOffset);

    /**
     * @return the version
     */
    float getVersion();

    /**
     * @param version the version to set
     */
    void setVersion(float version);

    /**
     * @return the sampleSize
     */
    byte getSampleSize();

    /**
     * @param sampleSize the sampleSize to set
     */
    void setSampleSize(byte sampleSize);

    /**
     * @return the privateDataSize
     */
    int getPrivateDataSize();

    /**
     * @param privateDataSize the privateDataSize to set
     */
    void setPrivateDataSize(int privateDataSize);

    /**
     * @return the privateDataOffset
     */
    int getPrivateDataOffset();

    /**
     * @param privateDataOffset the privateDataOffset to set
     */
    void setPrivateDataOffset(int privateDataOffset);

    /**
     * @return the basesOffset
     */
    int getBasesOffset();

    /**
     * @param basesOffset the basesOffset to set
     */
    void setBasesOffset(int basesOffset);
}
