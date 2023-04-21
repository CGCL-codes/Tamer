package com.hadeslee.yoyoplayer.util;

import com.hadeslee.yoyoplayer.lyric.*;
import com.hadeslee.yoyoplayer.player.ui.PlayerUI;
import com.hadeslee.yoyoplayer.playlist.PlayList;
import com.hadeslee.yoyoplayer.setting.OptionDialog;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 * 一个保存所有可配的信息的类，把所有可
 * 保存的信息放到一个对象里面，这样便于
 * 保存和读取
 * @author hadeslee
 */
public class Config implements Serializable {

    private static final long serialVersionUID = 20071127L;

    private static final Logger log = Logger.getLogger(Config.class.getName());

    public int voteOpenCount, voteOneHourCount;

    /*******************************************************************/
    public static final String NAME = "YOYOPlayer";

    public static final String EXTS = "snd,aifc,aif,wav,au,mp1,mp2,mp3,ogg,spx,flac,ape,mac";

    public static final File HOME = new File(System.getProperty("user.home") + File.separator + ".YOYOPlayer");

    public static final int POSBARMAX = 1000;

    public static final int VOLUMEMAX = 100;

    public static final int BALANCEMAX = 5;

    /*********************ActionCommand定义区**************************/
    public static final String PLAY = "play";

    public static final String PAUSE = "pause";

    public static final String STOP = "stop";

    public static final String PREVIOUS = "previous";

    public static final String NEXT = "next";

    public static final String CLOSE = "close";

    public static final String MINIMIZE = "minimize";

    public static final String SETTING = "setting";

    public static final String EQ_ON = "eq_on";

    public static final String EQ_OFF = "eq_off";

    public static final String LRC_ON = "lrc_on";

    public static final String LRC_OFF = "lrc_off";

    public static final String VOL_ON = "vol_on";

    public static final String VOL_OFF = "vol_off";

    public static final String PL_ON = "pl_on";

    public static final String PL_OFF = "pl_off";

    public static final String ABOUT = "about";

    public static final String EQ_ENABLE = "eq_enable";

    public static final String EQ_DISABLE = "eq_disable";

    public static final String EQ_AUTO_ENABLE = "eq_auto_enable";

    public static final String EQ_AUTO_DISABLE = "eq_auto_disable";

    public static final String EQ_PRESET = "eq_preset";

    /*******************检查更新策略常量定义区************************/
    public static final String CHECK_DAY = "check.day";

    public static final String CHECK_WEEK = "check.week";

    public static final String CHECK_MONTH = "check.month";

    public static final String CHECK_NONE = "check.none";

    /*******************读取文件标签策略常量定义区********************/
    public static final String READ_WHEN_ADD = "read.when.add";

    public static final String READ_WHEN_DISPLAY = "read.when.display";

    public static final String READ_WHEN_PLAY = "read.when.play";

    /*******************歌词对齐常量定义区***************************/
    public static final int LYRIC_LEFT_ALIGN = 1;

    public static final int LYRIC_RIGHT_ALIGN = 2;

    public static final int LYRIC_CENTER_ALIGN = 3;

    /*******************可视化区示波的消逝速度常量定义区**************/
    public static final int DISAPPEAR_QUICK = 1;

    public static final int DISAPPEAR_NORMAL = 2;

    public static final int DISAPPEAR_SLOW = 3;

    /*******************普通常量定义区*******************************/
    public static final int REPEAT_ONE = 100;

    public static final int REPEAT_ALL = 101;

    public static final String TITLETEXT = "YOYOPlayer";

    public static final String[] protocols = { "http:", "file:", "ftp:", "https:", "ftps:", "jar:" };

    public static final int ORDER_PLAY = 1;

    public static final int RANDOM_PLAY = 0;

    public static final int APEv2_ID3v2_ID3v1 = 1;

    public static final int ID3v2_APEv2_ID3v1 = 2;

    public static final int ID3v1_APEv2_ID3v2 = 3;

    public static final int ID3v1_ID3v2_APEv2 = 4;

    public static final int WRITEMODE_ID3v1 = 1;

    public static final int WRITEMODE_ID3v2 = 2;

    public static final int WRITEMODE_APEv2 = 4;

    public static final String LRC_WINDOW = "lrcWindow";

    public static final String PL_WINDOW = "plWindow";

    public static final String EQ_WINDOW = "eqWindow";

    public static final String MAIN_WINDOW = "mainWindow";

    public static final String UNKNOWN_WINDOW = "unknown";

    public static final int SNAP = 15;

    public static final int MOVE = 1;

    public static final int LEFT = 2;

    public static final int RIGHT = 3;

    public static final int TOP = 4;

    public static final int BOTTOM = 5;

    public static final int LEFT_TOP = 6;

    public static final int LEFT_BOTTOM = 7;

    public static final int RIGHT_TOP = 8;

    public static final int RIGHT_BOTTOM = 9;

    public static final int SHOWTIME_POSITIVE = 1;

    public static final int SHOWTIME_NEGATIVE = -1;

    /*******************************************************************/
    private static ResourceBundle rb = ResourceBundle.getBundle("com/hadeslee/yoyoplayer/util/UIInfo");

