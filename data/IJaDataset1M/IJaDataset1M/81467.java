package nl.huub.van.amelsvoort.client;

import nl.huub.van.amelsvoort.Defines;
import nl.huub.van.amelsvoort.Globals;
import nl.huub.van.amelsvoort.game.*;
import nl.huub.van.amelsvoort.qcommon.*;
import nl.huub.van.amelsvoort.spel.HuubModelStatus;
import nl.huub.van.amelsvoort.util.Math3D;

public class CL_ents {

    static int bitcounts[] = new int[32];

    static int bfg_lightramp[] = { 300, 400, 600, 300, 150, 75 };

    public static int ParseEntityBits(int bits[]) {
        int b, total;
        int i;
        int number;
        total = MSG.ReadByte(Globals.net_message);
        if ((total & Defines.U_MOREBITS1) != 0) {
            b = MSG.ReadByte(Globals.net_message);
            total |= b << 8;
        }
        if ((total & Defines.U_MOREBITS2) != 0) {
            b = MSG.ReadByte(Globals.net_message);
            total |= b << 16;
        }
        if ((total & Defines.U_MOREBITS3) != 0) {
            b = MSG.ReadByte(Globals.net_message);
            total |= b << 24;
        }
        for (i = 0; i < 32; i++) {
            if ((total & (1 << i)) != 0) {
                bitcounts[i]++;
            }
        }
        if ((total & Defines.U_NUMBER16) != 0) {
            number = MSG.ReadShort(Globals.net_message);
        } else {
            number = MSG.ReadByte(Globals.net_message);
        }
        bits[0] = total;
        return number;
    }

    public static void ParseDelta(HuubModelStatus from, HuubModelStatus to, int number, int bits) {
        to.set(from);
        Math3D.VectorCopy(from.oorsprong, to.oudeOorsprong);
        to.number = number;
        if ((bits & Defines.U_MODEL) != 0) {
            to.modelindex = MSG.ReadByte(Globals.net_message);
        }
        if ((bits & Defines.U_MODEL2) != 0) {
            to.modelindex2 = MSG.ReadByte(Globals.net_message);
        }
        if ((bits & Defines.U_MODEL3) != 0) {
            to.modelindex3 = MSG.ReadByte(Globals.net_message);
        }
        if ((bits & Defines.U_MODEL4) != 0) {
            to.modelindex4 = MSG.ReadByte(Globals.net_message);
        }
        if ((bits & Defines.U_FRAME8) != 0) {
            to.frame = MSG.ReadByte(Globals.net_message);
        }
        if ((bits & Defines.U_FRAME16) != 0) {
            to.frame = MSG.ReadShort(Globals.net_message);
        }
        if ((bits & Defines.U_SKIN8) != 0 && (bits & Defines.U_SKIN16) != 0) {
            to.skinnum = MSG.ReadLong(Globals.net_message);
        } else if ((bits & Defines.U_SKIN8) != 0) {
            to.skinnum = MSG.ReadByte(Globals.net_message);
        } else if ((bits & Defines.U_SKIN16) != 0) {
            to.skinnum = MSG.ReadShort(Globals.net_message);
        }
        if ((bits & (Defines.U_EFFECTS8 | Defines.U_EFFECTS16)) == (Defines.U_EFFECTS8 | Defines.U_EFFECTS16)) {
            to.effects = MSG.ReadLong(Globals.net_message);
        } else if ((bits & Defines.U_EFFECTS8) != 0) {
            to.effects = MSG.ReadByte(Globals.net_message);
        } else if ((bits & Defines.U_EFFECTS16) != 0) {
            to.effects = MSG.ReadShort(Globals.net_message);
        }
        if ((bits & (Defines.U_RENDERFX8 | Defines.U_RENDERFX16)) == (Defines.U_RENDERFX8 | Defines.U_RENDERFX16)) {
            to.makenFx = MSG.ReadLong(Globals.net_message);
        } else if ((bits & Defines.U_RENDERFX8) != 0) {
            to.makenFx = MSG.ReadByte(Globals.net_message);
        } else if ((bits & Defines.U_RENDERFX16) != 0) {
            to.makenFx = MSG.ReadShort(Globals.net_message);
        }
        if ((bits & Defines.U_ORIGIN1) != 0) {
            to.oorsprong[0] = MSG.ReadCoord(Globals.net_message);
        }
        if ((bits & Defines.U_ORIGIN2) != 0) {
            to.oorsprong[1] = MSG.ReadCoord(Globals.net_message);
        }
        if ((bits & Defines.U_ORIGIN3) != 0) {
            to.oorsprong[2] = MSG.ReadCoord(Globals.net_message);
        }
        if ((bits & Defines.U_ANGLE1) != 0) {
            to.hoeken[0] = MSG.ReadAngle(Globals.net_message);
        }
        if ((bits & Defines.U_ANGLE2) != 0) {
            to.hoeken[1] = MSG.ReadAngle(Globals.net_message);
        }
        if ((bits & Defines.U_ANGLE3) != 0) {
            to.hoeken[2] = MSG.ReadAngle(Globals.net_message);
        }
        if ((bits & Defines.U_OLDORIGIN) != 0) {
            MSG.ReadPos(Globals.net_message, to.oudeOorsprong);
        }
        if ((bits & Defines.U_SOUND) != 0) {
            to.sound = MSG.ReadByte(Globals.net_message);
        }
        if ((bits & Defines.U_EVENT) != 0) {
            to.event = MSG.ReadByte(Globals.net_message);
        } else {
            to.event = 0;
        }
        if ((bits & Defines.U_SOLID) != 0) {
            to.solid = MSG.ReadShort(Globals.net_message);
        }
    }

