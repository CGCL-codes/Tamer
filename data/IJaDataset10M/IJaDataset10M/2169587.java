package ie.ucd.searchengine.index;

import java.util.Vector;

/**
 * Converts from the specified type to documents
 * @author Brendan Maguire
 * @param <G> Type to convert from
 */
public interface IConverter<G> {

    /**
	 * Converts from the specified type to documents
	 * @param arg Elements to convert
	 * @return Returns the generated documents
	 */
    public Vector<IDocument> convert(Vector<G> arg);
}