    private boolean showLrc = true;

    private boolean showPlayList = true;

    private boolean showEq = true;

    private boolean repeatEnabled = true;

    private boolean equalizerOn = true;

    private boolean equalizerAuto;

    private boolean shadow = false;

    private boolean isSnapEqWindow = true, isSnapLrcWindow = true, isSnapPlWindow = true;

    private boolean isLinux;

    private boolean useProxy;

    private boolean mute;

    private String audioDevice;

    private String playListFileName;

    private String currentFileOrUrl;

    private String lastDir;

    private String encoding = "GBK";

    private String proxyHost, proxyPort;

    private String proxyUserName, proxyPwd;

    private String currentPlayListName;

    private int repeatStrategy = REPEAT_ALL;

    private int gainValue = VOLUMEMAX;

    private int panValue = 0;

    private int playStrategy = ORDER_PLAY;

    private int xLocation = 300, yLocation = 100;

    private int bufferSize = 8;

    private int readTagOrder = APEv2_ID3v2_ID3v1;

    private int writeTagMode = WRITEMODE_ID3v1;

    private int showTimeStyle = SHOWTIME_POSITIVE;

    private int[] lastEqualizer;

    private Point eqLocation, lrcLocation, plLocation;

    private Point disLrc, disEq, disPl;

    private Dimension lrcSize, plSize;

    private Vector<PlayList> playlists;

    private Map<String, Set<String>> componentMap;

    private Date lastCheckUpdate = new Date();

    /*******************************************************************/
    private transient JFrame topParent;

    private transient JDialog lrcWindow;

    private transient JDialog plWindow;

    private transient JDialog eqWindow;

    private transient List mixers;

    private transient OptionDialog optionDialog;

    private transient PlayerUI player;

    /*******************************************************************/
    private boolean startAutoMinimize;

    private boolean showTrayIcon = true;

    private boolean showPlayTip;

    private boolean showTitleInTaskBar = true;

    private transient boolean autoShutDown;

    private transient Date shutDownTime;

    private String checkUpdateStrategy = CHECK_WEEK;

    private boolean autoCloseDialogWhenSave;

    private boolean miniHide;

    /*******************************************************************/
    private Vector<File> searchLyricDirs = new Vector<File>();

    private boolean autoSearchLyricOnline = true;

    private boolean searchWhenInfoFull;

    private boolean selectBestLyric = true;

    private boolean autoRelatingWithMediaFile = true;

    private boolean autoOverWriteExistFile;

    private boolean saveTheSameNameAsMediaFile;

    private File saveLyricDir = new File(HOME, "Lyrics");

    /*******************************************************************/
    private boolean autoPlayWhenStart;

    private boolean maintainLastPlay;

    private double lastRate;

    private int sequencePlayInterval;

    private boolean stopWhenError;

    /*******************************************************************/
    private boolean canDnD = true;

    private boolean disableDelete;

    private boolean savePlayListByAbsolutePath = true;

    private boolean ignoreBadFile;

    private boolean showTooltipOnPlayList = true;

    private String readTagInfoStrategy = READ_WHEN_DISPLAY;

    private Color playlistTitleColor = new Color(0, 128, 255);

    private Color playlistHiLightColor = new Color(0, 244, 245);

    private Color playlistIndexColor = new Color(0, 128, 0);

    private Color playlistLengthColor = new Color(192, 128, 32);

    private Color playlistSelectedColor = Color.WHITE;

    private Color playlistSelectedBG = new Color(46, 96, 184);

    private Color playlistBackground1 = new Color(32, 32, 32);

    private Color playlistBackground2 = new Color(0, 0, 0);

    private Font playlistFont = new Font("Dialog", Font.PLAIN, 12);

    /*******************************************************************/
    private int lpState = LyricPanel.V;

    private int lyricAlignMode = LYRIC_CENTER_ALIGN;

    private int H_SPACE = 10;

    private int V_SPACE = 0;

    private boolean lyricShadow = true;

    private boolean karaoke = true;

    private boolean transparency;

    private boolean showLrcBorder = true;

    private Color lyricHilight = new Color(0, 244, 245);

    private Color lyricForeground = new Color(100, 100, 100);

    private Color lyricBackground = new Color(6, 6, 6);

    private Font lyricFont = new Font("Dialog", Font.PLAIN, 14);

    private boolean autoLoadLyric = true;

    private boolean cutBlankChars;

    private boolean hideWhenNoLyric;

    private boolean lyricTopShow;

    private boolean autoResize = true;

    private boolean onlyResizeWhenVerticalMode = true;

    private boolean mouseDragToSeekEnabled = true;

    private boolean antiAliasing;

    private boolean mouseScrollAjustTime = true;

    private int refreshInterval = 80;

    /*******************************************************************/
    private int audioChartDisplayMode = AudioChart.DISPLAY_MODE_SPECTRUM_ANALYSER;

    private int audioChartfps = 25;

    private Color audioChartTopColor = Color.RED;

    private Color audioChartCenterColor = Color.YELLOW;

    private Color audioChartbottomColor = new Color(0, 255, 255);

    private Color audioChartPeakColor = Color.WHITE;

