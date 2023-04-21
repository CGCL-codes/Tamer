package parexel.common;

import java.io.File;
import java.sql.*;
import java.text.*;
import java.util.*;

/**
 * Log java class implements general for all CxDD Data Integration Components logging process. General logging process writes information about all CxDD Data Integration events into:
 *- Oracle schema logging tables (see Physical Data Model in 2.4.1 section and CMDD schema in 3.4 section) using write_message(...) and write_state(...) store procedures in LOGGING package;
 *- logging text file for data utility;
 *- fixed e-mail boxes using SMTP protocol.
 * @version 1.0
 * @author Mirasoft, Alexander Malyarenko
*/
public class Log {

    /**Default date and time format in logging message*/
    private static final String DEFAULT_DATETIME_FORMAT = "dd-MM-yyyy:HH.mm.ss";

    /**Value for INFO status*/
    public static final String INFO = "INFO";

    /**Value for WARNING status*/
    public static final String WARNING = "WARNING";

    /**Value for ERROR status*/
    public static final String ERROR = "ERROR";

    /**Value for DEBUG status*/
    public static final String DEBUG = "DEBUG";

    /**Value for HIGH severity*/
    public static final String HIGH = "HIGH";

    /**Value for MIDLE severity*/
    public static final String MIDLE = "MIDDLE";

    /**Value for LOW severity*/
    public static final String LOW = "LOW";

    /**Logging message maximum length*/
    public static final int MAX_MESSAGE_LENGTH = 2000;

    /**Current utility or utility stage name*/
    private String sModule = "";

    /**Current utility or utility stage method name*/
    private String sModuleStep = "";

    /**Current logging process ID in T_CXDD_MODULE_STATES table*/
    private long lLogId = 0;

    /**Current connection object for Oracle schema*/
    private Connection con = null;

    /**Object for email sending with SMTP protocol using*/
    private SmtpEmail smtpEmail = null;

    /**Constructor for current class
	 * @param aModule Utility or utility stage name
	 * @param aModuleStep Utility or utility stage method name
	*/
    public Log(String aModule, String aModuleStep) {
        sModule = aModule;
        sModuleStep = aModuleStep;
    }

    /**Method to write logging message into text file*/
    public static void log(String sMessage) {
        System.out.println("[" + getCurrentTime(DEFAULT_DATETIME_FORMAT) + "] " + sMessage);
    }

    /**Method to get current date and time in indicated format*/
    private static String getCurrentTime(String sFormat) {
        DateFormat dateFormat = new SimpleDateFormat(sFormat);
        return dateFormat.format(Calendar.getInstance().getTime());
    }

    /**Method to write logging message into database, email box and text file depending of method parameters*/
    public void log(String aType, String aSeverity, String aMessage, String aTable, String aFileFormat, String aMailSubject) {
        StringBuffer sbLogMessage = new StringBuffer("");
        if (con != null && !aSeverity.equalsIgnoreCase(LOW)) {
            try {
                if (!con.isClosed()) {
                    CallableStatement cs = con.prepareCall("{call LOGGING.WRITE_MESSAGE(p_module =>?,p_modulestep =>?,p_type =>?,p_severity =>?,p_message =>?,p_table =>?,p_file_format =>?,p_state_id =>?)}");
                    cs.setString(1, sModule);
                    cs.setString(2, sModuleStep);
                    cs.setString(3, aType);
                    cs.setString(4, aSeverity);
                    if (aMessage != null && aMessage.length() > MAX_MESSAGE_LENGTH) cs.setString(5, aMessage.substring(MAX_MESSAGE_LENGTH)); else cs.setString(5, aMessage);
                    cs.setString(6, aTable);
                    cs.setString(7, aFileFormat);
                    cs.setLong(8, lLogId);
                    cs.registerOutParameter(8, Types.NUMERIC);
                    cs.execute();
                    lLogId = cs.getLong(8);
                    cs.close();
                }
            } catch (SQLException ex) {
                sbLogMessage.append(getCurrentTime(DEFAULT_DATETIME_FORMAT)).append("\t").append(ERROR).append("\tLogging\tSQLException:").append(ex.getMessage()).append("\n");
            }
        }
        sbLogMessage.append(getCurrentTime(DEFAULT_DATETIME_FORMAT)).append("\t").append(aType).append("\t").append(sModule).append(":").append(sModuleStep).append("\t").append(aMessage);
        if (smtpEmail != null && aSeverity.equalsIgnoreCase(HIGH)) {
            smtpEmail.sendEmail(aMessage, aMailSubject, this);
        }
        System.out.println(sbLogMessage.substring(0));
    }

