package webhook.teamcity.payload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import jetbrains.buildServer.log.Loggers;

public class WebHookPayloadManager {

    HashMap<String, WebHookPayload> formats = new HashMap<String, WebHookPayload>();

    Comparator<WebHookPayload> rankComparator = new WebHookPayloadRankingComparator();

    List<WebHookPayload> orderedFormatCollection = new ArrayList<WebHookPayload>();

    public WebHookPayloadManager() {
        Loggers.SERVER.info("WebHookPayloadManager :: Starting");
    }

    public void registerPayloadFormat(WebHookPayload payloadFormat) {
        Loggers.SERVER.info(this.getClass().getSimpleName() + " :: Registering payload " + payloadFormat.getFormatShortName() + " with rank of " + payloadFormat.getRank());
        formats.put(payloadFormat.getFormatShortName(), payloadFormat);
        this.orderedFormatCollection.add(payloadFormat);
        Collections.sort(this.orderedFormatCollection, rankComparator);
        Loggers.SERVER.debug(this.getClass().getSimpleName() + " :: Payloads list is " + this.orderedFormatCollection.size() + " items long. Payloads are ranked in the following order..");
        for (WebHookPayload pl : this.orderedFormatCollection) {
            Loggers.SERVER.debug(this.getClass().getSimpleName() + " :: Payload Name: " + pl.getFormatShortName() + " Rank: " + pl.getRank());
        }
    }

    public WebHookPayload getFormat(String formatShortname) {
        if (formats.containsKey(formatShortname)) {
            return formats.get(formatShortname);
        }
        return null;
    }

    public Boolean isRegisteredFormat(String format) {
        return formats.containsKey(format);
    }

    public Set<String> getRegisteredFormats() {
        return formats.keySet();
    }

    public Collection<WebHookPayload> getRegisteredFormatsAsCollection() {
        return orderedFormatCollection;
    }
}