    public static void DeltaEntity(frame_t frame, int newnum, HuubModelStatus old, int bits) {
        centity_t ent;
        HuubModelStatus state;
        ent = Globals.cl_entities[newnum];
        state = Globals.cl_parse_entities[Globals.cl.parse_entities & (Defines.MAX_PARSE_ENTITIES - 1)];
        Globals.cl.parse_entities++;
        frame.num_entities++;
        ParseDelta(old, state, newnum, bits);
        if (state.modelindex != ent.current.modelindex || state.modelindex2 != ent.current.modelindex2 || state.modelindex3 != ent.current.modelindex3 || state.modelindex4 != ent.current.modelindex4 || Math.abs(state.oorsprong[0] - ent.current.oorsprong[0]) > 512 || Math.abs(state.oorsprong[1] - ent.current.oorsprong[1]) > 512 || Math.abs(state.oorsprong[2] - ent.current.oorsprong[2]) > 512 || state.event == Defines.EV_PLAYER_TELEPORT || state.event == Defines.EV_OTHER_TELEPORT) {
            ent.serverframe = -99;
        }
        if (ent.serverframe != Globals.cl.frame.serverframe - 1) {
            ent.trailcount = 1024;
            ent.prev.set(state);
            if (state.event == Defines.EV_OTHER_TELEPORT) {
                Math3D.VectorCopy(state.oorsprong, ent.prev.oorsprong);
                Math3D.VectorCopy(state.oorsprong, ent.lerp_origin);
            } else {
                Math3D.VectorCopy(state.oudeOorsprong, ent.prev.oorsprong);
                Math3D.VectorCopy(state.oudeOorsprong, ent.lerp_origin);
            }
        } else {
            ent.prev.set(ent.current);
        }
        ent.serverframe = Globals.cl.frame.serverframe;
        ent.current.set(state);
    }

    private static final int[] iw = { 0 };

    public static void ParsePacketEntities(frame_t oldframe, frame_t newframe) {
        int newnum;
        int bits = 0;
        HuubModelStatus oldstate = null;
        int oldnum;
        newframe.parse_entities = Globals.cl.parse_entities;
        newframe.num_entities = 0;
        int oldindex = 0;
        if (oldframe == null) {
            oldnum = 99999;
        } else {
            oldstate = Globals.cl_parse_entities[(oldframe.parse_entities + oldindex) & (Defines.MAX_PARSE_ENTITIES - 1)];
            oldnum = oldstate.number;
        }
        while (true) {
            iw[0] = bits;
            newnum = ParseEntityBits(iw);
            bits = iw[0];
            if (newnum >= Defines.MAX_EDICTS) {
                Com.Error(Defines.ERR_DROP, "CL_ParsePacketEntities: bad number:" + newnum);
            }
            if (Globals.net_message.readcount > Globals.net_message.cursize) {
                Com.Error(Defines.ERR_DROP, "CL_ParsePacketEntities: end of message");
            }
            if (0 == newnum) {
                break;
            }
            while (oldnum < newnum) {
                if (Globals.cl_shownet.value == 3) {
                    Com.Printf("   unchanged: " + oldnum + "\n");
                }
                DeltaEntity(newframe, oldnum, oldstate, 0);
                oldindex++;
                if (oldindex >= oldframe.num_entities) {
                    oldnum = 99999;
                } else {
                    oldstate = Globals.cl_parse_entities[(oldframe.parse_entities + oldindex) & (Defines.MAX_PARSE_ENTITIES - 1)];
                    oldnum = oldstate.number;
                }
            }
            if ((bits & Defines.U_REMOVE) != 0) {
                if (Globals.cl_shownet.value == 3) {
                    Com.Printf("   remove: " + newnum + "\n");
                }
                if (oldnum != newnum) {
                    Com.Printf("U_REMOVE: oldnum != newnum\n");
                }
                oldindex++;
                if (oldindex >= oldframe.num_entities) {
                    oldnum = 99999;
                } else {
                    oldstate = Globals.cl_parse_entities[(oldframe.parse_entities + oldindex) & (Defines.MAX_PARSE_ENTITIES - 1)];
                    oldnum = oldstate.number;
                }
                continue;
            }
            if (oldnum == newnum) {
                if (Globals.cl_shownet.value == 3) {
                    Com.Printf("   delta: " + newnum + "\n");
                }
                DeltaEntity(newframe, newnum, oldstate, bits);
                oldindex++;
                if (oldindex >= oldframe.num_entities) {
                    oldnum = 99999;
                } else {
                    oldstate = Globals.cl_parse_entities[(oldframe.parse_entities + oldindex) & (Defines.MAX_PARSE_ENTITIES - 1)];
                    oldnum = oldstate.number;
                }
                continue;
            }
            if (oldnum > newnum) {
                if (Globals.cl_shownet.value == 3) {
                    Com.Printf("   baseline: " + newnum + "\n");
                }
                DeltaEntity(newframe, newnum, Globals.cl_entities[newnum].baseline, bits);
                continue;
            }
        }
        while (oldnum != 99999) {
            if (Globals.cl_shownet.value == 3) {
                Com.Printf("   unchanged: " + oldnum + "\n");
            }
            DeltaEntity(newframe, oldnum, oldstate, 0);
            oldindex++;
            if (oldindex >= oldframe.num_entities) {
                oldnum = 99999;
            } else {
                oldstate = Globals.cl_parse_entities[(oldframe.parse_entities + oldindex) & (Defines.MAX_PARSE_ENTITIES - 1)];
                oldnum = oldstate.number;
            }
        }
    }

