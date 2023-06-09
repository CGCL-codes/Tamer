package org.apache.hadoop.net;

import java.util.*;
import java.io.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.util.*;
import org.apache.hadoop.util.Shell.ShellCommandExecutor;
import org.apache.hadoop.conf.*;

/**
 * This class implements the {@link DNSToSwitchMapping} interface using a 
 * script configured via topology.script.file.name .
 */
public final class ScriptBasedMapping extends CachedDNSToSwitchMapping implements Configurable {

    public ScriptBasedMapping() {
        super(new RawScriptBasedMapping());
    }

    public ScriptBasedMapping(Configuration conf) {
        this();
        setConf(conf);
    }

    public Configuration getConf() {
        return ((RawScriptBasedMapping) rawMapping).getConf();
    }

    public void setConf(Configuration conf) {
        ((RawScriptBasedMapping) rawMapping).setConf(conf);
    }

    private static final class RawScriptBasedMapping implements DNSToSwitchMapping {

        private String scriptName;

        private Configuration conf;

        private int maxArgs;

        private static Log LOG = LogFactory.getLog(ScriptBasedMapping.class);

        public void setConf(Configuration conf) {
            this.scriptName = conf.get("topology.script.file.name");
            this.maxArgs = conf.getInt("topology.script.number.args", 100);
            this.conf = conf;
        }

        public Configuration getConf() {
            return conf;
        }

        public RawScriptBasedMapping() {
        }

        public List<String> resolve(List<String> names) {
            List<String> m = new ArrayList<String>(names.size());
            if (names.isEmpty()) {
                return m;
            }
            if (scriptName == null) {
                for (int i = 0; i < names.size(); i++) {
                    m.add(NetworkTopology.DEFAULT_RACK);
                }
                return m;
            }
            String output = runResolveCommand(names);
            if (output != null) {
                StringTokenizer allSwitchInfo = new StringTokenizer(output);
                while (allSwitchInfo.hasMoreTokens()) {
                    String switchInfo = allSwitchInfo.nextToken();
                    m.add(switchInfo);
                }
            }
            return m;
        }

        private String runResolveCommand(List<String> args) {
            int loopCount = 0;
            if (args.size() == 0) {
                return null;
            }
            StringBuffer allOutput = new StringBuffer();
            int numProcessed = 0;
            while (numProcessed != args.size()) {
                int start = maxArgs * loopCount;
                List<String> cmdList = new ArrayList<String>();
                cmdList.add(scriptName);
                for (numProcessed = start; numProcessed < (start + maxArgs) && numProcessed < args.size(); numProcessed++) {
                    cmdList.add(args.get(numProcessed));
                }
                File dir = null;
                String userDir;
                if ((userDir = System.getProperty("user.dir")) != null) {
                    dir = new File(userDir);
                }
                ShellCommandExecutor s = new ShellCommandExecutor(cmdList.toArray(new String[0]), dir);
                try {
                    s.execute();
                    allOutput.append(s.getOutput() + " ");
                } catch (Exception e) {
                    LOG.warn(StringUtils.stringifyException(e));
                    return null;
                }
                loopCount++;
            }
            return allOutput.toString();
        }
    }
}
