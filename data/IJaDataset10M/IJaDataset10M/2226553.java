package net.deytan.wofee.gae.persistence.proxy;

public interface Proxy {

    boolean isLoaded();

    Object getTarget();
}