    public static void ParsePlayerstate(frame_t oldframe, frame_t newframe) {
        int flags;
        player_state_t state;
        int i;
        int statbits;
        state = newframe.playerstate;
        if (oldframe != null) {
            state.set(oldframe.playerstate);
        } else {
            state.clear();
        }
        flags = MSG.ReadShort(Globals.net_message);
        if ((flags & Defines.PS_M_TYPE) != 0) {
            state.pmove.pm_type = MSG.ReadByte(Globals.net_message);
        }
        if ((flags & Defines.PS_M_ORIGIN) != 0) {
            state.pmove.origin[0] = MSG.ReadShort(Globals.net_message);
            state.pmove.origin[1] = MSG.ReadShort(Globals.net_message);
            state.pmove.origin[2] = MSG.ReadShort(Globals.net_message);
        }
        if ((flags & Defines.PS_M_VELOCITY) != 0) {
            state.pmove.velocity[0] = MSG.ReadShort(Globals.net_message);
            state.pmove.velocity[1] = MSG.ReadShort(Globals.net_message);
            state.pmove.velocity[2] = MSG.ReadShort(Globals.net_message);
        }
        if ((flags & Defines.PS_M_TIME) != 0) {
            state.pmove.pm_time = (byte) MSG.ReadByte(Globals.net_message);
        }
        if ((flags & Defines.PS_M_FLAGS) != 0) {
            state.pmove.pm_flags = (byte) MSG.ReadByte(Globals.net_message);
        }
        if ((flags & Defines.PS_M_GRAVITY) != 0) {
            state.pmove.gravity = MSG.ReadShort(Globals.net_message);
        }
        if ((flags & Defines.PS_M_DELTA_ANGLES) != 0) {
            state.pmove.delta_angles[0] = MSG.ReadShort(Globals.net_message);
            state.pmove.delta_angles[1] = MSG.ReadShort(Globals.net_message);
            state.pmove.delta_angles[2] = MSG.ReadShort(Globals.net_message);
        }
        if (Globals.cl.attractloop) {
            state.pmove.pm_type = Defines.PM_FREEZE;
        }
        if ((flags & Defines.PS_VIEWOFFSET) != 0) {
            state.viewoffset[0] = MSG.ReadChar(Globals.net_message) * 0.25f;
            state.viewoffset[1] = MSG.ReadChar(Globals.net_message) * 0.25f;
            state.viewoffset[2] = MSG.ReadChar(Globals.net_message) * 0.25f;
        }
        if ((flags & Defines.PS_VIEWANGLES) != 0) {
            state.viewangles[0] = MSG.ReadAngle16(Globals.net_message);
            state.viewangles[1] = MSG.ReadAngle16(Globals.net_message);
            state.viewangles[2] = MSG.ReadAngle16(Globals.net_message);
        }
        if ((flags & Defines.PS_KICKANGLES) != 0) {
            state.kick_angles[0] = MSG.ReadChar(Globals.net_message) * 0.25f;
            state.kick_angles[1] = MSG.ReadChar(Globals.net_message) * 0.25f;
            state.kick_angles[2] = MSG.ReadChar(Globals.net_message) * 0.25f;
        }
        if ((flags & Defines.PS_WEAPONINDEX) != 0) {
            state.gunindex = MSG.ReadByte(Globals.net_message);
        }
        if ((flags & Defines.PS_WEAPONFRAME) != 0) {
            state.gunframe = MSG.ReadByte(Globals.net_message);
            state.gunoffset[0] = MSG.ReadChar(Globals.net_message) * 0.25f;
            state.gunoffset[1] = MSG.ReadChar(Globals.net_message) * 0.25f;
            state.gunoffset[2] = MSG.ReadChar(Globals.net_message) * 0.25f;
            state.gunangles[0] = MSG.ReadChar(Globals.net_message) * 0.25f;
            state.gunangles[1] = MSG.ReadChar(Globals.net_message) * 0.25f;
            state.gunangles[2] = MSG.ReadChar(Globals.net_message) * 0.25f;
        }
        if ((flags & Defines.PS_BLEND) != 0) {
            state.blend[0] = MSG.ReadByte(Globals.net_message) / 255.0f;
            state.blend[1] = MSG.ReadByte(Globals.net_message) / 255.0f;
            state.blend[2] = MSG.ReadByte(Globals.net_message) / 255.0f;
            state.blend[3] = MSG.ReadByte(Globals.net_message) / 255.0f;
        }
        if ((flags & Defines.PS_FOV) != 0) {
            state.fov = MSG.ReadByte(Globals.net_message);
        }
        if ((flags & Defines.PS_RDFLAGS) != 0) {
            state.rdflags = MSG.ReadByte(Globals.net_message);
        }
        statbits = MSG.ReadLong(Globals.net_message);
        for (i = 0; i < Defines.MAX_STATS; i++) {
            if ((statbits & (1 << i)) != 0) {
                state.stats[i] = MSG.ReadShort(Globals.net_message);
            }
        }
    }

