package sisc.io.custom;

import sisc.data.Value;

public interface CustomPort {

    public CustomPortProxy getProxy();

    public Value getPortLocal();

    public void setPortLocal(Value v);
}
