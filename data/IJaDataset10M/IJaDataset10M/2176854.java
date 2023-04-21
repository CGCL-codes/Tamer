package com.aqua.syslog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.TimeZone;
import jsystem.extensions.analyzers.text.GetSpaceCounter;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import com.aqua.sysobj.conn.CliApplication;
import com.aqua.sysobj.conn.ConnectivityManager;

public class Syslog extends CliApplication {

    public ConnectivityManager conn;

    /**
	 * the syslog log file name (with full path)
	 */
    private String logFile;

    /**
	 * ftp server ip
	 */
    private String ftpServer;

    /**
	 * ftp - login user name
	 */
    private String ftpUsername;

    /**
	 * ftp - login password
	 */
    private String ftpPassword;

    /**
	 * it use this script file to start/stop/retart the syslog
	 */
    private String syslogScriptFile;

    /**
	 * if it's true, don't show the commands on the html report
	 */
    private boolean commandSilent = true;

    private FTPClient ftp = null;

    /**
	 * should be the file /etc/syslog.conf this file should has the line :
	 * �local0.* /usr/Cooter/tmp/setup_H_syslog.log�
	 */
    private String syslogConfFile1 = null;

    /**
	 * should be the file /etc/sysconfig/syslog this file should has the line :
	 * SYSLOGD_OPTIONS="-m 0 -r"
	 */
    private String syslogConfFile2 = null;

    public void init() throws Exception {
        super.init();
    }

    /**
	 * send command to the cli
	 * 
	 * @param position
	 * @param command
	 *            the command that will be send to the cli
	 * @throws Exception
	 */
    public void sendAnyCommand(String command) throws Exception {
        sendAnyCommand(command, 30000);
    }

    /**
	 * send command to the cli
	 * 
	 * @param command
	 *            the command that will be send to the cli
	 * @param timeOut
	 *            timeout in miliseconds
	 * @throws Exception
	 */
    public void sendAnyCommand(String command, long timeOut) throws Exception {
        SyslogCliCommand cliCommand = new SyslogCliCommand();
        cliCommand.setCommands(new String[] { command });
        cliCommand.setTimeout(timeOut);
        cliCommand.setSilent(isCommandSilent());
        handleCliCommand(command, cliCommand);
    }

    /**
	 * send command to the cli, and don't display it on the report
	 * 
	 * @param command
	 *            the command that will be send to the cli
	 * @param timeOut
	 *            timeout in miliseconds
	 * @throws Exception
	 */
    public void sendAnyCommandSilent(String command) throws Exception {
        SyslogCliCommand cliCommand = new SyslogCliCommand();
        cliCommand.setCommands(new String[] { command });
        cliCommand.setSilent(true);
        handleCliCommand(command, cliCommand);
    }

    /**
	 * clears the log file
	 * 
	 * @throws Exception
	 */
    private void clearLog() throws Exception {
        report.report("Clear SysLog");
        sendAnyCommandSilent("echo > " + this.getLogFile());
    }

    /**
	 * fetch the log file to the html full detailed report
	 * 
	 * @throws Exception
	 */
    private String fetch() throws Exception {
        Calendar c = Calendar.getInstance(TimeZone.getDefault());
        String tmpFileDirectory = "/tmp/";
        String tmpFileName = "syslogtmp" + c.getTimeInMillis() + ".log";
        sendAnyCommandSilent("cp -f " + this.getLogFile() + " " + tmpFileDirectory + tmpFileName);
        sendAnyCommandSilent("ls -l " + tmpFileDirectory + tmpFileName);
        sendAnyCommandSilent("chmod 777 " + tmpFileDirectory + tmpFileName);
        byte[] capFileArr = this.getFileFromFtp(tmpFileDirectory + tmpFileName);
        report.saveFile(tmpFileName, capFileArr);
        report.report("copy SysLog file");
        sendAnyCommandSilent("rm -f " + tmpFileDirectory + tmpFileName);
        return tmpFileName;
    }

    /**
	 * this function login to the ftp server, and return specific file (remote)
	 * as byte[]
	 * 
	 * @param remote
	 *            the name of the file that we want to get as byte[]
	 * @return the file as byte[]
	 * @throws Exception
	 */
    private byte[] getFileFromFtp(String remote) throws Exception {
        ftp = new FTPClient();
        int reply;
        ftp.connect(ftpServer);
        reply = ftp.getReplyCode();
        if (!FTPReply.isPositiveCompletion(reply)) {
            ftp.disconnect();
            throw new Exception("FTP server refused connection.");
        }
        if (!ftp.login(ftpUsername, ftpPassword)) {
            ftp.logout();
            throw new Exception("Cann't login to ftp.");
        }
        ftp.enterLocalPassiveMode();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ftp.retrieveFile(remote, baos);
        ftp.logout();
        if (ftp.isConnected()) {
            try {
                ftp.disconnect();
            } catch (IOException f) {
            }
        }
        return baos.toByteArray();
    }

    /**
	 * this function gets file name , invoke the ls linux command and gets the
	 * size of the file
	 * 
	 * @param fname
	 *            the file name
	 * @return the size of the file
	 * @throws Exception
	 */
    private BigInteger getFileSize(String fname) throws Exception {
        String lsCmd = "ls -l " + fname;
        sendAnyCommandSilent(lsCmd);
        GetSpaceCounter tc;
        analyze(tc = new GetSpaceCounter(lsCmd));
        String numberAsString = (tc.getCounter().replaceAll("\\p{Space}+", " ")).split(" ")[4];
        return new BigInteger(numberAsString);
    }

