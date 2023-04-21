package org.expasy.jpl.insilico.ms.fragmentation.fragmenter;

import static org.expasy.jpl.commons.ms.JPLMassAccuracy.*;
import java.util.EnumSet;
import java.util.Set;
import org.expasy.jpl.bio.exceptions.JPLAAByteUndefinedException;
import org.expasy.jpl.bio.exceptions.JPLEmptySequenceException;
import org.expasy.jpl.bio.exceptions.JPLInvalidAAModificationRuntimeException;
import org.expasy.jpl.bio.sequence.JPLAASequence;
import org.expasy.jpl.bio.sequence.JPLCTerminus;
import org.expasy.jpl.bio.sequence.JPLIAASequence;
import org.expasy.jpl.bio.sequence.JPLITerminus;
import org.expasy.jpl.bio.sequence.JPLNTerminus;
import org.expasy.jpl.bio.sequence.rich.JPLPFFSequence;
import org.expasy.jpl.commons.ms.JPLMassAccuracy;
import org.expasy.jpl.insilico.exceptions.JPLPrecursorUnfragmentableException;
import org.expasy.jpl.insilico.ms.fragmentation.JPLFragmentationType;
import org.expasy.jpl.insilico.ms.peak.JPLITheoSeqBasedMSPeak;
import org.expasy.jpl.insilico.ms.peak.JPLTheoSeqBasedMSPeak;

class JPLPeptideBackboneFragmenter extends JPLAbstractPeptideFragmenter {

    public JPLPeptideBackboneFragmenter(JPLFragmentationType fragType) {
        this(EnumSet.of(fragType), MONOISOTOPIC);
    }

    public JPLPeptideBackboneFragmenter(Set<JPLFragmentationType> fragTypes) {
        this(fragTypes, MONOISOTOPIC);
    }

    public JPLPeptideBackboneFragmenter(Set<JPLFragmentationType> fragTypes, JPLMassAccuracy massType) {
        super(fragTypes, massType);
    }

    /**
	 * Generate all fragments from precursor peptide.
	 * <p>
	 * The number of peak generated depends on :
	 * - the aa number
	 * - the precursor charge state
	 * <p>
	 * The total number of obtained peaks is :
	 *    1         <- the precursor
	 * +  (n-1) * 2 <- all the fragments Nt & Ct
	 * *  q         <- each fragment can be found at all intermediary charges
	 * => In this case, it is not impossible to found isolated fragment
	 *    of 1 AA protonated like precursor !!!!
	 *    This is the limit of the calculation
	 * !!!! It is dependant on the chemistry of AAs where each have to have a
	 * charge limitation value. 
	 * 
	 * Water/Ammonium losses :
	 * <ul>
	 * <li>Water loss occurs when backbone fragments lose a water molecule H2O. 
	 * Water is mainly lost from side chains of serine, threonine, aspartic acid,
	 * or glutamic acid residues.</li>
	 * <li>Ammonia loss occurs when fragments lose an ammonia molecule  NH3.
	 * The resulting fragments are denoted a*  b*, etc. Ammonia is mainly lost from
	 * the side chains of arginine, lysine, asparagine, or glutamine residues.</li>
	 *
	 * TODO : 1. In Phenyx + other ms tools, generation 2 more fragments :
	 *      : + a Nt fragment with mass parental mass
	 *      : + a Ct fragment with mass parental mass -OH
	 *      : I have to generate these fragments !! but where ?? 
	 *
	 *      : 2. If the precursor is a fragment -> define the rules that generate
	 *      : the good fragments and their denomination.
	 *
	 *      : Proposition :
	 *      : Prot       Nt-------------------------Ct
	 *      :  frag 1 -> Nt----- : a5 ....
	 *      :  frag 2 ->     --- : ax3 [x-fragment coming from a a-fragment]
	 *                           : or xa3 [Nt->Ct reading]
	 * 
	 * @throws JPLAAByteUndefinedException 
	 * @throws JPLEmptySequenceException 
	 * @throws JPLPrecursorUnfragmentableException 
	 * @throws JPLPrecursorAlreadyFragmentatedException 
	 * @throws NullPointerException if a precursor is not defined
	 */
    public void generateIonFragments() throws JPLAAByteUndefinedException, JPLEmptySequenceException, JPLPrecursorUnfragmentableException {
        if (peptidePrecursor == null) {
            throw new NullPointerException("no precursor defined to fragment");
        }
        JPLIAASequence precursorSequence = peptidePrecursor.getSequence();
        int precursorCharge = peptidePrecursor.getChargeState();
        int precursorSeqLen = precursorSequence.length();
        if (precursorSeqLen < JPLTheoSeqBasedMSPeak.getFragmentableMinLength()) {
            throw new JPLPrecursorUnfragmentableException(precursorSequence + " : too short sequence for fragmentation.");
        }
        if (precursorSequence instanceof JPLPFFSequence) {
            try {
                ((JPLPFFSequence) precursorSequence).checkNeutralLosses();
            } catch (JPLInvalidAAModificationRuntimeException e) {
                throw new JPLPrecursorUnfragmentableException(precursorSequence + " : contains invalid neutral loss.", e);
            }
        }
        for (int i = 0; i < precursorSeqLen - 1; i++) {
            for (JPLITerminus ionType : getFragmentTypes()) {
                JPLIAASequence sequence = null;
                if (ionType == JPLCTerminus.FragmentA || ionType == JPLCTerminus.FragmentB || ionType == JPLCTerminus.FragmentC) {
                    if (precursorSequence instanceof JPLPFFSequence) {
                        sequence = new JPLPFFSequence.Builder(precursorSequence).to(i).cTerm((JPLCTerminus) ionType).build();
                    } else {
                        sequence = new JPLAASequence.Builder(precursorSequence).to(i).cTerm((JPLCTerminus) ionType).build();
                    }
                } else {
                    if (precursorSequence instanceof JPLPFFSequence) {
                        sequence = new JPLPFFSequence.Builder(precursorSequence).from(i + 1).nTerm((JPLNTerminus) ionType).build();
                    } else {
                        sequence = new JPLAASequence.Builder(precursorSequence).from(i + 1).nTerm((JPLNTerminus) ionType).build();
                    }
                }
                if (logger.isDebugEnabled()) {
                    logger.debug("new subseq of type " + ionType + ": " + sequence);
                }
                for (int j = 0; j < precursorCharge; j++) {
                    JPLITheoSeqBasedMSPeak nextPeak = peaksPool.nextActivePeak();
                    nextPeak.setSequenceNConvert2peak(sequence, j + 1);
                    nextPeak.setMSLevel(peptidePrecursor.getMSLevel() + 1);
                    if (logger.isDebugEnabled()) {
                        logger.debug(nextPeak);
                    }
                }
            }
        }
    }
}
