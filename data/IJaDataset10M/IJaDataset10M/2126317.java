package dsr;

import java.util.Hashtable;
import com.jme.image.Texture;
import com.jme.scene.state.TextureState;
import com.jme.util.TextureManager;

public final class TextureStateCache {

    public static final short TEX_INTERIOR1 = 1;

    public static final short TEX_MOONROCK = 2;

    public static final short TEX_CORRIDOR1 = 3;

    public static final short TEX_INTERIOR2 = 4;

    public static final short TEX_RUBBLE = 5;

    public static final short TEX_PATH = 6;

    public static final short TEX_GRASS = 7;

    public static final short TEX_ROAD = 8;

    public static final short TEX_MUD = 9;

    public static final short TEX_WOODEN_FLOOR = 10;

    public static final short TEX_WATER = 11;

    public static final short TEX_BEACH = 12;

    public static final short TEX_SAND1 = 13;

    public static final short TEX_SAND2 = 14;

    public static final short TEX_WOODEN_PLANKS = 15;

    public static final short TEX_MOONBASE_BLUE = 16;

    public static final short TEX_CORRUGATED_WALL = 17;

    public static final short TEX_MUDTRACK_EW = 18;

    public static final short TEX_MUDTRACK_NS = 19;

    public static final short TEX_RAILWAY_EW = 20;

    public static final short TEX_RAILWAY_NS = 21;

    public static final short TEX_MUDTRACKS_RB = 22;

    public static final short TEX_MUDTRACKS_LT = 23;

    public static final short TEX_SNOW = 24;

    public static final short TEX_SPACEWALL = 25;

    public static final short TEX_STONE_TILES = 26;

    public static final short TEX_MUDTRACKS_RT = 27;

    public static final short TEX_CARPET1 = 28;

    public static final short TEX_WOODEN_FLOOR2 = 29;

    public static final short TEX_SECTOR1 = 30;

    public static final short TEX_STREET = 31;

    public static final short TEX_STREET_ZEBRA_LR = 32;

    public static final short TEX_MUDTRACKS_UD = 33;

    public static final short TEX_MUDTRACKS_UDR = 34;

    public static final short TEX_MUDTRACKS_LR = 35;

    public static final short TEX_MUDTRACKS_LB = 36;

    public static final short TEX_STREET_ZEBRA_UD = 37;

    public static final short TEX_YELLOW_LINE_INNER_TR = 38;

    public static final short TEX_YELLOW_LINE_INNER_TL = 39;

    public static final short TEX_YELLOW_LINE_INNER_BR = 40;

    public static final short TEX_YELLOW_LINE_INNER_BL = 41;

    public static final short TEX_YELLOW_LINE_OUTER_BL = 42;

    public static final short TEX_YELLOW_LINE_OUTER_BR = 43;

    public static final short TEX_YELLOW_LINE_OUTER_TL = 44;

    public static final short TEX_YELLOW_LINE_OUTER_TR = 45;

    public static final short TEX_YELLOW_LINE_L = 46;

    public static final short TEX_YELLOW_LINE_R = 47;

    public static final short TEX_YELLOW_LINE_T = 48;

    public static final short TEX_YELLOW_LINE_B = 49;

    public static final short TEX_ALIEN_SKIN = 50;

    public static final short TEX_BULKHEAD = 51;

    public static final short TEX_SPACESHIP_WALL = 52;

    public static final short TEX_TELEPORTER = 53;

    public static final short TEX_COUCH = 54;

    public static final short TEX_METAL_FLOOR5 = 55;

    public static final short TEX_METAL_FLOOR6 = 56;

    public static final short TEX_METAL_FLOOR15 = 57;

    public static final short TEX_HEDGE = 58;

    public static final short TEX_WOODEN_FENCE = 59;

    public static final short TEX_METAL_FLOOR41 = 60;

    public static final short TEX_LAB_FLOOR1 = 61;

    public static final short TEX_LAB_FLOOR2 = 62;

    public static final short TEX_ROAD_WHITE_LINE_UD = 63;

    public static final short TEX_ROAD_WHITE_LINE_LR = 64;

    public static final short TEX_SECTOR2 = 65;

    public static final short TEX_SECTOR3 = 66;

    public static final short TEX_SECTOR4 = 67;

    public static final short TEX_ESCAPE_HATCH = 68;

    public static final short TEX_BRICKS = 69;

    public static final short TEX_WHITE_LINE_LR = 70;

    public static final short TEX_WHITE_LINE_UD = 71;

    public static final short MAX_TEX_NUM = 71;

    private AppletMain main;

    private Hashtable<String, TextureState> cache = new Hashtable<String, TextureState>();

