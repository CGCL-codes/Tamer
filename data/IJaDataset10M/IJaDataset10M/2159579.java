package cn.csust.net2.manager.shared.service;

import cn.csust.net2.manager.shared.util.ServiceConstant;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath(value = ServiceConstant.MANAGER + ServiceConstant.SERVICE_NAME_SCHOOLREGISTER)
public interface SchoolRegisterService extends BaseService {

    boolean testRegisterAble();
}
