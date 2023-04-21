package net.assimilator.qa.core;

import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.sun.jini.qa.harness.SlaveTest;
import com.sun.jini.qa.harness.SlaveRequest;
import com.sun.jini.qa.harness.TestException;

/**
 * The class represents a table of hosts participating in the test.
 * You can use this table to e.g. search for a host by platform.
 * When constructed, the table talks to every host participating
 * in the test to gather necessary information.
 */
public class HostTable {

    /**
     * The logger used by this class.
     */
    private static Logger logger = Logger.getLogger("net.assimilator.qa.core");

    /**
     * Maps host names to platform description strings.
     */
    private LinkedHashMap hostMap = new LinkedHashMap();

    /**
     * Constructs a <code>HostTable</code>.
     *
     * @param hostList the list of hosts participating in the test.
     * @throws com.sun.jini.qa.harness.TestException if the test fails
     */
    public HostTable(ArrayList hostList) throws TestException {
        init(hostList);
    }

    /**
     * Finds a host whose platform description string contains
     * a subsequence that matches a given regular expression.
     * If there is more than one such host, the first suitable
     * one is selected. The platform description string has the
     * following format:
     * <pre>
     *      os.name os.version os.arch
     * </pre>
     * where <code>os.name</code>, <code>os.version</code>, and
     * <code>os.arch</code> are the corresponding system properties.
     *
     * @param regex the regular expression for which to look in
     *              the platform description string.
     * @return      the name of the found host, or <code>null</code>
     *              if there is no such host.
     */
    public String findHostByPlatform(String regex) {
        Pattern pattern = Pattern.compile(regex);
        for (Object o : hostMap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String platform = (String) entry.getValue();
            Matcher matcher = pattern.matcher(platform);
            if (matcher.find()) {
                return (String) entry.getKey();
            }
        }
        return null;
    }

    /**
     * Initializes the <code>hostMap</code>.
     *
     * @param hostList the list of hosts participating in the test.
     * @throws com.sun.jini.qa.harness.TestException if the test fails
     */
    private void init(ArrayList hostList) throws TestException {
        hostList = (ArrayList) hostList.clone();
        if (hostList.size() == 0) {
            hostList.add(null);
        }
        PlatformRequest request = new PlatformRequest();
        for (int i = 0; i < hostList.size(); i++) {
            String host = (String) hostList.get(i);
            String platform;
            if (i == 0) {
                platform = (String) request.doSlaveRequest(null);
            } else {
                platform = (String) SlaveTest.call(host, request);
            }
            hostMap.put(host, platform);
        }
        logger.info("");
        logger.info("Host table:");
        for (Object o : hostMap.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            String host = (String) entry.getKey();
            String platform = (String) entry.getValue();
            logger.info(host + ": " + platform);
        }
    }

    /**
     * The class represents a request sent to each host to determine
     * the host's platform.
     */
    private static class PlatformRequest implements SlaveRequest {

        /**
         * Is overridden to execute request-specific code.
         */
        public Object doSlaveRequest(SlaveTest slaveTest) {
            return System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch");
        }
    }
}
