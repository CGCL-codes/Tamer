package com.apps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.mlib.util.LoggerUtil;

public class Main {

    /**
	 * @param args
	 * @throws IOException 
	 */
    public static void main(String[] args) throws IOException {
        LoggerUtil.getDefaultLoggerUtil().println("==================Welcome!======================");
        StringBuilder appNameBuilder = new StringBuilder("");
        int count = 1;
        for (String appName : AppFactory.getAllApp().keySet()) {
            appNameBuilder.append("[" + appName + "]\t");
            if (count++ % 5 == 0) {
                appNameBuilder.append(appName + "\n");
            }
        }
        LoggerUtil.getDefaultLoggerUtil().println(appNameBuilder.toString());
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        LoggerUtil.getDefaultLoggerUtil().print(">");
        while (true) {
            line = reader.readLine();
            if (line.trim().equals("q")) {
                LoggerUtil.getDefaultLoggerUtil().print("Bye!");
                System.exit(0);
            } else {
                String[] pargs = line.split(" ");
                AbstractApplication app = AppFactory.getAllApp().get(pargs[0]);
                if (!pargs[0].trim().equals("") && app != null) {
                    try {
                        AbstractApplication appObject = app.getClass().newInstance();
                        appObject.execute(pargs);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    LoggerUtil.getDefaultLoggerUtil().println("Can not find app '" + pargs[0] + "'");
                }
            }
            LoggerUtil.getDefaultLoggerUtil().print(">");
        }
    }
}
