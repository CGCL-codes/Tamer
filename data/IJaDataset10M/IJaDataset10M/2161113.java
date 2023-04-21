package apollo.dataadapter.synteny;

import apollo.datamodel.Link;
import apollo.datamodel.SeqFeatureI;

/** 
 *  ResultPeptidePair, to sort out the 2 feats that come in on the one way link
 */
class ResultPeptidePair implements ResultAnnotPairI {

    private SeqFeatureI result;

    private SeqFeatureI transcript;

    private boolean transcriptIsFromTopSpecies = false;

    private AbstractLinker linker;

    ResultPeptidePair(SeqFeatureI feat1, SeqFeatureI feat2, AbstractLinker linker) {
        this.linker = linker;
        if (featFromAnnotSpecies(feat1)) {
            transcript = feat1;
            result = feat2;
            transcriptIsFromTopSpecies = true;
        } else {
            transcript = feat2;
            result = feat1;
            transcriptIsFromTopSpecies = false;
        }
    }

    /** Returns true if feat from species2, which is the peptide species */
    private boolean featFromAnnotSpecies(SeqFeatureI feat) {
        return !linker.featFromLinkSpecies1(feat);
    }

    protected SeqFeatureI getTranscript() {
        return transcript;
    }

    protected SeqFeatureI getResult() {
        return result;
    }

    public boolean isLinked() {
        String pepName = transcript.getName();
        if (transcript.hasPeptideSequence()) {
            pepName = transcript.getPeptideSequence().getName();
        } else if (transcript.isProteinCodingGene()) {
            System.out.println("WARNING: transcript " + pepName + " has no pep seq. Check that " + "there is sequence data.");
        }
        return result.getHitFeature().getName().equals(pepName);
    }

    /** Figure out which feature is a transcript and which is the result.
      Find the sub region of the exon result hit, and use that as the link feature 
      use the midpoint of  result which is in trascript coords - translate 
      to genomic of transcript cur set - find the exon in transcript that this hits
      then take the minimum of the exon and the hit as the feature (take out 
      blast bleeding over the edges)  */
    public Link createLink() {
        SeqFeatureI peptideResultHit = result.getHitFeature();
        int pepHitStart = peptideResultHit.getStart(), pepHitEnd = peptideResultHit.getEnd();
        int midPepHit = (pepHitStart + pepHitEnd) / 2;
        int genomicMid = getGenomicPosition(midPepHit);
        SeqFeatureI hitExon = transcript.getFeatureContaining(genomicMid);
        if (hitExon == null) {
            System.out.println("Unable to find transcript " + transcript.getName() + " exon for " + "result " + result.getName() + " hit coords " + pepHitStart + "-" + pepHitEnd + ". Check data.");
            return null;
        }
        int genHitStart = getGenomicPosition(pepHitStart);
        int genHitEnd = getGenomicPosition(pepHitEnd);
        int genHitLow = hitExon.getStrand() == 1 ? genHitStart : genHitEnd;
        int genHitHigh = hitExon.getStrand() == 1 ? genHitEnd : genHitStart;
        SeqFeatureI hitExonFragment = hitExon.cloneFeature();
        if (hitExonFragment.getLow() < genHitLow) hitExonFragment.setLow(genHitLow);
        if (hitExonFragment.getHigh() > genHitHigh) hitExonFragment.setHigh(genHitHigh);
        Link link = null;
        boolean hasSpecFeat = linker.hasSpeciesFeature();
        if (transcriptIsFromTopSpecies) {
            link = new Link(hitExonFragment, result, hasSpecFeat, linker.hasPercentIdentity());
            link.setSpeciesFeature1(hitExon);
            link.setTypeIsFeat1(false);
        } else {
            link = new Link(result, hitExonFragment, hasSpecFeat, linker.hasPercentIdentity());
            link.setSpeciesFeature2(hitExon);
        }
        return link;
    }

    /** Hit position is in peptide coords */
    protected int getGenomicPosition(int hitPosition) {
        return getTranscript().getGenomicPosForPeptidePos(hitPosition);
    }
}
