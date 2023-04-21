package com.haliyoo.adhere.handler;

import java.net.MalformedURLException;
import java.net.URL;
import com.haliyoo.adhere.R;
import com.haliyoo.adhere.activity.CreativeDesignActivity;
import com.haliyoo.adhere.activity.CreativeHallActivity;
import com.haliyoo.adhere.activity.HallActivity;
import com.haliyoo.adhere.activity.LoginActivity;
import com.haliyoo.adhere.bean.WeiboBean;
import com.haliyoo.adhere.controller.AdUserConstant;
import com.haliyoo.adhere.controller.OAuthConstant;
import com.haliyoo.adhere.util.BitmapGet;
import com.haliyoo.adhere.widget.PromptDialog;
import weibo4android.User;
import weibo4android.Weibo;
import weibo4android.WeiboException;
import weibo4android.http.AccessToken;
import weibo4android.http.RequestToken;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyCharmHandler implements OnClickListener {

    private static String TAG = "MyCharmHandler";

    private static MyCharmHandler instance = null;

    private static Context context = null;

    private static String strUserId = null;

    private static final int MSG_SET_CHARM = 0;

    private SetCharmHandler setCharmHandler = new SetCharmHandler();

    private User user = null;

    private int nMyCreative = 0;

    private int nMyCharm = 0;

    private int nMyContributions = 0;

    private int nMyColleagues = 0;

    public static synchronized MyCharmHandler getInstance(Context context) {
        if (HallActivity.getCheckedId() != R.id.radio_charm) {
            return null;
        }
        if (instance == null) {
            instance = new MyCharmHandler(context);
        }
        return instance;
    }

    public MyCharmHandler(Context context) {
        if (HallActivity.getCheckedId() != R.id.radio_charm) {
            return;
        }
        this.context = context;
        LayoutInflater inflater = LayoutInflater.from(context);
        RelativeLayout rlTitle = (RelativeLayout) ((Activity) context).findViewById(R.id.main_title);
        View vTitle = inflater.inflate(R.layout.title_write_refresh, null);
        rlTitle.removeAllViews();
        rlTitle.addView(vTitle);
        LinearLayout llContent = (LinearLayout) ((Activity) context).findViewById(R.id.llContent);
        View vContent = inflater.inflate(R.layout.user_charm, null);
        llContent.removeAllViews();
        llContent.addView(vContent);
        ImageButton ibWrite = (ImageButton) ((Activity) context).findViewById(R.id.IMAGE_BUTTON_WRITE);
        ibWrite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (HallActivity.getCheckedId() != R.id.radio_charm) {
                    return;
                }
                Intent intent = new Intent(MyCharmHandler.context, CreativeDesignActivity.class);
                ((Activity) MyCharmHandler.context).startActivity(intent);
            }
        });
        ImageButton ibRefresh = (ImageButton) ((Activity) context).findViewById(R.id.IMAGE_BUTTON_REFRESH);
        ibRefresh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (HallActivity.getCheckedId() != R.id.radio_charm) {
                    return;
                }
                setCharmByUserId(strUserId);
            }
        });
        TextView tvTitle = (TextView) ((Activity) context).findViewById(R.id.TEXT_VIEW_TITLE);
        tvTitle.setText(context.getString(R.string.STR_MY_CHARM));
    }

    /**
	 * 设置用户魅力
	 * @param strUserId 用户ID
	 */
    public void setCharmByUserId(String strUserId) {
        if (HallActivity.getCheckedId() != R.id.radio_charm) {
            return;
        }
        this.strUserId = strUserId;
        refreshShowProgressBar(true);
        new Thread() {

            @Override
            public void run() {
                if (HallActivity.getCheckedId() != R.id.radio_charm) {
                    return;
                }
                if (AdUserConstant.getInstance().count(AdUserConstant.getInstance().user, AdUserConstant.getInstance().sign, AdUserConstant.getInstance().user) == 1) {
                    nMyCreative = AdUserConstant.creativeCount;
                    nMyCharm = AdUserConstant.charmCount;
                    nMyContributions = AdUserConstant.contributionsCount;
                    nMyColleagues = AdUserConstant.colleaguesCount;
                }
                try {
                    user = OAuthConstant.getInstance().getWeibo().showUser(MyCharmHandler.strUserId);
                } catch (WeiboException e) {
                    e.printStackTrace();
                }
                setCharmHandler.sendEmptyMessage(MSG_SET_CHARM);
            }
        }.start();
    }

    void refreshShowProgressBar(boolean isShow) {
        if (HallActivity.getCheckedId() != R.id.radio_charm) {
            return;
        }
        ImageButton ibRefresh = (ImageButton) ((Activity) context).findViewById(R.id.IMAGE_BUTTON_REFRESH);
        ProgressBar pbRefresh = (ProgressBar) ((Activity) context).findViewById(R.id.PROGRESS_BAR_WAITING);
        if (isShow) {
            ibRefresh.setVisibility(View.GONE);
            pbRefresh.setVisibility(View.VISIBLE);
        } else {
            ibRefresh.setVisibility(View.VISIBLE);
            pbRefresh.setVisibility(View.GONE);
        }
    }

    class SetCharmHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            if (HallActivity.getCheckedId() != R.id.radio_charm) {
                return;
            }
            if (msg.what == MSG_SET_CHARM) {
                setCharm();
                refreshShowProgressBar(false);
            }
            super.handleMessage(msg);
        }
    }

    /**
	 * 设置用户魅力
	 */
    public void setCharm() {
        if (HallActivity.getCheckedId() != R.id.radio_charm) {
            return;
        }
        if (user == null) {
            return;
        }
        ImageView ivProfile = (ImageView) ((Activity) context).findViewById(R.id.ivPortrait);
        Bitmap bitmap = BitmapGet.getHttpBitmap(user.getProfileImageURL());
        ivProfile.setImageBitmap(bitmap);
        Log.i(TAG, "image uri:" + user.getAvatarLarge());
        TextView tvUser = (TextView) ((Activity) context).findViewById(R.id.tvNick);
        tvUser.setText(user.getName());
        TextView tvLocation = (TextView) ((Activity) context).findViewById(R.id.tvAddress1_content);
        tvLocation.setText(user.getLocation());
        TextView tvSignature = (TextView) ((Activity) context).findViewById(R.id.tvAccount_introduce_content);
        tvSignature.setText(user.getDescription());
        ImageView ivGender = (ImageView) ((Activity) context).findViewById(R.id.ivGental);
        if (user.getGender().equals("m")) {
            ivGender.setImageResource(R.drawable.male);
        } else {
            ivGender.setImageResource(R.drawable.female);
        }
        TextView tvMyCreative = (TextView) ((Activity) context).findViewById(R.id.tvAttention_count);
        TextView tvMyCharm = (TextView) ((Activity) context).findViewById(R.id.tvWeibo_count);
        TextView tvMyContributions = (TextView) ((Activity) context).findViewById(R.id.tvFans_count);
        TextView tvMyColleagues = (TextView) ((Activity) context).findViewById(R.id.tvTopic_count);
        tvMyCreative.setText(nMyCreative + "");
        tvMyCharm.setText(nMyCharm + "");
        tvMyContributions.setText(nMyContributions + "");
        tvMyColleagues.setText(nMyColleagues + "");
        LinearLayout llMyCreative = (LinearLayout) ((Activity) context).findViewById(R.id.llAttention);
        LinearLayout llMyCharm = (LinearLayout) ((Activity) context).findViewById(R.id.rlWeibo);
        LinearLayout llMyContributions = (LinearLayout) ((Activity) context).findViewById(R.id.llFans);
        LinearLayout llMyColleagues = (LinearLayout) ((Activity) context).findViewById(R.id.llTopic);
        llMyCreative.setOnClickListener(this);
        llMyCharm.setOnClickListener(this);
        llMyContributions.setOnClickListener(this);
        llMyColleagues.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (HallActivity.getCheckedId() != R.id.radio_charm) {
            return;
        }
        int id = v.getId();
        Intent intent = new Intent();
        switch(id) {
            default:
                Log.e(TAG, "error id touched.");
                break;
        }
    }
}
