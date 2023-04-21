package com.sun.corba.se.impl.interceptors;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import org.omg.PortableInterceptor.ForwardRequest;
import org.omg.PortableInterceptor.InvalidSlot;
import org.omg.PortableInterceptor.RequestInfo;
import org.omg.PortableInterceptor.LOCATION_FORWARD;
import org.omg.IOP.TaggedProfile;
import org.omg.IOP.TaggedComponent;
import org.omg.IOP.ServiceContextHelper;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.omg.CORBA.ParameterMode;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_INV_ORDER;
import org.omg.CORBA.BAD_PARAM;
import org.omg.CORBA.CompletionStatus;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.CTX_RESTRICT_SCOPE;
import org.omg.CORBA.ExceptionList;
import org.omg.CORBA.INTERNAL;
import org.omg.CORBA.LocalObject;
import org.omg.CORBA.NamedValue;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.NO_RESOURCES;
import org.omg.CORBA.NVList;
import org.omg.CORBA.Object;
import org.omg.CORBA.Policy;
import org.omg.CORBA.SystemException;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.UNKNOWN;
import org.omg.CORBA.UserException;
import org.omg.CORBA.portable.ApplicationException;
import org.omg.CORBA.portable.Delegate;
import org.omg.CORBA.portable.InputStream;
import org.omg.Dynamic.Parameter;
import com.sun.corba.se.spi.legacy.connection.Connection;
import com.sun.corba.se.spi.legacy.interceptor.RequestInfoExt;
import com.sun.corba.se.spi.ior.IOR;
import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
import com.sun.corba.se.spi.orb.ORB;
import com.sun.corba.se.spi.logging.CORBALogDomains;
import com.sun.corba.se.spi.servicecontext.ServiceContexts;
import com.sun.corba.se.spi.servicecontext.UnknownServiceContext;
import com.sun.corba.se.impl.encoding.CDRInputStream_1_0;
import com.sun.corba.se.impl.encoding.EncapsOutputStream;
import com.sun.corba.se.impl.orbutil.ORBUtility;
import com.sun.corba.se.impl.orbutil.ORBClassLoader;
import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.corba.se.impl.logging.InterceptorsSystemException;
import com.sun.corba.se.impl.logging.OMGSystemException;

/**
 * Implementation of the RequestInfo interface as specified in
 * orbos/99-12-02 section 5.4.1.
 */
public abstract class RequestInfoImpl extends LocalObject implements RequestInfo, RequestInfoExt {

    protected ORB myORB;

    protected InterceptorsSystemException wrapper;

    protected OMGSystemException stdWrapper;

    protected int flowStackIndex = 0;

    protected int startingPointCall;

    protected int intermediatePointCall;

    protected int endingPointCall;

    protected short replyStatus = UNINITIALIZED;

    protected static final short UNINITIALIZED = -1;

    protected int currentExecutionPoint;

    protected static final int EXECUTION_POINT_STARTING = 0;

    protected static final int EXECUTION_POINT_INTERMEDIATE = 1;

    protected static final int EXECUTION_POINT_ENDING = 2;

    protected boolean alreadyExecuted;

    protected Connection connection;

    protected ServiceContexts serviceContexts;

    protected ForwardRequest forwardRequest;

    protected IOR forwardRequestIOR;

    protected SlotTable slotTable;

    protected Exception exception;

    /**
     * Reset the info object so that it can be reused for a retry,
     * for example.
     */
    void reset() {
        flowStackIndex = 0;
        startingPointCall = 0;
        intermediatePointCall = 0;
        endingPointCall = 0;
        replyStatus = UNINITIALIZED;
        currentExecutionPoint = EXECUTION_POINT_STARTING;
        alreadyExecuted = false;
        connection = null;
        serviceContexts = null;
        forwardRequest = null;
        forwardRequestIOR = null;
        exception = null;
    }

    protected static final int MID_REQUEST_ID = 0;

    protected static final int MID_OPERATION = 1;

    protected static final int MID_ARGUMENTS = 2;

    protected static final int MID_EXCEPTIONS = 3;

    protected static final int MID_CONTEXTS = 4;

    protected static final int MID_OPERATION_CONTEXT = 5;

    protected static final int MID_RESULT = 6;

    protected static final int MID_RESPONSE_EXPECTED = 7;

    protected static final int MID_SYNC_SCOPE = 8;

    protected static final int MID_REPLY_STATUS = 9;

    protected static final int MID_FORWARD_REFERENCE = 10;

    protected static final int MID_GET_SLOT = 11;

