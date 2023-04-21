package org.chessworks.chessclub;

import java.util.Arrays;
import org.chessworks.chessclub.datagrams.DatagramType;
import org.chessworks.chessclub.socket.TrafficListener;

public class TrafficListenerStub implements TrafficListener {

    @Override
    public void onBell() {
        System.out.print("onBell();\n");
    }

    @Override
    public void onConnected() {
        System.out.print("onConnected();\n");
    }

    @Override
    public void onDisconnected(Exception cause) {
        System.out.format("onDisconnected(%1s);\n", String.valueOf(cause));
    }

    @Override
    public void onLevel1Begin(int depth, String user, DatagramType type, String windowID) {
        System.out.format("onLevel1Begin(%1d, \"%1s\", %1s, \"%1s\");\n", depth, user, type, windowID);
    }

    @Override
    public void onLevel1End(int depth) {
        System.out.format("onLevel1End(%1d);\n", depth);
    }

    @Override
    public void onLevel2(int depth, DatagramType type, String[] args) {
        System.out.format("onLevel2(%1d, %2s, %3s);\n", depth, type, Arrays.toString(args));
    }

    @Override
    public void onMyCmdBegin(int depth) {
        System.out.format("onMyCmdBegin(%1d);\n", depth);
    }

    @Override
    public void onMyCmdEnd(int depth) {
        System.out.format("onMyCmdEnd(%1d);\n", depth);
    }

    @Override
    public void onSpecialComment(int depth, String comment) {
        System.out.format("onText(%1d, \"%2s\");\n", depth, String.valueOf(comment));
    }

    @Override
    public void onText(int depth, String text) {
        System.out.format("onText(%1d, \"%2s\");\n", depth, String.valueOf(text));
    }

    @Override
    public void onOutboundCommand(String msg) {
        System.out.format("onOutboundCommand(\"%1s\");\n", String.valueOf(msg));
    }
}
