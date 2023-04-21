package uk.org.ogsadai.activity.xmldb;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.xmldb.XMLDBCollectionProvider;
import uk.org.ogsadai.resource.dataresource.xmldb.XMLDBCollectionUseException;
import uk.org.ogsadai.resource.dataresource.xmldb.XMLDBRegistrationException;
import uk.org.ogsadai.resource.dataresource.xmldb.XMLDBUnknownSubCollectionException;
import uk.org.ogsadai.tuple.ColumnNotFoundException;

/**
 * Lists available collections (of XML files) stored in the XML database.
 *  
 * <p> 
 * Activity inputs:
 * </p>
 * <ul>
 * <li>
 * <code>parentCollection</code>. Type: {@link java.lang.String}. Optional
 * input which specifies a collection of the XMLDB data resource
 * to list all its child collections. If not provided the root collection is
 * assumed.
 * </li>
 * </ul>
 * <p> 
 * Activity outputs:
 * </p>
 * <ul>
 * <li>
 * <code>data</code>. Type: OGSA-DAI list of {@link java.lang.String}. The names of the child collections
 * of the provided parent collection.
 * </li>
 * </ul>
 * <p>
 * Configuration parameters: none.
 * </p>
 * <p>
 * Activity input/output ordering: none.
 * </p>
 * <p>
 * Activity contracts: none.
 * </p>
 * <p>
 * Target data resource:
 * </p>
 * <ul>
 * <li>
 * Expected to implement 
 * {@link uk.org.ogsadai.resource.dataresource.xmldb.XMLDBCollectionProvider}.
 * </li>
 * </ul>
 * <p>
 * Behaviour: 
 * </p>
 * <ul>
 * <li>
 * This activity lists available collections (of XML files) stored in the XML database.
 * If a parent collection is provided then all its child collections will be written to the
 * output. If not provided then the root collection is assumed as the parent collection.
 * </li>
 * <li>
 * If the collection provided doesn't exist then then an
 * <code>ActivityUserException</code> will be raised.
 * </li>
 * </ul>
 *
 * @author The OGSA-DAI Project Team.
 */
public class XMLListCollectionsActivity extends MatchedIterativeActivity implements ResourceActivity {

    /** Copyright. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007.";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(XMLListCollectionsActivity.class);

    /**
     * Activity input name <code>parentCollection</code> - optional name of
     * collection to get its child collections - root collection assumed otherwise.
     * ({@link java.lang.String}).
     */
    public static final String INPUT_PARENT_COLLECTION = "parentCollection";

    /** 
     * Activity output name <code>data</code> - the child collections, 
     * if any (OGSA-DAI list of {@link java.lang.String}). 
     */
    public static final String OUTPUT_DATA = "data";

    /** The output writer. */
    public BlockWriter mOutput;

    /** The XML:DB connection provider. */
    private XMLDBCollectionProvider mResource;

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs() {
        return new ActivityInput[] { new TypedOptionalActivityInput(INPUT_PARENT_COLLECTION, String.class, "") };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        validateOutput(OUTPUT_DATA);
        mOutput = getOutput();
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData) throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        String subCollection = (String) iterationData[0];
        Collection collection = null;
        try {
            collection = mResource.getXMLDBCollection(subCollection);
            mOutput.write(ControlBlock.LIST_BEGIN);
            for (int i = 0; i < collection.listChildCollections().length; i++) {
                mOutput.write(collection.listChildCollections()[i]);
            }
            mOutput.write(ControlBlock.LIST_END);
        } catch (XMLDBUnknownSubCollectionException e) {
            throw new ActivityUserException(ErrorID.XMLDB_UNKNOWN_SUBCOLLECTION_ERROR, new String[] { subCollection }, e);
        } catch (XMLDBCollectionUseException e) {
            throw new ActivityProcessingException(ErrorID.XMLDB_COLLECTION_USE_ERROR, e);
        } catch (ColumnNotFoundException e) {
            throw new ActivityUserException(ErrorID.INVALID_TUPLE_TYPES_EXCEPTION, e);
        } catch (XMLDBRegistrationException e) {
            throw new ActivityProcessingException(ErrorID.XMLDB_REGISTRATION_ERROR, e);
        } catch (PipeClosedException e) {
            iterativeStageComplete();
        } catch (PipeIOException e) {
            throw new ActivityPipeProcessingException(e);
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        } catch (XMLDBException e) {
            throw new ActivityProcessingException(ErrorID.XMLDB_EXCEPTION, e);
        } finally {
            try {
                if (collection != null) {
                    mResource.releaseXMLDBCollection(collection);
                }
            } catch (XMLDBCollectionUseException e) {
                throw new ActivityProcessingException(ErrorID.XMLDB_COLLECTION_USE_ERROR, e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
    }

    /**
     * {@inheritDoc}
     */
    public Class getTargetResourceAccessorClass() {
        return XMLDBCollectionProvider.class;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetResourceAccessor(ResourceAccessor resourceAccessor) {
        mResource = (XMLDBCollectionProvider) resourceAccessor;
    }
}
