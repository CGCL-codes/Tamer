package com.ergal.ezweb.widgets;

import com.ergal.ezweb.core.PageWrapper;
import com.ergal.ezweb.core.RespStringWrapper;

/**
 * 组件处理类的接口
 * 在此定义每个widgets的渲染方法 传入要渲染的对象和输出
 * 需要定义一个渲染链
 * 
 * 具体的渲染流程是这样的 如果发现了一个组件就把剩下的部分交给此组件来渲染
 * 
 * 
 * 可用的标签有 @BaseContextPath @Iterator @IfMatch
 * @author <a href="mailto:ohergal@gmail.com">ohergal</a>
 *
 */
public interface Renderable {

    void render(PageWrapper pageObj, RespStringWrapper Resp);
}
