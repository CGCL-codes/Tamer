package com.halware.nakedide.eclipse.core.wiz.proj.clnt;

import java.lang.reflect.InvocationTargetException;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.pde.core.build.IBuildModelFactory;
import org.eclipse.pde.internal.core.build.WorkspaceBuildModel;
import org.eclipse.pde.internal.ui.wizards.IProjectProvider;
import org.eclipse.pde.ui.IFieldData;
import com.halware.nakedide.eclipse.core.wiz.proj.common.AbstractProjectCreationOperation;
import com.halware.nakedide.eclipse.core.wiz.proj.common.pages.GenerateExampleCodeProvider;

@SuppressWarnings("restriction")
public class ClientProjectCreationOperation extends AbstractProjectCreationOperation {

    private static final Logger LOGGER = Logger.getLogger(ClientProjectCreationOperation.class);

    protected Logger getLOGGER() {
        return LOGGER;
    }

    public ClientProjectCreationOperation(IFieldData data, IProjectProvider provider, GenerateExampleCodeProvider generateExampleCodeProvider) {
        super(data, provider, new ClientProjectTemplateWizard(generateExampleCodeProvider));
    }

    @Override
    protected void doAddAdditionalSourceOutputBuildEntries(WorkspaceBuildModel model, IBuildModelFactory factory) throws CoreException {
        addSourceOutputBuildEntry(model, "src/main/resources");
    }

    @Override
    protected void execute(IProgressMonitor monitor) throws CoreException, InvocationTargetException, InterruptedException {
        super.execute(monitor);
        moveImagesIntoBinary(monitor);
    }

    private void moveImagesIntoBinary(IProgressMonitor monitor) throws CoreException {
        IProject project = getProjectProvider().getProject();
        IJavaProject javaProject = JavaCore.create(project);
        IFolder srcMainResourcesFolder = project.getFolder("src/main/resources");
        IFolder imageFolder = getImageFolder(javaProject);
        if (!srcMainResourcesFolder.exists() || !imageFolder.exists()) {
            getLOGGER().warn("Unable to move images into source folder");
            return;
        }
        IPath srcMainResourcesPath = srcMainResourcesFolder.getFullPath().append("images");
        imageFolder.move(srcMainResourcesPath, true, monitor);
    }

    private IFolder getImageFolder(IJavaProject javaProject) throws JavaModelException {
        Object[] nonJavaResources = javaProject.getNonJavaResources();
        for (Object nonJavaResource : nonJavaResources) {
            if (!(nonJavaResource instanceof IFolder)) {
                continue;
            }
            IFolder folder = (IFolder) nonJavaResource;
            if (folder.getFullPath().lastSegment().equals("images")) {
                return folder;
            }
        }
        return null;
    }
}
