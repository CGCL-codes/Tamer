package net.sourceforge.ondex.parser.aracyc2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import net.sourceforge.ondex.args.ArgumentDefinition;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.event.type.EventType;
import net.sourceforge.ondex.event.type.GeneralOutputEvent;
import net.sourceforge.ondex.parser.AbstractONDEXParser;
import net.sourceforge.ondex.parser.aracyc2.parser.MetaData;
import net.sourceforge.ondex.parser.aracyc2.parser.concret.AbstractParser;
import net.sourceforge.ondex.parser.aracyc2.parser.concret.CompoundParser;
import net.sourceforge.ondex.parser.aracyc2.parser.concret.EnzymeParser;
import net.sourceforge.ondex.parser.aracyc2.parser.concret.GeneParser;
import net.sourceforge.ondex.parser.aracyc2.parser.concret.IParser;
import net.sourceforge.ondex.parser.aracyc2.parser.concret.PathwayParser;
import net.sourceforge.ondex.parser.aracyc2.parser.concret.ProteinParser;
import net.sourceforge.ondex.parser.aracyc2.parser.concret.PublicationParser;
import net.sourceforge.ondex.parser.aracyc2.parser.concret.ReactionParser;
import net.sourceforge.ondex.parser.aracyc2.parser.transformer.AbstractTransformer;
import net.sourceforge.ondex.parser.aracyc2.parser.transformer.CompoundTransformer;
import net.sourceforge.ondex.parser.aracyc2.parser.transformer.ECNumberTransformer;
import net.sourceforge.ondex.parser.aracyc2.parser.transformer.EnzymeTransformer;
import net.sourceforge.ondex.parser.aracyc2.parser.transformer.GeneTransformer;
import net.sourceforge.ondex.parser.aracyc2.parser.transformer.PathwayTransformer;
import net.sourceforge.ondex.parser.aracyc2.parser.transformer.ProteinTransformer;
import net.sourceforge.ondex.parser.aracyc2.parser.transformer.PublicationTransformer;
import net.sourceforge.ondex.parser.aracyc2.parser.transformer.ReactionTransformer;
import net.sourceforge.ondex.parser.aracyc2.parser.transformer.factory.TransformerFactory;
import net.sourceforge.ondex.parser.aracyc2.parser.util.AbstractReader;
import net.sourceforge.ondex.parser.aracyc2.parser.util.ColFileReader;
import net.sourceforge.ondex.parser.aracyc2.parser.util.DatFileReader;
import net.sourceforge.ondex.parser.aracyc2.sink.AbstractNode;
import net.sourceforge.ondex.parser.aracyc2.sink.factory.SinkFactory;

/**
 * Parser for the aracyc flatfile database. The data flow is Parser ->
 * AbstractReader -> AbstractParser -> AbstractTransformer
 * 
 * @author peschr
 * 
 */
public class Parser extends AbstractONDEXParser implements MetaData {

    /**
	 * Its used to define a configuration. A configuration consists of a
	 * parser, a transformer, a filename and the name of the sink class.
	 * 
	 * @author peschr
	 * 
	 */
    class Configuration {

        private AbstractParser parser;

        private Class<? extends AbstractTransformer> transformer;

        private String fileName;

        private SinkName sinkName;

        public SinkName getSinkName() {
            return this.sinkName;
        }

        public String getFileName() {
            return fileName;
        }

        public AbstractParser getParser() {
            return parser;
        }

        public Class<? extends AbstractTransformer> getTransformer() {
            return transformer;
        }

        public Configuration(AbstractParser parser, Class<? extends AbstractTransformer> transformer, String fileName, SinkName sinkName) {
            this.parser = parser;
            this.transformer = transformer;
            this.fileName = fileName;
            this.sinkName = sinkName;
        }
    }

    /**
	 * enum which defines all possible sink names
	 * @author peschr
	 *
	 */
    enum SinkName {

        Pathway, Reaction, Compound, Enzyme, Protein, Gene, Publication, ECNumber
    }

    private ArrayList<Configuration> configuration;

    private SinkName[] pathwayOrder = { SinkName.Pathway, SinkName.Reaction, SinkName.Compound, SinkName.Enzyme, SinkName.Protein, SinkName.Gene, SinkName.Publication };

    private static Parser instance;

