package net.sf.xqz.tests.utils.xtext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.xtext.junit.AbstractXtextTests;

public abstract class AbstractXQZXTextTests extends AbstractXtextTests {

    static String UBQT_FILE_2 = "import \"o.orchestror\";" + "\ndisplay(1440,900);" + "\nfiducial fiducial5 code 5;" + "\nfiducial fiducial8 code 8;" + "\nfiducial fiducial9 code 9;" + "\n" + "\naction udpModulateDrumMachineTempoAction trigger \"org.tetalab.tangible.puredata.setDrumMachineTempoAction\";" + "\naction udpSelectDrumMachineIntrumentAction trigger \"org.tetalab.tangible.puredata.selectDrumMachineInstrumentAction\";" + "\naction udpSetDrumMachineIntrumentPatternAction trigger \"org.tetalab.tangible.puredata.setDrumMachineInstrumentPatternAction\";" + "\n" + "\nreaction readInstrumentPatternReaction handles \"drumMachinePatternSettingCommand\" with \"org.tetalab.tangible.puredata.tcpCmdHandler\";" + "\n" + "\n" + "\nslot p0CheckboxSlot in P0 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p23CheckboxSlot in P23 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p45CheckboxSlot in P45 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p67CheckboxSlot in P67 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p90CheckboxSlot in P90 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p113CheckboxSlot in P113 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p135CheckboxSlot in P135 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p157CheckboxSlot in P157 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p180CheckboxSlot in P180 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p203CheckboxSlot in P203 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p225CheckboxSlot in P225 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p247CheckboxSlot in P247 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p270CheckboxSlot in P270 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p293CheckboxSlot in P293 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p315CheckboxSlot in P315 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\nslot p337CheckboxSlot in P337 kind PUSH status UNSELECTED {" + "\n  emits udpSetDrumMachineIntrumentPatternAction on WIDGET_SELECTION;" + "\n};" + "\n" + "\nproxima pureData127_0_0_1_port3002 maps fiducial9 to engineUDP2 range [0..9] radius 180 requires pureData127_0_0_1_port3000 {" + "\n  emits udpSelectDrumMachineIntrumentAction on PROXIMA_UPDATE;" + "\n};" + "\n" + "\nproxima pureData127_0_0_1_port3001 maps fiducial5 to engineUDP1 range [50..180] radius 180 {" + "\n  emits udpModulateDrumMachineTempoAction on PROXIMA_UPDATE;" + "\n};" + "\n" + "\nproxima pureData127_0_0_1_port3000 maps fiducial8 to engineUDP0 range [0..16] radius 180  requires pureData127_0_0_1_port3002 {" + "\n  p0CheckboxSlot, p23CheckboxSlot, p45CheckboxSlot, p67CheckboxSlot, p90CheckboxSlot, " + "\n  p113CheckboxSlot, p135CheckboxSlot, p157CheckboxSlot, p180CheckboxSlot, p203CheckboxSlot, " + "\n  p225CheckboxSlot, p247CheckboxSlot, p270CheckboxSlot, p293CheckboxSlot, p315CheckboxSlot, p337CheckboxSlot;" + "\n  emits udpSetDrumMachineIntrumentPatternAction on PROXIMA_ADD;" + "\n  handles readInstrumentPatternReaction with pureData127_0_0_1_port3002;" + "\n};" + "\n" + "\ncursor mouse (0,0) dimension (5,5) ;" + "";

    static String NETCONF_FILE_1 = "import \"o.orchestror\";" + "\nlistener listenerUSB0 : \"net.sf.smbt.i2c.thingm.cmdHandler\";" + "\ninterpreter interpreterUSB0 : \"net.sf.smbt.i2c.thingm.cmdInterpreter\";" + "\nport usb0 : \"COM3\" as USB at 19200;" + "\nscope orchestror1 {" + "\n\tbind engineUSB0 => interpreterUSB0  to usb0 with listenerUSB0;" + "\n};\n";

