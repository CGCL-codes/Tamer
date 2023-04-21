package com.aimluck.eip.services.orgutils.impl;

import com.aimluck.eip.services.orgutils.ALOrgUtilsFactoryService;
import com.aimluck.eip.services.orgutils.ALOrgUtilsHandler;

/**
 * 同期サービス用ファクトリクラスです。 <BR>
 * 
 * 
 */
public class ALDefaultOrgUtilsFactoryService extends ALOrgUtilsFactoryService {

    private final ALOrgUtilsHandler handler = new ALDefaultOrgUtilsHandler();

    @Override
    public ALOrgUtilsHandler getOrgUtilsHandler() {
        return handler;
    }
}
