package com.commandus.util;

import android.util.Log;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Upload byte array to the server using HTTP POST method
 * @author andrei
 *
 */
public class BinaryUploader {

    /**
	 * upload byte array to the server 
	 * @param src			file to upload
	 * @param contenttype	file content type
	 * @param name			parameter name
	 * @param uri			URI to upload
	 * @param timeout		tome out in ms
	 * @return				content stream
	 */
    public static InputStream upload(byte[] src, String contenttype, String name, URI uri, int timeout) {
        HttpPost httppost;
        httppost = new HttpPost(uri);
        HttpResponse response;
        try {
            ByteArrayEntity ise = new ByteArrayEntity(src);
            httppost.setEntity(ise);
            DefaultHttpClient httpclient = new DefaultHttpClient();
            response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            Log.d("httpPost", "get: " + response.getStatusLine());
            if (entity != null) {
                entity.consumeContent();
            }
            return entity.getContent();
        } catch (Exception e) {
            Log.e("upload", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
