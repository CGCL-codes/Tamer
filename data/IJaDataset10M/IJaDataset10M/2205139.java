package com.shengyijie.activity.usertab;

import java.util.ArrayList;
import org.apache.http.HttpResponse;
import com.shengyijie.activity.MainActivity;
import com.shengyijie.activity.R;
import com.shengyijie.activity.UserTabActivity;
import com.shengyijie.activity.commen.ItemActivity;
import com.shengyijie.activity.commen.ItemListActivity;
import com.shengyijie.activity.tab.RecommendedActivity;
import com.shengyijie.adapter.view.AlertBox;
import com.shengyijie.adapter.view.AlertBox.OnCanceledListener;
import com.shengyijie.adapter.viewadapter.ItemListAdapter;
import com.shengyijie.context.ContextApplication;
import com.shengyijie.model.http.HttpConnectApi;
import com.shengyijie.model.json.JsonParser;
import com.shengyijie.model.object.Listobject.ProjectList;
import com.shengyijie.model.object.baseobject.Item;
import com.shengyijie.model.object.baseobject.Project;
import com.shengyijie.util.Utility;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AttentionActivity extends Activity implements OnClickListener {

    private ListView itemListView;

    private ItemListAdapter listAdapter;

    private TextView tv_no;

    private View preView = null;

    public Task task;

    private HttpConnectApi httpConnect;

    private AlertBox mypDialog;

    public ArrayList<Item> itemList = new ArrayList<Item>();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attention);
        initView();
        getAttentionList();
    }

    public void initView() {
        itemListView = (ListView) this.findViewById(R.id.recommendedlist);
        itemListView.setOnItemClickListener(itemClickListener);
        tv_no = (TextView) this.findViewById(R.id.no);
        tv_no.setVisibility(View.INVISIBLE);
    }

    public void getAttentionList() {
        itemListView.postDelayed(new Runnable() {

            public void run() {
                mypDialog = new AlertBox(AttentionActivity.this, AttentionActivity.this, findViewById(R.id.attlist));
                mypDialog.setCompletedListener(new OnCanceledListener() {

                    @Override
                    public void onCanceled() {
                        if (task.getStatus() == AsyncTask.Status.RUNNING) {
                            task.cancel(true);
                        }
                    }
                });
                task = new Task();
                task.execute(TASK_LOAD_ITEM_LIST);
            }
        }, 1);
    }

    int pro_id;

    public OnItemClickListener itemClickListener = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                preView = view;
                Item content = (Item) parent.getItemAtPosition(position);
                ContextApplication.itemName = content.getName();
                pro_id = content.getID();
                task = new Task();
                task.execute(TASK_LOAD_ITEM);
            } catch (Exception e) {
            }
        }
    };

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this).setTitle("提示").setMessage("确定退出？").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                    Utility.clearUserSession(AttentionActivity.this);
                    AttentionActivity.this.finish();
                    MainActivity.tabhost.setCurrentTab(0);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int whichButton) {
                }
            }).show();
            return true;
        } else return super.onKeyDown(keyCode, event);
    }

    public void getAttentionListAdapter() {
        Item content = null;
        itemList.clear();
        for (int i = 0; i < projectList.getCount(); i++) {
            content = new Item();
            content.setID(projectList.getItem(i).getID());
            content.setImageUrl(projectList.getItem(i).getPro_pic_show());
            content.setName(projectList.getItem(i).getPro_name());
            content.setDes("招商电话-" + projectList.getItem(i).getZs_phone());
            itemList.add(content);
        }
        listAdapter = new ItemListAdapter(this, itemList, itemListView);
    }

    ProjectList projectList;

    public String doGetAttentionProject() {
        String result = "at_fail";
        httpConnect = HttpConnectApi.getInstance();
        try {
            HttpResponse response = httpConnect.getAttentionProject(ContextApplication.user.getSession_ID(), "0,50");
            if (response.getStatusLine().getStatusCode() == 200) {
                JsonParser jsonParser = JsonParser.getInstance();
                projectList = jsonParser.getAttentionProgect(response);
                if (projectList != null) {
                    if (projectList.getCount() > 0) {
                        getAttentionListAdapter();
                        result = "at_ok";
                    } else if (projectList.getCount() == 0) result = "at_0";
                }
            }
            return result;
        } catch (Exception e) {
        }
        return result;
    }

    public Project doGetProject(int id) {
        Project project;
        httpConnect = HttpConnectApi.getInstance();
        try {
            HttpResponse response = httpConnect.getProjectDetail(id);
            if (response.getStatusLine().getStatusCode() == 200) {
                JsonParser jsonParser = JsonParser.getInstance();
                project = jsonParser.getProgectDetail(response);
                if (ContextApplication.project != null) {
                    return project;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    boolean isFirstStart = true;

    protected void onResume() {
        super.onResume();
        try {
            if (!isFirstStart) {
                if (ContextApplication.isAttentionRefresh) {
                    task = new Task();
                    task.execute(TASK_LOAD_ITEM_LIST);
                    ContextApplication.isAttentionRefresh = false;
                }
            }
            isFirstStart = false;
        } catch (Exception e) {
        }
    }

    public String doGetProjectDetail() {
        String result = "pr_fail";
        httpConnect = HttpConnectApi.getInstance();
        try {
            HttpResponse response = httpConnect.getProjectDetail(pro_id);
            if (response.getStatusLine().getStatusCode() == 200) {
                JsonParser jsonParser = JsonParser.getInstance();
                ContextApplication.project = jsonParser.getProgectDetail(response);
                if (ContextApplication.project != null) {
                    result = "pr_ok";
                }
            }
            return result;
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public void onClick(View v) {
    }

    public void changeToItemActivity() {
        Intent intent = new Intent();
        intent.setClass(this, ItemActivity.class);
        this.startActivityForResult(intent, 1);
    }

    private static final int ALERT_LOADING = 1;

    private static final int RESULT_LOAD_ITEMDETAIL_OK = 16;

    private static final int RESULT_LOAD_ITEMDETAIL_FAIL = 17;

    private static final int RESULT_LOAD_ITEMLIST_OK = 2;

    private static final int RESULT_LOAD_ITEMLIST_0 = 3;

    private static final int RESULT_LOAD_ITEMLIST_FAIL = 4;

    private static final String TASK_LOAD_ITEM = "loaditem";

    private static final String TASK_LOAD_ITEM_LIST = "loaditemlist";

    private final Handler handler = new Handler(Looper.getMainLooper()) {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case ALERT_LOADING:
                    mypDialog.show();
                    break;
                case RESULT_LOAD_ITEMLIST_FAIL:
                    try {
                        mypDialog.dismiss();
                        itemListView.setAdapter(null);
                        tv_no.setVisibility(View.VISIBLE);
                        new AlertDialog.Builder(AttentionActivity.this).setTitle("提示").setMessage("尊敬的生意街会员，您尚未关注任何项目，现在就去浏览项目吧！").setPositiveButton("确定", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent i = new Intent();
                                i.setClass(AttentionActivity.this, MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                AttentionActivity.this.startActivity(i);
                                MainActivity.tabhost.setCurrentTab(0);
                                ContextApplication.tabtype = 0;
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        }).show();
                    } catch (Exception e) {
                    }
                    break;
                case RESULT_LOAD_ITEMLIST_0:
                    mypDialog.dismiss();
                    Toast.makeText(AttentionActivity.this, "对不起，暂无数据", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_LOAD_ITEMLIST_OK:
                    try {
                        mypDialog.dismiss();
                        itemListView.setAdapter(listAdapter);
                    } catch (Exception e) {
                    }
                    break;
                case RESULT_LOAD_ITEMDETAIL_OK:
                    mypDialog.dismiss();
                    changeToItemActivity();
                    break;
                case RESULT_LOAD_ITEMDETAIL_FAIL:
                    mypDialog.dismiss();
                    Toast.makeText(AttentionActivity.this, "对不起，暂无数据", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private class Task extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            if (params[0].equals(TASK_LOAD_ITEM)) {
                return doGetProjectDetail();
            } else if (params[0].equals(TASK_LOAD_ITEM_LIST)) {
                return doGetAttentionProject();
            } else return "";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("pr_fail")) {
                Message msg = Message.obtain();
                msg.what = RESULT_LOAD_ITEMDETAIL_FAIL;
                handler.sendMessage(msg);
            } else if (result.equals("pr_ok")) {
                Message msg = Message.obtain();
                msg.what = RESULT_LOAD_ITEMDETAIL_OK;
                handler.sendMessage(msg);
            } else if (result.equals("at_fail")) {
                Message msg = Message.obtain();
                msg.what = RESULT_LOAD_ITEMLIST_FAIL;
                handler.sendMessage(msg);
            } else if (result.equals("at_ok")) {
                Message msg = Message.obtain();
                msg.what = RESULT_LOAD_ITEMLIST_OK;
                handler.sendMessage(msg);
            } else if (result.equals("at_0")) {
                Message msg = Message.obtain();
                msg.what = RESULT_LOAD_ITEMLIST_FAIL;
                handler.sendMessage(msg);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Message msg = Message.obtain();
            msg.what = ALERT_LOADING;
            handler.sendMessage(msg);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }
}
