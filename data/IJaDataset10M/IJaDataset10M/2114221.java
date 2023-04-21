package net.sf.istcontract.editor;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IProjectActionFilter;

public class ISTContractNature implements IProjectNature, IProjectActionFilter {

    private IProject project;

    public ISTContractNature() {
    }

    public void configure() throws CoreException {
    }

    public void deconfigure() throws CoreException {
    }

    public IProject getProject() {
        return project;
    }

    public void setProject(IProject project) {
        this.project = project;
    }

    public boolean testAttribute(Object target, String name, String value) {
        return false;
    }
}
