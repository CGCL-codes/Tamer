package com.clanwts.lang;

import java.util.EventListener;

public interface EventProducer {

    public void addListener(EventListener l);

    public boolean isListener(EventListener l);

    public void removeListener(EventListener l);
}
