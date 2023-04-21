package edu.asu.vogon.files.extension;

import edu.asu.vogon.digitalHPS.IText;

public interface TextFileFactory {

    public IText produceText(String path);

    public IText[] produceTextsFromFile(String path);
}
