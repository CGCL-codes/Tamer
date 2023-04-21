package net.sourceforge.jsjavacomm.scorm;

import net.sourceforge.jsjavacomm.JSObjectWrapper;

/**
 * A wrapper around the SCORM runtime API.
 * 
 * @author <a href="mailto:mail@daniel.may.name">Daniel J. R. May</a>
 * @version 0.1, 13 Feb 2009
 * @see <a href="http://www.adlnet.gov/downloads/DownloadPage.aspx?ID=237">
 *      Sharable Content Object Reference Model (SCORM) 2004 3rd Edition
 *      Documentation Suite</a>
 */
public interface JSScormAPI extends JSObjectWrapper {

    /**
	 * The standard name for the SCORM API JavaScript Object in SCORM 2004.
	 */
    public static final String SCORM_2004_OBJECT_NAME = "API_1484_11";

    /**
	 * The standard name for the SCORM API JavaScript Object in SCORM 1.2.
	 */
    public static final String SCORM_1_2_OBJECT_NAME = "API";

    /**
	 * If, during the execution of an API function, no errors are encountered,
	 * the LMS shall set the API Instance’s error code to 0. This indicates that
	 * the API function invocation encountered no errors while processing the
	 * request. If after an API function call, the error code is 0, one can
	 * assume the following based on the actual function called:
	 * <ul>
	 * <li>initialize(): The API Instance has successfully performed the
	 * appropriate LMS specific communication session initialization procedures.
	 * The communication session has been established and the API Instance is
	 * ready for other function calls. The conceptual communication state of the
	 * API Instance is now "Running".</li>
	 * <li>getValue(): The requested data model element’s value is returned. The
	 * value from the request shall be considered reliable and accurate
	 * according to the LMS. The conceptual communication state has not changed.
	 * </li>
	 * <li>setValue(): The value passed in was successfully set by the LMS. A
	 * request to getValue() for the data model element used in the setValue()
	 * shall return the value that was stored by the LMS. The conceptual
	 * communication state has not changed.</li>
	 * <li>commit(): Any values that were set (using the setValue() method call)
	 * since initialize() or the last commit() method call, have been
	 * successfully forwarded to the persistent data store. This method
	 * guarantees that the data will be available during subsequent learner
	 * sessions within the same learner attempt with the SCO. The conceptual
	 * communication state has not changed.</li>
	 * <li>getLastError(), getErrorString() and getDiagnostic(): These API
	 * methods do not affect or alter the error code for the API Instance.</li>
	 * <li>terminate(): The API Instance has successfully performed the
	 * appropriate LMS specific communication session termination procedures.
	 * The communication session has ended. The conceptual communication state
	 * of the API Instance is now "Terminated".</li>
	 * </ul>
	 */
    public static final int NO_ERROR = 0;

    /**
	 * The General Exception error condition indicates that an exception
	 * occurred and no other specific error code exists. The API Instance shall
	 * use the General Exception error code in scenarios where a more specific
	 * error code is not defined.
	 */
    public static final int GENERAL_EXCEPTION = 101;

    /**
	 * The General Initialization Failure error condition indicates that a
	 * failure occurred while attempting to initialize the communication
	 * session. The General Initialization Failure error code shall be used by
	 * an API Instance when the communication session initialization process
	 * fails while the conceptual communication state is "Not Initialized" and
	 * no other specific error code exists. The API Instance shall set the error
	 * code to 102 and return "false" to the SCO. The conceptual communication
	 * state shall remain unchanged ("Not Initialized").
	 */
    public static final int GENERAL_INITIALIZATION_FAILURE = 102;

    /**
	 * The Already Initialized error condition indicates that the SCO attempted
	 * to initialize the communication session after the communication session
	 * has already been initialized successfully. The Already Initialized error
	 * code shall be used by an API Instance when the SCO attempts to initialize
	 * the communication session ( initialize() ) more than once during that
	 * communication session. This error code shall be used when the conceptual
	 * communication state is "Running" and the request is made to initialize
	 * the communication session. In this scenario, the API Instance shall set
	 * the error code to 103 and return a "false" to the SCO. The conceptual
	 * communication state shall remain unchanged ("Running").
	 */
    public static final int ALREADY_INITIALIZED = 103;

