package org.xiangxji.util.framework.framework;

/**
 * 管道处理的输入，属于管道上下文的一部分
 * @author xiangxji
 *
 */
public interface IPipeInput extends java.io.Serializable {

    public Object getParameter(String key);
}
