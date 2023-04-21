package mdrawer.control.action.table;

import mdrawer.control.action.ActionBase;
import mdrawer.exception.MDrawerException;
import mdrawer.model.dao.FactoryDao;
import mdrawer.model.dao.TableDao;
import mdrawer.util.Messages;
import mdrawer.util.Log;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Questa classe implementa l'operazione di cancellazione di una record.
 * L'operazione viene effettuata in due passi.
 * La prima consiste nel chiedere all'utente se vuole confermare o meno la 
 * cancellazione.
 * La seconda, a seconda di risposta negativa o affermativa, abortisce la 
 * cancellazione o la esegue.
 * @author Enrico Maschietto &lt;pub77@libero.it&gt;
 * @version 1.0.0 :: 2007-05-31
 */
public class Delete extends ActionBase {

    /**
	 * Vista per il passo 0.
	 */
    private static final String STEP0_DISPATCH_PATH = "/WEB-INF/jsp/table/delete_0.jsp";

    /**
	 * Indice nella mappa dei parametri del nome della tabella.
	 */
    private static final String PARAM_TABLE_NAME = "2";

    /**
	 * Indice nella mappa dei parametri dell'id del record.
	 */
    private static final String PARAM_ID = "3";

    /**
	 * Indice nella mappa dei parametri del numero di passo.
	 */
    private static final String PARAM_STEP = "4";

    /**
	 * Indice nella mappa dei parametri del risposta di conferma.
	 */
    private static final String PARAM_ASK_RESULT = "5";

    /**
	 * Nome della class. Usato per scopi di debug e di log.
	 */
    private static final String CLASS_NAME = Delete.class.getName();

    /**
	 * Log4j logger.
	 */
    private static final Logger log = Logger.getLogger(Delete.class);

    public void execute(HttpServletRequest request, HttpServletResponse response, ServletContext context, Map parameters) throws MDrawerException {
        try {
            Log.enterAction(CLASS_NAME, log);
            String table_name = getParameter(parameters, PARAM_TABLE_NAME);
            int id = getParameterInt(parameters, PARAM_ID);
            int step = getParameterInt(parameters, PARAM_STEP);
            switch(step) {
                case 0:
                    executeStep0(table_name, id, request, parameters);
                    break;
                case 1:
                    executeStep1(table_name, id, request, parameters);
                    break;
                default:
                    String message = Messages.get("action.invalid-step", new Object[] { "delete", new Integer(step) });
                    log.error(message);
                    throw new MDrawerException(message);
            }
            Log.exitAction(CLASS_NAME, log);
        } catch (Exception e) {
            String message = Messages.get("misc.fails", new Object[] { "action", e.getMessage() });
            log.error(message);
            throw new MDrawerException(message);
        }
    }

    /**
	 * Esegue il passo 0: passa alla vista di richiesta di conferma.
	 */
    private void executeStep0(String table_name, int id, HttpServletRequest request, Map parameters) {
        Log.exitStep(0, log);
        Log.settingAttributes(log);
        request.setAttribute("table_name", table_name);
        request.setAttribute("id", id);
        Log.settingAttributesDone(log);
        dispatchToSubview(STEP0_DISPATCH_PATH);
        Log.exitStep(0, log);
    }

    private void executeStep1(String table_name, int id, HttpServletRequest request, Map parameters) throws MDrawerException {
        Log.enterStep(1, log);
        dispatchToController("/table/show/" + table_name);
        String ask_result = getParameter(parameters, PARAM_ASK_RESULT);
        log.debug(Messages.get("action.confirm", new Object[] { "delete record", ask_result }));
        if (!ask_result.equals("yes")) {
            log.info(Messages.get("table.abort-delete", new Object[] { new Integer(id), table_name }));
            setMessages(request, new String[] { Messages.get("table.delete-record.cancel") });
        } else {
            log.info(Messages.get("table.confirm-delete", new Object[] { new Integer(id), table_name }));
            TableDao dao = (TableDao) FactoryDao.get("table");
            try {
                dao.deleteRecord(table_name, id);
                setMessages(request, new String[] { Messages.get("table.delete-record.ok") });
            } catch (MDrawerException e) {
                setErrors(request, new String[] { Messages.get("table.delete-record.ko") });
            }
        }
    }
}
