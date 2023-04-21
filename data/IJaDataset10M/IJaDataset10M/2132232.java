package com.versant.core.common;

import com.versant.core.jdo.*;
import java.util.NoSuchElementException;
import java.lang.reflect.InvocationTargetException;
import javax.jdo.*;

/**
	The purpose of this class is to encapsulate the Exception throwing.<br/>
	This is necessary to allow different exception classes for JDO, EJB and other api's <br/>
	Make sure, that all exceptions are thrown via this facility!
	(Exceptions include classes, that are JDO-only, like the JDO enhancer.)<br/><br/>
	This class is intended to be stateless.<br/><br/>
	With JDO an instance of BindingSupportImpl is directly used.
	
	Replacement pattern (incomplete, if not found in list: look here in source code):
		JDOUserException						invalidOperation
		JDOFatalUserException					runtime
		JDOFatalInternalException				internal
		JDOUnsupportedOptionException			unsupported
		JDOException							exception
		JDOFatalException						fatal
		JDODataStore							datastore
		JDOFatalDataStore						fatalDatastore
		
		IllegalArgumentException				illegalArgument
		NoSuchElementException					noSuchElement
		IndexOutOfBoundsException				indexOutOfBounds
		UnsupportedOperation					unsupportedOperation
		
		VersantObjectNotFoundException			objectNotFound
		VersantConcurrentUpdateException		concurrentUpdate
		VersantDetachedFieldAccessException	fieldDetached
		NotImplementedException					notImplemented
		VersantAuthenticationException			authentication
		
	List of exceptions from the exceptions:
		(this list should include exceptions, where BindingSupportImpl
		 is not necessary, because they are never user-visible, but
		 always caught internally)
		TargetLostException (?)
		...
		
	List of packages, where using BindingSupportImpl is not required:
		com.versant.core.jdo.tools.enhancer
		com.versant.core.jdo.jca
		com.versant.core.jdo.junit
		com.versant.core.jdo.licensegenerator
		com.versant.core.jdo.mbean
		com.versant.core.jdo.tools.workbench
	
	@author Christian Romberg
*/
public class BindingSupportImpl {

    private static BindingSupportImpl theInstance = new BindingSupportImpl();

    public static BindingSupportImpl getInstance() {
        return theInstance;
    }

    private BindingSupportImpl() {
    }

    public RuntimeException noSuchElement(String msg) {
        return new NoSuchElementException(msg);
    }

    public RuntimeException illegalArgument(String msg) {
        return new IllegalArgumentException(msg);
    }

    public RuntimeException security(String msg) {
        return new SecurityException(msg);
    }

    public RuntimeException runtime(String msg) {
        return new JDOFatalUserException(msg);
    }

    public RuntimeException license(String msg, Throwable t) {
        return runtime(msg);
    }

    /**
     * Recursively extract the real cause of e if it is wrapping other
     * exceptions.
     */
    public Throwable findCause(Throwable e) {
        if (e instanceof InvocationTargetException) {
            Throwable te = ((InvocationTargetException) e).getTargetException();
            if (te != null) return findCause(te);
        }
        return e;
    }

    public RuntimeException runtime(String msg, Throwable e) {
        return new JDOFatalUserException(msg, findCause(e));
    }

    public RuntimeException unsupported(String m) {
        return new JDOUnsupportedOptionException(m);
    }

    public RuntimeException unsupported() {
        return new JDOUnsupportedOptionException();
    }

    public RuntimeException unsupportedOperation(String msg) {
        return new UnsupportedOperationException(msg);
    }

    public RuntimeException indexOutOfBounds(String msg) {
        return new IndexOutOfBoundsException(msg);
    }

    public RuntimeException arrayIndexOutOfBounds(String msg) {
        return new ArrayIndexOutOfBoundsException(msg);
    }

    public RuntimeException arrayIndexOutOfBounds(int val) {
        return new ArrayIndexOutOfBoundsException(val);
    }

    public RuntimeException internal(String msg, Throwable cause) {
        return new JDOFatalInternalException(msg, findCause(cause));
    }

    public RuntimeException internal(String msg, Throwable[] cause) {
        return new JDOFatalInternalException(msg, cause);
    }

    public RuntimeException internal(String msg) {
        return new JDOFatalInternalException(msg);
    }

    public RuntimeException objectNotFound(String msg) {
        return new JDOObjectNotFoundException(msg);
    }

    public RuntimeException exception(String msg) {
        return new JDOException(msg);
    }

    public RuntimeException exception(String msg, Throwable nested) {
        return new JDOException(msg, findCause(nested));
    }

