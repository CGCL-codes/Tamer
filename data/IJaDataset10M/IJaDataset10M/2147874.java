package com.dmoving.log.ut;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.dmoving.log.LogReader;
import com.dmoving.log.exceptions.FatalErrException;
import com.dmoving.log.exceptions.RecoverableException;
import junit.framework.TestCase;

public class LogReaderUTClass extends TestCase {

    public static void main(String[] args) throws FatalErrException, RecoverableException, IOException {
        File logFile = new File("\\\\pvcent65\\opt_share\\IBM\\WebSphere\\PortalServer\\log\\trace.log");
        ArrayList<String> acceptFileList = new ArrayList<String>();
        acceptFileList.add("trace.log");
        LogReader logReader = LogReader.getReaderInstance(logFile, acceptFileList, "WebSphere_Portal");
        logReader.getInitialLog();
        System.in.read();
    }
}
