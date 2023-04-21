package com.hack23.cia.service.impl.common;

import gnu.trove.THashMap;
import gnu.trove.THashSet;
import java.awt.Color;
import java.awt.GradientPaint;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hack23.cia.model.application.impl.common.Agency;
import com.hack23.cia.model.application.impl.common.ImageContent;
import com.hack23.cia.model.application.impl.common.Language;
import com.hack23.cia.model.application.impl.common.LanguageContent;
import com.hack23.cia.model.application.impl.common.ParliamentChart;
import com.hack23.cia.model.sweden.impl.Ballot;
import com.hack23.cia.model.sweden.impl.ParliamentMember;
import com.hack23.cia.model.sweden.impl.PartyBallotResult;
import com.hack23.cia.model.sweden.impl.PoliticalParty;
import com.hack23.cia.model.sweden.impl.Vote;
import com.hack23.cia.model.sweden.impl.Vote.Position;
import com.hack23.cia.service.api.common.ChartService;

/**
 * The Class BallotChartServiceImpl.
 */
public class BallotChartServiceImpl implements ChartService {

    /** The logger. */
    private final Logger logger = LoggerFactory.getLogger(BallotChartServiceImpl.class);

    /**
	 * Creates the bar3 d chart.
	 * 
	 * @param dataset
	 *            the dataset
	 * @param title
	 *            the title
	 * @param xTitle
	 *            the x title
	 * @param yTitle
	 *            the y title
	 * @param fileName
	 *            the file name
	 * 
	 * @return the image content
	 */
    private ImageContent createBar3DChart(final CategoryDataset dataset, final String title, final String xTitle, final String yTitle, final String fileName) {
        JFreeChart chart = ChartFactory.createBarChart3D(title, xTitle, yTitle, dataset, PlotOrientation.VERTICAL, true, true, false);
        chart.setBackgroundPaint(new GradientPaint(0, 0, Color.white, 0, 1000, Color.blue));
        try {
            ByteArrayOutputStream file = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsPNG(file, chart, 800, 600);
            ImageContent imageContent = new ImageContent();
            imageContent.setFileName(fileName);
            imageContent.setMimetype("image/png");
            imageContent.setImageContent(file.toByteArray());
            return imageContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public final ImageContent createRecentActionsGraph(final List actionEventHistory) {
        String title = "Actions during 24 hours";
        String timeAxisLabel = "Hours";
        String valueAxisLabel = "Actions";
        TimeSeries totalByHourData = new TimeSeries("Total");
        Day day = new Day(new Date());
        for (Object o : actionEventHistory) {
            Integer i = (Integer) ((Object[]) o)[0];
            Long total = (Long) ((Object[]) o)[1];
            totalByHourData.add(new Hour(i, day), total);
        }
        TimeSeriesCollection actionsByHourDataset = new TimeSeriesCollection(totalByHourData);
        return createTimeSeriesChart(title, timeAxisLabel, valueAxisLabel, "ActionsByHour.jpg", actionsByHourDataset);
    }

    @SuppressWarnings("unchecked")
    public final ImageContent createResponseTimeGraph(final List responseTimeHistory) {
        String title = "Response Time during 24 hours";
        String timeAxisLabel = "Hours";
        String valueAxisLabel = "Response Time ms";
        TimeSeries avgResponseTimeByHourData = new TimeSeries("Average");
        TimeSeries minResponseTimeByHourData = new TimeSeries("Minimum");
        TimeSeries maxResponseTimeByHourData = new TimeSeries("Maximum");
        Day day = new Day(new Date());
        for (Object o : responseTimeHistory) {
            Integer i = (Integer) ((Object[]) o)[0];
            Double avg = (Double) ((Object[]) o)[1];
            Long min = (Long) ((Object[]) o)[2];
            Long max = (Long) ((Object[]) o)[3];
            avgResponseTimeByHourData.add(new Hour(i, day), avg);
            minResponseTimeByHourData.add(new Hour(i, day), min);
            maxResponseTimeByHourData.add(new Hour(i, day), max);
        }
        TimeSeriesCollection responseTimeByHourDataset = new TimeSeriesCollection(avgResponseTimeByHourData);
        responseTimeByHourDataset.addSeries(maxResponseTimeByHourData);
        responseTimeByHourDataset.addSeries(minResponseTimeByHourData);
        return createTimeSeriesChart(title, timeAxisLabel, valueAxisLabel, "responseTimeByHour.jpg", responseTimeByHourDataset);
    }

    /**
	 * Creates the time series chart.
	 * 
	 * @param title
	 *            the title
	 * @param timeAxisLabel
	 *            the time axis label
	 * @param valueAxisLabel
	 *            the value axis label
	 * @param fileName
	 *            the file name
	 * @param responseTimeByHourDataset
	 *            the response time by hour dataset
	 * 
	 * @return the image content
	 */
    private ImageContent createTimeSeriesChart(final String title, final String timeAxisLabel, final String valueAxisLabel, final String fileName, final TimeSeriesCollection responseTimeByHourDataset) {
        boolean legend = true;
        boolean tooltips = true;
        boolean urls = false;
        JFreeChart createTimeSeriesChart = ChartFactory.createTimeSeriesChart(title, timeAxisLabel, valueAxisLabel, responseTimeByHourDataset, legend, tooltips, urls);
        createTimeSeriesChart.setBackgroundPaint(new GradientPaint(0, 0, Color.white, 0, 1000, Color.blue));
        try {
            ByteArrayOutputStream file = new ByteArrayOutputStream();
            ChartUtilities.writeChartAsPNG(file, createTimeSeriesChart, 800, 600);
            ImageContent imageContent = new ImageContent();
            imageContent.setFileName(fileName);
            imageContent.setMimetype("image/png");
            imageContent.setImageContent(file.toByteArray());
            return imageContent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    @SuppressWarnings("unused")
    public final List<ImageContent> generateBallotCharts(final Agency agency, final Ballot ballot, final List<PoliticalParty> politicalParties, final Language language) {
        CategoryDataset dataset = new DefaultCategoryDataset();
        ArrayList<ImageContent> imageContents = new ArrayList<ImageContent>();
        LanguageContent titleOutCome = agency.getLanguageContentByKey(Agency.LanguageContentKey.WINNING_PERCENTAGE_BY_PARTY, language);
        LanguageContent valueAxisLabelOutCome = agency.getLanguageContentByKey(Agency.LanguageContentKey.WINNING_PERCENTAGE, language);
        LanguageContent titlePresent = agency.getLanguageContentByKey(Agency.LanguageContentKey.PRESENT_PERCENTAGE_BY_PARTY, language);
        LanguageContent valueAxisLabelPresent = agency.getLanguageContentByKey(Agency.LanguageContentKey.PRESENCE_PERCENTAGE, language);
        LanguageContent titleLoyal = agency.getLanguageContentByKey(Agency.LanguageContentKey.LOYAL_PERCENTAGE_BY_PARTY, language);
        LanguageContent valueAxisLabelLoyal = agency.getLanguageContentByKey(Agency.LanguageContentKey.LOYAL_PERCENT, language);
        LanguageContent timeAxisLabel = agency.getLanguageContentByKey(Agency.LanguageContentKey.BALLOTS, language);
        Map<PoliticalParty, TimeSeries> partyTimeSeriesWinMap = new THashMap<PoliticalParty, TimeSeries>();
        Map<PoliticalParty, TimeSeries> partyTimeSeriesPresentMap = new THashMap<PoliticalParty, TimeSeries>();
        Map<PoliticalParty, TimeSeries> partyTimeSeriesLoyalMap = new THashMap<PoliticalParty, TimeSeries>();
        TimeSeriesCollection winningPartyOverTimeSeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection presentPartyOverTimeSeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection loyalPartyOverTimeSeriesCollection = new TimeSeriesCollection();
        for (PoliticalParty politicalParty : politicalParties) {
            TimeSeries winTimeSeries = new TimeSeries(politicalParty.getName());
            winningPartyOverTimeSeriesCollection.addSeries(winTimeSeries);
            partyTimeSeriesWinMap.put(politicalParty, winTimeSeries);
            TimeSeries presentTimeSeries = new TimeSeries(politicalParty.getName());
            presentPartyOverTimeSeriesCollection.addSeries(presentTimeSeries);
            partyTimeSeriesPresentMap.put(politicalParty, presentTimeSeries);
            TimeSeries loyalTimeSeries = new TimeSeries(politicalParty.getName());
            loyalPartyOverTimeSeriesCollection.addSeries(loyalTimeSeries);
            partyTimeSeriesLoyalMap.put(politicalParty, loyalTimeSeries);
        }
        for (PoliticalParty politicalParty : politicalParties) {
            PartyBallotResult partyBallotResult = ballot.getPartyBallotResult(politicalParty.getShortCode());
        }
        imageContents.add(createBar3DChart(dataset, titleOutCome.getContent(), valueAxisLabelOutCome.getContent(), valueAxisLabelOutCome.getContent(), ParliamentChart.getFileName(ParliamentChart.Summary, ballot, language)));
        imageContents.add(createBar3DChart(dataset, titleOutCome.getContent(), valueAxisLabelOutCome.getContent(), valueAxisLabelOutCome.getContent(), ParliamentChart.getFileName(ParliamentChart.Outcome, ballot, language)));
        imageContents.add(createBar3DChart(dataset, titleOutCome.getContent(), valueAxisLabelOutCome.getContent(), valueAxisLabelOutCome.getContent(), ParliamentChart.getFileName(ParliamentChart.PoliticalPartyBehavior, ballot, language)));
        imageContents.add(createBar3DChart(dataset, titleOutCome.getContent(), valueAxisLabelOutCome.getContent(), valueAxisLabelOutCome.getContent(), ParliamentChart.getFileName(ParliamentChart.ProffessionalBehavior, ballot, language)));
        return imageContents;
    }

    @Override
    public final List<ImageContent> generateCommitteeReportCharts(final Agency agency, final List<Ballot> allBallots, final List<PoliticalParty> allPoliticalParties, final Language language) {
        return new ArrayList<ImageContent>();
    }

    public final List<ImageContent> generateParliamentCharts(final Agency agency, final List<Ballot> ballots, final List<PoliticalParty> politicalParties, final Language language) {
        LanguageContent titleOutCome = agency.getLanguageContentByKey(Agency.LanguageContentKey.WINNING_PERCENTAGE_BY_PARTY, language);
        LanguageContent valueAxisLabelOutCome = agency.getLanguageContentByKey(Agency.LanguageContentKey.WINNING_PERCENTAGE, language);
        LanguageContent titlePresent = agency.getLanguageContentByKey(Agency.LanguageContentKey.PRESENT_PERCENTAGE_BY_PARTY, language);
        LanguageContent valueAxisLabelPresent = agency.getLanguageContentByKey(Agency.LanguageContentKey.PRESENCE_PERCENTAGE, language);
        LanguageContent titleLoyal = agency.getLanguageContentByKey(Agency.LanguageContentKey.LOYAL_PERCENTAGE_BY_PARTY, language);
        LanguageContent valueAxisLabelLoyal = agency.getLanguageContentByKey(Agency.LanguageContentKey.LOYAL_PERCENT, language);
        LanguageContent timeAxisLabel = agency.getLanguageContentByKey(Agency.LanguageContentKey.BALLOTS, language);
        List<ImageContent> imageContents = new ArrayList<ImageContent>();
        Map<PoliticalParty, TimeSeries> partyTimeSeriesWinMap = new THashMap<PoliticalParty, TimeSeries>();
        Map<PoliticalParty, TimeSeries> partyTimeSeriesPresentMap = new THashMap<PoliticalParty, TimeSeries>();
        Map<PoliticalParty, TimeSeries> partyTimeSeriesLoyalMap = new THashMap<PoliticalParty, TimeSeries>();
        Map<String, Long> partyDayWinMap = new THashMap<String, Long>();
        Map<String, Long> partyDayPresentMap = new THashMap<String, Long>();
        Map<String, Long> partyDayLoyalMap = new THashMap<String, Long>();
        Map<Day, Long> dayBallotsTotalMap = new THashMap<Day, Long>();
        TimeSeriesCollection winningPartyOverTimeSeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection presentPartyOverTimeSeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection loyalPartyOverTimeSeriesCollection = new TimeSeriesCollection();
        for (PoliticalParty politicalParty : politicalParties) {
            TimeSeries winTimeSeries = new TimeSeries(politicalParty.getName());
            winningPartyOverTimeSeriesCollection.addSeries(winTimeSeries);
            partyTimeSeriesWinMap.put(politicalParty, winTimeSeries);
            TimeSeries presentTimeSeries = new TimeSeries(politicalParty.getName());
            presentPartyOverTimeSeriesCollection.addSeries(presentTimeSeries);
            partyTimeSeriesPresentMap.put(politicalParty, presentTimeSeries);
            TimeSeries loyalTimeSeries = new TimeSeries(politicalParty.getName());
            loyalPartyOverTimeSeriesCollection.addSeries(loyalTimeSeries);
            partyTimeSeriesLoyalMap.put(politicalParty, loyalTimeSeries);
        }
        Set<Day> days = new THashSet<Day>();
        for (Ballot ballot : ballots) {
            Day day = new Day(ballot.getDatum());
            days.add(day);
            Long dayBallotsTotal = dayBallotsTotalMap.get(day);
            if (dayBallotsTotal == null) {
                dayBallotsTotal = 0L;
            }
            dayBallotsTotal++;
            dayBallotsTotalMap.put(day, dayBallotsTotal);
            for (PoliticalParty politicalParty : politicalParties) {
                Long dayPartyPartyWin = partyDayWinMap.get(day + politicalParty.getShortCode());
                Long dayPartyPresent = partyDayPresentMap.get(day + politicalParty.getShortCode());
                Long dayPartyLoyal = partyDayLoyalMap.get(day + politicalParty.getShortCode());
                PartyBallotResult partyBallotResult = ballot.getPartyBallotResult(politicalParty.getShortCode());
                if (dayPartyPartyWin == null) {
                    dayPartyPartyWin = 0L;
                    dayPartyPresent = 0L;
                    dayPartyLoyal = 0L;
                }
                dayPartyPresent = dayPartyPresent + ((partyBallotResult.getTotalVotes() - partyBallotResult.getAbsentVotes()) * 100L / partyBallotResult.getTotalVotes());
                dayPartyLoyal = dayPartyLoyal + ((partyBallotResult.getTotalVotes() - partyBallotResult.getOpponentVotes()) * 100L / partyBallotResult.getTotalVotes());
                if (partyBallotResult.getWinningPosition().equals(ballot.getBallotResult().getWinningPosition())) {
                    dayPartyPartyWin = dayPartyPartyWin + (partyBallotResult.getWinningVotes() * 100 / partyBallotResult.getTotalVotes());
                } else {
                    dayPartyPartyWin = dayPartyPartyWin + (partyBallotResult.getOpponentVotes() * 100 / partyBallotResult.getTotalVotes());
                }
                partyDayWinMap.put(day + politicalParty.getShortCode(), dayPartyPartyWin);
                partyDayPresentMap.put(day + politicalParty.getShortCode(), dayPartyPresent);
                partyDayLoyalMap.put(day + politicalParty.getShortCode(), dayPartyLoyal);
            }
        }
        for (Day day : days) {
            Long dayBallotsTotal = dayBallotsTotalMap.get(day);
            for (PoliticalParty politicalParty : politicalParties) {
                TimeSeries timeSeries = partyTimeSeriesWinMap.get(politicalParty);
                Long dayPartyWin = partyDayWinMap.get(day + politicalParty.getShortCode());
                timeSeries.add(day, dayPartyWin / dayBallotsTotal);
                timeSeries = partyTimeSeriesPresentMap.get(politicalParty);
                Long dayPartyPresent = partyDayPresentMap.get(day + politicalParty.getShortCode());
                timeSeries.add(day, dayPartyPresent / dayBallotsTotal);
                timeSeries = partyTimeSeriesLoyalMap.get(politicalParty);
                Long dayPartyLoyal = partyDayLoyalMap.get(day + politicalParty.getShortCode());
                timeSeries.add(day, dayPartyLoyal / dayBallotsTotal);
            }
        }
        imageContents.add(createTimeSeriesChart(titleOutCome.getContent(), timeAxisLabel.getContent(), valueAxisLabelOutCome.getContent(), ParliamentChart.getFileName(ParliamentChart.Outcome, language), winningPartyOverTimeSeriesCollection));
        imageContents.add(createTimeSeriesChart(titlePresent.getContent(), timeAxisLabel.getContent(), valueAxisLabelPresent.getContent(), ParliamentChart.getFileName(ParliamentChart.ProffessionalBehavior, language), presentPartyOverTimeSeriesCollection));
        imageContents.add(createTimeSeriesChart(titleLoyal.getContent(), timeAxisLabel.getContent(), valueAxisLabelLoyal.getContent(), ParliamentChart.getFileName(ParliamentChart.PoliticalPartyBehavior, language), loyalPartyOverTimeSeriesCollection));
        return imageContents;
    }

    @Override
    public final List<ImageContent> generateParliamentMemberCharts(final Agency agency, final List<Ballot> ballots, final ParliamentMember parliamentMember, final List<PoliticalParty> politicalParties, final Language language) {
        LanguageContent titleOutCome = agency.getLanguageContentByKey(Agency.LanguageContentKey.OUTCOME, language);
        LanguageContent valueAxisLabelOutCome = agency.getLanguageContentByKey(Agency.LanguageContentKey.WINNING_PERCENTAGE, language);
        LanguageContent titlePresent = agency.getLanguageContentByKey(Agency.LanguageContentKey.PROFFESSIONAL_BEHAVIOR, language);
        LanguageContent valueAxisLabelPresent = agency.getLanguageContentByKey(Agency.LanguageContentKey.PRESENCE_PERCENTAGE, language);
        LanguageContent titleLoyal = agency.getLanguageContentByKey(Agency.LanguageContentKey.POLITICAL_PARTY_BEHAVIOR, language);
        LanguageContent valueAxisLabelLoyal = agency.getLanguageContentByKey(Agency.LanguageContentKey.LOYAL_PERCENT, language);
        LanguageContent timeAxisLabel = agency.getLanguageContentByKey(Agency.LanguageContentKey.BALLOTS, language);
        List<ImageContent> imageContents = new ArrayList<ImageContent>();
        TimeSeries winTimeSeries = new TimeSeries(titleOutCome.getContent());
        TimeSeries presentTimeSeries = new TimeSeries(titlePresent.getContent());
        TimeSeries loyalTimeSeries = new TimeSeries(titleLoyal.getContent());
        TimeSeriesCollection winningOverTimeSeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection presentOverTimeSeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection loyalOverTimeSeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection summaryOverTimeSeriesCollection = new TimeSeriesCollection();
        winningOverTimeSeriesCollection.addSeries(winTimeSeries);
        presentOverTimeSeriesCollection.addSeries(presentTimeSeries);
        loyalOverTimeSeriesCollection.addSeries(loyalTimeSeries);
        summaryOverTimeSeriesCollection.addSeries(winTimeSeries);
        summaryOverTimeSeriesCollection.addSeries(presentTimeSeries);
        summaryOverTimeSeriesCollection.addSeries(loyalTimeSeries);
        Set<Day> days = new THashSet<Day>();
        Map<Day, Long> dayBallotsTotalMap = new THashMap<Day, Long>();
        Map<Day, Long> dayWinMap = new THashMap<Day, Long>();
        Map<Day, Long> dayPresentMap = new THashMap<Day, Long>();
        Map<Day, Long> dayLoyalMap = new THashMap<Day, Long>();
        for (Ballot ballot : ballots) {
            Day day = new Day(ballot.getDatum());
            days.add(day);
            Long dayBallotsTotal = dayBallotsTotalMap.get(day);
            Long dayWin = dayWinMap.get(day);
            Long dayPresent = dayPresentMap.get(day);
            Long dayLoyal = dayLoyalMap.get(day);
            if (dayBallotsTotal == null) {
                dayBallotsTotal = 0L;
                dayWin = 0L;
                dayPresent = 0L;
                dayLoyal = 0L;
            }
            PartyBallotResult partyBallotResult = ballot.getPartyBallotResult(parliamentMember.getPoliticalParty().getShortCode());
            Vote voteForParliamentMember = ballot.getVoteForParliamentMember(parliamentMember);
            if (voteForParliamentMember != null) {
                if (voteForParliamentMember.isWinning()) {
                    dayWin++;
                }
                if (!voteForParliamentMember.getPosition().equals(Position.Absent)) {
                    dayPresent++;
                }
                if (!partyBallotResult.isRebelVote(voteForParliamentMember)) {
                    dayLoyal++;
                }
                dayBallotsTotal++;
            }
            dayBallotsTotalMap.put(day, dayBallotsTotal);
            dayWinMap.put(day, dayWin);
            dayPresentMap.put(day, dayPresent);
            dayLoyalMap.put(day, dayLoyal);
        }
        for (Day day : days) {
            Long dayBallotsTotal = dayBallotsTotalMap.get(day);
            if (dayBallotsTotal != 0) {
                Long dayWin = dayWinMap.get(day);
                winTimeSeries.add(day, dayWin * 100 / dayBallotsTotal);
                Long dayPresent = dayPresentMap.get(day);
                presentTimeSeries.add(day, dayPresent * 100 / dayBallotsTotal);
                Long dayLoyal = dayLoyalMap.get(day);
                loyalTimeSeries.add(day, dayLoyal * 100 / dayBallotsTotal);
            }
        }
        logger.info("Generate chart for " + parliamentMember.getName());
        imageContents.add(createTimeSeriesChart(titleOutCome.getContent() + " " + parliamentMember.getName(), timeAxisLabel.getContent(), valueAxisLabelOutCome.getContent(), ParliamentChart.getFileName(ParliamentChart.Outcome, parliamentMember, language), winningOverTimeSeriesCollection));
        imageContents.add(createTimeSeriesChart(titlePresent.getContent() + " " + parliamentMember.getName(), timeAxisLabel.getContent(), valueAxisLabelPresent.getContent(), ParliamentChart.getFileName(ParliamentChart.ProffessionalBehavior, parliamentMember, language), presentOverTimeSeriesCollection));
        imageContents.add(createTimeSeriesChart(titleLoyal.getContent() + " " + parliamentMember.getName(), timeAxisLabel.getContent(), valueAxisLabelLoyal.getContent(), ParliamentChart.getFileName(ParliamentChart.PoliticalPartyBehavior, parliamentMember, language), loyalOverTimeSeriesCollection));
        imageContents.add(createTimeSeriesChart(parliamentMember.getName(), timeAxisLabel.getContent(), "%", ParliamentChart.getFileName(ParliamentChart.Summary, parliamentMember, language), summaryOverTimeSeriesCollection));
        return imageContents;
    }

    @Override
    public final List<ImageContent> generatePoliticalPartyCharts(final Agency agency, final List<Ballot> ballots, final PoliticalParty politicalParty, final Language language) {
        LanguageContent titleOutCome = agency.getLanguageContentByKey(Agency.LanguageContentKey.OUTCOME, language);
        LanguageContent valueAxisLabelOutCome = agency.getLanguageContentByKey(Agency.LanguageContentKey.WINNING_PERCENTAGE, language);
        LanguageContent titlePresent = agency.getLanguageContentByKey(Agency.LanguageContentKey.PROFFESSIONAL_BEHAVIOR, language);
        LanguageContent valueAxisLabelPresent = agency.getLanguageContentByKey(Agency.LanguageContentKey.PRESENCE_PERCENTAGE, language);
        LanguageContent titleLoyal = agency.getLanguageContentByKey(Agency.LanguageContentKey.POLITICAL_PARTY_BEHAVIOR, language);
        LanguageContent valueAxisLabelLoyal = agency.getLanguageContentByKey(Agency.LanguageContentKey.LOYAL_PERCENT, language);
        LanguageContent timeAxisLabel = agency.getLanguageContentByKey(Agency.LanguageContentKey.BALLOTS, language);
        List<ImageContent> imageContents = new ArrayList<ImageContent>();
        TimeSeries winTimeSeries = new TimeSeries(titleOutCome.getContent());
        TimeSeries presentTimeSeries = new TimeSeries(titlePresent.getContent());
        TimeSeries loyalTimeSeries = new TimeSeries(titleLoyal.getContent());
        TimeSeriesCollection winningOverTimeSeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection presentOverTimeSeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection loyalOverTimeSeriesCollection = new TimeSeriesCollection();
        TimeSeriesCollection summaryOverTimeSeriesCollection = new TimeSeriesCollection();
        winningOverTimeSeriesCollection.addSeries(winTimeSeries);
        presentOverTimeSeriesCollection.addSeries(presentTimeSeries);
        loyalOverTimeSeriesCollection.addSeries(loyalTimeSeries);
        summaryOverTimeSeriesCollection.addSeries(winTimeSeries);
        summaryOverTimeSeriesCollection.addSeries(presentTimeSeries);
        summaryOverTimeSeriesCollection.addSeries(loyalTimeSeries);
        Set<Day> days = new THashSet<Day>();
        Map<Day, Long> dayBallotsTotalMap = new THashMap<Day, Long>();
        Map<Day, Long> dayWinMap = new THashMap<Day, Long>();
        Map<Day, Long> dayPresentMap = new THashMap<Day, Long>();
        Map<Day, Long> dayLoyalMap = new THashMap<Day, Long>();
        for (Ballot ballot : ballots) {
            Day day = new Day(ballot.getDatum());
            days.add(day);
            Long dayBallotsTotal = dayBallotsTotalMap.get(day);
            Long dayWin = dayWinMap.get(day);
            Long dayPresent = dayPresentMap.get(day);
            Long dayLoyal = dayLoyalMap.get(day);
            if (dayBallotsTotal == null) {
                dayBallotsTotal = 0L;
                dayWin = 0L;
                dayPresent = 0L;
                dayLoyal = 0L;
            }
            PartyBallotResult partyBallotResult = ballot.getPartyBallotResult(politicalParty.getShortCode());
            if (partyBallotResult != null) {
                dayWin = dayWin + (partyBallotResult.getWinningVotes() * 100L / partyBallotResult.getTotalVotes());
                dayPresent = dayPresent + ((partyBallotResult.getTotalVotes() - partyBallotResult.getAbsentVotes()) * 100L / partyBallotResult.getTotalVotes());
                dayLoyal = dayLoyal + ((partyBallotResult.getTotalVotes() - partyBallotResult.getOpponentVotes()) * 100L / partyBallotResult.getTotalVotes());
                dayBallotsTotal++;
            }
            dayBallotsTotalMap.put(day, dayBallotsTotal);
            dayWinMap.put(day, dayWin);
            dayPresentMap.put(day, dayPresent);
            dayLoyalMap.put(day, dayLoyal);
        }
        for (Day day : days) {
            Long dayBallotsTotal = dayBallotsTotalMap.get(day);
            if (dayBallotsTotal != 0) {
                Long dayWin = dayWinMap.get(day);
                winTimeSeries.add(day, dayWin / dayBallotsTotal);
                Long dayPresent = dayPresentMap.get(day);
                presentTimeSeries.add(day, dayPresent / dayBallotsTotal);
                Long dayLoyal = dayLoyalMap.get(day);
                loyalTimeSeries.add(day, dayLoyal / dayBallotsTotal);
            }
        }
        logger.info("Generate chart for " + politicalParty.getName());
        imageContents.add(createTimeSeriesChart(titleOutCome.getContent() + " " + politicalParty.getName(), timeAxisLabel.getContent(), valueAxisLabelOutCome.getContent(), ParliamentChart.getFileName(ParliamentChart.Outcome, politicalParty, language), winningOverTimeSeriesCollection));
        imageContents.add(createTimeSeriesChart(titlePresent.getContent() + " " + politicalParty.getName(), timeAxisLabel.getContent(), valueAxisLabelPresent.getContent(), ParliamentChart.getFileName(ParliamentChart.ProffessionalBehavior, politicalParty, language), presentOverTimeSeriesCollection));
        imageContents.add(createTimeSeriesChart(titleLoyal.getContent() + " " + politicalParty.getName(), timeAxisLabel.getContent(), valueAxisLabelLoyal.getContent(), ParliamentChart.getFileName(ParliamentChart.PoliticalPartyBehavior, politicalParty, language), loyalOverTimeSeriesCollection));
        imageContents.add(createTimeSeriesChart(politicalParty.getName(), timeAxisLabel.getContent(), "%", ParliamentChart.getFileName(ParliamentChart.Summary, politicalParty, language), summaryOverTimeSeriesCollection));
        return imageContents;
    }
}
