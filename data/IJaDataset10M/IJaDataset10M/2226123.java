package com.apps;

import java.util.Map;
import com.mlib.util.CommandUtil;
import com.mlib.util.LoggerUtil;

/**
 * 应用成基类
 * 
 * @author zitee@163.com
 * @time 2011-9-30 下午10:07:44
 * @version 1.0
 */
public abstract class AbstractApplication {

    public void execute(String args[]) {
        Map<String, String> parameters = CommandUtil.convertCMDParameter(args);
        if (parameters.get("help") != null) {
            this.printHelp();
            return;
        }
        this.run(parameters);
    }

    public abstract void run(Map<String, String> parameters);

    public abstract String help();

    public void printHelp() {
        LoggerUtil.getDefaultLoggerUtil().println(this.help());
    }
}
