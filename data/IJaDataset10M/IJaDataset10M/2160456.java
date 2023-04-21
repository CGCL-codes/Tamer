package org.photron.collection;

import org.photron.collection.storage.*;
import java.net.URI;
import java.util.Random;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * The <code>PhotronCollection</code> is where Photron handles the semantic
 * aspects of managing a collection of images.
 * 
 * @author jon
 */
public class PhotronCollection extends PhotronObject {

    protected StorageManager storageManager;

    protected Model collectionModel;

    protected String namespace = null;

    protected static final int SERIAL_IMAGE = 0;

    protected static final int SERIAL_IMAGE_GROUP = 1;

    protected static final int SERIAL_SUBJECT = 2;

    /**
	 * Creates a new <code>PhotronCollection</code> with no
	 * <code>StorageManager</code>.  If a
	 * <code>PhotronCollection</code> has no storage manager, it
	 * will resort to using a <code>MemoryStorageManager</code>,
	 * which (as the name implies) stores the collection in memory.
	 * 
	 * Note that a <code>StorageManager</code> <em>cannot</em> be
	 * assigned to this <code>PhotronCollection</code> later.  To
	 * dump the contents of this collection into a collection with
	 * a set <code>StorageManager</code> (i.e. to convert a locally
	 * stored collection to a collection stored in a remote
	 * database), it is necessary to:
	 * 
	 * <ol>
	 * 		<li>Create a new <code>PhotronCollection</code> with a
	 * 		set <code>StorageManager</code></li>
	 * 		<li>Copy the contents of this
	 * 		<code>PhotronCollection</code> to the new collection
	 * 		using the new collection's <code>importCollection</code>
	 * 		method</li>
	 * </ol>
	 * 
	 * This behavior is provided primarily for testing or for use
	 * by other applications.
	 */
    public PhotronCollection() {
        this(new DefaultStorageManager());
    }

    /**
	 * Creates a new <code>PhotronCollection</code> with the given
	 * <code>StorageManager</code>.  Note that the
	 * <code>StorageManager</code> cannot be changed later (see the
	 * no-argument constructor documentation for more information).
	 * 
	 * @param manager the <code>StorageManager</code> to be used by
	 * this <code>PhotronCollection</code>
	 */
    public PhotronCollection(StorageManager storageManager) {
        this.storageManager = storageManager;
        this.collectionModel = this.storageManager.getCollectionModel();
        if (this.collectionModel.isEmpty()) {
            String namespace = PhotronCollection.generateNamespace();
            this.setModelResource(this.collectionModel.createResource(namespace + "/collection"));
            this.setNamespace(namespace);
        } else {
            ResIterator rootIterator = this.collectionModel.listSubjectsWithProperty(RDF.type, CollectionVocabulary.PhotronCollection);
            if (rootIterator.hasNext()) {
                this.setModelResource(rootIterator.nextResource());
                if (rootIterator.hasNext()) {
                }
                if (this.getNamespace() == null) {
                    this.setNamespace(PhotronCollection.generateNamespace());
                }
            } else {
                String namespace = PhotronCollection.generateNamespace();
                this.setModelResource(this.collectionModel.createResource(namespace + "/collection"));
                this.setNamespace(namespace);
            }
        }
    }

    /**
	 * Returns a new <code>PhotronImage</code> associated with this
	 * <code>PhotronCollection</code>.  Note that the image won't actually be
	 * associated with the <code>PhotronCollection</code> until its
	 * <code>setParentGroup</code> method is called, nor will it have any
	 * properties set by default.
	 * 
	 * @return a new <code>PhotronImage</code> associated with this collection
	 */
    public PhotronImage getPhotronImage() {
        Resource imageResource = this.collectionModel.createResource(this.getNamespace() + "/collection/images/" + this.getNextSerial(SERIAL_IMAGE));
        return new PhotronImage(imageResource, this);
    }

    /**
	 * Returns a <code>PhotronImage</code> with the given URI.  If no such image
	 * exists, it is created.
	 * 
	 * If the image already existed, it will already have its properties set.
	 * Otherwise, it will behave like a <code>PhotronImage</code> created
	 * without a given URI.
	 * 
	 * @param uri the URI of the <code>PhotronImage</code> to be returned
	 * @return the <code>PhotronImage</code> corresponding to the given URI, or a new
	 * <code>PhotronImage</code> with the given URI if ones doesn't already exist
	 */
    public PhotronImage getPhotronImage(String uri) {
        return new PhotronImage(this.collectionModel.createResource(uri), this);
    }

    public PhotronImageGroup getPhotronImageGroup() {
        Resource groupResource = this.collectionModel.createResource(this.getNamespace() + "/collection/imageGroups/" + this.getNextSerial(SERIAL_IMAGE_GROUP));
        return new PhotronImageGroup(groupResource, this);
    }

    public PhotronImageGroup getPhotronImageGroup(String uri) {
        return new PhotronImageGroup(this.collectionModel.createResource(uri), this);
    }

    public PhotronSubject getPhotronSubject() {
        Resource subjectResource = this.collectionModel.createResource(this.getNamespace() + "/collection/subjects/" + this.getNextSerial(SERIAL_SUBJECT));
        return new PhotronSubject(subjectResource, this);
    }

    public PhotronSubject getPhotronSubject(String uri) {
        return new PhotronSubject(this.collectionModel.createResource(uri), this);
    }

