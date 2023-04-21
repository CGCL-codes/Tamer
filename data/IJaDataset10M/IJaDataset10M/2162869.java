package tgdh.comm;

import tgdh.TgdhException;
import edu.ds.p2p.message.CommBuf;
import edu.ds.p2p.mpi.CommWorld;

public class Communicator extends Thread {

    protected CommWorld world;

    public Communicator(CommWorld world) throws TgdhException {
        this.world = world;
    }

    public void send(TgdhMessage message) throws TgdhException {
        send(null, null, message);
    }

    public void send(Integer dest, Integer src, TgdhMessage message) {
        world.send(dest, CommBuf.build(message));
        if (printCommInfo && message != null) System.out.println((new StringBuilder()).append("Send:").append(message.toString()).toString());
    }

    public Object receive() throws TgdhException {
        return receive(2000L);
    }

    public Object receive(long timeout) throws TgdhException {
        CommBuf<TgdhMessage> obj = null;
        world.receive(null, obj);
        if (printCommInfo && obj != null) System.out.println((new StringBuilder()).append("Receive:").append(obj.toString()).toString());
        if (obj == null) return null;
        return obj.getData();
    }

    public void disconnect() {
    }

    public void close() {
    }

    public int getLocalAdresse() {
        return world.rank();
    }

    private boolean printCommInfo = true;
}
