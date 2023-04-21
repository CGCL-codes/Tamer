package org.grandtestauto.distributed.test;

import org.grandtestauto.distributed.*;
import java.util.*;

/**
 * This package of tests can only be run on a machine that has
 * grade at least 10 and is not running Windows 2003.
 */
public class GTAGrade implements Grade {

    public int grade() {
        return 10;
    }

    public boolean compatible(AgentDetails agentDetails) {
        Properties sysProps = agentDetails.systemProperties();
        String os = sysProps.getProperty("os.name");
        return !"Windows 2003".equals(os);
    }
}
