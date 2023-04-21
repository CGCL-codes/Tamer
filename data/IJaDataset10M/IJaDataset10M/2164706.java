package ti.sutc.ttif;

import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ti.exceptions.ProgrammingErrorException;
import ti.io.UDataInputStream;
import ti.io.UDataOutputStream;
import ti.mcore.Environment;
import ti.sutc.feature.Control;
import ti.sutc.feature.IDropHandler;
import ti.sutc.feature.VersionInfo;

/**
 * TTIF module for handling (simulated) interrupt related commands.
 */
public class ControlModule extends Module implements VersionInfo, Control {

    private boolean started;

    private boolean exitCmdSent = false;

    private Map<String, Object> attr;

    /**
   * Class Constructor.
   * 
   * @param ttif the TTIF SUTConnection, where some central state is maintained
   */
    public ControlModule(short nodeId, TTIFSUTConnection ttif) {
        super(nodeId, 0, ttif);
    }

    /**
   * Called after handshaking.  This should register any needed command 
   * handlers, which will stay registered for the duration of this connection
   * (ie. until {@link #doDisconnect} is called).
   * 
   * @param attr    the set of attributes sent from the target
   */
    public void doConnect(Map<String, Object> attr) {
        this.attr = attr;
        started = ((Number) (attr.get("TARGET_RUNNING"))).intValue() == 1;
        exitCmdSent = false;
        register(DROP_IND);
        register(START);
        register(EXIT);
        register(MAPS_REQ);
        register(MAPS_IND);
        register(MAPS_RSP);
    }

    /**
   * Called after/during disconnect, to give the module a chance to reset
   * any internal state.
   */
    public void doDisconnect() {
        this.attr = null;
        List<MapEntry> maps = this.maps;
        if (maps != null) {
            synchronized (maps) {
                maps.notify();
            }
        }
    }

    /**
   * Return the copyright string embedded in the target, or <code>null</code> 
   * if unknown.
   */
    public String getBuildCopyright() {
        return getAttr("COPYRIGHT");
    }

    /**
   * Return the version string embedded in the target, or <code>null</code> 
   * if unknown.
   */
    public String getBuildVersion() {
        return getAttr("VERSION");
    }

    /**
   * Return the build info string embedded in the target, or <code>null</code> 
   * if unknown.
   */
    public String getBuildInfo() {
        return getAttr("BUILDINFO");
    }

    /**
   * Return the build date string, or <code>null</code> if unknown.
   */
    public String getBuildDate() {
        return getAttr("APPLDATE");
    }

    /**
   * Return the build time string, or <code>null</code> if unknown
   */
    public String getBuildTime() {
        return getAttr("APPLTIME");
    }

    /**
   * Return the name of the user who performed the build, or 
   * <code>null</code> if unknown
   */
    public String getBuildUser() {
        return getAttr("USERNAME");
    }

    /**
   * Return the cmdline or node name, or <code>null</code> if unknown
   */
    public String getName() {
        return getAttr("CMDLINE");
    }

    private String getAttr(String name) {
        Map<String, Object> attr = this.attr;
        if (attr == null) return null;
        return (String) (attr.get(name));
    }

    /**
   * Information about a segment in the memory map
   */
    public static final class MapEntry {

        public final long base;

        public final long end;

        public final long foff;

        public final String path;

        public MapEntry(long base, long end, long foff, String path) {
            this.base = base;
            this.end = end;
            this.foff = foff;
            this.path = path;
        }

        public String toString() {
            return Long.toHexString(base) + "-" + Long.toHexString(end) + " " + path;
        }
    }

    private Object getMapsLock = new Object();

    private List<MapEntry> maps;

    /**
   * Get the list of segments in the target process's address space
   */
    public List<MapEntry> getMaps() {
        List<MapEntry> result = null;
        try {
            synchronized (getMapsLock) {
                result = maps = new ArrayList<MapEntry>();
                UDataOutputStream dout = dout();
                synchronized (maps) {
                    synchronized (dout) {
                        if (!hasSentExit()) {
                            System.err.println(ControlModule.this.hashCode() + ": requenst maps: " + System.identityHashCode(maps));
                            writeHeader(dout, MAPS_REQ, 0);
                            dout.flush();
                            requestDoWork();
                        }
                    }
                    System.err.println(ControlModule.this.hashCode() + ": wait maps: " + System.identityHashCode(maps));
                    maps.wait(6000);
                }
                maps = null;
            }
        } catch (InterruptedException e) {
        } catch (NullPointerException e) {
        } catch (IOException e) {
            handleIOException(e);
        }
        System.err.println(ControlModule.this.hashCode() + ": got maps: " + System.identityHashCode(maps));
        return result;
    }

