package com.fj.torkel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import com.fj.engine.MapObject;
import com.fj.engine.RPG;
import com.fj.engine.StringList;
import com.fj.engine.Thing;
import com.fj.torkel.util.Text;

public class StatusPanel extends TPanel {

    private static final long serialVersionUID = 3905800885761095223L;

    public static final int boxborder = 1;

    public static final Color powercolor = new Color(0, 128, 60);

    public static int charwidth = 0;

    public static int charheight = 0;

    public static int charmaxascent = 0;

    public StatusPanel() {
        super(Game.getQuestapp());
        setBackground(QuestApplet.PANELCOLOUR);
    }

    public Dimension getPreferredSize() {
        return new Dimension(208, 272);
    }

    public void paint(Graphics g) {
        super.paint(g);
        FontMetrics met = g.getFontMetrics(g.getFont());
        charwidth = met.charWidth(' ');
        charheight = met.getMaxAscent() + met.getMaxDescent();
        charmaxascent = met.getMaxAscent();
        Thing h = Game.hero();
        MapObject m = h.getMap();
        Rectangle bounds = getBounds();
        int width = bounds.width;
        int hp = h.getStat(RPG.ST_HPS);
        int hpm = h.getStat(RPG.ST_HPSMAX);
        float hel = ((float) hp) / hpm;
        if (hel < 0) hel = 0;
        if (hel > 1) hel = 1;
        Color healthcolor = new Color(((int) (255 - hel * 192)), ((int) (hel * 160)), ((int) (100 - hel * 100)));
        paintLabel(g, "Health: " + hp + "/" + hpm, 10, 13);
        paintBar(g, 10, 26, width - 20, 16, healthcolor, Color.black, hel);
        int yPos = 36 + charheight;
        paintStats(g, 10, yPos);
        yPos += 6 * charheight;
        paintLabel(g, "Exp: " + h.getStat(RPG.ST_EXP) + " / " + Hero.calcXPRequirement(h.getStat("Level") + 1), 10, yPos);
        yPos += charheight;
        paintLabel(g, "Piety: " + h.getStat(RPG.ST_PEITY), 10, yPos);
        yPos += charheight;
        {
            yPos += charheight;
            paintLabel(g, (m == null) ? "Six Feet Under" : m.getDescription(), 10, yPos);
            yPos += charheight;
            if ((m != null) && Game.isDebug()) {
                String levelName = m.name();
                paintLabel(g, "[" + levelName + "] lv=" + m.getLevel(), 10, yPos);
                yPos += charheight;
            }
            yPos += charheight;
        }
        {
            Thing[] atts = h.getFlaggedContents("IsEffect");
            StringList sl = new StringList();
            for (int i = 0; i < atts.length; i++) {
                sl.add(Text.capitalise(atts[i].getString("EffectName")));
            }
            Thing w1 = h.getWielded(RPG.WT_MAINHAND);
            Thing w2 = h.getWielded(RPG.WT_SECONDHAND);
            if ((w1 != null) && Item.isDamaged(w1)) sl.add("Damaged weapon");
            if ((w2 != null) && Item.isDamaged(w2)) sl.add("Damaged weapon");
            if ((w1 != null) && (!w1.getFlag("IsWeapon"))) w1 = null;
            if ((w2 != null) && (!w2.getFlag("IsWeapon"))) w2 = null;
            if ((w1 == null) && (w2 == null)) sl.add("Unarmed");
            Thing[] fs = h.getFlaggedContents("IsBeing");
            int followers = 0;
            int pursuers = 0;
            for (int i = 0; i < fs.length; i++) {
                if (fs[i].isHostile(h)) {
                    pursuers++;
                } else {
                    followers++;
                }
            }
            if (followers > 0) {
                sl.add(Integer.toString(followers) + ((followers > 1) ? " companions" : " companion"));
            }
            if (pursuers > 0) {
                sl.add(Integer.toString(pursuers) + ((pursuers > 1) ? " enemies in pursuit" : " enemy in pusuit"));
            }
            if (Hero.hasHungerString(h)) {
                String hunger = Text.capitalise(Hero.hungerString(h));
                sl.add(hunger);
            }
            sl = sl.compress();
            sl = sl.compact(21, ", ");
            for (int i = 0; i < sl.getCount(); i++) {
                paintLabel(g, sl.getString(i), 20, yPos);
                yPos += charheight;
            }
        }
    }

    private static String[] stats1 = new String[] { "SK", "ST", "AG", "TG", "Level" };

    private static String[] stats2 = new String[] { "IN", "WP", "CH", "CR", "Luck" };

    public void paintStats(Graphics g, int x, int y) {
        for (int i = 0; i < stats1.length; i++) {
            paintStat(g, stats1[i], x, y + i * charheight);
        }
        for (int i = 0; i < stats2.length; i++) {
            paintStat(g, stats2[i], x + 100, y + i * charheight);
        }
    }

    public void paintStat(Graphics g, String s, int x, int y) {
        paintLabel(g, s + ": " + Game.hero().getStat(s), x, y);
    }

    public static void paintBar(Graphics g, int x, int y, int w, int h, Color f, Color b, float amount) {
        if (amount > 1) amount = 1;
        int hh = h / 4;
        g.setColor(f);
        g.fillRect(x, y, (int) (w * amount), h);
        g.setColor(f.brighter());
        g.fillRect(x, y, (int) (w * amount), hh);
        g.setColor(f.darker());
        g.fillRect(x, y + 3 * hh, (int) (w * amount), h - 3 * hh);
        g.setColor(b);
        g.fillRect(x + (int) (w * amount), y, (int) (w * (1 - amount)), h);
        paintBox(g, x, y, w, h, false);
    }

    public static int paintLabel(Graphics g, String s, int x, int y) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(QuestApplet.INFOTEXTCOLOUR);
        g.drawString(s, x, y + charmaxascent - charheight / 2);
        return charwidth * s.length();
    }

    public static void paintBox(Graphics g, int x, int y, int w, int h, boolean raised) {
        if (raised) g.setColor(QuestApplet.PANELHIGHLIGHT); else g.setColor(QuestApplet.PANELSHADOW);
        g.fillRect(x, y, w, boxborder);
        g.fillRect(x, y, boxborder, h);
        if (!raised) g.setColor(QuestApplet.PANELHIGHLIGHT); else g.setColor(QuestApplet.PANELSHADOW);
        g.fillRect(x + 1, y + h - boxborder, w - 1, boxborder);
        g.fillRect(x + w - boxborder, y + 1, boxborder, h - 1);
    }
}
