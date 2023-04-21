package org.axsl.hyphen;

import org.axsl.i18n.Country;
import org.axsl.i18n.Language;

/**
 * Encapsulates a "word" and provides information about hyphenation break
 * possibilities in that a word.
 */
public interface Hyphenation {

    /**
     * Provides hyphenation point information for the underlying word.
     * Information for each character in the word is provided, some implicitly,
     * some explicitly.
     * Points not reported have an implicit value of zero (see
     * {@link #getWeights()} for details about the values.
     * Points reported (that is, that have an explicit value) contain two pieces
     * of information.
     * First is the index to the character being reported, which is returned
     * by this method.
     * Second is the explicit value of that hyphenation point, which is returned
     * in the corresponding element of {@link #getWeights()}.
     * <strong>Caveat:</strong> The array returned by this method is
     * <em>not necessarily</em> a list of valid hyphenation points.
     * Any corresponding negative values in {@link #getWeights()}
     * indicate a point that should be avoided.
     * See the {@code includeInhibitors} parameter on
     * {@link HyphenationServer#hyphenate(char[], int, int, Language, Country,
     * int, int, boolean)} and its overloaded sister methods.
     * @return A list of indexes to all non-zero hyphenation points.
     * @see #getWeights()
     */
    byte[] getPoints();

    /**
     * Provides hyphenation point values for the underlying word.
     * The returned values have a one-to-one correspondence with the items
     * returned by {@link #getPoints()}.
     * Valid values range from -2 (a truly awful hyphenation point) to +3 (an
     * ideal hyphenation point).
     * No returned values should be zero.
     * All points in the words that have no explicit information reported have
     * an implicit value of zero.
     * The implied zero indicates that the word should not be hyphenated at
     * that point, but that it is "less bad" than a lower explicit value.
     * Note that it is important to report the negative values as well as the
     * possible for cases where a "less bad" hyphenation point is better than
     * no hyphenation point at all.
     * @return An array containing the weights that correspond to each element
     * in {@link #getPoints()}.
     * @see #getPoints()
     * @see #getLiangWeights()
     */
    byte[] getWeights();

    /**
     * <p>Provides the same information as {@link #getWeights()}, except the
     * values returned use the Liang input scheme.
     * The Liang input scheme uses values between 0 and 5 (we exclude the 0
     * values since they are implied).
     * We have expanded the range to between 0 and 9.
     * Odd values indicate possible hyphenation points, even values (including
     * 0) indicate points that should not be hyphenated.
     * Higher numbers indicate a greater magnitude of "goodness" for odd
     * numbers, and a greater magnitude of "badness" for even numbers.
     * This method is included for backward compatibility with systems that
     * may already be dependent on the Liang input scheme.
     * (The Liang input scheme was probably optimized for input and point
     * selection efficiency, but is somewhat counter-intuitive for an
     * application that is evaluating the results).</p>
     *
     * <p>The following table compares the values returned by the two
     * methods.
     * Note that, except for the difference between "good" and "bad", the
     * various values have no absolute meanings, but only meanings
     * relative to the others.
     * Implementations are free to use some subset of the possible return
     * values.
     * For example, some implementations may return Liang values in the range
     * 0 to 5, and others may return them in the range 0 to 9.</p>
     * <table border="">
     *   <tr>
     *     <th>Description</th>
     *     <th align="right">Weight</th>
     *     <th align="right">Liang Input Scheme</th>
     *   </tr>
     *   <tr>
     *     <td>better than below</td>
     *     <td align="right">5</td>
     *     <td align="right">9</td>
     *   </tr>
     *   <tr>
     *     <td>better than below</td>
     *     <td align="right">4</td>
     *     <td align="right">7</td>
     *   </tr>
     *   <tr>
     *     <td>better than below</td>
     *     <td align="right">3</td>
     *     <td align="right">5</td>
     *   </tr>
     *   <tr>
     *     <td>better than below</td>
     *     <td align="right">2</td>
     *     <td align="right">3</td>
     *   </tr>
     *   <tr>
     *     <td>allowable</td>
     *     <td align="right">1</td>
     *     <td align="right">1</td>
     *   </tr>
     *   <tr>
     *     <td>avoid</td>
     *     <td align="right">0</td>
     *     <td align="right">0</td>
     *   </tr>
     *   <tr>
     *     <td>worse than above</td>
     *     <td align="right">-1</td>
     *     <td align="right">2</td>
     *   </tr>
     *   <tr>
     *     <td>worse than above</td>
     *     <td align="right">-2</td>
     *     <td align="right">4</td>
     *   </tr>
     *   <tr>
     *     <td>worse than above</td>
     *     <td align="right">-3</td>
     *     <td align="right">6</td>
     *   </tr>
     *   <tr>
     *     <td>worse than above</td>
     *     <td align="right">-4</td>
     *     <td align="right">8</td>
     *   </tr>
     * </table>
     *
     * @return An array containing the Liang input weights that correspond to
     * each element in {@link #getPoints()}.
     * @see #getWeights()
     */
    byte[] getLiangWeights();

    /**
     * Provides detailed information about a hyphenation break opportunity
     * if such information is available.
     * Most hyphen break opportunities do not need such information, as their
     * location (provided by {@link #getPoints()} and values (provided by
     * {@link #getWeights()} and {@link #getLiangWeights()} are sufficient to
     * tell client applications everything they need to make hyphenation
     * decisions.
     * {@link HyphenBreak} provides extra information for certain "hard" cases.
     * @param index The index into the array returned by {@link #getPoints()}
     * that specifies the point for which a HyphenBreak is desired.
     * @return The HyphenBreak instance for the hyphen break opportunity at
     * {@code index}, or null if there is none.
     */
    HyphenBreak getHyphenBreak(int index);
}
