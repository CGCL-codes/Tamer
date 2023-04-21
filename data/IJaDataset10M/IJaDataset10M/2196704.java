package com.jcorporate.expresso.services.validation;

import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;
import com.jcorporate.expresso.core.job.Job;
import com.jcorporate.expresso.core.job.ServerException;
import com.jcorporate.expresso.core.misc.CookieBase64;
import com.jcorporate.expresso.core.misc.DateTime;
import com.jcorporate.expresso.core.misc.EMailSender;
import com.jcorporate.expresso.core.misc.StringUtil;
import com.jcorporate.expresso.core.security.CryptoManager;
import com.jcorporate.expresso.core.security.User;
import com.jcorporate.expresso.kernel.exception.ChainedException;
import com.jcorporate.expresso.kernel.util.ClassLocator;
import com.jcorporate.expresso.kernel.util.FastStringBuffer;
import com.jcorporate.expresso.services.dbobj.JobQueue;
import com.jcorporate.expresso.services.dbobj.JobQueueParam;
import com.jcorporate.expresso.services.dbobj.Setup;
import com.jcorporate.expresso.services.dbobj.ValidationQueue;
import com.jcorporate.expresso.services.dbobj.ValidationQueueParam;
import org.apache.log4j.Logger;
import si.cit.eprojekti.emailer.util.link.MailerFacade;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * This class abstracts the storing of the data that goes along with any
 * particular validation operation. This class also provides functions for a
 * validation request to be created in the first place. In this first
 * incarnation, this class stores all the data using DBObjects.  If warranted
 * at a later time, the ideal way to change the functionality by making this
 * class abstract and then implementing DBValidationEntry and
 * XMLValidationEntry or whatever. Most of the methods in this class are
 * "protected", so that minor changes can be handled by simply extending this
 * class and overridining the necessary methods.
 *
 * @author Shash Chatterjee
 * @version $Revision: 3 $  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 *
 * @since Expresso 4.0
 */
public class ValidationEntry implements Serializable {

    /**
     * Parameter for validator
     */
    public static final String PRM_VALIDATOR = "$$$Validator";

    /**
     * PArameter for 'Expires After'
     */
    public static final String PRM_EXPIRES_AFTER = "$$$ExpiresAfter";

    /**
     * Parameter for 'Validation Title'
     */
    public static final String PRM_VAL_TITLE = "$$$ValTitle";

    /**
     * Parameter for 'Validation Description'
     */
    public static final String PRM_VAL_DESC = "$$$ValDesc";

    /**
     * Parameter for 'Validation Server'
     */
    public static final String PRM_VAL_SERVER = "$$$ValDServer";

    /**
     * Parameter for 'Validation Port'
     */
    public static final String PRM_VAL_PORT = "$$$ValPort";

    /**
     * Parameter for 'Validation webapp context'
     */
    public static final String PRM_VAL_CTX = "$$$ValContext";

    /**
     * Status for new
     */
    public static final String NEW = "N";

    /**
     * Status for waiting
     */
    public static final String WAITING = "W";

    /**
     * Status for available
     */
    public static final String AVAILABLE = "A";

    /**
     * Status for Validated
     */
    public static final String VALIDATED = "V";

    /**
     * Status for Expired
     */
    public static final String EXPIRED = "E";

    /**
     * Session validation key
     */
    public static final String SESSION_KEY = "expresso.services.validation.ValidationEntry";

    /**
     * The log4j logger
     */
    private static Logger log = Logger.getLogger(ValidationEntry.class);

    /**
     * JobQueue
     */
    protected JobQueue jq = null;

    /**
     * JobQueue parameter data object
     */
    protected JobQueueParam jqp = null;

    /**
     * default data context
     */
    protected String dataContext = "default";

    /**
     * Time it expires after
     */
    protected String expiresAfter = "120:0:0";

    /**
     * The Job class name
     */
    protected String jobClassName = com.jcorporate.expresso.services.job.ValidationJob.class.getName();

    /**
     * The Job Number
     */
    protected String jobNumber = null;

    /**
     * The validation context path
     */
    protected String valContextPath = null;

    /**
     * The default description
     */
    protected String valDesc = "Validation Job";

