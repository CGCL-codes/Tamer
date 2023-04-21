package net.videgro.oma.managers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import net.videgro.oma.domain.LogActivity;
import net.videgro.oma.domain.LogDiff;
import net.videgro.oma.persistence.ILogManagerDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@SuppressWarnings("serial")
public class LogManager implements Serializable {

    protected final Log logger = LogFactory.getLog(getClass());

    public LogManager() {
        super();
    }

    private ILogManagerDao logManagerDao;

    public void setLogManagerDao(ILogManagerDao logManagerDao) {
        this.logManagerDao = logManagerDao;
    }

    public void save(LogActivity logActivity) {
        if (!logActivity.getFromIp().isEmpty()) {
            logManagerDao.save(logActivity);
        }
    }

    public void save(LogDiff logDiff) {
        logManagerDao.save(logDiff);
    }

    public void save(ArrayList<LogDiff> diffs) {
        Iterator<LogDiff> iterator = diffs.iterator();
        while (iterator.hasNext()) {
            LogDiff logDiff = (LogDiff) iterator.next();
            this.save(logDiff);
        }
    }
}
