package org.salamy.salsa;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.cas.NonEmptyFSList;
import org.apache.uima.jcas.cas.TOP;
import org.salamy.types.Fe;
import org.salamy.types.Frame;
import org.salamy.types.FrameChild;
import org.salamy.types.GraphNode;
import org.salamy.types.NT;
import org.salamy.types.Sentence;
import org.salamy.types.T;
import org.salamy.types.Target;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * A SAX parser implementing the {@link Iterator} interface. Its {@link #next()} method returns a
 * {@link Sentence} from the corpus. The SAX work is done in a separate thread which must be started
 * with {@link #run()};
 * 
 * @author Peter Palaga
 * 
 */
public class SalsaParser implements Runnable, Iterator<Sentence> {

    /**
   * Thrown (and caught) only internally when the processing is over.
   * 
   * @author Peter Palaga
   */
    private static class ProcessingFinishedException extends RuntimeException {

        private static final long serialVersionUID = 1282982597240695609L;

        /**
     * A constructor.
     */
        public ProcessingFinishedException() {
            super();
        }
    }

    /**
   * An internal SAX handler.
   * 
   * @author Peter Palaga
   * 
   */
    private class SalsaHandler extends DefaultHandler {

        private Fe fe;

        private Frame frame;

        private boolean frameValid;

        private NT nt;

        /**
     * The <code>id</code> of the root of the current sentence.
     */
        private String rootId;

        /**
     * The sentence we are currently working on.
     */
        private Sentence s;

        /**
     * Current target element.
     */
        private Target target;

        /**
     * A constructor.
     */
        public SalsaHandler() {
            super();
        }

        @Override
        public void endElement(String uri, String localName, String name) throws SAXException {
            if (SalsaElements.s.equals(name)) {
                putSentence(s);
                s = null;
            } else if (SalsaElements.graph.equals(name)) {
                rootId = null;
            } else if (SalsaElements.nt.equals(name)) {
                nt = null;
            } else if (SalsaElements.frame.equals(name)) {
                if (frameValid) {
                    s.addFrame(frame);
                }
                frame = null;
            } else if (SalsaElements.fe.equals(name)) {
                if (frameValid) {
                    frame.addFe(fe);
                }
                fe = null;
            } else if (SalsaElements.target.equals(name)) {
                if (frameValid) {
                    frame.setTarget(target);
                }
                target = null;
            }
        }

        /**
     * Removes bad frames from the Sentence. Some frames in the corpus are referencing nodes from
     * other sentences or non-existing nodes. We do not want such frames.
     */
        private void invalidateFrame() {
            System.out.println("invalidating frame " + frame.getId());
            if (frameValid) {
                frameValid = false;
                if (frame != null) {
                    unlinkFes(frame.getFes());
                    frame.setFes(null);
                    if (frame.getTarget() != null) {
                        unlinkNodes(frame.getTarget().getNodes());
                        frame.setTarget(null);
                    }
                }
            }
        }

        @Override
        public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
            if (SalsaElements.annotation.equals(name)) {
            } else if (SalsaElements.body.equals(name)) {
            } else if (SalsaElements.corpus.equals(name)) {
            } else if (SalsaElements.edge.equals(name)) {
                s.addEdge(nt, attributes.getValue(SalsaAttributes.idref), attributes.getValue(SalsaAttributes.label));
            } else if (SalsaElements.edgelabel.equals(name)) {
            } else if (SalsaElements.fe.equals(name)) {
                if (frameValid) {
                    fe = frame.createFe(attributes.getValue(SalsaAttributes.id), attributes.getValue(SalsaAttributes.name));
                }
            } else if (SalsaElements.feature.equals(name)) {
            } else if (SalsaElements.fenode.equals(name)) {
                if (frameValid) {
                    GraphNode n = s.lookupNode(attributes.getValue(SalsaAttributes.idref));
                    if (n == null) {
                        invalidateFrame();
                        return;
                    }
                    if (target != null) {
                        target.addNode(n);
                    } else if (fe != null) {
                        fe.addNode(n);
                    } else {
                        throw new IllegalStateException("Fe and Target both null: no parent for fenode with idref '" + attributes.getValue(SalsaAttributes.idref) + "'");
                    }
                }
            } else if (SalsaElements.flag.equals(name)) {
            } else if (SalsaElements.format.equals(name)) {
            } else if (SalsaElements.frame.equals(name)) {
                frameValid = true;
                frame = s.createFrame(attributes.getValue(SalsaAttributes.id), attributes.getValue(SalsaAttributes.name));
            } else if (SalsaElements.frames.equals(name)) {
            } else if (SalsaElements.global.equals(name)) {
            } else if (SalsaElements.globals.equals(name)) {
            } else if (SalsaElements.graph.equals(name)) {
                rootId = attributes.getValue(SalsaAttributes.root);
            } else if (SalsaElements.head.equals(name)) {
            } else if (SalsaElements.meta.equals(name)) {
            } else if (SalsaElements.nonterminals.equals(name)) {
            } else if (SalsaElements.nt.equals(name)) {
                String id = attributes.getValue(SalsaAttributes.id);
                nt = s.createNT(id);
                nt.setCat(attributes.getValue(SalsaAttributes.cat));
                s.addNt(nt);
                if (rootId.equals(id)) {
                    s.setRoot(nt);
                }
            } else if (SalsaElements.part.equals(name)) {
            } else if (SalsaElements.s.equals(name)) {
                String id = attributes.getValue(SalsaAttributes.id);
                if (filter == null || filter.isInInvocationInterval(id)) {
                    startNewSentence();
                    s = new Sentence(getCas());
                    s.setId(id);
                } else {
                    System.out.println("finishing by sentence " + id);
                    throw new ProcessingFinishedException();
                }
            } else if (SalsaElements.secedge.equals(name)) {
            } else if (SalsaElements.secedgelabel.equals(name)) {
            } else if (SalsaElements.sem.equals(name)) {
            } else if (SalsaElements.splitword.equals(name)) {
            } else if (SalsaElements.splitwords.equals(name)) {
            } else if (SalsaElements.t.equals(name)) {
                T t = s.createT(attributes.getValue(SalsaAttributes.id));
                t.setLemma(attributes.getValue(SalsaAttributes.lemma));
                t.setMorph(attributes.getValue(SalsaAttributes.morph));
                t.setPos(attributes.getValue(SalsaAttributes.pos));
                t.setWord(attributes.getValue(SalsaAttributes.word));
                s.addT(t);
            } else if (SalsaElements.target.equals(name)) {
                if (frameValid) {
                    target = frame.createTarget(attributes.getValue(SalsaAttributes.lemma));
                }
            } else if (SalsaElements.terminals.equals(name)) {
            } else if (SalsaElements.usp.equals(name)) {
            } else if (SalsaElements.uspblock.equals(name)) {
            } else if (SalsaElements.uspfes.equals(name)) {
            } else if (SalsaElements.uspframes.equals(name)) {
            } else if (SalsaElements.uspitem.equals(name)) {
            } else if (SalsaElements.value.equals(name)) {
            } else if (SalsaElements.wordtags.equals(name)) {
            } else {
                throw new IllegalStateException("Unexpected element " + name);
            }
        }

        /**
     * Called from {@link #invalidateFrame()}.
     * 
     * @param list
     */
        private void unlinkFes(FSList list) {
            if (list instanceof NonEmptyFSList) {
                NonEmptyFSList neList = (NonEmptyFSList) list;
                TOP o = neList.getHead();
                if (o instanceof FrameChild) {
                    FrameChild fe = (FrameChild) neList.getHead();
                    unlinkNodes(fe.getNodes());
                } else {
                    String type = o == null ? "null" : o.getClass().getName();
                    System.out.println("Unexpected type '" + type + "'. Expected '" + FrameChild.class.getName() + "'");
                }
                unlinkFes(neList.getTail());
            }
        }

        /**
     * Called from {@link #invalidateFrame()}.
     * 
     * @param list
     */
        private FSList unlinkFrameChildren(FSList list) {
            if (list instanceof NonEmptyFSList) {
                NonEmptyFSList neList = (NonEmptyFSList) list;
                FrameChild ch = (FrameChild) neList.getHead();
                if (ch.getFrame() == frame) {
                    return unlinkFrameChildren(neList.getTail());
                } else {
                    neList.setTail(unlinkFrameChildren(neList.getTail()));
                    return neList;
                }
            }
            return list;
        }

        /**
     * Called from {@link #invalidateFrame()}.
     * 
     * @param list
     */
        private void unlinkNodes(FSList list) {
            if (list instanceof NonEmptyFSList) {
                NonEmptyFSList neList = (NonEmptyFSList) list;
                GraphNode gn = (GraphNode) neList.getHead();
                gn.setFrameChildren(unlinkFrameChildren(gn.getFrameChildren()));
                unlinkFes(neList.getTail());
            }
        }
    }

    /**
   * {@link JCas} instance used for creating new elements e.g. {@link Sentence}, {@link NT}, etc.
   */
    private JCas cas;

    /**
   * The character encoding of the corpus file.
   */
    private String encoding;

    /**
   * The filter for distinguishing between training and evaluation part of the corpus.
   */
    private EvaluationSetFilter filter;

    /**
   * Status variable.
   */
    private boolean finished = false;

    /**
   * The input.
   */
    private BufferedInputStream in;

    /**
   * Status variable.
   */
    private boolean newSentenceStarted = false;

    /**
   * The SAX parser.
   */
    private SAXParser saxParser;

    /**
   * The sentence we will return on the next {@link #next()} invocation.
   */
    private Sentence sentence;

    /**
   * A constructor.
   * 
   * @param in
   *          the {@link InputStream} to read from.
   * @param encoding
   *          the character encoding of the stream.
   */
    public SalsaParser(InputStream in, String encoding) {
        super();
        if (in instanceof BufferedInputStream) {
            this.in = (BufferedInputStream) in;
        } else {
            this.in = new BufferedInputStream(in);
        }
        this.encoding = encoding;
        (new Thread(this)).start();
    }

    /**
   * Close the {@link #in}
   */
    private void close() {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    /**
   * Signals that the processing is over.
   */
    private synchronized void finish() {
        cas = null;
        finished = true;
        notifyAll();
    }

    /**
   * Waits until the {@link #cas} is available and then returns it.
   * 
   * @return the new {@link #cas}
   */
    private synchronized JCas getCas() {
        while (this.cas == null) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        notifyAll();
        return cas;
    }

    /**
   * Gets the filter.
   * 
   * @return the filter
   */
    public EvaluationSetFilter getFilter() {
        return filter;
    }

    /**
   * Waits until the internal SAX worker produces a new sentence or exits and then returns
   * <code>true</code> or <code>false</code>.
   * 
   * @return <code>true</code> if there is a new {@link Sentence} to return; <code>false</code>
   *         otherwise.
   * 
   * @see java.util.Iterator#hasNext()
   */
    @Override
    public synchronized boolean hasNext() {
        while (!newSentenceStarted && !finished) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        notifyAll();
        return newSentenceStarted;
    }

    /**
   * returns the next {@link Sentence}.
   * 
   * @return next {@link Sentence}.
   * @see java.util.Iterator#next()
   */
    public synchronized Sentence next() {
        while (this.sentence == null && !finished) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        Sentence s = this.sentence;
        this.sentence = null;
        this.newSentenceStarted = false;
        notifyAll();
        return s;
    }

    /**
   * Creates the parser.
   * 
   * @throws ParserConfigurationException
   * @throws SAXException
   */
    private void open() throws ParserConfigurationException, SAXException {
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParser = saxParserFactory.newSAXParser();
    }

    /**
   * Starts the processing.
   * 
   * @throws SAXException
   * @throws IOException
   */
    private void process() throws SAXException, IOException {
        saxParser.parse(new InputSource(new InputStreamReader(in, encoding)), new SalsaHandler());
    }

    /**
   * Called form the SAX worker thread. Blocked until the previous sentence has been consumed by
   * {@link #next()}.
   * 
   * @param sentence
   */
    private synchronized void putSentence(Sentence sentence) {
        while (this.sentence != null) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        this.sentence = sentence;
        cas = null;
        notifyAll();
    }

    /**
   * Throw a {@link UnsupportedOperationException} in this implementation.
   * 
   * @see java.util.Iterator#remove()
   */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

    /**
   * Starts the internal SAX worker thread.
   * 
   * @see java.lang.Runnable#run()
   */
    @Override
    public void run() {
        try {
            open();
            process();
        } catch (ProcessingFinishedException e) {
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            close();
        }
        finish();
    }

    /**
   * Sets the {@link JCas} to use for creating new elements e.g. {@link Sentence}, {@link NT},
   * etc.
   * 
   * @param cas
   */
    public synchronized void setCas(JCas cas) {
        this.cas = cas;
        notifyAll();
    }

    /**
   * Sets the filter.
   * 
   * @param filter
   */
    public void setFilter(EvaluationSetFilter filter) {
        this.filter = filter;
    }

    /**
   * Called internally from the SAX worked thread.
   */
    private synchronized void startNewSentence() {
        while (this.newSentenceStarted) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        this.newSentenceStarted = true;
        notifyAll();
    }
}
