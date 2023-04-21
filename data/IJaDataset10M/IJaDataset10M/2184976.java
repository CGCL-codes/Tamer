package uk.org.ogsadai.activity.indexedfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import uk.org.ogsadai.activity.ActivityProcessingException;
import uk.org.ogsadai.activity.ActivityTerminatedException;
import uk.org.ogsadai.activity.ActivityUserException;
import uk.org.ogsadai.activity.MatchedIterativeActivity;
import uk.org.ogsadai.activity.extension.ResourceActivity;
import uk.org.ogsadai.activity.io.ActivityIOException;
import uk.org.ogsadai.activity.io.ActivityInput;
import uk.org.ogsadai.activity.io.ActivityPipeProcessingException;
import uk.org.ogsadai.activity.io.BlockWriter;
import uk.org.ogsadai.activity.io.ControlBlock;
import uk.org.ogsadai.activity.io.PipeClosedException;
import uk.org.ogsadai.activity.io.PipeIOException;
import uk.org.ogsadai.activity.io.PipeTerminatedException;
import uk.org.ogsadai.activity.io.TypedActivityInput;
import uk.org.ogsadai.activity.io.TypedOptionalActivityInput;
import uk.org.ogsadai.common.msgs.DAILogger;
import uk.org.ogsadai.metadata.MetadataWrapper;
import uk.org.ogsadai.resource.ResourceAccessor;
import uk.org.ogsadai.resource.dataresource.file.FileAccessProvider;
import uk.org.ogsadai.tuple.SimpleTuple;
import uk.org.ogsadai.tuple.Tuple;
import uk.org.ogsadai.tuple.TupleMetadata;

/**
 * An activity that performs a query over an index using the Jakarta Lucene full-text search 
 * engine.
 * <p>
 * Activity inputs:
 * </p>
 * <ul>
 *  <li><code>query</code>. Type: {@link java.lang.String}. The query expressed in Lucene query language.
 * </li>
 * <li><code>indexPath</code>. Type: {@link java.lang.String}. The path of the directory where the 
 * index files will be stored. This is an optional input which defaults to "/" and is expected to be an 
 * existing directory under the root directory of the file resource.</li>
 * <li> <code>analyzer</code>. Type: {@link org.apache.lucene.analysis.Analyzer}. The analyzer that will
 * be used for analysing the text. This is a mandatory input.</li>
 * <li> <code>docs</code>. Type: {@link java.lang.Integer}. The number of documents to be retrieved. It defaults
 * to 100.</li>
 * </ul>
 * <p>
 * Activity outputs:
 * <ul>
 * <li><code>data</code>. Type: OGSA-DAI list of 
 * {@link uk.org.ogsadai.tuple.Tuple} with the first item in the list an 
 * instance of {@link uk.org.ogsadai.metadata.MetadataWrapper} containing a 
 * {@link uk.org.ogsadai.tuple.TupleMetadata} object.  The tuples produced
 * by the query. These tuples are of the form (filename, offset, length).
 * </li>
 * </ul>
 * </p>
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
 * <li> {@link uk.org.ogsadai.resource.dataresource.file.FileAccessProvider}.
 * </li>
 * </ul>
 * <p>
 * Behaviour:
 * </p>
 * <ul>
 * <li> The activity performs a search over an index that is built by the Jakarta Lucene full-text search 
 * engine. It expects the directory under the file resource where the indices are lying as well as
 * a valid query in Lucene query language.
 * </li>
 * <li>If the indexPath provided doesn't exist, or is not a directory an <code>ActivityUserException</code> will be thrown.
 * </li>
 * <li>If the query is not of a valid syntax, then 
 * an <code>ActivityProcessingException</code> will be thrown.
 * </li>
 * <li>The output is an OGSA-DAI list of tuples each one containing the filename, the offset 
 * and the length where the query execution found matches. So such an OGSA-DAI list would look like:
 * <pre>
 * { metadata, ("uniprot.dat", 134, 343), ("uniprot.dat", 455, 788) }
 * </pre>
 * </li>
 * </ul>
 * 
 * @author The OGSA-DAI Project Team.
 */
public class LookupIndexFileActivity extends MatchedIterativeActivity implements ResourceActivity {

    /** Copyright statement. */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2010.";

    /** Logger. */
    private static final DAILogger LOG = DAILogger.getLogger(LookupIndexFileActivity.class);

    /** 
     * Activity input name <code>query</code> - the query.
     * ({@link java.lang.String}). 
     */
    public static final String QUERY = "query";

