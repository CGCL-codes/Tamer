package org.dmonix.oracledocgen.xml.generators;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.dmonix.jdbc.LoggablePreparedStatement;
import org.dmonix.oracledocgen.xml.Classes;
import org.dmonix.oracledocgen.xml.ObjectDocGenerator;
import org.dmonix.xml.XMLElement;

/**
 * Creates documentation for Scheduler Jobs
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: dmonix.org</p>
 * @author Peter Nerg
 * @version 1.0
 */
public class SchedulerJobDocGenerator extends ObjectDocGenerator {

    public static final String PARAM_JOB_TYPE = "JOB_TYPE";

    public static final String PARAM_JOB_ACTION = "JOB_ACTION";

    public static final String PARAM_SCHEDULE_NAME = "SCHEDULE_NAME";

    public static final String PARAM_REPEAT_INTERVAL = "REPEAT_INTERVAL";

    public static final String PARAM_AUTO_DROP = "AUTO_DROP";

    public static final String PARAM_RESTARTABLE = "RESTARTABLE";

    public static final String PARAM_COMMENTS = "COMMENTS";

    private String jobName;

    public SchedulerJobDocGenerator(String schema, String jobName) throws Exception {
        super(schema);
        this.jobName = jobName;
        super.addHead("Scheduler Job " + jobName.toUpperCase(), "../../../oracledoc.css", "JOB");
        XMLElement table = super.addTable(bodyNode, Classes.CLASS_MAIN_TABLE);
        XMLElement tr = super.addTR(table);
        XMLElement td = super.addTD(tr, Classes.CLASS_DESC_TEXT);
        XMLElement p = super.addElement(td, "P", "Scheduler Job " + jobName.toUpperCase());
        super.setClass(p, Classes.CLASS_MAIN_TITLE);
        super.addBR(td);
        this.getJobInfo();
    }

    /**
   * <A NAME="user_info"/>
     <TABLE CLASS="SUB_TABLE">
     <TR>
     <TD CLASS="SUB_TITLE">User information</TD>
     </TR>
     </TABLE>
     <TABLE CLASS="SIMPLE_TABLE">
     <TR>
     <TD CLASS="LIST_ITEM" VALIGN="top">Default tablespace</TD>
     <TD CLASS="DESC_TEXT" VALIGN="top">MPCDATA</TD>
     </TR>
     <TR>
     <TD CLASS="LIST_ITEM" VALIGN="top">Temporary tablespace</TD>
     <TD CLASS="DESC_TEXT" VALIGN="top">TEMP</TD>
     </TR>
     </TABLE>

   * @throws SQLException
   */
    private void getJobInfo() throws SQLException {
        super.addSubTable("job_info", "Job Information");
        XMLElement table = super.addSimpleTable();
        LoggablePreparedStatement stmt = super.getStatement("select JOB_TYPE, JOB_ACTION, SCHEDULE_NAME, REPEAT_INTERVAL, AUTO_DROP, RESTARTABLE, COMMENTS\n" + "from dba_scheduler_jobs\n" + "where OWNER = :owner\n" + "and JOB_NAME = :job");
        stmt.setString(1, super.userName);
        stmt.setString(2, jobName);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            XMLElement tr1 = super.addTR(table);
            super.addSimpleTableColumn(tr1, "Job type", true);
            super.addSimpleTableColumn(tr1, rs.getString("JOB_TYPE"));
            XMLElement tr2 = super.addTR(table);
            super.addSimpleTableColumn(tr2, "Schedule", true);
            super.addSimpleTableColumn(tr2, rs.getString("SCHEDULE_NAME"));
            XMLElement tr3 = super.addTR(table);
            super.addSimpleTableColumn(tr3, "Repeat interval", true);
            super.addSimpleTableColumn(tr3, rs.getString("REPEAT_INTERVAL"));
            XMLElement tr4 = super.addTR(table);
            super.addSimpleTableColumn(tr4, "Auto drop", true);
            super.addSimpleTableColumn(tr4, rs.getString("AUTO_DROP"));
            XMLElement tr5 = super.addTR(table);
            super.addSimpleTableColumn(tr5, "Restartable", true);
            super.addSimpleTableColumn(tr5, rs.getString("RESTARTABLE"));
            XMLElement tr6 = super.addTR(table);
            super.addSimpleTableColumn(tr6, "Comments", true);
            super.addSimpleTableColumn(tr6, rs.getString("COMMENTS"));
            XMLElement tr7 = super.addTR(table);
            super.addSimpleTableColumn(tr7, "Job action", true);
            super.addSimplePreTableColumn(tr7, rs.getString("JOB_ACTION"));
            super.setParameter(PARAM_JOB_TYPE, rs.getString("JOB_TYPE"));
            super.setParameter(PARAM_JOB_ACTION, rs.getString("JOB_ACTION"));
            super.setParameter(PARAM_SCHEDULE_NAME, rs.getString("SCHEDULE_NAME"));
            super.setParameter(PARAM_REPEAT_INTERVAL, rs.getString("REPEAT_INTERVAL"));
            super.setParameter(PARAM_AUTO_DROP, rs.getString("AUTO_DROP"));
            super.setParameter(PARAM_RESTARTABLE, rs.getString("RESTARTABLE"));
            super.setParameter(PARAM_COMMENTS, rs.getString("COMMENTS"));
        }
    }
}
