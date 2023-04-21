package tcg.plan.step;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import tcg.plan.PlanStep;
import tcg.common.CorbaHelper;
import tcg.common.DatabaseManager;
import tcg.common.LoggerManager;
import tcg.scada.cos.CosBooleanSeqHolder;
import tcg.scada.cos.CosDpErrorEnum;
import tcg.scada.cos.CosDpErrorSeqHolder;
import tcg.scada.cos.CosDpQualityEnum;
import tcg.scada.cos.CosDpValueStruct;
import tcg.scada.cos.CosDpValueUnion;
import tcg.scada.cos.ICosDataPointServer;

public class StepSendControl extends PlanStep {

    private static String VERSION = "01.01 (20091112)";

    private static int RCC_CHECK_INTERVAL_MSEC = 1000;

    private String datapointName = "";

    private int datapointValue = 0;

    private String datapointType = "";

    private int locationId = 0;

    private long rccTimeoutSec = 0;

    private boolean isDigitalControl = false;

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }
        for (int i = 0; i < args.length; i++) {
            if (args[i].equalsIgnoreCase("--version") || args[i].equalsIgnoreCase("-V")) {
                printVersion();
                return;
            } else if (args[i].equalsIgnoreCase("--help") || args[i].equalsIgnoreCase("-h")) {
                printUsage();
                return;
            }
        }
        StepSendControl instance = new StepSendControl();
        instance.logger = LoggerManager.getLogger(instance.getClass().toString());
        instance.execute(args);
    }

    @Override
    protected boolean parseArgument2(String[] args) {
        CommandLineParser cmdLnParser = new BasicParser();
        CommandLine cmdLn = null;
        Options optArgs = new Options();
        Option cmdLineArg1 = new Option("dataPointName", "Data Point Name (required)");
        cmdLineArg1.setRequired(true);
        cmdLineArg1.setArgs(1);
        cmdLineArg1.setArgName("data-point-name");
        Option cmdLineArg5 = new Option("dataPointValue", "Data Point Value (required)");
        cmdLineArg5.setRequired(true);
        cmdLineArg5.setArgs(1);
        cmdLineArg5.setArgName("data-point-value");
        optArgs.addOption(cmdLineArg1);
        optArgs.addOption(cmdLineArg5);
        try {
            cmdLn = cmdLnParser.parse(optArgs, args);
        } catch (ParseException pe) {
            logger.error("Can not parse argument: " + pe.getLocalizedMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Parameters:", optArgs);
            return false;
        }
        datapointName = cmdLn.getOptionValue("dataPointName");
        try {
            datapointValue = Integer.parseInt(cmdLn.getOptionValue("dataPointValue"));
        } catch (NumberFormatException nfe) {
            logger.error("Invalid datapoint value: " + cmdLn.getOptionValue("dataPointValue") + ". We only support numeric control value.");
            return false;
        }
        return true;
    }

    @Override
    protected boolean init() {
        if (null == DatabaseManager.getInstance()) {
            logger.error("Can not initialize database manager!");
            return false;
        }
        Connection conn = DatabaseManager.getConnection();
        if (conn == null) {
            logger.error("Can not get connection to database!");
            return false;
        }
        Statement stmt = null;
        ResultSet rs = null;
        String localSQL = "";
        long keyId = 0;
        try {
            localSQL = "SELECT KEYID, LOCATION_ID, RETURN_CONDITION_TIMEOUT, DATA_PT_TYPE " + " FROM OPC_DT_PT " + " WHERE FEP_DP_NAME = '" + datapointName + "' ";
            logger.debug("Query: " + localSQL);
            stmt = conn.createStatement();
            rs = stmt.executeQuery(localSQL);
            if (rs.next()) {
                keyId = rs.getLong("KEYID");
                locationId = rs.getInt("LOCATION_ID");
                rccTimeoutSec = rs.getLong("RETURN_CONDITION_TIMEOUT");
                datapointType = rs.getString("DATA_PT_TYPE");
            }
            rs.close();
            stmt.close();
        } catch (SQLException sqle) {
            logger.error("Can not get datapoint location id. Exception: " + sqle.toString());
            try {
                rs.close();
                stmt.close();
            } catch (Exception ex) {
            }
            DatabaseManager.returnConnection(sqle.getErrorCode());
            return false;
        }
        if (datapointType.equalsIgnoreCase("bool")) {
            isDigitalControl = true;
        }
        if (!isDigitalControl) {
            try {
                localSQL = " SELECT ALARM_VALUE FROM ALARM_STATE_DEF " + "WHERE KEYID=" + keyId + " AND ALARM_VALUE=" + datapointValue;
                logger.debug("Query: " + localSQL);
                stmt = conn.createStatement();
                rs = stmt.executeQuery(localSQL);
                if (!rs.next()) {
                    logger.error("Invalid input value! " + "Datapoint: " + datapointName + ". " + "Value: " + datapointValue + ".");
                    throw new SQLException();
                }
                rs.close();
                stmt.close();
            } catch (SQLException sqle) {
                logger.error("Can not validate input value. Exception: " + sqle.toString());
                try {
                    rs.close();
                    stmt.close();
                } catch (Exception ex) {
                }
                DatabaseManager.returnConnection(sqle.getErrorCode());
                return false;
            }
        }
        DatabaseManager.returnConnection(0);
        if (null == CorbaHelper.getInstance()) {
            logger.error("Can not initialize corba connection!");
            return false;
        }
        return true;
    }

    @Override
    protected boolean close() {
        return true;
    }

    @Override
    protected boolean running() {
        ICosDataPointServer dpServer = CorbaHelper.getActiveDataPointServer(locationId);
        if (dpServer == null) {
            logger.error("Can not get datapoint server for location " + locationId);
            return false;
        }
        String[] items = new String[1];
        items[0] = datapointName.replace(".Value", "");
        CosDpValueStruct[] values = new CosDpValueStruct[1];
        values[0] = new CosDpValueStruct();
        values[0].quality = CosDpQualityEnum.QualityGood;
        values[0].timestamp = 0;
        values[0].value = new CosDpValueUnion();
        CosDpErrorSeqHolder errors = new CosDpErrorSeqHolder();
        if (isDigitalControl) {
            if (datapointValue == 0) {
                values[0].value.boolValue(false);
            } else {
                values[0].value.boolValue(true);
            }
        } else {
            values[0].value.longValue(datapointValue);
        }
        logger.info("Sending control. Datapoint: " + items[0] + ". Value: " + datapointValue);
        try {
            dpServer.cosSetControlItem2(items, values, errors);
            if (errors.value[0].value() != CosDpErrorEnum.ErrNoError.value()) {
                logger.error("Fail to send control. Error: " + CorbaHelper.DpErrorCodeToString(errors.value[0]));
                return false;
            }
        } catch (Exception ex) {
            logger.error("Fail to send control. Exception: " + ex.getMessage());
            return false;
        }
        if (rccTimeoutSec > 0) {
            long timeoutMSec = (Calendar.getInstance().getTimeInMillis()) + (rccTimeoutSec * 1000);
            CosBooleanSeqHolder status = new CosBooleanSeqHolder();
            boolean rccStatus = false;
            int errorCounter = 0;
            while (timeoutMSec > Calendar.getInstance().getTimeInMillis()) {
                try {
                    dpServer.cosGetItemReturnConditionStatus2(items, status, errors);
                    if (errors.value[0].value() != CosDpErrorEnum.ErrNoError.value()) {
                        logger.warn("Fail to get return condition status. Error: " + CorbaHelper.DpErrorCodeToString(errors.value[0]));
                    } else {
                        if (status.value[0]) {
                            rccStatus = true;
                            break;
                        }
                    }
                    errorCounter = 0;
                } catch (Exception ex) {
                    logger.warn("Fail to get return condition status. Exception: " + ex.getMessage());
                    errorCounter++;
                }
                if (errorCounter > ERROR_THRESHOLD) {
                    break;
                }
                try {
                    Thread.sleep(RCC_CHECK_INTERVAL_MSEC);
                } catch (Exception ex) {
                }
            }
            if (!rccStatus) {
                logger.error("Return condition check (RCC) failed.");
                return false;
            }
        }
        logger.info("Send control is successful.");
        return true;
    }

    @Override
    protected void terminate() {
    }

    private static void printVersion() {
        System.out.println("Plan Send Control Step Version " + VERSION);
    }

    private static void printUsage() {
        System.out.println("Plan Send Control Step Version " + VERSION);
        System.out.println("");
        System.out.println("Command Line Parameters:  ");
        System.out.println(" --plan-execution-id <plan-id>      Plan Execution ID");
        System.out.println(" --executed-node-id <node-id>       Executed Node ID");
        System.out.println(" --port-no <port-no>       			Plan Master Port No");
        System.out.println(" --agent-port-no <port-no>          Plan Agent Port No");
        System.out.println(" --data-point-name <datapoint>      Data Point Name");
        System.out.println(" --data-point-value <value>         Data Point Value");
        System.out.println(" -h | --help                        Print out this help");
        System.out.println(" -v | --version                     Print out program version");
        System.out.println("");
    }
}
