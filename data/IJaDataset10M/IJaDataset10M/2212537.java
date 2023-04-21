package net.sf.reqbook.services.pipe;

import net.sf.reqbook.common.InternalErrorException;

/**
 * $Id: Pipeline.java,v 1.11 2006/02/21 10:59:16 poma Exp $
 * <p/>
 * Created: 25.05.2005
 * <p/>
 * Represents pipe line of SAX event handlers.
 * New elements in the pipe are added via {@link #addHandler} method.
 * All pipe handlers must implement {@link net.sf.reqbook.services.pipe.PipeHandler} interface.
 * @author Pavel Sher
 */
public interface Pipeline {

    /**
     * Appends new handler in the pipe.
     * @param handler
     */
    void addHandler(PipeHandler handler);

    /**
     * Appends new handler in the pipe and assigns it specified name.
     * @param handlerName name of the handler
     * @param handler
     */
    void addHandler(String handlerName, PipeHandler handler);

    /**
     * Returns handler having specified name
     * @param handlerName name of the handler to return
     * @return pipe handler or null if handler was not found
     */
    PipeHandler getHandler(String handlerName);

    /**
     * Binds all handlers together and returns first
     * handler in the pipe.
     * @return first handler in the pipe or null if
     * there are no handlers at all.
     */
    PipeHandler bindHandlers() throws InternalErrorException;

    /**
     * Re-sets pipeline configuration: drops all handlers,  clears all errors and parameters.
     */
    void reset();

    /**
     * Returns first handler in the pipe or null if there are no handlers at all
     * @return Returns first handler in the pipe or null if there are no handlers at all
     */
    PipeHandler getFirstHandler();
}
