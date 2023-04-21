package net.sf.refactorit.utils;

import net.sf.refactorit.ui.RuntimePlatform;
import org.apache.log4j.RollingFileAppender;
import java.io.File;

/**
 * RefactorItLogAppender -- appends log to refactorit.home/refactorit.log
 * Configuration in log4j.xml
 * 
 * @author <a href="mailto:tonis.vaga@aqris.com>Tonis Vaga</a>
 * @version $Revision: 1.1 $ $Date: 2005/12/09 12:03:19 $
 */
public class RefactorItLogAppender extends RollingFileAppender {

    public RefactorItLogAppender() {
        setFile(getLogFileLocation());
    }

    public static String getLogFileLocation() {
        return RuntimePlatform.getConfigDir() + File.separator + "refactorit.log";
    }
}
