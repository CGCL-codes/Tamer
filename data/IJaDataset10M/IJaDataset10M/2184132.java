package de.proteinms.xtandemparser.xtandem;

import java.io.Serializable;
import java.util.List;

/**
 * The peptide object contains all the information about itself and its
 * identifications, which are called "domains".
 *
 * @author Thilo Muth
 */
public class Peptide implements Serializable {

    /**
     * This variable contains the peptide id (as an index).
     */
    private String iPeptideID;

    /**
     * Contains the start position of the peptide == beginning of the protein's
     * peptide sequence
     */
    private int iStart = 0;

    /**
     * Contains the end position of the peptide == end ot the protein's peptide
     * sequence
     */
    private int iEnd = 0;

    /**
     * This String contains the peptide sequence
     */
    private String iSequence = null;

    /**
     * This String contains the original FASTA file path
     */
    private String iFastaFilePath = null;

    /**
     * Contains the spectrum number.
     */
    private int iSpectrumNumber;

    /**
     * The domain list.
     */
    private List<Domain> domains;

    /**
     * The Peptide constructor gets the peptide id the start + end position and
     * the sequences as string.
     *
     * @param aPeptideID
     * @param aStart
     * @param aEnd
     * @param aSequence
     */
    public Peptide(String aPeptideID, int aStart, int aEnd, String aSequence) {
        iPeptideID = aPeptideID;
        iStart = aStart;
        iEnd = aEnd;
        iSequence = aSequence;
    }

    /**
     * Returns the domains
     *
     * @return List<Domain> domains
     */
    public List<Domain> getDomains() {
        return domains;
    }

    /**
     * Sets the domains.
     *
     * @param domains
     */
    public void setDomains(List<Domain> domains) {
        this.domains = domains;
    }

    /**
     * This method gets the start position of the peptide
     *
     * @return int iStart
     */
    public int getStart() {
        return iStart;
    }

    /**
     * This method sets the start position of the peptide
     *
     * @param aStart
     */
    public void setStart(int aStart) {
        this.iStart = aStart;
    }

    /**
     * This method gets the end position of the peptide
     *
     * @return int iEnd
     */
    public int getEnd() {
        return iEnd;
    }

    /**
     * This method sets the end position of the peptide
     *
     * @param aEnd
     */
    public void setEnd(int aEnd) {
        this.iEnd = aEnd;
    }

    /**
     * This method returns the corrisponding spectrum number for the peptide
     *
     * @return iSpectrumNumber
     */
    public int getSpectrumNumber() {
        return iSpectrumNumber;
    }

    /**
     * Sets the spectrum number for the peptide.
     *
     * @param aSpectrumNumber
     */
    public void setSpectrumNumber(int aSpectrumNumber) {
        iSpectrumNumber = aSpectrumNumber;
    }

    /**
     * Returns the protein sequence of the peptide.
     *
     * @return iSequence
     */
    public String getSequence() {
        return iSequence;
    }

    /**
     * Sets the protein sequence of the peptide.
     *
     * @param aSequence
     */
    public void setSequence(String aSequence) {
        this.iSequence = aSequence;
    }

    /**
     * Returns the fasta file path.
     *
     * @return iFastaFilePath
     */
    public String getFastaFilePath() {
        return iFastaFilePath;
    }

    /**
     * Sets the fasta file path.
     *
     * @param aFastaFilePath
     */
    public void setFastaFilePath(String aFastaFilePath) {
        iFastaFilePath = aFastaFilePath;
    }

    /**
     * Returns the peptide id as string.
     *
     * @return iPeptideID
     */
    public String getPeptideID() {
        return iPeptideID;
    }

    /**
     * Overwritten toString()-method.
     *
     * @return String
     */
    public String toString() {
        return "PeptideID: " + iPeptideID + "\nSequence:\n" + iSequence;
    }
}
