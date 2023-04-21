package net.sourceforge.clearcase.commandline.output;

import net.sourceforge.clearcase.status.ClearCaseStatus;

/**
 * All classes that checks/parses the output from clearcase should implement
 * this interface.
 */
public interface PatternMatcherStrategy {

    public ClearCaseStatus check(StringBuffer output, ClearCaseStatus status);
}
