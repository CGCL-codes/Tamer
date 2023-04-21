package com.mindtree.techworks.insight.releng.mvn.nsis.io;

import java.io.File;
import java.io.IOException;
import com.mindtree.techworks.insight.releng.mvn.nsis.model.NsisProject;

/**
 * Interface for project file reader implementations.
 *
 * @author <a href="mailto:bindul_bhowmik@mindtree.com">Bindul Bhowmik</a>
 * @version $Revision: 87 $ $Date: 2007-12-28 06:19:31 -0500 (Fri, 28 Dec 2007) $
 */
public interface ProjectFileReader {

    /**
	 * Reads the project file
	 * @param nsisProjectFile 
	 * @return
	 * @throws IOException
	 */
    public NsisProject readProject(File nsisProjectFile) throws IOException;
}
