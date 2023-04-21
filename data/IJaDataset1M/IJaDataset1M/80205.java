package net.sf.container;

import java.net.URL;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.IOException;

/**
 * Parse a policy file and propagate parsing events to the parse listener.
 *
 * <p>
 * This interface is primarily used to provide with concrete parsing
 * algorithms for policy files.<br/>
 *
 * created on Jun 13, 2005
 * @author fiykov
 * @version $Revision: 1.1 $
 * @since
 *
 * @see net.sf.container.imp.PolicyFileParser
 */
public interface PolicyParser {

    /**
	 * set events listener
	 * @param listener to use
	 */
    public void setListener(PolicyParseListener listener);

    /**
	 * @return the attached events listener
	 */
    public PolicyParseListener getListener();

    /**
	 * parse policy file as given stream and url location
	 *
	 * @param url of the policy file
	 * @param policyFile content to be parsed
	 * @throws PolicyParseException
	 */
    public void parse(URL url, Reader policyFile) throws PolicyParseException;

    /**
	 * parse policy file as given url file location
	 *
	 * @param policyFile to parse
	 * @throws PolicyParseException
	 * @throws FileNotFoundException
	 */
    public void parse(URL policyFile) throws PolicyParseException, FileNotFoundException, IOException;
}