    /** 
     * Activity input name <code>indexPath</code> - the path of
     * the index directory.
     * ({@link java.lang.String}). Optional which defaults to
     * "/".
     */
    public static final String INDEX_PATH_INPUT = "indexPath";

    /** 
     * Activity input name <code>analyzer</code> - the analyzer
     * for text analysing.
     * ({@link java.lang.String}). Optional which defaults to
     * "StandardAnalyzer".
     */
    public static final String ANALYZER_INPUT = "analyzer";

    /** 
     * Activity input name <code>docs</code> - the number of
     * documents to be retrieved.
     * ({@link java.lang.Integer}). Optional which defaults to 100.
     */
    public static final String NO_OF_DOCS_INPUT = "docs";

    /** Activity output name <code>data</code> - OGSA-DAI list
     * containing matched files with offset and length. (an
     * OGSA-DAI list of tuples).
     */
    public static final String OUTPUT_RESULTS = "data";

    /** File access provider. */
    private FileAccessProvider mResource;

    /** The output block writer. */
    private BlockWriter mOutput;

    /**
     * {@inheritDoc}
     */
    public Class getTargetResourceAccessorClass() {
        return FileAccessProvider.class;
    }

    /**
     * {@inheritDoc}
     */
    public void setTargetResourceAccessor(ResourceAccessor targetResource) {
        mResource = (FileAccessProvider) targetResource;
    }

    /**
     * {@inheritDoc}
     */
    protected ActivityInput[] getIterationInputs() {
        return new ActivityInput[] { new TypedActivityInput(QUERY, String.class), new TypedOptionalActivityInput(INDEX_PATH_INPUT, String.class, "/"), new TypedActivityInput(ANALYZER_INPUT, Analyzer.class), new TypedOptionalActivityInput(NO_OF_DOCS_INPUT, Integer.class, new Integer(100)) };
    }

    /**
     * {@inheritDoc}
     */
    protected void preprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
        validateOutput(OUTPUT_RESULTS);
        mOutput = getOutput();
    }

    /**
     * {@inheritDoc}
     */
    protected void processIteration(Object[] iterationData) throws ActivityProcessingException, ActivityTerminatedException, ActivityUserException {
        final String queryExpr = (String) iterationData[0];
        final String indexPath = (String) iterationData[1];
        final Analyzer analyzer = (Analyzer) iterationData[2];
        final int docs = ((Integer) iterationData[3]).intValue();
        File indexDir = IndexedFilesUtilities.getIndexDirForLookup(mResource, indexPath);
        Query query = null;
        try {
            query = new QueryParser("contents", analyzer).parse(queryExpr);
        } catch (ParseException e) {
            throw new ActivityUserException(new IllegalIndexedFileQueryException(queryExpr, e));
        }
        TopDocs hits = null;
        IndexSearcher searcher = null;
        try {
            searcher = new IndexSearcher(indexDir.getCanonicalPath());
            hits = searcher.search(query, docs);
            mOutput.write(ControlBlock.LIST_BEGIN);
            TupleMetadata metadata = IndexedFilesUtilities.createMetadata();
            mOutput.write(new MetadataWrapper(metadata));
            List tupleData = new ArrayList();
            for (ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = searcher.doc(scoreDoc.doc);
                Field field = doc.getField("__datafile");
                String dataFilename = null;
                if (field != null) {
                    dataFilename = field.stringValue();
                }
                field = doc.getField("__offset");
                String offset = null;
                if (field != null) {
                    offset = field.stringValue();
                }
                field = doc.getField("__length");
                String length = null;
                if (field != null) {
                    length = field.stringValue();
                }
                Tuple tuple = null;
                if (dataFilename != null) {
                    tupleData.add(dataFilename);
                    tupleData.add(new Long(offset));
                    tupleData.add(new Long(length));
                    tuple = new SimpleTuple(tupleData);
                    mOutput.write(tuple);
                    tupleData.clear();
                }
            }
            mOutput.write(ControlBlock.LIST_END);
        } catch (PipeClosedException e) {
            iterativeStageComplete();
        } catch (PipeIOException e) {
            throw new ActivityPipeProcessingException(e);
        } catch (PipeTerminatedException e) {
            throw new ActivityTerminatedException();
        } catch (IOException e) {
            throw new ActivityIOException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected void postprocess() throws ActivityUserException, ActivityProcessingException, ActivityTerminatedException {
    }
}
