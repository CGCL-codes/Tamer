package cn.jcourse.host.shell.command;

import cn.jcourse.host.shell.ICommand;

/**
 * �๦������<br>
 * �ƶ��ļ�����<br>
 * �����ƶ��ļ�<br>
 * 
 * @author luoxueyuan
 * @version 0.1.0
 */
public class MvCommand implements ICommand {

    String cmd = "mv";

    public MvCommand(String oldDir, String newDir, String fileName) {
        cmd = cmd + " " + oldDir + fileName + " " + newDir;
    }

    public MvCommand(String oldDir, String newDir, String fileName, String newName) {
        cmd = cmd + " " + oldDir + fileName + " " + newDir + newName;
    }

    public String getCommand(String user) {
        return cmd;
    }
}
