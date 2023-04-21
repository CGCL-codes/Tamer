package com.google.code.jahath.gateway.ssh;

import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
import com.jcraft.jsch.UserInfo;

public class HostKeyRepositoryImpl implements HostKeyRepository {

    private final ServiceTracker tracker;

    public HostKeyRepositoryImpl(BundleContext bundleContext) {
        tracker = new ServiceTracker(bundleContext, HostKeyRepository.class.getName(), null);
        tracker.open();
    }

    public void add(HostKey hostkey, UserInfo ui) {
        throw new UnsupportedOperationException();
    }

    public int check(String host, byte[] key) {
        for (Object service : tracker.getServices()) {
            int result = ((HostKeyRepository) service).check(host, key);
            if (result != NOT_INCLUDED) {
                return result;
            }
        }
        return NOT_INCLUDED;
    }

    public HostKey[] getHostKey() {
        throw new UnsupportedOperationException();
    }

    public HostKey[] getHostKey(String host, String type) {
        throw new UnsupportedOperationException();
    }

    public String getKnownHostsRepositoryID() {
        throw new UnsupportedOperationException();
    }

    public void remove(String host, String type) {
        throw new UnsupportedOperationException();
    }

    public void remove(String host, String type, byte[] key) {
        throw new UnsupportedOperationException();
    }
}
