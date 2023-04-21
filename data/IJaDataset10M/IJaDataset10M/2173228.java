package com.entelience.provider.portal;

import org.apache.log4j.Logger;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;
import com.entelience.util.DateHelper;
import com.entelience.util.Logs;
import com.entelience.objects.portal.TimeSeries;
import com.entelience.objects.portal.Top;
import com.entelience.objects.portal.TopLine;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DbHttpSecurity
 * infos on http security
 */
public class DbHttpSecurity {

    private DbHttpSecurity() {
    }

    protected static final Logger _logger = Logs.getLogger();

    private static final Map<String, String> topVulnerabilitiesSet = new HashMap<String, String>();

    protected static Map<String, String> getTopVulnerabilitiesSet() {
        synchronized (topVulnerabilitiesSet) {
            if (topVulnerabilitiesSet.isEmpty()) {
                topVulnerabilitiesSet.put("all", "count_all");
                topVulnerabilitiesSet.put("denied", "count_denied");
                topVulnerabilitiesSet.put("granted", "count_granted");
                topVulnerabilitiesSet.put("warn", "count_warn");
                topVulnerabilitiesSet.put("denied_partial", "count_denied_partial");
                topVulnerabilitiesSet.put("granted_partial", "count_granted_partial");
                topVulnerabilitiesSet.put("warn_partial", "count_warn_partial");
            }
            return topVulnerabilitiesSet;
        }
    }

    private static final Map<String, String> topSecurityRulesSet = new HashMap<String, String>();

    protected static Map<String, String> getTopSecurityRulesSet() {
        synchronized (topSecurityRulesSet) {
            if (topSecurityRulesSet.isEmpty()) {
                topSecurityRulesSet.put("all", "count_all");
                topSecurityRulesSet.put("denied", "count_denied");
                topSecurityRulesSet.put("granted", "count_granted");
                topSecurityRulesSet.put("warn", "count_warn");
                topSecurityRulesSet.put("denied_partial", "count_denied_partial");
                topSecurityRulesSet.put("granted_partial", "count_granted_partial");
                topSecurityRulesSet.put("warn_partial", "count_warn_partial");
            }
            return topSecurityRulesSet;
        }
    }

    private static final Map<String, String> numberOfSecurityRuleActionEvolutionSet = new HashMap<String, String>();

    protected static Map<String, String> getNumberOfSecurityRuleActionEvolutionSet() {
        synchronized (numberOfSecurityRuleActionEvolutionSet) {
            if (numberOfSecurityRuleActionEvolutionSet.isEmpty()) {
                numberOfSecurityRuleActionEvolutionSet.put("denied", "count_denied");
                numberOfSecurityRuleActionEvolutionSet.put("granted", "count_granted");
                numberOfSecurityRuleActionEvolutionSet.put("warn", "count_warn");
                numberOfSecurityRuleActionEvolutionSet.put("all", "count_warn+count_granted+count_denied");
                numberOfSecurityRuleActionEvolutionSet.put("rules_denied", "count_rules_denied");
                numberOfSecurityRuleActionEvolutionSet.put("rules_granted", "count_rules_granted");
                numberOfSecurityRuleActionEvolutionSet.put("rules_warn", "count_rules_warn");
                numberOfSecurityRuleActionEvolutionSet.put("rules_denied_partial", "count_rules_denied_partial");
                numberOfSecurityRuleActionEvolutionSet.put("rules_granted_partial", "count_rules_granted_partial");
                numberOfSecurityRuleActionEvolutionSet.put("rules_warn_partial", "count_rules_warn_partial");
                numberOfSecurityRuleActionEvolutionSet.put("ips_denied", "count_denied_ips");
                numberOfSecurityRuleActionEvolutionSet.put("ips_granted", "count_granted_ips");
                numberOfSecurityRuleActionEvolutionSet.put("ips_warn", "count_warn_ips");
            }
            return numberOfSecurityRuleActionEvolutionSet;
        }
    }

    private static final Map<String, String> numberOfSecurityRuleEventsEvolutionSet = new HashMap<String, String>();

    protected static Map<String, String> getNumberOfSecurityRuleEventsEvolutionSet() {
        synchronized (numberOfSecurityRuleEventsEvolutionSet) {
            if (numberOfSecurityRuleEventsEvolutionSet.isEmpty()) {
                numberOfSecurityRuleEventsEvolutionSet.put("all", "count_all");
                numberOfSecurityRuleEventsEvolutionSet.put("denied", "count_denied");
                numberOfSecurityRuleEventsEvolutionSet.put("granted", "count_granted");
                numberOfSecurityRuleEventsEvolutionSet.put("warn", "count_warn");
                numberOfSecurityRuleEventsEvolutionSet.put("denied_partial", "count_denied_partial");
                numberOfSecurityRuleEventsEvolutionSet.put("granted_partial", "count_granted_partial");
                numberOfSecurityRuleEventsEvolutionSet.put("warn_partial", "count_warn_partial");
                numberOfSecurityRuleEventsEvolutionSet.put("ips_denied", "count_denied_ips");
                numberOfSecurityRuleEventsEvolutionSet.put("ips_granted", "count_granted_ips");
                numberOfSecurityRuleEventsEvolutionSet.put("ips_warn", "count_warn_ips");
            }
            return numberOfSecurityRuleEventsEvolutionSet;
        }
    }

    private static final Map<String, String> topSecurityCategories = new HashMap<String, String>();

    protected static Map<String, String> getTopSecurityCategoriesSet() {
        synchronized (topSecurityCategories) {
            if (topSecurityCategories.isEmpty()) {
                topSecurityCategories.put("denied", "count_req_denied");
                topSecurityCategories.put("granted", "count_req_granted");
                topSecurityCategories.put("warn", "count_req_warn");
            }
            return topSecurityCategories;
        }
    }

    private static final Map<String, String> topAccessedAssetsSet = new HashMap<String, String>();

