package org.expasy.jpl.core.mol.polymer.pept.fragmenter;

import java.util.EnumSet;
import java.util.Set;
import org.expasy.jpl.commons.base.process.ProcessException;
import org.expasy.jpl.commons.collection.symbol.Symbol;
import org.expasy.jpl.commons.collection.symbol.seq.SequenceSymbolCounter;
import org.expasy.jpl.core.mol.chem.MassCalculator;
import org.expasy.jpl.core.mol.monomer.aa.AAManager;
import org.expasy.jpl.core.mol.monomer.aa.AminoAcid;
import org.expasy.jpl.core.mol.polymer.BioPolymerUtils;
import org.expasy.jpl.core.mol.polymer.pept.Peptide;
import org.expasy.jpl.core.ms.spectrum.annot.FragmentAnnotationImpl;
import org.expasy.jpl.core.ms.spectrum.peak.AnnotatedPeak;
import org.expasy.jpl.core.ms.spectrum.peak.InvalidPeakException;

/**
 * Generate immonium ions from a sequence precursor
 * 
 * @author nikitin
 * 
 */
class ImmoniumGenerator extends AbstractPeptideFragmenter {

    /**
	 * this array of booleans trace the existence of all immoniums composing a
	 * precursor peak
	 */
    private boolean[] isImmoniumGenerated;

    public ImmoniumGenerator() {
        this(MassCalculator.getMonoAccuracyInstance());
    }

    public ImmoniumGenerator(final MassCalculator massType) {
        super(EnumSet.of(FragmentationType.IMMONIUM));
    }

    private void initImmoniumBooleans() {
        isImmoniumGenerated = new boolean[25];
        for (int i = 0; i < 25; i++) {
            isImmoniumGenerated[i] = false;
        }
    }

    public void process(Peptide peptide) throws ProcessException {
        try {
            setFragmentablePrecursor(peptide);
            generateFragments();
        } catch (PeptideFragmentationException e) {
            throw new ProcessException("cannot process " + peptide, e);
        }
    }

    public void generateFragments() throws PeptideFragmentationException {
        final Peptide sequence = peptidePrecursor;
        SequenceSymbolCounter<AminoAcid> occCounter = BioPolymerUtils.getSymbolCounter(sequence);
        initImmoniumBooleans();
        Set<Symbol<AminoAcid>> occurrences = occCounter.getSymbols();
        Set<AminoAcid> aas = AAManager.getInstance().lookUpDataFromSymbols(occurrences);
        updateSet(aas);
        for (final AminoAcid aa : aas) {
            final AnnotatedPeak nextPeak = peaksPool.nextActivePeak();
            try {
                nextPeak.setMz(massCalc.getMass(aa) + massCalc.getProtonMass());
                nextPeak.setAnnot(FragmentAnnotationImpl.newImmoniumAnnot(aa));
            } catch (InvalidPeakException e) {
                throw new PeptideFragmentationException(e.getMessage() + ": cannot create immonium peak from amino-acid " + aa, e);
            }
        }
    }

    private void updateSet(Set<AminoAcid> aaSet) {
        if (!aaSet.contains(AminoAcid.RAD_J)) {
            if (aaSet.contains(AminoAcid.RAD_I) && aaSet.contains(AminoAcid.RAD_L)) {
                aaSet.remove(AminoAcid.RAD_I);
                aaSet.remove(AminoAcid.RAD_L);
                aaSet.add(AminoAcid.RAD_J);
            }
        } else {
            if (aaSet.contains(AminoAcid.RAD_I)) {
                aaSet.remove(AminoAcid.RAD_I);
            }
            if (aaSet.contains(AminoAcid.RAD_L)) {
                aaSet.remove(AminoAcid.RAD_L);
            }
        }
    }
}
