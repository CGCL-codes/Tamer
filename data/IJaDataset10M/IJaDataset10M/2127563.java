package de.schlund.pfixcore.editor2.frontend.handlers;

import de.schlund.pfixcore.editor2.frontend.util.EditorResourceLocator;
import de.schlund.pfixcore.editor2.frontend.wrappers.RestoreImage;
import de.schlund.pfixcore.generator.IHandler;
import de.schlund.pfixcore.generator.IWrapper;
import de.schlund.pfixcore.workflow.Context;
import de.schlund.util.statuscodes.StatusCodeLib;

/**
 * Handles image restore from backup
 * 
 * @author Sebastian Marsching <sebastian.marsching@1und1.de>
 */
public class RestoreImageHandler implements IHandler {

    public void handleSubmittedData(Context context, IWrapper wrapper) throws Exception {
        RestoreImage input = (RestoreImage) wrapper;
        int ret = EditorResourceLocator.getImagesResource(context).restoreBackup(input.getVersion(), input.getLastModTime().longValue());
        if (ret == 1) {
            input.addSCodeVersion(StatusCodeLib.PFIXCORE_EDITOR_IMAGES_IMAGE_UNDEF);
        } else if (ret == 2) {
            input.addSCodeVersion(StatusCodeLib.PFIXCORE_EDITOR_IMAGESUPLOAD_IMAGEUPL_HASCHANGED);
        }
    }

    public void retrieveCurrentStatus(Context context, IWrapper wrapper) throws Exception {
    }

    public boolean prerequisitesMet(Context context) throws Exception {
        return true;
    }

    public boolean isActive(Context context) throws Exception {
        return (EditorResourceLocator.getImagesResource(context).getSelectedImage() != null);
    }

    public boolean needsData(Context context) throws Exception {
        return true;
    }
}
