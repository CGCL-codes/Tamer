package com.shengyijie.activity.share;

import com.shengyijie.model.object.baseobject.RenrenData;
import weibo4android.Weibo;

public class ShareConstant {

    public static int SHARE_SINA = 1;

    public static int SHARE_RENREN = 2;

    public static int SHARE_TENCNT = 3;

    public static int SHARE_MESSAGE = 4;

    public static int SHARE_EMAIL = 5;

    public static int share_type = SHARE_SINA;

    public static String RENREN_API_KEY = "2852afca250a407b8cd6daa5de05554b";

    public static String RENREN_API_SECRET = "f92dbf968b3c42bc829a4a19724e577e";

    public static String token = null;

    public static String tokenSecret = null;

    public static String TENCENTWEIBO_API_KEY = "801080441";

    public static String TENCENTWEIBO_API_SECRET = "a4595e358940958260d50ce7bfc2587e";

    public static Weibo weibo = null;

    public static ShareConstant instance = null;

    private RenrenData renrenData = null;

    public ShareConstant() {
    }

    ;

    public static synchronized ShareConstant getInstance() {
        if (instance == null) instance = new ShareConstant();
        return instance;
    }

    public Weibo getWeibo() {
        if (weibo == null) weibo = new Weibo();
        return weibo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    public void setTokenSecret(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    public RenrenData getRenrenData() {
        return renrenData;
    }

    public void setRenrenData(RenrenData renrenData) {
        this.renrenData = renrenData;
    }
}
