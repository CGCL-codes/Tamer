package org.sf.codejen;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * <b>Null</b> implementation of {@link Template}. All implementation of methods are empty or return null.
 * @author Shane Ng
 *
 */
public class TemplateAdapter implements Template {

    public boolean apply(Object model) throws TemplateException {
        return true;
    }

    public String getName() {
        return "Template Adapter";
    }

    public void addPostProcessor(TemplateProcessor processor) {
    }

    public void undo() {
    }

    public boolean isUndoable() {
        return false;
    }

    public List<File> getGeneratedFileList() {
        return Collections.emptyList();
    }

    public void setModelExtractor(ModelExtractor extractor) {
    }

    public void setFileNameGenerator(FileNameGenerator generator) {
    }
}
