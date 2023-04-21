package verinec.adaptation;

import verinec.VerinecException;
import verinec.util.ProgramExec;
import verinec.util.VerinecNamespaces;
import org.jdom.Element;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.util.logging.Logger;

/** Writes files in the local filesystem using java.io.File
  * todo: support for sudo 
  *
  * @author david.buchmann at unifr.ch
  * @version $Revision: 939 $
  */
public class DistCP implements IDistributor {

    private String prefix;

    /** Set the target for copy operation.
      * There is only the optional attribute prefix path to prepend to the filename.
      *
      * @param target a cp target according to the schema.
      */
    public void setTarget(Element target) {
        Element cp = target.getChild("cp", VerinecNamespaces.NS_TRANSLATION);
        prefix = cp.getAttributeValue("prefix");
    }

    /** Execute the distribution action, that is write the file specified in config to their filename.
      * 
      * @param config A result-file according to the schema.
      * @throws VerinecException If the config is not a result-file or the file can not be written.
      */
    public void distribute(Element config) throws VerinecException {
        if (!config.getName().equals("result-file")) {
            throw new VerinecException("The copy target can only be used for result-file output, not for " + config.getName());
        }
        File targFile = new File(prefix, config.getAttributeValue("filename"));
        if (targFile.exists()) {
            if (!targFile.delete()) throw new VerinecException("Failed to delete " + targFile + " before writing the new config file.");
        }
        try {
            targFile.getParentFile().mkdirs();
            FileWriter f = new FileWriter(targFile);
            f.write(config.getText());
            f.close();
        } catch (IOException ioe) {
            throw new VerinecException("Could not write out result file.", ioe);
        }
    }

    /** Execute a command using verinec.util.ProgramExec
      * 
      * @param command A command element of the pre- or post-process for the configuration.
      * @return Null if successful, a String containing the error message on failure of the executed program if the command has continueOnError. (Otherwise an execption is thrown on error.)
      * @throws VerinecException If the command execution fails (and continueOnError is false) or if the underlying communication protocol fails.
      */
    public String execute(Element command) throws VerinecException {
        String exec = command.getAttributeValue("execute");
        String strCOE = command.getAttributeValue("continueOnError");
        boolean continueOnError = (strCOE == null) ? false : strCOE.equalsIgnoreCase("true");
        String redirFile = command.getAttributeValue("redirectFile");
        int exitcode;
        OutputStream out = null;
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        if (redirFile != null) {
            try {
                File file = new File(redirFile);
                if (!file.getParentFile().exists()) if (!file.getParentFile().mkdirs()) throw new VerinecException("Could not create the necessary directories for the command output file " + redirFile);
                out = new FileOutputStream(file);
            } catch (IOException e) {
                throw new VerinecException("Error creating command output to file");
            }
        }
        try {
            exitcode = ProgramExec.execSync(exec, null, null, out, err, 0);
            if (exitcode == 0) {
                out.write(err.toByteArray());
            }
        } catch (InterruptedException ie) {
            throw new VerinecException("Execution of " + exec + " has been interrupted.", ie);
        } catch (IOException ioe) {
            throw new VerinecException("Could not find " + exec + ".", ioe);
        } finally {
            try {
                if (out != null) out.close();
            } catch (IOException ioe) {
                Logger.getLogger(getClass().getName()).warning("Failed closing file " + redirFile + " " + ioe.getMessage());
                Logger.getLogger(getClass().getName()).throwing(getClass().getName(), "execute", ioe);
            }
        }
        if (exitcode != 0) {
            if (continueOnError) {
                return err.toString();
            } else {
                throw new VerinecException("Executing " + exec + " failed with:\n" + err.toString());
            }
        }
        return null;
    }
}
