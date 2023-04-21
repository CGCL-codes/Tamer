package earutils.ant;

import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;
import earutils.expander.*;

/**
 * 
 * @author Sean C. Sullivan
 *  
 */
public class EarExpanderTask extends Task {

    private String earFilename;

    private String destDirectory;

    private boolean verbose = false;

    public void setEarFilename(String name) {
        earFilename = name;
    }

    public void execute() throws BuildException {
        validate();
        expandEAR();
    }

    public void setVerbose(boolean b) {
        verbose = b;
    }

    protected void expandEAR() {
        Expander expander = new Expander();
        if (verbose) {
            expander.addExpanderListener(new SimpleExpanderListener(verbose));
        }
        try {
            expander.expand(earFilename);
        } catch (Exception ex) {
            throw new BuildException(ex);
        }
    }

    public void setDestDir(String name) {
        destDirectory = name;
    }

    protected void validate() throws BuildException {
        if ((earFilename == null) || (earFilename.length() < 1)) {
            throw new BuildException("earfilename is required");
        }
        if ((destDirectory == null) || (destDirectory.length() < 1)) {
            throw new BuildException("destdir is required");
        }
    }
}
