package edu.psu.its.lionshare.metadata;

import java.io.File;
import java.awt.Image;

public interface MetadataExtractor {

    public Object extractMetadata(Object file) throws MetadataExtractionException;

    public String getSchemaUri();
}
