package mucode.abstractions;

import mucode.*;
import mucode.util.*;
import java.io.*;

/** This class provides some abstractions to deal with the relocation of
 * classes and threads at a higher level than the one enabled by the
 * primitives provided in the core {@link mucode} package. <p>
 *
 * Under the hood, <code>Relocator</code> objects still rely on a
 * <i>&micro;</i>Server, that is indeed specified at creation time (and can
 * later be changed). The operations provided by <code>Relocator</code> are
 * represented in terms of group shipping and handling. In fact, the semantics
 * of the operations in <code>Relocator</code> is partly implemented in this
 * class (that manages the creation and shipping of groups) and partly by the
 * <code>RelocationHandler</code> class (that manages the handling of the
 * group at destination). <p>
 * 
 * In other words, the effect of these operations could be obtained by the
 * programmer entirely by using only the concepts provided by the
 * <code>mucode</code> package. Nevertheless, <code>Relocator</code> provides
 * a more reasonable set of primitives for the application programmer. The
 * goal of <i>&micro;</i>Code is actually exactly this: to provide a minimal
 * set of primitives that can be used by other people to build libraries of
 * higher-level operations and concepts. <code>Relocator</code>, together with
 * the other classes in this package, like {@link MuAgent}, is an incarnation
 * of this design approach.
 * 
 * @author <a href="mailto:picco@elet.polimi.it">Gian Pietro Picco</a>
 * @version 1.0
 * @see ClosureConstants
 * @see MuServer */
public class Relocator implements ClosureConstants {

    static final String HANDLER = "mucode.abstractions.RelocationHandler";

    static final int COPY_THREAD = 10;

    static final int SPAWN_THREAD = 20;

    static final int FETCH_CLASSES = 30;

    static final int SHIP_CLASSES = 40;

    static final String PARAMETERS = "_PARS_";

    static final String THREADLABEL = "_THREAD_";

    private MuServer server = null;

    /**
   * Creates an instance of <code>Relocator</code>, by specifying the
   * <i>&micro;</i>Server that will be used to perform the operations.
   */
    public Relocator(MuServer server) {
        this.server = server;
    }

    /**
   * Returns the <i>&micro;</i>Server used by this instance of
   * <code>Relocator</code> to perform its operations.
   */
    public MuServer getServer() {
        return server;
    }

    /**
   * Sets the <i>&micro;</i>Server used by this instance of
   * <code>Relocator</code> to perform its operations. This allows to use a
   * single <code>Relocator</code> object to perform operations on different
   * <i>&micro;</i>Servers.
   */
    public void setServer(MuServer server) {
        this.server = server;
    }

    /** Clones the <code>Runnable</code> object passed as parameter, and ships
   * it on a different <i>&micro;</i>Server, where it will restart its
   * execution. The execution state is discarded, while the data state is
   * preserved.
   *
   * The <code>Runnable</code> object can be a <code>Thread</code> object or a
   * <code>Runnable</code> that has been used to spawn a thread, and that
   * retains the (data) state of the thread itself. In both cases, however,
   * the class implementing <code>Runnable</code> that is passed as a
   * parameter to this method, must also implement
   * <code>Serializable</code>. Otherwise, a {@link java.io.NotSerializableException} is thrown.
   * 
   * @param destination the address of the target <i>&micro;</i>Server,
   * specified as <code>hostname:port</code>.
   * @param t the <code>Runnable</code> object to be cloned on the remote
   * <i>&micro;</i>Server. It must implement the {@link java.io.Serializable}
   * interface.
   * @param classClosure determines the amount of closure transferred. The
   * acceptable values are defined in {@link ClosureConstants}. 
   * @param dynLink the address of a <i>&micro;</i>Server from which classes
   * not shipped along with the <code>Runnable</code>object can be retrieved
   * through dynamic linking. If <code>null</code>, remote dynamic linking is
   * prevented.
   * @param synch if <code>true</code>, the operation is blocking for the
   * caller, that is suspended until a return value is received after the
   * thread is restarted on the target <i>&micro;</i>Server. Otherwise, the
   * method returns immediately after group transmission is completed.
   * @exception NotSerializableException if the <code>Runnable</code>, or some
   * of its fields, are not serializable, i.e., they do not implement the
   * {@link java.io.Serializable} interface.
   * @exception IOException if the symbolic name given as
   * destination cannot be resolved in a proper IP address, or some problem
   * occurs with connection or serialization.  
   * @exception ClassNotFoundException if some of the classes referenced
   * by the group cannot be resolved.  */
    public final void copyThread(String destination, Runnable t, int classClosure, String dynLink, boolean synch) throws IOException, ClassNotFoundException {
        Class c = t.getClass();
        Group group = server.createGroup(c.getName(), HANDLER);
        group.setOperation(COPY_THREAD);
        group.addObject(THREADLABEL, t);
        group.addClasses(ClassInspector.getClassClosure(c, server, classClosure));
        group.setDynamicLinkSource(dynLink);
        group.setSynchronousTransfer(synch);
        group.ship(destination);
    }

