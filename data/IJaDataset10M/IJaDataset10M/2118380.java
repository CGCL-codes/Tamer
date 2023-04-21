package cz.cuni.mff.ksi.jinfer.attrstats.utils;

import cz.cuni.mff.ksi.jinfer.attrstats.experiments.Experiment;
import cz.cuni.mff.ksi.jinfer.attrstats.experiments.quality.Quality;
import cz.cuni.mff.ksi.jinfer.attrstats.objects.IdSet;
import cz.cuni.mff.ksi.jinfer.base.interfaces.Pair;
import cz.cuni.mff.ksi.jinfer.base.objects.ImmutablePair;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Attribute statistics module utils.
 *
 * @author vektor
 */
public final class Utils {

    private Utils() {
    }

    /**
   * Formatter that formats numbers to a maximum of 5 fraction digits.
   */
    public static final Format FORMAT = NumberFormat.getInstance();

    static {
        ((NumberFormat) FORMAT).setMaximumFractionDigits(5);
    }

    public static final String NA = "N/A";

    /**
   * Converts a boolean to {@link String} in form yes/no.
   *
   * @param b Boolean to be converted.
   * @return "yes" or "no".
   */
    public static String boolToString(final boolean b) {
        return b ? "yes" : "no";
    }

    /**
   * Returns the best solution from the provided pool in the context of the
   * provided experiment.
   *
   * @param experiment Experiment, in context of which to find the best solution.
   * @param solutions List of solutions among which to find the best one.
   * @return Best solution along with its {@link Quality quality}.
   */
    public static Pair<IdSet, Quality> getBest(final Experiment experiment, final List<IdSet> solutions) {
        IdSet bestSolution = null;
        Quality maxQuality = Quality.ZERO;
        for (final IdSet solution : solutions) {
            final Quality quality = experiment.getQualityMeasurement().measure(experiment, solution);
            if (quality.getScalar() >= maxQuality.getScalar()) {
                maxQuality = quality;
                bestSolution = solution;
            }
        }
        return new ImmutablePair<IdSet, Quality>(bestSolution, maxQuality);
    }

    /**
   * Returns the worst solution from the provided pool in the context of the
   * provided experiment.
   *
   * @param experiment Experiment, in context of which to find the worst solution.
   * @param solutions List of solutions among which to find the worst one.
   * @return Worst solution.
   */
    public static IdSet getWorst(final Experiment experiment, final List<IdSet> solutions) {
        IdSet worstSolution = null;
        Quality minQuality = null;
        for (final IdSet solution : solutions) {
            final Quality quality = experiment.getQualityMeasurement().measure(experiment, solution);
            if (minQuality == null || quality.getScalar() < minQuality.getScalar()) {
                minQuality = quality;
                worstSolution = solution;
            }
        }
        return worstSolution;
    }

    /**
   * Returns a new list, constructed from the provided list with the provided
   * element appended at the end.
   *
   * @param <T> Type parameter.
   * @param list List to append to.
   * @param element Element to be appended.
   * @return New appended list.
   */
    public static <T> List<T> append(final List<T> list, final T element) {
        final List<T> ret = new ArrayList<T>(list);
        ret.add(element);
        return ret;
    }
}
