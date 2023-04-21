package sample.threadCheck.old;

import edu.rice.cs.cunit.threadCheck.OnlyRunBy;
import edu.rice.cs.cunit.threadCheck.NotRunBy;

/**
 * A test for annotating classes and methods with allowed and disallowed threads and superclasses.
 * @author Mathias Ricken
 */
@OnlyRunBy(threadNames = { "interface3-1" })
@NotRunBy(threadNames = { "interface3-2" })
public interface ThreadCheckSample4Interface3 {

    @OnlyRunBy(threadNames = { "interface3method-1" })
    @NotRunBy(threadNames = { "interface3method-2" })
    void run3();
}
