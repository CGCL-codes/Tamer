package de.uniwuerzburg.informatik.mindmapper.file;

import java.io.IOException;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObjectExistsException;
import org.openide.loaders.MultiDataObject;
import org.openide.loaders.UniFileLoader;
import org.openide.util.NbBundle;

/**
 * DataLoader for XML Files of the mime-type text/x-mindmapper+xml for which
 * MindMapDataObject are created.
 * @author Christian "blair" Schwartz
 */
public class MindMapDataLoader extends UniFileLoader {

    /**
     * The mime-type of the xml file.
     */
    public static final String REQUIRED_MIME = "text/x-mindmapper+xml";

    private static final long serialVersionUID = 1L;

    /**
     * Create a new MindMapDataLoader.
     */
    public MindMapDataLoader() {
        super("de.uniwuerzburg.informatik.mindmapper.file.MindMapDataObject");
    }

    @Override
    protected String defaultDisplayName() {
        return NbBundle.getMessage(MindMapDataLoader.class, "LBL_MindMapperFile_loader_name");
    }

    @Override
    protected void initialize() {
        super.initialize();
        getExtensions().addMimeType(REQUIRED_MIME);
    }

    /**
     * Create the DataObject for the primary file.
     * @param primaryFile The file to create a MindMapDataObject for.
     * @return The MindMapDataObject for the primary file.
     * @throws org.openide.loaders.DataObjectExistsException If a DataObject
     * already exists for the Primary File.
     * @throws java.io.IOException
     */
    protected MultiDataObject createMultiObject(FileObject primaryFile) throws DataObjectExistsException, IOException {
        return new MindMapDataObject(primaryFile, this);
    }

    @Override
    protected String actionsContext() {
        return "Loaders/" + REQUIRED_MIME + "/Actions";
    }
}
