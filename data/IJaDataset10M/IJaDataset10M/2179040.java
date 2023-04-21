package org.javaongems.std.client.service;

public final class EndpointUtils {

    public interface Adapter {

        public String adapt(String service);
    }

    private EndpointUtils() {
    }

    private static Adapter instance;

    public static Adapter getInstance() {
        return instance;
    }

    public static void setInstance(Adapter instance) {
        EndpointUtils.instance = instance;
    }
}
