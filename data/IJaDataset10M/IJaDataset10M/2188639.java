package enzimaweb.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import enzimaweb.WebSerializer;

/**
 * UI template capable of rendering based on the given named resource.
 * 
 * @author Edmundo Andrade
 */
public class Template extends UIComponent {

    public static String TAG_START = "<!-- template start -->";

    public static String TAG_END = "<!-- template end -->";

    private String resourceName;

    private ClassLoader contextClassLoader;

    /**
     * Constructs a template instance without the resource name.
     */
    public Template() {
        this(null);
    }

    /**
     * Constructs a template instance with the specified resource name.
     * 
     * @param resourceName resource name
     */
    public Template(String resourceName) {
        setResourceName(resourceName);
    }

    /**
     * Returns this template's resource name.
     * 
     * @return resource name
     */
    public String getResourceName() {
        return resourceName;
    }

    /**
     * Defines this template's resource name.
     * 
     * @param resourceName new resource name
     */
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    /**
     * Returns this template's class loader to find/load resources.
     * 
     * @return class loader
     */
    public ClassLoader getContextClassLoader() {
        if (contextClassLoader == null) {
            contextClassLoader = Thread.currentThread().getContextClassLoader();
        }
        return contextClassLoader;
    }

    /**
     * Defines this template's class loader to find/load resources.
     * 
     * @param contextClassLoader new class loader
     */
    public void setContextClassLoader(ClassLoader contextClassLoader) {
        this.contextClassLoader = contextClassLoader;
    }

    public void serialize(WebSerializer serializer) {
        PrintWriter writer = serializer.getWriter();
        writer.println(TAG_START);
        InputStream in = getContextClassLoader().getResourceAsStream(resourceName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            try {
                boolean startFound = false;
                String line = reader.readLine();
                while (line != null) {
                    if (startFound) {
                        if (line.contains(TAG_END)) {
                            line = line.substring(0, line.lastIndexOf(TAG_END));
                            if (line.length() > 0) {
                                writer.println(line);
                            }
                            break;
                        }
                        writer.println(line);
                    } else {
                        if (line.contains(TAG_START)) {
                            line = line.substring(line.indexOf(TAG_START) + TAG_START.length());
                            if (line.length() > 0) {
                                writer.println(line);
                            }
                            startFound = true;
                        }
                    }
                    line = reader.readLine();
                }
            } finally {
                reader.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writer.println(TAG_END);
    }

    public String toString() {
        String result = getResourceName();
        if (result == null) {
            result = super.toString();
        }
        return result;
    }
}
