package de.mpiwg.vspace.metamodel.sitemap.helper.internal;

import org.eclipse.core.runtime.Path;
import de.mpiwg.vspace.generation.folders.share.GenFolderManager;
import de.mpiwg.vspace.genmodel.GenFile;
import de.mpiwg.vspace.genmodel.GenFolder;
import de.mpiwg.vspace.genmodel.GenmodelFactory;
import de.mpiwg.vspace.metamodel.sitemap.helper.share.IVSpaceElementSitemapHelper;
import de.mpiwg.vspace.metamodel.transformed.Image;

public abstract class AVSpaceElementSitemapHelper implements IVSpaceElementSitemapHelper {

    protected void prepareImage(Image image, String path, String filename) {
        setImageData(image, path, filename);
        setGenfile(image);
    }

    public void setImageData(Image image, String path, String id) {
        image.setImagePath(path);
        Path pathP = new Path(path);
        String ext = pathP.getFileExtension();
        image.setFileextension(ext);
        String lastSegment = pathP.lastSegment();
        if (lastSegment == null) image.setFilename(image.getId()); else {
            int indexExtDot = lastSegment.lastIndexOf(".");
            String name = lastSegment.substring(0, indexExtDot);
            image.setFilename(name);
        }
        image.setWidth(16);
        image.setHeight(16);
        image.setId(id);
    }

    public void setGenfile(Image image) {
        GenFile genfile = GenmodelFactory.eINSTANCE.createGenFile();
        GenFolder folder = GenFolderManager.INSTANCE.getNavigationImagesFolder();
        genfile.setParent(folder);
        genfile.setName(image.getFilename());
        genfile.setExtension(image.getFileextension());
        image.setGenfile(genfile);
    }
}
