package org.apache.velocity.app.event;

import org.apache.velocity.context.Context;
import org.apache.velocity.util.ContextAware;

/**
 *  Event handler called when the RHS of #set is null.  Lets an app approve / veto
 *  writing a log message based on the specific reference.
 *
 * @author <a href="mailto:wglass@forio.com">Will Glass-Husain</a>
 * @author <a href="mailto:geirm@optonline.net">Geir Magnusson Jr.</a>
 * @version $Id: NullSetEventHandler.java 470256 2006-11-02 07:20:36Z wglass $
 */
public interface NullSetEventHandler extends EventHandler {

    /**
     * Called when the RHS of a #set() is null, which will result
     * in a null LHS. All NullSetEventHandlers
     * are called in sequence until a false is returned.  If no NullSetEventHandler
     * is registered all nulls will be logged.
     *
     *  @param lhs  reference literal of left-hand-side of set statement
     *  @param rhs  reference literal of right-hand-side of set statement
     *  @return true if log message should be written, false otherwise
     */
    public boolean shouldLogOnNullSet(String lhs, String rhs);

    /**
     * Defines the execution strategy for shouldLogOnNullSet
     */
    static class ShouldLogOnNullSetExecutor implements EventHandlerMethodExecutor {

        private Context context;

        private String lhs;

        private String rhs;

        /**
         * when this is false, quit iterating
         */
        private boolean result = true;

        private boolean executed = false;

        ShouldLogOnNullSetExecutor(Context context, String lhs, String rhs) {
            this.context = context;
            this.lhs = lhs;
            this.rhs = rhs;
        }

        /**
         * Call the method shouldLogOnNullSet()
         *  
         * @param handler call the appropriate method on this handler
         */
        public void execute(EventHandler handler) {
            NullSetEventHandler eh = (NullSetEventHandler) handler;
            if (eh instanceof ContextAware) ((ContextAware) eh).setContext(context);
            executed = true;
            result = ((NullSetEventHandler) handler).shouldLogOnNullSet(lhs, rhs);
        }

        public Object getReturnValue() {
            return new Boolean(result);
        }

        public boolean isDone() {
            return executed && !result;
        }
    }
}
