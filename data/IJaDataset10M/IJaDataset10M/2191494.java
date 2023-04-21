package org.gardel.service.impl;

import org.gardel.service.Service;

public class ServiceImpl implements Service {

    private boolean gone = false;

    public void go() {
        System.out.println("Do something!!!");
        gone = true;
    }

    public boolean isGone() {
        return gone;
    }
}
