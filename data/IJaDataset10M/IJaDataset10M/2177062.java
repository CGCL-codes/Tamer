package com.aelitis.azureus.core.clientmessageservice.secure;

public interface SecureMessageServiceClientListener {

    public void complete(SecureMessageServiceClientMessage message);

    public void cancelled(SecureMessageServiceClientMessage message);

    public void aborted(SecureMessageServiceClientMessage message, String reason);
}
