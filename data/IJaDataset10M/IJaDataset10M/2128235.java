package therandomhomepage.common.rss;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: SHAMEED
 * Date: Jul 8, 2006
 * Time: 11:27:21 PM
 */
public class JSON2RSSParser {

    public static List parse(String jsonText) {
        JSONValue jsonValue;
        List rssItems = null;
        try {
            jsonValue = JSONParser.parse(jsonText);
            if (jsonValue != null) {
                JSONArray jsonArray = jsonValue.isArray();
                if (jsonArray != null) {
                    rssItems = new ArrayList();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONValue jsonObject = (JSONValue) jsonArray.get(i);
                        rssItems.add(parseChild(jsonObject));
                    }
                }
            }
        } catch (JSONException e) {
            GWT.log("Error while parsing ", e);
        }
        return rssItems;
    }

    public static RSSItem[] parseAsArray(String jsonText, int elementCount) {
        JSONValue jsonValue;
        RSSItem rssItems[] = null;
        try {
            jsonValue = JSONParser.parse(jsonText);
            if (jsonValue != null) {
                if (jsonValue.isArray() != null) {
                    rssItems = new RSSItem[elementCount];
                    JSONArray array = jsonValue.isArray();
                    parseArray(array, elementCount, rssItems);
                } else if (jsonValue.isObject() != null) {
                    rssItems = new RSSItem[elementCount];
                    JSONObject jsonObject = jsonValue.isObject();
                    JSONObject resultSet = (JSONObject) jsonObject.get("jsonFlickrFeed");
                    JSONArray resultArray = (JSONArray) resultSet.get("items");
                    if (resultArray != null) {
                        parseArray(resultArray, elementCount, rssItems);
                    }
                }
            }
        } catch (JSONException e) {
            GWT.log("Unable to parse RSS Items", e);
        }
        return rssItems;
    }

    private static void parseArray(JSONArray array, int elementCount, RSSItem[] rssItems) {
        for (int i = 0; i < array.size() && i < elementCount; i++) {
            JSONValue jsonObject = (JSONValue) array.get(i);
            rssItems[i] = parseChild(jsonObject);
        }
    }

    private static RSSItem parseChild(JSONValue jsonValue) {
        if (jsonValue.isObject() != null) {
            JSONValue title = parseChildAttribute(jsonValue, "title");
            JSONValue link = parseChildAttribute(jsonValue, "link");
            JSONValue guid = parseChildAttribute(jsonValue, "guid");
            if (guid == null) {
                guid = parseChildAttribute(jsonValue, "id");
            }
            JSONValue publishedDate = parseChildAttribute(jsonValue, "pubdate");
            JSONValue description = parseChildAttribute(jsonValue, "description");
            if (description == null) {
                description = parseChildAttribute(jsonValue, "atom_content");
            }
            RSSItem rssItem = new RSSItem(parseString(title), parseString(link), parseString(description));
            rssItem.setGuid(parseString(guid));
            rssItem.setPublishedDate(parseString(publishedDate));
            return rssItem;
        }
        return null;
    }

    private static String parseString(JSONValue str) {
        if (str != null && str.isString() != null) {
            return str.isString().stringValue().trim();
        }
        return "";
    }

    private static JSONValue parseChildAttribute(JSONValue childObject, String attributeKey) {
        if (childObject.isObject() != null && childObject.isObject().get(attributeKey) != null) {
            return childObject.isObject().get(attributeKey);
        }
        return null;
    }
}
