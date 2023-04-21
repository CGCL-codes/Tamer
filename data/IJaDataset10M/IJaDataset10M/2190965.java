package org.nexopenframework.ide.eclipse.ui.audit.impl;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.nexopenframework.ide.eclipse.commons.log.Logger;
import org.nexopenframework.ide.eclipse.ui.audit.AuditStrategy;
import org.nexopenframework.ide.eclipse.ui.audit.MarkerReporter;
import org.nexopenframework.ide.eclipse.ui.audit.support.LineNumberLocator;
import org.nexopenframework.ide.eclipse.ui.util.ServiceComponentUtil;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Checks existence of <code>@ServiceGatewayRef</code> jsr-175 metadata
 * correctly done (it refers a GoF Business Facade)</p>
 * 
 * @see AuditStrategy
 * @author Francesc Xavier Magdaleno
 * @version 1.0
 * @since 1.0
 */
public class ServiceGatewayRefStrategy implements AuditStrategy {

    private boolean enabled;

    public boolean isEnabled() {
        return enabled;
    }

    /**
	 * 
	 * @see org.nexopenframework.ide.eclipse.ui.audit.model.IAuditProperty#setEnabled(boolean)
	 */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
	 * 
	 * @see org.nexopenframework.ide.eclipse.ui.audit.model.IAuditProperty#getName()
	 */
    public String getName() {
        return "Proper use of @ServiceGatewayRef in controllers components";
    }

    /**
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
        final IField[] fields = type.getFields();
        if (fields == null || fields.length == 0) {
            Logger.getLog().info("No fields found in " + type.getFullyQualifiedName());
            return;
        }
        final String source = type.getSource();
        if (ServiceComponentUtil.isAnnotationPresent(source, new String[] { "ServiceGatewayRef" })) {
            for (final IField f : fields) {
                if (ServiceComponentUtil.isAnnotationPresent(f.getSource(), new String[] { "ServiceGatewayRef" })) {
                    handleServiceGatewayRef(type, marker, f);
                }
            }
        } else {
            Logger.getLog().info("No @ServiceGatewayRef found in " + type.getFullyQualifiedName());
        }
    }

    /**
	 * 
	 * @see org.nexopenframework.ide.eclipse.ui.audit.AuditStrategy#supports(org.eclipse.jdt.core.IType)
	 */
    public boolean supports(final IType type) throws JavaModelException {
        boolean bc = ServiceComponentUtil.isServiceImplementor(type) || ServiceComponentUtil.isJobTask(type) || ServiceComponentUtil.isBusinessImplementor(type);
        return !bc;
    }

    /**
	 * @param type
	 * @param marker
	 * @param f
	 * @throws CoreException
	 */
    private void handleServiceGatewayRef(final IType type, final MarkerReporter marker, final IField f) throws CoreException {
        final String str_type = Signature.toString(f.getTypeSignature());
        if (str_type.indexOf(".") > -1) {
            reportProblem(type, str_type, marker, str_type, f);
        }
        final IImportDeclaration[] ida = type.getCompilationUnit().getImports();
        for (final IImportDeclaration id : ida) {
            if ((id.getElementName().indexOf(str_type) > -1) && reportProblem(type, id.getElementName(), marker, str_type, f)) {
                break;
            }
        }
    }

    /**
	 * @param type
	 * @param typeName
	 * @param marker
	 * @param str_type
	 * @param f
	 * @return
	 * @throws CoreException
	 */
    private boolean reportProblem(final IType type, final String typeName, final MarkerReporter marker, final String str_type, final IField f) throws CoreException {
        final IType e_type = type.getJavaProject().findType(typeName);
        if (!(e_type.isInterface() && ServiceComponentUtil.isBusinessImplementor(e_type))) {
            Logger.getLog().debug("Element type NOT a Business Facade Interface :: " + e_type.getFullyQualifiedName());
            final int lineNumber = LineNumberLocator.locateLineNumber(str_type + f.getElementName(), type);
            marker.reportProblem(type, "You MUST use @ServiceGatewayRef related to Business Facade Interfaces", 0, 0, lineNumber, false);
            return true;
        }
        return false;
    }
}
