package com.entelience.report.itsm;

import com.entelience.esis.Activatable;
import com.entelience.report.Report;
import com.entelience.report.DbAssets;
import com.entelience.sql.Db;
import com.entelience.sql.DbHelper;
import com.entelience.util.DateHelper;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * computes metrics on IT System Mangement
 */
public class ItsmReport extends Report {

    public ItsmReport(Db workDb, Db statusDb) throws Exception {
        super(workDb, statusDb);
    }

    /**
     * ItSM probes that import data per assets
     *
     **/
    public List<Activatable> getActivatableElementsForReport(Db db) throws Exception {
        List<Activatable> l = new ArrayList<Activatable>();
        l.add(new com.entelience.probe.itsm.Unicenter());
        l.add(new com.entelience.probe.itsm.UnicenterSurveys());
        return l;
    }

    protected boolean runReport(Db db) throws Exception {
        computeTicketDaily(db);
        computeTicketTypeDaily(db);
        computeTicketCategoryDaily(db);
        computeTicketUserDaily(db);
        computeTicketIpsuDaily(db);
        computeTicketStatusDaily(db);
        DbAssets.computeAssetDaily(db);
        computeSatisfactionDaily(db);
        computeAnswerSatisfactionLevel(db);
        computeSatisfactionLevelDaily(db);
        return true;
    }

    private int getIntFromIntMap(Map<Integer, Integer> map, int id) {
        Integer res = map.get(id);
        if (res == null) return 0;
        return res;
    }

    private long getLongFromIntMap(Map<Integer, Long> map, int id) {
        Long res = map.get(id);
        if (res == null) return 0;
        return res;
    }

    /**
     * Compute satisfction levels for an answer. the level can be between 0 and 20.
     * Each answer has a weigh between 0 and 10 that is added or substracted to an initial level of 10
     * To know if this is added or substracted, we have several 'positive' attributes given to questions and answers
     */
    private void computeAnswerSatisfactionLevel(Db db) throws Exception {
        try {
            db.enter();
            PreparedStatement pstU = db.prepareStatement("UPDATE itsm.t_satisfaction_answer AS a SET satisfaction_level_id = itsm.t_satisfaction_level.t_satisfaction_level_id FROM itsm.t_satisfaction_level WHERE t_satisfaction_answer_id = ? AND min_level <= ? AND max_level >= ?");
            PreparedStatement pst = db.prepareStatement("SELECT a.t_satisfaction_answer_id, positive, always_positive, value, positive_question, q.t_satisfaction_question_id, answer, question, pattern FROM itsm.t_satisfaction_answer a INNER JOIN itsm.t_satisfaction_question q ON q.t_satisfaction_question_id = a.t_satisfaction_question_id LEFT JOIN itsm.t_satisfaction_answer_pattern p ON lower(a.answer) like lower(p.pattern) LEFT JOIN itsm.t_satisfaction_question_category cat ON cat.t_satisfaction_question_category_id = q.t_satisfaction_question_category_id WHERE a.satisfaction_level_id IS NULL");
            ResultSet rs = db.executeQuery(pst);
            if (rs.next()) {
                do {
                    int answerId = rs.getInt(1);
                    Boolean patternPositive = (Boolean) rs.getObject(2);
                    Boolean patternAlwaysPositive = (Boolean) rs.getObject(3);
                    Boolean questionPositive = (Boolean) rs.getObject(5);
                    String question = rs.getString(8);
                    String answer = rs.getString(7);
                    if (patternPositive == null) {
                        _logger.warn("Answer " + answer + " cannot be linked to any pattern");
                        continue;
                    }
                    int value = rs.getInt(4);
                    boolean globalPositive;
                    if (patternAlwaysPositive) {
                        globalPositive = patternPositive;
                    } else {
                        if (questionPositive == null) {
                            _logger.warn("Question " + question + " not categorized. Its answers cannot receive a level");
                            continue;
                        }
                        globalPositive = !(questionPositive ^ patternPositive);
                    }
                    int valueResult = globalPositive ? 10 + value : 10 - value;
                    _logger.info("Answer " + answer + " has a level value of " + valueResult);
                    pstU.setInt(1, answerId);
                    pstU.setInt(2, valueResult);
                    pstU.setInt(3, valueResult);
                    db.executeUpdate(pstU);
                } while (rs.next());
            } else {
                _logger.debug("No answer with a null satisfaction_level_id");
            }
        } finally {
            db.exit();
        }
    }

