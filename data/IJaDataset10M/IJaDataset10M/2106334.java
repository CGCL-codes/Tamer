package com.entelience.report.vuln;

import com.entelience.esis.Activatable;
import com.entelience.objects.raci.RACI;
import com.entelience.report.Report;
import com.entelience.util.DateHelper;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The VulnReport class compute metrics on vulnerabilties.
 * Some metric come directly from the probes (vendors reeived on a day ...)
 * But the majority come from the vulnerability process management module use.
 * Consequently, this report must be run daily, for the events that happened the day before.
 * (use the 'yesterday' option when running the report).
 * it should be run at midnight, in a separate CRON script, 
 * because it is in majority not dependant from the probes.
 *
 * The report is accurate in the time, thaks to the historic tables, except for 1 main metric : severity
 */
public class VulnerabilitiesReport extends Report {

    public VulnerabilitiesReport(Db workDb, Db statusDb) throws Exception {
        super(workDb, statusDb);
    }

    /**
     * linked to VRT module
     *
     **/
    public List<Activatable> getActivatableElementsForReport(Db db) throws Exception {
        List<Activatable> l = new ArrayList<Activatable>();
        l.add(new com.entelience.module.Vulnerabilities());
        return l;
    }

    protected boolean runReport(Db db) throws Exception {
        try {
            db.enter();
            cleanTables(db);
            computeVulnsDaily(db);
            computeTriageDaily(db);
            computeActionsDaily(db);
            computeDecisionDaily(db);
            computeStatusDaily(db);
            computePriorityDaily(db);
            computeSeverityDaily(db);
            computeUsersDaily(db);
            computeGroupsDaily(db);
            computeVendorsDaily(db);
            computeProductsDaily(db);
            computeVersionsDaily(db);
            computeUserDetailsDaily(db);
            computeUserPriorityDaily(db);
            computeUserSeverityDaily(db);
            computeUserDecisionDaily(db);
            computeUserStatusDaily(db);
            computeGroupDetailsDaily(db);
            computeGroupMembersDaily(db);
            computeGroupPriorityDaily(db);
            computeGroupSeverityDaily(db);
            computeGroupDecisionDaily(db);
            computeGroupStatusDaily(db);
            return true;
        } finally {
            db.exit();
        }
    }

    private static class GroupKey {

        public int groupId;

        public int raciId;

        public Integer axis;

        public Date date;

        public String toString() {
            try {
                return groupId + "/" + raciId + "/" + axis + "/" + DateHelper.HTMLDateOrNull(date);
            } catch (Exception e) {
                return super.toString();
            }
        }

        public int hashCode() {
            return toString().hashCode();
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            return o.hashCode() == hashCode();
        }
    }

    private GroupKey getKey(int groupId, int raciId, Date date, Integer axis) throws Exception {
        GroupKey gk = new GroupKey();
        gk.groupId = groupId;
        gk.raciId = raciId;
        gk.axis = axis;
        gk.date = date;
        return gk;
    }

    private RACI mergeGroupRacis(RACI r1, RACI r2) throws Exception {
        RACI ret = new RACI(r1);
        ret.setR(ret.isR() || r2.isR());
        ret.setA(ret.isA() || r2.isA());
        ret.setC(ret.isC() || r2.isC());
        ret.setI(ret.isI() || r2.isI());
        return ret;
    }

    private void updateGroupMetrics(Db db, PreparedStatement pstList, PreparedStatement pstSelect, PreparedStatement pstInsert, PreparedStatement pstUpdate, boolean hasAxis) throws Exception {
        try {
            db.enter();
            PreparedStatement pstHistory = db.prepareStatement("SELECT r_change, a_change, c_change, i_change, deleted FROM e_raci_history WHERE e_people_id = ? AND e_raci_obj = ? AND date(change_date) > ? ORDER BY change_date DESC");
            ResultSet rs = db.executeQuery(pstList);
            Integer axis = null;
            Map<GroupKey, RACI> aggregate = new HashMap<GroupKey, RACI>();
            if (rs.next()) {
                do {
                    RACI raci = new RACI();
                    raci.setRaciObjectId(rs.getInt(1));
                    raci.setUserId(rs.getInt(2));
                    raci.setR(rs.getBoolean(3));
                    raci.setA(rs.getBoolean(4));
                    raci.setC(rs.getBoolean(5));
                    raci.setI(rs.getBoolean(6));
                    int groupId = rs.getInt(7);
                    if (hasAxis) axis = Integer.valueOf(rs.getInt(8));
                    pstHistory.setInt(1, raci.getUserId());
                    pstHistory.setInt(2, raci.getRaciObjectId());
                    pstHistory.setTimestamp(3, DateHelper.sql(effective_date));
                    try {
                        db.enter();
                        ResultSet rsHistory = db.executeQuery(pstHistory);
                        if (rsHistory.next()) {
                            do {
                                raci.setR(raci.isR() ^ rsHistory.getBoolean(1));
                                raci.setA(raci.isA() ^ rsHistory.getBoolean(2));
                                raci.setC(raci.isC() ^ rsHistory.getBoolean(3));
                                raci.setI(raci.isI() ^ rsHistory.getBoolean(4));
                            } while (rsHistory.next());
                        }
                        if (!raci.isR() && !raci.isA() && !raci.isC() && !raci.isI()) continue;
                        RACI existing = aggregate.get(getKey(groupId, raci.getRaciObjectId(), effective_date, axis));
                        if (existing == null) {
                            aggregate.put(getKey(groupId, raci.getRaciObjectId(), effective_date, axis), raci);
                        } else {
                            aggregate.put(getKey(groupId, raci.getRaciObjectId(), effective_date, axis), mergeGroupRacis(raci, existing));
                        }
                    } finally {
                        db.exit();
                    }
                } while (rs.next());
            } else {
                _logger.debug("No RACI found");
            }
            for (Iterator<Map.Entry<GroupKey, RACI>> it = aggregate.entrySet().iterator(); it.hasNext(); ) {
                Map.Entry<GroupKey, RACI> me = it.next();
                GroupKey gk = me.getKey();
                RACI raci = (RACI) me.getValue();
                pstSelect.setDate(1, DateHelper.sqld(gk.date));
                pstSelect.setInt(2, gk.groupId);
                pstSelect.setBoolean(3, raci.isR());
                pstSelect.setBoolean(4, raci.isA());
                pstSelect.setBoolean(5, raci.isC());
                pstSelect.setBoolean(6, raci.isI());
                if (gk.axis != null) pstSelect.setInt(7, gk.axis.intValue());
                int nbRows = DbHelper.getKey(pstSelect).intValue();
                if (nbRows == 0) {
                    pstInsert.setDate(1, DateHelper.sqld(gk.date));
                    pstInsert.setInt(2, gk.groupId);
                    pstInsert.setBoolean(3, raci.isR());
                    pstInsert.setBoolean(4, raci.isA());
                    pstInsert.setBoolean(5, raci.isC());
                    pstInsert.setBoolean(6, raci.isI());
                    if (gk.axis != null) pstInsert.setInt(7, gk.axis.intValue());
                    db.executeUpdate(pstInsert);
                }
                pstUpdate.setInt(1, 1);
                pstUpdate.setDate(2, DateHelper.sqld(gk.date));
                pstUpdate.setInt(3, gk.groupId);
                pstUpdate.setBoolean(4, raci.isR());
                pstUpdate.setBoolean(5, raci.isA());
                pstUpdate.setBoolean(6, raci.isC());
                pstUpdate.setBoolean(7, raci.isI());
                if (gk.axis != null) pstUpdate.setInt(8, gk.axis.intValue());
                db.executeUpdate(pstUpdate);
            }
        } finally {
            db.exit();
        }
    }

