package org.igeek.atomqq.task;

import java.net.URL;
import java.util.Date;
import org.igeek.atomqq.activity.BaseActivity;
import org.igeek.atomqq.domain.Friend;
import org.igeek.atomqq.net.HttpConnection;
import org.igeek.atomqq.net.HttpConnection.Request_TYPE;
import org.igeek.atomqq.net.Response;
import org.igeek.atomqq.util.URLManager;
import org.json.JSONArray;
import org.json.JSONObject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 获取好友签名
 * 正常返回类型: application/json
 * 
 * GET application/json	http://s.web2.qq.com/api/get_single_long_nick2?tuin=qq号&vfwebqq=令牌&t=1328346731059时间	
 * @author hangxin1940@gmail.com
 * @version 创建时间：2012-1-29 下午1:32:26
 * 
 */
public class FriendSingleLongNickTask extends CachedAsyncTask<Void, Void, Void> {

    public static final int ERROR = 0x5021;

    private TaskResultListener listener;

    private LNickResponse response;

    private long uin;

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

    public FriendSingleLongNickTask(BaseActivity activity) {
        this.activity = activity;
    }

    public void setOnTaskResult(TaskResultListener listener, int flag) {
        this.listener = listener;
        this.flag = flag;
    }

    public void setParams(long uin) {
        this.uin = uin;
    }

    @Override
    protected Void doInBackground(Void... params) {
        String vfwebqq = HttpConnection.getCookieManager().getCookieValue("vfwebqq");
        StringBuffer surl = new StringBuffer(URLManager.FRIEND_LONG_NICK);
        surl.append("?tuin=");
        surl.append(uin);
        surl.append("&vfwebqq=");
        surl.append(vfwebqq);
        surl.append("&t=");
        surl.append(new Date().getTime());
        try {
            URL url = new URL(surl.toString());
            HttpConnection conn = new HttpConnection(url, Request_TYPE.GET);
            conn.setReferer(URLManager.REFER_s_web2_qq_com_proxy);
            conn.connect();
            Response cesponses = conn.getResponse();
            conn.disconnect();
            response = new LNickResponse();
            JSONObject r = cesponses.getJsonObj();
            JSONArray result = r.getJSONArray("result");
            String lnick = result.getJSONObject(0).getString("lnick");
            Friend f = new Friend(uin);
            f.setLnick(lnick);
            response.setFriend(f);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorDialog("网络异常", ERROR);
        }
        return null;
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

    public class LNickResponse extends Response {

        private Friend friend;

        public void setFriend(Friend friend) {
            this.friend = friend;
        }

        public Friend getFriend() {
            return friend;
        }
    }
}