    static String NETCONF_FILE_2 = "import \"o.orchestror\";" + "\nlistener listenerUDP : \"net.sf.smbt.tcp.puredata.cmdHandler\";" + "\ninterpreter interpreterUDP : \"net.sf.smbt.tcp.puredata.cmdInterpreter\";" + "\ndecoder decoderUDP : \"net.sf.smbt.tcp.puredata.frameInterpreter\";" + "\n" + "\nport udp0 : \"127.0.0.1:3000\" as UDP accept 1234;" + "\nport udp1 : \"127.0.0.1:3001\" as UDP;" + "\nport udp2 : \"127.0.0.1:3002\" as UDP;" + "\n" + "\n" + "\nscope orchestror1 {" + "\n  bind engineUDP0 => interpreterUDP <= decoderUDP to udp0 with listenerUDP;" + "\n  bind engineUDP1 => interpreterUDP to udp1 with listenerUDP;" + "\n  bind engineUDP2 => interpreterUDP to udp2 with listenerUDP;" + "\n};\n";

    static String ORCHESTROR_FILE_1 = "orchestror orchestror1 {" + "\nengine engineUSB0;" + "\nclient client1 { engineUSB0 };" + "\n  timeline timeline1 : engineUSB0 ;" + "\n  application application1 { client1 };" + "\n}\n";

    static String ORCHESTROR_FILE_2 = "orchestror orchestror1 {" + "\nengine engineUDP0;" + "\nengine engineUDP1;" + "\nengine engineUDP2;" + "\nclient client1 { engineUSB0 engineUDP0 engineUDP1 engineUDP2 };" + "\n  timeline timeline1 : engineUDP0 ;" + "\n  timeline timeline2 : engineUDP1 ;" + "\n  timeline timeline3 : engineUDP2 ;" + "\napplication application1 { client1 };" + "\n}\n";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public static void init1USB0TestWorkspace() {
        initTestWorkspace(null, NETCONF_FILE_1, ORCHESTROR_FILE_1);
    }

    public static void initDrumMachineTestWorkspace() {
        initTestWorkspace(UBQT_FILE_2, NETCONF_FILE_2, ORCHESTROR_FILE_2);
    }

    public static void initTestWorkspace(String UBQ_FILE, String NETCFG_FILE, String ORCHESTROR_FILE) {
        if (Platform.isRunning()) {
            try {
                IWorkspace workspace = ResourcesPlugin.getWorkspace();
                IWorkspaceRoot wsroot = workspace.getRoot();
                IProject p = wsroot.getProject("DefaultXQZProject");
                IProjectDescription desc = workspace.newProjectDescription(p.getName());
                if (p.exists()) {
                    p.delete(true, new NullProgressMonitor());
                }
                p.create(desc, null);
                if (!p.isOpen()) {
                    p.open(null);
                }
                Path dataDirPath = new Path("data");
                if (!p.getFolder(dataDirPath).exists()) {
                    p.getFolder(dataDirPath).create(true, true, new NullProgressMonitor());
                }
                File netconf = new File("n.netconf");
                FileOutputStream neOutputStream = new FileOutputStream(netconf);
                neOutputStream.write(NETCFG_FILE.getBytes());
                neOutputStream.flush();
                neOutputStream.close();
                if (!p.getFile("data/n.netconf").exists()) {
                    p.getFile("data/n.netconf").create(new FileInputStream(netconf), true, new NullProgressMonitor());
                }
                File orchestrorFile = new File("o.orchestror");
                FileOutputStream orOutputStream = new FileOutputStream(orchestrorFile);
                orOutputStream.write(ORCHESTROR_FILE.getBytes());
                orOutputStream.flush();
                orOutputStream.close();
                if (!p.getFile("data/o.orchestror").exists()) {
                    p.getFile("data/o.orchestror").create(new FileInputStream(orchestrorFile), true, new NullProgressMonitor());
                }
                if (UBQ_FILE != null) {
                    File ubqtFile = new File("u.ubqt");
                    FileOutputStream ubqtOutputStream = new FileOutputStream(ubqtFile);
                    ubqtOutputStream.write(UBQ_FILE.getBytes());
                    ubqtOutputStream.flush();
                    ubqtOutputStream.close();
                    if (!p.getFile("data/u.ubqt").exists()) {
                        p.getFile("data/u.ubqt").create(new FileInputStream(ubqtFile), true, new NullProgressMonitor());
                    }
                }
                p.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
            } catch (CoreException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new UnsupportedOperationException("OSGi needed !!!");
        }
    }
}
