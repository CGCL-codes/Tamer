package com.volantis.mcs.eclipse.builder.common.policies.impl;

import com.volantis.mcs.eclipse.builder.common.policies.EclipsePolicyBuilderManager;
import com.volantis.mcs.eclipse.builder.common.policies.GenericCreatePolicyConfiguration;
import com.volantis.mcs.eclipse.builder.common.policies.PolicyFileAccessException;
import com.volantis.mcs.eclipse.builder.common.policies.StandalonePolicyFileAccessor;
import com.volantis.mcs.eclipse.core.MCSProjectNature;
import com.volantis.mcs.objects.FileExtension;
import com.volantis.mcs.policies.PolicyBuilder;
import com.volantis.mcs.policies.PolicyType;
import com.volantis.mcs.project.BuilderBatchOperation;
import com.volantis.mcs.project.PolicyBuilderManager;
import com.volantis.mcs.project.TransactionLevel;
import com.volantis.mcs.repository.RepositoryException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A policy manager implementation to allow an Eclipse project to be exposed as
 * a policy repository.
 *
 * <p>Note that no support for batch operations or transaction levels other than
 * {@link com.volantis.mcs.project.TransactionLevel#NONE} is provided.</p>
 *
 * <p>This implementation depends on the {@link StandalonePolicyFileAccessor}
 * functionality
 */
public class EclipsePolicyBuilderManagerImpl implements EclipsePolicyBuilderManager {

    private IProject project;

    private StandalonePolicyFileAccessor standaloneAccessor = new StandalonePolicyFileAccessor();

    public EclipsePolicyBuilderManagerImpl(IProject project) {
        this.project = project;
    }

    protected IProject getProject() {
        return project;
    }

    public boolean supportsTransactionLevel(TransactionLevel level) {
        return TransactionLevel.NONE == level;
    }

    public void beginBatchOperation(TransactionLevel level) throws RepositoryException {
        if (!supportsTransactionLevel(level)) {
            throw new RepositoryException("Invalid transaction level");
        }
    }

    public void endBatchOperation() throws RepositoryException {
    }

    public void abortBatchOperation() throws RepositoryException {
    }

    public void performBatchOperation(BuilderBatchOperation operation, TransactionLevel level) throws RepositoryException {
    }

    public Collection getPolicyBuilderNames(PolicyType policyType) throws RepositoryException {
        return (policyType == null) ? getAllPolicyBuilderNames() : getTypedPolicyBuilderNames(policyType);
    }

    /**
     * Gets all policy names that match a specified (non-null) policy type.
     *
     * @param policyType The type of policy to enumerate.
     * @return A collection of names for the specified policy type
     * @throws RepositoryException if an error occurs
     */
    private Collection getTypedPolicyBuilderNames(PolicyType policyType) throws RepositoryException {
        Collection policyNames = new ArrayList();
        try {
            FileExtension extension = FileExtension.getFileExtensionForPolicyType(policyType);
            IFolder policySourceFolder = project.getFolder(getPolicySourcePath());
            addPolicyNames(policySourceFolder, extension, policyNames, policySourceFolder.getProjectRelativePath());
        } catch (CoreException ce) {
            throw new RepositoryException(ce);
        }
        return policyNames;
    }

    /**
     * Gets all policy names that match any policy type.
     *
     * @return A collection of names for all policy types
     * @throws RepositoryException if an error occurs
     */
    private Collection getAllPolicyBuilderNames() throws RepositoryException {
        Collection policyNames = new ArrayList();
        Iterator types = PolicyType.getPolicyTypes().iterator();
        while (types.hasNext()) {
            policyNames.addAll(getTypedPolicyBuilderNames((PolicyType) types.next()));
        }
        return policyNames;
    }

    /**
     * Recursively add policies with a specified file extension into a list.
     *
     * @param folder The folder to process recursively
     * @param extension The file extension to match
     * @param policyNames A list of policy names to add matching policies to
     */
    private void addPolicyNames(IFolder folder, FileExtension extension, Collection policyNames, IPath policyRoot) throws CoreException {
        IResource[] children = folder.members();
        for (int i = 0; i < children.length; i++) {
            if (children[i] instanceof IFolder) {
                addPolicyNames((IFolder) children[i], extension, policyNames, policyRoot);
            } else if (children[i] instanceof IFile) {
                IFile file = (IFile) children[i];
                String extensionString = file.getFileExtension();
                if (extension.matches(extensionString)) {
                    IPath policyPath = file.getProjectRelativePath();
                    if (policyRoot.isPrefixOf(policyPath)) {
                        IPath relativePolicyPath = policyPath.removeFirstSegments(policyRoot.segmentCount());
                        String policyName = relativePolicyPath.toString();
                        if (!policyName.startsWith("/")) {
                            policyName = "/" + policyName;
                        }
                        policyNames.add(policyName);
                    } else {
                        throw new IllegalStateException("Unexpected policy location");
                    }
                }
            }
        }
    }

    public PolicyBuilder getPolicyBuilder(String name) throws RepositoryException {
        PolicyBuilder policy = null;
        try {
            IFile policyFile = getFileFromPolicyName(name);
            if (policyFile.exists()) {
                policy = standaloneAccessor.loadPolicy(policyFile);
            }
        } catch (CoreException ce) {
            throw new RepositoryException(ce);
        } catch (PolicyFileAccessException pfae) {
            throw new RepositoryException(pfae);
        }
        return policy;
    }

    public void addPolicyBuilder(PolicyBuilder policy) throws RepositoryException {
        try {
            IFile policyFile = getFileFromPolicyName(policy.getName());
            standaloneAccessor.createPolicy(policyFile.getFullPath(), policy, new GenericCreatePolicyConfiguration(null));
        } catch (CoreException ce) {
            throw new RepositoryException(ce);
        } catch (PolicyFileAccessException pfae) {
            throw new RepositoryException(pfae);
        }
    }

    private IFile getFileFromPolicyName(String policyName) throws CoreException {
        IPath policyPath = new Path(policyName);
        IPath policySourcePath = getPolicySourcePath();
        IPath projectRelativePolicyPath = policySourcePath.append(policyPath.makeRelative());
        IFile policyFile = project.getFile(projectRelativePolicyPath);
        return policyFile;
    }

    public boolean removePolicyBuilder(String name) throws RepositoryException {
        boolean removed = false;
        try {
            IFile policyFile = getFileFromPolicyName(name);
            if (policyFile.exists()) {
                standaloneAccessor.deletePolicy(policyFile);
                removed = true;
            }
        } catch (CoreException ce) {
            throw new RepositoryException(ce);
        } catch (PolicyFileAccessException pfae) {
            throw new RepositoryException(pfae);
        }
        return removed;
    }

    public boolean updatePolicyBuilder(PolicyBuilder policy) throws RepositoryException {
        boolean updated = false;
        try {
            IFile policyFile = getFileFromPolicyName(policy.getName());
            standaloneAccessor.savePolicy(policy, policyFile, new NullProgressMonitor());
        } catch (CoreException ce) {
            throw new RepositoryException(ce);
        } catch (PolicyFileAccessException pfae) {
            throw new RepositoryException(pfae);
        }
        return updated;
    }

    public boolean requiresUnderlyingPolicyBuilderManager() {
        return false;
    }

    public void setUnderlyingPolicyBuilderManager(EclipsePolicyBuilderManager underlying) {
    }

    private IPath getPolicySourcePath() throws CoreException {
        MCSProjectNature mcsNature = (MCSProjectNature) project.getNature(MCSProjectNature.NATURE_ID);
        return mcsNature.getPolicySourcePath();
    }
}
