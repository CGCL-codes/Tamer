package net.sourceforge.compete.logic;

import net.sourceforge.compete.data.Competition;
import net.sourceforge.compete.data.Result;
import net.sourceforge.compete.data.Stage;
import net.sourceforge.compete.data.Team;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Copyright (c) 2010-2011 Rodica Balasa (rodiq@rodiq.ro).
 *
 * See the LICENSE file for terms of use.
 *
 */
public class TriathlonCalculator implements Calculator {

    private static Logger log = Logger.getLogger(TriathlonCalculator.class);

    public Double computeReferenceTotal(Stage stage, Session dbSession) {
        return stage.getNotFinishedDefaultPenalty();
    }

    public void recomputeResults(Result result, Session dbSession) {
        long time1 = System.currentTimeMillis();
        Double referenceTotal = computeReferenceTotal(result.getStage(), dbSession);
        if (!(result.getFinished().booleanValue() && result.getStart() != null && result.getFinish() != null)) {
            result.setStageTotal(referenceTotal + computePenalties(result));
            dbSession.update(result);
        }
        String sql = "select r from Result r where r.team.id = :teamId " + " and r.id != :resultId " + " and r.stage.parent.id = :parentId ";
        Query query = dbSession.createQuery(sql);
        query.setParameter("teamId", result.getTeam().getId());
        query.setParameter("resultId", result.getId());
        query.setParameter("parentId", result.getStage().getParent().getId());
        List teamResults = query.list();
        boolean finishedParentStage = result.getFinished().booleanValue();
        double teamTotal = result.getStageTotal().doubleValue();
        for (int i = 0; i < teamResults.size(); i++) {
            Result crtResult = (Result) teamResults.get(i);
            if (crtResult.getFinished().booleanValue()) {
                if (crtResult.getStageTotal() == null) throw new RuntimeException("Result " + crtResult.getId() + " is marked as finished but has no stage total!");
                teamTotal = teamTotal + crtResult.getStageTotal().doubleValue();
            } else {
                teamTotal = teamTotal + crtResult.getStage().getNotFinishedDefaultPenalty().doubleValue();
            }
        }
        result.setParentTotal(teamTotal);
        result.setGrandTotal(teamTotal);
        dbSession.update(result);
        for (int i = 0; i < teamResults.size(); i++) {
            Result crtResult = (Result) teamResults.get(i);
            crtResult.setParentTotal(teamTotal);
            crtResult.setGrandTotal(teamTotal);
            dbSession.update(crtResult);
            if (!crtResult.getFinished().booleanValue()) finishedParentStage = false;
        }
        long time2 = System.currentTimeMillis();
        log.info("recomputeResults in " + (time2 - time1) + " millies ");
    }

    private double computePenalties(Result result) {
        double cp = result.getCpValue();
        double penalties = result.getPenalty().doubleValue() + (result.getStage().getCp().doubleValue() - cp) * result.getStage().getCompetition().getCpValue().doubleValue();
        return penalties;
    }

    private void recomputeTotals(Team team, Stage parentStage, Session dbSession) {
        if (parentStage != null) {
            double parentTotal = getParentTotal(team, parentStage, dbSession);
            double grandTotal = getGrandTotal(team, parentStage, dbSession);
            String sql = " from net.sourceforge.compete.data.Result result " + " where result.stage.parent.id = :parentStageId " + " and result.team.id = :teamId ";
            log.info("sql: " + sql);
            Query query = dbSession.createQuery(sql);
            query.setParameter("parentStageId", parentStage.getId());
            query.setParameter("teamId", team.getId());
            List results = query.list();
            for (int i = 0; i < results.size(); i++) {
                Result result = (Result) results.get(i);
                result.setParentTotal(new Double(parentTotal));
                result.setGrandTotal(new Double(grandTotal));
                dbSession.saveOrUpdate(result);
            }
        }
    }

    public double getParentTotal(Team team, Stage parentStage, Session dbSession) {
        long time1 = System.currentTimeMillis();
        String sql = " select sum(result.stageTotal) " + " from net.sourceforge.compete.data.Result result " + " where result.stage.parent.id = :parentId " + " and result.team.id = :teamId ";
        Query query = dbSession.createQuery(sql);
        query.setParameter("parentId", parentStage.getId());
        query.setParameter("teamId", team.getId());
        Double parentTotal = (Double) query.list().iterator().next();
        if (parentTotal != null) {
            long time2 = System.currentTimeMillis();
            log.debug("parent total in " + (time2 - time1) + "millies");
            return (long) parentTotal.doubleValue();
        } else {
            log.error("unable to compute parent total for team id " + team.getId() + " and parentStage id " + parentStage.getId());
        }
        return 0;
    }

