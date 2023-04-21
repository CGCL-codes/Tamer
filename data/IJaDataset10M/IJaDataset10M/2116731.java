package com.shine.framework.NodeJs;

import com.shine.framework.core.util.CmdUtil;

/**
 * java node.js服务器管理类
 * 
 * @author viruscodecn@gmail.com
 * 
 */
public class NodeJsManager {

    private String nodeJsPath = "C:\\Program Files\\Js\\node-0.4.12-i686-pc-cygwin-complete\\bin\\node";

    private static NodeJsManager manager = null;

    public static NodeJsManager getManager() {
        if (manager == null) manager = new NodeJsManager();
        return manager;
    }

    public void startNodeJsServer(String jsPath) {
        StringBuffer cmd = new StringBuffer();
        cmd.append("\"");
        cmd.append(nodeJsPath);
        cmd.append("\" ");
        cmd.append(jsPath);
        System.out.println(cmd.toString());
        System.out.println(CmdUtil.execute("cd \"C:\\Program Files\\Js\\node-0.4.12-i686-pc-cygwin-complete\"", cmd.toString()));
    }
}
