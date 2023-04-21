package com.ixora.rms.client.model;

import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import com.ixora.rms.ResourceId;
import com.ixora.common.logging.AppLogger;
import com.ixora.common.logging.AppLoggerFactory;
import com.ixora.common.utils.Utils;
import com.ixora.rms.repository.Dashboard;
import com.ixora.rms.repository.DashboardId;
import com.ixora.rms.repository.DataViewId;

/**
 * @author Daniel Moraru
 */
final class DashboardModelHelperImpl implements DashboardModelHelper {

    /** Logger */
    private static final AppLogger logger = AppLoggerFactory.getLogger(DashboardModelHelperImpl.class);

    /** Session model */
    private SessionModel model;

    /**
     * Tuple class.
     */
    private static final class DataViewInfoData {

        private ResourceId context;

        private DataViewInfo info;

        /**
         * Constructor.
         * @param context
         * @param dvinfo
         */
        public DataViewInfoData(ResourceId context, DataViewInfo dvinfo) {
            this.context = context;
            this.info = dvinfo;
        }

        public ResourceId getContext() {
            return context;
        }

        public DataViewInfo getInfo() {
            return info;
        }
    }

    /**
     * Tuple class.
     */
    private static final class CounterInfoData {

        private ResourceId counterId;

        private CounterInfo info;

        /**
         * Constructor.
         * @param counterId
         * @param cinfo
         */
        public CounterInfoData(ResourceId counterId, CounterInfo cinfo) {
            this.counterId = counterId;
            this.info = cinfo;
        }

        public ResourceId getContext() {
            return counterId;
        }

        public CounterInfo getInfo() {
            return info;
        }
    }

    /**
     * Constructor.
     * @param model
     */
    DashboardModelHelperImpl(SessionModel model) {
        this.model = model;
    }

    /**
     * @param context
     */
    public void rollbackDashboards(ResourceId context) {
        DataViewInfoData[] data = getDataViewInfoData(context);
        if (data != null) {
            ResourceId rid;
            DataViewInfoData d;
            DataViewInfo dvinfo;
            for (int i = 0; i < data.length; i++) {
                d = data[i];
                dvinfo = d.getInfo();
                rid = d.getContext();
                if (!dvinfo.isCommitted()) {
                    model.getDataViewHelper().rollbackDataView(rid, dvinfo.getDataView().getName());
                }
            }
        }
        CounterInfoData[] counters = getCounterInfoData(context);
        if (!Utils.isEmptyArray(counters)) {
            for (CounterInfoData cid : counters) {
                model.getCounterHelper().rollbackCounter(cid.counterId, true);
            }
        }
        ArtefactInfoContainerImpl qc = model.getArtefactContainerImplForResource(context, true);
        if (qc == null) {
            if (logger.isTraceEnabled()) {
                logger.error("Couldn't find container for: " + context);
            }
            return;
        }
        Collection<DashboardInfoImpl> dis = qc.getDashboardInfoImpl();
        if (!Utils.isEmptyCollection(dis)) {
            for (DashboardInfoImpl di : dis) {
                di.rollback();
            }
        }
    }

    /**
	 * Sets the dashboards associated with the given resource.
	 * @param id a valid, non regex resource id
	 * @param q
	 */
    public void setDashboards(ResourceId id, Dashboard[] groups) {
        ArtefactInfoContainerImpl qc = model.getArtefactContainerImplForResource(id, true);
        if (qc == null) {
            if (logger.isTraceEnabled()) {
                logger.error("Couldn't find query container for: " + id.toString());
            }
            return;
        }
        qc.setDashboards(groups);
        recalculateDashboardsStatus(id);
        model.refreshNode(id);
    }

