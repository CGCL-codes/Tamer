package org.exist.xquery.functions.xmldb;

import org.exist.xquery.*;
import org.exist.xquery.update.Modification;
import org.exist.xquery.value.SequenceType;
import org.exist.xquery.value.Type;
import org.exist.xquery.value.Sequence;
import org.exist.xquery.value.IntegerValue;
import org.exist.dom.QName;
import org.exist.dom.NodeSet;
import org.exist.dom.DocumentSet;
import org.exist.EXistException;

public class Defragment extends BasicFunction {

    public static final FunctionSignature signatures[] = { new FunctionSignature(new QName("defragment", XMLDBModule.NAMESPACE_URI, XMLDBModule.PREFIX), "Start a defragmentation run on each document for which a node is passed in the first argument. " + "Fragmentation may occur if nodes are inserted into a document using XQuery update " + "extensions. " + "The second argument specifies the minimum number of fragmented pages which should " + "be in a document before it is considered for defragmentation. " + "Please note that defragmenting a document changes its internal structure, so any " + "references to this document will become invalid, in particular, variables pointing to " + "some nodes in the doc.", new SequenceType[] { new SequenceType(Type.NODE, Cardinality.ONE_OR_MORE), new SequenceType(Type.INTEGER, Cardinality.EXACTLY_ONE) }, new SequenceType(Type.ITEM, Cardinality.EMPTY)), new FunctionSignature(new QName("defragment", XMLDBModule.NAMESPACE_URI, XMLDBModule.PREFIX), "Start a defragmentation run on each document for which a node is passed in the first argument. " + "Fragmentation may occur if nodes are inserted into a document using XQuery update " + "extensions. " + "Please note that defragmenting a document changes its internal structure, so any " + "references to this document will become invalid, in particular, variables pointing to " + "some nodes in the doc.", new SequenceType[] { new SequenceType(Type.NODE, Cardinality.ONE_OR_MORE) }, new SequenceType(Type.ITEM, Cardinality.EMPTY)) };

    public Defragment(XQueryContext context, FunctionSignature signature) {
        super(context, signature);
    }

    public Sequence eval(Sequence[] args, Sequence contextSequence) throws XPathException {
        int splitCount = ((IntegerValue) args[1].itemAt(0)).getInt();
        NodeSet nodes = args[0].toNodeSet();
        DocumentSet docs = nodes.getDocumentSet();
        try {
            Modification.checkFragmentation(context, docs, splitCount);
        } catch (EXistException e) {
            throw new XPathException("An error occurred while defragmenting documents: " + e.getMessage(), e);
        }
        return Sequence.EMPTY_SEQUENCE;
    }
}
