package com.systop.common.modules.template.freemarker;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * <code>FreeMarkerConfigurationManager</code>继承了
 * {@link FreeMarkerConfigurationFactory},使之可以添加新的
 * @author Sam Lee
 * 
 */
@SuppressWarnings("unchecked")
public class FreeMarkerConfigurationManager extends FreeMarkerConfigurationFactory {

    /**
   * 用于保存Tamplate文件的路径.
   */
    private Set paths = new HashSet();

    /**
   * 用于标识是否添加了新的templateLoaderPath
   */
    private boolean pathChanged = false;

    /**
   * instance of freemarker Configuration.因为FreeMarkerConfigurationManager
   * 是由spring管理的bean，所以configuration是符合spring bean的生命周期的。在
   * FreeMarkerConfigurationManager中，
   * 如果pathChanged=true,configuration会重新 创建.
   */
    private Configuration configuration;

    /**
   * @see FreeMarkerConfigurationFactory#createConfiguration()
   */
    @Override
    public Configuration createConfiguration() throws IOException, TemplateException {
        try {
            if (configuration == null || pathChanged) {
                this.setTemplateLoaderPaths(null);
                configuration = super.createConfiguration();
                logger.info("Create new freemarker Configuration");
            } else {
                logger.info("Use existed Configuration");
            }
        } catch (FileNotFoundException e) {
            logger.warn("模板目录不存在:" + e.getMessage());
        }
        return configuration;
    }

    /**
   * @see FreeMarkerConfigurationFactory#setTemplateLoaderPath(String)
   */
    @Override
    public void setTemplateLoaderPath(String templateLoaderPath) {
        if (!paths.contains(templateLoaderPath)) {
            paths.add(templateLoaderPath);
            pathChanged = true;
            logger.info("Add new path [" + templateLoaderPath + "]");
        }
    }

    /**
   * @see FreeMarkerConfigurationFactory#setTemplateLoaderPaths(String[])
   */
    @Override
    public void setTemplateLoaderPaths(String[] templateLoaderPaths) {
        if (templateLoaderPaths != null) {
            for (int i = 0; i < templateLoaderPaths.length; i++) {
                setTemplateLoaderPath(templateLoaderPaths[i]);
            }
        }
        if (pathChanged) {
            String[] loaderPaths = (String[]) paths.toArray(new String[] {});
            if (loaderPaths != null) {
                logger.info("Reset templateLoader paths, size is [" + loaderPaths.length + "]");
                super.setTemplateLoaderPaths(loaderPaths);
            }
        }
    }
}
