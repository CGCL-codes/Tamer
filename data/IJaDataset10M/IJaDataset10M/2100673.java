package uk.co.nimp.scard;

import java.io.IOException;
import java.util.List;
import javax.smartcardio.CardNotPresentException;
import uk.co.nimp.smartcard.AnswerToReset;
import uk.co.nimp.smartcard.Apdu;
import uk.co.nimp.smartcard.UnexpectedCardResponseException;
import static uk.co.nimp.scard.NimpPcScTerminalManager.*;

class NimpPcScTerminal extends GenericTerminal {

    final long contextId;

    protected Long cardId;

    NimpPcScTerminal(long contextId, String name) {
        super(name);
        this.contextId = contextId;
        cardId = null;
    }

    @Override
    public boolean isCardPresent() throws ScardException {
        int[] status = NimpPcScTerminalManager.SCardGetStatusChange(contextId, 0, new int[] { NimpPcScTerminalManager.SCARD_STATE_UNAWARE }, new String[] { name });
        state = NimpPcScTerminalManager.win32Status2GenericTerminalState((byte) status[0]);
        return (status[0] & NimpPcScTerminalManager.SCARD_STATE_PRESENT) != 0;
    }

    private boolean waitForCard(boolean wantPresent, long timeout) throws ScardException {
        if (timeout < 0) {
            throw new IllegalArgumentException("timeout must not be negative");
        }
        if (timeout == 0) {
            timeout = NimpPcScTerminalManager.TIMEOUT_INFINITE;
        }
        int[] status = new int[] { NimpPcScTerminalManager.SCARD_STATE_UNAWARE };
        String[] readers = new String[] { name };
        try {
            status = NimpPcScTerminalManager.SCardGetStatusChange(contextId, 0, status, readers);
            state = NimpPcScTerminalManager.win32Status2GenericTerminalState((byte) status[0]);
            boolean present = (status[0] & NimpPcScTerminalManager.SCARD_STATE_PRESENT) != 0;
            if (wantPresent == present) {
                return true;
            }
            status = NimpPcScTerminalManager.SCardGetStatusChange(contextId, timeout, status, readers);
            present = (status[0] & NimpPcScTerminalManager.SCARD_STATE_PRESENT) != 0;
            if (wantPresent != present) {
                throw new ScardException("wait mismatch");
            }
            return true;
        } catch (PcScException e) {
            if (e.code == NimpPcScTerminalManager.SCARD_E_TIMEOUT) {
                return false;
            } else {
                throw new ScardException("waitForCard() failed", e);
            }
        }
    }

    @Override
    public boolean waitForCardPresent(long timeout) throws ScardException {
        return waitForCard(true, timeout);
    }

    @Override
    public boolean waitForCardAbsent(long timeout) throws ScardException {
        return waitForCard(false, timeout);
    }

    @Override
    public void connectImpl(int protocol, int activation) throws ScardException, CardNotPresentException {
        int sharingMode = SCARD_SHARE_SHARED;
        int connectProtocol;
        int activationPolicy;
        switch(protocol) {
            case PROTOCOL_T_0:
                connectProtocol = SCARD_PROTOCOL_T0;
                break;
            case PROTOCOL_T_1:
                connectProtocol = SCARD_PROTOCOL_T1;
                break;
            case PROTOCOL_DIRECT:
                connectProtocol = 0;
                sharingMode = SCARD_SHARE_DIRECT;
                break;
            case PROTOCOL_ANY:
                connectProtocol = SCARD_PROTOCOL_T0 | SCARD_PROTOCOL_T1;
                break;
            case PROTOCOL_T_15:
            case PROTOCOL_T_CL_A:
            case PROTOCOL_T_CL_B:
            case PROTOCOL_ANY_STD_CL:
            default:
                throw new IllegalArgumentException("protocol = " + GenericTerminal.getProtocolName(protocol));
        }
        switch(activation) {
            case ACTIVATION_FORCE_COLD:
                activationPolicy = SCARD_UNPOWER_CARD;
                break;
            case ACTIVATION_FORCE_WARM:
                activationPolicy = SCARD_RESET_CARD;
                break;
            case ACTIVATION_ANY:
                activationPolicy = SCARD_RESET_CARD;
                break;
            default:
                throw new IllegalArgumentException("activation = " + activation);
        }
        atr = null;
        if (null == cardId) {
            try {
                cardId = SCardConnect(contextId, name, sharingMode, connectProtocol);
                byte[] status = new byte[2];
                byte[] atrBytes = SCardStatus(cardId, status);
                atr = new AnswerToReset(atrBytes);
                if (ACTIVATION_ANY == activation) {
                    logAtr(atr);
                }
                this.protocol = NimpPcScTerminalManager.win32Protocol2GenericTerminalProtocol(status[1]);
                state = State.CARD_PRESENT;
            } catch (PcScException e) {
                if (e.code == NimpPcScTerminalManager.SCARD_W_REMOVED_CARD) {
                    throw new CardNotPresentException("No card present", e);
                } else {
                    throw new ScardException("connect() failed", e);
                }
            }
        }
        if ((null == atr) || (ACTIVATION_ANY != activation)) {
            try {
                SCardReconnect(cardId, sharingMode, connectProtocol, activationPolicy);
                byte[] status = new byte[2];
                byte[] atrBytes = SCardStatus(cardId, status);
                atr = new AnswerToReset(atrBytes);
                logAtr(atr);
                this.protocol = NimpPcScTerminalManager.win32Protocol2GenericTerminalProtocol(status[1]);
                state = State.CARD_PRESENT;
            } catch (PcScException e) {
                if (e.code == NimpPcScTerminalManager.SCARD_W_REMOVED_CARD) {
                    throw new CardNotPresentException("No card present", e);
                } else {
                    throw new ScardException("connect() failed", e);
                }
            }
        }
    }