    private Color audioChartlineColor = new Color(0, 255, 255);

    private int audioChartDisappearSpeed = DISAPPEAR_NORMAL;

    private int audioChartBarCount = 20;

    private static Config config = new Config();

    static {
        load();
    }

    public boolean isMiniHide() {
        return miniHide;
    }

    public void setMiniHide(boolean miniHide) {
        this.miniHide = miniHide;
    }

    public Date getLastCheckUpdate() {
        return lastCheckUpdate;
    }

    public void setLastCheckUpdate(Date lastCheckUpdate) {
        this.lastCheckUpdate = lastCheckUpdate;
    }

    public boolean isAutoCloseDialogWhenSave() {
        return autoCloseDialogWhenSave;
    }

    public void setAutoCloseDialogWhenSave(boolean autoCloseDialogWhenSave) {
        this.autoCloseDialogWhenSave = autoCloseDialogWhenSave;
    }

    public int getPanValue() {
        return panValue;
    }

    public void setPanValue(int panValue) {
        this.panValue = panValue;
    }

    public int getAudioChartBarCount() {
        return audioChartBarCount;
    }

    public void setAudioChartBarCount(int audioChartBarCount) {
        this.audioChartBarCount = audioChartBarCount;
    }

    public synchronized OptionDialog getOptionDialog() {
        if (optionDialog == null) {
            optionDialog = new OptionDialog(config.getTopParent(), true);
            optionDialog.setTitle(Config.getResource("Config.settingOption"));
            optionDialog.setLocationRelativeTo(null);
        }
        return optionDialog;
    }

    public PlayerUI getPlayer() {
        return player;
    }

    public void setPlayer(PlayerUI player) {
        this.player = player;
    }

    public Color getPlaylistSelectedBG() {
        return playlistSelectedBG;
    }

    public void setPlaylistSelectedBG(Color playlistSelectedBG) {
        this.playlistSelectedBG = playlistSelectedBG;
    }

    public Color getAudioChartCenterColor() {
        return audioChartCenterColor;
    }

    public void setAudioChartCenterColor(Color audioChartCenterColor) {
        this.audioChartCenterColor = audioChartCenterColor;
    }

    public int getAudioChartDisappearSpeed() {
        return audioChartDisappearSpeed;
    }

    public void setAudioChartDisappearSpeed(int audioChartDisappearSpeed) {
        this.audioChartDisappearSpeed = audioChartDisappearSpeed;
    }

    public Color getAudioChartTopColor() {
        return audioChartTopColor;
    }

    public void setAudioChartTopColor(Color audioChartTopColor) {
        this.audioChartTopColor = audioChartTopColor;
    }

    public Color getAudioChartbottomColor() {
        return audioChartbottomColor;
    }

    public void setAudioChartbottomColor(Color audioChartbottomColor) {
        this.audioChartbottomColor = audioChartbottomColor;
    }

    public Color getAudioChartPeakColor() {
        return audioChartPeakColor;
    }

    public void setAudioChartPeakColor(Color audioChartcolor) {
        this.audioChartPeakColor = audioChartcolor;
    }

    public Color getAudioChartlineColor() {
        return audioChartlineColor;
    }

    public void setAudioChartlineColor(Color audioChartlineColor) {
        this.audioChartlineColor = audioChartlineColor;
    }

    public boolean isAutoLoadLyric() {
        return autoLoadLyric;
    }

    public void setAutoLoadLyric(boolean autoLoadLyric) {
        this.autoLoadLyric = autoLoadLyric;
    }

    public boolean isCutBlankChars() {
        return cutBlankChars;
    }

    public void setCutBlankChars(boolean cutBlankChars) {
        this.cutBlankChars = cutBlankChars;
    }

    public boolean isHideWhenNoLyric() {
        return hideWhenNoLyric;
    }

    public void setHideWhenNoLyric(boolean hideWhenNoLyric) {
        this.hideWhenNoLyric = hideWhenNoLyric;
    }

    public int getLyricAlignMode() {
        return lyricAlignMode;
    }

    public void setLyricAlignMode(int lyricAlignMode) {
        this.lyricAlignMode = lyricAlignMode;
    }

    public boolean isLyricShadow() {
        return lyricShadow;
    }

    public void setLyricShadow(boolean lyricShadow) {
        this.lyricShadow = lyricShadow;
    }

    public boolean isMouseDragToSeekEnabled() {
        return mouseDragToSeekEnabled;
    }

    public void setMouseDragToSeekEnabled(boolean mouseDragToSeekEnabled) {
        this.mouseDragToSeekEnabled = mouseDragToSeekEnabled;
    }

    public boolean isOnlyResizeWhenVerticalMode() {
        return onlyResizeWhenVerticalMode;
    }

    public void setOnlyResizeWhenVerticalMode(boolean onlyResizeWhenVerticalMode) {
        this.onlyResizeWhenVerticalMode = onlyResizeWhenVerticalMode;
    }

    public boolean isCanDnD() {
        return canDnD;
    }

    public void setCanDnD(boolean canDnD) {
        this.canDnD = canDnD;
    }

    public boolean isDisableDelete() {
        return disableDelete;
    }

