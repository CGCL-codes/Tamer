package com.hs.framework.common.util;

public class BeanUtil {

    /**
	 * 通过类名获得类的实例
	 * @param beanName 类的全路径名
	 * @return 该类的实例
	 */
    public static Object getBean(String beanName) {
        Object result = null;
        try {
            Class thisClass = Class.forName(beanName);
            result = thisClass.newInstance();
            LogUtil.info("获得一个 Class 实例: " + beanName + " ---- from config");
        } catch (ClassNotFoundException e) {
            LogUtil.error("没有找到名字为：" + beanName + " 的类。");
            LogUtil.error("����错误信息：" + e);
        } catch (InstantiationException e) {
            LogUtil.error("加载指定类出错：" + beanName);
            LogUtil.error("����错误信息：" + e);
        } catch (IllegalAccessException e) {
            LogUtil.error("����实例化类出错：" + beanName);
            LogUtil.error("错误信息：" + e);
        }
        return result;
    }
}
