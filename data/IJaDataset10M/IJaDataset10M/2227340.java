package org.nexopenframework.ide.eclipse.ui.audit.impl;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.nexopenframework.ide.eclipse.commons.log.Logger;
import org.nexopenframework.ide.eclipse.commons.util.StringUtils;
import org.nexopenframework.ide.eclipse.ui.audit.AuditStrategy;
import org.nexopenframework.ide.eclipse.ui.audit.MarkerReporter;
import org.nexopenframework.ide.eclipse.ui.audit.support.LineNumberLocator;
import org.nexopenframework.ide.eclipse.ui.util.NexOpenProjectUtils;
import org.nexopenframework.ide.eclipse.ui.util.ServiceComponentUtil;

/**
 * <p>NexOpen Framework/p>
 * 
 * <p>Strategy for dealing with existence of TestCase classes for Configurable Services, Business Services
 * and Job Tasks.</p>
 * 
 * @see AuditStrategy
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class TestCaseStrategy implements AuditStrategy {

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return "TestCase existence for ServiceComponents (Business Components, " + "Configurable Components, Jobs Components,..)";
    }

    /**
	 * <p>It is not an error not develope testcases. So, it returns <code>false</code></p>
	 * 
	 * @see org.nexopenframework.ide.eclipse.ui.audit.model.IAuditProperty#isError()
	 */
    public boolean isError() {
        return false;
    }

    /**
	 * 
	 * @see org.nexopenframework.ide.eclipse.ui.audit.AuditStrategy#handleAudit(org.eclipse.jdt.core.IType, org.nexopenframework.ide.eclipse.ui.audit.MarkerReporter)
	 */
    public void handleAudit(final IType type, final MarkerReporter marker) throws CoreException {
        if (type.isInterface() || type.isEnum() || type.isAnnotation()) {
            return;
        }
        final IProject prj = type.getJavaProject().getProject();
        Logger.getLog().debug("TestCase strategy applied to :: " + prj.getName());
        IFolder testFolder = prj.getFolder("business/src/test/java");
        if (testFolder.exists()) {
            handleAuditTest(type, marker, testFolder);
        } else {
            testFolder = prj.getFolder("src/test/java");
            if (testFolder.exists()) {
                handleAuditTest(type, marker, testFolder);
                return;
            }
            Logger.getLog().error("Project [" + prj.getName() + "] is not a NexOpen project");
            throw new IllegalStateException("Not a NexOpen Project");
        }
    }

    /**
	 * <p></p>
	 * 
	 * @see org.nexopenframework.ide.eclipse.ui.audit.AuditStrategy#supports(org.eclipse.jdt.core.IType)
	 */
    public boolean supports(final IType type) throws JavaModelException {
        return ServiceComponentUtil.isBusinessImplementor(type) || ServiceComponentUtil.isServiceImplementor(type) || ServiceComponentUtil.isJobTask(type);
    }

    private void handleAuditTest(final IType type, final MarkerReporter marker, final IFolder testFolder) throws CoreException {
        final String testCaseName = NexOpenProjectUtils.is05xProject(type.getJavaProject().getProject()) ? StringUtils.replace(_getName(type), ".", "/") + "Test.java" : StringUtils.replace(type.getFullyQualifiedName(), ".", "/") + "Test.java";
        Logger.getLog().info("Checking existence of :: " + testCaseName);
        final IFile file = testFolder.getFile(testCaseName);
        if (!file.exists()) {
            final int lineNumber = LineNumberLocator.locateLineNumber(type.getElementName(), type);
            marker.reportProblem(type, "You SHOULD provide a JUnit TestCase located in business/src/test/java/" + testCaseName, 0, 0, lineNumber, false);
        }
    }

    private String _getName(final IType type) throws JavaModelException {
        return (ServiceComponentUtil.isBusinessImplementor(type) && type.getFullyQualifiedName().endsWith("Impl")) ? StringUtils.replace(type.getFullyQualifiedName(), "Impl", "") : type.getFullyQualifiedName();
    }
}
