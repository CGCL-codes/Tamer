package net.sourceforge.eclipsex.loader;

import java.io.IOException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;

/**
 * @author enrico
 *
 */
public class DocumentResource extends EXResource {

    private final EXResource _resource;

    private final IDocument _doc;

    private String _text = null;

    public DocumentResource(final EXLoader loader, final IFile file, final IDocument doc) throws Exception {
        _doc = doc;
        _resource = loader.findResource(file.getLocation().toFile());
    }

    public String getText() throws IOException {
        if (_text == null) {
            _text = _doc.get();
        }
        return _text;
    }

    public String getExtension() {
        return _resource.getExtension();
    }

    public String getName() {
        return _resource.getName();
    }

    public String getPackage() {
        return _resource.getPackage();
    }

    public boolean isDirectory() {
        return _resource.isDirectory();
    }

    public String getPath() {
        return _resource.getPath();
    }

    @Override
    public boolean exists() {
        return _resource.exists();
    }
}
