package examples.NodeMetacollections.SimpleFileConversion;

import net.sourceforge.esw.graph.INode;
import net.sourceforge.esw.graph.NodeFactory;
import net.sourceforge.esw.graph.context.INodeContextFactory;
import net.sourceforge.esw.graph.context.PrototypeDeepCloneNodeContextFactory;
import net.sourceforge.esw.collection.IMetaCollection;
import net.sourceforge.esw.collection.MetaFactory;
import net.sourceforge.esw.collection.FileTransducerAdapter;
import net.sourceforge.esw.collection.DelimitedTransducer;
import net.sourceforge.esw.collection.TransducerException;
import net.sourceforge.esw.collection.XMLTransducer;
import examples.NodeMetacollections.NodeTreeViewer.NodeTreeViewer;

/**
 * Demonstrates simple file conversion.
 * <p>
 *
 * Many developers are tasked with converting data from one kind of data
 * type to another. For example, if a vendor sends data as a comma delimited
 * file, but your process has been coded with XML in mind, you would have to
 * convert the comma delimited file to XML. The ESW framework makes this
 * task quite simple.
 * <p>
 *
 * To run this example, make sure the ESW jar files are in the classpath, and
 * the root examples directory is in your classpath, and that a Java 2 VM is in
 * your path, then execute:
 * <code>java examples.NodeMetacollections.SimpleFileConversion.SimpleFileConversion</code>.
 * <p>
 *
 * @see net.sourceforge.esw.collection.MetaCollection
 * @see net.sourceforge.esw.collection.MetaFactory
 * @see net.sourceforge.esw.collection.DelimitedTransducer
 * @see net.sourceforge.esw.collection.XMLTransducer
 *
 * @version 1.00, 12 Aug 2001
 */
public class SimpleFileConversion {

    /**
     * Constructs a new SimpleFileConversion instance. This constructor
     * performs the demonstration.
     */
    public SimpleFileConversion() {
        INodeContextFactory context = createNodeContextFactory();
        IMetaCollection meta = MetaFactory.createMetaCollection("CommaDelimitedData");
        DelimitedTransducer transducer = new DelimitedTransducer();
        transducer.addDelimiter(",");
        transducer.addDelimiter("\n");
        transducer.setContextFactory(context);
        FileTransducerAdapter adaptedTransducer = new FileTransducerAdapter("data.sdf", transducer);
        meta.setTransducer(adaptedTransducer);
        try {
            meta.get();
        } catch (TransducerException te) {
            te.printStackTrace();
        }
        NodeTreeViewer.showViewer(meta, true);
        XMLTransducer xmlTransducer = new XMLTransducer();
        FileTransducerAdapter adaptedTransducer2 = new FileTransducerAdapter("data.xml", xmlTransducer);
        meta.setTransducer(adaptedTransducer2);
        try {
            meta.put();
        } catch (TransducerException te) {
            te.printStackTrace();
        }
        System.exit(0);
    }

    /**
     * Creates an INodeContextFactory instance to give identifiers to the
     * delimited data.
     */
    protected INodeContextFactory createNodeContextFactory() {
        PrototypeDeepCloneNodeContextFactory context = new PrototypeDeepCloneNodeContextFactory();
        INode node = NodeFactory.createNode("Item");
        node.add(NodeFactory.createNode("Title"));
        node.add(NodeFactory.createNode("Authors"));
        node.add(NodeFactory.createNode("Cost"));
        node.add(NodeFactory.createNode("Source"));
        context.setPrototype(node);
        return context;
    }

    /**
     * Main entry point into this NodeCreation example. Simply calls
     * <code>new SimpleFileConversion</code> for this objects' constructor to
     * perform the demonstration.
     *
     * @param argsv the parameters passed into this SimpleFileConversion instance.
     */
    public static void main(String[] argsv) {
        new SimpleFileConversion();
    }
}