    protected static Map<String, String> getTopAccessedAssetsSet() {
        synchronized (topAccessedAssetsSet) {
            if (topAccessedAssetsSet.isEmpty()) {
                topAccessedAssetsSet.put("count", "SUM(count_all)");
                topAccessedAssetsSet.put("security_status", "SUM(count_security_status)");
                topAccessedAssetsSet.put("volume", "SUM(volume)");
                topAccessedAssetsSet.put("ips", "MAX(count_ips)");
                topAccessedAssetsSet.put("users", "MAX(count_users)");
                topAccessedAssetsSet.put("response_time", "SUM(average_response_time*count_all)/SUM(count_all)");
            }
            return topAccessedAssetsSet;
        }
    }

    private static final Map<String, String> topStatusSet = new HashMap<String, String>();

    protected static Map<String, String> getTopStatusSet() {
        synchronized (topStatusSet) {
            if (topStatusSet.isEmpty()) {
                topStatusSet.put("count", "SUM(count_all)");
                topStatusSet.put("volume", "SUM(volume)");
                topStatusSet.put("ips", "MAX(count_ips)");
                topStatusSet.put("users", "MAX(count_users)");
                topStatusSet.put("response_time", "SUM(average_response_time*count_all)/SUM(count_all)");
            }
            return topStatusSet;
        }
    }