    /**
     * The validation port
     */
    protected String valPort = null;

    /**
     * The validation server
     */
    protected String valServer = null;

    /**
     * The validation job title
     */
    protected String valTitle = "Validation Job";

    /**
     * ?
     */
    protected String valType = "default";

    /**
     * The validator class name
     */
    protected String validationClassName = null;

    /**
     * The validation queue
     */
    protected ValidationQueue vq = null;

    /**
     * current parameter number
     */
    protected int paramNum = 0;

    /**
     * This constructor is used by classes that submit a validation request
     * into the system. It creates a Job Queue Entry for the validation
     * request.  Application specific parameters are added using the
     * addParam(...) method, and the request finally committed using the
     * submit(...) method. Creation date: (9/23/2001 9:41:06 PM) Author: Shash
     * Chatterjee
     *
     * @param newDbName newDbName The database context to create the validation
     *        Job/Entry in.
     *
     * @throws AuthValidationException
     */
    public ValidationEntry(String newDbName) throws AuthValidationException {
        super();
        if ((newDbName == null) || (newDbName.equals(""))) {
            newDbName = "default";
        }
        dataContext = newDbName;
        try {
            int uid = User.getAdminId(newDbName);
            jq = new JobQueue();
            jq.setDataContext(dataContext);
            jq.setField(jq.FLD_UID, uid);
            jq.setField(jq.FLD_JOBCODE, jobClassName);
            jq.setField(jq.FLD_STATUS_CODE, "N");
            jq.add();
            jobNumber = jq.getField("JobNumber");
            jqp = new JobQueueParam();
            jqp.setDataContext(dataContext);
        } catch (DBException dbe) {
            throw new AuthValidationException("Database error creating job or parameters", dbe);
        }
        if (log.isDebugEnabled()) {
            log.debug("New entry started with job number " + jobNumber);
        }
    }

    /**
     * This constructor is used, after a validation request comes in, to
     * resurrect a validation entry from the DB. Creation date: (9/23/2001
     * 9:41:06 PM) Author: Shash Chatterjee
     *
     * @param dbName dbName The DB context to retrieve the validation entry
     *        from
     * @param id id The validation request id/seq. number
     *
     * @throws AuthValidationException AuthValidationException
     */
    public ValidationEntry(String dbName, String id) throws AuthValidationException {
        super();
        try {
            vq = new ValidationQueue(SecuredDBObject.SYSTEM_ACCOUNT);
            vq.setDataContext(dbName);
            vq.setField(vq.FLD_ID, id);
            if (!vq.find()) {
                throw new AuthValidationException("Validation queue id \"" + id + "\" not found in db \"" + dbName + "\"");
            }
        } catch (DBException dbe) {
            throw new AuthValidationException("DB error", dbe);
        }
    }

