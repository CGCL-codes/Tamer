package org.danann.cernunnos.runtime.web;

import javax.servlet.http.HttpServletResponse;
import org.danann.cernunnos.AttributePhrase;
import org.danann.cernunnos.EntityConfig;
import org.danann.cernunnos.Formula;
import org.danann.cernunnos.LiteralPhrase;
import org.danann.cernunnos.Phrase;
import org.danann.cernunnos.Reagent;
import org.danann.cernunnos.ReagentType;
import org.danann.cernunnos.SimpleFormula;
import org.danann.cernunnos.SimpleReagent;
import org.danann.cernunnos.Task;
import org.danann.cernunnos.TaskRequest;
import org.danann.cernunnos.TaskResponse;

/**
 * Implements a Task that sends an error through an 
 * {@link javax.servlet.http.HttpServletResponse HttpServletResponse} from 
 * Cernunnos XML.
 * 
 * @author <A href="mailto:argherna@gmail.com">Andy Gherna</A>
 */
public class ServletSendErrorTask implements Task {

    private Phrase response;

    private Phrase statusCode;

    private Phrase message;

    public static final Reagent STATUS_CODE = new SimpleReagent("STATUS_CODE", "@status-code", ReagentType.PHRASE, Integer.class, "The HTTP status code.  This should be an error code for this Task (although it is not enforced)");

    public static final Reagent MESSAGE = new SimpleReagent("MESSAGE", "text()", ReagentType.PHRASE, String.class, "Error message corresponding to the status code", new LiteralPhrase(""));

    public static final Reagent SOURCE = new SimpleReagent("SOURCE", "@source", ReagentType.PHRASE, HttpServletResponse.class, "Optional source response to set the status code of.  If not specified, the value of " + "the response attribute 'WebAttributes.RESPONSE' will be used.", new AttributePhrase(WebAttributes.RESPONSE));

    /**
	 * {@inheritDoc }
	 */
    public Formula getFormula() {
        Reagent[] reagents = new Reagent[] { STATUS_CODE, MESSAGE, SOURCE };
        final Formula rslt = new SimpleFormula(ServletSendErrorTask.class, reagents);
        return rslt;
    }

    /**
	 * {@inheritDoc }
	 */
    public void init(EntityConfig config) {
        this.response = (Phrase) config.getValue(SOURCE);
        this.statusCode = (Phrase) config.getValue(STATUS_CODE);
        this.message = (Phrase) config.getValue(MESSAGE);
    }

    /**
	 * {@inheritDoc }
	 */
    public void perform(TaskRequest req, TaskResponse res) {
        HttpServletResponse resp = (HttpServletResponse) response.evaluate(req, res);
        Integer sc = (Integer) statusCode.evaluate(req, res);
        String msg = (String) message.evaluate(req, res);
        try {
            resp.sendError(sc, msg);
        } catch (Throwable t) {
            throw new RuntimeException("Error setting status code " + sc + " with message " + msg, t);
        }
    }
}
