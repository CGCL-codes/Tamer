package org.robinfinch.clasj.competition;

import java.util.Collection;
import org.robinfinch.clasj.universals.TimeLineException;

/**
 * An entry rule describes which {@link org.robinfinch.clasj.competition.Competitor competitor} is
 * entitled to a place in a {@link org.robinfinch.clasj.competition.Competition competition}.
 * @author Mark Hoogenboom
 */
public interface EntryRule {

    public Competitor getCompetitor(Collection<Competitor> excluded) throws TimeLineException;
}
