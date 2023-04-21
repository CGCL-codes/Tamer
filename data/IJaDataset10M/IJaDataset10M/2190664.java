package com.mindtree.techworks.insight.releng.mvn.nsis.mojos;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import com.mindtree.techworks.insight.releng.mvn.nsis.actions.NsisAction;

/**
 * Generates A NSIS Installer for the project.
 * 
 * @see org.apache.maven.plugin.AbstractMojo
 * 
 * @author <a href="mailto:bindul_bhowmik@mindtree.com">Bindul Bhowmik</a>
 * @version $Revision: 114 $ $Date: 2008-01-13 04:34:38 -0500 (Sun, 13 Jan 2008) $
 * 
 * @aggregator
 * @goal compile
 * @phase package
 * @requiresDependencyResolution test
 * @requiresDependencyResolution compile
 * @requiresDependencyResolution runtime
 * @description Compiles the Nsis installer for the project
 */
public class NsisCompileMojo extends AbstractNsisMojo {

    /**
	 * The action to generate the NSIS Script
	 * 
	 * @component role-hint="script-generate"
	 * @required
	 * @readonly
	 */
    private NsisAction scriptGenerateAction;

    /**
	 * The action to Compile the NSIS Script
	 * 
	 * @component role-hint="compile"
	 * @required
	 * @readonly
	 */
    private NsisAction nsisCompileAction;

    /**
	 * The action to Copy resources for NSIS Compilation
	 * 
	 * @component role-hint="copy-resources"
	 * @required
	 * @readonly
	 */
    private NsisAction copyResourcesAction;

    public void executeNsisMojo() throws MojoExecutionException, MojoFailureException {
        scriptGenerateAction.execute(this);
        copyResourcesAction.execute(this);
        nsisCompileAction.execute(this);
    }
}
