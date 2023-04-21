package cn.jcourse.host.shell.command;

import cn.jcourse.host.shell.ICommand;
import java.io.File;

/**
 * �๦������<br>
 * �ı�Ŀ¼Ȩ������<br>
 * ���ڸı��û�Ŀ¼���ļ���Ȩ��<br>
 * 
 * @author luoxueyuan
 * @version 0.1.0
 */
public class ChmodCommand implements ICommand {

    String cmd = "chmod";

    public ChmodCommand(String mode, String DoF) {
        File f = new File(DoF);
        if (f.isFile()) {
            cmd = cmd + " " + mode + " " + DoF;
        } else if (f.isDirectory()) {
            cmd = cmd + " -R " + mode + " " + DoF;
        } else {
            cmd = null;
        }
    }

    public String getCommand(String user) {
        return cmd;
    }
}
