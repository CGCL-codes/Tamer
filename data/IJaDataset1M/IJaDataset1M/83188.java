package com.sf.plctest.s7emulator;

public interface EventSender {

    public void addEventListener(EventListener el);

    public void removeEventListener(EventListener el);
}
