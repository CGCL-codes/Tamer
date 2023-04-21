package com.hcs.receiver;

import java.util.Date;
import java.util.Iterator;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import com.hcs.R;
import com.hcs.activity.TransferAct;
import com.hcs.protocol.ISetUpService;
import com.hcs.protocol.impl.SetUpService;
import com.hcs.protocol.model.PollingContent;
import com.hcs.protocol.model.TPurchaseinadvanceWeb;
import com.hcs.protocol.model.TWorkBulletinWeb;
import com.hcs.protocol.utils.MyUtils;
import com.hcs.utils.DatabaseHelper;

public class PollingReceiver extends BroadcastReceiver {

    ISetUpService setUpService = null;

    private int updateBuyCount;

    private int updateWorkCount;

    boolean flag = true;

    private Context context;

    private Intent intent;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        Thread thread = new Thread(new PollingThread());
        thread.start();
    }

    /**
	 * 轮询线程
	 */
    class PollingThread implements Runnable {

        @Override
        public void run() {
            setUpService = new SetUpService();
            PollingContent pollingContent = setUpService.getPolling();
            if (null != pollingContent && (pollingContent.getBuyCount() != 0 || pollingContent.getWorkCount() != 0)) {
                if (updateDB(pollingContent) == 1) {
                    String service = Activity.NOTIFICATION_SERVICE;
                    NotificationManager nm = (NotificationManager) context.getSystemService(service);
                    Notification n = new Notification();
                    int icon = n.icon = R.drawable.icon;
                    String tickerText = "信息获取结果";
                    long when = System.currentTimeMillis();
                    n.icon = icon;
                    n.tickerText = tickerText;
                    n.when = when;
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(context, TransferAct.class);
                    PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    n.defaults = Notification.DEFAULT_ALL;
                    n.flags = Notification.FLAG_AUTO_CANCEL;
                    StringBuffer notificationContent = new StringBuffer();
                    notificationContent.append("获取");
                    if (updateBuyCount != 0) {
                        notificationContent.append("" + updateBuyCount + "条预购信息     ");
                    }
                    if (updateWorkCount != 0) {
                        notificationContent.append("" + updateWorkCount + "条工作简报信息");
                    }
                    n.setLatestEventInfo(context, "信息获取结果", notificationContent.toString(), pi);
                    Date now = new Date();
                    nm.notify(now.getHours() * 100 + now.getMinutes(), n);
                }
            }
        }
    }

    /**
	 * 根据轮询获取的数据更新本地库数据
	 * @return 操作结果 1：成功  0：失败
	 */
    private int updateDB(PollingContent pollingContent) {
        updateBuyCount = 0;
        updateWorkCount = 0;
        String ordersID = "";
        String bulltinsID = "";
        StringBuffer ordersIDSB = new StringBuffer();
        StringBuffer bulltinsIDSB = new StringBuffer();
        SQLiteDatabase db = new DatabaseHelper(context).openDatabase();
        try {
            db.beginTransaction();
            if (null != pollingContent.getPawList() && !pollingContent.getPawList().isEmpty()) {
                Iterator iterPurchar = pollingContent.getPawList().iterator();
                while (iterPurchar.hasNext()) {
                    TPurchaseinadvanceWeb piaWeb = (TPurchaseinadvanceWeb) iterPurchar.next();
                    int isSuccess = 0;
                    ContentValues values = new ContentValues();
                    values.put("CreateId", piaWeb.getCreateId());
                    values.put("CreateDate", MyUtils.dateFormat("yyyy-MM-dd kk:mm:ss", piaWeb.getCreateDate()));
                    values.put("AccountName", piaWeb.getAccountName());
                    values.put("MobilePhone_c", piaWeb.getMobilePhoneC());
                    values.put("Probability_c", piaWeb.getProbabilityC());
                    values.put("DateOfDelivery_c", piaWeb.getDateOfDeliveryC());
                    values.put("Product_c", piaWeb.getProductC());
                    values.put("Price_c", piaWeb.getPriceC());
                    values.put("CustomerStatus_c", piaWeb.getCustomerStatusC());
                    values.put("OldForNewService_c", piaWeb.getOldForNewServiceC());
                    values.put("Concern_c", piaWeb.getConcernC());
                    values.put("Competitor_c", piaWeb.getCompetitorId());
                    values.put("Comments_c", piaWeb.getCommentsC());
                    values.put("Salesname_c", piaWeb.getStaffNameC());
                    values.put("EvaluationComment_c", piaWeb.getEvaluationCommentC());
                    if (null != piaWeb.getFeedbackDateC() && !"".equals(piaWeb.getFeedbackDateC())) {
                        values.put("feedback_date_c", MyUtils.dateFormat("yyyy-MM-dd hh:mm:ss", piaWeb.getFeedbackDateC()));
                    }
                    SharedPreferences pre = context.getSharedPreferences("type", Context.MODE_WORLD_WRITEABLE);
                    String typecontent = pre.getString("type", "");
                    values.put("SendFlag", piaWeb.getSendFlag());
                    if (typecontent.equals("manager")) {
                        values.put("ReceiverFlag", 1);
                        String sql = " select * from t_PurchaseInAdvance_mapp where CreateId= " + piaWeb.getCreateId() + " and receiverflag=1 ";
                        Cursor cursor = db.rawQuery(sql, null);
                        if (cursor.getCount() == 1) {
                            if (db.update("t_PurchaseInAdvance_mapp", values, "CreateId=? and SendFlag=1 and receiverflag=1 ", new String[] { piaWeb.getCreateId() }) > 0) {
                                isSuccess = 2;
                            }
                        } else {
                            if (db.insert("t_PurchaseInAdvance_mapp", null, values) > 0) {
                                isSuccess = 1;
                            }
                        }
                    } else {
                        values.put("ReceiverFlag", 0);
                        String sql = " select * from t_PurchaseInAdvance_mapp where CreateId= " + piaWeb.getCreateId() + " and receiverflag=0 ";
                        Cursor cursor = db.rawQuery(sql, null);
                        if (cursor.getCount() == 1) {
                            if ((db.update("t_PurchaseInAdvance_mapp", values, "CreateId=? and receiverflag=0", new String[] { piaWeb.getCreateId() })) > 0) {
                                isSuccess = 1;
                            }
                        } else {
                            if (db.insert("t_PurchaseInAdvance_mapp", null, values) > 0) {
                                isSuccess = 1;
                            }
                        }
                    }
                    if (isSuccess != 0) {
                        ordersIDSB = ordersIDSB.append(piaWeb.getCreateId() + ",");
                        if (isSuccess == 1) {
                            updateBuyCount += 1;
                        }
                    }
                }
            }
            if (null != pollingContent.getWbwList() && !pollingContent.getWbwList().isEmpty()) {
                Iterator iterBullet = pollingContent.getWbwList().iterator();
                while (iterBullet.hasNext()) {
                    TWorkBulletinWeb workBulletinWeb = (TWorkBulletinWeb) iterBullet.next();
                    int isSuccess = 0;
                    ContentValues values = new ContentValues();
                    values.put("Type", workBulletinWeb.getType());
                    values.put("Day_Bulletin", workBulletinWeb.getDayBulletin());
                    values.put("Mouth_Bulletin", workBulletinWeb.getMouthBulletin());
                    values.put("Create_Time", MyUtils.dateFormat("yyyy-MM-dd hh:mm:ss", workBulletinWeb.getCreateTime()));
                    String sql = " select * from t_Work_Bulletin_mapp where Create_Time='" + MyUtils.dateFormat("yyyy-MM-dd hh:mm:ss", workBulletinWeb.getCreateTime()) + "'";
                    Cursor cursor = db.rawQuery(sql, null);
                    if (cursor.getCount() == 1) {
                        if (db.update("t_Work_Bulletin_mapp", values, "Create_Time=?", new String[] { MyUtils.dateFormat("yyyy-MM-dd hh:mm:ss", workBulletinWeb.getCreateTime()) }) > 0) {
                            isSuccess = 2;
                        }
                    } else {
                        if (db.insert("t_Work_Bulletin_mapp", null, values) > 0) {
                            isSuccess = 1;
                        }
                    }
                    if (isSuccess != 0) {
                        bulltinsIDSB = bulltinsIDSB.append(workBulletinWeb.getWorkBulletinId() + ",");
                        if (isSuccess == 1) {
                            updateWorkCount += 1;
                        }
                    }
                }
            }
            if (null != ordersIDSB.toString() && !"".equals(ordersIDSB.toString())) {
                ordersID = ordersIDSB.substring(0, ordersIDSB.length() - 1);
            }
            if (null != bulltinsIDSB.toString() && !"".equals(bulltinsIDSB.toString())) {
                bulltinsID = bulltinsIDSB.substring(0, bulltinsIDSB.length() - 1);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            try {
                setUpService.feedBackPolling(ordersID, bulltinsID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (updateBuyCount != 0 || updateWorkCount != 0) {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.close();
        }
        return 0;
    }
}
