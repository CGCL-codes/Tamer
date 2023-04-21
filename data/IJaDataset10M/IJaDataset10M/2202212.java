package bank.cnaps2.manager.service;

import gneit.topbase.core.lifecycle.event.UserSessionDestroyedEvent;
import gneit.topbase.core.lifecycle.listener.UserSessionDestroyedListener;

public class Cnaps2UserSessionDestroyedListener extends UserSessionDestroyedListener {

    @Override
    protected void onProcess(String eventName, UserSessionDestroyedEvent event) {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>> UserSessionDestroyedEvent Listened.");
        System.out.println("********** " + event.getCode());
        System.out.println("********** " + event.getExtraInfo("publisher"));
    }
}
