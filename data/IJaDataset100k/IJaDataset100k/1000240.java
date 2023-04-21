package net.sf.evemsp.gui.chr;

import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import net.sf.evemsp.SkillPlanner;
import net.sf.evemsp.data.ChrRecord;
import net.sf.evemsp.data.ChrSkill;
import net.sf.evemsp.data.ChrSkillGroup;
import net.sf.evemsp.gui.EveCanvas;
import net.sf.evemsp.gui.EvePage;
import net.sf.evemsp.util.Formatter;

public class ChrSkillListPage implements EvePage {

    private final ChrRecord record;

    private Vector entries = new Vector();

    private int selected = 0;

    private int startY = 0;

    private int maxEntries = 0;

    private int entryHeight = 0;

    private int[] transRBG;

    private EveCanvas canvas = SkillPlanner.getCanvas();

    private final class SgEntry {

        ChrSkillGroup grp;

        boolean expanded = false;

        boolean training = false;
    }

    public ChrSkillListPage(ChrRecord record) {
        this.record = record;
        for (Enumeration e = record.getGroups().elements(); e.hasMoreElements(); ) {
            ChrSkillGroup grp = (ChrSkillGroup) e.nextElement();
            SgEntry entry = new SgEntry();
            entry.grp = grp;
            if (record.getTraining() != null) {
                for (Enumeration s = grp.getSkills().elements(); s.hasMoreElements(); ) {
                    ChrSkill skl = (ChrSkill) s.nextElement();
                    if (skl.getSkill() == record.getTraining()) {
                        entry.training = true;
                    }
                }
            }
            entries.addElement(entry);
        }
    }

    public void keyPressed(int keyCode) {
        String keyName = canvas.getKeyName(keyCode);
        if (SkillPlanner.getBackName().equals(keyName)) {
            dispose();
            return;
        }
        int gameCode = canvas.getGameAction(keyCode);
        switch(gameCode) {
            case Canvas.FIRE:
                fire();
                break;
            case Canvas.RIGHT:
                break;
            case Canvas.LEFT:
                dispose();
                break;
            case Canvas.UP:
                selPrev();
                break;
            case Canvas.DOWN:
                selNext();
                break;
        }
    }

    private void fire() {
        Object entry = entries.elementAt(selected);
        if (entry instanceof ChrSkill) {
            ChrSkillPage skillPage = new ChrSkillPage(this, record, (ChrSkill) entry);
            canvas.setOverride(skillPage);
        } else {
            SgEntry sg = (SgEntry) entry;
            if (sg.expanded) {
                int sc = sg.grp.getSkills().size();
                while (sc > 0) {
                    entries.removeElementAt(selected + 1);
                    sc--;
                }
            } else {
                Vector vs = sg.grp.getSkills();
                for (int i = 0; i < vs.size(); i++) {
                    ChrSkill skl = (ChrSkill) vs.elementAt(i);
                    entries.insertElementAt(skl, selected + 1 + i);
                }
            }
            sg.expanded = !sg.expanded;
        }
    }

