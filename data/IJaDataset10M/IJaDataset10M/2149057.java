package org.eclipse.update.internal.operations;

import java.lang.reflect.*;
import org.eclipse.core.runtime.*;
import org.eclipse.update.configuration.*;
import org.eclipse.update.core.*;
import org.eclipse.update.operations.*;

public class RevertConfigurationOperation extends Operation implements IRevertConfigurationOperation {

    private IInstallConfiguration config;

    private IProblemHandler problemHandler;

    public RevertConfigurationOperation(IInstallConfiguration config, IProblemHandler problemHandler) {
        super();
        this.config = config;
        this.problemHandler = problemHandler;
    }

    public boolean execute(IProgressMonitor monitor, IOperationListener listener) throws CoreException, InvocationTargetException {
        IStatus status = OperationsManager.getValidator().validatePendingRevert(config);
        if (status != null && status.getCode() == IStatus.ERROR) {
            throw new CoreException(status);
        }
        try {
            ILocalSite localSite = SiteManager.getLocalSite();
            localSite.revertTo(config, monitor, problemHandler);
            localSite.save();
            return true;
        } catch (CoreException e) {
            UpdateUtils.logException(e);
            throw e;
        }
    }
}
