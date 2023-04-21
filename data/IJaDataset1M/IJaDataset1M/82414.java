package org.esfinge.aom.model.rolemapper.metadata.reader.provider;

import org.esfinge.aom.model.rolemapper.metadata.reader.EntityAnnotationReader;

public class EntityReaderProvider extends ElementReaderProvider {

    private static EntityReaderProvider instance;

    public static EntityReaderProvider getInstance() {
        if (instance == null) instance = new EntityReaderProvider();
        return instance;
    }

    private EntityReaderProvider() {
        metadataReader = new EntityAnnotationReader();
    }
}
