package com.entelience.soap;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Constructor;
import com.entelience.util.Config;
import com.entelience.util.PageHelper;
import com.entelience.sql.Db;
import com.entelience.objects.raci.RaciException;
import com.entelience.objects.FileImportHistory;
import com.entelience.objects.PageCounter;
import com.entelience.objects.chart.TimeSeries;
import com.entelience.objects.metrics.KeyMetricDetail;
import com.entelience.objects.mim.AnomalyStats;
import com.entelience.objects.mim.AslStatsElement;
import com.entelience.objects.mim.SnailData;
import com.entelience.objects.mim.SnailDataJsp;
import com.entelience.objects.mim.SnailPortion;
import com.entelience.objects.mim.UserDetail;
import com.entelience.objects.mim.UserMetrics;
import com.entelience.metrics.Metric;
import com.entelience.metrics.MetricFactory;
import com.entelience.metrics.MetricReports;
import com.entelience.raci.module.RaciIM;

/**
 * implement interface for soap class IdentityManagement
 */
public class soapIdentityManagement extends soapBase {

    private static final int defaultSlidingSize = 365;

    public soapIdentityManagement() throws Exception, RaciException {
        super();
    }

    public soapIdentityManagement(Integer fakedPeopleId) throws Exception, RaciException {
        super(fakedPeopleId, (Integer) null);
    }

    protected String getModule() {
        return "com.entelience.module.IdentityManagement";
    }

    /**
     * Create an instance of the right MetricFactory subclass from esis.properties
     */
    private MetricFactory newMetricFactory(Db db) throws Exception, RaciException {
        String reportClassName = Config.getObjectProperty(db, getClass().getName(), "IMReportClassName", "com.entelience.report.usertools.compliancy.UserReport", null);
        Class clUserReport = Class.forName(reportClassName);
        Class clConstructor[] = { Class.forName("com.entelience.sql.Db") };
        Constructor cUserImport = clUserReport.getConstructor(clConstructor);
        Object oParams[] = { db };
        return (MetricFactory) cUserImport.newInstance(oParams);
    }

    /**
     * @deprecated (why ?)
     */
    @Deprecated
    public Boolean setSlidingWindowSize(Integer size) throws Exception, RaciException {
        try {
            _logger.info("IM : setSlidingWindowSize (" + size + ")");
            if (size.intValue() == 28 || size.intValue() == 365 || size.intValue() == 1) {
                SessionHandler.setKey("SlidingWindowSize", String.valueOf(size));
                return Boolean.TRUE;
            } else return Boolean.FALSE;
        } catch (Exception e) {
            store(e);
            throw e;
        }
    }

    /**
     * @deprecated (why?)
     */
    @Deprecated
    public Integer getSlidingWindowSize() throws Exception, RaciException {
        try {
            _logger.info("IM : getSlidingWindowSize ()");
            int slideSize = defaultSlidingSize;
            String size = SessionHandler.getKey("SlidingWindowSize");
            if (size == null) size = String.valueOf(defaultSlidingSize);
            try {
                slideSize = Integer.parseInt(size);
            } catch (Exception e) {
                slideSize = defaultSlidingSize;
            }
            if (slideSize == 28 || slideSize == 365 || slideSize == 1) return Integer.valueOf(slideSize); else return Integer.valueOf(defaultSlidingSize);
        } catch (Exception e) {
            store(e);
            throw e;
        }
    }

