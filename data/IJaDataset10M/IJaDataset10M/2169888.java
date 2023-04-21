package com.easyblog.core.service;

import com.easyblog.core.dto.Config;

public interface ConfigService {

    public Config getConfig();

    public void updateConfig(Config config);
}
