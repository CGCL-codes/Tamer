package com.adserversoft.flexfuse.server.api.service;

import com.adserversoft.flexfuse.server.api.ui.ServerRequest;
import java.util.Map;

/**
 * Author: Vitaly Sazanovich
 * Email: Vitaly.Sazanovich@gmail.com
 */
public interface ITemplatesManagementService {

    public String[] getAdTag(ServerRequest sr);

    String getAdCode(ServerRequest sr, Map<String, Object> paramsMap);
}
