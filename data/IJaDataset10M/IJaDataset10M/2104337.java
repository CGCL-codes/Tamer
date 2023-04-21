package jcontrol.eib.extended.a_layer;

public interface A_Service extends A_BroadcastService, A_GroupDataService, A_DataConnlessService, A_DataConnectedService {

    public void setListener(Listener l);

    public interface Listener extends A_BroadcastService.Listener, A_GroupDataService.Listener, A_DataConnlessService.Listener, A_DataConnectedService.Listener {
    }
}
