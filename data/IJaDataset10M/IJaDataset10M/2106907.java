package org.doxygen.tools;

import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.util.List;
import java.util.ArrayList;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Execute;
import org.apache.tools.ant.taskdefs.PumpStreamHandler;

/**
 * This class is responsible for forking doxygen process. And capturing output
 * from the same.
 * 
 * @version $Revision: 1.1.2.3 $
 * 
 * @since Ant-Doxygen 1.3.1
 */
public class DoxygenProcess {

    DoxygenProcess(Project _project) {
        this.antProject = _project;
    }

    /**
	 * This method specifies the path to Doxygen. Users are allowed (encouraged)
	 * to use "doxygen" by default, but they are also allowed to specify
	 * something that is specific to their environment.
	 * 
	 * @param path
	 *            either absolute or relative to the Doxygen executable.
	 * 
	 */
    public final void setDoxygenPath(final String path) {
        doxygenPath = path;
    }

    /**
	 * This method ensures that we have a supported version of Doxygen in the
	 * environment. This Ant task knows many of the attributes in the minimum
	 * version. Attempts to specify items that the current Doxygen does not
	 * understand will be mysteriously ignored (Ant user perspective).
	 * Therefore, a check is made to highlight any potential version
	 * incompatibilities.
	 * 
	 * Ideally, each Ant task attribute would be version aware and collectively
	 * indicate which attributes are not supported by the underlying Doxygen
	 * executable. (But the code is not there yet.)
	 * 
	 * @param versionCompatibility
	 *            low-water mark as specified by the project file. Any detected
	 *            Doxygen version that is below this low-water mark will issue a
	 *            BuildException.
	 * 
	 * @test the Ant task should fail if the detected Doxygen version is too
	 *       old.
	 * 
	 * @test the Ant task should whine, but not fail if the user claims
	 *       ignoreVersion="true" and the detected Doxygen version is too old.
	 * 
	 * @test the Ant task should not fail if the detected Doxygen version is
	 *       minimally supported.
	 */
    public final void checkVersion(final String versionCompatibility) {
        if (versionCompatibility == null) {
            return;
        }
        try {
            List<String> args = new ArrayList<String>();
            args.add("--version");
            String fileVersion = invokeDoxygen(args);
            DoxygenTask.activityLog(false, "Detected Doxygen version:" + fileVersion + "  " + "Project requires version: " + versionCompatibility);
            DoxygenVersion systemVersion = new DoxygenVersion(fileVersion);
            if (!systemVersion.isCompatible(versionCompatibility)) {
                String message = "Detected Doxygen version does not satisfy " + "project requirements.\n";
                throw new BuildException(message);
            }
        } catch (NumberFormatException nfe) {
            throw new BuildException("Unable to detect Doxygen version.", nfe);
        }
    }

    /**
	 * This method executes the doxygen command line utility to generate the
	 * skeleton configuration file.
	 * 
	 * @test the amalgamated configuration file is generated in
	 *       System.getProperty("user.home").
	 * 
	 * @test The amalgamated configuration file basis can be controlled by the
	 *       Ant task user.
	 * 
	 * @param theConfigFilename
	 *            used by Doxygen.
	 * 
	 */
    public final void createConfig(final String theConfigFilename) {
        List<String> args = new ArrayList<String>();
        args.add("-s");
        args.add("-g");
        args.add(theConfigFilename);
        invokeDoxygen(args);
    }

    /**
	 * Execute the Doxygen against the specified amalgamated configuration file.
	 * 
	 * @param theConfigFilename
	 *            to be used by Doxygen.
	 */
    public final void executeDoxygenConfig(final String theConfigFilename) {
        List<String> args = new ArrayList<String>();
        args.add(theConfigFilename);
        invokeDoxygen(args);
    }

    /**
	 * This method invokes the Doxygen executable.
	 * 
	 * @test configuration file names with embedded spaces are protected with
	 *       surrounding double quotes.
	 * 
	 * @test output from the real invocation of Doxygen should be conditionally
	 *       included in the output from this Ant task.
	 * 
	 * @test Each invocation of Doxygen should be visible in the output from
	 *       this Ant task.
	 * 
	 * @param args
	 *            List of arguments to be passed to doxygen. The individual
	 *            elements are string.
	 * 
	 * @return a <code>String</code> value containing the the Doxygen
	 *         invocation/execution output.
	 */
    private String invokeDoxygen(final List<String> args) {
        String[] arguments = new String[1 + args.size()];
        StringBuilder res = new StringBuilder();
        arguments[0] = doxygenPath;
        try {
            Execute doxygen = new Execute(new PumpStreamHandler(new FileOutputStream(DOXY_TEMP)));
            if (antProject != null) {
                doxygen.setWorkingDirectory(this.antProject.getBaseDir());
            }
            StringBuilder sb = new StringBuilder("Doxygen command: " + arguments[0] + " ");
            int i = 1;
            for (String arg : args) {
                if (arg.indexOf(" ") != -1) {
                    arg = "\"" + arg + "\"";
                }
                arguments[i] = arg;
                sb.append(arguments[i] + " ");
                ++i;
            }
            DoxygenTask.activityLog(true, sb.toString());
            doxygen.setCommandline(arguments);
            doxygen.execute();
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(DOXY_TEMP)));
            String line = br.readLine();
            while (line != null) {
                res.append(line);
                line = br.readLine();
                DoxygenTask.activityLog(false, line);
            }
            return res.toString();
        } catch (IOException ioe) {
            throw new BuildException("Doxygen not found on the PATH.", ioe);
        }
    }

    /**
	 * Name of the temporary file to get the version of Doxygen installed on
	 * system. ${user.home} used as temporary file.
	 * 
	 */
    public static String DOXY_TEMP = "";

    static {
        try {
            DOXY_TEMP = File.createTempFile("doxygen", ".log").getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /** This is the default path to the Doxygen executable. */
    private String doxygenPath = "doxygen";

    /**
	 * Reference to the Ant project file. This will be used to extract the
	 * implicit project name.
	 */
    private Project antProject = null;
}
