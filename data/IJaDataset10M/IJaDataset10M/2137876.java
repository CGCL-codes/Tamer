package com.hadeslee.yoyoplayer.player.ui;

import com.hadeslee.yoyoplayer.equalizer.EqualizerUI;
import com.hadeslee.yoyoplayer.playlist.PlayListUI;
import com.hadeslee.yoyoplayer.lyric.Lyric;
import com.hadeslee.yoyoplayer.lyric.LyricUI;
import com.hadeslee.yoyoplayer.player.BasicController;
import com.hadeslee.yoyoplayer.player.BasicPlayerEvent;
import com.hadeslee.yoyoplayer.player.BasicPlayerException;
import com.hadeslee.yoyoplayer.player.BasicPlayerListener;
import com.hadeslee.yoyoplayer.util.AudioChart;
import com.hadeslee.yoyoplayer.util.Config;
import com.hadeslee.yoyoplayer.util.Loader;
import com.hadeslee.yoyoplayer.playlist.PlayList;
import com.hadeslee.yoyoplayer.playlist.PlayListItem;
import com.hadeslee.yoyoplayer.setting.AudioChartPanel;
import com.hadeslee.yoyoplayer.setting.OptionDialog;
import com.hadeslee.yoyoplayer.tag.SongInfoDialog;
import com.hadeslee.yoyoplayer.util.FileNameFilter;
import com.hadeslee.yoyoplayer.util.Playerable;
import com.hadeslee.yoyoplayer.util.SongInfo;
import com.hadeslee.yoyoplayer.util.Util;
import com.hadeslee.yoyoplayer.util.YOYOSlider;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.SourceDataLine;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author hadeslee
 */
public class PlayerUI extends JPanel implements Playerable, ActionListener, ChangeListener, BasicPlayerListener {

    private static final long serialVersionUID = 20071214L;

    public static final int INIT = 0;

    public static final int OPEN = 1;

    public static final int PLAY = 2;

    public static final int PAUSE = 3;

    public static final int STOP = 4;

    private static Logger log = Logger.getLogger(PlayerUI.class.getName());

    private JButton pre, next, play, stop;

    private JButton close, min;

    private JLabel about;

    private JLabel state, channel;

    protected JToggleButton pl, lrc, eq, speaker;

    private YOYOSlider pos, pan, volume;

    private AudioChart audioChart;

    private Lyric lyric;

    private BasicController player;

    private Map audioInfo;

    private int playerState;

    private Config config;

    private long lastScrollTime;

    private boolean posValueJump;

    private PlayListItem currentItem;

    private long secondsAmount;

    private Loader loader;

    private PlayList playlist;

    private String currentSongName;

    private String currentFileOrURL;

    private boolean currentIsFile;

    private String titleText;

    private PlayListUI playlistUI;

    private EqualizerUI equalizerUI;

    private LyricUI lyricUI;

    private SongInfo songInfo;

    private boolean posDragging;

    private double posValue;

    private TimePanel timePanel;

    private SongInfoPanel infoPanel;

    private Image[] playImgs, pauseImgs;

    private String currentState;

    private boolean isSeeked;

    private double lastRate;

    private final Object lock = new Object();

    private volatile boolean scrollTitle;

    private String title = Config.NAME;

    private Thread thread;

    private long seekedTime;

    public PlayerUI() {
        super(null);
        setPreferredSize(new Dimension(285, 155));
    }

    /**
     * 得到媒体当前的时间，以毫秒为单位
     * @return 时间
     */
    public long getTime() {
        if (player == null) {
            return -1;
        } else {
            return player.getMicrosecondPosition() / 1000 + seekedTime;
        }
    }

    void setLastRate(double rate) {
        this.lastRate = rate;
    }

    public void setPlayList(PlayList playlist) {
        this.playlist = playlist;
    }

    public AudioChart getAudioChart() {
        return audioChart;
    }

    public boolean loadPlaylist() {
        boolean loaded = false;
        String lastPlay = config.getCurrentFileOrUrl();
        log.log(Level.INFO, "lastPlay=" + lastPlay);
        if (lastPlay != null) {
            for (PlayListItem item : playlist.getAllItems()) {
                if (item.getLocation().equals(lastPlay)) {
                    log.log(Level.INFO, "找到了最后要播的匹配!!");
                    this.setCurrentSong(item);
                    break;
                }
            }
        }
        return loaded;
    }

    public void processJumpToFile(int modifiers) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void processPreferences(int modifiers) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void pressStart() {
        play.doClick();
    }

    public void pressEq() {
        eq.doClick();
    }

    public void pressLrc() {
        lrc.doClick();
    }

    public void pressPl() {
        pl.doClick();
    }

    /**
     * 给此面板本身以及所有的子组件都添加上这个
     * 监听器
     */
    private void addMouseVolumeListener() {
        MouseVolumeListener mv = new MouseVolumeListener();
        this.addMouseWheelListener(mv);
        int count = this.getComponentCount();
        for (int i = 0; i < count; i++) {
            this.getComponent(i).addMouseWheelListener(mv);
        }
    }

