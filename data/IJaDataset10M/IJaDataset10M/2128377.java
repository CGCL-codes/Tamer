package edu.usc.epigenome.uecgatk.YapingWalker;

import org.broad.tribble.bed.SimpleBEDFeature;
import org.broadinstitute.sting.gatk.contexts.AlignmentContext;
import org.broadinstitute.sting.utils.GenomeLoc;
import org.broadinstitute.sting.utils.pileup.ReadBackedPileupImpl;

public class NDRCallContext {

    private AlignmentContext context = null;

    private GenomeLoc loc = null;

    private boolean flag = false;

    private boolean wcgFlag = false;

    private boolean hcgFlag = false;

    private SimpleBEDFeature rodLoci = null;

    private double aveMethyInWindow = Double.NaN;

    public NDRCallContext(AlignmentContext context, GenomeLoc loc) {
        this.context = context;
        this.loc = loc;
    }

    public NDRCallContext(AlignmentContext context, GenomeLoc loc, SimpleBEDFeature rodLoci) {
        this.context = context;
        this.loc = loc;
        this.rodLoci = rodLoci;
    }

    public AlignmentContext getRealContext() {
        return context;
    }

    public boolean hasRealContext() {
        if (context == null) {
            return false;
        }
        return context.hasBasePileup();
    }

    public AlignmentContext getFakeContext() {
        ReadBackedPileupImpl fakePileup = new ReadBackedPileupImpl(loc);
        AlignmentContext fakeContext = new AlignmentContext(loc, fakePileup);
        return fakeContext;
    }

    public GenomeLoc getLoc() {
        return loc;
    }

    public SimpleBEDFeature getRodLoc() {
        return rodLoci;
    }

    public boolean getCytosinePatternFlag() {
        return flag;
    }

    public void setCytosinePatternFlag(boolean cytosinePatternFlag) {
        this.flag = cytosinePatternFlag;
    }

    public void setWcgPatternFlag(boolean wcgPatternFlag) {
        this.wcgFlag = wcgPatternFlag;
    }

    public boolean getWcgPatternFlag() {
        return wcgFlag;
    }

    public void setHcgPatternFlag(boolean hcgPatternFlag) {
        this.hcgFlag = hcgPatternFlag;
    }

    public boolean getHcgPatternFlag() {
        return hcgFlag;
    }

    public void setGchMethyInWindow(double aveMethy) {
        this.aveMethyInWindow = aveMethy;
    }

    public double getGchMethyInWindow() {
        return this.aveMethyInWindow;
    }
}