    /**
	 * The Content Instance Terminated error condition indicates that the
	 * communication session has already terminated. This error condition occurs
	 * when a SCO attempts to invoke the initialize() method after a
	 * successful call to the terminate() method has occurred. This error code
	 * shall be used when the conceptual communication state is "Terminated" and
	 * the request to initialize the communication session occurs. In this
	 * scenario, the API Instance shall set the error code to 104 and return a
	 * "false" to the SCO. The conceptual communication state shall remain
	 * unchanged ("Terminated").
	 */
    public static final int CONTENT_INSTANCE_TERMINATED = 104;

    /**
	 * The General Termination Failure error condition indicates a failure
	 * occurred while attempting to terminate the communication session. The
	 * General Termination Failure error code shall be used by an API Instance
	 * when the communication session termination process fails while the
	 * conceptual communication state is "Running" and no other error
	 * information is available (i.e., a more specific communication session
	 * termination error condition). The API Instance shall set the error code
	 * to 111 and return "false" to the SCO. The conceptual communication state
	 * shall remain unchanged ("Running").
	 */
    public static final int GENERAL_TERMINATION_FAILURE = 111;

    /**
	 * The Termination Before Initialization error condition indicates that the
	 * SCO attempted to terminate the communication session before the
	 * communication session was ever initialized (conceptual communication
	 * state is "Not Initialized"). The Termination Before Initialization error
	 * code shall be used by an API Instance when the SCO tries to invoke
	 * terminate() prior to a successful call to initialize(). The API
	 * Instance shall set the error code to 112 and return "false" to the SCO.
	 * The conceptual communication state shall remain unchanged ("Not
	 * Initialized").
	 */
    public static final int TERMINATION_BEFORE_INITIALIZATION = 112;

    /**
	 * The Termination After Termination error condition indicates that the SCO
	 * attempted to terminate the communication session after the communication
	 * session has already been terminated successfully. The Termination After
	 * Termination error code shall be used by an API Instance when the SCO has
	 * invoked the terminate() method after a previous terminate()
	 * method has already been processed successfully. The API Instance shall
	 * set the error code to 113 and return "false" to the SCO. The conceptual
	 * communication state shall remain unchanged ("Terminated").
	 */
    public static final int TERMINATION_AFTER_TERMINATION = 113;

    /**
	 * The Retrieve Data Before Initialization error condition indicates that
	 * the SCO attempted to retrieve data prior to a successful communication
	 * session initialization. The Retrieve Data Before Initialization error
	 * code shall be used by an API Instance when the SCO attempts to invoke the
	 * GetValue() method prior to a successful call to the initialize()
	 * method. The API Instance shall set the error code to 122 and return an
	 * empty string. The conceptual communication state shall
	 * remain unchanged ("Not Initialized").
	 */
    public static final int RETRIEVE_DATA_BEFORE_INITIALIZATION = 122;

    /**
	 * The Retrieve Data After Termination error condition indicates that the
	 * SCO attempted to retrieve data after the communication session has
	 * successfully terminated. The Retrieve Data After Termination error code
	 * shall be used by an API Instance when the SCO attempts to invoke the
	 * GetValue() method after a successful call to the terminate() method.
	 * The API Instance shall set the error code to 123 and return an empty
	 * string (). The conceptual communication state shall remain
	 * unchanged ("Terminated").
	 */
    public static final int RETRIEVE_DATA_AFTER_TERMINATION = 123;

    /**
	 * The Store Data Before Initialization error condition indicates that the
	 * SCO attempted to store data prior to a successful communication session
	 * initialization. The Store Data Before Initialization error code shall be
	 * used by an API Instance when the SCO attempts to invoke the SetValue()
	 * method prior to a successful call to the initialize() method. The API
	 * Instance shall set the error code to 132 and return "false". The
	 * conceptual communication state shall remain unchanged ("Not
	 * Initialized").
	 */
    public static final int STORE_DATA_BEFORE_INITIALIZATION = 132;

    /**
	 * The Store Data After Termination error condition indicates that the SCO
	 * attempted to store data after the communication session has successfully
	 * terminated. The Store Data After Termination error code shall be used by
	 * an API Instance when the SCO attempts to invoke the SetValue() method
	 * after a successful call to the terminate() method. The API Instance
	 * shall set the error code to 133 and return "false". The conceptual
	 * communication state shall remain unchanged ("Terminated").
	 */
    public static final int STORE_DATA_AFTER_TERMINATION = 133;

