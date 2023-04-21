package net.sourceforge.ivi.core.dfaaFactory;

import java.io.File;
import net.sourceforge.ivi.core.dfaa.IDFAA;
import net.sourceforge.ivi.core.iviException;

public interface IDFAAFactory {

    /**
	 * 
	 */
    IDFAA createDFAAFromFile(File file) throws iviException;
}
