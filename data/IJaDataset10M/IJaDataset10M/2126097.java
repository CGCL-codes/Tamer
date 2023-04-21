package org.grandtestauto.distributed.test;

import org.grandtestauto.*;
import org.grandtestauto.distributed.*;

/**
 * Runs the unit tests for the gta package.
 *
 * @author Tim Lavers
 */
@SimpleGrade(grade = 3)
public class UnitTester extends CoverageUnitTester {

    public UnitTester(GrandTestAuto gta) {
        super(gta);
    }
}