    /**Method to write logging message into database, email box and text file - short form*/
    public void log(String aType, String aMessage) {
        if (aType.equals(ERROR)) log(aType, MIDLE, aMessage, "", "", ""); else log(aType, LOW, aMessage, "", "", "");
    }

    /**Method to write module state in general logging process using LOGGING.WRITE_STATE store procedure*/
    public void setConnection(Connection acon, boolean aValid, ImportResult importResult) {
        con = acon;
        int iValid = 0;
        if (con != null) {
            try {
                if (!con.isClosed()) {
                    CallableStatement cs = con.prepareCall("{call LOGGING.WRITE_STATE(p_module =>?, p_valid =>?, p_id =>?, p_good_records=>?, p_bad_records=>?)}");
                    cs.setString(1, sModule);
                    if (lLogId > 0 && aValid) iValid = 1;
                    cs.setInt(2, iValid);
                    cs.setLong(3, lLogId);
                    if (importResult == null) {
                        cs.setLong(4, 0);
                        cs.setLong(5, 0);
                    } else {
                        cs.setLong(4, importResult.getTrue());
                        cs.setLong(5, importResult.getFalse());
                    }
                    cs.registerOutParameter(3, Types.NUMERIC);
                    cs.execute();
                    lLogId = cs.getLong(3);
                    cs.close();
                }
            } catch (SQLException ex) {
                log(ERROR, "SQLException:" + ex.getMessage());
            }
        }
    }

    /**Method to indicate SmtpEmail object for general logging process*/
    public void setSmtpEmail(SmtpEmail aSmtpEmail) {
        smtpEmail = aSmtpEmail;
    }

    /**Method to set current utility or utility stage method name for general logging process*/
    public void setModuleStep(String aModuleStep) {
        sModuleStep = aModuleStep;
    }

    /**Method to set current utility or utility stage name for general logging process*/
    public void setModule(String aModule) {
        sModule = aModule;
    }