    public void setDisableDelete(boolean disableDelete) {
        this.disableDelete = disableDelete;
    }

    public boolean isIgnoreBadFile() {
        return ignoreBadFile;
    }

    public void setIgnoreBadFile(boolean ignoreBadFile) {
        this.ignoreBadFile = ignoreBadFile;
    }

    public Color getPlaylistBackground1() {
        return playlistBackground1;
    }

    public void setPlaylistBackground1(Color playlistBackground1) {
        this.playlistBackground1 = playlistBackground1;
    }

    public Color getPlaylistBackground2() {
        return playlistBackground2;
    }

    public void setPlaylistBackground2(Color playlistBackground2) {
        this.playlistBackground2 = playlistBackground2;
    }

    public Font getPlaylistFont() {
        return playlistFont;
    }

    public void setPlaylistFont(Font playlistFont) {
        this.playlistFont = playlistFont;
    }

    public Color getPlaylistHiLightColor() {
        return playlistHiLightColor;
    }

    public void setPlaylistHiLightColor(Color playlistHiLightColor) {
        this.playlistHiLightColor = playlistHiLightColor;
    }

    public Color getPlaylistIndexColor() {
        return playlistIndexColor;
    }

    public void setPlaylistIndexColor(Color playlistIndexColor) {
        this.playlistIndexColor = playlistIndexColor;
    }

    public Color getPlaylistLengthColor() {
        return playlistLengthColor;
    }

    public void setPlaylistLengthColor(Color playlistLengthColor) {
        this.playlistLengthColor = playlistLengthColor;
    }

    public Color getPlaylistSelectedColor() {
        return playlistSelectedColor;
    }

    public void setPlaylistSelectedColor(Color playlistSelectedColor) {
        this.playlistSelectedColor = playlistSelectedColor;
    }

    public Color getPlaylistTitleColor() {
        return playlistTitleColor;
    }

    public void setPlaylistTitleColor(Color playlistTitleColor) {
        this.playlistTitleColor = playlistTitleColor;
    }

    public String getReadTagInfoStrategy() {
        return readTagInfoStrategy;
    }

    public void setReadTagInfoStrategy(String readTagInfoStrategy) {
        this.readTagInfoStrategy = readTagInfoStrategy;
    }

    public boolean isSavePlayListByAbsolutePath() {
        return savePlayListByAbsolutePath;
    }

    public void setSavePlayListByAbsolutePath(boolean savePlayListByAbsolutePath) {
        this.savePlayListByAbsolutePath = savePlayListByAbsolutePath;
    }

    public boolean isShowTooltipOnPlayList() {
        return showTooltipOnPlayList;
    }

    public void setShowTooltipOnPlayList(boolean showTooltipOnPlayList) {
        this.showTooltipOnPlayList = showTooltipOnPlayList;
    }

    public boolean isAutoPlayWhenStart() {
        return autoPlayWhenStart;
    }

    public void setAutoPlayWhenStart(boolean autoPlayWhenStart) {
        this.autoPlayWhenStart = autoPlayWhenStart;
    }

    public double getLastRate() {
        return lastRate;
    }

    public void setLastRate(double lastRate) {
        this.lastRate = lastRate;
    }

    public boolean isMaintainLastPlay() {
        return maintainLastPlay;
    }

    public void setMaintainLastPlay(boolean maintainLastPlay) {
        this.maintainLastPlay = maintainLastPlay;
    }

    public int getSequencePlayInterval() {
        return sequencePlayInterval;
    }

    public void setSequencePlayInterval(int sequencePlayInterval) {
        this.sequencePlayInterval = sequencePlayInterval;
    }

    public boolean isStopWhenError() {
        return stopWhenError;
    }

    public void setStopWhenError(boolean stopWhenError) {
        this.stopWhenError = stopWhenError;
    }

    public boolean isAutoOverWriteExistFile() {
        return autoOverWriteExistFile;
    }

    public void setAutoOverWriteExistFile(boolean autoOverWriteExistFile) {
        this.autoOverWriteExistFile = autoOverWriteExistFile;
    }

    public boolean isAutoRelatingWithMediaFile() {
        return autoRelatingWithMediaFile;
    }

    public void setAutoRelatingWithMediaFile(boolean autoRelatingWithMediaFile) {
        this.autoRelatingWithMediaFile = autoRelatingWithMediaFile;
    }

    public boolean isAutoSearchLyricOnline() {
        return autoSearchLyricOnline;
    }

    public void setAutoSearchLyricOnline(boolean autoSearchLyricOnline) {
        this.autoSearchLyricOnline = autoSearchLyricOnline;
    }

    public File getSaveLyricDir() {
        return saveLyricDir;
    }

    public void setSaveLyricDir(File saveLyricDir) {
        this.saveLyricDir = saveLyricDir;
    }

    public boolean isSaveTheSameNameAsMediaFile() {
        return saveTheSameNameAsMediaFile;
    }

    public void setSaveTheSameNameAsMediaFile(boolean saveTheSameNameAsMediaFile) {
        this.saveTheSameNameAsMediaFile = saveTheSameNameAsMediaFile;
    }

