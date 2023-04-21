package org.osmorc.manifest.lang.headerparser;

import org.jetbrains.annotations.NotNull;

/**
   * A match describes how good a header known to a particular header provider matches a given header.
 * The name of the given header may contain typos and so there may be no perfect match. A perfect match will
 * have a Levenshtein distance of 0. Worse matches will have greater Levenshtein distances.
 * 
 * @author Robert F. Beeger (robert@beeger.net)
 */
public class HeaderNameMatch implements Comparable<HeaderNameMatch> {

    public HeaderNameMatch(int distance, @NotNull HeaderParserProvider provider) {
        _distance = distance;
        _provider = provider;
    }

    public int getDistance() {
        return _distance;
    }

    public HeaderParserProvider getProvider() {
        return _provider;
    }

    /**
   * Matches are compared baaed on their distance from their Levenshtein distance.
   * @param o
   * @return
   */
    public int compareTo(HeaderNameMatch o) {
        return getDistance() - o.getDistance();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HeaderNameMatch that = (HeaderNameMatch) o;
        return _distance == that._distance && _provider.equals(that._provider);
    }

    @Override
    public int hashCode() {
        int result = _distance;
        result = 31 * result + _provider.hashCode();
        return result;
    }

    private int _distance;

    private HeaderParserProvider _provider;
}
