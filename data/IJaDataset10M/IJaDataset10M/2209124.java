package nl.huub.van.amelsvoort.client;

import nl.huub.van.amelsvoort.Defines;
import nl.huub.van.amelsvoort.Globals;
import nl.huub.van.amelsvoort.qcommon.CM;
import nl.huub.van.amelsvoort.qcommon.Com;
import nl.huub.van.amelsvoort.sys.Sys;
import java.util.StringTokenizer;
import nl.huub.van.amelsvoort.HuubHelp;

public class CL_view {

    static int num_cl_weaponmodels;

    static String[] cl_weaponmodels = new String[Defines.MAX_CLIENTWEAPONMODELS];

    static void PrepRefresh() {
        String mapname;
        int i;
        String name;
        float rotate;
        float[] axis = new float[3];
        if ((i = Globals.cl.configstrings[Defines.CS_MODELS + 1].length()) == 0) {
            return;
        }
        SCR.AddDirtyPoint(0, 0);
        SCR.AddDirtyPoint(Globals.viddef.getWidth() - 1, Globals.viddef.getHeight() - 1);
        if (HuubHelp.isLog) {
            HuubHelp.log("De naam van de map wordt samengesteld : " + Globals.cl.configstrings[Defines.CS_MODELS + 1], HuubHelp.MOMENTEEL_BELANGRIJK);
        }
        int mapsMappen = 7;
        if (Globals.cl.configstrings[Defines.CS_MODELS + 1].contains("maps")) {
            mapsMappen = 5;
            mapname = Globals.cl.configstrings[Defines.CS_MODELS + 1].substring(mapsMappen, i - 4);
        } else {
            mapname = Globals.cl.configstrings[Defines.CS_MODELS + 1].substring(mapsMappen, i - 4);
        }
        Com.Printf("CL_view Map naam : " + mapname + "\r\n");
        SCR.UpdateScreen();
        Globals.re.BeginRegistration(mapname);
        Com.Printf("                                     \r");
        Com.Printf("films\r");
        SCR.UpdateScreen();
        SCR.TouchPics();
        Com.Printf("                                     \r");
        CL_tent.RegisterTEntModels();
        num_cl_weaponmodels = 1;
        cl_weaponmodels[0] = "weapon.md2";
        for (i = 1; i < Defines.MAX_MODELS && Globals.cl.configstrings[Defines.CS_MODELS + i].length() != 0; i++) {
            name = new String(Globals.cl.configstrings[Defines.CS_MODELS + i]);
            if (name.length() > 37) {
                name = name.substring(0, 36);
            }
            if (name.charAt(0) != '*') {
                if (HuubHelp.isLog) {
                    HuubHelp.log("CL_view geen ster : " + name, HuubHelp.MOMENTEEL_BELANGRIJK);
                }
                Com.Printf("CL_view.java : " + name + "\r");
            }
            SCR.UpdateScreen();
            Sys.SendKeyEvents();
            if (name.charAt(0) == '#') {
                if (HuubHelp.isLog) {
                    HuubHelp.log("CL_view hekje : " + name, HuubHelp.MOMENTEEL_BELANGRIJK);
                }
                if (num_cl_weaponmodels < Defines.MAX_CLIENTWEAPONMODELS) {
                    cl_weaponmodels[num_cl_weaponmodels] = Globals.cl.configstrings[Defines.CS_MODELS + i].substring(1);
                    num_cl_weaponmodels++;
                }
            } else {
                Globals.cl.model_draw[i] = Globals.re.RegisterModel(Globals.cl.configstrings[Defines.CS_MODELS + i]);
                if (name.charAt(0) == '*') {
                    Globals.cl.model_clip[i] = CM.InlineModel(Globals.cl.configstrings[Defines.CS_MODELS + i]);
                } else {
                    Globals.cl.model_clip[i] = null;
                }
            }
            if (name.charAt(0) != '*') {
                Com.Printf("                                     \r");
            }
        }
        Com.Printf("CL_view => images\r");
        SCR.UpdateScreen();
        for (i = 1; i < Defines.MAX_IMAGES && Globals.cl.configstrings[Defines.CS_IMAGES + i].length() > 0; i++) {
            Globals.cl.image_precache[i] = Globals.re.RegisterPic(Globals.cl.configstrings[Defines.CS_IMAGES + i]);
            Sys.SendKeyEvents();
        }
        Com.Printf("                                     \r");
        for (i = 0; i < Defines.MAX_CLIENTS; i++) {
            if (Globals.cl.configstrings[Defines.CS_PLAYERSKINS + i].length() == 0) {
                continue;
            }
            Com.Printf("client " + i + '\r');
            SCR.UpdateScreen();
            Sys.SendKeyEvents();
            CL_parse.ParseClientinfo(i);
            Com.Printf("                                     \r");
        }
        CL_parse.LoadClientinfo(Globals.cl.baseclientinfo, "unnamed\\mannen/grunt");
        Com.Printf("sky\r");
        SCR.UpdateScreen();
        Com.Printf("                                     \r");
        Globals.re.EndRegistration();
        Console.ClearNotify();
        SCR.UpdateScreen();
        Globals.cl.refresh_prepped = true;
        Globals.cl.force_refdef = true;
    }

    public static void AddNetgraph() {
        int i;
        int in;
        int ping;
        if (SCR.scr_debuggraph.value == 0.0f || SCR.scr_timegraph.value == 0.0f) {
            return;
        }
        for (i = 0; i < Globals.cls.netchan.dropped; i++) {
            SCR.DebugGraph(30, 0x40);
        }
        for (i = 0; i < Globals.cl.surpressCount; i++) {
            SCR.DebugGraph(30, 0xdf);
        }
        in = Globals.cls.netchan.incoming_acknowledged & (Defines.CMD_BACKUP - 1);
        ping = (int) (Globals.cls.realtime - Globals.cl.cmd_time[in]);
        ping /= 30;
        if (ping > 30) {
            ping = 30;
        }
        SCR.DebugGraph(ping, 0xd0);
    }
}
