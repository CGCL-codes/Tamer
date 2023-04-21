package orxatas.travelme.entity;

import android.graphics.Bitmap;

/**
 * Entidad fotografía.
 * */
public class Photo {

    private int idOffline;

    private int idOnline;

    private String url;

    private String localPath;

    public Photo(int idOffline, int idOnline, String url) {
        this.idOffline = idOffline;
        this.idOnline = idOnline;
        this.url = url;
    }

    public Photo(int idOffline, String localPath) {
        this.idOffline = idOffline;
        this.localPath = localPath;
    }

    public int getIdOffline() {
        return idOffline;
    }

    public int getIdOnline() {
        return idOnline;
    }

    public void setIdOnline(int idOnline) {
        this.idOnline = idOnline;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }
}
