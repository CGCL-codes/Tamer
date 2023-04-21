package com.myc.findbugs.plugin;

import edu.umd.cs.findbugs.BugReporter;
import edu.umd.cs.findbugs.ba.Hierarchy;

/**
 * �������н�Exceptionδ����־ϵͳ����ĵط�.
 * @author Ma Yichao
 */
public class FindExceptionPrint extends AbstractFindbugsPlugin {

    public FindExceptionPrint(BugReporter br) {
        super(br);
    }

    @Override
    public void sawOpcode(int seen) {
        try {
            if (seen == INVOKEVIRTUAL && Hierarchy.isSubtype(getDottedClassConstantOperand(), "java.lang.Exception") && "printStackTrace".equals(getNameConstantOperand())) {
                reportBug("MYCSOFT_EXCEPTION_PRINT", NORMAL_PRIORITY);
            }
        } catch (ClassNotFoundException ex) {
            bugReporter.reportMissingClass(ex);
        }
    }
}
