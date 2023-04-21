package net.sf.shineframework.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * infrastructure definitions. This class can be used to retrieve the initial
 * definitions used to construct the infrastructure
 * 
 * @author amirk
 */
public class ShineFwDefinitions {

    /**
	 * retrieves the names of the infrastructure resources
	 * 
	 * @return an array of classpath resource names
	 * @throws IOException
	 */
    public static String[] getInfraResourceNames() throws IOException {
        List<String> result = new LinkedList<String>();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL core = loader.getResource("META-INF/services/net.sf.shineframework.core");
        addResources(result, core);
        Enumeration apps = loader.getResources("META-INF/services/net.sf.shineframework.application.custom");
        while (apps.hasMoreElements()) {
            addResources(result, (URL) apps.nextElement());
        }
        int suffix = 1;
        boolean found = true;
        while (found) {
            apps = loader.getResources("META-INF/services/net.sf.shineframework.application.custom." + (suffix++));
            found = false;
            while (apps.hasMoreElements()) {
                found = true;
                addResources(result, (URL) apps.nextElement());
            }
        }
        String[] resultArray = new String[result.size()];
        int count = 0;
        for (String s : result) {
            resultArray[count++] = s;
        }
        return resultArray;
    }

    private static void addResources(List<String> result, URL url) throws IOException {
        Properties props = new Properties();
        InputStream in = url.openStream();
        props.load(in);
        in.close();
        String resources = props.getProperty("resources");
        StringTokenizer st = new StringTokenizer(resources, ";");
        while (st.hasMoreTokens()) {
            result.add(st.nextToken());
        }
    }
}
