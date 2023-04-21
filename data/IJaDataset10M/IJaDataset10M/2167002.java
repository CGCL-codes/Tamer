package com.sitechasia.webx.core.dao.hibernate3;

import org.springframework.orm.hibernate3.LocalSessionFactoryBean;
import com.sitechasia.webx.core.utils.base.ResourceBinder;

/**
 * 将原来的session factory的管理分开,细化,设置hbm文件,达到分模块管理
 * 和MyLocalSessionFactoryBean相比，100%的按目录走，但是带来的问题是
 * 需要启动n个spring的applicationcontext实例，同时各个模块的文件名字必须
 * 一样，而且不能用xml结尾，模块的bean的名字也一样，这样，就对命名又 多了一条限制，当然，spring本身就不支持◎局部变量◎
 *
 * @author <a href="mailto:zhoumengchun@myce.net.cn">mczhou</a>
 * @version 1.2
 * @since JDK1.5
 */
public class NamedMoudleHbmLocalSessionFactoryBean extends LocalSessionFactoryBean {

    private ResourceBinder resourceBinder;

    public NamedMoudleHbmLocalSessionFactoryBean(ResourceBinder resourceBinder) {
        this.resourceBinder = resourceBinder;
    }

    /**
	 * 将原来的session factory的管理分开,细化,设置hbm文件 达到分模块管理,
	 *
	 * @param moudleHbm 各个模块的管理服务
	 */
    public void setMappingResourceServices(Object moudleHbm) {
        String[] mappingResources = resourceBinder.getResourceFile();
        super.setMappingResources(mappingResources);
    }

    /**
	 * 覆盖超类的方法,使其失效
	 */
    public void setMappingResources(String[] mappingResources) {
    }
}
