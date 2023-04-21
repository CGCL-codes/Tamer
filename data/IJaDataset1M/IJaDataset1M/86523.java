package net.sf.gham.core.control.download.task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import net.sf.gham.Gham;
import net.sf.gham.core.control.download.Calendario;
import net.sf.gham.core.control.download.DownloadInfo;
import net.sf.gham.core.control.download.DownloadInterruptedException;
import net.sf.gham.core.control.download.DownloadTask;
import net.sf.gham.core.control.download.MatchDownloader;
import net.sf.gham.core.entity.team.myteam.MyTeam;
import net.sf.gham.core.entity.team.otherteam.Team;
import net.sf.gham.core.util.CastUtils;
import net.sf.gham.core.util.HattrickWeek;
import net.sf.gham.core.util.constants.Directory;
import net.sf.gham.swing.util.XMLUtils;
import net.sf.jtwa.Messages;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author Fabio Collini
 */
public class DownloadMyMatchesTask extends DownloadTask {

    private boolean youth;

    private String matchesPage;

    @Override
    public void download(DownloadInfo info) throws java.io.IOException, DownloadInterruptedException {
        List<String> matchList = new LinkedList<String>();
        checkDir(Directory.MATCHES);
        checkDir(Directory.MATCHES_YOUTH);
        InputStream is = info.getConnection().getPage(matchesPage + "&isYouth=" + youth);
        String fileName = info.getTeamDir() + "matches" + (youth ? "_youth" : "") + ".xml";
        XMLUtils.writeToXML(is, fileName);
        is = new BufferedInputStream(new FileInputStream(fileName));
        Team team = null;
        MyTeam myTeam = Gham.main.getTeam();
        if (youth) {
            if (myTeam != null) {
                team = myTeam.getYouthTeam();
            }
        } else {
            team = myTeam;
        }
        if (team != null && team.getLastMatchWeek() != null) {
            parseMatchesArchive(info, matchList, is, true, team.getLastMatchWeek(), youth);
        } else {
            DownloadMatchesTask.parseMatchesArchive(info, matchList, is, true, info.getNumberOfMatches(), youth);
        }
        MatchDownloader.downloadMatches(info, matchList, youth);
    }

    private void checkDir(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public int getNumberOfTask(DownloadInfo info) {
        return info.getNumberOfMatches();
    }

    /**
     * Parses the matchesArchive.xml file to select the requested matches IDs
     * @param vec Vector to return
     * @param is InputStream matchesArchive.xml stream
     * @param youth2
     * @param numMatches int number of the matches
     * @param matchType int type of the matches
     * @param teamID int id of the team (used only when parsing matchesArchive)
     * @return Vector containing the matched IDs
     */
    private static void parseMatchesArchive(DownloadInfo info, List<String> vec, InputStream is, boolean firstTime, HattrickWeek lastMatchWeek, boolean youth) throws IOException {
        try {
            SAXBuilder builder = new SAXBuilder(false);
            Document doc = builder.build(is);
            Element root = doc.getRootElement();
            Element team = root.getChild("Team");
            Element matchList = team.getChild("MatchList");
            List<Element> matches = CastUtils.getChildren(matchList, "Match");
            ListIterator<Element> iter = matches.listIterator(matches.size());
            Element match = null;
            boolean exit = false;
            while (iter.hasPrevious() && !exit) {
                match = iter.previous();
                String status = match.getChildText("Status");
                HattrickWeek date = new HattrickWeek(match.getChildText("MatchDate"), true);
                if (lastMatchWeek.compareTo(date) >= 0) {
                    exit = true;
                } else if (status == null || status.equals("FINISHED")) {
                    int intType = Integer.parseInt(match.getChildText("MatchType"));
                    if (info.isMatchCompatible(intType)) {
                        if (!vec.contains(match.getChildText("MatchID"))) {
                            vec.add(match.getChildText("MatchID"));
                        }
                    }
                }
            }
            if (!exit) {
                String date = match.getChildText("MatchDate");
                String lastDate;
                if (date.indexOf(' ') != -1) {
                    lastDate = date.substring(0, date.indexOf(' '));
                } else {
                    lastDate = date;
                }
                String firstDate = Calendario.getMesePrecedente(lastDate);
                firstDate = Calendario.getMesePrecedente(firstDate);
                String page = "chppxml.axd?file=matchesarchive" + (!youth ? "&teamID=" + info.getId() : "") + (!firstTime ? "&FirstMatchDate=" + firstDate + "&LastMatchDate=" + lastDate : "") + "&isYouth=" + youth;
                is = info.getConnection().getPage(page);
                parseMatchesArchive(info, vec, is, false, lastMatchWeek, youth);
            }
        } catch (JDOMException jde) {
            throw new IOException(jde.getMessage());
        }
    }

    @Override
    public String getName() {
        return Messages.getString("Download_matches");
    }

    public String getMatchesPage() {
        return matchesPage;
    }

    public void setMatchesPage(String matchesPage) {
        this.matchesPage = matchesPage;
    }

    public boolean isYouth() {
        return youth;
    }

    public void setYouth(boolean youth) {
        this.youth = youth;
    }
}
