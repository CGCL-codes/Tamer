package org.nexopenframework.example.dwr.events;

import org.springframework.context.ApplicationEvent;

/**
 * <p>
 * NexTReT Open Framework
 * </p>
 * 
 * <p>
 * Test event class for AjaxNotifier
 * </p>
 * 
 * @author <a href="mailto:mbc@nextret.net">Marc Baiges Camprub�</a>
 * @version 1.0
 * @since 1.0
 */
public class TestEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;

    public String notifyInfo;

    public TestEvent(Object source) {
        super(source);
        notifyInfo = "hello";
    }

    public String getNotifyInfo() {
        return notifyInfo;
    }
}
