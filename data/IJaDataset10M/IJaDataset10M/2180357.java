package com.whale.util;

import android.content.Context;
import android.widget.Toast;

public class MsgBox {

    public static void showErrMsg(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     */
    public static void showNetworkErr(Context context) {
        Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     * @param string
     */
    public static void showFieldEmptyErr(Context context, String fieldName) {
        Toast.makeText(context, fieldName + "为空", Toast.LENGTH_SHORT).show();
    }

    /**
     * @param context
     */
    public static void showLogout(Context context) {
        Toast.makeText(context, "注销登出，请重新登录", Toast.LENGTH_SHORT).show();
    }

    public static void ShowSendSucced(Context context) {
        Toast.makeText(context, "发送成功", Toast.LENGTH_SHORT).show();
    }

    public static void ShowSendFailure(Context context) {
        Toast.makeText(context, "网络连接不上，发送失败", Toast.LENGTH_SHORT).show();
    }

    public static void showEmailErr(Context context) {
        Toast.makeText(context, "email格式不正确", Toast.LENGTH_SHORT).show();
    }
}
