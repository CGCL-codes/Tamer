package genetic;

import java.util.Random;
import tab.DebateData;
import tab.Judge;
import tab.Team;

public class JudgeAllocationGene implements Gene {

    private Judge mData[];

    private int mSize;

    private DebateData mDebateContext;

    private static Random mRan = new Random();

    public void randomize() {
        Judge[] lTemp = new Judge[mSize];
        for (int i = 0; i < mSize; ++i) {
            int k = mRan.nextInt(mSize);
            while (lTemp[k] != null) k = (k + 1) % mSize;
            lTemp[k] = mData[i];
        }
        mData = lTemp;
    }

    public Gene splice(Gene partner) {
        return null;
    }

    public void mutate() {
    }

    public double cost() {
        return 0;
    }

    public int compareTo(Gene pRhs) {
        if (pRhs instanceof JudgeAllocationGene) {
            JudgeAllocationGene lRhs = (JudgeAllocationGene) pRhs;
            double d = this.cost() - lRhs.cost();
            if (d > 0) return 1;
            if (d < 0) return -1;
            return 0;
        }
        throw new ClassCastException("Cannot compare JudgeAllocationGene with " + pRhs.getClass().getCanonicalName());
    }
}
