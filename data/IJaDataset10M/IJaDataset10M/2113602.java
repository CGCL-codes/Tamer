package maze.common.adv_nio.reader;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public interface ByteBufferReader {

    ByteBufferReader read();

    int getLength();

    int getStartOffset();

    int getReadyForReading();

    boolean isEnd();
}
