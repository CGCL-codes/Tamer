package com.nhncorp.cubridqa.listener;

import java.util.List;
import java.util.Map;
import org.eclipse.jface.dialogs.MessageDialog;
import com.nhncorp.cubridqa.utils.EnvGetter;

/**
 * Is used to check the system environment parameters.
 * Check system environment parameters,include $QA_REPOSITORY
 * ,$PROPERTIES_PATH,$init_path,$SHELL_CONFIG_PATH,$CLASSPATH
 * ,these five parameters should be set before running the QA
 * tool.
 * @ClassName: ShellEnvCheckUtil
 * @date 2009-9-7
 * @version V1.0 
 * Copyright (C) www.nhn.com
 */
public abstract class ShellEnvCheckUtil {

    public static boolean OK = true;

    public static boolean ERROR = false;

    public static boolean checkEnv(List<String> pathList) {
        if (hasShell(pathList)) {
            Map<String, String> map = EnvGetter.getenv();
            String[] keys = new String[] { "QA_REPOSITORY", "init_path", "SHELL_CONFIG_PATH", "CLASSPATH" };
            for (String key : keys) {
                if (!map.containsKey(key)) {
                    MessageDialog.openError(null, "ENVIRONMENT  VARIABLE ERROR!", "QA_REPOSITORY/init_path/SHELL_CONFIG_PATH/CLASSPATH environment variable error, please check and set!");
                    return ERROR;
                }
            }
        }
        return OK;
    }

    private static boolean hasShell(List<String> pathList) {
        for (String path : pathList) {
            path = path.replaceAll("\\\\", "/");
            if (-1 != path.toUpperCase().indexOf("/SHELL")) {
                return true;
            }
        }
        return false;
    }
}