    public void recalculateDashboardsStatus(ResourceId context) {
        ArtefactInfoContainerImpl acimpl = model.getArtefactContainerImplForResource(context, true);
        if (acimpl == null) {
            if (logger.isTraceEnabled()) {
                logger.error("Couldn't find container for: " + context);
            }
            return;
        }
        Collection<DashboardInfoImpl> cs = acimpl.getDashboardInfoImpl();
        if (cs == null) {
            return;
        }
        for (DashboardInfoImpl dinfo : cs) {
            if (dinfo.isCommitted()) {
                Dashboard db = dinfo.getDashboard();
                boolean enabled = dinfo.getFlag(DashboardInfo.ENABLED);
                boolean plotted = dinfo.getFlag(DashboardInfo.ACTIVATED);
                boolean committed = dinfo.isCommitted();
                ResourceId[] counters = db.getCounters();
                if (!Utils.isEmptyArray(counters)) {
                    for (ResourceId c : counters) {
                        if (context != null) {
                            c = c.complete(context);
                        }
                        CounterInfo cinfo = model.getCounterInfo(c, false);
                        if (cinfo == null) {
                            if (logger.isTraceEnabled()) {
                                logger.error("Couldn't find counter: " + c);
                            }
                            break;
                        }
                        if (!cinfo.getFlag(CounterInfo.ENABLED)) {
                            enabled = false;
                        }
                        if (!cinfo.getFlag(CounterInfo.ACTIVATED)) {
                            plotted = false;
                        }
                        if (!cinfo.isCommitted()) {
                            committed = false;
                        }
                    }
                }
                DataViewId[] views = db.getViews();
                if (!Utils.isEmptyArray(views)) {
                    DataViewId m;
                    DataViewInfo dvinfo;
                    for (int i = 0; i < views.length; i++) {
                        m = views[i];
                        if (context != null) {
                            m = m.complete(context);
                        }
                        model.getDataViewHelper().recalculateDataViewsStatus(m.getContext());
                        dvinfo = model.getDataViewInfo(m, false);
                        if (dvinfo == null) {
                            if (logger.isInfoEnabled()) {
                                logger.error("Couldn't find view info for: " + m + ". Dashboard " + dinfo.getTranslatedName() + " will be incomplete.");
                            }
                            break;
                        }
                        if (!dvinfo.getFlag(QueryInfo.ENABLED)) {
                            enabled = false;
                        }
                        if (!dvinfo.getFlag(QueryInfo.ACTIVATED)) {
                            plotted = false;
                        }
                        if (!dvinfo.isCommitted()) {
                            committed = false;
                        }
                    }
                }
                dinfo.setFlag(DashboardInfo.ENABLED, enabled);
                dinfo.setFlag(DashboardInfo.ACTIVATED, plotted);
                if (committed) {
                    dinfo.commit();
                }
            } else {
                dinfo.commit();
            }
        }
    }

    /**
	 * Returns the view info data for all views in all the dashboards
	 * in the given context.
	 * @param context
	 * @return
	 */
    private DataViewInfoData[] getDataViewInfoData(ResourceId context) {
        ArtefactInfoContainerImpl ac = model.getArtefactContainerImplForResource(context, true);
        if (ac == null) {
            if (logger.isTraceEnabled()) {
                logger.error("Couldn't find artefact container for: " + context.toString());
            }
            return null;
        }
        Collection<DashboardInfo> cs = ac.getDashboardInfo();
        if (cs == null) {
            return null;
        }
        List<DataViewInfoData> ret = new LinkedList<DataViewInfoData>();
        for (DashboardInfo dinfo : cs) {
            DataViewInfoData[] views = getDataViewInfoData(context, dinfo.getDashboard().getName());
            if (views != null) {
                ret.addAll(Arrays.asList(views));
            }
        }
        return ret.toArray(new DataViewInfoData[ret.size()]);
    }

    /**
	 * Returns the view info for all views in the given group
	 * in the given context.
	 * @param context
	 * @param dbName
	 * @return
	 */
    private DataViewInfoData[] getDataViewInfoData(ResourceId context, String dbName) {
        DashboardInfo dinfo = model.getDashboardInfo(new DashboardId(context, dbName), true);
        if (dinfo == null) {
            return null;
        }
        List<DataViewInfoData> ret = new LinkedList<DataViewInfoData>();
        Dashboard db = dinfo.getDashboard();
        DataViewId[] views = db.getViews();
        if (views == null) {
            return null;
        }
        DataViewId m;
        DataViewInfo dvinfo;
        for (int i = 0; i < views.length; i++) {
            m = views[i];
            if (context != null) {
                m = m.complete(context);
            }
            dvinfo = model.getDataViewInfo(m, true);
            if (dvinfo == null) {
                if (logger.isTraceEnabled()) {
                    logger.error("Dataview " + m + " not found for dashboard " + db.getName());
                }
                continue;
            }
            ret.add(new DataViewInfoData(m.getContext(), dvinfo));
        }
        return (DataViewInfoData[]) ret.toArray(new DataViewInfoData[ret.size()]);
    }

