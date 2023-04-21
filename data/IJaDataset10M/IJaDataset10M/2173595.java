package ds.nfcip.se;

import java.util.List;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;
import ds.nfcip.NFCIPAbstract;
import ds.nfcip.NFCIPException;
import ds.nfcip.NFCIPInterface;
import ds.nfcip.NFCIPUtils;

/**
 * Java SE implementation of NFCIPConnection for the ACS ACR122 NFC reader
 * 
 * @author F. Kooman <F.Kooman@student.science.ru.nl>
 * 
 */
public class NFCIPConnection extends NFCIPAbstract implements NFCIPInterface {

    private final byte IN_DATA_EXCHANGE = (byte) 0x40;

    private final byte IN_RELEASE = (byte) 0x52;

    private final byte IN_JUMP_FOR_DEP = (byte) 0x56;

    private final byte TG_GET_DATA = (byte) 0x86;

    private final byte TG_INIT_AS_TARGET = (byte) 0x8c;

    private final byte TG_SET_DATA = (byte) 0x8e;

    private final byte[] GET_FIRMWARE_VERSION = { (byte) 0xff, (byte) 0x00, (byte) 0x48, (byte) 0x00, (byte) 0x00 };

    /**
	 * temporary buffer for storing data from sendCommand when in initiator mode
	 */
    private byte[] tmpSendStorage;

    private CardTerminal terminal = null;

    private CardChannel ch = null;

    public NFCIPConnection() {
        super();
        blockSize = 240;
    }

    private void connectToTerminal() throws NFCIPException {
        if (terminal == null) throw new NFCIPException("need to set terminal device first");
        Card card;
        try {
            if (terminal.isCardPresent()) {
                card = terminal.connect("*");
                ch = card.getBasicChannel();
            } else {
                throw new NFCIPException("unsupported device");
            }
        } catch (CardException e) {
            throw new NFCIPException("problem with connecting to reader");
        }
        logMessage(2, "successful connection");
        logMessage(2, "ACS ACR122 firmware version: " + getFirmwareVersion());
    }

    protected void rawClose() throws NFCIPException {
    }

