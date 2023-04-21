package org.xactor.remoting;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transaction;
import org.jboss.remoting.CannotConnectException;
import org.jboss.remoting.Client;
import org.jboss.remoting.InvalidConfigurationException;
import org.jboss.remoting.InvokerLocator;
import org.xactor.Coordinator;
import org.xactor.RecoveryCoordinator;
import org.xactor.Resource;
import org.xactor.Synchronization;
import org.xactor.Terminator;
import org.xactor.TransactionFactory;
import org.xactor.tm.TxManager;

/**
 * Client-side DTM stubs are dynamic proxies that use this 
 * <code>InvocationHandler</code> implementation.
 *
 * @author <a href="mailto:reverbel@ime.usp.br">Francisco Reverbel</a>
 * @author <a href="mailto:ivanneto@gmail.com">Ivan Neto</a>
 */
public class ClientInvocationHandler implements InvocationHandler, Externalizable {

    static final long serialVersionUID = 2253923354553253502L;

    public static final char TRANSACTION_FACTORY = 'F';

    public static final char COORDINATOR = 'C';

    public static final char TERMINATOR = 'T';

    public static final char RESOURCE = 'R';

    public static final char RECOVERY_COORDINATOR = 'V';

    public static final char SYNCHRONIZATION = 'S';

    /** The DTM subsystem. */
    public static final String SUBSYSTEM = "DTM";

    private char interfaceCode;

    private long targetObjectId;

    private InvokerLocator[] locators;

    private Client client;

    private String stringRepresentation;

    /** No-arg constructor for externalization. */
    public ClientInvocationHandler() {
    }

    /** 
    * Constructs a <code>ClientInvocationHandler</code> for a target object
    * whose id is 0, given the remote interface of the target and its  
    * <code>InvokerLocator</code>s.
    */
    public ClientInvocationHandler(Class interf, InvokerLocator[] locators) throws Exception {
        this(interf, 0, locators);
    }

    /** 
    * Constructs a <code>ClientInvocationHandler</code> for a target object
    * given the remote interface of the target, its object id, and its
    * <code>InvokerLocator</code>s.
    */
    public ClientInvocationHandler(Class interf, long targetObjectId, InvokerLocator[] locators) throws Exception {
        this(getInterfaceCode(interf), targetObjectId, locators);
    }

    /** 
    * Constructs a <code>ClientInvocationHandler</code> for a target object
    * whose id is 0, given the remote interface code of the target and its 
    * <code>InvokerLocator</code>s.
    */
    public ClientInvocationHandler(char interfaceCode, InvokerLocator[] locators) throws Exception {
        this(interfaceCode, 0, locators);
    }

    /** 
    * Constructs a <code>ClientInvocationHandler</code> for a target object
    * given the remote interface code of the target, its object id, and its 
    * <code>InvokerLocator</code>s.
    */
    public ClientInvocationHandler(char interfaceCode, long targetObjectId, InvokerLocator[] locators) throws Exception {
        if (interfaceCode != TRANSACTION_FACTORY && interfaceCode != COORDINATOR && interfaceCode != TERMINATOR && interfaceCode != RESOURCE && interfaceCode != RECOVERY_COORDINATOR && interfaceCode != SYNCHRONIZATION) throw new IllegalArgumentException();
        if (locators.length == 0) throw new IllegalArgumentException();
        this.interfaceCode = interfaceCode;
        this.targetObjectId = targetObjectId;
        this.locators = locators;
    }

    /** 
    * Returns a <code>Class</code> instance representing the remote interface
    * implemented by the dynamic proxies that use this invocation handler.  
    */
    Class getClientInterface() {
        switch(interfaceCode) {
            case TRANSACTION_FACTORY:
                return TransactionFactory.class;
            case COORDINATOR:
                return Coordinator.class;
            case TERMINATOR:
                return Terminator.class;
            case RESOURCE:
                return Resource.class;
            case RECOVERY_COORDINATOR:
                return RecoveryCoordinator.class;
            case SYNCHRONIZATION:
                return Synchronization.class;
        }
        throw new RuntimeException("Illegal value in field interfaceCode");
    }

    /** 
    * Takes a <code>Class</code> instance representing a DTM interface
    * and converts is into an interface code.
    */
    private static char getInterfaceCode(Class interf) {
        if (interf == TransactionFactory.class) return TRANSACTION_FACTORY; else if (interf == Coordinator.class) return COORDINATOR; else if (interf == Terminator.class) return TERMINATOR; else if (interf == Resource.class) return RESOURCE; else if (interf == RecoveryCoordinator.class) return RECOVERY_COORDINATOR; else if (interf == Synchronization.class) return SYNCHRONIZATION; else throw new IllegalArgumentException("argument is not a DTM interface");
    }

