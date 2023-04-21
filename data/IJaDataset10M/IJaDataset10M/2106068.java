package com.mlib.jmx;

import java.io.IOException;
import java.util.Collection;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * 获取Java服务器信息的接口
 * 
 * @author zitee@163.com
 * @创建时间 2009-11-11 14:24:22
 * @version 1.0
 */
public interface JMX {

    /**
	 * 获取所有的MBean的名字
	 * 
	 * @return
	 * @throws IOException
	 */
    public Collection<ObjectName> getAllBeanName() throws IOException;

    /**
	 * 获取某个MBean的属性
	 * 
	 * @param beanName
	 * @param attrName
	 * @return
	 * @throws AttributeNotFoundException
	 * @throws InstanceNotFoundException
	 * @throws MBeanException
	 * @throws ReflectionException
	 * @throws IOException
	 */
    public Object getAttribute(ObjectName beanName, String attrName) throws AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException, IOException;

    /**
	 * 获取服务器所在操作系统信息
	 * 
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
    public OSBean getSystemInfo() throws MalformedObjectNameException, NullPointerException;

    /**
	 * 获取内存池信息 - CodeCache
	 * 
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
    public MPoolBean getCodeCache() throws MalformedObjectNameException, NullPointerException;

    /**
	 * 获取内存池信息 - EdenSpace
	 * 
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
    public MPoolBean getEdenSpace() throws MalformedObjectNameException, NullPointerException;

    /**
	 * 获取内存池信息 - PermGen
	 * 
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
    public MPoolBean getPermGen() throws MalformedObjectNameException, NullPointerException;

    /**
	 * 获取内存池信息 - SurvivorSpace
	 * 
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
    public MPoolBean getSurvivorSpace() throws MalformedObjectNameException, NullPointerException;

    /**
	 * 获取内存池信息 - TenuredGen
	 * 
	 * @return
	 * @throws MalformedObjectNameException
	 * @throws NullPointerException
	 */
    public MPoolBean getTenuredGen() throws MalformedObjectNameException, NullPointerException;

    /**
	 * 关闭与服务器的链接
	 * 
	 * @throws IOException
	 */
    public void close() throws IOException;
}
