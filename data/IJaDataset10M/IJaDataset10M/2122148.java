package uk.co.nimp.scard;

import com.atolsystems.atolutilities.AArrayUtilities;
import com.atolsystems.atolutilities.ACommandLineUtilities;
import com.atolsystems.atolutilities.ACommandLineUtilities.Arg;
import com.atolsystems.atolutilities.AFileUtilities;
import com.atolsystems.atolutilities.AStringUtilities;
import com.atolsystems.atolutilities.ArgSpec;
import com.atolsystems.atolutilities.MutableInteger;
import java.io.IOException;
import java.util.List;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.*;
import com.sun.jna.win32.StdCallFunctionMapper;
import java.io.File;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import uk.co.nimp.smartcard.AnswerToReset;
import static uk.co.nimp.scard.MP300Exception.*;
import uk.co.nimp.smartcard.Apdu;

public class VirtualTerminalManager extends GenericTerminalManager {

    static transient String virtualReaders[];

    static final String ARG_HELP = "-help";

    static final int ARG_HELP_ID = 0;

    static final String ARG_ADD_VIRTUAL_TERMINAL = "-addVirtualTerminal:";

    static final int ARG_ADD_VIRTUAL_TERMINAL_ID = 1;

    static final String CONF_FILE_NAME = "VirtualTerminalManager.conf";

    static {
        virtualReaders = new String[0];
    }

    public VirtualTerminalManager() throws IOException {
    }

    protected String getConfFileName() {
        return CONF_FILE_NAME;
    }

    @Override
    protected void loadConfigurationImpl(File argFile) throws IOException {
        try {
            if (argFile.exists()) {
                ArrayList<String> argList = ACommandLineUtilities.processArgFile(argFile);
                if ((null == argList) || argList.isEmpty()) return;
                String args[] = new String[argList.size()];
                argList.toArray(args);
                Integer curArgIndex = null;
                Arg curArg = null;
                curArgIndex = 0;
                ArrayList<String> virtualTerminalList = new ArrayList<String>();
                ArgSpec argSpecs[] = { new ArgSpec(ARG_HELP, ARG_HELP_ID), new ArgSpec(ARG_ADD_VIRTUAL_TERMINAL, ARG_ADD_VIRTUAL_TERMINAL_ID, ArgSpec.UNLIMITED_OCCURENCE) };
                Set<ArgSpec> specs = ACommandLineUtilities.addArgFileArgSpecs(argSpecs);
                argSpecs = new ArgSpec[specs.size()];
                specs.toArray(argSpecs);
                ACommandLineUtilities.checkArgs(args, argSpecs);
                while (curArgIndex < args.length) {
                    curArg = ACommandLineUtilities.getArg(args, argSpecs, curArgIndex, null);
                    if (null != curArg) {
                        switch(curArg.id) {
                            case ARG_HELP_ID:
                                StringBuilder sb = new StringBuilder();
                                sb.append("VirtualTerminalManager configuration file help\n\n");
                                sb.append(ARG_ADD_VIRTUAL_TERMINAL);
                                sb.append("<name of the reader>\n");
                                sb.append("Add a virtual reader.\n");
                                System.out.println(sb.toString());
                                break;
                            case ARG_ADD_VIRTUAL_TERMINAL_ID:
                                virtualTerminalList.add(curArg.value);
                                break;
                        }
                    }
                    curArgIndex++;
                }
                if (virtualTerminalList.size() > 0) {
                    virtualReaders = new String[virtualTerminalList.size()];
                    virtualTerminalList.toArray(virtualReaders);
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException("Exception occured during processing of configuration file below\n" + argFile.getAbsolutePath() + "\n", e);
        }
    }

    public static void initContext() throws ScardException {
        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                {
                    int status = 0;
                    if (RET_OK != status) {
                    }
                }
            }
        });
    }

    @Override
    public List<GenericTerminal> list() throws ScardException {
        List<GenericTerminal> terminalsList = new ArrayList<GenericTerminal>();
        for (int i = 0; i < virtualReaders.length; i++) {
            try {
                GenericTerminal terminal = getTerminalImpl(virtualReaders[i]);
                terminalsList.add(terminal);
            } catch (MP300Exception e) {
            }
        }
        return Collections.unmodifiableList(terminalsList);
    }

    private static final Map<String, Reference<GenericTerminal>> terminals = new HashMap<String, Reference<GenericTerminal>>();

    protected static GenericTerminal getTerminalImpl(String name) throws ScardException {
        Reference<GenericTerminal> ref = terminals.get(name);
        GenericTerminal terminal = (ref != null) ? ref.get() : null;
        if (terminal != null) {
            return terminal;
        }
        terminal = new VirtualTerminal(name);
        terminals.put(name, new WeakReference<GenericTerminal>(terminal));
        return terminal;
    }

    static void SCardSetVdd(String name, int vddMillivolts) throws ScardException {
    }

    static void SCardSetClk(String name, int clkHertz) throws ScardException {
    }

    static AnswerToReset SCardConnect(String name, int vddMillivolts, int clkHertz, int protocol, MutableInteger cyclesPerEtu) throws ScardException {
        switch(protocol) {
            case GenericTerminal.PROTOCOL_T_0:
            case GenericTerminal.PROTOCOL_T_1:
            case GenericTerminal.PROTOCOL_ANY:
            case GenericTerminal.PROTOCOL_DIRECT:
            case GenericTerminal.PROTOCOL_T_15:
            case GenericTerminal.PROTOCOL_T_CL_A:
            case GenericTerminal.PROTOCOL_T_CL_B:
            case GenericTerminal.PROTOCOL_ANY_STD_CL:
                break;
            default:
                throw new IllegalArgumentException("protocol = " + GenericTerminal.getProtocolName(protocol));
        }
        byte atrBuf[] = new byte[2];
        atrBuf[0] = 0x3F;
        atrBuf[1] = 0;
        AnswerToReset out = new AnswerToReset(atrBuf);
        cyclesPerEtu.value = 372;
        return out;
    }

    static void SCardTransmit(String name, Apdu apdu) throws ScardException {
        if (false == apdu.isResponseSet()) {
            if (apdu.isExpectedResponseSet()) apdu.setResponse(apdu.getExpectedResponseAPDU()); else {
                Apdu.CardResponse r = new Apdu.CardResponse(new byte[apdu.getExpectedLe()], (short) 0x9000);
                apdu.setResponse(r);
            }
        }
    }

    static void SCardDisconnect(String name, int disposition) throws ScardException {
    }

    static GenericTerminal.State SCardIsPresent(String name) throws ScardException {
        return GenericTerminal.State.CARD_PRESENT;
    }
}
