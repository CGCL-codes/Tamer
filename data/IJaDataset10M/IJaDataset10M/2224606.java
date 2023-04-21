package jp.joogoo.web.schema.cybozulive;

import java.util.List;
import java.util.Map;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Key;
import com.google.api.client.xml.XmlNamespaceDictionary;

public class NotifyFeed {

    public static final XmlNamespaceDictionary NAMESPACE_DICTIONARY = new XmlNamespaceDictionary();

    {
        Map<String, String> map = NAMESPACE_DICTIONARY.namespaceAliasToUriMap;
        map.put("", "http://www.w3.org/2005/Atom");
        map.put("activity", "http://activitystrea.ms/spec/1.0/");
        map.put("georss", "http://www.georss.org/georss");
        map.put("media", "http://search.yahoo.com/mrss/");
        map.put("thr", "http://purl.org/syndication/thread/1.0");
        map.put("cbl", "http://schema.cybozulive.com/common/2010");
        map.put("cblNtf", "http://schemas.cybozulive.com/notification/2010");
    }

    @Key
    public DateTime updated;

    @Key
    public Author author;

    @Key("openSearch:totalResults")
    public int totalResults;

    @Key("entry")
    public List<NotifyEntry> entry;

    @Key("link")
    public List<Link> links;
}
