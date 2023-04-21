package org.opentides.editor;

import java.beans.PropertyEditorSupport;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

/**
 * PropertyEditor for handling SystemCodes.
 * Used for mapping drop-down objects to forms values.
 * @author allantan
 */
public class MultipartFileUploadEditor extends PropertyEditorSupport {

    @Override
    public String getAsText() {
        if (getValue() != null) {
            CommonsMultipartFile multipart = (CommonsMultipartFile) getValue();
            return multipart.getOriginalFilename();
        }
        return "";
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        setValue(null);
    }
}
