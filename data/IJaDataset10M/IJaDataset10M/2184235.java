package com.nhncorp.cubridqa.ha.bo;

import com.nhncorp.cubridqa.ha.HAServer;

/**
 * 
 * @ClassName: IHAServerBO
 * @Description:
 * @date 2009-9-4
 * @version V1.0 Copyright (C) www.nhn.com
 */
public interface IHAServerBO extends IBaseBO {

    String[] getAll(String haId);

    HAServer get(String haId, String ipAndType);

    int save(String haId, HAServer server);

    int save(String haId, String oldIpandType, HAServer server);

    int delete(String haId, String ipAndType);
}