    /**
     * This constructor is used by the validation job to create a validation
     * entry from the job parameters. Application classes should have no need
     * to use this class/ Creation date: (9/23/2001 9:41:06 PM) Author: Shash
     * Chatterjee
     *
     * @param context context The DB context to use for the validation entry
     * @param paramsVector paramsVector All the params passed in to the job
     * @param jq jq The job queue entry in its entirety
     *
     * @throws AuthValidationException AuthValidationException
     */
    public ValidationEntry(String context, List paramsVector, JobQueue jq) throws AuthValidationException {
        super();
        String id = null;
        try {
            String expiresAfter = jq.getParamValue(ValidationEntry.PRM_EXPIRES_AFTER);
            Calendar now = Calendar.getInstance();
            StringTokenizer stk = new StringTokenizer(expiresAfter, ":");
            int hr = Integer.parseInt((String) stk.nextElement());
            int mn = Integer.parseInt((String) stk.nextElement());
            int sc = Integer.parseInt((String) stk.nextElement());
            now.add(Calendar.HOUR, hr);
            now.add(Calendar.MINUTE, mn);
            now.add(Calendar.SECOND, sc);
            valServer = jq.getParamValue(ValidationEntry.PRM_VAL_SERVER);
            valPort = jq.getParamValue(ValidationEntry.PRM_VAL_PORT);
            valContextPath = jq.getParamValue(ValidationEntry.PRM_VAL_CTX);
            vq = new ValidationQueue();
            vq.setDataContext(context);
            vq.setField(vq.FLD_STATUS_CODE, "N");
            vq.setField(vq.FLD_EXPIRES_AT, DateTime.getDateTimeForDB(now.getTime(), context));
            vq.setField(vq.FLD_VAL_CODE, createValidationCode());
            vq.setField(vq.FLD_VAL_HANDLER, jq.getParamValue(ValidationEntry.PRM_VALIDATOR));
            vq.setField(vq.FLD_PROCESSED_BY, "1");
            vq.add();
            id = vq.getField(vq.FLD_ID);
            int paramNum = 0;
            ValidationQueueParam vqp = new ValidationQueueParam();
            vqp.setDataContext(context);
            for (Iterator i = paramsVector.iterator(); i.hasNext(); ) {
                JobQueueParam jqp = (JobQueueParam) i.next();
                String key = jqp.getField("ParamCode");
                String val = jqp.getField("ParamValue");
                vqp.clear();
                vqp.setField(vqp.FLD_QUEUE_ID, id);
                vqp.setField(vqp.FLD_PARAM_NUM, Integer.toString(++paramNum));
                vqp.setField(vqp.FLD_PARAM_CODE, key);
                vqp.setField(vqp.FLD_PARAM_VAL, val);
                vqp.add();
            }
            vq.setField(vq.FLD_STATUS_CODE, "A");
            vq.update();
        } catch (DBException dbe) {
            throw new AuthValidationException("Database error storing validation entry", dbe);
        }
    }

