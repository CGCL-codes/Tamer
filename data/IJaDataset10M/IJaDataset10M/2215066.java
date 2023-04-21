package com.entelience.raci.risk;

import com.entelience.objects.raci.RACI;
import com.entelience.objects.raci.RaciInfoLine;
import com.entelience.objects.raci.RaciableBean;
import com.entelience.objects.raci.RaciObjectType;
import com.entelience.objects.risk.RiskReviewId;
import com.entelience.provider.risk.DbRiskReview;
import com.entelience.raci.BasicPropagationRaciMatrix;
import com.entelience.raci.RaciMatrix;
import com.entelience.raci.RaciHelper;
import com.entelience.raci.RaciDb;
import com.entelience.raci.module.RaciRiskAssessment;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;
import com.entelience.sql.IdHelper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class RaciRiskReview extends RaciModule {

    protected static final RaciObjectType raciObjectType = RaciObjectType.RISK_RISKREVIEW;

    private RiskReviewId rid = null;

    public RaciRiskReview(Db db, RiskReviewId id) throws Exception {
        this.rid = id;
        computeRaciObjectId(db);
    }

    public RaciRiskReview(RaciRiskAssessment parent) {
        super(parent);
    }

    public RaciRiskReview() {
        super();
    }

    @Override
    protected void setId(Db db, int id) throws Exception {
        this.rid = new RiskReviewId(id, 0);
        computeRaciObjectId(db);
    }

    public RaciObjectType getRaciObjectType() {
        return raciObjectType;
    }

    @Override
    protected final void computeRaciObjectId(Db db) throws Exception {
        computeRaciObjectId(db, rid.getId());
    }

    @Override
    public RaciableBean getRaciBean(Db db) throws Exception {
        Integer id = getRaciBeanHelper(db);
        return DbRiskReview.getRiskReview(db, new RiskReviewId(id.intValue(), 0));
    }

    @Override
    protected RaciMatrix getParent(Db db) throws Exception {
        return getParent();
    }

    /**
     * to be called when a risk is created
     *
     */
    public void riskReviewCreated(Db db, int riskReviewId, int modifier) throws Exception {
        _logger.info("RaciRiskReview - riskReviewCreated");
        setId(db, riskReviewId);
        RaciMatrix p = getParent(db);
        List<RaciInfoLine> moduleMasters = p.getRs(db);
        List<RACI> newList = new ArrayList<RACI>();
        Iterator<RaciInfoLine> it = moduleMasters.iterator();
        rdb = new RaciDb();
        rdb.setDb(db);
        while (it.hasNext()) {
            RACI raci = (RACI) it.next();
            raci.reset();
            raci.setA(true);
            raci.setRaciObjectId(getRaciObjectId());
            newList.add(raci);
        }
        if (newList.size() == 0 && modifier > 0) {
            RACI raci = new RACI();
            raci.setA(true);
            raci.setRaciObjectId(getRaciObjectId());
            raci.setUserId(modifier);
            newList.add(raci);
        }
        List<RACI> newList2 = new ArrayList<RACI>();
        List<RaciInfoLine> moduleAs = p.getAs(db);
        it = moduleAs.iterator();
        while (it.hasNext()) {
            RACI raci = (RACI) it.next();
            raci.reset();
            raci.setI(true);
            raci.setRaciObjectId(getRaciObjectId());
            newList2.add(raci);
        }
        rdb.setRacis(RaciHelper.mergeRacis(newList, newList2), modifier, false);
    }

    @Override
    protected boolean canAugment(RACI raci) throws Exception {
        return raci.isRA();
    }

    @Override
    protected boolean canCreate(boolean parentCanAugment, RACI raci) throws Exception {
        return (parentCanAugment);
    }

    @Override
    protected void updateHistory(Db db, int raciObj, int modifier) throws Exception {
        try {
            db.enter();
            _logger.debug("Updating risk history for possible new owner");
            PreparedStatement pst = db.prepareStatement("SELECT e_risk_review_id, obj_ser from risk.e_risk_review WHERE e_raci_obj = ?");
            pst.setInt(1, raciObj);
            RiskReviewId id = IdHelper.getRiskReviewId(pst);
            DbRiskReview.addRiskReviewHistory(db, id, modifier);
        } finally {
            db.exit();
        }
    }

    /**
     * direct dependencies for a risk control are controls
     */
    @Override
    protected List<Integer> getDirectDependencies(Db db, int objId) throws SQLException {
        try {
            db.enter();
            PreparedStatement pstSelect = db.prepareStatement("SELECT rc.e_raci_obj FROM risk.e_risk_control rc INNER JOIN risk.e_risk_review rr ON rr.e_risk_review_id = rc.e_risk_review_id WHERE rr.e_raci_obj = ? AND NOT rc.deleted");
            pstSelect.setInt(1, objId);
            ResultSet rs = db.executeQuery(pstSelect);
            List<Integer> racis = new ArrayList<Integer>();
            if (rs.next()) {
                do {
                    racis.add(Integer.valueOf(rs.getInt(1)));
                } while (rs.next());
            } else {
                _logger.debug("RaciRiskReview - getDirectDependencies - no row found");
            }
            return racis;
        } finally {
            db.exit();
        }
    }

    /**
     * indirect dependencies for a risk review are actions
     */
    @Override
    protected List<Integer> getIndirectDependencies(Db db, int objId) throws SQLException {
        try {
            db.enter();
            PreparedStatement pstSelect = db.prepareStatement("SELECT a.e_raci_obj FROM risk.e_action a INNER JOIN risk.e_risk_control rc ON rc.e_risk_control_id = a.e_risk_control_id INNER JOIN risk.e_risk_review rr ON rr.e_risk_review_id = rc.e_risk_review_id WHERE rr.e_raci_obj = ? AND a.e_status_id NOT IN (3, 4)");
            pstSelect.setInt(1, objId);
            ResultSet rs = db.executeQuery(pstSelect);
            List<Integer> racis = new ArrayList<Integer>();
            if (rs.next()) {
                do {
                    racis.add(Integer.valueOf(rs.getInt(1)));
                } while (rs.next());
            } else {
                _logger.debug("RaciRiskReview - getIndirectDependencies - no row found");
            }
            return racis;
        } finally {
            db.exit();
        }
    }
}
