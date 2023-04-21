package fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp;

public class Ocl4tstBuilder implements fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.IOcl4tstBuilder {

    public boolean isBuildingNeeded(org.eclipse.emf.common.util.URI uri) {
        return false;
    }

    public org.eclipse.core.runtime.IStatus build(fr.inria.uml4tst.papyrus.ocl4tst.ocl4tst.resource.ocl4tst.mopp.Ocl4tstResource resource, org.eclipse.core.runtime.IProgressMonitor monitor) {
        return org.eclipse.core.runtime.Status.OK_STATUS;
    }
}