    /**
     * Returns the view info for all counters that are part of the given dashboard
     * @param context
     * @param dbName
     * @return
     */
    private CounterInfoData[] getCounterInfoData(ResourceId context, String dbName) {
        DashboardInfo dinfo = model.getDashboardInfo(new DashboardId(context, dbName), true);
        if (dinfo == null) {
            return null;
        }
        List<CounterInfoData> ret = new LinkedList<CounterInfoData>();
        Dashboard db = dinfo.getDashboard();
        ResourceId[] counters = db.getCounters();
        if (counters == null) {
            return null;
        }
        ResourceId m;
        CounterInfo cinfo;
        for (int i = 0; i < counters.length; i++) {
            m = counters[i];
            if (context != null) {
                m = m.complete(context);
            }
            cinfo = model.getCounterInfo(m, true);
            if (cinfo == null) {
                if (logger.isTraceEnabled()) {
                    logger.error("Counter " + m + " not found for dashboard " + db.getName());
                }
                continue;
            }
            ret.add(new CounterInfoData(m, cinfo));
        }
        return ret.toArray(new CounterInfoData[ret.size()]);
    }

    /**
     * Returns the counter info for all counters for all dashboards in the given context
     * @param context
     * @param dbName
     * @return
     */
    private CounterInfoData[] getCounterInfoData(ResourceId context) {
        ArtefactInfoContainerImpl ac = model.getArtefactContainerImplForResource(context, true);
        if (ac == null) {
            if (logger.isTraceEnabled()) {
                logger.error("Couldn't find query group container for: " + context.toString());
            }
            return null;
        }
        Collection<DashboardInfo> cs = ac.getDashboardInfo();
        if (cs == null) {
            return null;
        }
        List<CounterInfoData> ret = new LinkedList<CounterInfoData>();
        for (DashboardInfo dinfo : cs) {
            CounterInfoData[] counters = getCounterInfoData(context, dinfo.getDashboard().getName());
            if (counters != null) {
                ret.addAll(Arrays.asList(counters));
            }
        }
        return ret.toArray(new CounterInfoData[ret.size()]);
    }

    /**
	 * Sets the flag for the given dashboard.
	 * @param flag
	 * @param context
	 * @param dbName
	 * @param value
	 * @param commit
	 */
    public void setDashboardFlag(int flag, ResourceId context, String dbName, boolean value, boolean commit) {
        DataViewInfoData[] data = getDataViewInfoData(context, dbName);
        if (data != null) {
            ResourceId rid;
            DataViewInfoData d;
            DataViewInfo dvinfo;
            for (int i = 0; i < data.length; i++) {
                d = data[i];
                dvinfo = d.getInfo();
                rid = d.getContext();
                model.getDataViewHelper().setDataViewFlag(flag, rid, dvinfo.getDataView().getName(), value, commit);
            }
        }
        CounterInfoData[] counters = getCounterInfoData(context, dbName);
        if (!Utils.isEmptyArray(counters)) {
            for (CounterInfoData cid : counters) {
                ResourceInfo cInfo = model.getCounterHelper().getCounterInfo(cid.counterId);
                if (cInfo != null) {
                    switch(flag) {
                        case DashboardInfo.ENABLED:
                            if (value || !cInfo.getCounterInfo().isCommitted()) {
                                model.getCounterHelper().setCounterFlag(cid.counterId, CounterInfo.ENABLED, value, true);
                            }
                            break;
                        case DashboardInfo.ACTIVATED:
                            model.getCounterHelper().setCounterFlag(cid.counterId, CounterInfo.ACTIVATED, value, true);
                    }
                }
            }
        }
        ArtefactInfoContainerImpl ac = model.getArtefactContainerImplForResource(context, true);
        if (ac == null) {
            if (logger.isTraceEnabled()) {
                logger.error("Couldn't find container for dashboard " + context);
            }
            return;
        }
        ac.setDashboardFlag(flag, dbName, value, commit);
        model.refreshNode(context);
    }

    /**
     * @see com.ixora.rms.client.model.DashboardModelHelper#addDashboard(com.ixora.rms.ResourceId, com.ixora.rms.repository.QueryGroup)
     */
    public void addDashboard(ResourceId id, Dashboard group) {
        ArtefactInfoContainerImpl ac = model.getArtefactContainerImplForResource(id, false);
        if (ac == null) {
            if (logger.isTraceEnabled()) {
                logger.error("Couldn't find container for: " + id);
            }
            return;
        }
        ac.addDashboard(group);
        recalculateDashboardsStatus(id);
        model.refreshNode(id);
    }

    /**
     * @see com.ixora.rms.client.model.DashboardModelHelper#removeDashboard(com.ixora.rms.ResourceId, java.lang.String)
     */
    public void removeDashboard(ResourceId id, String dbName) {
        ArtefactInfoContainerImpl ac = model.getArtefactContainerImplForResource(id, false);
        if (ac == null) {
            if (logger.isTraceEnabled()) {
                logger.error("Couldn't find container for: " + id);
            }
            return;
        }
        ac.removeDashboard(dbName);
        model.refreshNode(id);
    }

