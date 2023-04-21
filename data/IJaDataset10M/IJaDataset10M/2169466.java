package javaaxp.swingviewer.service.impl.rendering;

import java.awt.image.BufferedImage;
import javaaxp.core.service.CachingResourceLoader;
import javaaxp.core.service.IXPSFileAccess;
import javaaxp.core.service.model.document.IDocumentReference;

public class ImageLoader extends CachingResourceLoader<BufferedImage> {

    private IXPSFileAccess fFileAccess;

    private IDocumentReference fDocument;

    public ImageLoader(IDocumentReference docRef, IXPSFileAccess access) {
        fFileAccess = access;
        fDocument = docRef;
    }

    @Override
    protected BufferedImage loadResource(String uri) throws Exception {
        return fFileAccess.getImageResource(uri, fDocument);
    }
}