    public void paint(Graphics g) {
        Font f = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        g.setFont(f);
        int w = canvas.getWidth();
        String title = record.getName();
        int sWidth = f.stringWidth(title);
        int x = (w - sWidth) / 2;
        int y = 1 + f.getHeight();
        g.setColor(0x000000);
        g.drawString(title, x + 1, y + 2, Graphics.BOTTOM | Graphics.LEFT);
        g.setColor(0xFFFFFF);
        g.drawString(title, x, y, Graphics.BOTTOM | Graphics.LEFT);
        y += 1 + f.getHeight();
        f = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        g.setFont(f);
        g.drawString("Skills", w >> 1, y, Graphics.BOTTOM | Graphics.HCENTER);
        f = Font.getFont(Font.FACE_PROPORTIONAL, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        g.setFont(f);
        int eH = entryHeight = f.getHeight() * 2;
        int eW = canvas.getWidth();
        int eX = 0;
        int eY = startY = y;
        int remaining = canvas.getHeight() - (y + f.getHeight());
        maxEntries = Math.min(entries.size(), remaining / eH);
        int transLen = eH * eW;
        if (transRBG == null || transRBG.length != transLen) {
            transRBG = new int[transLen];
            for (int i = transLen; --i >= 0; ) {
                transRBG[i] = 0x55007070;
            }
        }
        int start = selected - (maxEntries >> 1);
        if (start < 0) {
            start = 0;
        } else {
            if (start + maxEntries >= entries.size()) {
                start = entries.size() - maxEntries;
            }
        }
        for (int i = start; i < start + maxEntries; i++) {
            Object entry = entries.elementAt(i);
            if (entry instanceof ChrSkill) {
                paintEntry(g, (ChrSkill) entry, eX, eY, eW, eH, selected == i);
            } else {
                paintEntry(g, (SgEntry) entry, eX, eY, eW, eH, selected == i);
            }
            eY += eH;
        }
    }

    void paintEntry(Graphics g, SgEntry grp, int x, int y, int w, int h, boolean selected) {
        int oldC = g.getColor();
        if (selected) {
            g.drawRGB(transRBG, 0, w, x, y, w, h, true);
        }
        if (grp.training) {
            long now = new Date().getTime();
            if (now >= record.getTrainingCompletes().getTime()) {
                g.setColor(0xFF0000);
            } else {
                g.setColor(0x00FF00);
            }
        }
        g.drawString(grp.grp.getName(), x, y, Graphics.LEFT | Graphics.TOP);
        Vector vs = grp.grp.getSkills();
        g.drawString(vs.size() + " Skill" + (vs.size() > 1 ? "s" : "") + ", " + Formatter.format(grp.grp.getSp()) + " SP", x, y + h, Graphics.LEFT | Graphics.BOTTOM);
        arrow(g, w - 14, y + h - 14, grp.expanded);
        g.setColor(oldC);
    }

    void arrow(Graphics g, int x, int y, boolean up) {
        g.drawRect(x, y, 11, 11);
        g.drawLine(x + 5, y + 3, x + 5, y + 8);
        g.drawLine(x + 6, y + 2, x + 6, y + 9);
        g.drawLine(x + 7, y + 3, x + 7, y + 8);
        if (up) {
            g.drawLine(x + 5, y + 2, x + 2, y + 5);
            g.drawLine(x + 7, y + 2, x + 10, y + 5);
        } else {
            g.drawLine(x + 5, y + 9, x + 2, y + 6);
            g.drawLine(x + 7, y + 9, x + 10, y + 6);
        }
    }

    void paintEntry(Graphics g, ChrSkill skl, int x, int y, int w, int h, boolean selected) {
        if (selected) {
            g.drawRGB(transRBG, 0, w, x, y, w, h, true);
        }
        int fg = g.getColor();
        boolean training = record.getTraining() == skl.getSkill();
        boolean completed = false;
        if (training) {
            long now = new Date().getTime();
            completed = now >= record.getTrainingCompletes().getTime();
            if (completed) {
                g.setColor(0xFF0000);
            } else {
                g.setColor(0x00FF00);
            }
        }
        g.drawString(skl.getSkill().getName(), x, y, Graphics.LEFT | Graphics.TOP);
        g.drawString(Formatter.format(skl.getSp()) + " SP", x, y + h, Graphics.LEFT | Graphics.BOTTOM);
        g.setColor(fg);
        int lvl = skl.getLevel();
        int R = g.getFont().getHeight() >> 1;
        for (int i = 1; i <= 5; i++) {
            int xR = w - (R * 6) - 2 + (R * i);
            int yR = y + h - R - 2;
            g.drawRect(xR, yR, R, R);
            if (i <= lvl) {
                g.setColor(0xA0A0A0);
                g.fillRect(xR + 1, yR + 1, R - 1, R - 1);
                g.setColor(fg);
            }
            if (training && i == 1 + lvl && canvas.getOverlay().isBlink()) {
                g.setColor(completed ? 0xFF0000 : 0x00FF00);
                g.fillRect(xR + 1, yR + 1, R - 1, R - 1);
                g.setColor(fg);
            }
        }
    }

    public void pointerPressed(int x, int y) {
        if (y >= startY) {
            int s = y - startY;
            s /= entryHeight;
            if (s < maxEntries) {
                int start = selected - (maxEntries >> 1);
                if (start < 0) {
                    start = 0;
                } else {
                    if (start + maxEntries >= entries.size()) {
                        start = entries.size() - maxEntries;
                    }
                }
                s += start;
                if (selected != s) {
                    selected = s;
                } else {
                    fire();
                }
            }
        } else {
            dispose();
        }
    }

    public void dispose() {
        canvas.setOverride(null);
        canvas.repaint();
    }

    private void selNext() {
        selected++;
        if (selected >= entries.size()) {
            selected = 0;
        }
    }

    private void selPrev() {
        selected--;
        if (selected < 0) {
            selected = entries.size() - 1;
        }
    }
}
