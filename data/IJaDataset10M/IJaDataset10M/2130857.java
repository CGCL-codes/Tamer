package org.jdiameter.client.impl.app.cca;

import java.io.Serializable;
import org.jdiameter.api.Request;
import org.jdiameter.common.api.app.cca.ClientCCASessionState;
import org.jdiameter.common.api.app.cca.ICCASessionData;

/**
 * 
 * @author <a href="mailto:baranowb@gmail.com"> Bartosz Baranowski </a>
 * @author <a href="mailto:brainslog@gmail.com"> Alexandre Mendonca </a>
 */
public interface IClientCCASessionData extends ICCASessionData {

    public boolean isEventBased();

    public void setEventBased(boolean b);

    public boolean isRequestTypeSet();

    public void setRequestTypeSet(boolean b);

    public ClientCCASessionState getClientCCASessionState();

    public void setClientCCASessionState(ClientCCASessionState state);

    public Serializable getTxTimerId();

    public void setTxTimerId(Serializable txTimerId);

    public Request getTxTimerRequest();

    public void setTxTimerRequest(Request txTimerRequest);

    public Request getBuffer();

    public void setBuffer(Request buffer);

    public int getGatheredRequestedAction();

    public void setGatheredRequestedAction(int gatheredRequestedAction);

    public int getGatheredCCFH();

    public void setGatheredCCFH(int gatheredCCFH);

    public int getGatheredDDFH();

    public void setGatheredDDFH(int gatheredDDFH);
}
