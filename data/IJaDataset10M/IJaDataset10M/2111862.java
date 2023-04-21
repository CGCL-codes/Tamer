package eu.s0ck3t.monstertris;

public class Constants {

    public static final int CANVAS_WIDTH = 176;

    public static final int CANVAS_HEIGHT = 208;

    public static final String PATH_GFX = "/gfx/";

    public static final String PATH_MUSIC = "/music/";

    public static final char[] KEY_NUM1_CHARS = new char[] { '.', '?', '!' };

    public static final char[] KEY_NUM2_CHARS = new char[] { 'a', 'b', 'c' };

    public static final char[] KEY_NUM3_CHARS = new char[] { 'd', 'e', 'f' };

    public static final char[] KEY_NUM4_CHARS = new char[] { 'g', 'h', 'i' };

    public static final char[] KEY_NUM5_CHARS = new char[] { 'j', 'k', 'l' };

    public static final char[] KEY_NUM6_CHARS = new char[] { 'm', 'n', 'o' };

    public static final char[] KEY_NUM7_CHARS = new char[] { 'p', 'q', 'r', 's' };

    public static final char[] KEY_NUM8_CHARS = new char[] { 't', 'u', 'v' };

    public static final char[] KEY_NUM9_CHARS = new char[] { 'w', 'x', 'y', 'z' };

    public static final char[] KEY_NUM0_CHARS = new char[] { ' ' };

    public static final int ALIGN_H_CENTER = 1;

    public static final int ALIGN_H_LEFT = 2;

    public static final int ALIGN_H_RIGHT = 4;

    public static final int ALIGN_H_DEFAULT = 0;

    public static final String MUSIC_GAME_MIDIS[] = { "bjorn__lynne-_beneath_another_sky.mid", "bjorn__lynne-_blue_jazz.mid", "bjorn__lynne-_chill_cafe.mid", "bjorn__lynne-_communication.mid", "bjorn__lynne-_funky_guppy.mid", "bjorn__lynne-_in_the_cave.mid", "bjorn__lynne-_lets_go.mid", "bjorn__lynne-_open_pod.mid", "bjorn__lynne-_retro_electro.mid", "bjorn__lynne-_rock_force.mid", "bjorn__lynne-_rockin_ruby.mid", "bjorn__lynne-_squadron_standby.mid", "bjorn__lynne-_the_chaos_warrior.mid", "bjorn__lynne-_the_enchanted_orchard.mid", "bjorn__lynne-_the_great_river_race.mid", "bjorn__lynne-_trailblazer.mid", "bjorn__lynne-_understatement.mid", "bjorn__lynne-_zombie_chase.mid" };

    public static final String IMAGE_BG = PATH_GFX + "game-bg.png";

    public static final String IMAGE_MENU_BG = PATH_GFX + "menu-bg.png";

    public static final String IMAGE_SCORES_BG = PATH_GFX + "scores-bg.png";

    public static final String IMAGE_SPLASH_BG = PATH_GFX + "splash-bg.png";

    public static final String IMAGE_EXIT_BG = PATH_GFX + "exiting-bg.png";

    public static final String IMAGE_PIECES_BG = PATH_GFX + "pieces-bg";

    public static final String IMAGE_GAMEOVER_BG = PATH_GFX + "gameover-bg.png";

    public static final String IMAGE_START_NEW_GAME_BG = PATH_GFX + "start_new_game-bg.png";

    public static final String IMAGE_HELP_BG = PATH_GFX + "help-bg.png";

    public static final int MAX_PIECES_BG = 8;

    public static final int MAX_RESOURCES = 51;

    public static final String IMAGE_NEW_GAME = PATH_GFX + "new_game.png";

    public static final String IMAGE_BEST_SCORES = PATH_GFX + "best_scores.png";

    public static final String IMAGE_EXIT = PATH_GFX + "exit.png";

    public static final String IMAGE_SCORES = PATH_GFX + "scores.png";

    public static final String IMAGE_ENTER_INITIALS = PATH_GFX + "enter_initials.png";

    public static final String IMAGE_MENU = PATH_GFX + "menu.png";

    public static final String IMAGE_CARRIAGE = PATH_GFX + "carriage.png";

    public static final String IMAGE_SPACE = PATH_GFX + "space.png";

    public static final String IMAGE_BLOCKS = PATH_GFX + "blocks.png";

    public static final String IMAGE_YES = PATH_GFX + "yes.png";

    public static final String IMAGE_NO = PATH_GFX + "no.png";

    public static final String IMAGE_HELP = PATH_GFX + "help.png";

    public static final int BLOCK_PIXEL_SIZE = 10;

    public static final String RS_SCORES = "mt_scores";

    public static final String RS_GRIDSETTINGS = "mt_gridSettings";

    public static final String RS_SG = "mt_sg";

    public static final String RS_SG_GRIDDATA = "mt_sg_gridData:";