    protected static final int MID_GET_REQUEST_SERVICE_CONTEXT = 12;

    protected static final int MID_GET_REPLY_SERVICE_CONTEXT = 13;

    protected static final int MID_RI_LAST = 13;

    /**
     * Creates a new RequestInfoImpl object.
     */
    public RequestInfoImpl(ORB myORB) {
        super();
        this.myORB = myORB;
        wrapper = InterceptorsSystemException.get(myORB, CORBALogDomains.RPC_PROTOCOL);
        stdWrapper = OMGSystemException.get(myORB, CORBALogDomains.RPC_PROTOCOL);
        PICurrent current = (PICurrent) (myORB.getPIHandler().getPICurrent());
        slotTable = current.getSlotTable();
    }

    /**
     * Implementation for request_id() differs for client and server 
     * implementations.
     *
     * Uniquely identifies an active request/reply sequence.  Once a
     * request/reply sequence is concluded this ID may be reused.  (this
     * is NOT necessarily the same as the GIOP request_id).
     */
    public abstract int request_id();

    /**
     * Implementation for operation() differs for client and server
     * implementations.
     *
     * The name of the operation being invoked.
     */
    public abstract String operation();

    /**
     * This method returns the list of arguments for the operation that was
     * invoked. It raises NO_RESOURCES exception if the operation is not invoked
     * by using DII mechanism.
     */
    public abstract Parameter[] arguments();

    /**
     * This method returns the list of exceptios  that was raised when the
     * operation was invoked. It raises NO_RESOURCES exception if the operation
     * is not invoked by using DII mechanism.
     */
    public abstract TypeCode[] exceptions();

    /**
     * This method returns the list of contexts for the DII operation. 
     * It raises NO_RESOURCES exception if the operation is not invoked by 
     * using DII mechanism.
     */
    public abstract String[] contexts();

    /**
     * This method returns the list of operation_context for the DII operation. 
     * It raises NO_RESOURCES exception if the operation is not invoked by 
     * using DII mechanism.
     */
    public abstract String[] operation_context();

    /**
     * This method returns the result from the invoked DII operation.
     * It raises NO_RESOURCES exception if the operation is not invoked by 
     * using DII mechanism.
     */
    public abstract Any result();

    /**
     * Implementation for response_expected() differs for client and server
     * implementations.
     *
     * Indicates whether a response is expected.  On the client, a reply is
     * not returned when response_expected is false, so receive_reply cannot
     * be called.  receive_other is called unless an exception occurs, in
     * which case receive_exception is called.  On the client, within
     * send_poll, this attribute is true.
     */
    public abstract boolean response_expected();

    /**
     * Defined in the Messaging specification.  Pertinent only when 
     * response_expected is false.  If response_expected is true, the value 
     * of sync_scope is undefined.  It defines how far the request shall 
     * progress before control is returned to the client.  This attribute may 
     * have one of the follwing values:
     * <ul>
     *   <li>Messaging::SYNC_NONE</li>
     *   <li>Messaging::SYNC_WITH_TRANSPORT</li>
     *   <li>Messaging::SYNC_WITH_SERVER</li>
     *   <li>Messaging::SYNC_WITH_TARGET</li>
     * </ul>
     */
    public short sync_scope() {
        checkAccess(MID_SYNC_SCOPE);
        return SYNC_WITH_TRANSPORT.value;
    }

    /**
     * Describes the state of the result of the operation invocation.  Its 
     * value can be one of the following:
     * <ul>
     *   <li>PortableInterceptor::SUCCESSFUL</li>
     *   <li>PortableInterceptor::SYSTEM_EXCEPTION</li>
     *   <li>PortableInterceptor::USER_EXCEPTION</li>
     *   <li>PortableInterceptor::LOCATION_FORWARD</li>
     *   <li>PortableInterceptor::TRANSPORT_RETRY</li>
     * </ul>
     */
    public short reply_status() {
        checkAccess(MID_REPLY_STATUS);
        return replyStatus;
    }

    /**
     * Implementation for forward_reference() differs for client and server
     * implementations.
     *
     * If the reply_status attribute is LOCATION_FORWARD
     * then this attribute will contain the object
     * to which the request will be forwarded.  It is indeterminate whether a
     * forwarded request will actually occur.
     */
    public abstract Object forward_reference();

    /**
     * Returns the data from the given slot of the PortableInterceptor::Current
     * that is in the scope of the request.
     * <p>
     * If the given slot has not been set, then an any containing a type code 
     * with a TCKind value of tk_null is returned.
     * <p>
     * If the ID does not define an allocated slot, InvalidSlot is raised.
     */
    public Any get_slot(int id) throws InvalidSlot {
        return slotTable.get_slot(id);
    }

