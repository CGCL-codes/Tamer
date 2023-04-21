package tcg.scada.cos;

import org.omg.PortableServer.POA;

/**
 * Generated from IDL interface "ICosDataPointClient".
 *
 * @author JacORB IDL compiler V 2.3-beta-2, 14-Oct-2006
 * @version generated at 18-Dec-2009 13:41:19
 */
public class ICosDataPointClientPOATie extends ICosDataPointClientPOA {

    private ICosDataPointClientOperations _delegate;

    private POA _poa;

    public ICosDataPointClientPOATie(ICosDataPointClientOperations delegate) {
        _delegate = delegate;
    }

    public ICosDataPointClientPOATie(ICosDataPointClientOperations delegate, POA poa) {
        _delegate = delegate;
        _poa = poa;
    }

    public tcg.scada.cos.ICosDataPointClient _this() {
        return tcg.scada.cos.ICosDataPointClientHelper.narrow(_this_object());
    }

    public tcg.scada.cos.ICosDataPointClient _this(org.omg.CORBA.ORB orb) {
        return tcg.scada.cos.ICosDataPointClientHelper.narrow(_this_object(orb));
    }

    public ICosDataPointClientOperations _delegate() {
        return _delegate;
    }

    public void _delegate(ICosDataPointClientOperations delegate) {
        _delegate = delegate;
    }

    public POA _default_POA() {
        if (_poa != null) {
            return _poa;
        }
        return super._default_POA();
    }

    public void cosCallback_OnServerShutdown(int subscriptionGroup) {
        _delegate.cosCallback_OnServerShutdown(subscriptionGroup);
    }

    public void cosCallback_OnServerSwitchOver(int subscriptionGroup, tcg.syscontrol.cos.CosOperationModeEnum operationMode) {
        _delegate.cosCallback_OnServerSwitchOver(subscriptionGroup, operationMode);
    }

    public void cosCallback_OnDataPointValueChange(int subscriptionGroup, long timestamp, tcg.scada.cos.CosSubscriptionItemStruct[] items) {
        _delegate.cosCallback_OnDataPointValueChange(subscriptionGroup, timestamp, items);
    }

    public void cosPoll() throws tcg.syscontrol.cos.CosPollException {
        _delegate.cosPoll();
    }
}
