package com.vivilab.smth.helper;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.vivilab.smth.model.Article;
import com.vivilab.smth.model.Board;
import com.vivilab.smth.model.TopTenPost;
import android.util.Log;

public class SmthHelper {

    public static final int STATE_LOGIN_OK = 1;

    public static final int STATE_LOGIN_FAIL = 0;

    public static final int STATE_SESSION_OK = 1;

    public static final int STATE_SESSION_FAIL = 0;

    private static final String TAG = "SmthHelper";

    private static String sessionKey;

    private static String gwUrl;

    private static String currentUser;

    private static String httpGet(String url) {
        try {
            URL getUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
            connection.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer result = new StringBuffer();
            String oneline;
            while ((oneline = reader.readLine()) != null) {
                result.append(oneline);
            }
            reader.close();
            connection.disconnect();
            return result.toString();
        } catch (Exception e) {
            Log.e(TAG, "httpGet,httpGet error", e);
            return null;
        }
    }

    private static String httpPost(String url, List namevalue) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost postUrl = new HttpPost(url);
        try {
            postUrl.setEntity(new UrlEncodedFormEntity(namevalue, HTTP.UTF_8));
            HttpResponse response;
            response = httpclient.execute(postUrl);
            if (response.getStatusLine().getStatusCode() == 200) {
                String strResult = EntityUtils.toString(response.getEntity());
                return strResult;
            } else {
                Log.e(TAG, "httpPost response error");
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "httpPost exception:", e);
            return null;
        }
    }

    public static void setApiHost(String url) {
        gwUrl = url;
    }

    public static int login(String id, String passwd) {
        try {
            String loginUrl = gwUrl + "login?id=" + URLEncoder.encode(id, "utf-8") + "&passwd=" + URLEncoder.encode(passwd, "utf-8");
            String result = httpGet(loginUrl);
            if (result != null) {
                JSONObject json = new JSONObject(result);
                Log.i(TAG, "login,info=" + result);
                String key = json.getString("k");
                if (!key.equals("0")) {
                    sessionKey = key;
                    currentUser = id;
                    return STATE_LOGIN_OK;
                } else return STATE_LOGIN_FAIL;
            } else {
                Log.i(TAG, "login,result is null");
                return STATE_LOGIN_FAIL;
            }
        } catch (Exception e) {
            Log.e(TAG, "login,login error", e);
            return STATE_LOGIN_FAIL;
        }
    }

    public static int logout() {
        try {
            String logoutUrl = gwUrl + "logout?key=" + sessionKey;
            String result = httpGet(logoutUrl);
            Log.i(TAG, "do logout,r:" + result);
            return 0;
        } catch (Exception e) {
            Log.e(TAG, "do logout exception:", e);
            return 1;
        }
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static List getFavorate() {
        try {
            String favUrl = gwUrl + "getfav?key=" + URLEncoder.encode(sessionKey, "utf-8");
            String result = httpGet(favUrl);
            if (result != null) {
                JSONObject json = new JSONObject(result);
                int login = json.getInt("l");
                if (login != STATE_SESSION_OK) {
                    Log.w(TAG, "getFavorate session fail");
                    return null;
                }
                JSONArray a = json.getJSONArray("r");
                Log.i(TAG, "getfav r:" + a);
                List resultList = new ArrayList();
                for (int i = 0; i < a.length(); i++) {
                    JSONObject b = a.getJSONObject(i);
                    Log.i(TAG, "we are adding" + b.toString());
                    Iterator it = b.keys();
                    resultList.add((String) it.next());
                }
                return resultList;
            } else return null;
        } catch (Exception e) {
            Log.e(TAG, "getFavorate error", e);
            return null;
        }
    }

    public static List getTopTopic() {
        try {
            String topUrl = gwUrl + "gettop?key=" + URLEncoder.encode(sessionKey, "utf-8");
            String result = httpGet(topUrl);
            if (result != null) {
                JSONObject json = new JSONObject(result);
                JSONArray a = json.getJSONArray("r");
                Log.i(TAG, "gettop:" + a);
                List resultList = new ArrayList();
                for (int i = 0; i < a.length(); i++) {
                    JSONObject b = a.getJSONObject(i);
                    TopTenPost top = new TopTenPost();
                    top.setAuthor(b.getString("a"));
                    top.setBoard(b.getString("b"));
                    top.setGid(b.getString("gid"));
                    top.setPubDate(b.getString("d"));
                    top.setTitle(b.getString("t"));
                    resultList.add(top);
                }
                return resultList;
            } else return null;
        } catch (Exception e) {
            Log.e(TAG, "getTop error", e);
            return null;
        }
    }

    public static Board getwz(String name, String pageaddition) {
        try {
            String boardUrl;
            if (pageaddition != null) {
                boardUrl = gwUrl + "getboard?ftype=1&key=" + sessionKey + "&board=" + URLEncoder.encode(name, "utf-8") + "&page=" + pageaddition;
            } else boardUrl = gwUrl + "getboard?ftype=1&key=" + sessionKey + "&board=" + URLEncoder.encode(name, "utf-8");
            String result = httpGet(boardUrl);
            if (result != null) {
                JSONObject json = new JSONObject(result);
                int login = json.getInt("l");
                if (login != STATE_SESSION_OK) {
                    Log.w(TAG, "getWz session fail");
                    return null;
                }
                JSONArray a = json.getJSONArray("r");
                Log.i(TAG, "getwz r:" + a);
                List resultList = new ArrayList();
                for (int i = 0; i < a.length(); i++) {
                    JSONObject b = a.getJSONObject(i);
                    Article at = new Article();
                    at.setId(b.getString("id"));
                    at.setInfo(b.getString("info"));
                    at.setTitle(b.getString("title"));
                    resultList.add(at);
                }
                Board board = new Board();
                board.setArticles(resultList);
                board.setPpage(json.getInt("pp"));
                board.setNpage(json.getInt("np"));
                board.setFtype(1);
                return board;
            } else {
                Log.w(TAG, "getwz no result");
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "getwz error", e);
            return null;
        }
    }

    public static Board getmark(String name, String pageaddition) {
        try {
            String boardUrl;
            if (pageaddition != null) {
                boardUrl = gwUrl + "getboard?ftype=3&key=" + sessionKey + "&board=" + URLEncoder.encode(name, "utf-8") + "&page=" + pageaddition;
            } else boardUrl = gwUrl + "getboard?ftype=3&key=" + sessionKey + "&board=" + URLEncoder.encode(name, "utf-8");
            String result = httpGet(boardUrl);
            if (result != null) {
                JSONObject json = new JSONObject(result);
                int login = json.getInt("l");
                if (login != STATE_SESSION_OK) {
                    Log.w(TAG, "getMark session fail");
                    return null;
                }
                JSONArray a = json.getJSONArray("r");
                Log.i(TAG, "getmark r:" + a);
                List resultList = new ArrayList();
                for (int i = 0; i < a.length(); i++) {
                    JSONObject b = a.getJSONObject(i);
                    Article at = new Article();
                    at.setId(b.getString("id"));
                    at.setInfo(b.getString("info"));
                    at.setTitle(b.getString("title"));
                    resultList.add(at);
                }
                Board board = new Board();
                board.setArticles(resultList);
                board.setPpage(json.getInt("pp"));
                board.setNpage(json.getInt("np"));
                board.setFtype(3);
                return board;
            } else {
                Log.w(TAG, "getmark no result");
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "getwz error", e);
            return null;
        }
    }

    public static Board gettopic(String name, String pageaddition) {
        try {
            String boardUrl;
            if (pageaddition != null) {
                boardUrl = gwUrl + "gettopic?key=" + sessionKey + "&board=" + name + "&page=" + pageaddition;
            } else boardUrl = gwUrl + "gettopic?key=" + sessionKey + "&board=" + name;
            String result = httpGet(boardUrl);
            if (result != null) {
                JSONObject json = new JSONObject(result);
                int login = json.getInt("l");
                if (login != STATE_SESSION_OK) {
                    Log.w(TAG, "getBoard session fail");
                    return null;
                }
                JSONArray a = json.getJSONArray("r");
                Log.i(TAG, "gettopic r:" + a);
                List resultList = new ArrayList();
                for (int i = 0; i < a.length(); i++) {
                    JSONObject b = a.getJSONObject(i);
                    Article at = new Article();
                    at.setId(b.getString("id"));
                    String timet = b.getString("d");
                    String author = b.getString("a");
                    Date pubDate = new Date(Long.valueOf(timet + "000"));
                    SimpleDateFormat sdf = new SimpleDateFormat("MM-dd", Locale.US);
                    at.setInfo(author + " " + sdf.format(pubDate));
                    at.setTitle("● " + b.getString("t"));
                    resultList.add(at);
                }
                Board board = new Board();
                board.setArticles(resultList);
                int page = json.getInt("p");
                board.setPpage(page - 1);
                board.setNpage(page + 1);
                board.setFtype(6);
                return board;
            } else {
                Log.w(TAG, "gettopic no result");
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "gettopic error", e);
            return null;
        }
    }

    public static Board getboard(String name, String pageaddition) {
        try {
            String boardUrl;
            if (pageaddition != null) {
                boardUrl = gwUrl + "getboard?key=" + sessionKey + "&board=" + URLEncoder.encode(name, "utf-8") + "&page=" + pageaddition;
            } else boardUrl = gwUrl + "getboard?key=" + sessionKey + "&board=" + URLEncoder.encode(name, "utf-8");
            String result = httpGet(boardUrl);
            if (result != null) {
                JSONObject json = new JSONObject(result);
                int login = json.getInt("l");
                if (login != STATE_SESSION_OK) {
                    Log.w(TAG, "getBoard session fail");
                    return null;
                }
                JSONArray a = json.getJSONArray("r");
                Log.i(TAG, "getboard r:" + a);
                List resultList = new ArrayList();
                for (int i = 0; i < a.length(); i++) {
                    JSONObject b = a.getJSONObject(i);
                    Article at = new Article();
                    at.setId(b.getString("id"));
                    at.setInfo(b.getString("info"));
                    at.setTitle(b.getString("title"));
                    resultList.add(at);
                }
                Board board = new Board();
                board.setArticles(resultList);
                board.setPpage(json.getInt("pp"));
                board.setNpage(json.getInt("np"));
                board.setFtype(0);
                return board;
            } else {
                Log.w(TAG, "getboard no result");
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "getboard error", e);
            return null;
        }
    }

    public static Article article(String board, String id, String p) {
        try {
            String articleUrl;
            if (p != null) articleUrl = gwUrl + "article?key=" + sessionKey + "&board=" + URLEncoder.encode(board, "utf-8") + "&id=" + id + "&p=" + p; else articleUrl = gwUrl + "article?key=" + sessionKey + "&board=" + URLEncoder.encode(board, "utf-8") + "&id=" + id;
            String result = httpGet(articleUrl);
            if (result != null) {
                JSONObject json = new JSONObject(result);
                int login = json.getInt("l");
                if (login != STATE_SESSION_OK) {
                    Log.w(TAG, "article session fail");
                    return null;
                }
                String content = json.getString("r");
                Log.i(TAG, "article r:" + content);
                Article article = new Article();
                article.setContent(content);
                article.setTitle(json.getString("t"));
                article.setTopid(json.getString("tid"));
                article.setId(json.getString("id"));
                return article;
            } else {
                Log.w(TAG, "article no result");
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "article error", e);
            return null;
        }
    }

    public static Article beautya(String board, String id, String p) {
        try {
            String articleUrl;
            if (p != null) articleUrl = gwUrl + "beautya?key=" + sessionKey + "&board=" + URLEncoder.encode(board, "utf-8") + "&id=" + id + "&p=" + p; else articleUrl = gwUrl + "beautya?key=" + sessionKey + "&board=" + URLEncoder.encode(board, "utf-8") + "&id=" + id;
            String result = httpGet(articleUrl);
            if (result != null) {
                JSONObject json = new JSONObject(result);
                int login = json.getInt("l");
                if (login != STATE_SESSION_OK) {
                    Log.w(TAG, "article session fail");
                    return null;
                }
                JSONObject a = json.getJSONObject("r");
                Log.i(TAG, "article r:" + a);
                Article article = new Article();
                article.setContent(a.getString("c"));
                article.setTitle(a.getString("t"));
                article.setTopid(a.getString("tid"));
                article.setId(a.getString("id"));
                article.setAuthor(a.getString("a"));
                article.setDate(a.getString("d").replaceAll("\n", ""));
                article.setId(a.getString("id"));
                article.setHasAttach(a.getInt("att"));
                article.setAtthUrl(a.getString("url"));
                return article;
            } else {
                Log.w(TAG, "article no result");
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "article error", e);
            return null;
        }
    }

    public static int post(String board, String title, String content, String reid) {
        try {
            Log.i(TAG, "ready to post:" + title + "," + content);
            String postUrl;
            postUrl = gwUrl + "post?key=" + sessionKey + "&board=" + URLEncoder.encode(board, "utf-8") + "&reid=" + reid;
            List<NameValuePair> namevalue = new ArrayList<NameValuePair>();
            namevalue.add(new BasicNameValuePair("title", title));
            namevalue.add(new BasicNameValuePair("content", content));
            String result = httpPost(postUrl, namevalue);
            if (result != null) {
                JSONObject json = new JSONObject(result);
                int login = json.getInt("l");
                if (login != STATE_SESSION_OK) {
                    Log.w(TAG, "post session fail");
                    return 1;
                }
                int state = json.getInt("r");
                Log.i(TAG, "post r:" + state);
                return state;
            } else {
                Log.w(TAG, "article no post");
                return 1;
            }
        } catch (Exception e) {
            Log.e(TAG, "article post error", e);
            return 1;
        }
    }
}
