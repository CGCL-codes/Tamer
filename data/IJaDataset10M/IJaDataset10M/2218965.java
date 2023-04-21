package abc.parser;

import abc.notation.TuneBook;

/**
 * Interface that should be implemented by any object that listens to tune book
 * parsing.
 * 
 * It listen too for tune parser event such as
 * {@link abc.parser.TuneParserListenerInterface#tuneBegin()}, noTune(),
 * tuneEnd().
 * 
 * @see abc.parser.TuneBookParserAdapter for a simple implementation
 * skeleton.
 */
public interface TuneBookParserListenerInterface extends TuneParserListenerInterface {

    /** Invoked when the tune book contains nothing: no header, no tune */
    public void emptyTuneBook();

    /** Invoked when the parsing of the tune book begins. */
    public void tuneBookBegin();

    /**
	 * Invoked when the parsing of a tune book has ended.
	 * 
	 * @param tunebook
	 *            The tune book that has just been parsed.
	 * @param abcRoot
	 *            The parsing tree which could be browsed to find errors, node
	 *            types.
	 */
    public void tuneBookEnd(TuneBook tuneBook, AbcNode abcRoot);
}
