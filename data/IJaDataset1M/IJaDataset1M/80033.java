package tests.com.rise.rois.server;

import junit.framework.TestCase;
import com.rise.rois.server.commands.CommandUtil;

public class TestCSVExport extends TestCase {

    public void testCSVExport() {
        String username = "test";
        String password = "password";
        String columns = "column_id";
        String table = "computers";
        String call = "/home/tim/temp/exporttocsv.sh " + username + " " + password + " " + columns + " " + table + " > /home/tim/temp/exportcsv.txt";
        System.out.println("Call > " + call);
        String output = CommandUtil.runCommand(true, "/bin/bash", "-c", call);
        System.out.println("Output > " + output);
    }
}
