package gov.nasa.worldwind.retrieve;

/**
 * @author Tom Gaskins
 * @version $Id: RetrievalPostProcessor.java 1 2011-07-16 23:22:47Z dcollins $
 */
public interface RetrievalPostProcessor {

    public java.nio.ByteBuffer run(Retriever retriever);
}