    protected void computeGroupStatusDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing group status Daily metrics");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_group_status_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_group_status_daily (calc_day, e_group_id, r, a, c, i, status) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pstSelect = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_group_status_daily WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND status = ?");
            String tableDef = "(SELECT e_raci_obj, tmp.e_people_id, r,a,c,i, e_group_id FROM (SELECT rac.e_raci_obj, rac.e_people_id, rac.r,rac.a,rac.c,rac.i FROM e_raci rac WHERE e_people_id <>0 UNION SELECT h.e_raci_obj, h.e_people_id, false, false, false, false FROM e_raci_history h WHERE h.current AND h.deleted AND date_trunc('day',h.change_date) <= date_trunc('day', date(?)) AND h.change_date = ( SELECT MAX (change_date) FROM e_raci_history hr WHERE hr.e_raci_obj = h.e_raci_obj AND date_trunc('day', hr.change_date) <= date_trunc('day', date(?)) ) ) AS tmp INNER JOIN (SELECT e_group_id, e_people_id from e_group_membership_history gh WHERE date_trunc('day', gh.change_date) <= date_trunc('day', date(?)) AND gh.member AND gh.change_date = ( SELECT MAX(change_date) FROM e_group_membership_history gmh WHERE gmh.e_group_id = gh.e_group_id AND gmh.e_people_id = gh.e_people_id AND date_trunc('day', gmh.change_date) <= date_trunc('day', date(?)) )) AS group_membership ON group_membership.e_people_id = tmp.e_people_id ) as rac";
            {
                _logger.debug("computeGroupStatusDaily - count_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, vh.mav_status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh ON vh.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh.change_date) <= date_trunc('day', date(?)) AND mav_status IS NOT NULL AND vh.status=1 AND vh.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_status_daily SET count_vulns = count_vulns + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND status = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeGroupStatusDaily - vulns_changed_this_day");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, vh1.mav_status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh1 ON vh1.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh1.change_date) <= date_trunc('day', date(?)) AND vh1.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh1.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) AND ( vh1.e_vulnerability_id NOT IN ( SELECT e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) OR (vh1.mav_status, vh1.e_vulnerability_id) NOT IN ( SELECT mav_status, e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) ) AND vh1.mav_status <> 0");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                pstList.setTimestamp(9, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(10, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(11, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(12, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_status_daily SET vulns_changed_this_day = vulns_changed_this_day + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND status = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeGroupStatusDaily - count_actions");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, ah.mav_status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_action_history ah ON ah.e_vulnerability_action_id = a.e_vulnerability_action_id WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND date_trunc('day', ah.change_date) <= date_trunc('day', date(?)) AND ah.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_status_daily SET count_actions = count_actions + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND status = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeGroupStatusDaily - actions_changed_this_day");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, ah1.mav_status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_action_history ah1 ON ah1.e_vulnerability_action_id = a.e_vulnerability_action_id WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND date_trunc('day', ah1.change_date) <= date_trunc('day', date(?)) AND ah1.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah1.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) AND ( ah1.e_vulnerability_action_id NOT IN ( SELECT e_vulnerability_action_id FROM vuln.e_vulnerability_action_history ah2 WHERE date_trunc('day', ah2.change_date) <= date_trunc('day', date(?)) AND ah2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah2.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) OR (ah1.mav_status, ah1.e_vulnerability_action_id) NOT IN ( SELECT mav_status, e_vulnerability_action_id FROM vuln.e_vulnerability_action_history ah2 WHERE date_trunc('day', ah2.change_date) <= date_trunc('day', date(?)) AND ah2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah2.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) ) AND ah1.mav_status <> 0");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                pstList.setTimestamp(9, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(10, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(11, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(12, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_status_daily SET actions_changed_this_day = actions_changed_this_day + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND status = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
        } finally {
            db.exit();
        }
    }

    protected void computeGroupDecisionDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing group decision Daily metrics");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_group_decision_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_group_decision_daily (calc_day, e_group_id, r, a, c, i, decision) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pstSelect = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_group_decision_daily WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND decision = ?");
            String tableDef = "(SELECT e_raci_obj, tmp.e_people_id, r,a,c,i, e_group_id FROM (SELECT rac.e_raci_obj, rac.e_people_id, rac.r,rac.a,rac.c,rac.i FROM e_raci rac WHERE e_people_id <>0 UNION SELECT h.e_raci_obj, h.e_people_id, false, false, false, false FROM e_raci_history h WHERE h.current AND h.deleted AND date_trunc('day',h.change_date) <= date_trunc('day', date(?)) AND h.change_date = ( SELECT MAX (change_date) FROM e_raci_history hr WHERE hr.e_raci_obj = h.e_raci_obj AND date_trunc('day', hr.change_date) <= date_trunc('day', date(?)) ) ) AS tmp INNER JOIN (SELECT e_group_id, e_people_id from e_group_membership_history gh WHERE date_trunc('day', gh.change_date) <= date_trunc('day', date(?)) AND gh.member AND gh.change_date = ( SELECT MAX(change_date) FROM e_group_membership_history gmh WHERE gmh.e_group_id = gh.e_group_id AND gmh.e_people_id = gh.e_people_id AND date_trunc('day', gmh.change_date) <= date_trunc('day', date(?)) )) AS group_membership ON group_membership.e_people_id = tmp.e_people_id ) as rac";
            {
                _logger.debug("computeGroupDecisionDaily - count_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, vh.status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh ON vh.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh.change_date) <= date_trunc('day', date(?)) AND vh.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_decision_daily SET count_vulns = count_vulns + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND decision = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeGroupDecisionDaily - vulns_changed_this_day");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, vh1.status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh1 ON vh1.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh1.change_date) <= date_trunc('day', date(?)) AND vh1.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh1.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) AND ( vh1.e_vulnerability_id NOT IN ( SELECT e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) OR (vh1.status, vh1.e_vulnerability_id) NOT IN ( SELECT status, e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) ) AND vh1.status <> 0");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                pstList.setTimestamp(9, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(10, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(11, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(12, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_decision_daily SET vulns_changed_this_day = vulns_changed_this_day + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND decision = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
        } finally {
            db.exit();
        }
    }

    protected void computeGroupSeverityDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing group severity Daily metrics");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_group_severity_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_group_severity_daily (calc_day, e_group_id, r, a, c, i, severity) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pstSelect = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_group_severity_daily WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND severity = ?");
            String tableDef = "(SELECT e_raci_obj, tmp.e_people_id, r,a,c,i, e_group_id FROM (SELECT rac.e_raci_obj, rac.e_people_id, rac.r,rac.a,rac.c,rac.i FROM e_raci rac WHERE e_people_id <>0 UNION SELECT h.e_raci_obj, h.e_people_id, false, false, false, false FROM e_raci_history h WHERE h.current AND h.deleted AND date_trunc('day',h.change_date) <= date_trunc('day', date(?)) AND h.change_date = ( SELECT MAX (change_date) FROM e_raci_history hr WHERE hr.e_raci_obj = h.e_raci_obj AND date_trunc('day', hr.change_date) <= date_trunc('day', date(?)) ) ) AS tmp INNER JOIN (SELECT e_group_id, e_people_id from e_group_membership_history gh WHERE date_trunc('day', gh.change_date) <= date_trunc('day', date(?)) AND gh.member AND gh.change_date = ( SELECT MAX(change_date) FROM e_group_membership_history gmh WHERE gmh.e_group_id = gh.e_group_id AND gmh.e_people_id = gh.e_people_id AND date_trunc('day', gmh.change_date) <= date_trunc('day', date(?)) )) AS group_membership ON group_membership.e_people_id = tmp.e_people_id ) as rac";
            {
                _logger.debug("computeGroupDecisionDaily - count_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, v.severity FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_severity_daily SET count_vulns = count_vulns + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND severity = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
        } finally {
            db.exit();
        }
    }

    protected void computeGroupPriorityDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing group priority Daily metrics");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_group_priority_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_group_priority_daily (calc_day, e_group_id, r, a, c, i, priority) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pstSelect = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_group_priority_daily WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND priority = ?");
            String tableDef = "(SELECT e_raci_obj, tmp.e_people_id, r,a,c,i, e_group_id FROM (SELECT rac.e_raci_obj, rac.e_people_id, rac.r,rac.a,rac.c,rac.i FROM e_raci rac WHERE e_people_id <>0 UNION SELECT h.e_raci_obj, h.e_people_id, false, false, false, false FROM e_raci_history h WHERE h.current AND h.deleted AND date_trunc('day',h.change_date) <= date_trunc('day', date(?)) AND h.change_date = ( SELECT MAX (change_date) FROM e_raci_history hr WHERE hr.e_raci_obj = h.e_raci_obj AND date_trunc('day', hr.change_date) <= date_trunc('day', date(?)) ) ) AS tmp INNER JOIN (SELECT e_group_id, e_people_id from e_group_membership_history gh WHERE date_trunc('day', gh.change_date) <= date_trunc('day', date(?)) AND gh.member AND gh.change_date = ( SELECT MAX(change_date) FROM e_group_membership_history gmh WHERE gmh.e_group_id = gh.e_group_id AND gmh.e_people_id = gh.e_people_id AND date_trunc('day', gmh.change_date) <= date_trunc('day', date(?)) )) AS group_membership ON group_membership.e_people_id = tmp.e_people_id ) as rac";
            {
                _logger.debug("computeGroupPriorityDaily - count_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, vh.priority FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh ON vh.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh.change_date) <= date_trunc('day', date(?)) AND vh.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_priority_daily SET count_vulns = count_vulns + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND priority = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeGroupPriorityDaily - vulns_changed_this_day");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, vh1.priority FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh1 ON vh1.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh1.change_date) <= date_trunc('day', date(?)) AND vh1.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh1.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) AND ( vh1.e_vulnerability_id NOT IN ( SELECT e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) OR (vh1.priority, vh1.e_vulnerability_id) NOT IN ( SELECT priority, e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) ) AND vh1.priority <> 0");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                pstList.setTimestamp(9, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(10, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(11, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(12, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_priority_daily SET vulns_changed_this_day = vulns_changed_this_day + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND priority = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeGroupPriorityDaily - count_actions");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, ah.priority FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_action_history ah ON ah.e_vulnerability_action_id = a.e_vulnerability_action_id WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND date_trunc('day', ah.change_date) <= date_trunc('day', date(?)) AND ah.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_priority_daily SET count_actions = count_actions + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND priority = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeGroupPriorityDaily - actions_changed_this_day");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, e_group_id, ah1.priority FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_action_history ah1 ON ah1.e_vulnerability_action_id = a.e_vulnerability_action_id WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND date_trunc('day', ah1.change_date) <= date_trunc('day', date(?)) AND ah1.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah1.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) AND ( ah1.e_vulnerability_action_id NOT IN ( SELECT e_vulnerability_action_id FROM vuln.e_vulnerability_action_history ah2 WHERE date_trunc('day', ah2.change_date) <= date_trunc('day', date(?)) AND ah2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah2.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) OR (ah1.priority, ah1.e_vulnerability_action_id) NOT IN ( SELECT priority, e_vulnerability_action_id FROM vuln.e_vulnerability_action_history ah2 WHERE date_trunc('day', ah2.change_date) <= date_trunc('day', date(?)) AND ah2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah2.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) ) AND ah1.priority <> 0");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                pstList.setTimestamp(9, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(10, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(11, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(12, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_priority_daily SET actions_changed_this_day = actions_changed_this_day + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND priority = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
        } finally {
            db.exit();
        }
    }

    protected void computeGroupMembersDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing group Details Daily members");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_group_members_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_group_members_daily (calc_day, e_group_id, count_members) SELECT ?, e_group_id, COUNT(e_people_id) from e_group_membership_history gh WHERE date_trunc('day', gh.change_date) <= date_trunc('day', date(?)) AND gh.member AND gh.change_date = ( SELECT MAX(change_date) FROM e_group_membership_history gmh WHERE gmh.e_group_id = gh.e_group_id AND gmh.e_people_id = gh.e_people_id AND date_trunc('day', gmh.change_date) <= date_trunc('day', date(?)) ) GROUP BY e_group_id");
            pstInsert.setTimestamp(1, DateHelper.sql(effective_date));
            pstInsert.setTimestamp(2, DateHelper.sql(effective_date));
            pstInsert.setTimestamp(3, DateHelper.sql(effective_date));
            db.executeUpdate(pstInsert);
        } finally {
            db.exit();
        }
    }

    protected void computeGroupDetailsDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing group Details Daily metrics");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_group_details_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_group_details_daily (calc_day, e_group_id, r, a, c, i) VALUES (?, ?, ?, ?, ?, ?)");
            PreparedStatement pstSelect = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_group_details_daily WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
            String tableDef = "(SELECT e_raci_obj, tmp.e_people_id, r,a,c,i, e_group_id FROM (SELECT rac.e_raci_obj, rac.e_people_id, rac.r,rac.a,rac.c,rac.i FROM e_raci rac WHERE e_people_id <>0 UNION SELECT h.e_raci_obj, h.e_people_id, false, false, false, false FROM e_raci_history h WHERE h.current AND h.deleted AND date_trunc('day',h.change_date) <= date_trunc('day', date(?)) AND h.change_date = ( SELECT MAX (change_date) FROM e_raci_history hr WHERE hr.e_raci_obj = h.e_raci_obj AND date_trunc('day', hr.change_date) <= date_trunc('day', date(?)) ) ) AS tmp INNER JOIN (SELECT e_group_id, e_people_id from e_group_membership_history gh WHERE date_trunc('day', gh.change_date) <= date_trunc('day', date(?)) AND gh.member AND gh.change_date = ( SELECT MAX(change_date) FROM e_group_membership_history gmh WHERE gmh.e_group_id = gh.e_group_id AND gmh.e_people_id = gh.e_people_id AND date_trunc('day', gmh.change_date) <= date_trunc('day', date(?)) )) AS group_membership ON group_membership.e_people_id = tmp.e_people_id ) as rac";
            {
                _logger.debug("computeGroupDetailsDaily - count_total_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, rac.e_group_id FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) ");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_details_daily SET count_total_vulns = count_total_vulns + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
            {
                _logger.debug("computeGroupDetailsDaily - count_late_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, rac.e_group_id FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND (date_trunc('day',v.d_mav_target) <= date_trunc('day', date(?)))");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_details_daily SET count_late_vulns = count_late_vulns + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
            {
                _logger.debug("computeGroupDetailsDaily - count_delayed_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, rac.e_group_id FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj LEFT JOIN vuln.e_vulnerability_history vh ON (vh.e_vulnerability_id = v.e_vulnerability_id AND date_trunc('day',vh.change_date) < date_trunc('day', date(?))) INNER JOIN vuln.e_vulnerability_history current ON ( current.e_vulnerability_id = v.e_vulnerability_id AND date_trunc('day',current.change_date) = date_trunc('day', date(?)) AND current.target_date = v.d_mav_target) WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND vh.target_date IS NOT NULL AND v.d_mav_target > vh.target_date");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_details_daily SET count_delayed_vulns = count_delayed_vulns + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
            {
                _logger.debug("computeGroupDetailsDaily - count_total_actions");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, rac.e_group_id FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) ");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_details_daily SET count_total_actions = count_total_actions + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
            {
                _logger.debug("computeGroupDetailsDaily - count_late_actions");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, rac.e_group_id FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND (date_trunc('day',a.target_date) <= date_trunc('day', date(?)))");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_details_daily SET count_late_actions = count_late_actions + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
            {
                _logger.debug("computeGroupDetailsDaily - count_delayed_actions");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, rac.e_group_id FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj LEFT JOIN vuln.e_vulnerability_action_history ah ON (ah.e_vulnerability_action_id = a.e_vulnerability_action_id AND date_trunc('day',ah.change_date) < date_trunc('day', date(?))) INNER JOIN vuln.e_vulnerability_action_history current ON ( current.e_vulnerability_action_id = a.e_vulnerability_action_id AND date_trunc('day',current.change_date) = date_trunc('day', date(?)) AND current.target_date = a.target_date) WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND ah.target_date IS NOT NULL AND a.target_date > ah.target_date");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(effective_date));
                pstList.setTimestamp(8, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_group_details_daily SET count_delayed_actions = count_delayed_actions + ? WHERE calc_day = ? AND e_group_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateGroupMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
        } finally {
            db.exit();
        }
    }

    protected void computeUserStatusDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing user status Daily metrics");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_user_status_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_user_status_daily (calc_day, e_people_id, r, a, c, i, status) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pstSelect = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_user_status_daily WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND status = ?");
            String tableDef = "(SELECT rac.e_raci_obj, rac.e_people_id, rac.r,rac.a,rac.c,rac.i FROM e_raci rac WHERE e_people_id <>0 UNION SELECT h.e_raci_obj, h.e_people_id, false, false, false, false FROM e_raci_history h WHERE h.current AND h.deleted AND date_trunc('day',h.change_date) <= date_trunc('day', date(?)) AND h.change_date = (SELECT MAX (change_date) FROM e_raci_history hr WHERE hr.e_raci_obj = h.e_raci_obj AND date_trunc('day', hr.change_date) <= date_trunc('day', date(?))) ) as rac";
            {
                _logger.debug("computeUserStatusDaily - count_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, vh.mav_status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh ON vh.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh.change_date) <= date_trunc('day', date(?)) AND vh.mav_status IS NOT NULL AND vh.status=1 AND vh.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_status_daily SET count_vulns = count_vulns + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND status = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeUserStatusDaily - vulns_changed_this_day");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, vh1.mav_status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh1 ON vh1.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh1.change_date) <= date_trunc('day', date(?)) AND vh1.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh1.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) AND ( vh1.e_vulnerability_id NOT IN ( SELECT e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) OR (vh1.mav_status, vh1.e_vulnerability_id) NOT IN ( SELECT mav_status, e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) ) AND vh1.mav_status <> 0");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(8, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(9, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(10, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_status_daily SET vulns_changed_this_day = vulns_changed_this_day + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND status = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeUserStatusDaily - count_actions");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, ah.mav_status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_action_history ah ON ah.e_vulnerability_action_id = a.e_vulnerability_action_id WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND date_trunc('day', ah.change_date) <= date_trunc('day', date(?)) AND ah.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_status_daily SET count_actions = count_actions + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND status = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeUserStatusDaily - actions_changed_this_day");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, ah1.mav_status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_action_history ah1 ON ah1.e_vulnerability_action_id = a.e_vulnerability_action_id WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND date_trunc('day', ah1.change_date) <= date_trunc('day', date(?)) AND ah1.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah1.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) AND ( ah1.e_vulnerability_action_id NOT IN ( SELECT e_vulnerability_action_id FROM vuln.e_vulnerability_action_history ah2 WHERE date_trunc('day', ah2.change_date) <= date_trunc('day', date(?)) AND ah2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah2.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) OR (ah1.mav_status, ah1.e_vulnerability_action_id) NOT IN ( SELECT mav_status, e_vulnerability_action_id FROM vuln.e_vulnerability_action_history ah2 WHERE date_trunc('day', ah2.change_date) <= date_trunc('day', date(?)) AND ah2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah2.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) ) AND ah1.mav_status <> 0");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(8, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(9, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(10, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_status_daily SET actions_changed_this_day = actions_changed_this_day + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND status = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
        } finally {
            db.exit();
        }
    }

    protected void computeUserDecisionDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing user decision Daily metrics");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_user_decision_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_user_decision_daily (calc_day, e_people_id, r, a, c, i, decision) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pstSelect = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_user_decision_daily WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND decision = ?");
            String tableDef = "(SELECT rac.e_raci_obj, rac.e_people_id, rac.r,rac.a,rac.c,rac.i FROM e_raci rac WHERE e_people_id <>0 UNION SELECT h.e_raci_obj, h.e_people_id, false, false, false, false FROM e_raci_history h WHERE h.current AND h.deleted AND date_trunc('day',h.change_date) <= date_trunc('day', date(?)) AND h.change_date = (SELECT MAX (change_date) FROM e_raci_history hr WHERE hr.e_raci_obj = h.e_raci_obj AND date_trunc('day', hr.change_date) <= date_trunc('day', date(?))) ) as rac";
            {
                _logger.debug("computeUserDecisionDaily - count_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, vh.status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh ON vh.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh.change_date) <= date_trunc('day', date(?)) AND vh.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_decision_daily SET count_vulns = count_vulns + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND decision = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeUserDecisionDaily - vulns_changed_this_day");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, vh1.status FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh1 ON vh1.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh1.change_date) <= date_trunc('day', date(?)) AND vh1.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh1.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) AND ( vh1.e_vulnerability_id NOT IN ( SELECT e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) OR (vh1.status, vh1.e_vulnerability_id) NOT IN ( SELECT status, e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) ) AND vh1.status <> 0");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(8, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(9, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(10, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_decision_daily SET vulns_changed_this_day = vulns_changed_this_day + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND decision = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
        } finally {
            db.exit();
        }
    }

    protected void computeUserSeverityDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing user severity Daily metrics");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_user_severity_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_user_severity_daily (calc_day, e_people_id, r, a, c, i, severity) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pstSelect = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_user_severity_daily WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND severity = ?");
            String tableDef = "(SELECT rac.e_raci_obj, rac.e_people_id, rac.r,rac.a,rac.c,rac.i FROM e_raci rac WHERE e_people_id <>0 UNION SELECT h.e_raci_obj, h.e_people_id, false, false, false, false FROM e_raci_history h WHERE h.current AND h.deleted AND date_trunc('day',h.change_date) <= date_trunc('day', date(?)) AND h.change_date = (SELECT MAX (change_date) FROM e_raci_history hr WHERE hr.e_raci_obj = h.e_raci_obj AND date_trunc('day', hr.change_date) <= date_trunc('day', date(?))) ) as rac";
            {
                _logger.debug("computeUserSeverityDaily - count_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, v.severity FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_severity_daily SET count_vulns = count_vulns + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND severity = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
        } finally {
            db.exit();
        }
    }

    protected void computeUserPriorityDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing user priority Daily metrics");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_user_priority_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_user_priority_daily (calc_day, e_people_id, r, a, c, i, priority) VALUES (?, ?, ?, ?, ?, ?, ?)");
            PreparedStatement pstSelect = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_user_priority_daily WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND priority = ?");
            String tableDef = "(SELECT rac.e_raci_obj, rac.e_people_id, rac.r,rac.a,rac.c,rac.i FROM e_raci rac WHERE e_people_id <>0 UNION SELECT h.e_raci_obj, h.e_people_id, false, false, false, false FROM e_raci_history h WHERE h.current AND h.deleted AND date_trunc('day',h.change_date) <= date_trunc('day', date(?)) AND h.change_date = (SELECT MAX (change_date) FROM e_raci_history hr WHERE hr.e_raci_obj = h.e_raci_obj AND date_trunc('day', hr.change_date) <= date_trunc('day', date(?))) ) as rac";
            {
                _logger.debug("computeUserPriorityDaily - count_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, vh.priority FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh ON vh.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh.change_date) <= date_trunc('day', date(?)) AND vh.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_priority_daily SET count_vulns = count_vulns + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND priority = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeUserPriorityDaily - vulns_changed_this_day");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, vh1.priority FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_history vh1 ON vh1.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND date_trunc('day', vh1.change_date) <= date_trunc('day', date(?)) AND vh1.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh1.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) AND ( vh1.e_vulnerability_id NOT IN ( SELECT e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) OR (vh1.priority, vh1.e_vulnerability_id) NOT IN ( SELECT priority, e_vulnerability_id FROM vuln.e_vulnerability_history vh2 WHERE date_trunc('day', vh2.change_date) <= date_trunc('day', date(?)) AND vh2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = vh2.e_vulnerability_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) ) AND vh1.priority <> 0");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(8, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(9, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(10, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_priority_daily SET vulns_changed_this_day = vulns_changed_this_day + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND priority = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeUserPriorityDaily - count_actions");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, ah.priority FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_action_history ah ON ah.e_vulnerability_action_id = a.e_vulnerability_action_id WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND date_trunc('day', ah.change_date) <= date_trunc('day', date(?)) AND ah.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_priority_daily SET count_actions = count_actions + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND priority = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
            {
                _logger.debug("computeUserPriorityDaily - actions_changed_this_day");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i, ah1.priority FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj INNER JOIN vuln.e_vulnerability_action_history ah1 ON ah1.e_vulnerability_action_id = a.e_vulnerability_action_id WHERE date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND date_trunc('day', ah1.change_date) <= date_trunc('day', date(?)) AND ah1.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah1.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) AND ( ah1.e_vulnerability_action_id NOT IN ( SELECT e_vulnerability_action_id FROM vuln.e_vulnerability_action_history ah2 WHERE date_trunc('day', ah2.change_date) <= date_trunc('day', date(?)) AND ah2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah2.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) OR (ah1.priority, ah1.e_vulnerability_action_id) NOT IN ( SELECT priority, e_vulnerability_action_id FROM vuln.e_vulnerability_action_history ah2 WHERE date_trunc('day', ah2.change_date) <= date_trunc('day', date(?)) AND ah2.change_date = (SELECT MAX (hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = ah2.e_vulnerability_action_id AND date_trunc('day', hi.change_date) <= date_trunc('day', date(?)) ) ) ) AND ah1.priority <> 0");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                pstList.setTimestamp(7, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(8, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(9, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                pstList.setTimestamp(10, DateHelper.sql(DateHelper.rollBackwards(effective_date)));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_priority_daily SET actions_changed_this_day = actions_changed_this_day + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ? AND priority = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, true);
            }
        } finally {
            db.exit();
        }
    }

    private void updateUserMetrics(Db db, PreparedStatement pstList, PreparedStatement pstSelect, PreparedStatement pstInsert, PreparedStatement pstUpdate, boolean hasAxis) throws Exception {
        try {
            db.enter();
            PreparedStatement pstHistory = db.prepareStatement("SELECT r_change, a_change, c_change, i_change, deleted FROM e_raci_history WHERE e_people_id = ? AND e_raci_obj = ? AND date(change_date) > ? ORDER BY change_date DESC");
            ResultSet rs = db.executeQuery(pstList);
            Integer axis = null;
            if (rs.next()) {
                do {
                    RACI raci = new RACI();
                    raci.setRaciObjectId(rs.getInt(1));
                    raci.setUserId(rs.getInt(2));
                    raci.setR(rs.getBoolean(3));
                    raci.setA(rs.getBoolean(4));
                    raci.setC(rs.getBoolean(5));
                    raci.setI(rs.getBoolean(6));
                    if (hasAxis) axis = Integer.valueOf(rs.getInt(7));
                    pstHistory.setInt(1, raci.getUserId());
                    pstHistory.setInt(2, raci.getRaciObjectId());
                    pstHistory.setTimestamp(3, DateHelper.sql(effective_date));
                    try {
                        db.enter();
                        ResultSet rsHistory = db.executeQuery(pstHistory);
                        if (rsHistory.next()) {
                            do {
                                raci.setR(raci.isR() ^ rsHistory.getBoolean(1));
                                raci.setA(raci.isA() ^ rsHistory.getBoolean(2));
                                raci.setC(raci.isC() ^ rsHistory.getBoolean(3));
                                raci.setI(raci.isI() ^ rsHistory.getBoolean(4));
                            } while (rsHistory.next());
                        }
                        if (!raci.isR() && !raci.isA() && !raci.isC() && !raci.isI()) continue;
                        pstSelect.setDate(1, DateHelper.sqld(effective_date));
                        pstSelect.setInt(2, raci.getUserId());
                        pstSelect.setBoolean(3, raci.isR());
                        pstSelect.setBoolean(4, raci.isA());
                        pstSelect.setBoolean(5, raci.isC());
                        pstSelect.setBoolean(6, raci.isI());
                        if (axis != null) pstSelect.setInt(7, axis.intValue());
                        int nbRows = DbHelper.getKey(pstSelect).intValue();
                        if (nbRows == 0) {
                            pstInsert.setDate(1, DateHelper.sqld(effective_date));
                            pstInsert.setInt(2, raci.getUserId());
                            pstInsert.setBoolean(3, raci.isR());
                            pstInsert.setBoolean(4, raci.isA());
                            pstInsert.setBoolean(5, raci.isC());
                            pstInsert.setBoolean(6, raci.isI());
                            if (axis != null) pstInsert.setInt(7, axis.intValue());
                            db.executeUpdate(pstInsert);
                        }
                        pstUpdate.setInt(1, 1);
                        pstUpdate.setDate(2, DateHelper.sqld(effective_date));
                        pstUpdate.setInt(3, raci.getUserId());
                        pstUpdate.setBoolean(4, raci.isR());
                        pstUpdate.setBoolean(5, raci.isA());
                        pstUpdate.setBoolean(6, raci.isC());
                        pstUpdate.setBoolean(7, raci.isI());
                        if (axis != null) pstUpdate.setInt(8, axis.intValue());
                        db.executeUpdate(pstUpdate);
                    } finally {
                        db.exit();
                    }
                } while (rs.next());
            } else {
                _logger.debug("No RACI found");
            }
        } finally {
            db.exit();
        }
    }

    protected void computeUserDetailsDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing user Details Daily metrics");
            PreparedStatement pstClean = db.prepareStatement("DELETE FROM vuln.e_user_details_daily WHERE calc_day = ?");
            pstClean.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstClean);
            PreparedStatement pstInsert = db.prepareStatement("INSERT INTO vuln.e_user_details_daily (calc_day, e_people_id, r, a, c, i) VALUES (?, ?, ?, ?, ?, ?)");
            PreparedStatement pstSelect = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_user_details_daily WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
            String tableDef = "(SELECT rac.e_raci_obj, rac.e_people_id, rac.r,rac.a,rac.c,rac.i FROM e_raci rac WHERE e_people_id <>0 UNION SELECT h.e_raci_obj, h.e_people_id, false, false, false, false FROM e_raci_history h WHERE h.current AND h.deleted AND date_trunc('day',h.change_date) <= date_trunc('day', date(?)) AND h.change_date = (SELECT MAX (change_date) FROM e_raci_history hr WHERE hr.e_raci_obj = h.e_raci_obj AND date_trunc('day', hr.change_date) <= date_trunc('day', date(?))) ) as rac";
            {
                _logger.debug("computeUserDetailsDaily - count_total_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj WHERE date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) ");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_details_daily SET count_total_vulns = count_total_vulns + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
            {
                _logger.debug("computeUserDetailsDaily - count_late_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj WHERE  date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND (date_trunc('day',v.d_mav_target) <= date_trunc('day', date(?))) ");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_details_daily SET count_late_vulns = count_late_vulns + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
            {
                _logger.debug("computeUserDetailsDaily - count_delayed_vulns");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability') INNER JOIN vuln.e_vulnerability v ON v.e_raci_obj = rac.e_raci_obj LEFT JOIN vuln.e_vulnerability_history vh ON (vh.e_vulnerability_id = v.e_vulnerability_id AND date_trunc('day',vh.change_date) < date_trunc('day', date(?))) INNER JOIN vuln.e_vulnerability_history current ON ( current.e_vulnerability_id = v.e_vulnerability_id AND date_trunc('day',current.change_date) = date_trunc('day', date(?)) AND current.target_date = v.d_mav_target) WHERE  date_trunc('day',v.receive_date) <= date_trunc('day', date(?)) AND (date_trunc('day',v.closed_date) > date_trunc('day', date(?)) OR v.closed_date IS NULL ) AND vh.target_date IS NOT NULL AND v.d_mav_target > vh.target_date ");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_details_daily SET count_delayed_vulns = count_delayed_vulns + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
            {
                _logger.debug("computeUserDetailsDaily - count_total_actions");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj WHERE  date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL )");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_details_daily SET count_total_actions = count_total_actions + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
            {
                _logger.debug("computeUserDetailsDaily - count_late_actions");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj WHERE  date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND (date_trunc('day',a.target_date) <= date_trunc('day', date(?)))");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_details_daily SET count_late_actions = count_late_actions + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
            {
                _logger.debug("computeUserDetailsDaily - count_delayed_actions");
                PreparedStatement pstList = db.prepareStatement("SELECT rac.e_raci_obj, rac.e_people_id, rac.r, rac.a, rac.c, rac.i FROM  " + tableDef + " INNER JOIN e_raci_objects objs ON objs.e_raci_obj = rac.e_raci_obj INNER JOIN e_raci_object_type type ON (type.e_raci_object_type_id = objs.e_raci_object_type_id AND type.schema_name='vuln' AND type.table_name = 'e_vulnerability_action') INNER JOIN vuln.e_vulnerability_action a ON a.e_raci_obj = rac.e_raci_obj LEFT JOIN vuln.e_vulnerability_action_history ah ON (ah.e_vulnerability_action_id = a.e_vulnerability_action_id AND date_trunc('day',ah.change_date) < date_trunc('day', date(?))) INNER JOIN vuln.e_vulnerability_action_history current ON ( current.e_vulnerability_action_id = a.e_vulnerability_action_id AND date_trunc('day',current.change_date) = date_trunc('day', date(?)) AND current.target_date = a.target_date) WHERE  date_trunc('day',a.creation_date) <= date_trunc('day', date(?)) AND (date_trunc('day',a.closed_date) > date_trunc('day', date(?)) OR a.closed_date IS NULL ) AND ah.target_date IS NOT NULL AND a.target_date > ah.target_date ");
                pstList.setTimestamp(1, DateHelper.sql(effective_date));
                pstList.setTimestamp(2, DateHelper.sql(effective_date));
                pstList.setTimestamp(3, DateHelper.sql(effective_date));
                pstList.setTimestamp(4, DateHelper.sql(effective_date));
                pstList.setTimestamp(5, DateHelper.sql(effective_date));
                pstList.setTimestamp(6, DateHelper.sql(effective_date));
                PreparedStatement pstUpdate = db.prepareStatement("UPDATE vuln.e_user_details_daily SET count_delayed_actions = count_delayed_actions + ? WHERE calc_day = ? AND e_people_id = ? AND r = ? AND a = ? AND c = ? AND i = ?");
                updateUserMetrics(db, pstList, pstSelect, pstInsert, pstUpdate, false);
            }
        } finally {
            db.exit();
        }
    }

    protected void computeVersionsDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Versions Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(DISTINCT vpv.e_version_id) FROM vuln.e_vulnerability_vpv vpv INNER JOIN vuln.e_vulnerability v ON vpv.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day', receive_date) <= date_trunc('day', date(?)) AND ((triage_date IS NULL AND is_new_published) OR date_trunc('day', triage_date) >  date_trunc('day', date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer inNew = DbHelper.getKey(pst);
            if (inNew == null) inNew = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT vpv.e_version_id) FROM vuln.e_vulnerability_vpv vpv INNER JOIN vuln.e_vulnerability v ON vpv.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',start_investigate) <= date_trunc('day', date(?)) AND (end_investigate IS NULL OR date_trunc('day',end_investigate) > date_trunc('day', date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer inInvest = DbHelper.getKey(pst);
            if (inInvest == null) inInvest = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT vpv.e_version_id) FROM vuln.e_vulnerability_vpv vpv INNER JOIN vuln.e_vulnerability v ON vpv.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',start_action) <= date_trunc('day',date(?)) AND (end_action IS NULL OR date_trunc('day',end_action) > date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer inAction = DbHelper.getKey(pst);
            if (inAction == null) inAction = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_version WHERE date_trunc('day', creation_date) = date_trunc('day', date(?))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            Integer created = DbHelper.getKey(pst);
            if (created == null) created = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_version WHERE (active AND date_trunc('day',obj_lm) <= date_trunc('day',date(?))) OR e_version_id IN ( SELECT e_version_id FROM asset.e_version_history h WHERE date_trunc('day',h.change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM asset.e_version_history hi WHERE hi.e_version_id = h.e_version_id AND date_trunc('day',change_date) <= date_trunc('day',date(?)) ) AND active)");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer active = DbHelper.getKey(pst);
            if (active == null) active = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_version WHERE (NOT active AND date_trunc('day',obj_lm) <= date_trunc('day', date(?))) OR e_version_id IN ( SELECT e_version_id FROM asset.e_version_history h WHERE date_trunc('day',h.change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM asset.e_version_history hi WHERE hi.e_version_id = h.e_version_id AND date_trunc('day',change_date) <= date_trunc('day',date(?)) ) AND NOT active)");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer na = DbHelper.getKey(pst);
            if (na == null) na = Integer.valueOf(0);
            pst = db.prepareStatement(st_ins_version_daily);
            pst.setDate(1, DateHelper.sqld(effective_date));
            pst.setInt(2, inNew.intValue());
            pst.setInt(3, inInvest.intValue());
            pst.setInt(4, inAction.intValue());
            pst.setInt(5, created.intValue());
            pst.setInt(6, active.intValue());
            pst.setInt(7, na.intValue());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new Exception("Error when trying to add a version daily metric");
            _logger.info("Products Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    private static final String st_ins_version_daily = "INSERT INTO vuln.e_version_daily (calc_day, in_new, in_repair, in_investigate, created, active, n_a) VALUES (?, ?, ?, ?, ?, ?, ?)";

    protected void computeProductsDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Products Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(DISTINCT vpv.e_product_id) FROM vuln.e_vulnerability_vpv vpv INNER JOIN vuln.e_vulnerability v ON vpv.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',receive_date) <= date_trunc('day',date(?)) AND ((triage_date IS NULL AND is_new_published) OR date_trunc('day',triage_date) >  date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer inNew = DbHelper.getKey(pst);
            if (inNew == null) inNew = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT vpv.e_product_id) FROM vuln.e_vulnerability_vpv vpv INNER JOIN vuln.e_vulnerability v ON vpv.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',start_investigate) <= date_trunc('day',date(?)) AND (end_investigate IS NULL OR date_trunc('day',end_investigate) > date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer inInvest = DbHelper.getKey(pst);
            if (inInvest == null) inInvest = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT vpv.e_product_id) FROM vuln.e_vulnerability_vpv vpv INNER JOIN vuln.e_vulnerability v ON vpv.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',start_action) <= date_trunc('day',date(?)) AND (end_action IS NULL OR date_trunc('day',end_action) > date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer inAction = DbHelper.getKey(pst);
            if (inAction == null) inAction = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_product WHERE date_trunc('day', creation_date) = date_trunc('day', date(?))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            Integer created = DbHelper.getKey(pst);
            if (created == null) created = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_product WHERE (active AND date_trunc('day',obj_lm) <= date_trunc('day',date(?))) OR e_product_id IN ( SELECT e_product_id FROM asset.e_product_history h WHERE date_trunc('day',h.change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM asset.e_product_history hi WHERE hi.e_product_id = h.e_product_id AND date_trunc('day',change_date) <=  date_trunc('day',date(?))) AND active)");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer active = DbHelper.getKey(pst);
            if (active == null) active = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_product WHERE (esis_hide AND date_trunc('day',obj_lm) <= date_trunc('day',date(?))) OR e_product_id IN ( SELECT e_product_id FROM asset.e_product_history h WHERE date_trunc('day',h.change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM asset.e_product_history hi WHERE hi.e_product_id = h.e_product_id AND date_trunc('day',change_date) <= date_trunc('day',date(?)) ) AND esis_hide)");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer ignore = DbHelper.getKey(pst);
            if (ignore == null) ignore = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_product WHERE (NOT active AND NOT esis_hide AND date_trunc('day',obj_lm) <= date_trunc('day',date(?))) OR e_product_id IN ( SELECT e_product_id FROM asset.e_product_history h WHERE date_trunc('day',h.change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM asset.e_product_history hi WHERE hi.e_product_id = h.e_product_id AND date_trunc('day',change_date) <= date_trunc('day',date(?)) ) AND NOT active AND NOT esis_hide)");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer na = DbHelper.getKey(pst);
            if (na == null) na = Integer.valueOf(0);
            pst = db.prepareStatement(st_ins_product_daily);
            pst.setDate(1, DateHelper.sqld(effective_date));
            pst.setInt(2, inNew.intValue());
            pst.setInt(3, inInvest.intValue());
            pst.setInt(4, inAction.intValue());
            pst.setInt(5, created.intValue());
            pst.setInt(6, active.intValue());
            pst.setInt(7, ignore.intValue());
            pst.setInt(8, na.intValue());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new Exception("Error when trying to add a product daily metric");
            _logger.info("Products Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    private static final String st_ins_product_daily = "INSERT INTO vuln.e_product_daily (calc_day, in_new, in_repair, in_investigate, created, active, ignored, n_a) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    protected void computeVendorsDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Vendors Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(DISTINCT vpv.e_vendor_id) FROM vuln.e_vulnerability_vpv vpv INNER JOIN vuln.e_vulnerability v ON vpv.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',receive_date) <= date_trunc('day',date(?)) AND ((triage_date IS NULL AND is_new_published) OR date_trunc('day',triage_date) >  date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer inNew = DbHelper.getKey(pst);
            if (inNew == null) inNew = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT vpv.e_vendor_id) FROM vuln.e_vulnerability_vpv vpv INNER JOIN vuln.e_vulnerability v ON vpv.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',start_investigate) <= date_trunc('day',date(?)) AND (end_investigate IS NULL OR date_trunc('day',end_investigate) > date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer inInvest = DbHelper.getKey(pst);
            if (inInvest == null) inInvest = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT vpv.e_vendor_id) FROM vuln.e_vulnerability_vpv vpv INNER JOIN vuln.e_vulnerability v ON vpv.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',start_action) <= date_trunc('day',date(?)) AND (end_action IS NULL OR date_trunc('day',end_action) > date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer inAction = DbHelper.getKey(pst);
            if (inAction == null) inAction = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_vendor WHERE date_trunc('day', creation_date) = date_trunc('day', date(?))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            Integer created = DbHelper.getKey(pst);
            if (created == null) created = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_vendor WHERE (active AND date_trunc('day',obj_lm) <= date_trunc('day',date(?))) OR e_vendor_id IN ( SELECT e_vendor_id FROM asset.e_vendor_history h WHERE date_trunc('day',h.change_date) <=  date_trunc('day',date(?))AND change_date = ( SELECT MAX(hi.change_date) FROM asset.e_vendor_history hi WHERE hi.e_vendor_id = h.e_vendor_id AND date_trunc('day',change_date) <= date_trunc('day',date(?)) ) AND active)");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer active = DbHelper.getKey(pst);
            if (active == null) active = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_vendor WHERE (esis_hide AND date_trunc('day',obj_lm) <= date_trunc('day',date(?))) OR e_vendor_id IN ( SELECT e_vendor_id FROM asset.e_vendor_history h WHERE date_trunc('day',h.change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM asset.e_vendor_history hi WHERE hi.e_vendor_id = h.e_vendor_id AND date_trunc('day',change_date) <= date_trunc('day',date(?)) ) AND esis_hide)");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer ignore = DbHelper.getKey(pst);
            if (ignore == null) ignore = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM asset.e_vendor WHERE (NOT active AND NOT esis_hide AND date_trunc('day',obj_lm) <= date_trunc('day',date(?))) OR e_vendor_id IN ( SELECT e_vendor_id FROM asset.e_vendor_history h WHERE date_trunc('day',h.change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM asset.e_vendor_history hi WHERE hi.e_vendor_id = h.e_vendor_id AND date_trunc('day',change_date) <=  date_trunc('day',date(?))) AND NOT active AND NOT esis_hide)");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer na = DbHelper.getKey(pst);
            if (na == null) na = Integer.valueOf(0);
            pst = db.prepareStatement(st_ins_vendor_daily);
            pst.setDate(1, DateHelper.sqld(effective_date));
            pst.setInt(2, inNew.intValue());
            pst.setInt(3, inInvest.intValue());
            pst.setInt(4, inAction.intValue());
            pst.setInt(5, created.intValue());
            pst.setInt(6, active.intValue());
            pst.setInt(7, ignore.intValue());
            pst.setInt(8, na.intValue());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new Exception("Error when trying to add a vendor daily metric");
            _logger.info("Vendors Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    private static final String st_ins_vendor_daily = "INSERT INTO vuln.e_vendor_daily (calc_day, in_new, in_repair, in_investigate, created, active, ignored, n_a) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * Note that this part cant be computed in the past because we don't have a group membership history  
     *
     */
    protected void computeGroupsDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Groups Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(DISTINCT gtp.e_group_id) FROM vuln.e_vulnerability_action_history h INNER JOIN vuln.e_vulnerability_action a ON a.e_vulnerability_action_id = h.e_vulnerability_action_id LEFT JOIN e_group_to_people gtp ON gtp.e_people_id = h.owner WHERE date_trunc('day',change_date) <=  date_trunc('day',date(?))AND change_date = ( SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = h.e_vulnerability_action_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND (a.closed_date  IS NULL OR date_trunc('day',a.closed_date) > date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer withOpenedActions = DbHelper.getKey(pst);
            if (withOpenedActions == null) withOpenedActions = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT gtp.e_group_id) FROM vuln.e_vulnerability_action_history h INNER JOIN vuln.e_vulnerability_action a ON a.e_vulnerability_action_id = h.e_vulnerability_action_id LEFT JOIN e_group_to_people gtp ON gtp.e_people_id = h.owner WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = h.e_vulnerability_action_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND (a.closed_date  IS NULL OR date_trunc('day',a.closed_date) > date_trunc('day',date(?))) AND date_trunc('day',a.target_date) <= date_trunc('day',date(?))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            pst.setTimestamp(4, DateHelper.sql(effective_date));
            Integer withLateActions = DbHelper.getKey(pst);
            if (withLateActions == null) withLateActions = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT gtp.e_group_id) from vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability v ON v.e_vulnerability_id = h.e_vulnerability_id LEFT JOIN e_group_to_people gtp ON gtp.e_people_id = h.owner WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND (v.closed_date  IS NULL OR date_trunc('day',v.closed_date) > date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer ownRepairingVuln = DbHelper.getKey(pst);
            if (ownRepairingVuln == null) ownRepairingVuln = Integer.valueOf(0);
            pst = db.prepareStatement(st_ins_grp_daily);
            pst.setDate(1, DateHelper.sqld(effective_date));
            pst.setInt(2, withOpenedActions.intValue());
            pst.setInt(3, withLateActions.intValue());
            pst.setInt(4, ownRepairingVuln.intValue());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new Exception("Error when trying to add a group daily metric");
            _logger.info("Groups Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    private static final String st_ins_grp_daily = "INSERT INTO vuln.e_groups_daily (calc_day, with_opened_actions, with_late_actions, own_unclosed_repairing_vuln) VALUES (?, ?, ?, ?)";

    protected void computeUsersDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Users Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(DISTINCT h.owner) FROM vuln.e_vulnerability_action_history h INNER JOIN vuln.e_vulnerability_action a ON a.e_vulnerability_action_id = h.e_vulnerability_action_id WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = h.e_vulnerability_action_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND (a.closed_date  IS NULL OR date_trunc('day',a.closed_date) > date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer withOpenedActions = DbHelper.getKey(pst);
            if (withOpenedActions == null) withOpenedActions = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT h.owner) FROM vuln.e_vulnerability_action_history h INNER JOIN vuln.e_vulnerability_action a ON a.e_vulnerability_action_id = h.e_vulnerability_action_id WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = h.e_vulnerability_action_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND (a.closed_date  IS NULL OR date_trunc('day',a.closed_date) > date_trunc('day',date(?))) AND date_trunc('day',h.target_date) <= date_trunc('day',date(?))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            pst.setTimestamp(4, DateHelper.sql(effective_date));
            Integer withLateActions = DbHelper.getKey(pst);
            if (withLateActions == null) withLateActions = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT h.owner) from vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability v ON v.e_vulnerability_id = h.e_vulnerability_id WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND (v.closed_date  IS NULL OR date_trunc('day',v.closed_date) > date_trunc('day',date(?))) AND h.mav_status IS NOT NULL");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer ownRepairingVuln = DbHelper.getKey(pst);
            if (ownRepairingVuln == null) ownRepairingVuln = Integer.valueOf(0);
            pst = db.prepareStatement(st_ins_user_daily);
            pst.setDate(1, DateHelper.sqld(effective_date));
            pst.setInt(2, withOpenedActions.intValue());
            pst.setInt(3, withLateActions.intValue());
            pst.setInt(4, ownRepairingVuln.intValue());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new Exception("Error when trying to add a user daily metric");
            _logger.info("Users Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    private static final String st_ins_user_daily = "INSERT INTO vuln.e_users_daily (calc_day, with_opened_actions, with_late_actions, own_unclosed_repairing_vuln) VALUES (?, ?, ?, ?)";

    protected void computeSeverityDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Severity Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(*), severity FROM vuln.e_vulnerability WHERE date_trunc('day', date(?)) = date_trunc('day', receive_date) GROUP BY severity");
            pst.setDate(1, DateHelper.sqld(effective_date));
            ResultSet rs = db.executeQuery(pst);
            PreparedStatement pstReceived = db.prepareStatement("UPDATE vuln.e_severity_daily SET count_received = ? WHERE calc_day = ? AND severity = ?");
            if (rs.next()) {
                do {
                    pstReceived.setInt(1, rs.getInt(1));
                    pstReceived.setDate(2, DateHelper.sqld(effective_date));
                    pstReceived.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstReceived);
                    if (res != 1) throw new Exception("Error when updating severity daily for received vulns");
                } while (rs.next());
            } else {
                _logger.debug("computeSeverityDaily -  count_received : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), severity FROM vuln.e_vulnerability WHERE date_trunc('day',receive_date) <= date_trunc('day',date(?)) AND ((triage_date IS NULL AND is_new_published) OR date_trunc('day',triage_date) >  date_trunc('day',date(?))) GROUP BY severity");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstNew = db.prepareStatement("UPDATE vuln.e_severity_daily SET count_new = ? WHERE calc_day = ? AND severity = ?");
            if (rs.next()) {
                do {
                    pstNew.setInt(1, rs.getInt(1));
                    pstNew.setDate(2, DateHelper.sqld(effective_date));
                    pstNew.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstNew);
                    if (res != 1) throw new Exception("Error when updating severity daily for new vulns");
                } while (rs.next());
            } else {
                _logger.debug("computeSeverityDaily -  count_new : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), severity FROM vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability v ON h.e_vulnerability_id = v.e_vulnerability_id WHERE date_trunc('day',change_date) <=  date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND date_trunc('day',v.start_action) <=  date_trunc('day',date(?))AND (v.end_action IS NULL OR date_trunc('day',v.end_action) > date_trunc('day',date(?))) GROUP BY severity ORDER BY severity");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            pst.setTimestamp(4, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstAction = db.prepareStatement("UPDATE vuln.e_severity_daily SET count_action = ? WHERE calc_day = ? AND severity = ? ");
            if (rs.next()) {
                do {
                    pstAction.setInt(1, rs.getInt(1));
                    pstAction.setDate(2, DateHelper.sqld(effective_date));
                    pstAction.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstAction);
                    if (res != 1) throw new Exception("Error when updating severity daily for repair vulns");
                } while (rs.next());
            } else {
                _logger.debug("computeSeverityDaily - count_action : no rows found");
            }
            _logger.info("Severity Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    protected void computePriorityDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Priority Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(*), priority FROM vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability v ON h.e_vulnerability_id = v.e_vulnerability_id WHERE  date_trunc('day',change_date) <=  date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND  date_trunc('day',change_date) <= date_trunc('day',date(?))) AND  date_trunc('day',v.start_action) <= date_trunc('day',date(?)) AND (v.end_action IS NULL OR  date_trunc('day',v.end_action) > date_trunc('day',date(?))) GROUP BY priority ORDER BY priority");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            pst.setTimestamp(4, DateHelper.sql(effective_date));
            ResultSet rs = db.executeQuery(pst);
            PreparedStatement pstVulns = db.prepareStatement("UPDATE vuln.e_priority_daily SET count_vulns_repair = ? WHERE calc_day = ? AND priority = ? ");
            if (rs.next()) {
                do {
                    pstVulns.setInt(1, rs.getInt(1));
                    pstVulns.setDate(2, DateHelper.sqld(effective_date));
                    pstVulns.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstVulns);
                    if (res != 1) throw new Exception("Error when updating priority daily for repair vulns");
                } while (rs.next());
            } else {
                _logger.debug("computePriorityDaily - count_vulns_repairs : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), priority FROM vuln.e_vulnerability_action_history h WHERE  date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = h.e_vulnerability_action_id AND  date_trunc('day',change_date) <= date_trunc('day',date(?))) GROUP BY priority ORDER BY priority");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstActions = db.prepareStatement("UPDATE vuln.e_priority_daily SET count_actions = ? WHERE calc_day = ? AND priority = ? ");
            if (rs.next()) {
                do {
                    pstActions.setInt(1, rs.getInt(1));
                    pstActions.setDate(2, DateHelper.sqld(effective_date));
                    pstActions.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstActions);
                    if (res != 1) throw new Exception("Error when updating priority daily for actions");
                } while (rs.next());
            } else {
                _logger.debug("computePriorityDaily - count_actions : no rows found");
            }
            _logger.info("Priority Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    protected void computeStatusDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Status Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(*), mav_status FROM vuln.e_vulnerability_history h WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) GROUP BY mav_status ORDER BY mav_status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            ResultSet rs = db.executeQuery(pst);
            PreparedStatement pstAll = db.prepareStatement("UPDATE vuln.e_status_daily SET count_all_vulns = ? WHERE calc_day = ? AND status = ? ");
            if (rs.next()) {
                do {
                    pstAll.setInt(1, rs.getInt(1));
                    pstAll.setDate(2, DateHelper.sqld(effective_date));
                    pstAll.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstAll);
                    if (res != 1) throw new Exception("Error when updating status daily for all vulns");
                } while (rs.next());
            } else {
                _logger.debug("computeStatusDaily - count_all : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), mav_status FROM vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability_vrt_items it ON it.e_vulnerability_id = h.e_vulnerability_id INNER JOIN vuln.e_vulnerability_vrt vrt ON vrt.e_vulnerability_vrt_id = it.e_vulnerability_vrt_id WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND vrt.closed GROUP BY mav_status ORDER BY mav_status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstClosedVRT = db.prepareStatement("UPDATE vuln.e_status_daily SET count_vulns_closed_vrt = ? WHERE calc_day = ? AND status = ? ");
            if (rs.next()) {
                do {
                    pstClosedVRT.setInt(1, rs.getInt(1));
                    pstClosedVRT.setDate(2, DateHelper.sqld(effective_date));
                    pstClosedVRT.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstClosedVRT);
                    if (res != 1) throw new Exception("Error when updating status daily for vulns in closed vrt");
                } while (rs.next());
            } else {
                _logger.debug("computeStatusDaily - count_closed_vrt : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), mav_status FROM vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability_vrt_items it ON it.e_vulnerability_id = h.e_vulnerability_id INNER JOIN vuln.e_vulnerability_vrt vrt ON vrt.e_vulnerability_vrt_id = it.e_vulnerability_vrt_id WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND vrt.start_date IS NULL GROUP BY mav_status ORDER BY mav_status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstNextVRT = db.prepareStatement("UPDATE vuln.e_status_daily SET count_vulns_next_vrt = ? WHERE calc_day = ? AND status = ? ");
            if (rs.next()) {
                do {
                    pstNextVRT.setInt(1, rs.getInt(1));
                    pstNextVRT.setDate(2, DateHelper.sqld(effective_date));
                    pstNextVRT.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstNextVRT);
                    if (res != 1) throw new Exception("Error when updating status daily for vulns in next vrt");
                } while (rs.next());
            } else {
                _logger.debug("computeStatusDaily - count_next_vrt : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), mav_status FROM vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability_vrt_items it ON it.e_vulnerability_id = h.e_vulnerability_id INNER JOIN vuln.e_vulnerability_vrt vrt ON vrt.e_vulnerability_vrt_id = it.e_vulnerability_vrt_id WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND vrt.start_date IS NOT NULL AND vrt.end_date IS NULL GROUP BY mav_status ORDER BY mav_status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstCurrentVRT = db.prepareStatement("UPDATE vuln.e_status_daily SET count_vulns_current_vrt = ? WHERE calc_day = ? AND status = ? ");
            if (rs.next()) {
                do {
                    pstCurrentVRT.setInt(1, rs.getInt(1));
                    pstCurrentVRT.setDate(2, DateHelper.sqld(effective_date));
                    pstCurrentVRT.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstCurrentVRT);
                    if (res != 1) throw new Exception("Error when updating status daily for vulns in current vrt");
                } while (rs.next());
            } else {
                _logger.debug("computeStatusDaily - count_current_vrt : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), mav_status FROM vuln.e_vulnerability_action_history h WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_action_history hi WHERE hi.e_vulnerability_action_id = h.e_vulnerability_action_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) GROUP BY mav_status ORDER BY mav_status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstAction = db.prepareStatement("UPDATE vuln.e_status_daily SET count_actions = ? WHERE calc_day = ? AND status = ? ");
            if (rs.next()) {
                do {
                    pstAction.setInt(1, rs.getInt(1));
                    pstAction.setDate(2, DateHelper.sqld(effective_date));
                    pstAction.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstAction);
                    if (res != 1) throw new Exception("Error when updating status daily for actions");
                } while (rs.next());
            } else {
                _logger.debug("computeStatusDaily - count_actions : no rows found");
            }
            _logger.info("Status Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    protected void computeDecisionDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Decisions Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(*), status FROM vuln.e_vulnerability_history h WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) GROUP BY status ORDER BY status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            ResultSet rs = db.executeQuery(pst);
            PreparedStatement pstAll = db.prepareStatement("UPDATE vuln.e_decision_daily SET count_all = ? WHERE calc_day = ? AND decision = ? ");
            if (rs.next()) {
                do {
                    pstAll.setInt(1, rs.getInt(1));
                    pstAll.setDate(2, DateHelper.sqld(effective_date));
                    pstAll.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstAll);
                    if (res != 1) throw new Exception("Error when updating decision daily for all vulns");
                } while (rs.next());
            } else {
                _logger.debug("computeDecisionDaily - count_all : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), status FROM vuln.e_vulnerability_history h WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND h.e_vulnerability_id = ( SELECT e_vulnerability_id FROM vuln.e_vulnerability v WHERE (closed_date IS NULL OR date_trunc('day',closed_date) > date_trunc('day',date(?))) AND v.e_vulnerability_id = h.e_vulnerability_id) GROUP BY status ORDER BY status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstAllUnclosed = db.prepareStatement("UPDATE vuln.e_decision_daily SET count_all_unclosed = ? WHERE calc_day = ? AND decision = ? ");
            if (rs.next()) {
                do {
                    pstAllUnclosed.setInt(1, rs.getInt(1));
                    pstAllUnclosed.setDate(2, DateHelper.sqld(effective_date));
                    pstAllUnclosed.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstAllUnclosed);
                    if (res != 1) throw new Exception("Error when updating decision daily for all unclosed vulns");
                } while (rs.next());
            } else {
                _logger.debug("computeDecisionDaily - count_all_unclosed : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), status FROM vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability_vrt_items it ON it.e_vulnerability_id = h.e_vulnerability_id INNER JOIN vuln.e_vulnerability_vrt vrt ON vrt.e_vulnerability_vrt_id = it.e_vulnerability_vrt_id WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND vrt.closed GROUP BY status ORDER BY status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstClosedVRT = db.prepareStatement("UPDATE vuln.e_decision_daily SET count_closed_vrt = ? WHERE calc_day = ? AND decision = ? ");
            if (rs.next()) {
                do {
                    pstClosedVRT.setInt(1, rs.getInt(1));
                    pstClosedVRT.setDate(2, DateHelper.sqld(effective_date));
                    pstClosedVRT.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstClosedVRT);
                    if (res != 1) throw new Exception("Error when updating decision daily for vulns in closed vrt");
                } while (rs.next());
            } else {
                _logger.debug("computeDecisionDaily - count_closed_vrt : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), status FROM vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability_vrt_items it ON it.e_vulnerability_id = h.e_vulnerability_id INNER JOIN vuln.e_vulnerability_vrt vrt ON vrt.e_vulnerability_vrt_id = it.e_vulnerability_vrt_id WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = ( SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND vrt.closed AND h.e_vulnerability_id = ( SELECT e_vulnerability_id FROM vuln.e_vulnerability v WHERE (closed_date IS NULL OR date_trunc('day',closed_date) > date_trunc('day',date(?))) AND v.e_vulnerability_id = h.e_vulnerability_id ) GROUP BY status ORDER BY status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstClosedVRTUnclosed = db.prepareStatement("UPDATE vuln.e_decision_daily SET count_closed_vrt_unclosed = ? WHERE calc_day = ? AND decision = ? ");
            if (rs.next()) {
                do {
                    pstClosedVRTUnclosed.setInt(1, rs.getInt(1));
                    pstClosedVRTUnclosed.setDate(2, DateHelper.sqld(effective_date));
                    pstClosedVRTUnclosed.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstClosedVRTUnclosed);
                    if (res != 1) throw new Exception("Error when updating decision daily for unclosed vulns in closed vrt");
                } while (rs.next());
            } else {
                _logger.debug("computeDecisionDaily - count_closed_vrt_unclosed : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), status FROM vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability_vrt_items it ON it.e_vulnerability_id = h.e_vulnerability_id INNER JOIN vuln.e_vulnerability_vrt vrt ON vrt.e_vulnerability_vrt_id = it.e_vulnerability_vrt_id WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND vrt.start_date IS NULL GROUP BY status ORDER BY status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstNextVRT = db.prepareStatement("UPDATE vuln.e_decision_daily SET count_next_vrt = ? WHERE calc_day = ? AND decision = ? ");
            if (rs.next()) {
                do {
                    pstNextVRT.setInt(1, rs.getInt(1));
                    pstNextVRT.setDate(2, DateHelper.sqld(effective_date));
                    pstNextVRT.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstNextVRT);
                    if (res != 1) throw new Exception("Error when updating decision daily for vulns in next vrt");
                } while (rs.next());
            } else {
                _logger.debug("computeDecisionDaily - count_next_vrt : no rows found");
            }
            pst = db.prepareStatement("SELECT COUNT(*), status FROM vuln.e_vulnerability_history h INNER JOIN vuln.e_vulnerability_vrt_items it ON it.e_vulnerability_id = h.e_vulnerability_id INNER JOIN vuln.e_vulnerability_vrt vrt ON vrt.e_vulnerability_vrt_id = it.e_vulnerability_vrt_id WHERE date_trunc('day',change_date) <= date_trunc('day',date(?)) AND change_date = (SELECT MAX(hi.change_date) FROM vuln.e_vulnerability_history hi WHERE hi.e_vulnerability_id = h.e_vulnerability_id AND date_trunc('day',change_date) <= date_trunc('day',date(?))) AND vrt.start_date IS NOT NULL AND vrt.end_date IS NULL GROUP BY status ORDER BY status");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            rs = db.executeQuery(pst);
            PreparedStatement pstCurrentVRT = db.prepareStatement("UPDATE vuln.e_decision_daily SET count_current_vrt = ? WHERE calc_day = ? AND decision = ? ");
            if (rs.next()) {
                do {
                    pstCurrentVRT.setInt(1, rs.getInt(1));
                    pstCurrentVRT.setDate(2, DateHelper.sqld(effective_date));
                    pstCurrentVRT.setInt(3, rs.getInt(2));
                    int res = db.executeUpdate(pstCurrentVRT);
                    if (res != 1) throw new Exception("Error when updating decision daily for vulns in current vrt");
                } while (rs.next());
            } else {
                _logger.debug("computeDecisionDaily - count_current_vrt : no rows found");
            }
            _logger.info("Decision Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    protected void computeActionsDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Actions Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability_action WHERE date_trunc('day',creation_date) <= date_trunc('day',date(?)) AND (closed_date IS NULL OR date_trunc('day',closed_date) > date_trunc('day',date(?))) AND NOT hidden");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer countUnclosed = DbHelper.getKey(pst);
            if (countUnclosed == null) countUnclosed = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability_action WHERE date_trunc('day',creation_date) <= date_trunc('day',date(?)) AND (closed_date IS NULL OR date_trunc('day',closed_date) > date_trunc('day',date(?))) AND NOT hidden AND date_trunc('day',target_date) > date_trunc('day',date(?))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            Integer countUnclosedLate = DbHelper.getKey(pst);
            if (countUnclosedLate == null) countUnclosedLate = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability_action WHERE date_trunc('day', closed_date) = date_trunc('day', date(?))");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countClosed = DbHelper.getKey(pst);
            if (countClosed == null) countClosed = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability_action WHERE NOT hidden AND date_trunc('day',creation_date) <= date_trunc('day',date(?))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            Integer countAll = DbHelper.getKey(pst);
            if (countAll == null) countAll = Integer.valueOf(0);
            pst = db.prepareStatement(st_ins_action_daily);
            pst.setDate(1, DateHelper.sqld(effective_date));
            pst.setInt(2, countUnclosed.intValue());
            pst.setInt(3, countUnclosedLate.intValue());
            pst.setInt(4, countClosed.intValue());
            pst.setInt(5, countAll.intValue());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new Exception("Error when trying to add a action daily metric");
            _logger.info("Action Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    private static final String st_ins_action_daily = "INSERT INTO vuln.e_actions_daily (calc_day, count_unclosed, count_unclosed_late, count_closed, count_all) VALUES(?, ?, ?, ?, ?)";

    protected void computeTriageDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Triage Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability WHERE date_trunc('day', date(?)) = date_trunc('day', triage_date) AND e_vulnerability_id IN (SELECT e_vulnerability_id FROM vuln.e_vulnerability_vrt_items)");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countTriaged = DbHelper.getKey(pst);
            if (countTriaged == null) countTriaged = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability WHERE date_trunc('day', date(?)) = date_trunc('day', triage_date) AND e_vulnerability_id IN (SELECT e_vulnerability_id FROM vuln.e_vulnerability_vrt_items) AND ignored");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countIgnored = DbHelper.getKey(pst);
            if (countIgnored == null) countIgnored = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability WHERE date_trunc('day', date(?)) = date_trunc('day', triage_date) AND e_vulnerability_id IN (SELECT e_vulnerability_id FROM vuln.e_vulnerability_vrt_items) AND status = 0");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countAdd = DbHelper.getKey(pst);
            if (countAdd == null) countAdd = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vote WHERE date_trunc('day', date(?)) = date_trunc('day', vote_date)");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countVotes = DbHelper.getKey(pst);
            if (countVotes == null) countVotes = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vote WHERE date_trunc('day', date(?)) = date_trunc('day', vote_date) AND decision = 0");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countVotesAdd = DbHelper.getKey(pst);
            if (countVotesAdd == null) countVotesAdd = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vote WHERE date_trunc('day', date(?)) = date_trunc('day', vote_date) AND decision = 4");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countVotesIgnore = DbHelper.getKey(pst);
            if (countVotesIgnore == null) countVotesIgnore = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT e_people_id) FROM vuln.e_vote WHERE date_trunc('day', date(?)) = date_trunc('day', vote_date)");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countVoter = DbHelper.getKey(pst);
            if (countVoter == null) countVoter = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability ev INNER JOIN vuln.e_vulnerability_vrt_items vvi ON vvi.e_vulnerability_id = ev.e_vulnerability_id INNER JOIN vuln.e_vulnerability_vrt vrt ON vrt.e_vulnerability_vrt_id = vvi.e_vulnerability_vrt_id WHERE date_trunc('day', ev.triage_date) <= date_trunc('day',date(?)) AND (vrt.start_date IS NULL OR date_trunc('day', vrt.start_date) > date_trunc('day',date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer countVulnsInNextVRT = DbHelper.getKey(pst);
            if (countVulnsInNextVRT == null) countVulnsInNextVRT = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability_history h WHERE status IN (1,2,3) AND change_date BETWEEN (SELECT vrt.creation_date FROM vuln.e_vulnerability_vrt vrt WHERE date_trunc('day',vrt.creation_date) <= date_trunc('day',date(?)) AND (vrt.start_date IS NULL OR  date_trunc('day',vrt.start_date) > date_trunc('day',date(?)))) AND ? AND e_vulnerability_id IN (SELECT vvi.e_vulnerability_id FROM vuln.e_vulnerability_vrt_items vvi INNER JOIN vuln.e_vulnerability_vrt vrt ON vrt.e_vulnerability_vrt_id = vvi.e_vulnerability_vrt_id WHERE date_trunc('day',vrt.creation_date) <= date_trunc('day',date(?)) AND (vrt.start_date IS NULL OR date_trunc('day',vrt.start_date) > date_trunc('day',date(?))))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            pst.setTimestamp(4, DateHelper.sql(effective_date));
            pst.setTimestamp(5, DateHelper.sql(effective_date));
            Integer countVulnsInNextVRTWithStatus = DbHelper.getKey(pst);
            if (countVulnsInNextVRTWithStatus == null) countVulnsInNextVRTWithStatus = Integer.valueOf(0);
            pst = db.prepareStatement(st_ins_triage_daily);
            pst.setDate(1, DateHelper.sqld(effective_date));
            pst.setInt(2, countTriaged.intValue());
            pst.setInt(3, countIgnored.intValue());
            pst.setInt(4, countAdd.intValue());
            pst.setInt(5, countVotes.intValue());
            pst.setInt(6, countVotesAdd.intValue());
            pst.setInt(7, countVotesIgnore.intValue());
            pst.setInt(8, countVoter.intValue());
            pst.setInt(9, countVulnsInNextVRT.intValue());
            pst.setInt(10, countVulnsInNextVRTWithStatus.intValue());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new Exception("Error when trying to add a triage daily metric");
            _logger.info("Triage Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    private static final String st_ins_triage_daily = "INSERT INTO vuln.e_triage_daily (calc_day, count_triaged, count_ignored, count_added, count_votes, count_vote_add, count_vote_ignore, count_voter, count_vuln_next_vrt, count_vulns_next_vrt_with_analyze) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    protected void computeVulnsDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing Vuln Daily metrics");
            PreparedStatement pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability WHERE date_trunc('day', date(?)) = date_trunc('day', receive_date)");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countReceived = DbHelper.getKey(pst);
            if (countReceived == null) countReceived = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT evr.e_vulnerability_id) FROM vuln.e_vulnerability_reports evr LEFT JOIN vuln.i_vuln iv ON evr.e_vuln_id = iv.e_vuln_id WHERE date_trunc('day', date(?)) = date_trunc('day', iv.last_modified)");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countUpdated = DbHelper.getKey(pst);
            if (countUpdated == null) countUpdated = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability WHERE date_trunc('day', date(?)) = date_trunc('day', receive_date) AND ((is_new_published AND NOT ignored) OR e_vulnerability_id IN (SELECT e_vulnerability_id FROM vuln.e_vulnerability_history))");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countReceivedNew = DbHelper.getKey(pst);
            if (countReceivedNew == null) countReceivedNew = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability WHERE date_trunc('day', receive_date) <= date_trunc('day', date(?)) AND ((triage_date IS NULL AND is_new_published) OR date_trunc('day', triage_date) >  date_trunc('day', date(?)))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer countNew = DbHelper.getKey(pst);
            if (countNew == null) countNew = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability WHERE ( is_new_published OR e_vulnerability_id IN ( SELECT e_vulnerability_id FROM vuln.e_vulnerability_history) AND date_trunc('day', triage_date) <= date_trunc('day', date(?))) AND date_trunc('day', receive_date) <= date_trunc('day', date(?))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            Integer countNotHidden = DbHelper.getKey(pst);
            if (countNotHidden == null) countNotHidden = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(DISTINCT ev.e_vulnerability_id) FROM vuln.e_vulnerability ev INNER JOIN vuln.e_vulnerability_vpv vpv ON vpv.e_vulnerability_id = ev.e_vulnerability_id INNER JOIN (SELECT vh.e_vendor_id, vh.active, vh.esis_hide FROM asset.e_vendor_history vh INNER JOIN asset.e_vendor v ON v.e_vendor_id = vh.e_vendor_id WHERE date_trunc('day', vh.change_date) <= date_trunc('day', date(?))  AND vh.change_date = ( SELECT MAX(change_date) FROM asset.e_vendor_history evh WHERE evh.e_vendor_id = vh.e_vendor_id AND date_trunc('day', evh.change_date) <= date_trunc('day', date(?)) ) UNION SELECT e_vendor_id, active, esis_hide FROM asset.e_vendor WHERE e_vendor_id NOT IN (SELECT e_vendor_id FROM asset.e_vendor_history)) AS ven ON ven.e_vendor_id = vpv.e_vendor_id INNER JOIN (SELECT ph.e_product_id, ph.active, ph.esis_hide FROM asset.e_product_history ph INNER JOIN asset.e_product p ON p.e_product_id = ph.e_product_id WHERE date_trunc('day', ph.change_date) <= date_trunc('day', date(?)) AND ph.change_date = ( SELECT MAX(change_date) FROM asset.e_product_history eph WHERE eph.e_product_id = ph.e_product_id AND date_trunc('day', eph.change_date) <= date_trunc('day', date(?)) ) UNION SELECT e_product_id, active, esis_hide FROM asset.e_product WHERE e_product_id NOT IN (SELECT e_product_id FROM asset.e_product_history)) AS prod ON prod.e_product_id = vpv.e_product_id WHERE date_trunc('day', date(?)) = date_trunc('day', receive_date) AND (ven.active AND NOT prod.esis_hide) AND (ev.is_new_published OR ev.e_vulnerability_id IN (SELECT e_vulnerability_id FROM vuln.e_vulnerability_vrt_items))");
            pst.setTimestamp(1, DateHelper.sql(effective_date));
            pst.setTimestamp(2, DateHelper.sql(effective_date));
            pst.setTimestamp(3, DateHelper.sql(effective_date));
            pst.setTimestamp(4, DateHelper.sql(effective_date));
            pst.setTimestamp(5, DateHelper.sql(effective_date));
            Integer countInTheProcess = DbHelper.getKey(pst);
            if (countInTheProcess == null) countInTheProcess = Integer.valueOf(0);
            pst = db.prepareStatement("SELECT COUNT(*) FROM vuln.e_vulnerability WHERE date_trunc('day', closed_date) = date_trunc('day', date(?))");
            pst.setDate(1, DateHelper.sqld(effective_date));
            Integer countClosed = DbHelper.getKey(pst);
            pst = db.prepareStatement(st_ins_vuln_daily);
            pst.setDate(1, DateHelper.sqld(effective_date));
            pst.setInt(2, countReceived.intValue());
            pst.setInt(3, countUpdated.intValue());
            pst.setInt(4, countReceivedNew.intValue());
            pst.setInt(5, countNew.intValue());
            pst.setInt(6, countNotHidden.intValue());
            pst.setInt(7, countClosed.intValue());
            pst.setInt(8, countInTheProcess.intValue());
            int res = db.executeUpdate(pst);
            if (res != 1) throw new Exception("Error when trying to add a vuln daily metric");
            _logger.info("Vuln Daily metrics OK");
        } finally {
            db.exit();
        }
    }

    private static final String st_ins_vuln_daily = "INSERT INTO vuln.e_vulns_daily (calc_day, count_received, count_updated, count_received_new, count_new, count_all_unhidden, count_closed, count_received_process) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * remove old data if any, and prefill the status/decision/priority/severity daily tables 
     **/
    protected void cleanTables(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Cleaning old computed metrics");
            PreparedStatement pst = db.prepareStatement("DELETE FROM vuln.e_actions_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            pst = db.prepareStatement("DELETE FROM vuln.e_decision_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            PreparedStatement pstId = db.prepareStatement("select e_status_id from vuln.e_vulnerability_status");
            pst = db.prepareStatement("INSERT INTO vuln.e_decision_daily (calc_day, decision) VALUES (?, ?)");
            ResultSet rs = db.executeQuery(pstId);
            if (rs.next()) {
                do {
                    pst.setDate(1, DateHelper.sqld(effective_date));
                    pst.setInt(2, rs.getInt(1));
                    db.executeUpdate(pst);
                } while (rs.next());
            } else {
                throw new Exception("No row found in e_vulnerability_status");
            }
            pst = db.prepareStatement("DELETE FROM vuln.e_groups_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            pst = db.prepareStatement("DELETE FROM vuln.e_priority_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            pstId = db.prepareStatement("select e_priority_id from vuln.e_vulnerability_priority");
            pst = db.prepareStatement("INSERT INTO vuln.e_priority_daily (calc_day, priority) VALUES (?, ?)");
            rs = db.executeQuery(pstId);
            if (rs.next()) {
                do {
                    pst.setDate(1, DateHelper.sqld(effective_date));
                    pst.setInt(2, rs.getInt(1));
                    db.executeUpdate(pst);
                } while (rs.next());
            } else {
                throw new Exception("No row found in e_vulnerability_priority");
            }
            pst = db.prepareStatement("DELETE FROM vuln.e_product_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            pst = db.prepareStatement("DELETE FROM vuln.e_severity_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            pstId = db.prepareStatement("select e_severity_id from vuln.e_vulnerability_severity");
            pst = db.prepareStatement("INSERT INTO vuln.e_severity_daily (calc_day, severity) VALUES (?, ?)");
            rs = db.executeQuery(pstId);
            if (rs.next()) {
                do {
                    pst.setDate(1, DateHelper.sqld(effective_date));
                    pst.setInt(2, rs.getInt(1));
                    db.executeUpdate(pst);
                } while (rs.next());
            } else {
                throw new Exception("No row found in e_vulnerability_severity");
            }
            pst = db.prepareStatement("DELETE FROM vuln.e_status_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            pstId = db.prepareStatement("select e_mav_status_id from vuln.e_vulnerability_mav_status");
            pst = db.prepareStatement("INSERT INTO vuln.e_status_daily (calc_day, status) VALUES (?, ?)");
            rs = db.executeQuery(pstId);
            if (rs.next()) {
                do {
                    pst.setDate(1, DateHelper.sqld(effective_date));
                    pst.setInt(2, rs.getInt(1));
                    db.executeUpdate(pst);
                } while (rs.next());
            } else {
                throw new Exception("No row found in e_vulnerability_severity");
            }
            pst = db.prepareStatement("DELETE FROM vuln.e_triage_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            pst = db.prepareStatement("DELETE FROM vuln.e_users_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            pst = db.prepareStatement("DELETE FROM vuln.e_vendor_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            pst = db.prepareStatement("DELETE FROM vuln.e_version_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            pst = db.prepareStatement("DELETE FROM vuln.e_vulns_daily WHERE calc_day = ?");
            pst.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pst);
            _logger.info("Cleaning OK");
        } finally {
            db.exit();
        }
    }
}
