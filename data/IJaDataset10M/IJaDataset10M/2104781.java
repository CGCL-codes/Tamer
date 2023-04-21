package chaski.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Qin Gao
 *
 */
public class ExternalProgramExecuter extends Thread {

    protected static final Log LOG = LogFactory.getLog(ExternalProgramExecuter.class);

    private String executable;

    private List<String> params = new LinkedList<String>();

    private OutputStream redirStdout;

    private OutputStream redirStderr;

    private InputStream redirStdin;

    private Map<String, String> env = new HashMap<String, String>();

    private String workingdir = ".";

    private int retCode;

    public ExternalProgramExecuter withWorkingDir(String dir) {
        workingdir = dir;
        return this;
    }

    public ExternalProgramExecuter withExecutable(String exe) {
        executable = exe;
        return this;
    }

    public ExternalProgramExecuter withParam(String param) {
        if (params == null) return this;
        params.add(param);
        return this;
    }

    public ExternalProgramExecuter withParams(String param) {
        if (params == null) return this;
        String[] ps = param.split("\\s+");
        return withParams(ps);
    }

    public ExternalProgramExecuter withParams(String[] ps) {
        if (ps == null) return this;
        for (String p : ps) params.add(p);
        return this;
    }

    public ExternalProgramExecuter withStdIn(InputStream stdin) {
        redirStdin = stdin;
        return this;
    }

    public ExternalProgramExecuter withStdOut(OutputStream stdout) {
        redirStdout = stdout;
        return this;
    }

    public ExternalProgramExecuter withStdErr(OutputStream stderr) {
        redirStderr = stderr;
        return this;
    }

    public ExternalProgramExecuter clearConf() {
        redirStdout = redirStderr = null;
        redirStdin = null;
        executable = null;
        params.clear();
        env.clear();
        env.putAll(System.getenv());
        return this;
    }

    public ExternalProgramExecuter withCleanEnv() {
        env.clear();
        return this;
    }

    public ExternalProgramExecuter withInheritedEnv() {
        env.clear();
        env.putAll(System.getenv());
        return this;
    }

    public ExternalProgramExecuter withEnv(String key, String value) {
        env.put(key, value);
        return this;
    }

    public ExternalProgramExecuter withEnv(String exp) {
        int pos = exp.indexOf("=");
        if (pos == -1) env.put(exp, ""); else env.put(exp.substring(0, pos), exp.substring(pos + 1));
        return this;
    }

    @Override
    public void run() {
        Runtime rt = Runtime.getRuntime();
        String[] param = new String[params.size() + 1];
        param[0] = executable;
        int i = 1;
        for (String s : params) {
            param[i++] = s;
        }
        String[] en = new String[env.size()];
        i = 0;
        for (Map.Entry<String, String> ent : env.entrySet()) {
            en[i++] = ent.getKey() + "=" + ent.getValue();
        }
        try {
            Process process = rt.exec(param, en, new File(workingdir));
            ThreadedStreamPipe pipestdin = new ThreadedStreamPipe(redirStdin, process.getOutputStream());
            pipestdin.setDaemon(true);
            pipestdin.start();
            ThreadedStreamPipe pipestdout = new ThreadedStreamPipe(process.getInputStream(), redirStdout);
            pipestdout.setDaemon(true);
            pipestdout.start();
            ThreadedStreamPipe pipestderr = new ThreadedStreamPipe(process.getErrorStream(), redirStderr);
            pipestderr.setDaemon(true);
            pipestderr.start();
            try {
                retCode = process.waitFor();
                Thread.sleep(100);
                pipestdin.cancel();
                pipestdout.cancel();
                pipestderr.cancel();
                Thread.sleep(3000);
                pipestdin.interrupt();
                pipestdout.interrupt();
                pipestderr.interrupt();
            } catch (InterruptedException e) {
                retCode = -1;
            }
        } catch (IOException e) {
            LOG.error("Error starting the process! ", e);
        }
    }
}
