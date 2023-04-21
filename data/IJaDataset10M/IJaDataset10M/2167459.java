package com._pmz0178.blogtxt.swing.util;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author pasha
 * 
 * @date 20080223
 */
public class BlogBeanFactory {

    public static final String MAIN_FRAME = "mainFrame";

    public static final String ABOUT_DIALOG = "aboutDialog";

    public static final String OPTIONS_DIALOG = "optionsDialog";

    public static final String BLOG_EDIT_DIALOG = "blogEditDialog";

    public static final String BLOG_DELETE_DIALOG = "blogDeleteDialog";

    public static final String CATEGORY_EDIT_DIALOG = "categoryEditDialog";

    public static final String CATEGORY_DELETE_DIALOG = "categoryDeleteDialog";

    public static final String TREE_ROOT_BUILDER = "treeRootBuilder";

    public static final String CONFIG_SERVICE = "configService";

    public static final String LAUNCHER_SERVICE = "launcherService";

    public static final String EXPORT_SERVICE = "exportService";

    public static final String BLOG_ITEM_EDIT_DIALOG = "blogItemEditDialog";

    private static final String RESOURCE_NAME = "blog-txt-swing-beans.xml";

    private static final BeanFactory beanFactory;

    static {
        AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(new String[] { RESOURCE_NAME });
        beanFactory = ctx.getBeanFactory();
        ctx.registerShutdownHook();
    }

    private BlogBeanFactory() {
    }

    /**
	 * @param name
	 * @return
	 */
    public static final Object getBean(String name) {
        return beanFactory.getBean(name);
    }
}
