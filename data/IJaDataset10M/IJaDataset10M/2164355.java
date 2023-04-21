package ch.ethz.dcg.spamato.filter;

import java.util.*;
import ch.ethz.dcg.plugin.PluginContext;
import ch.ethz.dcg.spamato.base.common.filter.SpamFilter;
import ch.ethz.dcg.spamato.base.common.stats.FilterStatistics;
import ch.ethz.dcg.spamato.factory.common.main.*;
import ch.ethz.dcg.spamato.webconfig.*;

public class FilterPageHandler extends AbstractPageHandler {

    private HashSet<String> started = new HashSet<String>();

    private HashSet<String> stopped = new HashSet<String>();

    private SpamatoFactory spamatoFactory;

    public FilterPageHandler(PluginContext pluginContext, SpamatoFactory spamatoFactory) {
        super(pluginContext);
        this.spamatoFactory = spamatoFactory;
    }

    public void renderPage(String page, Hashtable<String, String> parameters, HtmlWriter writer) {
        writer.startPanel("Filter Statistics");
        writer.writeLn("<p>This overview provides an insight into the effectiveness of filters.</p>");
        writer.writeLn("<p>You can enable/disable filters by (un)checking the check boxes in front of each filter name.");
        writer.writeLn(" <b>Don't forget to save your settings by clicking the 'Save' button below (or 'Cancel' to undo your changes).</b></p>");
        writer.writeLn("\n\n<table cellspacing='0' width='100%'>");
        Spamato spamato = this.spamatoFactory.getInstance();
        SpamFilter[] filters = (SpamFilter[]) spamato.getFilters().toArray(new SpamFilter[0]);
        Arrays.sort(filters, new Comparator<SpamFilter>() {

            public int compare(SpamFilter f1, SpamFilter f2) {
                try {
                    String name1 = f1.getName();
                    String name2 = f2.getName();
                    return name1.compareTo(name2);
                } catch (Exception e) {
                    return 0;
                }
            }
        });
        for (int i = 0; i < filters.length; i++) {
            SpamFilter filter = (SpamFilter) filters[i];
            FilterStatistics stats = FilterStatistics.getInstance();
            int spam = stats.getSpam(filter);
            int ok = stats.getHam(filter);
            int unknown = stats.getUnknown(filter);
            int unchecked = stats.getChecked() - stats.getChecked(filter);
            unchecked = (unchecked > 0 ? unchecked : 0);
            String filterHTML = "<b>" + filter.getName().replaceAll("\\s", "&nbsp;") + "</b>";
            filterHTML += "<br>(" + "<span style=\"color:rgb(160,0,0);\">" + spam + "</span>" + "/" + "<span style=\"color:rgb(0,160,0);\">" + ok + "</span>" + "/" + "<span style=\"color:rgb(64,64,64);\">" + unknown + "</span>" + "/" + "<span style=\"color:rgb(160,160,160);\">" + unchecked + "</span>" + ")";
            writer.writeLn("<tr>");
            boolean started = spamato.isFilterEnabled(filter.getID()) && !this.stopped.contains(filter.getID()) || this.started.contains(filter.getID());
            String actionUrl = !started ? "?action=start&key=" + filter.getID() : "?action=stop&key=" + filter.getID();
            writer.write("\t<td valign=\"top\">");
            writer.addCheckBox("start_" + filter.getID(), started, false, actionUrl);
            writer.writeLn("</td>");
            writer.write("\t<td valign=\"top\">" + "<b>(" + (i + 1) + ")</b>&nbsp;" + "</td>\n");
            writer.write("\t<td valign=\"top\">" + filterHTML + "</td>\n");
            if (i == 0) {
                writer.write("\t<td valign=\"middle\" rowspan=\"" + filters.length + "\">");
                writer.addImage("The Statistics of currently installed Filters", "/images/filter_stats1.jpg");
                writer.addBr();
                writer.addImage("The Statistics of currently installed Filters", "/images/filter_stats2.jpg");
                writer.addBr();
                writer.addImage("The Statistics of currently installed Filters", "/images/filter_stats3.jpg");
                writer.write("</td>\n");
            }
            writer.write("</tr>\n");
        }
        int spam = FilterStatistics.getInstance().getSpam();
        int ok = FilterStatistics.getInstance().getHam();
        int unknown = FilterStatistics.getInstance().getUnknown();
        int reported = FilterStatistics.getInstance().getReported();
        int revoked = FilterStatistics.getInstance().getRevoked();
        writer.writeLn("\t<td valign=\"top\">&nbsp;</td>");
        writer.writeLn("\t<td valign=\"top\">&nbsp;</td>");
        writer.writeLn("\t<td valign=\"top\" colspan=2><b>Overall</b>");
        writer.writeLn("\t<br>&nbsp;&nbsp;Total number of messages checked: " + (spam + ok + unknown));
        writer.writeLn("\t<br>&nbsp;&nbsp;Detected spam messages: " + "<span style=\"color:rgb(160,0,0);\">" + spam + "</span>");
        writer.writeLn("\t<br>&nbsp;&nbsp;Detected ham messages: " + "<span style=\"color:rgb(0,160,0);\">" + ok + "</span>");
        writer.writeLn("\t<br>&nbsp;&nbsp;Unknown messages: " + "<span style=\"color:rgb(64,64,64);\">" + unknown + "</span>");
        writer.writeLn("\t<br>&nbsp;&nbsp;Reported messages: " + reported);
        writer.writeLn("\t<br>&nbsp;&nbsp;Revoked messages: " + revoked);
        writer.writeLn("</td>");
        writer.writeLn("</table>");
        writer.endPanel();
    }

    public String executeAction(String name, Hashtable<String, String> parameters) {
        String key = (String) parameters.get("key");
        if ("start".equals(name)) {
            this.started.add(key);
            this.stopped.remove(key);
        } else if ("stop".equals(name)) {
            this.stopped.add(key);
            this.started.remove(key);
        } else {
            throw new RuntimeException("action '" + name + "' not found.");
        }
        return "";
    }

    public void abort() {
        this.started.clear();
        this.stopped.clear();
    }

    public void save() {
        Spamato spamato = this.spamatoFactory.getInstance();
        for (String key : started) {
            spamato.enableFilter(key);
        }
        for (String key : stopped) {
            spamato.disableFilter(key);
        }
    }
}
