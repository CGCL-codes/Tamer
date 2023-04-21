package it.ame.permflow.gui;

import it.ame.permflow.SBCMain;
import it.ame.permflow.IO.Devices;
import it.ame.permflow.lang.LanguageManager;
import it.ame.permflow.util.Logger;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;
import java.io.File;

class MenuHUD implements KeypadListener {

    private String[] menuNames = null;

    private String[][] subMenuNames;

    private String[][] subMenuNamesOptical = null;

    private String[][] subMenuNamesNone = null;

    private int selected = 0;

    private int subSelected = -1;

    MenuHUD() {
    }

    public void downArrow() {
        if (subSelected != -1) {
            if (subSelected < subMenuNames[selected].length - 1) ++subSelected;
        } else if (selected < menuNames.length - 1 && subSelected == -1) ++selected;
    }

    public void upArrow() {
        if (subSelected != -1 && subSelected > 0) --subSelected; else if (selected > 0 && subSelected == -1) --selected;
    }

    public void rightArrow() {
        if (selected == 1 && HUD.settings.isOpticalSensorEnabled() && (SBCMain.profiles.curFactory == null || SBCMain.profiles.curFelt == null || SBCMain.profiles.curFelt.getThreshold() == -1)) {
            HUD.alert(LanguageManager.getString("Menu10"), HUD.MESSAGE_WARNING);
            return;
        }
        if ((selected == 0 || selected == 1 || selected == 2) && subSelected == -1) subSelected = 0;
    }

    public void leftArrow() {
        if (subSelected != -1) subSelected = -1;
    }

    public void ok() {
        if (selected == 0) {
            if (subSelected == -1) subSelected = 0; else if (subSelected == 1) {
                HUD.profiles.whichView = ProfilesHUD.CREATE;
                HUD.profiles.subView = ProfilesHUD.PAPER;
                HUD.curComponent = HUD.profiles;
            } else if (subSelected == 2) {
                if (SBCMain.profiles.size() == 0) {
                    HUD.alert(LanguageManager.getString("Menu9"), HUD.MESSAGE_WARNING);
                    return;
                }
                HUD.profiles.whichView = ProfilesHUD.CREATE;
                HUD.profiles.subView = ProfilesHUD.FELT;
                HUD.curComponent = HUD.profiles;
            } else if (subSelected == 3) {
                if (SBCMain.profiles.curFelt == null || SBCMain.profiles.curFactory == null) {
                    HUD.alert(LanguageManager.getString("Menu12"), HUD.MESSAGE_WARNING);
                    return;
                }
                HUD.opticalCalibration.isInCalibration = true;
                HUD.curComponent = HUD.opticalCalibration;
            } else if (subSelected == 0) {
                if (SBCMain.profiles.size() == 0) {
                    HUD.alert(LanguageManager.getString("Menu9"), HUD.MESSAGE_WARNING);
                    return;
                }
                HUD.profiles.whichView = ProfilesHUD.MANAGE;
                HUD.curComponent = HUD.profiles;
            }
            init();
        } else if (selected == 1) {
            if (HUD.settings.isOpticalSensorEnabled() && (SBCMain.profiles.curFactory == null || SBCMain.profiles.curFelt == null || SBCMain.profiles.curFelt.getThreshold() == -1)) {
                HUD.alert(LanguageManager.getString("Menu10"), HUD.MESSAGE_WARNING);
                return;
            }
            if (subSelected == -1) {
                subSelected = 0;
                return;
            } else if (subSelected == 0) {
                HUD.graph.type = GraphHUD.HUM_GRAPH;
                SBCMain.devices.sendToDevice(new byte[] { '>', 'H', '1', 0x0D, 0x0A }, Devices.KEYPAD_DEVICE);
            } else if (subSelected == 1) {
                HUD.graph.type = GraphHUD.PERM_GRAPH;
                SBCMain.devices.sendToDevice(new byte[] { '>', 'P', '1', 0x0D, 0x0A }, Devices.KEYPAD_DEVICE);
            } else if (subSelected == 2) {
                {
                    HUD.graph.type = GraphHUD.BOTH_GRAPHS;
                    SBCMain.devices.sendToDevice(new byte[] { '>', 'H', '1', 0x0D, 0x0A }, Devices.KEYPAD_DEVICE);
                    SBCMain.devices.sendToDevice(new byte[] { '>', 'P', '1', 0x0D, 0x0A }, Devices.KEYPAD_DEVICE);
                    HUD.frame.repaint();
                }
            }
            HUD.curComponent = HUD.graph;
            init();
        } else if (selected == 3) {
            HUD.curComponent = HUD.settings;
            init();
        } else if (selected == 2) {
            if (subSelected == -1) subSelected = 0; else if (subSelected == 0) {
                HUD.tables.mode = TablesHUD.MANAGE;
                HUD.curComponent = HUD.tables;
                init();
            } else if (subSelected == 1) {
                HUD.tables.mode = TablesHUD.CREATE;
                HUD.curComponent = HUD.tables;
                init();
            }
        }
    }