    /**
   * Has the SUT already started running?
   * 
   * @return <code>true</code> if SUT is running
   */
    public boolean hasStarted() {
        return started;
    }

    /**
   * Send the start command to the hostbuild.  This should only happen
   * once.
   */
    public void sendStart() {
        UDataOutputStream dout = dout();
        if (dout == null) return;
        try {
            synchronized (dout) {
                if (!started) {
                    started = true;
                } else {
                    Environment.getEnvironment().error("already started SUT");
                    return;
                }
                writeHeader(dout, START, 0);
                dout.flush();
            }
        } catch (NullPointerException e) {
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    /**
   * Send the exit command to the hostbuild.
   */
    public void sendExit() {
        try {
            UDataOutputStream dout = dout();
            synchronized (dout) {
                try {
                    writeHeader(dout, EXIT, 0);
                } finally {
                    exitCmdSent = true;
                }
                dout.flush();
            }
        } catch (IOException e) {
            CharArrayWriter charArr = new CharArrayWriter();
            e.printStackTrace(new PrintWriter(charArr));
            Environment.getEnvironment().debug(0, charArr.toString());
        }
    }

    protected boolean isStarted() {
        return (dout() != null) && started && !hasSentExit();
    }

    protected boolean hasSentExit() {
        return exitCmdSent;
    }

    private static IDropHandler[] dropHandlers;

    private static IDropHandler[] getDropHandlers() {
        if (dropHandlers == null) {
            dropHandlers = (IDropHandler[]) ti.mcore.u.PluginUtil.getExecutableExtensions("ti.mcore.sutc.dropHandler", IDropHandler.class);
        }
        return dropHandlers;
    }

    private void fireDropEvent(long time, short nodeId, short numDropped) {
        IDropHandler[] dropHandlers = getDropHandlers();
        for (int i = 0; i < dropHandlers.length; i++) dropHandlers[i].handleDrop(time, nodeId, numDropped);
    }

    private Message DROP_IND = new Message(0, "DROP_IND") {

        @Override
        public void processMessage(UDataInputStream din, int compSts, int size, long time) throws IOException {
            short numDropped;
            if (ttif().getMajorVersion() == TTIFSUTConnection.MAJOR_VERSION_LEGACY) {
                time = din.readUInt();
                numDropped = din.readUByte();
                size -= 5;
            } else {
                numDropped = din.readShort();
                size -= 2;
            }
            while (size-- > 0) din.readByte();
            fireDropEvent(time, (short) nodeId, numDropped);
        }
    };

    private Message START = new Message(1, "START") {
    };

    private Message EXIT = new Message(2, "EXIT") {

        @Override
        public void processMessage(UDataInputStream din, int compSts, int size, long time) throws IOException {
            synchronized (ttif) {
                ttif.notify();
            }
        }
    };

    private Message MAPS_REQ = new Message(3, "MAPS_REQ") {
    };

    private Message MAPS_IND = new Message(4, "MAPS_IND") {

        @Override
        public void processMessage(UDataInputStream din, int compSts, int size, long time) throws IOException {
            int seqn = din.readUShort();
            long base = din.readUInt();
            long end = din.readUInt();
            long foff = din.readUInt();
            String path = readString(din);
            if (maps == null) throw new ProgrammingErrorException("didn't expect MAPS_IND now");
            if (seqn != maps.size()) System.err.println("unexpected seqn: " + seqn + ", expected: " + maps.size());
            System.err.println(ControlModule.this.hashCode() + ": append maps[" + maps.size() + "]: " + System.identityHashCode(maps));
            System.err.printf("  base=%08x, end=%08x, foff=%08x, path=%s\n", base, end, foff, path);
            maps.add(new MapEntry(base, end, foff, path));
        }
    };

    private Message MAPS_RSP = new Message(5, "MAPS_RSP") {

        @Override
        public void processMessage(UDataInputStream din, int compSts, int size, long time) throws IOException {
            int cnt = din.readUShort();
            if (maps == null) throw new ProgrammingErrorException("didn't expect MAPS_RSP now");
            if (cnt != maps.size()) System.err.println("unexpected count: " + cnt + ", expected: " + maps.size());
            synchronized (maps) {
                System.err.println(ControlModule.this.hashCode() + ": notify maps[" + maps.size() + "]: " + System.identityHashCode(maps));
                maps.notify();
            }
        }
    };
}
