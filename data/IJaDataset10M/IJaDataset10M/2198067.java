package unbbayes.io;

import java.io.IOException;

/**
 * Interface for a log manager (responsible for generate the log of a operation). 
 * 
 * @author Laecio Lima dos Santos (laecio@gmail.com)
 */
public interface ILogManager {

    /**
	 * Clear the log information. 
	 */
    public void clear();

    /**
     * Add the title of the log 
     */
    public void addTitle(String text);

    /**
     * Append the text 
     */
    public void append(String text);

    /**
     * Append the text only if debug = true
     */
    public void appendIfTrue(boolean debug, String text);

    /**
     * Append the text and make new line. 
     */
    public void appendln(String text);

    public void appendln(int identation, String text);

    /**
     * Append the text and make new line if debug = true. 
     */
    public void appendlnIfTrue(boolean debug, String text);

    public void appendlnIfTrue(int identation, boolean debug, String text);

    /**
     * Append separator line. 
     */
    public void appendSeparator();

    /**
     * Append a section title 
     */
    public void appendSectionTitle(String text);

    /**
     * Append a section title 
     */
    public void appendSpecialTitle(String text);

    /**
     * Get the log how a String. 
     */
    public String getLog();

    /**
     * Write the log in a file. 
     */
    public void writeToDisk(String fileName, boolean append) throws IOException;
}
