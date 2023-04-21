package uk.co.nimp.scard;

import com.atolsystems.atolutilities.ACommandLineUtilities;
import com.atolsystems.atolutilities.ACommandLineUtilities.Arg;
import com.atolsystems.atolutilities.AStringUtilities;
import com.atolsystems.atolutilities.ArgSpec;
import java.util.List;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;
import com.sun.jna.win32.StdCallFunctionMapper;
import java.io.File;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import uk.co.nimp.smartcard.AnswerToReset;
import static uk.co.nimp.scard.MP300Exception.*;
import uk.co.nimp.smartcard.Apdu;

public class MP65TerminalManager extends GenericTerminalManager {

    public static final int MP65 = 65;

    public MP65TerminalManager() {
    }

    public static interface Win32Mp300ComDll extends Library {

        short MPOS_OpenResource(int ResID, int CplNum, int BlockingMode);

        short MPOS_CloseResource(int ResID, int CplNum);

        short MPS_ResetHard(int CplNum);

        void SetDLLTimeOutValue(int timeout);

        short GetDLLTimeOutValue();

        int OpenCommunication(byte[] host);

        int CloseCommunication();

        short MPS_SendPPS2(int CplNum, int PPS, byte[] pPPSResponse);

        int SetDLLMode(int Flag);

        int AbortCoupler(int CplNum, byte[] host);

        int MPS_CouplerAbort(int CplNum);

        short MPS_BatchOpen(IntByReference pdwBatchId);

        short MPS_Add2Batch(int dwBatchId, int wRetCode, byte[] pstrRcmd);

        short MPS_AddResponse2Batch(int dwBatchId, byte[] pstrRcmd);

        short MPS_ExecuteBatch(int dwBatchId, int dwExecutionMode, IntByReference pdwFaultyLine);

        short MPS_CloseBatch(int dwBatchId);

        short SelectActiveDevice(int dwActDev);

        short StartDownloadTo(int CplNum, byte[] pFileName);

        short MPS_EndDownload(int CplNum);

        short MPS_SpyOpen(int CplNum);

        short MPS_SpyClose(int CplNum);

        short MPS_OpenLog(int CplNum, int mask_event, int Reserved);

        short MPS_CloseLog(int CplNum);

        short MPS_LogClockSelect(int CplNum, int Mode);

        short MPS_SetClkDiv(int CplNum, int value);

        short MPS_LedOn(int LedNum, int Colour);

        short MPS_LedOff(int LedNum);

        short MPS_OnCmm(int CplNum, int Frequency, ShortByReference pATRLg, byte[] pATRBuf);

        short MPS_OffCmm(int CplNum);

        short MPS_SendAPDU(int CplNum, int Command, int Lc, byte[] pLcField, int Le, byte[] pLeField, LongByReference pLeFieldLen, IntByReference SW1SW2);

        short MPS_GetProtocolParameters(int CplNum, int TypeParam, Pointer Param, int SizeofParam, LongByReference pParamSize);

        short MPS_VCCSelectMV(int CplNum, int ValVcc);

        short MPS_FrequencySelect(int CplNum, int Frequency);

        short MPS_ClockStop(int CplNum, int TgClockCount, int ThClockCount, int PinState);

        public static final int CLOCK_APPLIED = 11;

        public static final int CLOCK_STOP_HIGH = 12;

        public static final int CLOCK_STOP_LOW = 13;

        public static final int CLOCK_RESUME = 14;

        short MPS_CardDetect(int CplNum);
    }

    public static final int SIMULATOR = 0x100;

    public static final int SPY = 0x200;

    public static final int PORT_SERIAL1 = 1;

    public static final int PORT_SERIAL2 = 2;

    public static final int PORT_USB_EP1 = 101;

    public static final int PORT_USB_EP2 = 102;

    public static final int C1 = 201;

    public static final int C2 = 207;

    public static final int C3 = 67;

    public static final int CSWP = 224;

