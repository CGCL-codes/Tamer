package jepe.battle;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.image.*;
import java.awt.event.KeyEvent;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import java.util.*;
import jepe.map.Map;
import jepe.part.Game;

public class BattleObject {

    WildBattle parent;

    private jepe.util.Config config;

    String Pok_Name;

    int lvl;

    int id;

    int nat_id;

    float HPcalc;

    int Exp[] = new int[5];

    float EXPcalc;

    int status;

    private static int Normal = 0;

    private static int Poison = 1;

    int HP[] = new int[6];

    int Att[] = new int[6];

    int Def[] = new int[6];

    int Spa[] = new int[6];

    int Spe[] = new int[6];

    int Spd[] = new int[6];

    String Type1, Type2;

    int Gender;

    int Acc;

    boolean faint;

    String GenderT;

    String Move[] = new String[4];

    int MovePow[] = new int[4];

    String Atk1, Atk2, Atk3, Atk4;

    BufferedImage hpGreen;

    BufferedImage hpYellow;

    BufferedImage hpRed;

    private BufferedImage HP_Bar_Bfr;

    private Graphics HP_Bar_Canvas = null;

    boolean UpdateHPBar;

    private BufferedImage Exp_Bar_Bfr;

    private Graphics Exp_Bar_Canvas = null;

    boolean UpdateExpBar;

    private BufferedImage Front_sprite, Back_sprite;

    public BattleObject(String Name, int pNum, int natNum, int l) {
        Random randgen = new Random();
        int IV;
        Pok_Name = Name;
        IV = randgen.nextInt(32);
        lvl = l;
        genRandCalc(Att, 32, IV, 1.0);
        genRandCalc(Def, 32, randgen.nextInt(32), 1.0);
        genRandCalc(Spa, 52, randgen.nextInt(32), 0.9);
        genRandCalc(Spd, 34, randgen.nextInt(32), 1.1);
        genRandCalc(Spe, 34, randgen.nextInt(32), 1.0);
        HP[3] = lvl + 10 + ((124 * 2 + randgen.nextInt(32)) * lvl / 100);
        HP[4] = HP[5] = HP[3];
        Init_Images();
    }

    public void PrintBattleDetails() {
        System.out.println("************************************************");
        System.out.println("BattleObject Details for " + Pok_Name);
        System.out.println("Att Base: " + Att[0] + "\tIV " + Att[1] + "\tEV " + Att[2] + "\tcur " + Att[3] + "\tmin " + Att[4] + "\tmax " + Att[5]);
        System.out.println("Def Base: " + Def[0] + "\tIV " + Def[1] + "\tEV " + Def[2] + "\tcur " + Def[3] + "\tmin " + Def[4] + "\tmax " + Def[5]);
        System.out.println("Spa Base: " + Spa[0] + "\tIV " + Spa[1] + "\tEV " + Spa[2] + "\tcur " + Spa[3] + "\tmin " + Spa[4] + "\tmax " + Spa[5]);
        System.out.println("Spa Base: " + Spe[0] + "\tIV " + Spe[1] + "\tEV " + Spe[2] + "\tcur " + Spe[3] + "\tmin " + Spe[4] + "\tmax " + Spe[5]);
        System.out.println("Spd Base: " + Spd[0] + "\tIV " + Spd[1] + "\tEV " + Spd[2] + "\tcur " + Spd[3] + "\tmin " + Spd[4] + "\tmax " + Spd[5]);
        System.out.println("************************************************");
    }

