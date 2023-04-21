package com.sf.pac.auto.riven;

import java.awt.*;
import java.io.*;
import java.util.Arrays;
import javax.sound.sampled.*;
import javax.swing.*;
import com.sf.pac.*;
import com.sf.pac.man.CursorManager;
import com.sf.pac.ui.LAFChooser;
import com.sf.pac.ui.Operation;
import com.sf.pac.ui.PACApplication;
import com.sf.pac.util.Reader;

public class MHKInfo {

    Mohawk[] m;

    private static boolean[] print = { true, false, false, false };

    private static JFrame frame = null;

    public MHKInfo(Mohawk mo) {
        m = new Mohawk[] { mo };
    }

    public MHKInfo(Mohawk[] mo) {
        m = mo;
    }

    public static Object info(Mohawk mo, String srt, int rn, int d, boolean fi) throws IOException {
        return new MHKInfo(mo).info(srt, rn, d, fi);
    }

    public static byte[] info(Mohawk mo, int f) throws IOException {
        return new MHKInfo(new Mohawk[] { mo }).info(f);
    }

    public Object info(int arc, String srt, int rn, int d, boolean fi) throws IOException {
        for (int i = 0; i < arc; i++) if (m[i].rlookup(srt) != null) rn += m[i].rlookup(srt).rte;
        return info(srt, rn, d, fi);
    }

    public Object info(String srt, int rn, int d, boolean fi) throws IOException {
        Object[] t = getRes(srt, rn);
        if (t == null) throw new FileNotFoundException();
        int f = (Integer) t[0], srn = (Integer) t[3];
        Mohawk.ResourceType rt = (Mohawk.ResourceType) t[1];
        Mohawk.Resource res = (Mohawk.Resource) t[2];
        if (fi) println(1, "archive " + m[f] + " - " + rt.name + " " + srn + " - file " + res.fid);
        int fid = res.fid;
        boolean v1 = print[1], v2 = print[2];
        if (!fi) print[1] = print[2] = false;
        boolean isBigRes = in(srt, new String[] { "tBMP", "tWAV", "tMOV", "PICT", "WDIB", "MSND" });
        byte[] b = info(f, fid, isBigRes ? 0x100 : -1);
        Reader r = isBigRes ? new Reader.FileReader(m[f].file, m[f].fi[fid].off, m[f].fi[fid].siz) : new Reader.ArrayReader(b);
        if (d > 1) {
            print[1] = v1;
            print[2] = v2;
        }
        try {
            if (d == 0) return res; else if (Mohawk.equals(srt, "BLST")) parse_BLST(r, rn); else if (Mohawk.equals(srt, "CARD")) return parse_CARD(r, rn, f, srn); else if (Mohawk.equals(srt, "NAME")) return parse_NAME(r, rn); else if (Mohawk.equals(srt, "HSPT")) return parse_HSPT(r, rn, f, srn); else if (Mohawk.equals(srt, "FLST")) parse_FLST(r, rn); else if (Mohawk.equals(srt, "PLST")) return parse_PLST(r, rn); else if (Mohawk.equals(srt, "tBMP")) return parse_tBMP(r, rn); else if (Mohawk.equals(srt, "tWAV")) parse_tWAV(r, rn); else if (Mohawk.equals(srt, "VARS")) return parse_VARS(r, rn); else if (Mohawk.equals(srt, "CLRC")) parse_CLRC(r, rn); else if (Mohawk.equals(srt, "EXIT")) parse_EXIT(r, rn); else if (Mohawk.equals(srt, "HELP")) parse_HELP(r, rn); else if (Mohawk.equals(srt, "HINT")) parse_HINT(r, rn); else if (Mohawk.equals(srt, "INIT")) parse_INIT(r, rn); else if (Mohawk.equals(srt, "MJMP")) parse_MJMP(r, rn); else if (Mohawk.equals(srt, "MSND")) parse_tWAV(r, rn); else if (Mohawk.equals(srt, "PICT")) parse_PICT(r, rn); else if (Mohawk.equals(srt, "RLST")) parse_RLST(r, rn); else if (Mohawk.equals(srt, "VIEW")) parse_VIEW(r, rn); else if (Mohawk.equals(srt, "WDIB")) parse_WDIB(r, rn); else println(1, "unknown type " + rt.name);
        } finally {
            print[1] = v1;
            print[2] = v2;
        }
        return null;
    }

    public byte[] info(int fid) throws IOException {
        return info(0, fid, -1);
    }

    public byte[] info(int arc, int fid) throws IOException {
        return info(arc, fid, -1);
    }

    public byte[] info(int arc, int fid, int len) throws IOException {
        Mohawk.FileInfo h = m[arc].fi[fid];
        if (h.name == null) print(1, "<no name>"); else print(1, h.name.name);
        println(1, " flags " + Mohawk.bin(h.flags & 0xFF, 8) + " offset " + Mohawk.hex(h.off) + " size " + Mohawk.hex(h.siz) + "\n");
        m[arc].f.go_to(h.off);
        byte[] b = m[arc].f.read(len == -1 ? h.siz : len);
        println(2, "data:");
        present(b, 0, b.length > 0x100 ? 0x100 : b.length);
        if (b.length < h.siz) println(2, "...");
        return b;
    }

    private Hotspot[] parse_HSPT(Reader r, int rn, int arc, int srn) throws IOException {
        short record_count = r.readShort();
        println(1, record_count + " hotspots");
        Hotspot[] hspt = new Hotspot[record_count];
        for (int i = 0; i < record_count; i++) {
            hspt[i] = new Hotspot();
            hspt[i].blst_id = r.readShort();
            hspt[i].name_rec = r.readShort();
            short L = r.readShort();
            short T = r.readShort();
            short R = r.readShort();
            short B = r.readShort();
            hspt[i].bounds = new Rectangle(L, T, R - L, B - T);
            r.skip(2);
            hspt[i].cur = r.readShort();
            r.skip(6);
            println(1, 1, "blst=" + hspt[i].blst_id);
            println(1, 1, "name=" + hspt[i].name_rec);
            println(1, 1, "bounds=<" + L + "," + T + "," + (R - L) + "," + (B - T) + ">");
            println(1, 1, "cur=" + hspt[i].cur);
            hspt[i].hand = parseHandlers(r, 1, rn, arc, srn);
        }
        return hspt;
    }

