package uk.co.ordnancesurvey.rabbitgui;

import java.io.Serializable;
import uk.co.ordnancesurvey.rabbitgui.document.RbtDocumentModel;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;

/**
 * This interface provides a GUI-safe way of invoking a parsing task for a
 * rabbit document. Implementations will perform parsing of of the document in
 * such a way that the GUI does not freeze.
 * 
 * @author rdenaux
 * 
 */
public interface RbtGUIParserInvoker extends Serializable {

    /**
	 * Interface required for receiving the results of parsing tasks performed
	 * by a {@link RbtGUIParserInvoker}.
	 * 
	 * @author rdenaux
	 * 
	 */
    public interface ParserInvokerClient {

        void receiveRequestedParsedResult(IRabbitParsedResult aParsedResult);
    }

    /**
	 * Request this {@link RbtGUIParserInvoker} to initiate a parsing task for
	 * aDocModel as soon as possible. This method will return immediately; when
	 * the task completes aDocModel will be notified using the
	 * {@link ParserInvokerClient}. If you decide you do not need a new parsing
	 * for aDocModel, you can use method
	 * {@link #cancelParsingTask(RbtDocumentModel)}.
	 * 
	 * @param aDocModel
	 *            should implement {@link ParserInvokerClient} in order to
	 *            receive the parsed result once the parsing task finishes.
	 */
    void requestParsingTask(RbtDocumentModel aDocModel);

    /**
	 * This method is similar to {@link #requestParsingTask(RbtDocumentModel)},
	 * but the parsing task is only started after a short delay (default is
	 * 300ms). Also, if you invoke this method before the previous delay has
	 * occured, the previous parsing task will not be executed. There is thus no
	 * guarantee that the task will be performed. Use
	 * {@link #requestParsingTask(RbtDocumentModel)} if the task must be
	 * performed.
	 * 
	 * This method is useful in components such as editors, where a user can be
	 * typing quickly; in such cases you want to delay the parsing task in case
	 * the user keeps typing to avoid invoking a parsing task after every
	 * keystroke. Also, if the parsing task takes too long and the user edits
	 * the input, you want to wait until the user has stopped typing for a short
	 * while to perform the parsing.
	 * 
	 * @param aDocModel
	 */
    void requestDelayedParsing(RbtDocumentModel aDocModel);

    /**
	 * Cancel any open requests this RbtGUIParserInvoker may have for aDocModel
	 * 
	 * @param aDocModel
	 */
    void cancelParsingTask(RbtDocumentModel aDocModel);
}
