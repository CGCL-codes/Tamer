package org.cycads.loaders.gff3;

/**
 * The interface for things that listen to GFF event streams.
 * <p>
 * This allows the GFF push model to run over large collections of GFF, filter them and access other resources without
 * requiring vast numbers of GFF records to be in memory at any one time.
 * <p>
 * The stream includes both GFF records and comment lines. A particular handeler may choose to discard either of these.
 * <p>
 * It is assumed that a particular handler will only be used to listen to a single stream of events in a single thread.
 * Particular implementations may not impose this restriction.
 * 
 * @author Matthew Pocock
 * @author Thomas Down
 * @author Keith James (docs)
 */
public interface GFF3DocumentHandler {

    /**
	 * Indicates that a new GFF document has been started. This gives you a hook to set up per-document resources.
	 * 
	 * @param locator A URI for the stream being parsed.
	 */
    void startDocument(String locator);

    /**
	 * Indicates that the current GFF document has now ended.
	 * <p>
	 * This gives you the chance to flush results, or do calculations if you wish.
	 */
    void endDocument();

    /**
	 * A comment line has been encountered.
	 * <p>
	 * <span class="arg">comment</span> has already had the leading '<code>#</code>' removed, and may have had
	 * leading-and-trailing whitespace trimmed.
	 * 
	 * @param comment the comment <span class="type">String</span>
	 */
    void commentLine(String comment);

    /**
	 * A record line has been encountered.
	 * <p>
	 * It is already preseneted to you into a <span class="type">GFFRecord</span> object.
	 * 
	 * @param record the <span class="type">GFFRecord</span> containing all the info
	 * @throws InvalidSequence
	 */
    void recordLine(GFF3Record record) throws InvalidSequence;
}