    /**
    * Uses the <code>InvokerLocator</code> associated with this handler to
    * send out an <code>Invocation</code> containing this handler's target 
    * object id, the given method, and the given arguments.   
    */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Exception savedException = null;
        if (method.getDeclaringClass() == Object.class) {
            String methodName = method.getName();
            if (methodName.equals("toString")) return this.toString(); else if (methodName.equals("hashCode")) return new Integer(this.toString().hashCode()); else if (methodName.equals("equals")) return new Boolean(this.toString().equals(args[0].toString()));
        }
        TxManager tm = TxManager.getInstance();
        Transaction tx = tm.getCurrentTransaction();
        if (client != null) {
            try {
                if (tx != null) tm.disassociateThread();
                if (!client.isConnected()) client.connect();
                return client.invoke(new Invocation(targetObjectId, method, args));
            } catch (CannotConnectException e) {
                client = null;
            } finally {
                if (tx != null) tm.associateThread(tx);
            }
        }
        Invocation invocation = new Invocation(targetObjectId, method, args);
        for (int i = 0; i < locators.length; i++) {
            try {
                if (tx != null) tm.disassociateThread();
                client = new Client(locators[i], SUBSYSTEM);
                client.connect();
                return client.invoke(invocation);
            } catch (CannotConnectException e) {
                client = null;
                savedException = e;
            } catch (InvalidConfigurationException e) {
                client = null;
                savedException = e;
            } finally {
                if (tx != null) tm.associateThread(tx);
            }
        }
        throw new RemoteException(savedException.getClass().getName(), savedException);
    }

    /**
    * Reads a <code>ClientInvocationHandler</code> in externalized form: 
    * interface code, target object id, and locator URI.
    */
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        this.interfaceCode = in.readChar();
        this.targetObjectId = in.readLong();
        short len = in.readShort();
        if (len < 1) throw new IOException("ObjectInput does not contain a valid " + "ClientInvocationHandler");
        this.locators = new InvokerLocator[len];
        for (int i = 0; i < len; i++) {
            this.locators[i] = new InvokerLocator(in.readUTF());
        }
        this.client = null;
    }

    /**
    * Writes a <code>ClientInvocationHandler</code> in externalized form: 
    * interface code, target object id, and locator URI.
    */
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeChar(interfaceCode);
        out.writeLong(targetObjectId);
        out.writeShort(locators.length);
        for (int i = 0; i < locators.length; i++) {
            out.writeUTF(locators[i].getLocatorURI());
        }
    }

    /**
    * Converts a <code>ClientInvocationHandler</code> to string. These are
    * examples of stringfied handlers:
    * <p>
    * <code>T3d00000004c75,socket://server4.acme.com:3873/</code><br>
    * <code>C1b6,socket://zee.acme.com:3873/|rmi://zee.acme.com:5678/</code>
    * <p>
    * The handler comprises an interface code (the first character), 
    * immediately followed by the hexadecimal representation of the target 
    * object id (a long value), a comma (','), and a list of locator URIs 
    * separated by vertical bars ('|'s).
    */
    public String toString() {
        if (stringRepresentation == null) stringRepresentation = interfaceCode + Long.toHexString(targetObjectId) + ',' + locators[0].getLocatorURI();
        for (int i = 1; i < locators.length; i++) {
            stringRepresentation += '|' + locators[i].getLocatorURI();
        }
        return stringRepresentation;
    }

    /**
    * Converts a stringfied handler back into a 
    * <code>ClientInvocationHandler</code> instance.
    */
    public static ClientInvocationHandler fromString(String s) throws Exception {
        String locatorURI;
        InvokerLocator locator;
        int oidEndIndex = s.indexOf(',');
        if (oidEndIndex == -1) throw new IllegalArgumentException();
        String oidString = s.substring(1, oidEndIndex);
        int uriStartIndex = oidEndIndex + 1;
        int uriEndIndex = s.indexOf('|', uriStartIndex);
        if (uriEndIndex == -1) {
            locatorURI = s.substring(uriStartIndex);
            locator = new InvokerLocator(locatorURI);
            return new ClientInvocationHandler(s.charAt(0), Long.parseLong(oidString, 16), new InvokerLocator[] { locator });
        } else {
            List locatorList = new ArrayList();
            while (uriEndIndex != -1) {
                locatorURI = s.substring(uriStartIndex, uriEndIndex);
                locator = new InvokerLocator(locatorURI);
                locatorList.add(locator);
                uriStartIndex = uriEndIndex + 1;
                uriEndIndex = s.indexOf('|', uriStartIndex);
            }
            locatorURI = s.substring(uriStartIndex);
            locator = new InvokerLocator(locatorURI);
            locatorList.add(locator);
            InvokerLocator[] locators = (InvokerLocator[]) locatorList.toArray(new InvokerLocator[0]);
            return new ClientInvocationHandler(s.charAt(0), Long.parseLong(oidString, 16), locators);
        }
    }
}
