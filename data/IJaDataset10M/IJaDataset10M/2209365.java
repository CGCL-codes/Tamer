package com.chungco.rest.evdb.service;

import com.chungco.rest.evdb.IEvdbConfig;

public class MockEvdbConfig implements IEvdbConfig {

    public String getHostName() {
        return "http://api.evdb.com";
    }
}