    /**
	 * initialize the parser
	 * @param s
	 */
    public Parser() {
        instance = this;
        configuration = new ArrayList<Configuration>();
        configuration.add(new Configuration(new PublicationParser(), PublicationTransformer.class, "pubs.dat", SinkName.Publication));
        configuration.add(new Configuration(new GeneParser(), GeneTransformer.class, "genes.dat", SinkName.Gene));
        configuration.add(new Configuration(new GeneParser(), GeneTransformer.class, "genes.col", SinkName.Gene));
        configuration.add(new Configuration(new ProteinParser(), ProteinTransformer.class, "proteins.dat", SinkName.Protein));
        configuration.add(new Configuration(new EnzymeParser(), EnzymeTransformer.class, "enzrxns.dat", SinkName.Enzyme));
        configuration.add(new Configuration(new CompoundParser(), CompoundTransformer.class, "compounds.dat", SinkName.Compound));
        configuration.add(new Configuration(new ReactionParser(), ReactionTransformer.class, "reactions.dat", SinkName.Reaction));
        configuration.add(new Configuration(new PathwayParser(), PathwayTransformer.class, "pathways.dat", SinkName.Pathway));
        configuration.add(new Configuration(null, ECNumberTransformer.class, null, SinkName.ECNumber));
    }

    public String getName() {
        return new String("Aracyc2");
    }

    public String getVersion() {
        return new String("22.10.2007");
    }

    /**
	 * sets the ONDEXGraph and starts the parser
	 */
    public void start() {
        try {
            ReadAllFiles();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Returns a reader for a specific file extension
	 * 
	 * @param fileName
	 * @param inputDir
	 * @param listener
	 * @return AbstractReader - an abstract reader
	 * @throws Exception
	 */
    private AbstractReader getReader(String fileName, String inputDir, IParser listener) throws Exception {
        String wholeFileName = inputDir + "/" + fileName;
        if (fileName.substring(fileName.length() - 3).equals("dat")) return new DatFileReader(wholeFileName, listener); else return new ColFileReader(wholeFileName, listener);
    }

    /**
	 * iterates over the fileNames array. In the first step it reads the files
	 * and stores the information in sink objects. In the second step it
	 * transforms the sink objects into concepts and relations.
	 * 
	 * @throws Exception
	 */
    private void ReadAllFiles() throws Exception {
        for (Configuration c : configuration) {
            if (c.getFileName() == null) continue;
            String fileName = c.getFileName();
            IParser listener = c.getParser();
            AbstractReader reader = getReader(fileName, pa.getInputDir(), listener);
            fireEventOccurred(new GeneralOutputEvent("parse " + fileName, getCurrentMethodName()));
            while (reader.hasNext()) {
                reader.next();
            }
        }
        fireEventOccurred(new GeneralOutputEvent("creating concepts", getCurrentMethodName()));
        Iterator<AbstractNode> iterator1 = SinkFactory.getInstance().iterator();
        while (iterator1.hasNext()) {
            AbstractNode node = iterator1.next();
            getTransformer(node.getClass().getSimpleName());
            AbstractTransformer transformer = TransformerFactory.getInstance(getTransformer(node.getClass().getSimpleName()), this);
            transformer.nodeToConcept(node);
            transformer.addCommonDetailsToConcept(node.getConcept(), node);
        }
        fireEventOccurred(new GeneralOutputEvent("creating relations and adding context information", getCurrentMethodName()));
        for (SinkName str : pathwayOrder) {
            Iterator<AbstractNode> iterator = SinkFactory.getInstance().typeIterator(str.name());
            while (iterator.hasNext()) {
                AbstractNode node = iterator.next();
                AbstractTransformer transformer = TransformerFactory.getInstance(getTransformer(node.getClass().getSimpleName()), this);
                transformer.pointerToRelationsCore(node);
            }
        }
    }

    /**
	 * returns a transformer for a sink object.
	 * 
	 * @param nodeName
	 * @return
	 * @throws NoSuchElementException
	 */
    private Class<? extends AbstractTransformer> getTransformer(String nodeName) throws NoSuchElementException {
        for (Configuration c : this.configuration) {
            if (c.getSinkName().toString().equals(nodeName)) return c.getTransformer();
        }
        throw new NoSuchElementException();
    }

    /**
	 * returns the ONDEXGraph
	 * @return
	 */
    public ONDEXGraph getGraph() {
        return graph;
    }

    /**
	 * no validators are used
	 */
    @Override
    public String[] requiresValidators() {
        return new String[0];
    }

    /**
	 * no arguments are used
	 */
    public ArgumentDefinition<?>[] getArgumentDefinitions() {
        return new ArgumentDefinition<?>[0];
    }

    public static void propagateEventOccurred(EventType et) {
        if (instance != null) instance.fireEventOccurred(et);
    }

    /**
	 * Convenience method for outputing the current method name in a dynamic way
	 * @return the calling method name
	 */
    public static String getCurrentMethodName() {
        Exception e = new Exception();
        StackTraceElement trace = e.fillInStackTrace().getStackTrace()[1];
        String name = trace.getMethodName();
        String className = trace.getClassName();
        int line = trace.getLineNumber();
        return "[CLASS:" + className + " - METHOD:" + name + " LINE:" + line + "]";
    }

    @Override
    public boolean readsDirectory() {
        return true;
    }

    @Override
    public boolean readsFile() {
        return false;
    }
}
