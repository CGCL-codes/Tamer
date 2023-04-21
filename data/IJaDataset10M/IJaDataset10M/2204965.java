package org.mm.proxycache.config;

import com.google.inject.Singleton;

@Singleton
public class Config {

    public String proxyAddress;

    public int proxyPort;

    public boolean cacheEnabled;

    public boolean cachePrivate;
}
