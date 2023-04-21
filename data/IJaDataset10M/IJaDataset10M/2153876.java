package neembuu.vfs.test;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import jpfm.DirectoryStream;
import jpfm.FileAttributesProvider;
import jpfm.FormatterEvent;
import jpfm.JPfm;
import jpfm.mount.MountFlags;
import jpfm.SystemUtils;
import jpfm.MountListener;
import jpfm.UnmountException;
import jpfm.VolumeVisibility;
import jpfm.fs.FSUtils;
import jpfm.mount.Mount;
import jpfm.mount.MountParams.ParamType;
import jpfm.mount.MountParamsBuilder;
import jpfm.mount.Mounts;
import jpfm.volume.CommonFileAttributesProvider;
import jpfm.volume.VeryBigFile;
import jpfm.volume.vector.VectorRootDirectory;
import neembuu.mediafire.MediafireDirectLinkProvider;
import neembuu.util.logging.LoggerUtil;
import neembuu.vfs.NeembuuVirtualFileSystem;

/**
 *
 * @author Shashank Tulsyan
 */
public class MonitorFrame extends Thread implements MountListener, ActionListener {

    String[] filesToMount;

    String mountLocation;

    String heapLocation;

    NeembuuVirtualFileSystem fileSystem;

    VectorRootDirectory volume;

    Mount mount;

    final JPfm.Manager jpfmManager;

    private static JFrame frame;

    JPanel content;

    protected static MonitorFrame SINGLETON;

    private static final Logger LOGGER = LoggerUtil.getLogger();

    public static final boolean DEBUG = true;

    public MonitorFrame(final String mountLocation, final String heapLocation, final String[] filesToMount) {
        super("MonitorFrameThread");
        jpfmManager = JPfm.setDefaultManager(new JPfm.DefaultManager.LibraryLoader() {

            @Override
            public boolean loadLibrary(Logger logger) {
                try {
                    if (SystemUtils.IS_OS_WINDOWS) System.load("f:\\neembuu\\nbvfs_native\\jpfm\\VS_project_files\\Release\\jpfm.dll"); else System.load("/Volumes/Files and Workspace/neembuu/nbvfs_native/jpfm_mac/jpfm_xcode/build/Debug/libjpfm.dylib");
                    logger.info("using f:\\neembuu\\nbvfs_native\\jpfm\\VS_project_files\\Release\\jpfm.dll");
                    return true;
                } catch (Exception e) {
                    return false;
                }
            }
        }, true);
        this.filesToMount = filesToMount;
        this.heapLocation = heapLocation;
        this.mountLocation = mountLocation;
        createVolume();
        this.start();
        class CreateGuiThread extends Thread {

            public CreateGuiThread() {
                super("CreateGuiThread");
            }

            @Override
            public void run() {
                createGUI();
            }
        }
        new CreateGuiThread().start();
    }