    public TextureStateCache(AppletMain m) {
        main = m;
    }

    public TextureState get(int tex_code) {
        return get(GetFilename(tex_code));
    }

    public static String GetFilename(int tex_code) {
        switch(tex_code) {
            case TEX_INTERIOR1:
                return AppletMain.DATA_DIR + "textures/metalfloor1.jpg";
            case TEX_INTERIOR2:
                return AppletMain.DATA_DIR + "textures/floor3.jpg";
            case TEX_MOONROCK:
                return AppletMain.DATA_DIR + "textures/moonrock.png";
            case TEX_CORRIDOR1:
                return AppletMain.DATA_DIR + "textures/corridor.jpg";
            case TEX_RUBBLE:
                return AppletMain.DATA_DIR + "textures/rubble.jpg";
            case TEX_PATH:
                return AppletMain.DATA_DIR + "textures/road1.png";
            case TEX_GRASS:
                return AppletMain.DATA_DIR + "textures/grass.jpg";
            case TEX_ROAD:
                return AppletMain.DATA_DIR + "textures/road2.png";
            case TEX_MUD:
                return AppletMain.DATA_DIR + "textures/mud.png";
            case TEX_WOODEN_FLOOR:
                return AppletMain.DATA_DIR + "textures/floor02.png";
            case TEX_WATER:
                return AppletMain.DATA_DIR + "textures/water.png";
            case TEX_BEACH:
                return AppletMain.DATA_DIR + "textures/beach.png";
            case TEX_SAND1:
                return AppletMain.DATA_DIR + "textures/sand1.png";
            case TEX_SAND2:
                return AppletMain.DATA_DIR + "textures/sand2.png";
            case TEX_WOODEN_PLANKS:
                return AppletMain.DATA_DIR + "textures/wooden_planks_lr.jpg";
            case TEX_MOONBASE_BLUE:
                return AppletMain.DATA_DIR + "textures/ufo2_03.png";
            case TEX_CORRUGATED_WALL:
                return AppletMain.DATA_DIR + "textures/wall2.jpg";
            case TEX_MUDTRACK_EW:
                return AppletMain.DATA_DIR + "textures/mudtrack_ew.jpg";
            case TEX_MUDTRACK_NS:
                return AppletMain.DATA_DIR + "textures/mudtrack_ns.jpg";
            case TEX_RAILWAY_EW:
                return AppletMain.DATA_DIR + "textures/railway_ew.jpg";
            case TEX_RAILWAY_NS:
                return AppletMain.DATA_DIR + "textures/railway_ns.jpg";
            case TEX_MUDTRACKS_RB:
                return AppletMain.DATA_DIR + "textures/mudtracks_br.png";
            case TEX_MUDTRACKS_LT:
                return AppletMain.DATA_DIR + "textures/mudtracks_tl.png";
            case TEX_SNOW:
                return AppletMain.DATA_DIR + "textures/snow.jpg";
            case TEX_SPACEWALL:
                return AppletMain.DATA_DIR + "textures/spacewall.png";
            case TEX_STONE_TILES:
                return AppletMain.DATA_DIR + "textures/stone_tiles.jpg";
            case TEX_MUDTRACKS_RT:
                return AppletMain.DATA_DIR + "textures/mudtracks_tr.png";
            case TEX_CARPET1:
                return AppletMain.DATA_DIR + "textures/carpet006.jpg";
            case TEX_WOODEN_FLOOR2:
                return AppletMain.DATA_DIR + "textures/wood_b_9.jpg";
            case TEX_SECTOR1:
                return AppletMain.DATA_DIR + "textures/sector1.png";
            case TEX_STREET:
                return AppletMain.DATA_DIR + "textures/street001.jpg";
            case TEX_STREET_ZEBRA_UD:
                return AppletMain.DATA_DIR + "textures/street010_ud.jpg";
            case TEX_STREET_ZEBRA_LR:
                return AppletMain.DATA_DIR + "textures/street010_lr.jpg";
            case TEX_MUDTRACKS_UD:
                return AppletMain.DATA_DIR + "textures/mudtracks_tb.png";
            case TEX_MUDTRACKS_UDR:
                return AppletMain.DATA_DIR + "textures/mudtracks_tbr.png";
            case TEX_MUDTRACKS_LR:
                return AppletMain.DATA_DIR + "textures/mudtracks_lr.png";
            case TEX_MUDTRACKS_LB:
                return AppletMain.DATA_DIR + "textures/mudtracks_bl.png";
            case TEX_YELLOW_LINE_INNER_TR:
                return AppletMain.DATA_DIR + "textures/2ln_crn_ne_g.png";
            case TEX_YELLOW_LINE_INNER_TL:
                return AppletMain.DATA_DIR + "textures/2ln_crn_nw_g.png";
            case TEX_YELLOW_LINE_INNER_BR:
                return AppletMain.DATA_DIR + "textures/2ln_crn_se_g.png";
            case TEX_YELLOW_LINE_INNER_BL:
                return AppletMain.DATA_DIR + "textures/2ln_crn_sw_g.png";
            case TEX_YELLOW_LINE_OUTER_BL:
                return AppletMain.DATA_DIR + "textures/2ln_crnw_ne_g.png";
            case TEX_YELLOW_LINE_OUTER_BR:
                return AppletMain.DATA_DIR + "textures/2ln_crnw_nw_g.png";
            case TEX_YELLOW_LINE_OUTER_TL:
                return AppletMain.DATA_DIR + "textures/2ln_crnw_se_g.png";
            case TEX_YELLOW_LINE_OUTER_TR:
                return AppletMain.DATA_DIR + "textures/2ln_crnw_sw_g.png";
            case TEX_YELLOW_LINE_L:
                return AppletMain.DATA_DIR + "textures/2ln_l.png";
            case TEX_YELLOW_LINE_R:
                return AppletMain.DATA_DIR + "textures/2ln_r.png";
            case TEX_YELLOW_LINE_T:
                return AppletMain.DATA_DIR + "textures/2ln_t.png";
            case TEX_YELLOW_LINE_B:
                return AppletMain.DATA_DIR + "textures/2ln_b.png";
            case TEX_ALIEN_SKIN:
                return AppletMain.DATA_DIR + "textures/alienskin2.jpg";
            case TEX_BULKHEAD:
                return AppletMain.DATA_DIR + "textures/bulkhead.jpg";
            case TEX_SPACESHIP_WALL:
                return AppletMain.DATA_DIR + "textures/spaceship_wall.png";
            case TEX_TELEPORTER:
                return AppletMain.DATA_DIR + "textures/teleporter.jpg";
            case TEX_COUCH:
                return AppletMain.DATA_DIR + "textures/couch_e.jpg";
            case TEX_METAL_FLOOR5:
                return AppletMain.DATA_DIR + "textures/floor5.jpg";
            case TEX_METAL_FLOOR6:
                return AppletMain.DATA_DIR + "textures/floor006.png";
            case TEX_METAL_FLOOR15:
                return AppletMain.DATA_DIR + "textures/floor015.png";
            case TEX_HEDGE:
                return AppletMain.DATA_DIR + "textures/hedge02.jpg";
            case TEX_WOODEN_FENCE:
                return AppletMain.DATA_DIR + "";
            case TEX_METAL_FLOOR41:
                return AppletMain.DATA_DIR + "textures/floor0041.png";
            case TEX_LAB_FLOOR1:
                return AppletMain.DATA_DIR + "textures/lab_floor1.jpg";
            case TEX_LAB_FLOOR2:
                return AppletMain.DATA_DIR + "textures/lab_floor2.png";
            case TEX_ROAD_WHITE_LINE_LR:
                return AppletMain.DATA_DIR + "textures/street001_wl_lr.jpg";
            case TEX_ROAD_WHITE_LINE_UD:
                return AppletMain.DATA_DIR + "textures/street001_wl_ud.jpg";
            case TEX_SECTOR2:
                return AppletMain.DATA_DIR + "textures/sector2.png";
            case TEX_SECTOR3:
                return AppletMain.DATA_DIR + "textures/sector3.png";
            case TEX_SECTOR4:
                return AppletMain.DATA_DIR + "textures/sector4.png";
            case TEX_ESCAPE_HATCH:
                return AppletMain.DATA_DIR + "textures/escape_hatch.jpg";
            case TEX_BRICKS:
                return AppletMain.DATA_DIR + "textures/bricks.jpg";
            case TEX_WHITE_LINE_LR:
                return AppletMain.DATA_DIR + "textures/white_line_lr.png";
            case TEX_WHITE_LINE_UD:
                return AppletMain.DATA_DIR + "textures/white_line_ud.png";
            default:
                System.err.println("Warning: Unknown texture code: " + tex_code);
                return AppletMain.DATA_DIR + "textures/metalfloor1.jpg";
        }
    }

    public TextureState get(String tex) {
        while (cache.containsKey(tex) == false) {
            TextureState ts = main.getDisplay().getRenderer().createTextureState();
            ts.setEnabled(true);
            Texture t = TextureManager.loadTexture(tex, Texture.MinificationFilter.NearestNeighborLinearMipMap, Texture.MagnificationFilter.NearestNeighbor);
            ts.setTexture(t, 0);
            cache.put(tex, ts);
        }
        return cache.get(tex);
    }
}
