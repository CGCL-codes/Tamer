package net.conquiris.lucene.document;

import java.io.Reader;
import org.apache.lucene.analysis.TokenStream;

/**
 * Base interface for field adders.
 * @author Andres Rodriguez
 */
public interface TextFieldAdder<B extends BaseDocumentBuilder<B>> {

    /**
	 * Adds the field to the document field with the current information and the provided value.
	 * @param value Field value.
	 * @return The document builder.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 * @throws IllegalStateException if the document was already built.
	 */
    B add(String value);

    /**
	 * Adds the field to the document indexed, tokenized but not stored field with the current term
	 * vector information..
	 * @param reader Field value reader.
	 * @return The document builder.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 * @throws IllegalStateException if the document was already built.
	 */
    B add(Reader reader);

    /**
	 * Adds the field to the document indexed, tokenized but not stored field with the current term
	 * vector information..
	 * @param tokenStream Field value token stream.
	 * @return The document builder.
	 * @throws IllegalStateException if the field is neither stored nor indexed.
	 * @throws IllegalStateException if the document was already built.
	 */
    B add(TokenStream tokenStream);
}
