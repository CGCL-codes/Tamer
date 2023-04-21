package org.jecars.client;

/** JC_Defs
 *
 * @version $Id: JC_Defs.java,v 1.10 2008/10/21 10:13:05 weertj Exp $
 */
public class JC_Defs {

    public static final String EXPIRE_DATE = "jecars:ExpireDate";

    public static final String OUTPUTTYPE_ATOM = "atom";

    public static final String OUTPUTTYPE_HTML = "html";

    public static final String OUTPUTTYPE_PROPERTIES = "properties";

    public static final String OUTPUTTYPE_BACKUP = "backup";

    public static final String OUTPUTASATOM = "alt=" + OUTPUTTYPE_ATOM;

    public static final String OUTPUTASHTML = "alt=" + OUTPUTTYPE_HTML;

    public static final String OUTPUTASPROPERTIES = "alt=" + OUTPUTTYPE_PROPERTIES;

    public static final String OUTPUTASBACKUP = "alt=" + OUTPUTTYPE_BACKUP;

    public static final String ATOM_TITLE = "title";

    public static final String ATOM_CATEGORY = "category";

    public static final String UNSTRUCT_NODETYPE = "jecars:unstruct_nodetype";

    public static final String PARAM_GETALLPROPS = "getAllProperties";

    public static final String FILTER_EVENTTYPES = "FET";

    public static final String BACKUP_MIMETYPE = "text/jecars-backup";

    /**** JeCARS Tools
  
      [jecars:Tool] > jecars:dataresource, mix:referenceable
    - jecars:AutoStart      (Boolean)
    - jecars:ToolTemplate   (Path)
    - jecars:ToolClass      (String)
    - jecars:StateRequest   (String) < '(start|suspend|resume|stop)'
    - jecars:State          (String)='open.notrunning' mandatory autocreated
    - jecars:PercCompleted  (Double)='0'
    + *                     (jecars:parameterresource) multiple
    + *                     (jecars:inputresource)     multiple
    + *                     (jecars:outputresource)    multiple
    + jecars:Parameter      (jecars:dataresource) multiple
    + jecars:Input          (jecars:dataresource) multiple
    + jecars:Output         (jecars:dataresource) multiple
   */
    public static final String STATEREQUEST = "jecars:StateRequest";

    public static final String STATEREQUEST_START = "start";

    public static final String STATEREQUEST_ABORT = "abort";

    public static final String STATEREQUEST_PAUSE = "pause";

    public static final String STATEREQUEST_STOP = "stop";

    public static final String STATE_NONE = "none";

    public static final String STATE_UNKNOWN = "unknown";

    public static final String STATE_OPEN = "open.";

    public static final String STATE_OPEN_NOTRUNNING = "open.notrunning";

    public static final String STATE_OPEN_NOTRUNNING_SUSPENDED = "open.notrunning.suspended";

    public static final String STATE_OPEN_RUNNING_INIT = "open.running.init";

    public static final String STATE_OPEN_RUNNING_INPUT = "open.running.input";

    public static final String STATE_OPEN_RUNNING_PARAMETERS = "open.running.parameters";

    public static final String STATE_OPEN_RUNNING_OUTPUT = "open.running.output";

    public static final String STATE_OPEN_RUNNING = "open.running";

    public static final String STATE_PAUSED = ".paused";

    public static final String STATE_OPEN_ABORTING = "open.aborting";

    public static final String STATE_CLOSED = "closed.";

    public static final String STATE_CLOSED_COMPLETED = "closed.completed";

    public static final String STATE_CLOSED_ABNORMALCOMPLETED = "closed.abnormalCompleted";

    public static final String STATE_CLOSED_ABNORMALCOMPLETED_TERMINATED = "closed.abnormalCompleted.terminated";

    public static final String STATE_CLOSED_ABNORMALCOMPLETED_ABORTED = "closed.abnormalCompleted.aborted";

    public static final String TRUE = "true";

    public static final String FALSE = "false";

    /** Value indicating that a numeric field is not set.
   */
    public static final int UNDEFINED = -999999;
}