    public static final int MC1 = 228;

    public static final int TC1 = 202;

    public static final int TC2 = 205;

    public static final int TC3 = 131;

    public static final int CL1 = 203;

    public static final int CL2 = 206;

    public static final int TCL1 = 120;

    public static final int TCL2 = 122;

    public static final int MHC6 = 204;

    public static final int SWPSPY = 232;

    public static final int SC1 = (SIMULATOR | SWPSPY);

    public static final int MT1 = 210;

    public static final int SCL1 = 200;

    public static final int TPC1 = 208;

    protected static Throwable initException;

    protected static final Win32Mp300ComDll win32Mp300ComDll;

    private static final int RES_ID = MP65;

    static transient Map<String, Integer> couplers;

    static final String ARG_HELP = "-help";

    static final int ARG_HELP_ID = 0;

    static final String ARG_ADD_COM_PORT = "-addComPort:";

    static final int ARG_ADD_COM_PORT_ID = 1;

    static final String ARG_ADD_COUPLER = "-addCoupler:";

    static final int ARG_ADD_COUPLER_ID = 2;

    static final String CONF_FILE_NAME = "Mp65TerminalManager.conf";

    static final String DEFAULT_MP300_DLL = "MP300COM";

    static final String DEFAULT_COM = "USB";

    static final int DEFAULT_COUPLER = 0;

    static final int DLL_MODE = 2;

    static {
        Throwable ex;
        Win32Mp300ComDll trial;
        try {
            couplers = new HashMap<String, Integer>();
            Map<String, Object> options = new HashMap<String, Object>();
            options.put(Library.OPTION_FUNCTION_MAPPER, new StdCallFunctionMapper());
            trial = (Win32Mp300ComDll) Native.loadLibrary(DEFAULT_MP300_DLL, Win32Mp300ComDll.class, options);
            trial = (Win32Mp300ComDll) Native.synchronizedLibrary(trial);
            ex = null;
        } catch (Throwable e) {
            ex = e;
            trial = null;
        }
        win32Mp300ComDll = trial;
        initException = ex;
        if (null == ex) {
            try {
                initContext();
            } catch (ScardException ex1) {
                initException = ex1;
            }
        }
    }

    static void cleanupCoupler(int deviceId, int couplerId) {
        try {
            int status = win32Mp300ComDll.SelectActiveDevice(deviceId);
            if (RET_OK != status) {
                System.out.println("SelectActiveDevice returned " + status);
            }
            status = win32Mp300ComDll.MPOS_CloseResource(RES_ID, couplerId);
            if (RET_OK != status) {
                System.out.println("MPOS_CloseResource returned " + status);
            }
        } catch (Throwable e) {
        }
    }

