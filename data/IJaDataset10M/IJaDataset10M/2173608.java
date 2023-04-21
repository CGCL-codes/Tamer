package com.guanda.swidgex.widgets.mp3player;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileFilter;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import com.guanda.swidgex.api.Configurator;
import com.guanda.swidgex.api.IConfiguration;
import com.guanda.swidgex.api.IDesktopContextListener;
import com.guanda.swidgex.api.IMouseAwareWidget;
import com.guanda.swidgex.api.IWidgetContext;
import com.guanda.swidgex.api.IWidgetListener;
import com.guanda.swidgex.widgets.mp3player.positioner.B2T;
import com.guanda.swidgex.widgets.mp3player.positioner.BL2TR;
import com.guanda.swidgex.widgets.mp3player.positioner.BR2TL;
import com.guanda.swidgex.widgets.mp3player.positioner.L2R;
import com.guanda.swidgex.widgets.mp3player.positioner.R2L;
import com.guanda.swidgex.widgets.mp3player.positioner.T2B;
import com.guanda.swidgex.widgets.mp3player.positioner.TL2BR;
import com.guanda.swidgex.widgets.mp3player.positioner.TR2BL;

public class MP3Player extends JComponent implements IMouseAwareWidget, IExecutableExtension, IDesktopContextListener {

    private static final long serialVersionUID = 1L;

    private static final Color BG = new Color(58, 68, 78);

    private static final String CONFIG_ID = "com.guanda.swidgex.widgets.mp3player";

    private URL m3uURL;

    private List<M3UEntry> songs;

    private Timer timer;

    private PlayTask task;

    private JList mp3List;

    private JPanel llPanel;

    private JScrollPane mp3JSP;

    @Override
    public void init(IWidgetContext context) {
        timer = new Timer();
        context.getDesktopContext().addContextListener(this);
        readList();
        jump2(0);
    }

    public void setM3URL(URL url) {
        m3uURL = url;
        readList();
        jump2(0);
    }

    public void addMp32List(File mp3File, int index) {
        M3UEntry entry = new M3UEntry(mp3File);
        DefaultListModel listModel = (DefaultListModel) mp3List.getModel();
        synchronized (songs) {
            songs.add(index, entry);
            listModel.add(index, entry);
        }
        enableCtrls();
        jump2(index);
    }

    private void stopPlay() {
        if (task != null) {
            if (task.isPaused()) task.resume();
            task.cancel();
            slider.setValue(0);
            task = null;
            btnPP.setIcon(new ImageIcon(getClass().getResource("/icons/play.png")));
            playing = false;
        }
    }

    private void pausePlay() {
        if (task != null) {
            task.pause();
            btnPP.setIcon(new ImageIcon(getClass().getResource("/icons/play.png")));
            playing = false;
        }
    }

    private void startPlay() {
        if (songs == null) {
            readList();
        }
        int index = mp3List.getSelectedIndex();
        if (index == -1) {
            index = 0;
        }
        if (task == null) jump2(index); else {
            if (task.isPaused() && !task.isClosed()) {
                task.resume();
                btnPP.setIcon(new ImageIcon(getClass().getResource("/icons/pause.png")));
                playing = true;
            } else {
                jump2(index);
            }
        }
    }

    public void jump2(int index) {
        stopPlay();
        play(index);
    }

    private void play(int index) {
        task = new PlayTask(index, this);
        timer.schedule(task, 100, 100);
        btnPP.setIcon(new ImageIcon(getClass().getResource("/icons/pause.png")));
        btnStop.setEnabled(true);
        playing = true;
    }

    private void playNext() {
        int current = mp3List.getSelectedIndex();
        int size = songs.size();
        int index = (current + 1) % size;
        jump2(index);
    }

    private void playPrev() {
        int current = mp3List.getSelectedIndex();
        int size = songs.size();
        int index = current - 1;
        if (index == -1) index = size - 1;
        jump2(index);
    }

    private void resetPlayLocation() {
        int value = slider.getValue();
        if (task != null) {
            task.jump2(value);
        }
    }