    /**Method to define input configuration files full path names*/
    public boolean startProcess(String[] arg, Map<String, String> amPropFile) {
        boolean result = true;
        if (arg.length > 0 && arg[0].indexOf("?") > 0) {
            printUsage();
            result = false;
        } else {
            log(Log.INFO, "Job started...");
            String sCurrentDir = new File(".").getAbsoluteFile().getAbsolutePath();
            switch(arg.length) {
                case 0:
                    amPropFile.put(Config.FILES_CFG, sCurrentDir + File.separator + Config.FILES_CFG);
                    amPropFile.put(Config.EMAIL_CFG, sCurrentDir + File.separator + Config.EMAIL_CFG);
                    amPropFile.put(Config.ERRORS_CFG, sCurrentDir + File.separator + Config.ERRORS_CFG);
                    amPropFile.put(Config.GENERAL_CFG, sCurrentDir + File.separator + Config.GENERAL_CFG);
                    amPropFile.put(Config.DB_CFG, sCurrentDir + File.separator + Config.DB_CFG);
                    break;
                case 1:
                    amPropFile.put(Config.FILES_CFG, arg[0]);
                    amPropFile.put(Config.EMAIL_CFG, sCurrentDir + File.separator + Config.EMAIL_CFG);
                    amPropFile.put(Config.ERRORS_CFG, sCurrentDir + File.separator + Config.ERRORS_CFG);
                    amPropFile.put(Config.GENERAL_CFG, sCurrentDir + File.separator + Config.GENERAL_CFG);
                    amPropFile.put(Config.DB_CFG, sCurrentDir + File.separator + Config.DB_CFG);
                    break;
                case 2:
                    amPropFile.put(Config.FILES_CFG, arg[0]);
                    amPropFile.put(Config.EMAIL_CFG, arg[1]);
                    amPropFile.put(Config.ERRORS_CFG, sCurrentDir + File.separator + Config.ERRORS_CFG);
                    amPropFile.put(Config.GENERAL_CFG, sCurrentDir + File.separator + Config.GENERAL_CFG);
                    amPropFile.put(Config.DB_CFG, sCurrentDir + File.separator + Config.DB_CFG);
                    break;
                case 3:
                    amPropFile.put(Config.FILES_CFG, arg[0]);
                    amPropFile.put(Config.EMAIL_CFG, arg[1]);
                    amPropFile.put(Config.ERRORS_CFG, arg[2]);
                    amPropFile.put(Config.GENERAL_CFG, sCurrentDir + File.separator + Config.GENERAL_CFG);
                    amPropFile.put(Config.DB_CFG, sCurrentDir + File.separator + Config.DB_CFG);
                    break;
                case 4:
                    amPropFile.put(Config.FILES_CFG, arg[0]);
                    amPropFile.put(Config.EMAIL_CFG, arg[1]);
                    amPropFile.put(Config.ERRORS_CFG, arg[2]);
                    amPropFile.put(Config.GENERAL_CFG, arg[3]);
                    amPropFile.put(Config.DB_CFG, sCurrentDir + File.separator + Config.DB_CFG);
                    break;
                case 5:
                    amPropFile.put(Config.FILES_CFG, arg[0]);
                    amPropFile.put(Config.EMAIL_CFG, arg[1]);
                    amPropFile.put(Config.ERRORS_CFG, arg[2]);
                    amPropFile.put(Config.GENERAL_CFG, arg[3]);
                    amPropFile.put(Config.DB_CFG, arg[4]);
                    break;
            }
        }
        return result;
    }

    /**Method to write into logging process finish message about utility working*/
    public void finishProcess(boolean aResult, ImportResult importResult, ParameterParser pParser) {
        int recordsCount = 0;
        if (aResult) {
            recordsCount = (importResult == null ? 0 : importResult.getTrue());
            if (pParser != null) {
                int i = pParser.getTaskNumber();
                StringBuffer sbTemp = new StringBuffer(pParser.store.sMailProblemDescription[i].substring(0, pParser.store.sMailProblemDescription[i].indexOf("&1")));
                sbTemp.append(recordsCount).append(pParser.store.sMailProblemDescription[i].substring(pParser.store.sMailProblemDescription[i].indexOf("&1") + 2));
                log(Log.INFO, pParser.store.sMailSeverity[i], sbTemp.substring(0), "", "", pParser.store.sMailSubject[i]);
            } else {
                log(Log.INFO, "Job finished!" + (recordsCount > 0 ? " Records imported - " + recordsCount + "." : ""));
            }
            setConnection(con, true, importResult);
        } else {
            recordsCount = (importResult == null ? 0 : importResult.getFalse());
            log(Log.INFO, "Unsuccessfully finished!" + (recordsCount > 0 ? " Bad records count - " + recordsCount + "." : ""));
        }
        try {
            if (con != null && !con.isClosed()) con.close();
        } catch (SQLException ex) {
            Log.log("SQLException: " + ex.getMessage());
        }
    }

    /**Method to print help message*/
    private void printUsage() {
        System.out.println("Usage: java " + sModule + " [FILES_CFG [EMAIL_CFG [ERRORS_CFG [GENERAL_CFG [DB_CFG]]]]]");
        System.out.println("where FILES_CFG - full path and name of the files configuration file");
        System.out.println("      EMAIL_CFG - full path and name of the email configuration file");
        System.out.println("      ERRORS_CFG - full path and name of the errors configuration file");
        System.out.println("      GENERAL_CFG - full path and name of the general configuration file");
        System.out.println("      DB_CFG - full path and name of the db configuration file");
    }
}
