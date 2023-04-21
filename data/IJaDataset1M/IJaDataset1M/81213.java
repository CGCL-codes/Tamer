package com.qspin.qtaste.util.versioncontrol.impl;

import com.qspin.qtaste.util.versioncontrol.*;
import com.qspin.qtaste.util.Exec;
import com.qspin.qtaste.util.Log4jLoggerFactory;
import java.io.ByteArrayOutputStream;
import org.apache.log4j.Logger;

/**
 * SubversionVersionControl is the implementation for Subversion version contol tool
 * @author lvboque
 */
public class SubversionVersionControl implements VersionControlInterface {

    private static Logger logger = Log4jLoggerFactory.getLogger(SubversionVersionControl.class);

    public String getVersion(String path) {
        Exec executor = new Exec();
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            executor.exec("svn info " + path, null, null, null, output);
            String outputStr = output.toString();
            String svnTagString = "/tags/";
            int index = outputStr.indexOf(svnTagString);
            if (index == -1) {
                return "undefined";
            }
            int startIndex = index + svnTagString.length();
            String[] tokens = outputStr.substring(startIndex).split("/");
            String versionString = tokens[0];
            return versionString;
        } catch (Exception e) {
            logger.fatal("Error extracting testscript version from svn", e);
        }
        return "undefined";
    }
}
