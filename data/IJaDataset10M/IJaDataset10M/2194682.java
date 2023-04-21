package org.photron.collection.storage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.util.Properties;
import org.photron.collection.PhotronImage;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * A DefaultStorageManager doesn't really do much beyond generating a usable
 * RDF model for a PhotronCollection.  It's mostly useful for testing.
 * 
 * @author jon
 */
public class DefaultStorageManager implements StorageManager {

    protected Model collectionModel;

    public DefaultStorageManager() {
        this.collectionModel = com.hp.hpl.jena.rdf.model.ModelFactory.createDefaultModel();
    }

    public void attachImageData(PhotronImage image, URI dataURI) {
    }

    public void removeImage(PhotronImage image) {
    }

    public BufferedImage getImageData(PhotronImage image) {
        return null;
    }

    public Model getCollectionModel() {
        return this.collectionModel;
    }

    public void setProperties(Properties properties) {
    }

    public void loadCollection() {
    }

    public void saveCollection(File destinationFile) {
    }

    public void setDefinitionFile(File definitionFile) {
    }
}