    public Vector<File> getSearchLyricDirs() {
        return searchLyricDirs;
    }

    public void setSearchLyricDirs(Vector<File> v) {
        this.searchLyricDirs = v;
    }

    public boolean isSearchWhenInfoFull() {
        return searchWhenInfoFull;
    }

    public void setSearchWhenInfoFull(boolean searchWhenInfoFull) {
        this.searchWhenInfoFull = searchWhenInfoFull;
    }

    public boolean isSelectBestLyric() {
        return selectBestLyric;
    }

    public void setSelectBestLyric(boolean selectBestLyric) {
        this.selectBestLyric = selectBestLyric;
    }

    public boolean isAutoShutDown() {
        return autoShutDown;
    }

    public void setAutoShutDown(boolean autoShutDown) {
        this.autoShutDown = autoShutDown;
    }

    public boolean isStartAutoMinimize() {
        return startAutoMinimize;
    }

    public void setStartAutoMinimize(boolean autoStartMini) {
        this.startAutoMinimize = autoStartMini;
    }

    public String getCheckUpdateStrategy() {
        return checkUpdateStrategy;
    }

    public void setCheckUpdateStrategy(String checkUpdateStrategy) {
        this.checkUpdateStrategy = checkUpdateStrategy;
    }

    public boolean isShowPlayTip() {
        return showPlayTip;
    }

    public void setShowPlayTip(boolean showPlayTip) {
        this.showPlayTip = showPlayTip;
    }

    public boolean isShowTitleInTaskBar() {
        return showTitleInTaskBar;
    }

    public void setShowTitleInTaskBar(boolean showTitleInTaskBar) {
        this.showTitleInTaskBar = showTitleInTaskBar;
    }

    public boolean isShowTrayIcon() {
        return showTrayIcon;
    }

    public void setShowTrayIcon(boolean showTrayIcon) {
        this.showTrayIcon = showTrayIcon;
    }

    public Date getShutDownTime() {
        return shutDownTime;
    }

    public void setShutDownTime(Date shutDownTime) {
        this.shutDownTime = shutDownTime;
    }

    public boolean isShowLrcBorder() {
        return showLrcBorder;
    }

    public void setShowLrcBorder(boolean showBorder) {
        this.showLrcBorder = showBorder;
    }

    public boolean isTransparency() {
        return transparency;
    }

