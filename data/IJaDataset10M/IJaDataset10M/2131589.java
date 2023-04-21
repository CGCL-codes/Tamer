package taskgraph.tasks.text;

import taskgraph.Config;
import taskgraph.channels.CharChannel;
import taskgraph.ports.CharInputPort;
import taskgraph.ports.CharOutputPort;
import taskgraph.tasks.CompositeTask;

/**
 * Find distinct lines across the whole text input. This is in contrast
 * to task {@code UniqueLines}, which works locally to find distinct or unique
 * lines by comparing one line to the next.
 *  
 * @author Armando Blancas
 * @see taskgraph.tasks.text.SortLines
 * @see taskgraph.tasks.text.UniqueLines
 */
public class GlobalUniqueLines extends CompositeTask {

    private static final int DEFAULT_CAPACITY = Config.get().channelCapacity();

    private CharInputPort input;

    private CharOutputPort output;

    private int bufferSize = DEFAULT_CAPACITY;

    /**
	 * This constructor allows creating instances as beans.
	 */
    public GlobalUniqueLines() {
    }

    /**
     * Creates a fully configured instance.
     * 
     * @param input The char input port.
     * @param output The char output port.
     */
    public GlobalUniqueLines(final CharInputPort input, final CharOutputPort output) {
        setInput(input);
        setOutput(output);
    }

    /**
	 * Gets the buffer size for reading chunks of characters.
	 * 
	 * @return the bufferSize
	 */
    public int getBufferSize() {
        return bufferSize;
    }

    /**
     * Gets the input port.
     * 
	 * @return The char input port.
	 */
    public CharInputPort getInput() {
        return input;
    }

    /**
     * Gets the output port.
     * 
	 * @return The char output port.
	 */
    public CharOutputPort getOutput() {
        return output;
    }

    /**
	 * Sets the buffer size for reading chunks of characters.
	 * <p>The argument should not be less than the default. If it is, the
	 * default size is used instead.
	 * 
	 * @param bufferSize the bufferSize to set
	 */
    public void setBufferSize(final int bufferSize) {
        this.bufferSize = (bufferSize < DEFAULT_CAPACITY) ? DEFAULT_CAPACITY : bufferSize;
    }

    /**
	 * Sets the input port.
	 * 
	 * @param input The char input port.
	 */
    public void setInput(final CharInputPort input) {
        if (input == null) throw new IllegalArgumentException("input == null");
        this.input = input;
    }

    /**
	 * Sets the output port.
	 * 
	 * @param output The char output port.
	 */
    public void setOutput(final CharOutputPort output) {
        if (output == null) throw new IllegalArgumentException("output == null");
        this.output = output;
    }

    /**
     * Creates a graph that finds distinct lines across the whole input
     * by first sorting all of the the incoming data.
     * <pre>
     * --input--> [SortLines] --ch1--> [UniqueLines] --output-->
     * </pre>
	 */
    @Override
    protected void setup() {
        CharChannel ch = new CharChannel(bufferSize);
        add(new SortLines(input, ch.getOutputPort()));
        add(new UniqueLines(ch.getInputPort(), output));
    }
}
