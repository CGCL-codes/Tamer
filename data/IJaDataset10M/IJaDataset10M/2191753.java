package com.atech.update.startup;

import com.atech.update.config.ComponentEntry;
import com.atech.update.config.UpdateConfiguration;

/**
 *  This file is part of ATech Tools library.
 *  
 *  StartupFileCreator - For creating startup file (one) - deprecated
 *  Copyright (C) 2008  Andy (Aleksander) Rozman (Atech-Software)
 *  
 *  
 *  This library is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public
 *  License as published by the Free Software Foundation; either
 *  version 2.1 of the License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this library; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA 
 *  
 *  
 *  For additional information about this project please visit our project site on 
 *  http://atech-tools.sourceforge.net/ or contact us via this emails: 
 *  andyrozman@users.sourceforge.net or andy@atech-software.com
 *  
 *  @author Andy {andy@atech-software.com}
 *
*/
public class StartupFileCreator {

    String os_name;

    UpdateConfiguration uc;

    String separator = ";";

    String root = "..";

    String extension = "";

    /**
	 * Constructor
	 * 
	 * @param uc
	 */
    public StartupFileCreator(UpdateConfiguration uc) {
        this.uc = uc;
    }

    /**
	 * Get Startup File Body
	 * 
	 * @return get body of file
	 */
    public String[] getStartupFileBody() {
        this.os_name = System.getProperty("os.name");
        if ((os_name.contains("Linux")) || (os_name.contains("FreeBSD"))) return createShScript(); else if (os_name.contains("Mac")) return createBashScript(); else if (os_name.contains("Win")) return createWindowsScript(); else {
            printNotSupported();
            return null;
        }
    }

    private void printNotSupported() {
        System.out.println("This Operating System (" + os_name + ") is not supported " + "\nby ATech's Startup/Update Manager.");
        System.out.println("If you wish to help us with support for your OS please contact us");
        System.out.println("on our email (support@atech-software.com).");
    }

    private String[] createShScript() {
        return createShellScript("sh");
    }

    private String[] createBashScript() {
        return createShellScript("bash");
    }

    private String[] createShellScript(String shell) {
        this.separator = ":";
        this.root = this.uc.root;
        StringBuffer sb = new StringBuffer();
        sb.append("#!/bin/" + shell + "\n\n");
        String del_db = uc.root + "/data/db/pis_int.lck";
        sb.append("#   Delete Db Lock File\n\n");
        sb.append("if [ -f " + del_db + "]; then \n" + "   rm " + del_db + "\n" + "fi\n\n");
        sb.append("#   Run Application\n");
        sb.append(this.uc.java_exe);
        sb.append(" -classpath ");
        sb.append("." + this.separator);
        sb.append(this.getFileList());
        sb.append(" ");
        sb.append(this.uc.main_class);
        return getReturnValue("sh", sb.toString());
    }

    private String[] createWindowsScript() {
        this.separator = ";";
        this.root = this.uc.root;
        StringBuffer sb = new StringBuffer();
        sb.append("@echo off\n\n");
        String del_db = uc.root + "/data/db/pis_int.lck";
        sb.append("rem   Delete Db Lock File\n");
        sb.append("if exist " + del_db + " del " + del_db + "\n\n");
        sb.append("rem   Run Application\n");
        sb.append(this.uc.java_exe);
        sb.append(" -classpath ");
        sb.append("." + this.separator);
        sb.append(this.getFileList());
        sb.append(" ");
        sb.append(this.uc.main_class);
        return getReturnValue("cmd", sb.toString());
    }

    private String[] getReturnValue(String ext, String body) {
        String[] ret_value = new String[2];
        ret_value[0] = ext;
        ret_value[1] = body;
        return ret_value;
    }

    private String getFileList() {
        StringBuffer files = new StringBuffer();
        int count = this.uc.Components().size() - 1;
        for (int i = 0; i <= count; i++) {
            ComponentEntry ce = this.uc.Components().get(i);
            String path = root + ce.root_dir;
            files.append(parseRoot(path, ce.files));
            if (count != i) files.append(this.separator);
        }
        return files.toString();
    }

    /**
	 * Parse Root
	 * 
	 * @param path path for file
	 * @param full_string full_string for parsing
	 * 
	 * @return resolved root
	 */
    public String parseRoot(String path, String full_string) {
        full_string = full_string.replaceAll(";", this.separator);
        return full_string.replaceAll("%ROOT%", path);
    }
}
