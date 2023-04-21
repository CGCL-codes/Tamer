package tei.cr.utils.stax;

import tei.cr.utils.stax.StAXNode;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventConsumer;
import java.util.List;

/**
 * <p>A <code>StAXBuffer</code> is an {@link XMLEventConsumer} able
 * to buffer a stream of {@link XMLEvent} and turn this buffer into
 * a walkable tree structure ({@link #getTreeRoot()}) or into a
 * {@link XMLEventReader} ({@link #getStAXReader()}).</p>
 * 
 * Once either {@link #getTreeRoot()} or {@link #getStAXReader()}
 * has been called, this buffer must not accept to buffer any more events.
 * This property may be tested using {@link #isConsumer()}.
 *
 * @author Sylvain Loiseau &lt;sloiseau@u-paris10.fr>
 * @version 0.1
 */
public interface StAXBuffer extends XMLEventConsumer {

    /**
     * Get an unmodifiable tree of events to be walked.
     * 
     * @return a <code>StAXNode</code>
     *
     * @throws WellFormednessException if the bufferised {@link
     * XMLEvent} may not be properly turn into a well formed tree.
     */
    public StAXNode getTreeRoot() throws WellFormednessException;

    /**
     * Get an unmodifiable list of events to be consumed.
     * 
     * @return a <code>XMLEventReader</code>
     */
    public XMLEventReader getStAXReader();

    /**
    * Get an unmodifiable list of the events contained by this buffer.
     * 
     * @return a list of XMLEvent.
     */
    public List<XMLEvent> getXMLEvents();

    /**
     * Test if this buffer is ready for accepting more <code>XMLEvent</code>s.
     *
     * @return <code>true</code> if this buffer may receive new <code>XMLEvent</code>s,
     * <code>false</code> otherwise.
     */
    public boolean isConsumer();
}
