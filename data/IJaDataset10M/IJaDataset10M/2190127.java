package net.sourceforge.pebble.event.response;

import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.domain.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks comment and TrackBack content for specified regexs and sets
 * the state of such responses to pending. This includes all user-definable
 * fields of the response : title, content, source name and source link. The
 * spam score is incremented for each field that exceeds the threshold. 
 *
 * @author Simon Brown
 */
public class ContentSpamListener extends BlogEntryResponseListenerSupport {

    /** the log used by this class */
    private static final Log log = LogFactory.getLog(ContentSpamListener.class);

    /** the default regex list */
    public static final String DEFAULT_REGEX_LIST = "cialis, viagra, poker, casino, xanax, holdem, hold-em, hold em, sex, craps, fuck, shit, teenage, phentermine, blackjack, roulette, gambling, pharmacy, carisoprodol, pills, penis, penis enlargement, anal, hentai, anime, vicodin, massage, nude, ejaculation, porn, gay, naked, girl, teens, babe, masturbating, squirt, incest, fetish, discount, cheap, interesdting, levitra, government, grants, loan, &\\#.*;, kasino, slots, play, bingo, mortgage, baccarat";

    /** the default threshold for the number of content matches */
    public static final int DEFAULT_THRESHOLD = 0;

    /** the name of the regex list property */
    public static final String REGEX_LIST_KEY = "ContentSpamListener.regexList";

    /** the name of the threshold property */
    public static final String THRESHOLD_KEY = "ContentSpamListener.threshold";

    /**
   * Called when a comment or TrackBack has been added.
   *
   * @param response a Response
   */
    protected void blogEntryResponseAdded(Response response) {
        PluginProperties props = response.getBlogEntry().getBlog().getPluginProperties();
        String regexList = props.getProperty(REGEX_LIST_KEY);
        String regexes[] = null;
        if (regexList != null) {
            regexes = regexList.split(",");
        } else {
            regexes = new String[0];
        }
        int threshold = DEFAULT_THRESHOLD;
        if (props.hasProperty(THRESHOLD_KEY)) {
            try {
                threshold = Integer.parseInt(props.getProperty(THRESHOLD_KEY));
            } catch (NumberFormatException nfe) {
                log.error(nfe.getMessage());
            }
        }
        if (!contentWithinThreshold(response.getTitle(), regexes, threshold)) {
            log.info(response.getTitle() + " marked as pending : threshold for title exceeded");
            response.setPending();
            response.incrementSpamScore();
        }
        if (!contentWithinThreshold(response.getSourceName(), regexes, threshold)) {
            log.info(response.getTitle() + " marked as pending : threshold for source name exceeded");
            response.setPending();
            response.incrementSpamScore();
        }
        if (!contentWithinThreshold(response.getSourceLink(), regexes, threshold)) {
            log.info(response.getTitle() + " marked as pending : threshold for source link exceeded");
            response.setPending();
            response.incrementSpamScore();
        }
        if (!contentWithinThreshold(response.getContent(), regexes, threshold)) {
            log.info(response.getTitle() + " marked as pending : threshold for content exceeded");
            response.setPending();
            response.incrementSpamScore();
        }
    }

    private boolean contentWithinThreshold(String content, String regexes[], int threshold) {
        if (content == null || content.trim().length() == 0) {
            return true;
        }
        int count = 0;
        for (int i = 0; i < regexes.length; i++) {
            Pattern p = Pattern.compile(regexes[i].trim(), Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
            Matcher m = p.matcher(content);
            while (m.find()) {
                count++;
                if (count > threshold) {
                    return false;
                }
            }
        }
        return true;
    }
}
