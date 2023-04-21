package net.kano.joustsim.oscar.oscar.service.icbm.ft.controllers;

import java.net.InetAddress;

public interface PassiveConnector extends Connector {

    int getLocalPort();

    InetAddress getLocalHost();
}
