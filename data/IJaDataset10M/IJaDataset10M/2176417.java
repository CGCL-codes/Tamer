package net.sf.edevtools.tools.logviewer.core.sources;

import java.util.HashMap;
import java.util.Map;
import net.sf.edevtools.tools.logviewer.core.exceptions.EDvTLogViewerException;
import net.sf.edevtools.tools.logviewer.core.model.ILogSummary;
import net.sf.edevtools.tools.logviewer.core.model.ILogViewCoreConstants;
import net.sf.edevtools.tools.logviewer.core.parsers.LogParserManager;
import net.sf.edevtools.tools.logviewer.core.sources.file.LogSourceFile;
import net.sf.edevtools.tools.logviewer.core.sources.socket.LogSourceSocket;

/**
 * TODO comment
 * 
 * @author Christoph Graupner
 * @since 0.1.0
 * @version 0.1.0
 */
public abstract class LogSourceFactory {

    public static final int TYPE_XML = 1 << 10;

    public static final int TYPE_STD = 1 << 11;

    protected static LogSourceFactory sfInstance;

    public static LogSourceFactory getInstance() {
        if (sfInstance == null) {
            sfInstance = new LogSourceFactory() {
            };
        }
        return sfInstance;
    }

    protected LogSourceFactory() {
    }

    /**
	 * TODO comment
	 * 
	 * @param aLogFrameworkID
	 * @param aSourceType
	 * @return
	 * @throws EDvTLogViewerException
	 *           If any configuration error occurs in created {@link LogSourceFile} or
	 *           {@link LogSourceSocket}.
	 */
    public ILogSource createLogSource(ILogSummary aTargetLog, String aLogFrameworkID, int aSourceType, String aFormatID, Map<String, Object> aProperties) throws EDvTLogViewerException {
        if (aProperties == null) aProperties = new HashMap<String, Object>(2);
        aProperties.put(ILogSource.PROP_LOGFRAMEWORKID, aLogFrameworkID);
        aProperties.put(ILogSource.PROP_LOGFORMATID, aFormatID);
        if (ILogViewCoreConstants.LOGFRAMEWORK_JUTIL.equals(aLogFrameworkID)) {
            ILogSource ret = null;
            switch(aSourceType) {
                case ILogSource.TYPE_SOCKET:
                    ret = new LogSourceSocket(aTargetLog, aProperties);
                    break;
                case ILogSource.TYPE_FILE:
                    ret = new LogSourceFile(aTargetLog, aProperties);
                    break;
            }
            if (ret != null) {
                ret.setTransformerFactory(LogParserManager.getInstance().getFactory(aLogFrameworkID, aFormatID));
            }
            return ret;
        } else if (ILogViewCoreConstants.LOGFRAMEWORK_LOG4J.equals(aLogFrameworkID)) {
        }
        return null;
    }
}