    /**
     * get the description of an anomaly with its anomaly_id
     */
    public String getAnomalyForAnomalyId(int anomaly_id) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getAnomalyForAnomalyId (" + anomaly_id + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            return metrics.lookUpForAnomaly(anomaly_id);
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get list of users that have failed in the import for a given anomaly
     * return an ArrayList of user_id
     */
    public String[] getFailedUsersForAnomaly(int anomaly_id) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getFailedUsersForAnomaly (" + anomaly_id + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            List<String> al = metrics.getFailedUsersForAnomaly(anomaly_id);
            String[] res = new String[al.size()];
            res = (String[]) al.toArray(res);
            return res;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get stats for user that have failed in the import
     * return an ArrayList of ArrayList composed with anomaly_id, description, and number of users concerned
     */
    public AnomalyStats[] getFailedUsersStats() throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getFailedUsersStats ()");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            List<AnomalyStats> al = metrics.getFailedUsersStats();
            AnomalyStats[] res = new AnomalyStats[al.size()];
            res = (AnomalyStats[]) al.toArray(res);
            return res;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get details for an user
     */
    public UserDetail getUserDetail(String user_id) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getUserDetail (" + user_id + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            return metrics.getUserDetail(user_id);
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get list of users with their metrics that are managed by an asl
     * we can filter for having only users that doesn't meet the targets (filter=true), or have all users
     */
    public UserMetrics[] getUsersForAslForPrint(String asl_id, String filter, int sws, String org, String geo) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getUsersForAslForPrint (" + asl_id + ", " + filter + "," + sws + "," + org + "," + geo + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            Metric[] mets = { mf.getMetric("dpc", sws), mf.getMetric("ddc", sws), mf.getMetric("dde", sws), mf.getMetric("dds", sws) };
            Boolean filtered;
            try {
                filtered = Boolean.valueOf(filter);
            } catch (Exception e) {
                filtered = Boolean.TRUE;
            }
            List<UserMetrics> elts = metrics.getUsersForAsl(asl_id, filtered.booleanValue(), mets, geo, org, null, null, null);
            UserMetrics[] ret = new UserMetrics[elts.size()];
            ret = (UserMetrics[]) elts.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public PageCounter getCountersForUsersForAsl(String asl_id, String filter, String geo, String org) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getCountersForUsersForAsl (" + asl_id + ", " + filter + "," + geo + "," + org + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] mets = { mf.getMetric("dpc", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("dds", slidingWindowSize) };
            Boolean filtered;
            try {
                filtered = Boolean.valueOf(filter);
            } catch (Exception e) {
                filtered = Boolean.TRUE;
            }
            return metrics.getCountersForUsersForAsl(asl_id, filtered.booleanValue(), mets, geo, org);
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get list of users with their metrics that are managed by an asl
     * we can filter for having only users that doesn't meet the targets (filter=true), or have all users
     */
    public UserMetrics[] getUsersForAsl(String asl_id, String filter, String geo, String org, String order, String way, Integer pageIn) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getUsersForAsl (" + asl_id + ", " + filter + ", " + geo + ", " + org + ", " + order + ", " + way + ", " + pageIn + ")");
            Integer page = pageIn;
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] mets = { mf.getMetric("dpc", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("dds", slidingWindowSize) };
            Boolean filtered;
            try {
                filtered = Boolean.valueOf(filter);
            } catch (Exception e) {
                filtered = Boolean.TRUE;
            }
            if (page == null) page = Integer.valueOf(1);
            List<UserMetrics> elts = metrics.getUsersForAsl(asl_id, filtered.booleanValue(), mets, geo, org, order, way, page);
            UserMetrics[] ret = new UserMetrics[elts.size()];
            ret = (UserMetrics[]) elts.toArray(ret);
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get nb pages for pop up user
     *
     */
    public Integer[] getAslStatsForGeo(String geoName, String filter) throws Exception, RaciException {
        Db db = dbRW();
        try {
            _logger.info("IM : getAslStatsForGeo (" + geoName + "," + filter + ")");
            db.begin();
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] mets = { mf.getMetric("dpc", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("dds", slidingWindowSize) };
            Boolean filtered;
            try {
                filtered = Boolean.valueOf(filter);
            } catch (Exception e) {
                filtered = Boolean.TRUE;
            }
            int nbRows = metrics.getNbRowsAslStatsForGeo(geoName, slidingWindowSize, mets, filtered.booleanValue());
            Integer[] ret = new Integer[2];
            ret[0] = Integer.valueOf(nbRows);
            ret[1] = Integer.valueOf(PageHelper.getNumberOfPages(db, nbRows));
            db.rollback();
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get stats for an administrator of a given org for print
     *
     */
    public AslStatsElement[] getAslStatsForGeoForPrint(String geoName, String order, String desc, String filter, int sws) throws Exception, RaciException {
        Db db = dbRW();
        try {
            _logger.info("IM : getAslStatsForGeoForPrint (" + geoName + ", " + order + ", " + desc + ", " + filter + "," + sws + ")");
            db.begin();
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            Metric[] mets = { mf.getMetric("dpc", sws), mf.getMetric("ddc", sws), mf.getMetric("dde", sws), mf.getMetric("dds", sws) };
            Boolean filtered;
            try {
                filtered = Boolean.valueOf(filter);
            } catch (Exception e) {
                filtered = Boolean.TRUE;
            }
            List<AslStatsElement> al;
            al = metrics.getAslStatsForGeo(geoName, sws, mets, null, order, desc, filtered.booleanValue());
            AslStatsElement[] ase = new AslStatsElement[al.size()];
            ase = (AslStatsElement[]) al.toArray(ase);
            db.rollback();
            return ase;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get stats for an administrator of a given geo
     *
     */
    public AslStatsElement[] getAslStatsForGeoListAsls(String geoName, int pageNumber, String order, String desc, String filter) throws Exception, RaciException {
        Db db = dbRW();
        try {
            _logger.info("IM : getAslStatsForGeoListAsls (" + geoName + ", " + pageNumber + ", " + order + ", " + desc + "," + filter + ")");
            db.begin();
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] mets = { mf.getMetric("dpc", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("dds", slidingWindowSize) };
            Boolean filtered;
            try {
                filtered = Boolean.valueOf(filter);
            } catch (Exception e) {
                filtered = Boolean.TRUE;
            }
            if (pageNumber <= 0) return null;
            List<AslStatsElement> al = metrics.getAslStatsForGeo(geoName, slidingWindowSize, mets, Integer.valueOf(pageNumber), order, desc, filtered.booleanValue());
            AslStatsElement[] ase = new AslStatsElement[al.size()];
            ase = (AslStatsElement[]) al.toArray(ase);
            db.rollback();
            return ase;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get nb pages for pop up user
     *
     */
    public Integer[] getAslStatsForOrg(String orgName, String filter) throws Exception, RaciException {
        Db db = dbRW();
        try {
            _logger.info("IM : getAslStatsForOrg (" + orgName + "," + filter + ")");
            db.begin();
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] mets = { mf.getMetric("dpc", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("dds", slidingWindowSize) };
            Boolean filtered;
            try {
                filtered = Boolean.valueOf(filter);
            } catch (Exception e) {
                filtered = Boolean.TRUE;
            }
            int nbRows = metrics.getNbRowsAslStatsForOrg(orgName, slidingWindowSize, mets, filtered.booleanValue());
            Integer[] ret = new Integer[2];
            ret[0] = Integer.valueOf(nbRows);
            ret[1] = Integer.valueOf(PageHelper.getNumberOfPages(db, nbRows));
            db.rollback();
            return ret;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get stats for an administrator of a given org for print
     *
     */
    public AslStatsElement[] getAslStatsForOrgForPrint(String orgName, String order, String desc, String filter, int sws) throws Exception, RaciException {
        Db db = dbRW();
        try {
            _logger.info("IM : getAslStatsForOrgForPrint (" + orgName + ", " + order + ", " + desc + ", " + filter + "," + sws + ")");
            db.begin();
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            Metric[] mets = { mf.getMetric("dpc", sws), mf.getMetric("ddc", sws), mf.getMetric("dde", sws), mf.getMetric("dds", sws) };
            Boolean filtered;
            try {
                filtered = Boolean.valueOf(filter);
            } catch (Exception e) {
                filtered = Boolean.TRUE;
            }
            List<AslStatsElement> al;
            al = metrics.getAslStatsForOrg(orgName, sws, mets, null, order, desc, filtered.booleanValue());
            AslStatsElement[] ase = new AslStatsElement[al.size()];
            ase = (AslStatsElement[]) al.toArray(ase);
            db.rollback();
            return ase;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get stats for an administrator of a given org
     *
     */
    public AslStatsElement[] getAslStatsForOrgListAsls(String orgName, int pageNumber, String order, String desc, String filter) throws Exception, RaciException {
        Db db = dbRW();
        try {
            _logger.info("IM : getAslStatsForOrgListAsls (" + orgName + ", " + pageNumber + ", " + order + ", " + desc + "," + filter + ")");
            db.begin();
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] mets = { mf.getMetric("dpc", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("dds", slidingWindowSize) };
            Boolean filtered;
            try {
                filtered = Boolean.valueOf(filter);
            } catch (Exception e) {
                filtered = Boolean.TRUE;
            }
            if (pageNumber <= 0) return null;
            List<AslStatsElement> al;
            al = metrics.getAslStatsForOrg(orgName, slidingWindowSize, mets, Integer.valueOf(pageNumber), order, desc, filtered.booleanValue());
            AslStatsElement[] ase = new AslStatsElement[al.size()];
            ase = (AslStatsElement[]) al.toArray(ase);
            db.rollback();
            return ase;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * Get a metric detail for childs of a organization at a given level.
     * example : orgName="", metricName="dde", level=1 : give us all dde metrics for level1 organizations
     * example : orgName="AC", metricName="dde", level=2 : give us all dde metrics for level2 AC sub-organizations
     * example : orgName="AC/DAIC", metricName="dde", level=3 : give us all dde metrics for level3 AC/DAIC sub-organizations
     * example : orgName="AC/DAIC/CIN2", metricName="dde", level=4 : give us all dde metrics for level4 AC/DAIC/CIN2 sub-organizations
     */
    public KeyMetricDetail[] getMetricForOrgUnit(String orgName, String metricName, int level) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getMetricForOrgUnit (" + orgName + ", " + metricName + ", " + level + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] met = { mf.getMetric(metricName, slidingWindowSize) };
            List<KeyMetricDetail> result = new ArrayList<KeyMetricDetail>();
            try {
                List<String> ali[] = metrics.getOrganizationTitles(orgName, level, met);
                for (int i = 0; i < ali[0].size(); i++) {
                    KeyMetricDetail md = metrics.getOrganizationDetailledMetric(met[0], ali[0].get(i).toString(), level);
                    md.setName(ali[0].get(i).toString());
                    md.setLong_name(ali[1].get(i).toString());
                    result.add(md);
                }
                if (ali[0].size() == 0 && level > 1) {
                    KeyMetricDetail md = metrics.getOrganizationDetailledMetric(met[0], orgName, level - 1);
                    if (md == null || md.getN() == 0) return null;
                    md.setName(orgName);
                    md.setLong_name(orgName);
                    result.add(md);
                }
            } catch (IndexOutOfBoundsException e) {
                KeyMetricDetail md = metrics.getOrganizationDetailledMetric(met[0], orgName, level - 1);
                if (md == null || md.getN() == 0) return null;
                md.setName(orgName);
                md.setLong_name(orgName);
                result.add(md);
            }
            KeyMetricDetail[] mds = new KeyMetricDetail[result.size()];
            mds = (KeyMetricDetail[]) result.toArray(mds);
            return mds;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * Get a metric detail for childs of a geography at a given level.
     * example : geoName="", metricName="dde", level=1 : give us all dde metrics for level1 geography (for all continents)
     * example : geoName="EUROPE", metricName="dde", level=2 : give us all dde metrics for level2 geography (all european countries)
     * example : geoName="FR", metricName="dde", level=3 : give us all dde metrics for level3 geography (all french units)
     */
    public KeyMetricDetail[] getMetricForGeoUnit(String geoName, String metricName, int level) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getMetricForGeoUnit (" + geoName + ", " + metricName + ", " + level + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] met = { mf.getMetric(metricName, slidingWindowSize) };
            List<KeyMetricDetail> result = new ArrayList<KeyMetricDetail>();
            try {
                List<String> ali[] = metrics.getGeographicTitles(geoName, level, met);
                for (int i = 0; i < ali[0].size(); i++) {
                    KeyMetricDetail md = metrics.getGeographyDetailledMetric(met[0], ali[0].get(i).toString(), level);
                    md.setName(ali[0].get(i).toString());
                    md.setLong_name(ali[1].get(i).toString());
                    result.add(md);
                }
                if (ali[0].size() == 0 && level > 1) {
                    KeyMetricDetail md = metrics.getGeographyDetailledMetric(met[0], geoName, level - 1);
                    if (md == null || md.getN() == 0) return null;
                    md.setName(geoName);
                    md.setLong_name(geoName);
                    result.add(md);
                }
            } catch (IndexOutOfBoundsException e) {
                KeyMetricDetail md = metrics.getGeographyDetailledMetric(met[0], geoName, level - 1);
                if (md == null || md.getN() == 0) return null;
                md.setName(geoName);
                md.setLong_name(geoName);
                result.add(md);
            }
            KeyMetricDetail[] mds = new KeyMetricDetail[result.size()];
            mds = (KeyMetricDetail[]) result.toArray(mds);
            return mds;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * Get a metric detail on an geographic name of a given level.
     * level : between 1 and 3
     */
    public KeyMetricDetail[] getGeoMetricDetail(String geoName, int level) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getGeoMetricDetail (" + geoName + ", " + level + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] mets = { mf.getMetric("dds", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dpc", slidingWindowSize) };
            List<KeyMetricDetail> al = new ArrayList<KeyMetricDetail>();
            KeyMetricDetail md = null;
            if (level < 1 || level > 3) return null;
            for (int i = 0; i < mets.length; i++) {
                md = metrics.getGeographyDetailledMetric(mets[i], geoName, level);
                al.add(md);
            }
            KeyMetricDetail[] mds = new KeyMetricDetail[al.size()];
            mds = (KeyMetricDetail[]) al.toArray(mds);
            return mds;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * Get a metric detail on an organization name of a given level.
     * level : between 1 and 4
     */
    public KeyMetricDetail[] getOrgMetricDetail(String orgName, int level) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getOrgMetricDetail (" + orgName + ", " + level + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] mets = { mf.getMetric("dds", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dpc", slidingWindowSize) };
            List<KeyMetricDetail> al = new ArrayList<KeyMetricDetail>();
            KeyMetricDetail md = null;
            if (level < 1 || level > 4) return null;
            for (int i = 0; i < mets.length; i++) {
                md = metrics.getOrganizationDetailledMetric(mets[i], orgName, level);
                al.add(md);
            }
            KeyMetricDetail[] mds = new KeyMetricDetail[al.size()];
            mds = (KeyMetricDetail[]) al.toArray(mds);
            return mds;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * Prepare a time series based on a given metric over a given period.
     * timeScale : 1month, 3months, 1year, 2years, 5years
     * metric_name : for asl, ddc, ...
     */
    public TimeSeries getMetricEvolution(String metric_name, String timeScale) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getMetricEvolution (" + metric_name + ", " + timeScale + ")");
            checkUserCanAccess(db);
            return getMetricEvolutionInternal(db, metric_name, timeScale);
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    private TimeSeries getMetricEvolutionInternal(Db db, String metric_name, String timeScale) throws Exception, RaciException {
        try {
            db.enter();
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            TimeSeries r = metrics.getMetricEvolution(mf.getMetric(metric_name, slidingWindowSize), timeScale);
            r.setXAxisTitle("t");
            r.setYAxisTitle("Jours");
            r.setTitle(metric_name);
            return r;
        } finally {
            db.exit();
        }
    }

    /**
     * Prepare a time series based on a given metric over a given period for a geographic unit.
     * timeScale : 1month, 3months, 1year, 2years, 5years
     * metric_name : for asl, ddc, ...
     */
    public TimeSeries getMetricEvolutionGeography(String metric_name, String timeScale, String geography) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getMetricEvolutionGeography (" + metric_name + ", " + timeScale + ", " + geography + ")");
            checkUserCanAccess(db);
            if (geography == null || geography.length() == 0) return getMetricEvolutionInternal(db, metric_name, timeScale);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            TimeSeries r = metrics.getMetricEvolutionGeography(mf.getMetric(metric_name, slidingWindowSize), timeScale, geography);
            r.setXAxisTitle("t");
            r.setYAxisTitle("Jours");
            r.setTitle(metric_name);
            return r;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * Prepare a time series based on a given metric over a given period for a organization unit.
     * timeScale : 1month, 3months, 1year, 2years, 5years
     * metric_name : for asl, ddc, ...
     */
    public TimeSeries getMetricEvolutionOrganization(String metric_name, String timeScale, String organization) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getMetricEvolutionOrganization (" + metric_name + ", " + timeScale + ", " + organization + ")");
            checkUserCanAccess(db);
            if (organization == null || organization.length() == 0) return getMetricEvolutionInternal(db, metric_name, timeScale);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            TimeSeries r = metrics.getMetricEvolutionOrganization(mf.getMetric(metric_name, slidingWindowSize), timeScale, organization);
            r.setXAxisTitle("t");
            r.setYAxisTitle("Jours");
            r.setTitle(metric_name);
            return r;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get organizationnal datas ready for print in jsp
     * examples calls : getOrgSnailForPrint("", 1) : get all data for level 1 (all orgs),
     * getOrgSnailForPrint("", 2) : get all data for level 2 (all org level2),
     * getOrgSnailForPrint("AC", 2) : get all data for level 2 for AC org,
     * getOrgSnailForPrint("AC/DCFC", 3) : get all data for level 3 for AC/DCFC,
     * getOrgSnailForPrint("AC/DCFC/SCCP", 4) : get all data for level 4 for AC/DCFC/SCCP,
     */
    public SnailDataJsp getOrgSnailForPrint(String orgName, int level, int scale) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getOrgSnailForPrint (" + orgName + ", " + level + ", " + scale + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = scale;
            Metric[] mets = { mf.getMetric("dpc", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("dds", slidingWindowSize) };
            SnailDataJsp sd = new SnailDataJsp();
            sd.setName(orgName);
            if (level < 1 || level > 4) return null;
            sd.setTitles(new ArrayList<String>());
            List<String> ali[] = metrics.getOrganizationTitles(orgName, level, mets);
            sd.setTitles(ali[0]);
            List<String> metric_names = new ArrayList<String>();
            metric_names.add("dpc");
            metric_names.add("ddc");
            metric_names.add("dde");
            metric_names.add("dds");
            List<Double> tgt_levels = new ArrayList<Double>();
            for (int i = 0; i < mets.length; i++) tgt_levels.add(new Double(getTargetLevel(mets[i].getMetricShortName())));
            sd.setTarget_levels(tgt_levels);
            sd.setMetric_names(metric_names);
            sd.setDatas(metrics.getOrganizationSnailElements(sd.getTitles(), level, mets, true));
            return sd;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get organizationnal datas ready for display in a snail
     * examples calls : getOrgSnail("", 1) : get all data for level 1 (all orgs),
     * getOrgSnail("", 2) : get all data for level 2 (all org level2),
     * getOrgSnail("AC", 2) : get all data for level 2 for AC org,
     * getOrgSnail("AC/DCFC", 3) : get all data for level 3 for AC/DCFC,
     * getOrgSnail("AC/DCFC/SCCP", 4) : get all data for level 4 for AC/DCFC/SCCP,
     */
    public SnailData getOrgSnail(String orgName, int level) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getOrgSnail (" + orgName + ", " + level + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            metrics.computePercents();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] mets = { mf.getMetric("dds", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dpc", slidingWindowSize) };
            SnailData sd = new SnailData();
            sd.setLevel(level);
            sd.setAskedUnit(orgName);
            if (level < 1 || level > 4) return null;
            List<String> ali[] = metrics.getOrganizationTitles(orgName, level, mets);
            List<Boolean> hasChildren = metrics.getHasChildrenForOrganization(ali[0], level, mets);
            List<List<Number>> colorNumbers = metrics.getOrganizationSnailElements(ali[0], level, mets, false);
            List<SnailPortion> datas = new ArrayList<SnailPortion>();
            for (int i = 0; i < ali[0].size(); i++) {
                SnailPortion tmp = new SnailPortion();
                tmp.setName(ali[0].get(i).toString());
                tmp.setLong_name(ali[1].get(i).toString());
                tmp.setHasChildren(hasChildren.get(i).booleanValue());
                List<Number> met = colorNumbers.get(i);
                for (int j = 0; j < mets.length; j++) {
                    if ("dds".equals(mets[j].getMetricShortName())) {
                        tmp.setDdsNumber(((Integer) met.get(j)).intValue());
                        tmp.setDdsString(metrics.getLevelForColor((Integer) met.get(j)));
                    } else if ("dde".equals(mets[j].getMetricShortName())) {
                        tmp.setDdeNumber(((Integer) met.get(j)).intValue());
                        tmp.setDdeString(metrics.getLevelForColor((Integer) met.get(j)));
                    } else if ("ddc".equals(mets[j].getMetricShortName())) {
                        tmp.setDdcNumber(((Integer) met.get(j)).intValue());
                        tmp.setDdcString(metrics.getLevelForColor((Integer) met.get(j)));
                    } else if ("dpc".equals(mets[j].getMetricShortName())) {
                        tmp.setDpcNumber(((Integer) met.get(j)).intValue());
                        tmp.setDpcString(metrics.getLevelForColor((Integer) met.get(j)));
                    }
                }
                datas.add(tmp);
            }
            sd.setDatas(datas);
            return sd;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get geographic datas ready for print in jsp
     * examples calls : getGeoSnailForPrint("", 1) : get all data for level 1 (all orgs),
     * getOrgSnailForPrint("", 2) : get all data for level 2 (all org level2),
     * getOrgSnailForPrint("AC", 2) : get all data for level 2 for AC org,
     * getOrgSnailForPrint("AC/DCFC", 3) : get all data for level 3 for AC/DCFC,
     * getOrgSnailForPrint("AC/DCFC/SCCP", 4) : get all data for level 4 for AC/DCFC/SCCP,
     */
    public SnailDataJsp getGeoSnailForPrint(String geoName, int level, int scale) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getGeoSnailForPrint (" + geoName + ", " + level + ", " + scale + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = scale;
            Metric[] mets = { mf.getMetric("dpc", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("dds", slidingWindowSize) };
            if (level < 1 || level > 4) return null;
            SnailDataJsp sd = new SnailDataJsp();
            sd.setName(metrics.lookupForFullCountryName(geoName));
            sd.setTitles(new ArrayList<String>());
            List<String> ali[] = metrics.getGeographicTitles(geoName, level, mets);
            sd.setTitles(ali[1]);
            List<String> metric_names = new ArrayList<String>();
            metric_names.add("dpc");
            metric_names.add("ddc");
            metric_names.add("dde");
            metric_names.add("dds");
            List<Double> tgt_levels = new ArrayList<Double>();
            for (int i = 0; i < mets.length; i++) tgt_levels.add(new Double(getTargetLevel(mets[i].getMetricShortName())));
            sd.setTarget_levels(tgt_levels);
            sd.setMetric_names(metric_names);
            sd.setDatas(metrics.getGeographicSnailElements(ali[0], level, mets, true));
            return sd;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get organizationnal datas ready for display in a snail
     * examples calls : getOrgSnail("", 1) : get all data for level 1 (all orgs),
     * getOrgSnail("", 2) : get all data for level 2 (all org level2),
     * getOrgSnail("AC", 2) : get all data for level 2 for AC org,
     * getOrgSnail("AC/DCFC", 3) : get all data for level 3 for AC/DCFC,
     * getOrgSnail("AC/DCFC/SCCP", 4) : get all data for level 4 for AC/DCFC/SCCP,
     */
    public SnailData getGeogSnail(String geoName, int level) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getGeogSnail (" + geoName + ", " + level + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            metrics.computePercents();
            int slidingWindowSize = getSlidingWindowSize().intValue();
            Metric[] mets = { mf.getMetric("dds", slidingWindowSize), mf.getMetric("dde", slidingWindowSize), mf.getMetric("ddc", slidingWindowSize), mf.getMetric("dpc", slidingWindowSize) };
            SnailData sd = new SnailData();
            sd.setLevel(level);
            sd.setAskedUnit(geoName);
            if (level < 1 || level > 4) return null;
            List<String> ali[] = metrics.getGeographicTitles(geoName, level, mets);
            List<Boolean> hasChildren = metrics.getHasChildrenForGeography(ali[0], level, mets);
            List<List<Number>> colorNumbers = metrics.getGeographicSnailElements(ali[0], level, mets, false);
            List<SnailPortion> datas = new ArrayList<SnailPortion>();
            for (int i = 0; i < ali[0].size(); i++) {
                SnailPortion tmp = new SnailPortion();
                tmp.setName(ali[0].get(i).toString());
                tmp.setLong_name(ali[1].get(i).toString());
                tmp.setHasChildren(hasChildren.get(i).booleanValue());
                List<Number> met = colorNumbers.get(i);
                for (int j = 0; j < mets.length; j++) {
                    if ("dds".equals(mets[j].getMetricShortName())) {
                        tmp.setDdsNumber(((Integer) met.get(j)).intValue());
                        tmp.setDdsString(metrics.getLevelForColor((Integer) met.get(j)));
                    } else if ("dde".equals(mets[j].getMetricShortName())) {
                        tmp.setDdeNumber(((Integer) met.get(j)).intValue());
                        tmp.setDdeString(metrics.getLevelForColor((Integer) met.get(j)));
                    } else if ("ddc".equals(mets[j].getMetricShortName())) {
                        tmp.setDdcNumber(((Integer) met.get(j)).intValue());
                        tmp.setDdcString(metrics.getLevelForColor((Integer) met.get(j)));
                    } else if ("dpc".equals(mets[j].getMetricShortName())) {
                        tmp.setDpcNumber(((Integer) met.get(j)).intValue());
                        tmp.setDpcString(metrics.getLevelForColor((Integer) met.get(j)));
                    }
                }
                datas.add(tmp);
            }
            sd.setDatas(datas);
            return sd;
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * Query the target level for a particular metric.
     * slidingWindowSize is ignored for this call.
     */
    public double getTargetLevel(String metric_nameIn) throws Exception, RaciException {
        Db db = dbRO();
        try {
            String metric_name = metric_nameIn;
            _logger.info("IM : getTargetLevel (" + metric_name + ")");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = 1;
            return metrics.getTargetLevel(mf.getMetric(metric_name, slidingWindowSize));
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * Set the target level for a particular metric.
     * slidingWindowSize is ignored for this call.
     */
    public Boolean setTargetLevel(String metric_nameIn, double target) throws Exception, RaciException {
        Db db = dbRW();
        try {
            String metric_name = metric_nameIn;
            _logger.info("IM : setTargetLevel (" + metric_name + ", " + target + ")");
            db.begin();
            checkUserCanAdmin(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            int slidingWindowSize = 1;
            int ret = metrics.setTargetLevel(mf.getMetric(metric_name, slidingWindowSize), target);
            if (ret == 0) {
                db.rollback();
                return Boolean.FALSE;
            } else {
                db.commit();
                return Boolean.TRUE;
            }
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    public FileImportHistory getImportInfosForAnomalies() throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getImportInfoForAnomalies");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            return metrics.getImportInfosForAnomalies();
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get the last import date for a user file
     * we can filter with the arg ('true' -> last good import, 'false' -> last bad import)
     */
    public java.util.Date getLastImportDate(String filter) throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getLastImportDate");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            return metrics.getLastImportDate(filter);
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    /**
     * get all file import history
     */
    public FileImportHistory[] getFileImportStatus() throws Exception, RaciException {
        Db db = dbRO();
        try {
            _logger.info("IM : getFileImportStatus");
            checkUserCanAccess(db);
            MetricFactory mf = newMetricFactory(db);
            MetricReports metrics = mf.getMetricReports();
            return metrics.getFileImportStatus();
        } catch (Exception e) {
            store(e);
            throw e;
        } finally {
            db.safeClose();
        }
    }

    private void checkUserCanAccess(Db db) throws Exception {
        RaciIM rim = new RaciIM(db);
        if (!rim.canAccessRO(db, getCurrentUser())) {
            throw rim.getRaciException(db, "You cannot access to this module");
        }
    }

    private void checkUserCanAdmin(Db db) throws Exception {
        RaciIM rim = new RaciIM(db);
        if (!rim.canManageTargets(db, getCurrentUser())) {
            throw rim.getRaciException(db, "You cannot change targets on this module");
        }
    }
}