    private JPanel north;

    private JLabel lblCaption;

    private JSlider slider;

    private PushingLayout pushingLayout;

    private LyricPane lyricPane;

    private static final int ICON_PAD = 20;

    private static final int ICON_W = 10;

    private class JSliderMouse extends MouseInputAdapter {

        private boolean dragged;

        @Override
        public void mouseClicked(MouseEvent e) {
            resetPlayLocation();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (dragged) {
                dragged = false;
                resetPlayLocation();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            dragged = true;
        }
    }

    JButton btnPP;

    boolean playing;

    JButton btnStop;

    JButton btnPrev;

    JButton btnNext;

    private void btnPP_actionPerformed(ActionEvent e) {
        if (!playing) {
            startPlay();
        } else {
            pausePlay();
        }
    }

    @Override
    public JComponent getComponent() {
        setBackground(BG);
        setBorder(BorderFactory.createLineBorder(BG.brighter()));
        setLayout(new BorderLayout());
        north = new JPanel();
        north.setLayout(new BorderLayout());
        north.setBackground(BG);
        add(north, BorderLayout.NORTH);
        lblCaption = new JLabel();
        lblCaption.setHorizontalAlignment(JLabel.CENTER);
        lblCaption.setFont(new Font("Dialog", Font.PLAIN, 14));
        lblCaption.setText(" ");
        lblCaption.setForeground(Color.white);
        north.add(lblCaption, BorderLayout.NORTH);
        JPanel p = new JPanel();
        p.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
        p.setLayout(new BorderLayout());
        p.setBackground(BG);
        north.add(p, BorderLayout.CENTER);
        slider = new JSlider();
        slider.setBackground(BG);
        slider.setValue(0);
        slider.setEnabled(false);
        JSliderMouse mouse = new JSliderMouse();
        slider.addMouseListener(mouse);
        slider.addMouseMotionListener(mouse);
        p.add(slider, BorderLayout.CENTER);
        JPanel ctrlPanel = new JPanel();
        p.add(ctrlPanel, BorderLayout.SOUTH);
        ctrlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 2, 2));
        ctrlPanel.setBackground(BG);
        btnPP = new NavButton();
        btnPP.setToolTipText("Play");
        btnPP.setIcon(new ImageIcon(getClass().getResource("/icons/play.png")));
        btnPP.setPreferredSize(new Dimension(ICON_PAD, ICON_PAD));
        btnPP.setEnabled(false);
        btnPP.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                btnPP_actionPerformed(e);
            }
        });
        ctrlPanel.add(btnPP);
        btnStop = new NavButton();
        btnStop.setToolTipText("Stop");
        btnStop.setIcon(new ImageIcon(getClass().getResource("/icons/stop.png")));
        btnStop.setPreferredSize(new Dimension(ICON_PAD, ICON_PAD));
        btnStop.setEnabled(false);
        btnStop.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                stopPlay();
            }
        });
        ctrlPanel.add(btnStop);
        btnPrev = new NavButton();
        btnPrev.setToolTipText("Previous");
        btnPrev.setIcon(new ImageIcon(getClass().getResource("/icons/prev_song.png")));
        btnPrev.setPreferredSize(new Dimension(ICON_PAD, ICON_PAD));
        btnPrev.setEnabled(false);
        btnPrev.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                playPrev();
            }
        });
        ctrlPanel.add(btnPrev);
        btnNext = new NavButton();
        btnNext.setToolTipText("Next");
        btnNext.setIcon(new ImageIcon(getClass().getResource("/icons/next_song.png")));
        btnNext.setPreferredSize(new Dimension(ICON_PAD, ICON_PAD));
        btnNext.setEnabled(false);
        btnNext.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                playNext();
            }
        });
        ctrlPanel.add(btnNext);
        north.add(new MySeparator(), BorderLayout.SOUTH);
        JPanel lPanel = new JPanel(new BorderLayout());
        add(lPanel, BorderLayout.CENTER);
        llPanel = new JPanel(pushingLayout = new PushingLayout());
        lPanel.add(llPanel, BorderLayout.CENTER);
        llPanel.setBackground(BG);
        mp3List = new JList();
        enableDnD();
        enableDelete();
        mp3List.setModel(new DefaultListModel());
        mp3JSP = new JScrollPane(mp3List, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        llPanel.add(mp3JSP, "off");
        lyricPane = new LyricPane();
        lyricPane.setFont(new Font("Dialog", Font.PLAIN, 14));
        llPanel.add(lyricPane, "on");
        JPanel tmp = new JPanel(new BorderLayout());
        tmp.setBackground(BG);
        tmp.add(new MySeparator(), BorderLayout.NORTH);
        lPanel.add(tmp, BorderLayout.SOUTH);
        south = new JPanel(new FlowLayout(FlowLayout.CENTER, 2, 2));
        tmp.add(south, BorderLayout.CENTER);
        south.setBackground(BG);
        JButton btnPlay = new NavButton();
        btnPlay.setToolTipText("Open list...");
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/icons/list.png")));
        btnPlay.setPreferredSize(new Dimension(ICON_PAD, ICON_PAD));
        btnPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loadList();
            }
        });
        south.add(btnPlay);
        btnPlay = new NavButton();
        btnPlay.setToolTipText("Open directory...");
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/icons/dir.png")));
        btnPlay.setPreferredSize(new Dimension(ICON_PAD, ICON_PAD));
        btnPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openDir();
            }
        });
        south.add(btnPlay);
        btnPlay = new NavButton();
        btnPlay.setToolTipText("Save list...");
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/icons/save.png")));
        btnPlay.setPreferredSize(new Dimension(ICON_PAD, ICON_PAD));
        btnPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                saveList();
            }
        });
        south.add(btnPlay);
        btnPlay = new NavButton();
        btnPlay.setToolTipText("Clear list...");
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/icons/clear.png")));
        btnPlay.setPreferredSize(new Dimension(ICON_PAD, ICON_PAD));
        btnPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                clearList();
            }
        });
        south.add(btnPlay);
        JPanel swPanel = new JPanel(new BorderLayout());
        swPanel.setBackground(BG);
        south.add(swPanel);
        btnPlay = new NavButton();
        btnPlay.setToolTipText("Switch Views");
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/icons/switch.png")));
        btnPlay.setPreferredSize(new Dimension(ICON_PAD - 1, ICON_PAD));
        btnPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                switchViews();
            }
        });
        swPanel.add(btnPlay, BorderLayout.CENTER);
        btnPlay = new NavButton();
        btnPlay.setToolTipText("Switching Styles ...");
        btnPlay.setIcon(new ImageIcon(getClass().getResource("/icons/drop.png")));
        btnPlay.setPreferredSize(new Dimension(ICON_W - 1, ICON_PAD));
        final JButton b = btnPlay;
        btnPlay.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                selectStyle(b);
            }
        });
        swPanel.add(btnPlay, BorderLayout.EAST);
        mp3JSP.setBackground(BG);
        mp3JSP.setBorder(null);
        mp3List.setBackground(BG);
        mp3List.setForeground(Color.lightGray);
        mp3List.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = mp3List.getSelectedIndex();
                    if (index != -1) {
                        jump2(index);
                    }
                }
            }
        });
        return this;
    }

    protected void selectStyle(JButton b) {
        JPopupMenu popup = new JPopupMenu();
        JCheckBoxMenuItem jbmi = new JCheckBoxMenuItem();
        jbmi.setText("Left to Right");
        jbmi.setSelected(pushingLayout.getPositioner() instanceof L2R);
        jbmi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pushingLayout.setPositioner(new L2R());
                switchViews();
            }
        });
        popup.add(jbmi);
        jbmi = new JCheckBoxMenuItem();
        jbmi.setText("Top-Left to Bottom-Right");
        jbmi.setSelected(pushingLayout.getPositioner() instanceof TL2BR);
        jbmi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pushingLayout.setPositioner(new TL2BR());
                switchViews();
            }
        });
        popup.add(jbmi);
        jbmi = new JCheckBoxMenuItem();
        jbmi.setText("Top to Bottom");
        jbmi.setSelected(pushingLayout.getPositioner() instanceof T2B);
        jbmi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pushingLayout.setPositioner(new T2B());
                switchViews();
            }
        });
        popup.add(jbmi);
        jbmi = new JCheckBoxMenuItem();
        jbmi.setText("Top-Right to Bottom-Left");
        jbmi.setSelected(pushingLayout.getPositioner() instanceof TR2BL);
        jbmi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pushingLayout.setPositioner(new TR2BL());
                switchViews();
            }
        });
        popup.add(jbmi);
        jbmi = new JCheckBoxMenuItem();
        jbmi.setText("Right to Left");
        jbmi.setSelected(pushingLayout.getPositioner() instanceof R2L);
        jbmi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pushingLayout.setPositioner(new R2L());
                switchViews();
            }
        });
        popup.add(jbmi);
        jbmi = new JCheckBoxMenuItem();
        jbmi.setText("Bottom-Right to Top-Left");
        jbmi.setSelected(pushingLayout.getPositioner() instanceof BR2TL);
        jbmi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pushingLayout.setPositioner(new BR2TL());
                switchViews();
            }
        });
        popup.add(jbmi);
        jbmi = new JCheckBoxMenuItem();
        jbmi.setText("Bottom to Top");
        jbmi.setSelected(pushingLayout.getPositioner() instanceof B2T);
        jbmi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pushingLayout.setPositioner(new B2T());
                switchViews();
            }
        });
        popup.add(jbmi);
        jbmi = new JCheckBoxMenuItem();
        jbmi.setText("Bottom-Left to Top-Right");
        jbmi.setSelected(pushingLayout.getPositioner() instanceof BL2TR);
        jbmi.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                pushingLayout.setPositioner(new BL2TR());
                switchViews();
            }
        });
        popup.add(jbmi);
        popup.setInvoker(b);
        Point p = b.getLocationOnScreen();
        popup.setLocation(p.x, p.y + b.getHeight());
        popup.setVisible(true);
    }

    private void switchViews() {
        if (pushingLayout.isOn()) {
            pushingLayout.triggerOffComponent();
        } else {
            pushingLayout.triggerOnComponent();
        }
    }

    private void clearList() {
        songs.clear();
        DefaultListModel dlm = (DefaultListModel) mp3List.getModel();
        dlm.clear();
    }

    private JPanel south;

    private void saveList() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".m3u");
            }

            @Override
            public String getDescription() {
                return "M3U File";
            }
        });
        int ok = jfc.showSaveDialog(this);
        if (ok == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            saveList2(file);
            try {
                m3uURL = file.toURI().toURL();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveList2(File file) {
        try {
            FileWriter fos = new FileWriter(file);
            PrintWriter pw = new PrintWriter(fos);
            pw.println("#EXTM3U");
            synchronized (songs) {
                for (M3UEntry song : songs) {
                    pw.println(song.mp3File.getAbsolutePath());
                }
            }
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void enableDelete() {
        mp3List.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE) {
                    deleteSong();
                }
            }
        });
    }

    private void deleteSong() {
        Object[] values = mp3List.getSelectedValues();
        if (values != null && values.length > 0) {
            for (Object value : values) {
                if (value != null) {
                    songs.remove(value);
                    DefaultListModel dlm = (DefaultListModel) mp3List.getModel();
                    dlm.removeElement(value);
                }
            }
        }
    }

    private void enableDnD() {
        mp3List.setDragEnabled(true);
        mp3List.setTransferHandler(new Mp3ListTransferHandler(this));
        mp3List.setDropMode(DropMode.INSERT);
    }

    private void readList() {
        readSongList();
        DefaultListModel dlm = new DefaultListModel();
        for (M3UEntry song : songs) {
            dlm.addElement(song);
        }
        mp3List.setModel(dlm);
        enableCtrls();
    }

    private void enableCtrls() {
        btnPP.setEnabled(true);
        btnPrev.setEnabled(true);
        btnNext.setEnabled(true);
        slider.setEnabled(true);
    }

    public void readListFromDir(File dir) {
        songs = Collections.synchronizedList(new ArrayList<M3UEntry>());
        readSongsFromDir(dir, true);
        DefaultListModel dlm = new DefaultListModel();
        for (M3UEntry song : songs) {
            dlm.addElement(song);
        }
        mp3List.setModel(dlm);
        enableCtrls();
    }

    private void readSongsFromDir(File dir, boolean recursive) {
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isFile() && file.getName().toLowerCase().endsWith(".mp3")) {
                M3UEntry entry = new M3UEntry(file);
                songs.add(entry);
            } else if (file.isDirectory() && recursive) {
                readSongsFromDir(file, true);
            }
        }
    }

    private void readSongList() {
        songs = Collections.synchronizedList(new ArrayList<M3UEntry>());
        BufferedReader br = null;
        try {
            InputStream in = m3uURL.openStream();
            br = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equals("")) continue;
                if (line.startsWith("#EXTM3U")) {
                    continue;
                } else if (line.startsWith("#EXTINF")) {
                    continue;
                } else {
                    File file = new File(line);
                    M3UEntry entry = new M3UEntry(file);
                    songs.add(entry);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void createContextAction(JPopupMenu popup) {
        popup.add(new AbstractAction("Start Play") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                startPlay();
            }
        });
        popup.add(new AbstractAction("Stop Play") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                stopPlay();
            }
        });
        popup.add(new AbstractAction("Pause Play") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                pausePlay();
            }
        });
        popup.add(new AbstractAction("Open play list...") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                loadList();
            }
        });
        popup.add(new AbstractAction("Open a directory...") {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                openDir();
            }
        });
        popup.addSeparator();
    }

    private void loadList() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.getName().endsWith(".m3u");
            }

            @Override
            public String getDescription() {
                return "M3U File";
            }
        });
        int ok = jfc.showOpenDialog(this);
        if (ok == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            try {
                m3uURL = file.toURI().toURL();
                readList();
                jump2(0);
            } catch (MalformedURLException e) {
            }
        }
    }

    private void openDir() {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int ok = jfc.showOpenDialog(this);
        if (ok == JFileChooser.APPROVE_OPTION) {
            File file = jfc.getSelectedFile();
            readListFromDir(file);
            jump2(0);
        }
    }

    @Override
    public void setInitializationData(IConfigurationElement config, String propertyName, Object data) throws CoreException {
        IConfiguration pref = Configurator.getDefaultConfigurator().getConfig(CONFIG_ID);
        String m3u = pref.getString("m3u");
        if (m3u != null) {
            try {
                m3uURL = new URL(m3u);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            IConfigurationElement[] children = config.getChildren("parameter");
            for (IConfigurationElement child : children) {
                if ("m3u".equals(child.getAttribute("name"))) {
                    try {
                        m3uURL = new URL(child.getAttribute("value"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public boolean isMouseAware(Component c) {
        if (c == mp3JSP) return true;
        if (c == north) return true;
        if (c == lblCaption) return true;
        if (c == south) return true;
        if (c == lyricPane) return true;
        return false;
    }

    public List<M3UEntry> getSongs() {
        if (songs == null) {
            songs = Collections.synchronizedList(new ArrayList<M3UEntry>());
        }
        return songs;
    }

    public JList getList() {
        return mp3List;
    }

    public JLabel getCaption() {
        return lblCaption;
    }

    public JSlider getSlider() {
        return slider;
    }

    public LyricPane getLyricPane() {
        return this.lyricPane;
    }

    @Override
    public void addWidgetListener(IWidgetListener l) {
    }

    @Override
    public void removeWidgetListener(IWidgetListener l) {
    }

    @Override
    public void onExit() {
        if (m3uURL != null) {
            IConfiguration config = Configurator.getDefaultConfigurator().getConfig(CONFIG_ID);
            String url = m3uURL.toExternalForm();
            if (url != null) config.setString("m3u", url);
        }
    }
}