    /**
     * Implementation for get_request_service_context() differs for client 
     * and server implementations.
     *
     * This operation returns a copy of the service context with the given ID
     * that is associated with the request.  If the request's service context
     * does not contain an etry for that ID, BAD_PARAM with a minor code of
     * TBD_BP is raised.
     */
    public abstract org.omg.IOP.ServiceContext get_request_service_context(int id);

    /**
     * Implementation for get_reply_service_context() differs for client 
     * and server implementations.
     *
     * This operation returns a copy of the service context with the given ID
     * that is associated with the reply.  IF the request's service context
     * does not contain an entry for that ID, BAD_PARAM with a minor code of
     * TBD_BP is raised.
     */
    public abstract org.omg.IOP.ServiceContext get_reply_service_context(int id);

    /**
     * @return The connection on which the request is made.
     *
     * Note: we store the connection as an internal type but
     * expose it here as an external type.
     */
    public com.sun.corba.se.spi.legacy.connection.Connection connection() {
        return connection;
    }

    /**
     * Inserts the UserException inside the given ApplicationException
     * into the given Any.  Throws an UNKNOWN with minor code
     * OMGSYstemException.UNKNOWN_USER_EXCEPTION if the Helper class could not be 
     * found to insert it with.
     */
    private void insertApplicationException(ApplicationException appException, Any result) throws UNKNOWN {
        try {
            RepositoryId repId = RepositoryId.cache.getId(appException.getId());
            String className = repId.getClassName();
            String helperClassName = className + "Helper";
            Class helperClass = ORBClassLoader.loadClass(helperClassName);
            Class[] readParams = new Class[1];
            readParams[0] = org.omg.CORBA.portable.InputStream.class;
            Method readMethod = helperClass.getMethod("read", readParams);
            InputStream ueInputStream = appException.getInputStream();
            ueInputStream.mark(0);
            UserException userException = null;
            try {
                java.lang.Object[] readArguments = new java.lang.Object[1];
                readArguments[0] = ueInputStream;
                userException = (UserException) readMethod.invoke(null, readArguments);
            } finally {
                try {
                    ueInputStream.reset();
                } catch (IOException e) {
                    throw wrapper.markAndResetFailed(e);
                }
            }
            insertUserException(userException, result);
        } catch (ClassNotFoundException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        } catch (NoSuchMethodException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        } catch (SecurityException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        } catch (IllegalAccessException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        } catch (IllegalArgumentException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        } catch (InvocationTargetException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        }
    }

    /**
     * Inserts the UserException into the given Any.  
     * Throws an UNKNOWN with minor code
     * OMGSYstemException.UNKNOWN_USER_EXCEPTION if the Helper class could not be 
     * found to insert it with.
     */
    private void insertUserException(UserException userException, Any result) throws UNKNOWN {
        try {
            if (userException != null) {
                Class exceptionClass = userException.getClass();
                String className = exceptionClass.getName();
                String helperClassName = className + "Helper";
                Class helperClass = ORBClassLoader.loadClass(helperClassName);
                Class[] insertMethodParams = new Class[2];
                insertMethodParams[0] = org.omg.CORBA.Any.class;
                insertMethodParams[1] = exceptionClass;
                Method insertMethod = helperClass.getMethod("insert", insertMethodParams);
                java.lang.Object[] insertMethodArguments = new java.lang.Object[2];
                insertMethodArguments[0] = result;
                insertMethodArguments[1] = userException;
                insertMethod.invoke(null, insertMethodArguments);
            }
        } catch (ClassNotFoundException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        } catch (NoSuchMethodException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        } catch (SecurityException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        } catch (IllegalAccessException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        } catch (IllegalArgumentException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        } catch (InvocationTargetException e) {
            throw stdWrapper.unknownUserException(CompletionStatus.COMPLETED_MAYBE, e);
        }
    }

    /**
     * Internal utility method to convert an NVList into a PI Parameter[]
     */
    protected Parameter[] nvListToParameterArray(NVList parNVList) {
        int count = parNVList.count();
        Parameter[] plist = new Parameter[count];
        try {
            for (int i = 0; i < count; i++) {
                Parameter p = new Parameter();
                plist[i] = p;
                NamedValue nv = parNVList.item(i);
                plist[i].argument = nv.value();
                plist[i].mode = ParameterMode.from_int(nv.flags() - 1);
            }
        } catch (Exception e) {
            throw wrapper.exceptionInArguments(e);
        }
        return plist;
    }

