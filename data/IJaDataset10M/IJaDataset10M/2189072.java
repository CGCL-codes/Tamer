package com.cronopista.lightpacker.unknown;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import com.cronopista.lightpacker.OS;
import com.cronopista.lightpacker.actions.Action;
import com.cronopista.lightpacker.actions.CreateShortcutAction;
import com.cronopista.lightpacker.actions.EditFileAction;
import com.cronopista.lightpacker.actions.FileTypeAssociationAction;
import com.cronopista.lightpacker.actions.OpenWebpageAction;
import com.cronopista.lightpacker.steps.Installer;
import com.cronopista.util.StringUtils;

/**
 * @author Eduardo Rodr�guez
 * 
 */
public class OS_Unknown implements OS {

    public void createShortcut(String target, String icon, String output) {
    }

    public boolean openWebPage(String url) {
        return false;
    }

    public boolean openFile(String file) {
        return false;
    }

    public boolean executeProcess(String[] commands, String[] enviroment, boolean lock) {
        try {
            Process p = Runtime.getRuntime().exec(commands, enviroment);
            if (lock) {
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = null;
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
                input.close();
            }
        } catch (IOException e) {
            StringBuffer c = new StringBuffer();
            for (int i = 0; i < commands.length; i++) {
                c.append(commands[i]).append(' ');
            }
            Installer.getInstance().getLogger().log("Error executing process " + c.toString() + "\n" + StringUtils.getStackTrace(e));
            return false;
        }
        return true;
    }

    public boolean canExecuteAction(Action action) {
        if (action instanceof CreateShortcutAction) return false;
        if (action instanceof OpenWebpageAction) return false;
        if (action instanceof EditFileAction) return false;
        if (action instanceof FileTypeAssociationAction) return false;
        return true;
    }

    public boolean associateFileType(String ext, String filetype, String program) {
        return false;
    }
}