    public static void FireEntityEvents(frame_t frame) {
        HuubModelStatus s1;
        int pnum, num;
        for (pnum = 0; pnum < frame.num_entities; pnum++) {
            num = (frame.parse_entities + pnum) & (Defines.MAX_PARSE_ENTITIES - 1);
            s1 = Globals.cl_parse_entities[num];
            if (s1.event != 0) {
                CL_fx.EntityEvent(s1);
            }
            if ((s1.effects & Defines.EF_TELEPORTER) != 0) {
                CL_fx.TeleporterParticles(s1);
            }
        }
    }

    public static void ParseFrame() {
        int cmd;
        int len;
        frame_t old;
        Globals.cl.frame.reset();
        Globals.cl.frame.serverframe = MSG.ReadLong(Globals.net_message);
        Globals.cl.frame.deltaframe = MSG.ReadLong(Globals.net_message);
        Globals.cl.frame.servertime = Globals.cl.frame.serverframe * 100;
        if (Globals.cls.serverProtocol != 26) {
            Globals.cl.surpressCount = MSG.ReadByte(Globals.net_message);
        }
        if (Globals.cl_shownet.value == 3) {
            Com.Printf("   frame:" + Globals.cl.frame.serverframe + "  delta:" + Globals.cl.frame.deltaframe + "\n");
        }
        if (Globals.cl.frame.deltaframe <= 0) {
            Globals.cl.frame.valid = true;
            old = null;
            Globals.cls.demowaiting = false;
        } else {
            old = Globals.cl.frames[Globals.cl.frame.deltaframe & Defines.UPDATE_MASK];
            if (!old.valid) {
                Com.Printf("Delta from invalid frame (not supposed to happen!).\n");
            }
            if (old.serverframe != Globals.cl.frame.deltaframe) {
                Com.Printf("Delta frame too old.\n");
            } else if (Globals.cl.parse_entities - old.parse_entities > Defines.MAX_PARSE_ENTITIES - 128) {
                Com.Printf("Delta parse_entities too old.\n");
            } else {
                Globals.cl.frame.valid = true;
            }
        }
        if (Globals.cl.time > Globals.cl.frame.servertime) {
            Globals.cl.time = Globals.cl.frame.servertime;
        } else if (Globals.cl.time < Globals.cl.frame.servertime - 100) {
            Globals.cl.time = Globals.cl.frame.servertime - 100;
        }
        len = MSG.ReadByte(Globals.net_message);
        MSG.ReadData(Globals.net_message, Globals.cl.frame.areabits, len);
        cmd = MSG.ReadByte(Globals.net_message);
        CL_parse.SHOWNET(CL_parse.svc_strings[cmd]);
        if (cmd != Defines.svc_playerinfo) {
            Com.Error(Defines.ERR_DROP, "CL_ParseFrame: not playerinfo");
        }
        ParsePlayerstate(old, Globals.cl.frame);
        cmd = MSG.ReadByte(Globals.net_message);
        CL_parse.SHOWNET(CL_parse.svc_strings[cmd]);
        if (cmd != Defines.svc_packetentities) {
            Com.Error(Defines.ERR_DROP, "CL_ParseFrame: not packetentities");
        }
        ParsePacketEntities(old, Globals.cl.frame);
        Globals.cl.frames[Globals.cl.frame.serverframe & Defines.UPDATE_MASK].set(Globals.cl.frame);
        if (Globals.cl.frame.valid) {
            if (Globals.cls.state != Defines.ca_active) {
                Globals.cls.state = Defines.ca_active;
                Globals.cl.force_refdef = true;
                Globals.cl.predicted_origin[0] = Globals.cl.frame.playerstate.pmove.origin[0] * 0.125f;
                Globals.cl.predicted_origin[1] = Globals.cl.frame.playerstate.pmove.origin[1] * 0.125f;
                Globals.cl.predicted_origin[2] = Globals.cl.frame.playerstate.pmove.origin[2] * 0.125f;
                Math3D.VectorCopy(Globals.cl.frame.playerstate.viewangles, Globals.cl.predicted_angles);
                if (Globals.cls.disable_servercount != Globals.cl.servercount && Globals.cl.refresh_prepped) {
                    SCR.EndLoadingPlaque();
                }
            }
            Globals.cl.sound_prepped = true;
            FireEntityEvents(Globals.cl.frame);
            CL_pred.CheckPredictionError();
        }
    }

    private static final entity_t ent = new entity_t();

