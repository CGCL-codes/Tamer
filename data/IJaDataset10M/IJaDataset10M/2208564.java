package de.highbyte_le.weberknecht.request.processing;

import java.io.IOException;
import java.sql.Connection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import de.highbyte_le.weberknecht.request.ContentProcessingException;
import de.highbyte_le.weberknecht.request.actions.ActionExecutionException;
import de.highbyte_le.weberknecht.request.actions.ExecutableAction;
import de.highbyte_le.weberknecht.request.actions.TaskedExecutableAction;
import de.highbyte_le.weberknecht.request.routing.RoutingTarget;

/**
 * Executes an {@link ExecutableAction}.
 * 
 * @author pmairif
 */
public class ActionExecution implements Processor {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response, RoutingTarget routingTarget, ExecutableAction action, ProcessingChain chain) throws ProcessingException {
        try {
            if (action instanceof TaskedExecutableAction) ((TaskedExecutableAction) action).execute(request, response, routingTarget.getTask()); else action.execute(request, response);
            chain.doContinue();
        } catch (ServletException e) {
            throw new ProcessingException("servlet exception: " + e.getMessage(), e);
        } catch (IOException e) {
            throw new ProcessingException("i/o exception: " + e.getMessage(), e);
        } catch (ActionExecutionException e) {
            throw new ProcessingException("action execution exception: " + e.getMessage(), e);
        } catch (ContentProcessingException e) {
            throw new ProcessingException("content processing exception: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean needsDatabase() {
        return false;
    }

    @Override
    public void setDatabase(Connection con) {
    }
}
