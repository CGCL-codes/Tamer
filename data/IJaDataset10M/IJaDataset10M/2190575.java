package aimclient;

import net.kano.joscar.ByteBlock;
import net.kano.joscar.tlv.Tlv;
import net.kano.joscar.tlv.TlvTools;
import net.kano.joscar.flap.ClientFlapConn;
import net.kano.joscar.flap.FlapPacketEvent;
import net.kano.joscar.flapcmd.LoginFlapCmd;
import net.kano.joscar.flapcmd.SnacCommand;
import net.kano.joscar.flapcmd.SnacPacket;
import net.kano.joscar.net.ClientConnEvent;
import net.kano.joscar.snac.SnacPacketEvent;
import net.kano.joscar.snac.SnacResponseEvent;
import net.kano.joscar.snaccmd.auth.AuthRequest;
import net.kano.joscar.snaccmd.auth.AuthResponse;
import net.kano.joscar.snaccmd.auth.ClientVersionInfo;
import net.kano.joscar.snaccmd.auth.KeyRequest;
import net.kano.joscar.snaccmd.auth.KeyResponse;
import net.kano.joscar.snaccmd.chat.ChatMsg;
import net.kano.joscar.snaccmd.chat.SendChatMsgIcbm;
import java.net.InetAddress;

public class LoginConn extends AbstractFlapConn {

    protected boolean loggedin = false;

    public LoginConn(JoscarTester tester) {
        super(tester);
    }

    public LoginConn(String host, int port, JoscarTester tester) {
        super(host, port, tester);
    }

    public LoginConn(InetAddress ip, int port, JoscarTester tester) {
        super(ip, port, tester);
    }

    protected void handleStateChange(ClientConnEvent e) {
        System.out.println("login connection state is now " + e.getNewState() + ": " + e.getReason());
        if (e.getNewState() == ClientFlapConn.STATE_CONNECTED) {
            System.out.println("sending flap version and key request");
            getFlapProcessor().sendFlap(new LoginFlapCmd());
            request(new KeyRequest(tester.getScreenname()));
        } else if (e.getNewState() == ClientFlapConn.STATE_FAILED) {
            tester.loginFailed("connection failed: " + e.getReason());
        } else if (e.getNewState() == ClientFlapConn.STATE_NOT_CONNECTED) {
            if (!loggedin) {
                tester.loginFailed("connection lost: " + e.getReason());
            }
        }
    }

    protected void handleFlapPacket(FlapPacketEvent e) {
    }

    protected void handleSnacPacket(SnacPacketEvent e) {
    }

    protected void handleSnacResponse(SnacResponseEvent e) {
        SnacCommand cmd = e.getSnacCommand();
        System.out.println("login conn got command " + Integer.toHexString(cmd.getFamily()) + "/" + Integer.toHexString(cmd.getCommand()) + ": " + cmd);
        if (cmd instanceof KeyResponse) {
            KeyResponse kr = (KeyResponse) cmd;
            ByteBlock authkey = kr.getKey();
            ClientVersionInfo version = new ClientVersionInfo("AOL Instant Messenger, version 5.2.3292/WIN32", 5, 1, 0, 3292, 238);
            request(new AuthRequest(tester.getScreenname(), tester.getPassword(), version, authkey));
        } else if (cmd instanceof AuthResponse) {
            AuthResponse ar = (AuthResponse) cmd;
            int error = ar.getErrorCode();
            if (error != -1) {
                System.out.println("connection error! code: " + error);
                if (ar.getErrorUrl() != null) {
                    System.out.println("Error URL: " + ar.getErrorUrl());
                }
            } else {
                loggedin = true;
                tester.setScreennameFormat(ar.getScreenname());
                tester.startBosConn(ar.getServer(), ar.getPort(), ar.getCookie());
                System.out.println("connecting to " + ar.getServer() + ":" + ar.getPort());
            }
            disconnect();
        }
    }
}
