package org.jcvi.glk.elvira.report.locator;

public interface LocatorGroup {

    Locator getFastaLocator();

    Locator getAssemblyArchiveLocator();

    Locator getTraceArchiveLocater();

    Locator getTaxonomyLocator();

    Locator getNucleotideLocator();
}
