package se.sics.tasim.tac04.is;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.logging.Logger;
import se.sics.isl.html.HtmlUtils;
import se.sics.isl.html.HtmlWriter;
import se.sics.isl.util.FormatUtils;
import se.sics.tasim.is.common.InfoServer;
import se.sics.tasim.is.common.ResultManager;
import se.sics.tasim.logtool.LogReader;
import se.sics.tasim.logtool.ParticipantInfo;
import se.sics.tasim.props.SCMInfo;
import se.sics.tasim.tac04.Participant;
import se.sics.tasim.tac04.TAC04Constants;
import se.sics.tasim.tac04.TAC04SimulationInfo;

public class TAC04ResultManager extends ResultManager implements TAC04Constants {

    private static final Logger log = Logger.getLogger(TAC04ResultManager.class.getName());

    private static final String POSITIVE_PARTICIPANT_COLOR = "#0000c0";

    private static final String NEUTRAL_PARTICIPANT_COLOR = null;

    private static final String NEGATIVE_PARTICIPANT_COLOR = "#c00000";

    public TAC04ResultManager() {
    }

    protected void generateResult() throws IOException {
        TAC04SimulationInfo simInfo;
        LogReader reader = getLogReader();
        int simulationID = reader.getSimulationID();
        String serverName = reader.getServerName();
        reader.setContext(SCMInfo.createContext());
        try {
            simInfo = new TAC04SimulationInfo(reader);
        } catch (Exception e) {
            throw (IOException) new IOException("could not parse simulation log " + simulationID).initCause(e);
        }
        String destinationFile = getDestinationPath() + "index.html";
        log.info("generating results for simulation " + simulationID + " to " + destinationFile);
        HtmlWriter html = new HtmlWriter(new FileWriter(destinationFile));
        Participant[] participants = simInfo.getParticipantsByRole(MANUFACTURER);
        if (participants != null) {
            participants = (Participant[]) participants.clone();
            Arrays.sort(participants, Participant.getResultComparator());
        }
        html.pageStart("Results for game " + simulationID + '@' + serverName);
        html.h3("Result for game " + simulationID + '@' + serverName + " played at " + InfoServer.getServerTimeAsString(reader.getStartTime()));
        html.table("border=1").colgroup(1).colgroup(9, "align=right").tr().th("Player", "rowspan=2").th("Revenue", "align=center rowspan=2").th("Interest", "align=center rowspan=2").th("Costs", "align=center colspan=4").th("Margin&nbsp;1", "align=center rowspan=2").th("Margin&nbsp;2", "align=center rowspan=2").th("Result", "align=center rowspan=2").tr().th("Material", "align=center").th("Storage", "align=center").th("Penalty", "align=center colspan=2");
        ParticipantInfo[] agentInfos = null;
        String[] agentColors = null;
        long[] agentScores = null;
        if (participants != null) {
            agentInfos = new ParticipantInfo[participants.length];
            agentColors = new String[participants.length];
            agentScores = new long[participants.length];
            for (int i = 0, n = participants.length; i < n; i++) {
                Participant player = participants[i];
                ParticipantInfo agentInfo = player.getInfo();
                String name = agentInfo.getName();
                long cost = player.getCost();
                long storageCost = player.getStorageCost();
                long penalties = player.getPenalties();
                long revenue = player.getRevenue();
                long interest = player.getInterest();
                long result = player.getResult();
                agentInfos[i] = agentInfo;
                if (result < 0) {
                    agentColors[i] = NEGATIVE_PARTICIPANT_COLOR;
                } else if (result > 0) {
                    agentColors[i] = POSITIVE_PARTICIPANT_COLOR;
                } else {
                    agentColors[i] = NEUTRAL_PARTICIPANT_COLOR;
                }
                agentScores[i] = result;
                html.tr().td(agentInfo.isBuiltinAgent() ? "<em>" + name + "</em>" : name).td(getAmountAsString(revenue)).td(getAmountAsString(interest)).td(getAmountAsString(cost)).td(getAmountAsString(storageCost)).td(getAmountAsString(penalties)).td(cost > 0 ? "" + (int) (100d * penalties / (cost + storageCost + penalties) + 0.5) : (penalties > 0 ? "100" : "0")).text('%');
                if (revenue > 0) {
                    html.td("" + (int) (100d * (revenue - cost - storageCost) / revenue + 0.5)).text('%').td("" + (int) (100d * (revenue + interest - cost - storageCost - penalties) / revenue + 0.5)).text('%');
                } else {
                    html.td("0%").td("0%");
                }
                html.td();
                formatAmount(html, result);
            }
        }
        html.tableEnd();
        html.text("Download game data ").tag('a').attr("href", getGameLogName()).text("here").tagEnd('a').p();
        html.table("border=1").colgroup(1).colgroup(2, "align=right").colgroup(2).colgroup(1, "align=right").tr().th("Player").th("Orders", "align=center").th("Utilization", "align=center").th("Deliveries (on&nbsp;time/late/missed)", "colspan=2").th("DPerf", "align=center");
        if (participants != null) {
            for (int i = 0, n = participants.length; i < n; i++) {
                Participant player = participants[i];
                ParticipantInfo agentInfo = player.getInfo();
                String name = agentInfo.getName();
                int orders = player.getCustomerOrders();
                int deliveries = player.getCustomerDeliveries();
                html.tr().td(agentInfo.isBuiltinAgent() ? "<em>" + name + "</em>" : name).td("" + orders).td("" + player.getAverageUtilization()).text('%').td();
                if (orders > 0) {
                    int lateDeliveries = player.getCustomerLateDeliveries();
                    int missedDeliveries = player.getCustomerMissedDeliveries();
                    int onTime = (int) (100d * (deliveries - lateDeliveries) / orders + 0.5);
                    int late = (int) (100d * lateDeliveries / orders + 0.5);
                    int missed = (int) (100d * missedDeliveries / orders + 0.5);
                    HtmlUtils.progress(html, 100, 8, onTime, late, missed);
                    html.td().text(deliveries - lateDeliveries).text("&nbsp;/&nbsp;").text(lateDeliveries).text("&nbsp;/&nbsp;").text(missedDeliveries);
                    html.td("" + onTime + '%');
                } else {
                    html.text("&nbsp;").td("&nbsp;").td("0%");
                }
            }
        }
        html.tableEnd();
        html.p().tag("hr").tag("font", "size=-1").tag("em").text("<b>Margin&nbsp;1</b> is the margin excluding bank interest and penalties while <b>Margin&nbsp;2</b> includes bank interest and penalties.<br>" + "<b>DPerf</b> is the delivery performance.").tagEnd("em").tagEnd("font");
        html.pageEnd();
        html.close();
        addSimulationToHistory(agentInfos, agentColors);
        addSimulationResult(agentInfos, agentScores);
    }

    private String getAmountAsString(long amount) {
        return FormatUtils.formatLong(amount, "&nbsp;");
    }

    private void formatAmount(HtmlWriter html, long amount) {
        if (amount < 0) {
            html.tag("font", "color=red").text(getAmountAsString(amount)).tagEnd("font");
        } else {
            html.text(getAmountAsString(amount));
        }
    }

    public static void main(String args[]) throws IOException, ParseException {
        TAC04ResultManager rman = new TAC04ResultManager();
        LogReader reader = new LogReader(new FileInputStream(args[0]));
        rman.generateResult(reader, args[1]);
    }
}
