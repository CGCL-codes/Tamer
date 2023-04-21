package edu.asu.vogon.files.extension;

import edu.asu.vogon.digitalHPS.IText;
import edu.asu.vogon.util.domain.SaveHelper;

public abstract class ATextFileFactory implements TextFileFactory {

    public ATextFileFactory() {
        super();
    }

    public void saveText(IText text) {
        SaveHelper.save(text);
    }

    public String getId() {
        return SaveHelper.createNewId();
    }
}
