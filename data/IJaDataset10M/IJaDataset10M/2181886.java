package dplayer;

import java.util.Locale;
import dplayer.gui.i18n.I18N;
import dplayer.media.MPlayer;
import dplayer.player.TrackList.RepeatMode;
import dplayer.scanner.Scanner;

/**
 * Collection of settings.
 */
public final class Settings extends AbstractSettings {

    static {
        sFile = new File("dplayer.properties", new String[] { "dplayer properties", "Copyright (C) 2007 Tobias Winterhalter (tobias@wcondev.de)" });
    }

    private static final Section VAR = new Section("Various settings");

    public static final Property LANG = new Property(VAR, "lang", "GUI language (string value, empty = use OS language).", "");

    public static final Property ROOTS = new Property(VAR, "roots", "List of roots to be searched for music files (comma separated).", "");

    public static final Property DISPLAY_POSITION_IN_TITLE = new Property(VAR, "display.positionInTitle", "Display current track position as window title.", "true");

    public static final Property DISPLAY_SKIPPED = new Property(VAR, "display.skipped", "Display (or hide) tracks that are marked to be skipped by player.", "false");

    public static final Property DISPLAY_COVER = new Property(VAR, "display.cover", "Try to display cover as background of track table.", "true");

    public static final Property COVERS_PATH = new Property(VAR, "covers.path", "Path to search for covers (comma separated).", ".");

    public static final Property REMOTE_ENABLED = new Property(VAR, "remote.enabled", "Allow remote control commands.", "true");

    public static final Property REMOTE_PORT = new Property(VAR, "remote.port", "Port to listen for remote control commands.", "5099");

    public static final Property STARTUP_RESUME = new Property(VAR, "startup.resume", "After startup, play track from previous session.", "true");

    public static final Property STARTUP_RESUME_WITH_OFFSET = new Property(VAR, "startup.resume.withOffset", "After startup, resume to play the track of the previous session.", "false");

    public static final Property SCANNER_POLICY = new Property(VAR, "scanner.policy", "Policy to use for scanning directories (aggressive|lazy).", new String[] { Scanner.Policy.LAZY.name().toLowerCase(), Scanner.Policy.LAZY.name().toLowerCase(), Scanner.Policy.AGGRESSIVE.name().toLowerCase() });

    private static final Section LASTFM = new HiddenSection("last.fm settings");

    public static final Property LAST_FM_ENABLED = new Property(LASTFM, "lastfm.enabled", "Submit to last.fm or not: true/false.", "false");

    public static final Property LAST_FM_USER_NAME = new Property(LASTFM, "lastfm.user.name", "");

    public static final Property LAST_FM_PASSWORD = new Property(LASTFM, "lastfm.user.password", "");

    private static final Section EXT = new Section("External applications");

    public static final Property PLAYER = new Property(EXT, "player", "Media player to use for playback.", "mplayer");

    public static final Property MIXER = new Property(EXT, "mixer", "Media mixer to use for volume control.", new String[] { "none", "amixer", "WinExtMixer" });

    public static final Property AMIXER_COMMAND = new Property(EXT, "amixer.command", "Path and name of amixer executable.", "amixer");

    public static final Property MPLAYER_COMMAND = new Property(EXT, "mplayer.command", "Path and name of mplayer executable.", new String[] { "mplayer", "mplayer", "m.exe" });

    public static final Property MPLAYER_PRIORITY = new Property(EXT, "mplayer.priority", "The process priority for mplayer.", MPlayer.Priority.ABOVENORMAL.name().toLowerCase());

    private static final Section CACHE = new Section("Cache settings");

    public static final Property CACHE_ENABLED = new Property(CACHE, "cache.enabled", "Enable caching of music file properties (e.g. tags, 'skip' flag, ...).", "true");

    public static final Property CACHE_SONGS_FILE = new Property(CACHE, "cache.songs.file", "File for music file cache.", "songs.db");

    private static final Section LOG = new LoggerSection("Logger settings");

    public static final Property LOG_ROOT_LOGGER = new Property(LOG, "log4j.rootLogger", "INFO, DPLAYERLOG");

    public static final Property LOG_DPLAYER_APPENDER = new Property(LOG, "log4j.appender.DPLAYERLOG", "dplayer.LogAppender");

    public static final Property LOG_DPLAYER_LAYOUT = new Property(LOG, "log4j.appender.DPLAYERLOG.layout", "org.apache.log4j.PatternLayout");

    public static final Property LOG_DPLAYER_PATTERN = new Property(LOG, "log4j.appender.DPLAYERLOG.layout.ConversionPattern", "%-5p [%t] %c - %m%n");

    public static final Property LOG_DPLAYER_SIZE = new Property(LOG, "log4j.appender.DPLAYERLOG.size", "8192");

    private static final Section AUTO = new Section("Automatic settings - DO NOT MODIFY");

    public static final Property HISTORY_FILE = new Property(AUTO, "history.file", "");

    public static final Property HISTORY_FILE_OFFSET = new Property(AUTO, "history.file.offset", "0");

    public static final Property HISTORY_REPEAT = new Property(AUTO, "history.repeat", "none");

    public static final Property HISTORY_SHUFFLE = new Property(AUTO, "history.shuffle", "false");

    public static final Property HISTORY_VOLUME = new Property(AUTO, "history.volume", "40");

    public static final Property WINDOW_X = new Property(AUTO, "window.x", "0");

    public static final Property WINDOW_Y = new Property(AUTO, "window.y", "0");

    public static final Property WINDOW_W = new Property(AUTO, "window.w", "640");

    public static final Property WINDOW_H = new Property(AUTO, "window.h", "480");

    public static final Property WINDOW_MAXIMIZED = new Property(AUTO, "window.maximized", "false");

    public static final Property WINDOW_MINIMIZED = new Property(AUTO, "window.minimized", "false");

    public static final Property TABLE_COLUMN1_W = new Property(AUTO, "table.column1.w", "200");

    public static final Property TABLE_COLUMN2_W = new Property(AUTO, "table.column2.w", "80");

    public static final Property TREE_W = new Property(AUTO, "tree.w", "30");

    public static void init() {
        load();
        if (LANG.getString().length() == 2) {
            I18N.setLocale(new Locale(LANG.getString()));
        }
    }

    public static void setRepeat(final RepeatMode repeat) {
        HISTORY_REPEAT.setString(repeat.name().toLowerCase());
    }

    public static RepeatMode getRepeat() {
        return RepeatMode.valueOf(HISTORY_REPEAT.getString().toUpperCase());
    }

    public static void setScannerPolicy(final Scanner.Policy p) {
        SCANNER_POLICY.setString(p.name().toLowerCase());
    }

    public static Scanner.Policy getScannerPolicy() {
        return Scanner.Policy.valueOf(SCANNER_POLICY.getString().toUpperCase());
    }

    public static String[] getScannerPolicies() {
        final String[] a = new String[Scanner.Policy.values().length];
        for (int i = 0; i < a.length; i++) {
            final String name = Scanner.Policy.values()[i].name();
            a[i] = I18N.get("SCANNER_POLICY_" + name, name.toLowerCase());
        }
        return a;
    }
}
