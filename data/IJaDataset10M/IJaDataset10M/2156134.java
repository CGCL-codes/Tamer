package org.hitchhackers.tools.jmx.connection;

import java.io.IOException;
import java.net.MalformedURLException;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXServiceURL;
import org.hitchhackers.tools.jmx.util.parser.ParsedCommandLine;

public interface JMXConnectionFactory {

    JMXServiceURL buildURLFromCommandLine(ParsedCommandLine commandLine) throws MalformedURLException;

    JMXServiceURL assembleURL(String namingHost, int namingPort) throws MalformedURLException, IOException;

    JMXServiceURL assembleURL(String namingHost, int namingPort, String serverProtocol, String jndiPath) throws MalformedURLException;

    MBeanServerConnection getConnection(JMXServiceURL url) throws IOException;

    void returnConnection(JMXServiceURL url, MBeanServerConnection mbeanServerConnection);
}
