package relex.parser;

import relex.Sentence;

/**
 * Interface to the parsing stage within RelEx. Implementations are 
 * responsible for both parsing a piece of text and converting it 
 * to RelEx's <code>FeatureNode</code> based representation. 
 *
 * @author Borislav Iordanov
 *
 */
public interface IParser {

    /**
	 * Parse a piece of text, usually a single sentence delimited by 
	 * proper punctuation, convert it to RelEx representation and 
	 * return the result.
	 * 
	 * Once this method returns, the parser is available again. 
	 * 
	 * @param text The text/sentence to parse.
	 * @return The parse result as a <code>Sentence</code> instance. 
	 *         This method should never return <code>null</code>. 
	 * @throws ParseException Whenever something went wrong during 
	 *         the parsing, such as failure to acquire a resource
	 *         or some such. No exception should be thrown if the 
	 *         text is unparseable.
	 */
    Sentence parse(String text) throws ParseException;
}
