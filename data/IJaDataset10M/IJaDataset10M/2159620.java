package it.gashale.jacolib.rpc;

import it.gashale.jacolib.core.JacolibError;
import it.gashale.jacolib.json.Serializer;
import java.io.StringReader;

public class RPCClientStub {

    private Serializer serializer;

    private RPCChannel channel;

    protected RPCClientStub(Serializer ser) {
        serializer = ser;
    }

    public RPCChannel getRPCChannel() {
        return channel;
    }

    public void setRPCChannel(RPCChannel v) {
        channel = v;
    }

    public RPCResponse exec(RPCRequest req) throws JacolibError {
        String res = exec(serializer.encode(req));
        return (RPCResponse) serializer.decode(new StringReader(res));
    }

    public String exec(String req) throws JacolibError {
        return channel.exec(req);
    }

    public void quit() throws JacolibError {
        channel.quit();
    }

    protected void quit_p() {
        serializer = null;
        channel = null;
    }
}