    /**
     * Utility to wrap the given Exception in an Any object and return it.
     * If the exception is a UserException which cannot be inserted into 
     * an any, then this returns an Any containing the system exception 
     * UNKNOWN.
     */
    protected Any exceptionToAny(Exception exception) {
        Any result = myORB.create_any();
        if (exception == null) {
            throw wrapper.exceptionWasNull2();
        } else if (exception instanceof SystemException) {
            ORBUtility.insertSystemException((SystemException) exception, result);
        } else if (exception instanceof ApplicationException) {
            try {
                ApplicationException appException = (ApplicationException) exception;
                insertApplicationException(appException, result);
            } catch (UNKNOWN e) {
                ORBUtility.insertSystemException(e, result);
            }
        } else if (exception instanceof UserException) {
            try {
                UserException userException = (UserException) exception;
                insertUserException(userException, result);
            } catch (UNKNOWN e) {
                ORBUtility.insertSystemException(e, result);
            }
        }
        return result;
    }

    /**
     * Utility method to look up a service context with the given id and
     * convert it to an IOP.ServiceContext.  Uses the given HashMap as
     * a cache.  If not found in cache, the result is inserted in the cache.
     */
    protected org.omg.IOP.ServiceContext getServiceContext(HashMap cachedServiceContexts, ServiceContexts serviceContexts, int id) {
        org.omg.IOP.ServiceContext result = null;
        Integer integerId = new Integer(id);
        result = (org.omg.IOP.ServiceContext) cachedServiceContexts.get(integerId);
        if (result == null) {
            com.sun.corba.se.spi.servicecontext.ServiceContext context = serviceContexts.get(id);
            if (context == null) throw stdWrapper.invalidServiceContextId();
            EncapsOutputStream out = new EncapsOutputStream(myORB);
            context.write(out, GIOPVersion.V1_2);
            InputStream inputStream = out.create_input_stream();
            result = ServiceContextHelper.read(inputStream);
            cachedServiceContexts.put(integerId, result);
        }
        return result;
    }

    /**
     * Utility method to add an IOP.ServiceContext to a core.ServiceContexts
     * object.  If replace is true, any service context with the given id
     * is replaced.  
     * <p>
     * Raises BAD_INV_ORDER if replace is false and a service context with
     * the given id already exists.
     * <p>
     * Uses the given HashMap as a cache.  If a service context is placed
     * in the container, it goes in the HashMap as well.  
     */
    protected void addServiceContext(HashMap cachedServiceContexts, ServiceContexts serviceContexts, org.omg.IOP.ServiceContext service_context, boolean replace) {
        int id = 0;
        EncapsOutputStream outputStream = new EncapsOutputStream(myORB);
        InputStream inputStream = null;
        UnknownServiceContext coreServiceContext = null;
        ServiceContextHelper.write(outputStream, service_context);
        inputStream = outputStream.create_input_stream();
        coreServiceContext = new UnknownServiceContext(inputStream.read_long(), (org.omg.CORBA_2_3.portable.InputStream) inputStream);
        id = coreServiceContext.getId();
        if (serviceContexts.get(id) != null) if (replace) serviceContexts.delete(id); else throw stdWrapper.serviceContextAddFailed(new Integer(id));
        serviceContexts.put(coreServiceContext);
        cachedServiceContexts.put(new Integer(id), service_context);
    }

    /**
     * Sets the number of interceptors whose starting interception
     * points were successfully invoked on this client call.  As specified
     * in orbos/99-12-02, section 5.2.1., not all interceptors will
     * be invoked if a ForwardRequest exception or a system exception
     * is raised.  This keeps track of how many were successfully executed
     * so we know not to execute the corresponding ending interception
     * points for the interceptors whose starting interception points
     * were not completed.  This simulates the "Flow Stack Visual Model"
     * presented in section 5.1.3.*/
    protected void setFlowStackIndex(int num) {
        this.flowStackIndex = num;
    }

    /**
     * Returns the number of interceptors whose starting interception
     * points were actually invoked on this client request.  See
     * setFlowStackIndex for more details.
     */
    protected int getFlowStackIndex() {
        return this.flowStackIndex;
    }

    /**
     * Sets which ending interception point should be called
     * for each interceptor in the virtual flow stack.
     */
    protected void setEndingPointCall(int call) {
        this.endingPointCall = call;
    }

