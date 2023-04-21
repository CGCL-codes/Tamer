package cn.chengdu.in.type;

import java.io.Serializable;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import cn.chengdu.in.android.util.AndroidUtil;

/**
 * @author Declan.Z(declan.zhang@gmail.com)
 * @date 2011-12-22
 */
public class Banner implements IcdType, Serializable {

    private int id;

    private String udpi;

    private String hdpi;

    private String mdpi;

    private String content;

    private int type;

    private String att;

    public static final int TYPE_WEB_URL = 1 << 1;

    public static final int TYPE_USER = 1 << 2;

    public static final int TYPE_PLACE = 1 << 3;

    public static final int TYPE_BADGE = 1 << 4;

    public static final int NONE_BANNER_ID = -1;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUdpi() {
        return udpi;
    }

    public void setUdpi(String udpi) {
        this.udpi = udpi;
    }

    public String getHdpi() {
        return hdpi;
    }

    public void setHdpi(String hdpi) {
        this.hdpi = hdpi;
    }

    public String getMdpi() {
        return mdpi;
    }

    public void setMdpi(String mdpi) {
        this.mdpi = mdpi;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        if (content != null) {
            String[] arr = content.split("://");
            if ("http".equalsIgnoreCase(arr[0])) {
                type = TYPE_WEB_URL;
                att = content;
            } else if ("user".equalsIgnoreCase(arr[0])) {
                type = TYPE_USER;
                att = arr[1];
            } else if ("badge".equalsIgnoreCase(arr[0])) {
                type = TYPE_BADGE;
                att = arr[1];
            } else if ("place".equalsIgnoreCase(arr[0])) {
                type = TYPE_PLACE;
                att = arr[1];
            }
        }
    }

    public int getType() {
        return type;
    }

    public String getAtt() {
        return att;
    }

    /**
     * 根据设备返回适合的uri
     * @return
     */
    public Uri getUri(Context context) {
        int width = AndroidUtil.getScreenWidth((Activity) context);
        String uri;
        if (width > 480) {
            uri = udpi;
        } else if (width < 480) {
            uri = mdpi;
        } else {
            uri = hdpi;
        }
        return Uri.parse(uri);
    }
}
