package org.mazix.constants;

import static java.util.Locale.ENGLISH;
import static java.util.Locale.FRENCH;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * The class which defines language constants (matching the language XML file). They are used to
 * match the XML tags to translate the game.
 * 
 * @author Benjamin Croizet (graffity2199@yahoo.fr)
 * @since 0.3
 * @version 0.7
 */
public final class LanguageConstants {

    /** The default locale. */
    public static final Locale DEFAULT_LOCALE = ENGLISH;

    /**
     * The <code>Map</code> containing all supported locales. It has <code>Locale</code> as keys and
     * the associated language name as values.
     */
    public static final Map<Locale, String> SUPPORTED_LOCALES = createSupportedLocalesMap();

    /**
     * The reverse <code>Map</code> which stores language name as keys and <code>Locale</code> as
     * values.
     */
    public static final Map<String, Locale> REVERSE_SUPPORTED_LOCALES = createReverseSupportedLocalesMap();

    /** Game name. */
    public static final String GAME_TITLE_LANG = "game_name";

    /** Official levels. */
    public static final String OFFICIAL_LEVELS_LANG = "official_levels";

    /** Contribution levels. */
    public static final String CONTRIB_LEVELS_LANG = "contrib_levels";

    /** Profile. */
    public static final String PROFILE_LANG = "profile";

    /** Options. */
    public static final String OPTIONS_LANG = "options";

    /** Help. */
    public static final String HELP_LANG = "help";

    /** Credits. */
    public static final String CREDITS_LANG = "credits";

    /** Level editor. */
    public static final String LEVEL_EDITOR_LANG = "level_editor";

    /** Quit. */
    public static final String QUIT_LANG = "quit";

    /** New profile. */
    public static final String NEW_PROFILE_LANG = "new_profile";

    /** Select profile. */
    public static final String SELECT_PROFILE_LANG = "select_profile";

    /** Modify profile. */
    public static final String MODIFY_PROFILE_LANG = "modify_profile";

    /** Delete profile. */
    public static final String DELETE_PROFILE_LANG = "delete_profile";

    /** Back. */
    public static final String BACK_LANG = "back";

    /** Ok. */
    public static final String OK_LANG = "ok";

    /** Name. */
    public static final String NAME_LANG = "name";

    /** Profile info. */
    public static final String PROFILE_INFO_LANG = "profile_info";

    /** Reset progression. */
    public static final String RESET_PROGRESSIONS_LANG = "reset_progressions";

    /** Last build. */
    public static final String LAST_BUILD_LANG = "last_build";

    /** Web site. */
    public static final String WEB_SITE_LANG = "web_site";

    /** Original author. */
    public static final String ORIGINAL_AUTHOR_LANG = "original_author";

    /** Java version. */
    public static final String JAVA_VERSION_LANG = "java_version";

    /** License. */
    public static final String LICENCE_LANG = "licence";

    /** Language. */
    public static final String LANGUAGE_LANG = "language";

    /** Sound effect volume. */
    public static final String SOUND_EFFECT_VOLUME_LANG = "sound_effect_volume";

    /** Sound music volume. */
    public static final String SOUND_MUSIC_VOLUME_LANG = "sound_music_volume";

    /** Screen resolution. */
    public static final String SCREEN_RESOLUTION_LANG = "screen_resolution";

    /** Not enough profile error message. */
    public static final String NOT_ENOUGH_PROFILE_LANG = "not_enough_profile";

    /** Existing profile error message. */
    public static final String NEW_PROFILE_ALREADY_EXIST_LANG = "new_profile_already_exists";

    /** Existing profile error message. */
    public static final String MODIFY_PROFILE_ALREADY_EXIST_LANG = "modify_profile_already_exists";

    /** Empty profile error message. */
    public static final String EMPTY_PROFILE_NAME_LANG = "empty_profile_name";

    /** Modify profile name. */
    public static final String MODIFY_PROFILE_NAME_LANG = "modify_profile_name";

    /** Restart game. */
    public static final String RESTART_GAME_LANG = "restart_game_exists";

    /** Series. */
    public static final String SERIES_LANG = "series";

    /** Level. */
    public static final String LEVEL_LANG = "level";

    /** Time. */
    public static final String TIME_LANG = "time";

    /** Moves. */
    public static final String MOVES_LANG = "moves";

    /** The series finished info message. */
    public static final String SERIES_FINISHED_LANG = "series_finished";

    /** The level finished info message. */
    public static final String LEVEL_FINISHED_LANG = "level_finished";

    /** The level finished with the next unblocked info message. */
    public static final String LEVEL_FINISHED_UNBLOCKED_LANG = "level_finished_next_unblocked";

    /** The time over info message. */
    public static final String TIME_OVER_LANG = "time_over";

    /**
     * Creates the reverse <code>Map</code> of all supported language name.
     * 
     * @return the unmodifiable <code>Map</code> of all supported language name.
     */
    private static Map<String, Locale> createReverseSupportedLocalesMap() {
        final Map<String, Locale> reverseSupportedLocales = new HashMap<String, Locale>();
        reverseSupportedLocales.put("English", ENGLISH);
        reverseSupportedLocales.put("Francais", FRENCH);
        return Collections.unmodifiableMap(reverseSupportedLocales);
    }

    /**
     * Creates the <code>Map</code> of all supported locales.
     * 
     * @return the unmodifiable <code>Map</code> of all supported locales.
     */
    private static Map<Locale, String> createSupportedLocalesMap() {
        final Map<Locale, String> supportedLocales = new HashMap<Locale, String>();
        supportedLocales.put(ENGLISH, "English");
        supportedLocales.put(FRENCH, "Francais");
        return Collections.unmodifiableMap(supportedLocales);
    }

    /**
     * Private constructor to prevent from instantiation.
     * 
     * @since 0.7
     */
    private LanguageConstants() {
    }
}
