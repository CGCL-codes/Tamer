package org.cloud.android.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

public class CloudFileManagementService {

    public static String sayHelloToEveryone() {
        String result = "Hi, everyboday.";
        return result;
    }

    /**
	 * create a new tag(folder) under a certain tag(folder).
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param maxNumberOfFeedString this is a string format not a number format indicate the maximun number of feeds you want to receive, usually set this number to "10"
	 * @param owner_alias a string indicate the user you login to CLOUD, like "cs588", "yi" etc.
	 * @return an raw information string with all the information return from server.
	 */
    public static String get_feeds_for_current_user(String sessionid, String maxNumberOfFeedString, String owner_alias) {
        String resultJsonString = "some problem existed inside the create_new_tag() function if you see this string";
        try {
            Log.d("current running function name:", "get_feeds_for_current_user");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Profile"));
            nameValuePairs.add(new BasicNameValuePair("m", "get_feeds"));
            nameValuePairs.add(new BasicNameValuePair("only_public_feeds", "false"));
            nameValuePairs.add(new BasicNameValuePair("count", maxNumberOfFeedString));
            nameValuePairs.add(new BasicNameValuePair("order", "DESC"));
            nameValuePairs.add(new BasicNameValuePair("order_field", "modified"));
            nameValuePairs.add(new BasicNameValuePair("owner_alias", owner_alias));
            nameValuePairs.add(new BasicNameValuePair("before_feed_key", "current_id"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            resultJsonString = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", resultJsonString);
            return resultJsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonString;
    }

    /**
	 * return a List, including all the files and tags(only two types defined in CLOUD) that shown on the user's specific tag(folder).
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session 
	 * @param tag a tag use for specify the specific tag(folder) the user want to open
	 * @return a list object with JsonObject object in the list, JsonObject is defined by Yi, please inform him for more information.
	 * 
	 * For example, the root tag(folder) is:@, below the root tag(folder),in default,there are 4 tags: 
	 * (1) @/Pictures (2) @/Videos (3) @/Music (4) @/Documents
	 * notes:
	 * (1)use "/" to get into sub tags(folders)
	 * (2)the file format in the return list is(just one example):
	 * {"name":"Her Eye.jpg","key":"f2e9bbd951795a6fb3f3e9bc23bd3990","content_type":"image","last_modified":"September 19, 2010 12:10 pm","size_mb":"0.148285","is_public":"0","server_domain":"s2.cloud.cm","is_shared":0}
	 * (3)the tag format in the return list is(just one example):
	 * {"name":"Pictures","key":"@\/Pictures","content_type":"tag","last_modified":"September 19, 2010 12:08 pm","is_shared":0.0}
	 * (4)use the file key to access to specific file
	 */
    public static List<JsonObject> get_contents_by_tag(String sessionid, String tag) {
        ArrayList files = new ArrayList();
        try {
            Log.d("current running function name:", "get_contents_by_tag");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("offset", "0"));
            nameValuePairs.add(new BasicNameValuePair("tag", tag));
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("count", "30"));
            nameValuePairs.add(new BasicNameValuePair("m", "get_contents_by_tag"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            String jsonstring = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", jsonstring);
            JsonObject result = JsonObject.parse(jsonstring);
            Float status = (Float) result.getValue("result_code");
            Log.d("responseStatusCode", status.toString());
            if (!status.equals((float) 0.0)) {
                throw new RuntimeException("Error! get files info failed");
            }
            JsonObject data = (JsonObject) result.getValue("data");
            files = (ArrayList) data.getValue("files");
            if (files.size() == 0) {
                files = new ArrayList();
                return files;
            } else {
                return files;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
	 * Please DO NOT USE this function by now.
	 * Development Status: NOT Tested.
	 * return a List, including all the share files and tags(including me and the others).
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 */
    public static List<JsonObject> get_all_share_contents(String sessionid) {
        ArrayList files = new ArrayList();
        try {
            Log.d("current running function name:", "get_share_contents");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("offset", "0"));
            nameValuePairs.add(new BasicNameValuePair("c", "Shared"));
            nameValuePairs.add(new BasicNameValuePair("count", "30"));
            nameValuePairs.add(new BasicNameValuePair("m", "get_others_initial_contents"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            String jsonstring = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", jsonstring);
            JsonObject result = JsonObject.parse(jsonstring);
            Float status = (Float) result.getValue("result_code");
            Log.d("DEBUG", status.toString());
            if (!status.equals((float) 0.0)) {
                throw new RuntimeException("Error! get files info failed");
            }
            JsonObject data = (JsonObject) result.getValue("data");
            files = (ArrayList) data.getValue("files");
            return files;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
	 * Please DO NOT USE this function by now.
	 * Development Status: NOT Tested.
	 * return a List, including all the share files and tags(including ONLY me).
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 */
    public static List<JsonObject> get_personal_share_contents(String sessionid) {
        ArrayList files = new ArrayList();
        try {
            Log.d("current running function name:", "get_share_contents");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Shared"));
            nameValuePairs.add(new BasicNameValuePair("m", "get_personal_shared_data"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            String jsonstring = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", jsonstring);
            JsonObject result = JsonObject.parse(jsonstring);
            Float status = (Float) result.getValue("result_code");
            Log.d("DEBUG", status.toString());
            if (!status.equals((float) 0.0)) {
                throw new RuntimeException("Error! get files info failed");
            }
            JsonObject data = (JsonObject) result.getValue("data");
            files = (ArrayList) data.getValue("files");
            return files;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return files;
    }

    /**
	 * When you get a specific file information and know that this file is an image type and this file's key, you can
	 * use this function directly.
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param key a string that specific picture file
	 * @return a Bitmap object that can directly display in android using ImageView with the thumbnail size picture
	 */
    public static Bitmap download_picture_thumb(String sessionid, String key) {
        OutputStream os = null;
        String urlString = "https://mt0-s2.cloud.cm/rpc/raw?c=Pictures&m=download_picture_thumb&thumb_size=medium&key=" + key;
        Bitmap bitmap = null;
        ArrayList files = new ArrayList();
        try {
            URL url = new URL(urlString);
            Log.d("current running function name:", "download_picture_thumb");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", "PHPSESSID=" + sessionid);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
	 * When you get a specific file information and know that this file is an image type and this file's key, you can
	 * use this function directly.
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param key a string that specific picture file
	 * @return a Bitmap object that can directly display in android using ImageView with the original size picture
	 */
    public static Bitmap download_picture_original(String sessionid, String key) {
        OutputStream os = null;
        String urlString = "https://mt0-s2.cloud.cm/rpc/raw?c=Pictures&m=download_picture&key=" + key;
        Bitmap bitmap = null;
        ArrayList files = new ArrayList();
        try {
            URL url = new URL(urlString);
            Log.d("current running function name:", "download_picture_original");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", "PHPSESSID=" + sessionid);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            InputStream is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            return bitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
	 * create a new tag(folder) under a certain tag(folder).
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param parentTagName a string that specific the parent tag
	 * @param tagName a string that specific the tag name want to created
	 * @return a status string indicate whether it is successful or not
	 */
    public static String create_new_tag(String sessionid, String parentTagName, String tagName) {
        String resultJsonString = "some problem existed inside the create_new_tag() function if you see this string";
        try {
            Log.d("current running function name:", "create_new_tag");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "create_new_tag"));
            nameValuePairs.add(new BasicNameValuePair("parent_tag", parentTagName));
            nameValuePairs.add(new BasicNameValuePair("tag", tagName));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            resultJsonString = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", resultJsonString);
            return resultJsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonString;
    }

    /**
	 * delete a specific tag given its absolutePath.
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param absolutePathForTheSpesificTag a string that specific the absolute path for the spesific Tag. eg. If you want to
	 * @return a status string indicate whether it is successful or not
	 * Example: if we want to delete the tag named:28102010Test4, and in this field, you will need to specify as: @/Pictures/28102010Test4
	 * the tag:28102010Test4 and his corresponding sub tags and files will be deleted permanently
	 */
    public static String remove_tag(String sessionid, String absolutePathForTheSpesificTag) {
        String resultJsonString = "some problem existed inside the create_new_tag() function if you see this string";
        try {
            Log.d("current running function name:", "remove_tag");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "remove_tag"));
            nameValuePairs.add(new BasicNameValuePair("absolute_tags", absolutePathForTheSpesificTag));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            resultJsonString = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", resultJsonString);
            return resultJsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonString;
    }

    /**
	 * delete a specific file given its key.
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param key a string that specific the file key
	 * @return a status string indicate whether it is successful or not
	 */
    public static String remove_file(String sessionid, String key) {
        String resultJsonString = "some problem existed inside the create_new_tag() function if you see this string";
        try {
            Log.d("current running function name:", "remove_file");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "remove_file"));
            nameValuePairs.add(new BasicNameValuePair("keys", key));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            resultJsonString = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", resultJsonString);
            return resultJsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonString;
    }

    /**
	 * move the source tags(one or more) into the destination tag(ONLY one)
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param absolutePathForTheSourceTags a string that specific the absolute path for the specific Tags. eg. If you want to
	 * move the tag named:28102010Test4 and in this field, you will need to specify as: @/Pictures/28102010Test4.
	 * If you have multiple tags to move, please follow this format: tag1;tag2;tag3, 
	 * please use the semicolon to seperate each tag
	 * @param absolutePathForTheDestinationTag the destination tag 
	 * @return a status string indicate whether it is successful or not
	 */
    public static String move_tags(String sessionid, String absolutePathForTheMovedTags, String absolutePathForTheDestinationTag) {
        String resultJsonString = "some problem existed inside the create_new_tag() function if you see this string";
        try {
            Log.d("current running function name:", "move_tags");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "move_tag"));
            nameValuePairs.add(new BasicNameValuePair("absolute_new_parent_tag", absolutePathForTheDestinationTag));
            nameValuePairs.add(new BasicNameValuePair("absolute_tags", absolutePathForTheMovedTags));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            resultJsonString = EntityUtils.toString(response.getEntity());
            return resultJsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonString;
    }

    /**
	 * move the files(one or more) into the destination tag(ONLY one)
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param keys a string that specific key for the specific files.
	 * If you have multiple files to move, please follow this format: key1;key2;key3, 
	 * please use the semicolon to seperate each key
	 * @param absolutePathForTheDestinationTag the destination tag 
	 * @return a status string indicate whether it is successful or not
	 */
    public static String move_files(String sessionid, String keys, String absolutePathForTheDestinationTag) {
        String resultJsonString = "some problem existed inside the create_new_tag() function if you see this string";
        try {
            Log.d("current running function name:", "move_files");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "move_file"));
            nameValuePairs.add(new BasicNameValuePair("absolute_new_parent_tag", absolutePathForTheDestinationTag));
            nameValuePairs.add(new BasicNameValuePair("keys", keys));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            resultJsonString = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", resultJsonString);
            return resultJsonString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultJsonString;
    }

    /**
	 * rename a specific file
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param key specify the file key for you to rename
	 * @param newFileName the new file name you want to set
	 */
    public static String rename_file(String sessionid, String key, String newFileName) {
        String jsonstring = "";
        try {
            Log.d("current running function name:", "rename_file");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "rename_file"));
            nameValuePairs.add(new BasicNameValuePair("new_name", newFileName));
            nameValuePairs.add(new BasicNameValuePair("key", key));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            jsonstring = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", jsonstring);
            return jsonstring;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonstring;
    }

    /**
	 * rename a specific tag
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param originalTag specify the original tag with absolute path for you to rename
	 * @param newTagName the new tag name you want to set
	 */
    public static String rename_tag(String sessionid, String originalTag, String newTagName) {
        String jsonstring = "";
        try {
            Log.d("current running function name:", "rename_tag");
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("https://mt0-app.cloud.cm/rpc/json");
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("c", "Storage"));
            nameValuePairs.add(new BasicNameValuePair("m", "rename_tag"));
            nameValuePairs.add(new BasicNameValuePair("new_tag_name", newTagName));
            nameValuePairs.add(new BasicNameValuePair("absolute_tag", originalTag));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            httppost.setHeader("Cookie", "PHPSESSID=" + sessionid);
            HttpResponse response = httpclient.execute(httppost);
            jsonstring = EntityUtils.toString(response.getEntity());
            Log.d("jsonStringReturned:", jsonstring);
            return jsonstring;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonstring;
    }

    /**
	 * download a specific file from CLOUD using the file key
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param key use the key file to specify the file you want to download
	 */
    public static InputStream download_file(String sessionid, String key) {
        InputStream is = null;
        String urlString = "https://s2.cloud.cm/rpc/raw?c=Storage&m=download_file&key=" + key;
        try {
            String apple = "";
            URL url = new URL(urlString);
            Log.d("current running function name:", "download_file");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Cookie", "PHPSESSID=" + sessionid);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            is = conn.getInputStream();
            return is;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("download problem", "download problem");
        }
        return is;
    }

    /**
	 * upload a specific file from your device to CLOUD remote server
	 * @param sessionid a string that communicate with the CLOUD remote server for the specific session
	 * @param localFilePath the local device path that specify the file on the mobile device
	 * @param remoteTagPath the remote CLOUD server tag path that specify the tag location you want to store this file
	 * For example: 
	 * on mobile device, the localFilePath will something like: /data/data/record16.JPG
	 * on remote cloud server, the remoteTagPath will something like: @/Pictures/Sample 
	 */
    public static String upload_file(String sessionid, String localFilePath, String remoteTagPath) {
        String jsonstring = "If you see this message, there is some problem inside the function:upload_file()";
        String srcPath = localFilePath;
        String uploadUrl = "https://s2.cloud.cm/rpc/json/?session_id=" + sessionid + "&c=Storage&m=upload_file&tag=" + remoteTagPath;
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "******";
        try {
            URL url = new URL(uploadUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + srcPath.substring(srcPath.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);
            FileInputStream fis = new FileInputStream(srcPath);
            byte[] buffer = new byte[8192];
            int count = 0;
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            jsonstring = br.readLine();
            dos.close();
            is.close();
            return jsonstring;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonstring;
    }
}
