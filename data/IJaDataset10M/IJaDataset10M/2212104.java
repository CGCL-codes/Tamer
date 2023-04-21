package com.ouroboroswiki.core;

import java.io.IOException;
import java.io.InputStream;

public interface WritableContentRepository extends ContentRepository {

    void writeToContent(Object principal, String uniqueName, InputStream ins) throws IOException, ContentException, ContentValidationException;
}