    /**
	 * this function check if specific file is growing it checks the file size,
	 * wait 5 seconds and check the size again if it's greater it return true,
	 * else it returns false
	 * 
	 * @param fname
	 *            the file name that we are checking
	 * @return if the file is growing or not
	 * @throws Exception
	 */
    public boolean isGrow(String fname, int seconds) throws Exception {
        BigInteger size1 = this.getFileSize(fname);
        Thread.sleep(seconds);
        if (getFileSize(fname).compareTo(size1) == 1) return true;
        return false;
    }

    /**
	 * this function check if the log file (set in the sut file) is growing. it
	 * checks the file size,wait 5 seconds and check the size again if it's
	 * greater it return true, else it returns false
	 * 
	 * @param fname
	 *            the file name that we are checking
	 * @return if the file is growing or not
	 * @throws Exception
	 */
    public boolean isGrow() throws Exception {
        return this.isGrow(getLogFile(), 5000);
    }

    private void executeSyslogParam(String param) throws Exception {
        sendAnyCommand(syslogScriptFile + " " + param);
    }

    /**
	 * starts syslog proccess
	 * 
	 * @throws Exception
	 */
    public void startSyslogProcess() throws Exception {
        executeSyslogParam("start");
    }

    public void stopSyslogProcess() throws Exception {
        executeSyslogParam("stop");
    }

    public void statusSyslogProcess() throws Exception {
        executeSyslogParam("status");
    }

    public void restartSyslogProcess() throws Exception {
        executeSyslogParam("restart");
    }

    public void condRestartSyslogProcess() throws Exception {
        executeSyslogParam("condrestart");
    }

    private void fixSyslogConf1() throws Exception {
        String syslogAddLine = "local0.* +" + this.getLogFile();
        String cmd = "cat " + this.getSyslogConfFile1() + " | egrep \"" + syslogAddLine + "\"";
        sendAnyCommand(cmd);
        String tmpTestAgainst = this.getTestAgainstObject().toString();
        String tmpTA2 = tmpTestAgainst.substring(tmpTestAgainst.indexOf(cmd) + cmd.length(), tmpTestAgainst.length());
        syslogAddLine = syslogAddLine.replace("+", "     ");
        if (!(tmpTA2.replaceAll(" +", " ").contains(syslogAddLine.replaceAll(" +", " ")))) sendAnyCommand("echo \"" + syslogAddLine + "\" >> " + syslogConfFile1);
    }

    private void fixSyslogConf2() throws Exception {
        String searchLine = "SYSLOGD_OPTIONS";
        String badLine = "SYSLOGD_OPTIONS=\"-m 0\"";
        String shouldBeLine = "SYSLOGD_OPTIONS=\"-m 0 -r\"";
        String cmd = "cat " + this.getSyslogConfFile2() + " | egrep " + searchLine;
        sendAnyCommand(cmd);
        String tmpTestAgainst = this.getTestAgainstObject().toString();
        String tmpTA2 = tmpTestAgainst.substring(tmpTestAgainst.indexOf(cmd) + cmd.length(), tmpTestAgainst.length());
        tmpTA2 = tmpTA2.split("\\r\\n")[1].replaceAll("\\r", "");
        if (!tmpTA2.equals(shouldBeLine)) {
            Calendar c = Calendar.getInstance(TimeZone.getDefault());
            String tmpFileName = "/tmp/syslogtmp" + c.getTimeInMillis();
            sendAnyCommand("sed 's/" + badLine + "/" + shouldBeLine + "/g' " + this.getSyslogConfFile2() + " > " + tmpFileName);
            sendAnyCommand("mv -f " + tmpFileName + " " + this.getSyslogConfFile2());
        }
    }

    public void fixSyslog() throws Exception {
        fixSyslogConf1();
        fixSyslogConf2();
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public void setFtpServer(String ftpServer) {
        this.ftpServer = ftpServer;
    }

    public void setFtpUsername(String ftpUsername) {
        this.ftpUsername = ftpUsername;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public void setSyslogConfFile1(String syslogConfFile1) {
        this.syslogConfFile1 = syslogConfFile1;
    }

    public void setSyslogConfFile2(String syslogConfFile2) {
        this.syslogConfFile2 = syslogConfFile2;
    }

    public void setSyslogScriptFile(String syslogScriptFile) {
        this.syslogScriptFile = syslogScriptFile;
    }

    /**
	 * 
	 * @return the second config file full name this file should has the line :
	 *         SYSLOGD_OPTIONS="-m 0 -r"
	 */
    public String getSyslogConfFile2() {
        return syslogConfFile2;
    }

    /**
	 * 
	 * @return the first config file full name this file should has the line :
	 *         �local0.* /usr/Cooter/tmp/setup_H_syslog.log�
	 */
    public String getSyslogConfFile1() {
        return syslogConfFile1;
    }

    /**
	 * @return the name of the script file that start/stop/retart the syslog
	 */
    public String getSyslogScriptFile() {
        return syslogScriptFile;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public String getFtpServer() {
        return ftpServer;
    }

    public String getFtpUsername() {
        return ftpUsername;
    }

    /**
	 * @return the syslog log file name
	 */
    public String getLogFile() {
        return logFile;
    }

    public void startSyslog() throws Exception {
        fixSyslog();
        restartSyslogProcess();
        if (!isGrow()) report.report("syslog file is not growing", false);
        clearLog();
    }

    public String stopSyslog(boolean saveCapFile) throws Exception {
        if (saveCapFile) return fetch(); else return null;
    }

    public boolean isCommandSilent() {
        return commandSilent;
    }

    public void setCommandSilent(boolean isCommandSilent) {
        this.commandSilent = isCommandSilent;
    }
}
