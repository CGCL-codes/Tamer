package com.l2jserver.gameserver.model;

/**
 * This class ...
 *
 * @version $Revision: 1.3 $ $Date: 2004/10/23 22:12:44 $
 */
public class L2Macro {

    public static final int CMD_TYPE_SKILL = 1;

    public static final int CMD_TYPE_ACTION = 3;

    public static final int CMD_TYPE_SHORTCUT = 4;

    public int id;

    public final int icon;

    public final String name;

    public final String descr;

    public final String acronym;

    public final L2MacroCmd[] commands;

    public static class L2MacroCmd {

        public final int entry;

        public final int type;

        public final int d1;

        public final int d2;

        public final String cmd;

        public L2MacroCmd(int pEntry, int pType, int pD1, int pD2, String pCmd) {
            entry = pEntry;
            type = pType;
            d1 = pD1;
            d2 = pD2;
            cmd = pCmd;
        }
    }

    /**
     *
     */
    public L2Macro(int pId, int pIcon, String pName, String pDescr, String pAcronym, L2MacroCmd[] pCommands) {
        id = pId;
        icon = pIcon;
        name = pName;
        descr = pDescr;
        acronym = pAcronym;
        commands = pCommands;
    }
}
