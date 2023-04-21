package com.jaccal;

import com.jaccal.command.Command;
import com.jaccal.pcsc.JaccalPCSC;
import com.jaccal.pcsc.JaccalPCSCException;
import com.jaccal.pcsc.PCSC;
import java.io.IOException;

/**
 * Jaccal-PCSC specific implementation of the Session
 * @author Thomas Tarpin-Lyonnet
 *
 */
public class JaccalPCSCSession extends Session {

    private JaccalPCSC sessionPcsc;

    private boolean inUse = false;

    /**
     * 
     */
    public JaccalPCSCSession() {
        super();
    }

    /**
     * Initializes the session for update access.
     */
    public Atr open() throws CardException {
        Atr atr = new Atr();
        try {
            sessionPcsc.connectCard(PCSC.SCARD_SHARE_SHARED);
            byte[] atrBytes = sessionPcsc.getCardAtr();
            atr.setCardAtr(atrBytes);
        } catch (JaccalPCSCException e) {
            throw new CardException(e);
        }
        inUse = true;
        return atr;
    }

    /**
     * Closes the session
     */
    public void close() throws CardException {
        try {
            sessionPcsc.disconnectCard(PCSC.SCARD_UNPOWER_CARD);
        } catch (JaccalPCSCException e) {
            inUse = false;
            throw new CardException(e);
        }
        inUse = false;
    }

    /**
	 * Directly sends APDU commands to the card
	 * @return A card response object
	 * @param command The command object
	 */
    public CardResponse execute(Command command) throws CardException {
        CardResponse res = new CardResponse();
        if (!inUse) throw new CardException("A session needs to be opened first");
        try {
            byte[] resp = sessionPcsc.transmitApdu(command.getBytes(), 0, command.getBytes().length);
            byte[] data = new byte[resp.length - 2];
            for (int i = 0; i < data.length; i++) {
                data[i] = resp[i];
            }
            res.setData(data);
            StatusWord sw = new StatusWord(resp[resp.length - 2], resp[resp.length - 1]);
            res.setStatusWord(sw);
        } catch (JaccalPCSCException e) {
            throw new CardException(e);
        } catch (IOException e) {
            throw new CardException(e);
        }
        return res;
    }

    /**
     * @param sessionPcsc The sessionPcsc to set.
     */
    public void setSessionPcsc(JaccalPCSC sessionPcsc) {
        this.sessionPcsc = sessionPcsc;
    }

    /**
     * 
     * @return State of 
     */
    public boolean isInUse() {
        return inUse;
    }

    /**
	 * 
	 * @return The reader name attached to this session
	 */
    public String getSessionReader() {
        return sessionPcsc.getCardReader();
    }
}