    public void cancel() {
        try {
            {
                SBCMain.profiles.save();
                it.ame.permflow.core.Report.save();
                SBCMain.tables.save();
                SBCMain.saveSelections();
                HUD.settings.save();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.reportException(e);
        }
    }

    public void measure() {
    }

    public void init() {
    }

    public void draw(Graphics2D g2) {
        menuNames = new String[] { LanguageManager.getString("Menu0") + "...", LanguageManager.getString("Menu1") + "...", LanguageManager.getString("Tables10") + "...", LanguageManager.getString("Settings0") + "..." };
        subMenuNamesOptical = new String[][] { { LanguageManager.getString("Menu0"), LanguageManager.getString("Menu2"), LanguageManager.getString("Menu3"), LanguageManager.getString("Menu11") }, { LanguageManager.getString("Menu4"), LanguageManager.getString("Menu5"), LanguageManager.getString("Menu6") }, { LanguageManager.getString("Menu7"), LanguageManager.getString("Menu8") }, {} };
        subMenuNamesNone = new String[][] { { LanguageManager.getString("Menu0"), LanguageManager.getString("Menu2"), LanguageManager.getString("Menu3") }, { LanguageManager.getString("Menu4"), LanguageManager.getString("Menu5"), LanguageManager.getString("Menu6") }, { LanguageManager.getString("Menu7"), LanguageManager.getString("Menu8") }, {} };
        if (HUD.settings.isOpticalSensorEnabled()) subMenuNames = subMenuNamesOptical; else subMenuNames = subMenuNamesNone;
        int OFFSET_Y = 150;
        int OFFSET_X = 130;
        g2.setStroke(new BasicStroke(10.0f));
        for (int i = 0; i < menuNames.length; ++i) {
            if (i == selected) drawButton(g2, OFFSET_X, OFFSET_Y + i * 80, 16, menuNames[i], true, Color.BLACK); else drawButton(g2, OFFSET_X, OFFSET_Y + i * 80, 16, menuNames[i], false, (subSelected != -1) ? (Color.GRAY) : (Color.BLACK));
        }
        if (subSelected != -1) {
            for (int i = 0; i < subMenuNames[selected].length; ++i) {
                drawButton(g2, OFFSET_X + 260, OFFSET_Y + i * 80, 16, subMenuNames[selected][i], (i == subSelected) ? (true) : (false), Color.BLACK);
            }
        }
        g2.setColor(Color.RED);
        g2.fillRoundRect(100, 55, 600, 50, 20, 20);
        g2.setColor(Color.BLACK);
        g2.drawString(LanguageManager.getString("Beta"), 400 - g2.getFontMetrics().stringWidth(LanguageManager.getString("Beta")) / 2, 85);
        if (!SBCMain.DEBUG_MODE) {
            g2.setColor(Color.BLACK);
            g2.setFont(HUD.smallFont);
            g2.drawString(LanguageManager.getString("Menu13") + ": " + getFreeSpaceString(), 420, 550);
            g2.drawString(LanguageManager.getString("Menu14") + ": " + getFreeSpaceTime(), 400, 570);
            g2.setFont(HUD.boldFont);
        }
    }

    void drawButton(Graphics2D g2, int x, int y, int length, String label, boolean selected, Color textColor) {
        g2.drawImage(HUD.images[0], x, y, null);
        for (int i = 0; i < length - 1; ++i) g2.drawImage(HUD.images[1], x + (i + 1) * 14, y, null);
        g2.drawImage(HUD.images[2], x + length * 14, y, null);
        g2.setColor(textColor);
        g2.setFont(HUD.boldFont);
        g2.drawString(label, x + 5 + length * 7 - g2.getFontMetrics().stringWidth(label) / 2, y + 32);
        if (selected) {
            g2.setColor(Color.RED);
            g2.draw(new RoundRectangle2D.Double(x, y, 14 * (length + 1), 5 + 47, 10, 10));
            g2.setColor(Color.BLACK);
        }
    }

    public void f1() {
    }

    public void f2() {
    }

    private long getFreeSpace() {
        File[] roots = File.listRoots();
        return roots[0].getFreeSpace();
    }

    private String getFreeSpaceString() {
        String[] unit = { "bytes", "Kbytes", "Mbytes", "Gbytes" };
        long freeSpace = getFreeSpace();
        int count = 0;
        String ret = freeSpace + " " + unit[count];
        while (freeSpace > 1024) {
            count++;
            freeSpace = freeSpace / 1024;
            ret = freeSpace + " " + unit[count];
        }
        return ret;
    }

    private String getFreeSpaceTime() {
        int CAMPIONAMENTO_HUM = 1024;
        int CAMPIONAMENTO_PERM = 256;
        int BYTES_PER_CAMP = 15;
        long BYTES_PER_SECOND = BYTES_PER_CAMP * (CAMPIONAMENTO_PERM + CAMPIONAMENTO_HUM);
        long freeSpace = getFreeSpace();
        long seconds = freeSpace / BYTES_PER_SECOND;
        int hours = (int) seconds / 3600;
        int minutes = (int) (seconds / 60) % 60;
        seconds = (int) seconds % 60;
        return formatString(hours, minutes, (int) seconds);
    }

    private String formatString(int hours, int minutes, int seconds) {
        String str = new String();
        if (hours < 10) str = "0";
        str += hours + ":";
        if (minutes < 10) str += "0";
        str += minutes + ":";
        if (seconds < 10) str += "0";
        str += seconds;
        return str;
    }
}
