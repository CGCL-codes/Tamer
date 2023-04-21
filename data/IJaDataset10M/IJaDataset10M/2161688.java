package com.hk.jms;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import weibo4j.WeiboException;
import weibo4j.http.ImageItem;
import com.hk.bean.taobao.JsonKey;
import com.hk.bean.taobao.Tb_Answer;
import com.hk.bean.taobao.Tb_Ask;
import com.hk.bean.taobao.Tb_Item;
import com.hk.bean.taobao.Tb_Item_Cmt;
import com.hk.bean.taobao.Tb_User_Api;
import com.hk.frame.util.DataUtil;
import com.hk.frame.util.JsonUtil;
import com.hk.frame.util.ResourceConfig;
import com.hk.frame.web.http.HttpUtil;
import com.hk.svr.Tb_AskService;
import com.hk.svr.Tb_ItemService;
import com.hk.svr.Tb_Item_CmtService;
import com.hk.svr.Tb_UserService;
import com.hk.svr.pub.SinaUtil;

public class OtherApiConsumer {

    @Autowired
    private Tb_UserService tbUserService;

    @Autowired
    private Tb_Item_CmtService itemCmtService;

    @Autowired
    private Tb_AskService tbAskService;

    @Autowired
    private Tb_ItemService tbItemService;

    @Autowired
    private HttpUtil httpUtil;

    private final Log log = LogFactory.getLog(OtherApiConsumer.class);

    public void processMessage(String value) {
        JmsMsg jmsMsg = new JmsMsg(value);
        if (jmsMsg.getHead().equals(JmsMsg.HEAD_OTHER_API_SINAFOLLOW_CREATE_FOLLOW)) {
            this.processSinaFollow(jmsMsg.getBody());
            return;
        }
        if (jmsMsg.getHead().equals(JmsMsg.HEAD_OTHER_API_CREATE_ITEM_CMT_TO_STATUS)) {
            this.processCreateCmtStatusToSina(jmsMsg.getBody());
            return;
        }
        if (jmsMsg.getHead().equals(JmsMsg.HEAD_OTHER_API_CREATE_ASK_TO_STATUS)) {
            this.processCreateAskStatusToSina(jmsMsg.getBody());
            return;
        }
        if (jmsMsg.getHead().equals(JmsMsg.HEAD_OTHER_API_CREATE_ANSWER_TO_STATUS)) {
            this.processCreateAnswerStatusToSina(jmsMsg.getBody());
            return;
        }
        if (jmsMsg.getHead().equals(JmsMsg.HEAD_OTHER_API_SHARE_ITEM)) {
            this.processShareItem(jmsMsg.getBody());
            return;
        }
        log.error("unknown message type [ " + value + " ]");
    }

