package network.server;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Iterator;
import network.DataPacket;
import com.sun.sgs.app.AppContext;
import com.sun.sgs.app.Channel;
import com.sun.sgs.app.ManagedObject;
import com.sun.sgs.app.Task;
import com.sun.sgs.app.util.ScalableList;

/**
 * the task that sends the update message to all connected clients
 * @author Jack
 *
 */
public class UpdateTask implements Task, Serializable {

    private static final long serialVersionUID = 1L;

    long start = 0;

    long diff = 0;

    public void run() throws Exception {
        long current = System.currentTimeMillis();
        if (start != 0) {
            diff = current - start;
            sendUpdateMessage(diff);
        }
        start = current;
    }

    /**
	 * sends an update message to all clients
	 * @param ms the number of milliseconds to advance the game time
	 */
    private void sendUpdateMessage(long ms) {
        try {
            ManagedObject temp = AppContext.getDataManager().getBindingForUpdate(Server.userInputData);
            if (temp instanceof ScalableList<?>) {
                ScalableList<?> s = (ScalableList<?>) temp;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeLong(ms);
                Iterator<?> i = s.iterator();
                while (i.hasNext()) {
                    dos.write(((DataPacket) i.next()).getData());
                }
                ByteBuffer buffer = ByteBuffer.wrap(baos.toByteArray());
                Channel c = AppContext.getChannelManager().getChannel(Server.allChannelName);
                c.send(null, buffer);
            }
        } catch (IOException a) {
        }
    }
}
