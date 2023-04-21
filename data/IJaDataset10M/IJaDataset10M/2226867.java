package net.sf.profiler4j.console.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import java.util.Map;
import net.sf.profiler4j.agent.ServerUtil;

/**
 * CLASS_COMMENT
 * 
 * @author Antonio S. R. Gomes
 */
public class RuntimeInfo {

    private String bootClassPath;

    private String classPath;

    private List<String> inputArguments;

    private String libraryPath;

    private String name;

    private String vmName;

    private long startTime;

    private long upTime;

    private Map<String, String> systemProperties;

    public String getBootClassPath() {
        return this.bootClassPath;
    }

    public String getClassPath() {
        return this.classPath;
    }

    public List<String> getInputArguments() {
        return this.inputArguments;
    }

    public String getLibraryPath() {
        return this.libraryPath;
    }

    public String getName() {
        return this.name;
    }

    public long getStartTime() {
        return this.startTime;
    }

    public Map<String, String> getSystemProperties() {
        return this.systemProperties;
    }

    public long getUpTime() {
        return this.upTime;
    }

    public String getVmName() {
        return this.vmName;
    }

    public static RuntimeInfo read(ObjectInputStream in) throws IOException {
        RuntimeInfo ri = new RuntimeInfo();
        ri.bootClassPath = in.readUTF();
        ri.classPath = in.readUTF();
        ri.inputArguments = ServerUtil.readStringList(in);
        ri.libraryPath = in.readUTF();
        ri.name = in.readUTF();
        ri.vmName = in.readUTF();
        ri.startTime = in.readLong();
        ri.upTime = in.readLong();
        ri.systemProperties = ServerUtil.readStringMap(in);
        return ri;
    }
}
