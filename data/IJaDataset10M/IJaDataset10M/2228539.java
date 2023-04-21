package net.sf.wwusmart.browsing;

import java.io.Serializable;
import java.util.*;
import net.sf.wwusmart.algorithms.framework.*;

/**
 * Represents a combination of matchings on the same set of shapes. The
 * combination is done by a <code>CombinationAlgorithm</code>.
 *
 * The different matchings are combined into a single matching.
 *
 * @author Stephen
 * @version $Rev: 777 $
 */
public class Combination extends AlgorithmBrowsingOperation implements Serializable {

    static final long serialVersionUID = 8923756519237856387L;

    /**
     * The list of the matchings, which shall be combined. Must not be
     * <code>null</code> or empty.
     */
    List<Match> matchList;

    /**
     * The list of the per match parameters settings of the combination
     * algorithm <code>algorithm</code> for all matches in
     * <code>matchList</code> in the same order.
     */
    List<ParametersSetting> perMatchParametersSettings;

    /**
     * The least similarity index allowed for shapes to appear in the result.
     */
    double threshold;

    /**
     * Constructor for <code>Combination</code>.
     *
     * @param matchList list of the matchings, which shall be combined. Must not
     *  be <code>null</code> or empty.
     * @param perMatchParametersSettings    the per match parameters settings of
     *  <code>combinationAlgorithm</code> for the matchings in
     *  <code>matchList</code> in the same order.
     * @param combinationAlgorithm  the algorithm, which is used to combine the matchings. Must
     *  not be <code>null</code>.
     * @param params    the parameter setting for
     *  <code>combinationAlgorithm</code>. Must not be <code>null</code>.
     * @param threshold the least similarity index allowed for shapes to appear
     *  in the result.
     */
    public Combination(List<Match> matchList, List<ParametersSetting> perMatchParametersSettings, CombinationAlgorithm combinationAlgorithm, ParametersSetting params, double threshold) {
        super(combinationAlgorithm, params);
        if (matchList.size() != perMatchParametersSettings.size()) throw new IllegalArgumentException("Number of matches and match parameters must be equal!");
        this.perMatchParametersSettings = perMatchParametersSettings;
        if (matchList == null || matchList.size() < 2) throw new IllegalArgumentException("'matchList' must contain at least two matchings!");
        this.matchList = matchList;
        if (!(0 <= threshold && threshold <= 1)) throw new RuntimeException("Threshold has to be between 0 and 1.");
        this.threshold = threshold;
    }

    /**
     * Returns a clone of the combination with <code>BaseResult</code> and
     * <code>Result</code> set to <code>null</code>.
     *
     * @return a clone of the combination.
     */
    @Override
    protected Object clone() {
        Vector<Match> newMatchList = new Vector<Match>(matchList.size());
        for (Match m : matchList) {
            newMatchList.add((Match) m.clone());
        }
        Vector<ParametersSetting> newPerMatchSettings = new Vector<ParametersSetting>(perMatchParametersSettings.size());
        for (ParametersSetting ps : perMatchParametersSettings) {
            newPerMatchSettings.add(ps);
        }
        return new Combination(newMatchList, newPerMatchSettings, (CombinationAlgorithm) algorithm, parametersSetting, threshold);
    }

    /**
     * returns the list of the matchings, which shall be combined.
     *
     * @return list of the matchings.
     */
    public List<Match> getMatchList() {
        return matchList;
    }

    /**
     * Returns a list of the per match parameters settings of the combination
     * algorithm <code>algorithm</code> for all matches in
     * <code>matchList</code> in the same order.
     */
    List<ParametersSetting> getPerMatchSettings() {
        return perMatchParametersSettings;
    }

    /**
     * Getter for the {@linkplain ParametersSetting} used for the Match
     * with index {@code idx} in this Combinations Match list.
     * 
     * @throws IllegalArgumentException If there is no Match with index {@code
     *                                 idx}.
     * @param idx Index of the Match of which the ParametersSettings should be
     *            returned.
     * @return The ParametersSetting
     */
    public ParametersSetting getMatchParametersSetting(int idx) {
        if (idx < 0 || idx >= matchList.size()) throw new IllegalArgumentException("there is no match " + idx + "!");
        return perMatchParametersSettings.get(idx);
    }

    /**
     * returns the algorithm, which is used to combine the matchings.
     *
     * @return the combination algorithm.
     */
    public CombinationAlgorithm getCombinationAlgorithm() {
        return (CombinationAlgorithm) algorithm;
    }

    /**
     * returns the least similarity index allowed for shapes to appear in the
     *  result.
     *
     * @return the least similarity index allowed for shapes to appear in the
     *  result.
     */
    public double getThreshold() {
        return threshold;
    }
}
