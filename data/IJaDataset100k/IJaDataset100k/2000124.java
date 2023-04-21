package com.jcorporate.expresso.services.dbobj;

import com.jcorporate.expresso.core.controller.ControllerRequest;
import com.jcorporate.expresso.core.db.DBException;
import com.jcorporate.expresso.core.dbobj.SecuredDBObject;

/**
 * UserGroup is a grouping of a number of users for security purposes
 *
 * @author Mike Dubman
 * @version $Revision: 3 $  $Date: 2006-03-01 06:17:08 -0500 (Wed, 01 Mar 2006) $
 */
public class JobHandlerRegistry extends SecuredDBObject {

    private static final String thisClass = JobHandlerRegistry.class.getName();

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_OSNAME = "OSName";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_LOAD_AVERAGE = "LoadAverage";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_CURR_JOBNUM = "CurrentJobs";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_TIMESTAMP = "TimStamp";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_HOSTNAME = "HostName";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_STATUS = "Status";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_SERVERID = "ServerID";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_FREEMEM = "FreeMem";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_TOTALMEM = "TotalMem";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_USEDMEM = "UsedMem";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_POWERFACTOR = "PowerFactor";

    /**
      * DOCUMENT ME!
      */
    public static final String FLD_OPERATION = "ControlCommand";

    /**
     * DOCUMENT ME!
     *
     * @throws DBException upon initialization error
     *
     * @see com.jcorporate.expresso.core.dbobj.SecuredDBObject
     */
    public JobHandlerRegistry() throws DBException {
        super();
    }

    /**
     * For using DBObjects within Controllers.  Initializes based upon the
     * current user and the requested db. [Of course this can be modified
     * later]
     *
     * @param request - The controller request handed to you by the framework.
     *
     * @throws DBException upon initialization error
     */
    public JobHandlerRegistry(ControllerRequest request) throws DBException {
        super(request);
    }

    /**
     * DOCUMENT ME!
     *
     * @param uid The Expresso user id
     *
     * @throws DBException upon initialization error
     */
    public JobHandlerRegistry(int uid) throws DBException {
        super(uid);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws DBException
     *
     * @see com.jcorporate.expresso.core.dbobj.SecuredDBObject#setupFields
     */
    protected synchronized void setupFields() throws DBException {
        setTargetTable("JOBHANDLERREGISTRY");
        setDescription("DBjobHandlerRegistry");
        addField(FLD_LOAD_AVERAGE, "int", 0, true, "jhLoadAvg");
        addField(FLD_CURR_JOBNUM, "int", 0, true, "nofJobs");
        addField(FLD_TIMESTAMP, "datetime", 0, true, "timeStamp");
        addField(FLD_STATUS, "varchar", 255, true, "jobHandlerStatus");
        addField(FLD_POWERFACTOR, "double", 0, true, "powerFactor");
        addField(FLD_TOTALMEM, "int", 0, true, "totalMemory");
        addField(FLD_FREEMEM, "int", 0, true, "freeMemory");
        addField(FLD_USEDMEM, "int", 0, true, "memoryInUse");
        addField(FLD_SERVERID, "int", 0, false, "handlingServer");
        addField(FLD_HOSTNAME, "varchar", 160, false, "jobHandlerName");
        addField(FLD_OSNAME, "varchar", 255, false, "jobHandlerOSName");
        addField(FLD_OPERATION, "varchar", 255, true, "jobHandlerControlCommand");
        addKey(FLD_SERVERID);
        addKey(FLD_HOSTNAME);
    }
}