    /**
	 * The Commit Before Initialization error condition indicates that the SCO
	 * attempted to commit data to persistent storage prior to a successful
	 * communication session initialization. The Commit Before Initialization
	 * error code shall be used by an API Instance when the SCO attempts to
	 * invoke the commit() method prior to a successful call to the
	 * initialize() method. The API Instance shall set the error code to 142
	 * and return "false". The conceptual communication state shall remain
	 * unchanged ("Not Initialized").
	 */
    public static final int COMMIT_BEFORE_INITIALIZATION = 142;

    /**
	 * The Commit After Termination error condition indicates that the SCO
	 * attempted to commit data to persistent storage after the communication
	 * session has successfully terminated. The Commit After Termination error
	 * code shall be used by an API Instance when the SCO attempts to invoke the
	 * commit() method after a successful call to the terminate() method.
	 * The API Instance shall set the error code to 143 and return "false". The
	 * conceptual communication state shall remain unchanged ("Terminated").
	 */
    public static final int COMMIT_AFTER_TERMINATION = 143;

    /**
	 * The General Argument Error error condition indicates that an attempt was
	 * made to pass an invalid argument to one of the API functions, and no
	 * other defined error condition can be used to describe the error. Data
	 * Model errors should be used to specify a more specific error condition ,
	 * if one occurs. If no other error code can be used to describe the error
	 * condition, the API Instance should use error code 201. One scenario where
	 * this error code shall be used occurs when parameters are passed to the
	 * following API calls: • initialize() • terminate() • commit() All
	 * three of these API calls have a restriction that an empty string
	 * parameter is passed to it. If a SCO passes any other argument to these
	 * function calls the API Instance shall return "false" and set the error
	 * code to 201. The conceptual communication state shall remain unchanged.
	 */
    public static final int GENERAL_ARGUMENT_ERROR = 201;

    /**
	 * The General Get Failure error condition indicates a general get failure
	 * has occurred and no other information on the error is available (more
	 * specific error code). This error condition acts as a catch all condition
	 * for processing a GetValue() request. The General Get Failure error code
	 * shall be used by an API Instance when a retrieve data event (GetValue())
	 * error has occurred and the API Instance cannot determine a more specific
	 * error condition to report (more specific error code). The API Instance
	 * shall set the error code to 301 and return an empty string ().
	 * This error condition can only happen if the conceptual communication
	 * state is "Running". If this error condition is encountered, the
	 * conceptual communication state shall remain unchanged ("Running").
	 */
    public static final int GENERAL_GET_FAILURE = 301;

    /**
	 * The General Set Failure error condition indicates a general set failure
	 * has occurred and no other information on the error is available (more
	 * specific error code). This error condition acts as a catch-all condition
	 * for processing a SetValue() request. The General Set Failure error code
	 * shall be used by an API Instance when a store data event (SetValue())
	 * error has occurred and the API Instance cannot determine a more specific
	 * error condition to report (more specific error code). The API Instance
	 * shall set the error code to 351 and return "false". This error condition
	 * can only happen if the conceptual communication state is "Running". If
	 * this error condition is encountered, the conceptual communication state
	 * shall remain unchanged ("Running").
	 */
    public static final int GENERAL_SET_FAILURE = 351;

    /**
	 * The General Commit Failure error condition indicates a general commit
	 * failure has occurred and no other information on the error is available
	 * (more specific error code). This error condition acts as a catch all
	 * condition. The General Commit Failure error code shall be used by an API
	 * Instance when a commit data event (commit()) error has occurred and the
	 * API Instance cannot determine a more specific error condition to report
	 * (more specific error code). The API Instance shall set the error code to
	 * 391 and return "false". This error condition can only happen if the
	 * conceptual communication state is "Running". If this error condition is
	 * encountered, the conceptual communication state shall remain unchanged
	 * ("Running").
	 */
    public static final int GENERAL_COMMIT_FAILURE = 391;

