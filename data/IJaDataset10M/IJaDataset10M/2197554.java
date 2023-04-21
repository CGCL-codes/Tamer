package com.compomics.icelogo.core.data.sequenceset;

import com.compomics.icelogo.core.enumeration.AminoAcidEnum;
import com.compomics.icelogo.core.interfaces.ISequenceSet;
import com.compomics.util.protein.Protein;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA. User: kenny Date: Jul 16, 2009 Time: 9:40:57 AM
 * <p/>
 * This class
 */
public class RegionalFastaSequenceSet extends PartialSequenceSet {

    private AminoAcidEnum[] iAminoAcidEnums;

    private int iPrefix;

    private int iSuffix;

    private int iNumberOfReturnedSequences = 0;

    private String iActiveSequence;

    private static boolean iRightSamplingDirection = true;

    /**
     * Creates a new PartialRegionalSequenceSet. This class extends the PartialSequenceSet , this extension randomly
     * represents only a part of the parent's fasta database. <br>For example, if Swissprot has about 6000 yeast
     * entries, this class allows you to randomly only use an absolute number 'n' out of these. If n equals '100', then
     * '100' entries are chosen randomly out of the complete fasta file. The RegionalSequenceSet comes with a series of
     * AminoAcidEnums that center the returned sequences. The subset size equals the number of AminoAcidEnums.
     *
     * @param aAminoAcidEnums
     * @param aSequenceSet    The SequenceSet parent wherefrom this partial SequenceSet was derived.
     *                        <b>aSequenceSet.reset() is called upon initiation!</b>
     * @param aPrefix         The number of AminoAcids that must precede the given residue.
     * @param aSuffix         The number of AminoAcids that must follow ther given residue.
     */
    public RegionalFastaSequenceSet(final AminoAcidEnum[] aAminoAcidEnums, final ISequenceSet aSequenceSet, int aPrefix, int aSuffix) {
        super(aSequenceSet, aAminoAcidEnums.length);
        iAminoAcidEnums = aAminoAcidEnums;
        iPrefix = aPrefix;
        iSuffix = aSuffix;
    }

    /**
     * Private method builds the indexes for the partial sequenceset. <br>The class variable 'iIndexSet' stores random
     * ints between '0' and the number of sequences in the parent fasta file. The 'nextSequence()' method then makes use
     * of this index by only returning parent sequences if the class variable 'iIterationIndex' is also in this
     * 'iIndexSet'.
     */
    @Override
    protected void buildRandomIndexSet() {
        iIndexSet = new TreeSet<Integer>();
        int lNumberOfParentSequences = getParentNumberOfSequences();
        int lNumberOfIndexedSequences = (int) (iNumberOfSequences * 1.1);
        for (int i = 0; i < lNumberOfIndexedSequences; i++) {
            while (!iIndexSet.add(iRandom.nextSecureInt(0, lNumberOfParentSequences - 1))) {
            }
        }
    }

    /**
     * Returns the last used protein sequence as retrieved from the parent SequenceSet.
     *
     * @return String with the last used protein sequence.
     */
    public String getActiveSequence() {
        return iActiveSequence;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String nextSequence() {
        String lResult = null;
        if (iNumberOfReturnedSequences < iNumberOfSequences) {
            boolean proceed = true;
            do {
                iActiveSequence = iSequenceSet.nextSequence();
                if (iActiveSequence == null) {
                    rebuild();
                } else {
                    if (iIndexSet.contains(iIterationIndex++)) {
                        char aa = iAminoAcidEnums[iNumberOfReturnedSequences].getOneLetterCode();
                        if (iRightSamplingDirection) {
                            StringBuffer lTempBuffer = new StringBuffer(iActiveSequence);
                            lTempBuffer = lTempBuffer.reverse();
                            iActiveSequence = lTempBuffer.toString();
                        }
                        iRightSamplingDirection = !iRightSamplingDirection;
                        int lRandLimit = 10;
                        for (int i = 0; i < lRandLimit; i++) {
                            int rand = iRandom.nextSecureInt(0, (iActiveSequence.length() - 1));
                            int index = iActiveSequence.indexOf(aa, rand);
                            if (iRightSamplingDirection) {
                                if (index - iSuffix >= 0 && index + iPrefix <= (iActiveSequence.length() - 1)) {
                                    lResult = iActiveSequence.substring(index - iSuffix, index + (iPrefix + 1));
                                    if (iRightSamplingDirection) {
                                        StringBuffer lTempBuffer = new StringBuffer(lResult);
                                        lTempBuffer = lTempBuffer.reverse();
                                        lResult = lTempBuffer.toString();
                                    }
                                    i = lRandLimit;
                                    proceed = false;
                                }
                            } else {
                                if (index - iPrefix >= 0 && index + iSuffix <= (iActiveSequence.length() - 1)) {
                                    lResult = iActiveSequence.substring(index - iPrefix, index + (iSuffix + 1));
                                    i = lRandLimit;
                                    proceed = false;
                                }
                            }
                        }
                    }
                }
            } while (proceed);
            iNumberOfReturnedSequences++;
        }
        return lResult;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        iNumberOfReturnedSequences = 0;
        rebuild();
    }

    private void rebuild() {
        iSequenceSet.reset();
        iIterationIndex = 0;
        iActiveSequence = null;
        this.buildRandomIndexSet();
    }

    /**
     * Attempts to retrieve the active protein instance. This can only be returned if the regional fasta sequenceset was
     * consctructed from a Fasta file.
     *
     * @return The parent active Protein instance. Null if the parent SequenceSet is no FastaSequenceSet.
     */
    public Protein getParentProtein() {
        Protein lResult = null;
        if (iSequenceSet instanceof FastaSequenceSet) {
            lResult = ((FastaSequenceSet) iSequenceSet).getActiveProtein();
        }
        return lResult;
    }
}
