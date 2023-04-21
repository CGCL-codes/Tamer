package org.gary.base.service;

import java.util.List;
import org.gary.base.model.Online;
import org.gary.core.factories.BasicServiceFactory;

/**
 * QQ在线服务列表接口
 * @author Gary
 *
 */
public interface OnlineService extends BasicServiceFactory<Online> {

    /**
	 * 得到在线QQ服务列表
	 * @return
	 */
    public List<Online> getOnline();
}