    /**
	 * The Undefined Data Model Element error condition indicates that: • The
	 * data model element passed as the parameter in the GetValue(parameter) is
	 * undefined and not recognized by the API Instance. This condition
	 * indicates that an attempt was made to use a data model element that is
	 * not recognized by the API Instance. • The data model element passed as
	 * parameter_1 in the SetValue(parameter_1, parameter_2) is undefined and
	 * not recognized by the API Instance. This condition indicates that an
	 * attempt was made to use a data model element that is not recognized by
	 * the API Instance. An unrecognized or undefined data model element is any
	 * element that is not formally defined by the SCORM Run-Time Environment
	 * Data Model or an implementation- defined extension element that is not
	 * recognized by the API Instance. The Undefined Data Model Element error
	 * code shall be used by an API Instance when one of the two scenarios
	 * described above is encountered. The API Instance shall: • For a
	 * GetValue() request: set the error code to 401 and return an empty
	 * string. This error condition can only happen if the
	 * conceptual communication state is "Running". If this error condition is
	 * encountered, the conceptual communication state shall remain unchanged
	 * ("Running"). • For a SetValue() request: set the error code to 401 and
	 * return "false". This error condition can only happen if the conceptual
	 * communication state is "Running". If this error condition is encountered,
	 * the conceptual communication state shall remain unchanged ("Running").
	 */
    public static final int UNDEFINED_DATA_MODEL_ELEMENT = 401;

    /**
	 * The Unimplemented Data Model Element error condition indicates that: •
	 * The data model element passed as parameter in the GetValue(parameter) is
	 * recognized by the API Instance but is not implemented. • The data model
	 * element passed as parameter_1 in the SetValue(parameter_1, parameter_2)
	 * is recognized by the API Instance but is not implemented. All of the
	 * SCORM Run-Time Environment Data Model elements are required to be
	 * implemented by an LMS. This error condition shall not occur when
	 * accessing SCORM Run-Time Environment Data Model elements, but may occur
	 * when accessing extension data model elements.
	 */
    public static final int UNIMPLEMENTED_DATA_MODEL_ELEMENT = 402;

    /**
	 * The Data Model Element Value Not Initialized error condition indicates
	 * that a SCO attempted to retrieve a data model value that has never been
	 * initialized. A data model element may be initialized in several manners:
	 * Time Environment Data Model, SCORM Navigation Data Model or any other
	 * data model used in a SCORM environment (extension data model). The Data
	 * Model Element Type Mismatch error code shall be used by an API Instance
	 * if the value passed as parameter_2 in a SetValue() does not evaluate to a
	 * valid type or defined format for the data model element indicated in
	 * parameter_1 of a SetValue(). The API Instance shall set the error code to
	 * 406 and return "false". This error condition can only happen if the
	 * conceptual communication state is "Running". If this error condition is
	 * encountered, the conceptual communication state shall remain unchanged
	 * ("Running").
	 */
    public static final int DATA_MODEL_ELEMENT_VALUE_NOT_INITIALIZED = 403;

    /**
	 * The Data Model Element Value Out Of Range error condition indicates that
	 * a SCO attempted to store a data model value for an element, however the
	 * value was not in the specified range of values for the element. This
	 * error condition may be encountered with the use of the SCORM Run-Time
	 * Environment Data Model, SCORM Navigation Data Model or any other data
	 * model used in a SCORM environment (extension data model). The Data Model
	 * Element Value Out Of Range error code shall be used by an API Instance if
	 * the value passed as parameter_2 in a SetValue() is out of range for the
	 * data model element indicated in parameter_1 of a SetValue(). The API
	 * Instance shall set the error code to 407 and return "false". This error
	 * condition can only happen if the conceptual communication state is
	 * "Running". If this error condition is encountered, the conceptual
	 * communication state shall remain unchanged ("Running").
	 */
    public static final int DATA_MODEL_ELEMENT_VALUE_OUT_OF_RANGE = 407;

    /**
	 *The Data Model Dependency Not Established error condition shall be used
	 * when relevant dependencies are not in place. A dependency represents one
	 * or more key values in a data model that shall have been set prior to
	 * other data model elements. The dependencies data model elements for the
	 * SCORM Run-Time Environment Data Model are described in Section 4: SCORM®
	 * Run-Time Environment Data Model. This error condition is described by the
	 * IEEE standard as being used for situations that may arise during a
	 * GetValue() or SetValue() request, however, SCORM does not define any
	 * situations for use of this error code during the processing of GetValue()
	 * requests. For SetValue() requests, some data model elements have
	 * requirements that certain other data model elements be set prior to other
	 * data model elements. By setting elements in a specific order, this
	 * maintains the integrity of a dependency being met. If one of these
	 * dependency requirements have not been established the LMS shall set the
	 * API Instance error code to 408 and return "false". This error condition
	 * can only happen if the conceptual communication state is "Running". If
	 * this error condition is encountered, the conceptual communication state
	 * shall remain unchanged ("Running").
	 */
    public static final int DATA_MODEL_DEPENDENCY_NOT_ESTABLISHED = 408;

