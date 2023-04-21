package com.kdev.qq.task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONObject;
import com.kdev.qq.bean.QQuser;
import com.kdev.qq.utils.BaikeSearch;
import com.kdev.qq.utils.Base64;
import com.kdev.qq.utils.Conf;
import com.kdev.qq.utils.ExpressUtils;
import com.kdev.qq.utils.HttpUtils;
import com.kdev.qq.utils.Utils;

/**
 * @author kick.smiles@gmail.com <br />
 *         Date : Dec 8, 2011
 * 
 */
public class SendUserMessagePoll extends Thread {

    private static final Log log = LogFactory.getLog(SendUserMessagePoll.class);

    private QQuser user;

    private String content = "";

    private String to_uin;

    public SendUserMessagePoll(QQuser user, String content, String to_uin) {
        this.user = user;
        this.content = content;
        this.to_uin = to_uin;
    }

    @Override
    public void run() {
        String sendTextMessage = "";
        String sendUrl = Conf.getSEND_MESSAGE_URL();
        Map<String, String> param = new HashMap<String, String>();
        try {
            sendTextMessage = content;
            System.out.println("USER GEt MEssage ：" + sendTextMessage);
            ;
            if (content.indexOf("@帮助") >= 0) {
                sendTextMessage = Conf.HELP_MSG;
            } else {
                if (content.indexOf("[\"face\",") != -1 || content.indexOf("[\"cface\",{\"") != -1) {
                    content = "系统不允许你发图片";
                }
                if (content.indexOf("@off-group") == 0) {
                    Conf.isRobot = false;
                }
                if (content.indexOf("@on-group") == 0) {
                    Conf.isRobot = true;
                }
                if (content.indexOf("@") == 0) {
                    if (content != "" && content.indexOf("@cn-en") == 0 || content.indexOf("@中文") == 0) {
                        RobotPoll robotPoll = new RobotPoll();
                        sendTextMessage = robotPoll.Translate(content.replaceFirst("@cn-en", ""), "zh-en");
                    }
                    if (content != "" && content.indexOf("@en-cn") == 0 || content.indexOf("@英文") == 0) {
                        RobotPoll robotPoll = new RobotPoll();
                        sendTextMessage = robotPoll.Translate(content.replaceFirst("@en-cn", ""), "en-zh");
                    }
                    if (content != "" && content.indexOf("@BASE64") == 0) {
                        sendTextMessage = Base64.encode(content.replaceFirst("@BASE64", ""));
                    }
                    if (content != "" && content.indexOf("@KD") == 0 || content.indexOf("@kd") == 0 || content.indexOf("@快递") == 0) {
                        ExpressUtils exp = new ExpressUtils();
                        sendTextMessage = exp.getExp(content);
                    }
                } else if (content.indexOf("#") != -1 && content.trim().length() >= 3) {
                    sendTextMessage = BaikeSearch.BaiKe(content);
                } else if (Conf.isRobot) {
                    log.info("发送跟话消息");
                } else {
                    this.interrupt();
                    log.info("不发送群消息线程自动结束");
                    return;
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("to", to_uin);
            jsonObject.put("face", "101");
            JSONArray jsonArray = new JSONArray();
            JSONObject face = new JSONObject();
            face.put("face", "101");
            jsonArray.add(sendTextMessage);
            jsonArray.add(Utils.getFont());
            jsonObject.put("content", jsonArray.toString());
            jsonObject.put("msg_id", UUID.randomUUID());
            jsonObject.put("clientid", this.user.getClientid());
            jsonObject.put("psessionid", this.user.getPsessionid());
            param.put("r", jsonObject.toString());
            param.put("clientid", String.valueOf(this.user.getClientid()));
            param.put("psessionid", this.user.getPsessionid());
        } catch (Exception e) {
            log.info("发送消息失败 [to_uin]" + to_uin + "  ErrorException  :  " + e);
        }
        DefaultHttpClient client = new DefaultHttpClient();
        HttpUtils httpUtils = new HttpUtils(this.user);
        HttpResponse response;
        try {
            this.sleep(600);
            response = client.execute(httpUtils.PostMethod(sendUrl, param));
            @SuppressWarnings("unused") String result = EntityUtils.toString(response.getEntity(), "UTF-8");
            List<Cookie> cookieList = client.getCookieStore().getCookies();
            for (Cookie cookie : cookieList) {
                log.info("Cookie : " + cookie.getName() + "  " + cookie.getValue());
            }
            log.info("发送消息成功 [to_uin]: " + to_uin);
        } catch (Exception e) {
            log.info("发送失败 " + e);
            this.interrupt();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
}
