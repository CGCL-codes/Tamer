package edu.indiana.cs.b534.torrent.impl.message;

import edu.indiana.cs.b534.torrent.TDictionary;
import edu.indiana.cs.b534.torrent.TString;
import edu.indiana.cs.b534.torrent.TorrentException;
import edu.indiana.cs.b534.torrent.Utils;
import edu.indiana.cs.b534.torrent.message.PeerDictionary;
import edu.indiana.cs.b534.torrent.struct.TStringImpl;

public class PeerDictionaryImpl implements PeerDictionary {

    public static final TString IP_KEY = TStringImpl.newInstance("ip,port");

    public static final TString PEER_ID_KEY = TStringImpl.newInstance("peer id");

    public static final TString PORT_KEY = TStringImpl.newInstance("port");

    private String ip = "";

    private String peerID;

    private int port;

    public PeerDictionaryImpl(String ip, String peerID, int port) throws TorrentException {
        this.ip = ip;
        this.peerID = peerID;
        this.port = port;
    }

    public PeerDictionaryImpl(byte[] compactPeerData) throws TorrentException {
        if (compactPeerData.length != 6) {
            throw new TorrentException("Compact data should be of length 6");
        }
        for (int i = 0; i < 4; i++) {
            byte b = compactPeerData[i];
            ip += ((int) b & 0xFF) + ".";
        }
        ip = ip.substring(0, ip.length() - 1);
        port = ((int) compactPeerData[4] & 0xFF) * 256 + ((int) compactPeerData[5] & 0xFF);
    }

    public PeerDictionaryImpl(TDictionary dictionary) throws TorrentException {
        ip = Utils.getStrVal(dictionary, IP_KEY, false);
        peerID = Utils.getStrVal(dictionary, PEER_ID_KEY, false);
        port = Integer.valueOf(Utils.getStrVal(dictionary, PORT_KEY, false));
    }

    public String getIP() {
        return ip;
    }

    public String getPeerId() {
        return peerID;
    }

    public int getPort() {
        return port;
    }

    public void setIP(String ip) {
        this.ip = ip;
    }

    public void setPeerId(String peerID) {
        this.peerID = peerID;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return peerID + "#" + ip + ":" + port;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PeerDictionary) {
            return ip.equals(((PeerDictionary) obj).getIP()) && port == ((PeerDictionary) obj).getPort();
        } else {
            return false;
        }
    }
}
