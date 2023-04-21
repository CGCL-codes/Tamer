package net.sourceforge.quexec.connect;

import java.io.Reader;

/**
 * Component that reads from an object of class {@link Reader}.
 * 
 * The component will process the output of the given {@link Reader}.
 * 
 * @author schickin
 *
 */
public interface ConnectReader {

    /**
	 * Return the configured {@link Reader}.
	 */
    public Reader getReader();

    /**
	 * Set the configured {@link Reader}.
	 * 
	 * Must be called before the component starts processing and may not be
	 * changed afterwards.
	 */
    public void setReader(Reader reader);
}
