package org.jpos.iso.channel;

import org.jpos.iso.FilteredBase;
import org.jpos.iso.ISOException;
import org.jpos.iso.ISOFilter.VetoException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOPackager;
import org.jpos.util.*;
import java.io.IOException;

public class LoopbackChannel extends FilteredBase implements LogSource {

    boolean usable = true;

    private int[] cnt;

    String name;

    BlockingQueue queue;

    Logger logger;

    String realm;

    public LoopbackChannel() {
        super();
        cnt = new int[SIZEOF_CNT];
        queue = new BlockingQueue();
    }

    /**
    * setPackager is optional on LoopbackChannel, it is
    * used for debugging/formating purposes only
    */
    public void setPackager(ISOPackager packager) {
    }

    public void connect() {
        cnt[CONNECT]++;
        usable = true;
        setChanged();
        notifyObservers();
    }

    /**
     * disconnects ISOChannel
     */
    public void disconnect() {
        usable = false;
        setChanged();
        notifyObservers();
    }

    public void reconnect() {
        usable = true;
        setChanged();
        notifyObservers();
    }

    public boolean isConnected() {
        return usable;
    }

    public void send(ISOMsg m) throws IOException, ISOException, VetoException {
        if (!isConnected()) throw new ISOException("unconnected ISOChannel");
        LogEvent evt = new LogEvent(this, "loopback-send", m);
        m = applyOutgoingFilters(m, evt);
        queue.enqueue(m);
        cnt[TX]++;
        notifyObservers();
        Logger.log(evt);
    }

    public void send(byte[] b) throws IOException, ISOException, VetoException {
        if (!isConnected()) throw new ISOException("unconnected ISOChannel");
        LogEvent evt = new LogEvent(this, "loopback-send", b);
        queue.enqueue(b);
        cnt[TX]++;
        notifyObservers();
        Logger.log(evt);
    }

    public ISOMsg receive() throws IOException, ISOException {
        if (!isConnected()) throw new ISOException("unconnected ISOChannel");
        try {
            ISOMsg m = (ISOMsg) ((ISOMsg) queue.dequeue()).clone();
            LogEvent evt = new LogEvent(this, "loopback-receive", m);
            m = applyIncomingFilters(m, evt);
            cnt[RX]++;
            notifyObservers();
            Logger.log(evt);
            return m;
        } catch (InterruptedException e) {
            throw new IOException(e.toString());
        } catch (BlockingQueue.Closed e) {
            throw new IOException(e.toString());
        }
    }

    public void setUsable(boolean usable) {
        this.usable = usable;
        setChanged();
        notifyObservers();
    }

    public int[] getCounters() {
        return cnt;
    }

    public void setName(String name) {
        this.name = name;
        NameRegistrar.register("channel." + name, this);
    }

    public String getName() {
        return name;
    }

    public ISOPackager getPackager() {
        return null;
    }

    public void resetCounters() {
        for (int i = 0; i < SIZEOF_CNT; i++) cnt[i] = 0;
    }

    public void setLogger(Logger logger, String realm) {
        this.logger = logger;
        this.realm = realm;
    }

    public String getRealm() {
        return realm;
    }

    public Logger getLogger() {
        return logger;
    }
}
