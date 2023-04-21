package de.mpiwg.vspace.util.images;

import java.io.File;
import java.io.FilenameFilter;
import org.eclipse.core.resources.IProject;
import org.eclipse.swt.graphics.Point;
import de.mpiwg.vspace.common.error.UserErrorException;
import de.mpiwg.vspace.common.project.ProjectManager;

public class ImageScaler {

    public static final ImageScaler INSTANCE = new ImageScaler();

    private ImageScaler() {
    }

    public String getScaledImage(ImageData image, String folder, int maxWidth, int maxHeight) throws UserErrorException {
        if (image.getPathInProject() == null || image.getPathInProject().trim().equals("")) return null;
        File tempFolder = new File(folder);
        String imageId = "scene" + image.getPathInProject().hashCode();
        IProject project = ProjectManager.getInstance().getCurrentProject();
        String projectFolderPath = project.getLocation().toOSString() + File.separator;
        boolean doNextStep = true;
        String imagePath = image.getPathInProject();
        if (imagePath == null || imagePath.trim().equals("")) {
        }
        File imageToCopy = new File(projectFolderPath + image.getPathInProject());
        if (!imageToCopy.exists()) doNextStep = false;
        if (doNextStep) {
            Point newSize = ImageProcessor.getSize(imageToCopy.getAbsolutePath(), image.getWidth(), image.getHeight(), maxWidth, maxHeight);
            if (newSize == null) doNextStep = false;
            Point oldSize = ImageProcessor.getCurrentSize(imageToCopy.getAbsolutePath());
            if (oldSize == null) doNextStep = false;
            if (doNextStep) {
                if ((oldSize.x > newSize.x) || (oldSize.y > newSize.y)) {
                    String newFilename = imageId + "_w" + newSize.x + "h" + newSize.y;
                    String[] cachedImages = tempFolder.list(new IdFilenameFilter(newFilename));
                    if ((cachedImages != null) && (cachedImages.length > 0)) {
                        File cachedImage = new File(tempFolder.getAbsolutePath() + File.separator + cachedImages[0]);
                        if (cachedImage.exists()) return cachedImage.getAbsolutePath();
                    } else {
                        return ImageProcessor.resizeImage(imageToCopy.getAbsolutePath(), tempFolder, null, newFilename, newSize.x, newSize.y, ImageConstants.DEFAULT_SCENE_IMAGE_WIDTH, ImageConstants.DEFAULT_SCENE_IMAGE_HEIGHT);
                    }
                }
                return imageToCopy.getAbsolutePath();
            }
        }
        return null;
    }

    /**
	 * Filterclass to find already scaled images by id.
	 * 
	 * @author Julia Damerow
	 * 
	 */
    private class IdFilenameFilter implements FilenameFilter {

        private String id;

        public IdFilenameFilter(String id) {
            this.id = id;
        }

        public boolean accept(File dir, String name) {
            if (name.substring(0, name.lastIndexOf(".")).equals(id)) return true;
            return false;
        }
    }
}
