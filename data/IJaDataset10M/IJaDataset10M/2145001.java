package be.kuleuven.peno3.mobiletoledo.Data.Client;

import java.io.IOException;
import java.util.Calendar;
import org.apache.commons.httpclient.HttpException;
import org.json.JSONException;
import org.json.JSONObject;
import be.kuleuven.peno3.mobiletoledo.model.QueueInfo;
import com.google.gson.Gson;

public abstract class QueueClient extends Client {

    public static QueueInfo getLength(String location) {
        try {
            String json = stringOfUrl(host + "/QueueHandler/getLength?location=" + encode(location));
            QueueInfo[] q = new Gson().fromJson(json, QueueInfo[].class);
            if (q == null) {
                return null;
            }
            try {
                return q[0];
            } catch (ArrayIndexOutOfBoundsException ex) {
                ex.printStackTrace();
                return null;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (HttpException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static QueueInfo[] getLengthHistory(String location) {
        try {
            String json = stringOfUrl(host + "/QueueHandler/getLengthHistory?location=" + encode(location));
            QueueInfo[] q = new Gson().fromJson(json, QueueInfo[].class);
            return q;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addLength(String location, int length, Calendar when, String author) {
        try {
            String url = "location=" + location + "&length=" + length + "&when=" + toSQLString(when) + "&author=" + author;
            url = host + "/QueueHandler/addLength?" + encode(url);
            String json = stringOfUrl(url);
            String result = null;
            try {
                result = (String) new JSONObject(json).get("result");
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            if (result.indexOf("successful") > 0) {
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