    public double getGrandTotal(Team team, Stage parentStage, Session dbSession) {
        long time1 = System.currentTimeMillis();
        String sql = " select sum(result.stageTotal) " + " from net.sourceforge.compete.data.Result result " + " where (result.stage.parent.id = :parentId or" + "   result.stage.code < :stageCode) " + " and result.team.id = :teamId ";
        Query query = dbSession.createQuery(sql);
        query.setParameter("parentId", parentStage.getId());
        query.setParameter("teamId", team.getId());
        query.setParameter("stageCode", parentStage.getCode());
        Double grandTotal = (Double) query.list().iterator().next();
        if (grandTotal != null) {
            long time2 = System.currentTimeMillis();
            log.debug("grand total in " + (time2 - time1) + "millies");
            return (long) grandTotal.doubleValue();
        } else {
            log.warn("unable to compute grand total for parent stage id " + parentStage.getId() + " teamId " + team.getId());
        }
        return 0;
    }

    public double computeTotal(Result result) {
        double points = getPoints(result);
        double total = points + computePenalties(result);
        total = Math.round(total);
        return total;
    }

    public double getPoints(Result result) {
        long totalSeconds = 0;
        totalSeconds = getTotalSeconds(result);
        return (totalSeconds * result.getStage().getCoeficient().doubleValue());
    }

    public long getTotalSeconds(Result result) {
        long totalSeconds;
        totalSeconds = computeSecondsDifference(result.getStart(), result.getFinish());
        if (result.getStopTimeStart() != null && result.getStopTimeFinish() != null) {
            long stopTimeSeconds = computeSecondsDifference(result.getStopTimeStart(), result.getStopTimeFinish());
            totalSeconds = totalSeconds - stopTimeSeconds;
        }
        return totalSeconds;
    }

    public long getTotalMinutes(Result result) {
        return (long) (getTotalSeconds(result) / 60);
    }

    public static long computeSecondsDifference(Time startTime, Time finishTime, boolean extraDay) {
        long totalSeconds = computeSecondsDifference(startTime, finishTime);
        if (extraDay) totalSeconds = totalSeconds + 24 * 60 * 60;
        return totalSeconds;
    }

    public static long computeSecondsDifference(Time startTime, Time finishTime) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(startTime);
        long startTimeSeconds = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 + calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);
        calendar.setTime(finishTime);
        long finishTimeSeconds = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 + calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);
        if (finishTimeSeconds < startTimeSeconds) {
            finishTimeSeconds = finishTimeSeconds + 24 * 60 * 60;
        }
        long totalSeconds = finishTimeSeconds - startTimeSeconds;
        return totalSeconds;
    }

    public String getPrettyTotal(Double total) {
        if (total != null) {
            long hours = total.longValue() / 3600;
            long minutes = (total.longValue() - 3600 * hours) / 60;
            long seconds = total.longValue() - 3600 * hours - 60 * minutes;
            return format(hours) + ":" + format(minutes) + ":" + format(seconds);
        } else return "";
    }

    private String format(long value) {
        if (value < 10) {
            return "0" + value;
        } else {
            return "" + value;
        }
    }

    public void computeCompetitionTotals(Long competitionId, Session dbSession) {
        String sql = "from net.sourceforge.compete.data.Team  team " + " where team.competition.id = :competitionId";
        Query query = dbSession.createQuery(sql);
        query.setParameter("competitionId", competitionId);
        List teams = query.list();
        for (int i = 0; i < teams.size(); i++) {
            Team team = (Team) teams.get(i);
            String teamSql = "from net.sourceforge.compete.data.Result result " + " where result.team.id = :teamId ";
            double teamTotal = 0;
            List teamResults = dbSession.createQuery(teamSql).setParameter("teamId", team.getId()).list();
            for (int j = 0; j < teamResults.size(); j++) {
                Result result = (Result) teamResults.get(j);
                if (!result.getFinished().booleanValue()) {
                    teamTotal = teamTotal + result.getStage().getNotFinishedDefaultPenalty().doubleValue();
                } else {
                    if (result.getStageTotal() == null) {
                        throw new RuntimeException("result with id " + result.getId() + " has no stage total!");
                    }
                    teamTotal = teamTotal + result.getStageTotal().doubleValue();
                }
            }
            for (int j = 0; j < teamResults.size(); j++) {
                Result result = (Result) teamResults.get(j);
                result.setParentTotal(new Double(teamTotal));
                result.setGrandTotal(new Double(teamTotal));
                dbSession.update(result);
            }
        }
    }
}
