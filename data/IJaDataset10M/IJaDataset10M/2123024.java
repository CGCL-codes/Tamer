package org.s3b.service;

import java.util.List;

/**
 * Interface definition of query elements
 *
 * @author   Krystian Samp, Adam Westerski, Sebastian Ryszard Kruk,
 */
public interface Element extends org.corrib.jonto.wordnet.LinkRankingArgument {

    public void addEntry(Object entry);

    public void addEntries(List<Object> entries);
}