    private void createVolume() {
        volume = new VectorRootDirectory(10, 3, CommonFileAttributesProvider.DEFAULT);
        fileSystem = new NeembuuVirtualFileSystem(volume);
        if (SystemUtils.IS_OS_WINDOWS) {
            VeryBigFile veryBigFile = new VeryBigFile(volume);
            volume.add(veryBigFile);
        }
        for (String s : filesToMount) {
            if (s.startsWith("http://")) {
                System.out.println(s);
                try {
                    if (s.contains("www.mediafire")) {
                        double random = Math.random();
                        try {
                            MonitoredHttpFile mhttpfile = new MonitoredHttpFile(MediafireDirectLinkProvider.class, s, random + "_nameCouldNotBeFound.mediafire.com.http", -1, heapLocation, volume);
                            volume.add(mhttpfile);
                        } catch (Exception any) {
                            LOGGER.log(Level.INFO, " ", any);
                        }
                    } else if (s.endsWith("rmvb")) {
                        MonitoredHttpFile mhttpfile = new MonitoredHttpFile(s, "test120k.http.rmvb", -1, heapLocation, volume);
                        volume.add(mhttpfile);
                    } else if (s.endsWith("mp4")) {
                        MonitoredHttpFile mhttpfile = new MonitoredHttpFile(s, "LesChevaliersduCielHDPromo.mp4", -1, heapLocation, volume);
                        volume.add(mhttpfile);
                    } else if (s.endsWith("mkv")) {
                        MonitoredHttpFile mhttpfile = new MonitoredHttpFile(s, "dn.http.mkv", -1, heapLocation, volume);
                        volume.add(mhttpfile);
                    } else {
                        MonitoredHttpFile mhttpfile = new MonitoredHttpFile(s, s.substring(s.lastIndexOf('/') + 1), -1, heapLocation, volume);
                        volume.add(mhttpfile);
                    }
                } catch (Exception a) {
                    LOGGER.log(Level.INFO, " ", a);
                }
            }
            File ff = new File(s);
            if (!ff.exists()) {
                LOGGER.log(Level.INFO, "ignoring : file not found={0}", s);
                continue;
            }
            try {
                ThrottledRealFile file1 = new ThrottledRealFile(ff.getAbsolutePath(), ThrottledRealFile.INVALID_CPS, volume);
                volume.add(file1);
            } catch (Exception a) {
                LOGGER.log(Level.INFO, " ", a);
            }
        }
        String basicPath = null;
        if (SystemUtils.IS_OS_WINDOWS) {
            basicPath = "J:\\neembuu\\realfiles\\";
        } else if (SystemUtils.IS_OS_LINUX) {
            basicPath = "/media/j/neembuu/realfiles/";
        } else {
            basicPath = "/Volumes/MIDS/neembuu/realfiles/";
        }
        try {
            File[] files = new File(basicPath).listFiles();
            for (int i = 0; i < files.length; i++) {
                String nextPath = files[i].getAbsolutePath();
                try {
                    ThrottledRealFile file1 = new ThrottledRealFile(nextPath, ThrottledRealFile.INVALID_CPS, volume);
                    volume.add(file1);
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "could not put file", e);
                }
            }
        } catch (Exception any) {
            LOGGER.log(Level.FINE, "could not put file", any);
        }
    }

    public void createGUI() {
        frame.addWindowListener(new CloseHandler());
        content = new JPanel(new FlowLayout());
        JButton n = new JButton("Unmount");
        n.setActionCommand("unmount");
        n.addActionListener(this);
        n.setBounds(10, 10, 300, 100);
        JButton showOpenId = new JButton("Show open ids");
        showOpenId.setActionCommand("showOpenId");
        showOpenId.addActionListener(this);
        showOpenId.setBounds(10, 230, 300, 100);
        content.add(showOpenId);
        content.add(n);
        JButton printPendingOps = new JButton("Print pending ops");
        printPendingOps.setActionCommand("printPendingOps");
        printPendingOps.addActionListener(this);
        content.add(printPendingOps);
        JButton gcChecker = new JButton("GC Check");
        gcChecker.setActionCommand("gcChecker");
        gcChecker.addActionListener(this);
        content.add(gcChecker);
        for (FileAttributesProvider ff : (DirectoryStream) volume) {
            if (ff instanceof MonitoredHttpFile) content.add(((MonitoredHttpFile) ff).getFilePanel()); else if (ff instanceof MonitoredRealFile) content.add(((MonitoredRealFile) ff).getFilePanel()); else if (ff instanceof MonitoredAbstractFile) {
                content.add(((MonitoredAbstractFile) ff).getFilePanel());
            } else {
                LOGGER.log(Level.INFO, "cannot add {0} to display frame ", ff);
            }
        }
        content.setPreferredSize(new Dimension(600, 5000));
        JScrollPane scrollPane = new JScrollPane(content);
        frame.setContentPane(scrollPane);
        frame.setPreferredSize(new Dimension(650, 600 + 100));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(SystemUtils.OS_ARCH);
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception lookandfeelexception) {
            LOGGER.log(Level.INFO, " ", lookandfeelexception);
        }
        frame = new JFrame("Monitored Neembuu Virtual Volume (containing real files) ");
        frame.setMaximumSize(new Dimension(428, 380 + 100));
        final String[] filesToMount;
        String mountLocation;
        String heapLocation;
        if (SystemUtils.IS_OS_WINDOWS) {
            mountLocation = "j:\\neembuu\\virtual\\monitored.nbvfs";
            heapLocation = "J:\\neembuu\\heap\\";
        } else if (SystemUtils.IS_OS_LINUX) {
            mountLocation = "/media/j/neembuu/virtual/monitored14/";
            heapLocation = "/media/j/neembuu/heap/";
        } else {
            mountLocation = "/Volumes/MIDS/neembuu/virtual/monitored/";
            heapLocation = "/Volumes/MIDS/neembuu/heap/";
        }
        boolean askUser = false;
        try {
            if (!new File(mountLocation).exists()) {
                askUser = true;
            }
        } catch (Exception any) {
            askUser = true;
        }
        if (askUser) {
            javax.swing.JOptionPane.showMessageDialog(frame, "Please first choose a mount location (perferably a file).", "This app is for experimental purpose, may not be very user friendly", javax.swing.JOptionPane.PLAIN_MESSAGE);
            javax.swing.JFileChooser fileChooser = new javax.swing.JFileChooser();
            fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
            int retVal = fileChooser.showOpenDialog(frame);
            if (retVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                mountLocation = fileChooser.getSelectedFile().getAbsoluteFile().getPath();
            } else {
                System.err.println("User did not select a mount location");
                System.exit(-1);
            }
            javax.swing.JOptionPane.showMessageDialog(frame, "Choose a folder which we can use to temporarily\n" + "store the downloaded data.  \n", "This app is for experimental purpose, may not be very user friendly", javax.swing.JOptionPane.PLAIN_MESSAGE);
            fileChooser = new javax.swing.JFileChooser();
            fileChooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
            retVal = fileChooser.showOpenDialog(frame);
            if (retVal == javax.swing.JFileChooser.APPROVE_OPTION) {
                heapLocation = fileChooser.getSelectedFile().getAbsoluteFile().getPath();
            } else {
                System.err.println("User did not select a directory");
                System.exit(-1);
            }
        }
        System.setProperty("neembuu.vfs.test.MoniorFrame.resumepolicy", "emptyDirectory");
        if (SystemUtils.IS_OS_WINDOWS) {
            filesToMount = new String[] { "http://localhost:8080/LocalFileServer-war/servlet/FileServer?totalFileSpeedLimit=120&newConnectionTimemillisec=1&file=test120k.rmvb" };
        } else if (SystemUtils.IS_OS_LINUX) {
            filesToMount = new String[] { "http://neembuu.sourceforge.net/test120k.rmvb", "/media/j/Videos/Requiem_for_a_Duel.mkv" };
        } else {
            System.out.println("someother os, probably mac :" + SystemUtils.OS_NAME);
            filesToMount = new String[] { "http://update0.jdownloader.org/test120k.rmvb", "/Volumes/MIDS/Videos/Requiem_for_a_Duel.mkv" };
        }
        final String ml = mountLocation;
        final String hl = heapLocation;
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                MonitorFrame frame = new MonitorFrame(ml, hl, filesToMount);
                SINGLETON = frame;
            }
        });
    }

    @Override
    public void eventOccurred(FormatterEvent event) {
        if (event.getEventType() == FormatterEvent.EVENT.SUCCESSFULLY_MOUNTED) {
            LOGGER.info("testing cascade mounting\n\n\n\n\n\n\n\n\n");
            class kq extends Thread {

                public kq() {
                    super("Process thread");
                    setDaemon(true);
                }

                @Override
                public void run() {
                    try {
                        if (SystemUtils.IS_OS_WINDOWS) {
                            setPriority(MIN_PRIORITY);
                            Thread.sleep(3000);
                            String process = "p:\\x86\\VideoLAN\\VLC\\vlc.exe";
                            ProcessBuilder pb = new ProcessBuilder(process, "J:\\neembuu\\virtual\\monitored.nbvfs\\test120k.http.rmvb");
                            System.out.println("Attach profiler");
                        }
                    } catch (Exception a) {
                        LOGGER.log(Level.INFO, " ", a);
                    }
                }
            }
            new kq().start();
            if (true) return;
            Set<FileAttributesProvider> files = new LinkedHashSet<FileAttributesProvider>();
            files.add(this.volume.get("S01E01 - How To Use A Webcam.wmv.001"));
            files.add(this.volume.get("S01E01 - How To Use A Webcam.wmv.002"));
            files.add(this.volume.get("S01E01 - How To Use A Webcam.wmv.003"));
            files.add(this.volume.get("S01E01 - How To Use A Webcam.wmv.004"));
            files.add(this.volume.get("S01E01 - How To Use A Webcam.wmv.005"));
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            if (e.getActionCommand().equalsIgnoreCase("unmount")) {
                Thread[] t = new Thread[mount.getThreadGroup(jpfmManager).activeCount()];
                mount.getThreadGroup(jpfmManager).enumerate(t);
                System.out.println("++thread list++");
                for (int i = 0; i < t.length; i++) {
                    Thread thread = t[i];
                    System.out.println(thread);
                }
                System.out.println("--thread list--");
                mount.unMount();
                System.out.println("unmounted");
            }
            if (e.getActionCommand().equalsIgnoreCase("showOpenId")) {
                Iterator<FileAttributesProvider> it = volume.iterator();
                System.out.println("++open id of all++");
                while (it.hasNext()) {
                    FileAttributesProvider next = it.next();
                    System.out.print(next.getName());
                    System.out.print(" ");
                    System.out.println(next.getFileDescriptor());
                    System.out.print(" ");
                    System.out.print(" class=" + next.getClass());
                }
                System.out.println("--open id of all--");
            }
            if (e.getActionCommand().equals("printPendingOps")) {
                FSUtils.printIncompleteOperationsBasic(fileSystem);
                return;
            }
            if (e.getActionCommand().equals("gcChecker")) {
                System.out.println("NumberOfReadInstancesInMemory=" + jpfm.operations.ReadImpl.numberOfReadInstancesInMemory);
                return;
            }
        } catch (UnmountException u) {
            LOGGER.log(Level.INFO, " ", u);
        }
    }

    class CloseHandler extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            try {
                if (mount != null) mount.unMount();
            } catch (Exception a) {
                LOGGER.log(Level.INFO, " ", a);
            }
            System.exit(0);
        }
    }

    @Override
    public void run() {
        MountFlags mountFlags = null;
        if (SystemUtils.IS_OS_WINDOWS) {
            mountFlags = new MountFlags.Builder().setForceUnbuffered().setWorldRead().setSystemVisible().build();
        } else {
            mountFlags = new MountFlags.Builder().setForceUnbuffered().build();
        }
        try {
            mount = Mounts.mount(new MountParamsBuilder().set(ParamType.LISTENER, this).set(ParamType.MOUNT_LOCATION, mountLocation).set(ParamType.FILE_SYSTEM, fileSystem).set(ParamType.EXIT_ON_UNMOUNT, false).set(ParamType.VOLUME_VISIBILITY, VolumeVisibility.GLOBAL).set(ParamType.MOUNT_FLAGS, mountFlags).build());
        } catch (Exception any) {
            LOGGER.log(Level.INFO, " ", any);
            System.exit(1);
        }
    }
}