    private void computeSatisfactionLevelDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing satisfaction level daily");
            Date nextDate = DateHelper.rollForwards(effective_date);
            PreparedStatement pstDel = db.prepareStatement("DELETE FROM itsm.t_satisfaction_level_daily WHERE calc_day = ?");
            pstDel.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstDel);
            Set<Integer> levels = new HashSet<Integer>();
            Map<Integer, Integer> nbAnswers = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbUniqueUsers = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbDistinctTickets = new HashMap<Integer, Integer>();
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT satisfaction_level_id, COUNT(*) FROM itsm.t_satisfaction_survey_response rep INNER JOIN itsm.t_satisfaction_answer a ON a.t_satisfaction_answer_id = rep.t_satisfaction_answer_id WHERE answer_date >= ? AND answer_date < ? GROUP BY 1");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbAnswers.put(rs.getInt(1), rs.getInt(2));
                        levels.add(rs.getInt(1));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT satisfaction_level_id, COUNT(DISTINCT r.t_app_login_id) FROM itsm.t_satisfaction_survey_response rep INNER JOIN itsm.t_satisfaction_answer a ON a.t_satisfaction_answer_id = rep.t_satisfaction_answer_id INNER JOIN itsm.t_satisfaction_survey_result r ON r.t_satisfaction_survey_result_id = rep.t_satisfaction_survey_result_id WHERE answer_date >= ? AND answer_date < ? GROUP BY 1");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbUniqueUsers.put(rs.getInt(1), rs.getInt(2));
                        levels.add(rs.getInt(1));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT satisfaction_level_id, COUNT(DISTINCT t.t_ticket_id) FROM itsm.t_satisfaction_survey_response rep INNER JOIN itsm.t_satisfaction_answer a ON a.t_satisfaction_answer_id = rep.t_satisfaction_answer_id INNER JOIN itsm.t_satisfaction_survey_to_ticket t ON t.t_satisfaction_survey_result_id = rep.t_satisfaction_survey_result_id WHERE answer_date >= ? AND answer_date < ? GROUP BY 1");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbDistinctTickets.put(rs.getInt(1), rs.getInt(2));
                        levels.add(rs.getInt(1));
                    } while (rs.next());
                }
            }
            PreparedStatement pstI = db.prepareStatement("INSERT INTO itsm.t_satisfaction_level_daily (calc_day, t_satisfaction_level_id, count_answers, count_users, count_related_tickets) VALUES (?, ?, ?, ?, ?)");
            for (Iterator<Integer> it = levels.iterator(); it.hasNext(); ) {
                int level = it.next();
                pstI.setDate(1, DateHelper.sqld(effective_date));
                pstI.setInt(2, level);
                pstI.setInt(3, getIntFromIntMap(nbAnswers, level));
                pstI.setInt(4, getIntFromIntMap(nbUniqueUsers, level));
                pstI.setInt(5, getIntFromIntMap(nbDistinctTickets, level));
                db.executeUpdate(pstI);
            }
        } finally {
            db.exit();
        }
    }

    private void computeSatisfactionDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing satisfaction daily");
            Date nextDate = DateHelper.rollForwards(effective_date);
            PreparedStatement pstDel = db.prepareStatement("DELETE FROM itsm.t_satisfaction_daily WHERE calc_day = ?");
            pstDel.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstDel);
            int nbAnswers = 0;
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*) FROM itsm.t_satisfaction_survey_response WHERE answer_date >= ? AND answer_date < ?");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbAnswers = DbHelper.getIntKey(pstSel);
            }
            int nbUniqueUsers = 0;
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t_app_login_id) FROM itsm.t_satisfaction_survey_result r WHERE result_date >= ? AND result_date < ?");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbUniqueUsers = DbHelper.getIntKey(pstSel);
            }
            int nbDistinctTickets = 0;
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id) FROM itsm.t_satisfaction_survey_result r INNER JOIN itsm.t_satisfaction_survey_to_ticket t ON t.t_satisfaction_survey_result_id = r.t_satisfaction_survey_result_id WHERE result_date >= ? AND result_date < ?");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbDistinctTickets = DbHelper.getIntKey(pstSel);
            }
            Map<Integer, Integer> nbQuestionsPerSurvey = new HashMap<Integer, Integer>();
            {
                PreparedStatement pstFillMap = db.prepareStatement("SELECT q.t_satisfaction_survey_id, COUNT(*) FROM itsm.t_satisfaction_question q GROUP BY 1");
                ResultSet rs = db.executeQuery(pstFillMap);
                if (rs.next()) {
                    do {
                        nbQuestionsPerSurvey.put(rs.getInt(1), rs.getInt(2));
                    } while (rs.next());
                } else {
                    _logger.warn("Table itsm.t_satisfaction_question not filled");
                }
            }
            int nbUnansweredQuestions = 0;
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t_satisfaction_answer_id), result_date, r.t_satisfaction_survey_id, r.t_app_login_id FROM itsm.t_satisfaction_survey_result r INNER JOIN itsm.t_satisfaction_survey_response resp ON resp.t_satisfaction_survey_result_id = r.t_satisfaction_survey_result_id WHERE result_date >= ? AND result_date < ? GROUP BY 2,3,4");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        int answered = rs.getInt(1);
                        int surveyId = rs.getInt(3);
                        Integer expected = nbQuestionsPerSurvey.get(surveyId);
                        if (expected == null) expected = 0;
                        if (answered > expected) {
                            _logger.warn("More answers than questions");
                        } else if (answered < expected) {
                            nbUnansweredQuestions += expected - answered;
                        }
                    } while (rs.next());
                } else {
                    _logger.debug("No answers this day");
                }
            }
            PreparedStatement pstI = db.prepareStatement("INSERT INTO itsm.t_satisfaction_daily (calc_day, count_answers, count_users, count_unanswered_questions, count_related_tickets) VALUES (?, ?, ?, ?, ?)");
            pstI.setDate(1, DateHelper.sqld(effective_date));
            pstI.setInt(2, nbAnswers);
            pstI.setInt(3, nbUniqueUsers);
            pstI.setInt(4, nbUnansweredQuestions);
            pstI.setInt(5, nbDistinctTickets);
            db.executeUpdate(pstI);
        } finally {
            db.exit();
        }
    }

    private void computeTicketIpsuDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing ticket/ipsu daily");
            PreparedStatement pstDel = db.prepareStatement("DELETE FROM itsm.t_ticket_ipsu_daily WHERE calc_day = ?");
            pstDel.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstDel);
            Date nextDate = DateHelper.rollForwards(effective_date);
            Set<TwoInts> ipsuIds = new HashSet<TwoInts>();
            Map<TwoInts, Integer> nbExisting = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbOpened = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbUnresolved = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbNew = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbClosed = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbResolved = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbUpdated = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbStatusChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbCategoryChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbIpsuChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbAssigneeChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbSupportLevelChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbSlaViolations = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Long> minTimeToClose = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> maxTimeToClose = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> avgTimeToClose = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> minTimeToResolve = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> maxTimeToResolve = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> avgTimeToResolve = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> minOpenTime = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> maxOpenTime = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> avgOpenTime = new HashMap<TwoInts, Long>();
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbExisting.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbOpened.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) AND (resolve_date IS NULL OR resolve_date > ?) GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                pstSel.setDate(3, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbUnresolved.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date >= ? AND open_date < ? GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbNew.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE close_date >= ? AND close_date < ? GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbClosed.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE resolve_date >= ? AND resolve_date < ? GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbResolved.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbUpdated.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'status' GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbStatusChange.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'category' GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbCategoryChange.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND (field = 'impact' OR field = 'priority' OR field = 'severity' OR field = 'urgency') GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbIpsuChange.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'assignee' GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbAssigneeChange.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'support_lev' GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbSupportLevelChange.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(close_date - open_date)), extract('epoch' from MAX(close_date - open_date)), extract('epoch' from AVG(close_date - open_date)), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE close_date >= ? AND close_date < ? AND close_date > open_date GROUP BY 4, 5");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(4), rs.getInt(5));
                        minTimeToClose.put(ids, rs.getLong(1));
                        maxTimeToClose.put(ids, rs.getLong(2));
                        avgTimeToClose.put(ids, rs.getLong(3));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(resolve_date - open_date)), extract('epoch' from MAX(resolve_date - open_date)), extract('epoch' from AVG(resolve_date - open_date)), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE resolve_date >= ? AND resolve_date < ? AND resolve_date > open_date GROUP BY 4, 5");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(4), rs.getInt(5));
                        minTimeToResolve.put(ids, rs.getLong(1));
                        maxTimeToResolve.put(ids, rs.getLong(2));
                        avgTimeToResolve.put(ids, rs.getLong(3));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(? - open_date)), extract('epoch' from MAX(? - open_date)), extract('epoch' from AVG(? - open_date)), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date > ? OR close_date IS NULL) GROUP BY 4, 5");
                pstSel.setDate(1, DateHelper.sqld(nextDate));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                pstSel.setDate(3, DateHelper.sqld(nextDate));
                pstSel.setDate(4, DateHelper.sqld(effective_date));
                pstSel.setDate(5, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(4), rs.getInt(5));
                        minOpenTime.put(ids, rs.getLong(1));
                        maxOpenTime.put(ids, rs.getLong(2));
                        avgOpenTime.put(ids, rs.getLong(3));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), COALESCE(i.normalized_ipsu_id, i.t_ipsu_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_ipsu ti ON ti.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ipsu i ON i.t_ipsu_id = ti.t_ipsu_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND sla_violation GROUP BY 2, 3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbSlaViolations.put(ids, rs.getInt(1));
                        ipsuIds.add(ids);
                    } while (rs.next());
                }
            }
            PreparedStatement pstIns = db.prepareStatement("INSERT INTO itsm.t_ticket_ipsu_daily (calc_day, t_ipsu_id, count_new, count_opened, count_unresolved, count_closed, count_updated, count_resolved, count_status_changed, count_ipsu_changed, count_category_changed, count_assignee_changed, count_supp_lvl_changed, min_time_to_close, max_time_to_close, avg_time_to_close, min_time_to_resolve, max_time_to_resolve, avg_time_to_resolve, min_open_time, max_open_time, avg_open_time, count_existing, count_with_sla_violation, t_ticket_type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (Iterator<TwoInts> it = ipsuIds.iterator(); it.hasNext(); ) {
                TwoInts ids = it.next();
                pstIns.setDate(1, DateHelper.sqld(effective_date));
                pstIns.setInt(2, ids.id);
                pstIns.setInt(3, getIntFromMap(nbNew, ids));
                pstIns.setInt(4, getIntFromMap(nbOpened, ids));
                pstIns.setInt(5, getIntFromMap(nbUnresolved, ids));
                pstIns.setInt(6, getIntFromMap(nbClosed, ids));
                pstIns.setInt(7, getIntFromMap(nbUpdated, ids));
                pstIns.setInt(8, getIntFromMap(nbResolved, ids));
                pstIns.setInt(9, getIntFromMap(nbStatusChange, ids));
                pstIns.setInt(10, getIntFromMap(nbCategoryChange, ids));
                pstIns.setInt(11, getIntFromMap(nbIpsuChange, ids));
                pstIns.setInt(12, getIntFromMap(nbAssigneeChange, ids));
                pstIns.setInt(13, getIntFromMap(nbSupportLevelChange, ids));
                pstIns.setLong(14, getLongFromMap(minTimeToClose, ids));
                pstIns.setLong(15, getLongFromMap(maxTimeToClose, ids));
                pstIns.setLong(16, getLongFromMap(avgTimeToClose, ids));
                pstIns.setLong(17, getLongFromMap(minTimeToResolve, ids));
                pstIns.setLong(18, getLongFromMap(maxTimeToResolve, ids));
                pstIns.setLong(19, getLongFromMap(avgTimeToResolve, ids));
                pstIns.setLong(20, getLongFromMap(minOpenTime, ids));
                pstIns.setLong(21, getLongFromMap(maxOpenTime, ids));
                pstIns.setLong(22, getLongFromMap(avgOpenTime, ids));
                pstIns.setInt(23, getIntFromMap(nbExisting, ids));
                pstIns.setInt(24, getIntFromMap(nbSlaViolations, ids));
                pstIns.setInt(25, ids.typeId);
                db.executeUpdate(pstIns);
            }
        } finally {
            db.exit();
        }
    }

    private static class TwoInts {

        public TwoInts(int id, int typeId) {
            this.id = id;
            this.typeId = typeId;
        }

        public String toString() {
            return id + "," + typeId;
        }

        public int hashCode() {
            return toString().hashCode();
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            return o.hashCode() == hashCode();
        }

        int id;

        int typeId;
    }

    private static class UserRaci {

        public UserRaci(int userId, boolean r, boolean a, boolean c, boolean i, int typeId) {
            this.userId = userId;
            this.r = r;
            this.a = a;
            this.c = c;
            this.i = i;
            this.typeId = typeId;
        }

        public String toString() {
            return userId + "," + r + "," + a + "," + c + "," + i + "," + typeId;
        }

        public int hashCode() {
            return toString().hashCode();
        }

        public boolean equals(Object o) {
            if (o == null) return false;
            return o.hashCode() == hashCode();
        }

        int userId;

        boolean r;

        boolean a;

        boolean c;

        boolean i;

        int typeId;
    }

    private void computeTicketUserDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing ticket/user daily");
            PreparedStatement pstDel = db.prepareStatement("DELETE FROM itsm.t_ticket_user_daily WHERE calc_day = ?");
            pstDel.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstDel);
            Date nextDate = DateHelper.rollForwards(effective_date);
            Set<UserRaci> userIds = new HashSet<UserRaci>();
            Map<UserRaci, Integer> nbExisting = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbOpened = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbUnresolved = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbNew = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbClosed = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbResolved = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbUpdated = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbStatusChange = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbCategoryChange = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbIpsuChange = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbAssigneeChange = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbSupportLevelChange = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Integer> nbSlaViolations = new HashMap<UserRaci, Integer>();
            Map<UserRaci, Long> minTimeToClose = new HashMap<UserRaci, Long>();
            Map<UserRaci, Long> maxTimeToClose = new HashMap<UserRaci, Long>();
            Map<UserRaci, Long> avgTimeToClose = new HashMap<UserRaci, Long>();
            Map<UserRaci, Long> minTimeToResolve = new HashMap<UserRaci, Long>();
            Map<UserRaci, Long> maxTimeToResolve = new HashMap<UserRaci, Long>();
            Map<UserRaci, Long> avgTimeToResolve = new HashMap<UserRaci, Long>();
            Map<UserRaci, Long> minOpenTime = new HashMap<UserRaci, Long>();
            Map<UserRaci, Long> maxOpenTime = new HashMap<UserRaci, Long>();
            Map<UserRaci, Long> avgOpenTime = new HashMap<UserRaci, Long>();
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbExisting.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbOpened.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) AND (resolve_date IS NULL OR resolve_date > ?) AND tr.r GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                pstSel.setDate(3, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbUnresolved.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date >= ? AND open_date < ? AND tr.r GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbNew.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE close_date >= ? AND close_date < ? AND tr.r GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbClosed.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE resolve_date >= ? AND resolve_date < ? AND tr.r GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbResolved.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id  INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND tr.r GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbUpdated.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND tr.r AND field = 'status' GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbStatusChange.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND tr.r AND field = 'category' GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbCategoryChange.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND tr.r AND (field = 'impact' OR field = 'priority' OR field = 'severity' OR field = 'urgency') GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbIpsuChange.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND tr.r AND field = 'assignee' GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbAssigneeChange.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND tr.r AND field = 'support_lev' GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbSupportLevelChange.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(close_date - open_date)), extract('epoch' from MAX(close_date - open_date)), extract('epoch' from AVG(close_date - open_date)), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE close_date >= ? AND close_date < ? AND tr.r AND close_date > open_date GROUP BY 4,5,6,7,8,9");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(4), rs.getBoolean(5), rs.getBoolean(6), rs.getBoolean(7), rs.getBoolean(8), rs.getInt(9));
                        minTimeToClose.put(ur, rs.getLong(1));
                        maxTimeToClose.put(ur, rs.getLong(2));
                        avgTimeToClose.put(ur, rs.getLong(3));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(resolve_date - open_date)), extract('epoch' from MAX(resolve_date - open_date)), extract('epoch' from AVG(resolve_date - open_date)), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE resolve_date >= ? AND resolve_date < ? AND tr.r AND resolve_date > open_date GROUP BY 4,5,6,7,8,9");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(4), rs.getBoolean(5), rs.getBoolean(6), rs.getBoolean(7), rs.getBoolean(8), rs.getInt(9));
                        minTimeToResolve.put(ur, rs.getLong(1));
                        maxTimeToResolve.put(ur, rs.getLong(2));
                        avgTimeToResolve.put(ur, rs.getLong(3));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(? - open_date)), extract('epoch' from MAX(? - open_date)), extract('epoch' from AVG(? - open_date)), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date > ? OR close_date IS NULL) AND tr.r GROUP BY 4,5,6,7,8,9");
                pstSel.setDate(1, DateHelper.sqld(nextDate));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                pstSel.setDate(3, DateHelper.sqld(nextDate));
                pstSel.setDate(4, DateHelper.sqld(effective_date));
                pstSel.setDate(5, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(4), rs.getBoolean(5), rs.getBoolean(6), rs.getBoolean(7), rs.getBoolean(8), rs.getInt(9));
                        minOpenTime.put(ur, rs.getLong(1));
                        maxOpenTime.put(ur, rs.getLong(2));
                        avgOpenTime.put(ur, rs.getLong(3));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT th.t_ticket_id), tr.t_app_login_id, tr.r, tr.a, tr.c, tr.i, COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_raci tr ON tr.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND sla_violation GROUP BY 2,3,4,5,6,7");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        UserRaci ur = new UserRaci(rs.getInt(2), rs.getBoolean(3), rs.getBoolean(4), rs.getBoolean(5), rs.getBoolean(6), rs.getInt(7));
                        nbSlaViolations.put(ur, rs.getInt(1));
                        userIds.add(ur);
                    } while (rs.next());
                }
            }
            PreparedStatement pstIns = db.prepareStatement("INSERT INTO itsm.t_ticket_user_daily (calc_day, t_app_login_id, count_new, count_opened, count_unresolved, count_closed, count_updated, count_resolved, count_status_changed, count_ipsu_changed, count_category_changed, count_assignee_changed, count_supp_lvl_changed, min_time_to_close, max_time_to_close, avg_time_to_close, min_time_to_resolve, max_time_to_resolve, avg_time_to_resolve, min_open_time, max_open_time, avg_open_time, r, a, c, i, count_existing, count_with_sla_violation, t_ticket_type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (Iterator<UserRaci> it = userIds.iterator(); it.hasNext(); ) {
                UserRaci usr = it.next();
                pstIns.setDate(1, DateHelper.sqld(effective_date));
                pstIns.setInt(2, usr.userId);
                pstIns.setInt(3, getIntFromMap(nbNew, usr));
                pstIns.setInt(4, getIntFromMap(nbOpened, usr));
                pstIns.setInt(5, getIntFromMap(nbUnresolved, usr));
                pstIns.setInt(6, getIntFromMap(nbClosed, usr));
                pstIns.setInt(7, getIntFromMap(nbUpdated, usr));
                pstIns.setInt(8, getIntFromMap(nbResolved, usr));
                pstIns.setInt(9, getIntFromMap(nbStatusChange, usr));
                pstIns.setInt(10, getIntFromMap(nbCategoryChange, usr));
                pstIns.setInt(11, getIntFromMap(nbIpsuChange, usr));
                pstIns.setInt(12, getIntFromMap(nbAssigneeChange, usr));
                pstIns.setInt(13, getIntFromMap(nbSupportLevelChange, usr));
                pstIns.setLong(14, getLongFromMap(minTimeToClose, usr));
                pstIns.setLong(15, getLongFromMap(maxTimeToClose, usr));
                pstIns.setLong(16, getLongFromMap(avgTimeToClose, usr));
                pstIns.setLong(17, getLongFromMap(minTimeToResolve, usr));
                pstIns.setLong(18, getLongFromMap(maxTimeToResolve, usr));
                pstIns.setLong(19, getLongFromMap(avgTimeToResolve, usr));
                pstIns.setLong(20, getLongFromMap(minOpenTime, usr));
                pstIns.setLong(21, getLongFromMap(maxOpenTime, usr));
                pstIns.setLong(22, getLongFromMap(avgOpenTime, usr));
                pstIns.setBoolean(23, usr.r);
                pstIns.setBoolean(24, usr.a);
                pstIns.setBoolean(25, usr.c);
                pstIns.setBoolean(26, usr.i);
                pstIns.setInt(27, getIntFromMap(nbExisting, usr));
                pstIns.setInt(28, getIntFromMap(nbSlaViolations, usr));
                pstIns.setInt(29, usr.typeId);
                db.executeUpdate(pstIns);
            }
        } finally {
            db.exit();
        }
    }

    private int getIntFromMap(Map<UserRaci, Integer> map, UserRaci id) {
        Integer res = map.get(id);
        if (res == null) return 0;
        return res;
    }

    private long getLongFromMap(Map<UserRaci, Long> map, UserRaci id) {
        Long res = map.get(id);
        if (res == null) return 0;
        return res;
    }

    private int getIntFromMap(Map<TwoInts, Integer> map, TwoInts id) {
        Integer res = map.get(id);
        if (res == null) return 0;
        return res;
    }

    private long getLongFromMap(Map<TwoInts, Long> map, TwoInts id) {
        Long res = map.get(id);
        if (res == null) return 0;
        return res;
    }

    private void computeTicketCategoryDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing ticket/category daily");
            PreparedStatement pstDel = db.prepareStatement("DELETE FROM itsm.t_ticket_category_daily WHERE calc_day = ?");
            pstDel.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstDel);
            Date nextDate = DateHelper.rollForwards(effective_date);
            Set<TwoInts> catIds = new HashSet<TwoInts>();
            Map<TwoInts, Integer> nbExisting = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbOpened = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbUnresolved = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbNew = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbClosed = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbResolved = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbUpdated = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbStatusChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbCategoryChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbIpsuChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbAssigneeChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbSupportLevelChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbSlaViolations = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Long> minTimeToClose = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> maxTimeToClose = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> avgTimeToClose = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> minTimeToResolve = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> maxTimeToResolve = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> avgTimeToResolve = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> minOpenTime = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> maxOpenTime = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> avgOpenTime = new HashMap<TwoInts, Long>();
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbExisting.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbOpened.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) AND (resolve_date IS NULL OR resolve_date > ?) GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                pstSel.setDate(3, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbUnresolved.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date >= ? AND open_date < ? GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbNew.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE close_date >= ? AND close_date < ? GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbClosed.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE resolve_date >= ? AND resolve_date < ? GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbResolved.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbUpdated.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'status' GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbStatusChange.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'category' GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbCategoryChange.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND (field = 'impact' OR field = 'priority' OR field = 'severity' OR field = 'urgency') GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbIpsuChange.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'assignee' GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbAssigneeChange.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'support_lev' GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbSupportLevelChange.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(close_date - open_date)), extract('epoch' from MAX(close_date - open_date)), extract('epoch' from AVG(close_date - open_date)), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE close_date >= ? AND close_date < ? AND close_date > open_date GROUP BY 4,5");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(4), rs.getInt(5));
                        minTimeToClose.put(ids, rs.getLong(1));
                        maxTimeToClose.put(ids, rs.getLong(2));
                        avgTimeToClose.put(ids, rs.getLong(3));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(resolve_date - open_date)), extract('epoch' from MAX(resolve_date - open_date)), extract('epoch' from AVG(resolve_date - open_date)), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE resolve_date >= ? AND resolve_date < ? AND resolve_date > open_date GROUP BY 4,5");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(4), rs.getInt(5));
                        minTimeToResolve.put(ids, rs.getLong(1));
                        maxTimeToResolve.put(ids, rs.getLong(2));
                        avgTimeToResolve.put(ids, rs.getLong(3));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(? - open_date)), extract('epoch' from MAX(? - open_date)), extract('epoch' from AVG(? - open_date)), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date > ? OR close_date IS NULL) GROUP BY 4,5");
                pstSel.setDate(1, DateHelper.sqld(nextDate));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                pstSel.setDate(3, DateHelper.sqld(nextDate));
                pstSel.setDate(4, DateHelper.sqld(effective_date));
                pstSel.setDate(5, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(4), rs.getInt(5));
                        minOpenTime.put(ids, rs.getLong(1));
                        maxOpenTime.put(ids, rs.getLong(2));
                        avgOpenTime.put(ids, rs.getLong(3));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(c.normalized_category_id, c.t_category_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_ticket_category tc ON tc.t_ticket_id = t.t_ticket_id INNER JOIN itsm.t_category c ON c.t_category_id = tc.t_category_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND sla_violation GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbSlaViolations.put(ids, rs.getInt(1));
                        catIds.add(ids);
                    } while (rs.next());
                }
            }
            PreparedStatement pstIns = db.prepareStatement("INSERT INTO itsm.t_ticket_category_daily (calc_day, t_category_id, count_new, count_opened, count_unresolved, count_closed, count_updated, count_resolved, count_status_changed, count_ipsu_changed, count_category_changed, count_assignee_changed, count_supp_lvl_changed, min_time_to_close, max_time_to_close, avg_time_to_close, min_time_to_resolve, max_time_to_resolve, avg_time_to_resolve, min_open_time, max_open_time, avg_open_time, count_existing, count_with_sla_violation, t_ticket_type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (Iterator<TwoInts> it = catIds.iterator(); it.hasNext(); ) {
                TwoInts catId = it.next();
                pstIns.setDate(1, DateHelper.sqld(effective_date));
                pstIns.setInt(2, catId.id);
                pstIns.setInt(3, getIntFromMap(nbNew, catId));
                pstIns.setInt(4, getIntFromMap(nbOpened, catId));
                pstIns.setInt(5, getIntFromMap(nbUnresolved, catId));
                pstIns.setInt(6, getIntFromMap(nbClosed, catId));
                pstIns.setInt(7, getIntFromMap(nbUpdated, catId));
                pstIns.setInt(8, getIntFromMap(nbResolved, catId));
                pstIns.setInt(9, getIntFromMap(nbStatusChange, catId));
                pstIns.setInt(10, getIntFromMap(nbCategoryChange, catId));
                pstIns.setInt(11, getIntFromMap(nbIpsuChange, catId));
                pstIns.setInt(12, getIntFromMap(nbAssigneeChange, catId));
                pstIns.setInt(13, getIntFromMap(nbSupportLevelChange, catId));
                pstIns.setLong(14, getLongFromMap(minTimeToClose, catId));
                pstIns.setLong(15, getLongFromMap(maxTimeToClose, catId));
                pstIns.setLong(16, getLongFromMap(avgTimeToClose, catId));
                pstIns.setLong(17, getLongFromMap(minTimeToResolve, catId));
                pstIns.setLong(18, getLongFromMap(maxTimeToResolve, catId));
                pstIns.setLong(19, getLongFromMap(avgTimeToResolve, catId));
                pstIns.setLong(20, getLongFromMap(minOpenTime, catId));
                pstIns.setLong(21, getLongFromMap(maxOpenTime, catId));
                pstIns.setLong(22, getLongFromMap(avgOpenTime, catId));
                pstIns.setInt(23, getIntFromMap(nbExisting, catId));
                pstIns.setInt(24, getIntFromMap(nbSlaViolations, catId));
                pstIns.setInt(25, catId.typeId);
                db.executeUpdate(pstIns);
            }
        } finally {
            db.exit();
        }
    }

    private void computeTicketStatusDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing ticket/status daily");
            PreparedStatement pstDel = db.prepareStatement("DELETE FROM itsm.t_ticket_status_daily WHERE calc_day = ?");
            pstDel.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstDel);
            Date nextDate = DateHelper.rollForwards(effective_date);
            Set<TwoInts> stsIds = new HashSet<TwoInts>();
            Map<TwoInts, Integer> nbExisting = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbOpened = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbUnresolved = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbNew = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbClosed = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbResolved = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbUpdated = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbStatusChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbCategoryChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbIpsuChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbAssigneeChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbSupportLevelChange = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Integer> nbSlaViolations = new HashMap<TwoInts, Integer>();
            Map<TwoInts, Long> minTimeToClose = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> maxTimeToClose = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> avgTimeToClose = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> minTimeToResolve = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> maxTimeToResolve = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> avgTimeToResolve = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> minOpenTime = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> maxOpenTime = new HashMap<TwoInts, Long>();
            Map<TwoInts, Long> avgOpenTime = new HashMap<TwoInts, Long>();
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbExisting.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbOpened.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) AND (resolve_date IS NULL OR resolve_date > ?) GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                pstSel.setDate(3, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbUnresolved.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date >= ? AND open_date < ? GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbNew.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE close_date >= ? AND close_date < ? GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbClosed.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE resolve_date >= ? AND resolve_date < ? GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbResolved.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbUpdated.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'status' GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbStatusChange.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'category' GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbCategoryChange.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND (field = 'impact' OR field = 'priority' OR field = 'severity' OR field = 'urgency') GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbIpsuChange.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'assignee' GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbAssigneeChange.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'support_lev' GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbSupportLevelChange.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(close_date - open_date)), extract('epoch' from MAX(close_date - open_date)), extract('epoch' from AVG(close_date - open_date)), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE close_date >= ? AND close_date < ? AND close_date > open_date GROUP BY 4,5");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(4), rs.getInt(5));
                        minTimeToClose.put(ids, rs.getLong(1));
                        maxTimeToClose.put(ids, rs.getLong(2));
                        avgTimeToClose.put(ids, rs.getLong(3));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(resolve_date - open_date)), extract('epoch' from MAX(resolve_date - open_date)), extract('epoch' from AVG(resolve_date - open_date)), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE resolve_date >= ? AND resolve_date < ? AND resolve_date > open_date GROUP BY 4,5");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(4), rs.getInt(5));
                        minTimeToResolve.put(ids, rs.getLong(1));
                        maxTimeToResolve.put(ids, rs.getLong(2));
                        avgTimeToResolve.put(ids, rs.getLong(3));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(? - open_date)), extract('epoch' from MAX(? - open_date)), extract('epoch' from AVG(? - open_date)), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date > ? OR close_date IS NULL) GROUP BY 4,5");
                pstSel.setDate(1, DateHelper.sqld(nextDate));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                pstSel.setDate(3, DateHelper.sqld(nextDate));
                pstSel.setDate(4, DateHelper.sqld(effective_date));
                pstSel.setDate(5, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(4), rs.getInt(5));
                        minOpenTime.put(ids, rs.getLong(1));
                        maxOpenTime.put(ids, rs.getLong(2));
                        avgOpenTime.put(ids, rs.getLong(3));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(s.normalized_status_id, s.t_status_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history th INNER JOIN itsm.t_ticket t ON t.t_ticket_id = th.t_ticket_id INNER JOIN itsm.t_status s ON s.t_status_id = t.t_status_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND sla_violation GROUP BY 2,3");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        TwoInts ids = new TwoInts(rs.getInt(2), rs.getInt(3));
                        nbSlaViolations.put(ids, rs.getInt(1));
                        stsIds.add(ids);
                    } while (rs.next());
                }
            }
            PreparedStatement pstIns = db.prepareStatement("INSERT INTO itsm.t_ticket_status_daily (calc_day, t_status_id, count_new, count_opened, count_unresolved, count_closed, count_updated, count_resolved, count_status_changed, count_ipsu_changed, count_category_changed, count_assignee_changed, count_supp_lvl_changed, min_time_to_close, max_time_to_close, avg_time_to_close, min_time_to_resolve, max_time_to_resolve, avg_time_to_resolve, min_open_time, max_open_time, avg_open_time, count_existing, count_with_sla_violation, t_ticket_type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (Iterator<TwoInts> it = stsIds.iterator(); it.hasNext(); ) {
                TwoInts stId = it.next();
                pstIns.setDate(1, DateHelper.sqld(effective_date));
                pstIns.setInt(2, stId.id);
                pstIns.setInt(3, getIntFromMap(nbNew, stId));
                pstIns.setInt(4, getIntFromMap(nbOpened, stId));
                pstIns.setInt(5, getIntFromMap(nbUnresolved, stId));
                pstIns.setInt(6, getIntFromMap(nbClosed, stId));
                pstIns.setInt(7, getIntFromMap(nbUpdated, stId));
                pstIns.setInt(8, getIntFromMap(nbResolved, stId));
                pstIns.setInt(9, getIntFromMap(nbStatusChange, stId));
                pstIns.setInt(10, getIntFromMap(nbCategoryChange, stId));
                pstIns.setInt(11, getIntFromMap(nbIpsuChange, stId));
                pstIns.setInt(12, getIntFromMap(nbAssigneeChange, stId));
                pstIns.setInt(13, getIntFromMap(nbSupportLevelChange, stId));
                pstIns.setLong(14, getLongFromMap(minTimeToClose, stId));
                pstIns.setLong(15, getLongFromMap(maxTimeToClose, stId));
                pstIns.setLong(16, getLongFromMap(avgTimeToClose, stId));
                pstIns.setLong(17, getLongFromMap(minTimeToResolve, stId));
                pstIns.setLong(18, getLongFromMap(maxTimeToResolve, stId));
                pstIns.setLong(19, getLongFromMap(avgTimeToResolve, stId));
                pstIns.setLong(20, getLongFromMap(minOpenTime, stId));
                pstIns.setLong(21, getLongFromMap(maxOpenTime, stId));
                pstIns.setLong(22, getLongFromMap(avgOpenTime, stId));
                pstIns.setInt(23, getIntFromMap(nbExisting, stId));
                pstIns.setInt(24, getIntFromMap(nbSlaViolations, stId));
                pstIns.setInt(25, stId.typeId);
                db.executeUpdate(pstIns);
            }
        } finally {
            db.exit();
        }
    }

    private void computeTicketDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing ticket daily");
            PreparedStatement pstDel = db.prepareStatement("DELETE FROM itsm.t_ticket_daily WHERE calc_day = ?");
            pstDel.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstDel);
            int nbExisting;
            int nbOpened;
            int nbUnresolved;
            int nbNew;
            int nbClosed;
            int nbResolved;
            int nbUpdated;
            int nbStatusChange;
            int nbCategoryChange;
            int nbIpsuChange;
            int nbAssigneeChange;
            int nbSupportLevelChange;
            int nbSlaViolations;
            long minTimeToClose, maxTimeToClose, avgTimeToClose;
            long minTimeToResolve, maxTimeToResolve, avgTimeToResolve;
            long minOpenTime, maxOpenTime, avgOpenTime;
            Date nextDate = DateHelper.rollForwards(effective_date);
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*) FROM itsm.t_ticket WHERE open_date <= ? ");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                nbExisting = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*) FROM itsm.t_ticket WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?)");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                nbOpened = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*) FROM itsm.t_ticket WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) AND (resolve_date IS NULL OR resolve_date > ?)");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                pstSel.setDate(3, DateHelper.sqld(effective_date));
                nbUnresolved = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*) FROM itsm.t_ticket WHERE open_date >= ? AND open_date < ?");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbNew = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*) FROM itsm.t_ticket WHERE close_date >= ? AND close_date < ?");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbClosed = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*) FROM itsm.t_ticket WHERE resolve_date >= ? AND resolve_date < ?");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbResolved = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t_ticket_id) FROM itsm.t_ticket_history WHERE change_date >= ? AND change_date < ? ");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbUpdated = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t_ticket_id) FROM itsm.t_ticket_history WHERE change_date >= ? AND change_date < ? AND field = 'status'");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbStatusChange = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t_ticket_id) FROM itsm.t_ticket_history WHERE change_date >= ? AND change_date < ? AND field = 'category'");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbCategoryChange = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t_ticket_id) FROM itsm.t_ticket_history WHERE change_date >= ? AND change_date < ? AND (field = 'impact' OR field = 'priority' OR field = 'severity' OR field = 'urgency')");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbIpsuChange = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t_ticket_id) FROM itsm.t_ticket_history WHERE change_date >= ? AND change_date < ? AND field = 'assignee'");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbAssigneeChange = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t_ticket_id) FROM itsm.t_ticket_history WHERE change_date >= ? AND change_date < ? AND field = 'support_lev'");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbSupportLevelChange = DbHelper.getIntKey(pstSel);
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(close_date - open_date)), extract('epoch' from MAX(close_date - open_date)), extract('epoch' from AVG(close_date - open_date)) FROM itsm.t_ticket WHERE close_date >= ? AND close_date < ? AND close_date > open_date");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    minTimeToClose = rs.getLong(1);
                    maxTimeToClose = rs.getLong(2);
                    avgTimeToClose = rs.getLong(3);
                } else {
                    _logger.warn("Could not get min/max/avg time to close. No data?");
                    minTimeToClose = 0;
                    maxTimeToClose = 0;
                    avgTimeToClose = 0;
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(resolve_date - open_date)), extract('epoch' from MAX(resolve_date - open_date)), extract('epoch' from AVG(resolve_date - open_date)) FROM itsm.t_ticket WHERE resolve_date >= ? AND resolve_date < ? AND resolve_date > open_date");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    minTimeToResolve = rs.getLong(1);
                    maxTimeToResolve = rs.getLong(2);
                    avgTimeToResolve = rs.getLong(3);
                } else {
                    _logger.warn("Could not get min/max/avg time to close. No data?");
                    minTimeToResolve = 0;
                    maxTimeToResolve = 0;
                    avgTimeToResolve = 0;
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(? - open_date)), extract('epoch' from MAX(? - open_date)), extract('epoch' from AVG(? - open_date)) FROM itsm.t_ticket WHERE open_date <= ? AND (close_date > ? OR close_date IS NULL)");
                pstSel.setDate(1, DateHelper.sqld(nextDate));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                pstSel.setDate(3, DateHelper.sqld(nextDate));
                pstSel.setDate(4, DateHelper.sqld(effective_date));
                pstSel.setDate(5, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    minOpenTime = rs.getLong(1);
                    maxOpenTime = rs.getLong(2);
                    avgOpenTime = rs.getLong(3);
                } else {
                    _logger.warn("Could not get min/max/avg open time. No data?");
                    minOpenTime = 0;
                    maxOpenTime = 0;
                    avgOpenTime = 0;
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t_ticket_id) FROM itsm.t_ticket_history WHERE change_date >= ? AND change_date < ? AND sla_violation");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                nbSlaViolations = DbHelper.getIntKey(pstSel);
            }
            PreparedStatement pstIns = db.prepareStatement("INSERT INTO itsm.t_ticket_daily (calc_day, count_new, count_opened, count_unresolved, count_closed, count_updated, count_resolved, count_status_changed, count_ipsu_changed, count_category_changed, count_assignee_changed, count_supp_lvl_changed, min_time_to_close, max_time_to_close, avg_time_to_close, min_time_to_resolve, max_time_to_resolve, avg_time_to_resolve, min_open_time, max_open_time, avg_open_time, count_existing, count_with_sla_violation) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstIns.setDate(1, DateHelper.sqld(effective_date));
            pstIns.setInt(2, nbNew);
            pstIns.setInt(3, nbOpened);
            pstIns.setInt(4, nbUnresolved);
            pstIns.setInt(5, nbClosed);
            pstIns.setInt(6, nbUpdated);
            pstIns.setInt(7, nbResolved);
            pstIns.setInt(8, nbStatusChange);
            pstIns.setInt(9, nbIpsuChange);
            pstIns.setInt(10, nbCategoryChange);
            pstIns.setInt(11, nbAssigneeChange);
            pstIns.setInt(12, nbSupportLevelChange);
            pstIns.setLong(13, minTimeToClose);
            pstIns.setLong(14, maxTimeToClose);
            pstIns.setLong(15, avgTimeToClose);
            pstIns.setLong(16, minTimeToResolve);
            pstIns.setLong(17, maxTimeToResolve);
            pstIns.setLong(18, avgTimeToResolve);
            pstIns.setLong(19, minOpenTime);
            pstIns.setLong(20, maxOpenTime);
            pstIns.setLong(21, avgOpenTime);
            pstIns.setInt(22, nbExisting);
            pstIns.setInt(23, nbSlaViolations);
            db.executeUpdate(pstIns);
        } finally {
            db.exit();
        }
    }

    private void computeTicketTypeDaily(Db db) throws Exception {
        try {
            db.enter();
            _logger.info("Computing ticket type daily");
            PreparedStatement pstDel = db.prepareStatement("DELETE FROM itsm.t_ticket_type_daily WHERE calc_day = ?");
            pstDel.setDate(1, DateHelper.sqld(effective_date));
            db.executeUpdate(pstDel);
            PreparedStatement pstIns = db.prepareStatement("INSERT INTO itsm.t_ticket_type_daily (calc_day, count_new, count_opened, count_unresolved, count_closed, count_updated, count_resolved, count_status_changed, count_ipsu_changed, count_category_changed, count_assignee_changed, count_supp_lvl_changed, min_time_to_close, max_time_to_close, avg_time_to_close, min_time_to_resolve, max_time_to_resolve, avg_time_to_resolve, min_open_time, max_open_time, avg_open_time, count_existing, count_with_sla_violation, t_ticket_type_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            Set<Integer> typeIds = new HashSet<Integer>();
            Map<Integer, Integer> nbExisting = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbOpened = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbUnresolved = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbNew = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbClosed = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbResolved = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbUpdated = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbStatusChange = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbCategoryChange = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbIpsuChange = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbAssigneeChange = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbSupportLevelChange = new HashMap<Integer, Integer>();
            Map<Integer, Integer> nbSlaViolations = new HashMap<Integer, Integer>();
            Map<Integer, Long> minTimeToClose = new HashMap<Integer, Long>();
            Map<Integer, Long> maxTimeToClose = new HashMap<Integer, Long>();
            Map<Integer, Long> avgTimeToClose = new HashMap<Integer, Long>();
            Map<Integer, Long> minTimeToResolve = new HashMap<Integer, Long>();
            Map<Integer, Long> maxTimeToResolve = new HashMap<Integer, Long>();
            Map<Integer, Long> avgTimeToResolve = new HashMap<Integer, Long>();
            Map<Integer, Long> minOpenTime = new HashMap<Integer, Long>();
            Map<Integer, Long> maxOpenTime = new HashMap<Integer, Long>();
            Map<Integer, Long> avgOpenTime = new HashMap<Integer, Long>();
            Date nextDate = DateHelper.rollForwards(effective_date);
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbExisting.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbOpened.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date IS NULL OR close_date > ?) AND (resolve_date IS NULL OR resolve_date > ?) GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(effective_date));
                pstSel.setDate(3, DateHelper.sqld(effective_date));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbUnresolved.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date >= ? AND open_date < ? GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbNew.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE close_date >= ? AND close_date < ? GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbClosed.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(*), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE resolve_date >= ? AND resolve_date < ? GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbResolved.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history h INNER JOIN itsm.t_ticket t ON t.t_ticket_id = h.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbUpdated.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history h INNER JOIN itsm.t_ticket t ON t.t_ticket_id = h.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'status' GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbStatusChange.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history h INNER JOIN itsm.t_ticket t ON t.t_ticket_id = h.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'category' GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbCategoryChange.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history h INNER JOIN itsm.t_ticket t ON t.t_ticket_id = h.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND (field = 'impact' OR field = 'priority' OR field = 'severity' OR field = 'urgency') GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbIpsuChange.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history h INNER JOIN itsm.t_ticket t ON t.t_ticket_id = h.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'assignee' GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbAssigneeChange.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history h INNER JOIN itsm.t_ticket t ON t.t_ticket_id = h.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND field = 'support_lev' GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbSupportLevelChange.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(close_date - open_date)), extract('epoch' from MAX(close_date - open_date)), extract('epoch' from AVG(close_date - open_date)), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE close_date >= ? AND close_date < ? AND close_date > open_date GROUP BY 4");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        int typeId = rs.getInt(4);
                        minTimeToClose.put(typeId, rs.getLong(1));
                        maxTimeToClose.put(typeId, rs.getLong(2));
                        avgTimeToClose.put(typeId, rs.getLong(3));
                        typeIds.add(typeId);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(resolve_date - open_date)), extract('epoch' from MAX(resolve_date - open_date)), extract('epoch' from AVG(resolve_date - open_date)), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE resolve_date >= ? AND resolve_date < ? AND resolve_date > open_date GROUP BY 4");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        int typeId = rs.getInt(4);
                        minTimeToResolve.put(typeId, rs.getLong(1));
                        maxTimeToResolve.put(typeId, rs.getLong(2));
                        avgTimeToResolve.put(typeId, rs.getLong(3));
                        typeIds.add(typeId);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT extract('epoch' from MIN(? - open_date)), extract('epoch' from MAX(? - open_date)), extract('epoch' from AVG(? - open_date)), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket t INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE open_date <= ? AND (close_date > ? OR close_date IS NULL) GROUP BY 4");
                pstSel.setDate(1, DateHelper.sqld(nextDate));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                pstSel.setDate(3, DateHelper.sqld(nextDate));
                pstSel.setDate(4, DateHelper.sqld(effective_date));
                pstSel.setDate(5, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        int typeId = rs.getInt(4);
                        minOpenTime.put(typeId, rs.getLong(1));
                        maxOpenTime.put(typeId, rs.getLong(2));
                        avgOpenTime.put(typeId, rs.getLong(3));
                        typeIds.add(typeId);
                    } while (rs.next());
                }
            }
            {
                PreparedStatement pstSel = db.prepareStatement("SELECT COUNT(DISTINCT t.t_ticket_id), COALESCE(tt.normalized_type_id, tt.t_type_id) FROM itsm.t_ticket_history h INNER JOIN itsm.t_ticket t ON t.t_ticket_id = h.t_ticket_id INNER JOIN itsm.t_type tt ON tt.t_type_id = t.t_type_id WHERE change_date >= ? AND change_date < ? AND sla_violation GROUP BY 2");
                pstSel.setDate(1, DateHelper.sqld(effective_date));
                pstSel.setDate(2, DateHelper.sqld(nextDate));
                ResultSet rs = db.executeQuery(pstSel);
                if (rs.next()) {
                    do {
                        nbSlaViolations.put(rs.getInt(2), rs.getInt(1));
                        typeIds.add(rs.getInt(2));
                    } while (rs.next());
                }
            }
            for (Iterator<Integer> it = typeIds.iterator(); it.hasNext(); ) {
                int typeId = it.next();
                pstIns.setDate(1, DateHelper.sqld(effective_date));
                pstIns.setInt(2, getIntFromIntMap(nbNew, typeId));
                pstIns.setInt(3, getIntFromIntMap(nbOpened, typeId));
                pstIns.setInt(4, getIntFromIntMap(nbUnresolved, typeId));
                pstIns.setInt(5, getIntFromIntMap(nbClosed, typeId));
                pstIns.setInt(6, getIntFromIntMap(nbUpdated, typeId));
                pstIns.setInt(7, getIntFromIntMap(nbResolved, typeId));
                pstIns.setInt(8, getIntFromIntMap(nbStatusChange, typeId));
                pstIns.setInt(9, getIntFromIntMap(nbIpsuChange, typeId));
                pstIns.setInt(10, getIntFromIntMap(nbCategoryChange, typeId));
                pstIns.setInt(11, getIntFromIntMap(nbAssigneeChange, typeId));
                pstIns.setInt(12, getIntFromIntMap(nbSupportLevelChange, typeId));
                pstIns.setLong(13, getLongFromIntMap(minTimeToClose, typeId));
                pstIns.setLong(14, getLongFromIntMap(maxTimeToClose, typeId));
                pstIns.setLong(15, getLongFromIntMap(avgTimeToClose, typeId));
                pstIns.setLong(16, getLongFromIntMap(minTimeToResolve, typeId));
                pstIns.setLong(17, getLongFromIntMap(maxTimeToResolve, typeId));
                pstIns.setLong(18, getLongFromIntMap(avgTimeToResolve, typeId));
                pstIns.setLong(19, getLongFromIntMap(minOpenTime, typeId));
                pstIns.setLong(20, getLongFromIntMap(maxOpenTime, typeId));
                pstIns.setLong(21, getLongFromIntMap(avgOpenTime, typeId));
                pstIns.setInt(22, getIntFromIntMap(nbUpdated, typeId));
                pstIns.setInt(23, getIntFromIntMap(nbUpdated, typeId));
                pstIns.setInt(24, typeId);
                db.executeUpdate(pstIns);
            }
        } finally {
            db.exit();
        }
    }
}
