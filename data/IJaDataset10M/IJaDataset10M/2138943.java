package net.solosky.maplefetion.event.action.success;

import net.solosky.maplefetion.event.action.SuccessEvent;
import net.solosky.maplefetion.sipc.SipcStatus;

/**
 *
 * 发送消息成功
 *
 * @author solosky <solosky772@qq.com>
 *
 */
public class SendChatMessageSuccessEvent extends SuccessEvent {

    /**
	 * 服务器发回消息的返回值
	 */
    private int sipcStatus;

    /**
	 * @param sipcStatus
	 */
    public SendChatMessageSuccessEvent(int sipcStatus) {
        super();
        this.sipcStatus = sipcStatus;
    }

    /**
	 * 是否发送至手机
	 * @return
	 */
    public boolean isSendToMobile() {
        return this.sipcStatus == SipcStatus.SEND_SMS_OK;
    }

    /**
	 * 是否发送至客户端
	 * @return
	 */
    public boolean isSendToClient() {
        return this.sipcStatus == SipcStatus.ACTION_OK;
    }
}
