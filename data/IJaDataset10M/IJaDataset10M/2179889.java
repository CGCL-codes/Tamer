package com.trinea.sns.activity;

import java.util.List;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.trinea.sns.entity.StatusInfo;
import com.trinea.sns.entity.UserInfo;
import com.trinea.sns.service.SnsService;
import com.trinea.sns.serviceImpl.OAuthServiceImpl;
import com.trinea.sns.serviceImpl.SnsServiceImpl;
import com.trinea.sns.util.SnsConstant;
import com.trinea.sns.utilImpl.FileUtils;
import com.trinea.sns.utilImpl.LongUtils;

public class ViewStatusDetailActivity extends Activity implements OnTouchListener, OnGestureListener {

    private Intent intent = null;

    private String website;

    private long statusId;

    private StatusInfo statusInfo;

    private SnsServiceImpl snsServiceImpl = new SnsServiceImpl();

    private OAuthServiceImpl oAuthService = new OAuthServiceImpl();

    private SnsService snsService;

    private Thread operateThread;

    private Context context;

    private ProgressDialog progressDialog = null;

    private EHandler handler = new EHandler();

    public ScrollView scrollView;

    public ImageView statusImage;

    public ImageView userIcon;

    public TextView userDetailInfo;

    public TextView time;

    public TextView statusContent;

    public TextView sourceStatusDetail;

    private int scrollViewY = 0;

    private long[] statusIdArray = null;