    static void AddPacketEntities(frame_t frame) {
        HuubModelStatus s1;
        float autorotate;
        int i;
        int pnum;
        centity_t cent;
        int autoanim;
        clientinfo_t ci;
        int effects, renderfx;
        autorotate = Math3D.anglemod(Globals.cl.time / 10);
        autoanim = 2 * Globals.cl.time / 1000;
        ent.clear();
        for (pnum = 0; pnum < frame.num_entities; pnum++) {
            s1 = Globals.cl_parse_entities[(frame.parse_entities + pnum) & (Defines.MAX_PARSE_ENTITIES - 1)];
            cent = Globals.cl_entities[s1.number];
            effects = s1.effects;
            renderfx = s1.makenFx;
            if ((effects & Defines.EF_ANIM01) != 0) {
                ent.frame = autoanim & 1;
            } else if ((effects & Defines.EF_ANIM23) != 0) {
                ent.frame = 2 + (autoanim & 1);
            } else if ((effects & Defines.EF_ANIM_ALL) != 0) {
                ent.frame = autoanim;
            } else if ((effects & Defines.EF_ANIM_ALLFAST) != 0) {
                ent.frame = Globals.cl.time / 100;
            } else {
                ent.frame = s1.frame;
            }
            if ((effects & Defines.EF_PENT) != 0) {
                effects &= ~Defines.EF_PENT;
                effects |= Defines.EF_COLOR_SHELL;
                renderfx |= Defines.RF_SHELL_RED;
            }
            if ((effects & Defines.EF_QUAD) != 0) {
                effects &= ~Defines.EF_QUAD;
                effects |= Defines.EF_COLOR_SHELL;
                renderfx |= Defines.RF_SHELL_BLUE;
            }
            if ((effects & Defines.EF_DOUBLE) != 0) {
                effects &= ~Defines.EF_DOUBLE;
                effects |= Defines.EF_COLOR_SHELL;
                renderfx |= Defines.RF_SHELL_DOUBLE;
            }
            if ((effects & Defines.EF_HALF_DAMAGE) != 0) {
                effects &= ~Defines.EF_HALF_DAMAGE;
                effects |= Defines.EF_COLOR_SHELL;
                renderfx |= Defines.RF_SHELL_HALF_DAM;
            }
            ent.oldframe = cent.prev.frame;
            ent.backlerp = 1.0f - Globals.cl.lerpfrac;
            if ((renderfx & (Defines.RF_FRAMELERP | Defines.RF_BEAM)) != 0) {
                Math3D.VectorCopy(cent.current.oorsprong, ent.origin);
                Math3D.VectorCopy(cent.current.oudeOorsprong, ent.oldorigin);
            } else {
                for (i = 0; i < 3; i++) {
                    ent.origin[i] = ent.oldorigin[i] = cent.prev.oorsprong[i] + Globals.cl.lerpfrac * (cent.current.oorsprong[i] - cent.prev.oorsprong[i]);
                }
            }
            if ((renderfx & Defines.RF_BEAM) != 0) {
                ent.alpha = 0.30f;
                ent.skinnum = (s1.skinnum >> ((Globals.rnd.nextInt(4)) * 8)) & 0xff;
                Math.random();
                ent.model = null;
            } else {
                if (s1.modelindex == 255) {
                    ent.skinnum = 0;
                    ci = Globals.cl.clientinfo[s1.skinnum & 0xff];
                    ent.skin = ci.skin;
                    ent.model = ci.model;
                    if (null == ent.skin || null == ent.model) {
                        ent.skin = Globals.cl.baseclientinfo.skin;
                        ent.model = Globals.cl.baseclientinfo.model;
                    }
                    if ((renderfx & Defines.RF_USE_DISGUISE) != 0) {
                        if (ent.skin.name.startsWith("spelers/mannen")) {
                            ent.skin = Globals.re.RegisterSkin("spelers/mannen/disguise.pcx");
                            ent.model = Globals.re.RegisterModel("spelers/mannen/tris.md2");
                        } else if (ent.skin.name.startsWith("spelers/female")) {
                            ent.skin = Globals.re.RegisterSkin("spelers/vrouwen/disguise.pcx");
                            ent.model = Globals.re.RegisterModel("spelers/vrouwen/tris.md2");
                        } else if (ent.skin.name.startsWith("spelers/cyborg")) {
                            ent.skin = Globals.re.RegisterSkin("spelers/cyborg/disguise.pcx");
                            ent.model = Globals.re.RegisterModel("spelers/cyborg/tris.md2");
                        }
                    }
                } else {
                    ent.skinnum = s1.skinnum;
                    ent.skin = null;
                    ent.model = Globals.cl.model_draw[s1.modelindex];
                }
            }
            if (renderfx == Defines.RF_TRANSLUCENT) {
                ent.alpha = 0.70f;
            }
            if ((effects & Defines.EF_COLOR_SHELL) != 0) {
                ent.flags = 0;
            } else {
                ent.flags = renderfx;
            }
            if ((effects & Defines.EF_ROTATE) != 0) {
                ent.angles[0] = 0;
                ent.angles[1] = autorotate;
                ent.angles[2] = 0;
            } else if ((effects & Defines.EF_SPINNINGLIGHTS) != 0) {
                ent.angles[0] = 0;
                ent.angles[1] = Math3D.anglemod(Globals.cl.time / 2) + s1.hoeken[1];
                ent.angles[2] = 180;
                {
                    float[] forward = { 0, 0, 0 };
                    float[] start = { 0, 0, 0 };
                    Math3D.AngleVectors(ent.angles, forward, null, null);
                    Math3D.VectorMA(ent.origin, 64, forward, start);
                    V.AddLight(start, 100, 1, 0, 0);
                }
            } else {
                float a1, a2;
                for (i = 0; i < 3; i++) {
                    a1 = cent.current.hoeken[i];
                    a2 = cent.prev.hoeken[i];
                    ent.angles[i] = Math3D.LerpAngle(a2, a1, Globals.cl.lerpfrac);
                }
            }
            if (s1.number == Globals.cl.playernum + 1) {
                ent.flags |= Defines.RF_VIEWERMODEL;
                if ((effects & Defines.EF_FLAG1) != 0) {
                    V.AddLight(ent.origin, 225, 1.0f, 0.1f, 0.1f);
                } else if ((effects & Defines.EF_FLAG2) != 0) {
                    V.AddLight(ent.origin, 225, 0.1f, 0.1f, 1.0f);
                } else if ((effects & Defines.EF_TAGTRAIL) != 0) {
                    V.AddLight(ent.origin, 225, 1.0f, 1.0f, 0.0f);
                } else if ((effects & Defines.EF_TRACKERTRAIL) != 0) {
                    V.AddLight(ent.origin, 225, -1.0f, -1.0f, -1.0f);
                }
                continue;
            }
            if (s1.modelindex == 0) {
                continue;
            }
            if ((effects & Defines.EF_BFG) != 0) {
                ent.flags |= Defines.RF_TRANSLUCENT;
                ent.alpha = 0.30f;
            }
            if ((effects & Defines.EF_PLASMA) != 0) {
                ent.flags |= Defines.RF_TRANSLUCENT;
                ent.alpha = 0.6f;
            }
            if ((effects & Defines.EF_SPHERETRANS) != 0) {
                ent.flags |= Defines.RF_TRANSLUCENT;
                if ((effects & Defines.EF_TRACKERTRAIL) != 0) {
                    ent.alpha = 0.6f;
                } else {
                    ent.alpha = 0.3f;
                }
            }
            V.AddEntity(ent);
            if ((effects & Defines.EF_COLOR_SHELL) != 0) {
                if ((renderfx & Defines.RF_SHELL_HALF_DAM) != 0) {
                    if (FS.Developer_searchpath(2) == 2) {
                        if ((renderfx & (Defines.RF_SHELL_RED | Defines.RF_SHELL_BLUE | Defines.RF_SHELL_DOUBLE)) != 0) {
                            renderfx &= ~Defines.RF_SHELL_HALF_DAM;
                        }
                    }
                }
                if ((renderfx & Defines.RF_SHELL_DOUBLE) != 0) {
                    if (FS.Developer_searchpath(2) == 2) {
                        if ((renderfx & (Defines.RF_SHELL_RED | Defines.RF_SHELL_BLUE | Defines.RF_SHELL_GREEN)) != 0) {
                            renderfx &= ~Defines.RF_SHELL_DOUBLE;
                        }
                        if ((renderfx & Defines.RF_SHELL_RED) != 0) {
                            renderfx |= Defines.RF_SHELL_BLUE;
                        } else if ((renderfx & Defines.RF_SHELL_BLUE) != 0) {
                            if ((renderfx & Defines.RF_SHELL_GREEN) != 0) {
                                renderfx &= ~Defines.RF_SHELL_BLUE;
                            } else {
                                renderfx |= Defines.RF_SHELL_GREEN;
                            }
                        }
                    }
                }
                ent.flags = renderfx | Defines.RF_TRANSLUCENT;
                ent.alpha = 0.30f;
                V.AddEntity(ent);
            }
            ent.skin = null;
            ent.skinnum = 0;
            ent.flags = 0;
            ent.alpha = 0;
            if (s1.modelindex2 != 0) {
                if (s1.modelindex2 == 255) {
                    ci = Globals.cl.clientinfo[s1.skinnum & 0xff];
                    i = (s1.skinnum >> 8);
                    if (0 == Globals.cl_vwep.value || i > Defines.MAX_CLIENTWEAPONMODELS - 1) {
                        i = 0;
                    }
                    ent.model = ci.weaponmodel[i];
                    if (null == ent.model) {
                        if (i != 0) {
                            ent.model = ci.weaponmodel[0];
                        }
                        if (null == ent.model) {
                            ent.model = Globals.cl.baseclientinfo.weaponmodel[0];
                        }
                    }
                } else {
                    ent.model = Globals.cl.model_draw[s1.modelindex2];
                }
                if (Globals.cl.configstrings[Defines.CS_MODELS + (s1.modelindex2)].equalsIgnoreCase("modellen/items/shell/tris.md2")) {
                    ent.alpha = 0.32f;
                    ent.flags = Defines.RF_TRANSLUCENT;
                }
                V.AddEntity(ent);
                ent.flags = 0;
                ent.alpha = 0;
            }
            if (s1.modelindex3 != 0) {
                ent.model = Globals.cl.model_draw[s1.modelindex3];
                V.AddEntity(ent);
            }
            if (s1.modelindex4 != 0) {
                ent.model = Globals.cl.model_draw[s1.modelindex4];
                V.AddEntity(ent);
            }
            if ((effects & Defines.EF_POWERSCREEN) != 0) {
                ent.model = CL_tent.cl_mod_powerscreen;
                ent.oldframe = 0;
                ent.frame = 0;
                ent.flags |= (Defines.RF_TRANSLUCENT | Defines.RF_SHELL_GREEN);
                ent.alpha = 0.30f;
                V.AddEntity(ent);
            }
            if ((effects & ~Defines.EF_ROTATE) != 0) {
                if ((effects & Defines.EF_ROCKET) != 0) {
                    CL_fx.RocketTrail(cent.lerp_origin, ent.origin, cent);
                    V.AddLight(ent.origin, 200, 1, 1, 0);
                } else if ((effects & Defines.EF_BLASTER) != 0) {
                    if ((effects & Defines.EF_TRACKER) != 0) {
                        CL_newfx.BlasterTrail2(cent.lerp_origin, ent.origin);
                        V.AddLight(ent.origin, 200, 0, 1, 0);
                    } else {
                        CL_fx.BlasterTrail(cent.lerp_origin, ent.origin);
                        V.AddLight(ent.origin, 200, 1, 1, 0);
                    }
                } else if ((effects & Defines.EF_HYPERBLASTER) != 0) {
                    if ((effects & Defines.EF_TRACKER) != 0) {
                        V.AddLight(ent.origin, 200, 0, 1, 0);
                    } else {
                        V.AddLight(ent.origin, 200, 1, 1, 0);
                    }
                } else if ((effects & Defines.EF_GIB) != 0) {
                    CL_fx.DiminishingTrail(cent.lerp_origin, ent.origin, cent, effects);
                } else if ((effects & Defines.EF_GRENADE) != 0) {
                    CL_fx.DiminishingTrail(cent.lerp_origin, ent.origin, cent, effects);
                } else if ((effects & Defines.EF_FLIES) != 0) {
                    CL_fx.FlyEffect(cent, ent.origin);
                } else if ((effects & Defines.EF_BFG) != 0) {
                    if ((effects & Defines.EF_ANIM_ALLFAST) != 0) {
                        CL_fx.BfgParticles(ent);
                        i = 200;
                    } else {
                        i = bfg_lightramp[s1.frame];
                    }
                    V.AddLight(ent.origin, i, 0, 1, 0);
                } else if ((effects & Defines.EF_TRAP) != 0) {
                    ent.origin[2] += 32;
                    CL_fx.TrapParticles(ent);
                    i = (Globals.rnd.nextInt(100)) + 100;
                    V.AddLight(ent.origin, i, 1, 0.8f, 0.1f);
                } else if ((effects & Defines.EF_FLAG1) != 0) {
                    CL_fx.FlagTrail(cent.lerp_origin, ent.origin, 242);
                    V.AddLight(ent.origin, 225, 1, 0.1f, 0.1f);
                } else if ((effects & Defines.EF_FLAG2) != 0) {
                    CL_fx.FlagTrail(cent.lerp_origin, ent.origin, 115);
                    V.AddLight(ent.origin, 225, 0.1f, 0.1f, 1);
                } else if ((effects & Defines.EF_TAGTRAIL) != 0) {
                    CL_newfx.TagTrail(cent.lerp_origin, ent.origin, 220);
                    V.AddLight(ent.origin, 225, 1.0f, 1.0f, 0.0f);
                } else if ((effects & Defines.EF_TRACKERTRAIL) != 0) {
                    if ((effects & Defines.EF_TRACKER) != 0) {
                        float intensity;
                        intensity = (float) (50 + (500 * (Math.sin(Globals.cl.time / 500.0) + 1.0)));
                        if (Globals.vidref_val == Defines.VIDREF_GL) {
                            V.AddLight(ent.origin, intensity, -1.0f, -1.0f, -1.0f);
                        } else {
                            V.AddLight(ent.origin, -1.0f * intensity, 1.0f, 1.0f, 1.0f);
                        }
                    } else {
                        CL_newfx.Tracker_Shell(cent.lerp_origin);
                        V.AddLight(ent.origin, 155, -1.0f, -1.0f, -1.0f);
                    }
                } else if ((effects & Defines.EF_TRACKER) != 0) {
                    CL_newfx.TrackerTrail(cent.lerp_origin, ent.origin, 0);
                    if (Globals.vidref_val == Defines.VIDREF_GL) {
                        V.AddLight(ent.origin, 200, -1, -1, -1);
                    } else {
                        V.AddLight(ent.origin, -200, 1, 1, 1);
                    }
                } else if ((effects & Defines.EF_GREENGIB) != 0) {
                    CL_fx.DiminishingTrail(cent.lerp_origin, ent.origin, cent, effects);
                } else if ((effects & Defines.EF_IONRIPPER) != 0) {
                    CL_fx.IonripperTrail(cent.lerp_origin, ent.origin);
                    V.AddLight(ent.origin, 100, 1, 0.5f, 0.5f);
                } else if ((effects & Defines.EF_BLUEHYPERBLASTER) != 0) {
                    V.AddLight(ent.origin, 200, 0, 0, 1);
                } else if ((effects & Defines.EF_PLASMA) != 0) {
                    if ((effects & Defines.EF_ANIM_ALLFAST) != 0) {
                        CL_fx.BlasterTrail(cent.lerp_origin, ent.origin);
                    }
                    V.AddLight(ent.origin, 130, 1, 0.5f, 0.5f);
                }
            }
            Math3D.VectorCopy(ent.origin, cent.lerp_origin);
        }
    }