    public BattleObject(Element e, int lvl) {
        int i = 0;
        if (e.getAttributeValue("name") != null) Pok_Name = e.getAttributeValue("name"); else Pok_Name = "Missingo";
        if (e.getChild("type1") != null) Type1 = e.getChild("type1").getText();
        if (e.getChild("type2") != null) Type2 = e.getChild("type2").getText();
        if (e.getChild("stats") != null) {
            Element StatBase = e.getChild("stats");
            Element Temp = null;
            Temp = StatBase.getChild("hp");
            if (Temp != null) {
                if (Temp.getAttribute("base") != null) HP[0] = Integer.parseInt(Temp.getAttributeValue("base")); else HP[0] = 5;
            }
            Temp = StatBase.getChild("att");
            if (Temp != null) {
                if (Temp.getAttribute("base") != null) Att[0] = Integer.parseInt(Temp.getAttributeValue("base")); else Att[0] = 5;
            }
            Temp = StatBase.getChild("def");
            if (Temp != null) {
                if (Temp.getAttribute("base") != null) Def[0] = Integer.parseInt(Temp.getAttributeValue("base")); else Def[0] = 5;
            }
            Temp = StatBase.getChild("spa");
            if (Temp != null) {
                if (Temp.getAttribute("base") != null) Spa[0] = Integer.parseInt(Temp.getAttributeValue("base")); else Spa[0] = 5;
            }
            Temp = StatBase.getChild("spd");
            if (Temp != null) {
                if (Temp.getAttribute("base") != null) Spd[0] = Integer.parseInt(Temp.getAttributeValue("base")); else Spd[0] = 5;
            }
            Temp = StatBase.getChild("spe");
            if (Temp != null) {
                if (Temp.getAttribute("base") != null) Spe[0] = Integer.parseInt(Temp.getAttributeValue("base")); else Spe[0] = 5;
            }
        }
        genIVs();
        Init_Images();
    }

    private void genIVs() {
        Random randgen = new Random();
        genRandCalc(Att, randgen.nextInt(32), 1.0);
        genRandCalc(Def, randgen.nextInt(32), 1.0);
        genRandCalc(Spa, randgen.nextInt(32), 0.9);
        genRandCalc(Spd, randgen.nextInt(32), 1.1);
        genRandCalc(Spe, randgen.nextInt(32), 1.0);
        HP[3] = lvl + 10 + ((HP[0] * 2 + randgen.nextInt(32)) * lvl / 100);
        HP[4] = HP[5] = HP[3];
    }

    public BattleObject(String Name) {
        Pok_Name = Name;
        if (Name == "Sputteren") {
            lvl = 8;
            HP[5] = HP[4] = HP[3] = 24;
            SetMinMax(Def, 9);
            SetMinMax(Spa, 14);
            SetMinMax(Spd, 7);
            SetMinMax(Spe, 15);
            Exp[4] = 0;
            Exp[0] = 65;
            Exp[1] = 150;
            Exp[2] = 184;
            Exp[3] = 112;
            Gender = 1;
        } else if (Name == "Missingno.") {
            lvl = 7;
            HP[5] = HP[4] = HP[3] = 21;
            SetMinMax(Att, 10, 5);
            SetMinMax(Def, 9, 5);
            SetMinMax(Spe, 16, 5);
            SetMinMax(Spd, 16);
            Gender = 1;
            Exp[4] = 0;
            Exp[0] = 65;
            Exp[1] = 150;
            Exp[2] = 184;
            Exp[3] = 112;
        }
        HPcalc = 0.48f * (((float) HP[3] / (float) HP[5]) * 100);
        Init_Images();
    }

    boolean isfaint() {
        return faint;
    }

