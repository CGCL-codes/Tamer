package net.sf.jaybox.gui;

import java.io.*;
import java.net.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import net.sf.jaybox.common.*;
import net.sf.jaybox.iso.*;
import net.sf.jaybox.ftp.*;
import net.sf.jaybox.ftp.*;

public class MainWindow extends Dialog implements WindowListener, ActionListener, Runnable {

    private ItemList list;

    private Button bLoadIso, bLoadTax, bClearList, bExecuteList, bAbout, bClose;

    private ProgressPanel progressPanel;

    private InetAddress bind_address;

    public MainWindow() {
        super(new Frame(), "[jaybox " + Constants.VERSION + "] ");
        setLayout(new BorderLayout(10, 10));
        Configuration.load();
        add(new Logo(), BorderLayout.NORTH);
        add(list = new ItemList(), BorderLayout.CENTER);
        Panel p = new Panel(new GridLayout(8, 1, 5, 5));
        add(p, BorderLayout.EAST);
        bLoadIso = add_button(p, "Load X-ISO");
        bLoadTax = add_button(p, "Load TAX");
        bClearList = add_button(p, "Clear Queue");
        p.add(new SplitLine(false));
        bExecuteList = add_button(p, "GO!");
        p.add(new SplitLine(false));
        bAbout = add_button(p, "About");
        bClose = add_button(p, "Close");
        bExecuteList.setForeground(Color.blue);
        bClose.setForeground(Color.red);
        bLoadTax.setEnabled(false);
        add(progressPanel = new ProgressPanel(), BorderLayout.SOUTH);
        addWindowListener(this);
        setVisible(true);
        pack();
        setResizable(false);
    }

    private void add_label(Panel p, String str) {
        Label l = new Label("              " + str);
        l.setForeground(Color.blue);
        p.add(l);
    }

    private Panel add_panel(Panel p, boolean left) {
        Panel p1 = new Panel(new FlowLayout(left ? FlowLayout.LEFT : FlowLayout.RIGHT));
        p.add(p1);
        return p1;
    }

    private Button add_button(Panel p, String str) {
        Button b = new Button(str);
        b.addActionListener(this);
        p.add(b);
        return b;
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowClosing(WindowEvent e) {
        onClose();
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == bLoadIso) onLoadIso(); else if (src == bClose) onClose(); else if (src == bLoadTax) onLoadTax(); else if (src == bClearList) onClearList(); else if (src == bExecuteList) onExecuteList(); else if (src == bAbout) onAbout();
    }

    private void onClose() {
        Configuration.save();
        System.exit(0);
    }

    private void onAbout() {
        MessageDialog md = new MessageDialog("About Jaybox");
        md.showMessage("jaybox " + Constants.VERSION + "\n" + " \n" + "By vahidi@sf.net\n" + "Visit our SourceForge page for updates: http://jaybox.sf.net\n" + " \n \n" + "(and yes, the code for the stupid 3D logo that people seem to love is also there)");
    }

    private void onLoadIso() {
        FileDialog fd = new FileDialog(this, "Select an XBOX ISO file to load", FileDialog.LOAD);
        fd.setDirectory(Configuration.getString("working.dir"));
        fd.setVisible(true);
        String filename = fd.getFile();
        if (filename != null) {
            Configuration.setString("working.dir", fd.getDirectory());
            IsoLoadDialog ild = new IsoLoadDialog(fd.getDirectory(), filename);
            Target t = ild.getTarget();
            if (t != null) {
                list.add(t);
            }
        }
    }

    private void onLoadTax() {
    }

    private void onClearList() {
        list.empty();
    }

    private void onExecuteList() {
        Thread thread = new Thread(this);
        try {
            thread.setPriority(Thread.MIN_PRIORITY);
        } catch (Exception i_dont_care) {
        }
        thread.start();
    }

    public void run() {
        Progress p = progressPanel.getProgress();
        p.total_bytes = 0;
        p.total_files = 0;
        for (Iterator<Target> it = list.iterator(); it.hasNext(); ) {
            Target t = it.next();
            p.total_bytes += t.getBytes();
            p.total_files += t.getFiles();
        }
        bind_address = null;
        try {
            bind_address = InetAddress.getByName(Configuration.getString("ftp.bind"));
        } catch (Exception ignored) {
        }
        p.active = true;
        for (Iterator<Target> it = list.iterator(); it.hasNext(); ) {
            Target t = it.next();
            try {
                execute(t, p);
                it.remove();
                list.rebuild();
            } catch (IOException exx) {
                exx.printStackTrace();
                ExceptionDialog ed = new ExceptionDialog("Error while processing " + t.name);
                ed.showException(exx);
            }
        }
        p.active = false;
    }

    private void execute(Target t, Progress p) throws IOException {
        if (t.source == Target.SourceType.SOURCE_ISO) {
            ReaderCallback rc = null;
            if (t.dest == Target.DestinationType.DEST_FTP) {
                rc = new FtpExtractReader(t.ftp_dir, t.ftp_host, t.ftp_port, t.ftp_user, t.ftp_pass, bind_address);
            } else if (t.dest == Target.DestinationType.DEST_EXTRACT) {
                rc = new LocalExtractReader(t.extract_dir);
            }
            IsoReader ir = new IsoReader(t.iso_file);
            rc.setProgress(p);
            ir.read(rc);
        } else {
            throw new IOException("NOT IMPLEMENTED YET");
        }
    }
}
