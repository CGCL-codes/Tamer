package org.exmaralda.folker.utilities;

/**
 *
 * @author thomas
 */
public class Constants {

    static boolean isFOLK;

    static {
        isFOLK = false;
    }

    public static boolean isFolkContext() {
        return isFOLK;
    }

    public static final int NEW_ICON = 0;

    public static final int OPEN_ICON = 1;

    public static final int SAVE_ICON = 2;

    public static final int SAVE_AS_ICON = 3;

    public static final int IMPORT_ICON = 4;

    public static final int EXPORT_ICON = 5;

    public static final int OUTPUT_ICON = 6;

    public static final int COPY_ICON = 7;

    public static final int SEARCH_ICON = 8;

    public static final int REPLACE_ICON = 9;

    public static final int EDIT_PREFERENCES_ICON = 10;

    public static final int EDIT_SPEAKERS_ICON = 11;

    public static final int EDIT_RECORDING_ICON = 12;

    public static final int PLAY_SELECTION_ICON = 13;

    public static final int LOOP_SELECTION_ICON = 14;

    public static final int PLAY_ICON = 15;

    public static final int PAUSE_ICON = 16;

    public static final int STOP_ICON = 17;

    public static final int ZOOM_ICON = 18;

    public static final int SHIFT_SELECTION_ICON = 19;

    public static final int DETACH_SELECTION_ICON = 20;

    public static final int ADD_EVENT_ICON = 21;

    public static final int APPEND_EVENT_ICON = 22;

    public static final int REMOVE_EVENT_ICON = 23;

    public static final int TIMESTAMP_EVENT_ICON = 24;

    public static final int SPLIT_EVENT_ICON = 25;

    public static final int MERGE_EVENTS_ICON = 26;

    public static final int FILL_GAPS_ICON = 27;

    public static final int INSERT_PAUSE_ICON = 28;

    public static final int NEXT_ERROR_ICON = 29;

    public static final int RECORDING_WARNING_ICON = 30;

    public static final int REWIND_ICON = 31;

    public static final int FORWARD_ICON = 32;

    public static final int APPEND_INTERVAL_ICON = 33;

    public static final int NAVIGATE_ICON = 34;

    public static final int RESCUE_ICON = 35;

    public static final int PLAY_NEXT_SEGMENT_ICON = 36;

    private static String[] ICON_PATHS = { "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/document-new.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/document-open.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/document-save.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/document-save-as.png", "/org/exmaralda/partitureditor/partiture/Icons/Import.gif", "/org/exmaralda/partitureditor/partiture/Icons/Export.gif", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/mimetypes/x-office-presentation.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/edit-copy.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/edit-find.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/edit-find-replace.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/categories/preferences-system.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/apps/system-users.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/mimetypes/video-x-generic.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/media-playback-start.png", "/org/exmaralda/folker/tangoicons/othericons/media-play-loop-16.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/media-playback-start.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/media-playback-pause.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/media-playback-stop.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/system-search.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/go-next.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/mail-send-receive.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/list-add.png", "/org/exmaralda/folker/tangoicons/othericons/add-bottom.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/list-remove.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/appointment-new.png", "/org/exmaralda/partitureditor/partiture/Icons/Split.png", "/org/exmaralda/partitureditor/partiture/Icons/Merge.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/format-indent-more.png", "/org/exmaralda/folker/tangoicons/othericons/quarter_rest.gif", "/org/exmaralda/folker/tangoicons/othericons/error-icon.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/32x32/mimetypes/audio-x-generic.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/media-seek-backward.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/media-seek-forward.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/go-last.png", "/org/exmaralda/folker/tangoicons/othericons/compass_icon.gif", "/org/exmaralda/folker/tangoicons/othericons/rescue.png", "/org/exmaralda/folker/tangoicons/tango-icon-theme-0.8.1/16x16/actions/media-skip-forward.png" };

    public static final String GAT_PHONEME = "\\p{Ll}";

    public static final String GAT_STRESSED_PHONEME = "\\p{Lu}";

    public static final String GAT_UNSTRESSED_SYLLABLE = "(" + GAT_PHONEME + "++(:" + "?+" + GAT_PHONEME + "*+" + ")*+)";

    public static final String GAT_STRESSED_SYLLABLE = "(" + GAT_STRESSED_PHONEME + "++(:" + "?+" + GAT_STRESSED_PHONEME + "*+" + ")*+)";

    public static final String GAT_GLOTTAL_STOP = "\\u0027";

    public static final String GAT_ASSIMILATION = "\\+";

    public static final String GAT_LATCHING = "=";

    public static final String GAT_INTONATION = "[\\.\\?\\-\\,;]";

    public static final String GAT_BOUNDARY = "(" + GAT_INTONATION + " ?)";

    public static final String GAT_SEMI_BOUNDARY = GAT_INTONATION + GAT_LATCHING;

    public static final String GAT_BREATHE = "_?h{1,3}+";

    public static final String GAT_NV = "\\(\\(" + "[^\\(-\\.\\d].*" + "\\)\\)";

    public static final String GAT_NUMERICAL_PAUSE = "\\(" + "\\d{1,2}\\.\\d{1,2}" + "\\)";

    public static final String GAT_OTHER_PAUSE = "\\(" + "(-{1,3}|\\.)" + "\\)";

    public static final String GAT_WORD_LEVEL0 = GAT_PHONEME + "(:" + "?+" + GAT_PHONEME + "*+" + ")++" + GAT_GLOTTAL_STOP + "?";

    public static final String GAT_WORD = "(((" + GAT_UNSTRESSED_SYLLABLE + "++" + GAT_STRESSED_SYLLABLE + "?+" + GAT_UNSTRESSED_SYLLABLE + "*+" + ")" + "|(" + GAT_STRESSED_SYLLABLE + GAT_UNSTRESSED_SYLLABLE + "*+" + ")))" + GAT_GLOTTAL_STOP + "?";

    public static final String GAT_ASSIMILATED_WORDS = GAT_WORD + GAT_ASSIMILATION + GAT_WORD;

    public static final String GAT_WORD_CHAIN = GAT_WORD + "( " + GAT_WORD + ")*+";

    public static final String GAT_UNCERTAIN = "\\(" + GAT_WORD_CHAIN + "(" + "\\|" + GAT_WORD_CHAIN + ")*+\\)";

    public static final String GAT_ELEMENT = "(" + GAT_BREATHE + "|" + GAT_UNCERTAIN + "|" + GAT_WORD + "|" + GAT_ASSIMILATED_WORDS + "|" + GAT_NV + "|" + GAT_NUMERICAL_PAUSE + "|" + GAT_OTHER_PAUSE + "|" + ")";

    public static final String GAT_EVENT = GAT_LATCHING + "?" + "(" + GAT_ELEMENT + "( |" + GAT_SEMI_BOUNDARY + "))*+" + GAT_ELEMENT + "?" + GAT_BOUNDARY + "?";

    public static final String GAT_SEGMENT = GAT_LATCHING + "?" + "(" + GAT_ELEMENT + "( |" + GAT_SEMI_BOUNDARY + "))*+" + GAT_ELEMENT + "?" + GAT_BOUNDARY + " ";

    public javax.swing.Icon getIcon(int iconNumber) {
        String path = ICON_PATHS[iconNumber];
        return new javax.swing.ImageIcon(getClass().getResource(path));
    }
}
