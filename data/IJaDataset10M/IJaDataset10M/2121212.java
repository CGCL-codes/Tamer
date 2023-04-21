package uk.co.nimp.scard;

import com.atolsystems.atolutilities.AStringUtilities;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.ptr.*;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.WString;
import com.sun.jna.win32.StdCallFunctionMapper;
import com.sun.jna.win32.StdCallLibrary;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NimpPcScTerminalManager extends GenericTerminalManager {

    public static interface NimpPcscDll extends StdCallLibrary {

        NativeLong NimpSCardEstablishContext(int dwScope, Pointer pvReserved1, Pointer pvReserved2, NativeLongByReference phContext);

        NativeLong NimpSCardReleaseContext(NativeLong hContext);

        NativeLong NimpSCardListReadersW(NativeLong hContext, String mszGroups, short[] mszReaders, ShortByReference pcchReaders);

        public static class SCARD_READERSTATE extends Structure {

            public WString szReader;

            public Pointer pvUserData;

            public int dwCurrentState;

            public int dwEventState;

            public int cbAtr;

            public byte rgbAtr[];
        }

        NativeLong NimpSCardGetStatusChangeW(NativeLong hContext, int dwTimeout, SCARD_READERSTATE rgReaderStates[], int cReaders);

        NativeLong NimpSCardConnectW(NativeLong hContext, WString szReader, int dwShareMode, int dwPreferredProtocols, NativeLongByReference phCard, IntByReference pdwActiveProtocol);

        NativeLong NimpSCardReconnect(NativeLong hCard, int dwShareMode, int dwPreferredProtocols, int dwInitialization, IntByReference pdwActiveProtocol);

        NativeLong NimpSCardDisconnect(NativeLong hCard, int dwDisposition);

        NativeLong NimpSCardStatusW(NativeLong hCard, WString szReaderName, IntByReference pcchReaderLen, IntByReference pdwState, IntByReference pdwProtocol, byte pbAtr[], IntByReference pcbAtrLen);

        public static class SCARD_IO_REQUEST extends Structure {

            public int dwProtocol;

            public int cbPciLength;
        }

        NativeLong NimpSCardTransmit(NativeLong hCard, SCARD_IO_REQUEST pioSendPci, byte[] pbSendBuffer, int cbSendLength, SCARD_IO_REQUEST pioRecvPci, byte[] pbRecvBuffer, IntByReference pcbRecvLength);
    }

    static final Throwable initException;

    static final NimpPcscDll nimpPcscDll;

    static final String CONF_FILE_NAME = "NimpPcScTerminalManager.conf";

    private static long contextId = 0;

    static {
        Throwable ex;
        NimpPcscDll trial;
        try {
            Map<String, Object> options = new HashMap<String, Object>();
            options.put(Library.OPTION_FUNCTION_MAPPER, new StdCallFunctionMapper());
            trial = (NimpPcscDll) Native.loadLibrary("C:\\Documents and Settings\\seb\\My Documents\\Visual Studio 2010\\Projects\\NimpPcsc\\Debug\\NimpPcsc.dll", NimpPcscDll.class, options);
            ex = null;
        } catch (Throwable e) {
            ex = e;
            trial = null;
        }
        if (null == trial) System.out.println("Dll not loaded");
        nimpPcscDll = trial;
        initException = ex;
        if (null == initException) {
            try {
                NimpPcScTerminalManager.initContext();
            } catch (ScardException ex1) {
                Logger.getLogger(NimpPcScTerminalManager.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
    }

    public NimpPcScTerminalManager() {
    }

    protected String getConfFileName() {
        return CONF_FILE_NAME;
    }

    @Override
    protected void loadConfigurationImpl(File argFile) throws IOException {
    }

    public static void initContext() throws ScardException {
        if (contextId == 0) {
            NativeLongByReference phContext = new NativeLongByReference();
            phContext.setValue(new NativeLong((long) 0));
            NativeLong status = nimpPcscDll.NimpSCardEstablishContext(SCARD_SCOPE_USER, Pointer.NULL, Pointer.NULL, phContext);
            Runtime.getRuntime().addShutdownHook(new Thread() {

                @Override
                public void run() {
                    if (0 != contextId) nimpPcscDll.NimpSCardReleaseContext(new NativeLong(contextId));
                }
            });
            if (0 != status.longValue()) {
                throw new PcScException(status.intValue());
            }
            contextId = phContext.getValue().longValue();
        }
    }

    /**
     * 
     * @param win32Status
     * @return the equivalent GenericTerminal.State
     */
    protected static GenericTerminal.State win32Status2GenericTerminalState(byte win32Status) {
        if (SCARD_ABSENT == win32Status) return GenericTerminal.State.CARD_ABSENT;
        return GenericTerminal.State.CARD_PRESENT;
    }

    /**
     *
     * @param win32Protocol
     * @return the equivalent GenericTerminal protocol code
     */
    protected static int win32Protocol2GenericTerminalProtocol(byte win32Protocol) {
        switch(win32Protocol) {
            case SCARD_PROTOCOL_T0:
                return GenericTerminal.PROTOCOL_T_0;
            case SCARD_PROTOCOL_T1:
                return GenericTerminal.PROTOCOL_T_1;
            default:
                return GenericTerminal.PROTOCOL_UNDEFINED;
        }
    }

    @Override
    public List<GenericTerminal> list() throws ScardException {
        ShortByReference requiredSize = new ShortByReference();
        NativeLong status = nimpPcscDll.NimpSCardListReadersW(new NativeLong(contextId), null, null, requiredSize);
        List<GenericTerminal> terminalsList;
        if (SCARD_E_NO_READERS_AVAILABLE == status.longValue()) {
            terminalsList = new ArrayList<GenericTerminal>(0);
            return Collections.unmodifiableList(terminalsList);
        }
        if (SCARD_S_SUCCESS != status.longValue()) {
            throw new PcScException(status.intValue());
        }
        short nativeOut[] = new short[requiredSize.getValue()];
        status = nimpPcscDll.NimpSCardListReadersW(new NativeLong(contextId), null, nativeOut, requiredSize);
        if (SCARD_S_SUCCESS != status.longValue()) {
            throw new PcScException(status.intValue());
        }
        String[] readerNames = AStringUtilities.shortsToStrings(nativeOut);
        terminalsList = new ArrayList<GenericTerminal>(readerNames.length);
        for (int i = 0; i < readerNames.length; i++) {
            String name = readerNames[i];
            GenericTerminal terminal = getTerminalImpl(name);
            terminalsList.add(terminal);
            int[] terminalStatus = SCardGetStatusChange(contextId, 0, new int[] { SCARD_STATE_UNAWARE }, new String[] { name });
            GenericTerminal.State newState = win32Status2GenericTerminalState((byte) terminalStatus[0]);
            switch(newState) {
                case CARD_ABSENT:
                    if ((GenericTerminal.State.CARD_PRESENT == terminal.state) || (GenericTerminal.State.CARD_INSERTION == terminal.state)) terminal.state = GenericTerminal.State.CARD_REMOVAL; else terminal.state = GenericTerminal.State.CARD_ABSENT;
                    break;
                case CARD_PRESENT:
                    if ((GenericTerminal.State.CARD_ABSENT == terminal.state) || (GenericTerminal.State.CARD_REMOVAL == terminal.state)) terminal.state = GenericTerminal.State.CARD_INSERTION; else terminal.state = GenericTerminal.State.CARD_PRESENT;
                    break;
            }
        }
        return Collections.unmodifiableList(terminalsList);
    }

    private static final Map<String, Reference<GenericTerminal>> terminals = new HashMap<String, Reference<GenericTerminal>>();

    protected static GenericTerminal getTerminalImpl(String name) {
        synchronized (terminals) {
            Reference<GenericTerminal> ref = terminals.get(name);
            GenericTerminal terminal = (ref != null) ? ref.get() : null;
            if (terminal != null) {
                return terminal;
            }
            terminal = new PcScTerminal(contextId, name);
            terminals.put(name, new WeakReference<GenericTerminal>(terminal));
            return terminal;
        }
    }

    static long SCardConnect(long contextId, String readerName, int shareMode, int preferredProtocols) throws PcScException {
        NativeLongByReference phCard = new NativeLongByReference();
        IntByReference pdwActiveProtocol = new IntByReference();
        NativeLong status = nimpPcscDll.NimpSCardConnectW(new NativeLong(contextId), new WString(readerName), shareMode, preferredProtocols, phCard, pdwActiveProtocol);
        if (SCARD_S_SUCCESS != status.longValue()) {
            throw new PcScException(status.intValue());
        }
        return phCard.getValue().longValue();
    }

    static long SCardReconnect(long cardId, int shareMode, int preferredProtocols, int activationPolicy) throws PcScException {
        NativeLongByReference phCard = new NativeLongByReference();
        IntByReference pdwActiveProtocol = new IntByReference();
        NativeLong status = nimpPcscDll.NimpSCardReconnect(new NativeLong(cardId), shareMode, preferredProtocols, activationPolicy, pdwActiveProtocol);
        if (SCARD_S_SUCCESS != status.longValue()) {
            throw new PcScException(status.intValue());
        }
        return phCard.getValue().longValue();
    }

    static byte[] SCardTransmit(long cardId, int protocol, byte[] buf, int ofs, int len) throws PcScException {
        NimpPcscDll.SCARD_IO_REQUEST pioSendPci = new NimpPcscDll.SCARD_IO_REQUEST();
        pioSendPci.dwProtocol = protocol;
        pioSendPci.cbPciLength = 8;
        IntByReference pcbRecvLength = new IntByReference();
        byte pbRecvBuffer[] = new byte[65536 + 16];
        pcbRecvLength.setValue(65536 + 16);
        if (0 != ofs) {
            byte temp[] = new byte[len];
            for (int i = 0; i < len; i++) {
                temp[i] = buf[ofs + i];
            }
            buf = temp;
        }
        NativeLong callStatus = nimpPcscDll.NimpSCardTransmit(new NativeLong(cardId), pioSendPci, buf, len, (NimpPcscDll.SCARD_IO_REQUEST) null, pbRecvBuffer, pcbRecvLength);
        if (SCARD_S_SUCCESS != callStatus.longValue()) {
            throw new PcScException("buf.length=" + buf.length + ", len=" + len, callStatus.intValue());
        }
        byte out[] = new byte[pcbRecvLength.getValue()];
        for (int i = 0; i < pcbRecvLength.getValue(); i++) {
            out[i] = pbRecvBuffer[i];
        }
        return out;
    }

    static byte[] SCardStatus(long cardId, byte[] status) throws ScardException {
        IntByReference pcchReaderLen = new IntByReference();
        IntByReference pdwState = new IntByReference();
        IntByReference pdwProtocol = new IntByReference();
        byte pbAtr[] = new byte[36];
        IntByReference pcbAtrLen = new IntByReference();
        pcbAtrLen.setValue(36);
        NativeLong callStatus = nimpPcscDll.NimpSCardStatusW(new NativeLong(cardId), null, pcchReaderLen, pdwState, pdwProtocol, pbAtr, pcbAtrLen);
        if (SCARD_S_SUCCESS != callStatus.longValue()) {
            throw new PcScException(callStatus.intValue());
        }
        byte out[] = new byte[pcbAtrLen.getValue()];
        for (int i = 0; i < pcbAtrLen.getValue(); i++) {
            out[i] = pbAtr[i];
        }
        status[0] = (byte) pdwState.getValue();
        status[1] = (byte) pdwProtocol.getValue();
        return out;
    }

    static void SCardDisconnect(long cardId, int disposition) throws PcScException {
        NativeLong status = nimpPcscDll.NimpSCardDisconnect(new NativeLong(cardId), disposition);
        if (SCARD_S_SUCCESS != status.longValue()) {
            throw new PcScException(status.intValue());
        }
    }

    static int[] SCardGetStatusChange(long contextId, long timeout, int[] currentState, String[] readerNames) throws ScardException {
        NimpPcscDll.SCARD_READERSTATE readerStates[] = new NimpPcscDll.SCARD_READERSTATE[readerNames.length];
        for (int i = 0; i < readerNames.length; i++) {
            readerStates[i] = new NimpPcscDll.SCARD_READERSTATE();
            readerStates[i].szReader = new WString(readerNames[i]);
            readerStates[i].pvUserData = Pointer.NULL;
            readerStates[i].dwCurrentState = currentState[i];
            readerStates[i].dwEventState = SCARD_STATE_UNAWARE;
            readerStates[i].cbAtr = 0;
            readerStates[i].rgbAtr = new byte[36];
        }
        NativeLong status = nimpPcscDll.NimpSCardGetStatusChangeW(new NativeLong(contextId), (int) timeout, readerStates, readerNames.length);
        if (SCARD_S_SUCCESS != status.longValue()) {
            throw new PcScException(status.intValue());
        }
        int out[] = new int[readerNames.length];
        for (int i = 0; i < readerNames.length; i++) {
            out[i] = readerStates[i].dwEventState;
        }
        return out;
    }

    static final int SCARD_S_SUCCESS = 0x00000000;

    static final int SCARD_E_CANCELLED = 0x80100002;

    static final int SCARD_E_CANT_DISPOSE = 0x8010000E;

    static final int SCARD_E_INSUFFICIENT_BUFFER = 0x80100008;

    static final int SCARD_E_INVALID_ATR = 0x80100015;

    static final int SCARD_E_INVALID_HANDLE = 0x80100003;

    static final int SCARD_E_INVALID_PARAMETER = 0x80100004;

    static final int SCARD_E_INVALID_TARGET = 0x80100005;

    static final int SCARD_E_INVALID_VALUE = 0x80100011;

    static final int SCARD_E_NO_MEMORY = 0x80100006;

    static final int SCARD_F_COMM_ERROR = 0x80100013;

    static final int SCARD_F_INTERNAL_ERROR = 0x80100001;

    static final int SCARD_F_UNKNOWN_ERROR = 0x80100014;

    static final int SCARD_F_WAITED_TOO_LONG = 0x80100007;

    static final int SCARD_E_UNKNOWN_READER = 0x80100009;

    static final int SCARD_E_TIMEOUT = 0x8010000A;

    static final int SCARD_E_SHARING_VIOLATION = 0x8010000B;

    static final int SCARD_E_NO_SMARTCARD = 0x8010000C;

    static final int SCARD_E_UNKNOWN_CARD = 0x8010000D;

    static final int SCARD_E_PROTO_MISMATCH = 0x8010000F;

    static final int SCARD_E_NOT_READY = 0x80100010;

    static final int SCARD_E_SYSTEM_CANCELLED = 0x80100012;

    static final int SCARD_E_NOT_TRANSACTED = 0x80100016;

    static final int SCARD_E_READER_UNAVAILABLE = 0x80100017;

    static final int SCARD_W_UNSUPPORTED_CARD = 0x80100065;

    static final int SCARD_W_UNRESPONSIVE_CARD = 0x80100066;

    static final int SCARD_W_UNPOWERED_CARD = 0x80100067;

    static final int SCARD_W_RESET_CARD = 0x80100068;

    static final int SCARD_W_REMOVED_CARD = 0x80100069;

    static final int SCARD_W_INSERTED_CARD = 0x8010006A;

    static final int SCARD_E_UNSUPPORTED_FEATURE = 0x8010001F;

    static final int SCARD_E_PCI_TOO_SMALL = 0x80100019;

    static final int SCARD_E_READER_UNSUPPORTED = 0x8010001A;

    static final int SCARD_E_DUPLICATE_READER = 0x8010001B;

    static final int SCARD_E_CARD_UNSUPPORTED = 0x8010001C;

    static final int SCARD_E_NO_SERVICE = 0x8010001D;

    static final int SCARD_E_SERVICE_STOPPED = 0x8010001E;

    static final int SCARD_E_NO_READERS_AVAILABLE = 0x8010002E;

    static final int WINDOWS_ERROR_INVALID_HANDLE = 6;

    static final int WINDOWS_ERROR_INVALID_PARAMETER = 87;

    static final int SCARD_LEAVE_CARD = 0x0000;

    static final int SCARD_RESET_CARD = 0x0001;

    static final int SCARD_UNPOWER_CARD = 0x0002;

    static final int SCARD_EJECT_CARD = 0x0003;

    static final int SCARD_SCOPE_USER = 0x0000;

    static final int SCARD_SCOPE_TERMINAL = 0x0001;

    static final int SCARD_SCOPE_SYSTEM = 0x0002;

    static final int SCARD_SCOPE_GLOBAL = 0x0003;

    static final int SCARD_SHARE_EXCLUSIVE = 0x0001;

    static final int SCARD_SHARE_SHARED = 0x0002;

    static final int SCARD_SHARE_DIRECT = 0x0003;

    static final int SCARD_STATE_UNAWARE = 0x0000;

    static final int SCARD_STATE_IGNORE = 0x0001;

    static final int SCARD_STATE_CHANGED = 0x0002;

    static final int SCARD_STATE_UNKNOWN = 0x0004;

    static final int SCARD_STATE_UNAVAILABLE = 0x0008;

    static final int SCARD_STATE_EMPTY = 0x0010;

    static final int SCARD_STATE_PRESENT = 0x0020;

    static final int SCARD_STATE_ATRMATCH = 0x0040;

    static final int SCARD_STATE_EXCLUSIVE = 0x0080;

    static final int SCARD_STATE_INUSE = 0x0100;

    static final int SCARD_STATE_MUTE = 0x0200;

    static final int SCARD_STATE_UNPOWERED = 0x0400;

    static final int TIMEOUT_INFINITE = 0xffffffff;

    static final int SCARD_AUTOALLOCATE = -1;

    static final int SCARD_PROTOCOL_T0 = 0x0001;

    static final int SCARD_PROTOCOL_T1 = 0x0002;

    static final int SCARD_PROTOCOL_RAW = 0x10000;

    static final int SCARD_UNKNOWN = 0x0000;

    static final int SCARD_ABSENT = 0x0001;

    static final int SCARD_PRESENT = 0x0002;

    static final int SCARD_SWALLOWED = 0x0003;

    static final int SCARD_POWERED = 0x0004;

    static final int SCARD_NEGOTIABLE = 0x0005;

    static final int SCARD_SPECIFIC = 0x0006;
}