    protected void releaseTargets() throws NFCIPException {
        if (getMode() == INITIATOR || getMode() == FAKE_TARGET) {
            transmit(IN_RELEASE, new byte[] { 0x00 });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected void setInitiatorMode() throws NFCIPException {
        byte[] initiatorPayload = { 0x00, 0x02, 0x01, 0x00, (byte) 0xff, (byte) 0xff, 0x00, 0x00 };
        transmit(IN_JUMP_FOR_DEP, initiatorPayload);
    }

    protected void setTargetMode() throws NFCIPException {
        byte[] targetPayload = { (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x40, (byte) 0x01, (byte) 0xFE, (byte) 0xA2, (byte) 0xA3, (byte) 0xA4, (byte) 0xA5, (byte) 0xA6, (byte) 0xA7, (byte) 0xC0, (byte) 0xC1, (byte) 0xC2, (byte) 0xC3, (byte) 0xC4, (byte) 0xC5, (byte) 0xC6, (byte) 0xC7, (byte) 0xFF, (byte) 0xFF, (byte) 0xAA, (byte) 0x99, (byte) 0x88, (byte) 0x77, (byte) 0x66, (byte) 0x55, (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11, (byte) 0x00, (byte) 0x00 };
        transmit(TG_INIT_AS_TARGET, targetPayload);
    }

    /**
	 * Set the terminal to use
	 * 
	 * @param terminalNumber
	 *            the terminal to use, specify this as a number, the first
	 *            terminal has number 0
	 */
    public void setTerminal(int terminalNumber) throws NFCIPException {
        List<CardTerminal> terminals;
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            terminals = factory.terminals().list();
            if (terminals.size() == 0) terminals = null;
        } catch (CardException c) {
            terminals = null;
        }
        if (terminals != null && terminalNumber >= 0 && terminalNumber < terminals.size()) terminal = terminals.get(terminalNumber);
        connectToTerminal();
    }

    /**
	 * Sends and receives APDUs to and from the PN53x, handles APDU and NFCIP
	 * data transfer error handling.
	 * 
	 * @param instr
	 *            The PN53x instruction
	 * @param payload
	 *            The payload to send
	 * 
	 * @return The response payload (without instruction bytes and status bytes)
	 */
    private byte[] transmit(byte instr, byte[] payload) throws NFCIPException {
        if (ch == null) throw new NFCIPException("channel not open");
        logMessage(3, instructionToString(instr));
        int payloadLength = (payload != null) ? payload.length : 0;
        byte[] instruction = { (byte) 0xd4, instr };
        byte[] header = { (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) (instruction.length + payloadLength) };
        byte[] cmd = NFCIPUtils.appendToByteArray(header, instruction, 0, instruction.length);
        if (instr == IN_DATA_EXCHANGE) {
            cmd = NFCIPUtils.appendToByteArray(cmd, new byte[] { 0x01 }, 0, 1);
            cmd[4]++;
        }
        cmd = NFCIPUtils.appendToByteArray(cmd, payload);
        try {
            logMessage(4, "Sent     (" + cmd.length + " bytes): " + NFCIPUtils.byteArrayToString(cmd));
            CommandAPDU c = new CommandAPDU(cmd);
            ResponseAPDU r = ch.transmit(c);
            byte[] ra = r.getBytes();
            logMessage(4, "Received (" + ra.length + " bytes): " + NFCIPUtils.byteArrayToString(ra));
            if (r.getSW1() == 0x63 && r.getSW2() == 0x27) {
                throw new CardException("wrong checksum from contactless response (0x63 0x27");
            } else if (r.getSW1() == 0x63 && r.getSW2() == 0x7f) {
                throw new CardException("wrong PN53x command (0x63 0x7f)");
            } else if (r.getSW1() != 0x90 && r.getSW2() != 0x00) {
                throw new CardException("unknown error (" + NFCIPUtils.byteToString(r.getSW1()) + " " + NFCIPUtils.byteToString(r.getSW2()));
            }
            if ((instr == TG_SET_DATA || instr == TG_GET_DATA || instr == IN_DATA_EXCHANGE) && ra[2] != (byte) 0x00) {
                throw new NFCIPException("communication error (" + NFCIPUtils.byteToString(ra[2]) + ")");
            }
            ra = NFCIPUtils.subByteArray(ra, 2, ra.length - 4);
            if (instr == TG_GET_DATA || instr == IN_DATA_EXCHANGE) {
                ra = NFCIPUtils.subByteArray(ra, 1, ra.length - 1);
            }
            return ra;
        } catch (CardException e) {
            throw new NFCIPException("problem with transmitting data (" + e.getMessage() + ")");
        }
    }

    /**
	 * Convert an instruction byte to a human readable text
	 * 
	 * @param instr
	 *            the instruction byte
	 * @return the human readable text
	 */
    private String instructionToString(byte instr) {
        switch(instr) {
            case IN_DATA_EXCHANGE:
                return "IN_DATA_EXCHANGE";
            case IN_RELEASE:
                return "IN_RELEASE";
            case IN_JUMP_FOR_DEP:
                return "IN_JUMP_FOR_DEP";
            case TG_GET_DATA:
                return "TG_GET_DATA";
            case TG_INIT_AS_TARGET:
                return "TG_INIT_AS_TARGET";
            case TG_SET_DATA:
                return "TG_SET_DATA";
            default:
                return "UNKNOWN INSTRUCTION (" + NFCIPUtils.byteToString(instr) + ")";
        }
    }

    protected void sendCommand(byte[] data) throws NFCIPException {
        if (getMode() == INITIATOR || getMode() == FAKE_TARGET) {
            tmpSendStorage = transmit(IN_DATA_EXCHANGE, data);
        } else {
            transmit(TG_SET_DATA, data);
        }
    }

    protected byte[] receiveCommand() throws NFCIPException {
        if (getMode() == INITIATOR || getMode() == FAKE_TARGET) {
            return tmpSendStorage;
        } else {
            return transmit(TG_GET_DATA, null);
        }
    }

    private String getFirmwareVersion() throws NFCIPException {
        try {
            CommandAPDU c = new CommandAPDU(GET_FIRMWARE_VERSION);
            if (ch == null) {
                throw new NFCIPException("channel not open");
            }
            return new String(ch.transmit(c).getBytes());
        } catch (CardException e) {
            throw new NFCIPException("problem requesting firmware version");
        }
    }
}
