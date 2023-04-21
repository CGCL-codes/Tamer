package de.hattrickorganizer.net.rmiHOFriendly;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import de.hattrickorganizer.model.Server;
import de.hattrickorganizer.model.Spielbericht;
import de.hattrickorganizer.tools.HOLogger;

/**
 * DOCUMENT ME!
 *
 * @author thomas.werth
 */
public class HOServerImp implements Chat {

    /** TODO Missing Parameter Documentation */
    protected DataOutputStream m_clOutput;

    /** TODO Missing Parameter Documentation */
    protected HoServerWorker m_clWorker;

    /** TODO Missing Parameter Documentation */
    protected MatchFinder m_clMatch;

    /** TODO Missing Parameter Documentation */
    protected Server m_clMainServer;

    /** TODO Missing Parameter Documentation */
    protected ServerSocket m_clServer;

    /** TODO Missing Parameter Documentation */
    protected Socket m_clClient;

    /** TODO Missing Parameter Documentation */
    protected boolean m_bClientAbbruch;

    /** TODO Missing Parameter Documentation */
    protected boolean m_bClientBereit = true;

    /** TODO Missing Parameter Documentation */
    protected boolean m_bClientPause;

    /** TODO Missing Parameter Documentation */
    protected boolean m_bWriting;

    /**
     * Creates a new instance of HOServerImp
     *
     * @param server TODO Missing Constructuor Parameter Documentation
     */
    public HOServerImp(Server server) {
        m_clMainServer = server;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param bool TODO Missing Method Parameter Documentation
     */
    public final void setAbbruch(boolean bool) {
        m_bClientAbbruch = bool;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param bool TODO Missing Method Parameter Documentation
     */
    public final void setBereit(boolean bool) {
        m_bClientBereit = bool;
    }

    /**
     * Setter for property m_clClient.
     *
     * @param m_clClient New value of property m_clClient.
     */
    public final void setClient(java.net.Socket m_clClient) {
        this.m_clClient = m_clClient;
    }

    /**
     * Getter for property m_clClient.
     *
     * @return Value of property m_clClient.
     */
    public final java.net.Socket getClient() {
        return m_clClient;
    }

    /**
     * Getter for property m_bClientAbbruch.
     *
     * @return Value of property m_bClientAbbruch.
     */
    public final boolean isClientAbbruch() {
        return m_bClientAbbruch;
    }

    /**
     * Getter for property m_bClientBereit.
     *
     * @return Value of property m_bClientBereit.
     */
    public final boolean isClientBereit() {
        return m_bClientBereit;
    }

    /**
     * Getter for property m_bClientPause.
     *
     * @return Value of property m_bClientPause.
     */
    public final boolean isClientPause() {
        return m_bClientPause;
    }

    /**
     * Setter for property m_clOutput.
     *
     * @param m_clOutput New value of property m_clOutput.
     */
    public final void setOutput(java.io.DataOutputStream m_clOutput) {
        this.m_clOutput = m_clOutput;
    }

    /**
     * Getter for property m_clOutput.
     *
     * @return Value of property m_clOutput.
     */
    public final java.io.DataOutputStream getOutput() {
        return m_clOutput;
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param bool TODO Missing Method Parameter Documentation
     */
    public final void setPause(boolean bool) {
        m_bClientPause = bool;
    }

    /**
     * Setter for property m_clServer.
     *
     * @param m_clServer New value of property m_clServer.
     */
    public final void setServerSocket(java.net.ServerSocket m_clServer) {
        this.m_clServer = m_clServer;
    }

    /**
     * Getter for property m_clServer.
     *
     * @return Value of property m_clServer.
     */
    public final java.net.ServerSocket getServerSocket() {
        return m_clServer;
    }

    /**
     * Erstellt das Spiel und sendet sein CallbackObjekt
     *
     * @param clientteam TODO Missing Constructuor Parameter Documentation
     */
    public final void createFriendly(de.hattrickorganizer.model.ServerTeam clientteam) {
        if (m_clMatch != null) {
            m_clMatch.setRunning(false);
        }
        m_clMainServer.setGastTeam(clientteam);
        new Thread(m_clMainServer).start();
    }

    /**
     * erzeugt den Server und registriert ihn auf Wunsch in I-Net
     *
     * @param register Soll der Server öffentlich gemacht werden
     * @param ipAdresse global Adress des Servers
     * @param port Port
     * @param info Infos zu diesem Server
     */
    public final void createServer(boolean register, String ipAdresse, int port, String info) {
        try {
            if (register) {
                m_clMatch = new MatchFinder(ipAdresse, port, info);
                new Thread(m_clMatch).start();
            }
            m_clServer = new ServerSocket(port);
            m_clServer.setSoTimeout(0);
            m_clWorker = new HoServerWorker(this);
            new Thread(m_clWorker).start();
        } catch (Exception e) {
        }
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param trainer TODO Missing Method Parameter Documentation
     * @param msg TODO Missing Method Parameter Documentation
     * @param heim TODO Missing Method Parameter Documentation
     */
    public final void recieveMsg(String trainer, String msg, boolean heim) {
        m_clMainServer.sendChatMsg(trainer, msg, heim);
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param bool TODO Missing Method Parameter Documentation
     */
    public void sendAbbruch(boolean bool) {
    }

    /**
     * gibt eine Auswechslung an
     *
     * @param textKey TODO Missing Constructuor Parameter Documentation
     * @param teamName TODO Missing Constructuor Parameter Documentation
     * @param auswechselSpieler TODO Missing Constructuor Parameter Documentation
     * @param einwechselSpieler TODO Missing Constructuor Parameter Documentation
     * @param variante TODO Missing Constructuor Parameter Documentation
     * @param spielminute TODO Missing Constructuor Parameter Documentation
     * @param heim TODO Missing Constructuor Parameter Documentation
     */
    public final void sendAuswechslung(byte textKey, String teamName, String auswechselSpieler, String einwechselSpieler, int variante, int spielminute, boolean heim) {
        requestWriteAccess();
        try {
            m_clOutput.writeByte(Chat.NET_AUSWECHSLUNG_MSG);
            m_clOutput.writeByte(textKey);
            m_clOutput.writeUTF(teamName);
            m_clOutput.writeUTF(auswechselSpieler);
            m_clOutput.writeUTF(einwechselSpieler);
            m_clOutput.writeInt(variante);
            m_clOutput.writeInt(spielminute);
            m_clOutput.writeBoolean(heim);
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
        releaseWriteAccess();
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param bool TODO Missing Method Parameter Documentation
     */
    public void sendBereit(boolean bool) {
    }

    /**
     * TODO Missing Method Documentation
     *
     * @param trainer TODO Missing Method Parameter Documentation
     * @param msg TODO Missing Method Parameter Documentation
     * @param heim TODO Missing Method Parameter Documentation
     */
    public final void sendChatMsg(String trainer, String msg, boolean heim) {
        requestWriteAccess();
        try {
            m_clOutput.writeByte(Chat.NET_CHAT_MSG);
            m_clOutput.writeUTF(trainer);
            m_clOutput.writeUTF(msg);
            m_clOutput.writeBoolean(heim);
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
        releaseWriteAccess();
        m_bClientBereit = true;
        m_clMainServer.sendChatMsg(trainer, msg, heim);
    }

    /**
     * Fangesang
     *
     * @param key FanGesangsArt ,steht für Anfeuerung, verhöhnen
     * @param art (Spieler, Team, Trainer )
     * @param variante welche Variante
     * @param fanTeam Füllmaterial für die Variable
     * @param fanManager TODO Missing Constructuor Parameter Documentation
     * @param gegnerTeam TODO Missing Constructuor Parameter Documentation
     * @param gegnerManager TODO Missing Constructuor Parameter Documentation
     * @param heim TODO Missing Constructuor Parameter Documentation
     */
    public final void sendFangesang(int key, int art, int variante, String fanTeam, String fanManager, String gegnerTeam, String gegnerManager, boolean heim) {
        requestWriteAccess();
        try {
            m_clOutput.writeByte(Chat.NET_FANGESANG_MSG);
            m_clOutput.writeInt(key);
            m_clOutput.writeInt(art);
            m_clOutput.writeInt(variante);
            m_clOutput.writeUTF(fanTeam);
            m_clOutput.writeUTF(fanManager);
            m_clOutput.writeUTF(gegnerTeam);
            m_clOutput.writeUTF(gegnerManager);
            m_clOutput.writeBoolean(heim);
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
        releaseWriteAccess();
    }

    /**
     * Gibt einen Infotext wieder z.B: gleich geht's los, Halbzeit, ElferSchießen, Spielende...
     *
     * @param textKey == Info, Vortext usw.
     * @param variante TODO Missing Constructuor Parameter Documentation
     */
    public final void sendInfoText(byte textKey, int variante) {
        requestWriteAccess();
        try {
            m_clOutput.writeByte(Chat.NET_INFO_MSG);
            m_clOutput.writeByte(textKey);
            m_clOutput.writeInt(variante);
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
        releaseWriteAccess();
    }

    /**
     * übermittelt Inforamtionen zu einer Karte
     *
     * @param textKey TODO Missing Constructuor Parameter Documentation
     * @param spielerName TODO Missing Constructuor Parameter Documentation
     * @param spielminute TODO Missing Constructuor Parameter Documentation
     * @param variante TODO Missing Constructuor Parameter Documentation
     * @param trainerVariante TODO Missing Constructuor Parameter Documentation
     * @param heim TODO Missing Constructuor Parameter Documentation
     */
    public final void sendKarte(byte textKey, String spielerName, int spielminute, int variante, int trainerVariante, boolean heim) {
        requestWriteAccess();
        try {
            m_clOutput.writeByte(Chat.NET_KARTE_MSG);
            m_clOutput.writeByte(textKey);
            m_clOutput.writeUTF(spielerName);
            m_clOutput.writeInt(spielminute);
            m_clOutput.writeInt(variante);
            m_clOutput.writeInt(trainerVariante);
            m_clOutput.writeBoolean(heim);
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
        releaseWriteAccess();
    }

    /**
     * gibt an das ne Minute rum ist :)
     *
     * @param spielminute TODO Missing Constructuor Parameter Documentation
     */
    public final void sendMinuteRum(int spielminute) {
        requestWriteAccess();
        try {
            m_clOutput.writeByte(Chat.NET_SPIELMINUTE_MSG);
            m_clOutput.writeInt(spielminute);
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
        releaseWriteAccess();
    }

    public void sendPause(boolean bool) {
    }

    /**
     * Spielbeginn
     *
     * @param sb TODO Missing Constructuor Parameter Documentation
     */
    public final void sendSpielbeginn(Spielbericht sb) {
        requestWriteAccess();
        try {
            m_clOutput.writeByte(Chat.NET_SPIELBEGINN_MSG);
            sb.save(m_clOutput);
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
        releaseWriteAccess();
    }

    /**
     * Spielende
     *
     * @param sb TODO Missing Constructuor Parameter Documentation
     */
    public final void sendSpielende(Spielbericht sb) {
        requestWriteAccess();
        try {
            m_clOutput.writeByte(Chat.NET_SPIELENDE_MSG);
            sb.save(m_clOutput);
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
        releaseWriteAccess();
    }

    /**
     * zeigt eine Torchance an Inhalt am besten in thread auslagern! Wenn Thread fertig aus diesem
     * Heraus bool isTorchancefertig auf true setzen  sofort return dieser MEthode weil ist ja 'n
     * Thread
     *
     * @param heimTeam TODO Missing Constructuor Parameter Documentation
     * @param schuetze TODO Missing Constructuor Parameter Documentation
     * @param vorbereiter1 TODO Missing Constructuor Parameter Documentation
     * @param verteidiger1 TODO Missing Constructuor Parameter Documentation
     * @param verteidiger2 TODO Missing Constructuor Parameter Documentation
     * @param torwart TODO Missing Constructuor Parameter Documentation
     * @param torart TODO Missing Constructuor Parameter Documentation
     * @param torchance TODO Missing Constructuor Parameter Documentation
     * @param variante TODO Missing Constructuor Parameter Documentation
     * @param trainerVariante TODO Missing Constructuor Parameter Documentation
     * @param trainerVariante2 TODO Missing Constructuor Parameter Documentation
     * @param aktionen TODO Missing Constructuor Parameter Documentation
     * @param spielminute TODO Missing Constructuor Parameter Documentation
     */
    public final void sendTorchance(boolean heimTeam, String schuetze, String vorbereiter1, String verteidiger1, String verteidiger2, String torwart, byte torart, byte torchance, int variante, int trainerVariante, int trainerVariante2, int[] aktionen, int spielminute) {
        requestWriteAccess();
        try {
            m_clOutput.writeByte(Chat.NET_TORCHANCE_MSG);
            m_clOutput.writeBoolean(heimTeam);
            m_clOutput.writeUTF(schuetze);
            m_clOutput.writeUTF(vorbereiter1);
            m_clOutput.writeUTF(verteidiger1);
            m_clOutput.writeUTF(verteidiger2);
            m_clOutput.writeUTF(torwart);
            m_clOutput.writeByte(torart);
            m_clOutput.writeByte(torchance);
            m_clOutput.writeInt(variante);
            m_clOutput.writeInt(trainerVariante);
            m_clOutput.writeInt(trainerVariante2);
            if (aktionen != null) {
                m_clOutput.writeInt(aktionen.length);
                for (int i = 0; i < aktionen.length; i++) {
                    m_clOutput.writeInt(aktionen[i]);
                }
            } else {
                m_clOutput.writeInt(0);
            }
            m_clOutput.writeInt(spielminute);
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
        releaseWriteAccess();
    }

    /**
     * stellt eine Verletzung dar
     *
     * @param textKey TODO Missing Constructuor Parameter Documentation
     * @param spielerName TODO Missing Constructuor Parameter Documentation
     * @param spielminute TODO Missing Constructuor Parameter Documentation
     * @param variante TODO Missing Constructuor Parameter Documentation
     * @param trainerVariante TODO Missing Constructuor Parameter Documentation
     * @param heim TODO Missing Constructuor Parameter Documentation
     */
    public final void sendVerletzung(byte textKey, String spielerName, int spielminute, int variante, int trainerVariante, boolean heim) {
        requestWriteAccess();
        try {
            m_clOutput.writeByte(Chat.NET_VERLETZUNG_MSG);
            m_clOutput.writeByte(textKey);
            m_clOutput.writeUTF(spielerName);
            m_clOutput.writeInt(spielminute);
            m_clOutput.writeInt(variante);
            m_clOutput.writeInt(trainerVariante);
            m_clOutput.writeBoolean(heim);
        } catch (Exception e) {
            HOLogger.instance().log(getClass(), e);
        }
        releaseWriteAccess();
    }

    /**
     * TODO Missing Method Documentation
     */
    public final void shutdown() {
        try {
            if (m_clClient != null) {
                m_clWorker.setRun(false);
                m_clOutput.close();
                m_clClient.close();
            }
            m_clServer.setSoTimeout(1);
            m_clServer.close();
            m_clClient = null;
            m_clWorker = null;
            m_clServer = null;
        } catch (IOException e) {
            HOLogger.instance().log(getClass(), e);
        }
        if (m_clMatch != null) {
            if (m_clMatch.isRunning()) {
                m_clMatch.setRunning(false);
            }
            m_clMatch = null;
        }
    }

    /**
     * TODO Missing Method Documentation
     */
    protected final void finalize() {
        shutdown();
    }

    /**
     * gibt die Schreibrechte wieder frei
     */
    protected final synchronized void releaseWriteAccess() {
        m_bClientBereit = false;
        try {
            m_clOutput.flush();
        } catch (Exception e) {
        }
        m_bWriting = false;
    }

    /**
     * fordert Schreibrechte für den Output!
     */
    protected final synchronized void requestWriteAccess() {
        while (m_bWriting) {
            try {
                Thread.sleep(20);
            } catch (Exception e) {
            }
        }
        m_bWriting = true;
    }
}
