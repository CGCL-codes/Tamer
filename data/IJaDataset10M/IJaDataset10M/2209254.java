package uk.org.ogsadai.activity.xmldb;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ErrorCodes;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockMaker;
import uk.org.ogsadai.activity.io.BlockMakerException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.ListIterator;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.activity.io.TypedOptionalListActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.xmldb.XMLDBCollectionProvider;
import uk.org.ogsadai.resource.dataresource.xmldb.XMLDBCollectionUseException;
import uk.org.ogsadai.resource.dataresource.xmldb.XMLDBRegistrationException;
import uk.org.ogsadai.resource.dataresource.xmldb.XMLDBUnknownSubCollectionException;

/**
 * An activity that performs an XPath query against an XML database and returns
 * the results of the query execution.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 * <li> <code>expression</code>. Type: {@link java.lang.String}. The XPath
 * expression to be executed.This is a mandatory input. The query should be in
 * valid XPath syntax. 
 * </li>
 * <li> <code>collection</code>. Type: {@link java.lang.String}. It
 * specifies a sub-collection of the XMLDB data resource under which the query
 * will be executed. It is an optional input which defaults to "". If it doesnt
 * exist then an <code>ActivityUserException</code> will be raised. 
 * </li>
 * <li> <code>resourceId</code>. Type: {@link java.lang.String}. It
 * specifies a resource ID  of the XMLDB data resource at which the query
 * will be targetted. It is an optional input which defaults to "". If it 
 * is not present then the expression is applied to all
 * resources in the collection.
 * </li>
 * <li> <code>namespace</code>. Type: OGSA-DAI list of {@link java.lang.String}. It
 * sets namespace mappings in the internal namespace map used to evaluate queries. 
 * It is an optional input. which defaults to <code>null</code>. Each namespace 
 * should be of the form <code>NAMESPACE-PREFIX:NAMESPACE-URN</code>.
 * </li>
 * </ul>
 * <p>
 * Activity outputs:
 * </p>
 * <ul>
 * <li> <code>data</code>. Type: OGSA-DAI list of <code>char[]</code>. The 
 * result from the query execution. It represents a 
 * <code>org.xmldb.api.base.ResourceSet</code> object in XML.In an XML 
 * representation of a ResourceSet, each resource is in its own
 * <code>resource</code> tag.
 * Therefore the result will look like:
 * <pre>
 * &lt;resourceSet&gt;
 *   &lt;resource&gt;
 *     ...
 *   &lt;/resource&gt;
 *   &lt;resource&gt;
 *     ...
 *   &lt;/resource&gt;
 *   ...
 * &lt;/resourceSet&gt;
 * </pre>
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
 * <li>{@link uk.org.ogsadai.resource.dataresource.xmldb.XMLDBCollectionProvider}.
 * </li>
 * </ul>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li> This activity executes an XPath query against an XML database, optionally
 * under a subcollection or against a specific resource. Namespaces can be provided as well for
 * the query evaluation.</li>
 * <li> If the subcollection provided doesn't exist then an
 * <code>ActivityUserException</code> will be raised. </li>
 * <li> If the no resource id is provided, then the query will target against all resources of
 * the corresponding collection. </li>
 * <li>Before the query is executed, a connection to the XMLDB is established which is released
 * after the query execution and the retrieval of the results.
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class XPathQueryActivity extends MatchedIterativeActivity implements ResourceActivity {

    /** Copyright notice. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2008.";

    /** Logger.*/
    private static final DAILogger LOG = DAILogger.getLogger(XPathQueryActivity.class);

    /**
     * Activity input name (<code>collection</code>) - name of
     * parent collection.
     * ({@link java.lang.String}).
     */
    public static final String INPUT_COLLECTION = "collection";

    /**
     * Activity input name (<code>resourceId</code>) - resource ID.
     * ({@link java.lang.String}).
     */
    public static final String INPUT_RESOURCE_ID = "resourceId";

    /**
     * Activity input name (<code>namespace</code>) - namespaces for
     * query evaluation
     * (OGSA-DAI list of {@link java.lang.String}).
     */
    public static final String INPUT_NAMESPACE = "namespace";

    /**
     * Activity input name (<code>expression</code>) - XPath expression.
     * ({@link java.lang.String}).
     */
    public static final String INPUT_EXPRESSION = "expression";

    /**
     * Activity output name (<code>data</code>) - The
     * results of the query execution(an OGSA-DAI list of <code>byte[]</code>).
     */
    public static final String OUTPUT_DATA = "data";

    /** The XML:DB connection provider. */
    private XMLDBCollectionProvider mResource;

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs() {
        return new ActivityInput[] { new TypedOptionalActivityInput(INPUT_COLLECTION, String.class, ""), new TypedOptionalActivityInput(INPUT_RESOURCE_ID, String.class, ""), new TypedOptionalListActivityInput(INPUT_NAMESPACE, String.class), new TypedActivityInput(INPUT_EXPRESSION, String.class) };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        validateOutput(OUTPUT_DATA);
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData) throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        String subCollection = (String) iterationData[0];
        String resourceId = (String) iterationData[1];
        ListIterator namespaces = (ListIterator) iterationData[2];
        String expression = (String) iterationData[3];
        Collection collection = null;
        try {
            collection = getCollection(subCollection);
            ResourceSet results = null;
            try {
                XPathQueryService mService = (XPathQueryService) collection.getService("XPathQueryService", "1.0");
                mService.setProperty("indent", "yes");
                if (namespaces != null) {
                    Object block;
                    while ((block = namespaces.nextValue()) != null) {
                        String namespace = (String) block;
                        int splitter = namespace.indexOf(":");
                        if ((namespace.length() < 3) || (splitter < 1) || (splitter == (namespace.length() - 1))) {
                            throw new ActivityUserException(ErrorID.XMLDB_NAMESPACE_FORMAT_ERROR, new Object[] { namespace });
                        }
                        String prefix = namespace.substring(0, splitter);
                        String uri = namespace.substring(splitter + 1);
                        mService.setNamespace(prefix, uri);
                    }
                }
                if (resourceId.length() == 0) {
                    results = mService.query(expression);
                } else {
                    results = mService.queryResource(resourceId, expression);
                }
            } catch (XMLDBException e) {
                raiseXMLDBException(e, resourceId);
            }
            try {
                BlockMaker xmlBlocks = new XMLResourceSetBlockMaker(results);
                BlockWriter outputBlocks = getOutput();
                outputBlocks.write(ControlBlock.LIST_BEGIN);
                while (xmlBlocks.hasNext()) {
                    outputBlocks.write(xmlBlocks.next());
                }
                outputBlocks.write(ControlBlock.LIST_END);
            } catch (BlockMakerException e) {
                throw new ActivityProcessingException(ErrorID.BLOCK_MAKER_EXCEPTION, e);
            } catch (PipeClosedException e) {
                iterativeStageComplete();
            } catch (PipeIOException e) {
                throw new ActivityPipeProcessingException(e);
            } catch (PipeTerminatedException e) {
                throw new ActivityTerminatedException();
            }
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

    /**
     * Try to get a collection from the XMLDB database.
     * 
     * @param subCollection
     *            the name of the subcollection
     * @return the collection corresponding to the subcollection name
     * @throws ActivityUserException
     *             if the error is passed back to the client
     * @throws ActivityProcessingException
     *             if there was an internal problem
     */
    public Collection getCollection(String subCollection) throws ActivityUserException, ActivityProcessingException {
        try {
            return mResource.getXMLDBCollection(subCollection);
        } catch (XMLDBUnknownSubCollectionException e) {
            throw new ActivityUserException(ErrorID.XMLDB_UNKNOWN_SUBCOLLECTION_ERROR, new String[] { subCollection }, e);
        } catch (XMLDBCollectionUseException e) {
            throw new ActivityProcessingException(ErrorID.XMLDB_COLLECTION_USE_ERROR, e);
        } catch (XMLDBRegistrationException e) {
            throw new ActivityProcessingException(ErrorID.XMLDB_REGISTRATION_ERROR, e);
        }
    }

    /**
     * Raises an ActivityUserException or ActivityProcessingException depending
     * on the error code of the XMLDBException.
     * 
     * @param exception
     *            the XMLDB exception
     * @param resourceId
     *            Id of the target XML resource or <code>null</code>
     * @throws ActivityUserException
     *             if the error is passed back to the client
     * @throws ActivityProcessingException
     *             if there was an internal problem
     */
    public void raiseXMLDBException(XMLDBException exception, String resourceId) throws ActivityUserException, ActivityProcessingException {
        if ((exception.errorCode == ErrorCodes.NO_SUCH_RESOURCE) || (exception.errorCode == ErrorCodes.INVALID_RESOURCE)) {
            throw new ActivityUserException(ErrorID.XMLDB_UNKNOWN_RESOURCE_ERROR, new String[] { resourceId }, exception);
        } else if (exception.errorCode == ErrorCodes.VENDOR_ERROR) {
            throw new ActivityUserException(ErrorID.XMLDB_EXCEPTION, new String[] { exception.getMessage() }, exception);
        } else {
            throw new ActivityProcessingException(ErrorID.XMLDB_EXCEPTION, exception);
        }
    }
}
