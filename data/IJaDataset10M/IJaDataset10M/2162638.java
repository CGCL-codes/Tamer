package net.villonanny.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.villonanny.ConversationException;
import net.villonanny.EventLog;
import net.villonanny.TimeWhenRunnable;
import net.villonanny.Translator;
import net.villonanny.Util;
import net.villonanny.type.ResourceType;
import net.villonanny.type.ResourceTypeMap;
import org.apache.log4j.Logger;

public class MarketSite extends Building {

    private static final Logger log = Logger.getLogger(MarketSite.class);

    private int merchantsFree, merchantCapacity;

    public static final int RC_NORESOURCES = -1;

    public static final int RC_NOMERCHANTS = -2;

    private Map<TimeWhenRunnable, ResourceTypeMap> returningMerchants;

    public MarketSite(String name, String urlString, Translator translator) {
        super(name, urlString, translator);
    }

    public void fetch(Util util) throws ConversationException {
        super.fetch(util);
        returningMerchants = new HashMap<TimeWhenRunnable, ResourceTypeMap>();
        merchantsFree = 0;
        merchantCapacity = 0;
        Pattern p;
        Matcher m;
        String page = util.httpGetPage(getUrlString());
        p = Pattern.compile("(?s)(?i)<tr><td colspan=\"2\">\\w+ (\\d+)/(\\d+)<br><br></td></tr>");
        m = p.matcher(page);
        while (m.find()) {
            String stringNumber = m.group(1).trim();
            try {
                merchantsFree = Integer.parseInt(stringNumber);
            } catch (NumberFormatException nfe) {
                throw new ConversationException("Error parsing free merchants in " + this.getName());
            }
        }
        p = Pattern.compile("(?s)(?i)<p>[^<]+<b>(\\d+)</b> [^<]+</p>");
        m = p.matcher(page);
        while (m.find()) {
            String stringNumber = m.group(1).trim();
            try {
                merchantCapacity = Integer.parseInt(stringNumber);
            } catch (NumberFormatException nfe) {
                throw new ConversationException("Error parsing merchant capacity in " + this.getName());
            }
        }
        p = Pattern.compile("(?s)(?i)<td width=\"21%\"><a href=\"spieler\\.php\\?uid=(\\d+)\"><span class=\"c0\">([^<]+)</span></a></td>\\s*<td colspan=\"\\d+\">[^<]+<a href=\"karte\\.php\\?d=(\\d+)\\&c=[^\"]+\"><span class=\"c0\">([^<]+)</span></a></td>\\s*</tr>\\s*<tr><td>[^<]+</td><td><span id=timer\\d+>([\\d:]+)</span>[^<]+</td><td>[^\\d]+([\\d:]+)</td></tr>\\s*<tr class=\"cbg1\"><td>[^<]+</td><td class=\"s7\" colspan=\"2\"><span class=\"c f10\"><img.*src=\"img/un/r/1\\.gif\".*>(\\d+) \\| <img.*src=\"img/un/r/2\\.gif\".*>(\\d+) \\| <img.*src=\"img/un/r/3\\.gif\".*>(\\d+) \\| <img.*src=\"img/un/r/4\\.gif\".*>(\\d+)</td>");
        m = p.matcher(page);
        if (!m.find()) {
            p = Pattern.compile("(?s)(?i)<td width=\"21%\"><a href=\"spieler\\.php\\?uid=(\\d+)\"><span class=\"c0\">([^<]+)</span></a></td>\\s*<td colspan=\"\\d+\">[^<]+<a href=\"karte\\.php\\?d=(\\d+)\\&c=[^\"]+\"><span class=\"c0\">([^<]+)</span></a></td>\\s*</tr>\\s*<tr><td>[^<]+</td><td><span id=timer\\d+>([\\d:]+)</span>[^<]+</td><td>[^\\d]+([\\d:]+)</td></tr>\\s*<tr class=\"cbg1\"><td>[^<]+</td><td class=\"s7\" colspan=\"2\"><span class=\"c f10\"><img[^>]*class=\"r1\"[^>]+>(\\d+) \\| <img[^>]*class=\"r2\"[^>]+>(\\d+) \\| <img[^>]*class=\"r3\"[^>]+>(\\d+) \\| <img[^>]*class=\"r4\"[^>]+>(\\d+)</td>");
            m = p.matcher(page);
        }
        while (m.find()) {
            String timeString = m.group(6).trim();
            String stringNumber1 = m.group(7).trim();
            String stringNumber2 = m.group(8).trim();
            String stringNumber3 = m.group(9).trim();
            String stringNumber4 = m.group(10).trim();
            try {
                ResourceTypeMap resources = new ResourceTypeMap();
                resources.put(ResourceType.WOOD, Integer.parseInt(stringNumber1));
                resources.put(ResourceType.CLAY, Integer.parseInt(stringNumber2));
                resources.put(ResourceType.IRON, Integer.parseInt(stringNumber3));
                resources.put(ResourceType.CROP, Integer.parseInt(stringNumber4));
                TimeWhenRunnable arrivalTime = new TimeWhenRunnable(System.currentTimeMillis() + Util.timeToSeconds(timeString) * Util.MILLI_SECOND);
                returningMerchants.put(arrivalTime, resources);
                System.out.println("Mercadores retornando");
            } catch (NumberFormatException nfe) {
                throw new ConversationException("Error parsing incoming merchants in " + this.getName());
            }
        }
    }