    /**
     * @see com.ixora.rms.client.model.DashboardModelHelper#getAllDashboardsToRealize()
     */
    @SuppressWarnings("unchecked")
    public Map<ResourceId, Collection<DashboardInfo>> getAllDashboardsToRealize() {
        Map<ResourceId, Collection<DashboardInfo>> ret = new HashMap<ResourceId, Collection<DashboardInfo>>();
        SessionNode sn = model.getSessionNode();
        Enumeration<ResourceNode> e = sn.breadthFirstEnumeration();
        ResourceNode rn;
        while (e.hasMoreElements()) {
            rn = e.nextElement();
            Collection<DashboardInfo> col = sn.getArtefactInfoContainer().getDashboardsToRealize();
            if (col != null) {
                ResourceId rid = rn.getResourceId();
                ret.put(rid, col);
            }
        }
        return ret;
    }

    /**
     * @see com.ixora.rms.client.model.DashboardModelHelper#getAllDashboardsToUnRealize()
     */
    @SuppressWarnings("unchecked")
    public Map<ResourceId, Collection<DashboardInfo>> getAllDashboardsToUnRealize() {
        Map<ResourceId, Collection<DashboardInfo>> ret = new HashMap<ResourceId, Collection<DashboardInfo>>();
        SessionNode sn = model.getSessionNode();
        Enumeration<ResourceNode> e = sn.breadthFirstEnumeration();
        ResourceNode rn;
        while (e.hasMoreElements()) {
            rn = e.nextElement();
            Collection<DashboardInfo> col = sn.getArtefactInfoContainer().getDashboardsToUnRealize();
            if (col != null) {
                ResourceId rid = rn.getResourceId();
                ret.put(rid, col);
            }
        }
        return ret;
    }

    /**
     * @see com.ixora.rms.client.model.DashboardModelHelper#isDashboardReady(com.ixora.rms.internal.ResourceId, com.ixora.rms.repository.QueryGroup)
     */
    public boolean isDashboardReady(ResourceId context, Dashboard dashboard) {
        boolean ready = true;
        ResourceId[] counters = dashboard.getCounters();
        if (!Utils.isEmptyArray(counters)) {
            for (ResourceId c : counters) {
                if (context != null) {
                    c = c.complete(context);
                }
                CounterInfo cinfo = model.getCounterInfo(c, true);
                if (cinfo == null) {
                    return false;
                }
            }
        }
        DataViewId[] views = dashboard.getViews();
        if (ready && !Utils.isEmptyArray(views)) {
            for (DataViewId m : views) {
                if (context != null) {
                    m = m.complete(context);
                }
                DataViewInfo dvinfo = model.getDataViewInfo(m, true);
                if (dvinfo == null) {
                    return false;
                }
                if (!model.getQueryHelper().isQueryReady(m.getContext(), dvinfo.getDataView().getQueryDef())) {
                    return false;
                }
            }
        }
        return ready;
    }

    /**
	 * @see com.ixora.rms.client.model.DashboardModelHelper#commitDashboard(com.ixora.rms.internal.ResourceId, java.lang.String)
	 */
    public void commitDashboard(ResourceId context, String dashboard) {
        ArtefactInfoContainerImpl qc = model.getArtefactContainerImplForResource(context, true);
        if (qc == null) {
            return;
        }
        DashboardInfoImpl di = qc.getDashboardInfoImpl(dashboard);
        if (di == null) {
            if (logger.isTraceEnabled()) {
                logger.error("Couldn't find dashboard info for: " + di);
            }
            return;
        }
        di.commit();
    }

    /**
	 * @see com.ixora.rms.client.model.DashboardModelHelper#getAllCommittedDashboards(int)
	 */
    @SuppressWarnings("unchecked")
    public Collection<DashboardId> getAllCommittedDashboards(int flag) {
        Collection<DashboardId> ret = new LinkedList<DashboardId>();
        Enumeration<SessionModelTreeNode> e = model.getSessionNode().breadthFirstEnumeration();
        while (e.hasMoreElements()) {
            SessionModelTreeNode sn = (SessionModelTreeNode) e.nextElement();
            Collection<DashboardInfo> dashboards = sn.getArtefactInfoContainer().getDashboardInfo();
            if (!Utils.isEmptyCollection(dashboards)) {
                for (DashboardInfo di : dashboards) {
                    if (di.isCommitted() && di.getFlag(flag)) {
                        ret.add(new DashboardId(sn.getResourceId(), di.getDashboard().getName()));
                    }
                }
            }
        }
        return ret;
    }
}
