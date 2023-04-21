package edu.usc.epigenome.uecgatk.YapingWalker;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sf.samtools.SAMRecord;

import org.apache.log4j.Logger;
import org.broadinstitute.sting.gatk.GenomeAnalysisEngine;
import org.broadinstitute.sting.gatk.contexts.AlignmentContext;
import org.broadinstitute.sting.gatk.contexts.ReferenceContext;
import org.broadinstitute.sting.gatk.refdata.RefMetaDataTracker;
import org.broadinstitute.sting.gatk.uscec.bisulfitesnpmodel.BisSNPUtils;
import org.broadinstitute.sting.gatk.uscec.bisulfitesnpmodel.BisulfiteArgumentCollection;
import org.broadinstitute.sting.gatk.uscec.bisulfitesnpmodel.BisulfiteDiploidSNPGenotypePriors;
import org.broadinstitute.sting.gatk.uscec.bisulfitesnpmodel.BisulfiteGenotyperEngine;
import org.broadinstitute.sting.gatk.walkers.genotyper.GenotypePriors;
import org.broadinstitute.sting.utils.GenomeLoc;
import org.broadinstitute.sting.utils.exceptions.UserException;
import org.broadinstitute.sting.utils.fasta.CachingIndexedFastaSequenceFile;
import org.broadinstitute.sting.utils.pileup.PileupElement;
import org.broadinstitute.sting.utils.pileup.ReadBackedPileup;
import org.broadinstitute.sting.utils.pileup.ReadBackedPileupImpl;

public class NDRdetectionEngine {

	private NDRargumentCollection NAC = null;
	
	// the priors object
    private GenotypePriors genotypePriors;

    // the various loggers
    private Logger logger = null;
	
	public NDRdetectionEngine(GenomeAnalysisEngine toolkit,NDRargumentCollection NAC, Logger logger) {
		// TODO Auto-generated constructor stub
		
		initialize(toolkit, NAC, logger);
	}
	
	protected void initialize(GenomeAnalysisEngine toolkit, NDRargumentCollection NAC, Logger logger){
		this.NAC = NAC.clone();
        this.logger = logger;
        genotypePriors = new BisulfiteDiploidSNPGenotypePriors();
        
	}
	
	public NDRCallContext calculateNDRscore(RefMetaDataTracker tracker, ReferenceContext ref, AlignmentContext context){
		NDRCallContext ncc = new NDRCallContext();
		String cytosinePattern = "GCH-2";
		ReadBackedPileup pileup = context.getBasePileup();
		GenomeLoc location = pileup.getLocation();
		String contig = location.getContig();
		int position = location.getStart();
		for(int i = 0 - NAC.nucPosWindow/2; i <= NAC.nucPosWindow/2; i++){
			GenomeLoc loc = ref.getGenomeLocParser().createGenomeLoc(contig, position + i );
			
			List<SAMRecord> reads =  new ArrayList<SAMRecord>();;
			List<Integer> elementOffsets = new ArrayList<Integer>();

			for ( PileupElement p : pileup ) {
					int elementOffset = i + p.getOffset();
					if(elementOffset < 0 || elementOffset > p.getRead().getReadLength()-1)
						continue;
					elementOffsets.add(elementOffset);
					reads.add(p.getRead());
			}
			ReadBackedPileup tmpPileup = new ReadBackedPileupImpl(loc,reads,elementOffsets);
			
			ReferenceContext tmpRef = new ReferenceContext(ref.getGenomeLocParser(),loc, ref.getWindow(),ref.getBases());
		}
		BisSNPUtils it = new BisSNPUtils();
		it.checkCytosineStatus(cytosinePattern, pileup, tracker, ref, priors, bac, methyStatus)
		
		return null;
		
	}
	
	

}