    private static final entity_t gun = new entity_t();

    static void AddViewWeapon(player_state_t ps, player_state_t ops) {
        int i;
        if (0 == Globals.cl_gun.value) {
            return;
        }
        if (ps.fov > 90) {
            return;
        }
        gun.clear();
        if (Globals.gun_model != null) {
            gun.model = Globals.gun_model;
        } else {
            gun.model = Globals.cl.model_draw[ps.gunindex];
        }
        if (gun.model == null) {
            return;
        }
        for (i = 0; i < 3; i++) {
            gun.origin[i] = Globals.cl.refdef.vieworg[i] + ops.gunoffset[i] + Globals.cl.lerpfrac * (ps.gunoffset[i] - ops.gunoffset[i]);
            gun.angles[i] = Globals.cl.refdef.viewangles[i] + Math3D.LerpAngle(ops.gunangles[i], ps.gunangles[i], Globals.cl.lerpfrac);
        }
        if (Globals.gun_frame != 0) {
            gun.frame = Globals.gun_frame;
            gun.oldframe = Globals.gun_frame;
        } else {
            gun.frame = ps.gunframe;
            if (gun.frame == 0) {
                gun.oldframe = 0;
            } else {
                gun.oldframe = ops.gunframe;
            }
        }
        gun.flags = Defines.RF_MINLIGHT | Defines.RF_DEPTHHACK | Defines.RF_WEAPONMODEL;
        gun.backlerp = 1.0f - Globals.cl.lerpfrac;
        Math3D.VectorCopy(gun.origin, gun.oldorigin);
        V.AddEntity(gun);
    }

