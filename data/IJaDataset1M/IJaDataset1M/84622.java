package Bookmark;

import java.io.File;
import java.util.Collection;

/**
 *
 * @author sahaqiel
 */
interface MarkIOIfc {

    public String getFileDescription();

    public String[] getFileExtensions();

    public String getName();

    public String getFirstFileLine();

    public Collection<MarkItemIfc> openFile(File fileToImport);

    public boolean saveFile(File fileToSave, Collection<MarkItemIfc> bookmarks);
}