    /**
     * Retrieves the current ending point call type (see
     * setEndingPointCall for more details).
     */
    protected int getEndingPointCall() {
        return this.endingPointCall;
    }

    /**
     * Sets which intermediate interception point should be called
     * for each interceptor in the virtual flow stack.
     */
    protected void setIntermediatePointCall(int call) {
        this.intermediatePointCall = call;
    }

    /**
     * Retrieves the current intermediate point call type (see
     * setEndingPointCall for more details).
     */
    protected int getIntermediatePointCall() {
        return this.intermediatePointCall;
    }

    /**
     * Sets which starting interception point should be called
     * for each interceptor in the virtual flow stack.
     */
    protected void setStartingPointCall(int call) {
        this.startingPointCall = call;
    }

    /**
     * Retrieves the current starting point call type (see
     * setStartingPointCall for more details).
     */
    protected int getStartingPointCall() {
        return this.startingPointCall;
    }

    /**
     * Returns true if all interceptors' starting and ending points
     * have already executed to completion, or false if not yet.
     */
    protected boolean getAlreadyExecuted() {
        return this.alreadyExecuted;
    }

    /**
     * Sets whether all interceotrs' starting and ending points
     * have already been executed to completion.
     */
    protected void setAlreadyExecuted(boolean alreadyExecuted) {
        this.alreadyExecuted = alreadyExecuted;
    }

    /**
     * Sets the value to be returned by reply_status
     */
    protected void setReplyStatus(short replyStatus) {
        this.replyStatus = replyStatus;
    }

    /**
     * Gets the current reply_status without doing an access check
     * (available only to package and subclasses)
     */
    protected short getReplyStatus() {
        return this.replyStatus;
    }

    /**
     * Stores the given ForwardRequest object for later analysis.
     * This version supplements setForwardRequest( IOR );
     */
    protected void setForwardRequest(ForwardRequest forwardRequest) {
        this.forwardRequest = forwardRequest;
        this.forwardRequestIOR = null;
    }

    /** 
     * Stores the given IOR for later forward request analysis.
     * This version supplements setForwardRequest( ForwardRequest );
     */
    protected void setForwardRequest(IOR ior) {
        this.forwardRequestIOR = ior;
        this.forwardRequest = null;
    }

    /**
     * Retrieves the ForwardRequest object as a ForwardRequest exception.
     */
    protected ForwardRequest getForwardRequestException() {
        if (this.forwardRequest == null) {
            if (this.forwardRequestIOR != null) {
                org.omg.CORBA.Object obj = iorToObject(this.forwardRequestIOR);
                this.forwardRequest = new ForwardRequest(obj);
            }
        }
        return this.forwardRequest;
    }

    /**
     * Retrieves the IOR of the ForwardRequest exception.
     */
    protected IOR getForwardRequestIOR() {
        if (this.forwardRequestIOR == null) {
            if (this.forwardRequest != null) {
                this.forwardRequestIOR = ORBUtility.getIOR(this.forwardRequest.forward);
            }
        }
        return this.forwardRequestIOR;
    }

    /**
     * Sets the exception to be returned by received_exception and
     * received_exception_id.
     */
    protected void setException(Exception exception) {
        this.exception = exception;
    }

    /**
     * Returns the exception to be returned by received_exception and
     * received_exception_id.
     */
    Exception getException() {
        return this.exception;
    }

    /**
     * Sets the execution point that we are currently executing
     * (starting points, intermediate points, or ending points).
     * This allows us to enforce the validity table.
     */
    protected void setCurrentExecutionPoint(int executionPoint) {
        this.currentExecutionPoint = executionPoint;
    }

    /**
     * Check whether the caller is allowed to access this method at
     * this particular time.  This is overridden in subclasses to implement
     * the validity table specified in ptc/00-04-05, table 21-1 and 21-2.
     * The currentExecutionPoint attribute is checked, and if access is
     * forbidden at this time, BAD_INV_ORDER is raised with a minor code of
     * TBD_BIO.
     *
     * @param methodID The ID of this method, one of the MID_* constants.
     *     This allows us to easily look up the method access in a table.
     *     Note that method ids may overlap between subclasses.
     */
    protected abstract void checkAccess(int methodID) throws BAD_INV_ORDER;

    /**
     * The server side does an explicit set rather than taking the
     * current PICurrent table as is done in the general RequestInfoImpl
     * constructor.
     */
    void setSlotTable(SlotTable slotTable) {
        this.slotTable = slotTable;
    }

    protected org.omg.CORBA.Object iorToObject(IOR ior) {
        return ORBUtility.makeObjectReference(ior);
    }
}