    private void initUI() {
        eq = Util.createJToggleButton("player/eq", config.isShowEq() ? Config.EQ_OFF : Config.EQ_ON, this, config.isShowEq());
        pl = Util.createJToggleButton("player/pl", config.isShowPlayList() ? Config.PL_OFF : Config.PL_ON, this, config.isShowPlayList());
        lrc = Util.createJToggleButton("player/lrc", config.isShowLrc() ? Config.LRC_OFF : Config.LRC_ON, this, config.isShowLrc());
        speaker = Util.createJToggleButton("player/speaker", config.isMute() ? Config.VOL_ON : Config.VOL_OFF, this, config.isMute());
        pre = Util.createJButton("player/pre", Config.PREVIOUS, this);
        next = Util.createJButton("player/next", Config.NEXT, this);
        play = Util.createJButton("player/play", Config.PLAY, this);
        stop = Util.createJButton("player/stop", Config.STOP, this);
        close = Util.createJButton("player/close", Config.CLOSE, this);
        min = Util.createJButton("player/min", Config.MINIMIZE, this);
        playImgs = Util.getImages("player/play", 3);
        pauseImgs = Util.getImages("player/pause", 3);
        setButtonLocation(eq, 160, 94);
        setButtonLocation(pl, 201, 94);
        setButtonLocation(lrc, 241, 94);
        setButtonLocation(speaker, 28, 138);
        setButtonLocation(pre, 155, 134);
        setButtonLocation(play, 188, 134);
        setButtonLocation(stop, 218, 134);
        setButtonLocation(next, 249, 134);
        setButtonLocation(close, 275, 7);
        setButtonLocation(min, 260, 7);
        Image ball1 = Util.getImage("player/ball1.png");
        Image ball2 = Util.getImage("player/ball2.png");
        Image ball3 = Util.getImage("player/ball3.png");
        pos = Util.createSlider(0, Config.POSBARMAX, 0, ball1, ball2, ball3, Util.getImage("player/pos1.png"), Util.getImage("player/pos2.png"), this, SwingConstants.HORIZONTAL);
        pan = Util.createSlider(-Config.BALANCEMAX, Config.BALANCEMAX, 0, Util.getImage("player/panBall1.png"), Util.getImage("player/panBall2.png"), null, null, null, this, SwingConstants.HORIZONTAL);
        volume = Util.createSlider(0, Config.VOLUMEMAX, Config.VOLUMEMAX, ball1, ball2, ball3, Util.getImage("player/volume1.png"), Util.getImage("player/volume2.png"), this, SwingConstants.HORIZONTAL);
        pos.setBounds(10, 108, 270, 15);
        volume.setBounds(43, 130, 82, 15);
        pan.setBounds(160, 72, 90, 13);
        this.add(pos);
        this.add(volume);
        this.add(pan);
        pan.setValue(config.getPanValue());
        volume.setValue(config.getGainValue());
        audioChart = new AudioChart();
        audioChart.setDisplayMode(config.getAudioChartDisplayMode());
        audioChart.setSpectrumAnalyserBandCount(config.getAudioChartBarCount());
        audioChart.setPeakColor(config.getAudioChartPeakColor());
        audioChart.setScopeColor(config.getAudioChartlineColor());
        Color c3 = config.getAudioChartTopColor();
        Color c2 = config.getAudioChartCenterColor();
        Color c1 = config.getAudioChartbottomColor();
        audioChart.setSpectrumAnalyserColors(Util.getColors(c1, c2, c3, 256));
        audioChart.setSpectrumAnalyserDecay(0.05f);
        audioChart.setBounds(158, 30, 92, 37);
        this.add(audioChart);
        timePanel = new TimePanel();
        Dimension di = timePanel.getPreferredSize();
        timePanel.setBounds(15, 45, di.width, di.height);
        this.add(timePanel);
        state = new JLabel(Config.getResource("state.stop"));
        channel = new JLabel(Config.getResource("songinfo.channel.stereo"));
        channel.setForeground(Color.WHITE);
        state.setForeground(Color.WHITE);
        state.setBounds(94, 52, 50, 14);
        channel.setBounds(94, 37, 50, 14);
        this.add(state);
        this.add(channel);
        infoPanel = new SongInfoPanel();
        songInfo = new SongInfo();
        infoPanel.setInfo(songInfo);
        infoPanel.setBounds(17, 88, 117, 14);
        Image img = Util.getImage("logo.png");
        img = img.getScaledInstance(19, 19, Image.SCALE_SMOOTH);
        about = new JLabel(new ImageIcon(img));
        about.setBounds(256, 30, 19, 19);
        about.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent me) {
                showOptionDialog(me);
            }

