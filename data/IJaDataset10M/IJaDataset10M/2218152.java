package edu.asu.vspace.dspace.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import de.mpiwg.vspace.common.error.UserErrorException;
import de.mpiwg.vspace.extension.ExceptionHandlingService;
import de.mpiwg.vspace.util.images.ImageProcessor;
import edu.asu.vspace.dspace.dspaceMetamodel.extension.datamodel.IDatastream;

public class ImageSelectionWizardPage extends SelectionWizardPage {

    protected Image previewImage;

    protected Label imageLabel;

    public ImageSelectionWizardPage(String pageName) {
        super(pageName, DSpaceContentProvider.IMAGE);
    }

    @Override
    public void createControl(Composite parent) {
        super.createControl(parent);
        imageLabel = new Label(cContent, SWT.NONE);
    }

    protected void setPreview(IDatastream datastream) {
        imageLabel.setImage(null);
        fileLabel.setText(datastream.getFilename());
        fileLabel.pack();
        if (previewImage != null) previewImage.dispose();
        Point size = cContent.getSize();
        if (size == null) size = cContent.computeSize(SWT.DEFAULT, SWT.DEFAULT);
        Point resized = null;
        try {
            resized = ImageProcessor.getSize(datastream.getLocalPath(), size.x, size.y, size.x, size.y);
        } catch (UserErrorException e1) {
            ExceptionHandlingService.INSTANCE.handleException(e1);
        }
        if (resized != null) {
            ImageData imageData = new ImageData(datastream.getLocalPath());
            imageData = imageData.scaledTo(resized.x, resized.y);
            previewImage = new Image(getShell().getDisplay(), imageData);
            imageLabel.setImage(previewImage);
            imageLabel.pack();
        }
    }

    @Override
    public void dispose() {
        if (previewImage != null && !previewImage.isDisposed()) previewImage.dispose();
        super.dispose();
    }
}