    private Handler[] parse_CARD(Reader r, int rn, int arc, int srn) throws IOException {
        short name_rec = r.readShort();
        r.skip(2);
        println(1, 1, "name=" + name_rec);
        Handler[] hand = parseHandlers(r, 1, rn, arc, srn);
        return hand;
    }

    private Handler[] parseHandlers(Reader r, int q, int rn, int arc, int srn) throws IOException {
        short handler_count = r.readShort();
        Handler[] hand = new Handler[handler_count];
        for (int i = 0; i < handler_count; i++) {
            hand[i] = new Handler();
            hand[i].event_type = r.readShort();
            short cmd_count = r.readShort();
            println(1, q + 1, "event=" + hand[i].event_type + " (" + new String[] { "mouse pressed in rect", "mouse pressed in rect (phantom)", "mouse released in rect", "mouse moved, pressed, or released in rect", "mouse held in rect", "mouse moved in rect", "card loaded", "leaving card", "?", "card opened", "display update" }[hand[i].event_type] + ")");
            hand[i].cmds = new Command[cmd_count];
            for (int j = 0; j < cmd_count; j++) {
                hand[i].cmds[j] = parseCommand(r, q + 2, rn, arc, srn);
            }
        }
        return hand;
    }

    public String var = "adoit", ext = "xblabopenbook";

    private Command parseCommand(Reader r, int q, int rn, int arc, int srn) throws IOException {
        short command = r.readShort();
        if (command == 8) {
            SwitchCmd sc = new SwitchCmd();
            r.skip(2);
            sc.var = r.readShort();
            if (sc.var == 0) sc.var = 0;
            try {
                String s = getVarName(arc, sc.var);
                println(2, "(" + rn + ") " + m[arc] + " " + srn + " switch(" + s + ")");
                println(1, q, "switch on " + Mohawk.hex(sc.var) + " = " + s);
            } catch (IOException e) {
            }
            short value_count = r.readShort();
            sc.vals = new SwitchVal[value_count];
            for (int i = 0; i < value_count; i++) {
                sc.vals[i] = new SwitchVal();
                sc.vals[i].value = r.readShort();
                println(1, q + 1, (sc.vals[i].value == -1) ? "default" : "case " + sc.vals[i].value);
                short cmd_count = r.readShort();
                sc.vals[i].cmds = new Command[cmd_count];
                for (int j = 0; j < cmd_count; j++) {
                    sc.vals[i].cmds[j] = parseCommand(r, q + 2, rn, arc, srn);
                }
            }
            return sc;
        } else {
            ParamCmd pc = new ParamCmd();
            pc.cmd = command;
            short arg_count = r.readShort();
            pc.args = new short[arg_count];
            for (int i = 0; i < arg_count; i++) {
                pc.args[i] = r.readShort();
            }
            try {
                if (pc.cmd == 2) println(1, q, "goto card " + pc.args[0]); else if (pc.cmd == 4) {
                    String s = "play ";
                    try {
                        s += m[arc].fi[m[arc].rlookup("tWAV").res[pc.args[0]].fid].name.name;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        s += "<nonexistent>";
                    }
                    println(1, q, s + " volume=" + pc.args[1] + " u=" + pc.args[2]);
                } else if (pc.cmd == 7) {
                    String s = getVarName(arc, pc.args[0]);
                    println(2, "(" + rn + ") " + m[arc] + " " + srn + " " + s + "=" + pc.args[1]);
                    println(1, q, s + "=" + pc.args[1]);
                } else if (pc.cmd == 13) println(1, q, "cursor " + new String[][] { {}, { null, null, "zip" }, { null, null, null, "open", "closed" }, { "pointer", "left", "right", "down", "up", "curled left", "curled right", "flipped down" }, { null, "red", "orange", "yellow", "green", "blue", "purple" }, { "black dot" }, {}, {}, {}, { "blank" } }[pc.args[0] / 1000][pc.args[0] % 1000]); else if (pc.cmd == 17) {
                    String name = ((String[]) info(arc, "NAME", 2, 1, false))[pc.args[0]];
                    String s = "";
                    for (int i = 0; i < pc.args[1]; i++) s += "," + pc.args[i + 2];
                    if (name.equals(ext)) println(2, rn + " " + m[arc] + " " + srn);
                    println(1, q, name + "(" + (pc.args[1] == 0 ? "" : s.substring(1)) + ")");
                } else if (pc.cmd == 18) println(1, q, "transition " + new String[] { "right", "left", "down", "up", "right", "left", "down", "up", "right", "left", "down", "up", "right", "left", "down", "up", "dissolve", "dissolve 2" }[pc.args[0]] + (pc.args.length == 5 ? " <" + pc.args[1] + "," + pc.args[2] + "," + (pc.args[3] - pc.args[1]) + "," + (pc.args[4] - pc.args[2]) + ">" : "")); else if (pc.cmd == 19) println(1, q, "reload"); else if (pc.cmd == 24) {
                    String s = getVarName(arc, pc.args[0]);
                    println(2, "(" + rn + ") " + m[arc] + " " + srn + " " + s + "+=" + pc.args[1]);
                    println(1, q, s + "+=" + pc.args[1]);
                } else if (pc.cmd == 32) {
                    String s = "play ";
                    try {
                        s += m[arc].fi[m[arc].rlookup("tMOV").res[pc.args[0]].fid].name.name;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        s += "<nonexistent>";
                    }
                    println(1, q, s + " in foreground");
                } else if (pc.cmd == 33) {
                    String s = "play ";
                    try {
                        s += m[arc].fi[m[arc].rlookup("tMOV").res[pc.args[0]].fid].name.name;
                    } catch (ArrayIndexOutOfBoundsException e) {
                        s += "tMOV " + pc.args[0] + " - <nonexistent>";
                    }
                    println(1, q, s + " in background");
                } else if (pc.cmd == 39) {
                    print(1, q, "draw PLST " + pc.args[0] + " - ");
                    try {
                        Object[] t = getRes("tBMP", ((PLSTrec[]) info("PLST", rn, 1, false))[pc.args[0] - 1].bmp);
                        Mohawk.FileInfo f = m[(Integer) t[0]].fi[((Mohawk.Resource) t[2]).fid];
                        println(1, f.name == null ? "<unnamed>" : f.name.name);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        println(1, "<nonexistent>");
                    }
                } else if (pc.cmd == 44) println(1, q, "activate FLST " + pc.args[0]); else {
                    print(1, q, "" + pc.cmd);
                    for (int i = 0; i < arg_count; i++) print(1, " " + pc.args[i]);
                    println(1, " (?)");
                }
                if (pc.cmd == 7 || pc.cmd == 24) {
                    Object[] o = getRes("HSPT", rn);
                    String s = getVarName((Integer) o[0], pc.args[0]);
                    if (s.equals(var)) println(2, "(" + rn + ") " + m[(Integer) o[0]] + " " + (Integer) o[3] + " " + s + (pc.cmd == 7 ? "=" : "+=") + pc.args[1]);
                }
            } catch (IOException e) {
            }
            return pc;
        }
    }

