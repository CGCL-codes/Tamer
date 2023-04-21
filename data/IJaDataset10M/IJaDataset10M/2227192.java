package com.aqua.stations.linux;

import java.io.File;
import jsystem.extensions.analyzers.text.FindText;
import jsystem.extensions.analyzers.text.TextNotFound;
import com.aqua.stations.StationTest;

public class deleteFromHostsFileTests extends StationTest {

    public void testDeleteFromHostsFile1() throws Exception {
        String[] host = new String[] { "123.456.789.012", "www.walla.co.il" };
        report.step("add new host to hosts file:" + host[0] + " " + host[1]);
        station.addToHostsFile(new File("/etc/hosts"), host[0], host[1]);
        station.getCliSession(false).cliCommand("cat /etc/hosts");
        station.getCliSession(false).analyze(new FindText(host[0] + " " + host[1]));
        report.step("delete host from hosts file:" + host[0] + " " + host[1]);
        station.deleteFromHostsFile(host[0], host[1]);
        station.getCliSession(false).cliCommand("cat /etc/hosts");
        station.getCliSession(false).analyze(new TextNotFound(host[0] + " " + host[1]));
    }

    public void testDeleteFromHostsFile2() throws Exception {
        String[] host = new String[] { "123.456.789.012", "www.walla.co.il" };
        report.step("add new host to hosts file:" + host[0] + " " + host[1]);
        station.addToHostsFile(new File("/etc/hosts"), host[0], host[1]);
        station.getCliSession(false).cliCommand("cat /etc/hosts");
        station.getCliSession(false).getTestAgainstObject().toString();
        station.getCliSession(false).analyze(new FindText(host[0] + " " + host[1]));
        report.step("delete host from hosts file:" + host[0] + " " + host[1]);
        station.deleteFromHostsFile(host[0], host[1]);
        station.getCliSession(false).cliCommand("cat /etc/hosts");
        station.getCliSession(false).getTestAgainstObject().toString();
        station.getCliSession(false).analyze(new TextNotFound(host[0] + " " + host[1]));
        report.step("delete host from hosts file:" + host[0] + " " + host[1]);
        station.deleteFromHostsFile(host[0], host[1]);
        station.getCliSession(false).cliCommand("cat /etc/hosts");
        station.getCliSession(false).analyze(new TextNotFound(host[0] + " " + host[1]));
    }

    public void testDeleteFromHostsFile3() throws Exception {
        String[] host = new String[] { "123.456.789.012", "www.walla.co.il" };
        report.step("add new host to hosts file:" + host[0] + " " + host[1]);
        station.addToHostsFile(new File("/etc/hosts"), host[0], host[1]);
        station.getCliSession(false).cliCommand("cat /etc/hosts");
        station.getCliSession(false).getTestAgainstObject().toString();
        station.getCliSession(false).analyze(new FindText(host[0] + " " + host[1]));
        report.step("add new host to hosts file:" + host[0] + " " + host[1]);
        station.addToHostsFile(new File("/etc/hosts"), host[0], host[1]);
        station.getCliSession(false).cliCommand("cat /etc/hosts");
        station.getCliSession(false).getTestAgainstObject().toString();
        station.getCliSession(false).analyze(new FindText(host[0] + " " + host[1]));
        report.step("delete host from hosts file:" + host[0] + " " + host[1]);
        station.deleteFromHostsFile(host[0], host[1]);
        station.getCliSession(false).cliCommand("cat /etc/hosts");
        station.getCliSession(false).analyze(new TextNotFound(host[0] + " " + host[1]));
        report.step("delete host from hosts file:" + host[0] + " " + host[1]);
        station.deleteFromHostsFile(host[0], host[1]);
        station.getCliSession(false).cliCommand("cat /etc/hosts");
        station.getCliSession(false).getTestAgainstObject().toString();
        station.getCliSession(false).analyze(new TextNotFound(host[0] + " " + host[1]));
    }
}
