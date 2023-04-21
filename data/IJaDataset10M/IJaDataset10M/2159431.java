package funcenter.main;

import funcenter.gui.JanelaLogin;
import funcenter.gui.JanelaPrincipal;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import sun.jvmstat.monitor.HostIdentifier;
import sun.jvmstat.monitor.MonitorException;
import sun.jvmstat.monitor.MonitoredHost;
import sun.jvmstat.monitor.MonitoredVm;
import sun.jvmstat.monitor.MonitoredVmUtil;
import sun.jvmstat.monitor.VmIdentifier;

/**
 *
 * @author arthur
 */
public class Main {

    private static JanelaPrincipal janelaPrincipal;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        RuntimeMXBean rt = ManagementFactory.getRuntimeMXBean();
        final int runtimePid = Integer.parseInt(rt.getName().substring(0, rt.getName().indexOf("@")));
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                if (getMonitoredVMs(runtimePid)) {
                    try {
                        Runtime.getRuntime().exec("java -classpath ./lib/hsqldb.jar org.hsqldb.Server -database.0 funcenter -dbname.0 funcenter");
                    } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        Thread.sleep(2500);
                    } catch (InterruptedException e) {
                    }
                    ;
                    String nativeLF = UIManager.getSystemLookAndFeelClassName();
                    try {
                        UIManager.setLookAndFeel(nativeLF);
                    } catch (InstantiationException e) {
                    } catch (ClassNotFoundException e) {
                    } catch (UnsupportedLookAndFeelException e) {
                    } catch (IllegalAccessException e) {
                    }
                    log();
                } else {
                    JOptionPane.showMessageDialog(null, "O Fun Center ja está sendo rodado.");
                }
            }
        });
    }

    public static JanelaPrincipal getJanelaPrincipal() {
        if (janelaPrincipal == null) {
            janelaPrincipal = new JanelaPrincipal();
        }
        return janelaPrincipal;
    }

    public static void log() {
        janelaPrincipal = null;
        JanelaLogin janela = new JanelaLogin();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = janela.getSize().width;
        int h = janela.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        janela.setLocation(x, y);
        janela.setVisible(true);
    }

    private static boolean getMonitoredVMs(int processPid) {
        MonitoredHost host;
        Set vms;
        try {
            host = MonitoredHost.getMonitoredHost(new HostIdentifier((String) null));
            vms = host.activeVms();
        } catch (java.net.URISyntaxException sx) {
            throw new InternalError(sx.getMessage());
        } catch (MonitorException mx) {
            throw new InternalError(mx.getMessage());
        }
        MonitoredVm mvm = null;
        String processName = null;
        try {
            mvm = host.getMonitoredVm(new VmIdentifier(String.valueOf(processPid)));
            processName = MonitoredVmUtil.commandLine(mvm);
            processName = processName.substring(processName.lastIndexOf("\\") + 1, processName.length());
            mvm.detach();
        } catch (Exception ex) {
        }
        for (Object vmid : vms) {
            if (vmid instanceof Integer) {
                int pid = ((Integer) vmid).intValue();
                String name = vmid.toString();
                try {
                    mvm = host.getMonitoredVm(new VmIdentifier(name));
                    name = MonitoredVmUtil.commandLine(mvm);
                    name = name.substring(name.lastIndexOf("\\") + 1, name.length());
                    mvm.detach();
                    if ((name.equalsIgnoreCase(processName)) && (processPid != pid)) {
                        return false;
                    }
                } catch (Exception x) {
                }
            }
        }
        return true;
    }
}
