package it.newinstance.watchdog.samples;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Luigi R. Viggiano
 * @version $Revision: 53 $
 * @since 27-nov-2005
 */
public class WatchDog {

    private static final String IP_PATTERN = "\\D(\\d*(\\.\\d*){3})";

    private static final Pattern ILLEGAL_USER = Pattern.compile(".*Illegal user.*from.*" + IP_PATTERN + ".*");

    private static final Pattern INVALID_PASS = Pattern.compile(".*Failed password for.*from.*" + IP_PATTERN + ".*");

    private BufferedReader input;

    private File denyFile;

    private int threshold;

    private Map<String, Integer> ipMap;

    public WatchDog(String log, String deny, int threshold) throws FileNotFoundException {
        ipMap = new HashMap<String, Integer>(10);
        input = new BufferedReader(new FileReader(log));
        denyFile = new File(deny);
        this.threshold = threshold;
    }

    public void guard() throws IOException {
        Util.info("Bobby watching the sheeps...");
        String line = null;
        while ((line = input.readLine()) != null) {
            if ("SHUTDOWN".equals(line)) break;
            boolean exceeded = update(ILLEGAL_USER.matcher(line)) || update(INVALID_PASS.matcher(line));
            if (exceeded) save(ipMap);
        }
        Util.info("Bobby sleeping peacefully...");
    }

    private boolean update(Matcher matcher) {
        if (!matcher.matches()) return false;
        String ip = matcher.group(1);
        Integer value = ipMap.get(ip);
        int count = (value != null) ? value.intValue() : 0;
        int newCount = count + 1;
        ipMap.put(ip, new Integer(newCount));
        Util.info("ip: " + ip + "\t fails:" + newCount);
        return count < threshold && newCount >= threshold;
    }

    private void save(Map<String, Integer> ipMap) throws IOException {
        Util.info("Saving " + denyFile);
        BufferedReader template = new BufferedReader(new InputStreamReader(Util.getStream("/hosts.deny")));
        BufferedWriter output = new BufferedWriter(new FileWriter(denyFile));
        String line = null;
        while ((line = template.readLine()) != null) {
            output.write(line);
            output.write("\n");
        }
        output.write("ALL:");
        for (String ip : ipMap.keySet()) output.write("\\\n    " + ip);
        output.write("\n\n");
        output.close();
        template.close();
    }

    public static void main(String[] args) throws IOException {
        Properties conf = new Properties();
        conf.load(Util.getStream("/WatchDog.config"));
        WatchDog bobby = new WatchDog(conf.getProperty("watchdog.input.file", "dev/watch-dog.fifo"), conf.getProperty("watchdog.deny.file", "/etc/hosts.deny"), Integer.parseInt(conf.getProperty("watchdog.allowed.fails", "3")));
        bobby.guard();
    }

    private static class Util {

        private static Object[] data = new Object[] { new Date(), "INFO", new StringBuffer() };

        private static void info(String message) {
            Date time = (Date) data[0];
            time.setTime(System.currentTimeMillis());
            StringBuffer buffer = (StringBuffer) data[2];
            buffer.setLength(0);
            buffer.append(message);
            System.out.println(MessageFormat.format("{0,date} {0,time} [{1}]: {2}", data));
            System.out.flush();
        }

        private static InputStream getStream(String path) {
            return WatchDog.class.getResourceAsStream(path);
        }
    }
}
