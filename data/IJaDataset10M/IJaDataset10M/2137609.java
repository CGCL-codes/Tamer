package com.android.zweibo.bean;

import android.graphics.drawable.Drawable;

public class UserInfo {

    private Long id;

    private String userId;

    private String userName;

    private String token;

    private String tokenSecret;

    private String isDefault;

    private Drawable userIcon;

    public static final String TB_NAME = "UserInfo";

    public static final String ID = "_id";

    public static final String USER_ID = "userId";

    public static final String USER_NAME = "userName";

    public static final String TOKEN = "token";

    public static final String TOKEN_SECRET = "tokenSecret";

    public static final String IS_DEFAULT = "isDefault";

    public static final String USER_ICON = "userIcon";

    public UserInfo() {
        super();
    }

    public UserInfo(String userID, String userName, String token, String tokenSecret, String isDefault) {
        super();
        this.userId = userID;
        this.userName = userName;
        this.token = token;
        this.tokenSecret = tokenSecret;
        this.isDefault = isDefault;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    public Drawable getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(Drawable userIcon) {
        this.userIcon = userIcon;
    }
}