    static void CalcViewValues() {
        int i;
        float lerp, backlerp;
        frame_t oldframe;
        player_state_t ps, ops;
        ps = Globals.cl.frame.playerstate;
        i = (Globals.cl.frame.serverframe - 1) & Defines.UPDATE_MASK;
        oldframe = Globals.cl.frames[i];
        if (oldframe.serverframe != Globals.cl.frame.serverframe - 1 || !oldframe.valid) {
            oldframe = Globals.cl.frame;
        }
        ops = oldframe.playerstate;
        if (Math.abs(ops.pmove.origin[0] - ps.pmove.origin[0]) > 256 * 8 || Math.abs(ops.pmove.origin[1] - ps.pmove.origin[1]) > 256 * 8 || Math.abs(ops.pmove.origin[2] - ps.pmove.origin[2]) > 256 * 8) {
            ops = ps;
        }
        lerp = Globals.cl.lerpfrac;
        if ((Globals.cl_predict.value != 0) && 0 == (Globals.cl.frame.playerstate.pmove.pm_flags & pmove_t.PMF_NO_PREDICTION)) {
            int delta;
            backlerp = 1.0f - lerp;
            for (i = 0; i < 3; i++) {
                Globals.cl.refdef.vieworg[i] = Globals.cl.predicted_origin[i] + ops.viewoffset[i] + Globals.cl.lerpfrac * (ps.viewoffset[i] - ops.viewoffset[i]) - backlerp * Globals.cl.prediction_error[i];
            }
            delta = (int) (Globals.cls.realtime - Globals.cl.predicted_step_time);
            if (delta < 100) {
                Globals.cl.refdef.vieworg[2] -= Globals.cl.predicted_step * (100 - delta) * 0.01;
            }
        } else {
            for (i = 0; i < 3; i++) {
                Globals.cl.refdef.vieworg[i] = ops.pmove.origin[i] * 0.125f + ops.viewoffset[i] + lerp * (ps.pmove.origin[i] * 0.125f + ps.viewoffset[i] - (ops.pmove.origin[i] * 0.125f + ops.viewoffset[i]));
            }
        }
        if (Globals.cl.frame.playerstate.pmove.pm_type < Defines.PM_DEAD) {
            for (i = 0; i < 3; i++) {
                Globals.cl.refdef.viewangles[i] = Globals.cl.predicted_angles[i];
            }
        } else {
            for (i = 0; i < 3; i++) {
                Globals.cl.refdef.viewangles[i] = Math3D.LerpAngle(ops.viewangles[i], ps.viewangles[i], lerp);
            }
        }
        for (i = 0; i < 3; i++) {
            Globals.cl.refdef.viewangles[i] += Math3D.LerpAngle(ops.kick_angles[i], ps.kick_angles[i], lerp);
        }
        Math3D.AngleVectors(Globals.cl.refdef.viewangles, Globals.cl.v_forward, Globals.cl.v_right, Globals.cl.v_up);
        Globals.cl.refdef.fov_x = ops.fov + lerp * (ps.fov - ops.fov);
        for (i = 0; i < 4; i++) {
            Globals.cl.refdef.blend[i] = ps.blend[i];
        }
        AddViewWeapon(ps, ops);
    }

