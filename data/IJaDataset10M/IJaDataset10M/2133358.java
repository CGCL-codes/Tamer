package model;

import framework.IClientThread;
import framework.IUserInfo;
import java.io.ObjectOutputStream;
import logic.ClientThread;

/**
 *
 * @author swift
 */
public class UserInfo implements IUserInfo {

    private String func;

    private ObjectOutputStream out;

    private String nickname;

    private ClientThread clientThread;

    /**
     * Mit dem Konstruktor werden direkt die Daten in das Objekt geschrieben.
     *
     * @param name Diese Variable enthaelt den Namen des Users
     * @param out Diese Variable enthaelt die ausgehende Verbindung zum User
     */
    public UserInfo(String func, String nickname, ObjectOutputStream out, ClientThread clientThread) {
        this.func = func;
        this.nickname = nickname;
        this.out = out;
        this.clientThread = clientThread;
    }

    /**
     * Mit dieser Methode erhaelt man die ausgehende Verbindung zum User
     *
     * @return ObjectOutputStream Enthaelt die ausgehende Verbindung zum User.
     */
    public ObjectOutputStream getOOS() {
        return out;
    }

    /**
     * Mit dieser Methode erhaelt man den Namen des Users
     *
     * @return String Enthaelt den Namen des Users.
     */
    public String getFunction() {
        return func;
    }

    /**
     * Mit dieser Methode kann man den Namen des Benutzers setzten
     *
     * @param name Enthaelt den Namen des Users.
     */
    public void setFunction(String func) {
        this.func = func;
    }

    /**
     * Mit dieser Methode erhaelt man den Nickname des Users
     *
     * @return String Enthaelt den Nickname des Users.
     */
    public String getNickName() {
        return nickname;
    }

    /**
     * Mit dieser Methode kann man den Nickname des Benutzers setzten
     *
     * @param name Enthaelt den Nickname des Users.
     */
    public void setNickName(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Mit dieser Methode erhaelt man den Thread des Clients
     *
     * @return ClientThread Enthaelt den Thread des Clients
     */
    public IClientThread getClientThread() {
        return clientThread;
    }
}