    /** Creates a new thread at the specified <i>&micro;</i>Server. The
   * programmer can specify a set of classes, that will be loaded in the
   * private class space of the thread spawned in the target
   * <i>&micro;</i>Server. The methods provided by {@link
   * mucode.util.ClassInspector} may be useful in determining these
   * classes.<p>
   * 
   * A thread will be spawned remotely by using an object whose class is
   * specified by the <code>root</code> parameter. Such class must
   * implement the {@link java.lang.Runnable} interface, either directly of by
   * subclassing from {@link java.lang.Thread}. <p>
   *
   * Optionally, an array of parameters can be specified, which can be used at
   * destination to instantiate the thread from <code>root</code> using a
   * constructor other than the default one. The type of the elements in
   * <code>parameters</code> must match the signature of one of the
   * constructors in <code>root</code>.
   *
   * @param destination the address of the target <i>&micro;</i>Server,
   * specifieed as <code>hostname:port</code>.
   * @param root the name of the class used to spawn the thread at
   * destination. Fully specified names can be used to resolve name clashes
   * across packages.
   * @param classNames the names of the classes needed by the thread at
   * destination, that will be loaded in its private class space.
   * @param parameters the parameters for instantiating the thread on the
   * remote <i>&micro;</i>Server. The types of this parameters must match the
   * signature of one of the constructors of <code>root</code>. Moreover,
   * all the parameters must be serializable.
   * @param dynLink the address of a <i>&micro;</i>Server from which classes
   * not shipped along with the <code>Runnable</code>object can be retrieved
   * through dynamic linking. If <code>null</code>, remote dynamic linking is
   * prevented.
   * @param synch if <code>true</code>, the operation is blocking for the
   * caller, that is suspended until a return value is received after the
   * thread is restarted on the target <i>&micro;</i>Server. Otherwise, the
   * method returns immediately after group transmission is completed.
   * @exception NotSerializableException if some of the parameters are not
   * serializable, i.e., they do not implement the {@link
   * java.io.Serializable} interface.
   * @exception IOException if the symbolic name given as
   * destination cannot be resolved in a proper IP address, or some problem
   * occurs with connection or serialization.  
   * @exception ClassNotFoundException if some of the classes referenced
   * by the group cannot be resolved.  */
    public final void spawnThread(String destination, String root, String[] classNames, Serializable[] parameters, String dynLink, boolean synch) throws IOException, ClassNotFoundException {
        Group group = server.createGroup(root, HANDLER);
        group.setOperation(SPAWN_THREAD);
        if (parameters != null) group.addObject(PARAMETERS, parameters);
        group.addClasses(classNames);
        group.setDynamicLinkSource(dynLink);
        group.setSynchronousTransfer(synch);
        group.ship(destination);
    }

    /** Copies and transfers one or more classes from the shared class space of
   * a remote <i>&micro;</i>Server to the one of the <i>&micro;</i>Server
   * hosting the thread requesting the operation.
   * 
   * @param source the address of the source <i>&micro;</i>Server, specified
   * as <code>hostname:port</code>.
   * @param classNames the names of the classes to be transferred.
   * @param classClosure determines the amount of closure transferred. The
   * acceptable values are defined in {@link ClosureConstants}. Note, however,
   * that the value <code>NONE</code> is not allowed, and will generate an
   * <code>IllegalArgumentException</code>.
   * @param synch if <code>true</code>, the operation is blocking for the
   * caller, that is suspended until a return value is received after the
   * thread is restarted on the target <i>&micro;</i>Server. Otherwise, the
   * method returns immediately after group transmission is completed.
   * @exception IOException if the symbolic name given as
   * destination cannot be resolved in a proper IP address, or some problem
   * occurs with connection or serialization.  
   * @exception ClassNotFoundException if some of the classes referenced
   * by the group cannot be resolved.  */
    public final void fetchClasses(String source, String[] classNames, int classClosure, boolean synch) throws IOException, ClassNotFoundException {
        if (classClosure == NONE) throw new IllegalArgumentException("NONE is not a legal class closure" + " option for fetchClass()");
        Group group = server.createGroup(null, HANDLER);
        group.setOperation(FETCH_CLASSES);
        group.addObject(PARAMETERS, new Serializable[] { new Integer(classClosure), classNames });
        group.setDynamicLinkSource(null);
        group.setSynchronousTransfer(synch);
        group.ship(source);
    }

    /** Copies and transfers one or more classes from the <i>&micro;</i>Server
   * used by this class to the shared class space of another
   * <i>&micro;</i>Server.
   *
   * @param destination the address of the target <i>&micro;</i>Server,
   * specified as <code>hostname:port</code>.
   * @param classNames the names of the classes to be transferred.
   * @param classClosure determines the amount of closure transferred. The
   * acceptable values are defined in {@link ClosureConstants}. Note, however,
   * that the value <code>NONE</code> is not allowed, and will generate an
   * <code>IllegalArgumentException</code>.
   * @param synch if <code>true</code>, the operation is blocking for the
   * caller, that is suspended until a return value is received after the
   * thread is restarted on the target <i>&micro;</i>Server. Otherwise, the
   * method returns immediately after group transmission is completed.
   * @exception IOException if the symbolic name given as
   * destination cannot be resolved in a proper IP address, or some problem
   * occurs with connection or serialization.  
   * @exception ClassNotFoundException if some of the classes referenced
   * by the group cannot be resolved.  */
    public final void shipClasses(String destination, String[] classNames, boolean synch) throws IOException, ClassNotFoundException {
        Group group = server.createGroup(null, HANDLER);
        group.setOperation(SHIP_CLASSES);
        group.addClasses(classNames);
        group.setDynamicLinkSource(null);
        group.setSynchronousTransfer(synch);
        group.ship(destination);
    }
}
