package org.freelords.network.client.fake;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import java.io.IOException;
import org.freelords.network.fake.FakeKryo;

public class FakeKryonetClient extends Client {

    public boolean isRunning = false;

    public boolean listenerAdded = false;

    public boolean commandSent = false;

    public boolean isConnected = false;

    public FakeKryo kryo;

    @Override
    public Kryo getKryo() {
        this.kryo = new FakeKryo();
        return kryo;
    }

    @Override
    public void start() {
        this.isRunning = true;
    }

    @Override
    public void connect(int i, String string, int i1) throws IOException {
        this.isConnected = true;
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public void addListener(Listener listener) {
        this.listenerAdded = true;
    }

    @Override
    public int sendTCP(Object object) {
        this.commandSent = true;
        return 0;
    }
}
