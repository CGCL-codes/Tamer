package de.banh.bibo.gwtTagLib.js.client;

import org.puremvc.java.multicore.patterns.facade.Facade;
import de.banh.bibo.gwtTagLib.js.client.controller.StartupCommand;

/**
 * Singleton.
 * @author Thomas
 *
 */
public class ApplicationFacade extends Facade {

    private static ApplicationFacade instance = new ApplicationFacade();

    private ApplicationFacade() {
        super("ApplicationFacade");
    }

    public static ApplicationFacade getInstance() {
        return instance;
    }

    /**
	 * body: SuggestionQueryResultVO
	 */
    public static final String RECIEVED_SUGGESTIONS = "RECIEVED_SUGGESTIONS";

    /**
	 * body: ValidationResultVO
	 */
    public static final String RECIEVED_VALIDATION_RESULT = "RECIEVED_VALIDATION_RESULT";

    /**
	 * body: SubmitResultVO
	 */
    public static final String SUBMIT_RESULT_RECIEVED = "SUBMIT_RESULT_RECIEVED";

    /**
	 * body: DialogShowCmdVO
	 */
    public static final String SHOW_DIALOG = "SHOW_DIALOG";

    /**
	 * body: Tupel<Object,SubmitProxyResultVO> - sender,result 
	 */
    public static final String DIALOG_CLOSED_SUCESSFULLY = "DIALOG_CLOSED_SUCESSFULLY";

    /**
	 * body: none
	 */
    public static final String STARTUP = "STARTUP_COMMAND";

    /**
	 * body: RequestSuggestionsCmdVO
	 */
    public static final String RETRIEVE_SUGGESTIONS = "RETRIEVE_SUGGESTIONS_COMMAND";

    /**
	 * body: ValidateCmdVO
	 */
    public static final String VALIDATE = "VALIDATE_COMMAND";

    /**
	 * body: GWTSuggestBoxMediator - der Mediator, dessen Wert jetzt validiert wurde
	 */
    public static final String VALIDATION_COMPLETE = "VALIDATION_COMPLETE";

    /**
	 * body: SubmitProxyCmdVO
	 */
    public static final String SUBMIT_DIALOG_RESULTS = "SUBMIT_DIALOG_RESULTS";

    /**
	 * body: ListAddDlgResultVO
	 */
    public static final String LIST_ADD_DLG_CLOSED = "LIST_ADD_DLG_CLOSED";

    public void start() {
        registerCommand(STARTUP, new StartupCommand());
        sendNotification(STARTUP);
    }
}