    public static void initContext() throws ScardException {
        if (null != initException) {
            throw new ScardException("", initException);
        }
        if (2 == DLL_MODE) {
            int status = win32Mp300ComDll.SetDLLMode(DLL_MODE);
            if (RET_OK != status) {
                throw new MP300Exception("SetDLLMode failed", status);
            }
        }
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                {
                    for (Entry<Integer, Integer> coupler : toClean.entrySet()) {
                        cleanupCoupler(coupler.getKey(), coupler.getValue());
                    }
                }
            }
        });
    }

    protected String getConfFileName() {
        return CONF_FILE_NAME;
    }

    @Override
    protected void loadConfigurationImpl(File argFile) throws Exception {
        if (null != initException) throw new Exception(initException);
        if (null == win32Mp300ComDll) throw new NullPointerException(DEFAULT_MP300_DLL + " could not be loaded");
        try {
            if (argFile.exists()) {
                ArrayList<String> argList = ACommandLineUtilities.processArgFile(argFile);
                if ((null == argList) || argList.isEmpty()) return;
                String args[] = new String[argList.size()];
                argList.toArray(args);
                Integer curArgIndex = null;
                Arg curArg = null;
                curArgIndex = 0;
                ArgSpec argSpecs[] = { new ArgSpec(ARG_HELP, ARG_HELP_ID), new ArgSpec(ARG_ADD_COM_PORT, ARG_ADD_COM_PORT_ID, ArgSpec.UNLIMITED_OCCURENCE), new ArgSpec(ARG_ADD_COUPLER, ARG_ADD_COUPLER_ID, ArgSpec.UNLIMITED_OCCURENCE) };
                Set<ArgSpec> specs = ACommandLineUtilities.addArgFileArgSpecs(argSpecs);
                argSpecs = new ArgSpec[specs.size()];
                specs.toArray(argSpecs);
                args = ACommandLineUtilities.processArgFile(new File("").getAbsoluteFile(), args);
                ACommandLineUtilities.checkArgs(args, argSpecs);
                String curPort = DEFAULT_COM;
                while (curArgIndex < args.length) {
                    curArg = ACommandLineUtilities.getArg(args, argSpecs, curArgIndex, null);
                    if (null != curArg) {
                        switch(curArg.id) {
                            case ARG_HELP_ID:
                                StringBuilder sb = new StringBuilder();
                                sb.append("Mp65TerminalManager configuration file help\n\n");
                                sb.append(ARG_ADD_COM_PORT);
                                sb.append("<name of port>\n");
                                sb.append("Add a port. Typical name looks like \"USB\", \"USB:MSQ.08.46.31\" and so on\n");
                                System.out.println(sb.toString());
                                break;
                            case ARG_ADD_COM_PORT_ID:
                                curPort = curArg.value;
                                couplers.put(curPort, DEFAULT_COUPLER);
                                break;
                        }
                    }
                    curArgIndex++;
                }
            } else {
                couplers.put(DEFAULT_COM, DEFAULT_COUPLER);
            }
        } catch (Throwable e) {
            throw new RuntimeException("Exception occured during processing of configuration file below\n" + argFile.getAbsolutePath() + "\n", e);
        }
    }

    @Override
    public List<GenericTerminal> list() throws ScardException {
        List<GenericTerminal> terminalsList = new ArrayList<GenericTerminal>();
        if (null != initException) return Collections.unmodifiableList(terminalsList);
        for (Entry<String, Integer> coupler : couplers.entrySet()) {
            try {
                GenericTerminal terminal = getTerminalImpl(RES_ID, coupler.getKey(), coupler.getValue());
                terminalsList.add(terminal);
            } catch (MP300Exception e) {
            }
        }
        return Collections.unmodifiableList(terminalsList);
    }

    public static String resourceId2Name(int resourceId) {
        switch(resourceId) {
            case MP65:
                return "MP65";
            case TC3:
                return "MP300TC3";
            default:
                throw new RuntimeException("unknown resourceId: " + resourceId);
        }
    }

    public static void main(String[] args) throws MP300Exception, ScardException {
        String host = "USB:MSQ.08.46.31";
        int couplerId = 0;
        int deviceId = 0;
        int resourceId = RES_ID;
        byte[] readerBytes = AStringUtilities.StringToBytesWithFinalNull(host);
        int status = win32Mp300ComDll.OpenCommunication(readerBytes);
        if (RET_OK != status) throw new MP300Exception("Fail to establish communication with " + host, status);
        status = win32Mp300ComDll.MPOS_OpenResource(resourceId, couplerId, OVERRIDE);
        if (RET_OK != status) throw new MP300Exception("Fail to establish communication with " + host, status);
        status = win32Mp300ComDll.MPS_ResetHard(couplerId);
        if (RET_OK != status) throw new MP300Exception("Fail to establish communication with " + host, status);
        SCardSetVdd(deviceId, couplerId, 5000);
        status = win32Mp300ComDll.MPS_CardDetect(couplerId);
        GenericTerminal.State out;
        switch(status) {
            case RET_OK:
                out = GenericTerminal.State.CARD_PRESENT;
                break;
            case CRET_ABSENT:
                out = GenericTerminal.State.CARD_ABSENT;
                break;
            default:
                throw new MP300Exception("MPS_CardDetect failed", status);
        }
        System.out.println(out);
        win32Mp300ComDll.CloseCommunication();
    }

    private static final Map<String, Reference<GenericTerminal>> terminals = new HashMap<String, Reference<GenericTerminal>>();

    private static final Map<Integer, Integer> toClean = new HashMap<Integer, Integer>();

    private static int nextDeviceId = 0;

    private static int currentDeviceId = 0;

    protected static synchronized GenericTerminal getTerminalImpl(int resourceId, String host, int couplerId) throws ScardException {
        String name = resourceId2Name(resourceId) + " on " + host + " coupler " + couplerId;
        Reference<GenericTerminal> ref = terminals.get(name);
        GenericTerminal terminal = (ref != null) ? ref.get() : null;
        if (terminal != null) {
            try {
                terminal.isCardPresent();
                return terminal;
            } catch (Throwable e) {
                terminals.remove(name);
                MP65Terminal mp65 = (MP65Terminal) terminal;
                cleanupCoupler(mp65.deviceId, mp65.couplerId);
                System.out.println("MP65 disconnected, to use it again, please restart the application.");
                return null;
            }
        }
        int status;
        int bu = win32Mp300ComDll.GetDLLTimeOutValue();
        try {
            win32Mp300ComDll.SetDLLTimeOutValue(1);
            currentDeviceId = nextDeviceId;
            if (2 == DLL_MODE) {
                status = win32Mp300ComDll.SelectActiveDevice(currentDeviceId);
                if (RET_OK != status) throw new MP300Exception("SelectActiveDevice failed: ", status);
            }
            byte[] readerBytes = AStringUtilities.StringToBytesWithFinalNull(host);
            status = win32Mp300ComDll.OpenCommunication(readerBytes);
            if (RET_OK != status) throw new MP300Exception("Fail to establish communication with " + host, status);
            status = win32Mp300ComDll.MPOS_OpenResource(resourceId, couplerId, OVERRIDE);
            if (RET_OK != status) throw new MP300Exception("Fail to establish communication with " + host, status);
            status = win32Mp300ComDll.MPS_ResetHard(couplerId);
            if (RET_OK != status) throw new MP300Exception("Fail to establish communication with " + host, status);
            toClean.put(currentDeviceId, couplerId);
            nextDeviceId++;
            switch(resourceId) {
                case MP65:
                    terminal = new MP65Terminal(currentDeviceId, couplerId, name, readerBytes);
                    break;
                case TC3:
                    terminal = new MP300TC3Terminal(currentDeviceId, couplerId, name, readerBytes);
                    break;
            }
            terminals.put(name, new WeakReference<GenericTerminal>(terminal));
        } finally {
            win32Mp300ComDll.SetDLLTimeOutValue(bu);
        }
        return terminal;
    }

    static synchronized void setDeviceId(int deviceId) throws MP300Exception {
        if (2 == DLL_MODE) {
            if (deviceId != currentDeviceId) {
                int status = win32Mp300ComDll.SelectActiveDevice(deviceId);
                if (RET_OK != status) throw new MP300Exception("SelectActiveDevice failed: ", status);
                currentDeviceId = deviceId;
            }
        }
    }

    static synchronized void SCardSetVdd(int deviceId, int couplerId, int vddMillivolts) throws ScardException {
        int status;
        setDeviceId(deviceId);
        status = win32Mp300ComDll.MPS_VCCSelectMV(couplerId, vddMillivolts);
        if (RET_OK != status) {
            throw new MP300Exception("MPS_VCCSelectMV failed. deviceId = " + deviceId + ", couplerId=" + couplerId + ", vddMillivolts = " + vddMillivolts, status);
        }
    }

    static synchronized void SCardSetClk(int deviceId, int couplerId, int clkHertz) throws ScardException {
        int status;
        setDeviceId(deviceId);
        status = win32Mp300ComDll.MPS_FrequencySelect(couplerId, clkHertz);
        if (RET_OK != status) {
            throw new MP300Exception("MPS_FrequencySelect failed", status);
        }
    }

    static synchronized void SCardClkStop(int deviceId, int couplerId, int TgClockCount, int ThClockCount, int PinState) throws ScardException {
        int status;
        setDeviceId(deviceId);
        status = win32Mp300ComDll.MPS_ClockStop(couplerId, TgClockCount, ThClockCount, PinState);
        if (RET_OK != status) {
            throw new MP300Exception("MPS_ClockStop failed", status);
        }
    }

    static synchronized AnswerToReset SCardConnect(int deviceId, int couplerId, int clkHertz, int protocol, boolean autoPps) throws ScardException {
        setDeviceId(deviceId);
        switch(protocol) {
            case GenericTerminal.PROTOCOL_T_0:
                break;
            case GenericTerminal.PROTOCOL_T_1:
                break;
            case GenericTerminal.PROTOCOL_ANY:
                break;
            case GenericTerminal.PROTOCOL_DIRECT:
            case GenericTerminal.PROTOCOL_T_15:
            case GenericTerminal.PROTOCOL_T_CL_A:
            case GenericTerminal.PROTOCOL_T_CL_B:
            case GenericTerminal.PROTOCOL_ANY_STD_CL:
            default:
                throw new IllegalArgumentException("protocol = " + GenericTerminal.getProtocolName(protocol));
        }
        ShortByReference atrLenRef = new ShortByReference();
        atrLenRef.setValue((short) 256);
        int status;
        byte atrBuf[] = new byte[256];
        status = win32Mp300ComDll.MPS_OnCmm(couplerId, clkHertz, atrLenRef, atrBuf);
        if (RET_OK != status) {
            throw new MP300Exception("MPS_OnCmm failed", status);
        }
        short len = atrLenRef.getValue();
        AnswerToReset out = new AnswerToReset(atrBuf, 0, len);
        if (autoPps && out.acceptPps() && (out.getSupportedFiDi() != 0x11)) {
            byte ppsBuf[] = new byte[10];
            for (int i = 0; i < ppsBuf.length; i++) {
                ppsBuf[i] = 0;
            }
            int pps = 0x10000000 + (out.getSupportedFiDi() << 16);
            if (GenericTerminal.PROTOCOL_T_1 == protocol) pps |= 0x01000000;
            status = win32Mp300ComDll.MPS_SendPPS2(couplerId, pps, ppsBuf);
            if (RET_OK != status) {
                throw new MP300Exception("MPS_SendPPS2 failed", status);
            }
        }
        return out;
    }

    static synchronized Apdu.CardResponse SCardTransmit(int deviceId, int couplerId, int command, byte[] lcData, int le) throws ScardException {
        setDeviceId(deviceId);
        LongByReference actualLeByRef = new LongByReference();
        actualLeByRef.setValue(le);
        IntByReference internalSw = new IntByReference();
        internalSw.setValue(0);
        byte[] leData = new byte[le];
        int internalLe = le;
        if (0 == le) internalLe = NO_LE;
        if (256 == le) internalLe = 0;
        int lc = lcData.length;
        if (0 == lc) lc = NO_LC;
        int status = win32Mp300ComDll.MPS_SendAPDU(couplerId, command, lc, lcData, internalLe, leData, actualLeByRef, internalSw);
        if (RET_OK != status) {
            throw new MP300Exception("MPS_SendAPDU failed", status);
        }
        int actualLe = (int) actualLeByRef.getValue();
        if (actualLe != le) {
            byte[] actualLeData = new byte[actualLe];
            for (int i = 0; i < actualLe; i++) actualLeData[i] = leData[i];
            leData = actualLeData;
        }
        Apdu.CardResponse out = new Apdu.CardResponse(leData, (short) internalSw.getValue());
        return out;
    }

    static synchronized void SCardDisconnect(int deviceId, int couplerId) throws MP300Exception {
        setDeviceId(deviceId);
        int status = win32Mp300ComDll.MPS_OffCmm(couplerId);
        if (RET_OK != status) {
            throw new MP300Exception("MPS_OffCmm failed", status);
        }
    }

    static void SCardForceDisconnection(int deviceId, int couplerId) throws MP300Exception {
        setDeviceId(deviceId);
        int status = win32Mp300ComDll.AbortCoupler(couplerId, null);
        if (RET_OK != status) {
            throw new MP300Exception("AbortCoupler failed", status);
        }
    }

    static synchronized GenericTerminal.State SCardIsPresent(int deviceId, int couplerId) throws ScardException {
        setDeviceId(deviceId);
        int status = win32Mp300ComDll.MPS_CardDetect(couplerId);
        GenericTerminal.State out;
        switch(status) {
            case RET_OK:
                out = GenericTerminal.State.CARD_PRESENT;
                break;
            case CRET_ABSENT:
                out = GenericTerminal.State.CARD_ABSENT;
                break;
            default:
                throw new MP300Exception("MPS_CardDetect failed", status);
        }
        return out;
    }

    public static final int NO_LC = 0x80000000;

    public static final int LC_EXTENDED = 0x40000000;

    public static final int NO_LC_SEND = 0x20000000;

    public static final int NO_LE = 0x80000000;

    public static final int LE_EXTENDED = 0x40000000;

    public static final int NO_LE_SEND = 0x20000000;

    public static final int BATCH_EXECUTE_STOP_ON_ERROR = 0;

    public static final int BATCH_EXECUTE_IGNORE_ERRORS = 1;

    public static final int LED1 = 1;

    public static final int LED_RED = 0;

    public static final int LED_GREEN = 1;

    public static final int LED_ORANGE = 2;

    public static final int LED_OFF = 3;

    public static final int LED_MAX = 2;

    public static final int SEM_MODE_WAIT = 1;

    public static final int SEM_MODE_NO_WAIT = 0;

    public static final int BLOCKING = SEM_MODE_WAIT;

    public static final int NOT_BLOCKING = SEM_MODE_NO_WAIT;

    public static final int CONCURRENT_MODE = 2;

    public static final int OVERRIDE = 3;

    void batchInterfaceTest() throws ScardException {
        int status = 0;
        IntByReference pdwBatchId = new IntByReference();
        status = win32Mp300ComDll.MPS_BatchOpen(pdwBatchId);
        if (RET_OK != status) {
            throw new ScardException("MPS_BatchOpen returned " + status);
        }
        int batchId = pdwBatchId.getValue();
        String command = "CPSR 0 5 008A004402 05";
        status = win32Mp300ComDll.MPS_Add2Batch(batchId, 0, AStringUtilities.StringToBytes(command));
        if (RET_OK != status) {
            throw new ScardException("MPS_Add2Batch returned " + status);
        }
        String expected = "CPSR 0 5 8A00089000";
        status = win32Mp300ComDll.MPS_AddResponse2Batch(batchId, AStringUtilities.StringToBytes(expected));
        if (RET_OK != status) {
            throw new ScardException("MPS_AddResponse2Batch returned " + status);
        }
        IntByReference pdwFaultyLine = new IntByReference();
        status = win32Mp300ComDll.MPS_ExecuteBatch(batchId, BATCH_EXECUTE_STOP_ON_ERROR, pdwFaultyLine);
        if (RET_OK != status) {
            throw new ScardException("MPS_ExecuteBatch returned " + status + "\n" + "pdwFaultyLine=" + pdwFaultyLine.getValue());
        }
        status = win32Mp300ComDll.MPS_CloseBatch(batchId);
        if (RET_OK != status) {
            throw new ScardException("MPS_CloseBatch returned " + status);
        }
    }
}