    void Update_Stats() {
        switch(status) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                break;
        }
    }

    private BattleObject(WildBattle p) {
        this.parent = p;
    }

    private void Init_Images() {
        UpdateHPBar = true;
        UpdateExpBar = true;
        hpGreen = null;
        hpYellow = null;
        hpRed = null;
        Front_sprite = null;
        Back_sprite = null;
        HP_Bar_Bfr = null;
    }

    public void set_Sprites(BufferedImage fr, BufferedImage ba) {
        Front_sprite = fr;
        Back_sprite = ba;
    }

    public void set_HP_imgs(BufferedImage hpG, BufferedImage hpY, BufferedImage hpR) {
        hpGreen = hpG;
        hpYellow = hpY;
        hpRed = hpR;
        HP_Bar_Bfr = new BufferedImage(200, 10, BufferedImage.TYPE_INT_ARGB);
        HP_Bar_Canvas = HP_Bar_Bfr.getGraphics();
        genNewHPBar();
    }

    private void genRandCalc(int s[], int c, double d) {
        genRandCalc(s, s[0], c, (float) d);
    }

    private void genRandCalc(int s[], int b, int c, double d) {
        genRandCalc(s, b, c, (float) d);
    }

    private void genRandCalc(int s[], int a, int b, float d) {
        genRandCalc(s, a, b, (int) (d * 100));
    }

    private void genRandCalc(int stat[], int base, int ev, int pers) {
        Random randomGenerator = new Random();
        stat[0] = base;
        stat[1] = ev;
        stat[2] = 0;
        stat[3] = (5 + (stat[0] * 2 + stat[1]) * lvl / 100) * pers / 100;
        stat[4] = stat[3] - 5;
        stat[5] = stat[3] + 5;
    }

    private void SetMinMax(int Val[], int base) {
        SetMinMax(Val, base, 4);
    }

    private void SetMinMax(int Val[], int base, int diff) {
        Val[2] = 0;
        Val[3] = base;
        if ((base - diff) < 1) Val[4] = 0; else Val[4] = Val[3] - diff;
        Val[5] = Val[3] + diff;
    }

    public boolean incAtt() {
        if ((Att[3] == Att[5])) return false;
        Att[3]++;
        return true;
    }

    public boolean decAtt() {
        if ((Att[3] == Att[4])) return false;
        Att[3]--;
        return true;
    }

    public void decHP(int HPMod) {
        if (0 < HPMod) HP[4] -= HPMod; else HP[4] += HPMod;
        if (HP[4] <= -2) {
            faint = true;
            HP[4] = -1;
        }
    }

    public void incHP(int HPMod) {
        if (0 < HPMod) HP[4] += HPMod; else HP[4] -= HPMod;
        if (HP[4] < HP[5]) HP[4] = HP[5];
    }

    public void gainEXP(int xpMod) {
        if (0 < xpMod) Exp[4] = xpMod; else Exp[4] -= xpMod;
    }

    public void update(long elapsedtime) {
        HPcalc = 0.48f * (((float) HP[3] / (float) HP[5]) * 100);
        EXPcalc = 0.64f * 100;
        if (HP[3] != HP[4]) {
            if (HP[3] < HP[4]) HP[3]++;
            if (HP[3] > HP[4]) HP[3]--;
            UpdateHPBar = true;
        }
        if (Exp[4] != 0) {
            UpdateExpBar = true;
            Exp[1]++;
            Exp[4]--;
            if (Exp[1] == Exp[2]) {
                Exp[3] = Exp[2];
                Exp[2] += lvl * (lvl + 1);
                lvl++;
            }
        }
    }

    private void genNewExpBar() {
        Exp_Bar_Bfr = new BufferedImage(100, 10, BufferedImage.TYPE_INT_ARGB);
        Exp_Bar_Canvas = Exp_Bar_Bfr.getGraphics();
        Exp_Bar_Canvas.setColor(new Color(50, 50, 255, 60));
        if (EXPcalc != 0) Exp_Bar_Canvas.fillRect(0, 0, (int) EXPcalc, 3);
        UpdateExpBar = false;
    }

    private void genNewHPBar() {
        HP_Bar_Bfr = new BufferedImage(100, 10, BufferedImage.TYPE_INT_ARGB);
        HP_Bar_Canvas = HP_Bar_Bfr.getGraphics();
        for (int j = 0; j <= HPcalc; j++) {
            if (25 <= HPcalc) {
                HP_Bar_Canvas.drawImage(hpGreen, j, 0, null);
            } else if (7 <= HPcalc) {
                HP_Bar_Canvas.drawImage(hpYellow, j, 0, null);
            } else if (0 <= HPcalc && 0 < HP[3]) {
                HP_Bar_Canvas.drawImage(hpRed, j, 0, null);
            }
        }
        UpdateHPBar = false;
    }

    public void renderExpBar(Graphics g, int x, int y) {
        if (UpdateExpBar) genNewExpBar();
        g.drawImage(Exp_Bar_Bfr, x, y, null);
    }

    public void renderHPBar(Graphics g, int x, int y) {
        if (UpdateHPBar) genNewHPBar();
        g.drawImage(HP_Bar_Bfr, x, y, null);
    }

    public void render(Graphics g, boolean front, int x, int y) {
        if (front) {
            if (Front_sprite == null) return;
            g.drawImage(Front_sprite, x, y, null);
        } else {
            if (Back_sprite == null) return;
            g.drawImage(Back_sprite, x, y, null);
        }
    }
}
