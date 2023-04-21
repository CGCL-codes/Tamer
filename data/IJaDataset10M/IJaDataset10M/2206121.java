package org.fto.jthink.j2ee.ejb;

import org.fto.jthink.config.Configuration;
import org.fto.jthink.resource.ResourceManager;
import org.fto.jthink.util.ReflectHelper;

/**
 * 主要用于查找EJB的主接口。 具体应用中建议创建此类型的子类型来查找EJB主接口，
 * 这样方便在不同EJB容器环境中自动切换，可以只修改配置文件的情况下就可以将应用运行在远程EJB容器/本地EJB容器/非EJB容器中。
 * 
 *
 * <p><pre><b>
 * 历史更新记录:</b>
 * 2005-07-04  创建此类型
 * </pre></p>
 * 
 * 
 * @author   wenjian
 * @version  1.0
 * @since    JThink 1.0
 * 
 */
public class EJBCaller {

    private ResourceManager resManager;

    private String resContainerName;

    private Configuration config;

    /**
   * 创建EJBCaller类型的实例
   * 
   * @param resManager       资源管理器
   * @param resContainerName 资源容器名, 用于缓存EJB的Home主接口。注意：此资源容器名称所对应的资源容器必须要存在于resManager中
   * @param config           系统配置信息
   */
    public EJBCaller(ResourceManager resManager, String resContainerName, Configuration config) {
        this.resManager = resManager;
        this.resContainerName = resContainerName;
        this.config = config;
    }

    /**
   * 返回EJBHomeFactory工厂。 此工厂必须要在配置文件中正确配置。
   * 
   * @param serverId EJB服务器ID，此ID是在配置文件中配置的EJB服务器的ID
   * 
   * @return EJBHomeFactory工厂接口的具体实现的实例
   */
    public EJBHomeFactory getEJBHomeFactory(String serverId) {
        String factoryClass = config.getEJBServerConfig(serverId).getChild("ejb-caller").getChildText("factory-class");
        return (EJBHomeFactory) ReflectHelper.newInstance(ReflectHelper.forName(factoryClass), new Class[] { ResourceManager.class, String.class, Configuration.class, String.class }, new Object[] { resManager, resContainerName, config, serverId });
    }
}