    /**
	 * The function is used to initiate the communication session. It allows the
	 * LMS to handle LMS specific initialization issues.
	 * 
	 * @return <code>true</code> if communication session initialization, as
	 *         determined by the LMS, was successful. <code>false</code> if
	 *         session initialization, as determined by the LMS, was
	 *         unsuccessful. The API Instance shall set the error code to a
	 *         value specific to the error encountered. The SCO may call
	 *         {@link #getLastError() getLastError()} to determine the type of
	 *         error. More detailed information pertaining to the error may be
	 *         provided by the LMS through the {@link #getDiagnostic(String)
	 *         getDiagnostic()} function.
	 */
    boolean initialize();

    /**
	 * The function is used to terminate the communication session. It is used
	 * by the SCO when the SCO has determined that it no longer needs to
	 * communicate with the LMS. The {@link #terminate() terminate()} function
	 * also shall cause the persistence of any data (i.e., an implicit
	 * {@link #commit() commit()} call) set by the SCO since the last successful
	 * call to {@link #initialize() initialize()} or {@link #commit() commit()},
	 * whichever occurred most recently. This guarantees to the SCO that all
	 * data set by the SCO has been persisted by the LMS. Once the communication
	 * session has been successfully terminated, the SCO is only permitted to
	 * call the Support Methods.
	 * 
	 * @return <code>true</code> shall be returned if termination of the
	 *         communication session, as determined by the LMS, was successful.
	 *         <code>false</code> if termination of the communication session,
	 *         as determined by the LMS, was unsuccessful. The API instance
	 *         shall set the error code to a value specific to the error
	 *         encountered. The SCO may call {@link #getLastError()
	 *         getLastError()} to determine the type of error. More detailed
	 *         information pertaining to the error may be provided by the LMS
	 *         through the {@link #getDiagnostic(String) getDiagnostic()}
	 *         function.
	 */
    boolean terminate();

    /**
	 * Requests information from an LMS. It permits the SCO to request
	 * information from the LMS to determine among other things:
	 * <ul>
	 * <li>Values for data model elements supported by the LMS.</li>
	 * <li>Version of the data model supported by the LMS.</li>
	 * <li>Whether or not specific data model elements are supported.</li>
	 * </ul>
	 * 
	 * @param name
	 *            the complete identification of a data model element.
	 * @return the value associated with the parameter or if there is an error
	 *         an empty string. The SCO may call {@link #getLastError()
	 *         getLastError()} to determine the type of error. More detailed
	 *         information pertaining to the error may be provided by the LMS
	 *         through the {@link #getDiagnostic(String) getDiagnostic()}
	 *         function. The SCO should not rely on an empty string returned
	 *         from this function as being a valid value. The SCO should check
	 *         to see if the error code indicates that no error was encountered.
	 *         If this is the case, then the empty string is a valid value
	 *         returned from the LMS. If an error condition was encountered
	 *         during the processing of the request, then this would be
	 *         indicated by an appropriate error code.
	 */
    String getValue(final String name);

    /**
	 * Sets the value of the named parameter. This method allows the SCO to send
	 * information to the LMS for storage. The API Instance may be designed to
	 * immediately persist data that was set (to the server-side component) or
	 * store data in a local (client-side) cache.
	 * 
	 * @param name
	 *            the complete identification of a data model element to be set.
	 * @param value
	 *            the new value.
	 * @return
	 *         <code>true<code> if the LMS accepts the new value. <code>false</code>
	 *         shall be returned if the LMS encounters an error in setting the
	 *         new value. The SCO may call {@link #getLastError()
	 *         getLastError()} to determine the type of error. More detailed
	 *         information pertaining to the error may be provided by the LMS
	 *         through the {@link #getDiagnostic(String) getDiagnostic()}
	 *         function.
	 */
    boolean setValue(final String name, final String value);

