package pokeglobal.client.network;

import org.apache.mina.common.IoSession;
import pokeglobal.client.GlobalGame;
import pokeglobal.client.logic.Whirlpool;

/** 
 * Sends packets to the server. Allows easy remodeling of networking.
 */
public class PacketGenerator {

    private IoSession gameSession;

    private static PacketGenerator packetGen;

    public PacketGenerator(IoSession session) {
        gameSession = session;
        packetGen = this;
    }

    public static PacketGenerator get() {
        return packetGen;
    }

    /** 
     * Tell the server the player is moving.
     * 0 = Up
     * 1 = Right
     * 2 = Down
     * 3 = Left
     */
    public void move(int i) {
        if (i >= 0 && i < 4) {
            switch(i) {
                case 0:
                    gameSession.write("U");
                    break;
                case 1:
                    gameSession.write("R");
                    break;
                case 2:
                    gameSession.write("D");
                    break;
                case 3:
                    gameSession.write("L");
                    break;
            }
        }
    }

    /** 
	 * Send a login packet
	 */
    public void login(String username, String password) {
        Whirlpool hasher = new Whirlpool();
        hasher.NESSIEinit();
        hasher.NESSIEadd(password);
        byte[] hashed = new byte[64];
        hasher.NESSIEfinalize(hashed);
        java.math.BigInteger bi = new java.math.BigInteger(hashed);
        String hashedStr = bi.toString(16);
        if (hashedStr.length() % 2 != 0) {
            hashedStr = "0" + hashedStr;
        }
        gameSession.write("A" + username.trim() + new String(new char[] { (char) 27 }) + hashedStr);
    }

    public void register(String username, String password, int starterPokemon, int characterAppearance, String email) {
        Whirlpool hasher = new Whirlpool();
        hasher.NESSIEinit();
        hasher.NESSIEadd(password);
        byte[] hashed = new byte[64];
        hasher.NESSIEfinalize(hashed);
        java.math.BigInteger bi = new java.math.BigInteger(hashed);
        String hashedStr = bi.toString(16);
        if (hashedStr.length() % 2 != 0) {
            hashedStr = "0" + hashedStr;
        }
        gameSession.write("S" + username.trim() + new String(new char[] { (char) 27 }) + hashedStr + new String(new char[] { (char) 27 }) + starterPokemon + new String(new char[] { (char) 27 }) + characterAppearance + new String(new char[] { (char) 27 }) + email);
    }

    public void write(String message) {
        gameSession.write(message);
    }

    public boolean isConnected() {
        return gameSession.isConnected();
    }

    public void logout() {
        gameSession.write("Q");
        gameSession.close();
    }
}
