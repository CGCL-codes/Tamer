package mdrawer.control.action.table;

import mdrawer.control.action.ActionBase;
import mdrawer.exception.MDrawerException;
import mdrawer.model.dao.FactoryDao;
import mdrawer.model.dao.TableDao;
import mdrawer.model.Record;
import mdrawer.model.Column;
import mdrawer.model.ColumnValue;
import mdrawer.model.Schema;
import mdrawer.model.Table;
import mdrawer.util.Messages;
import mdrawer.util.Log;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

/**
 * Questa classe implementa l'operazione di modifica di un record.
 * L'operazione viene eseguita in 3 passi:
 * 1. inserimento dei nuovi volori per le colonne.
 * 2. richiesta di conferma
 * 3. esecuzione update o annullamento operazione.
 * @author Enrico Maschietto &lt;pub77@libero.it&gt;
 * @version 1.0.0 :: 2007-05-31
 */
public class Edit extends ActionBase {

    /**
	 * Vista per il passo 0.
	 */
    private static final String STEP0_DISPATCH_PATH = "/WEB-INF/jsp/table/edit_0.jsp";

    /**
	 * Vista per il passo 1.
	 */
    private static final String STEP1_DISPATCH_PATH = "/WEB-INF/jsp/table/edit_1.jsp";

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
    private static final String CLASS_NAME = Edit.class.getName();

    /**
	 * Log4j logger.
	 */
    private static final Logger log = Logger.getLogger(Edit.class);

    public void execute(HttpServletRequest request, HttpServletResponse response, ServletContext context, Map parameters) throws MDrawerException {
        try {
            Log.enterAction(CLASS_NAME, log);
            HttpSession session = request.getSession(false);
            if (session == null) {
                throw new MDrawerException("session error");
            }
            Table table = (Table) session.getAttribute("table");
            if (table == null) {
                throw new MDrawerException("session error: table is null");
            }
            String table_name = getParameter(parameters, PARAM_TABLE_NAME);
            int id = getParameterInt(parameters, PARAM_ID);
            int step = getParameterInt(parameters, PARAM_STEP);
            switch(step) {
                case 0:
                    executeStep0(table, id, request, parameters);
                    break;
                case 1:
                    executeStep1(table, id, request, parameters);
                    break;
                case 2:
                    executeStep2(table, id, request, parameters);
                    break;
                default:
                    String message = Messages.get("action.invalid-step", new Object[] { "edit", new Integer(step) });
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
	 * Esegue il passo 0: passa alla vista di inserimento valori colonne.
	 */
    private void executeStep0(Table table, int id, HttpServletRequest request, Map parameters) {
        Log.exitStep(0, log);
        Schema schema = table.getSchema();
        Record record = table.selectRecordById(id);
        Map record_map = new HashMap();
        try {
            for (int i = 0; i < schema.getColumnsNumber(); i++) {
                record_map.put(schema.getColumn(i).getName(), record.getColumn(i).getValue());
            }
        } catch (MDrawerException e) {
        }
        Log.settingAttributes(log);
        request.setAttribute("record", record);
        request.setAttribute("record_map", record_map);
        Log.settingAttributesDone(log);
        dispatchToSubview(STEP0_DISPATCH_PATH);
        Log.exitStep(0, log);
    }

    /**
	 * Esegue il passo 1: richiesta di conferma.
	 */
    private void executeStep1(Table table, int id, HttpServletRequest request, Map parameters) throws MDrawerException {
        Log.exitStep(0, log);
        Record record = new Record();
        Iterator i = table.getSchema().getColumns().iterator();
        while (i.hasNext()) {
            Column column = (Column) i.next();
            String value = request.getParameter("column_" + column.getName());
            ColumnValue column_value = new ColumnValue(value);
            record.addColumn(column_value);
        }
        HttpSession session = request.getSession(false);
        if (session == null) {
            throw new MDrawerException("session error");
        }
        Log.settingAttributes(log);
        session.setAttribute("record", record);
        request.setAttribute("id", id);
        Log.settingAttributesDone(log);
        dispatchToSubview(STEP1_DISPATCH_PATH);
        Log.exitStep(0, log);
    }

    /**
	 * Esegue il passo 2: in caso di conferma al passo precedente esegue 
	 * l'update, altrimenti annulla l'operazione.
	 * In entrambi i casi passa alla vista di elenco dei record.
	 */
    private void executeStep2(Table table, int id, HttpServletRequest request, Map parameters) throws MDrawerException {
        Log.enterStep(1, log);
        HttpSession session = request.getSession(false);
        String table_name = table.getSchema().getName();
        if (session == null) {
            throw new MDrawerException("session error");
        }
        Record record = (Record) session.getAttribute("record");
        record.setId(id);
        dispatchToController("/table/show/" + table_name);
        String ask_result = getParameter(parameters, PARAM_ASK_RESULT);
        log.debug(Messages.get("action.confirm", new Object[] { "edit record", ask_result }));
        if (!ask_result.equals("yes")) {
            log.info(Messages.get("table.abort-edit", new Object[] { new Integer(id), table_name }));
            setMessages(request, new String[] { Messages.get("table.edit-record.cancel") });
        } else {
            log.info(Messages.get("table.confirm-edit", new Object[] { new Integer(id), table_name }));
            TableDao dao = (TableDao) FactoryDao.get("table");
            try {
                dao.updateRecord(record, table.getSchema());
                setMessages(request, new String[] { Messages.get("table.edit-record.ok") });
            } catch (MDrawerException e) {
                setErrors(request, new String[] { Messages.get("table.edit-record.ko") });
            }
        }
    }
}
