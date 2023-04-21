package com.kongur.network.erp.manager.factory;

import com.kongur.network.erp.enums.EnumPlatform;
import com.kongur.network.erp.manager.ic.PlatformItemCatManager;
import com.kongur.network.erp.manager.ic.PlatformItemManager;

/**
 * ��Ʒ����Ŀ�ӿڹ���
 * @author gaojf
 * @version $Id: PlatformItemManagerFactory.java,v 0.1 2012-3-13 ����08:15:01 gaojf Exp $
 */
public interface PlatformItemManagerFactory {

    /**
     * ���ƽ̨��ö�Ӧ��Ʒ�ӿ�
     * @param platform
     * @return
     */
    public PlatformItemManager getPlatformItemManager(EnumPlatform platform);

    /**
     * ���ƽ̨��ö�Ӧ��Ŀ�ӿ�
     * @param platform
     * @return
     */
    public PlatformItemCatManager getPlatformItemCatManager(EnumPlatform platform);
}
