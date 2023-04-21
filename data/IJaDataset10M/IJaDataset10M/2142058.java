package dsr.hud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import dsr.AppletMain;
import dsr.SoundEffects;
import dsr.gui.AbstractComponent;
import dsrwebserver.tables.GamesTable;

public class UnitMovementIcons extends AbstractComponent {

    private static Font font_normal;

    private static int SQ_SIZE;

    private static final Color background_col = new Color(1f, 1f, 1f, HUD.TRANSPARENCY);

    private static final long serialVersionUID = 1L;

    public UnitMovementIcons(AppletMain m, HUD hud) {
        super(m, hud, hud.getImageWidth() - (hud.getImageWidth() / 4), hud.getImageHeight() - (hud.getImageWidth() / 4), hud.getImageWidth() / 4, hud.getImageWidth() / 4, true, hud);
        font_normal = HUD.DEF_FONT_NORMAL;
        SQ_SIZE = hud.getImageWidth() / 12;
    }

    public boolean mouseClicked(Point p) {
        int x2 = (p.x - this.x) / SQ_SIZE;
        int y2 = (p.y - this.y) / SQ_SIZE;
        if (main.getCurrentUnit() != null) {
            main.playSound(SoundEffects.CLICK);
            if (x2 == 0 && y2 == 0) {
                main.getCurrentUnit().model.turnBy(-45);
            } else if (x2 == 1 && y2 == 0) {
                main.getCurrentUnit().model.moveFwd();
            } else if (x2 == 2 && y2 == 0) {
                main.getCurrentUnit().model.turnBy(45);
            } else if (x2 == 0 && y2 == 1) {
                main.getCurrentUnit().model.moveStrafeLeft();
            } else if (x2 == 2 && y2 == 1) {
                main.getCurrentUnit().model.moveStrafeRight();
            } else if (x2 == 1 && y2 == 2) {
                main.getCurrentUnit().model.moveBack();
            }
            main.refreshHUD();
            return true;
        }
        return false;
    }

    public void paint(Graphics2D g) {
        try {
            if (main.game_data != null) {
                if (main.game_data.game_status == GamesTable.GS_STARTED) {
                    if (main.getCurrentUnit() != null) {
                        g.setBackground(background_col);
                        g.clearRect(x, y, width - 1, height - 1);
                        g.setFont(font_normal);
                        for (int y2 = 0; y2 < 3; y2++) {
                            for (int x2 = 0; x2 < 3; x2++) {
                                g.setColor(Color.white);
                                g.drawRect(x + (x2 * SQ_SIZE), y + (y2 * SQ_SIZE), SQ_SIZE, SQ_SIZE);
                                g.setColor(Color.LIGHT_GRAY);
                                String[] s = getText(x2, y2);
                                g.drawString(s[0], x + (x2 * SQ_SIZE) + 5, y + (y2 * SQ_SIZE) + 17);
                                g.drawString(s[1], x + (x2 * SQ_SIZE) + 5, y + (y2 * SQ_SIZE) + 32);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String[] getText(int x, int y) {
        if (x == 0 && y == 0) {
            String s[] = { "Turn", "Left" };
            return s;
        } else if (x == 1 && y == 0) {
            String s[] = { "Move", "Fwd" };
            return s;
        } else if (x == 2 && y == 0) {
            String s[] = { "Turn", "Right" };
            return s;
        } else if (x == 0 && y == 1) {
            String s[] = { "Slide", "Left" };
            return s;
        } else if (x == 2 && y == 1) {
            String s[] = { "Slide", "Right" };
            return s;
        } else if (x == 1 && y == 2) {
            String s[] = { "Move", "Back" };
            return s;
        }
        String s[] = { "", "" };
        return s;
    }
}
