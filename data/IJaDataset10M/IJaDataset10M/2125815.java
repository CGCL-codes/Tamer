package org.exist.xquery.functions.util;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.exist.dom.DocumentSet;
import org.exist.dom.NodeSet;
import org.exist.dom.QName;
import org.exist.indexing.IndexWorker;
import org.exist.indexing.OrderedValuesIndex;
import org.exist.storage.Indexable;
import org.exist.util.Occurrences;
import org.exist.util.ValueOccurrences;
import org.exist.xquery.BasicFunction;
import org.exist.xquery.Cardinality;
import org.exist.xquery.Dependency;
import org.exist.xquery.FunctionSignature;
import org.exist.xquery.Profiler;
import org.exist.xquery.XPathException;
import org.exist.xquery.XQueryContext;
import org.exist.xquery.value.FunctionParameterSequenceType;
import org.exist.xquery.value.FunctionReturnSequenceType;
import org.exist.xquery.value.IntegerValue;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;

/**
 * @author Pierrick Brihaye <pierrick.brihaye@free.fr>
 */
public class IndexKeyDocuments extends BasicFunction {

    protected static final Logger logger = Logger.getLogger(IndexKeyDocuments.class);

    protected static final FunctionParameterSequenceType nodeParam = new FunctionParameterSequenceType("nodes", Type.NODE, Cardinality.ZERO_OR_MORE, "The nodes whose content is indexed");

    protected static final FunctionParameterSequenceType valueParam = new FunctionParameterSequenceType("value", Type.ATOMIC, Cardinality.EXACTLY_ONE, "The indexed value to search for");

    protected static final FunctionParameterSequenceType indexParam = new FunctionParameterSequenceType("index", Type.STRING, Cardinality.EXACTLY_ONE, "The index in which the search is made");

    protected static final FunctionReturnSequenceType result = new FunctionReturnSequenceType(Type.INTEGER, Cardinality.ZERO_OR_ONE, "the number of documents for the indexed value");

    public static final FunctionSignature[] signatures = { new FunctionSignature(new QName("index-key-documents", UtilModule.NAMESPACE_URI, UtilModule.PREFIX), "Return the number of documents for an indexed value.", new SequenceType[] { nodeParam, valueParam }, result), new FunctionSignature(new QName("index-key-documents", UtilModule.NAMESPACE_URI, UtilModule.PREFIX), "Return the number of documents for an indexed value.", new SequenceType[] { nodeParam, valueParam, indexParam }, result) };

    public IndexKeyDocuments(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        if (context.getProfiler().isEnabled()) {
            context.getProfiler().start(this);
            context.getProfiler().message(this, Profiler.DEPENDENCIES, "DEPENDENCIES", Dependency.getDependenciesName(this.getDependencies()));
            if (contextSequence != null) context.getProfiler().message(this, Profiler.START_SEQUENCES, "CONTEXT SEQUENCE", contextSequence);
        }
        Sequence result;
        if (args[0].isEmpty()) result = Sequence.EMPTY_SEQUENCE; else {
            NodeSet nodes = args[0].toNodeSet();
            DocumentSet docs = nodes.getDocumentSet();
            if (this.getArgumentCount() == 3) {
                IndexWorker indexWorker = context.getBroker().getIndexController().getWorkerByIndexName(args[2].itemAt(0).getStringValue());
                if (indexWorker == null) throw new XPathException(this, "Unknown index: " + args[2].itemAt(0).getStringValue());
                Map<String, Object> hints = new HashMap<String, Object>();
                if (indexWorker instanceof OrderedValuesIndex) hints.put(OrderedValuesIndex.START_VALUE, args[1]); else logger.warn(indexWorker.getClass().getName() + " isn't an instance of org.exist.indexing.OrderedIndexWorker. Start value '" + args[1] + "' ignored.");
                Occurrences[] occur = indexWorker.scanIndex(context, docs, nodes, hints);
                if (occur.length == 0) result = Sequence.EMPTY_SEQUENCE; else result = new IntegerValue(occur[0].getDocuments());
            } else {
                ValueOccurrences occur[] = context.getBroker().getValueIndex().scanIndexKeys(docs, nodes, (Indexable) args[1]);
                if (occur.length == 0) result = Sequence.EMPTY_SEQUENCE; else result = new IntegerValue(occur[0].getDocuments());
            }
        }
        if (context.getProfiler().isEnabled()) context.getProfiler().end(this, "", result);
        return result;
    }
}
