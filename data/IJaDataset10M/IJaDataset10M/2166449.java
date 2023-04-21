package sample.threadCheck.xmlOnly;

import edu.rice.cs.cunit.threadCheck.OnlyRunBy;
import edu.rice.cs.cunit.threadCheck.ThreadDesc;

/**
 * A test for annotating classes as "always ok for any thread" in a superclass.
 * @author Mathias Ricken
 */
@OnlyRunBy(@ThreadDesc(name = ".*"))
public interface ThreadCheckSample6Interface {

    void run();
}
