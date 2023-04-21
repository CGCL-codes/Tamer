package cn.poco.food.more;

import oauth.signpost.OAuthProvider;
import oauth.signpost.basic.DefaultOAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import cn.poco.food.R;
import cn.poco.food.MainTabActivity;
import cn.poco.util.Cons;
import cn.poco.util.oauth.QWeiboSyncApi;
import cn.poco.wblog.xauth4sina.Xauth4SinaData;
import cn.poco.wblog.xauth4sina.Xauth4SinaService;
import android.app.Activity;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class ShareSetActivity extends Activity {

    public static final int SHARESINASUCCESS = 0;

    public static final int SHAREQQSUCCESS = 1;

    public static final int SHAREERROR = 4;

    private RelativeLayout sinaLayout, qqLayout;

    private TextView sinaTips, qqTips;

    private Button backBtn;

    private Context context;

    private ProgressDialog progressDialog;

    public static final int ontype_more = 1;

    public static final int ontype_publish = 2;

    private int onType;

    private CommonsHttpOAuthConsumer httpOauthConsumer;

    private OAuthProvider httpOauthprovider;

    private String sinaAccesToken, sinaAccessTokenSecret, qqAccesToken, qqAccessTokenSecret;

    private SharedPreferences spref;

    private QWeiboSyncApi qWeiboSyncApi;

    private String requestToken, requestTokenSecret;

    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.share_set);
        context = this;
        spref = context.getSharedPreferences(Cons.SFShareSet, MODE_PRIVATE);
        onType = getIntent().getIntExtra(Cons.SHARE_SET_ONTYPE, 1);
        sinaLayout = (RelativeLayout) findViewById(R.id.share_set_sina_layout);
        qqLayout = (RelativeLayout) findViewById(R.id.share_set_qq_layout);
        sinaTips = (TextView) findViewById(R.id.share_set_sina_tips);
        qqTips = (TextView) findViewById(R.id.share_set_qq_tips);
        backBtn = (Button) findViewById(R.id.back_button);
        backBtn.setVisibility(View.GONE);
        if (!spref.getString(Cons.SFShareSetQQKey, "").equals("")) {
            qqTips.setText("�Ѱ�");
        } else {
            qqTips.setText("δ��");
        }
        if (!spref.getString(Cons.SFShareSetSinaKey, "").equals("")) {
            sinaTips.setText("�Ѱ�");
        } else {
            sinaTips.setText("δ��");
        }
        backBtn.setOnClickListener(listener);
        sinaLayout.setOnClickListener(listener);
        qqLayout.setOnClickListener(listener);
    }

    OnClickListener listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.share_set_sina_layout:
                    String sinaKey = spref.getString(Cons.SFShareSetSinaKey, "");
                    if (sinaKey != null && !sinaKey.equals("")) {
                        System.out.println(sinaKey == null + sinaKey);
                        System.out.println("sinake");
                        chanceShare(true);
                    } else {
                        shareToSinaSet();
                    }
                    break;
                case R.id.share_set_qq_layout:
                    String qqkey = spref.getString(Cons.SFShareSetQQKey, "");
                    if (qqkey != null && !qqkey.equals("")) {
                        chanceShare(false);
                    } else {
                        shareToQQSet();
                    }
                    break;
                case R.id.back_button:
                    finish();
                default:
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            Bundle bundle = data.getExtras();
            switch(resultCode) {
                case 0:
                    final String ping = bundle.getString(Cons.SINA_VERIFIER);
                    progressDialog = ProgressDialog.show(context, "", "���Ժ�...");
                    progressDialog.setCancelable(true);
                    new Thread(new Runnable() {

                        public void run() {
                            try {
                                httpOauthprovider.setOAuth10a(true);
                                httpOauthprovider.retrieveAccessToken(httpOauthConsumer, ping);
                                sinaAccesToken = httpOauthConsumer.getToken();
                                sinaAccessTokenSecret = httpOauthConsumer.getTokenSecret();
                                System.out.println("sina: " + "---key" + sinaAccesToken + "---screcret:" + sinaAccessTokenSecret);
                                Editor editor = spref.edit();
                                editor.putString(Cons.SFShareSetSinaKey, sinaAccesToken);
                                editor.putString(Cons.SFShareSetSinaScreet, sinaAccessTokenSecret);
                                editor.commit();
                                handler.sendEmptyMessage(SHARESINASUCCESS);
                            } catch (Exception e) {
                                handler.sendEmptyMessage(SHAREERROR);
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case 1:
                    final String verify = data.getStringExtra(Cons.QQ_VERIFIER);
                    progressDialog = ProgressDialog.show(context, "", "���Ժ�...");
                    progressDialog.setCancelable(true);
                    new Thread(new Runnable() {

                        public void run() {
                            try {
                                String accessString = qWeiboSyncApi.getAccessToken(Cons.QQ_CONSUMER_KEY, Cons.QQ_CONSUMER_SECRET, requestToken, requestTokenSecret, verify);
                                String userToken = accessString.substring(accessString.indexOf("oauth_token=") + 12, accessString.indexOf("&oauth_token_secret"));
                                String userSecret = accessString.substring(accessString.indexOf("&oauth_token_secret=") + 20, accessString.indexOf("&name"));
                                String name = accessString.substring(accessString.indexOf("&name=") + 6);
                                qqAccesToken = userToken;
                                qqAccessTokenSecret = userSecret;
                                Editor editor = spref.edit();
                                editor.putString(Cons.SFShareSetQQKey, qqAccesToken);
                                editor.putString(Cons.SFShareSetQQScreet, qqAccessTokenSecret);
                                editor.commit();
                                handler.sendEmptyMessage(SHAREQQSUCCESS);
                                System.out.println("qq:" + "---key" + qqAccesToken + "---screcret:" + qqAccessTokenSecret);
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(SHAREERROR);
                            }
                        }
                    }).start();
                    break;
                default:
                    break;
            }
        } else {
        }
    }

    Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch(msg.what) {
                case SHARESINASUCCESS:
                    Toast.makeText(context, "�󶨵�����΢���ɹ�", Toast.LENGTH_SHORT).show();
                    sinaTips.setText("�Ѱ�");
                    progressDialog.dismiss();
                    break;
                case SHAREQQSUCCESS:
                    Toast.makeText(context, "����Ѷ΢���ɹ�", Toast.LENGTH_SHORT).show();
                    qqTips.setText("�Ѱ�");
                    progressDialog.dismiss();
                    break;
                case SHAREERROR:
                    Toast.makeText(context, "������?������", Toast.LENGTH_SHORT).show();
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    break;
                default:
                    break;
            }
        }

        ;
    };

    private void chanceShare(final boolean isSina) {
        Builder builder = new Builder(context);
        String message = isSina ? "ȷ��ȡ����?����΢��" : "ȷ��ȡ����Ѷ΢��";
        builder.setTitle("����");
        builder.setMessage(message);
        builder.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Editor editor = spref.edit();
                if (isSina) {
                    editor.remove(Cons.SFShareSetSinaKey);
                    editor.remove(Cons.SFShareSetSinaScreet);
                    editor.commit();
                    sinaTips.setText("δ��");
                } else {
                    editor.remove(Cons.SFShareSetQQKey);
                    editor.remove(Cons.SFShareSetQQScreet);
                    editor.commit();
                    qqTips.setText("δ��");
                }
            }
        });
        builder.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void shareToSinaSet() {
        progressDialog = ProgressDialog.show(context, "", "���Ժ�...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        new Thread(new Runnable() {

            public void run() {
                try {
                    runOnUiThread(new Runnable() {

                        public void run() {
                            httpOauthConsumer = new CommonsHttpOAuthConsumer(Cons.SINA_CONSUMER_KEY, Cons.SINA_CONSUMER_SECRET);
                            httpOauthprovider = new DefaultOAuthProvider("http://api.t.sina.com.cn/oauth/request_token", "http://api.t.sina.com.cn/oauth/access_token", "http://api.t.sina.com.cn/oauth/authorize");
                            try {
                                String authUrl = httpOauthprovider.retrieveRequestToken(httpOauthConsumer, "");
                                System.out.println(authUrl);
                                Intent intent = new Intent(context, ShareSetSinaWebViewActivity.class);
                                intent.putExtra(Cons.SINA_AUTH_URL, authUrl);
                                progressDialog.dismiss();
                                startActivityForResult(intent, 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void shareToSinaSetX() {
        View loginDialogViw = getLayoutInflater().inflate(R.layout.login_dialog, null);
        final EditText userNameEdit = (EditText) loginDialogViw.findViewById(R.id.login_dialog_username_edit);
        final EditText passWordEdit = (EditText) loginDialogViw.findViewById(R.id.login_dialog_password_edit);
        Button lgLoginButton = (Button) loginDialogViw.findViewById(R.id.login_dialog_login_button);
        Button lgCancelButton = (Button) loginDialogViw.findViewById(R.id.login_dialog_cancel);
        ToggleButton loginDialogPreButton = (ToggleButton) loginDialogViw.findViewById(R.id.login_dialog_prefenceButton);
        loginDialogPreButton.setVisibility(View.INVISIBLE);
        dialog = new Dialog(context);
        dialog.setContentView(loginDialogViw);
        dialog.show();
        lgLoginButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                final String sinaUserName = userNameEdit.getText().toString();
                final String sinaPassWord = passWordEdit.getText().toString();
                if ("".equals(sinaUserName)) {
                    Toast.makeText(context, "����������΢���˺�", Toast.LENGTH_LONG).show();
                } else if (!"".equals(sinaUserName) && "".equals(sinaPassWord)) {
                    Toast.makeText(context, "����������΢������", Toast.LENGTH_LONG).show();
                } else {
                    dialog.dismiss();
                    progressDialog = ProgressDialog.show(context, null, "��֤��...");
                    progressDialog.setCancelable(true);
                    new Thread(new Runnable() {

                        public void run() {
                            try {
                                Xauth4SinaData xauth4SinaData = Xauth4SinaService.xauth4Sina(sinaUserName, sinaPassWord);
                                System.out.println("1111");
                                if (xauth4SinaData != null) {
                                    String userId = xauth4SinaData.getUserId();
                                    String userToken = xauth4SinaData.getAccessToken();
                                    String userSecret = xauth4SinaData.getAccessTokenSecret();
                                    sinaAccesToken = userToken;
                                    sinaAccessTokenSecret = userSecret;
                                    Editor editor = spref.edit();
                                    editor.putString(Cons.SFShareSetSinaKey, sinaAccesToken);
                                    editor.putString(Cons.SFShareSetSinaScreet, sinaAccessTokenSecret);
                                    editor.commit();
                                    handler.sendEmptyMessage(SHARESINASUCCESS);
                                } else {
                                    handler.sendEmptyMessage(SHAREERROR);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                handler.sendEmptyMessage(SHAREERROR);
                            }
                        }
                    }).start();
                }
            }
        });
        lgCancelButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void shareToQQSet() {
        progressDialog = ProgressDialog.show(context, "", "���Ժ�...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        new Thread(new Runnable() {

            public void run() {
                try {
                    qWeiboSyncApi = new QWeiboSyncApi();
                    String resquestString = qWeiboSyncApi.getRequestToken(Cons.QQ_CONSUMER_KEY, Cons.QQ_CONSUMER_SECRET);
                    requestToken = resquestString.substring(resquestString.indexOf("oauth_token=") + 12, resquestString.indexOf("&oauth_token_secret"));
                    requestTokenSecret = resquestString.substring(resquestString.indexOf("&oauth_token_secret=") + 20, resquestString.indexOf("&oauth_callback_confirmed"));
                    final String authUrl = "https://open.t.qq.com/cgi-bin/authorize?oauth_token=" + requestToken;
                    runOnUiThread(new Runnable() {

                        public void run() {
                            progressDialog.dismiss();
                            Intent intent = new Intent(context, ShareSetSinaWebViewActivity.class);
                            intent.putExtra(Cons.QQ_AUTH_URL, authUrl);
                            startActivityForResult(intent, 1);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.deep_menu_to_first, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch(item.getItemId()) {
            case R.id.item01:
                intent = new Intent(this, cn.poco.food.MainTabActivity.class);
                cn.poco.food.MainTabActivity.tabHost.setCurrentTab(0);
                startActivity(intent);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
