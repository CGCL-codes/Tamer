package pkg.multipart;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;

/**
 *
 * @author will
 */
public class MemoryItemFactory implements FileItemFactory {

    public FileItem createItem(String fieldName, String contentType, boolean isFormField, String fileName) {
        MemoryFileItem i = new MemoryFileItem();
        i.setFieldName(fieldName);
        i.setContentType(contentType);
        i.setFormField(isFormField);
        i.setName(fileName);
        return i;
    }
}
