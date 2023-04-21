package shieh.pnn.core;

/**
 * Weights are normalized after Hebbian learning if the vector norm exceeds unity
 *
 * In the current version, the wt_min and wt_max are ignored. 
 */
public class LearnAlgoCompetitive extends AbstractLearnAlgo {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7663081875984386364L;

    protected double pow = 2;

    public LearnAlgoCompetitive() {
    }

    public LearnAlgoCompetitive(double pow) {
        this.pow = pow;
    }

    @Override
    public void calcWeights(Projection proj) {
        LearnAlgoUnnormalized.getInstance().calcWeights(proj);
        for (ConnGroup g : proj.cggi) {
            double sum = 0D, norm = 0D;
            for (Connection c : g) sum += Math.pow(c.wt_new, pow);
            if (sum > 1D) {
                norm = Math.pow(sum, 1D / pow);
                for (Connection c : g) c.wt_new /= norm;
            }
        }
    }
}
