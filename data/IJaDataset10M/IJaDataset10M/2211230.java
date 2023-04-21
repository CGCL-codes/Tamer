package cn.zkoss.zk4love.web.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.util.GenericForwardComposer;
import org.zkoss.zul.api.Paging;
import org.zkoss.zul.event.PagingEvent;

public class PagingEventController extends GenericForwardComposer {

    private static final long serialVersionUID = 958938579523480947L;

    private Paging paging;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        paging.addEventListener("onPaging", new EventListener() {

            @Override
            public void onEvent(Event event) throws Exception {
                PagingEvent evt = (PagingEvent) event;
                System.out.println(" onEvent(:" + evt.getActivePage());
            }
        });
    }

    public void onPaging$paging(PagingEvent event) {
        System.out.println("onClick$paging:" + event.getActivePage());
    }
}
