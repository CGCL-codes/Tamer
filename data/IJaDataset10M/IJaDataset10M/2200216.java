package fr.jade.fraclite.orb;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.objectweb.fractal.api.Interface;
import org.objectweb.naming.Binder;

/**
 * A skeleton gives access to a local interface of a Fractal
 * component. It unmarshalls invocation messages and calls the corresponding
 * methods on the local interface to which it gives access. Like stubs, a
 * skeleton gives access to the "functional" interface provided by a server
 * interface, and also gives access to the methods of the {@link Interface}
 * interface implemented by this server interface.
 */
public class Skeleton {

    /**
   * The local server interface to which this skeleton gives access.
   */
    protected Map<Long, Object> targets;

    protected Binder binder;

    protected ServerSocket ss;

    protected ExecutorService es;

    /**
   * Constructs a new {@link Skeleton}.
   * @throws IOException 
   */
    public Skeleton(ExecutorService es, Binder binder, int port) throws IOException {
        targets = new HashMap<Long, Object>();
        this.binder = binder;
        this.ss = new ServerSocket(port);
        this.es = es;
    }

    public void registerTarget(Object target) {
        long itfId = ((Interface) target).getFcItfGUId();
        targets.put(itfId, target);
    }

    public void start() throws IOException {
        new Thread() {

            public void run() {
                try {
                    while (true) {
                        Socket s = ss.accept();
                        es.execute(new SkeletonTask(s, targets, binder));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public int getPort() {
        return ss.getLocalPort();
    }

    @Override
    public void finalize() {
        try {
            ss.close();
        } catch (IOException ignored) {
        }
    }
}
