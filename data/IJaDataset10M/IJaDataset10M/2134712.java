package com.guzzservices.manager;

import java.util.List;
import com.guzzservices.business.Configuration;
import com.guzzservices.business.ConfigurationGroup;
import com.guzzservices.sso.LoginUser;

/**
 * 系统配置manager
 * 
 * @author liu kaixuan
 * @date 2006-7-11 11:39:55
 */
public interface IConfigurationManager {

    public ConfigurationGroup getGroup(String groupId);

    public List<Configuration> listConfigurations(String groupId);

    public void addGroup(LoginUser loginUser, ConfigurationGroup group);

    public void updateGroup(ConfigurationGroup group);

    public Configuration getById(String id);

    public Configuration getByParameterName(String groupName, String paramName);

    public void update(Configuration config);

    public void updateByBatch(String groupId, List<Configuration> configs);

    public void save(Configuration config);
}
