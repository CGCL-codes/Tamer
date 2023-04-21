package com.lavans.lremote.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.xpath.XPathExpressionException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import com.lavans.luz2.commons.StringUtils;
import com.lavans.luz2.utils.Config;

/**
 * Service Manager Implementation for XML file(default).
 * @author dobashi
 *
 */
public class BeanManagerXml {

    /** logger */
    private static final Log logger = LogFactory.getLog(BeanManagerXml.class);

    /**  */
    private static Map<String, String> packageNameMap = new ConcurrentHashMap<String, String>();

    /** cache of all service */
    private static Map<String, BeanInfo> beanMap = new ConcurrentHashMap<String, BeanInfo>();

    private BeanManagerXml() {
    }

    static {
        init();
    }

    /** initialize */
    public static void init() {
        beanMap.clear();
        Config config = Config.getInstance("lremote.xml");
        try {
            NodeList packageList = config.getNodeList("/lremote/beans");
            for (int i = 0; i < packageList.getLength(); i++) {
                Element node = (Element) packageList.item(i);
                String group = node.getAttribute("group");
                String packageName = node.getAttribute("package");
                packageNameMap.put(group, packageName);
            }
            NodeList nodeList = config.getNodeList("//bean");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Element node = (Element) nodeList.item(i);
                Node parent = node.getParentNode();
                BeanInfo bean = new BeanInfo();
                bean.id = node.getAttribute("id");
                bean.className = node.getAttribute("class");
                if (parent.getNodeName().equals("beans")) {
                    bean.group = node.getAttribute("group");
                    bean.packageName = node.getAttribute("package");
                    if (!StringUtils.isEmpty(bean.packageName)) {
                        bean.id = bean.packageName + "." + bean.id;
                        bean.className = bean.packageName + "." + bean.className;
                    }
                }
                beanMap.put(bean.id, bean);
            }
        } catch (XPathExpressionException e) {
            logger.error("Unable to find /lremote/service_base_path");
        }
    }

    /**
	 * get bean class. �p�b�P�[�W�w��L��
	 * @param name
	 * @param id
	 * @return
	 */
    public static Class<? extends Object> getBeanClass(String group, String id) {
        return getBeanClass(toFullId(group, id));
    }

    /**
	 * bean��reflection�N���X��Ԃ��B
	 * @param id
	 * @return
	 */
    public static Class<? extends Object> getBeanClass(String id) {
        BeanInfo bean = getBeanInfo(id);
        return bean.getClazz();
    }

    /**
	 * get bean instance. �p�b�P�[�W�w��L��
	 * @param name
	 * @param id
	 * @return
	 */
    public static Object getBean(String group, String id) {
        return getBean(toFullId(group, id));
    }

    /**
	 * bean��singleton�C���X�^���X��Ԃ�
	 * @param id
	 * @return
	 */
    public static Object getBean(String id) {
        BeanInfo bean = getBeanInfo(id);
        return bean.getInstance();
    }

    public static String toFullId(String group, String id) {
        String packageName = packageNameMap.get(group);
        if (!StringUtils.isEmpty(packageName)) {
            id = packageName + "." + id;
        }
        return id;
    }

    private static BeanInfo getBeanInfo(String id) {
        BeanInfo bean = beanMap.get(id);
        if (bean == null) {
            bean = new BeanInfo();
            bean.id = bean.className = id;
            beanMap.put(id, bean);
        }
        return bean;
    }
}

class BeanInfo {

    /** logger */
    private static final Log logger = LogFactory.getLog(BeanInfo.class);

    /** parent group info */
    public String group;

    public String packageName;

    /** bean info */
    public String id;

    public String className;

    public String initMethod;

    private Class<? extends Object> clazz = null;

    private Object instance = null;

    /**
	 * Class�N���X��Ԃ��B��x�ǂݍ��񂾂�L���b�V�����čė��p�B
	 * @return
	 */
    public Class<? extends Object> getClazz() {
        if (clazz != null) {
            return clazz;
        }
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            logger.error("bean class is not found[" + className + "]", e);
        }
        return clazz;
    }

    /**
	 * instance��Ԃ��B��x�ǂݍ��񂾂�L���b�V�����čė��p�B
	 * @return
	 */
    public Object getInstance() {
        if (instance != null) {
            return instance;
        }
        try {
            instance = getClazz().newInstance();
        } catch (Exception e) {
            logger.error("bean instance cannot created [" + className + "]", e);
        }
        if (!StringUtils.isEmpty(initMethod)) {
            try {
                clazz.getMethod(initMethod, (Class<?>[]) null).invoke(instance, (Object[]) null);
            } catch (Exception e) {
                logger.error("init method call error [" + className + "#" + initMethod + "()]", e);
            }
        }
        return instance;
    }
}
