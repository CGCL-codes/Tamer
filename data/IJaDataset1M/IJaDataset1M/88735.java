package com.clouds.aic.tools;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
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

public class LocalPhoneFileManagementService {

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
	 * return a List, including all the files and tags(only two types defined in CLOUD) that shown on the user's specific tag(folder).
	 * @param tag a tag use for specify the specific tag(folder) the user want to open
	 * @return a list object with JsonObject object in the list, JsonObject is defined by Yi, please inform him for more information.
	 * 
	 */
    public static List<JsonObject> get_contents_by_tag_local(String tag) {
        ArrayList files = new ArrayList();
        String filePath = "/" + tag;
        File[] filesInFileFormat = new File(filePath).listFiles();
        for (File file : filesInFileFormat) {
            String content_type = "";
            if (file.isFile()) {
                content_type = "file";
            } else if (file.isDirectory()) {
                content_type = "tag";
            } else {
                content_type = "unknown";
            }
            JsonObject jsonObjectInstance = new JsonObject();
            List<JsonPair> elements = new ArrayList<JsonPair>();
            JsonPair jsonPairInstance = new JsonPair("name", file.getName());
            JsonPair jsonPairInstance2 = new JsonPair("content_type", content_type);
            elements.add(jsonPairInstance);
            elements.add(jsonPairInstance2);
            jsonObjectInstance.elements = elements;
            files.add(jsonObjectInstance);
        }
        return files;
    }

    /**
	 * create a new tag(folder) under a certain tag(folder) in local phone.
	 * 
	 * @param parentTagName a string that specific the parent tag
	 * @param tagName a string that specific the tag name want to created
	 * @return a status string indicate whether it is successful or not
	 */
    public static String create_new_tag_local(String parentTagName, String tagName) {
        String resultJsonString = "some problem existed inside the create_new_tag() function if you see this string";
        Log.d("current running function name:", "create_new_tag_local");
        Log.d("parentTagName", parentTagName);
        Log.d("tagName", tagName);
        String filePath = "/" + parentTagName + "/" + tagName;
        File fileHandler = new File(filePath);
        if (fileHandler.mkdirs()) {
            resultJsonString = "Folder been created";
        } else {
            resultJsonString = "target directory already exists or one of the directories can not be created";
        }
        return resultJsonString;
    }

    /**
	 * Please Do Not use this function at this moment.
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
	 * Please Do Not use this function at this moment.
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
	 * Please Do Not use this function at this moment.
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
	 * Please Do Not use this function at this moment.
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
	 * Please Do Not use this function at this moment.
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
	 * Please Do Not use this function at this moment.
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
