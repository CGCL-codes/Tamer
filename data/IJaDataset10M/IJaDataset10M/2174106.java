package cn.csust.net2.manager.client.ux;

import com.extjs.gxt.ui.client.widget.Info;
import com.google.gwt.user.client.rpc.AsyncCallback;

/** 
 * @author 韩忠金
 * @version 创建时间：2012-2-5 上午10:03:27 
 * 类说明 
 * 将回调客户端的异常统一处理
 * 
 */
public abstract class ServiceCallback<T> implements AsyncCallback<T> {

    @Override
    public void onFailure(Throwable caught) {
        Info.display("系统信息", "Update操作失败1111");
    }
}