    private void processShareItem(String body) {
        Map<String, String> map = JsonUtil.getMapFromJson(body);
        String itemid_str = map.get(JsonKey.ITEMID);
        if (itemid_str == null) {
            return;
        }
        String userid_str = map.get(JsonKey.USERID);
        long itemid = Long.valueOf(itemid_str);
        long userid = Long.valueOf(userid_str);
        Tb_Item tbItem = this.tbItemService.getTb_Item(itemid);
        if (tbItem == null) {
            return;
        }
        String content = null;
        String item_url = "http://" + map.get(JsonKey.SERVER_NAME) + map.get(JsonKey.CONTEXTPATH) + "/tb/item?itemid=" + itemid_str;
        if (tbItem.getClick_url() != null && tbItem.getCommission() > 0) {
            content = ResourceConfig.getText("ehk.buyitem.sina.contentwithclick_url", tbItem.getTitle(), DataUtil.fmtDouble(tbItem.getUserYouHuiPrice(), "#.#"), item_url);
        } else {
            content = ResourceConfig.getText("ehk.buyitem.sina.content", tbItem.getTitle(), item_url);
        }
        ImageItem imageItem = null;
        if (tbItem.getPic_url() != null) {
            try {
                byte[] imgbyte = this.httpUtil.getByteArrayResult(tbItem.getPic_url());
                imageItem = new ImageItem("pic", imgbyte);
            } catch (Exception e) {
            }
        }
        Tb_User_Api userApi = this.tbUserService.getTb_User_Api(userid, Tb_User_Api.REG_SOURCE_SINA);
        if (userApi == null) {
            return;
        }
        try {
            SinaUtil.updateStatus(userApi.getAccess_token(), userApi.getToken_secret(), content, imageItem);
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }

    private void processCreateAnswerStatusToSina(String body) {
        Map<String, String> map = JsonUtil.getMapFromJson(body);
        String ansid_str = map.get(JsonKey.ANSWER_ID);
        String server_name = map.get(JsonKey.SERVER_NAME);
        String contextpath = map.get(JsonKey.CONTEXTPATH);
        if (ansid_str == null) {
            return;
        }
        long ansid = Long.valueOf(ansid_str);
        Tb_Answer tbAnswer = this.tbAskService.getTb_Answer(ansid);
        if (tbAnswer == null) {
            return;
        }
        String content = DataUtil.limitTextRow(tbAnswer.getContent(), 80) + " http://" + server_name + contextpath + "/tb/ask?aid=" + tbAnswer.getAid();
        Tb_User_Api userApi = this.tbUserService.getTb_User_Api(tbAnswer.getUserid(), Tb_User_Api.REG_SOURCE_SINA);
        if (userApi == null) {
            return;
        }
        try {
            SinaUtil.updateStatus(userApi.getAccess_token(), userApi.getToken_secret(), content);
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }

    private void processCreateAskStatusToSina(String body) {
        Map<String, String> map = JsonUtil.getMapFromJson(body);
        String aid_str = map.get(JsonKey.ASK_ID);
        String server_name = map.get(JsonKey.SERVER_NAME);
        String contextpath = map.get(JsonKey.CONTEXTPATH);
        if (aid_str == null) {
            return;
        }
        long aid = Long.valueOf(aid_str);
        Tb_Ask tbAsk = this.tbAskService.getTb_Ask(aid);
        if (tbAsk == null) {
            return;
        }
        String content = DataUtil.toTextRow(tbAsk.getTitle()) + " http://" + server_name + contextpath + "/tb/ask?aid=" + aid;
        Tb_User_Api userApi = this.tbUserService.getTb_User_Api(tbAsk.getUserid(), Tb_User_Api.REG_SOURCE_SINA);
        try {
            SinaUtil.updateStatus(userApi.getAccess_token(), userApi.getToken_secret(), content);
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }

    private void processCreateCmtStatusToSina(String body) {
        Map<String, String> map = JsonUtil.getMapFromJson(body);
        String cmtid_str = map.get(JsonKey.ITEM_CMTID);
        String server_name = map.get(JsonKey.SERVER_NAME);
        String contextpath = map.get(JsonKey.CONTEXTPATH);
        if (cmtid_str == null) {
            return;
        }
        long cmtid = Long.valueOf(cmtid_str);
        Tb_Item_Cmt tbItemCmt = this.itemCmtService.getTb_Item_Cmt(cmtid);
        if (tbItemCmt == null) {
            return;
        }
        String content = DataUtil.limitHtmlRow(tbItemCmt.getContent(), 80) + "http://" + server_name + contextpath + "/tb/item?itemid=" + tbItemCmt.getItemid();
        Tb_User_Api userApi = this.tbUserService.getTb_User_Api(tbItemCmt.getUserid(), Tb_User_Api.REG_SOURCE_SINA);
        try {
            SinaUtil.updateStatus(userApi.getAccess_token(), userApi.getToken_secret(), content);
        } catch (WeiboException e) {
            e.printStackTrace();
        }
    }

    private void processSinaFollow(String body) {
        Map<String, String> map = JsonUtil.getMapFromJson(body);
        String userid_str = map.get(JsonKey.USERID);
        String friendid_str = map.get(JsonKey.FRIENDID);
        if (userid_str == null || friendid_str == null) {
            return;
        }
        long userid = Long.valueOf(userid_str);
        long friendid = Long.valueOf(friendid_str);
        Tb_User_Api userApi = this.tbUserService.getTb_User_Api(userid, Tb_User_Api.REG_SOURCE_SINA);
        if (userApi == null) {
            return;
        }
        Tb_User_Api friendApi = this.tbUserService.getTb_User_Api(friendid, Tb_User_Api.REG_SOURCE_SINA);
        if (friendApi == null) {
            return;
        }
        try {
            SinaUtil.followUser(userApi.getAccess_token(), userApi.getToken_secret(), friendApi.getUid());
        } catch (WeiboException e) {
            log.error(e.getMessage());
        }
    }
}
