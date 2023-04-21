package com.pcmsolutions.device.EMU.database.events.content;

/**
 * User: paulmeehan
 * Date: 03-Sep-2004
 * Time: 18:15:51
 */
public interface ContentEventHandler<CE extends ContentEvent, RE extends ContentRequestEvent> {

    void postEvent(CE ev);

    void postEvent(CE ev, boolean externalFirst);

    void postInternalEvent(CE ev);

    void postExternalEvent(CE ev);

    public void sendInternalEvent(final CE ev) throws Exception;

    boolean sendRequest(RE ev);

    void sync();
}