    /**
	 * Imports the contents of the given <code>PhotronCollection</code> into
	 * this one.  This method may optionally overwrite the existing contents of
	 * this collection.
	 * 
	 * @param collection the collection to import
	 * @param overwrite specifies whether the contents of the existing
	 * collection should be overwritten.  If <code>true</code>, this
	 * collection's <code>empty</code> method is called before the new
	 * collection is imported.  If <code>false</code>, the contents of
	 * the new collection are added.
	 */
    public void importCollection(PhotronCollection collection, boolean overwrite) {
        if (overwrite) {
            this.empty();
        }
    }

    /**
	 * Completely empties a collection's backing model, and calls for the
	 * <code>StorageManager</code> associated with this collection to empty
	 * itself, as well.
	 */
    public void empty() {
    }

    private static String generateNamespace() {
        return "http://photron.sourceforge.net/collections/" + java.lang.Math.abs(new Random().nextLong());
    }

    protected void setNamespace(String namespace) {
        StmtIterator namespaceIterator = this.getModelResource().listProperties(CollectionVocabulary.collectionNamespace);
        if (namespaceIterator.hasNext()) {
            namespaceIterator.nextStatement().changeObject(namespace);
            if (namespaceIterator.hasNext()) {
            }
        } else {
            this.getModelResource().addProperty(CollectionVocabulary.collectionNamespace, namespace);
        }
    }

    /**
	 * Retrieve the namespace used for this collection.  The namespace can (and
	 * should) be prepended to a unique serial number to generate unique URIs
	 * for objects in the collection.
	 * @return the namespace used for this collection
	 */
    private String getNamespace() {
        StmtIterator namespaceIterator = this.getModelResource().listProperties(CollectionVocabulary.collectionNamespace);
        if (namespaceIterator.hasNext()) {
            return namespaceIterator.nextStatement().getString();
        } else {
            return null;
        }
    }

    /**
	 * In order to ensure that all objects added to a
	 * <code>PhotronCollection</code> have unique URIs, a
	 * <code>PhotronCollection</code> maintains a set of serial counters.  This
	 * method returns a unique <code>String</code> to be appended to the base
	 * URI of an object and automatically increments the counter.
	 * 
	 * @param serialType an integer representing which counter should be used.
	 * Valid <code>serialType</code>s are:
	 * 
	 * <ul>
	 * 		<li>SERIAL_IMAGE</li>
	 * 		<li>SERIAL_IMAGE_GROUP</li>
	 * 		<li>SERIAL_SUBJECT</li>
	 * </ul>
	 * 
	 * @return a <code>String</code> representing a unique identifier to be
	 * appended to a base URI.  Serial strings are ten-digit numbers (for
	 * example, "0000000001").
	 */
    private String getNextSerial(int serialType) {
        StmtIterator serialIterator = null;
        switch(serialType) {
            case SERIAL_IMAGE:
                serialIterator = this.getModelResource().listProperties(CollectionVocabulary.imageSerial);
                break;
            case SERIAL_IMAGE_GROUP:
                serialIterator = this.getModelResource().listProperties(CollectionVocabulary.imageGroupSerial);
                break;
            case SERIAL_SUBJECT:
                serialIterator = this.getModelResource().listProperties(CollectionVocabulary.subjectSerial);
                break;
        }
        if (serialIterator.hasNext()) {
            Statement serialStatement = serialIterator.nextStatement();
            int currentSerial = serialStatement.getInt();
            if (serialIterator.hasNext()) {
            }
            serialStatement.changeObject(this.collectionModel.createTypedLiteral(new Integer(currentSerial + 1)));
            String currentSerialString = Integer.toString(currentSerial);
            String paddingZeros = "";
            for (int x = 0; x < (10 - currentSerialString.length()); x++) {
                paddingZeros += "0";
            }
            return paddingZeros + currentSerialString;
        } else {
            switch(serialType) {
                case SERIAL_IMAGE:
                    this.getModelResource().addProperty(CollectionVocabulary.imageSerial, this.collectionModel.createTypedLiteral(new Integer(2)));
                    break;
                case SERIAL_IMAGE_GROUP:
                    this.getModelResource().addProperty(CollectionVocabulary.imageGroupSerial, this.collectionModel.createTypedLiteral(new Integer(2)));
                    break;
                case SERIAL_SUBJECT:
                    this.getModelResource().addProperty(CollectionVocabulary.subjectSerial, this.collectionModel.createTypedLiteral(new Integer(2)));
                    break;
            }
            return "0000000001";
        }
    }

    /**
	 * Notifies this collection's <code>StorageManager</code> that an image is
	 * attempting to associate image data with itself.  Note that this method
	 * should <strong>not</strong> be called directly (hence the
	 * <code>protected</code> access modifier), as it will automatically be
	 * called when the (<code>public</code>)
	 * <code>PhotronImage.attachImageData</code> method is called.
	 * @param image the <code>PhotronImage</code> to attach image data to
	 * @param url the URL from which the image data can be read
	 */
    protected void attachImageData(PhotronImage image, URI uri) {
        this.storageManager.attachImageData(image, uri);
    }

    /**
	 * Dumps the contents of the model to <code>System.out</code> for debugging
	 * purposes.
	 */
    public void dumpModel() {
        this.collectionModel.write(System.out, "RDF/XML-ABBREV");
    }
}