    public RuntimeException exception(String msg, Throwable[] nested) {
        return new JDOException(msg, nested);
    }

    public RuntimeException exception(String msg, Throwable nested, Object failed) {
        return new JDOException(msg, findCause(nested), failed);
    }

    public RuntimeException duplicateKey(String msg, Throwable nested, Object failed) {
        return new VersantDuplicateKeyException(msg, findCause(nested));
    }

    public RuntimeException lockNotGranted(String msg, Throwable[] nested, Object failed) {
        if (nested == null) return new VersantLockTimeoutException(msg);
        return new VersantLockTimeoutException(msg, nested);
    }

    public RuntimeException fatal(String msg) {
        return new JDOFatalException(msg);
    }

    public RuntimeException fatal(String msg, Throwable t) {
        return new JDOFatalException(msg, findCause(t));
    }

    public RuntimeException concurrentUpdate(String msg, Object failed) {
        return new JDOOptimisticVerificationException(msg);
    }

    public RuntimeException optimisticVerification(String msg, Object failed) {
        return new JDOOptimisticVerificationException(msg);
    }

    public RuntimeException fieldDetached() {
        return new VersantDetachedFieldAccessException();
    }

    public RuntimeException datastore(String msg, Throwable t) {
        return new JDODataStoreException(msg, findCause(t));
    }

    public RuntimeException datastore(String msg) {
        return new JDODataStoreException(msg);
    }

    public RuntimeException fatalDatastore(String msg, Throwable t) {
        return new JDOFatalDataStoreException(msg, findCause(t));
    }

    public RuntimeException fatalDatastore(String msg) {
        return new JDOFatalDataStoreException(msg);
    }

    public RuntimeException poolFull(String msg) {
        return new VersantConnectionPoolFullException(msg);
    }

    public RuntimeException nullElement(String msg) {
        return new VersantNullElementException(msg);
    }

    public RuntimeException invalidObjectId(String msg, Throwable cause) {
        return new JDOUserException(msg, findCause(cause));
    }

    public RuntimeException query(String msg, Throwable cause) {
        return new JDOUserException(msg, findCause(cause));
    }

    public RuntimeException query(String msg) {
        return new JDOUserException(msg, (Throwable) null);
    }

    public boolean isOwnQueryException(Throwable e) {
        return e instanceof JDOUserException;
    }

    public boolean isOwnInvalidObjectIdException(Throwable e) {
        return e instanceof JDOUserException;
    }

    public boolean isOwnException(Throwable e) {
        return e instanceof JDOException;
    }

    public boolean isOwnFatalUserException(Throwable e) {
        return e instanceof JDOFatalUserException;
    }

    public boolean isOwnFatalException(Throwable e) {
        return e instanceof JDOFatalException;
    }

    public boolean isOwnInternalException(Throwable t) {
        return t instanceof JDOFatalInternalException;
    }

    public boolean isOwnDatastoreException(Throwable t) {
        return t instanceof JDODataStoreException;
    }

    public boolean isOwnFatalDatastoreException(Throwable t) {
        return t instanceof JDOFatalDataStoreException;
    }

    public boolean isOwnConcurrentUpdateException(Throwable t) {
        return t instanceof JDOOptimisticVerificationException;
    }

    public boolean isOwnObjectNotFoundException(Throwable t) {
        return t instanceof JDOObjectNotFoundException;
    }

    public boolean isOwnUnsupportedException(Throwable t) {
        return t instanceof JDOUnsupportedOptionException;
    }

    public boolean isOutOfMemoryError(Throwable t) {
        return t instanceof OutOfMemoryError;
    }

    public boolean isError(Throwable t) {
        return t instanceof Error;
    }

    public Object getFailedObject(Exception e) {
        return ((JDOException) e).getFailedObject();
    }

    public RuntimeException invalidOperation(String msg, Throwable cause) {
        return new JDOUserException(msg, findCause(cause));
    }

    public RuntimeException objectNotEnhanced(Object o) {
        String cl = (o == null ? "Class of passed object" : "Class " + o.getClass().getName());
        String msg = cl + " is not declared persistent or not enhanced.";
        return new JDOFatalUserException(msg);
    }

    public RuntimeException transactionConflict(Object obj) {
        return transactionConflict("Object conflicts with other PersistenceManager", obj);
    }

    public RuntimeException transactionConflict(String msg, Object obj) {
        return new JDOUserException(msg, obj);
    }

    public RuntimeException notImplemented(String m) {
        return new NotImplementedException(m);
    }

    public RuntimeException invalidOperation(String msg) {
        return new JDOUserException(msg);
    }
}
