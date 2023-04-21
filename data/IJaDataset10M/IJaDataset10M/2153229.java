package com.entelience.report.patch.compliancy;

import com.entelience.esis.Activatable;
import com.entelience.sql.Db;
import com.entelience.report.Report;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class analyticTimeToThreat extends Report {

    public analyticTimeToThreat(Db workDb, Db statusDb) throws Exception {
        super(workDb, statusDb);
    }

    /**
     * nessus?
     *
     **/
    public List<Activatable> getActivatableElementsForReport(Db db) throws Exception {
        List<Activatable> l = new ArrayList<Activatable>();
        l.add(new com.entelience.probe.patch.NessusScan());
        return l;
    }

    protected final boolean runReport(Db db) throws Exception {
        try {
            db.enter();
            PreparedStatement st_ins_vuln = db.prepareStatement("INSERT INTO er_patch_com_att_v ( e_report_id, name, e_vulnerability_id, date_published, date_reported, value ) SELECT ?, *, extract(epoch from age(r,p)) from ( select k.host, evr.e_vulnerability_id, iv.publish_date as p, min(t.t) as r from vuln.e_vulnerability_reports evr INNER JOIN vuln.e_vulnerability ev ON ev.e_vulnerability_id = evr.e_vulnerability_id INNER JOIN vuln.i_vuln iv ON ev.e_vuln_id_primary = iv.e_vuln_id INNER JOIN vuln.t_vuln_nessus v ON evr.e_vuln_id = v.e_vuln_id INNER JOIN vuln.t_scan_nessus_results r ON r.hash_key = v.hash_key INNER JOIN vuln.t_scan_nessus_keys k ON k.t_scan_nessus_keys_id = r.t_scan_nessus_keys_id INNER JOIN vuln.t_scan_nessus_keys k2 ON (k2.host = k.host AND k2.t_scan_id = k.t_scan_id) INNER JOIN vuln.t_scan_nessus_timestamps t ON k2.t_scan_nessus_keys_id = t.t_scan_nessus_keys_id INNER JOIN net.t_ip i ON host(i.ip) = k.host INNER JOIN asset.e_asset_ip ai ON ai.t_ip_id = i.t_ip_id INNER JOIN asset.e_asset a ON a.e_asset_id = ai.e_asset_id WHERE k2.info = 'host_start' GROUP BY 1, 2, 3 ) as foo WHERE foo.p != foo.r;");
            PreparedStatement st_upd_s = db.prepareStatement("INSERT INTO er_patch_com_att_s (e_report_id, name, value) SELECT ?, name, avg(value) from er_patch_com_att_v where e_report_id = ? group by 1, 2;");
            PreparedStatement st_upd_g = db.prepareStatement("INSERT INTO er_patch_com_att_g (e_report_id, name, value) SELECT ?, 'global', avg(value) from er_patch_com_att_s where e_report_id = ? group by 1;");
            st_ins_vuln.setObject(1, report_id);
            int n1 = db.executeUpdate(st_ins_vuln);
            _logger.debug(n1 + " rows inserted for threat report ");
            st_upd_s.setObject(1, report_id);
            st_upd_s.setObject(2, report_id);
            int n2 = db.executeUpdate(st_upd_s);
            _logger.debug(n2 + " rows inserted for threat report ");
            st_upd_g.setObject(1, report_id);
            st_upd_g.setObject(2, report_id);
            int n3 = db.executeUpdate(st_upd_g);
            _logger.debug(n3 + " rows inserted for threat report ");
            return true;
        } finally {
            db.exit();
        }
    }
}
