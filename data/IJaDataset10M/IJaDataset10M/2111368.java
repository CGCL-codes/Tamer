package g4mfs.impl.org.peertrust.tnviz.app;

import g4mfs.impl.org.peertrust.config.Configurable;
import g4mfs.impl.org.peertrust.event.AnswerEvent;
import g4mfs.impl.org.peertrust.event.EventDispatcher;
import g4mfs.impl.org.peertrust.event.PTEvent;
import g4mfs.impl.org.peertrust.event.PTEventListener;
import g4mfs.impl.org.peertrust.event.QueryEvent;
import g4mfs.impl.org.peertrust.exception.ConfigurationException;
import org.apache.log4j.Logger;
import g4mfs.impl.org.peertrust.event.*;
import g4mfs.impl.org.peertrust.net.Answer;
import g4mfs.impl.org.peertrust.net.Query;

/**
 * <p>
 * 
 * </p><p>
 * $Id: TNVizListener.java,v 1.1 2005/11/30 10:35:09 ionut_con Exp $
 * <br/>
 * Date: 10-Feb-2005
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:09 $
 * by $Author: ionut_con $
 * </p>
 * @author Sebastian Wittler and Michael Sch?fer
 */
public class TNVizListener implements PTEventListener, Configurable {

    private static Logger log = Logger.getLogger(TNVizListener.class);

    EventDispatcher _dispatcher;

    private Graphics graphics;

    public TNVizListener() {
        graphics = new TNGraphics();
    }

    public void init() throws ConfigurationException {
        String msg = null;
        if (_dispatcher == null) {
            msg = "There not exist an event dispatcher";
            throw new ConfigurationException(msg);
        }
        _dispatcher.register(this);
    }

    public void event(PTEvent event) {
        if (event instanceof QueryEvent) {
            Query query = ((QueryEvent) event).getQuery();
            log.debug("New query received from " + query.getSource().getAlias() + ": " + query.getGoal() + " - " + query.getReqQueryId() + " - " + query.getTrace().printTrace());
            graphics.addQuery(query);
        } else if (event instanceof AnswerEvent) {
            Answer answer = ((AnswerEvent) event).getAnswer();
            log.debug("New answer received from " + answer.getSource().getAlias() + ": " + answer.getGoal() + " - " + answer.getReqQueryId() + " - " + answer.getTrace().printTrace());
            graphics.addAnswer(answer);
        }
        graphics.updateGraph();
    }

    public EventDispatcher getEventDispatcher() {
        return _dispatcher;
    }

    /**
	 * @param _dispatcher The _dispatcher to set.
	 */
    public void setEventDispatcher(EventDispatcher dispatcher) {
        this._dispatcher = dispatcher;
    }
}
