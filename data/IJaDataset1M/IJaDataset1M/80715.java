package edu.ycp.android.bowlingconcepts;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class ShirtImage {

    public ShirtImage(String link, String imageURL, String description) throws IOException {
        this.link = link;
        setImage(imageURL);
        this.description = description;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setImage(String imageURL) throws IOException {
        URL url = new URL(imageURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        image = BitmapFactory.decodeStream(is);
    }

    public Bitmap getImage() {
        return image;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    private String link;

    private Bitmap image;

    private String description;
}
