package com.taobao.top.domain;

import com.taobao.top.mapping.ApiClass;
import com.taobao.top.mapping.ApiField;
import com.taobao.top.mapping.ApiListClass;

/**
 * NotifyInfo Data Structure.
 *
 * @author carver.gu
 * @since 1.0, Apr 11, 2010
 */
@ApiClass("notify_info")
@ApiListClass("notify_infos")
public class NotifyInfo extends BaseObject {

    private static final long serialVersionUID = 1L;

    @ApiField("is_notify")
    private String isNotify;

    @ApiField("topic")
    private String topic;

    public String getIsNotify() {
        return this.isNotify;
    }

    public void setIsNotify(String isNotify) {
        this.isNotify = isNotify;
    }

    public String getTopic() {
        return this.topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
