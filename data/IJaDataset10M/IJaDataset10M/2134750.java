package gov.nasa.jpf;

import gov.nasa.jpf.jvm.JVM;
import gov.nasa.jpf.search.Search;
import gov.nasa.jpf.util.Printable;

/**
 * abstraction that is used by Search objects to determine if program
 * properties have been violated (e.g. NoUncaughtExceptions)
 */
public interface Property extends Printable {

    boolean check(Search search, JVM vm);

    String getErrorMessage();

    String getExplanation();

    void reset();
}
