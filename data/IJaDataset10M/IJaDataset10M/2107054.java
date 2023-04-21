package orinoco.layout;

import java.io.IOException;
import orinoco.Alignment;
import orinoco.write.FormatWriter;

/**
 * Atomic command implementation
 */
class WriteText implements Command {

    /**
	 * the output writer
	 */
    private FormatWriter writer;

    /**
	 * The text to write
	 */
    private String text;

    /**
	 * Constructor
	 * 
	 * @param t
	 *            the text
	 * @param fw
	 *            the output writer
	 */
    public WriteText(FormatWriter fw, String t) {
        writer = fw;
        text = t;
    }

    /**
	 * Executes the command
	 * 
	 * @exception IOException
	 */
    public void execute() throws IOException {
        writer.writeText(text);
    }
}
