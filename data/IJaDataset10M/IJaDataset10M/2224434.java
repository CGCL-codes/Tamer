package com.mindtree.techworks.insight.releng.mvn.nsis.actions.resolver;

import java.io.File;
import java.util.List;
import com.mindtree.techworks.insight.releng.mvn.nsis.actions.MojoInfo;
import com.mindtree.techworks.insight.releng.mvn.nsis.actions.NsisActionExecutionException;
import com.mindtree.techworks.insight.releng.mvn.nsis.model.SetBase;

/**
 * Resolves the file set and other dependencies.
 * 
 * @author <a href="mailto:bindul_bhowmik@mindtree.com">Bindul Bhowmik</a>
 * @version $Revision: 89 $ $Date: 2008-01-07 03:15:48 -0500 (Mon, 07 Jan 2008) $
 * 
 */
public interface Resolver {

    /**
	 * Gets the list of file names for the script generation
	 */
    public List getRelativeFilePath(SetBase setBase, MojoInfo mojoInfo) throws NsisActionExecutionException;

    /**
	 * Copies the files to the temp directory
	 */
    public void copyFiles(SetBase setBase, MojoInfo mojoInfo, File archiveTempDir) throws NsisActionExecutionException;
}
