package org.igeek.atomqq.task;

import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.igeek.atomqq.activity.BaseActivity;
import org.igeek.atomqq.domain.Group;
import org.igeek.atomqq.net.HttpConnection;
import org.igeek.atomqq.net.HttpConnection.Request_TYPE;
import org.igeek.atomqq.net.Response;
import org.igeek.atomqq.util.URLManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 获取好友列表
 * 正常返回类型: application/json
 * 
 * POST application/json	http://s.web2.qq.com/api/get_group_name_list_mask2	
 * @author hangxin1940@gmail.com
 * @version 创建时间：2012-2-08 上午1:30:16
 * 
 */
public class GroupListTask extends CachedAsyncTask<Void, Void, Void> {

    public static final int ERROR = 0x5027;

    private TaskResultListener listener;

    private GroupListResponse response;

    private int flag;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch(msg.what) {
                case ERROR:
                    activity.showDialog(ERROR, msg.getData());
                    break;
                default:
                    activity.showDialog(BaseActivity.DIALOG_ERROR, msg.getData());
                    break;
            }
        }
    };

    private BaseActivity activity;

    public GroupListTask(BaseActivity activity) {
        this.activity = activity;
    }

    public void setOnTaskResult(TaskResultListener listener, int flag) {
        this.listener = listener;
        this.flag = flag;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            String vfwebqq = HttpConnection.getCookieManager().getCookieValue("vfwebqq");
            StringBuffer surl = new StringBuffer(URLManager.GROUP_LIST);
            JSONObject post = new JSONObject();
            post.put("vfwebqq", vfwebqq);
            surl.append("?r=");
            surl.append(URLEncoder.encode(post.toString(), "utf-8"));
            URL url = new URL(surl.toString());
            HttpConnection conn = new HttpConnection(url, Request_TYPE.POST);
            conn.setReferer(URLManager.REFER_d_web2_qq_com_proxy);
            conn.connect();
            Response cesponses = conn.getResponse();
            conn.disconnect();
            JSONObject r = cesponses.getJsonObj();
            List<Group> groups = createGroup(r);
            response = new GroupListResponse();
            response.setGroups(groups);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("网络异常", ERROR);
        }
        return null;
    }

    /**
	 * 组装群
	 * @param frs
	 * @return
	 * @throws JSONException 
	 */
    private List<Group> createGroup(JSONObject r) throws JSONException {
        List<Group> groups = Collections.synchronizedList(new ArrayList<Group>());
        JSONObject result = r.getJSONObject("result");
        JSONArray grs = result.getJSONArray("gnamelist");
        Map<Long, String> marks = new HashMap<Long, String>();
        JSONArray gms = result.getJSONArray("gmarklist");
        for (int i = 0; i < gms.length(); i++) {
            JSONObject g = gms.getJSONObject(i);
            long gid = g.getLong("uin");
            String name = g.getString("markname");
            marks.put(gid, name);
        }
        for (int i = 0; i < grs.length(); i++) {
            JSONObject g = grs.getJSONObject(i);
            long gid = g.getLong("gid");
            long flag = g.getLong("flag");
            String name = g.getString("name");
            long code = g.getLong("code");
            Group group = new Group(gid);
            group.setFlag(flag);
            group.setName(name);
            group.setCode(code);
            String mark = marks.get(gid);
            group.setMarkname(null == mark ? "" : mark);
            groups.add(group);
        }
        return groups;
    }

    private void showErrorDialog(String message, int flag) {
        Bundle bundle = new Bundle();
        bundle.putString(BaseActivity.FLAG_ERROR, message);
        Message msg = new Message();
        msg.setData(bundle);
        msg.what = flag;
        handler.sendMessage(msg);
    }

    @Override
    protected void onPostExecute(Void result) {
        if (null != response) listener.onTaskResult(response, flag);
    }

    public class GroupListResponse extends Response {

        private List<Group> groups;

        public void setGroups(List<Group> groups) {
            this.groups = groups;
        }

        public List<Group> getGroups() {
            return groups;
        }
    }
}
