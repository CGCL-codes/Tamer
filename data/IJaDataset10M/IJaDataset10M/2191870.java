package com.sl.eventlog.core.engine;

import com.sl.eventlog.domain.hostform.Host;
import com.sl.eventlog.domain.hostform.HostDetail;
import com.sl.eventlog.service.readlog.HostBean;
import com.sl.eventlog.service.readlog.LogPojo;
import com.sl.eventlog.service.readlog.LogReadBean;
import com.sl.eventlog.service.readlog.ReadLogException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.List;

public class ReadEventLogServiceImpl implements ReadEventLogService {

    private Log log = LogFactory.getLog(ReadEventLogServiceImpl.class);

    private LogReadBean logReadBean;

    public ReadEventLogServiceImpl(String remoteName, String loginName, String passWord) {
        this.logReadBean = new LogReadBean(remoteName, loginName, passWord);
    }

    public boolean chekConnect(String remoteName, String loginName, String passWord) throws ReadLogException {
        return logReadBean.chekConnect(remoteName, loginName, passWord);
    }

    public List<LogPojo> readLogList(String remoteName, String logFenlei) throws ReadLogException {
        return logReadBean.readLogList(remoteName, logFenlei);
    }

    public List<LogPojo> readLogList(Host host) throws ReadLogException {
        log.debug("readLogList from " + host.getIpAdress() + " start ");
        HostDetail hostDetail = host.getHostDetail();
        Long lastMsgTime = hostDetail.getLastMsgTime();
        if (lastMsgTime == null) {
            lastMsgTime = hostDetail.getNextScanTime() - hostDetail.getIntervalTimes();
        }
        List<LogPojo> pojos = logReadBean.readLogList(logReadBean.getRemoteName(), "Application", lastMsgTime);
        pojos.addAll(logReadBean.readLogList(logReadBean.getRemoteName(), "System", lastMsgTime));
        log.debug("readLogList end");
        return pojos;
    }

    public LogPojo readLog(String remoteName, String logFenlei, long eventId) throws ReadLogException {
        return logReadBean.readLog(remoteName, logFenlei, eventId);
    }

    public long getTheOldIndexId(String remoteName, String logFenlei) throws ReadLogException {
        return logReadBean.getTheOldIndexId(remoteName, logFenlei);
    }

    public long getTheNumberOfEvent(String remoteName, String logFenlei) throws ReadLogException {
        return logReadBean.getTheNumberOfEvent(remoteName, logFenlei);
    }

    public List<HostBean> listHostName() {
        return logReadBean.listHostName();
    }
}
