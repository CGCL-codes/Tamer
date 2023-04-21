package net.simpleframework.workflow;

import java.util.Collection;

/**
 * 这是一个开源的软件，请在LGPLv3下合法使用、修改或重新发布。
 * 
 * @author 陈侃(cknet@126.com, 13910090885)
 *         http://code.google.com/p/simpleframework/
 *         http://www.simpleframework.net
 */
public interface IListenerAware<T extends AbstractWorkflowBean> {

    Collection<String> getEventListeners(T bean);

    void addEventListener(T bean, String listenerClass);

    boolean removeEventListener(T bean, String listenerClass);
}
