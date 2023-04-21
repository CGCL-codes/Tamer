package de.rauchhaupt.games.poker.holdem.lib.remoteplayer;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.client.services.complex.RemoteHoldemPokerResult;
import de.rauchhaupt.games.poker.holdem.lib.remoteplayer.server.wsstubs.ClientHoldemPokerResult;

public class WebTools {

    static Logger stdlog = Logger.getLogger(WebTools.class);

    public static String toRef(String aString) {
        String hostString = aString;
        try {
            String myIp = InetAddress.getLocalHost().getHostAddress().toString();
            hostString = aString.replaceAll(myIp, "localhost");
        } catch (UnknownHostException e) {
        }
        return "<a href=\"" + hostString + "\">" + aString + "</a>";
    }

    public static void checkResult(ClientHoldemPokerResult aResult) {
        if (aResult.getHoldemPokerException() == null) return;
        if ("".equals(aResult.getHoldemPokerException())) return;
        stdlog.error("Got exception from remote player: '" + aResult.getHoldemPokerException() + "'");
        throw new HoldemPokerException(aResult.getHoldemPokerException());
    }

    public static void checkResult(RemoteHoldemPokerResult aResult) {
        if ("".equals(aResult.getHoldemPokerException())) return;
        throw new HoldemPokerException(aResult.getHoldemPokerException());
    }

    public static RemoteHoldemPokerResult createResult(Exception aException) {
        stdlog.error("An error occured", aException);
        RemoteHoldemPokerResult returnValue = new de.rauchhaupt.games.poker.holdem.lib.remoteplayer.client.services.complex.RemoteHoldemPokerResult(aException.getMessage());
        return returnValue;
    }

    public static final String EMPTY_STRING = "";

    public static String nz(String aString) {
        return nz(aString, EMPTY_STRING);
    }

    /**
	 * This method return empty String, if a given is null.
	 * 
	 * @param aObject
	 *            to be checked
	 * @return empty String, if given value is null; otherwise original string
	 */
    public static String nzToString(Object aObject) {
        return nzToString(aObject, EMPTY_STRING);
    }

    /**
	 * This method return empty String, if a given is null.
	 * 
	 * @param aString
	 *            to be checked
	 * @return empty String, if given value is null; otherwise original string
	 */
    public static String nzToString(Object aObject, String aStringIfNull) {
        return aObject == null ? aStringIfNull : aObject.toString();
    }

    /**
	 * This method returns required String, if a given one is null. <b>You can
	 * use the Nz function to return zero, a zero-length string (" "), or
	 * another specified value when a String is Null.
	 * 
	 * @param aString
	 *            to be checked
	 * @param aStringIfNull
	 *            A String that supplies a value to be returned if the first
	 *            String argument is Null.
	 * @return empty String, if given value is null; otherwise original string
	 */
    public static String nz(String aString, String aStringIfNull) {
        return aString == null ? aStringIfNull : aString;
    }
}