    /**
	 * Thanks to <a href="http://homepage.mac.com/rshayter/Riveal.html">Ron
	 * Hayter</a> for allowing me to adapt his code to decompress the Riven
	 * images.
	 */
    private Image parse_tBMP(Reader r, int rn) throws IOException {
        short width = r.readShort();
        short height = r.readShort();
        byte comp = r.readByte();
        byte tru = r.readByte();
        int[] pix = new int[width * height];
        if (tru == 4) for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) pix[i * width + j] = 0xFF << 24 | (r.readByte() & 0xFF) | ((r.readByte() & 0xFF) << 8) | ((r.readByte() & 0xFF) << 16);
            r.skip(width);
        } else {
            r.skip(4);
            int[] clr = new int[256];
            for (int i = 0; i < 256; i++) {
                clr[i] = 0xFF << 24 | (r.readByte() & 0xFF) | ((r.readByte() & 0xFF) << 8) | ((r.readByte() & 0xFF) << 16);
            }
            byte[] pixi = new byte[width * height];
            if (comp == 0) for (int i = 0; i < width * height; i++) pixi[i] = r.readByte(); else if (comp == 4) {
                r.skip(4);
                int n = 0;
                while (n < width * height) {
                    int cmd = r.readByte() & 0xFF;
                    if (cmd == 0) break;
                    if (cmd > 0x00 && cmd < 0x40) for (int i = 0; i < 2 * cmd; i++) pixi[n++] = r.readByte(); else if (cmd > 0x40 && cmd < 0x80) {
                        int nPairs = cmd - 0x40;
                        for (int i = 0; i < 2 * nPairs; i++) pixi[n] = pixi[(n++) - 2];
                    } else if (cmd > 0x80 && cmd < 0xC0) {
                        int nQuads = cmd - 0x80;
                        for (int i = 0; i < 4 * nQuads; i++) pixi[n] = pixi[(n++) - 4];
                    } else if (cmd > 0xC0 && cmd <= 0xFF) {
                        int nSubs = cmd - 0xC0;
                        for (int i = 0; i < nSubs; i++) {
                            int sub = r.readByte() & 0xFF;
                            if (sub > 0x00 && sub < 0x10) {
                                int back = n - 2 * sub;
                                pixi[n++] = pixi[back++];
                                pixi[n++] = pixi[back++];
                            } else if (sub == 0x10) {
                                pixi[n] = pixi[(n++) - 2];
                                pixi[n++] = r.readByte();
                            } else if (sub > 0x10 && sub < 0x20) {
                                int back = sub & 0x0F;
                                pixi[n] = pixi[(n++) - 2];
                                pixi[n] = pixi[(n++) - back];
                            } else if (sub > 0x20 && sub < 0x30) {
                                int delta = sub & 0x0F;
                                pixi[n] = pixi[(n++) - 2];
                                pixi[n] = (byte) (pixi[(n++) - 2] + delta);
                            } else if (sub > 0x30 && sub < 0x40) {
                                int delta = sub & 0x0F;
                                pixi[n] = pixi[(n++) - 2];
                                pixi[n] = (byte) (pixi[(n++) - 2] - delta);
                            } else if (sub == 0x40) {
                                pixi[n++] = r.readByte();
                                pixi[n] = pixi[(n++) - 2];
                            } else if (sub > 0x40 && sub < 0x50) {
                                int back = sub & 0x0F;
                                pixi[n] = pixi[(n++) - back];
                                pixi[n] = pixi[(n++) - 2];
                            } else if (sub == 0x50) {
                                pixi[n++] = r.readByte();
                                pixi[n++] = r.readByte();
                            } else if (sub > 0x50 && sub < 0x58) {
                                int back = sub & 0x07;
                                pixi[n] = pixi[(n++) - back];
                                pixi[n++] = r.readByte();
                            } else if (sub > 0x58 && sub < 0x60) {
                                int back = sub & 0x07;
                                pixi[n++] = r.readByte();
                                pixi[n] = pixi[(n++) - back];
                            } else if (sub > 0x60 && sub < 0x70) {
                                int delta = sub & 0x0F;
                                pixi[n++] = r.readByte();
                                pixi[n] = (byte) (pixi[(n++) - 2] + delta);
                            } else if (sub > 0x70 && sub < 0x80) {
                                int delta = sub & 0x0F;
                                pixi[n++] = r.readByte();
                                pixi[n] = (byte) (pixi[(n++) - 2] - delta);
                            } else if (sub > 0x80 && sub < 0x90) {
                                int delta = sub & 0x0F;
                                pixi[n] = (byte) (pixi[(n++) - 2] + delta);
                                pixi[n] = pixi[(n++) - 2];
                            } else if (sub > 0x90 && sub < 0xA0) {
                                int delta = sub & 0x0F;
                                pixi[n] = (byte) (pixi[(n++) - 2] + delta);
                                pixi[n++] = r.readByte();
                            } else if (sub == 0xA0) {
                                int deltas = r.readByte() & 0xFF;
                                pixi[n] = (byte) (pixi[(n++) - 2] + (deltas >> 4));
                                pixi[n] = (byte) (pixi[(n++) - 2] + (deltas & 0x0F));
                            } else if (sub >= 0xA4 && sub < 0xA8) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 3; j++) pixi[n] = pixi[(n++) - back];
                                pixi[n++] = r.readByte();
                            } else if (sub >= 0xA8 && sub < 0xAC) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 4; j++) pixi[n] = pixi[(n++) - back];
                            } else if (sub >= 0xAC && sub < 0xB0) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 5; j++) pixi[n] = pixi[(n++) - back];
                                pixi[n++] = r.readByte();
                            } else if (sub == 0xB0) {
                                int deltas = r.readByte() & 0xFF;
                                pixi[n] = (byte) (pixi[(n++) - 2] + (deltas >> 4));
                                pixi[n] = (byte) (pixi[(n++) - 2] - (deltas & 0x0F));
                            } else if (sub >= 0xB4 && sub < 0xB8) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 6; j++) pixi[n] = pixi[(n++) - back];
                            } else if (sub >= 0xB8 && sub < 0xBC) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 7; j++) pixi[n] = pixi[(n++) - back];
                                pixi[n++] = r.readByte();
                            } else if (sub >= 0xBC && sub < 0xC0) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 8; j++) pixi[n] = pixi[(n++) - back];
                            } else if (sub > 0xC0 && sub < 0xD0) {
                                int delta = sub & 0x0F;
                                pixi[n] = (byte) (pixi[(n++) - 2] - delta);
                                pixi[n] = pixi[(n++) - 2];
                            } else if (sub > 0xD0 && sub < 0xE0) {
                                int delta = sub & 0x0F;
                                pixi[n] = (byte) (pixi[(n++) - 2] - delta);
                                pixi[n++] = r.readByte();
                            } else if (sub == 0xE0) {
                                int deltas = r.readByte() & 0xFF;
                                pixi[n] = (byte) (pixi[(n++) - 2] - (deltas >> 4));
                                pixi[n] = (byte) (pixi[(n++) - 2] + (deltas & 0x0F));
                            } else if (sub >= 0xE4 && sub < 0xE8) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 9; j++) pixi[n] = pixi[(n++) - back];
                                pixi[n++] = r.readByte();
                            } else if (sub >= 0xE8 && sub < 0xEC) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 10; j++) pixi[n] = pixi[(n++) - back];
                            } else if (sub >= 0xEC && sub < 0xF0) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 11; j++) pixi[n] = pixi[(n++) - back];
                                pixi[n++] = r.readByte();
                            } else if (sub == 0xF0) {
                                int deltas = r.readByte() & 0xFF;
                                pixi[n] = (byte) (pixi[(n++) - 2] - (deltas >> 4));
                                pixi[n] = (byte) (pixi[(n++) - 2] - (deltas & 0x0F));
                            } else if (sub >= 0xF4 && sub < 0xF8) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 12; j++) pixi[n] = pixi[(n++) - back];
                            } else if (sub >= 0xF8 && sub < 0xFC) {
                                int back = (sub & 0x03) << 8 | r.readByte() & 0xFF;
                                for (int j = 0; j < 13; j++) pixi[n] = pixi[(n++) - back];
                                pixi[n++] = r.readByte();
                            } else if (sub == 0xFC) {
                                int x = r.readByte() & 0xFF;
                                int back = (x & 0x03) << 8 | r.readByte() & 0xFF;
                                int nPixels = (x >> 3) * 2 + 3;
                                int s = (x & 0x04) >> 2;
                                for (int j = 0; j < nPixels + s; j++) pixi[n] = pixi[(n++) - back];
                                if (s == 0) pixi[n++] = r.readByte();
                            } else println(1, "Unknown subcommand: " + Mohawk.hex(sub));
                        }
                    } else println(1, "Unknown command: " + Mohawk.hex(cmd));
                }
                try {
                    while (r.readByte() == 0) ;
                } catch (IOException e) {
                }
                if (r.loc() < r.length()) {
                    println(1, "Extra byte-data:");
                    present(r.read(r.length() - r.loc()));
                }
            }
            for (int i = 0; i < width * height; i++) pix[i] = clr[pixi[i] & 0xFF];
        }
        Image i = generate(width, height, pix);
        if (print[1]) display(i);
        return i;
    }

    private Image generate(int w, int h, int[] pix) {
        return Toolkit.getDefaultToolkit().createImage(new java.awt.image.MemoryImageSource(w, h, pix, 0, w));
    }

    public static void display(Image i) {
        if (frame != null) frame.setVisible(false);
        frame = new JFrame(PointAndClick.title);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setBackground(Color.BLACK);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.getContentPane().add(new JLabel(new ImageIcon(i)));
        frame.pack();
        frame.setLocation(100, 100);
        frame.setVisible(true);
    }

    private String[] parse_NAME(Reader r, int rn) throws IOException {
        short field_count = r.readShort();
        short[] off = new short[field_count];
        for (int i = 0; i < field_count; i++) off[i] = r.readShort();
        r.skip(field_count * 2);
        String[] arr = new String[field_count];
        println(1, field_count + " entries");
        for (int i = 0; i < field_count; i++) {
            r.go_to(2 + field_count * 4 + off[i]);
            arr[i] = r.readCString();
            println(1, i + " " + arr[i]);
        }
        return arr;
    }

    private void parse_BLST(Reader r, int rn) throws IOException {
        short record_count = r.readShort();
        println(1, record_count + " records");
        for (int i = 0; i < record_count; i++) {
            short index = r.readShort();
            short enabled = r.readShort();
            short hspt_id = r.readShort();
            println(1, index + " HSPT " + hspt_id + (enabled == 1 ? " en" : " dis") + "abled");
        }
    }

    private void parse_FLST(Reader r, int rn) throws IOException {
        short record_count = r.readShort();
        if (record_count != 0) println(1, "(" + rn + ") " + record_count + " records");
        if (record_count > 1) println(2, "(" + rn + ") " + record_count + " records");
        for (int i = 0; i < record_count; i++) {
            short index = r.readShort();
            short sfxe_id = r.readShort();
            short u0 = r.readShort();
            println(1, index + " SFXE " + sfxe_id + " u0=" + u0);
        }
    }

    private PLSTrec[] parse_PLST(Reader r, int rn) throws IOException {
        short record_count = r.readShort();
        PLSTrec[] rec = new PLSTrec[record_count];
        println(1, record_count + " records");
        for (int i = 0; i < record_count; i++) {
            rec[i] = new PLSTrec();
            r.skip(2);
            rec[i].bmp = r.readShort();
            short L = r.readShort();
            short T = r.readShort();
            short R = r.readShort();
            short B = r.readShort();
            rec[i].bounds = new Rectangle(L, T, R - L, B - T);
            println(1, "bmp=" + rec[i].bmp);
            Object[] t = getRes("tBMP", rec[i].bmp);
            Mohawk.Name n = m[(Integer) t[0]].fi[((Mohawk.Resource) t[2]).fid].name;
            if (n != null) println(1, "   =" + n.name);
            println(1, "bounds=" + rec[i].bounds);
        }
        return rec;
    }

    private int[] parse_VARS(Reader r, int rn) throws IOException {
        int var_count = r.length() / 12;
        int[] rec = new int[var_count];
        println(1, var_count + " variables");
        for (int i = 0; i < var_count; i++) {
            int u0 = r.readInt();
            int u1 = r.readInt();
            rec[i] = r.readInt();
            try {
                println(1, "variable " + Mohawk.hex(i, 2) + "=" + ((String[]) info("NAME", 0, 1, false))[i] + "=" + rec[i] + " u0=" + u0 + " u1=" + u1);
            } catch (IOException e) {
            }
        }
        return rec;
    }

    private void parse_tWAV(Reader r, int rn) throws IOException {
        r.skip(12);
        String type = r.readString(4);
        boolean cue = type.equals("Cue#");
        boolean adpc = type.equals("ADPC");
        if (cue) {
            int len = r.readInt() + 8;
            println(1, "Cue: (len " + len + ")");
            present(r.read(len - 8));
            r.skip(4);
        }
        if (cue || adpc) {
            int len = r.readInt() + 8;
            println(1, "ADPC: (len " + len + ")");
            present(r.read(len - 8));
            r.skip(4);
        }
        int len = r.readInt();
        short rate = r.readShort();
        int count = r.readInt();
        byte bps = r.readByte();
        byte chan = r.readByte();
        boolean adpcm = r.readShort() == 1;
        println(1, "Sample rate: " + rate);
        println(1, "Sample count: " + count);
        println(1, bps + " bits per sample");
        println(1, chan + " channel" + (chan == 1 ? "" : "s"));
        println(1, (adpcm ? "ADPCM" : "MPEG-2") + " encoded");
        println(1, "Data:");
        present(r.read(0x100));
        println(2, "...");
        ByteArrayOutputStream baos = new ByteArrayOutputStream(len + 44);
        DataOutputStream wav = new DataOutputStream(baos);
        wav.write(new byte[] { 'R', 'I', 'F', 'F' });
        wav.writeInt(len + 36);
        wav.write(new byte[] { 'W', 'A', 'V', 'E', 'f', 'm', 't', ' ' });
        wav.writeInt(16);
        wav.writeShort(1);
        wav.writeShort(chan);
        wav.writeInt(rate);
        wav.writeInt((bps >> 3) * chan);
        wav.writeInt(bps);
        wav.write(new byte[] { 'd', 'a', 't', 'a' });
        wav.writeInt(len);
        wav.write(r.read(len));
        present(baos.toByteArray(), 0, 0x100);
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(new ByteArrayInputStream(baos.toByteArray()));
            DataLine.Info dli = new DataLine.Info(Clip.class, ais.getFormat());
            println(1, "line " + dli + (AudioSystem.isLineSupported(dli) ? " is" : " is not") + " supported");
            Clip mus = (Clip) AudioSystem.getLine(dli);
            mus.open(ais);
            mus.loop(1);
            Thread.sleep(mus.getMicrosecondLength() / 1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parse_CLRC(Reader r, int rn) throws IOException {
        println(1, Mohawk.hex(r.readLShort(), 0, false) + " " + Mohawk.hex(r.readLShort(), 0, false));
    }

    private void parse_EXIT(Reader r, int rn) throws IOException {
        Object[] t = getRes("EXIT", rn);
        print(1, "*" + m[(Integer) t[0]].fName + " " + rn);
        print(1, " ");
        readType5(r, rn, 2);
        println(1, "");
    }

    private void parse_HELP(Reader r, int rn) throws IOException {
        short[] nums = new short[r.readLShort()];
        for (int i = 0; i < nums.length; i++) nums[i] = r.readLShort();
        println(1, rn + " " + Arrays.toString(nums) + " \"" + r.readCString() + "\"");
    }

    private void parse_HINT(Reader r, int rn) throws IOException {
        short record_count = r.readLShort();
        println(1, rn + "-" + record_count);
        for (int i = 0; i < record_count; i++) {
            if (i == record_count) println(1, "-------");
            short rid = r.readLShort();
            short u1 = r.readLShort();
            boolean n = (u1 == 0 || u1 == 2 || u1 == 200 || u1 == 300 || u1 == 400 || u1 == 500 || u1 == 600 || u1 == 999 || u1 == -1);
            println(1, rid + " " + u1 + (n ? "" : " ---------------------"));
        }
        if (r.loc() != r.length()) println(1, "-------");
    }

    private void parse_INIT(Reader r, int rn) throws IOException {
        Object[] t = getRes("INIT", rn);
        print(1, "*" + m[(Integer) t[0]].fName + " " + rn);
        print(1, " ");
        readType5(r, rn, 1);
        println(1, "");
    }

    private void parse_MJMP(Reader r, int rn) throws IOException {
        short s = r.readLShort();
        println(1, rn + ": " + s + " " + Mohawk.hex(s, 4));
    }

    /**
	 * Translates an Apple QuickDraw PICT format image into a usable Java
	 * {@code Image}. The PICT format is defined in {@link
	 * http://developer.apple.com/documentation/mac/QuickDraw/QuickDraw-2.html}.
	 * 
	 * PICT is of type Picture (after an empty 512-byte custom header). Sample:
	 * 
	 * <pre>
	 * $"004F"     picture size; this value is reliable for version 1 pictures
	 * $"0002 0002 006E 00AA"  bounding rectangle of picture
	 * $"11"       picVersion opcode for version 1
	 *    $"01"    version number 1
	 * $"01"       ClipRgn opcode to define clipping region for picture
	 *    $"000A"  region size
	 *    $"0002 0002 006E 00AA"  bounding rectangle for region
	 * $"0A"       FillPat opcode; fill pattern specified in next 8 bytes
	 *    $"77DD 77DD 77DD 77DD"  fill pattern
	 * $"34"       fillRect opcode; rectangle specified in next 8 bytes
	 *    $"0002 0002 006E 00AA"  rectangle to fill
	 * $"0A"       FillPat opcode; fill pattern specified in next 8 bytes
	 *    $"8822 8822 8822 8822"  fill pattern
	 * $"5C"       fillSameOval opcode
	 * $"71"       paintPoly opcode
	 *    $"001A"  size of polygon
	 *    $"0002 0002 006E 00AA"  bounding rectangle for polygon
	 *    $"006E 0002 0002 0054 006E 00AA 006E 0002"   polygon points
	 * $"FF"       EndOfPicture opcode; end of picture
</pre>
	 * 
	 * @param b
	 * @param rn
	 * @return the translated image
	 */
    private Image parse_PICT(Reader r, int rn) throws IOException {
        Image i = null;
        return i;
    }

    private void parse_RLST(Reader r, int rn) throws IOException {
        Object[] t = getRes("RLST", rn);
        int records = r.readLShort();
        println(1, m[(Integer) t[0]].fName + " " + rn + " " + records);
        for (int i = 0; i < records; i++) {
            int type = -1, flag = -1;
            for (int j = 0; j < 7; j++) {
                short s = r.readLShort();
                if (j == 0) type = s; else if (j == 1) flag = s;
                String p = s < 0 ? Mohawk.hex(s, 4) : "" + s;
                if (j == 2) p = " <" + p; else if (j == 5) p = " " + p + ">"; else p = " " + p;
                print(1, p);
            }
            if (type == 5) {
                print(1, " ");
                readType5(r, rn, 0);
            } else if (type == 6) {
                print(1, " ");
                readType6(r, rn);
            } else if (type == 7) readType7(r, rn); else if (type == 8) readType8(r, rn); else if (type >= 10 && type <= 12) {
                readType8(r, rn);
                for (int j = 0; j < 10; j++) print(1, (j == 0 ? " <" : " ") + r.readLShort());
                print(1, ">");
                for (int j = 0; j < (type == 10 ? 4 : 3); j++) {
                    short num = r.readLShort();
                    print(1, " " + num + " {");
                    for (int k = 0; k < num; k++) print(1, (k == 0 ? "" : " ") + r.readLShort());
                    print(1, "}");
                }
                if (type == 12) for (int j = 0; j < 6; j++) {
                    short s = r.readLShort();
                    String p = s < 0 ? Mohawk.hex(s, 4) : "" + s;
                    if (j == 2) p = " <" + p; else if (j == 5) p = " " + p + ">"; else p = " " + p;
                    print(1, p);
                }
            } else if (type == 13) print(1, " " + r.readLShort() + " " + r.readLShort()); else if (type == 14) ;
            println(1, "");
        }
        println(1, "");
    }

    Object[] cmds = new Object[0];

    private void readType5(Reader r, int rn, int type) throws IOException {
        short cnt = r.readLShort();
        print(1, "" + cnt);
        for (int i = 0; i < cnt; i++) {
            short u1 = 0;
            if (type > 0) u1 = r.readLShort();
            short cmd = r.readLShort(), var = r.readLShort(), num = r.readLShort();
            boolean f = true;
            int[] vars = null;
            for (int o = 0; o < cmds.length; o++) {
                Object[] j = (Object[]) cmds[o];
                if (j[0].equals(cmd)) {
                    Object[] rp = new Object[j.length + 1];
                    System.arraycopy(j, 0, rp, 0, j.length);
                    rp[j.length] = vars = new int[num + 1];
                    cmds[o] = rp;
                    f = false;
                }
            }
            if (f) {
                Object[] rp = new Object[cmds.length + 1];
                System.arraycopy(cmds, 0, rp, 0, cmds.length);
                rp[cmds.length] = new Object[] { cmd, vars = new int[num + 1] };
                cmds = rp;
            }
            print(1, (type > 0 ? " [" + u1 + " " : " [") + cmd + " " + var + " " + num);
            vars[0] = var;
            if (num > 0) {
                print(1, " {");
                for (int j = 0; j < num; j++) print(1, (j == 0 ? "" : " ") + (vars[j + 1] = r.readLShort()));
                print(1, "}");
            }
            if (type > 1) {
                short after = r.readLShort();
                if (after != 1) cnt += 0;
                print(1, " " + after);
            }
            print(1, "]");
            cnt += 0;
        }
    }

    private void readType6(Reader r, int rn) throws IOException {
        readType5(r, rn, 0);
        print(1, " \"" + r.readCString() + "\"");
        if (r.loc() % 2 == 1) r.skip(1);
        for (int i = 0; i < 7; i++) print(1, (i == 0 ? " <" : " ") + r.readLShort());
        print(1, ">");
    }

    private void readType7(Reader r, int rn) throws IOException {
        short var = r.readLShort(), num = r.readLShort();
        print(1, " " + var + " " + num);
        for (int j = 0; j < num; j++) {
            short cmd = r.readLShort();
            print(1, " " + cmd + "(");
            if (cmd == 5) readType5(r, rn, 0); else if (cmd == 6) readType6(r, rn);
            print(1, ")");
        }
    }

    private void readType8(Reader r, int rn) throws IOException {
        readType7(r, rn);
        short var = r.readLShort(), num = r.readLShort();
        print(1, " " + var + " " + num);
        for (int j = 0; j < num; j++) {
            short wdib = r.readLShort();
            print(1, " [" + wdib);
            short l = r.readLShort();
            if (l == -1) print(1, " <FFFF>]"); else {
                short t = r.readLShort(), R = r.readLShort(), b = r.readLShort();
                print(1, " <" + l + " " + t + " " + R + " " + b + ">]");
            }
        }
    }

    private void parse_VIEW(Reader r, int rn) throws IOException {
        short flag = r.readLShort(), count = r.readLShort();
        print(1, rn + " " + Mohawk.hex(flag, 4) + " " + count);
        if (count == 0) print(1, " (" + r.readLShort() + ")"); else for (int i = 0; i < count; i++) {
            print(1, " [" + r.readLShort());
            short ids = r.readLShort();
            print(1, " " + ids);
            for (int j = 0; j < ids; j++) print(1, " {" + r.readLShort() + "}");
        }
        print(1, " ");
        readVIEWSound(r, rn);
        short elCount = r.readLShort();
        print(1, " " + elCount);
        for (int i = 0; i < elCount; i++) {
            short type = r.readLShort();
            print(1, " [" + type);
            if (type == 3) {
                print(1, " " + r.readLShort());
                short idCount = r.readLShort();
                print(1, " " + idCount + " " + r.readLShort());
                for (int j = 0; j < idCount; j++) print(1, " {" + r.readLShort() + "}");
            } else print(1, " " + r.readLShort());
            print(1, "]");
        }
        println(1, " <" + r.readLShort() + " " + r.readLShort() + " " + r.readLShort() + " " + r.readLShort() + ">");
    }

    private void readVIEWSound(Reader r, int rn) throws IOException {
        short snd = r.readLShort();
        print(1, "" + snd);
        if (snd == -1 || snd == -3) ; else if (snd == -4) {
            short var = r.readLShort(), count = r.readLShort();
            print(1, " " + var + " " + count);
            for (int i = 0; i < count; i++) {
                print(1, " (");
                readVIEWSound(r, rn);
                print(1, ")");
            }
        } else print(1, " <" + Mohawk.hex(r.readShort(), 4) + ">");
    }

    /**
	 * Thanks again to <a href="http://homepage.mac.com/rshayter/Riveal.html">
	 * Ron Hayter</a> for his LZBMPExtractor code, which has been modified to
	 * be able to decompress the LZ77 BMP files in the WDIB resources.
	 */
    public int parse_WDIB(Reader r, int rn) throws IOException {
        int nRingBufferBits = 10;
        int ringBufferSize = 1 << nRingBufferBits;
        int ringBufferMask = ringBufferSize - 1;
        int lengthBias = 3;
        int offsetBias = 66;
        int nOutBytes = r.readLInt();
        if (nOutBytes <= 0) return 0;
        byte[] outBytes = new byte[nOutBytes];
        int outByteN = 0;
        byte[] ringBuffer = new byte[ringBufferSize];
        int nInBytes = r.length() - 4;
        for (int inByteN = 0; inByteN < nInBytes; ) {
            int type = r.readByte() & 0xFF;
            inByteN++;
            for (int bit = 0x01; bit < 0x100; bit <<= 1) if ((type & bit) == 0) {
                if (inByteN + 1 >= nInBytes) break;
                int lengthPlusOffset = r.readByte();
                inByteN += 2;
                int length = ((lengthPlusOffset & ~ringBufferMask) >> nRingBufferBits) + lengthBias;
                int offset = (lengthPlusOffset + offsetBias) & ringBufferMask;
                for (int i = 0; i < length; ++i) {
                    byte t = ringBuffer[offset++ & 0x3FF];
                    if (outByteN < nOutBytes) outBytes[outByteN] = t;
                    ringBuffer[outByteN & 0x3FF] = t;
                    ++outByteN;
                }
            } else {
                if (inByteN >= nInBytes) break;
                byte t = (byte) (r.readByte() & 0xFF);
                ++inByteN;
                if (outByteN < nOutBytes) outBytes[outByteN] = t;
                ringBuffer[outByteN & 0x3FF] = t;
                ++outByteN;
            }
        }
        present(outBytes, 0, nOutBytes);
        Mohawk.readBMP(new Reader.ArrayReader(outBytes));
        return 1;
    }

    public static void present(byte[] b) {
        present(b, 0, b.length);
    }

    public static void present(byte[] b, int off, int len) {
        int k = off;
        int lines = len / 16 + (len % 16 == 0 ? 0 : 1);
        for (int i = 0; i < lines; i++) {
            int linelen = i == lines - 1 ? len - i * 16 : 16;
            for (int j = 0; j < linelen; j++) {
                String s = Integer.toHexString(b[j + k] & 0xFF);
                while (s.length() < 2) s = "0" + s;
                print(2, s + " ");
            }
            for (int j = linelen; j < 16; j++) print(2, "   ");
            String t = "  ";
            for (int j = 0; j < linelen; j++) {
                if ((b[j + k] < 0x20) || (b[j + k] > 0xFF)) t += "."; else t += new String(new byte[] { b[j + k] });
            }
            println(2, t);
            k += 16;
        }
    }

    static short readShort(byte[] b, int pos) {
        return (short) ((b[pos + 1] & 0xFF) | ((b[pos] & 0xFF) << 8));
    }

    static int readInt(byte[] b, int pos) {
        return (b[pos + 3] & 0xFF) | ((b[pos + 2] & 0xFF) << 8) | ((b[pos + 1] & 0xFF) << 16) | ((b[pos] & 0xFF) << 24);
    }

    static short readBShort(byte[] b, int pos) {
        return (short) ((b[pos] & 0xFF) | ((b[pos + 1] & 0xFF) << 8));
    }

    static int readBInt(byte[] b, int pos) {
        return (b[pos] & 0xFF) | ((b[pos + 1] & 0xFF) << 8) | ((b[pos + 2] & 0xFF) << 16) | ((b[pos + 3] & 0xFF) << 24);
    }

    static void println(int chan, String s) {
        print(chan, 0, s + "\n");
    }

    static void print(int chan, String s) {
        print(chan, 0, s);
    }

    static void println(int chan, int pad, String s) {
        print(chan, pad, s + "\n");
    }

    static void print(int chan, int pad, String s) {
        if (print[chan]) {
            for (; pad > 0; pad--) s = "  " + s;
            System.out.print(s);
        }
    }

    static void set(boolean[] n) {
        print = n;
    }

    public static boolean in(String a, String[] b) {
        for (String i : b) if (Mohawk.equals(a, i)) return true;
        return false;
    }

    Mohawk.FileInfo getFile(int fid) {
        for (int i = 0; i < m.length; i++) if (fid < m[i].fi.length) return m[i].fi[fid]; else fid -= m[i].fi.length;
        return null;
    }

    /**
	 * 
	 * @param srt
	 * @param rn
	 * @return a four-item array of information: the file index, the resource
	 * type of the candidate file, the resource itself, and the local resource
	 * ID.
	 */
    Object[] getRes(String srt, int rn) {
        for (int i = 0; i < m.length; i++) {
            Mohawk.ResourceType r = m[i].rlookup(srt);
            if (r == null) ; else if (rn < r.rte) return new Object[] { i, r, r.res[rn], rn }; else rn -= r.rte;
        }
        return null;
    }

    int getResM(String srt) {
        int max = 0;
        for (int i = 0; i < m.length; i++) if (m[i].rlookup(srt) != null) max += m[i].rlookup(srt).rte;
        return max;
    }

    Mohawk.Resource lookup(String name) {
        for (int i = 0; i < m.length; i++) if (m[i].lookup.containsKey(name)) return m[i].lookup.get(name);
        return null;
    }

    class Hotspot {

        short blst_id;

        short name_rec;

        Rectangle bounds;

        short cur;

        Handler[] hand;
    }

    class Handler {

        short event_type;

        Command[] cmds;
    }

    interface Command {
    }

    class ParamCmd implements Command {

        short cmd;

        short[] args;
    }

    class SwitchCmd implements Command {

        short var;

        SwitchVal[] vals;
    }

    class SwitchVal {

        short value;

        Command[] cmds;
    }

    class PLSTrec {

        short bmp;

        Rectangle bounds;
    }

    private String getNodeName(int rnum) throws IOException {
        for (PLSTrec i : (PLSTrec[]) info("PLST", rnum, 1, false)) {
            Object[] r = getRes("tBMP", i.bmp);
            if (m[(Integer) r[0]].fi[((Mohawk.Resource) r[2]).fid].name != null) return m[(Integer) r[0]].fi[((Mohawk.Resource) r[2]).fid].name.name;
        }
        return null;
    }

    private String getVarName(int arc, int rnum) throws IOException {
        if (getResM("NAME") == 0) throw new IOException(); else return ((String[]) info(arc, "NAME", 3, 1, false))[rnum];
    }

    public void runFromDataFile(String dir, String pkg, int st) throws IOException {
        PointAndClick pac = new PointAndClick(dir);
        pac.W = 608;
        pac.H = 392;
        pac.cm.gen = new CursorManager.Generator() {

            public Image generate(String s, CursorManager c) {
                Mohawk.class.getResource(s);
                return c.tk.getImage(Mohawk.class.getResource(s));
            }
        };
        pac.cm.put(CursorManager.DEFAULT, "cursors/3000.gif", 6, 1);
        pac.cm.put(CursorManager.ACTIVE, "cursors/3000.gif", 6, 1);
        for (int i = 1; i <= 6; i++) pac.cm.put("300" + i, "cursors/300" + i + ".gif", 16, 16);
        pac.am = new com.sf.pac.man.AreaManager();
        pac.ni.nodes.put(pkg, new java.util.ArrayList<Node>());
        pac.ni.loaded = pkg;
        int cards = getResM("CARD");
        Progger p = new Progger(cards);
        try {
            for (int i = 0; i < cards; i++) {
                Node cur = new Node(pac, pkg, false, null);
                cur.add(new ImageArea(new com.sf.pac.util.UNL(getNodeName(i), pkg), new Point(0, 0), null));
                println(1, cur + "");
                pac.ni.nodes.get(pkg).add(cur);
                Hotspot[] hspts = (Hotspot[]) info("HSPT", i, 1, false);
                for (Hotspot h : hspts) {
                    String op = "", s;
                    for (Handler handler : h.hand) for (Command c : handler.cmds) {
                        s = null;
                        if (c instanceof ParamCmd) {
                            ParamCmd pc = (ParamCmd) c;
                            switch(pc.cmd) {
                                case 2:
                                    s = "" + (pc.args[0] - 1);
                                    break;
                                case 7:
                                    s = getVarName((Integer) getRes("HSPT", i)[0], pc.args[0]) + "=" + pc.args[1];
                                    break;
                                case 14:
                                    s = "<" + pc.args[0] + ">";
                                    break;
                                case 24:
                                    s = getVarName((Integer) getRes("HSPT", i)[0], pc.args[0]);
                                    s += "=" + s + "+" + pc.args[1];
                                    break;
                                default:
                                    s = "?";
                            }
                        }
                        op += "," + s;
                    }
                    if (!op.equals("")) op = op.substring(1);
                    cur.add(new Area(h.bounds, h.cur == 3000 ? com.sf.pac.man.CursorManager.ACTIVE : h.cur + "", Operation.encode(op, pkg), null));
                }
                p.inc();
            }
        } finally {
            p.exit();
        }
        new PACApplication(new MHKReg(this, pac, pkg, st), null);
    }

    private static class MHKReg extends PACApplication.AppReg {

        MHKInfo mo;

        String pkg;

        int st;

        public MHKReg(MHKInfo m, PointAndClick p, String k, int s) {
            super(p);
            mo = m;
            pkg = k;
            st = s;
        }

        public Object register(com.sf.pac.man.State s) {
            s = new com.sf.pac.man.State(pac, pkg, st);
            pac.st = s;
            System.out.println(pac.st);
            return s;
        }

        public Object register(ImageArea a) throws IllegalArgumentException {
            try {
                return a.set(new ImageIcon((Image) mo.info("tBMP", mo.lookup(a.loc.name).rn, 1, false)));
            } catch (IOException e) {
                throw new IllegalArgumentException("IOException: " + e.getMessage());
            }
        }
    }

    public static class Progger {

        LookAndFeel laf;

        JProgressBar jp;

        JFrame fr;

        int cur = 0, max;

        boolean creating = true;

        Thread creator;

        public Progger(int m) {
            max = m;
            creator = new Thread("Progress Bar") {

                public void run() {
                    try {
                        laf = UIManager.getLookAndFeel();
                        if (isInterrupted()) return;
                        try {
                            UIManager.setLookAndFeel(LAFChooser.SYS);
                        } catch (Exception e) {
                        }
                        if (isInterrupted()) return;
                        jp = new JProgressBar(0, max);
                        if (isInterrupted()) return;
                        fr = new JFrame(PointAndClick.title);
                        fr.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                        fr.add(jp);
                        fr.pack();
                        if (isInterrupted()) return;
                        fr.setVisible(true);
                        jp.setValue(cur);
                    } finally {
                        creating = false;
                    }
                }
            };
            creator.start();
        }

        public void inc() {
            cur++;
            if (!creating) jp.setValue(cur);
        }

        public void exit() {
            creator.interrupt();
            if (fr != null) fr.setVisible(false);
            if (laf != null) try {
                UIManager.setLookAndFeel(laf);
            } catch (Exception e) {
            }
        }
    }
}
