package com.hk.jms;

import java.util.Map;
import com.hk.frame.util.DataUtil;
import com.hk.frame.util.JsonUtil;

public class JmsMsg {

    public static final String HEAD_NEWS_CREATE_ITEM = "news_create_item";

    public static final String HEAD_NEWS_CREATE_ITEM_CMT = "news_create_item_cmt";

    public static final String HEAD_NEWS_CREATE_ASK = "news_create_ask";

    public static final String HEAD_NEWS_CREATE_ANSWER = "news_create_answer";

    public static final String HEAD_NEWS_CREATE_FOLLOW = "news_create_follow";

    public static final String HEAD_OTHER_API_SINAFOLLOW_CREATE_FOLLOW = "other_api_sinafollow_create_follow";

    public static final String HEAD_OTHER_API_CREATE_ITEM_CMT_TO_STATUS = "other_api_create_item_cmt_to_status";

    public static final String HEAD_OTHER_API_CREATE_ASK_TO_STATUS = "other_api_create_ask_to_status";

    public static final String HEAD_OTHER_API_CREATE_ANSWER_TO_STATUS = "other_api_create_answer_to_status";

    public static final String HEAD_OTHER_API_SHARE_ITEM = "other_api_share_item";

    public static final String HEAD_NOTICE_CREATE_ITEM_CMT = "notice_create_item_cmt";

    public static final String HEAD_NOTICE_FOLLOW_USER = "notice_follow_user";

    public static final String HEAD_NOTICE_CREATE_ANSWER = "notice_answer";

    public static final String HEAD_NOTICE_CREATE_ITEM_CMT_REPLY = "notice_create_item_cmt_reply";

    public static final String HEAD_NOTICE_SELECT_BEST_ANSWER = "notice_select_best_answer";

    public static final String HEAD_USER_REPORT_ADDITEM = "user_report_additem";

    public JmsMsg() {
    }

    public JmsMsg(String value) {
        String[] tmp = value.split("&");
        this.head = tmp[0];
        this.body = DataUtil.urlDecoder(tmp[1]);
    }

    public JmsMsg(String head, String body) {
        this.head = head;
        this.body = body;
    }

    private String head;

    /**
	 * json格数数据
	 */
    private String body;

    public void setHead(String head) {
        this.head = head;
    }

    public String getHead() {
        return head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setBody(Map<String, String> map) {
        this.setBody(JsonUtil.toJson(map));
    }

    public String toMessage() {
        return this.head + "&" + DataUtil.urlEncoder(this.body);
    }
}
