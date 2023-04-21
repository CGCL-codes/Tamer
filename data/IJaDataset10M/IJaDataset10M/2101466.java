package plugins.scanner;

import java.io.File;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import com.umc.beans.media.MediaFactory;
import com.umc.collector.Publisher;
import com.umc.plugins.scanner.IPluginScanner;

/**
 * This plugin can scan following directory structures for episodes:
 * 
 * 
 * Call:
 * sp.searchFiles(new File("Z:\\Serien"), "", "");
 * 
 * @author DonGyros
 *
 */
public class CrazySeriesScanner implements IPluginScanner {

    private static Logger log = Logger.getLogger("com.umc.plugins.scanner");

    private String seasonPattern = "^\\d{2}$";

    private String episodePattern = "^(\\[[\\dS-]+\\])\\s-\\s(.*).(" + Publisher.getInstance().getParamScannerMoviefiles() + ")";

    private Pattern episode = Pattern.compile("\\[([\\dS-]+)\\] - (.*)\\.(.{2,4})");

    public CrazySeriesScanner() {
    }

    public SCANNERTYPE getType() {
        return SCANNERTYPE.SERIES;
    }

    public String getName() {
        return "Heho's crazy series scanner";
    }

    public String getDescription() {
        return "With this scanner it is possible to scan for episodes in crazy directory structures dfgdsgsdfgsdf";
    }

    public String getAuthor() {
        return "heho";
    }

    public double getVersion() {
        return 0.1;
    }

    public void searchFiles(File dir, boolean recursive) {
        searchFiles2(dir, "", "");
    }

    private void searchFiles2(File dir, String name, String season) {
        Matcher m = null;
        int epnr;
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                if (files[i].getName().matches(seasonPattern)) {
                    season = files[i].getName();
                } else {
                    name = files[i].getName();
                }
                searchFiles2(files[i], name, season);
            } else {
                if (files[i].getName().matches(episodePattern)) {
                    int ses = season.equals("") ? 1 : Integer.parseInt(season);
                    m = episode.matcher(files[i].getName());
                    m.find();
                    if (m.group(1).contains("-")) {
                        StringTokenizer st = new StringTokenizer(m.group(1), "-");
                        while (st.hasMoreTokens()) {
                            epnr = Integer.valueOf(st.nextToken());
                            log.info(files[i].getName() + "   Serie: [" + name + "] Seaoson [" + ses + "] episode [" + epnr + "] Name [" + m.group(2) + "]");
                            MediaFactory.createEpisodeBean(files[i], name, null, ses, epnr);
                        }
                    } else if (m.group(1).contains("S")) {
                        epnr = Integer.valueOf(m.group(1).replace("S", ""));
                        log.info("Serie: [" + name + "] Seaoson [" + ses + "] episode [" + epnr + "] Special Name [" + m.group(2) + "]");
                    } else {
                        epnr = Integer.valueOf(m.group(1));
                        log.info(files[i].getName() + "   Serie: [" + name + "] Seaoson [" + ses + "] episode [" + epnr + "] Name [" + m.group(2) + "]");
                        MediaFactory.createEpisodeBean(files[i], name, null, ses, epnr);
                    }
                } else {
                    log.warn("!!INVALID!!" + files[i]);
                }
            }
        }
    }

    public void setSeasonPattern(String seasonPattern) {
        this.seasonPattern = seasonPattern;
    }

    public void setEpisodePattern(String episodePattern) {
        this.episodePattern = episodePattern;
    }

    public void setEpisode(Pattern episode) {
        this.episode = episode;
    }
}
