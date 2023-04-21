package org.apache.commons.fileupload;

import java.io.File;

/**
 * <a href="LiferayFileItemFactory.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.8 $
 *
 */
public class LiferayFileItemFactory extends DefaultFileItemFactory {

    public static final int DEFAULT_SIZE = 0;

    public LiferayFileItemFactory(File tempDir) {
        _tempDir = tempDir;
    }

    public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
        return new LiferayFileItem(fieldName, contentType, isFormField, fileName, DEFAULT_SIZE, _tempDir);
    }

    private File _tempDir;
}
