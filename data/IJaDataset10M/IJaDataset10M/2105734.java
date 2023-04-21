package org.jsmtpd.core.receive;

import java.io.IOException;
import java.net.Socket;
import org.jsmtpd.generic.threadpool.IThreadedClass;

/**
 * Implements the threaded interface of generic pool
 * holds instance of the smtp protocol receiving handler
 * @author Jean-Francois POUX
 */
public class ReceiverWorkerImpl implements IThreadedClass {

    private ProtocolHandler proto = new ProtocolHandler();

    private Socket rec = null;

    public void doJob() {
        if (rec.getClass().getName().toLowerCase().contains("ssl")) proto.init(rec, true);
        proto.init(rec, false);
    }

    public void forceShutdown() {
        try {
            if (rec != null) rec.close();
        } catch (IOException e) {
        }
    }

    public void gracefullShutdown() {
        try {
            if (rec != null) rec.close();
        } catch (IOException e) {
        }
    }

    public void setParam(Object o) {
        rec = (Socket) o;
    }
}
