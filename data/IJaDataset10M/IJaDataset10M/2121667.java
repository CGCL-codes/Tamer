package common.webapp.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import common.exception.MyException;
import common.exception.NotCurrentUserException;
import common.exception.NotLoginException;

/**
 * Class <code>AjaxAction</code> is AjaxAction
 * @author <a href="mailto:zmuwang@gmail.com">muwang zheng</a>
 * @version 1.0, 2011-4-10
 */
public class AjaxAction extends ViewAction {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = -6321947531132908749L;

    /**
	 * log
	 */
    protected static final Log LOG = LogFactory.getLog(AjaxAction.class);

    /**
	 * 成功
	 */
    public static final String TYPE_SUCCESS = "0";

    /**
	 * 失败
	 */
    public static final String TYPE_FAIL = "1";

    /**
	 * 不是当前用户
	 */
    public static final String TYPE_NOTCURRENTUSER = "11";

    /**
	 * 没登录
	 */
    public static final String TYPE_NOTLOGIN = "10";

    /**
	 * 默认message
	 */
    private static final String MESSAGE_DEFAULT = "操作成功";

    /**
	 * 默认操作失败信息
	 */
    public static final String MESSAGE_FAIL = "操作失败";

    /**
	 * 返回类型
	 */
    private String type;

    /**
	 * @param type the type to set
	 */
    public void setType(String type) {
        this.type = type;
    }

    /**
	 * @param type the type to set
	 */
    public void setType(Throwable e) {
        if (e instanceof NotLoginException) {
            this.type = TYPE_NOTLOGIN;
        } else if (e instanceof NotCurrentUserException) {
            this.type = TYPE_NOTCURRENTUSER;
        } else if (e instanceof MyException) {
            this.type = TYPE_FAIL;
        }
    }

    /**
	 * 返回类型：TYPE_SUCCESS，TYPE_FAIL
	 * @return
	 */
    public String getType() {
        return hasErrors() ? (type == null ? TYPE_FAIL : type) : TYPE_SUCCESS;
    }

    /**
	 * 获取消息
	 * @return String
	 */
    public String getMessage() {
        String ret = super.getMessage();
        return null == ret ? MESSAGE_DEFAULT : ret;
    }

    /**
	 * ajax返回数据
	 */
    public Object data;

    /**
	 * @return the data
	 */
    public Object getData() {
        return data;
    }

    /**
	 * @param data the data to set
	 */
    public void setData(Object data) {
        this.data = data;
    }
}
