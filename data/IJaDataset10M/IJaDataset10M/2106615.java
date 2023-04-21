package org.lemma.set.crisp;

import javax.annotation.Author;
import javax.annotation.Copyright;
import javax.annotation.Version;
import org.lemma.set.fuzzy.FuzzyFiniteSet;

/**
 * <p>
 *  TODO: Javadoc for {@code CrispFiniteSet}
 * </p>
 *
 * @author Christopher Beatty [christopher.beatty@gmail.com]
 * @version 1.0.0
 */
@Version(major = "1", minor = "0", patch = "0", date = "2006-10-10T12:00:00-5:00", authors = { @Author(name = "Christopher Beatty", email = "christopher.beatty@gmail.com") })
@Copyright
public interface CrispFiniteSet<D> extends FuzzyFiniteSet<D, org.lemma.number.Boolean> {
}