    static void AddEntities() {
        if (Globals.cls.state != Defines.ca_active) {
            return;
        }
        if (Globals.cl.time > Globals.cl.frame.servertime) {
            if (Globals.cl_showclamp.value != 0) {
                Com.Printf("high clamp " + (Globals.cl.time - Globals.cl.frame.servertime) + "\n");
            }
            Globals.cl.time = Globals.cl.frame.servertime;
            Globals.cl.lerpfrac = 1.0f;
        } else if (Globals.cl.time < Globals.cl.frame.servertime - 100) {
            if (Globals.cl_showclamp.value != 0) {
                Com.Printf("low clamp " + (Globals.cl.frame.servertime - 100 - Globals.cl.time) + "\n");
            }
            Globals.cl.time = Globals.cl.frame.servertime - 100;
            Globals.cl.lerpfrac = 0;
        } else {
            Globals.cl.lerpfrac = 1.0f - (Globals.cl.frame.servertime - Globals.cl.time) * 0.01f;
        }
        if (Globals.cl_timedemo.value != 0) {
            Globals.cl.lerpfrac = 1.0f;
        }
        CalcViewValues();
        AddPacketEntities(Globals.cl.frame);
        CL_tent.AddTEnts();
        CL_fx.AddParticles();
        CL_fx.AddDLights();
        CL_fx.AddLightStyles();
    }

    public static void GetEntitySoundOrigin(int ent, float[] org) {
        centity_t old;
        if (ent < 0 || ent >= Defines.MAX_EDICTS) {
            Com.Error(Defines.ERR_DROP, "CL_GetEntitySoundOrigin: bad ent");
        }
        old = Globals.cl_entities[ent];
        Math3D.VectorCopy(old.lerp_origin, org);
    }
}
