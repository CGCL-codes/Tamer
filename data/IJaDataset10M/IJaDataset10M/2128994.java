package com.dcivision.workflow.core;

import com.dcivision.framework.ApplicationException;

/**
 * <p>Class Name:       ContentStoreException.java    </p>
 * <p>Description:      The exception class will be throwed when content store consists of exception.</p>
 * @author              Angus Shiu
 * @company             DCIVision Limited
 * @creation date       02/12/2003
 * @version             $Revision: 1.3 $
 */
public class ContentStoreException extends ApplicationException {

    public static final String REVISION = "$Revision: 1.3 $";

    /**  
   * Constructor
   */
    public ContentStoreException() {
        super();
    }

    /**
   * Constructor
   * @param msg The user's message
   */
    public ContentStoreException(String msg) {
        super(msg);
    }

    /**
   * Constructor
   * @param e The user's exception
   */
    public ContentStoreException(Exception e) {
        super(e);
    }

    /**
   * Constructor
   * @param msg The user's message
   * @param e The user's exception
   */
    public ContentStoreException(String msg, Exception e) {
        super(msg, e);
    }
}
