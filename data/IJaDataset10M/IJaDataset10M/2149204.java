package net.sf.gridarta.var.atrinik;

import net.sf.gridarta.gui.utils.GUIConstants;
import net.sf.gridarta.utils.SystemIcons;

/**
 * Defines common UI constants used in different dialogs and all used icon
 * files.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 */
public interface IGUIConstants extends GUIConstants {

    /**
     * Default difficulty for newly created maps.
     */
    int DEF_MAPDIFFICULTY = 1;

    /**
     * Default map size (both height and width).
     */
    int DEF_MAPSIZE = 24;

    /**
     * Default width for pickmaps.
     */
    int DEF_PICKMAP_WIDTH = 7;

    /**
     * Default height for pickmaps.
     */
    int DEF_PICKMAP_HEIGHT = 7;

    /**
     * The directory that contains all scripts.
     */
    String SCRIPTS_DIR = "dev/editor/scripts";

    /**
     * The directory that contains all pickmaps.
     */
    String PICKMAP_DIR = "dev/editor/pickmaps";

    String ARCHDEF_FILE = "archdef.dat";

    String ARCH_FILE = "archetypes";

    /**
     * File to store the animation tree information after arch collection.
     */
    String ANIMTREE_FILE = "animtree";

    String ARTIFACTS_FILE = "artifacts";

    String TILE_NORTH = SystemIcons.SYSTEM_DIR + "north.png";

    /**
     * Name of the files the spell information (names and numbers).
     */
    String SPELL_FILE = "spells.xml";
}
