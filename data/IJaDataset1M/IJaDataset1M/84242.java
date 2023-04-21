package com.shengyijie.context;

import com.shengyijie.model.object.Listobject.GuesstBookList;
import com.shengyijie.model.object.baseobject.GuesstBook;
import com.shengyijie.model.object.baseobject.Project;
import com.shengyijie.model.object.baseobject.PushMessage;
import com.shengyijie.model.object.baseobject.User;
import android.app.Activity;
import android.app.Application;
import android.widget.Button;

public class ContextApplication extends Application {

    public static String TAG = "syj_log";

    public static boolean isEnterprise = false;

    public static String[] list;

    public int vH = 0;

    public int vW = 0;

    public static int prePage;

    public static int tabtype = 0;

    public static boolean isMessageRefresh = false;

    public static boolean isAttentionRefresh = false;

    public static String shareName = "";

    public static String shareUrl = "";

    public static String shareContent = "";

    public static Project project;

    public static PushMessage pushMessage;

    public static GuesstBookList guesstBookList;

    public static GuesstBook guesstBook;

    public static int industry_id = 1;

    public static int city_id = 1;

    public static String keyword = "";

    public static Activity activity;

    public static String itemName;

    public Button right;

    public static boolean isUserLogin = false;

    public static final int PAGE_CATEGORY = 1;

    public static final int PAGE_HOME = 0;

    public static final int PAGE_RECOMMENDED = 2;

    public static final int PAGE_USER = 3;

    public static final int PAGE_MORE = 4;

    public static final int PAGE_USERCENTER = 6;

    public static final int PAGE_ATTENTION = 7;

    public static final int PAGE_MESSAGE = 8;

    public static int USER_TYPE = 0;

    public static final int USER_TYPE_PERSONAL = 0;

    public static final int USER_TYPE_ENTERPRISE = 1;

    public static final int RESULT_CODE_1000 = 1000;

    public static final int RESULT_CODE_1001 = 1001;

    public static final int RESULT_CODE_1002 = 1002;

    public static final int RESULT_CODE_1003 = 1003;

    public static final int RESULT_CODE_1004 = 1004;

    public static final int RESULT_CODE_1005 = 1005;

    public static final int RESULT_CODE_1006 = 1006;

    public static final int RESULT_CODE_1007 = 1007;

    public static final int RESULT_CODE_OK = 2000;

    public static User user = new User();

    public void onCreate() {
        super.onCreate();
    }
}