    /**
	 * Requests forwarding to the persistent data store any data from the SCO
	 * that may have been cached by the API Instance since the last call to
	 * {@link #initialize() initialize()} or {@link #commit() commit()},
	 * whichever occurred most recently. The LMS would then set the error code
	 * to 0 (No Error encountered) and return <code>true</code>. If the API
	 * Instance does not cache values, {@link #commit() commit()} shall return
	 * <code>true</code> and set the error code to 0 (No Error encountered) and
	 * do no other processing. Cached data shall not be modified because of a
	 * call to the commit data method. For example, if the SCO sets the value of
	 * a data model element, then calls the commit data method, and then
	 * subsequently gets the value of the same data model element, the value
	 * returned shall be the value set in the call prior to invoking the commit
	 * data method. The {@link #commit() commit()} method can be used as a
	 * precautionary mechanism by the SCO. The method can be used to guarantee
	 * that data set by the setValue() is persisted to reduce the likelihood
	 * that data is lost because the communication session is interrupted, ends
	 * abnormally or otherwise terminates prematurely prior to a call to
	 * {@link #terminate() terminate()}.
	 * 
	 * @return <code>true</code> if the data was successfully persisted to a
	 *         long-term data store. <code>false</code> if the data was
	 *         unsuccessfully persisted to a long-term data store. The API
	 *         Instance shall set the error code to a value specific to the
	 *         error encountered. The SCO may call {@link #getLastError()
	 *         getLastError()} to determine the type of error. More detailed
	 *         information pertaining to the error may be provided by the LMS
	 *         through the {@link #getDiagnostic(String) getDiagnostic()}
	 *         function.
	 */
    boolean commit();

    /**
	 * Requests the error code for the current error state of the API instance.
	 * If a SCO calls this method, the API Instance shall not alter the current
	 * error state, but simply return the requested information. A best practice
	 * recommendation is to check to see if a Session Method or Data-transfer
	 * Method was successful. The {@link #getLastError() getLastError()} can be
	 * used to return the current error code. If an error was encountered during
	 * the processing of a function, the SCO may take appropriate steps to
	 * alleviate the problem.
	 * 
	 * @return the error code reflecting the current error state of the API
	 *         Instance. The error code is an integer in the range from 0 to
	 *         65536 inclusive) representing the error code of the last error
	 *         encountered.
	 */
    int getLastError();

    /**
	 * Retrieve a textual description of the current error state. The function
	 * is used by a SCO to request the textual description for the error code
	 * specified by the value of the parameter. This call has no effect on the
	 * current error state; it simply returns the requested information.
	 * 
	 * @param errorCode
	 *            the error code corresponding to an error message.
	 * @return a textual message containing a description of the error code
	 *         specified by the value of the parameter. The following
	 *         requirements shall be adhered to for all return values:
	 *         <ul>
	 *         <li>The return value shall be a string that has a maximum length
	 *         of 255 characters.</li>
	 *         <li>SCORM makes no requirement on what the text of the string
	 *         shall contain. The error codes themselves are explicitly and
	 *         exclusively defined. The textual description for the error code
	 *         is LMS specific.</li>
	 *         <li>If the requested error code is unknown by the LMS, an empty
	 *         string shall be returned. This is the only time that an empty
	 *         string shall be returned.</li>
	 *         </ul>
	 */
    String getErrorString(final int errorCode);

    /**
	 * Get diagnostic information from the LMS. This method allows the LMS to
	 * define additional diagnostic information through the API Instance. This
	 * call has no effect on the current error state; it simply returns the
	 * requested information.
	 * 
	 * @param an
	 *            implementer–specific value for diagnostics. The maximum length
	 *            of the parameter value shall be 255 characters. The value of
	 *            the parameter may be an error code, but is not limited to just
	 *            error codes.
	 * @return the diagnostic information. The maximum length of the string
	 *         returned shall be 255 characters. If the parameter is unknown by
	 *         the LMS, an empty string shall be returned. If the parameter
	 *         passed into the {@link #getDiagnostic(String) getDiagnostic()}
	 *         function is an empty string, then it is recommended that the
	 *         function return a string representing diagnostic information
	 *         about the last error encountered.
	 */
    String getDiagnostic(final String param);
}