    @Override
    protected void disconnectImpl() throws ScardException {
        if (GenericTerminal.State.CARD_PRESENT != state) {
            return;
        }
        if (null != cardId) {
            long id = cardId;
            cardId = null;
            SCardDisconnect(id, NimpPcScTerminalManager.SCARD_UNPOWER_CARD);
        }
    }

    @Override
    protected void forceDisconnectImpl() throws ScardException {
        disconnectImpl();
    }

    private byte[] concat(byte[] b1, byte[] b2, int n2) {
        int n1 = b1.length;
        if ((n1 == 0) && (n2 == b2.length)) {
            return b2;
        }
        byte[] res = new byte[n1 + n2];
        System.arraycopy(b1, 0, res, 0, n1);
        System.arraycopy(b2, 0, res, n1, n2);
        return res;
    }

    private static final byte[] B0 = new byte[0];

    private static final boolean t1StripLe = false;

    private byte[] doTransmit(byte[] command, boolean t0GetResponse, boolean t1GetResponse) throws ScardException {
        if (null == cardId) {
            throw new ScardException("Connection with card is not established.");
        }
        int n = command.length;
        boolean t0 = protocol == SCARD_PROTOCOL_T0;
        boolean t1 = protocol == SCARD_PROTOCOL_T1;
        if (t0 && (n >= 7) && (command[4] == 0)) {
            throw new ScardException("Extended length forms not supported for T=0");
        }
        if ((t0 || (t1 && t1StripLe)) && (n >= 7)) {
            int lc = command[4] & 0xff;
            if (lc != 0) {
                if (n == lc + 6) {
                    n--;
                }
            } else {
                lc = ((command[5] & 0xff) << 8) | (command[6] & 0xff);
                if (n == lc + 9) {
                    n -= 2;
                }
            }
        }
        boolean getresponse = (t0 && t0GetResponse) || (t1 && t1GetResponse);
        int k = 0;
        byte[] result = B0;
        while (true) {
            if (++k >= 32) {
                throw new ScardException("Could not obtain response");
            }
            byte[] response = SCardTransmit(cardId, protocol, command, 0, n);
            int rn = response.length;
            if (getresponse && (rn >= 2)) {
                if ((rn == 2) && (response[0] == 0x6c)) {
                    command[n - 1] = response[1];
                    continue;
                }
                if (response[rn - 2] == 0x61) {
                    if (rn > 2) {
                        result = concat(result, response, rn - 2);
                    }
                    command[1] = (byte) 0xC0;
                    command[2] = 0;
                    command[3] = 0;
                    command[4] = response[rn - 1];
                    n = 5;
                    continue;
                }
            }
            result = concat(result, response, rn);
            break;
        }
        return result;
    }

    @Override
    public void sendApduImpl(Apdu apdu) throws ScardException, UnexpectedCardResponseException {
        byte[] commandBytes = apdu.getCommandAPDU().getBytes();
        byte[] responseBytes;
        if (0 == apdu.getExpectedLe()) {
            responseBytes = doTransmit(commandBytes, false, false);
        } else {
            responseBytes = doTransmit(commandBytes, autoGetResponse, autoGetResponse);
        }
        apdu.setResponse(responseBytes);
    }

    public static void main(String[] args) throws ScardException, IOException, CardNotPresentException, Exception {
        NimpPcScTerminalManager manager = new NimpPcScTerminalManager();
        manager.loadConfiguration(Main.getDefaultConfFolder(), "PcScTest");
        List<GenericTerminal> terminals = manager.list();
        if (0 == terminals.size()) {
            System.out.println("PcSc terminal not detected.");
            return;
        }
        GenericTerminal terminal = terminals.get(0);
        Apdu test = new Apdu("001C0000");
        terminal.coldConnect();
        System.out.println("connected to " + terminal + " using protocol " + GenericTerminal.getProtocolName(terminal.getProtocol()));
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            terminal.sendApdu(test);
        }
        long end = System.currentTimeMillis();
        long delta = end - start;
        System.out.println("delta= " + delta + " ms");
    }
}
