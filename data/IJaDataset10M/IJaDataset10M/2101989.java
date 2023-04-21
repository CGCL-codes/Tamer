package cn.myapps.core.workflow.notification.ejb;

import java.util.ArrayList;
import java.util.Collection;
import cn.myapps.core.user.ejb.BaseUser;
import cn.myapps.core.user.ejb.UserVO;

public class SendNotification extends Notification {

    private int[] receiverTypes;

    private BaseUser submitter;

    /**
	 * 构造方法
	 * 
	 * @param applicationid
	 *            应用标识
	 */
    public SendNotification(String applicationid) {
        this.applicationid = applicationid;
    }

    /**
	 * 获取接收类型
	 * 
	 * @return 接收类型
	 */
    public int[] getReceiverTypes() {
        return receiverTypes;
    }

    /**
	 * 设置接收类型
	 * 
	 * @param receiverTypes
	 *            接收类型
	 */
    public void setReceiverTypes(int[] receiverTypes) {
        this.receiverTypes = receiverTypes;
    }

    /**
	 * 发送信息给用户
	 * 
	 * @param responsible
	 * @throws Exception
	 */
    public void send() throws Exception {
        Collection responsibles = new ArrayList();
        for (int i = 0; i < receiverTypes.length; i++) {
            switch(receiverTypes[i]) {
                case RESPONSIBLE_TYPE_SUBMITTER:
                    responsibles.add(submitter);
                    break;
                case RESPONSIBLE_TYPE_AUTHOR:
                    responsibles.add((UserVO) document.getAuthor());
                    break;
                case RESPONSIBLE_TYPE_ALL:
                    responsibles.addAll(this.responsibles);
                    break;
                default:
                    break;
            }
        }
        setResponsibles(responsibles);
        super.send();
    }

    /**
	 * 获取提交者
	 * 
	 * @return 提交者
	 */
    public BaseUser getSubmitter() {
        return submitter;
    }

    /**
	 * 设置提交者
	 * 
	 * @param submitter
	 *            提交者
	 */
    public void setSubmitter(BaseUser submitter) {
        this.submitter = submitter;
    }
}