    public int sendResource(Util util, ResourceTypeMap resources, String x, String y, String targetVillage, boolean allowLess, Village village) throws ConversationException {
        if (merchantsFree == 0) {
            return RC_NOMERCHANTS;
        }
        int neededMerchants = 0;
        ResourceTypeMap available = village.getAvailableResources();
        for (ResourceType aresource : available.keySet()) {
            if (aresource == ResourceType.FOOD) {
                continue;
            }
            int toSend = resources.get(aresource);
            int got = available.get(aresource);
            if (got < toSend) {
                if (allowLess) {
                    resources.put(aresource, got);
                    toSend = got;
                } else {
                    log.debug("Not enough resources");
                    return RC_NORESOURCES;
                }
            }
            neededMerchants += (int) Math.ceil(toSend / (double) merchantCapacity);
        }
        if (neededMerchants > merchantsFree) {
            if (!allowLess) {
                log.debug("Not enough merchants");
                return RC_NOMERCHANTS;
            } else {
                int toRemoveTot = neededMerchants - merchantsFree;
                while (toRemoveTot > 0) {
                    for (ResourceType aresource : resources.keySet()) {
                        int origAmount = resources.get(aresource);
                        if (origAmount <= 0 || aresource == ResourceType.FOOD) {
                            continue;
                        }
                        int toRemove = origAmount % merchantCapacity;
                        if (toRemove == 0) {
                            toRemove = merchantCapacity;
                        }
                        int newAmount = Math.max(origAmount - toRemove, 0);
                        resources.put(aresource, newAmount);
                        toRemoveTot--;
                        if (toRemoveTot == 0) {
                            break;
                        }
                    }
                }
            }
        }
        String page;
        Pattern p;
        Matcher m;
        String destinationVillage = null;
        page = util.httpGetPage(getUrlString());
        List<String> postNames = new ArrayList<String>();
        List<String> postValues = new ArrayList<String>();
        Util.addHiddenPostFields(page, "<form method=\"POST\" name=\"snd\" action=\"build.php\">", postNames, postValues);
        Util.addButtonCoordinates("s1", 50, 20, postNames, postValues);
        for (ResourceType resource : resources.keySet()) {
            int amount = resources.get(resource);
            postNames.add("r" + (resource.toInt() + 1));
            postValues.add(Integer.toString(amount));
        }
        postNames.add("dname");
        postValues.add(targetVillage == null ? "" : targetVillage);
        postNames.add("x");
        postValues.add(x == null ? "" : x);
        postNames.add("y");
        postValues.add(y == null ? "" : y);
        page = util.httpPostPage(getUrlString(), postNames, postValues, false);
        postNames.clear();
        postValues.clear();
        Util.addHiddenPostFields(page, "<form method=\"POST\" name=\"snd\" action=\"build.php\">", postNames, postValues);
        Util.addButtonCoordinates("s1", 50, 20, postNames, postValues);
        for (ResourceType resource : resources.keySet()) {
            if (resource == ResourceType.FOOD) {
                break;
            }
            int amount = resources.get(resource);
            postNames.add("r" + (resource.toInt() + 1));
            postValues.add(Integer.toString(amount));
        }
        p = Pattern.compile("(?s)(?i)<p class=\"f135\">([^<]*)</p>");
        m = p.matcher(page);
        if (m.find()) {
            destinationVillage = m.group(1);
        }
        if (destinationVillage == null) {
            this.fetch(util);
            log.debug("Available merchants = " + merchantsFree);
            log.debug("Available resources = " + available);
            log.debug("Resources to send = " + resources);
            Util.saveTestPattern("No village after first post", p, page);
            throw new ConversationException("Can't send resources: no village after first post");
        }
        EventLog.log("Sending merchants to " + destinationVillage);
        p = Pattern.compile("(?s)(?i)</td><td>(\\d+:\\d+:\\d+)</td></tr>");
        m = p.matcher(page);
        String tripTimeString;
        if (m.find()) {
            tripTimeString = m.group(1).trim();
        } else {
            throw new ConversationException("Can't find trip time");
        }
        page = util.httpPostPage(getUrlString(), postNames, postValues, false);
        EventLog.log("Resources (" + resources.toStringNoFood() + ") sent to " + destinationVillage + ", will arrive in " + tripTimeString);
        return Util.timeToSeconds(tripTimeString);
    }

    public int getMerchantsFree() {
        return merchantsFree;
    }

    public TimeWhenRunnable getWhenNextMerchantFree() {
        TimeWhenRunnable nextMerchantFree = TimeWhenRunnable.NEVER;
        for (TimeWhenRunnable freeMerchant : returningMerchants.keySet()) {
            if (nextMerchantFree.after(freeMerchant)) {
                nextMerchantFree = freeMerchant;
            }
        }
        return nextMerchantFree;
    }
}
