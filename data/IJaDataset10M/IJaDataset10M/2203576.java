package org.audiocomplex.bt;

public interface BluetoothDiscoveryListener {

    void discoveryCompleted();

    void serviceDiscovered(String name, String url);
}