    GestureDetector gestureDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.view_status_detail_activity);
        context = getApplicationContext();
        progressDialog = new ProgressDialog(ViewStatusDetailActivity.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        intent = this.getIntent();
        if (!intent.equals(null)) {
            website = intent.getStringExtra(SnsConstant.WEBSITE_TYPE);
            if (SnsConstant.WEB_NAME_MAP.containsKey(website)) {
                snsService = snsServiceImpl.getSnsService(website);
                statusId = intent.getLongExtra("statusId", 0);
                statusIdArray = intent.getLongArrayExtra("statusIdArray");
                if (snsService != null && statusId != 0) {
                    initView();
                }
                if (statusIdArray != null && statusIdArray.length > 0) {
                    gestureDetector = new GestureDetector((OnGestureListener) this);
                    LinearLayout viewSnsLayout = (LinearLayout) findViewById(R.id.viewStatusDetailLY);
                    viewSnsLayout.setOnTouchListener(this);
                    viewSnsLayout.setLongClickable(true);
                }
            }
        }
    }

    public void initView() {
        setProgressBarIndeterminateVisibility(true);
        progressDialog.setMessage("��ݻ�ȡ��,���Ժ�...");
        progressDialog.show();
        if (scrollView == null) {
            scrollView = (ScrollView) findViewById(R.id.viewStatusDetailSV);
        }
        if (userIcon == null) {
            userIcon = (ImageView) findViewById(R.id.userDetailIconImageView);
        }
        if (userDetailInfo == null) {
            userDetailInfo = (TextView) findViewById(R.id.userDetailInfoTextView);
        }
        if (statusContent == null) {
            statusContent = (TextView) findViewById(R.id.statusDetailTextView);
        }
        if (sourceStatusDetail == null) {
            sourceStatusDetail = (TextView) findViewById(R.id.sourceStatusDetailTextView);
        }
        if (statusImage == null) {
            statusImage = (ImageView) findViewById(R.id.statusDetailImageView);
        }
        userIcon.setImageDrawable(null);
        userDetailInfo.setText(null);
        statusContent.setText(null);
        sourceStatusDetail.setText(null);
        statusImage.setImageDrawable(null);
        if (operateThread == null || !operateThread.isAlive()) {
            operateThread = new Thread() {

                public void run() {
                    List<UserInfo> userInfoList = oAuthService.getWebsiteSelectedUserInfo(context, website);
                    for (UserInfo userInfo : userInfoList) {
                        statusInfo = snsService.getStatus(userInfo, statusId);
                        handler.sendEmptyMessage(1);
                        break;
                    }
                }
            };
            operateThread.start();
        }
        scrollView.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (scrollView.getScrollY() == 0) {
                        Intent intent = new Intent(ViewStatusDetailActivity.this, ViewStatusActivity.class);
                        intent.putExtra(SnsConstant.WEBSITE_TYPE, website);
                        startActivity(intent);
                    } else if (scrollView.getScrollY() - scrollViewY < 2 && scrollView.getScrollY() >= scrollViewY) {
                    } else {
                        scrollViewY = scrollView.getScrollY();
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        progressDialog = new ProgressDialog(ViewStatusDetailActivity.this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        if (!intent.equals(null)) {
            website = intent.getStringExtra(SnsConstant.WEBSITE_TYPE);
            if (SnsConstant.WEB_NAME_MAP.containsKey(website)) {
                snsService = snsServiceImpl.getSnsService(website);
                statusId = intent.getLongExtra("statusId", 0);
                statusIdArray = intent.getLongArrayExtra("statusIdArray");
                if (snsService != null && statusId != 0) {
                    initView();
                }
                if (statusIdArray != null && statusIdArray.length > 0) {
                    gestureDetector = new GestureDetector((OnGestureListener) this);
                    LinearLayout viewSnsLayout = (LinearLayout) findViewById(R.id.viewStatusDetailLY);
                    viewSnsLayout.setOnTouchListener(this);
                    viewSnsLayout.setLongClickable(true);
                }
            }
        }
    }

    class EHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case 1:
                    if (statusInfo != null) {
                        userDetailInfo.setText(statusInfo.getUser().getUserName() + "\n��˿��" + statusInfo.getUser().getFollowersCount() + "\n΢����" + statusInfo.getUser().getStatusesCount());
                        statusContent.setText(statusInfo.getStatusContent());
                        if (statusInfo.getSourceStatus() != null) {
                            sourceStatusDetail.setText(statusInfo.getSourceStatus().getStatusContent());
                        }
                        Drawable iconDrawable = FileUtils.loadImageFromUrl(statusInfo.getUser().getIconUrl());
                        userIcon.setImageDrawable(iconDrawable);
                        if (statusInfo.isContainImage()) {
                            Drawable imageDrawable = FileUtils.loadImageFromUrl(statusInfo.getMiddlePictureUrl());
                            statusImage.setImageDrawable(imageDrawable);
                        }
                    }
                    progressDialog.dismiss();
                    setProgressBarIndeterminateVisibility(false);
                    break;
            }
        }
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > SnsConstant.FLING_MIN_DISTANCE && Math.abs(velocityX) > SnsConstant.FLING_MIN_VELOCITY) {
            long nextId = LongUtils.getNext(statusIdArray, statusId, -1);
            if (nextId == -1) {
                Toast.makeText(context, "û����һ��״̬", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ViewStatusDetailActivity.this, ViewStatusDetailActivity.class);
                intent.putExtra(SnsConstant.WEBSITE_TYPE, website);
                intent.putExtra("statusId", nextId);
                intent.putExtra("statusIdArray", statusIdArray);
                startActivity(intent);
            }
        } else if (e2.getX() - e1.getX() > SnsConstant.FLING_MIN_DISTANCE && Math.abs(velocityX) > SnsConstant.FLING_MIN_VELOCITY) {
            long lastId = LongUtils.getLast(statusIdArray, statusId, -1);
            if (lastId == -1) {
                Toast.makeText(context, "û����һ��״̬", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(ViewStatusDetailActivity.this, ViewStatusDetailActivity.class);
                intent.putExtra(SnsConstant.WEBSITE_TYPE, website);
                intent.putExtra("statusId", lastId);
                intent.putExtra("statusIdArray", statusIdArray);
                startActivity(intent);
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    ;

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
        } else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
        }
    }
}
