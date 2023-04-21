package Statistics;

/**
 * Specifies how the DMGReduction attribute will act.
 * @author Hetalar
 * 07/02/2012 
 */
public class DMGReduction extends Stat {

    private static final String STAT_NAME = "DMG REDUCTION";

    public DMGReduction(Statistics stat, int startingValue) {
        super(stat, startingValue, STAT_NAME, Statistics.STAT_DMG_REDUCTION);
    }

    public DMGReduction(Statistics stat) {
        super(stat, 0, STAT_NAME, Statistics.STAT_DMG_REDUCTION);
    }

    protected void applyStat(int oldValue) {
    }
}
