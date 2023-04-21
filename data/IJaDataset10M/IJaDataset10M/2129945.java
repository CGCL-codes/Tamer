package org.sourceforge.fortify;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Do fortify scan goal.
 * 
 * @goal scan
 */
public class FortifyScanMojo extends FortifyParseMojo {

    public void execute() throws MojoExecutionException {
        super.execute();
        getLog().info("FORTIFY SCAN START");
        fortifyScan();
        getLog().info("FORTIFY SCAN END");
    }

    public void fortifyScan() throws MojoExecutionException {
        execCommand(new String[] { getFortifyExec(), "-b", getBuildId(), "-logfile", getLogName() + "-scan.log", "-f", getBuildId() + ".fpr", "-Xmx" + getMaxHeap(), "-scan", "-verbose" });
    }
}
