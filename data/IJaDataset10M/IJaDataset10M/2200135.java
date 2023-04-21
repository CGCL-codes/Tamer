package openPayments.Backup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import openPayments.ServiceHelpers;
import openPayments.Logging.DefaultExceptionHandler;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BackupServices {

    /**
	 * @param clientId
	 * @param appKey
	 * @return
	 */
    public static Attribute[] GetAttributeTotals(String appKey, String entryNameStartingWith, String matchingValue, String matchingValue1, String matchingValue2, String matchingTag, String matchingCategory, int onlyTotalsGreaterThan) {
        try {
            JSONArray finalResult = GetAttributeTotalsRaw(appKey, entryNameStartingWith, matchingValue, matchingValue1, matchingValue2, matchingTag, matchingCategory, onlyTotalsGreaterThan);
            Attribute[] attrs = BackupUtils.GetAttributes(finalResult);
            return attrs;
        } catch (Exception e) {
            DefaultExceptionHandler.logException(Thread.currentThread(), e);
        }
        return null;
    }

    public static JSONArray GetAttributeTotalsRaw(String appKey, String entryNameStartingWith, String matchingValue, String matchingValue1, String matchingValue2, String matchingTag, String matchingCategory, int onlyTotalsGreaterThan) throws IOException, ClientProtocolException, UnsupportedEncodingException, JSONException {
        String pathSegment = "Services/GetAttributeTotals?" + ServiceHelpers.AddQueryString("appKey", appKey, false) + ServiceHelpers.AddQueryString("entryNameStartingWith", entryNameStartingWith, false) + ServiceHelpers.AddQueryString("matchingValue", matchingValue, false) + ServiceHelpers.AddQueryString("matchingValue1", matchingValue1, false) + ServiceHelpers.AddQueryString("matchingValue2", matchingValue2, false) + ServiceHelpers.AddQueryString("matchingTag", matchingTag, false) + ServiceHelpers.AddQueryString("matchingCategory", matchingCategory, false) + ServiceHelpers.AddQueryString("onlyTotalsGreaterThan", String.valueOf(onlyTotalsGreaterThan), true);
        JSONArray finalResult = ServiceHelpers.Get(pathSegment);
        return finalResult;
    }

    /**
	 * @param clientId
	 * @param appKey
	 * @return
	 */
    public static Attribute[] GetAttributes(String clientId, String appKey) {
        try {
            JSONArray finalResult = GetAttributesRaw(clientId, appKey);
            Attribute[] attrs = BackupUtils.GetAttributes(finalResult);
            if (attrs != null && attrs.length > 0) return attrs;
        } catch (Exception e) {
            DefaultExceptionHandler.logException(Thread.currentThread(), e);
        }
        return null;
    }

    public static JSONArray GetAttributesRaw(String clientId, String appKey) throws IOException, ClientProtocolException, UnsupportedEncodingException, JSONException {
        String pathSegment = "Services/GetAttributes?" + ServiceHelpers.AddQueryString("clientId", clientId, false) + ServiceHelpers.AddQueryString("appKey", appKey, true);
        JSONArray finalResult = ServiceHelpers.Get(pathSegment);
        return finalResult;
    }

    /**
	 * @param clientId
	 * @param appKey
	 * @return
	 */
    public static boolean SetAttribute(String clientId, String appKey, String entryName, String entryValue, String entryValue1, String entryValue2, String entryCategory, String entryTag) {
        try {
            JSONArray finalResult = SetAttributeRaw(clientId, appKey, entryName, entryValue, entryValue1, entryValue2, entryCategory, entryTag);
            String result = ServiceHelpers.GetString(finalResult);
            return Boolean.valueOf(result).booleanValue();
        } catch (Exception e) {
            DefaultExceptionHandler.logException(Thread.currentThread(), e);
        }
        return false;
    }

    public static JSONArray SetAttributeRaw(String clientId, String appKey, String entryName, String entryValue, String entryValue1, String entryValue2, String entryCategory, String entryTag) throws IOException, ClientProtocolException, UnsupportedEncodingException, JSONException {
        String pathSegment = "Services/Attribute";
        JSONObject parameters = new JSONObject();
        parameters.put("clientId", clientId);
        parameters.put("appKey", appKey);
        parameters.put("entryName", entryName);
        parameters.put("entryValue", entryValue);
        parameters.put("entryValue1", entryValue1);
        parameters.put("entryValue2", entryValue2);
        parameters.put("entryCategory", entryCategory);
        parameters.put("entryTag", entryTag);
        JSONArray finalResult = ServiceHelpers.Post(pathSegment, parameters);
        return finalResult;
    }

    public static Attribute[] FindAttributesMatching(String clientId, String appKey, String entryName, String entryValue, String entryCategory) {
        try {
            JSONArray finalResult = FindAttributesMatchingRaw(clientId, appKey, entryName, entryValue, entryCategory);
            Attribute[] attrs = BackupUtils.GetAttributes(finalResult);
            if (attrs != null && attrs.length > 0) return attrs;
        } catch (Exception e) {
            DefaultExceptionHandler.logException(Thread.currentThread(), e);
        }
        return null;
    }

    /**
	 * Finds a set of attributes matching all non-null arguments passed. Max
	 * number of results is limited to 10. All arguments are optional.
	 * 
	 * @param clientId
	 * @param appKey
	 * @param entryName
	 * @param entryValue
	 * @param entryCategory
	 * @return
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws JSONException
	 */
    public static JSONArray FindAttributesMatchingRaw(String clientId, String appKey, String entryName, String entryValue, String entryCategory) throws IOException, ClientProtocolException, UnsupportedEncodingException, JSONException {
        String pathSegment = "Services/FindAttributesMatching?" + ServiceHelpers.AddQueryString("clientId", clientId, false) + ServiceHelpers.AddQueryString("appKey", appKey, false) + ServiceHelpers.AddQueryString("entryName", entryName, false) + ServiceHelpers.AddQueryString("entryValue", entryValue, false) + ServiceHelpers.AddQueryString("entryCategory", entryCategory, true);
        JSONArray finalResult = ServiceHelpers.Get(pathSegment);
        return finalResult;
    }
}