    /**
	 * getTopVulnerabilities
	 */
    public static Top getTopVulnerabilities(Db db, String metric, String siteRestriction, String categoryRestriction, boolean withDescription, Calendar baseDate, int period, int nbVulns, String displayType) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopVulnerabilitiesSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getTopVulnerabilities; invalid counting value: " + metric);
            StringBuffer sql = new StringBuffer("SELECT SUM(");
            StringBuffer sqlTotal = new StringBuffer("SELECT SUM(");
            sql.append(resolvedMetric);
            sqlTotal.append(resolvedMetric);
            if (withDescription) {
                sql.append("), COALESCE(rv.vuln_name || '( ' || iv.title || ')', rv.vuln_name) ");
            } else {
                sql.append("), rv.vuln_name ");
            }
            sqlTotal.append(") ");
            if (siteRestriction == null) {
                sql.append(" FROM http.t_security_rule_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id LEFT JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = r.t_security_rule_category_id INNER JOIN http.t_security_rule_to_vulnerability rv ON rv.t_security_rule_id = d.t_security_rule_id LEFT JOIN vuln.e_vulnerability ev ON ev.e_vulnerability_id = rv.e_vuln_id LEFT JOIN vuln.i_vuln iv ON iv.e_vuln_id = ev.e_vuln_id_primary WHERE NOT r.obsolete ");
                sqlTotal.append(" FROM http.t_security_rule_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id LEFT JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = r.t_security_rule_category_id INNER JOIN http.t_security_rule_to_vulnerability rv ON rv.t_security_rule_id = d.t_security_rule_id LEFT JOIN vuln.e_vulnerability ev ON ev.e_vulnerability_id = rv.e_vuln_id LEFT JOIN vuln.i_vuln iv ON iv.e_vuln_id = ev.e_vuln_id_primary WHERE NOT r.obsolete ");
            } else {
                sql.append(" FROM http.t_security_rule_site_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id INNER JOIN http.t_domain dom ON dom.t_domain_id = s.t_domain_id LEFT JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = r.t_security_rule_category_id INNER JOIN http.t_security_rule_to_vulnerability rv ON rv.t_security_rule_id = d.t_security_rule_id LEFT JOIN vuln.e_vulnerability ev ON ev.e_vulnerability_id = rv.e_vuln_id LEFT JOIN vuln.i_vuln iv ON iv.e_vuln_id = ev.e_vuln_id_primary WHERE NOT r.obsolete AND lower(CASE WHEN s.site_name IS NULL OR s.site_name = '' THEN dom.domain_name ELSE s.site_name || '.' || dom.domain_name END) = lower(?) ");
                sqlTotal.append(" FROM http.t_security_rule_site_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id INNER JOIN http.t_domain dom ON dom.t_domain_id = s.t_domain_id LEFT JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = r.t_security_rule_category_id INNER JOIN http.t_security_rule_to_vulnerability rv ON rv.t_security_rule_id = d.t_security_rule_id LEFT JOIN vuln.e_vulnerability ev ON ev.e_vulnerability_id = rv.e_vuln_id LEFT JOIN vuln.i_vuln iv ON iv.e_vuln_id = ev.e_vuln_id_primary WHERE NOT r.obsolete AND lower(CASE WHEN s.site_name IS NULL OR s.site_name = '' THEN dom.domain_name ELSE s.site_name || '.' || dom.domain_name END) = lower(?) ");
            }
            if (categoryRestriction != null) {
                sql.append(" AND lower(c.category_name) = lower(?) ");
                sqlTotal.append(" AND lower(c.category_name) = lower(?) ");
            }
            sql.append(" AND calc_day <= ? AND calc_day > ? GROUP BY 2 HAVING SUM(").append(resolvedMetric).append(") > 0 ORDER BY 1 DESC LIMIT ?");
            sqlTotal.append(" AND calc_day <= ? AND calc_day > ? ");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            Calendar now = (Calendar) baseDate.clone();
            Calendar now_1 = (Calendar) now.clone();
            Calendar now_2 = (Calendar) now.clone();
            now_1.add(Calendar.DAY_OF_YEAR, -period);
            now_2.add(Calendar.DAY_OF_YEAR, -(2 * period));
            int n = 1;
            if (siteRestriction != null) {
                pst.setString(n++, siteRestriction);
            }
            if (categoryRestriction != null) {
                pst.setString(n++, categoryRestriction);
            }
            pst.setDate(n++, DateHelper.sqld(now.getTime()));
            pst.setDate(n++, DateHelper.sqld(now_1.getTime()));
            pst.setInt(n++, nbVulns);
            ResultSet rs = db.executeQuery(pst);
            List<TopLine> al = PortalDataProvider.toTopLine(rs);
            Top t = new Top(now_1.getTime(), now.getTime());
            t.setElements(al);
            if (t.getElements() == null || t.getElements().size() == 0) {
                t.setTotal(0);
                t.computeSubtotalFromElements();
                return t;
            }
            PreparedStatement pstCount = db.prepareStatement(sqlTotal.toString());
            n = 1;
            if (siteRestriction != null) {
                pstCount.setString(n++, siteRestriction);
            }
            if (categoryRestriction != null) {
                pstCount.setString(n++, categoryRestriction);
            }
            pstCount.setDate(n++, DateHelper.sqld(now.getTime()));
            pstCount.setDate(n++, DateHelper.sqld(now_1.getTime()));
            t.setTotal(DbHelper.getLong(pstCount));
            if ("top".equals(displayType)) {
                n = 1;
                if (siteRestriction != null) {
                    pst.setString(n++, siteRestriction);
                }
                if (categoryRestriction != null) {
                    pst.setString(n++, categoryRestriction);
                }
                pst.setDate(n++, DateHelper.sqld(now.getTime()));
                pst.setDate(n++, DateHelper.sqld(now_1.getTime()));
                pst.setInt(n++, nbVulns);
                rs = db.executeQuery(pst);
                List<TopLine> old = PortalDataProvider.toTopLine(rs);
                t.addRankEvolution(old);
            }
            t.computeSubtotalFromElements();
            t.computePercents();
            return t;
        } finally {
            db.exit();
        }
    }

    /**
	 * getVulnEvolution
	 */
    public static TimeSeries getVulnEvolution(Db db, String metric, String vulnName, String timeScale, Integer nbDays) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopVulnerabilitiesSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getVulnEvolution; invalid counting value: " + metric);
            StringBuffer sql = new StringBuffer("SELECT SUM(");
            sql.append(resolvedMetric);
            sql.append("), date_trunc(?, d.calc_day) ");
            sql.append(" FROM http.t_security_rule_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id LEFT JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = r.t_security_rule_category_id INNER JOIN http.t_security_rule_to_vulnerability rv ON rv.t_security_rule_id = d.t_security_rule_id LEFT JOIN vuln.e_vulnerability ev ON ev.e_vulnerability_id = rv.e_vuln_id LEFT JOIN vuln.i_vuln iv ON iv.e_vuln_id = ev.e_vuln_id_primary WHERE NOT r.obsolete AND lower(rv.vuln_name) = lower(?) ");
            if (nbDays != null) sql.append(" AND date_trunc(?, d.calc_day) > (current_date - interval '" + nbDays + " days') ");
            sql.append(" GROUP BY 2 ORDER BY 2");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            int n = 1;
            pst.setString(n++, timeScale);
            pst.setString(n++, vulnName);
            if (nbDays != null) pst.setString(n++, timeScale);
            ResultSet rs = db.executeQuery(pst);
            Date max = DateHelper.getFirstDayOfCurrentTimeUnit(timeScale);
            return PortalDataProvider.toTimeSeries(db, rs, max, timeScale);
        } finally {
            db.exit();
        }
    }

    /**
	 * getTopSecurityRules
	 */
    public static Top getTopSecurityRules(Db db, String metric, String siteRestriction, String categoryRestriction, boolean withDescription, Calendar baseDate, int period, int nbRules, String displayType) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopSecurityRulesSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getTopSecurityRules; invalid counting value: " + metric);
            StringBuffer sql = new StringBuffer("SELECT SUM(");
            StringBuffer sqlTotal = new StringBuffer("SELECT SUM(");
            sql.append(resolvedMetric);
            sqlTotal.append(resolvedMetric);
            if (withDescription) {
                sql.append("), COALESCE(r.rule_def  || ' (' || r.description || ')', r.rule_def) ");
            } else {
                sql.append("), r.rule_def ");
            }
            sqlTotal.append(") ");
            if (siteRestriction == null) {
                sql.append(" FROM http.t_security_rule_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id LEFT JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = r.t_security_rule_category_id WHERE NOT r.obsolete ");
                sqlTotal.append(" FROM http.t_security_rule_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id LEFT JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = r.t_security_rule_category_id WHERE NOT r.obsolete ");
            } else {
                sql.append(" FROM http.t_security_rule_site_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id INNER JOIN http.t_domain dom ON dom.t_domain_id = s.t_domain_id LEFT JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = r.t_security_rule_category_id WHERE NOT r.obsolete AND lower(CASE WHEN s.site_name IS NULL OR s.site_name = '' THEN dom.domain_name ELSE s.site_name || '.' || dom.domain_name END) = lower(?) ");
                sqlTotal.append(" FROM http.t_security_rule_site_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id INNER JOIN http.t_domain dom ON dom.t_domain_id = s.t_domain_id LEFT JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = r.t_security_rule_category_id WHERE NOT r.obsolete AND lower(CASE WHEN s.site_name IS NULL OR s.site_name = '' THEN dom.domain_name ELSE s.site_name || '.' || dom.domain_name END) = lower(?) ");
            }
            if (categoryRestriction != null) {
                sql.append(" AND lower(c.category_name) = lower(?) ");
                sqlTotal.append(" AND lower(c.category_name) = lower(?) ");
            }
            sql.append(" AND calc_day <= ? AND calc_day > ? GROUP BY 2 HAVING SUM(").append(resolvedMetric).append(") > 0 ORDER BY 1 DESC LIMIT ?");
            sqlTotal.append(" AND calc_day <= ? AND calc_day > ? ");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            Calendar now = (Calendar) baseDate.clone();
            Calendar now_1 = (Calendar) now.clone();
            Calendar now_2 = (Calendar) now.clone();
            now_1.add(Calendar.DAY_OF_YEAR, -period);
            now_2.add(Calendar.DAY_OF_YEAR, -(2 * period));
            int n = 1;
            if (siteRestriction != null) {
                pst.setString(n++, siteRestriction);
            }
            if (categoryRestriction != null) {
                pst.setString(n++, categoryRestriction);
            }
            pst.setDate(n++, DateHelper.sqld(now.getTime()));
            pst.setDate(n++, DateHelper.sqld(now_1.getTime()));
            pst.setInt(n++, nbRules);
            ResultSet rs = db.executeQuery(pst);
            List<TopLine> al = PortalDataProvider.toTopLine(rs);
            Top t = new Top(now_1.getTime(), now.getTime());
            t.setElements(al);
            if (t.getElements() == null || t.getElements().size() == 0) {
                t.setTotal(0);
                t.computeSubtotalFromElements();
                return t;
            }
            PreparedStatement pstCount = db.prepareStatement(sqlTotal.toString());
            n = 1;
            if (siteRestriction != null) {
                pstCount.setString(n++, siteRestriction);
            }
            if (categoryRestriction != null) {
                pstCount.setString(n++, categoryRestriction);
            }
            pstCount.setDate(n++, DateHelper.sqld(now.getTime()));
            pstCount.setDate(n++, DateHelper.sqld(now_1.getTime()));
            t.setTotal(DbHelper.getLong(pstCount));
            if ("top".equals(displayType)) {
                n = 1;
                if (siteRestriction != null) {
                    pst.setString(n++, siteRestriction);
                }
                if (categoryRestriction != null) {
                    pst.setString(n++, categoryRestriction);
                }
                pst.setDate(n++, DateHelper.sqld(now.getTime()));
                pst.setDate(n++, DateHelper.sqld(now_1.getTime()));
                pst.setInt(n++, nbRules);
                rs = db.executeQuery(pst);
                List<TopLine> old = PortalDataProvider.toTopLine(rs);
                t.addRankEvolution(old);
            }
            t.computeSubtotalFromElements();
            t.computePercents();
            return t;
        } finally {
            db.exit();
        }
    }

    /**
	 * getNumberOfSecurityRuleActionEvolution
	 */
    public static TimeSeries getNumberOfSecurityRuleActionEvolution(Db db, String metric, String siteRestriction, String timeScale, Integer nbDays) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getNumberOfSecurityRuleActionEvolutionSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getNumberOfSecurityRuleActionEvolution; invalid counting value: " + metric);
            boolean useAggregate = false;
            if ("denied".equalsIgnoreCase(metric) || "granted".equalsIgnoreCase(metric) || "warn".equalsIgnoreCase(metric) || "all".equalsIgnoreCase(metric)) {
                useAggregate = true;
            }
            StringBuffer from = new StringBuffer(" FROM ");
            if (siteRestriction == null) {
                from.append("http.t_security_daily d WHERE true ");
            } else {
                from.append("http.t_security_site_daily d INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id INNER JOIN http.t_domain dom ON dom.t_domain_id = s.t_domain_id WHERE true ");
            }
            StringBuffer sql = new StringBuffer("SELECT ");
            if (useAggregate) sql.append("SUM("); else sql.append("MAX(");
            sql.append(resolvedMetric).append("), date_trunc(?, d.calc_day) as new_date ");
            sql.append(from);
            if (nbDays != null) {
                sql.append(" AND date_trunc(?, d.calc_day) > (current_date - interval '" + nbDays + " days') ");
            }
            if (siteRestriction != null) {
                sql.append(" AND lower(CASE WHEN s.site_name IS NULL OR s.site_name = '' THEN dom.domain_name ELSE s.site_name || '.' || dom.domain_name END) = lower(?) ");
            }
            sql.append(" GROUP BY 2 ORDER BY 2");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            int n = 1;
            pst.setString(n++, timeScale);
            if (nbDays != null) pst.setString(n++, timeScale);
            if (siteRestriction != null) {
                pst.setString(n++, siteRestriction);
            }
            ResultSet rs = db.executeQuery(pst);
            Date max = DateHelper.getFirstDayOfCurrentTimeUnit(timeScale);
            return PortalDataProvider.toTimeSeries(db, rs, max, timeScale);
        } finally {
            db.exit();
        }
    }

    /**
	 * getNumberOfSecurityRuleEventsEvolution
	 */
    public static TimeSeries getNumberOfSecurityRuleEventsEvolution(Db db, String metric, String ruleName, String siteRestriction, String timeScale, Integer nbDays) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getNumberOfSecurityRuleEventsEvolutionSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getNumberOfSecurityRuleEventsEvolution; invalid counting value: " + metric);
            boolean useAggregate = true;
            if ("ips_denied".equalsIgnoreCase(metric) || "ips_granted".equalsIgnoreCase(metric) || "ips_warn".equalsIgnoreCase(metric)) {
                useAggregate = false;
            }
            StringBuffer sql = new StringBuffer("SELECT ");
            if (useAggregate) sql.append("SUM("); else sql.append("MAX(");
            sql.append(resolvedMetric).append("), date_trunc(?, d.calc_day) as new_date ");
            if (siteRestriction == null) {
                sql.append(" FROM http.t_security_rule_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id WHERE NOT r.obsolete AND lower(r.rule_def) = lower(?)");
            } else {
                sql.append(" FROM http.t_security_rule_site_daily d INNER JOIN http.t_security_rule r ON d.t_security_rule_id = r.t_security_rule_id INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id INNER JOIN http.t_domain dom ON dom.t_domain_id = s.t_domain_id WHERE NOT r.obsolete AND lower(s.site_name || '.' || dom.domain_name) = lower(?) AND lower(r.rule_def) = lower(?)");
            }
            if (nbDays != null) {
                sql.append(" AND date_trunc(?, d.calc_day) > (current_date - interval '" + nbDays + " days') ");
            }
            sql.append(" GROUP BY 2 ORDER BY 2");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            int n = 1;
            pst.setString(n++, timeScale);
            if (siteRestriction != null) {
                pst.setString(n++, siteRestriction);
            }
            pst.setString(n++, ruleName);
            if (nbDays != null) pst.setString(n++, timeScale);
            ResultSet rs = db.executeQuery(pst);
            Date max = DateHelper.getFirstDayOfCurrentTimeUnit(timeScale);
            return PortalDataProvider.toTimeSeries(db, rs, max, timeScale);
        } finally {
            db.exit();
        }
    }

    /**
	 * getTopSecurityCategories
	 */
    public static Top getTopSecurityCategories(Db db, String metric, Calendar baseDate, int period, int nbCats, String displayType) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopSecurityCategoriesSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getTopSecurityCategories; invalid counting value: " + metric);
            StringBuffer sql = new StringBuffer("SELECT SUM(");
            StringBuffer sqlTotal = new StringBuffer("SELECT SUM(");
            sql.append(resolvedMetric);
            sqlTotal.append(resolvedMetric);
            sql.append("), c.category_name FROM http.t_security_rule_category_daily d INNER JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = d.t_security_rule_category_id WHERE calc_day <= ? AND calc_day > ? GROUP BY 2 HAVING SUM(").append(resolvedMetric).append(") > 0 ORDER BY 1 DESC LIMIT ?");
            sqlTotal.append(") FROM http.t_security_rule_category_daily d INNER JOIN http.t_security_rule_category c ON c.t_security_rule_category_id = d.t_security_rule_category_id WHERE calc_day <= ? AND calc_day > ?");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            Calendar now = (Calendar) baseDate.clone();
            Calendar now_1 = (Calendar) now.clone();
            Calendar now_2 = (Calendar) now.clone();
            now_1.add(Calendar.DAY_OF_YEAR, -period);
            now_2.add(Calendar.DAY_OF_YEAR, -(2 * period));
            int n = 1;
            pst.setDate(n++, DateHelper.sqld(now.getTime()));
            pst.setDate(n++, DateHelper.sqld(now_1.getTime()));
            pst.setInt(n++, nbCats);
            ResultSet rs = db.executeQuery(pst);
            List<TopLine> al = PortalDataProvider.toTopLine(rs);
            Top t = new Top(now_1.getTime(), now.getTime());
            t.setElements(al);
            if (t.getElements() == null || t.getElements().size() == 0) {
                t.setTotal(0);
                t.computeSubtotalFromElements();
                return t;
            }
            PreparedStatement pstCount = db.prepareStatement(sqlTotal.toString());
            n = 1;
            pstCount.setDate(n++, DateHelper.sqld(now.getTime()));
            pstCount.setDate(n++, DateHelper.sqld(now_1.getTime()));
            t.setTotal(DbHelper.getLong(pstCount));
            if ("top".equals(displayType)) {
                n = 1;
                pst.setDate(n++, DateHelper.sqld(now.getTime()));
                pst.setDate(n++, DateHelper.sqld(now_1.getTime()));
                pst.setInt(n++, nbCats);
                rs = db.executeQuery(pst);
                List<TopLine> old = PortalDataProvider.toTopLine(rs);
                t.addRankEvolution(old);
            }
            t.computeSubtotalFromElements();
            t.computePercents();
            return t;
        } finally {
            db.exit();
        }
    }

    /**
	 * getTopSecurityActions
	 */
    public static Top getTopSecurityActions(Db db, String siteRestriction, Calendar baseDate, int period, int nbActions, String displayType) throws Exception {
        try {
            db.enter();
            StringBuffer sql = new StringBuffer();
            StringBuffer sqlTotal = new StringBuffer();
            if (siteRestriction == null) {
                sql.append("SELECT SUM(count_denied), 'Denied' FROM http.t_security_site_daily WHERE calc_day <= ? AND calc_day > ? ");
                sql.append("UNION ALL SELECT SUM(count_granted), 'Granted' FROM http.t_security_site_daily WHERE calc_day <= ? AND calc_day > ? ");
                sql.append("UNION ALL SELECT SUM(count_warn), 'Warn' FROM http.t_security_site_daily WHERE calc_day <= ? AND calc_day > ? ORDER BY 1 DESC LIMIT ?");
                sqlTotal.append("SELECT SUM(count_denied+count_granted+count_warn) FROM http.t_security_site_daily WHERE calc_day <= ? AND calc_day > ?");
            } else {
                sql.append("SELECT SUM(count_denied), 'Denied' FROM http.t_security_site_daily d ");
                sql.append("INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id ");
                sql.append("INNER JOIN http.t_domain dom ON dom.t_domain_id = s.t_domain_id ");
                sql.append("WHERE calc_day <= ? AND calc_day > ? AND lower(s.site_name || '.' || dom.domain_name) = lower(?) ");
                sql.append("UNION ALL SELECT SUM(count_granted), 'Granted' FROM http.t_security_site_daily d ");
                sql.append("INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id ");
                sql.append("INNER JOIN http.t_domain dom ON dom.t_domain_id = s.t_domain_id ");
                sql.append("WHERE calc_day <= ? AND calc_day > ? AND lower(s.site_name || '.' || dom.domain_name) = lower(?) ");
                sql.append("UNION ALL SELECT SUM(count_warn), 'Warn' FROM http.t_security_site_daily d ");
                sql.append("INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id ");
                sql.append("INNER JOIN http.t_domain dom ON dom.t_domain_id = s.t_domain_id ");
                sql.append("WHERE calc_day <= ? AND calc_day > ? AND lower(CASE WHEN s.site_name IS NULL OR s.site_name = '' ");
                sql.append("THEN dom.domain_name ELSE s.site_name || '.' || dom.domain_name END) = lower(?) ORDER BY 1 DESC LIMIT ?");
                sqlTotal.append("SELECT SUM(count_denied+count_granted+count_warn) FROM http.t_security_site_daily d ");
                sqlTotal.append("INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id ");
                sqlTotal.append("INNER JOIN http.t_domain dom ON dom.t_domain_id = s.t_domain_id ");
                sqlTotal.append("WHERE calc_day <= ? AND calc_day > ? AND lower(CASE WHEN s.site_name IS NULL OR s.site_name = '' ");
                sqlTotal.append("THEN dom.domain_name ELSE s.site_name || '.' || dom.domain_name END) = lower(?)");
            }
            PreparedStatement pst = db.prepareStatement(sql.toString());
            Calendar now = (Calendar) baseDate.clone();
            Calendar now_1 = (Calendar) now.clone();
            Calendar now_2 = (Calendar) now.clone();
            now_1.add(Calendar.DAY_OF_YEAR, -period);
            now_2.add(Calendar.DAY_OF_YEAR, -(2 * period));
            int n = 1;
            for (int i = 0; i < 3; i++) {
                pst.setDate(n++, DateHelper.sqld(now.getTime()));
                pst.setDate(n++, DateHelper.sqld(now_1.getTime()));
                if (siteRestriction != null) {
                    pst.setString(n++, siteRestriction);
                }
            }
            pst.setInt(n++, nbActions);
            ResultSet rs = db.executeQuery(pst);
            List<TopLine> al = PortalDataProvider.toTopLine(rs);
            Top t = new Top(now_1.getTime(), now.getTime());
            t.setElements(al);
            if (t.getElements() == null || t.getElements().size() == 0) {
                t.setTotal(0);
                t.computeSubtotalFromElements();
                return t;
            }
            PreparedStatement pstCount = db.prepareStatement(sqlTotal.toString());
            n = 1;
            pstCount.setDate(n++, DateHelper.sqld(now.getTime()));
            pstCount.setDate(n++, DateHelper.sqld(now_1.getTime()));
            if (siteRestriction != null) {
                pstCount.setString(n++, siteRestriction);
            }
            t.setTotal(DbHelper.getLong(pstCount));
            if ("top".equals(displayType)) {
                n = 1;
                for (int i = 0; i < 3; i++) {
                    pst.setDate(n++, DateHelper.sqld(now.getTime()));
                    pst.setDate(n++, DateHelper.sqld(now_1.getTime()));
                    if (siteRestriction != null) {
                        pst.setString(n++, siteRestriction);
                    }
                }
                pst.setInt(n++, nbActions);
                rs = db.executeQuery(pst);
                List<TopLine> old = PortalDataProvider.toTopLine(rs);
                t.addRankEvolution(old);
            }
            t.computeSubtotalFromElements();
            t.computePercents();
            return t;
        } finally {
            db.exit();
        }
    }

    /**
	 * getTopAccessedAssets
	 */
    public static Top getTopAccessedAssets(Db db, String metric, Calendar baseDate, int period, int nb, String displayType) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopAccessedAssetsSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getTopAccessedAssets; invalid counting value: " + metric);
            boolean computeTotal = false;
            if ("count".equalsIgnoreCase(metric) || "security_status".equalsIgnoreCase(metric) || "volume".equalsIgnoreCase(metric)) {
                computeTotal = true;
            }
            StringBuffer sql = new StringBuffer("SELECT ");
            StringBuffer sqlTotal = new StringBuffer("SELECT ");
            sql.append(resolvedMetric);
            sqlTotal.append(resolvedMetric);
            sql.append(", a.display_name ");
            sql.append(" FROM http.t_security_access_asset_daily d INNER JOIN asset.e_asset a ON a.e_asset_id = d.e_asset_id WHERE calc_day <= ? AND calc_day > ? GROUP BY 2 ORDER BY 1 DESC LIMIT ?");
            sqlTotal.append(" FROM http.t_security_access_asset_daily d INNER JOIN asset.e_asset a ON a.e_asset_id = d.e_asset_id WHERE calc_day <= ? AND calc_day > ? ");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            Calendar now = (Calendar) baseDate.clone();
            Calendar now_1 = (Calendar) now.clone();
            Calendar now_2 = (Calendar) now.clone();
            now_1.add(Calendar.DAY_OF_YEAR, -period);
            now_2.add(Calendar.DAY_OF_YEAR, -(2 * period));
            pst.setDate(1, DateHelper.sqld(now.getTime()));
            pst.setDate(2, DateHelper.sqld(now_1.getTime()));
            pst.setInt(3, nb);
            ResultSet rs = db.executeQuery(pst);
            List<TopLine> al = PortalDataProvider.toTopLineWithUrl(rs, false);
            Top t = new Top(now_1.getTime(), now.getTime());
            t.setElements(al);
            if (t.getElements() == null || t.getElements().size() == 0) {
                t.setTotal(0);
                t.computeSubtotalFromElements();
                return t;
            }
            if (computeTotal) {
                PreparedStatement pstCount = db.prepareStatement(sqlTotal.toString());
                pstCount.setDate(1, DateHelper.sqld(now.getTime()));
                pstCount.setDate(2, DateHelper.sqld(now_1.getTime()));
                t.setTotal(DbHelper.getLong(pstCount));
            }
            if ("top".equals(displayType)) {
                pst.setDate(1, DateHelper.sqld(now.getTime()));
                pst.setDate(2, DateHelper.sqld(now_1.getTime()));
                pst.setInt(3, nb);
                rs = db.executeQuery(pst);
                List<TopLine> old = PortalDataProvider.toTopLine(rs);
                t.addRankEvolution(old);
            }
            if (computeTotal) {
                t.computeSubtotalFromElements();
                t.computePercents();
            }
            return t;
        } finally {
            db.exit();
        }
    }

    /**
	 * getTopAccessedSites
	 */
    public static Top getTopAccessedSites(Db db, String metric, Calendar baseDate, int period, int nb, String displayType) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopAccessedAssetsSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getTopAccessedSites; invalid counting value: " + metric);
            boolean computeTotal = false;
            if ("count".equalsIgnoreCase(metric) || "security_status".equalsIgnoreCase(metric) || "volume".equalsIgnoreCase(metric)) {
                computeTotal = true;
            }
            StringBuffer sql = new StringBuffer("SELECT ");
            StringBuffer sqlTotal = new StringBuffer("SELECT ");
            sql.append(resolvedMetric);
            sqlTotal.append(resolvedMetric);
            sql.append(", CASE WHEN s.site_name IS NULL OR s.site_name = '' THEN s.domain_name ELSE s.site_name || '.' || s.domain_name END ");
            sql.append(" FROM http.t_security_access_site_daily d INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id WHERE calc_day <= ? AND calc_day > ? GROUP BY 2 ORDER BY 1 DESC LIMIT ?");
            sqlTotal.append(" FROM http.t_security_access_site_daily d INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id WHERE calc_day <= ? AND calc_day > ? ");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            Calendar now = (Calendar) baseDate.clone();
            Calendar now_1 = (Calendar) now.clone();
            Calendar now_2 = (Calendar) now.clone();
            now_1.add(Calendar.DAY_OF_YEAR, -period);
            now_2.add(Calendar.DAY_OF_YEAR, -(2 * period));
            pst.setDate(1, DateHelper.sqld(now.getTime()));
            pst.setDate(2, DateHelper.sqld(now_1.getTime()));
            pst.setInt(3, nb);
            ResultSet rs = db.executeQuery(pst);
            List<TopLine> al = PortalDataProvider.toTopLine(rs);
            Top t = new Top(now_1.getTime(), now.getTime());
            t.setElements(al);
            if (t.getElements() == null || t.getElements().size() == 0) {
                t.setTotal(0);
                t.computeSubtotalFromElements();
                return t;
            }
            if (computeTotal) {
                PreparedStatement pstCount = db.prepareStatement(sqlTotal.toString());
                pstCount.setDate(1, DateHelper.sqld(now.getTime()));
                pstCount.setDate(2, DateHelper.sqld(now_1.getTime()));
                t.setTotal(DbHelper.getLong(pstCount));
            }
            if ("top".equals(displayType)) {
                pst.setDate(1, DateHelper.sqld(now.getTime()));
                pst.setDate(2, DateHelper.sqld(now_1.getTime()));
                pst.setInt(3, nb);
                rs = db.executeQuery(pst);
                List<TopLine> old = PortalDataProvider.toTopLine(rs);
                t.addRankEvolution(old);
            }
            if (computeTotal) {
                t.computeSubtotalFromElements();
                t.computePercents();
            }
            return t;
        } finally {
            db.exit();
        }
    }

    /**
	 * getTopAccessedPaths
	 */
    public static Top getTopAccessedPaths(Db db, String metric, Calendar baseDate, int period, int nb, String displayType) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopAccessedAssetsSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getTopAccessedPaths; invalid counting value: " + metric);
            boolean computeTotal = false;
            if ("count".equalsIgnoreCase(metric) || "security_status".equalsIgnoreCase(metric) || "volume".equalsIgnoreCase(metric)) {
                computeTotal = true;
            }
            StringBuffer sql = new StringBuffer("SELECT ");
            StringBuffer sqlTotal = new StringBuffer("SELECT ");
            sql.append(resolvedMetric);
            sqlTotal.append(resolvedMetric);
            sql.append(", CASE WHEN s.site_name IS NULL OR s.site_name = '' THEN s.domain_name ELSE s.site_name || '.' || s.domain_name END || path ");
            sql.append(" FROM http.t_security_access_site_path_daily d INNER JOIN http.t_site_path p ON p.t_site_path_id = d.t_site_path_id INNER JOIN http.t_domain_site s ON s.t_domain_site_id = p.t_domain_site_id WHERE calc_day <= ? AND calc_day > ? GROUP BY 2 ORDER BY 1 DESC LIMIT ?");
            sqlTotal.append(" FROM http.t_security_access_site_path_daily d INNER JOIN http.t_site_path p ON p.t_site_path_id = d.t_site_path_id INNER JOIN http.t_domain_site s ON s.t_domain_site_id = p.t_domain_site_id WHERE calc_day <= ? AND calc_day > ? ");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            Calendar now = (Calendar) baseDate.clone();
            Calendar now_1 = (Calendar) now.clone();
            Calendar now_2 = (Calendar) now.clone();
            now_1.add(Calendar.DAY_OF_YEAR, -period);
            now_2.add(Calendar.DAY_OF_YEAR, -(2 * period));
            pst.setDate(1, DateHelper.sqld(now.getTime()));
            pst.setDate(2, DateHelper.sqld(now_1.getTime()));
            pst.setInt(3, nb);
            ResultSet rs = db.executeQuery(pst);
            List<TopLine> al = PortalDataProvider.toTopLine(rs);
            Top t = new Top(now_1.getTime(), now.getTime());
            t.setElements(al);
            if (t.getElements() == null || t.getElements().size() == 0) {
                t.setTotal(0);
                t.computeSubtotalFromElements();
                return t;
            }
            if (computeTotal) {
                PreparedStatement pstCount = db.prepareStatement(sqlTotal.toString());
                pstCount.setDate(1, DateHelper.sqld(now.getTime()));
                pstCount.setDate(2, DateHelper.sqld(now_1.getTime()));
                t.setTotal(DbHelper.getLong(pstCount));
            }
            if ("top".equals(displayType)) {
                pst.setDate(1, DateHelper.sqld(now.getTime()));
                pst.setDate(2, DateHelper.sqld(now_1.getTime()));
                pst.setInt(3, nb);
                rs = db.executeQuery(pst);
                List<TopLine> old = PortalDataProvider.toTopLine(rs);
                t.addRankEvolution(old);
            }
            if (computeTotal) {
                t.computeSubtotalFromElements();
                t.computePercents();
            }
            return t;
        } finally {
            db.exit();
        }
    }

    /**
	 * getTopHttpStatus
	 */
    public static Top getTopHttpStatus(Db db, String metric, Calendar baseDate, int period, int nb, String displayType) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopStatusSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getTopHttpStatus; invalid counting value: " + metric);
            boolean computeTotal = false;
            if ("count".equalsIgnoreCase(metric) || "volume".equalsIgnoreCase(metric)) {
                computeTotal = true;
            }
            StringBuffer sql = new StringBuffer("SELECT ");
            StringBuffer sqlTotal = new StringBuffer("SELECT ");
            sql.append(resolvedMetric);
            sqlTotal.append(resolvedMetric);
            sql.append(", COALESCE(http_status_code || ' (' || status || ')', d.http_status::text) ");
            sql.append(" FROM http.t_security_access_status_daily d INNER JOIN http.e_http_status st ON st.http_status_code = d.http_status WHERE calc_day <= ? AND calc_day > ? GROUP BY 2 ORDER BY 1 DESC LIMIT ?");
            sqlTotal.append(" FROM http.t_security_access_status_daily d INNER JOIN http.e_http_status st ON st.http_status_code = d.http_status WHERE calc_day <= ? AND calc_day > ? ");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            Calendar now = (Calendar) baseDate.clone();
            Calendar now_1 = (Calendar) now.clone();
            Calendar now_2 = (Calendar) now.clone();
            now_1.add(Calendar.DAY_OF_YEAR, -period);
            now_2.add(Calendar.DAY_OF_YEAR, -(2 * period));
            pst.setDate(1, DateHelper.sqld(now.getTime()));
            pst.setDate(2, DateHelper.sqld(now_1.getTime()));
            pst.setInt(3, nb);
            ResultSet rs = db.executeQuery(pst);
            List<TopLine> al = PortalDataProvider.toTopLine(rs);
            Top t = new Top(now_1.getTime(), now.getTime());
            t.setElements(al);
            if (t.getElements() == null || t.getElements().size() == 0) {
                t.setTotal(0);
                t.computeSubtotalFromElements();
                return t;
            }
            if (computeTotal) {
                PreparedStatement pstCount = db.prepareStatement(sqlTotal.toString());
                pstCount.setDate(1, DateHelper.sqld(now.getTime()));
                pstCount.setDate(2, DateHelper.sqld(now_1.getTime()));
                t.setTotal(DbHelper.getLong(pstCount));
            }
            if ("top".equals(displayType)) {
                pst.setDate(1, DateHelper.sqld(now.getTime()));
                pst.setDate(2, DateHelper.sqld(now_1.getTime()));
                pst.setInt(3, nb);
                rs = db.executeQuery(pst);
                List<TopLine> old = PortalDataProvider.toTopLine(rs);
                t.addRankEvolution(old);
            }
            if (computeTotal) {
                t.computeSubtotalFromElements();
                t.computePercents();
            }
            return t;
        } finally {
            db.exit();
        }
    }

    /**
	 * getHttpStatusEvolution
	 */
    public static TimeSeries getHttpStatusEvolution(Db db, String metric, String status, String timeScale, Integer nbDays) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopStatusSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getHttpStatusEvolution; invalid counting value: " + metric);
            StringBuffer sql = new StringBuffer("SELECT ");
            sql.append(resolvedMetric);
            sql.append(", date_trunc(?, d.calc_day) ");
            sql.append(" FROM http.t_security_access_status_daily d WHERE lower(d.http_status::text) = lower(?)");
            if (nbDays != null) sql.append(" AND date_trunc(?, d.calc_day) > (current_date - interval '" + nbDays + " days') ");
            sql.append(" GROUP BY 2 ORDER BY 2");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            int n = 1;
            pst.setString(n++, timeScale);
            pst.setString(n++, status);
            if (nbDays != null) pst.setString(n++, timeScale);
            ResultSet rs = db.executeQuery(pst);
            Date max = DateHelper.getFirstDayOfCurrentTimeUnit(timeScale);
            return PortalDataProvider.toTimeSeries(db, rs, max, timeScale);
        } finally {
            db.exit();
        }
    }

    /**
	 * getPathEvolution
	 */
    public static TimeSeries getPathEvolution(Db db, String metric, String path, String timeScale, Integer nbDays) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopAccessedAssetsSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getPathEvolution; invalid counting value: " + metric);
            StringBuffer sql = new StringBuffer("SELECT ");
            sql.append(resolvedMetric);
            sql.append(", date_trunc(?, d.calc_day) ");
            sql.append(" FROM http.t_security_access_site_path_daily d INNER JOIN http.t_site_path p ON p.t_site_path_id = d.t_site_path_id INNER JOIN http.t_domain_site s ON s.t_domain_site_id = p.t_domain_site_id WHERE lower(CASE WHEN s.site_name IS NULL OR s.site_name = '' THEN s.domain_name ELSE s.site_name || '.' || s.domain_name END || path) = lower(?)");
            if (nbDays != null) sql.append(" AND date_trunc(?, d.calc_day) > (current_date - interval '" + nbDays + " days') ");
            sql.append(" GROUP BY 2 ORDER BY 2");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            int n = 1;
            pst.setString(n++, timeScale);
            pst.setString(n++, path);
            if (nbDays != null) pst.setString(n++, timeScale);
            ResultSet rs = db.executeQuery(pst);
            Date max = DateHelper.getFirstDayOfCurrentTimeUnit(timeScale);
            return PortalDataProvider.toTimeSeries(db, rs, max, timeScale);
        } finally {
            db.exit();
        }
    }

    /**
	 * getSiteEvolution
	 */
    public static TimeSeries getSiteEvolution(Db db, String metric, String site, String timeScale, Integer nbDays) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopAccessedAssetsSet().get(metric);
            if (resolvedMetric == null) throw new IllegalArgumentException("DbHttpSecurity - getSiteEvolution; invalid counting value: " + metric);
            StringBuffer sql = new StringBuffer("SELECT ");
            sql.append(resolvedMetric);
            sql.append(", date_trunc(?, d.calc_day) ");
            sql.append(" FROM http.t_security_access_site_daily d INNER JOIN http.t_domain_site s ON s.t_domain_site_id = d.t_domain_site_id WHERE lower(CASE WHEN s.site_name IS NULL OR s.site_name = '' THEN s.domain_name ELSE s.site_name || '.' || s.domain_name END) = lower(?) ");
            if (nbDays != null) sql.append(" AND date_trunc(?, d.calc_day) > (current_date - interval '" + nbDays + " days') ");
            sql.append(" GROUP BY 2 ORDER BY 2");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            int n = 1;
            pst.setString(n++, timeScale);
            pst.setString(n++, site);
            if (nbDays != null) pst.setString(n++, timeScale);
            ResultSet rs = db.executeQuery(pst);
            Date max = DateHelper.getFirstDayOfCurrentTimeUnit(timeScale);
            return PortalDataProvider.toTimeSeries(db, rs, max, timeScale);
        } finally {
            db.exit();
        }
    }

    /**
	 * getAssetEvolution
	 */
    public static TimeSeries getAssetEvolution(Db db, String metric, String asset, String timeScale, Integer nbDays) throws Exception {
        try {
            db.enter();
            String resolvedMetric = getTopAccessedAssetsSet().get(metric);
            if (resolvedMetric == null) {
                throw new IllegalArgumentException("DbHttpSecurity - getAssetEvolution; invalid counting value: " + metric);
            }
            StringBuffer sql = new StringBuffer("SELECT ");
            sql.append(resolvedMetric);
            sql.append(", date_trunc(?, d.calc_day) ");
            sql.append(" FROM http.t_security_access_asset_daily d INNER JOIN asset.e_asset a ON a.e_asset_id = d.e_asset_id WHERE lower(a.display_name) = lower(?) ");
            if (nbDays != null) sql.append(" AND date_trunc(?, d.calc_day) > (current_date - interval '" + nbDays + " days') ");
            sql.append(" GROUP BY 2 ORDER BY 2");
            PreparedStatement pst = db.prepareStatement(sql.toString());
            int n = 1;
            pst.setString(n++, timeScale);
            pst.setString(n++, asset);
            if (nbDays != null) pst.setString(n++, timeScale);
            ResultSet rs = db.executeQuery(pst);
            Date max = DateHelper.getFirstDayOfCurrentTimeUnit(timeScale);
            return PortalDataProvider.toTimeSeries(db, rs, max, timeScale);
        } finally {
            db.exit();
        }
    }
}