            public void mouseEntered(MouseEvent me) {
                about.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            public void mouseExited(MouseEvent me) {
                about.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
            }
        });
        this.add(about);
        this.add(infoPanel);
        this.addMouseListener(new MouseAdapter() {

            public void mouseReleased(MouseEvent me) {
                if (me.getButton() == MouseEvent.BUTTON3) {
                    showOptionDialog(me);
                }
            }
        });
    }

    public Loader getLoader() {
        return loader;
    }

    /**
     * 用于显示关于的一系列菜单的方法
     * 供自己还有Main调用,因为Main里面
     * 注册了系统栏图标
     */
    public void showOptionDialog(MouseEvent me) {
        JPopupMenu pop = new JPopupMenu("Set");
        pop.add(new AbstractAction(Config.getResource("PlayerUI.option")) {

            public void actionPerformed(ActionEvent e) {
                JDialog jd = config.getOptionDialog();
                jd.setVisible(true);
            }
        });
        pop.addSeparator();
        pop.add(createPlayMenu());
        pop.add(createVolumeMenu());
        pop.add(createPlayModeMenu());
        pop.add(createPlayWhichMenu());
        pop.addSeparator();
        pop.add(createAudioChartMenu());
        pop.add(createLyricMenu());
        pop.add(createEqMenu());
        pop.addSeparator();
        pop.add(createViewMenu());
        pop.add(new AbstractAction(Config.getResource("PlayerUI.minimize")) {

            public void actionPerformed(ActionEvent e) {
                loader.minimize();
            }
        });
        pop.add(new AbstractAction(Config.getResource("PlayerUI.exit")) {

            public void actionPerformed(ActionEvent e) {
                closePlayer();
            }
        });
        pop.show(me.getComponent(), me.getX(), me.getY());
    }

    private JMenu createPlayMenu() {
        JMenu menu = new JMenu(Config.getResource("PlayerUI.playControl"));
        menu.add(new AbstractAction(playerState == PLAY ? Config.getResource("PlayerUI.pause") : Config.getResource("PlayerUI.play")) {

            public void actionPerformed(ActionEvent e) {
                if (playerState == PLAY) {
                    processPause(0);
                } else {
                    processPlay(0);
                }
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.stop")) {

            public void actionPerformed(ActionEvent e) {
                processStop(0);
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.ff5")) {

            public void actionPerformed(ActionEvent e) {
                if (currentItem == null) {
                    return;
                }
                int sec = timePanel.getSeconds();
                if (sec >= 5) {
                    setTime((sec - 5) * 1000);
                } else {
                    setTime(0);
                }
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.ss5")) {

            public void actionPerformed(ActionEvent e) {
                if (currentItem == null) {
                    return;
                }
                int sec = timePanel.getSeconds();
                if (sec + 5 < currentItem.getLength()) {
                    setTime((sec + 5) * 1000);
                }
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.pre")) {

            public void actionPerformed(ActionEvent e) {
                previousSong();
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.next")) {

            public void actionPerformed(ActionEvent e) {
                nextSong();
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.playFile")) {

            public void actionPerformed(ActionEvent e) {
                JFileChooser jf = Util.getFileChooser(new FileNameFilter(Config.EXTS, Config.getResource("playlist.filechooser.name"), true), JFileChooser.FILES_ONLY);
                int i = jf.showOpenDialog(config.getPlWindow());
                if (i == JFileChooser.APPROVE_OPTION) {
                    File f = jf.getSelectedFile();
                    PlayListItem item = new PlayListItem(Util.getSongName(f), f.getPath(), -1, true);
                    playlist.removeAllItems();
                    playlist.appendItem(item);
                    playlistUI.repaint();
                    playerState = PLAY;
                    setCurrentSong(item);
                }
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.playURL")) {

            public void actionPerformed(ActionEvent e) {
                String s = JOptionPane.showInputDialog(config.getTopParent(), Config.getResource("playlist.add.inputurl"));
                if (s != null) {
                    if (Config.startWithProtocol(s)) {
                        PlayListItem item = new PlayListItem(s, s, -1, false);
                        playlist.removeAllItems();
                        playlist.appendItem(item);
                        playlistUI.repaint();
                        playerState = PLAY;
                        setCurrentSong(item);
                    } else {
                        JOptionPane.showMessageDialog(config.getPlWindow(), Config.getResource("playlist.add.invalidUrl"));
                    }
                }
            }
        });
        return menu;
    }

    private JMenu createVolumeMenu() {
        JMenu menu = new JMenu(Config.getResource("PlayerUI.volumeControl"));
        menu.add(new AbstractAction(Config.getResource("PlayerUI.increase")) {

            public void actionPerformed(ActionEvent e) {
                int value = volume.getValue();
                volume.setValue(value + 10);
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.decrease")) {

            public void actionPerformed(ActionEvent e) {
                int value = volume.getValue();
                volume.setValue(value - 10);
            }
        });
        menu.addSeparator();
        JCheckBoxMenuItem mute = new JCheckBoxMenuItem(Config.getResource("PlayerUI.mute"), config.isMute());
        mute.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                JCheckBoxMenuItem check = (JCheckBoxMenuItem) ae.getSource();
                config.setMute(check.isSelected());
                changeSpeakerState();
            }
        });
        menu.add(mute);
        return menu;
    }

    private void changeSpeakerState() {
        if (config.isMute()) {
            speaker.setActionCommand(Config.VOL_ON);
            speaker.setSelected(true);
            try {
                player.setGain(0);
            } catch (BasicPlayerException ex) {
                Logger.getLogger(PlayerUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            speaker.setActionCommand(Config.VOL_OFF);
            speaker.setSelected(false);
            int gainValue = volume.getValue();
            int maxGain = volume.getMaximum();
            if (gainValue == 0) {
                try {
                    player.setGain(0);
                } catch (BasicPlayerException ex) {
                }
            } else {
                try {
                    player.setGain((double) gainValue / (double) maxGain);
                } catch (BasicPlayerException ex) {
                }
            }
        }
    }

    private JMenu createPlayModeMenu() {
        JMenu menu = new JMenu(Config.getResource("playlist.mode"));
        ButtonGroup bg1 = new ButtonGroup();
        ButtonGroup bg2 = new ButtonGroup();
        JRadioButtonMenuItem noCircle = new JRadioButtonMenuItem(Config.getResource("playlist.mode.noCircle"));
        noCircle.setSelected(!config.isRepeatEnabled());
        menu.add(noCircle).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                config.setRepeatEnabled(false);
            }
        });
        JRadioButtonMenuItem singleCircle = new JRadioButtonMenuItem(Config.getResource("playlist.mode.singleCircle"));
        singleCircle.setSelected(config.isRepeatEnabled() && config.getRepeatStrategy() == Config.REPEAT_ONE);
        menu.add(singleCircle).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                config.setRepeatEnabled(true);
                config.setRepeatStrategy(Config.REPEAT_ONE);
            }
        });
        JRadioButtonMenuItem allCircle = new JRadioButtonMenuItem(Config.getResource("playlist.mode.allCircle"));
        allCircle.setSelected(config.isRepeatEnabled() && config.getRepeatStrategy() == Config.REPEAT_ALL);
        menu.add(allCircle).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                config.setRepeatEnabled(true);
                config.setRepeatStrategy(Config.REPEAT_ALL);
            }
        });
        menu.addSeparator();
        JRadioButtonMenuItem orderPlay = new JRadioButtonMenuItem(Config.getResource("playlist.mode.orderPlay"));
        orderPlay.setSelected(config.getPlayStrategy() == Config.ORDER_PLAY);
        menu.add(orderPlay).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                config.setPlayStrategy(Config.ORDER_PLAY);
            }
        });
        JRadioButtonMenuItem randomPlay = new JRadioButtonMenuItem(Config.getResource("playlist.mode.randomPlay"));
        randomPlay.setSelected(config.getPlayStrategy() == Config.RANDOM_PLAY);
        menu.add(randomPlay).addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                config.setPlayStrategy(Config.RANDOM_PLAY);
            }
        });
        bg1.add(noCircle);
        bg1.add(singleCircle);
        bg1.add(allCircle);
        bg2.add(orderPlay);
        bg2.add(randomPlay);
        return menu;
    }

    private JMenu createPlayWhichMenu() {
        JMenu menu = new JMenu(Config.getResource("PlayerUI.playSong"));
        for (final PlayListItem item : playlist.getAllItems()) {
            menu.add(new AbstractAction(item.getFormattedName()) {

                public void actionPerformed(ActionEvent e) {
                    playerState = PLAY;
                    setCurrentSong(item);
                }
            });
        }
        return menu;
    }

    private JMenu createAudioChartMenu() {
        JMenu menu = new JMenu(Config.getResource("PlayerUI.audioChart"));
        menu.add(new AbstractAction(Config.getResource("PlayerUI.analyzing")) {

            public void actionPerformed(ActionEvent e) {
                audioChart.setDisplayMode(AudioChart.DISPLAY_MODE_SPECTRUM_ANALYSER);
                config.setAudioChartDisplayMode(AudioChart.DISPLAY_MODE_SPECTRUM_ANALYSER);
                audioChart.repaint();
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.line")) {

            public void actionPerformed(ActionEvent e) {
                audioChart.setDisplayMode(AudioChart.DISPLAY_MODE_SCOPE);
                config.setAudioChartDisplayMode(AudioChart.DISPLAY_MODE_SCOPE);
                audioChart.repaint();
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.noshow")) {

            public void actionPerformed(ActionEvent e) {
                audioChart.setDisplayMode(AudioChart.DISPLAY_MODE_OFF);
                config.setAudioChartDisplayMode(AudioChart.DISPLAY_MODE_OFF);
                audioChart.repaint();
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.optional")) {

            public void actionPerformed(ActionEvent e) {
                OptionDialog jd = config.getOptionDialog();
                jd.setSelected(Config.getResource("PlayerUI.view"));
                jd.setVisible(true);
            }
        });
        return menu;
    }

    private JMenu createLyricMenu() {
        JMenu menu = new JMenu(Config.getResource("PlayerUI.lrc"));
        Util.generateLyricMenu(menu, lyricUI.getLyricPanel());
        return menu;
    }

    private JMenu createEqMenu() {
        JMenu menu = new JMenu(Config.getResource("PlayerUI.eq"));
        menu.add(new AbstractAction(Config.getResource("PlayerUI.autoConfig")) {

            public void actionPerformed(ActionEvent e) {
                equalizerUI.getAutoButton().doClick();
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.onoff")) {

            public void actionPerformed(ActionEvent e) {
                equalizerUI.getOnOffButton().doClick();
            }
        });
        menu.addSeparator();
        JMenu selects = new JMenu(Config.getResource("PlayerUI.selectableTypes"));
        for (String s : EqualizerUI.presets) {
            JMenuItem item = new JMenuItem(Config.getResource(s));
            item.addActionListener(equalizerUI);
            item.setActionCommand(s);
            selects.add(item);
        }
        menu.add(selects);
        return menu;
    }

    private JMenu createViewMenu() {
        JMenu menu = new JMenu(Config.getResource("PlayerUI.viewType"));
        menu.add(new AbstractAction(Config.getResource("PlayerUI.lrcWindow")) {

            public void actionPerformed(ActionEvent e) {
                loader.toggleLyricWindow(!config.getLrcWindow().isVisible());
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.eqWindow")) {

            public void actionPerformed(ActionEvent e) {
                loader.toggleEqualizer(!config.getEqWindow().isVisible());
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.plWindow")) {

            public void actionPerformed(ActionEvent e) {
                loader.togglePlaylist(!config.getPlWindow().isVisible());
            }
        });
        menu.addSeparator();
        menu.add(new AbstractAction(Config.getResource("PlayerUI.fileProperty")) {

            public void actionPerformed(ActionEvent e) {
                if (currentItem != null) {
                    SongInfoDialog info = new SongInfoDialog(config.getTopParent(), true, currentItem);
                    info.setVisible(true);
                }
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.reOrder")) {

            public void actionPerformed(ActionEvent e) {
                loader.reRange();
            }
        });
        menu.add(new AbstractAction(Config.getResource("PlayerUI.alwaysOnTop")) {

            public void actionPerformed(ActionEvent e) {
            }
        });
        return menu;
    }

    public PopupMenu createPopupMenu() {
        PopupMenu pm;
        return null;
    }

    /**
     * 设置按钮的位置,不管这个按钮多大
     * @param ab 按钮
     * @param x 中心点的X
     * @param y 中心点的Y
     */
    private void setButtonLocation(AbstractButton ab, int x, int y) {
        int width = ab.getIcon().getIconWidth();
        int height = ab.getIcon().getIconHeight();
        ab.setBounds(x - width / 2, y - height / 2, width, height);
        this.add(ab);
    }

    /**
     * 加载界面
     */
    public void loadUI(Loader l, Config c) {
        this.loader = l;
        this.config = c;
        ImageBorder border = new ImageBorder();
        Image bg = Util.getImage("player/main.png");
        border.setImage(bg);
        this.setBorder(border);
        playlistUI = new PlayListUI();
        playlistUI.setPlayerUI(this);
        lyricUI = new LyricUI();
        lyricUI.setPlayer(this);
        equalizerUI = new EqualizerUI();
        equalizerUI.setPlayer(this);
        equalizerUI.loadUI();
        initUI();
        addMouseVolumeListener();
        loader.loaded();
        thread = new Thread() {

            public void run() {
                int index = 0;
                while (true) {
                    if (scrollTitle && config.isShowTitleInTaskBar()) {
                        if (index > title.length() - 1) {
                            index = 0;
                        }
                        String temp = title.substring(index) + title.substring(0, index);
                        loader.setTitle(temp);
                        index++;
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException ex) {
                        }
                    } else {
                        loader.setTitle(title);
                        synchronized (lock) {
                            try {
                                lock.wait();
                            } catch (InterruptedException ex) {
                                Logger.getLogger(PlayerUI.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            }
        };
        thread.start();
    }

    public void setShowTile(boolean b) {
        if (b) {
            synchronized (lock) {
                lock.notifyAll();
            }
        }
    }

    public LyricUI getLyricUI() {
        return lyricUI;
    }

    public EqualizerUI getEqualizerUI() {
        return equalizerUI;
    }

    public PlayListUI getPlaylistUI() {
        return playlistUI;
    }

    public synchronized void processStateUpdated(BasicPlayerEvent event) {
        log.log(Level.FINE, event.toString() + "\t,Time:" + System.nanoTime() + "\t,Thread:" + Thread.currentThread());
        int eventState = event.getCode();
        Object obj = event.getDescription();
        if (eventState == BasicPlayerEvent.EOM) {
            title = Config.NAME;
            if ((playerState == PAUSE) || (playerState == PLAY)) {
                if (config.isRepeatEnabled()) {
                    if (config.getRepeatStrategy() == Config.REPEAT_ALL) {
                        try {
                            Thread.sleep(config.getSequencePlayInterval() * 1000);
                            this.nextSong();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(PlayerUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else if (config.getRepeatStrategy() == Config.REPEAT_ONE) {
                        this.play();
                    }
                }
            }
        } else if (eventState == BasicPlayerEvent.PLAYING) {
            int sec = (int) currentItem.getLength();
            if (sec <= 0) {
                Long duration = (Long) audioInfo.get("duration");
                if (duration != null) {
                    sec = (int) (duration / 1000000);
                    currentItem.setDuration(sec);
                }
            }
            timePanel.reset(sec);
            infoPanel.reset(currentItem);
            lastScrollTime = System.currentTimeMillis();
            posValueJump = false;
            if (audioInfo.containsKey("basicplayer.sourcedataline")) {
                if (audioChart != null) {
                    audioChart.setupDSP((SourceDataLine) audioInfo.get("basicplayer.sourcedataline"));
                    audioChart.startDSP((SourceDataLine) audioInfo.get("basicplayer.sourcedataline"));
                }
            }
        } else if (eventState == BasicPlayerEvent.SEEKING) {
            posValueJump = true;
        } else if (eventState == BasicPlayerEvent.SEEKED) {
            try {
                player.setGain(((double) volume.getValue() / (double) volume.getMaximum()));
                player.setPan((float) pan.getValue() / 10.0f);
            } catch (BasicPlayerException e) {
            }
        } else if (eventState == BasicPlayerEvent.OPENING) {
            if ((obj instanceof URL) || (obj instanceof InputStream)) {
                showTitle(Config.getResource("title.buffering"));
            }
        } else if (eventState == BasicPlayerEvent.STOPPED) {
            if (audioChart != null) {
                audioChart.stopDSP();
                audioChart.repaint();
            }
        }
        changeStateTitle(eventState);
    }

    /**
     * 根据当前的状态改变标题的状态改变
     * @param state
     */
    private void changeStateTitle(int eventState) {
        switch(eventState) {
            case BasicPlayerEvent.EOM:
            case BasicPlayerEvent.STOPPED:
                showTitle(Config.getResource("state.stop"));
                currentState = Config.getResource("state.stop");
                scrollTitle = false;
                if (currentItem != null) {
                    title = currentItem.getFormattedName() + " - " + Config.NAME + "  ";
                }
                log.log(Level.FINE, "停止:" + System.nanoTime() + "Thread:" + Thread.currentThread());
                break;
            case BasicPlayerEvent.PLAYING:
            case BasicPlayerEvent.RESUMED:
                showTitle(Config.getResource("state.play"));
                currentState = Config.getResource("state.play");
                title = currentItem.getFormattedName() + " - " + Config.NAME + "  ";
                scrollTitle = true;
                log.log(Level.FINE, "播放:" + System.nanoTime() + "Thread:" + Thread.currentThread());
                synchronized (lock) {
                    lock.notifyAll();
                }
                break;
            case BasicPlayerEvent.PAUSED:
                showTitle(Config.getResource("state.pause"));
                currentState = Config.getResource("state.pause");
                title = currentItem.getFormattedName() + " - " + Config.NAME + "  ";
                scrollTitle = false;
                log.log(Level.FINE, "暂停:" + System.nanoTime() + "Thread:" + Thread.currentThread());
                break;
        }
    }

    private void showTitle(String title) {
        if (state != null) {
            state.setText(title);
        }
    }

    public long getTimeLengthEstimation(Map properties) {
        long milliseconds = -1;
        int byteslength = -1;
        if (properties != null) {
            if (properties.containsKey("audio.length.bytes")) {
                byteslength = ((Integer) properties.get("audio.length.bytes")).intValue();
            }
            if (properties.containsKey("duration")) {
                milliseconds = (int) (((Long) properties.get("duration")).longValue()) / 1000;
            } else {
                int bitspersample = -1;
                int channels = -1;
                float samplerate = -1.0f;
                int framesize = -1;
                if (properties.containsKey("audio.samplesize.bits")) {
                    bitspersample = ((Integer) properties.get("audio.samplesize.bits")).intValue();
                }
                if (properties.containsKey("audio.channels")) {
                    channels = ((Integer) properties.get("audio.channels")).intValue();
                }
                if (properties.containsKey("audio.samplerate.hz")) {
                    samplerate = ((Float) properties.get("audio.samplerate.hz")).floatValue();
                }
                if (properties.containsKey("audio.framesize.bytes")) {
                    framesize = ((Integer) properties.get("audio.framesize.bytes")).intValue();
                }
                if (bitspersample > 0) {
                    milliseconds = (int) (1000.0f * byteslength / (samplerate * channels * (bitspersample / 8)));
                } else {
                    milliseconds = (int) (1000.0f * byteslength / (samplerate * framesize));
                }
            }
        }
        return milliseconds;
    }

    public void processProgress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
        int byteslength = -1;
        long total = -1;
        if (currentItem != null) {
            total = currentItem.getLength();
        }
        if (total <= 0) {
            total = (long) Math.round(getTimeLengthEstimation(audioInfo) / 1000);
            currentItem.setDuration(total);
            playlistUI.repaint();
        }
        if (total <= 0) {
            total = -1;
        }
        if (audioInfo.containsKey("basicplayer.sourcedataline")) {
            if (audioChart != null) {
                audioChart.writeDSP(pcmdata);
            }
        }
        if (audioInfo.containsKey("audio.length.bytes")) {
            byteslength = ((Integer) audioInfo.get("audio.length.bytes")).intValue();
        }
        float progress = -1.0f;
        if ((bytesread > 0) && ((byteslength > 0))) {
            progress = bytesread * 1.0f / byteslength * 1.0f;
            config.setLastRate(progress);
        }
        if (audioInfo.containsKey("audio.type")) {
            String audioformat = (String) audioInfo.get("audio.type");
            if (audioformat.equalsIgnoreCase("mp3")) {
                if (properties.containsKey("mp3.shoutcast.metadata.StreamTitle")) {
                    String shoutTitle = ((String) properties.get("mp3.shoutcast.metadata.StreamTitle")).trim();
                    if (shoutTitle.length() > 0) {
                        if (currentItem != null) {
                            String sTitle = " (" + currentItem.getFormattedDisplayName() + ")";
                            if (!currentItem.getFormattedName().equals(shoutTitle + sTitle)) {
                                currentItem.setFormattedDisplayName(shoutTitle + sTitle);
                            }
                        }
                    }
                }
                if (properties.containsKey("mp3.equalizer")) {
                    equalizerUI.setBands((float[]) properties.get("mp3.equalizer"));
                }
                if (total > 0) {
                    secondsAmount = (long) (total * progress);
                } else {
                    secondsAmount = -1;
                }
            } else if (audioformat.equalsIgnoreCase("wave")) {
                secondsAmount = (long) (total * progress);
            } else {
                secondsAmount = (long) Math.round(microseconds / 1000000);
                equalizerUI.setBands(null);
            }
        } else {
            secondsAmount = (long) Math.round(microseconds / 1000000);
            equalizerUI.setBands(null);
        }
        if (secondsAmount < 0) {
            secondsAmount = (long) Math.round(microseconds / 1000000);
        }
        int seconds = (int) secondsAmount;
        timePanel.setTime(seconds);
        if (total != 0) {
            if (posValueJump == false) {
                int pValue = (Math.round(secondsAmount * Config.POSBARMAX / total));
                pos.setValue(pValue);
            }
        } else {
            pos.setValue(0);
        }
        long ctime = System.currentTimeMillis();
        long lctime = lastScrollTime;
    }

    public void setLyric(Lyric ly) {
        this.lyric = ly;
        if (currentItem != null) {
            currentItem.setLyricFile(ly.getLyricFile());
        }
    }

    public void setTime(long time) {
        if (time < 0) {
            return;
        }
        if (currentItem != null) {
            long length = currentItem.getLength() * 1000;
            if (length < 0) {
                return;
            }
            double rate = time * 1.0 / length;
            processSeek(rate);
        }
    }

    public void saveConfig() {
    }

    public void readConfig() {
    }

    public void setShowLyric(boolean b) {
        lrc.setSelected(b);
        lrc.setActionCommand(b ? Config.LRC_OFF : Config.LRC_ON);
        loader.toggleLyricWindow(b);
    }

    public JFrame getTopParent() {
        return config.getTopParent();
    }

    public void play() {
        processPlay(0);
    }

    public void pause() {
        processPause(0);
    }

    public void stop() {
        processStop(0);
    }

    public void nextSong() {
        processNext(0);
    }

    public void previousSong() {
        processPrevious(0);
    }

    public void actionPerformed(ActionEvent e) {
        final ActionEvent evt = e;
        if (e.getActionCommand().equals(Config.PAUSE)) {
            processActionEvent(e);
        } else if ((e.getActionCommand().equals(Config.PLAY)) && (playerState == PAUSE)) {
            processActionEvent(e);
        } else if (e.getActionCommand().equals(Config.CLOSE)) {
            closePlayer();
        } else if (e.getActionCommand().equals(Config.MINIMIZE)) {
            loader.minimize();
        } else if (e.getActionCommand().equals(Config.SETTING)) {
            OptionDialog jd = new OptionDialog(config.getTopParent(), true);
            jd.setTitle("关于");
            jd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            jd.setVisible(true);
        } else {
            new Thread("PlayerUIActionEvent") {

                public void run() {
                    processActionEvent(evt);
                }
            }.start();
        }
    }

    public void processStop(int modifiers) {
        if ((playerState == PAUSE) || (playerState == PLAY)) {
            try {
                player.stop();
            } catch (BasicPlayerException e) {
            }
            playerState = STOP;
            secondsAmount = 0;
            pos.setValue(0);
            timePanel.reset();
            pos.setEnabled(false);
            changePlayPauseState(playerState);
        }
    }

    /**
     * 这里处理关闭播放器的方法,
     * 关闭的时候要保存目录的所有配置,然后再释放资源
     */
    protected void closePlayer() {
        log.log(Level.INFO, "调用了closePlayer");
        if ((playerState == PAUSE) || (playerState == PLAY)) {
            try {
                if (player != null) {
                    player.stop();
                }
            } catch (BasicPlayerException e) {
            }
        }
        config.setGainValue(volume.getValue());
        config.setPanValue(pan.getValue());
        loader.close();
    }

    public PlayListItem getCurrentItem() {
        return currentItem;
    }

    public void setPlayerState(int state) {
        this.playerState = state;
    }

    public void setCurrentSong(PlayListItem pli) {
        seekedTime = 0L;
        log.log(Level.INFO, "调用了setCurrentSong()" + Thread.currentThread());
        if (config.getReadTagInfoStrategy().equals(Config.READ_WHEN_PLAY)) {
            pli.getTagInfo();
        }
        currentItem = pli;
        playlistUI.setCurrentItem(pli);
        int playerStateMem = playerState;
        if ((playerState == PAUSE) || (playerState == PLAY)) {
            try {
                player.stop();
            } catch (BasicPlayerException e) {
            }
            playerState = STOP;
            secondsAmount = 0;
            timePanel.reset();
        }
        playerState = OPEN;
        if (pli != null) {
            pli.getTagInfo();
            infoPanel.repaint();
            currentSongName = pli.getFormattedName();
            currentFileOrURL = pli.getLocation();
            currentIsFile = pli.isFile();
        } else {
            if (config.isRepeatEnabled()) {
                if (playlist != null) {
                    if (playlist.getPlaylistSize() > 0) {
                        playlist.begin();
                        PlayListItem rpli = playlist.getCursor();
                        if (rpli != null) {
                            rpli.getTagInfo();
                            currentSongName = rpli.getFormattedName();
                            currentFileOrURL = rpli.getLocation();
                            currentIsFile = rpli.isFile();
                            currentItem = rpli;
                        }
                    } else {
                        currentSongName = Config.TITLETEXT;
                        currentFileOrURL = null;
                        currentIsFile = false;
                        currentItem = null;
                    }
                }
            } else {
                currentSongName = Config.TITLETEXT;
                currentFileOrURL = null;
                currentIsFile = false;
                currentItem = null;
            }
        }
        if (currentIsFile == true) {
            pos.setEnabled(true);
            pos.setHideThumb(false);
        } else {
            pos.setValue(0);
            pos.setEnabled(false);
            pos.setHideThumb(true);
        }
        config.setCurrentFileOrUrl(currentFileOrURL);
        titleText = currentSongName.toUpperCase();
        showMessage(titleText);
        playlist.setItemSelected(pli, playlist.getSelectedIndex());
        playlistUI.repaint();
        if ((playerStateMem == PLAY) || (playerStateMem == PAUSE)) {
            processPlay(MouseEvent.BUTTON1_MASK);
        }
    }

    /**
     * 在这里显示一些常用的东西
     * 比如改变标题,以及时间清0,还有,歌曲信息的改变
     * 比如比特率,单双声道等全部重置,因为一首新的歌曲要开始了
     * 即使不开始,在不播的时候清除这些也是应该的
     * @param titleText
     */
    public void showMessage(String titleText) {
    }

    /**
     * 处理播放的请求,这下就要分很多种情况了,哈哈
     * @param modifiers
     */
    protected void processPlay(int modifiers) {
        log.log(Level.INFO, "processPlay....... ");
        if (playlist.isModified()) {
            PlayListItem pli = playlist.getCursor();
            log.log(Level.INFO, "播放列表改了..." + pli);
            if (pli == null) {
                playlist.begin();
                pli = playlist.getCursor();
            }
            setCurrentSong(pli);
            playlist.setModified(false);
            playlistUI.repaint();
        }
        if (playerState == PAUSE) {
            try {
                player.resume();
            } catch (BasicPlayerException e) {
            }
            playerState = PLAY;
        } else if (playerState == PLAY) {
            try {
                player.stop();
            } catch (BasicPlayerException e) {
            }
            playerState = PLAY;
            secondsAmount = 0;
            timePanel.reset();
            if (currentFileOrURL != null) {
                try {
                    if (currentIsFile == true) {
                        player.open(new File(currentFileOrURL));
                    } else {
                        player.open(new URL(currentFileOrURL));
                    }
                    player.play();
                } catch (Exception ex) {
                    showMessage(Config.getResource("title.invalidfile"));
                }
            }
        } else if ((playerState == STOP) || (playerState == OPEN)) {
            try {
                player.stop();
            } catch (BasicPlayerException e) {
            }
            if (currentFileOrURL != null) {
                try {
                    if (currentIsFile == true) {
                        player.open(new File(currentFileOrURL));
                    } else {
                        player.open(new URL(currentFileOrURL));
                    }
                    player.play();
                    lyric = new Lyric(currentItem);
                    lyricUI.setLyric(lyric);
                    titleText = currentSongName.toUpperCase();
                    int bitRate = -1;
                    if (currentItem != null) {
                        bitRate = currentItem.getBitrate();
                    }
                    if ((bitRate <= 0) && (audioInfo.containsKey("bitrate"))) {
                        bitRate = ((Integer) audioInfo.get("bitrate")).intValue();
                    }
                    if ((bitRate <= 0) && (audioInfo.containsKey("audio.framerate.fps")) && (audioInfo.containsKey("audio.framesize.bytes"))) {
                        float FR = ((Float) audioInfo.get("audio.framerate.fps")).floatValue();
                        int FS = ((Integer) audioInfo.get("audio.framesize.bytes")).intValue();
                        bitRate = Math.round(FS * FR * 8);
                    }
                    int channels = -1;
                    if (currentItem != null) {
                        channels = currentItem.getChannels();
                    }
                    if ((channels <= 0) && (audioInfo.containsKey("audio.channels"))) {
                        channels = ((Integer) audioInfo.get("audio.channels")).intValue();
                    }
                    float sampleRate = -1.0f;
                    if (currentItem != null) {
                        sampleRate = currentItem.getSamplerate();
                    }
                    if ((sampleRate <= 0) && (audioInfo.containsKey("audio.samplerate.hz"))) {
                        sampleRate = ((Float) audioInfo.get("audio.samplerate.hz")).floatValue();
                    }
                    long lenghtInSecond = -1L;
                    if (currentItem != null) {
                        lenghtInSecond = currentItem.getLength();
                    }
                    if ((lenghtInSecond <= 0) && (audioInfo.containsKey("duration"))) {
                        lenghtInSecond = ((Long) audioInfo.get("duration")).longValue() / 1000000;
                    }
                    if ((lenghtInSecond <= 0) && (audioInfo.containsKey("audio.length.bytes"))) {
                        lenghtInSecond = (long) Math.round(getTimeLengthEstimation(audioInfo) / 1000);
                        if (lenghtInSecond > 0) {
                            int minutes = (int) Math.floor(lenghtInSecond / 60);
                            int hours = (int) Math.floor(minutes / 60);
                            minutes = minutes - hours * 60;
                            int seconds = (int) (lenghtInSecond - minutes * 60 - hours * 3600);
                            if (seconds >= 10) {
                                titleText = "(" + minutes + ":" + seconds + ") " + titleText;
                            } else {
                                titleText = "(" + minutes + ":0" + seconds + ") " + titleText;
                            }
                        }
                    }
                    bitRate = Math.round((bitRate / 1000));
                    currentItem.setSampled(String.valueOf(Math.round((sampleRate / 1000))) + "kHz");
                    if (bitRate > 999) {
                        bitRate = (bitRate / 100);
                        currentItem.setBitRate(bitRate + "Hkbps");
                    } else {
                        currentItem.setBitRate(String.valueOf(bitRate) + "kbps");
                    }
                    if (channels == 2) {
                        currentItem.setChannels(Config.getResource("songinfo.channel.stereo"));
                    } else if (channels == 1) {
                        currentItem.setChannels(Config.getResource("songinfo.channel.mono"));
                    }
                } catch (BasicPlayerException bpe) {
                    showMessage(Config.getResource("title.invalidfile"));
                } catch (MalformedURLException mue) {
                    showMessage(Config.getResource("title.invalidfile"));
                }
                try {
                    if (config.isMute()) {
                        player.setGain(0);
                    } else {
                        player.setGain(((double) volume.getValue() / (double) volume.getMaximum()));
                    }
                    player.setPan((float) pan.getValue() / 10.0f);
                } catch (BasicPlayerException e) {
                }
                playerState = PLAY;
            }
        }
        changePlayPauseState(playerState);
        pos.setEnabled(true);
        if (!isSeeked && config.isAutoPlayWhenStart() && config.isMaintainLastPlay()) {
            isSeeked = true;
            processSeek(lastRate);
        }
    }

    /**
     *根据播放或者暂停来改变那个按钮的图标
     * @param state 状态
     */
    private void changePlayPauseState(int state) {
        if (state == PLAY) {
            play.setActionCommand(Config.PAUSE);
            play.setIcon(new ImageIcon(pauseImgs[0]));
            play.setRolloverIcon(new ImageIcon(pauseImgs[1]));
            play.setPressedIcon(new ImageIcon(pauseImgs[2]));
        } else if (state == PAUSE || state == STOP) {
            play.setActionCommand(Config.PLAY);
            play.setIcon(new ImageIcon(playImgs[0]));
            play.setRolloverIcon(new ImageIcon(playImgs[1]));
            play.setPressedIcon(new ImageIcon(playImgs[2]));
        }
    }

    public void processPause(int modifiers) {
        if (playerState == PLAY) {
            try {
                player.pause();
            } catch (BasicPlayerException e) {
            }
            playerState = PAUSE;
            changePlayPauseState(playerState);
        } else if (playerState == PAUSE) {
            try {
                player.resume();
            } catch (BasicPlayerException e) {
            }
            playerState = PLAY;
            changePlayPauseState(playerState);
        }
    }

    public void processNext(int modifiers) {
        playlist.nextCursor();
        playlistUI.repaint();
        PlayListItem pli = playlist.getCursor();
        setCurrentSong(pli);
    }

    public void processPrevious(int modifiers) {
        playlist.previousCursor();
        playlistUI.repaint();
        PlayListItem pli = playlist.getCursor();
        setCurrentSong(pli);
    }

    public void processActionEvent(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (false) {
        } else if (cmd.equalsIgnoreCase(Config.EQ_ON)) {
            loader.toggleEqualizer(true);
            eq.setActionCommand(Config.EQ_OFF);
        } else if (cmd.equalsIgnoreCase(Config.EQ_OFF)) {
            loader.toggleEqualizer(false);
            eq.setActionCommand(Config.EQ_ON);
        } else if (cmd.equalsIgnoreCase(Config.PL_ON)) {
            loader.togglePlaylist(true);
            pl.setActionCommand(Config.PL_OFF);
        } else if (cmd.equalsIgnoreCase(Config.PL_OFF)) {
            loader.togglePlaylist(false);
            pl.setActionCommand(Config.PL_ON);
        } else if (cmd.equals(Config.LRC_ON)) {
            loader.toggleLyricWindow(true);
            lrc.setActionCommand(Config.LRC_OFF);
        } else if (cmd.equals(Config.LRC_OFF)) {
            loader.toggleLyricWindow(false);
            lrc.setActionCommand(Config.LRC_ON);
        } else if (cmd.equals(Config.VOL_ON)) {
            config.setMute(false);
            changeSpeakerState();
        } else if (cmd.equals(Config.VOL_OFF)) {
            config.setMute(true);
            changeSpeakerState();
        } else if (cmd.equals(Config.STOP)) {
            processStop(e.getModifiers());
        } else if (cmd.equals(Config.PLAY)) {
            processPlay(e.getModifiers());
        } else if (cmd.equals(Config.PAUSE)) {
            processPause(e.getModifiers());
        } else if (cmd.equals(Config.STOP)) {
            processStop(e.getModifiers());
        } else if (cmd.equals(Config.NEXT)) {
            processNext(e.getModifiers());
        } else if (cmd.equals(Config.PREVIOUS)) {
            processPrevious(e.getModifiers());
        }
    }

    @SuppressWarnings("static-access")
    public void stateChanged(ChangeEvent e) {
        Object src = e.getSource();
        if (src == volume) {
            Object[] args = { String.valueOf(volume.getValue()) };
            String volumeText = MessageFormat.format(config.getResource("slider.volume.text"), args);
            if (volume.getValueIsAdjusting()) {
                showTitle(volumeText);
            } else {
                showTitle(currentState);
            }
            try {
                int gainValue = volume.getValue();
                int maxGain = volume.getMaximum();
                config.setVolume(gainValue);
                if (config.isMute()) {
                    return;
                }
                if (gainValue == 0) {
                    player.setGain(0);
                } else {
                    player.setGain(((double) gainValue / (double) maxGain));
                }
            } catch (Exception ex) {
            }
        } else if (src == pan) {
            Object[] args = { String.valueOf(Math.abs(pan.getValue() * 10 / Config.BALANCEMAX)) };
            String balanceText = null;
            if (pan.getValue() > 0) {
                balanceText = MessageFormat.format(Config.getResource("slider.balance.text.right"), args);
            } else if (pan.getValue() < 0) {
                balanceText = MessageFormat.format(Config.getResource("slider.balance.text.left"), args);
            } else {
                balanceText = MessageFormat.format(config.getResource("slider.balance.text.center"), args);
            }
            if (pan.getValueIsAdjusting()) {
                showTitle(balanceText);
            } else {
                showTitle(currentState);
            }
            try {
                float balanceValue = pan.getValue() * 1.0f / Config.BALANCEMAX;
                player.setPan(balanceValue);
            } catch (Exception ex) {
            }
        } else if (src == pos) {
            if (pos.getValueIsAdjusting() == false) {
                if (posDragging == true) {
                    posDragging = false;
                    posValue = pos.getValue() * 1.0 / Config.POSBARMAX;
                    processSeek(posValue);
                }
            } else {
                posDragging = true;
                posValueJump = true;
            }
        }
    }

    protected void processSeek(double rate) {
        try {
            if ((audioInfo != null) && (audioInfo.containsKey("audio.type"))) {
                String type = (String) audioInfo.get("audio.type");
                if ((type.equalsIgnoreCase("mp3")) && (audioInfo.containsKey("audio.length.bytes"))) {
                    long skipBytes = Math.round(((Integer) audioInfo.get("audio.length.bytes")).intValue() * rate);
                    player.seek(skipBytes);
                    long length = currentItem.getLength();
                    if (length > 0) {
                        seekedTime = (long) (length * 1000 * rate);
                    }
                } else if ((type.equalsIgnoreCase("wave")) && (audioInfo.containsKey("audio.length.bytes"))) {
                    long skipBytes = Math.round(((Integer) audioInfo.get("audio.length.bytes")).intValue() * rate);
                    player.seek(skipBytes);
                    long length = currentItem.getLength();
                    if (length > 0) {
                        seekedTime = (long) (length * 1000 * rate);
                    }
                } else {
                    posValueJump = false;
                }
            } else {
                posValueJump = false;
            }
        } catch (BasicPlayerException ioe) {
            posValueJump = false;
        }
    }

    public void opened(Object stream, Map properties) {
        audioInfo = properties;
    }

    public void progress(int bytesread, long microseconds, byte[] pcmdata, Map properties) {
        processProgress(bytesread, microseconds, pcmdata, properties);
    }

    public void stateUpdated(BasicPlayerEvent event) {
        processStateUpdated(event);
    }

    public void setController(BasicController controller) {
        this.player = controller;
    }

    private class MouseVolumeListener implements MouseWheelListener {

        public void mouseWheelMoved(MouseWheelEvent e) {
            int index = e.getUnitsToScroll();
            int value = volume.getValue();
            volume.setValue(value - index);
        }
    }
}
