package za.co.me23.chat;

import javax.microedition.lcdui.Font;

/**
 *
 * @author Refais
 */
public class Settings {

    public static int SCREEN_WIDTH = 176;

    public static int SCREEN_HEIGHT = 220;

    public static int CONTENT_WIDTH = SCREEN_WIDTH - 6;

    public static int DRAW_CONTENT_WIDTH = SCREEN_WIDTH - 6;

    public static int FLAG_XPOS = DRAW_CONTENT_WIDTH - 17;

    public static final int FONT_SMALL = Font.SIZE_SMALL;

    public static final int FONT_MEDIUM = Font.SIZE_MEDIUM;

    public static final int FONT_LARGE = Font.SIZE_LARGE;

    public static final Font SMALL_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, FONT_SMALL);

    public static final Font MEDIUM_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, FONT_MEDIUM);

    public static final Font LARGE_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, FONT_LARGE);

    public static final Font LARGE_B_FONT = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, FONT_LARGE);

    public static final int LINE_ITEM_HEIGHT_SMALL = SMALL_FONT.getHeight() + 4;

    public static final int LINE_ITEM_HEIGHT_MEDIUM = MEDIUM_FONT.getHeight() + 4;

    public static final int LINE_ITEM_HEIGHT_LARGE = LARGE_FONT.getHeight() + 4;

    public static final int COMMAND_BAR_HEIGHT = SMALL_FONT.getHeight() + 2;

    public static final int COMMAND_BAR_COLOR1 = 0x00FFAA;

    public static final int COMMAND_BAR_COLOR2 = 0x000000;

    public static final int SCROLL_BAR1_COLOUR1 = 0x000000;

    public static final int SCROLL_BAR1_COLOUR2 = 0xFFFFFF;

    public static final int SELECT_BAR_COLOUR1 = 0xAAAAAA;

    public static final int SELECT_BAR_COLOUR2 = 0xBBBBBB;

    public static final int SCROLL_BAR2_COLOUR1 = 0xFFFFFF;

    public static final int SCROLL_BAR2_COLOUR2 = 0xFF0000;

    public static int CONTENT_PANEL_HEIGHT = SCREEN_HEIGHT - TitleBar.TITLE_BAR_HEIGHT - COMMAND_BAR_HEIGHT;

    public static int NOTIFICATION_FONT_COLOR = 0xFF0000;

    public static int COMMANDBAR_FONT_COLOR = 0xFEFEFE;

    public static int GROUP_FONT_COLOR = 0x000000;

    public static int CONTACT_FONT_COLOR = 0x000000;

    public static int INVITE_FONT_COLOR = 0x000000;

    public static int SYSTEM_MESSAGE_FONT_COLOR = 0x000000;

    public static int MENU_MESSAGE_FONT_COLOR_TRUE = 0x000000;

    public static int MENU_MESSAGE_FONT_COLOR_FALSE = 0xFF0000;

    public static int LINE_COLOR = 0x00AAFF;

    public static boolean KEYS_ENABLED = true;

    public static String ERROR_MESSAGE = "";

    public static int ERROR_CODE = 0;

    public static int MESSASE_DRAW_HEADER = 2;

    public static int MESSASE_DRAW_SUB = 4;

    public static int CONTACT_ITEM_DRAW_HEADER = 19;

    public static int CONTACT_ITEM_DRAW_SUB = 21;

    public static int GROUP_DRAW = 18;

    public static boolean FILE_CONN_API = System.getProperty("microedition.io.file.FileConnection.version") != null;

    public static void getSettings() {
        CONTENT_WIDTH = SCREEN_WIDTH - 6;
        DRAW_CONTENT_WIDTH = SCREEN_WIDTH - 6;
        FLAG_XPOS = DRAW_CONTENT_WIDTH - 17;
        CONTENT_PANEL_HEIGHT = SCREEN_HEIGHT - TitleBar.TITLE_BAR_HEIGHT - COMMAND_BAR_HEIGHT;
    }
}
