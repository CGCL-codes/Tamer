package com.mw3d.swt.util;

/**
 * Application properties. Properties will be settings like which
 * models is supported in this applications.
 * 
 * @author ndebruyn
 * Created on May 26, 2005
 */
public class AppProperties {

    public static final String PATH_LOGO = "data/images/splash.jpg";

    public static final String PATH_SELECT = "data/images/select.gif";

    public static final String PATH_TRANS = "data/images/move.gif";

    public static final String PATH_ROT = "data/images/rotate.gif";

    public static final String PATH_SCALE = "data/images/scale.gif";

    public static final String PATH_CAM_ON_TERRAIN = "data/images/camonterrain.gif";

    public static final String PATH_CAM_BUMP_TERRAIN = "data/images/cambumpterrain.gif";

    public static final String PATH_CAM_FREE = "data/images/camfree.gif";

    public static final String PATH_FREE_OBJ = "data/images/freeobj.gif";

    public static final String PATH_STICK_OBJ = "data/images/stickobj.gif";

    public static final String PATH_WIRE = "data/images/wire.gif";

    public static final String PATH_TEXTURE = "data/images/texture.gif";

    public static final String PATH_2D_VIEW = "data/images/2Dview.gif";

    public static final String PATH_SCRIPT = "data/images/script.gif";

    public static final String PATH_ADD = "data/images/add.png";

    public static final String PATH_GRASS = "data/textures/grass.png";

    public static final String PATH_TERRAIN_DEFAULT = "data/textures/default.jpg";

    public static final String PATH_SKY_DEFAULT = "data/textures/nopreview.jpg";

    public static final String PATH_DEFAULT_FONT = "data/textures/defaultfont.tga";

    public static final String PATH_DEFAULT_PARTICLE_IMGAGE = "data/textures/flaresmall.jpg";

    public static String modelPath = null;

    public static String[] getLoadModelFormats() {
        return new String[] { "*.jme" };
    }

    public static String[] getImportModelFormats() {
        return new String[] { "*.jme", "*.md2", "*.md3", "*.3ds", "*.ms3d", "*.obj", "*.ase", "*.*" };
    }

    public static String[] getLoadTextureFormats() {
        return new String[] { "*.*", "*.png", "*.jpg", "*.jpeg", "*.bmp" };
    }

    public static String[] getLoadSoundFormates() {
        return new String[] { "*.wav", "*.ogg", "*.mp3" };
    }

    public static String[] getLoadScriptFormates() {
        return new String[] { "*.bsh" };
    }

    public static boolean isSupportedModelFormat(String modelfile) {
        boolean succ = false;
        if (modelfile.endsWith(".jme") || modelfile.endsWith(".JME")) {
            succ = true;
        }
        return succ;
    }

    public static boolean isSupportedScriptFormat(String scriptfile) {
        boolean succ = false;
        if (scriptfile.endsWith(".bsh") || scriptfile.endsWith(".BSH")) {
            succ = true;
        }
        return succ;
    }

    public static boolean isSupportedSoundFormat(String textfile) {
        boolean succ = false;
        String[] tex = getLoadSoundFormates();
        for (int i = 0; i < tex.length; i++) {
            if (textfile.endsWith(tex[i].substring(tex[i].length() - 3, tex[i].length())) || textfile.endsWith(tex[i].substring(tex[i].length() - 3, tex[i].length()).toUpperCase())) {
                succ = true;
            }
        }
        return succ;
    }

    public static boolean isSupportedTextureFormat(String textfile) {
        boolean succ = false;
        String[] tex = getLoadTextureFormats();
        for (int i = 0; i < tex.length; i++) {
            if (textfile.endsWith(tex[i].substring(tex[i].length() - 3, tex[i].length())) || textfile.endsWith(tex[i].substring(tex[i].length() - 3, tex[i].length()).toUpperCase())) {
                succ = true;
            }
        }
        return succ;
    }

    public static String getScriptTemplate() {
        String template = "//Please add you functions and calls inside the update method.\n" + "//Update must always be in the script file.\n" + "//Availible object:\n" + "//   - properties : Properties\n" + "//   - entity : Entity\n" + "//   - entities : List\n" + "//   - cosality : List of entities in the space\n" + "//   - tpf : float\n" + "//   - out : OutputStream\n\n" + "init() {\n" + "\t//Initialization method called first.\n" + "\t\n" + "}\n\n" + "update() {\n" + "\t//Call method here.\n" + "\t\n" + "}\n";
        return template;
    }

    public static String getModelPath() {
        return modelPath;
    }

    public static void setModelPath(String modelPath) {
        AppProperties.modelPath = modelPath;
    }
}
