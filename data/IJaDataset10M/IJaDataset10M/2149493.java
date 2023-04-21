package org.imogene.web.contrib;

import java.io.File;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.imogene.studio.contrib.ImogeneModelNature;
import org.imogene.studio.contrib.interfaces.GenerationManager;
import org.imogene.studio.contrib.interfaces.IconCopyTask;

/**
 * Copy the images define in the project model to the generated project.
 * @author Medes-IMPS 
 */
public class WebIconCopyTask implements IconCopyTask {

    @Override
    public void copyIcons(GenerationManager manager) throws CoreException {
        ImogeneModelNature mmn = (ImogeneModelNature) manager.getSelectedProject().getNature(ImogeneModelNature.ID);
        String imageFolder = "src/org/imogene/" + mmn.getModelName().toLowerCase() + "/public/images";
        IFolder destinationFolder = manager.getGeneratedProject().getFolder(imageFolder);
        File dire = new File(destinationFolder.getLocation().toOSString());
        if (!dire.exists()) {
            dire.mkdirs();
            manager.getGeneratedProject().refreshLocal(IResource.DEPTH_INFINITE, null);
        }
        for (IResource res : mmn.getIconsFolder().members()) res.copy(destinationFolder.getFullPath().append(res.getName()), true, null);
    }
}
