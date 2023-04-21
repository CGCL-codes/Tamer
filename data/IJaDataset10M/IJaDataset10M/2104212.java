package com.nhncorp.cubridqa.replication.config;

import java.util.List;
import com.nhncorp.cubridqa.console.ConsoleAgent;
import com.nhncorp.cubridqa.console.util.StringUtil;
import com.nhncorp.cubridqa.replication.util.SystemConst;

/**
 * 
 * @ClassName: SystemHandle
 * @Description: used for some handle of system
 * 
 * 
 * @date 2009-9-1
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class SystemHandle {

    /**
	 * system exit
	 */
    public static void exit() {
        System.out.println("System will exit!");
        System.exit(0);
    }

    /**
	 * get user current dir
	 * 
	 * @return
	 */
    public static String getUserDir() {
        return System.getProperty("user.dir");
    }

    /**
	 * execute use .
	 * 
	 * @param filename
	 * @return
	 */
    public static int executePointShell(String filename) {
        String[] cmd = new String[3];
        cmd[0] = "/bin/bash";
        cmd[1] = "-c";
        cmd[2] = ".  " + filename;
        return ShellFileMaker.executeShell(cmd);
    }

    public static int executeShShell(String filename) {
        String[] cmd = new String[3];
        cmd[0] = "/bin/bash";
        cmd[1] = "-c";
        cmd[2] = "sh  " + filename;
        return ShellFileMaker.executeShell(cmd);
    }

    /**
	 * execute use .
	 * 
	 * @param filename
	 * @return
	 */
    public static int executeCmdShell(String comd) {
        String[] cmd = new String[3];
        cmd[0] = "/bin/bash";
        cmd[1] = "-c";
        cmd[2] = comd;
        return ShellFileMaker.executeShell(cmd);
    }

    /**
	 * execute use expect
	 * 
	 * @param filename
	 * @return
	 */
    public static int executeExpectShell(String filename) {
        String[] cmd = new String[3];
        cmd[0] = "/bin/bash";
        cmd[1] = "-c";
        cmd[2] = "expect  " + filename;
        return ShellFileMaker.executeShell(cmd);
    }

    /**
	 * execute in windows
	 * 
	 * @param filename
	 * @return
	 */
    public static int executeWCmdShell(String filename) {
        filename = StringUtil.replaceForCygwin(filename);
        String[] cmd = new String[3];
        cmd[0] = "cmd.exe";
        cmd[1] = "/C";
        cmd[2] = "expect  " + filename;
        return ShellFileMaker.executeShell(cmd);
    }

    /**
	 * check the result of shell execute
	 * 
	 * @param result
	 * @throws Exception
	 */
    public static void checkExecuteResult(int result) throws Exception {
        switch(result) {
            case -1:
                throw new Exception("Error: System error! ");
            case 99:
                throw new Exception("Error: Access denied! ");
            case 98:
                throw new Exception("Error: Connection refused! ");
            case 97:
                throw new Exception("Error: DbName already existed!");
            case 96:
                throw new Exception("Error: DbName doesnot existed!");
            case 95:
                throw new Exception("Error: Database still running!");
            case 1:
                break;
        }
    }

    /**
	 * 
	 * @param ip
	 * @param user
	 * @param password
	 * @param remotepath
	 * @param filename
	 * @param localpath
	 * @param scp
	 * @throws Exception
	 */
    public static void downloadFile(String ip, String user, String password, String remotepath, String filename, String localpath, String scp) throws Exception {
        ConsoleAgent.addMessage("Download " + filename + " from master database ......");
        if (!EnvironmentCheck.isIpCanConnected(ip)) {
            ConsoleAgent.addMessage("Error: Cannot connect " + ip);
            throw new Exception("Error: Cannot connect " + ip);
        }
        StringBuffer context = ShellFileMaker.makeExpectShellHead();
        if (scp == null) throw new Exception("Error : Download Shell is null!");
        scp = RemoteAccess.getSCPDownloadRemoteAccessShell(ip, user, password, remotepath + filename, localpath, scp);
        context.append(scp + "\r");
        context = ShellFileMaker.makeExpectShellEnd(context);
        String shellname = ShellFileMaker.writer(context.toString(), SystemConst.QA_PATH + "/temp" + "/sqlxdownload.sh");
        SystemHandle.checkExecuteResult(SystemHandle.executeExpectShell(shellname));
        ShellFileMaker.removeFile(shellname);
    }

    /**
	 * 
	 * @param ip
	 * @param user
	 * @param password
	 * @param remotepath
	 * @param filename
	 * @param localpath
	 * @param scp
	 * @throws Exception
	 */
    public static void uploadFile(String ip, String user, String password, String remotepath, String filename, String localpath, String scp) throws Exception {
        ConsoleAgent.addMessage("Upload " + filename + " to master database ......" + SystemConst.LINE_SEPERATOR);
        if (!EnvironmentCheck.isIpCanConnected(ip)) {
            ConsoleAgent.addMessage("Error: Cannot connect " + ip);
            throw new Exception("Error: Cannot connect " + ip);
        }
        StringBuffer context = ShellFileMaker.makeExpectShellHead();
        if (scp == null) {
            ConsoleAgent.addMessage("Error:  Upload Shell is null !");
            throw new Exception("Error : Upload Shell is null !");
        }
        scp = RemoteAccess.getSCPUploadRemoteAccessShell(ip, user, password, localpath + "/" + filename, remotepath, scp);
        context.append(scp + "\r");
        context = ShellFileMaker.makeExpectShellEnd(context);
        String shellname = ShellFileMaker.writer(context.toString(), SystemConst.QA_PATH + "/temp" + "/sqlxupload.sh");
        SystemHandle.checkExecuteResult(SystemHandle.executeExpectShell(shellname));
        ConsoleAgent.addMessage("Upload Done" + SystemConst.LINE_SEPERATOR);
        ShellFileMaker.removeFile(shellname);
    }

    /**
	 * 
	 * @param ip
	 * @param user
	 * @param password
	 * @param localpath
	 * @param remotepath
	 * @param scp
	 * @throws Exception
	 */
    public static void uploadFilePath(String ip, String user, String password, String localpath, String remotepath, String scp) throws Exception {
        if (!EnvironmentCheck.isIpCanConnected(ip)) {
            ConsoleAgent.addMessage("Error: Cannot connect " + ip);
            throw new Exception("Error: Cannot connect " + ip);
        }
        StringBuffer context = ShellFileMaker.makeExpectShellHead();
        if (scp == null) {
            ConsoleAgent.addMessage("Error:  Upload Shell is null !");
            throw new Exception("Error : Upload Shell is null !");
        }
        scp = RemoteAccess.getSCPUploadPathRemoteAccessShell(ip, user, password, localpath, remotepath, scp);
        context.append(scp + "\r");
        context = ShellFileMaker.makeExpectShellEnd(context);
        String shellname = ShellFileMaker.writer(context.toString(), SystemConst.QA_PATH + "temp" + "/sqlxuploadpath.sh");
        SystemHandle.checkExecuteResult(SystemHandle.executeExpectShell(shellname));
        ConsoleAgent.addMessage("Upload Done" + SystemConst.LINE_SEPERATOR);
        ShellFileMaker.removeFile(shellname);
    }

    /**
	 * copy file
	 * 
	 * @param sourcepath
	 * @param targetpath
	 * @param cp
	 * @throws Exception
	 */
    public static void copyFile(String sourcepath, String targetpath, String cp) throws Exception {
        StringBuffer context = ShellFileMaker.makeExpectShellHead();
        if (cp == null) {
            ConsoleAgent.addMessage("Error:  Copy Shell is null !");
            throw new Exception("Error : Copy Shell is null !");
        }
        cp = cp.replace("$source", sourcepath);
        cp = cp.replace("$target", targetpath);
        context = context.append(cp + SystemConst.LINE_SEPERATOR);
        context = ShellFileMaker.makeExpectShellEnd(context);
        String shellname = ShellFileMaker.writer(context.toString(), SystemConst.QA_PATH + "/temp/temp.sh");
        SystemHandle.checkExecuteResult(SystemHandle.executeExpectShell(shellname));
        ConsoleAgent.addMessage("Done");
        ShellFileMaker.removeFile(shellname);
    }

    public static List getList() {
        return ShellFileMaker.getList();
    }

    public static void main(String[] args) {
        String abc = "C:/temp/";
        String tmp = abc.substring(0, 1);
        abc = "/cygdrive/" + tmp + abc.substring(2);
        System.out.println(abc);
    }
}
