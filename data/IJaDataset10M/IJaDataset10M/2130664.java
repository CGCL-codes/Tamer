package edu.usc.epigenome.uecgatk.bisulfitesnpmodel;

import java.util.BitSet;
import org.broadinstitute.sting.gatk.contexts.ReferenceContext;
import org.broadinstitute.sting.utils.sam.GATKSAMRecord;
import edu.usc.epigenome.uecgatk.bisulfitesnpmodel.BadBaseFilterBisulfite;

/**
 * @author yaping
 * @contact lyping1986@gmail.com
 * @time 2012 Mar 16, 2012 3:48:16 PM
 * 
 */
public class GATKSAMRecordFilterStorage {

    /**
	 * 
	 */
    private BitSet mBitSet = null;

    private GATKSAMRecord GATKrecord = null;

    public GATKSAMRecordFilterStorage(GATKSAMRecord GATKrecord, ReferenceContext ref, BisulfiteArgumentCollection BAC) {
        this.GATKrecord = GATKrecord;
        BadBaseFilterBisulfite badReadPileupFilter = new BadBaseFilterBisulfite(ref, BAC);
        this.setGoodBases(badReadPileupFilter, true);
    }

    public GATKSAMRecordFilterStorage(GATKSAMRecord GATKrecord, BadBaseFilterBisulfite badReadPileupFilter) {
        this.GATKrecord = GATKrecord;
        this.setGoodBases(badReadPileupFilter, true);
    }

    public void setGoodBases(BadBaseFilterBisulfite filter, boolean abortIfAlreadySet) {
        if (mBitSet == null || !abortIfAlreadySet) mBitSet = filter.getGoodBases(GATKrecord);
    }

    public boolean isGoodBase(int index) {
        return (mBitSet == null || mBitSet.size() <= index ? true : mBitSet.get(index));
    }
}
