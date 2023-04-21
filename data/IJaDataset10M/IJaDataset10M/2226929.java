package net.sf.repbot.server;

import net.sf.repbot.FibsListener;
import java.util.regex.*;

/** Implementation of the async tell command. 
 *
 *  @author  Avi Kivity
 */
public class ShoutImpl implements Shout {

    private CommandQueue connection;

    private Pattern p_affirmative = Pattern.compile("You shout: (.*)");

    /** Creates a new instance of ShoutImpl */
    public ShoutImpl(CommandQueue connection) {
        this.connection = connection;
    }

    /** Shouts something.  */
    public void shout(String what, ReplyListener listener) {
        String command = "shout " + what;
        Request request = new Request(listener);
        CommandQueue.Handle handle = connection.send(command, request);
        request.setHandle(handle);
    }

    private class Request implements CommandQueue.Listener {

        private ReplyListener listener;

        private CommandQueue.Handle handle;

        public Request(ReplyListener listener) {
            this.listener = listener;
        }

        /** Called on a received line.  */
        public void onLine(String line, FibsListener connection) {
            Matcher m;
            m = p_affirmative.matcher(line);
            if (m.matches()) {
                handle.acknowledge();
                listener.onShout();
                return;
            }
        }

        /** Called when the command has been unacknowledged for too long.  */
        public void onTimeout() {
            listener.onTimeout();
        }

        /** Sets the command handle. */
        public void setHandle(CommandQueue.Handle handle) {
            this.handle = handle;
        }
    }
}
