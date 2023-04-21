package org.wijiscommons.ssaf.process.dropoff;

import org.wijiscommons.ssaf.config.SSAFConfigProvider;
import org.wijiscommons.ssaf.config.impl.SSAFConfigProviderImpl;
import org.wijiscommons.ssaf.exception.DropoffMailBoxException;
import org.wijiscommons.ssaf.message.Message;

public interface SSAFMessageReceivalProcess {

    SSAFConfigProvider provider = SSAFConfigProviderImpl.getInstance();

    public boolean receiveMessage(Message message) throws DropoffMailBoxException;
}
