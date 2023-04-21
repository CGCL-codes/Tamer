package org.mobicents.ssf.flow.event;

import org.mobicents.ssf.event.EventType;

/**
 * SipFlowEventFactoryから呼び出されます。
 * このインタフェースを実装したクラスをSpring上で定義することで、生成するイベントを
 * 追加することが可能です。
 * 
 * @author nisihara
 *
 */
public interface EventCreator {

    public SipFlowEvent createEvent(Object source, Object event, EventType type);
}
