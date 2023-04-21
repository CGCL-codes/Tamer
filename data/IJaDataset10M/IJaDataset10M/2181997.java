package org.xfeep.asura.core.event;

import java.util.EventListener;
import org.xfeep.asura.core.match.Matcher;

public interface ServiceEventListener extends EventListener {

    public Class getServiceClass();

    public Matcher getDetailMatcher();

    public void onServiceChanged(ServiceEvent event);
}