    public static final String RS_SG_CP = "mt_sg_currentPiece:";

    public static final String RS_SG_NP = "mt_sg_nextPiece:";

    public static final String RS_SG_GP_VARS = "mt_sg_gamePlayVars:";

    public static final int PIECES_AREA_X = 2;

    public static final int PIECES_AREA_Y = 6;

    public static final int PIECES_AREA_WIDTH = 100;

    public static final int PIECES_AREA_HEIGHT = 206;

    public static final int NEXT_PIECE_AREA_X = 110;

    public static final int NEXT_PIECE_AREA_Y = 9;

    public static final int NEXT_PIECE_AREA_WIDTH = 57;

    public static final int NEXT_PIECE_AREA_HEIGHT = 53;

    public static final int NEXT_PIECE_AREA_CENTER_X = NEXT_PIECE_AREA_X + NEXT_PIECE_AREA_WIDTH / 2;

    public static final int NEXT_PIECE_AREA_CENTER_Y = NEXT_PIECE_AREA_Y + NEXT_PIECE_AREA_HEIGHT / 2;

    public static final String IMAGE_RESUME = PATH_GFX + "resume.png";

    public static final String IMAGE_BACK = PATH_GFX + "back.png";

    public static final int STATE_MENU = 1;

    public static final int STATE_GAME = 2;

    public static final int STATE_SCORES = 3;

    public static final int STATE_GAMEOVER = 4;

    public static final int STATE_ENTER_INITIALS = 5;

    public static final int STATE_START_NEW_GAME = 6;

    public static final int STATE_HELP = 7;

    public static final int STATE_INIT = STATE_MENU;

    public static final int KEY_JOY_SOFTKEY = -5;

    public static final int KEY_RIGHT_SOFTKEY = -7;

    public static final int LEVEL_X = 170;

    public static final int LEVEL_Y = 78;

    public static final int LINES_X = 170;

    public static final int LINES_Y = 106;

    public static final int SCORE_X = 137;

    public static final int SCORE_Y = 160;

    public static final int EI_LABEL_X = 60;

    public static final int EI_LABEL_Y = 70;

    public static final int SCORE_LABEL_X = 65;

    public static final int SCORE_LABEL_Y = 70;

    public static final int GAMEOVER_SCORE_X = 86;

    public static final int GAMEOVER_SCORE_Y = 100;

    public static final int GAMEOVER_SCORES_SCORE_X = 30;

    public static final int GAMEOVER_SCORES_SCORE_Y = 50;

    public static final int GAMEOVER_SCORES_INITIAL_X = GAMEOVER_SCORES_SCORE_X + 115;

    public static final int INITIALS_X = 88;

    public static final int INITIALS_Y = 125;

    public static final int MENU_YES_X = 74;

    public static final int MENU_YES_Y = 114;

    public static final int MENU_NO_X = 77;

    public static final int MENU_NO_Y = 136;

    public static final int MENU_IMAGE_YES_INDEX = 0;

    public static final int MENU_IMAGE_NO_INDEX = 1;

    public static final int MENU_NEW_GAME_X = 48;

    public static final int MENU_NEW_GAME_Y = 64;

    public static final int MENU_BEST_SCORES_X = 48;

    public static final int MENU_BEST_SCORES_Y = 86;

    public static final int MENU_HELP_X = 48;

    public static final int MENU_HELP_Y = 108;

    public static final int MENU_EXIT_X = 48;

    public static final int MENU_EXIT_Y = 132;

    public static final int MENU_IMAGE_INDEX_NONE = -1;

    public static final int MENU_IMAGE_NEW_GAME_INDEX = 0;

    public static final int MENU_IMAGE_BEST_SCORES_INDEX = 1;

    public static final int MENU_IMAGE_HELP_INDEX = 2;

    public static final int MENU_IMAGE_EXIT_INDEX = 3;

    public static final int IMAGE_RIGHT_SOFTKEY_X = 122;

    public static final int IMAGE_RIGHT_SOFTKEY_Y = 188;

    public static final int IMAGE_JOY_SOFTKEY_X = 69;

    public static final int IMAGE_JOY_SOFTKEY_Y = 188;

    public static final int SPLASH_SCREEN_DELAY = 0;

    public static final int PIECES_SPEED_FASTER = 15;

    public static final int MAIN_LOOP_DELAY = 1000 / 30;

    public static final int COLOR_GRID = 0xb2e2d7;

    public static final int COLOR_STATS_BG = 0xc0f3e8;

    public static final int COLOR_GAME_BG = 0xc0f3e8;

    public static final int COLOR_NEXT_PIECE_BG = 0xb2e2d7;

    public static final int COLOR_GAMEOVER_BG = 0xc0f3e8;

    public static final int COLOR_CARRIAGE = 0x291eb0;

    public static final int MAX_SCORES = 6;
}
