package com.ttdev.centralwire;

import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("serversService")
@Transactional
public class DefaultServersService implements ServersService {

    private static final Logger logger = LoggerFactory.getLogger(DefaultServersService.class);

    @Autowired
    private Servers servers;

    @Autowired
    private Reports reports;

    @Autowired
    private ReportExpiry reportExpiry;

    @Autowired
    private TripwireExecutor tripwireExecutor;

    public void setServers(Servers servers) {
        this.servers = servers;
    }

    public void setReports(Reports reports) {
        this.reports = reports;
    }

    public void setTripwireExecutor(TripwireExecutor tripwireExecutor) {
        this.tripwireExecutor = tripwireExecutor;
    }

    public void acceptingChanges(final Server s) {
        logger.info("Accepting changes for server {}", s.getHostname());
        tripwireExecutor.acceptChanges(s);
    }

    public Status getStatus(Server s) {
        try {
            Report r = reports.getFor(s);
            return r == null ? Status.ERROR_OBTAINING_REPORT : r.getStatus(reportExpiry.getLowerBound());
        } catch (RuntimeException e) {
            logger.warn("Got exception", e);
            return Status.ERROR_OBTAINING_REPORT;
        }
    }

    public List<Server> getAllServers() {
        return servers.getAll();
    }

    public void obtainReport(Server s) {
        logger.info("Obtaining report for server {}", s.getHostname());
        Report r = tripwireExecutor.obtainReport(s);
        reports.add(r);
    }

    public void add(Server s) {
        logger.info("Adding server {}", s.getHostname());
        if (servers.exists(s.getHostname())) {
            throw new DuplicateHostnameException(s.getHostname());
        }
        servers.add(s);
    }

    public void update(Server s) {
        logger.info("Updating server {}", s.getHostname());
        try {
            Server loaded = servers.findByName(s.getHostname());
            if (!loaded.getId().equals(s.getId())) {
                throw new DuplicateHostnameException(s.getHostname());
            }
        } catch (EmptyResultDataAccessException e) {
        }
        servers.update(s);
    }

    public void delete(Server s) {
        logger.info("Deleting server {}", s.getHostname());
        Server current = servers.update(s);
        Report r = reports.getFor(current);
        if (r != null) {
            logger.info("Deleting the associated report {}", r.getReportFileName());
            reports.delete(r);
        }
        servers.delete(current);
    }

    @Override
    public Report getReport(Server s) {
        Report r = reports.getFor(s);
        return r;
    }

    @Override
    public String getReportContent(Server s) {
        return reports.getFor(s).getContentAsString();
    }

    @Override
    public int getNoServers() {
        return servers.getNoServers();
    }

    @Override
    public Iterator<Server> getServersInRange(int first, int count) {
        return servers.getServersInRange(reportExpiry, first, count).iterator();
    }
}
