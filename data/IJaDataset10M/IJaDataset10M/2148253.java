package com.fh.auge.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;
import com.fh.auge.investment.Investment;
import com.fh.auge.investment.InvestmentDao;
import com.fh.auge.snapshot.DetailSnapshot;
import com.fh.auge.snapshot.IntervalPerformanceSnapshot;
import com.fh.auge.snapshot.PerformanceSnapshot;
import com.fh.auge.snapshot.SnapshotFactory;
import com.fh.auge.snapshot.SnapshotProperties;

public class InvestmentsAction extends MultiAction {

    private InvestmentDao investmentDao;

    private SnapshotFactory snapshotFactory;

    private SnapshotProperties properties;

    public Event loadInvestments(RequestContext context) throws Exception {
        System.err.println("InvestmentsAction.loadInvestments");
        List<Investment> investments = investmentDao.getInvestments();
        context.getFlowScope().put("groupSnapshot", snapshotFactory.getAdapter(investments, DetailSnapshot.class));
        Map<Investment, DetailSnapshot> snapshots = new HashMap<Investment, DetailSnapshot>();
        for (Investment investment : investments) {
            snapshots.put(investment, (DetailSnapshot) snapshotFactory.getAdapter(investment, DetailSnapshot.class));
        }
        context.getFlowScope().put("investments", investments);
        context.getFlowScope().put("snapshots", snapshots);
        loadRef(context, investments);
        return success();
    }

    private void loadRef(RequestContext context, Object subject) {
        context.getFlowScope().put("performance", snapshotFactory.getAdapter(subject, PerformanceSnapshot.class));
        context.getFlowScope().put("intervalPerformance", snapshotFactory.getAdapter(subject, IntervalPerformanceSnapshot.class));
        context.getFlowScope().put("properties", properties);
    }

    public Event loadInvestment(RequestContext context) throws Exception {
        String id = context.getFlowScope().getRequiredString("id");
        System.err.println("InvestmentsAction.loadInvestment " + id);
        Investment investment = investmentDao.findById(id);
        context.getFlowScope().put("investment", investment);
        context.getFlowScope().put("snapshot", snapshotFactory.getAdapter(investment, DetailSnapshot.class));
        loadRef(context, investment);
        return success();
    }

    @Required
    public void setInvestmentDao(InvestmentDao investmentDao) {
        this.investmentDao = investmentDao;
    }

    @Required
    public void setSnapshotFactory(SnapshotFactory snapshotFactory) {
        this.snapshotFactory = snapshotFactory;
    }

    @Required
    public void setProperties(SnapshotProperties properties) {
        this.properties = properties;
    }
}
