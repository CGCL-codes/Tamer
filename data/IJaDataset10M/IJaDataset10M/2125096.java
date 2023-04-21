package org.campware.dream.modules.actions;

import java.io.StringWriter;
import org.apache.commons.logging.*;
import org.apache.velocity.context.Context;
import org.apache.velocity.exception.*;
import org.apache.velocity.app.Velocity;

/**
 * This tool allows access to velocity via the current context.
 * The evaluate method allows the string results of a reference to be
 * parsed at will using the evaluate() method.
 *
 * @author Bill Boland
 */
public class VelocityTool {

    static Log log = LogFactory.getLog(VelocityTool.class);

    /**
     * Context used for evaluate method.
     */
    protected Context context;

    /**
     * Default constructor.
     */
    public VelocityTool() {
    }

    /**
     * Constructs the tool and sets the context.
     * @param c the Context object
     */
    public VelocityTool(Context c) {
        context = c;
    }

    /**
     * Sets the context that will be used by the tool.
     * @param context the Context object
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Get the context currently assigned to the tool
     * @param c the Context object
     */
    public Context getContext() {
        return context;
    }

    /**
     * A method to evaluate a template string using the assigned context.
     * It is just a think wrapper around the Velocity.evaluate method.
     * The context is usually the context in which the tool resides but it could
     * be initialized with a different context.
     * @param s the string to be evaluated (a string template)
     * @return the resulting sting from the evaluation.
     */
    public String evaluate(String s) {
        if (s != null) {
            log.debug("evaluate: " + s);
            StringWriter stringWriter = new StringWriter(s.length());
            try {
                Velocity.evaluate(context, stringWriter, "VelocityTool.evaluate", s);
                stringWriter.close();
                return stringWriter.toString();
            } catch (ResourceNotFoundException rnfe) {
                log.error("ResourceNotFoundException: " + rnfe, rnfe);
            } catch (ParseErrorException pee) {
                log.error("ParseErrorException: " + pee, pee);
            } catch (Exception e) {
                log.error("Exception: " + e, e);
            }
        }
        return s;
    }

    protected void finalize() throws Throwable {
        log.debug("Finalizing Velocity Tool.");
    }
}