    public void setTransparency(boolean transparency) {
        this.transparency = transparency;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public Map<String, Set<String>> getComponentMap() {
        return componentMap;
    }

    public int getAudioChartfps() {
        return audioChartfps;
    }

    public void setAudioChartfps(int audioChartfps) {
        this.audioChartfps = audioChartfps;
    }

    public int getAudioChartDisplayMode() {
        return audioChartDisplayMode;
    }

    public void setAudioChartDisplayMode(int audioChartDisplayMode) {
        this.audioChartDisplayMode = audioChartDisplayMode;
    }

    public boolean isAntiAliasing() {
        return antiAliasing;
    }

    public void setAntiAliasing(boolean antiAliasing) {
        this.antiAliasing = antiAliasing;
    }

    public boolean isLyricTopShow() {
        return lyricTopShow;
    }

    public void setLyricTopShow(boolean topShow) {
        this.lyricTopShow = topShow;
    }

    public boolean isMouseScrollAjustTime() {
        return mouseScrollAjustTime;
    }

    public void setMouseScrollAjustTime(boolean mouseScrollAjustTime) {
        this.mouseScrollAjustTime = mouseScrollAjustTime;
    }

    public int getShowTimeStyle() {
        return showTimeStyle;
    }

    public void setShowTimeStyle(int showTimeStyle) {
        this.showTimeStyle = showTimeStyle;
    }

    public Dimension getLrcSize() {
        return lrcSize;
    }

    public void setLrcSize(Dimension lrcSize) {
        this.lrcSize = lrcSize;
    }

    public Dimension getPlSize() {
        return plSize;
    }

    public void setPlSize(Dimension plSize) {
        this.plSize = plSize;
    }

    public boolean isKaraoke() {
        return karaoke;
    }

    public void setKaraoke(boolean karaoke) {
        this.karaoke = karaoke;
    }

    public int getReadTagOrder() {
        return readTagOrder;
    }

    public void setReadTagOrder(int readTagOrder) {
        this.readTagOrder = readTagOrder;
    }

    public int getWriteTagMode() {
        return writeTagMode;
    }

    public void setWriteTagMode(int writeTagMode) {
        this.writeTagMode = writeTagMode;
    }

    public List getMixers() {
        return mixers;
    }

    public void setMixers(List mixers) {
        this.mixers = mixers;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getProxyPwd() {
        return proxyPwd;
    }

    public void setProxyPwd(String proxyPwd) {
        this.proxyPwd = proxyPwd;
    }

    public String getProxyUserName() {
        return proxyUserName;
    }

    public void setProxyUserName(String proxyUserName) {
        this.proxyUserName = proxyUserName;
    }

    public boolean isUseProxy() {
        return useProxy;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void updateDistance() {
        disLrc = new Point(lrcWindow.getLocation().x - topParent.getLocation().x, lrcWindow.getLocation().y - topParent.getLocation().y);
        disEq = new Point(eqWindow.getLocation().x - topParent.getLocation().x, eqWindow.getLocation().y - topParent.getLocation().y);
        disPl = new Point(plWindow.getLocation().x - topParent.getLocation().x, plWindow.getLocation().y - topParent.getLocation().y);
    }

    public boolean isLinux() {
        return isLinux;
    }

    public Point getDisEq() {
        return disEq;
    }

    public Point getDisLrc() {
        return disLrc;
    }

    public Point getDisPl() {
        return disPl;
    }

    public String getCurrentPlayListName() {
        return currentPlayListName;
    }

    public void setCurrentPlayListName(String currentPlayListName) {
        this.currentPlayListName = currentPlayListName;
    }

    public JDialog getPlWindow() {
        return plWindow;
    }

    public void setPlWindow(JDialog plWindow) {
        this.plWindow = plWindow;
    }

    public JDialog getLrcWindow() {
        return lrcWindow;
    }

    public void setLrcWindow(JDialog lrcWindow) {
        this.lrcWindow = lrcWindow;
    }

    public JDialog getEqWindow() {
        return eqWindow;
    }

    public void setEqWindow(JDialog eqWindow) {
        this.eqWindow = eqWindow;
    }

    public boolean isShadow() {
        return shadow;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    public void addPlayList(PlayList list) {
        playlists.add(list);
    }

    public Vector<PlayList> getPlayLists() {
        return playlists;
    }

    public Point getEqLocation() {
        return eqLocation;
    }

    public void setEqLocation(Point eqLocation) {
        this.eqLocation = eqLocation;
    }

    public Point getLrcLocation() {
        return lrcLocation;
    }

    public void setLrcLocation(Point lrcLocation) {
        this.lrcLocation = lrcLocation;
    }

    public Point getPlLocation() {
        return plLocation;
    }

    public void setPlLocation(Point plLocation) {
        this.plLocation = plLocation;
    }

    public boolean isSnapEqWindow() {
        return isSnapEqWindow;
    }

    public void setIsSnapEqWindow(boolean isSnapEqWindow) {
        this.isSnapEqWindow = isSnapEqWindow;
    }

    public boolean isSnapLrcWindow() {
        return isSnapLrcWindow;
    }

    private void setSnapWindow(Component com, boolean snap) {
        if (com == this.lrcWindow) {
            this.setIsSnapLrcWindow(snap);
        } else if (com == this.plWindow) {
            this.setIsSnapPlWindow(snap);
        } else if (com == this.eqWindow) {
            this.setIsSnapEqWindow(snap);
        }
    }

    public String getComponentName(Component com) {
        if (com == this.lrcWindow) {
            return LRC_WINDOW;
        } else if (com == this.plWindow) {
            return PL_WINDOW;
        } else if (com == this.eqWindow) {
            return EQ_WINDOW;
        } else if (com == this.topParent) {
            return MAIN_WINDOW;
        } else {
            return UNKNOWN_WINDOW;
        }
    }

    private int getDirection(int dis, Rectangle myBound, Rectangle otherBound) {
        int x1 = (int) myBound.getCenterX();
        int y1 = (int) myBound.getCenterY();
        int x2 = (int) otherBound.getCenterX();
        int y2 = (int) otherBound.getCenterY();
        int abs = Math.abs(x1 - x2 - myBound.width / 2 - otherBound.width / 2 - dis);
        if (abs < 3) {
            return RIGHT;
        }
        abs = Math.abs(x2 - x1 - myBound.width / 2 - otherBound.width / 2 - dis);
        if (abs < 3) {
            return LEFT;
        }
        abs = Math.abs(y1 - y2 - myBound.height / 2 - otherBound.height / 2 - dis);
        if (abs < 3) {
            return BOTTOM;
        }
        abs = Math.abs(y2 - y1 - myBound.height / 2 - otherBound.height / 2 - dis);
        if (abs < 3) {
            return TOP;
        }
        return -1;
    }

    private void udpateComponentMapWithLoation() {
        List<Component> list = new ArrayList<Component>();
        Rectangle otherBound = new Rectangle();
        Rectangle myBound = new Rectangle();
        list.add(eqWindow);
        list.add(lrcWindow);
        list.add(topParent);
        list.add(plWindow);
        componentMap.clear();
        Component me = lrcWindow;
        me.getBounds(myBound);
        Set<String> set = new HashSet<String>();
        for (Component c1 : list) {
            if (c1 != null && c1 != me && c1.isShowing() && me.isShowing()) {
                c1.getBounds(otherBound);
                int dis = Util.getDistance(myBound, otherBound);
                if (Math.abs(dis) <= 3) {
                    set.add(getComponentName(c1));
                    if (c1 == topParent) {
                        break;
                    }
                }
            }
        }
        componentMap.put(getComponentName(me), set);
        set = new HashSet<String>();
        me = plWindow;
        me.getBounds(myBound);
        for (Component c1 : list) {
            if (c1 != null && c1 != me && c1.isShowing() && me.isShowing()) {
                c1.getBounds(otherBound);
                int dis = Util.getDistance(myBound, otherBound);
                if (Math.abs(dis) <= 3) {
                    set.add(getComponentName(c1));
                    if (c1 == topParent) {
                        break;
                    }
                }
            }
        }
        componentMap.put(getComponentName(me), set);
        set = new HashSet<String>();
        me = eqWindow;
        me.getBounds(myBound);
        for (Component c1 : list) {
            if (c1 != null && c1 != me && c1.isShowing() && me.isShowing()) {
                c1.getBounds(otherBound);
                int dis = Util.getDistance(myBound, otherBound);
                if (Math.abs(dis) <= 3) {
                    set.add(getComponentName(c1));
                    if (c1 == topParent) {
                        break;
                    }
                }
            }
        }
        componentMap.put(getComponentName(me), set);
    }

    /**
     * 更新组件的吸附状态
     */
    public void updateComponentSnap() {
        udpateComponentMapWithLoation();
        log.log(Level.CONFIG, componentMap.toString());
        String me = this.getComponentName(lrcWindow);
        Set<String> set = componentMap.get(me);
        Set<String> list = new HashSet<String>();
        list.add(me);
        boolean find = false;
        out: while (true) {
            if (set.size() == 0) {
                break out;
            } else {
                Set<String> temp = new HashSet<String>();
                for (String other : set) {
                    log.log(Level.FINEST, "me=" + me + ",other=" + other);
                    if (other.equals(MAIN_WINDOW)) {
                        find = true;
                        break out;
                    } else if (!list.contains(other)) {
                        temp.addAll(componentMap.get(other));
                    }
                    list.add(other);
                }
                set.removeAll(list);
                set.addAll(temp);
            }
        }
        this.setSnapWindow(lrcWindow, find);
        me = this.getComponentName(plWindow);
        list.clear();
        list.add(me);
        set = componentMap.get(me);
        find = false;
        out: while (true) {
            if (set.size() == 0) {
                break out;
            } else {
                Set<String> temp = new HashSet<String>();
                for (String other : set) {
                    log.log(Level.FINEST, "me=" + me + ",other=" + other);
                    if (other.equals(MAIN_WINDOW)) {
                        find = true;
                        break out;
                    } else if (!list.contains(other)) {
                        temp.addAll(componentMap.get(other));
                    }
                    list.add(other);
                }
                set.removeAll(list);
                set.addAll(temp);
            }
        }
        this.setSnapWindow(plWindow, find);
        me = this.getComponentName(eqWindow);
        list.clear();
        list.add(me);
        set = componentMap.get(me);
        find = false;
        out: while (true) {
            if (set.size() == 0) {
                break out;
            } else {
                Set<String> temp = new HashSet<String>();
                for (String other : set) {
                    log.log(Level.FINEST, "me=" + me + ",other=" + other);
                    if (other.equals(MAIN_WINDOW)) {
                        find = true;
                        break out;
                    } else if (!list.contains(other)) {
                        temp.addAll(componentMap.get(other));
                    }
                    list.add(other);
                }
                set.removeAll(list);
                set.addAll(temp);
            }
        }
        this.setSnapWindow(eqWindow, find);
    }

    public void setIsSnapLrcWindow(boolean isSnapLrcWindow) {
        this.isSnapLrcWindow = isSnapLrcWindow;
    }

    public boolean isSnapPlWindow() {
        return isSnapPlWindow;
    }

    public void setIsSnapPlWindow(boolean isSnapPlWindow) {
        this.isSnapPlWindow = isSnapPlWindow;
    }

    public int[] getLastEqualizer() {
        return lastEqualizer;
    }

    public int getXLocation() {
        return xLocation;
    }

    public int getYLocation() {
        return yLocation;
    }

    public String getAudioDevice() {
        return audioDevice;
    }

    public String getCurrentFileOrUrl() {
        return currentFileOrUrl;
    }

    public boolean isEqualizerOn() {
        return equalizerOn;
    }

    public static boolean load() {
        ObjectInputStream ois = null;
        try {
            if (!HOME.exists()) {
                HOME.mkdirs();
            }
            ois = new ObjectInputStream(new FileInputStream(new File(Config.HOME, NAME + ".dat")));
            config = (Config) ois.readObject();
            log.log(Level.INFO, Config.getResource("SongInfoDialog.loadConfigSuccess"));
            return true;
        } catch (Exception ex) {
            log.log(Level.SEVERE, Config.getResource("SongInfoDialog.loadConfigFailure"));
            return false;
        } finally {
            try {
                config.isLinux = System.getProperty("os.name").startsWith("Linux");
                ois.close();
            } catch (Exception ex) {
            }
        }
    }

    public static boolean load(String initConfig) {
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(initConfig));
            config = (Config) ois.readObject();
            ois.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                ois.close();
            } catch (Exception ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void save(String path) {
        try {
            FileOutputStream fout = new FileOutputStream(new File(path));
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(config);
            oos.flush();
            fout.close();
            log.log(Level.INFO, Config.getResource("SongInfoDialog.saveConfigSuccess"));
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public static void save() {
        try {
            if (!Config.HOME.exists()) {
                Config.HOME.mkdirs();
            }
            FileOutputStream fout = new FileOutputStream((new File(Config.HOME, NAME + ".dat")));
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(config);
            oos.flush();
            fout.close();
            log.log(Level.INFO, Config.getResource("SongInfoDialog.saveConfigSuccess"));
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public String getPlaylistFilename() {
        return null;
    }

    public void setCurrentFileOrUrl(String currentFileOrUrl) {
        this.currentFileOrUrl = currentFileOrUrl;
    }

    public int getGainValue() {
        return gainValue;
    }

    public void setEqualizerAuto(boolean b) {
        this.equalizerAuto = b;
    }

    public boolean isEqualizerAuto() {
        return equalizerAuto;
    }

    public void setEqualizerOn(boolean b) {
        this.equalizerOn = b;
    }

    public void setGainValue(int gainValue) {
        this.gainValue = gainValue;
    }

    public void setIconParent(ImageIcon jlguiIcon) {
    }

    public void setLastEqualizer(int[] gainValue) {
        this.lastEqualizer = gainValue;
    }

    public void setLocation(int x, int y) {
        this.xLocation = x;
        this.yLocation = y;
    }

    public String getPlayListFileName() {
        return playListFileName;
    }

    public void setPlayListFileName(String playListFileName) {
        this.playListFileName = playListFileName;
    }

    public int getPlayStrategy() {
        return playStrategy;
    }

    public void setPlayStrategy(int playStrategy) {
        this.playStrategy = playStrategy;
    }

    public static boolean startWithProtocol(String input) {
        boolean ret = false;
        if (input != null) {
            input = input.toLowerCase();
            for (int i = 0; i < protocols.length; i++) {
                if (input.startsWith(protocols[i])) {
                    ret = true;
                    break;
                }
            }
        }
        return ret;
    }

    public boolean isRepeatEnabled() {
        return repeatEnabled;
    }

    public void setRepeatEnabled(boolean b) {
        repeatEnabled = b;
    }

    public boolean isShowEq() {
        return showEq;
    }

    public void setAudioDevice(String mixerName) {
        this.audioDevice = mixerName;
    }

    public void setPlaylistFilename(String string) {
        this.playListFileName = string;
    }

    public void setShowEq(boolean showEq) {
        this.showEq = showEq;
    }

    public boolean isShowLrc() {
        return showLrc;
    }

    public void setShowLrc(boolean showLrc) {
        this.showLrc = showLrc;
    }

    public boolean isShowPlayList() {
        return showPlayList;
    }

    public void setShowPlayList(boolean showPlayList) {
        this.showPlayList = showPlayList;
    }

    public static Config getInstance() {
        return config;
    }

    public String getExtensions() {
        return null;
    }

    public static String getResource(String key) {
        return rb.getString(key);
    }

    public int getRepeatStrategy() {
        return repeatStrategy;
    }

    public void setRepeatStrategy(int repeatStrategy) {
        this.repeatStrategy = repeatStrategy;
    }

    public boolean isAutoResize() {
        return autoResize;
    }

    public void setAutoResize(boolean autoResize) {
        this.autoResize = autoResize;
    }

    public int getLpState() {
        return lpState;
    }

    public void setLpState(int lpState) {
        this.lpState = lpState;
    }

    public Color getLyricBackground() {
        return lyricBackground;
    }

    public void setLyricBackground(Color BACK_GROUND) {
        this.lyricBackground = BACK_GROUND;
    }

    public Font getLyricFont() {
        return lyricFont;
    }

    public void setLyricFont(Font FONT) {
        this.lyricFont = FONT;
    }

    public Color getLyricForeground() {
        return lyricForeground;
    }

    public void setLyricForeground(Color FORE_GROUND) {
        this.lyricForeground = FORE_GROUND;
    }

    public Color getLyricHilight() {
        return lyricHilight;
    }

    public void setLyricHilight(Color HIGH_LIGHT) {
        this.lyricHilight = HIGH_LIGHT;
    }

    public int getH_SPACE() {
        return H_SPACE;
    }

    public void setH_SPACE(int H_SPACE) {
        this.H_SPACE = H_SPACE;
    }

    public int getRefreshInterval() {
        return refreshInterval;
    }

    public void setRefreshInterval(int REFRESH_INTERVAL) {
        this.refreshInterval = REFRESH_INTERVAL;
    }

    public int getV_SPACE() {
        return V_SPACE;
    }

    public void setTopParent(JFrame aThis) {
        this.topParent = aThis;
    }

    public JFrame getTopParent() {
        return topParent;
    }

    public void setV_SPACE(int V_SPACE) {
        this.V_SPACE = V_SPACE;
    }

    public void setVolume(int gainValue) {
        this.gainValue = gainValue;
    }

    public String getLastDir() {
        return lastDir;
    }

    private Config() {
        lastEqualizer = new int[10];
        playlists = new Vector<PlayList>();
        Arrays.fill(lastEqualizer, 50);
        componentMap = new HashMap<String, Set<String>>();
        searchLyricDirs.add(saveLyricDir);
    }

    /**
     * 得到全局的单例的config对象
     * @return
     */
    public static synchronized Config getConfig() {
        return config;
    }
}