    /**
     * This default constructor should never be used, and is marked private and
     * protected with an exception just for that reason. Creation date:
     * (9/23/2001 9:41:06 PM) Author: Shash Chatterjee
     *
     * @throws AuthValidationException
     */
    private ValidationEntry() throws AuthValidationException {
        throw new AuthValidationException("Please do not use the default constructor");
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     */
    public void setContextPath(String path) {
        valContextPath = path;
    }

    /**
     * Sets the description for the validation job Creation date: (9/23/2001
     * 9:41:06 PM) Author: Shash Chatterjee
     *
     * @param desc desc The description
     */
    public void setDesc(String desc) {
        valDesc = desc;
    }

    /**
     * Method to retrieve the absolute date/time when the request expires.
     * Creation date: (9/23/2001 9:41:06 PM) Author: Shash Chatterjee
     *
     * @return The expiry date/time
     *
     * @throws AuthValidationException
     */
    public Date getExpiresAt() throws AuthValidationException {
        if (vq == null) {
            throw new AuthValidationException("Use loadQueueEntry(...) method first");
        }
        try {
            Date ex = vq.getFieldDate(vq.FLD_EXPIRES_AT);
            return ex;
        } catch (DBException dbe) {
            throw new AuthValidationException("DB error", dbe);
        }
    }

    /**
     * Sets the name of the job class for validation requests. Currently the
     * only job that should be specified is ValidationJob Creation date:
     * (9/23/2001 9:41:06 PM) Author: Shash Chatterjee
     *
     * @param name name Class name of validation job
     *
     * @throws AuthValidationException AuthValidationException if the class
     *         isn't an Expresso Job class
     */
    public void setJobClassName(String name) throws AuthValidationException {
        try {
            Job.instantiate(name);
        } catch (ServerException se) {
            throw new AuthValidationException("Class \"" + name + "\" is not an Expresso Job", se);
        }
        jobClassName = name;
    }

    /**
     * Method to return all the application-specific parameters associated with
     * this validation request Creation date: (9/23/2001 9:41:06 PM) Author:
     * Shash Chatterjee
     *
     * @return Hashtable of all the parameters, keyed by parameter name
     *
     * @throws AuthValidationException
     */
    public Hashtable getParams() throws AuthValidationException {
        if (vq == null) {
            throw new AuthValidationException("Use loadQueueEntry(...) method first");
        }
        Hashtable params = new Hashtable(8);
        try {
            String id = vq.getField(vq.FLD_ID);
            ValidationQueueParam vqp = new ValidationQueueParam();
            vqp.setField(ValidationQueueParam.FLD_QUEUE_ID, id);
            vqp.setDataContext(dataContext);
            for (Iterator i = vqp.searchAndRetrieveList().iterator(); i.hasNext(); ) {
                ValidationQueueParam oneParam = (ValidationQueueParam) i.next();
                params.put(oneParam.getField(oneParam.FLD_PARAM_CODE), oneParam.getField(oneParam.FLD_PARAM_VAL));
            }
        } catch (DBException dbe) {
            throw new AuthValidationException("DB error", dbe);
        }
        return params;
    }

    /**
     * DOCUMENT ME!
     *
     * @param port DOCUMENT ME!
     */
    public void setPort(String port) {
        valPort = port;
    }

    /**
     * DOCUMENT ME!
     *
     * @param server DOCUMENT ME!
     */
    public void setServer(String server) {
        valServer = server;
    }

    /**
     * Sets the current status of the validation entry Creation date:
     * (9/23/2001 9:41:06 PM) Author: Shash Chatterjee
     *
     * @param newStatus newStatus = "A"=available, "N"=new, "W"=waiting,
     *        "V"=validated, "E"=expired
     *
     * @throws AuthValidationException AuthValidationException
     */
    public void setStatus(String newStatus) throws AuthValidationException {
        if (vq == null) {
            throw new AuthValidationException("Use loadQueueEntry(...) method first");
        }
        try {
            vq.setField(vq.FLD_STATUS_CODE, newStatus);
            vq.update();
        } catch (DBException dbe) {
            throw new AuthValidationException("DB error", dbe);
        }
    }

    /**
     * Returns the current status of the validation request Creation date:
     * (9/23/2001 9:41:06 PM) Author: Shash Chatterjee
     *
     * @return "N","A","V","E" - for "new", "available", "validated", "expired"
     *
     * @throws AuthValidationException AuthValidationException
     */
    public String getStatus() throws AuthValidationException {
        if (vq == null) {
            throw new AuthValidationException("Use loadQueueEntry(...) method first");
        }
        try {
            String status = vq.getField(vq.FLD_STATUS_CODE);
            return status;
        } catch (DBException dbe) {
            throw new AuthValidationException("DB error", dbe);
        }
    }

    /**
     * Set's the title of the validation entry job Creation date: (9/23/2001
     * 9:41:06 PM) Author: Shash Chatterjee
     *
     * @param title title The title
     */
    public void setTitle(String title) {
        valTitle = title;
    }

    /**
     * Method to set the app-specific validation handler associated with this
     * validation request Creation date: (9/23/2001 9:41:06 PM) Author: Shash
     * Chatterjee
     *
     * @param className className Class name of a class that implements
     *        ValidationHandler interface
     *
     * @throws AuthValidationException AuthValidationException if the handler
     *         is not of the correct class, or if the instantiation fails
     */
    public void setValidationHandler(String className) throws AuthValidationException {
        StringUtil.assertNotBlank(className, "ValidationHandler class name " + " may not be blank or null here");
        try {
            Class c = ClassLocator.loadClass(className);
            c.newInstance();
        } catch (ClassNotFoundException cn) {
            throw new AuthValidationException("ValidationHandler object '" + className + "' not found", cn);
        } catch (InstantiationException ie) {
            throw new AuthValidationException("ValidationHandler object '" + className + "' cannot be instantiated", ie);
        } catch (IllegalAccessException iae) {
            throw new AuthValidationException("Illegal access loading " + "ValidationHandler object '" + className + "'", iae);
        }
        validationClassName = className;
    }

    /**
     * Method to set the app-specific validation handler associated with this
     * validation request
     *
     * @param clazz Class that implements
     *        ValidationHandler interface
     *
     * @throws AuthValidationException AuthValidationException if the handler
     *         is not of the correct class, or if the instantiation fails
     */
    public void setValidationHandler(Class clazz) throws AuthValidationException {
        try {
            clazz.newInstance();
        } catch (IllegalAccessException ex) {
            throw new AuthValidationException("Illegal access loading " + "ValidationHandler object '" + clazz.getName() + "'", ex);
        } catch (InstantiationException ex) {
            throw new AuthValidationException("ValidationHandler object '" + clazz.getName() + "' cannot be instantiated", ex);
        }
        validationClassName = clazz.getName();
    }

    /**
     * Adds an application specific parameter into the validation entry
     * Creation date: (9/23/2001 9:41:06 PM) Author: Shash Chatterjee
     *
     * @param name name Name of the parameter
     * @param value value Value of the parameter
     *
     * @throws AuthValidationException
     */
    public void addParam(String name, String value) throws AuthValidationException {
        try {
            jqp.clear();
            jqp.setField("JobNumber", jobNumber);
            paramNum++;
            jqp.setField("ParamNumber", Integer.toString(paramNum));
            jqp.setField("ParamCode", name);
            jqp.setField("ParamValue", value);
            jqp.add();
        } catch (DBException dbe) {
            throw new AuthValidationException("Database error adding job parameter", dbe);
        }
        if (log.isDebugEnabled()) {
            log.debug("Job id= " + jobNumber + ": Adding param name=" + name + " value=" + value);
        }
    }

    /**
     * Function to match the code in the DB versus the code in the request.
     * Creation date: (9/23/2001 9:41:06 PM) Author: Shash Chatterjee
     *
     * @param code code Code in the request URL
     *
     * @return true if code matches, false otherwise
     *
     * @throws AuthValidationException AuthValidationException
     */
    public boolean codeMatches(String code) throws AuthValidationException {
        try {
            String storedCode = vq.getField(vq.FLD_VAL_CODE);
            if (storedCode.equals(code)) {
                return true;
            } else {
                return false;
            }
        } catch (DBException dbe) {
            throw new AuthValidationException("DB error", dbe);
        }
    }

    /**
     * Deletes the validation entry and all the connected parameters
     *
     * @throws AuthValidationException if no record is loaded OR there's an
     *         error deleting the records.
     */
    public void delete() throws AuthValidationException {
        if (vq == null) {
            throw new AuthValidationException("Use loadQueueEntry(...) method first");
        }
        try {
            vq.delete(true);
            vq = null;
        } catch (DBException dbe) {
            throw new AuthValidationException("DB error", dbe);
        }
    }

    /**
     * Method to set the time delta from current time when the request will
     * expire if unused Creation date: (9/23/2001 9:41:06 PM) Author: Shash
     * Chatterjee
     *
     * @param hr hr Delta hours
     * @param min min Delta minutes
     * @param sec sec Delta seconds
     */
    public void expiresAfter(int hr, int min, int sec) {
        FastStringBuffer fsb = FastStringBuffer.getInstance();
        try {
            fsb.append(hr);
            fsb.append(":");
            fsb.append(min);
            fsb.append(":");
            fsb.append(sec);
            expiresAfter = fsb.toString();
        } finally {
            fsb.release();
            fsb = null;
        }
    }

    /**
     * Utility function to instantiate the app-specific validation handler
     * Creation date: (9/23/2001 9:41:06 PM) Author: Shash Chatterjee
     *
     * @return Object of class ValidationHandler
     *
     * @throws AuthValidationException AuthValidationException
     */
    public ValidationHandler instantiateHandler() throws AuthValidationException {
        ValidationHandler vh = null;
        String className = null;
        try {
            className = vq.getField(vq.FLD_VAL_HANDLER);
        } catch (DBException dbe) {
            throw new AuthValidationException("DB error", dbe);
        }
        StringUtil.assertNotBlank(className, "ValidationHandler class name " + " may not be blank or null here");
        try {
            Class c = ClassLocator.loadClass(className);
            vh = (ValidationHandler) c.newInstance();
        } catch (ClassNotFoundException cn) {
            throw new AuthValidationException("ValidationHandler object '" + className + "' not found", cn);
        } catch (InstantiationException ie) {
            throw new AuthValidationException("ValidationHandler object '" + className + "' cannot be instantiated", ie);
        } catch (IllegalAccessException iae) {
            throw new AuthValidationException("llegal access loading " + "ValidationHandler object '" + className + "'", iae);
        }
        return vh;
    }

    /**
     * Insert the method's description here. Creation date: (9/23/2001 9:41:06
     * PM) Author: Shash Chatterjee
     *
     * @param dbName The data context to use
     * @param from the &quot;From&quot; email field.
     * @param addresses the email addresses to notify
     * @param subject the subject of the email
     * @param content The body of the email message
     *
     * @throws AuthValidationException
     */
    public static void notifyByEmail(String dbName, String from, Vector addresses, String subject, String content) throws AuthValidationException {
        EMailSender ems = new EMailSender();
        ems.setFromAddress(from);
        ems.setDBName(dbName);
        for (Enumeration e = addresses.elements(); e.hasMoreElements(); ) {
            String oneAddr = (String) e.nextElement();
            try {
                MailerFacade.sendMail(dbName, oneAddr, subject, content);
            } catch (Exception exc) {
                throw new AuthValidationException("notifyByEmail()" + ":Uncaught exception sending e-mail", exc);
            }
        }
    }

    /**
     * Insert the method's description here. Creation date: (9/23/2001 9:41:06
     * PM) Author: Shash Chatterjee
     *
     * @throws AuthValidationException upon error
     */
    public void submit() throws AuthValidationException {
        try {
            if ((valServer == null) || valServer.equals("")) {
                valServer = Setup.getValue(dataContext, "HTTPServ");
            }
            if ((valPort == null) || valPort.equals("")) {
                valPort = Setup.getValue(dataContext, "ServletPort");
            }
            if ((valContextPath == null) || valContextPath.equals("")) {
                valContextPath = Setup.getValue(dataContext, "ContextPath");
            }
            addParam(PRM_VALIDATOR, validationClassName);
            addParam(PRM_EXPIRES_AFTER, expiresAfter);
            addParam(PRM_VAL_TITLE, valTitle);
            addParam(PRM_VAL_DESC, valDesc);
            addParam(PRM_VAL_SERVER, valServer);
            addParam(PRM_VAL_PORT, valPort);
            addParam(PRM_VAL_CTX, valContextPath);
            jq.setField(jq.FLD_STATUS_CODE, AVAILABLE);
            jq.update();
        } catch (DBException dbe) {
            throw new AuthValidationException("Database error updating job status to active", dbe);
        }
    }

    /**
     * Utility function to create a URL based on the webapp context, and setup
     * values such as the http server/port etc.  It tags on the db context,
     * the validation entry id and the validation code as request parameters.
     * Creation date: (9/23/2001 9:41:06 PM) Author: Shash Chatterjee
     *
     * @return The URL for use by the validator to approve the validation
     *         request
     *
     * @throws AuthValidationException upon error
     */
    public String validationURL() throws AuthValidationException {
        FastStringBuffer authURL = FastStringBuffer.getInstance();
        String returnValue = null;
        try {
            try {
                String db = vq.getDataContext();
                authURL.append("http://");
                authURL.append(valServer);
                authURL.append(":" + valPort);
                authURL.append(valContextPath);
                authURL.append("/Validate.do?");
                authURL.append("ctx=" + db);
                authURL.append("&id=" + vq.getField(vq.FLD_ID));
                authURL.append("&code=" + vq.getField(vq.FLD_VAL_CODE));
            } catch (DBException dbe) {
                throw new AuthValidationException("DB error", dbe);
            }
            returnValue = authURL.toString();
        } finally {
            authURL.release();
        }
        return returnValue;
    }

    /**
     * Create a cryptographically sound validation code for use in the
     * validation operation [Currently generates a 256-bit random number]
     * Creation date: (9/23/2001 9:41:06 PM) Author: Shash Chatterjee,
     * strengthened by Mike Rimov
     *
     * @return  The validation code
     */
    protected static String createValidationCode() {
        final int length = 256 / 8;
        byte[] possibleNumbers;
        try {
            possibleNumbers = CryptoManager.getInstance().getRandomGenerator().getRandomBytes(length);
        } catch (ChainedException e) {
            possibleNumbers = new byte[length];
            Logger.getLogger("com.jcorporate.expresso.core.security.").error("Create Validation Code", e);
            for (int i = 0; i < length; i++) {
                possibleNumbers[i] = (byte) (Math.random() * 256);
            }
        }
        return CookieBase64.encodeNoPadding(possibleNumbers);
    }
}
